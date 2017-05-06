package THU_Guide;

import java.util.Scanner;

import com.iflytek.cloud.speech.RecognizerListener;
import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SpeechUtility;
import com.iflytek.cloud.speech.SynthesizerListener;

import helper.*;
import stage.*;
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
    	
    	while(true){
    	Scanner sc=new Scanner(System.in);
    	Aiplatform aiplatform=new Aiplatform();
    	Map map=new Map();       
    	//String nlu_input="我要从清芬园到罗姆楼怎么走？";
    	String nlu_input=sc.nextLine();
    	//System.out.println("input:"+nlu_input);
    	System.out.println("------waiting for nlu response-----");
    	JSONObject nlu_result=aiplatform.nlu(nlu_input);
    	System.out.println("------get nlu response-----");
    	
    	if(nlu_result.get("errno").toString().equals("0")){
    		//System.out.println(nlu_result);
    		JSONArray patternlist=nlu_result.getJSONObject("msg").getJSONArray("patternlist");
    		int max_level=0;
    		int max_offset=-1;
    		for(int i=0;i<patternlist.size();i++){
    			
    			if(Integer.parseInt(patternlist.getJSONObject(i).get("_level").toString())>max_level){
    				max_level=Integer.parseInt(patternlist.getJSONObject(i).get("_level").toString());
    				max_offset=i;
    			}
    		}
    		String act_type=nlu_result.getJSONObject("msg").getJSONArray("patternlist").getJSONObject(max_offset).get("_act_type").toString();
    		//System.out.println(act_type);
    		if(act_type.equals("guide0")){
    			//String place=nlu_result.getJSONObject("msg").getJSONArray("patternlist").getJSONObject(0).get("place").toString();
    			//int addr_offset=place.indexOf(',');
    			String src_addr=nlu_result.getJSONObject("msg").getJSONArray("patternlist").getJSONObject(max_offset).get("srcplace").toString();
    			String dst_addr=nlu_result.getJSONObject("msg").getJSONArray("patternlist").getJSONObject(max_offset).get("dstplace").toString();
    			String mode="walking";
    			String map_result=map.getdirection("清华大学"+src_addr,"清华大学"+ dst_addr, mode);
    			int distance_offset=map_result.indexOf('$');
    			String distance=map_result.substring(0, distance_offset);
    			String routes=map_result.substring(distance_offset+1, map_result.length());
    			//System.out.println(place);
    			//System.out.println(addr_offset);
    			//System.out.println(src_addr);
    			//System.out.println(dst_addr);
    			//System.out.println(map_result);
    			//System.out.println(distance);
    			//System.out.println(routes);
    			JSONObject nlg_input=new JSONObject();
    			nlg_input.put("_act_type", "guide0");
    			nlg_input.put("p1", src_addr);
    			nlg_input.put("p2", dst_addr);
    			nlg_input.put("distance", distance);
    			nlg_input.put("route", routes);
    			System.out.println("-----waiting for nlg response-----");
    			JSONObject nlg_result=aiplatform.nlg(nlg_input);
    			System.out.println("-----get nlg response-----");
    			if(nlg_result.get("errno").toString().equals("0")){
    				String output=nlg_result.getJSONArray("msg").getJSONObject(0).get("output").toString();
    		    	System.out.println("output:"+output);
    			}
    		}
    		if(act_type.equals("guide1")){
    			Guide_stage guide_stage=new Guide_stage();
    			String src_addr=nlu_result.getJSONObject("msg").getJSONArray("patternlist").getJSONObject(max_offset).get("srcplace").toString();
    			guide_stage.src_addr=src_addr;
    			guide_stage.mode="walking";
    			
    			JSONObject nlg_input=new JSONObject();
    			while(!guide_stage.IsReady()){
    				nlg_input.put("_act_type", "guide2");
    				nlg_input.put("p1", src_addr);
    				System.out.println("-----waiting for nlg response-----");
    				JSONObject nlg_result=aiplatform.nlg(nlg_input);
    				System.out.println("-----get nlg response-----");
    				if(nlg_result.get("errno").toString().equals("0")){
        				String output=nlg_result.getJSONArray("msg").getJSONObject(0).get("output").toString();
        		    	System.out.println("output:"+output);
        			}
    				
    				String str=null;
    				str=sc.nextLine();
    				nlu_input=str;
    				System.out.println("------waiting for nlu response-----");
    		    	nlu_result=aiplatform.nlu(nlu_input);
    		    	System.out.println("------get nlu response-----");
    		    	act_type=nlu_result.getJSONObject("msg").getJSONArray("patternlist").getJSONObject(0).get("_act_type").toString();
    		    	if(act_type.equals("guideinfo")){
    		    		String dst_addr=nlu_result.getJSONObject("msg").getJSONArray("patternlist").getJSONObject(0).get("place").toString();
    		    		guide_stage.dst_addr=dst_addr;
    		    	}
    			}
    			String map_result=map.getdirection("清华大学"+guide_stage.src_addr, "清华大学"+guide_stage.dst_addr, guide_stage.mode);
    			int distance_offset=map_result.indexOf('$');
    			String distance=map_result.substring(0, distance_offset);
    			String routes=map_result.substring(distance_offset+1, map_result.length());
    			
    			nlg_input.put("_act_type", "guide0");
    			nlg_input.put("p1", guide_stage.src_addr);
    			nlg_input.put("p2", guide_stage.dst_addr);
    			nlg_input.put("distance", distance);
    			nlg_input.put("route", routes);
    			System.out.println("-----waiting for nlg response-----");
    			JSONObject nlg_result=aiplatform.nlg(nlg_input);
    			System.out.println("-----get nlg response-----");
    			if(nlg_result.get("errno").toString().equals("0")){
    				String output=nlg_result.getJSONArray("msg").getJSONObject(0).get("output").toString();
    		    	System.out.println("output:"+output);
    			}
    			
    			
    		}
    		if(act_type.equals("guide2")){
    			Guide_stage guide_stage=new Guide_stage();
    			String dst_addr=nlu_result.getJSONObject("msg").getJSONArray("patternlist").getJSONObject(max_offset).get("dstplace").toString();
    			guide_stage.dst_addr=dst_addr;
    			guide_stage.mode="walking";
    			
    			JSONObject nlg_input=new JSONObject();
    			while(!guide_stage.IsReady()){
    				nlg_input.put("_act_type", "guide1");
    				nlg_input.put("p2", dst_addr);
    				System.out.println("-----waiting for nlg response-----");
    				JSONObject nlg_result=aiplatform.nlg(nlg_input);
    				System.out.println("-----get nlg response-----");
    				if(nlg_result.get("errno").toString().equals("0")){
        				String output=nlg_result.getJSONArray("msg").getJSONObject(0).get("output").toString();
        		    	System.out.println("output:"+output);
        			}
    				
    				String str=null;
    				str=sc.nextLine();
    				nlu_input=str;
    				System.out.println("------waiting for nlu response-----");
    		    	nlu_result=aiplatform.nlu(nlu_input);
    		    	System.out.println("------get nlu response-----");
    		    	//System.out.println(nlu_result);
    		    	act_type=nlu_result.getJSONObject("msg").getJSONArray("patternlist").getJSONObject(0).get("_act_type").toString();
    		    	if(act_type.equals("guideinfo")){
    		    		//System.out.println(act_type.equals("guideinfo"));
    		    		String src_addr=nlu_result.getJSONObject("msg").getJSONArray("patternlist").getJSONObject(0).get("place").toString();
    		    		guide_stage.src_addr=src_addr;
    		    	}
    			}
    			String map_result=map.getdirection("清华大学"+guide_stage.src_addr, "清华大学"+guide_stage.dst_addr, guide_stage.mode);
    			int distance_offset=map_result.indexOf('$');
    			String distance=map_result.substring(0, distance_offset);
    			String routes=map_result.substring(distance_offset+1, map_result.length());
    			
    			nlg_input.put("_act_type", "guide0");
    			nlg_input.put("p1", guide_stage.src_addr);
    			nlg_input.put("p2", guide_stage.dst_addr);
    			nlg_input.put("distance", distance);
    			nlg_input.put("route", routes);
    			System.out.println("-----waiting for nlg response-----");
    			JSONObject nlg_result=aiplatform.nlg(nlg_input);
    			System.out.println("-----get nlg response-----");
    			if(nlg_result.get("errno").toString().equals("0")){
    				String output=nlg_result.getJSONArray("msg").getJSONObject(0).get("output").toString();
    		    	System.out.println("output:"+output);
    			}
    		}
    		if(act_type.equals("recommend11")){
    			JSONObject nlg_input=new JSONObject();
    			String type=nlu_result.getJSONObject("msg").getJSONArray("patternlist").getJSONObject(max_offset).get("type").toString();
    			JSONObject kb_input=new JSONObject();
    			//System.out.println( type);
    			kb_input.put("sparql", "select ?type where{?type  <http://ciia.cs.tsinghua.edu.cn/dialog#type> \""+type+"\"}");
    	    	//kb_input.put("sparql", "select?type?where{?type.}");
    	    	//System.out.println( "select ?type where{?type  <http://ciia.cs.tsinghua.edu.cn/dialog#type> \"食堂\"}");
    	    	JSONObject kb_result=aiplatform.kb(kb_input);
    	    	String list="";
    	    	if(kb_result.get("errno").toString().equals("0")){
    	    		JSONArray msg=kb_result.getJSONArray("msg");
    	    		for(int i=0;i<msg.size();i++){
    	    			String type_result=msg.getJSONObject(i).getString("type").toString();
    	    			//System.out.println("before replace:"+type_result);
    	    			type_result=type_result.replace("http://ciia.cs.tsinghua.edu.cn/dialog/", "");
    	    			//System.out.println("after replace:"+type_result);
    	    			list+=type_result+";";
    	    		}
    	    	}else{
    	    		System.out.println("kb query error");
    	    	}
    	        //System.out.println( list);
    	        nlg_input.put("_act_type", "recommend11");
    			nlg_input.put("type", type);
    			nlg_input.put("list", list);
    			System.out.println("-----waiting for nlg response-----");
    			JSONObject nlg_result=aiplatform.nlg(nlg_input);
    			System.out.println("-----get nlg response-----");
    			if(nlg_result.get("errno").toString().equals("0")){
    				String output=nlg_result.getJSONArray("msg").getJSONObject(0).get("output").toString();
    		    	System.out.println("output:"+output);
    			}
    		}
    		//System.out.println(nlu_result);
    		
    	}else{
    		System.out.println("------request error-----");
    	}
    	/*
    	String ak="uvA18GaMEwv8Lezh7OFow3xlnVTSxJk0";
    	String baidu_ak="y5wUhthyEOfD9SG0fzBNrUGjBGp8QfBa";
    	String origin_address="清华大学清芬园";
    	String destination_address="清华大学-罗姆楼";
    	String mode="riding";
    	*/
    	//String result=map.getdirection(origin_address,destination_address,mode);
        //System.out.println(result);    	
    	/*
        SpeechRecognizer mIat= SpeechRecognizer.createRecognizer();
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
        mIat.setParameter(SpeechConstant.APPID, "59082d33");
        mIat.setParameter(SpeechConstant.VAD_BOS, "10000");
        mIat.setParameter(SpeechConstant.VAD_EOS, "2000");
        System.out.println("before startListening");
        SpeechUtility.createUtility("appid=59082d33");
        MscTest test=new MscTest();
        mIat.startListening(test.mRecoListener);
        System.out.println("after startListening");
        */
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
    	SpeechSynthesizer mTts= SpeechSynthesizer.createSynthesizer( );
    	//2.合成参数设置，详见《MSC Reference Manual》SpeechSynthesizer 类
    	mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
    	mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
    	mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
    	//设置合成音频保存位置（可自定义保存位置），保存在“./tts_test.pcm”
    	//如果不需要保存合成音频，注释该行代码
    	//mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./tts_test.pcm");
    	//3.开始合成
    	mTts.startSpeaking("语音合成测试程序", mSynListener);
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
}
