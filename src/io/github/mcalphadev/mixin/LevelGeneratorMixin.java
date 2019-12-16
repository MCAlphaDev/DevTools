package io.github.mcalphadev.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.mcalphadev.api.worldgen.TerrainDecorateEvent;
import io.github.mcalphadev.api.worldgen.WorldGenEvents;
import net.minecraft.level.Level;
import net.minecraft.level.gen.ILevelGenerator;
import net.minecraft.level.gen.LevelGenerator;

@Mixin(LevelGenerator.class)
public class LevelGeneratorMixin {
	@Shadow
	private Random rand;

	@Shadow
	private Level level;

	@Inject(at = @At(value = "HEAD", shift = At.Shift.AFTER), method = "decorate")
	private void injectDecorateEvent(final ILevelGenerator levelGenerator, final int chunkX, final int chunkZ, CallbackInfo info) {
		this.rand.setSeed(this.level.seed);
		this.rand.setSeed(chunkX * (this.rand.nextLong() / 2L * 2L + 1L) + chunkZ * (this.rand.nextLong() / 2L * 2L + 1L) ^ this.level.seed);

		// Alpha minecraft converts chunk coords to block coords with n * 16
		WorldGenEvents.TERRAIN_DECORATE.post(new TerrainDecorateEvent(this.level, this.rand, chunkX * 16, chunkZ * 16)); 
	}
}
