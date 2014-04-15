package me.mervin.util;

import java.util.HashMap;

public class PairMap<T1,T2> extends HashMap<Pair<T1>, T2> {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PairMap<Integer, Integer> p = new PairMap<Integer, Integer>();
		p.put(new Pair<Integer>(1, 2), 1);
		p.put(new Pair<Integer>(1, 2), 2);
		p.put(new Pair<Integer>(2, 2), 2);
		p.put(new Pair<Integer>(3, 2), 90);
		D.p(p.size());
		D.p(p.get(new Pair<Integer>(3, 2)));
		if(p.containsKey(new Pair<Integer>(3, 2))){
			D.m();
		}
	}

}
