package othello.ui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class DiffDialog extends JPanel{
	private static final long serialVersionUID = 1L;
	private JTextPane textPane;
	private int [][] old;
	private int [][] ne;
	
	public DiffDialog() {
		super(new FlowLayout());
		textPane = new JTextPane();
		this.add(textPane);
	}

	public void changeTextPane() {
		textPane.setText("");
		StyledDocument doc = textPane.getStyledDocument();

		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		StyleConstants.setForeground(keyWord, Color.RED);
		StyleConstants.setBackground(keyWord, Color.YELLOW);
		StyleConstants.setBold(keyWord, true);

		try {
			doc.insertString(0, "   ", null);
			for (int y = 0; y < old.length; ++y) {
				doc.insertString(doc.getLength(), "  " + y, null);
			}
			doc.insertString(doc.getLength(), "\n", null);
			for (int y = 0; y < old.length; ++y) {
				doc.insertString(doc.getLength(), y + "[", null);
				for (int x = 0; x < ne.length; ++x) {
					if (old[y][x] == -1) {
						doc.insertString(doc.getLength(), " " + old[y][x], null);
					} else {
							doc.insertString(doc.getLength(), "  " + old[y][x], null);
					}
				}
				doc.insertString(doc.getLength(), "]\n", null);
			}
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		try {
			doc.insertString(doc.getLength(), "   ", null);
			for (int y = 0; y < old.length; ++y) {
				doc.insertString(doc.getLength(), "  " + y, null);
			}
			doc.insertString(doc.getLength(), "\n", null);
			for (int y = 0; y < old.length; ++y) {
				doc.insertString(doc.getLength(), y + "[", null);
				for (int x = 0; x < ne.length; ++x) {
					if (ne[y][x] == -1) {
						if (ne[y][x] == old[y][x])
							doc.insertString(doc.getLength(), " " + ne[y][x], null);
						else {
							doc.insertString(doc.getLength(), " " + ne[y][x], keyWord);
						}
					} else {
						if (ne[y][x] == old[y][x])
							doc.insertString(doc.getLength(), "  " + ne[y][x], null);
						else {
							doc.insertString(doc.getLength(), "  " + ne[y][x], keyWord);
						}
					}
				}
				doc.insertString(doc.getLength(), "]\n", null);
			}
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void changeTab(int [][]old, int [][] ne) {
		this.old = old;
		this.ne = ne;
	}
	
	public static void show(JFrame frame, Point point, int[][] old, int[][] ne) {
		JDialog dialog = new JDialog(frame, "DiffDialog", true);
		JPanel jpanel = new JPanel();
		JTextPane textPane = new JTextPane();
		dialog.setContentPane(jpanel);
		jpanel.add(textPane);


		StyledDocument doc = textPane.getStyledDocument();

		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		StyleConstants.setForeground(keyWord, Color.RED);
		StyleConstants.setBackground(keyWord, Color.YELLOW);
		StyleConstants.setBold(keyWord, true);

/*		SimpleAttributeSet keyWordFirst = new SimpleAttributeSet();
		StyleConstants.setForeground(keyWordFirst, Color.RED);
		StyleConstants.setBackground(keyWordFirst, Color.GREEN);
		StyleConstants.setBold(keyWordFirst, true);*/

		try {
			doc.insertString(0, "  ", null);
			for (int y = 0; y < old.length; ++y) {
				doc.insertString(doc.getLength(), "  " + y, null);
			}
			System.out.println(point);
			doc.insertString(doc.getLength(), "\n", null);
			for (int y = 0; y < old.length; ++y) {
				doc.insertString(doc.getLength(), y + "[", null);
				for (int x = 0; x < ne.length; ++x) {
					if (y == point.y && x == point.x) {
						doc.insertString(doc.getLength(), "  " + ne[y][x], keyWord);
					} else if (ne[y][x] == -1) {
						if (ne[y][x] == old[y][x])
							doc.insertString(doc.getLength(), " " + ne[y][x], null);
						else {
							doc.insertString(doc.getLength(), " " + ne[y][x], keyWord);
						}
					} else {
						if (ne[y][x] == old[y][x])
							doc.insertString(doc.getLength(), "  " + ne[y][x], null);
						else {
							doc.insertString(doc.getLength(), "  " + ne[y][x], keyWord);
						}
					}
				}
				doc.insertString(doc.getLength(), "]\n", null);
			}
			dialog.pack();
			dialog.setVisible(true);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}
