package io.github.mcalphadev.launcher;

import java.io.File;
import java.util.List;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class ClientTweaker implements ITweaker {
	private String[] args = new String[0];

	@Override
	public void acceptOptions(List<String> localArgs, File gameDir, File assetsDir, String profile) {
		args = localArgs.toArray(new String[0]);
	}

	@Override
	public String[] getLaunchArguments() {
		return args;
	}

	@Override
	public String getLaunchTarget() {
		return "net.minecraft.client.Minecraft";
	}

	@Override
	public void injectIntoClassLoader(LaunchClassLoader classloader) {
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
