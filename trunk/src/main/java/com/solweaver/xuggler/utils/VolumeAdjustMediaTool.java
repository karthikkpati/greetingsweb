package com.solweaver.xuggler.utils;

import java.nio.ShortBuffer;

import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.event.IAudioSamplesEvent;

public class VolumeAdjustMediaTool extends MediaToolAdapter{

	// the amount to adjust the volume by
	private double mVolume;

	public VolumeAdjustMediaTool(double volume) {

		mVolume = volume;

	}

	@Override
	public void onAudioSamples(IAudioSamplesEvent event) {

		// get the raw audio bytes and adjust it's value

		ShortBuffer buffer = event.getAudioSamples().getByteBuffer().asShortBuffer();

		for (int i = 0; i < buffer.limit(); ++i) {

			buffer.put(i, (short) (buffer.get(i) * mVolume));

		}

		// call parent which will pass the audio onto next tool in chain

		super.onAudioSamples(event);

	}
}
