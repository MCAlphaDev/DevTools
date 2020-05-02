package io.github.mcalphadev.api.worldgen;

import io.github.mcalphadev.impl.TerrainDecorateEventType;
import io.github.mcalphadev.impl.TerrainGenerateEventType;

public final class WorldGenEvents {
	private WorldGenEvents() {
	}

	public static final TerrainGenerateEventType SHAPE_CHUNK = new TerrainGenerateEventType("devtools:shape_chunk");
	public static final TerrainGenerateEventType REPLACE_BLOCKS = new TerrainGenerateEventType("devtools:replace_blocks");
	public static final TerrainDecorateEventType TERRAIN_DECORATE = new TerrainDecorateEventType("devtools:terrain_decorate");
}
