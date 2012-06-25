package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logic.Board;
import logic.Player;
import logic.io.SimpleCellPalette;
import teampg.grid2d.point.RelPos;

import static java.awt.event.KeyEvent.*;

/**
 * Displays a JLabel containing the Board data and handles key events. Modified
 * slightly from Justin's snake game
 * 
 * @author Justin Rempel
 * @author JWill <Jackson.Williams at camosun.ca>
 * 
 */
public class Game extends JFrame implements KeyListener {
	private static final long serialVersionUID = 1L;
	private final Board board;
	private final Player player;

	private static final Dimension DISPLAY_SIZE = new Dimension(48, 48);
	private static final int FONT_SIZE = 12;

	public static void main(String[] args) {
		new Game();
	}

	private Game() {
		board = new Board();
		player = new Player(board);
		board.init(player);

		JLabel game = new JLabel(getHTMLDisplay());
		game.setFont(new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE));

		JPanel panel = new JPanel();
		panel.add(game, BorderLayout.CENTER);
		add(panel);

		setTitle("Infinichunk");
		setSize(DISPLAY_SIZE.width * 14, DISPLAY_SIZE.height * 16);
		setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		addKeyListener(this);
		setVisible(true);

		while (true) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				System.out.println("Timer interrupted");
				e.printStackTrace();
			}
			game.setText(getHTMLDisplay());
		}

	}

	private String getHTMLDisplay() {
		return board.display(board.getPos(player), DISPLAY_SIZE);
	}

	@Override
	public synchronized void keyPressed(KeyEvent keyEvent) {
		int key = keyEvent.getKeyCode();
		switch (key) {
		case VK_LEFT:
			player.move(RelPos.LEFT);
			break;
		case VK_UP:
			player.move(RelPos.UP);
			break;
		case VK_RIGHT:
			player.move(RelPos.RIGHT);
			break;
		case VK_DOWN:
			player.move(RelPos.DOWN);
			break;
		case VK_F3:
			BufferedImage im = board.exportToPNG(new SimpleCellPalette());
			try {
				File saveLoc = new File("out.png");
				ImageIO.write(im, "png", saveLoc);
				System.out.println("Saved map image dump to "
						+ saveLoc.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Could not write the image file.");
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}