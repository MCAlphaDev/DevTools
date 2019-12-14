package io.github.mcalphadev.launcher;

import net.minecraft.launchwrapper.IClassTransformer;

public class ClientClassTransformer implements IClassTransformer {
	@Override
	public byte[] transform(String name, String transformedName, byte[] probablyOpcodes) {
		return probablyOpcodes;
	}
}
