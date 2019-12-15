package io.github.mcalphadev.api;

public final class NamespacedIdentifier {
	private final String namespace;
	private final String id;

	public NamespacedIdentifier(String namespace, String id) {
		this.namespace = namespace;
		this.id = id;
	}

	public NamespacedIdentifier(String stringForm) {
		String[] strs = stringForm.split(" ");

		if (strs.length == 0) {
			throw new RuntimeException("Illegal String Identifier! " + stringForm);
		} else if (strs.length == 1) {
			this.namespace = "minecraft";
			this.id = strs[0].trim();
		} else {
			this.namespace = strs[0].trim();
			this.id = strs[1].trim();
		}
	}

	public String getNamespace() {
		return this.namespace;
	}

	public String getIdentifier() {
		return this.id;
	}

	@Override
	public String toString() {
		return this.namespace + ":" + this.id;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof String) {
			return other.equals(this.toString());
		} else if (other instanceof NamespacedIdentifier) {
			NamespacedIdentifier otherId = (NamespacedIdentifier) other;
			return otherId.id.equals(this.id) && otherId.namespace.equals(this.namespace);
		} else {
			return false;
		}
	}
}
