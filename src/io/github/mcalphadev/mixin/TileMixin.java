package io.github.mcalphadev.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import io.github.mcalphadev.impl.IdSetter;
import net.minecraft.game.tile.Tile;

@Mixin(Tile.class)
public class TileMixin implements IdSetter {
	@Shadow
	@Final
	@Mutable
	private int id;

	@Override
	public void setNewId(int newId) {
		this.id = newId;
	}
}
