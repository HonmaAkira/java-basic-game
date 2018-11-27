package ゲーム;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class ARFrame extends JFrame {
	//ARPanelのインスタンス化
	//メインクラスのあるこのARFrameクラスでインスタンス化する
	private ARPanel panel = null;

	public static void main(String[] args) {
		//プログラム実行のためのARFrameをインスタンス化する
		ARFrame mg01 = new ARFrame();
	}

	//コンストラクタ
	public ARFrame() {

		//スーパークラスを呼び出す
		super();
		//×を押すと終了する
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//レイアウト設定
		super.setLayout(new BorderLayout());
		//パネル設定
		panel = new ARPanel();
		//フレームのコンテントペインを置き換える
		super.setContentPane(panel);
		//フレームを表示
		super.setVisible(true);
		//サイズを最適化する
		super.pack();

		//フォーカスをあてる
		//この二文がなかった時はプログラムが動かなかったのでフォーカスは大事
		super.requestFocus();
		panel.requestFocus();

	}
}
