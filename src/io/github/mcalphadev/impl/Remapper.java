package io.github.mcalphadev.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

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
	private static final Set<Tile> VANILLA_TILES = new HashSet<>();
	private static final Set<ItemType> VANILLA_ITEM_TYPES = new HashSet<>();

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
						Tile[] newTileIds = new Tile[256];
						ItemType[] tileItems = new ItemType[256];

						// vanilla ids
						for (Tile t : VANILLA_TILES) {
							newTileIds[t.id] = t;
							tileItems[t.id] = ItemType.itemLookup[t.id];
						}

						// modded ids
						for (Entry<NamespacedId, Tile> entry : MODDED_TILES.entrySet()) {
							String key = entry.getKey().toString();

							if (tiles.containsKey(key)) {
								Tile tile = entry.getValue();
								ItemType itemType = ItemType.itemLookup[tile.id];

								int newId = tiles.getInt(key);
								newTileIds[newId] = tile;
								tileItems[newId] = itemType;
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
									newTileIds[i] = unMappedTiles.remove(0);

									if (unMappedTiles.isEmpty()) {
										break;
									}
								}
							}
						}

						// write to original arrays
						System.arraycopy(newTileIds, 0, Tile.TILE_LOOKUP, 0, 256);
						System.arraycopy(tileItems, 0, ItemType.itemLookup, 0, 256);
					}

					// Remap Item Types
					CompoundTag itemTypes = data.getCompoundTag("itemtypes");

					if (itemTypes != null) {
						AlphaModApi.LOGGER.info("Remapping Item Types");
						List<ItemType> unMappedItemTypes = new ArrayList<>();
						ItemType[] newItemTypeIds = new ItemType[32000 - 256];

						// vanilla ids
						for (ItemType i : VANILLA_ITEM_TYPES) {
							newItemTypeIds[i.id - 256] = i;
						}

						// modded ids
						for (Entry<NamespacedId, ItemType> entry : MODDED_ITEM_TYPES.entrySet()) {
							String key = entry.getKey().toString();

							if (itemTypes.containsKey(key)) {
								ItemType itemType = entry.getValue();

								int newId = itemTypes.getInt(key);
								newItemTypeIds[newId - 256] = itemType;
								((IdSetter) itemType).setNewId(newId);
							} else {
								unMappedItemTypes.add(entry.getValue());
							}
						}

						// new modded ids (not already in registry map)
						int i = 0;
						int max = newItemTypeIds.length;

						while (!unMappedItemTypes.isEmpty()) {
							if (newItemTypeIds[i] == null) {
								newItemTypeIds[i] = unMappedItemTypes.remove(0);
							}

							if (++i > max) {
								break;
							}
						}

						// Write to original array
						System.arraycopy(newItemTypeIds, 0, ItemType.itemLookup, 256, newItemTypeIds.length);
					}

					// Remap Recipes
					AlphaModApi.LOGGER.info("Remapping Recipes");

					for (ShapedRecipe recipe : ((ShapedRecipesAccessor) ShapedRecipes.getInstance()).getRecipes()) {
						// remap recipe input ids
						int[] recipeIds = ((ShapedRecipeAccessor) recipe).getRecipe();

						for (int i = 0; i < recipeIds.length; ++i) {
							if (i != 0) {
								recipeIds[i] = oldItemTypes[i].id;
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

	static {
		// tiles
		Class<?> tcls = Tile.class;

		for (Field f : Tile.class.getFields()) {
			if (Modifier.isStatic(f.getModifiers())) {
				if (tcls.isAssignableFrom(f.getType())) {
					try {
						Tile t = (Tile) f.get(null);

						if (t != null) {
							VANILLA_TILES.add(t);
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						throw new RuntimeException("[DevTools] Unable to access a vanilla tile for the remapper", e);
					}
				}
			}
		}

		// item types
		Class<?> icls = ItemType.class;

		for (Field f : ItemType.class.getFields()) {
			if (Modifier.isStatic(f.getModifiers())) {
				if (icls.isAssignableFrom(f.getType())) {
					try {
						ItemType t = (ItemType) f.get(null);

						if (t != null) {
							VANILLA_ITEM_TYPES.add(t);
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						throw new RuntimeException("[DevTools] Unable to access a vanilla item type for the remapper", e);
					}
				}
			}
		}
	}
}
