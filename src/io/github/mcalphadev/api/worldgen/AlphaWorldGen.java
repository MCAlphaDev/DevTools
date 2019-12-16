package io.github.mcalphadev.api.worldgen;

import io.github.mcalphadev.impl.AlphaWorldGenImpl;

public final class AlphaWorldGen {
	private AlphaWorldGen() {
	}
	
	public static void disableVanillaChunkShape() {
		AlphaWorldGenImpl.disableVanillaChunkShape();
	}
}
