package io.github.mcalphadev.impl;

import java.util.Random;

import io.github.mcalphadev.api.event.EventType;
import io.github.mcalphadev.api.worldgen.TerrainGenerateEventCallback;

public final class TerrainGenerateEventType extends EventType<TerrainGenerateEventCallback> {
	public TerrainGenerateEventType(String name) {
		super(name);
	}
	
	public void post(final byte[] blocks, final Random rand, final int chunkX, final int chunkZ) {
		this.updateEventSubscribers();
		this.subscribers.forEach(event -> event.modifyTerrain(blocks, rand, chunkX, chunkZ));
	}
}
