package io.github.mcalphadev.api.worldgen;

import java.util.Random;

import io.github.mcalphadev.api.event.Event;

public class TerrainGenerateEvent extends Event {
	public final int chunkX, chunkZ;
	public final Random rand;
	public final byte[] chunkBlocks;

	public TerrainGenerateEvent(final byte[] chunkBlocks, Random rand, int chunkX, int chunkZ) {
		this.chunkBlocks = chunkBlocks;
		this.rand = rand;
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
	}
}
