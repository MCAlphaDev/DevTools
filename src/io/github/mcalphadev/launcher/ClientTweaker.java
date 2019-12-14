package io.github.mcalphadev.launcher;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;

import net.minecraft.launchwrapper.AlphaVanillaTweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

/**
 * Main tweaker, run with...
 * 
 * <ul>
 * 	<li>Main class: net.minecraft.launchwrapper.Launch
 * 	<li>Program args: --tweakClass io.github.mcalphadev.launcher.ClientTweaker
 * </ul>
 *
 * Optionally --gameDir "${workspace_loc:DevTools/run}" for not running in .minecraft
 */
public class ClientTweaker extends AlphaVanillaTweaker {
	@Override
	public void injectIntoClassLoader(LaunchClassLoader classloader) {
		super.injectIntoClassLoader(classloader);

		classloader.addClassLoaderExclusion("org.objectweb.asm.");
		classloader.addClassLoaderExclusion("org.spongepowered.asm.");
		classloader.addClassLoaderExclusion("io.github.mcalphadev.launcher.");
		
		// TODO load stuff here

		classloader.registerTransformer("io.github.mcalphadev.launcher.ClientClassTransformer");
		
		// Mixin
		MixinBootstrap.init();
		MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
	}

}
