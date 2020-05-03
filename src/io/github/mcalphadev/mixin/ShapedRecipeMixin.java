package io.github.mcalphadev.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import io.github.mcalphadev.impl.IdSetter;
import net.minecraft.game.recipe.ShapedRecipe;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin implements IdSetter {
	@Shadow
	@Final
	@Mutable
	private int resultId;

	@Override
	public void setNewId(int newId) {
		this.resultId = newId;
	}
}
