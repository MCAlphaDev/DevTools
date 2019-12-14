package io.github.mcalphadev.mixin;

import java.io.File;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.launchwrapper.Launch;

@Mixin(targets = "bf")
public abstract class SoundThreadMixin {
	@Inject(method = "<init>", at = @At("RETURN"), remap = false)
	public void init(CallbackInfo callback) {
		a(); //Force a resource reload to add our predownloaded assets
	}

	@Inject(method = "run()V", at = @At("HEAD"), remap = false, cancellable = true)
	public void dontRun(CallbackInfo callback) {
		callback.cancel(); //Avoid trying (and loudly failing) to download sound stuff 
	}

	@Overwrite
	public void a() {
		for (File file : Launch.assetsDir.listFiles()) {
			if (file.isDirectory()) {
				a(file, file.getName() + '/');
			}
		}
	}

	@Shadow
	public abstract void a(File file, String prefix);
}