package org.scorelang.util;

import java.util.Arrays;

public class ScoreVector<T> {
	
	private Object[] _data;
	private int _size;
	private int _capacity;
	
	public ScoreVector() {
		this(10);
	}
	
	public ScoreVector(int initCapacity) {
		_data = new Object[initCapacity];
		_capacity = initCapacity;
	}
	
	// Resize the thing
	
	private void resize(int newcap) {
		_data = Arrays.copyOf(_data, newcap);
		_capacity = newcap;
		_size = Math.min(newcap, _size);
	}
	
	// Push
	
	public int push(T value) {
		if (_size >= _capacity)
			resize(_capacity * 2);
		_data[_size++] = value;
		return _size - 1;
	}
	
	// Pop
	
	@SuppressWarnings("unchecked")
	public T pop() {
		Object res = _data[--_size];
		if (_size < _capacity / 2)
			resize(_capacity / 2);
		return (T) res;
	}
	
	// Get
	
	@SuppressWarnings("unchecked")
	public T get(int idx) {
		if (idx < 0 || idx >= _size)
			throw new IndexOutOfBoundsException(Integer.toString(idx));
		return (T) _data[idx];
	}
	
	// Getters
	
	public int size() {
		return _size;
	}
	
	// To String
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('['); sb.append(' ');
		for (int i = 0; i < _size; i++) {
			sb.append(get(i));
			if (i < _size - 1)
				sb.append(',');
			sb.append(' ');
		}
		sb.append(']');
		return sb.toString();
	}
	
}