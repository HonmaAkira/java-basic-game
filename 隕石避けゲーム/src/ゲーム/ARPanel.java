package ゲーム;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ARPanel extends JPanel {
	//このクラスの持つ属性(フィールド)

	//キーアダプター
	private MGKeyAdapter mgka = null;

	//キーを押している状態一覧
	private boolean[] keyPressTbl = null;

	//宇宙船位置
	private int x = 0;
	private int y = 0;

	//速度
	private int mx = 0;

	//宇宙船
	private BufferedImage[] imageShips = null;

	//宇宙船の向き
	private int muki = 0;

	//タイマー
	private java.util.Timer timerThis = null;

	//	経過時間
	private int time = 0;

	//コンストラクタ
	public ARPanel(){
		//スーパークラスを呼び出す
		super();
		try {

			//パネルサイズ
			super.setPreferredSize(new Dimension(800, 800));

			//レイアウト設定
			super.setLayout(null);

			//キー押下状況一覧
			keyPressTbl = new boolean[256];

			//宇宙船を読み込む(3種類の画像)、定型文なので覚える
			//3種類の画像を入れる配列を用意
			imageShips = new BufferedImage[3];
			InputStream is0 = this.getClass().getResourceAsStream("imageShip0.gif");
			imageShips[0]=ImageIO.read(is0);
			is0.close();

			InputStream is1 = this.getClass().getResourceAsStream("imageShipR.gif");
			imageShips[1] = ImageIO.read(is1);
			is1.close();

			InputStream is2 = this.getClass().getResourceAsStream("imageShipL.gif");
			imageShips[2] = ImageIO.read(is2);
			is2.close();


			//キーアダプタを生成、定型文
			mgka = new MGKeyAdapter();

			//パネルにキーリスナーを追加する
			this.addKeyListener(mgka);

			//初期化処理
			init();

//		タイマーを生成
			timerThis = new java.util.Timer();

			//タイマーをスタート
			timerThis.scheduleAtFixedRate(new TimerActionListerner(),1000l,8l);

		}catch(Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this,"ERROR:"+ex.toString());
		}
	}//end ARPanel

	//初期化処理
	public void init() {

		//時間
		time = 0;

		//宇宙船初期位置
		x = 384;
		y = 640;

	}//end init

	//実行
	public void run() {

		//時間+1
		time++;

		//宇宙船移動のタイミング
		if (time % 6 == 0) {

			//向きを初期化
			muki = 0;

			//左の場合
			if (isKeyCodePressed(KeyEvent.VK_LEFT)) {
				//左向きにする
				muki = 2;
				//加速する
				mx = mx - 1;
			}

			//右の場合
			if (isKeyCodePressed(KeyEvent.VK_RIGHT)) {
				//左向きにする
				muki = 1;
				//加速する
				mx = mx + 1;

			}
		} //end 宇宙船移動のタイミング

		//移動タイミング
		if (time % 2 == 0) {
			//移動する
			x = x + mx;

			//一番左に来た場合
			if (x < 0) {
				x = 0;
				mx = 0;
				//一番右に来た場合
			} else if (x > this.getWidth() - 32) {
				x = this.getWidth() - 32;
				mx = 0;

			}

			//表示タイミング
		} else {
			//再描画
			repaint();
		}
	}//end run



	//描画メソッド
	//ペイントする必要があるときにこのメソッドが呼び出されます
	public void paint(Graphics g) {

		//画面を塗りつぶす
		g.setColor(Color.black);
		g.fillRect(0, 0, 800, 800);

		//宇宙船を描画する
		g.drawImage(imageShips[muki],x, y,32,40, this);
	}//end paint



	//キー押下かどうかをチェック
	public boolean isKeyCodePressed(int KeyCode) {
		//void型ではないのでKeyCodeが引数に入った時に戻り値が存在する
		return keyPressTbl[KeyCode];

	}//end isKeyCodePressed



	//キーアダプタークラス
	private class MGKeyAdapter extends KeyAdapter{

		//キーを押したときに呼ばれる
		@Override
		public void keyPressed(KeyEvent ke) {
			int code = ke.getKeyCode();
			if(code<256) {
				keyPressTbl[code] = true;
			}
		}

		//キーを離したときに呼ばれる
		@Override
		public void keyReleased(KeyEvent ke) {
			int code = ke.getKeyCode();
			if(code<256) {
				keyPressTbl[code] = false;
			}

		}


	}//end MGKeyAdapter



	//タイマークラス
	private class TimerActionListerner extends TimerTask{
		//TimerTaskクラスには一回のゲームでの実行すべきrun()メソッドをオーバーライドする必要があることを覚えておく
		@Override
		public void run() {

			//実行メソッド呼び出し
			ARPanel.this.run();

		}//end actionPerformed
	}//end TimerActionListerner

}
