package io.github.mcalphadev.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import io.github.mcalphadev.impl.RecipeResultSetter;
import net.minecraft.game.item.ItemInstance;
import net.minecraft.game.recipe.ShapedRecipe;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin implements RecipeResultSetter {
	@Shadow
	@Final
	@Mutable
	private int resultId;

	@Shadow
	private ItemInstance result;

	@Override
	public void setResult(ItemInstance result) {
		this.result = result;
		this.resultId = result.id;
	}
}
