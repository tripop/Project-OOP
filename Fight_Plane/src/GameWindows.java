import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameWindows extends WindowAdapter {
GamePanel game;
	
	public GameWindows(GamePanel game){
		game.frame.addWindowListener(this);
		this.game = game;
	}
	
	public void windowClosing(WindowEvent w) {
		if(w.getSource() == game.frame){
			new Quit(this);
			game.play = false;
		}else if(w.getSource() instanceof Quit){
			((Quit)(w.getSource())).dispose();
			if(!game.mainPanel && !game.resultPanel) game.play = true;
		}
		
	}
	
	private class Quit extends JFrame implements ActionListener{
		JLabel label = new JLabel("Are you sure you want to exit the game?");
		JButton yes = new JButton("Yes");
		JButton no = new JButton("No");
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		
		public Quit(GameWindows gw){
			setSize(300, 100);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			setResizable(false);
			setLocationRelativeTo(null);
			addWindowListener(gw);
			yes.addActionListener(this);
			no.addActionListener(this);
			panel1.setLayout(new FlowLayout());
			panel2.setLayout(new FlowLayout());
			panel1.add(label);
			panel2.add(yes);
			panel2.add(no);
			add(panel1);
			add(panel2,BorderLayout.SOUTH);
			setVisible(true);
		}
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == no) {
				dispose();
				if(!game.mainPanel && !game.resultPanel) game.play = true;
			}
			else
				System.exit(0);
		}	
	}
}
