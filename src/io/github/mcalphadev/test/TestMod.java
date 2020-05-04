package io.github.mcalphadev.test;

import java.util.Random;

import io.github.mcalphadev.api.Items;
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
public class TestMod implements TerrainGenerateEventCallback {
	private static Logger logger;

	@Initialiser(LoadEvent.INIT)
	public static void onInitialise() {
		logger = new Logger("ExampleMod");
		logger.info("This line was printed by an example mod");

		WorldGenEvents.REPLACE_BLOCKS.addEventSubscriber(new TestMod());
		RecipeManager.addShapedRecipe(new ItemInstance(ItemType.diamond, 1), "#", "#", '#', Tile.DIRT);

		tile = Tiles.register(new ExampleTile(Tiles.getNextId(), TileMaterial.ROCK), "example:tile");
		logger.info("Received Tile Id: " + tile.id);

		RecipeManager.addShapedRecipe(new ItemInstance(tile), "#", '#', Tile.DIRT);

		addItemType2();
		itemType = Items.register(new ExampleItemType(Items.getNextId()).textureIndex(69), "example:itemtype");
		logger.info("Received Item Type Id: " + itemType.id);

		RecipeManager.addShapedRecipe(new ItemInstance(itemType), "#", '#', tile);
	}

	private static ExampleTile tile;
	private static ItemType itemType;
	private static ItemType itemType2;

	private static void addItemType2() {
		itemType2 = Items.register(new ExampleItemType(Items.getNextId()).textureIndex(2), "example:itemtype2");
		logger.info("Received Item Type 2 Id: " + itemType2.id);
		RecipeManager.addShapedRecipe(new ItemInstance(itemType2, 3), "##", '#', tile);
	}

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
