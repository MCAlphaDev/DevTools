package io.github.mcalphadev.api.worldgen;

import java.util.Random;

import io.github.mcalphadev.api.event.Event;
import net.minecraft.level.Level;

public class TerrainPopulateEvent extends Event {
	public final int x, z;
	public final Random rand;
	public final Level level;

	public TerrainPopulateEvent(Level level, Random rand, int x, int z) {
		this.level = level;
		this.rand = rand;
		this.x = x;
		this.z = z;
	}
}
