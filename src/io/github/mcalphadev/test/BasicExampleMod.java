package io.github.mcalphadev.test;

import java.util.Random;

import io.github.mcalphadev.api.Tiles;
import io.github.mcalphadev.api.game.RecipeManager;
import io.github.mcalphadev.api.worldgen.TerrainGenerateEventCallback;
import io.github.mcalphadev.api.worldgen.WorldGenEvents;
import io.github.mcalphadev.loader.api.Initialiser;
import io.github.mcalphadev.loader.api.LoadEvent;
import io.github.mcalphadev.loader.api.Mod;
import io.github.mcalphadev.log.Logger;
import net.minecraft.game.item.ItemInstance;
import net.minecraft.game.item.ItemType;
import net.minecraft.game.tile.Tile;
import net.minecraft.game.tile.TileMaterial;

@Mod("example")
public class BasicExampleMod implements TerrainGenerateEventCallback {
	@Initialiser(LoadEvent.INIT)
	public static void onInitialise() {
		new Logger("ExampleMod").info("This line was printed by an example mod");

		WorldGenEvents.REPLACE_BLOCKS.addEventSubscriber(new BasicExampleMod());
		RecipeManager.addShapedRecipe(new ItemInstance(ItemType.DIAMOND, 1), "#", "#", '#', Tile.DIRT);

		//tile = new ExampleTile(Tiles.getNextId(), TileMaterial.ROCK);
		System.out.println("Received Tile Id: " + tile.id);

		RecipeManager.addShapedRecipe(new ItemInstance(tile.item), "#", '#', Tile.DIRT);
	}

	private static ExampleTile tile;

	private static int getBlockArrayIndex(int localX, int y, int localZ) {
		return (localX * 16 + localZ) * 128 + y;
	}

	@Override
	public void modifyTerrain(byte[] chunkBlocks, Random rand, int chunkX, int chunkZ) {
		for (int localX = 0; localX < 16; ++localX) {
			for (int localZ = 0; localZ < 16; ++localZ) {
				final int y = 125;
				chunkBlocks[getBlockArrayIndex(localX, y, localZ)] = (byte) Tile.GLASS.id;
			}
		}
	}
}
