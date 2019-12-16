package io.github.mcalphadev.api.event;

import java.util.List;

import com.google.common.collect.Lists;

import io.github.mcalphadev.api.NamespacedIdentifier;
import io.github.mcalphadev.impl.EventsImpl;

public abstract class EventType<T> {
	private final NamespacedIdentifier name;
	private final List<T> toAdd = Lists.newArrayList();
	private boolean flag = false;

	public EventType(String name) {
		this(new NamespacedIdentifier(name));
	}

	protected final List<T> subscribers = Lists.newArrayList();

	public EventType(NamespacedIdentifier name) {
		this.name = name;

		EventsImpl.register(name, this);
	}

	/**
	 * Call this at the beginning of your post method
	 */
	protected void updateEventSubscribers() {
		if (this.flag) {
			this.toAdd.forEach(s -> this.subscribers.add(s));
			this.toAdd.clear();
			this.flag = false;
		}
	}

	public void addEventSubscriber(T subscriber) {
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
}
