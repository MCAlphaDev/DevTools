package io.github.mcalphadev.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.game.item.ItemInstance;
import net.minecraft.game.recipe.ShapedRecipe;

@Mixin(ShapedRecipe.class)
public interface ShapedRecipeAccessor {
	@Accessor("recipe")
	int[] getRecipe();
	
	@Accessor("result")
	ItemInstance getResult();
}
