package Gt.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Gt.common.UrlCall;
import Gt.user.utils.GtTransactionHistoryUtils;

public class WebSericeCalling {
	public static Logger log = LoggerFactory.getLogger("Gt.main.Extension.UserLoginHandler");
	private static final String SERVER_PRIVATE_KEY = "DC892D7D7EE2EE48F2426030C3D0A76F59400D851562165D8F283B4E3AEA5C82FF065847B49B4CC27F3DA907D7ADC3DAE7F643A9FB9133E5A1EC2EE549A5D9A4";
	public static void main(String[] args) {
		try {
			/*HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("user_id", 18);
			map.put("chip_type",1);
			map.put("game_table_id", 6);
			map.put("chips", 25);
			map.put("cr_dr",1);
			map.put("remarks","test");
			map.put("user_id", "18");
			map.put("amount", 500);
			map.put("cash_type", 0);
			map.put("plateform", 2);
			map.put("source", 0);
			map.put("source_id", 2);
			map.put("email", "kjoshi30@gmail.com");
			//map.put("password", "12345678");
			map.put("device_type", "3");*/
			//map.put("withdraw_method", 3);
			// map.put("session_key", "54f41101e533575f4b59caa7a31411c2");
			// http://104.236.29.155/index.php/user/auth/signup
			int playerId = 0;
			HashMap<String, Object> requestesParameters = new HashMap<String, Object>();
			requestesParameters.put("session_key", "bd4968a1519dc6c8aea28ecbb4566421");
			String url = "http://192.168.0.51:8090/gt_games_platform/" + UrlCall.API_MY_ACCOUNT;
			String response = null;
			try {
//				response = sendPost(requestesParameters, url, "bd4968a1519dc6c8aea28ecbb4566421", false);
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (response != null) {
				Map<String, Object> value = new Gson().fromJson(
						response, new TypeToken<HashMap<String, Object>>() {}.getType()
						);
				Map<String, Object> data = new Gson().fromJson(
						value.get("data").toString(), new TypeToken<HashMap<String, Object>>() {}.getType()
						);
				Map<String, Object> userBalance = new Gson().fromJson(
						data.get("user_balance").toString(), new TypeToken<HashMap<String, Object>>() {}.getType()
						);
				if(data.containsKey("user_details")) {
					Map<String, Object> userDetails = new Gson().fromJson(
							data.get("user_details").toString(), new TypeToken<HashMap<String, Object>>() {}.getType()
							);
					Double userId = (Double) userDetails.get("user_id");
					playerId = userId.intValue();
				}
				Double realBalance = (Double) userBalance.get("real_amount");
				Double dummyBalance = (Double) userBalance.get("dummy_amount");
			}

			
		} catch (Exception e) {
			// aca5d5d0f1eae3f988c24b0c3056f077
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e);
		}
		
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		
	}

	@SuppressWarnings("unused")
	private static String createMD5(String s) throws NoSuchAlgorithmException {
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(s.getBytes(), 0, s.length());
		System.out
				.println("MD5: " + new BigInteger(1, m.digest()).toString(16));
		return new BigInteger(1, m.digest()).toString(16).trim();
	}

