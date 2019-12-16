package io.github.mcalphadev.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.game.item.ItemInstance;
import net.minecraft.game.recipe.ShapedRecipes;

@Mixin(ShapedRecipes.class)
public interface ShapedRecipesInvoker {
	@Invoker("addRecipe")
	void addRecipe(final ItemInstance itemInstance, final Object[] array);
}
