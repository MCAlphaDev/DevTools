package io.github.mcalphadev.impl;

import java.util.HashMap;
import java.util.Map;

import io.github.mcalphadev.api.NamespacedIdentifier;
import io.github.mcalphadev.api.event.EventType;

public final class EventsImpl {
	private static final Map<NamespacedIdentifier, EventType<?>> eventMap = new HashMap<>();

	public static void register(NamespacedIdentifier name, EventType<?> eventType) {
		eventMap.put(name, eventType);
	}

	public static EventType<?> getEventType(NamespacedIdentifier id) {
		return eventMap.get(id);
	}
}
