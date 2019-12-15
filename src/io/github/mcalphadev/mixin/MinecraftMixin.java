package io.github.mcalphadev.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.mcalphadev.loader.AlphaModLoader;
import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Inject(at = @At("HEAD"), method = "catchGLError")
	private void injectModInit(String phase, CallbackInfo info) {
		if (phase.endsWith("tup")) {
			if (phase.equals("Startup")) {
				AlphaModLoader.getInstance().initialise();
			} else if (phase.equals("Post startup")) {
				AlphaModLoader.getInstance().postInitialise();
			}
		}
	}
}
