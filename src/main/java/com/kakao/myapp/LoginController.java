package com.kakao.myapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Controller
public class LoginController {
	
	@RequestMapping(value="/" ,method=RequestMethod.GET)
	public String login(Model model) {
		String reqUrl = "https://kauth.kakao.com/oauth/authorize"
				+ "?client_id=f98f37d47a62be09db4e5ccfed265b61"
				+ "&redirect_uri=http://localhost:8080/myapp/loginCallBack"
				+ "&response_type=code";
		
		model.addAttribute("reqUrl",reqUrl);
		return "/main";
	}
	
	@RequestMapping(value="/loginCallBack", method=RequestMethod.GET)
	public String loginProc(@RequestParam("code")String code,Model model) {
		System.out.println("여기까지 옴");
		System.out.println(code);
		String accessToken = getAccessToken(code);
		System.out.println(accessToken);
		
		HashMap<String,Object> userInfo = getUserInfo(accessToken);
		
		System.out.println(userInfo.get("email"));
		
		System.out.println("email:"+userInfo.get("email"));
		System.out.println("nickname"+userInfo.get("nickName"));
		
		model.addAttribute("email",userInfo.get("email"));
		model.addAttribute("accessToken", accessToken);
		return "/main";
	}
	
	//토큰 값 가져오기
	public String getAccessToken(String code) {
		String access_Token="";
		String refresh_Token="";
		String reqURL="https://kauth.kakao.com/oauth/token";
		
		try {
			URL url = new URL(reqURL);
			HttpURLConnection connection =(HttpURLConnection)url.openConnection();
			//HttpURLConnection 객체를 통해 url에 값을 입/출력 할 수 있음
			connection.setRequestMethod("POST");
			//POST요청 시 필요한 설정
			connection.setDoOutput(true);
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
			StringBuilder builder= new StringBuilder();
			builder.append("grant_type=authorization_code");
			builder.append("&client_id=f98f37d47a62be09db4e5ccfed265b61");
			builder.append("&redirect_uri=http://localhost:8080/myapp/loginCallBack");
			builder.append("&code="+code);
			
			bw.write(builder.toString());
			bw.flush();
			
			// 요청을 통해 얻은 json 타입의  Response 메시지 읽어오기
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line="";
			String result="";
			
			while((line = br.readLine()) !=null) {
				result+=line;
			}
			
			System.out.println("responseBody"+result);
			
			//Gson 라이브러리에 포함된 클래스로 json 데이터 읽어들이기
			JsonElement element = JsonParser.parseString(result);
			access_Token = element.getAsJsonObject().get("access_token").getAsString();
			refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();
			br.close();
			bw.close();
	}catch(IOException e) {
		e.printStackTrace();
		}
		
		return access_Token;
	}
	
	// 유저 정보 조회
	public HashMap<String,Object> getUserInfo(String accessToken){
		HashMap<String,Object> userInfo= new HashMap<String,Object>();
		String reqURL="https://kapi.kakao.com/v2/user/me";
		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			
			//요청 헤더에 포함되야 하는 내용
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
			
			// 응답 내용 읽어오기
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),StandardCharsets.UTF_8));

	            String line = "";
	            String result = "";

	            while ((line = br.readLine()) != null) {
	                result += line;
	            }
	            System.out.println("responseBody:"+result);
	            JsonElement element = JsonParser.parseString(result);
	            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
	            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
	            
	            String nickName = properties.getAsJsonObject().get("nickname").getAsString();
	            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();
	            
	            userInfo.put("accessToken", accessToken);
	            userInfo.put("nickName", nickName);
	            userInfo.put("email", email);
	            
		}catch(IOException e) {
			e.printStackTrace();
		}
		return userInfo;
	}
}
