package io.github.purpleloop.gameengine.sound;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A sound player.
 * 
 * Sounds can be put in a cache if their size is below the cache limit.
 * If not, they are played directly.
 */
public class SoundPlayer {

	/** The class logger. */
	private static Log log = LogFactory.getLog(SoundPlayer.class);

	/** Size of the data buffer for the playback (in number of frames). */
	static final int SOUND_PLAYBACK_BUFFER_FRAME_COUNT = 20000;

	/** Maximal size of a sound that can be set in soundDataCacheForFile (256ko). */
	private static final int CACHE_SIZE_LIMIT = 256 * 1024;

	/**
	 * Cache of the audio data for files, used when the size of data is below the
	 * CACHE_SIZE_LIMIT.
	 * 
	 * Each audio chunk is stored by it's name in the map.
	 * 
	 */
	private Map<String, AudioChunk> soundDataCacheForSoundName;

	/**
	 * Builds a sound player.
	 */
	public SoundPlayer() {

		// Prepares the cache of sound data associated with the player
		soundDataCacheForSoundName = new HashMap<>();
	}

	/**
	 * Plays the sound corresponding to a WAV audio file.
	 * 
	 * @param soundFileName The sound file to play
	 */
	public void play(String soundFileName) {

		// log.debug("Playing "+soundFileName.getName());
		if (soundDataCacheForSoundName.containsKey(soundFileName)) {

			// The sound is in cache
			// log.debug("Reuse Cached sound "+soundFileName);
			AudioChunk audioChunk = soundDataCacheForSoundName.get(soundFileName);
			playWithCache(audioChunk);

		} else {

			File soundFile = new File(soundFileName);
			if (!soundFile.exists()) {
				log.error("Error while playing the audio. The file '" + soundFile.getAbsolutePath() + "' is missing.");
				return;
			}

			long length = soundFile.length();

			if (length > CACHE_SIZE_LIMIT) {
				directPlay(soundFile, length);

			} else {
				// Puts the sound in cache
				AudioChunk audioChunk = new AudioChunk(soundFile, length);
				soundDataCacheForSoundName.put(soundFileName, audioChunk);
				playWithCache(audioChunk);
			}

		}

	}

	/**
	 * Plays a sound, directly (use for larger audio data).
	 * 
	 * @param soundFile the sound file
	 * @param length    length of the data in bytes
	 */
	private void directPlay(File soundFile, long length) {

		try {

			AudioFileFormat audioFileFormat = AudioSystem.getAudioFileFormat(soundFile);
			AudioFormat audioFormat = audioFileFormat.getFormat();

			// Creates a descriptor able to manage the audio format
			Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);

			try (
					// Opens the sound file for reading
					FileInputStream wavFileInputStreamflux = new FileInputStream(soundFile);

					// Transforms the input stream in an audio input stream whose format
					// is specified by the audioFormat
					AudioInputStream audioSamplesInputStream = new AudioInputStream(wavFileInputStreamflux, audioFormat,
							length / audioFormat.getFrameSize());

			) {
				play(audioSamplesInputStream, audioFormat.getFrameSize(), dataLineInfo);
			}

		} catch (FileNotFoundException e) {
			log.error("The audio file to play cannot be found  " + soundFile.getAbsoluteFile(), e);
		} catch (IOException e) {
			log.error("An I/O error while reading the audio file " + soundFile.getAbsoluteFile() + " (close)", e);
		} catch (UnsupportedAudioFileException e) {
			log.error("The audio format is not supported for the file " + soundFile.getAbsolutePath(), e);
		}
	}

	/**
	 * Plays a sound, using the audio cache.
	 * 
	 * @param audioChunk the audio chunk to play
	 */
	private void playWithCache(AudioChunk audioChunk) {

		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(audioChunk.getData());
			play(bais, audioChunk.getFrameSize(), audioChunk.getDataLineInfo());
			bais.close();
		} catch (IOException e) {
			log.error("An I/O error while playing the audio chunk (close): ", e);
		}
	}

	/**
	 * Produces the sound obtained from a stream containing audio data.
	 * 
	 * @param is           the stream containing the audio data
	 * @param frameSize    size of a single frame of audio data
	 * @param dataLineInfo informations about the audio data line
	 */
	public synchronized void play(InputStream is, int frameSize, Line.Info dataLineInfo) {

		byte[] buffer = new byte[SOUND_PLAYBACK_BUFFER_FRAME_COUNT * frameSize];
		int numBytesRead;

		try (
				// Gets an audio data line corresponding to the descriptor
				SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);) {

			// Opens and starts the audio line
			sourceDataLine.open();
			sourceDataLine.start();

			try {
				// Loops on the reading of a buffer and the writing of it's data in the audio
				// line
				while ((numBytesRead = is.read(buffer, 0, buffer.length)) != -1) {
					if (numBytesRead > 0) {
						sourceDataLine.write(buffer, 0, numBytesRead);
					}

				}

			} catch (IOException e) {
				log.error("play - I/O error while playing the sound", e);
			}

			// Cleans the source data line
			sourceDataLine.drain();

		} catch (LineUnavailableException e) {
			log.error("play - The audio line is unavailable to play the sound : ", e);
		}

	}

}
