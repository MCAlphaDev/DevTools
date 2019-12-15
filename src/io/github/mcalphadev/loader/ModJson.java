package io.github.mcalphadev.loader;

public class ModJson {
	public static class FormatBase {
		public int format = 0;
	}
	
	public static class Format0 extends FormatBase {
		public String mainClass = "";
		public String mixins = "";
	}
}
