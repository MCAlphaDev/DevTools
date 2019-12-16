package io.github.mcalphadev.api.worldgen;

import java.util.Random;

public interface TerrainGenerateEventCallback {
	void modifyTerrain(final byte[] chunkBlocks, final Random rand, final int chunkX, final int chunkZ);
}
