package io.github.mcalphadev.impl;

public final class AlphaWorldGenImpl {
	private AlphaWorldGenImpl() {
	}

	private static boolean cancelVanillaGenBase = false;

	public static void disableVanillaChunkShape() {
		cancelVanillaGenBase = true;
	}

	public static boolean cancelVanillaGenBase() {
		return cancelVanillaGenBase;
	}
}
