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
	private String wName;
	private int wValue;
	
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
	
	@Inject
	public void setWally(Wally wally){
		this.wName = wally.getName();
		this.wValue = wally.getValue();
		getLogger().info("wally={}", wally);		
	}
	
	UserChangeService(){
	}
	
}
