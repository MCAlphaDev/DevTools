package io.github.mcalphadev.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import io.github.mcalphadev.api.NamespacedId;
import io.github.mcalphadev.mixin.ShapedRecipeAccessor;
import io.github.mcalphadev.mixin.ShapedRecipesAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.data.AbstractTag;
import net.minecraft.data.CompoundTag;
import net.minecraft.game.item.ItemType;
import net.minecraft.game.recipe.ShapedRecipe;
import net.minecraft.game.recipe.ShapedRecipes;
import net.minecraft.game.tile.Tile;

public class Remapper {
	private static final LinkedHashMap<NamespacedId, Tile> MODDED_TILES = new LinkedHashMap<>();
	private static final LinkedHashMap<NamespacedId, ItemType> MODDED_ITEM_TYPES = new LinkedHashMap<>();

	public static void register(Tile tile, NamespacedId remapperId) {
		MODDED_TILES.put(remapperId, tile);
	}

	public static void register(ItemType tile, NamespacedId remapperId) {
		MODDED_ITEM_TYPES.put(remapperId, tile);
	}

	public static void remapFor(String s) {
		File file;
		(file = new File(new File(Minecraft.b(), "saves"), s)).mkdirs();
		try {
			if(!(file = new File(file, "registryMap.nbt")).createNewFile()) {
				AlphaModApi.LOGGER.info("Reading Registry Map");

				// remap from registry
				try (DataInputStream reader = new DataInputStream(new FileInputStream(file))) {
					CompoundTag data = (CompoundTag) AbstractTag.readTag(reader);

					// Remap Tiles
					CompoundTag tiles = data.getCompoundTag("tiles");
					ItemType[] oldItemTypes = new ItemType[32000];
					System.arraycopy(ItemType.itemLookup, 0, oldItemTypes, 0, 32000);

					if (tiles != null) {
						AlphaModApi.LOGGER.info("Remapping Tiles");
						List<Tile> unMappedTiles = new ArrayList<>();
						Tile[] newTileIds = IdProviders.vanillaTiles();
						ItemType[] newItemTypeIds = IdProviders.vanillaItemTypes();

						// modded ids
						for (Entry<NamespacedId, Tile> entry : MODDED_TILES.entrySet()) {
							String key = entry.getKey().toString();

							if (tiles.containsKey(key)) {
								Tile tile = entry.getValue();
								ItemType itemType = ItemType.itemLookup[tile.id];

								int newId = tiles.getInt(key);
								newTileIds[newId] = tile;
								newItemTypeIds[newId] = itemType;
								AlphaModApi.LOGGER.debug("[Remapper/Tile] Old:New " + tile.id + ":" + newId);
								((IdSetter) tile).setNewId(newId);
								((IdSetter) itemType).setNewId(newId);
							} else {
								unMappedTiles.add(entry.getValue());
							}
						}

						// new modded ids (not already in registry map)
						if (!unMappedTiles.isEmpty()) {
							// 0 is null for air.
							for (int i = 1; i < 256; ++i) {
								if (newTileIds[i] == null) {
									Tile tile = unMappedTiles.remove(0); // next tile
									AlphaModApi.LOGGER.debug("[Remapper/Tile] Adding new entry at: " + tile.id);
									newTileIds[i] = tile;
									newItemTypeIds[i] = ItemType.itemLookup[tile.id]; // set item id for tile
									((IdSetter) newTileIds[i]).setNewId(i);
									((IdSetter) newItemTypeIds[i]).setNewId(i);

									if (unMappedTiles.isEmpty()) {
										break;
									}
								}
							}
						}

						// write to original tile array
						System.arraycopy(newTileIds, 0, Tile.TILE_LOOKUP, 0, 256);

						// Remap Item Types
						CompoundTag itemTypes = data.getCompoundTag("itemtypes");

						AlphaModApi.LOGGER.info("Remapping Item Types");
						List<ItemType> unMappedItemTypes = new ArrayList<>();

						// modded ids
						for (Entry<NamespacedId, ItemType> entry : MODDED_ITEM_TYPES.entrySet()) {
							String key = entry.getKey().toString();

							if (itemTypes.containsKey(key)) {
								ItemType itemType = entry.getValue();

								int newId = itemTypes.getInt(key);
								newItemTypeIds[newId] = itemType;
								AlphaModApi.LOGGER.debug("[Remapper/ItemType] Old:New " + itemType.id + ":" + newId);
								((IdSetter) itemType).setNewId(newId);
							} else {
								unMappedItemTypes.add(entry.getValue());
							}
						}

						// new modded ids (not already in registry map)
						int i = 257;
						int max = newItemTypeIds.length;

						while (!unMappedItemTypes.isEmpty()) {
							if (newItemTypeIds[i] == null) {
								newItemTypeIds[i] = unMappedItemTypes.remove(0);
								((IdSetter) newItemTypeIds[i]).setNewId(i);
							}

							if (++i > max) {
								break;
							}
						}

						// Write to original array
						System.arraycopy(newItemTypeIds, 0, ItemType.itemLookup, 0, newItemTypeIds.length);
					}

					// Remap Recipes
					AlphaModApi.LOGGER.info("Remapping Recipes");

					for (ShapedRecipe recipe : ((ShapedRecipesAccessor) ShapedRecipes.getInstance()).getRecipes()) {
						// remap recipe input ids
						int[] recipeIds = ((ShapedRecipeAccessor) recipe).getRecipe();

						for (int i = 0; i < recipeIds.length; ++i) {
							int oldId = recipeIds[i];

							if (oldId > 0) {
								int newId = oldItemTypes[oldId].id;

								if (oldId == newId) {
									continue;
								}

								AlphaModApi.LOGGER.debug("[Remapper/Recipe] Old:New " + oldId + ":" + newId);
								recipeIds[i] = newId;
							}
						}

						// recompute output id
						((IdSetter) recipe).setNewId(((ShapedRecipeAccessor) recipe).getResult().id);
					}
				}
			}

			// create new registry map and save it to the file (do this every time for updated ids)
			AlphaModApi.LOGGER.info("Writing Registry Map");

			try (DataOutputStream writer = new DataOutputStream(new FileOutputStream(file))) {
				AbstractTag.writeTag(createRegistryMap(), writer);
			}
		} catch (IOException e) {
			throw new RuntimeException("[DevTools] Error while running registry remapper", e);
		}
	}

	private static CompoundTag createRegistryMap() {
		CompoundTag registryMap = new CompoundTag();
		CompoundTag tiles = new CompoundTag();
		CompoundTag itemTypes = new CompoundTag();

		AlphaModApi.LOGGER.info("Adding tile ids to registry map");
		// add tile ids to registry
		MODDED_TILES.forEach((id, tile) -> {
			tiles.add(id.toString(), tile.id);
		});

		AlphaModApi.LOGGER.info("Adding item ids to registry map");
		// add item type ids to registry
		MODDED_ITEM_TYPES.forEach((id, itemtype) -> {
			itemTypes.add(id.toString(), itemtype.id);
		});

		registryMap.add("tiles", tiles);
		registryMap.add("itemtypes", itemTypes);

		return registryMap;
	}
}
