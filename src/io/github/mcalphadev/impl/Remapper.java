package io.github.mcalphadev.impl;

import java.io.File;
import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.data.CompoundTag;

public class Remapper {
	public static void remapFor(String s) {
		File file;
		(file = new File(new File(Minecraft.b(), "saves"), s)).mkdirs();
		try {
			if((file = new File(file, "registryMap.nbt")).createNewFile()) {
				CompoundTag registryMap = new CompoundTag();
				
			} else {
				
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
