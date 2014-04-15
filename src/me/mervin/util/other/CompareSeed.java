package me.mervin.util.other;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import me.mervin.util.D;

public class CompareSeed implements Comparator<Object> {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
/*		TreeMap<Number, Number> map = new TreeMap<Number, Number>(new CompareSeed());
		//TreeMap<Number, Number> map = new TreeMap<Number, Number>();
		map.put(11, 1);
		map.put(12, 2);
		map.put(13, 0);
		map.put(15,-2);
		
		Iterator<Number> it = map.keySet().iterator();
		while(it.hasNext()){
			D.p(it.next());
		}*/
		
		
		SortedSet<Map.Entry<Integer, Double>> sortedset = new TreeSet<Map.Entry<Integer, Double>>(
	            new Comparator<Map.Entry<Integer, Double>>() {
	                @Override
	                public int compare(Map.Entry<Integer, Double> e1,
	                        Map.Entry<Integer, Double> e2) {
	                    return -e1.getValue().compareTo(e2.getValue());
	                	//return e1.getKey().compareTo(e2.getKey());
	                }
	            });

	  //sortedset.addAll(myMap.entrySet());
	  SortedMap<Integer, Double> myMap = new TreeMap<Integer, Double>();
	    myMap.put(111, 10.0);
	    myMap.put(1122, 9.0);
	    myMap.put(333, 11.0);
	    myMap.put(444, 2.0);
	    sortedset.addAll(myMap.entrySet());
	    System.out.println(sortedset);
	    myMap.put(444, 20.0);
	    System.out.println(sortedset);
	    Iterator<Entry<Integer, Double>> it = sortedset.iterator();
	    while(it.hasNext()){
	    	D.p(it.next().getKey());
	    }
	    //D.p(myMap);
	}

	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		
		D.p("####################");
		D.p("###"+o1.toString());
		D.p("@@@"+o2.getClass());
		return 1;
	}

}
