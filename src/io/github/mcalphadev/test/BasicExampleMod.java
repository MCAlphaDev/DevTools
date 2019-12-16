package io.github.mcalphadev.test;

import io.github.mcalphadev.api.worldgen.TerrainGenerateEvent;
import io.github.mcalphadev.api.worldgen.WorldGenEvents;
import io.github.mcalphadev.loader.api.Initialiser;
import io.github.mcalphadev.loader.api.LoadEvent;
import io.github.mcalphadev.loader.api.Mod;
import io.github.mcalphadev.log.Logger;
import net.minecraft.game.tile.Tile;

@Mod("example")
public class BasicExampleMod {
	@Initialiser(LoadEvent.INIT)
	public static void onInitialise() {
		new Logger("ExampleMod").info("This line was printed by an example mod");
		
		WorldGenEvents.REPLACE_BLOCKS.addEventSubscriber(event -> modifyTerrain(event));
	}

	private static void modifyTerrain(TerrainGenerateEvent event) {
		for (int localX = 0; localX < 16; ++localX) {
			for (int localZ = 0; localZ < 16; ++localZ) {
				final int y = 125;
				event.chunkBlocks[getBlockArrayIndex(localX, y, localZ)] = (byte) Tile.GLASS.id;
			}
		}
	}
	
	private static int getBlockArrayIndex(int localX, int y, int localZ) {
		return (localX * 16 + localZ) * 128 + y;
	}
}
