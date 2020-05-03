package io.github.mcalphadev.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.mcalphadev.impl.Remapper;
import io.github.mcalphadev.loader.AlphaModLoader;
import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Inject(at = @At(value = "CONSTANT", args = "stringValue=Startup"), method = "initialise")
	private void injectModInit(CallbackInfo info) {
		AlphaModLoader.getInstance().loadMods(); // load mods later because mixin load order problems
		AlphaModLoader.getInstance().initialise();
	}

	@Inject(at = @At(value = "CONSTANT", args = "stringValue=Post startup"), method = "initialise")
	private void injectModPosInit(CallbackInfo info) {
		AlphaModLoader.getInstance().postInitialise();
	}

	// old: <init>(Ljava/io/File;Ljava/lang/String;J)V
	// could use it if I figured out how NEW works tbh
	@Inject(at = @At(value = "INVOKE", target = "net/minecraft/client/Minecraft.b()Ljava/io/File;"), method = "loadLevel")
	private void loadLevel(final String s, CallbackInfo info) {
		Remapper.remapFor(s);
	}
}
