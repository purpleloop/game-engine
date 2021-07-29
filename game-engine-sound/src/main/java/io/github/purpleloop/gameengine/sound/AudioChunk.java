package io.github.purpleloop.gameengine.sound;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class stores informations to play a short sound from a memory cache.
 */
public class AudioChunk {

	/** Class logger. */
	private static Log log = LogFactory.getLog(AudioChunk.class);

	/** The audio data cache. */
	private byte[] data = null;

	/** Size of the sample (frame) in bytes. */
	private int frameSize;

	/** Kind of audio dataline required to play the sound sample. */
	private Line.Info dataLineInfo = null;

	/**
	 * Reads the audio chunk from a file.
	 * 
	 * @param soundFile the audio file to load
	 * @param fileSize  size of the file (to prevent an extra IO access)
	 */
	public AudioChunk(File soundFile, long fileSize) {

		log.debug("Storing sound in cache " + soundFile);

		// Allocates the audio data cache of the same size of the file
		data = new byte[(int) fileSize];

		try {

			AudioFileFormat audioFileFormat = AudioSystem.getAudioFileFormat(soundFile);
			AudioFormat audioFormat = audioFileFormat.getFormat();
			this.frameSize = audioFormat.getFrameSize();
			log.debug("The audioFormat of the chunk is " + audioFormat.toString());

			// Creates a descriptor able to manage the audio format
			dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);

			long numberOfFrames = fileSize / audioFormat.getFrameSize();

			try (
					// Opens the file for reading
					FileInputStream fileInputStream = new FileInputStream(soundFile);

					// Transforms the audio data input stream into an audio
					// stream compatible with the audioFormat.
					AudioInputStream audioInputStream = new AudioInputStream(fileInputStream, audioFormat, numberOfFrames);) {

				int currentOffset = 0;
				int bufferSizeInBytes = SoundPlayer.SOUND_PLAYBACK_BUFFER_FRAME_COUNT * frameSize;
				int numBytesRead;
				while ((numBytesRead = audioInputStream.read(data, currentOffset, bufferSizeInBytes)) != -1) {
					currentOffset = currentOffset + numBytesRead;
				}
			}

		} catch (FileNotFoundException e) {
			log.error("The sound file is missing " + soundFile.getAbsoluteFile(), e);
		} catch (IOException e) {
			log.error("An I/O error occured while reading the audio file " + soundFile.getAbsoluteFile() + " (close)",
					e);
		} catch (UnsupportedAudioFileException e) {
			log.error("Unsupported audio format audio found while reading the file " + soundFile.getAbsolutePath(), e);
		}
	}

	/** @return the audio data of the chunk */
	public byte[] getData() {
		return data;
	}

	/**
	 * @return informations on the audio line required to play the chunk
	 */
	public Line.Info getDataLineInfo() {
		return dataLineInfo;
	}

	/** @return the size of a frame in the audio data */
	public int getFrameSize() {
		return frameSize;
	}

}
