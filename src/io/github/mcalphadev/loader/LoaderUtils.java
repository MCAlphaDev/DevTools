package io.github.mcalphadev.loader;

import java.io.File;
import java.io.FilenameFilter;

public final class LoaderUtils {
	private LoaderUtils() {
	}

	public static File[] findAllJars(final File modsFolder) {
		return modsFolder.listFiles(new FilenameFilter() {
			@Override public boolean accept(File dir, String name) {
				return name.endsWith(".jar") && !(new File(dir.getAbsolutePath() + "/" + name).isDirectory());
			}
		});
	}
}
