package org.gorany.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.java.Log;

@Controller
@Log
public class Kakaocontroller {

	/*
	 * @GetMapping("/data") public void list() { log.info("로긴 데이타 겟페이지임다~~~~"); }
	 */

	@GetMapping("/sample1")
	public void sample1() {
		log.info("샘플 데이타 겟페이지임다~~~~");
	}

	// rest api
	@RequestMapping(value = "/data", produces = "application/json", method = { RequestMethod.GET, RequestMethod.POST })
	public void kakaoLogin(@RequestParam("code") String code, HttpServletRequest request,
			HttpServletResponse httpServlet) {

		log.info("과연 여기에....?");
		log.info("code: " + code);

		JsonNode jsonToken = getAccessToken(code);
		log.info("JSON 반환 : " + jsonToken.get("access_token"));

		//return code;
		/////////////////////////////////////////////////////////////
		//사용자 정보 요청

        JsonNode userInfo = getKakaoUserInfo(code);

        // Get id

 		String id = userInfo.path("id").asText();

 		String nickname = null;

 		String thumbnailImage = null;

 		String profileImage = null;

 		String message = null;

     		

        // 유저정보 카톡에서 가져오기 Get properties

		JsonNode properties = userInfo.path("properties");

		if (properties.isMissingNode()) {

			// if "name" node is missing

		} else {

			nickname = properties.path("nickname").asText();

			thumbnailImage = properties.path("thumbnail_image").asText();

			profileImage = properties.path("profile_image").asText();



			log.info("nickname : " + nickname);

			log.info("thumbnailImage : " + thumbnailImage);

			log.info("profileImage : " + profileImage);

		}




	}
	
	
	public static JsonNode getAccessToken(String autorize_code){ 

	    final String RequestUrl = "https://kauth.kakao.com/oauth/token";



	     List<NameValuePair> postParams = new ArrayList<>();

	    postParams.add(new BasicNameValuePair("grant_type", "authorization_code"));

	    postParams.add(new BasicNameValuePair("client_id", "7bcfa7f67474740c64a1494f6c7a0f67"));    // REST API KEY

	    postParams.add(new BasicNameValuePair("redirect_uri", "http://localhost:8080/data"));    // 리다이렉트 URI

	    postParams.add(new BasicNameValuePair("code", autorize_code));    // 로그인 과정중 얻은 code 값



	    final HttpClient client = HttpClientBuilder.create().build();

	    final HttpPost post = new HttpPost(RequestUrl);

	    JsonNode returnNode = null;

	

	    try {

	      post.setEntity(new UrlEncodedFormEntity(postParams));

	      final HttpResponse response = client.execute(post);

	      final int responseCode = response.getStatusLine().getStatusCode();



	      System.out.println("\nSending 'POST' request to URL : " + RequestUrl);

	      System.out.println("Post parameters : " + postParams);

	      System.out.println("Response Code : " + responseCode);

	     

	      //JSON 형태 반환값 처리

	      ObjectMapper mapper = new ObjectMapper();

	      returnNode = mapper.readTree(response.getEntity().getContent());



	    } catch (UnsupportedEncodingException e) {

	      e.printStackTrace();

	    } catch (ClientProtocolException e) {

	      e.printStackTrace();

	    } catch (IOException e) {

	      e.printStackTrace();

	    } finally {

	        // clear resources

	    }

	    return returnNode;

	}
	
	//사용자 정보
	public static JsonNode getKakaoUserInfo(String autorize_code) {

		

		 final String RequestUrl = "https://kapi.kakao.com/v1/user/me";

		    

		    String CLIENT_ID = "7bcfa7f67474740c64a1494f6c7a0f67"; // REST API KEY

		    String REDIRECT_URI = "http://localhost:8080/data"; // 리다이렉트 URI

		    String code = autorize_code; // 로그인 과정중 얻은 토큰 값



		    final HttpClient client = HttpClientBuilder.create().build();

		    final HttpPost post = new HttpPost(RequestUrl);

		    

		    // add header

		    post.addHeader("Authorization", "Bearer " + autorize_code);

		    

		    JsonNode returnNode = null;

		    

		    try {

		      final HttpResponse response = client.execute(post);

		      final int responseCode = response.getStatusLine().getStatusCode();



		      System.out.println("\nSending 'POST' request to URL : " + RequestUrl);

		      System.out.println("Response Code : " + responseCode);



		      //JSON 형태 반환값 처리

		      ObjectMapper mapper = new ObjectMapper();

		      returnNode = mapper.readTree(response.getEntity().getContent());

		      

		    } catch (UnsupportedEncodingException e) {

		      e.printStackTrace();

		    } catch (ClientProtocolException e) {

		      e.printStackTrace();

		    } catch (IOException e) {

		      e.printStackTrace();

		    } finally {

		        // clear resources

		    }

		    return returnNode;

		}



	

}
