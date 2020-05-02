package io.github.mcalphadev.api;

import net.minecraft.game.item.ItemType;
import net.minecraft.game.tile.Tile;

public final class Tiles {
	private static int nextId = 1; // 0 is air

	public static int getNextId() {
		while (Tile.TILE_LOOKUP[nextId] != null) {
			++nextId; // I could make it a one liner
			// but you need content in the method body or optimisers complain.
		}

		return nextId;
	}

	public static void addItemType(Tile tile) {
		ItemType.itemLookup[tile.id] = new av(tile.id - 256);
	}
}
