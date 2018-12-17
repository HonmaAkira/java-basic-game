package ゲーム;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Wave
 * <pre>
 * Wave演奏を制御します
 * </pre>
 */
public class KSoundWave {

	private Clip clip;

	/**
	 * コンストラクタ
	 * <pre>
	 * Waveオブジェクトを生成します
	 * </pre>
	 * @param obj パスを決めるオブジェクト
	 * @param fileName ファイル名
	 * @param flgLoop true：繰り返す false：繰り返さない
	 */
	public KSoundWave(Object obj, String fileName, boolean flgLoop){

		try{
			if(obj == null){
				obj = this;
			}
			InputStream is = obj.getClass().getResourceAsStream(fileName);
			AudioInputStream sound = AudioSystem.getAudioInputStream(is);
			AudioFormat format = sound.getFormat();
			DataLine.Info di = new DataLine.Info(Clip.class, format);
			this.clip = (Clip) AudioSystem.getLine(di);
			clip.open(sound);

		}catch(UnsupportedAudioFileException ex){
			ex.printStackTrace();
			return;
		}catch(IOException ex){
			ex.printStackTrace();
			return;
		}catch(LineUnavailableException ex){
			ex.printStackTrace();
			return;
		}

	} // end KSoundWave

	/**
	 * 音源スタート
	 */
	public void start(){
		clip.setFramePosition(0);
		clip.start();
	}

	/**
	 * 音源ストップ
	 */
	public void stop(){
		clip.stop();
	}

}
