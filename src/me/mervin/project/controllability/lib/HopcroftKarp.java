package me.mervin.project.controllability.lib;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import me.mervin.util.D;


 /**
 *   HopcroftKarp.java
 *    
 *  @author Mervin.Wong  DateTime 2013-10-11 下午4:49:52    
 *  @version 0.4.0
 *  **********************************************************<br/>
 * 
 *  Ref:
 *  	- Hopcroft, John E., and Richard M. Karp. "An n^5/2 algorithm for maximum matchings in bipartite graphs." SIAM Journal on computing 2.4 (1973): 225-231.
 * 		- http://en.wikipedia.org/wiki/Matching_(graph_theory)#Maximum_matchings_in_bipartite_graphs
 * 		- http://en.wikipedia.org/wiki/Hopcroft-Karp_algorithm
 * 		- http://code.activestate.com/recipes/123641/
 * 		- https://raw.github.com/pierre-dejoue/kart-match/master/src/fr/neuf/perso/pdejoue/kart_match/HopcroftKarp.java
 */
public class HopcroftKarp {

	private int edges = 0;
	private Map<Number, Integer> last = new HashMap<Number, Integer>();
	private Map<Integer, Integer> prev = new HashMap<Integer, Integer>();
	private Map<Integer, Number> head = new HashMap<Integer, Number>();
	
	private Map<Number, Number> matching = new HashMap<Number, Number>();
	private Map<Number, Integer> dist = null;
	private Map<Number, Boolean> used = new HashMap<Number, Boolean>();
	private Map<Number, Boolean> vis = null;
	
	private Set<Number> uSet = new HashSet<Number>();
	private Set<Number> vSet = new HashSet<Number>();
	
	
	public HopcroftKarp(){
	
	}
	
	public void addEdge(Number u, Number v) {
	    this.head.put(this.edges, v);
	    if(this.last.containsKey(u)){
	    	this.prev.put(this.edges, this.last.get(u));
	    }else{
	    	this.prev.put(this.edges, -1);
	    }
	    this.last.put(u, edges++);
	    
	    this.uSet.add(u);
	    this.vSet.add(v);
	}
	
	private void bfs(){
		this.dist = new HashMap<Number, Integer>();
		Queue<Number> queue = new LinkedList<Number>();
		for(Number u:this.uSet){
			if( (!this.used.containsKey(u)) || (this.used.containsKey(u) && !this.used.get(u))){
				queue.add(u);
				this.dist.put(u, 0);
			}//if
		}//for
		
		while(!queue.isEmpty()){
			Number u1 = queue.poll();
			for(int e = this.last.get(u1); e >= 0; e = this.prev.get(e)){
				Number v1 = this.head.get(e);
				if(this.matching.containsKey(v1)){
					Number u2 = this.matching.get(v1);
					if(u2.longValue() >= 0 && !this.dist.containsKey(u2)){
						this.dist.put(u2, dist.get(u1)+1);
						queue.add(u2);
					}//if
				}//if
				
			}//for
		}//while
	}
	
	private boolean dfs(Number u1){
		this.vis.put(u1, true);
		for(int e = this.last.get(u1); e >= 0; e = this.prev.get(e)){
			Number v = this.head.get(e);
			//int u2 = this.matching.get(v);
			if(!this.matching.containsKey(v) || (!this.vis.containsKey(this.matching.get(v)) || !this.vis.get(this.matching.get(v))) && this.dist.get(this.matching.get(v)) == this.dist.get(u1)+1 && this.dfs(this.matching.get(v))){
				this.matching.put(v, u1);
				this.used.put(u1, true);
				return true;
			}
		}
		return false;
	}
	
	public Map<Number, Number> matching(){
		for(int res = 0;;){
			this.bfs();
			this.vis = new HashMap<Number, Boolean>();
			int f = 0;
			for(Number u: this.uSet){
				if((!this.used.containsKey(u) || !this.used.get(u)) && this.dfs(u)){
					f++;
				}//if
			}//for
			if(f == 0){
				//D.p(this.matching);
				return this.matching;
			}
			res += f;
		}
	} 

	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		HopcroftKarp hk = new HopcroftKarp();
		//hk.init(2, 3);
		long l, r ;
		l = Long.parseLong("111111111111");
		r = Long.parseLong("111111111112");
		hk.addEdge(l, r);
		l = Long.parseLong("111111111111");
		r = Long.parseLong("111111111113");
		hk.addEdge(l, r);
		l = Long.parseLong("111111111111");
		r = Long.parseLong("111111111114");
		hk.addEdge(l, r);
		l = Long.parseLong("111111111111");
		r = Long.parseLong("111111111115");
		hk.addEdge(l, r);
		l = Long.parseLong("111111111111");
		r = Long.parseLong("111111111116");
		hk.addEdge(l, r);
		l = Long.parseLong("111111111112");
		r = Long.parseLong("111111111116");
		hk.addEdge(l, r);
/*		hk.addEdge(11, 13);
		hk.addEdge(11, 14 );
		hk.addEdge(11, 15);
		hk.addEdge(11, 16);
		hk.addEdge(12, 16);*/
		//hk.addEdge(5, 6);
		D.p(hk.matching());
	}

}
