package com.haocom.util.opools;

/**
 * StringBuilder¶ÔÏó³Ø. <br>
 * <p>
 * Copyright: Copyright (c) 2009-6-26 ÏÂÎç03:12:04
 * <p>
 * Company: 
 * <p>
 * Author: ChengFan
 * <p>
 * Version: 1.0
 * <p>
 */
public class StringBuilderPool extends ObjectPool<StringBuilder> {

	int maxCapacity = 2000;

	/**
	 * @param poolSize
	 * @param maxStringBuilderCapacity
	 */
	public StringBuilderPool(int poolSize, int maxStringBuilderCapacity) {
		super(poolSize);
		this.maxCapacity = maxStringBuilderCapacity;
	}

	@Override
	protected StringBuilder newObject() {
		return new StringBuilder(500);
	}

	@Override
	protected void resetObject(StringBuilder e) {
		if (e.capacity() > maxCapacity) {
			e.setLength(maxCapacity);
			e.trimToSize();
		}
		e.setLength(0);
	}
}
