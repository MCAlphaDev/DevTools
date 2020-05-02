package io.github.mcalphadev.api.worldgen;

import java.util.Random;

import net.minecraft.level.Level;

public interface TerrainDecorateEvent {
	void decorate(Level level, Random rand, int x, int z);
}
