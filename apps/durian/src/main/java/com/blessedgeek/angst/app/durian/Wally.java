package com.blessedgeek.angst.app.durian;

import org.springframework.stereotype.Component;

@Component
public class Wally {

	final private String name = "Wally";
	final private int value = 1000;

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}
}
