package io.github.mcalphadev.api;

public final class NamespacedId {
	private final String namespace;
	private final String id;

	public NamespacedId(String namespace, String id) {
		this.namespace = namespace;
		this.id = id;
	}

	public NamespacedId(String stringForm) {
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
		} else if (other instanceof NamespacedId) {
			NamespacedId otherId = (NamespacedId) other;
			return otherId.id.equals(this.id) && otherId.namespace.equals(this.namespace);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		int result = 5;
		result = 29 * result + this.namespace.hashCode();
		result = 29 * result + this.id.hashCode();
		return result;
	}
}
