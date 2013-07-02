package com.crispico.flower.mp.codesync.base;


public class RootMatchRegistry {
	
	/**
	 * Temporar; ulterior cate o instanta va apartine probabil lui ProjectGroup.
	 */
	public static RootMatchRegistry INSTANCE = new RootMatchRegistry();

	/**
	 * Temporar; ulterior se va folosi un map probabil.
	 */
	public Match singleRootMatch;
	
	public Match getRootMatchById(int id) {
		return singleRootMatch;
	}
}