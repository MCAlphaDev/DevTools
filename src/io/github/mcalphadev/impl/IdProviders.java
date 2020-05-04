package io.github.mcalphadev.impl;

import java.util.Arrays;

import net.minecraft.game.item.ItemType;
import net.minecraft.game.tile.Tile;

public class IdProviders {
	private static int itemTypeId = 0;
	private static int tileId = 1; // 0 is air
	static final Tile[] vanillaTiles;
	static final ItemType[] vanillaItemTypes;

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

	static ItemType[] vanillaItemTypes() {
		return Arrays.copyOf(vanillaItemTypes, vanillaItemTypes.length);
	}

	static Tile[] vanillaTiles() {
		return Arrays.copyOf(vanillaTiles, vanillaTiles.length);
	}

	static {
		vanillaTiles = Arrays.copyOf(Tile.TILE_LOOKUP, Tile.TILE_LOOKUP.length);
		vanillaItemTypes = Arrays.copyOf(ItemType.itemLookup, ItemType.itemLookup.length);
	}
}
