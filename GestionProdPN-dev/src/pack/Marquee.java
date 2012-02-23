package pack;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * commposant graphique qui affiche un message déroulant
 * @author BERON Jean-Sebastien
 *
 */
class Marquee extends JPanel implements ActionListener {

	private static final long serialVersionUID = 479877668733621786L;
	
	//attributs
	private static final int RATE = 10;
    private final Timer timer = new Timer(1000 / RATE, this);
    private final JLabel label = new JLabel();
    private final String s;
    private final int n;
    private int index;

    /**
     * constructeur
     * @param s
     * @param n
     */
    public Marquee(String s, int n) {
    	
        if (s == null || n < 1) {
            throw new IllegalArgumentException("Null string or n < 1");
        }
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append(' ');
        }
        this.s = sb + s + sb;
        this.n = n;
        label.setText(sb.toString());
        label.setFont(new Font("Arial", 1, 30));
		label.setForeground(Color.WHITE);
		this.setBackground(Color.BLUE);
        this.add(label);
        
    }//fin Marquee()

    /**
     * procedure qui lance le deroulement du message
     */
    public void start() {
        timer.start();
    }

    /**
     * procedure qui arrete le deroulement du message
     */
    public void stop() {
        timer.stop();
    }

    /**
     * procedure de l'interface ActionListener
     */
    public void actionPerformed(ActionEvent e) {
    	
        index++;
        if (index > s.length() - n) {
            index = 0;
        }
        label.setText(s.substring(index, index + n));
        
    }//fin actionPerformed()
    
}//fin class