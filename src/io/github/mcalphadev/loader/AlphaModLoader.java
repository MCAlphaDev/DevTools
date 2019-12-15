package io.github.mcalphadev.loader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;

import org.apache.logging.log4j.core.util.Loader;

import com.google.gson.Gson;

import io.github.mcalphadev.log.Logger;
import net.minecraft.launchwrapper.Launch;

public class AlphaModLoader {
	private final Logger log;
	private final File modsFolder;

	public AlphaModLoader() {
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
	
	public AlphaModLoader load() throws IOException {
		Enumeration<URL> modInfos = findAllAddons();
		
		while (modInfos.hasMoreElements()) {
			URL modInfo = modInfos.nextElement();
			try (FileReader reader = new FileReader(modInfo.toString())) {
				// TODO finish this
				ModJson abstractModJson = GSON.fromJson(modInfo, ModJson.class);
			}
		}
		
		return this;
	}

	public AlphaModLoader getInstance() {
		return instance;
	}

	private static final Gson GSON = new Gson();
	private static AlphaModLoader instance;
}
