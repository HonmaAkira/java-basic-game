package シューティング;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class MGFrame extends JFrame {
	private MGPanel panel = null;

	public static void main(String[] args) {
		MGFrame mg01 = new MGFrame();
	}

	//コンストラクタ

	public MGFrame() {
		//スーパークラスを呼び出す
		super();
		//xボタンが押されたら終了する
		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//レイアウト設定
		super.setLayout(new BorderLayout());
		//パネルを設定する
		panel = new MGPanel();
		//フレームのコンテントペインを置き換える
		super.setContentPane(panel);
		//フレームの表示
		super.setVisible(true);
		//サイズを最適化する
		super.pack();

	}//end MGFrame
}
