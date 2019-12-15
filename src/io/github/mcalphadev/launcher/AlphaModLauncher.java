package io.github.mcalphadev.launcher;

import net.minecraft.launchwrapper.Launch;

public class AlphaModLauncher {
	private AlphaModLauncher() {
	}

	public static void main(String[] args) { // Eclipse run config program args: --assetsDir "${workspace_loc:DevTools/assets}"
		Launch.main(args("--tweakClass io.github.mcalphadev.launcher.ClientTweaker", args));
	}

	private static String[] args(String string, String[] originalArgs) {
		final int originalArgsLength = originalArgs.length;

		String[] defaultArgs = string.split(" ");
		final int defaultArgsLength = defaultArgs.length;

		String[] resultArgs = new String[defaultArgsLength + originalArgsLength];

		for (int i = 0; i < defaultArgsLength; ++i) {
			resultArgs[i] = defaultArgs[i];
		}

		for (int i = 0; i < originalArgsLength; ++i) {
			resultArgs[i + defaultArgsLength] = originalArgs[i];
		}

		return resultArgs;
	}
}
