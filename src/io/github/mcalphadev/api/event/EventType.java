package io.github.mcalphadev.api.event;

import java.util.List;

import com.google.common.collect.Lists;

import io.github.mcalphadev.api.NamespacedIdentifier;
import io.github.mcalphadev.impl.EventsImpl;

public final class EventType<T extends Event> {
	private final NamespacedIdentifier name;
	private final List<Subscriber<T>> subscribers = Lists.newArrayList();
	private final List<Subscriber<T>> toAdd = Lists.newArrayList();
	private boolean flag = false;

	public EventType(String name) {
		this(new NamespacedIdentifier(name));
	}
	
	public EventType(NamespacedIdentifier name) {
		this.name = name;

		EventsImpl.register(name, this);
	}

	public void post(T event) {
		if (this.flag) {
			this.toAdd.forEach(s -> this.subscribers.add(s));
			this.toAdd.clear();
			this.flag = false;
		}

		this.subscribers.forEach(s -> s.onReceiveEvent(event));
	}

	public void addEventSubscriber(Subscriber<T> subscriber) {
		this.flag = true;
		this.toAdd.add(subscriber);
	}

	public NamespacedIdentifier getId() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name.toString();
	}

	public static EventType<?> getTypeForId(NamespacedIdentifier id) {
		return EventsImpl.getEventType(id);
	}

	public static interface Subscriber<T extends Event> {
		void onReceiveEvent(T event);
	}
}
