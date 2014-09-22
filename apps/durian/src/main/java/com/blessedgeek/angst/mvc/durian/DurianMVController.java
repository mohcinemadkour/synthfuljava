package com.blessedgeek.angst.mvc.durian;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.blessedgeek.angst.common.AAngsta;

@Controller
public class DurianMVController
extends AAngsta{

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
