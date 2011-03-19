package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Node;

public class HashMultiMap<K,V> extends HashMap<K, List<V>> {
	public void putMulti(K key, V item) {
		if (containsKey(key)) {
			get(key).add(item);
		} else {
			List<V> list = new ArrayList<V>();
			list.add(item);
			
			put(key, list);
		}
	}
}