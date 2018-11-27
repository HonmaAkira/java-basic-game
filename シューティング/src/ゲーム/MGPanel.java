package ゲーム;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

//パネルクラス
//パネル処理を行うためのMGPanelクラス
//このクラスの中にパネル出力メソッド・描画メソッドの2つのメソッドを持つ
//javax.swingクラスのJPanelクラスを継承して自分用の画面表示を作る
public class MGPanel extends JPanel {
	//フェーズ
		private int phase =0;

	//マウスアダプタ
	private MGMouseAdapter mgma = null;


	//背景
	private BufferedImage imageBackground = null;

	//得点
	private int score =0;
	private Font fontScore = new Font("MSゴシック", Font.BOLD|Font.ITALIC, 24);

	//制限時間
	private int time =1859;
	private Font fontTime = new Font("MSゴシック",Font.BOLD,24);

	//ゲーム終了
	private Font fontGameover = new Font("MSゴシック",Font.BOLD,48);


	//ピコハン座標
	private int px=0;
	//ピコハン座標
	private int py=0;

	//もぐら座標
	private int mx=0;
	//もぐら座標
	private int my=0;

	//ピコピコハンマー
	private BufferedImage[] imagePHs =null;
	//ピコピコハンマー状態
	private int ph = 0;

	//モグラ
	private BufferedImage[] imageMs =null;
	//モグラ状態
	private int m = 0;
	//モグラの時間
	private int timeM = 0;
	//タイマー
	private java.util.Timer timerThis = null;

	//BGM
	private KSoundMidi midiMoguraDance = null;

