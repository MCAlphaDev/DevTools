package io.github.mcalphadev.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.mcalphadev.api.worldgen.TerrainGenerateEvent;
import io.github.mcalphadev.api.worldgen.TerrainDecorateEvent;
import io.github.mcalphadev.api.worldgen.WorldGenEvents;
import io.github.mcalphadev.impl.AlphaWorldGenImpl;
import net.minecraft.game.tile.Sand;
import net.minecraft.level.Level;
import net.minecraft.level.gen.ILevelGenerator;
import net.minecraft.level.gen.LevelGenerator;

@Mixin(LevelGenerator.class)
public class LevelGeneratorMixin {
	@Shadow
	private Random rand;

	@Shadow
	private Level level;

	@Inject(at = @At("HEAD"), method = "generateBase")
	private void injectRemoveVanillaChunkShape(final int chunkX, final int chunkZ, final byte[] blocks, CallbackInfo info) {
		if (AlphaWorldGenImpl.cancelVanillaGenBase()) {
			WorldGenEvents.SHAPE_CHUNK.post(new TerrainGenerateEvent(blocks, this.rand, chunkX, chunkZ));
			info.cancel();
		}
	}

	@Inject(at = @At("TAIL"), method = "generateBase")
	private void injectShapeChunkEvent(final int chunkX, final int chunkZ, final byte[] blocks, CallbackInfo info) {
		WorldGenEvents.SHAPE_CHUNK.post(new TerrainGenerateEvent(blocks, this.rand, chunkX, chunkZ));
	}

	@Inject(at = @At("TAIL"), method = "replaceBlocks")
	private void injectReplaceBlocksEvent(final int chunkX, final int chunkZ, final byte[] blocks, CallbackInfo info) {
		WorldGenEvents.REPLACE_BLOCKS.post(new TerrainGenerateEvent(blocks, this.rand, chunkX, chunkZ));
	}

	@Inject(at = @At("HEAD"), method = "decorate")
	private void injectDecorateEvent(final ILevelGenerator levelGenerator, final int chunkX, final int chunkZ, CallbackInfo info) {
		Sand.dontDoFalling = true;
		this.rand.setSeed(this.level.seed);
		this.rand.setSeed(chunkX * (this.rand.nextLong() / 2L * 2L + 1L) + chunkZ * (this.rand.nextLong() / 2L * 2L + 1L) ^ this.level.seed);

		// Alpha minecraft converts chunk coords to block coords with n * 16
		WorldGenEvents.TERRAIN_DECORATE.post(new TerrainDecorateEvent(this.level, this.rand, chunkX * 16, chunkZ * 16)); 
	}
}
