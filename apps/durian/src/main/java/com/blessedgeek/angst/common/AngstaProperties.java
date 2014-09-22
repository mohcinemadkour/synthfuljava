package com.blessedgeek.angst.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AngstaProperties
extends Properties {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void setPropertiesTextFile(String path) throws IOException{
		InputStream propin = this.getClass().getClassLoader().getResourceAsStream(path);
		if (propin!=null) {
			this.load(propin);
		}
	}
	
	public void setPropertiesXMLFile(String path) throws IOException{
		InputStream propin = this.getClass().getClassLoader().getResourceAsStream(path);
		if (propin!=null) {
			this.loadFromXML(propin);
		}
	}
}
