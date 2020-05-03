package io.github.mcalphadev.api.game;

import io.github.mcalphadev.mixin.ShapedRecipesAccessor;
import net.minecraft.game.item.ItemInstance;
import net.minecraft.game.recipe.ShapedRecipes;

public final class RecipeManager {
	private RecipeManager() {
	}

	public static void addShapedRecipe(final ItemInstance result, final Object... recipe) {
		((ShapedRecipesAccessor) ShapedRecipes.getInstance()).addShapedRecipe(result, recipe);
	}
}
