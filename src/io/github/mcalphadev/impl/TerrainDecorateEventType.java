package io.github.mcalphadev.impl;

import java.util.Random;

import io.github.mcalphadev.api.event.EventType;
import io.github.mcalphadev.api.worldgen.TerrainDecorateEvent;
import net.minecraft.level.Level;

public final class TerrainDecorateEventType extends EventType<TerrainDecorateEvent> {
	public TerrainDecorateEventType(String name) {
		super(name);
	}
	
	public void post(Level level, Random rand, int x, int z) {
		this.updateEventSubscribers();
		this.subscribers.forEach(event -> event.decorate(level, rand, x, z));
	}
}
