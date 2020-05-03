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
import io.github.mcalphadev.loader.AlphaModLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.data.AbstractTag;
import net.minecraft.data.CompoundTag;
import net.minecraft.game.item.ItemType;
import net.minecraft.game.tile.Tile;

public class Remapper {
	private static final LinkedHashMap<NamespacedId, Tile> MODDED_TILES = new LinkedHashMap<>();
	private static final Set<Tile> VANILLA_TILES = new HashSet<>();

	public static void register(Tile tile, NamespacedId remapperId) {
		MODDED_TILES.put(remapperId, tile);
	}

	public static void remapFor(String s) {
		File file;
		(file = new File(new File(Minecraft.b(), "saves"), s)).mkdirs();
		try {
			if(!(file = new File(file, "registryMap.nbt")).createNewFile()) {
				AlphaModLoader.getInstance().getLog().info("Reading Registry Map");

				// remap from registry
				try (DataInputStream reader = new DataInputStream(new FileInputStream(file))) {
					CompoundTag data = (CompoundTag) AbstractTag.readTag(reader);

					// Remap Tiles
					CompoundTag tiles = data.getCompoundTag("tiles");
					List<Tile> unMapped = new ArrayList<>();
					Tile[] newIds = new Tile[256];
					ItemType[] items = new ItemType[256];

					// vanilla ids
					for (Tile t : VANILLA_TILES) {
						newIds[t.id] = t;
						items[t.id] = ItemType.itemLookup[t.id];
					}

					// modded ids
					for (Entry<NamespacedId, Tile> entry : MODDED_TILES.entrySet()) {
						String key = entry.getKey().toString();

						if (tiles.containsKey(key)) {
							Tile tile = entry.getValue();
							ItemType itemType = ItemType.itemLookup[tile.id];

							int newId = tiles.getInt(key);
							newIds[newId] = tile;
							items[newId] = itemType;
							((IdSetter) tile).setNewId(newId);
							((IdSetter) itemType).setNewId(newId);
						} else {
							unMapped.add(entry.getValue());
						}
					}

					// new modded ids (not already in registry map)
					if (!unMapped.isEmpty()) {
						// 0 is null for air.
						for (int i = 1; i < 256; ++i) {
							if (newIds[i] == null) {
								newIds[i] = unMapped.remove(0);

								if (unMapped.isEmpty()) {
									break;
								}
							}
						}
					}

					// write to original arrays
					System.arraycopy(newIds, 0, Tile.TILE_LOOKUP, 0, 256);
					System.arraycopy(items, 0, ItemType.itemLookup, 0, 256);
				}
			}

			// create new registry map and save it to the file (do this every time for updated ids)
			AlphaModLoader.getInstance().getLog().info("Writing Registry Map");

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

		// add tile ids to registry
		MODDED_TILES.forEach((id, tile) -> {
			tiles.add(id.toString(), tile.id);
		});

		registryMap.add("tiles", tiles);

		return registryMap;
	}

	static {
		Class<?> tcls = Tile.class;

		for (Field f : Tile.class.getFields()) {
			if (Modifier.isStatic(f.getModifiers())) {
				if (tcls.isAssignableFrom(f.getType())) {
					try {
						VANILLA_TILES.add((Tile) f.get(null));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						throw new RuntimeException("[DevTools] Unable to find id of vanilla tile for the remapper");
					}
				}
			}
		}
	}
}
