package Gt.common;

import java.io.BufferedReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kevinsawicki.http.HttpRequest;

public class GtUserAccountsUtils {
	
	public static Logger log = LoggerFactory.getLogger("Gt.main.Extension.ThreePattiExtension");
	 

	public static JSONObject getPlayerInfo(String domainName, String api,HashMap<String,String> params) {
		JSONObject retJson =null;
		log.info("HTTP get Player Info, domain: " + domainName + " , api: " + api + " params: " + params.toString());
		try {
			JSONObject jsonParam = new JSONObject(); 
			for(Map.Entry<String, String> entry : params.entrySet()){
				jsonParam.put(entry.getKey(),entry.getValue());

			}
			log.info("HTTP get Player Info, " + " Json params: " + jsonParam.toString());
			retJson = sendPostRequest(domainName, api, jsonParam);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("HTTP get player info error", e);
		} catch(Exception e){
			e.printStackTrace();
			log.error("HTTP get player info error", e);
		}
		
		return retJson;

	}
	

	@SuppressWarnings("unused")
	private static JSONObject sendPostRequest(String domainName, String api,JSONObject jsonParam) throws Exception{
		JSONObject json = null;
		URL obj = new URL("http://" + domainName + api);
		
		log.info("HTTP sendPostRequest " + " URL: " + domainName + api);
		// Attempt to use HttpRequest to send post request to parse cloud
		HttpRequest request = HttpRequest.post(obj).contentType("application/json");
		
		// request.header("X-Parse-Application-Id",
		// "**************************");
		// request.header("X-Parse-REST-API-Key", "********************");
		request.acceptJson();
		/*
		 * JSONObject jsonParam = new JSONObject(); jsonParam.put("username",
		 * "rohan"); jsonParam.put("password", "Golden@123");
		 */
		request.send(jsonParam.toString().getBytes("UTF-8"));
		if (request.ok()) {
			// System.out.println("HttpRequest WORKED");
			String inputLine;
			StringBuffer response = new StringBuffer();
			BufferedReader in = new BufferedReader(request.bufferedReader());
			while ((inputLine = in.readLine()) != null) {
				response.append("\n");
				response.append(inputLine);
			}
			log.info("response : " + response.toString());
			json = new JSONObject(response.toString());
		}
		return json;

	}
	
	@SuppressWarnings("unused")
	private static JSONObject sendGetRequest(String domainName, String api,String auth) throws Exception{
		JSONObject json = null;
		URL obj = new URL("http://"+domainName + api);
		// Attempt to use HttpRequest to send post request to parse cloud
		HttpRequest request = HttpRequest.get(obj).contentType(
				"application/json");
		// request.header("X-Parse-Application-Id",
		// "**************************");
		// request.header("X-Parse-REST-API-Key", "********************");
		request.acceptJson();
		request.header("Authorization", auth);
		/*
		 * JSONObject jsonParam = new JSONObject(); jsonParam.put("username",
		 * "rohan"); jsonParam.put("password", "Golden@123");
		 */
		//request.send(jsonParam.toString().getBytes("UTF-8"));
		request.code();
		request.body();
		if (request.ok()) {
			// System.out.println("HttpRequest WORKED");
			String inputLine;
			StringBuffer response = new StringBuffer();
			BufferedReader in = new BufferedReader(request.bufferedReader());
			while ((inputLine = in.readLine()) != null) {
				response.append("\n");
				response.append(inputLine);
			}
			System.out.println(response.toString());
			json = new JSONObject(response.toString());
		}
		return json;

	}
}
