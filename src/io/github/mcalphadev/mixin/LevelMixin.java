package io.github.mcalphadev.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.level.Level;

@Mixin(Level.class)
/**
 * Was here for testing
 */
public class LevelMixin {
	/*@Inject(at = @At("RETURN"), method = "<init>(Ljava/lang/String;)V")
	private void onCreate(final String name, CallbackInfo info) {
		System.out.println("[Test] This constructor is used!"); // not called, since server
	}

	@Inject(at = @At("RETURN"), method = "<init>(Ljava/io/File;Ljava/lang/String;J)V")
	private void onCreate(final File f, final String name, final long seed, CallbackInfo info) {
		// this one is called
	}*/
}
