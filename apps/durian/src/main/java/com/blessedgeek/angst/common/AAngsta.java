package com.blessedgeek.angst.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class AAngsta {
	final private Logger logger = LoggerFactory.getLogger(this.getClass());
	final protected Logger getLogger() {
		return logger;
	}


}
