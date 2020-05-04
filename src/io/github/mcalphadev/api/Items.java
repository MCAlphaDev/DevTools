package io.github.mcalphadev.api;

import io.github.mcalphadev.impl.IdProviders;
import io.github.mcalphadev.impl.Remapper;
import net.minecraft.game.item.ItemType;

public final class Items {

	/**
	 * Registers the item type to the remapper.
	 * @return the item type passed in.
	 */
	public static <T extends ItemType> T register(T itemType, String name) {
		return register(itemType, new NamespacedId(name));
	}

	/**
	 * Registers the item type to the remapper.
	 * @return the item type passed in.
	 */
	public static <T extends ItemType> T register(T itemType, NamespacedId name) {
		Remapper.register(itemType, name);
		return itemType;
	}

	/**
	 * @return the next available item type id.
	 */
	public static int getNextId() {
		return IdProviders.itemType();
	}
}
