package io.github.mcalphadev.api.event;

import java.util.List;

import com.google.common.collect.Lists;

import io.github.mcalphadev.api.NamespacedId;
import io.github.mcalphadev.impl.EventsImpl;

public abstract class EventType<T> {
	private final NamespacedId name;
	private final List<T> toAdd = Lists.newArrayList();

	private boolean flag = false;

	public EventType(String name) {
		this(new NamespacedId(name));
	}

	protected final List<T> subscribers = Lists.newArrayList();

	public EventType(NamespacedId name) {
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

	public NamespacedId getId() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name.toString();
	}

	public static EventType<?> getTypeForId(NamespacedId id) {
		return EventsImpl.getEventType(id);
	}
}
