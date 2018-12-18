import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
	Font font = new Font(Font.MONOSPACED, Font.BOLD, 32);
	KeyConsole keys;
	MouseConsole mouse;
	GameWindows window;

	JFrame frame = new JFrame("Fight Plane");
	volatile boolean play;
	boolean inited = false;
	boolean up = false, down = false, right = false, left = false;
	boolean gameOver;
	boolean mainPanel;
	boolean resultPanel;

	BufferedImage backGround;
	BufferedImage opacity;
	Menu menuPanel;
	Result result;

	private long diff, start = System.currentTimeMillis();
	double theta;
	Thread thread;
	int turbo = 4;
	int numEnemies = 12;
	int enemiesSpeed = 3;
	int bulletSpeed = 10;
	int numHearts = 6;
	int remainingHearts = numHearts;
	int numMotherShip = 5;
	int numberOfEnemyBullets = 15;
	int numBullets = 25;
	int countEnemy = numEnemies + numMotherShip;
	int timeForBullet = 1;
	int seconds = 90;
	int heart2 = 3;
	static int score = 0;

	Player player;
	HpBar[] hearts;
	Enemy[] enemy;
	Bullet[] bullets;
	Bullet[] enemyBullets;
	Enemy[] motherShip;

	public GamePanel() {
		setSize(1080, 800);
		frame.setSize(1080, 800);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setResizable(false);
		setFocusable(true);
		frame.setLocationRelativeTo(null);
		setBackground(Color.BLACK);
		frame.add(this);
		File background = new File("back2_menu.png");
		try {
			backGround = (ImageIO.read(background)).getSubimage(0, 0, 1080, 800);
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.setVisible(true);
	}

	public void init() throws IOException {
		keys = new KeyConsole(this);
		mouse = new MouseConsole(this);
		window = new GameWindows(this);
		gameOver = true;
		play = false;
		resultPanel = false;
		menuPanel = new Menu(this);
		mainPanel = true;
		result = new Result(this);
		player = new Player();
		remainingHearts = numHearts;

		hearts = new HpBar[numHearts];
		for (int i = 0; i < numHearts; i++) {
			hearts[i] = new HpBar();
			hearts[i].x = 40 * i; // ระยะห่าวงระหว่างหัวใจ
		}

		enemy = new Enemy[numEnemies];
		for (int i = 0; i < numEnemies; i++) {
			enemy[i] = new Enemy();
		}
		motherShip = new Enemy[numMotherShip];
		for (int i = 0; i < numMotherShip; i++) {
			motherShip[i] = new Enemy(2);
		}

		thread = new Thread(this);

		bullets = new Bullet[numBullets];
		for (int i = 0; i < numBullets; i++) {
			bullets[i] = new Bullet(210, 200, 0);
		}

		enemyBullets = new Bullet[numberOfEnemyBullets];
		for (int i = 0; i < numberOfEnemyBullets; i++) {
			enemyBullets[i] = new Bullet(5);
		}

		inited = true;
	}

	public void run() {
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}

		while (true) {

			main();

			while (!gameOver) {
				try {
					play();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			result();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		if (inited) {
			if (mainPanel) {
				menuPanel.draw(g2d);
			} else if (!gameOver) {
				g2d.drawImage(backGround, 0, 0, null); // BackGround

				for (int i = 0; i < numHearts; i++)
					g2d.drawImage(hearts[i].getImage(), hearts[i].x, 600, null);

				theta = Math.atan2(mouse.getY() - (player.y + 40), mouse.getX() - (player.x + 50)) + Math.PI / 2;

				for (int i = 0; i < numBullets; i++) {
					if (bullets[i].fired) {
						g2d.drawImage(bullets[i].getImage(), bullets[i].x, bullets[i].y, null);
					}
				}
				for (int i = 0; i < numberOfEnemyBullets; i++) {
					if (enemyBullets[i].fired) {
						g2d.drawImage(enemyBullets[i].getImage(), enemyBullets[i].x, enemyBullets[i].y, null);
					}
				}

				player.draw(g2d, theta);

				for (int v = 0; v < numEnemies; v++) {
					if (enemy[v].Alive) {
						g2d.drawImage(enemy[v].getImage(), enemy[v].x, enemy[v].y, null);

					}

				}

				for (int v = 0; v < numMotherShip; v++) {
					if (motherShip[v].Alive) {
						g2d.drawImage(motherShip[v].getImage(), motherShip[v].x, motherShip[v].y, null);
					}

				}

			} else if (resultPanel) {
				try {
					result.draw(g2d);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void main(String[] args) {
		new Thread(new GamePanel()).start();
	}

	public void sleep(int fps) {
		if (fps > 0) {
			diff = System.currentTimeMillis() - start;
			long targetDelay = 1000 / fps;
			if (diff < targetDelay) {
				try {
					Thread.sleep(targetDelay - diff);
				} catch (InterruptedException e) {
				}
			}
			start = System.currentTimeMillis();
		}
	}

	public void addBullet(int x) {
		if (x == 1) {
			for (int i = 0; i < numBullets; i++) {
				if (!bullets[i].fired) {
					if (i % 2 == 0)
						bullets[i].x = player.x + 70;
					else
						bullets[i].x = player.x + 30;
					bullets[i].y = player.y + 40;
					bullets[i].theta = theta;
					bullets[i].sin = Math.sin(bullets[i].theta - Math.PI / 2);
					bullets[i].cos = Math.cos(bullets[i].theta - Math.PI / 2);
					bullets[i].fired = true;
					break;
				}
			}
		}

		if (x == 2) {
			for (int i = 0; i < numBullets; i++) {
				if (!bullets[i].fired) {
					bullets[i].fired = true;
					bullets[i].x = player.x + 40;
					bullets[i].y = player.y + 40;
					bullets[i].theta = Math.random() * Math.PI * 2;
					bullets[i].sin = Math.sin(bullets[i].theta - Math.PI / 2);
					bullets[i].cos = Math.cos(bullets[i].theta - Math.PI / 2);
					continue;
				}
			}
		}
	}

	public void addEnemyBullet(Enemy sEnemy) {
		for (int i = 0; i < numberOfEnemyBullets; i++) {
			if (!bullets[i].fired) {
				enemyBullets[i].x = sEnemy.x + 25;
				enemyBullets[i].y = sEnemy.y + 25;
				enemyBullets[i].theta = Math.atan2((player.y + 40) - (sEnemy.y), (player.x + 50) - (sEnemy.x));
				enemyBullets[i].sin = Math.sin(enemyBullets[i].theta); // - Math.PI/ 2)
				enemyBullets[i].cos = Math.cos(enemyBullets[i].theta);
				enemyBullets[i].fired = true;
				break;
			}
		}
	}

	public void play() throws IOException {

		while (play) {
			if (player.Alive) {
				if (up && player.y > 0)
					player.setY(player.y - turbo);
				if (down && player.y < 580)
					player.setY(player.y + turbo);
				if (right && player.x < 980)
					player.setX(player.x + turbo);
				if (left && player.x > 0)
					player.setX(player.x - turbo);

				for (int i = 0; i < numEnemies; i++) {
					enemy[i].theta = Math.atan2(enemy[i].y - (player.y + 40), enemy[i].x - (player.x + 50));

					enemy[i].y = enemy[i].y - (int) (enemiesSpeed * Math.sin(enemy[i].theta));

					enemy[i].x = enemy[i].x - (int) (enemiesSpeed * Math.cos(enemy[i].theta));

					if (enemy[i].Alive && remainingHearts != 0 && enemy[i].getBounds().intersects(player.getBounds())) {
						enemy[i].Alive = false;
						countEnemy--;
						score += 10;
						hearts[--remainingHearts].setImage();
						heart2--;
					}
				}

				for (int i = 0; i < numMotherShip; i++) {

					if (motherShip[i].Alive && remainingHearts != 0
							&& motherShip[i].getBounds().intersects(player.getBounds())) {
						motherShip[i].Alive = false;
						countEnemy--;
						score += 50;
						hearts[--remainingHearts].setImage();
						heart2--;
					}
				}

				for (int i = 0; i < numBullets; i++) {
					if (bullets[i].fired) {
						for (int v = 0; v < numEnemies; v++) {
							if (enemy[v].Alive) {
								if (enemy[v].getBounds().intersects(bullets[i].getBounds())) {
									enemy[v].Alive = false;
									countEnemy--;
									score += 10;
									bullets[i].fired = false;
								}
							}
						}
						for (int v = 0; v < numMotherShip; v++) {
							if (motherShip[v].Alive) {
								if (motherShip[v].getBounds().intersects(bullets[i].getBounds())) {
									motherShip[v].Alive = false;
									countEnemy--;
									score += 50;
									bullets[i].fired = false;
								}
							}
						}

						if (bullets[i].x < -100 || bullets[i].x > 1180 || bullets[i].y < -100 || bullets[i].y > 760)
							bullets[i].fired = false;
						else {
							bullets[i].y = bullets[i].y + (int) (bulletSpeed * bullets[i].sin);

							bullets[i].x = bullets[i].x + (int) (bulletSpeed * bullets[i].cos);
						}
					}
				}

				for (int i = 0; i < numberOfEnemyBullets; i++) {
					if (enemyBullets[i].fired) {
						if (player.getBounds().intersects(enemyBullets[i].getBounds())) {
							hearts[--remainingHearts].setImage();
							heart2--;
							enemyBullets[i].fired = false;
						}

						if (enemyBullets[i].x < -200 || enemyBullets[i].x > 1300 || enemyBullets[i].y < -200
								|| enemyBullets[i].y > 1000)
							enemyBullets[i].fired = false;
						else {
							enemyBullets[i].y = enemyBullets[i].y + (int) (bulletSpeed * enemyBullets[i].sin);

							enemyBullets[i].x = enemyBullets[i].x + (int) (bulletSpeed * enemyBullets[i].cos);
						}
					}
				}
				if (timeForBullet % seconds == 0) {
					int rand = (int) (Math.random() * numMotherShip);
					if (motherShip[rand].Alive) {
						addEnemyBullet(motherShip[rand]);
					}
				}

				if (remainingHearts == 0) {
					result.won = true;
					play = false;
					gameOver = true;
					resultPanel = true;
				}

				if (countEnemy == 0) {
					menuPanel.clicked = false;
					numEnemies = 10;
					numHearts = heart2;
					remainingHearts = heart2;
					numMotherShip = 5;
					countEnemy = numEnemies + numMotherShip;
					continued();
					mainPanel = false;
					play = true;
					gameOver = false;
				}

				repaint();
				sleep(60);
				timeForBullet++;
			}
		}
	}

	public void main() {

		while (mainPanel) {

			if (menuPanel.rectangle[0].contains(mouse.pClicked))
				menuPanel.clicked = true;
			else {
				if (!(menuPanel.rectangle[4].contains(mouse.pClicked) || menuPanel.rectangle[5].contains(mouse.pClicked)
						|| menuPanel.rectangle[6].contains(mouse.pClicked))) {
					menuPanel.clicked = false;
				}

			}

			if (menuPanel.rectangle[3].contains(mouse.pClicked))
				System.exit(0);

			if (menuPanel.rectangle[2].contains(mouse.pClicked)) {
				mouse.pClicked.x = 0;
				mouse.pClicked.y = 0;
				new About();
			}
			if (menuPanel.rectangle[1].contains(mouse.pClicked)) {
				mouse.pClicked.x = 0;
				mouse.pClicked.y = 0;
				new Help();
			}

			// Set ค่าเริ่มเกม
			if (menuPanel.clicked && menuPanel.rectangle[5].contains(mouse.pClicked)) {
				menuPanel.clicked = false;
				numEnemies = 10;
				numHearts = 3;
				remainingHearts = 3;
				numMotherShip = 5;
				countEnemy = numEnemies + numMotherShip;
				reset();
				mainPanel = false;
				play = true;
				gameOver = false;
			}
			repaint();
			sleep(60);

		}
	}

	public void result() {
		result.painted = false;
		mouse.pClicked.x = 0;
		mouse.pClicked.y = 0;
		while (resultPanel) {

			if (result.rectangle[1].contains(mouse.pClicked))
				System.exit(0);
			if (result.rectangle[0].contains(mouse.pClicked)) {
				mouse.pClicked.x = 0;
				mouse.pClicked.y = 0;
				resultPanel = false;
				mainPanel = true;
			}

			repaint();
			sleep(60);
			result.painted = true;

		}
	}

	public void reset() {

		for (int i = 0; i < numHearts; i++) {
			hearts[i].setHealth();
		}

		for (int i = 0; i < numEnemies; i++) {
			enemy[i].Alive = true;
			enemy[i].setLocation();
		}

		for (int i = 0; i < numMotherShip; i++) {
			motherShip[i].Alive = true;
			motherShip[i].setLocation();
		}

		for (int i = 0; i < numBullets; i++) {
			bullets[i].fired = false;
		}

		// Set จุดเกิดของ Player
		player.x = 1080 / 2;
		player.y = 800 / 2;
		score = 0;
	}

	public void continued() {

		for (int i = 0; i < numEnemies; i++) {
			enemy[i].Alive = true;
			enemy[i].setLocation();
		}

		for (int i = 0; i < numMotherShip; i++) {
			motherShip[i].Alive = true;
			motherShip[i].setLocation();
		}

		for (int i = 0; i < numBullets; i++) {
			bullets[i].fired = false;
		}

		enemiesSpeed += 1;
	}
}
