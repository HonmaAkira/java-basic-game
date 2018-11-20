package シューティング;

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
 * Wave演奏を制御する
 * </pre>
 */
public class KSoundWave {

	private Clip clip;

	/**
	 * コンストラクタ
	 * <pre>
	 * Waveオブジェクトを生成する
	 * </pre>
	 * @param obj パスを決めるオブジェクト
	 * @param fileName ファイル名
	 * @param flgLoop true:繰り返す false:繰り返さない**/


	public KSoundWave(Object obj, String fileName, boolean flgLoop) {

		try {
			if (obj == null) {
				obj = this;
			}
			//プロジェクトからファイルを引っ張ってくる
			InputStream is = obj.getClass().getResourceAsStream(fileName);
			//AudioSystemクラスのgetAudioInputStream(1)の引数に指定された入力ストリームから
			//オーディオ入力ストリームを取得します
			//getAudioInputStream()メソッドで取得したオーディオ入力ストリームを
			//AudioInputStreamクラス型の変数soundに格納し、使えるようにする
			AudioInputStream sound = AudioSystem.getAudioInputStream(is);
			//getFormat()メソッドで取得したサウンドデータをAudioFormat型のクラス変数formatへ格納する
			AudioFormat format = sound.getFormat();
			//短い音声出力Clipを使用するときの決まり文句である。覚える事。
			DataLine.Info di = new DataLine.Info(Clip.class, format);
//			DataLine型をClip型にキャストして型変更し使用している
			this.clip = (Clip) AudioSystem.getLine(di);
//			Clipを使用して音声データを流す
			clip.open(sound);

		} catch (UnsupportedAudioFileException ex) {
			ex.printStackTrace();
			return;
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		} catch (LineUnavailableException ex) {
			ex.printStackTrace();
			return;
		}

	} // end KSoundWave

	//演奏スタート
	public void start() {
		clip.setFramePosition(0);
		clip.start();
	}


	 //演奏ストップ
	public void stop() {
		clip.stop();
	}

}
