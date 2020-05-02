package io.github.mcalphadev.test;

import net.minecraft.game.tile.Tile;
import net.minecraft.game.tile.TileMaterial;

// this breaks
public class ExampleTile extends Tile {
	public ExampleTile(int id, TileMaterial material) {
		super(id, 0, material);
		this.setHardness(0.1f);
		this.setResistance(30.0f);
		this.setSound(Tile.SOUND_CLOTH);
	}
}
