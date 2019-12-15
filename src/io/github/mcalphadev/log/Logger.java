package io.github.mcalphadev.log;

public class Logger {
	private String formattedName;
	private boolean debug = false;

	public Logger(String name) {
		this.formattedName = "[" + name + "] [";
	}

	public Logger setDebug(boolean debug) {
		this.debug = debug;
		return this;
	}

	public boolean isDebug() {
		return debug;
	}

	public void info(String info) {
		this.out("INFO", info);
	}
	public void infoAll(String info, String...concats) {
		StringBuilder sb = new StringBuilder(info);
		for (String concat : concats) {
			sb.append(concat);
		}

		this.out("INFO", sb.toString());
	}

	public void debug(String info) {
		if (debug) {
			this.out("DEBUG", info);
		}
	}
	public void debugAll(String info, String...concats) {
		StringBuilder sb = new StringBuilder(info);
		for (String concat : concats) {
			sb.append(concat);
		}
		if (debug) {
			this.out("DEBUG", sb.toString());
		}
	}

	public void warn(String info) {
		this.out("WARN", info);
	}

	protected void out(String type, String out) {
		System.out.println(new StringBuilder()
				.append(formattedName)
				.append(type)
				.append("] ")
				.append(out)
				.toString()
				);
	}
}
