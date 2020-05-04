package io.github.mcalphadev.impl;

import net.minecraft.game.item.ItemType;
import net.minecraft.game.tile.Tile;

public class IdProviders {
	static int itemTypeId = 0;
	static int tileId = 1; // 0 is air

	public static int itemType() {
		while (ItemType.itemLookup[itemTypeId + 256] != null) {
			++itemTypeId; // I could make it a one liner
			// but you need content in the method body or optimisers complain.
		}

		return itemTypeId;
	}

	public static int tile() {
		while (Tile.TILE_LOOKUP[tileId] != null) {
			++tileId; // I could make it a one liner
			// but you need content in the method body or optimisers complain.
		}

		return tileId;
	}
}
