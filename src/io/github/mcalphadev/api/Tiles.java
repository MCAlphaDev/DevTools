package io.github.mcalphadev.api;

import io.github.mcalphadev.impl.Remapper;
import net.minecraft.game.item.ItemType;
import net.minecraft.game.item.TileItem;
import net.minecraft.game.tile.Tile;

public final class Tiles {
	private static int nextId = 1; // 0 is air

	/**
	 * Registers the tile to the remapper.
	 * @return the tile passed in.
	 */
	public static <T extends Tile> T register(T tile, String name) {
		return register(tile, new NamespacedId(name));
	}

	/**
	 * Registers the tile to the remapper.
	 * @return the tile passed in.
	 */
	public static <T extends Tile> T register(T tile, NamespacedId name) {
		Remapper.register(tile, name);
		return tile;
	}

	public static int getNextId() {
		while (Tile.TILE_LOOKUP[nextId] != null) {
			++nextId; // I could make it a one liner
			// but you need content in the method body or optimisers complain.
		}

		return nextId;
	}

	public static void addItemType(Tile tile) {
		ItemType.itemLookup[tile.id] = new TileItem(tile.id - 256);
	}
}
