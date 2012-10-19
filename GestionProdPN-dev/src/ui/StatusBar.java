package ui;

import javax.swing.JProgressBar;
import javax.swing.JTextPane;

public class StatusBar {
	
	private JTextPane sp;
	private JProgressBar pb;
	private static final String statusPrefixe = Messages.getString("StatusBar.Prefixe");
	private int max;
	
	public StatusBar(JTextPane _sp, JProgressBar _pb)
	{
		sp = _sp;
		pb = _pb;
		max=100;
	}
	
	public void set(String status) {
		if (status != null) {
			Common.setStatus(status);
		} else {
			Common.setStatus(Messages.getString("StatusBar.NoAction")); //$NON-NLS-1$
		}
	}
	public void set(int prog) {
		if (prog == -1) {
			prog = max;
		}
		Common.setProgress(Math.min(prog,max));
	}
	
	public void set(String status, int prog) {
		set(status);
		set(prog);
	}
	
	public void update() {
		sp.setText(statusPrefixe + Common.getStatus());
		pb.setValue(Common.getProgress());
		//System.out.println("U:"+Common.getProgress()+Common.getStatus());
		//pb.updateUI();
	}
	public void update(String status) {
		set(status);
		update();
	}
	public void update(int prog) {
		set(prog);
		update();
	}
	public void update(String status, int prog) {
		set(status, prog);
		update();
	}
	public void updateIncr(String status) {
		set(status);
		incr();
	}

	public void select() {
		Common.setCurBar(this);
	}
	
	public void setMax(int _max) {
		max = _max;
		pb.setMaximum(max);
		update();
	}
	public void incr() {
		set(Common.getProgress()+1);
		update();
	}
}
