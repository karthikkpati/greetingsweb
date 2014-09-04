package com.solweaver.xuggler.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.mediatool.event.VideoPictureEvent;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.BgrConverter;

public class StaticImageMediaTool extends MediaToolAdapter{


	BufferedImage overlay = null;
	
	private int embeddedImageMinWidth;

	private int embeddedImageMaxWidth;
	
	private int embeddedImageMinHeight;
	
	private int embeddedImageMaxHeight;
	

	public StaticImageMediaTool(String overLayImageFile, int embeddedImageMinWidth, int embeddedImageMaxWidth,
			int embeddedImageMinHeight, int embeddedImageMaxHeight) {

		try {

			overlay = ImageIO.read(new File(overLayImageFile));
			this.embeddedImageMinWidth = embeddedImageMinWidth;
			this.embeddedImageMinHeight = embeddedImageMinHeight;
			this.embeddedImageMaxHeight = embeddedImageMaxHeight;
			this.embeddedImageMaxWidth = embeddedImageMaxWidth;

		}catch (IOException e) {

			e.printStackTrace();

			throw new RuntimeException("Could not open file");

		}

	}

	
	@Override
	public void onVideoPicture(IVideoPictureEvent event) {

		IVideoPicture pic = event.getPicture();
		
		BufferedImage outputImage = combineImages(event.getImage());
		
		BgrConverter bgrConverter = new BgrConverter(pic.getPixelType(), pic.getWidth(), pic.getHeight(), outputImage.getWidth(), outputImage.getHeight());
 
		IVideoPicture out = bgrConverter.toPicture(outputImage, pic.getTimeStamp());
		
		IVideoPictureEvent asc = new VideoPictureEvent(event.getSource(), out, event.getStreamIndex());
		super.onVideoPicture(asc);
		out.delete();

		// call parent which will pass the video onto next tool in chain

		//super.onVideoPicture(event);

	}

	
	private BufferedImage combineImages(BufferedImage inputImage){
	
		/*try {
			//overlay = ImageIO.read(new File("f:/karthik/t1.jpg")); 
			overlay = ImageIO.read(new File("c:/karthik/junk/grite/Birthday.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		// create the new image, canvas size is the max. of both image sizes
		BufferedImage combined = new BufferedImage(overlay.getWidth(), overlay.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		// paint both images, preserving the alpha channels
		Graphics g = combined.getGraphics();
		
		g.drawImage(overlay, 0, 0, null);
		//g.drawImage(newImage, 538, 191, 900, 550, null);
		//g.drawImage(inputImage, 50, 160, embeddedImageWidth, embeddedImageHeight, null);
		g.drawImage(inputImage, embeddedImageMinWidth, embeddedImageMinHeight, embeddedImageMaxWidth, embeddedImageMaxHeight, null);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Ariel", Font.ITALIC, 30));
		g.drawString("Happy BDay Solweaver ", 550, 250);
		return combined;
	}


}
