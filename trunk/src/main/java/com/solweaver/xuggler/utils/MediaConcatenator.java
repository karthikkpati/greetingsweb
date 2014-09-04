package com.solweaver.xuggler.utils;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.AudioSamplesEvent;
import com.xuggle.mediatool.event.IAddStreamEvent;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.ICloseCoderEvent;
import com.xuggle.mediatool.event.ICloseEvent;
import com.xuggle.mediatool.event.IOpenCoderEvent;
import com.xuggle.mediatool.event.IOpenEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.mediatool.event.VideoPictureEvent;
import com.xuggle.xuggler.IAudioSamples;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.BgrConverter;

public class MediaConcatenator extends MediaToolAdapter
{
  // the current offset
 
  public static long mOffset;
 
  // the next video timestamp
 
  public static long mNextVideo;
 
  // the next audio timestamp
 
  public static long mNextAudio;

  // the index of the audio stream
 
  private final int mAudoStreamIndex;
 
  // the index of the video stream
 
  private final int mVideoStreamIndex;
  
  private Double mAngleOfRotation;
 
  /**
   * Create a concatenator.
   *
   * @param audioStreamIndex index of audio stream
   * @param videoStreamIndex index of video stream
   */
 
  public MediaConcatenator(int audioStreamIndex, int videoStreamIndex, Double angleOfRotation)
  {
    mAudoStreamIndex = audioStreamIndex;
    mVideoStreamIndex = videoStreamIndex;
    mAngleOfRotation = angleOfRotation;
  }
 
  public void onAudioSamples(IAudioSamplesEvent event)
  {
    IAudioSamples samples = event.getAudioSamples();
   
    // set the new time stamp to the original plus the offset established
    // for this media file

    long newTimeStamp = samples.getTimeStamp() + mOffset;

    // keep track of predicted time of the next audio samples, if the end
    // of the media file is encountered, then the offset will be adjusted
    // to this time.

    mNextAudio = samples.getNextPts();

    // set the new timestamp on audio samples

    samples.setTimeStamp(newTimeStamp);

    // create a new audio samples event with the one true audio stream
    // index

    super.onAudioSamples(new AudioSamplesEvent(this, samples,
      mAudoStreamIndex));
  }

  public void onVideoPicture(IVideoPictureEvent event)
  {
	  BufferedImage inputImage =  event.getImage();
		 
	  IVideoPicture picture = event.getMediaData();
	  /*IVideoPicture picture = event.getMediaData();*/
	    
	    long originalTimeStamp = picture.getTimeStamp();

	    // set the new time stamp to the original plus the offset established
	    // for this media file

	    long newTimeStamp = originalTimeStamp + mOffset;
	    // keep track of predicted time of the next video picture, if the end
	    // of the media file is encountered, then the offset will be adjusted
	    // to this this time.
	    //
	    // You'll note in the audio samples listener above we used
	    // a method called getNextPts().  Video pictures don't have
	    // a similar method because frame-rates can be variable, so
	    // we don't now.  The minimum thing we do know though (since
	    // all media containers require media to have monotonically
	    // increasing time stamps), is that the next video timestamp
	    // should be at least one tick ahead.  So, we fake it.
	   
	    mNextVideo = newTimeStamp + 1;

	    // set the new timestamp on video samples

	    picture.setTimeStamp(newTimeStamp);
	 
	  IVideoPictureEvent asc = null;
	  BufferedImage rotatedImage = null;
	  if(mAngleOfRotation != null && inputImage != null){
		rotatedImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), inputImage.getType());
		Graphics2D g2d = rotatedImage.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.rotate(Math.toRadians(mAngleOfRotation), inputImage.getWidth()/2, inputImage.getHeight()/2);
		g2d.drawImage(inputImage, null, 0,0);
		
		BgrConverter bgrConverter = new BgrConverter(picture.getPixelType(), picture.getWidth(), picture.getHeight(), rotatedImage.getWidth(), rotatedImage.getHeight());
		  
		IVideoPicture out = bgrConverter.toPicture(rotatedImage, newTimeStamp);
		asc = new VideoPictureEvent(this, out, mVideoStreamIndex);
	}else{
		asc = new VideoPictureEvent(this, picture,
			      mVideoStreamIndex);
	}
	  
	  
    // create a new video picture event with the one true video stream
    // index

    super.onVideoPicture(asc);
  }
 
  public void onClose(ICloseEvent event)
  {
    // update the offset by the larger of the next expected audio or video
    // frame time

    mOffset = Math.max(mNextVideo, mNextAudio);

    if (mNextAudio < mNextVideo)
    {
      // In this case we know that there is more video in the
      // last file that we read than audio. Technically you
      // should pad the audio in the output file with enough
      // samples to fill that gap, as many media players (e.g.
      // Quicktime, Microsoft Media Player, MPlayer) actually
      // ignore audio time stamps and just play audio sequentially.
      // If you don't pad, in those players it may look like
      // audio and video is getting out of sync.

      // However kiddies, this is demo code, so that code
      // is left as an exercise for the readers. As a hint,
      // see the IAudioSamples.defaultPtsToSamples(...) methods.
    }
  }

  public void onAddStream(IAddStreamEvent event)
  {
    // overridden to ensure that add stream events are not passed down
    // the tool chain to the writer, which could cause problems
  }

  public void onOpen(IOpenEvent event)
  {
    // overridden to ensure that open events are not passed down the tool
    // chain to the writer, which could cause problems
  }

  public void onOpenCoder(IOpenCoderEvent event)
  {
    // overridden to ensure that open coder events are not passed down the
    // tool chain to the writer, which could cause problems
  }

  public void onCloseCoder(ICloseCoderEvent event)
  {
    // overridden to ensure that close coder events are not passed down the
    // tool chain to the writer, which could cause problems
  }
}