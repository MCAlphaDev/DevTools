package io.github.mcalphadev.api;

import net.minecraft.game.item.ItemType;

public final class Items {
	private static int nextId = 0;

	public static int getNextId() {
		while (ItemType.itemLookup[nextId + 256] != null) {
			++nextId; // I could make it a one liner
			// but you need content in the method body or optimisers complain.
		}

		return nextId;
	}
}