	public static String sendPost(HashMap<String, Object> requestesParameters, String url,String sessionKey) throws Exception {

		URL obj = new URL(url);

		// Attempt to use HttpRequest to send post request to parse cloud
		HttpRequest request = HttpRequest.post(obj).contentType(
				"application/json");
		
		if(sessionKey != null){
			request.header("server_key", sessionKey);
		}
		
		request.acceptJson();
		JSONObject jsonParam = new JSONObject();
		for (Map.Entry<String, Object> entry : requestesParameters.entrySet()) {
			jsonParam.put(entry.getKey(), entry.getValue());
			log.info(entry.getKey() +"   :  "+entry.getValue());
		}
		log.info(jsonParam.toString());
		request.send(jsonParam.toString().getBytes("UTF-8"));
		log.info("\nSending 'POST' request to URL : " + url);
		log.info("Response Code : " + request.code());
		log.debug("\nSending 'POST' request to URL : " + url);
		log.debug("Response Code : " + request.code());
		if (request.ok()) {
			System.out.println("HttpRequest WORKED");
			String inputLine;
			Map<String, List<String>> map = request.headers();

			for (Map.Entry<String, List<String>> entry : map.entrySet()) {
				// System.out.println("Key : " + entry.getKey() +
				// " ,Value : " + entry.getValue());
			}
			StringBuffer response = new StringBuffer();
			BufferedReader in = new BufferedReader(request.bufferedReader());
			while ((inputLine = in.readLine()) != null) {
				response.append("\n");
				response.append(inputLine);
			}
			System.out.println(response.toString());
			log.debug(response.toString());
			return response.toString();
		} else {
			log.debug("HttpRequest FAILED " + request.code()	+ request.body());
			return null;
		}

	}

	@SuppressWarnings("unused")
	private static JSONObject sendGetRequest(String domainName, String api,String auth) throws Exception {
		JSONObject json = null;
		URL obj = new URL("http://" + domainName + api);
		// Attempt to use HttpRequest to send post request to parse cloud
		HttpRequest request = HttpRequest.get(obj);
		request.header("Authorization", auth);
		// request.header("Authorization: Token "+auth);
		/*
		 * JSONObject jsonParam = new JSONObject(); jsonParam.put("username",
		 * "rohan"); jsonParam.put("password", "Golden@123");
		 * request.send(jsonParam.toString().getBytes("UTF-8"));
		 */

		/*
		 * JSONObject jsonParam = new JSONObject(); jsonParam.put("username",
		 * "rohan"); jsonParam.put("password", "Golden@123");
		 */
		// request.send(jsonParam.toString().getBytes("UTF-8"));
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
	
	private static void addMoney() throws Exception{
		
		for(int i=534; i<=633; i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			
			map.put("user_id", i);
			map.put("amount", 1000000);
			map.put("cash_type",3);
			map.put("plateform", 2);
			map.put("source", 0);
			map.put("source_id", 2);
			
//			String response = WebSericeCalling.sendPost(map, "http://demo.dragon3patti.com/user/finance/server_deposit", SERVER_PRIVATE_KEY,true);
			String response = null;
			if (response != null) {

				Map<String, Object> value = new Gson().fromJson(response, new TypeToken<HashMap<String, Object>>() {
				}.getType());

				String a = value.get("data").toString();

				Map<String, Object> data = new Gson().fromJson(a, new TypeToken<HashMap<String, Object>>() {
				}.getType());

				
			}
			
		}
		
	}
	
	private static void createUsers() throws Exception{
		
		String[] usernames = new String[100];
		String data = "";
		BufferedReader reader = null;
		File botuser = new File("C:/users/Dell/Desktop/botusernames.txt");
		try {
			reader = new BufferedReader(new FileReader(botuser));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			if (reader != null) {
				int i = 0;
				while ((data = reader.readLine()) != null) {
					System.out.println(data);
					usernames[i++]=data;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i=1; i<=100; i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			
			map.put("dob","Jan 01, 1970");
			map.put("email",usernames[i-1]+"@gmail.com");
			map.put("password","12345678");
			map.put("device_type","3");
			
			String response = WebSericeCalling.sendPost(map, "http://13.126.42.69/user/auth/signup",null);
			System.out.println(response);
			if (response != null) {

				Map<String, Object> value = new Gson().fromJson(response, new TypeToken<HashMap<String, Object>>() {
				}.getType());

				String a = value.get("data").toString();

				Map<String, Object> data1 = new Gson().fromJson(a, new TypeToken<HashMap<String, Object>>() {
				}.getType());

				
			}
			
		}
	}

}
