package io.github.mcalphadev.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.level.Level;

@Mixin(Level.class)
public class LevelMixin {
	@Inject(at = @At("RETURN"), method = "<init>")
	private void onCreate(final String name, CallbackInfo info) {
		System.out.println("[Test] This constructor is used!");
	}
}
