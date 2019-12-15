package io.github.mcalphadev.launcher;

import java.util.Arrays;

import net.minecraft.launchwrapper.Launch;

public class AlphaModLauncher {
	private AlphaModLauncher() {
	}

	public static void main(String[] args) { // Eclipse run config program args: --assetsDir "${workspace_loc:DevTools/assets}"
		Launch.main(args(args, "--tweakClass", "io.github.mcalphadev.launcher.ClientTweaker"));
	}

	private static String[] args(String[] originalArgs, String...defaultArgs) {
		final int originalArgsLength = originalArgs.length;
		final int defaultArgsLength = defaultArgs.length;

		String[] resultArgs = Arrays.copyOf(originalArgs, originalArgsLength + defaultArgsLength);
		System.arraycopy(defaultArgs, 0, resultArgs, originalArgsLength, defaultArgsLength);

		return resultArgs;
	}
}