	//効果音
//	private KSoundWave waveMoguraDeru = null;
//	private KSoundWave wavePicoHammerMiss = null;
//	private KSoundWave wavePicoHammerHit = null;
	//コンストラクタ
	public MGPanel() {
	//スーパークラスを呼び出す
		super();
		try {
	//パネルサイズ
		super.setPreferredSize(new Dimension(800,600));
	//レイアウト設定
		super.setLayout(null);
	//マウスアダプタを生成
		mgma = new MGMouseAdapter();
	//パネルにマウスリスナーを追加する
		super.addMouseListener(mgma);
		super.addMouseMotionListener(mgma);
	//ピコピコハンマーを読み込む
		imagePHs = new BufferedImage[2];
		InputStream isPH00 = this.getClass().getResourceAsStream("PH00.gif");
		imagePHs[0] = ImageIO.read(isPH00);
		isPH00.close();

		InputStream isPH01 = this.getClass().getResourceAsStream("PH01.gif");
		imagePHs[1] = ImageIO.read(isPH01);
		isPH01.close();


		//モグラを読み込む
			//画像を格納するBufferedImageの配列を作成
			imageMs = new BufferedImage[2];
			//同じパッケージ内の画像ファイルを指定してInputStreamの変数に格納する
			InputStream isM00 = this.getClass().getResourceAsStream("M00.gif");
			//BufferedImageの用意した配列の一つに取得した画像ファイルを格納する
			imageMs[0] = ImageIO.read(isM00);
			//close()は大事！！処理落ちやエラーの原因となる
			isM00.close();


			InputStream isM01 = this.getClass().getResourceAsStream("M01.gif");
			imageMs[1] = ImageIO.read(isM01);
			//close()は大事！！処理落ちやエラーの原因となる
			isM01.close();

			//タイマーを生成
			timerThis = new java.util.Timer();
			//タイマーをスタート
			timerThis.scheduleAtFixedRate(new TimerActionTimerTask(), 1000l, 16l);

		//背景を読み込む
			InputStream isBackgrond = this.getClass().getResourceAsStream("Background.jpg");
			imageBackground = ImageIO.read(isBackgrond);
			isBackgrond.close();

		//BGMを生成
			midiMoguraDance =new KSoundMidi(this,"MoguraDance.mid",false);

		//効果音を生成
//			waveMoguraDeru = new KSoundWave(this,"sound\\MoguraDeru.wav",false);
//			wavePicoHammerMiss = new KSoundWave(this,"sound\\PicoHammerMiss.wav",false);
//			wavePicoHammerHit = new KSoundWave(this,"sound\\PicoHammerHit.wav",false);
		//初期化
			init();

		}catch(Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "ERROR :" + ex.toString());
		}
	}//end MGPanel

	//初期化
	public void init() {
		//フェーズをゲーム中にする
		phase = 0;
		//モグラの状態
		m = 0;
		//得点
		score = 0;
		//時間
		time = 1859;
		//BGMを初期化
		//KSoundMidiクラスの中のinit()を使う
		midiMoguraDance.init();

		//BGMをスタート
		//KSoundMidiクラスの中のstart()を使う
		midiMoguraDance.start();
	}//end init


	//実行
	public void run() {

		//フェーズ
		switch(phase) {

		case 0 :
			//制限時間を減らす
			time--;
			//制限時間が来たら
			if(time == 0) {
				//時間を戻す
				time = 300;
				//フェーズ変更しゲーム終了
				phase = 1;
				//ブレイク
				break;
			}
			//やられている場合
			if(timeM !=0) {
				//時間を-1する
				timeM--;
				if(timeM ==0) {
					//新しいモグラを表示する
					m=0;

					mx = (int)(Math.random() *550);
					my = (int)(Math.random() *450);

					//効果音をスタート
//					waveMoguraDeru.start();
				}//end if 時間が0になったら
			}// end if やられている場合

			//ブレイク
			break;

		case 1 :
			//制限時間を減らす
			time--;
			//制限時間が来たら
			if(time == 0) {
				//初期化
				init();
			}
			//ブレイク
			break;
		}//end switch

	}// end run

	//描画メソッド
	//ペイントする必要があるときにこのメソッドを呼び出されます

	public void paint(Graphics g) {

		//背景を描写する
		g.drawImage(imageBackground, 0, 0, 800, 600,this);

		//モグラを描画する
				g.drawImage(imageMs[m], mx, my, 100, 100, this);

		//ピコピコハンマーを描画する
		g.drawImage(imagePHs[ph], px, py, 100,88,this);

		//得点を表示する
		g.setColor(Color.yellow);
		g.setFont(fontScore);
		g.drawString("得点:"+score, 0, 24);

		//ゲーム中の場合
		if(phase == 0) {
			//制限時間を表示する
			g.setColor(Color.RED);
			g.setFont(fontTime);
			g.drawString("残り時間:"+time/60, 300, 24);

		//ゲームオーバーの場合
		}else if(phase ==1) {
			g.setColor(Color.LIGHT_GRAY);
			g.setFont(fontGameover);
			//Graphicsクラスで文字などをsystem.out.printlnのように出力したいときは
			//drawStringメソッドを使って、文字列、文字の大きさを指定する
			g.drawString("ゲーム終了", 240, 300);

			//コメント
			if(score>=30) {
				g.drawString("おぬしもやるのう", 240, 350);
			}else if(score>=20) {
				g.drawString("もう一歩じゃな", 240, 350);
			}else if(score>=10) {
				g.drawString("まだまだじゃな", 240,350 );
			}else {
				g.drawString("がんばるのじゃ", 240, 350);
			}
		}//end if ゲームオーバーの場合

	}//end paint

	//マウスアダプタ
	//java.awt.eventクラスのMouseAdapterクラスを継承して自分用のマウス入出力処理を書く
	//ゲームを動かす要素の中の一つとしてMGMouseAdapterクラスの中に3つのメソッドを作って格納しておく

	private class MGMouseAdapter extends MouseAdapter{


		//マウスが押されたときに呼ばれるメソッド
		public void mousePressed(MouseEvent me) {
			//マウスイベントの内容を出力

			//ゲーム中の場合
			if(phase == 0) {

				//ピコハンをたたく
				ph = 1;

				//場所を記憶する
				px = me.getX()-100;
				py = me.getY()-100;

				//やられていない場合
				if(m==0) {

					//もぐらとの当たり判定
					if(px>mx-50&&px<mx+90&&py>my-70&&py<my+60) {
						//やられた
						m=1;
						timeM = 30;
						score++;
						//効果音をスタート
//						wavePicoHammerHit.start();

						//外れた場合
					}else {
//						wavePicoHammerMiss.start();
					}

				}//end やられていない場合
			}//end if ゲーム中の場合

		}//end mousePressed


		public void mouseReleased(MouseEvent me) {
		//マウスイベントの内容を出力



				//ピコハンをあげる
				ph = 0;
				//場所を記憶する
				px = me.getX()-100;
				py = me.getY()-100;


		} //end mouseReleased


		public void mouseMoved(MouseEvent me) {
		//マウスイベントの内容を出力

		//場所を記憶する
			px = me.getX()-100;
			py = me.getY()-100;
		//描画する
			repaint();
		} //end mouseMoved


		public void mouseDragged(MouseEvent me) {
		//マウスイベントの内容を出力

		//場所を記憶する
			px = me.getX()-100;
			py = me.getY()-100;
		//描画する
			repaint();
		} //end mouseDragged

	} //end MGMouseAdapter

	//タイマークラス

	private class TimerActionTimerTask extends TimerTask{
		//runクラスを呼び出す
		public void run() {
			//実行メソッドの呼び出し
			MGPanel.this.run();
			//描画する
			repaint();
		}//end actionPerformed
	}//end TimerActionTimerTask
}
