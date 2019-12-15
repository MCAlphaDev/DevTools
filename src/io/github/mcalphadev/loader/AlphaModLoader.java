package io.github.mcalphadev.loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixins;

import com.google.gson.Gson;

import io.github.mcalphadev.loader.api.Initializer;
import io.github.mcalphadev.loader.api.Mod;
import io.github.mcalphadev.log.Logger;
import net.minecraft.launchwrapper.Launch;

public final class AlphaModLoader {
	private final Logger log;
	private final File modsFolder;

	private final List<ModJson.Format0> modJsons = new ArrayList<>();
	private final List<Method> initializers = new ArrayList<>();
	private final Map<String, Class<?>> modClasses = new HashMap<>();

	private AlphaModLoader() {
		log = new Logger("ValLoader").setDebug(true);
		log.debug("Created Loader Successfully.");

		modsFolder = new File("./addons");
		if (!modsFolder.mkdirs()) {
			log.debug("Mods folder could not be created. This is likely because it already exists.");
		} else {
			log.debug("Created mods folder.");
		}

		instance = this;
	}

	private Enumeration<URL> findAllAddons() throws IOException {
		File[] jars = LoaderUtils.findAllJars(modsFolder);
		for (File file : jars) {
			try {
				// add to launchwrapper's classloader
				URL jURL = file.toURI().toURL();
				Launch.classLoader.addURL(jURL);
			} catch (MalformedURLException e) {
				log.debugAll("MalformedUrl: ", e.toString(), " , Skipping file");
			}
		}

		return Launch.classLoader.getResources("mod.json");
	}

	public void loadMods() {
		Enumeration<URL> modInfos;
		try {
			modInfos = findAllAddons();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		while (modInfos.hasMoreElements()) {
			URL modInfo = modInfos.nextElement();
			try (FileReader reader = new FileReader(modInfo.toString())) {
				int format = GSON.fromJson(reader, ModJson.AbstractModJson.class).format;
				switch (format) {
				// if version is older than the latest version, load with compatibility layer converting formats
				case 0:
					modJsons.add(GSON.fromJson(reader, ModJson.Format0.class));
				default:
					throw new RuntimeException("Invalid format version in mod!");
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		modJsons.forEach(json -> loadMod(json.mainClass, json.mixins));
	}

	private void loadMod(String modClass, String mixins) {
		try {
			Class<?> modClazz = Launch.classLoader.loadClass(modClass);
			if (modClazz.isAnnotationPresent(Mod.class)) {
				Mod annotation = modClazz.getAnnotation(Mod.class);
				modClasses.put(annotation.value(), modClazz);

				Method[] methods = modClazz.getMethods();

				for (Method method : methods) {
					if (method.isAnnotationPresent(Initializer.class)) {
						initializers.add(method);
					}
				}
			} else {
				log.warn("Class is not a mod class! " + modClass);
			}
		} catch (ClassNotFoundException e) {
			log.warn("Could not load mod class! " + modClass);
			return;
		}

		Mixins.addConfiguration(mixins);
	}

	public static AlphaModLoader getInstance() {
		return instance == null ? new AlphaModLoader() : instance;
	}

	private static final Gson GSON = new Gson();
	private static AlphaModLoader instance;
}
