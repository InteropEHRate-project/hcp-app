package eu.interopehrate.hcpapp.mvc.commands.currentpatient.laboratorytests;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.function.BiFunction;

public class DoubleKeyHashMap<T1, T2, T3> {
	private final HashMap<Pair<T1, T2>, T3> hashMap;

	public DoubleKeyHashMap() {
		hashMap = new HashMap<>();
	}

	@SuppressWarnings("hiding")
	static class Pair<T1, T2> {
		T1 key1;
		T2 key2;

		Pair(T1 key1, T2 key2) {
			this.key1 = key1;
			this.key2 = key2;
		}

		@Override
		public int hashCode() {
			return this.key1.hashCode() + this.key2.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this)
				return true;
			if (!(obj instanceof Pair)) {
				return false;
			}
			@SuppressWarnings("unchecked")
			Pair<T1, T2> pair = (Pair<T1, T2>) obj;
			return this.key1.equals(pair.key1) && this.key2.equals(pair.key2);
		}
	}

	public T3 put(T1 key1, T2 key2, T3 value) {
		T3 prevVal;
		prevVal = this.hashMap.get(new Pair<>(key1, key2));
		this.hashMap.put(new Pair<>(key1, key2), value);
		return prevVal;
	}

	public T3 get(T1 key1, T2 key2) {
		T3 val;
		val = this.hashMap.get(new Pair<>(key1, key2));
		return val;
	}

	public T3 remove(T1 key1, T2 key2) {
		T3 prevVal;
		prevVal = this.hashMap.get(new Pair<>(key1, key2));
		this.hashMap.remove(new Pair<>(key1, key2));
		return prevVal;
	}

	public boolean containsKey(T1 key1, T2 key2) {
		return this.hashMap.containsKey(new Pair<>(key1, key2));
	}

	public Set<Pair<T1, T2>> keySet() {
		Set<Pair<T1, T2>> keySet;
		keySet = this.hashMap.keySet();
		return keySet;
	}

	public int size() {
		return this.hashMap.size();
	}
	
	public Iterator<Pair<T1, T2>> iterator(){
		return this.keySet().iterator();
	}
    	
	public T3 computeIfAbsent(T1 key1, T2 key2, BiFunction<T1,T2,T3> value) {
        	return this.hashMap.computeIfAbsent(new Pair<>(key1, key2), (k) -> value.apply(key1,key2));
	}

	@Override
	public String toString() {
		StringBuilder stringBuffer = new StringBuilder("{\n");
		Set<Pair<T1, T2>> keySet = this.keySet();
		Iterator<Pair<T1, T2>> itr = keySet.iterator();
		while (itr.hasNext()) {
			Pair<T1, T2> keyPair = itr.next();
			stringBuffer.append("   [").append(keyPair.key1).append(", ").append(keyPair.key2).append("] = ")
					.append(this.hashMap.get(new Pair<>(keyPair.key1, keyPair.key2)));
			if(itr.hasNext()) {
				stringBuffer.append(",\n");
			}
		}
		stringBuffer.append("\n}");
		return stringBuffer.toString();
	}
}
