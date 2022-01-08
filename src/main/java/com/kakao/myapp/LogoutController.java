package com.kakao.myapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class LogoutController {
	
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public String kakaoLogout(@RequestParam("accessToken")String accessToken) {
		System.out.println("�α׾ƿ� controller ���� ��");
		 String reqURL ="https://kapi.kakao.com/v1/user/logout";
	        try {
	            URL url = new URL(reqURL);
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setRequestMethod("POST");
	            //conn.setRequestProperty("charset", "utf-8"); //���ڵ� ���� �ʿ��ϸ� �ϱ�
	            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
	            int responseCode = conn.getResponseCode();
	            System.out.println("responseCode : " + responseCode);
	 
	            if(responseCode ==400)
	                throw new RuntimeException("īī�� �α׾ƿ� ���� ���� �߻�");
	            
	            
	            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            
	            String br_line = "";
	            String result = "";
	            while ((br_line = br.readLine()) != null) {
	                result += br_line;
	            }
	            System.out.println("���");
	            System.out.println(result);
	        }catch(IOException e) {
	            
	        }
		return "redirect:/";
	}
}
