package io.github.mcalphadev.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import io.github.mcalphadev.impl.IdSetter;
import net.minecraft.game.item.ItemType;

@Mixin(ItemType.class)
public class ItemTypeMixin implements IdSetter {
	@Shadow
	@Final
	@Mutable
	private int id;

	@Override
	public void setNewId(int newId) {
		this.id = newId;
	}
}
