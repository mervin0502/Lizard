package me.mervin.util;

import java.util.HashMap;

import me.mervin.util.other.INode;

/**
 * @param <T> - data type to store
 */
public class FibonacciHeap<T>{
	/**
	 * the forest of trees is kept in double linked list
	 * min points to the element on top of the heap
	 */
	private Node min, head, tail;
	
	private HashMap<T, Node> key2NodeMap = new HashMap<T, Node>();
	/**
	 * the number of trees in the forest
	 */
	private int rootsCount;
	/**
	 * the number of elements in the heap
	 */
	public int elCount;
	
	/**
	 * 1/log(golden ratio). Used when the trees are reordered
	 */
	private static final double GR = 1.0 / Math.log((1.0 + Math.sqrt(5.0)) / 2.0);
	
	public FibonacciHeap(){
		min = head = tail = null;
		rootsCount = elCount = 0;
	}
	
	/**
	 * @return true if there are no elements in the heap false otherwise
	 */
	public boolean empty(){
		return min == null;
	}
	
	/**
	 * Note that values in the heap are ordered by their
	 * key.The node with the smallest key is on top.
	 * This operation is fast - O(1)
	 * @param weight key to the element
	 * @param val the data kept
	 * @return reference to the element in the heap.This reference 
	 * can be used for the decrease key operation
	 */
	public INode<T> insert(double weight, T val){
		Node node = new Node(weight,val);
		this.key2NodeMap.put(val, node);
		if(head != null){
			tail.next = head.prev = node;
			node.next = head;
			node.prev = tail;
			tail = node;
		}else{
			head = tail = min = node.prev = node.next = node;
		}
		if(min.key > weight){
				min = node;
		}
		rootsCount++;
		elCount++;
		return node;
	}
	/**
	 * 
	 * @param val
	 * @return INode<T>
	 */
	public INode<T> getNode(T val){
		return this.key2NodeMap.get(val);
	}
	/**
	 * Runs for O(log n) amortized
	 * @return node with the smallest key in the heap
	 */
	public INode<T> extractMin(){
		if(empty()){
			return null;
		}
		Node oldMin = min;
		if(elCount == 1){
			head = tail = min = null;
			elCount = rootsCount = 0;
			return oldMin;
		}
		int newRootsCnt = numRoots(oldMin);
		removeFromList(oldMin);
		Node child = oldMin.child;
		if(child != null){
			addToList(child);
		}
		rootsCount += (newRootsCnt - 1);
		elCount--;
		reorder();
		/*
		 * nullify in order to know that this element is excluded from the forest
		 * and to avoid adding it again if it is passed to the decrease key method
		 */
		oldMin.parent = oldMin.prev = oldMin.next = oldMin.child = null;
		return oldMin;
	}
	
	/**
	 * @param node
	 * @return true if the node has not been excluded from the heap via the extract method
	 */
	public boolean isExcluded(INode<T> node){
		Node n = (Node)node;
		return n.parent == null &&  n.prev == null && n.next == null && n.child == null;
	}
	
	/**
	 * decrease key of an element and if the heap constraint is violated the element
	 * moves up the heap possibly to the top.
	 * Note: if you pass reference to element that has previously been excluded via the
	 * extract method no action is taken and the method returns
	 * Runs in O(1) amortized time.
	 * 
	 * @param node heap element
	 * @param weight the decrease amount
	 * @throws ClassCastException if a node instance is passed different from the one 
	 * returned from the insert method
	 * @throws IllegalArgumentException if node instance if null or delta is a negative number
	 */
	public void decreseKey(INode<T> node, double weight){
		if(node == null){
			throw new IllegalArgumentException("node cannot be null");
		}else if(weight > node.key()){
			//throw new IllegalArgumentException("the new value cannot be greater number! You passed: " + weight);
		}else{
			Node nd = (Node)node;
			Node x = nd;
			if(nd.parent == null && nd.prev == null && nd.next == null && nd.child == null){
				return;
			}
			
			nd.key = weight;
			Node parent = nd.parent;
			if(parent != null && nd.key < parent.key){
				Node next = null;
				do{
					next = nd.next;
					//1. exclude from current list
					nd.prev.next = nd.next;
					nd.next.prev = nd.prev;
					parent.degree--;
					
					//2. add to main list
					nd.parent = null;
					tail.next = nd;
					nd.prev = tail;
					tail = nd;
					nd.next = head;
					head.prev = nd;
					rootsCount++;
					
					/*
					 * fix case when parent has only one child
					 */
					if(next != nd){
						parent.child = next;
					}else{
						parent.child = null;
					}
					
					nd.marked = false;
					
					nd = parent;
					parent = parent.parent;
				}while(parent != null && parent.marked && parent.parent != null);
				
				if(parent != null && parent.parent != null && !parent.marked){
					parent.marked = true;
				}
			}
			if(min.key > x.key){
				min = x;
			}
		}
	}
	
