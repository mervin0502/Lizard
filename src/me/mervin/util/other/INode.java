package me.mervin.util.other;

/**
 * Reference to node in Fibonacci heap
 * @param <T>
 */
public interface INode<T> {
	public double key();
	public T value();
}
