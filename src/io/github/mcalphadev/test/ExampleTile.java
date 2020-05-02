package io.github.mcalphadev.test;

import io.github.mcalphadev.api.Items;
import net.minecraft.game.item.BlockItem;
import net.minecraft.game.item.ItemType;
import net.minecraft.game.tile.Tile;
import net.minecraft.game.tile.TileMaterial;

// this breaks
public class ExampleTile extends Tile {
	public ExampleTile(int id, TileMaterial material) {
		super(id, material);
		this.item = new BlockItem(Items.getNextId(), 0, 0, 0).a(id);
	}

	public final ItemType item;
}
