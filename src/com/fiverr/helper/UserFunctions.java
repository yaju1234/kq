package com.fiverr.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.util.Log;

public class UserFunctions {
	
	private JSON_Parser jsonParser;
	
	//webservice url
	private static String APIUrl="http://playgroundhumor.com/demo/webservice/mywebservice.php";
	
	//webservice tags
	private static String register_tag ="register";
	private static String login_tag ="login";
	private static String insert_quote_tag ="inser_Quote";
	private static String quote_tag = "quoteSelect";
	private static String quote_tag_all = "quoteSelectAll";
	private static String create_kid_profile_tag ="inser_kid_profile";
	private static String rate_quote_tag="rate";
	private static String mark_fav_quote_tag="mark_fav";
	private static String get_kids_tag="get_kids";
	private static String get_kid_details="kids_details";
	private static String get_fav_quotes_tag="get_fav_quotes";
	private static String edit_kid_details_tag ="update_kid_profile";
	
	//constructor
	public UserFunctions(){
		jsonParser = new JSON_Parser();
	}
	
	/**
	 * function make register request
	 * @param username
	 * @param emial
	 * @password
	 * @phone no
	 * @country
	 * @state
	 */
	public JSONObject registerUser(String username,String email,String password, String phoneno,String Gender, String country, String state, String city){
		//Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("gender", Gender));
		params.add(new BasicNameValuePair("phone", phoneno));
		params.add(new BasicNameValuePair("country", country));
		params.add(new BasicNameValuePair("state", state));
		params.add(new BasicNameValuePair("city", state));
		
		//getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(APIUrl, params);
		Log.e("RESPONSE ", json.toString());
		return json;		
	}
	
	/**
	 * Function make login request
	 * @param email
	 * @param password
	 */
	public JSONObject checkUser(String email,String password){
		//Building parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		
		//getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(APIUrl, params);
		
		//return json
		return json;
	}
	
	/**
	 * Function make get quotes request
	 * 
	 */
	/*public JSONArray getAllQuotes(){
		//Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("tag", quote_tag));
		//getting JSON Object
		JSONArray jsonArray = jsonParser.getJSONArrayFromUrl(APIUrl, params);
		return jsonArray;
	}*/
	
	public JSONObject getQuotes(String ParentID){
		//Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("tag", quote_tag));
				params.add(new BasicNameValuePair("parent_id", ParentID));
		//getting JSON Object
		JSONObject jsonObject = jsonParser.getJSONFromUrl(APIUrl, params);
		return jsonObject;
	}
	
	public JSONObject getFavQuotes(String parent_ID){
		//Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", get_fav_quotes_tag));
		params.add(new BasicNameValuePair("parent_id",parent_ID));
		
		//getting JSON Object
		JSONObject jsonObject = jsonParser.getJSONFromUrl(APIUrl, params);
		return jsonObject;
	}
	
	/** Function Create Kid profile
	 * @param Parnet_Id
	 * @param Name
	 * @param Nick Name
	 * @param Sex
	 * @param Age
	 * @param About
	 * 
	 * @return JsonPbject
	 * 
	 */
	
	public JSONObject createKid(String parent_id,String name,String nickname,String sex,String age,String about,String image){
		// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("tag",create_kid_profile_tag));
			params.add(new BasicNameValuePair("parent_id",parent_id));
			params.add(new BasicNameValuePair("name",name));
			params.add(new BasicNameValuePair("nick_name",nickname));
			params.add(new BasicNameValuePair("sex", sex));
			params.add(new BasicNameValuePair("age", age));
			params.add(new BasicNameValuePair("about",about));
			params.add(new BasicNameValuePair("image",image));
			
			//getting jsonobject
			JSONObject jsonObject = jsonParser.getJSONFromUrl(APIUrl, params);
			return jsonObject;
		
	}
	
	public JSONObject getAllQuotes(){
	
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", quote_tag_all));
		params.add(new BasicNameValuePair("parent_id", "0"));
		//
		JSONObject jsonObject = jsonParser.getJSONFromUrl(APIUrl, params);
		System.out.println("!--"+jsonObject.toString());
		return jsonObject;
	
	}
	
	public JSONObject insertQuote(String parent_ID,String kid_ID,String quote,String image,String video,String avgRate){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", insert_quote_tag));
		params.add(new BasicNameValuePair("parent_id", parent_ID));
		params.add(new BasicNameValuePair("kid_id",kid_ID));
		params.add(new BasicNameValuePair("quote",quote));
		params.add(new BasicNameValuePair("image",image));
		params.add(new BasicNameValuePair("video",video));
		params.add(new BasicNameValuePair("avg_rate",avgRate));
		//
		JSONObject jsonObject = jsonParser.getJSONFromUrl(APIUrl, params);
		return jsonObject;
		
	}
	
	public JSONObject rateQuote(String parent_ID,String quote_id,String rate){
		List<NameValuePair> params=new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag",rate_quote_tag));
		params.add(new BasicNameValuePair("parent_id",parent_ID));
		params.add(new BasicNameValuePair("quote_id",quote_id));
		params.add(new BasicNameValuePair("rate",rate));
		
		//
		JSONObject jsonObject = jsonParser.getJSONFromUrl(APIUrl, params);
		return jsonObject;
		
	}
	
	
	public JSONObject getKids(String parent_ID){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", get_kids_tag));
		params.add(new BasicNameValuePair("parent_id", parent_ID));
		
		JSONObject jsonObject = jsonParser.getJSONFromUrl(APIUrl, params);
		return jsonObject;
	}
	
	public JSONObject markFavQuote(String parent_ID,String quote_ID){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag",mark_fav_quote_tag));
		params.add(new BasicNameValuePair("parent_id",parent_ID));
		params.add(new BasicNameValuePair("quote_id" ,quote_ID));
			
		JSONObject jsonObject =jsonParser.getJSONFromUrl(APIUrl, params);
		return jsonObject;
			
		}
	
	public JSONObject getKidDetails(String kid_ID){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag",get_kid_details));
		params.add(new BasicNameValuePair("kids_id", kid_ID));
		
		JSONObject jsonObject = jsonParser.getJSONFromUrl(APIUrl, params);
		return jsonObject;		
	}
	
	public JSONObject editKidProfile(String kid_ID,String Parent_ID,String name,String Nick_name,String sex,String age,String about,String Image){
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag",edit_kid_details_tag));
		params.add(new BasicNameValuePair("kids_id",kid_ID));
		params.add(new BasicNameValuePair("parent_id",Parent_ID));
		params.add(new BasicNameValuePair("name",name));
		params.add(new BasicNameValuePair("nick_name",Nick_name));
		params.add(new BasicNameValuePair("sex",sex));
		params.add(new BasicNameValuePair("age",age));
		params.add(new BasicNameValuePair("about",about));
		params.add(new BasicNameValuePair("image",Image));
		
		JSONObject jsonObject = jsonParser.getJSONFromUrl(APIUrl, params);
		return jsonObject;
	}
	
	

}
