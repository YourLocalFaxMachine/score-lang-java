package org.scorelang.util;

import java.util.Arrays;

public class ScoreVector<T> {
	
	private final int _initCapacity;
	
	private Object[] _data;
	private int _size;
	private int _capacity;
	
	public ScoreVector() {
		this(10);
	}
	
	public ScoreVector(int initCapacity) {
		if (initCapacity < 1)
			initCapacity = 1;
		_initCapacity = initCapacity;
		clear();
	}
	
	// Resize the thing
	
	private void resize(int newcap) {
		if (newcap < 4)
			newcap = 4;
		_data = Arrays.copyOf(_data, newcap);
		_capacity = newcap;
		_size = Math.min(newcap, _size);
	}
	
	public void clear() {
		_data = new Object[_initCapacity];
		_capacity = _initCapacity;
		_size = 0;
	}
	
	public void setSize(int size) {
		resize(size);
		_size = size;
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
	
	@SuppressWarnings("unchecked")
	public T top() {
		return (T) _data[_size - 1];
	}
	
	// Set
	
	@SuppressWarnings("unchecked")
	public void set(int idx, T value) {
		if (idx < 0 || idx >= _size)
			throw new IndexOutOfBoundsException(Integer.toString(idx));
		_data[idx] = value;
	}
	
	// Special
	
	public ScoreVector<T> sub(int start, int end) {
		ScoreVector<T> res = new ScoreVector<T>(_capacity);
		for (int i = start; i <= end; i++)
			res.push(get(i));
		return res;
	}
	
	public ScoreVector<T> reverse() {
		ScoreVector<T> res = new ScoreVector<T>(_capacity);
		for (int i = 0; i < _size; i++)
			res.push(get(_size - i - 1));
		return res;
	}
	
	// Getters
	
	public int size() {
		return _size;
	}
	
	public boolean isEmpty() {
		return _size == 0;
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