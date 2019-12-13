package io.github.mcalphadev.launcher;

import org.spongepowered.asm.launch.MixinBootstrap;

public class AlphaModLauncher {
	private AlphaModLauncher() {
	}
	
	public static void main(String[] args) {
		MixinBootstrap.init();
	}
}
