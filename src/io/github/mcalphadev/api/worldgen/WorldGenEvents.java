package io.github.mcalphadev.api.worldgen;

import io.github.mcalphadev.api.event.EventType;

public final class WorldGenEvents {
	private WorldGenEvents() {
	}

	public static final EventType<TerrainGenerateEvent> SHAPE_CHUNK = new EventType<>("devtools:shape_chunk");
	public static final EventType<TerrainGenerateEvent> REPLACE_BLOCKS = new EventType<>("devtools:replace_blocks");
	public static final EventType<TerrainPopulateEvent> TERRAIN_DECORATE = new EventType<>("devtools:terrain_decorate");
}
