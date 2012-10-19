package ui;

public enum Common {

	INSTANCE;
	
	private static String Status;
	private static int Progress;
	private static StatusBar curBar = null;
	
	private Common() {
		setStatus("");
		setProgress(0);
	}

	public static int getProgress() {
		return Progress;
	}

	public static void setProgress(int progress) {
		Progress = progress;
	}

	public static String getStatus() {
		return Status;
	}

	public static void setStatus(String status) {
		Status = status;
	}

	public static StatusBar getCurBar() {
		return curBar;
	}

	public static void setCurBar(StatusBar curBar) {
		Common.curBar = curBar;
	}
}