	private int numRoots(Node node){
		Node temp = node.child;
		if(temp != null){
			do{
				temp.parent = null;
				temp.marked = false;
				temp = temp.next;
			}while(temp != node.child);
		}
		if(node != null){
			return node.degree;
		}else{
			return 0;
		}
	}

	/**
	 * Establishes order in the forest by joining trees with 
	 * same order until all trees in the forest are of different
	 * order
	 */
	private void reorder() {
		int rootCount = rootsCount;
		double size = 0;
		//D.p((Math.log(elCount) /Math.log(2)));
		//size = ((int) Math.floor((Math.log(elCount) /Math.log(2)) * GR)) + 1;
		//size = ((int) Math.floor(Math.log(elCount)/Math.log(2) )) + 2;
		size = elCount;
		Object[] orders = new Object[(int)size];
		Node cur = min = head;
		int order;
		Node next = null;
		Node temp = null;
		Node stored = null;
		while(rootCount != 0){
			next = cur.next;
			order = cur.degree;
			while(true){
				//D.p(order);
				stored = (Node) orders[order];
				if(stored == null){
					break;
				}
				if(stored.key  < cur.key){
					temp = cur;
					cur = stored;
					stored = temp;
				}
				joinTrees(cur, stored);
				orders[order] = null;
				order++;
			}
			orders[order] = cur;
			if(cur.key <= min.key){
				min = cur;
			}
			cur = next;
			rootCount--;
		}
	}
	
	/**
	 * Joins b as a leftmost child of a
	 * @param a
	 * @param b
	 */
	private void joinTrees(Node a, Node b){
//		System.out.println("join " + b.val + ":" +b.key + " as child of " + a.val + ":" + a.key);
		rootsCount--;
		removeFromList(b);
		a.degree++;
		Node child = a.child;
		if(child != null){
			Node childPrev = child.prev;
			childPrev.next = b;
			child.prev = b;
			b.next = child;
			b.prev = childPrev;
		}else{
			b.next = b.prev = b;
		}
		a.child = b;
		b.parent = a;
		b.marked = false;
	}
	
	private void addToList(Node node){
		Node nodeTail = (node.prev == null) ? node : node.prev;
		if(nodeTail == null){
			node.next = node.prev = node;
		}
		if(head == null){
			head = min = node;
			tail = nodeTail;
		}else{
			tail.next = node;
			nodeTail.next = head;
			node.prev = tail;
			head.prev = nodeTail;
			tail = nodeTail;
		}
	}
	
	/**
	 * removes a node from a double linked list.
	 * Also moves the main pointers if needed
	 * @param node
	 */
	private void removeFromList(Node node){
		if(head == tail){
			head = tail = min = null;
		}
		removeFromSubList(node);
	}
	
	private void removeFromSubList(Node node){
		if(node == head){
			head = head.next;
		}
		if(node == tail){
			tail = tail.prev;
		}
		node.prev.next = node.next;
		node.next.prev = node.prev;
	}
	
	/**
	 * Represents element from the heap
	 */
	private class Node implements INode<T> {
		private Node prev, next, child, parent;
		private T val;
 		private int degree;
		private boolean marked;
		double key;
		
		public Node(double weight, T value){
			prev = next = child = parent = null;
			val = value;
			degree = 0;
			marked = false;
			this.key = weight;
		}

		public double key() {
			return key;
		}

		public T value() {
			return val;
		}
	}
}
/**
 * Reference to node in Fibonacci heap
 * @param <T>
 */
/*interface INode<T> {
	public int key();
	public T value();
}*/