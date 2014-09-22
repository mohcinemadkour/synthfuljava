package com.blessedgeek.angst.app.durian;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.blessedgeek.angst.common.AAngsta;
import com.blessedgeek.angst.common.AngstaProperties;

@Service
public class UserChangeService
extends AAngsta{

	private String greeting;
	private Message message;
	private AngstaProperties aProperties;
	
	@Inject
	public void setGreeting (String greeting) {
		this.greeting = greeting;
		getLogger().info("greeting={}", greeting);
	}

	@Inject
	public void setMessage (Message message) {
		this.message = message;
		getLogger().info("message={}", message);
	}
	
	@Inject
	public void setAProperties(AngstaProperties aProperties) {
		this.aProperties = aProperties;
		getLogger().info("aProperties={}", aProperties);
	}
	
	UserChangeService(){
	}
	
}
