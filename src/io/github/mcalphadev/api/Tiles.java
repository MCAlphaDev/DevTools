package io.github.mcalphadev.api;

import io.github.mcalphadev.impl.IdProviders;
import io.github.mcalphadev.impl.Remapper;
import net.minecraft.game.item.ItemType;
import net.minecraft.game.item.TileItem;
import net.minecraft.game.tile.Tile;

public final class Tiles {

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

	/**
	 * @return the next available tile id.
	 */
	public static int getNextId() {
		return IdProviders.tile();
	}

	public static void addItemType(Tile tile) {
		ItemType.itemLookup[tile.id] = new TileItem(tile.id - 256);
	}
}
