package org.flowerplatform.common.util;

public class Pair<A, B> {
	public A a;
	public B b;
	
	public Pair(A a, B b) {
		super();
		this.a = a;
		this.b = b;
	}

	@Override
	public int hashCode() {
		return a.hashCode() ^ b.hashCode();
	}

	@Override
	public String toString() {
		return "(" + a.toString() + ", " + b.toString() + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Pair<?, ?>)) {
			return false;
		}
		Pair<?, ?> otherObj = (Pair<?, ?>) obj;
		return 
			((a == null && otherObj.a == null) || (a != null && a.equals(otherObj.a))) &&
			((b == null && otherObj.b == null) || (b != null && b.equals(otherObj.b)));
	}
	
}
