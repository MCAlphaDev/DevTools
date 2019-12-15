package io.github.mcalphadev.test;

import io.github.mcalphadev.loader.api.Initialiser;
import io.github.mcalphadev.loader.api.LoadEvent;
import io.github.mcalphadev.loader.api.Mod;
import io.github.mcalphadev.log.Logger;

@Mod("example")
public class BasicExampleMod {
	@Initialiser(LoadEvent.INIT)
	public static void onInitialize() {
		new Logger("ExampleMod").info("This line was printed by an example mod");
	}
}
