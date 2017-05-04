package helper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class Map {
	public String getdirection(String origin,String destination,String mode){
    	String baidu_ak="y5wUhthyEOfD9SG0fzBNrUGjBGp8QfBa";
    	String origin_address=origin;
    	String destination_address=destination;
    	String direction_mode=mode;
    	String result="";
       	String url_baidu_map_direction="http://api.map.baidu.com/direction/v1?mode="+direction_mode+"&origin="+origin_address+"&destination="+destination_address+"&origin_region=北京&destination_region=北京"+"&output=json&ak="+baidu_ak;
    	//System.out.println( url_baidu_map_direction);
    	HttpRequestUtils httpquest_direction=new HttpRequestUtils();
    	JSONObject baidu_map_direction=httpquest_direction.httpGet(url_baidu_map_direction);
    	//System.out.println( baidu_map_direction);
    	//System.out.println( baidu_map_direction.getJSONObject("result"));
    	JSONArray steps=baidu_map_direction.getJSONObject("result").getJSONArray("routes").getJSONObject(0).getJSONArray("steps");
    	String distance=baidu_map_direction.getJSONObject("result").getJSONArray("routes").getJSONObject(0).get("distance").toString();
    	result+=distance+'$';
    	for(int i=0;i<steps.size();i++){
    		result+=steps.getJSONObject(i).get("instructions").toString();
    		//System.out.println(steps.getJSONObject(i).get("instructions").toString());
    		//System.out.println(steps.getJSONObject(i).getJSONObject("instructions"));
    	}
    	result=result.replaceAll("<b>", "");
    	result=result.replaceAll("</b>", "");
    	return result;
    }
}
