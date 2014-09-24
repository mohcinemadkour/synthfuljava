package com.blessedgeek.angst.mvc.durian;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.blessedgeek.angst.common.AAngsta;

@Controller
@RequestMapping("/duriangrove")
public class DurianGroveMVController
extends AAngsta{

	/**
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 * 
	 * Browser url:
	 *  http://{hostname}:{port}/{context}/v/h2g2j
	 * e.g.,
	 *  http://localhost:8080/durian/v/h2g2j
	 */
    @RequestMapping(value = "/h2g2j", method = {RequestMethod.GET,RequestMethod.POST})
	public String anotherHandleRequest(ModelMap model,
			@RequestParam("who") String who,
			@RequestParam("what") String what,
			@RequestParam("when") String when)			
	throws Exception {
		model.addAttribute("who", who);
		model.addAttribute("why", "does the sea rush to shore?");
		
		getLogger().info("anotherHandleRequest: {},{},{} ", who, what, when);
		return "Cello";
	}
}
