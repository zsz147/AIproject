package THU_Guide;

import helper.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        
    	Map map=new Map();       
    	String ak="uvA18GaMEwv8Lezh7OFow3xlnVTSxJk0";
    	String baidu_ak="y5wUhthyEOfD9SG0fzBNrUGjBGp8QfBa";
    	String origin_address="清华大学清芬园";
    	String destination_address="清华大学-罗姆楼";
    	String mode="riding";
    	
    	String result=map.getdirection(origin_address,destination_address,mode);
        System.out.println(result);    	
    	
    	/*
    	String origin_address="清华大学清芬园";
    	String url_baidu_map_origin="http://api.map.baidu.com/geocoder/v2/?address="+origin_address+"&output=json&ak="+baidu_ak;
    	System.out.println( url_baidu_map_origin);
    	HttpRequestUtils httpquest_origin=new HttpRequestUtils();
    	JSONObject baidu_map_origin=httpquest_origin.httpGet(url_baidu_map_origin);
    	System.out.println( baidu_map_origin);
    
    	String lng_origin=((JSONObject)((JSONObject)baidu_map_origin.get("result")).get("location")).get("lng").toString();
    	String lat_origin=((JSONObject)((JSONObject)baidu_map_origin.get("result")).get("location")).get("lat").toString();
    	
    	System.out.println( "lng_origin:"+lng_origin);
    	System.out.println( "lat_origin:"+lat_origin);
    	
    	String destination_address="清华大学-罗姆楼";
    	String url_baidu_map_destination="http://api.map.baidu.com/geocoder/v2/?address="+destination_address+"&output=json&ak="+baidu_ak;
    	System.out.println( url_baidu_map_destination);
    	HttpRequestUtils httpquest_destination=new HttpRequestUtils();
    	JSONObject baidu_map_destination=httpquest_destination.httpGet(url_baidu_map_destination);
    	System.out.println( baidu_map_destination);
    
    	String lng_destination=((JSONObject)((JSONObject)baidu_map_destination.get("result")).get("location")).get("lng").toString();
    	String lat_destination=((JSONObject)((JSONObject)baidu_map_destination.get("result")).get("location")).get("lat").toString();
    	
    	System.out.println( "lng_destination:"+lng_destination);
    	System.out.println( "lng_destination:"+lng_destination);
    	
    	//String mode="walking";
    	String mode="riding";
       	String url_baidu_map_direction="http://api.map.baidu.com/direction/v1?mode="+mode+"&origin="+origin_address+"&destination="+destination_address+"&origin_region=北京&destination_region=北京"+"&output=json&ak="+baidu_ak;
    	System.out.println( url_baidu_map_direction);
    	HttpRequestUtils httpquest_direction=new HttpRequestUtils();
    	JSONObject baidu_map_direction=httpquest_direction.httpGet(url_baidu_map_direction);
    	System.out.println( baidu_map_direction);
    	System.out.println( baidu_map_direction.getJSONObject("result"));
    	JSONArray steps=baidu_map_direction.getJSONObject("result").getJSONArray("routes").getJSONObject(0).getJSONArray("steps");
    	System.out.println();
    	System.out.println("origin_address:"+origin_address);
    	System.out.println("destination_address:"+destination_address);
    	for(int i=0;i<steps.size();i++){
    		System.out.println(steps.getJSONObject(i).get("instructions").toString());
    		//System.out.println(steps.getJSONObject(i).getJSONObject("instructions"));
    	}
    	*/
    	/*
    	MscTest test=new MscTest();
    	test.listen();
    	*/
    	/*
    	JSONObject nlu_input=new JSONObject();
    	
    	//nlu_input.put("input", "有什么不辣的川菜吗？");
    	nlu_input.put("input", "我要从fit楼到罗姆楼怎么走");
    	//nlu_input.put("input", "你好");
    	String url_nlu="http://115.182.62.171:9000/api/nlu/"+ak;
    	JSONObject nlu_out=httpquest.httpPost(url_nlu, nlu_input);
        System.out.println( nlu_out);
        */
        /*
        JSONObject nlg_input=new JSONObject();
    	nlg_input.put("_act_type", "hi");
    	String url_nlg="http://115.182.62.171:9000/api/nlg/"+ak;
    	JSONObject nlg_out=httpquest.httpPost(url_nlg, nlg_input);
        System.out.println( nlg_out.toString());
        */
        /*
        JSONObject kb_input=new JSONObject();
    	kb_input.put("sparql", "select?s?p?where{?s?p?o.}");
    	String url_kb="http://115.182.62.171:9000/api/kb/"+ak;
    	JSONObject kb_out=httpquest.httpPost(url_kb, kb_input);
        System.out.println( kb_out);
        */
    }
    
}
