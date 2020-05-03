package io.github.mcalphadev.api;

import io.github.mcalphadev.impl.Remapper;
import net.minecraft.game.item.ItemType;

public final class Items {
	private static int nextId = 0;

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
		while (ItemType.itemLookup[nextId + 256] != null) {
			++nextId; // I could make it a one liner
			// but you need content in the method body or optimisers complain.
		}

		return nextId;
	}
}
