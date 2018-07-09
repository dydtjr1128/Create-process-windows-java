public class ProcessBuild {
	private final int subProcessNum = 10;
	private final int pubProcessNum = 0;
	private String topic = "a/b";
	
	public void run() {
		startProcess(subProcessNum,true);//sub
		startProcess(pubProcessNum,false);
	}

	public void startProcess(int num, boolean sub) {
		for (int i = 0; i < num; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					String command[] = (sub ? getSubscriberCommand() : getPublisherCommand());
					// String command[] = {"cmd.exe", "/c", "dir"};
					ProcessBuilder builder = new ProcessBuilder(command);
					builder.redirectError();
					builder.redirectInput(ProcessBuilder.Redirect.INHERIT);
					builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
					try {
						Process process = builder.start();

						process.waitFor();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public String[] getSubscriberCommand() {
		final int TBF[] = { 50, 100, 150, 200, 250 };
		final String subPath = "C:\\Temp\\mosquitto-1.4.14\\build\\client\\Release\\mosquitto_sub.exe";
		int index = (int) (Math.random() * TBF.length);
		String command[] = {"cmd.exe", "/c", "start", "\"Sub" + TBF[index] + "\"", "cmd.exe","/c", subPath, "-t", topic, "-F", Integer.toString(TBF[index])};
		return command;
	}

	public String[] getPublisherCommand() {
		final String pubPath = "C:\\Temp\\mosquitto-1.4.14\\build\\client\\Release\\mosquitto_pub.exe";
		String message = Integer.toString((int) (Math.random() * 31 + 10));// 10~40
		String command[] = { pubPath, "-t", topic, "-m", message };
		return command;
	}
}
