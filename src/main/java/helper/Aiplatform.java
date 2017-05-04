package helper;

import net.sf.json.JSONObject;

public class Aiplatform {
	public String ak="uvA18GaMEwv8Lezh7OFow3xlnVTSxJk0";
	public JSONObject nlu(String input){
		JSONObject nlu_input=new JSONObject();
		nlu_input.put("input", input);
		String url_nlu="http://115.182.62.171:9000/api/nlu/"+ak;
		HttpRequestUtils httpquest=new HttpRequestUtils();
    	JSONObject nlu_out=httpquest.httpPost(url_nlu, nlu_input);
    	return nlu_out;
	}
	public JSONObject nlg(JSONObject nlginput){
		String url_nlg="http://115.182.62.171:9000/api/nlg/"+ak;
		HttpRequestUtils httpquest=new HttpRequestUtils();
    	JSONObject nlg_out=httpquest.httpPost(url_nlg, nlginput);
    	return nlg_out;
	}
	public JSONObject kb(JSONObject kbinput){
		String url_kb="http://115.182.62.171:9000/api/kb/"+ak;
		HttpRequestUtils httpquest=new HttpRequestUtils();
    	JSONObject kb_out=httpquest.httpPost(url_kb, kbinput);
    	return kb_out;
	}
}
