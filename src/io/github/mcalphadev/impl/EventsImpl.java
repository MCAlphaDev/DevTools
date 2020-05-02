package io.github.mcalphadev.impl;

import java.util.HashMap;
import java.util.Map;

import io.github.mcalphadev.api.NamespacedId;
import io.github.mcalphadev.api.event.EventType;

public final class EventsImpl {
	private static final Map<NamespacedId, EventType<?>> eventMap = new HashMap<>();

	public static void register(NamespacedId name, EventType<?> eventType) {
		eventMap.put(name, eventType);
	}

	public static EventType<?> getEventType(NamespacedId id) {
		return eventMap.get(id);
	}
}
