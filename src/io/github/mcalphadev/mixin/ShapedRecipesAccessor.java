package io.github.mcalphadev.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.game.item.ItemInstance;
import net.minecraft.game.recipe.ShapedRecipe;
import net.minecraft.game.recipe.ShapedRecipes;

@Mixin(ShapedRecipes.class)
public interface ShapedRecipesAccessor {
	@Invoker("addRecipe")
	void addShapedRecipe(final ItemInstance itemInstance, final Object[] array);

	@Accessor("recipes")
	List<ShapedRecipe> getRecipes();
}
