package ゲーム;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

/**
 * MIDI

 * MIDI演奏を制御します

 */
public class KSoundMidi {

	private Sequencer sequencer;

	/**
	 * コンストラクタ

	 * Midiオブジェクトを生成する

	 * @param obj パスを決めるオブジェクト
	 * @param fileName ファイル名
	 * @param flgLoop true:繰り返す false:繰り返さない
	 */


	public KSoundMidi(Object obj, String fileName, boolean flgLoop) {

		try {

			if (obj == null) {
				obj = this;
			}
			//filenameにはmid拡張子又はwav拡張子のファイル名が入る
			//ここでファイルを取得する
			InputStream is = obj.getClass().getResourceAsStream(fileName);
			//取得したファイルからmidi情報を取り出し、Sequence型の変数に格納する
			Sequence s = MidiSystem.getSequence(is);
			//MidiSystemクラスのgetSequencer()メソッドで音を鳴らすシーケンサーを取得して
			//このKSoundMidiクラスの属性に格納する
			this.sequencer = MidiSystem.getSequencer();
			//boolean型の引数flgLoopを決めることで、ゲーム中はmidi音源を繰り返し続ける
			if (flgLoop) {
				this.sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
			}
			this.sequencer.open();
			this.sequencer.setSequence(s);

		} catch (InvalidMidiDataException ex) {
			ex.printStackTrace();
			return;
		} catch (MidiUnavailableException ex) {
			ex.printStackTrace();
			return;
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}

	} // end KSoundMidi

	/**
	 * 音源スタート
	 */
	public void start() {
		sequencer.start();
	}

	/**
	 * 音源ストップ
	 */
	public void stop() {
		sequencer.stop();
	}

	/**
	 * 音源位置初期化
	 */
	public void init() {
		sequencer.setTickPosition(0l);
	}

	/**
	 * 演奏中かどうかを返します
	 * <pre>
	 * start()からstop()が呼び出されるまでtrueを返します
	 * </pre>
	 */
	public boolean isRunning() {
		return sequencer.isRunning();
	}

}
