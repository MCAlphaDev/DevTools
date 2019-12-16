package io.github.mcalphadev.api.worldgen;

import io.github.mcalphadev.api.event.EventType;

public final class WorldGenEvents {
	private WorldGenEvents() {
	}

	public static final EventType<TerrainDecorateEvent> TERRAIN_DECORATE = new EventType<>("terrain_decorate");
}
