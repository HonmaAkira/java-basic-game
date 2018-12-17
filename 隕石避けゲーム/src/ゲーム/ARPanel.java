package ゲーム;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ARPanel extends JPanel {
	//このクラスの持つ属性(フィールド)

	/**
	 * フェーズ
	 */
	private final int PHASE_TITLE = 0;
	private final int PHASE_GAMESTART = 1;
	private final int PHASE_GAME = 2;
	private final int PHASE_CLASH = 3;
	private final int PHASE_GAMEOVER = 4;

	/**
	 * フォント
	 */
	private final Font FONT_TEXT = new Font("MSゴシック",Font.PLAIN,16);
	private final Font FONT_GAMEOVER = new Font("MSゴシック",Font.BOLD,24);

	/**
	 * フェーズ
	 */
	private int phase = 0;

	/**
	 * スコア
	 */
	private int score = 0;
	private int scoreHigh = 30000;

	/**
	 * キーアダプター
	 */
	private MGKeyAdapter mgka = null;

	/**
	 * キーを押している状態一覧
	 */
	private boolean[] keyPressTbl = null;

	/**
	 * 背景1位置
	 */
	private int yBackground1 = 0;
	/**
	 * 背景2位置
	 */
	private int yBackground2 = 0;

	/**
	 * 宇宙船位置
	 */
	private int x = 0;
	private int y = 0;


	/**
	 * 小惑星位置
	 */
	private int[] xAsteroids = new int[100];
	private int[] yAsteroids = new int[100];

	/**小惑星速度
	 *
	 */
	private int[] mxAsteroids = new int[100];
	private int[] myAsteroids = new int[100];

	/**
	 * 小惑星角度
	 */
	private int[] angleAsteroids = new int[100];
	private int[] velocityAsteroids = new int[100];

	/**小惑星大きさ
	 *
	 */
	private int[] widthAsteroids = new int[100];

	/**
	 * 背景１
	 */
	private BufferedImage imageBackground1 = null;
	/**
	 * 背景２
	 */
	private BufferedImage imageBackground2 = null;

	/**
	 * 小惑星
	 */
	private BufferedImage imageAsteroids = null;

	/**
	 * 速度
	 */
	private int mx = 0;
	private int my = 0;
	/**
	 * 宇宙船
	 */
	private BufferedImage[] imageShips = null;

	/**
	 * 宇宙船の向き
	 */
	private int muki = 0;

	/**
	 * タイマー
	 */
	private java.util.Timer timerThis = null;

	/**
	 * 経過時間
	 */
	private int time = 0;

	/**
	 * 時間(いろいろ)
	 */
	private int timeWork = 0;

	/**
	 * 効果音
	 */
	private KSoundWave[] waveSpace = null;
	private KSoundWave waveGetReady = null;
	private KSoundWave waveClash = null;
	private KSoundWave waveGameOver = null;

	/**
	 * 音楽
	 */
	private KSoundMidi midiSound = null;

	/**
	 * ポジション
	 */
	private int posBgm = 0;

	/**
	 * 時間(BGM)
	 */
	private int timeBgm = 0;
	private int timeBgmNext = 0;


	/**
	 * コンストラクタ
	 */
	public ARPanel(){
		/**
		 * スーパークラスを呼び出す
		 */
		super();
		try {

			/**
			 * パネルサイズ
			 */
			super.setPreferredSize(new Dimension(800, 800));

			/**
			 * レイアウト設定
			 */
			super.setLayout(null);


			//ゲームスタート
			start();

			/**
			 * キー押下状況一覧
			 */
			keyPressTbl = new boolean[256];

			/**
			 * 背景１を読み込む
			 */
			InputStream isBackground1 = this.getClass().getResourceAsStream("imageBackground1.gif");
			imageBackground1 = ImageIO.read(isBackground1);
			isBackground1.close();

			/**
			 * 背景２を読み込む
			 */
			InputStream isBackground2 = this.getClass().getResourceAsStream("imageBackground2.gif");
			imageBackground2 = ImageIO.read(isBackground2);
			isBackground2.close();

			/**
			 * 小惑星を読み込む
			 */
			InputStream isAsteroids = this.getClass().getResourceAsStream("imageAsteroid.gif");
			imageAsteroids = ImageIO.read(isAsteroids);
			isAsteroids.close();

			//宇宙船を読み込む(3種類の画像)、定型文なので覚える
			/**
			 * 3種類の画像を入れる配列を用意
			 */
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


			/**
			 * キーアダプタを生成、定型文
			 */
			mgka = new MGKeyAdapter();

			/**
			 * パネルにキーリスナーを追加する
			 */
			this.addKeyListener(mgka);

			/**
			 * 効果音を生成
			 */
			waveSpace = new KSoundWave[4];
			waveSpace[0] = new KSoundWave(this, "waveSpace0.wav", false);
			waveSpace[1] = new KSoundWave(this, "waveSpace1.wav", false);
			waveSpace[2] = new KSoundWave(this, "waveSpace2.wav", false);
			waveSpace[3] = new KSoundWave(this, "waveSpace3.wav", false);
			waveGetReady = new KSoundWave(this , "waveGetReady.wav", false);
			waveClash = new KSoundWave(this, "waveClash.wav", false);
			waveGameOver = new KSoundWave(this, "waveGameOver.wav", false);


			midiSound = new KSoundMidi(this, "midiGame.mid", false);

			/**
			 * 初期化処理
			 */
			init();

			/**
			 * タイマーを生成
			 */
			timerThis = new java.util.Timer();

			/**
			 * タイマーをスタート
			 */
			timerThis.scheduleAtFixedRate(new TimerActionListerner(),1000l,8l);

		}catch(Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this,"ERROR:"+ex.toString());
		}
	}//end ARPanel

	/**
	 * 初期化処理
	 */
	public void init() {

		//時間
		time = 0;

		//スコア
		score = 0;

		//宇宙船初期位置
		x = 384;
		y = 640;

		mx = 0;
		my = 0;

		//音楽を初期化
		midiSound.init();

		//音楽スタート
		midiSound.start();

		//効果音をスタート
		waveGetReady.start();

		/**
		 * 時間(BGM)
		 */
		timeBgm = 0;

		/**
		 * 次の時間(BGM)
		 */
		timeBgmNext = 0;



		//小惑星を初期化
		for(int i=0;i<100;i++) {
			yAsteroids[i] = -9999;
		}

	}//end init

	/**
	 * ゲームスタート
	 */
	public void start() {
		phase = PHASE_GAME;

	}


	//実行
	public void run() {

		//時間+1
		time++;



		//背景１移動タイミング
		if(time%5 == 0) {
			/**
			 * 背景を進める
			 */
			yBackground1++;
			if(yBackground1>0) {
				yBackground1 = -800;
			}
		}//end 背景１移動タイミング

		//背景２移動タイミング
		if(time%2 ==0) {
			/**
			 * 背景を進める
			 */
			yBackground2 = yBackground2 + 1;
			if(yBackground2>0) {
				yBackground2 = -800;
			}
		}

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

			//ゲーム中の場合
			if(phase == PHASE_GAME) {


				//スコア＋１０
				score = score+10;

				//ハイスコアを超えた場合
				if(score>scoreHigh) {
					//ハイスコアを更新
					scoreHigh = score;
				}
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
			//障害物に当たった場合
			}else if(phase == PHASE_CLASH) {

				//時間＋１
				timeWork++;

				//位置移動
				x = x + mx;
				y = y + my;

				//二秒たったら
				if(timeWork == 120) {

					//時間を初期化
					timeWork = 0;

					//効果音をスタート
					waveGameOver.start();

					//ゲームオーバー
					phase = PHASE_GAMEOVER;
				}
			}else if(phase == PHASE_GAMEOVER) {

				//時間＋１
				timeWork++;

				//五秒たったら
				if(timeWork ==300) {

					//初期化
					init();

					//ゲームスタート
					start();
				}
			}





			//小惑星作る?
			if(Math.random()<0.02) {

				//空いている小惑星を探す
				int pos = -1;
				for(int i= 0;i<100;i++) {
					if(yAsteroids[i] ==-9999) {
						pos = i;
						break;
					}
				}

				//小惑星を初期化
				widthAsteroids[pos] = (int)(Math.random() * 150)+30;
				xAsteroids[pos] = (int)(Math.random() * 800);
				yAsteroids[pos] = -widthAsteroids[pos];
				mxAsteroids[pos] = 3 - (int)(Math.random() * 6);
				myAsteroids[pos] = 5 + (int)(Math.random() * 5);
				angleAsteroids[pos] = (int)(Math.random() * 360);
				velocityAsteroids[pos] = (int) (Math.random() * 10);
			}//end 小惑星作る?

			//小惑星一覧分、ループ
			for(int i = 0;i<100;i++) {

				//小惑星が存在する
				if(yAsteroids[i] !=-9999) {

					//移動する
					xAsteroids[i] = xAsteroids[i] + mxAsteroids[i];
					yAsteroids[i] = yAsteroids[i] + myAsteroids[i];
					angleAsteroids[i] =angleAsteroids[i] +velocityAsteroids[i];

					//衝突判定
					if(xAsteroids[i] <x+10 &&x+32-10<xAsteroids[i]+widthAsteroids[i]
							&&yAsteroids[i] <y+20&&y+40-20 <yAsteroids[i]+widthAsteroids[i]) {

						//宇宙船を下げる
						mx = mxAsteroids[i];
						my = myAsteroids[i];

						//効果音スタート
						waveClash.start();

						//時間を初期化
						timeWork = 0;

						//激突
						phase = PHASE_CLASH;
					}

					//外に出たら
					if(yAsteroids[i] > 800) {
						//小惑星削除
						yAsteroids[i] = -9999;
					}
				}//end 小惑星が存在する
			}//end 小惑星一覧分、ループ

			//表示タイミング
		}//end 移動タイミング

		//再描画タイミング
		if(time%2 == 1) {
			repaint();
		}

		//時間(BGM)+1
		timeBgm++;

		//効果音挿入タイミング
		if(timeBgm>timeBgmNext) {

			//効果音挿入タイミング
			waveSpace[posBgm].start();

			//時間(BGM)
			timeBgm = 0;
			//次の時間(BGM)
			timeBgmNext = (int)(Math.random() * 840)+420;
			//ポジション(BGM)
			posBgm = (posBgm + 1 + (int)(Math.random() * 3)) % 4;

		}//end 効果音挿入タイミング



	}//end run



	//描画メソッド
	//ペイントする必要があるときにこのメソッドが呼び出されます
	public void paint(Graphics g) {

		/**
		 * Graphics2Dを取得
		 */
		Graphics2D g2 = (Graphics2D) g;

		/**
		 * Transformを取得する
		 */
		AffineTransform af = new AffineTransform();

		/**
		 * 背景１を描画する
		 */
		/**
		 * 疑似スクロールのため背景画像を2枚縦に配置する。
		 */
		g.drawImage(imageBackground1, 0, yBackground1, 800, 800, this);
		g.drawImage(imageBackground1, 0, yBackground1+800,800,800, this);

		/**
		 * 背景2を描画する
		 */
		/**
		 * 疑似スクロールのため背景画像を2枚縦に配置する。
		 */
		g.drawImage(imageBackground2, 0, yBackground1,800,800,this);
		g.drawImage(imageBackground2, 0, yBackground2+800, 800, 800, this);


		/**
		 * 小惑星一覧、ループ
		 */
		for(int i=0;i<100;i++) {

			//小惑星が存在する?
			if(yAsteroids[i] != -9999) {

				/**
				 * 回転を計算
				 */
				double radAsteroids = angleAsteroids[i] * Math.PI/180;

				/**
				 * 表示位置計算
				 */
				double xAsteroid = (double)xAsteroids[i] + widthAsteroids[i] /2;
				double yAsteroid = (double)yAsteroids[i] + widthAsteroids[i] /2;

				/**
				 * 回転を加え、表示位置を決定する
				 */
				af.setToRotation(radAsteroids, xAsteroid, yAsteroid);

				/**
				 * Transformを設定する
				 */
				g2.setTransform(af);

				/**
				 * 小惑星を描画する
				 */
				g.drawImage(imageAsteroids, xAsteroids[i], yAsteroids[i],widthAsteroids[i],widthAsteroids[i],this);
			}//end 小惑星が存在する?
		}//小惑星一覧、ループ

		/**
		 * 回転と表示位置を戻す
		 */
			af.setToRotation(0d, 0d, 0d);

		/**
		 * Transformを設定する
		 */
			g2. setTransform(af);

		/**
		 * 宇宙船を描画する
		 */
		g.drawImage(imageShips[muki],x, y,32,40, this);

		/**
		 * 色の設定
		 */
		g2.setColor(Color.LIGHT_GRAY);

		/**
		 * フォントの設定
		 */
		g2.setFont(FONT_TEXT);

		/**
		 * スコアの表示
		 */
		g2.drawString("SCORE"+score,100,20);

		/**
		 * ハイスコアの表示
		 */
		g2.drawString("HIGHSCORE"+scoreHigh, 500,20);

		/**
		 * ゲームオーバーの場合
		 */
		if(phase==PHASE_GAMEOVER) {

			//フォントの設定
			g2.setFont(FONT_GAMEOVER);

			//ゲームオーバーを表示
			g2.drawString("GAME_OVER", 300, 350);
		}


	}//end paint



	/**
	 * キー押下かどうかをチェック
	 * @param KeyCode
	 * @return
	 */
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



	/**
	 * タイマークラス
	 * @author akira
	 *
	 */
	private class TimerActionListerner extends TimerTask{
		//TimerTaskクラスには一回のゲームでの実行すべきrun()メソッドをオーバーライドする必要があることを覚えておく
		@Override
		public void run() {

			/**
			 * 実行メソッド呼び出し
			 */
			ARPanel.this.run();

		}//end actionPerformed
	}//end TimerActionListerner

}
