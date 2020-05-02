package io.github.mcalphadev.api;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.game.item.ItemType;
import net.minecraft.game.item.TileItem;
import net.minecraft.game.tile.Tile;

public final class Tiles {
	private static int nextId = 1; // 0 is air
	private static final BiMap<NamespacedId, Tile> REGISTRY = HashBiMap.create();

	public static <T extends Tile> T register(T tile, NamespacedId name) {
		REGISTRY.put(name, tile);
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
