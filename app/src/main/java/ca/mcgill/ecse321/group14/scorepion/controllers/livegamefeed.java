package ca.mcgill.ecse321.group14.scorepion.controllers;

import java.util.ArrayList;
import java.util.List;

/**
 * This singleton class holds string elements in a list. It uses the singleton design pattern so that a single
 * instance is accessible from anywhere within the application. It's used for generating the live feed
 * in the live mode.
 */
public class livegamefeed {

	private List<String> listOfElements = new ArrayList<>();
	private static livegamefeed ourInstance = new livegamefeed();

	public static livegamefeed getInstance() {
		return ourInstance;
	}

	private livegamefeed() {
	}

	public List<String> getList () {
		return listOfElements;
	}

	public void clear () {
		listOfElements = new ArrayList<>();
	}

	public void add (String element) {
		listOfElements.add(0, element);
	}
}
