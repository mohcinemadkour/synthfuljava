package com.blessedgeek.angst.mvc.durian;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.blessedgeek.angst.app.durian.Wally;
import com.blessedgeek.angst.common.AAngsta;

@Controller
public class DurianMVController
extends AAngsta{

	private Wally wally;

	@Inject
	public void setWally(Wally wally){
		this.wally = wally;
		getLogger().info("mvc wally={}", wally);		
	}
	
	/**
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 * 
	 * Browser url: http://localhost:8080/durian/v/h1
	 */
    @RequestMapping(value = "/h1", method = RequestMethod.GET)
	public String handleRequestInternal(ModelMap model)			
	throws Exception {
		
		getLogger().info("####");
		return "Hello";
	}

}
