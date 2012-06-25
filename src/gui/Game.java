package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

	public static void main(String[] args) {
		new Game();
	}

	private Game() {
		board = new Board();
		player = new Player(board);
		board.init(player);

		JLabel game = new JLabel(getHTMLDisplay());
		game.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

		JPanel panel = new JPanel();
		panel.add(game, BorderLayout.CENTER);
		add(panel);

		setTitle("Infinichunk");
		setSize(DISPLAY_SIZE.width * 14, DISPLAY_SIZE.height * 16);
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
		case 37: // left
			player.move(RelPos.LEFT);
			break;
		case 38: // up
			player.move(RelPos.UP);
			break;
		case 39: // right
			player.move(RelPos.RIGHT);
			break;
		case 40: // down
			player.move(RelPos.DOWN);
			break;
		case 114: // F3
			BufferedImage im = board.exportToPNG(new SimpleCellPalette());

			try {
				File saveLoc = new File("out.png");
				ImageIO.write(im, "png", saveLoc);
				System.out.println("Saved map image dump to " + saveLoc.getAbsolutePath());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Could not write the image file.");
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}
}