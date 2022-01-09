package com.kakao.myapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SummerController {

	@RequestMapping("/summerNote/upload")
	public String upload(){
		System.out.println("upload() 동작 중");
		return "summer";
	}
	
	@RequestMapping(value="/summerNote/detail", method=RequestMethod.POST)
//	@ResponseBody
	public String detail(@RequestParam("content")String content,Model model) {
		System.out.println("detail() 작동 중");
		System.out.println(content);
		
		model.addAttribute("content",content);
		return "detail";
	}
}
