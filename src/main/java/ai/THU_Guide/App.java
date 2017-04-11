package ai.THU_Guide;

import helper.HttpRequestUtils;
import net.sf.json.JSONObject;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	String ak="uvA18GaMEwv8Lezh7OFow3xlnVTSxJk0";
    	
    	HttpRequestUtils httpquest=new HttpRequestUtils();
    	
    	JSONObject nlu_input=new JSONObject();
    	//nlu_input.put("input", "有什么不辣的川菜吗？");
    	nlu_input.put("input", "我要从fit楼到罗姆楼怎么走");
    	//nlu_input.put("input", "你好");
    	String url_nlu="http://115.182.62.171:9000/api/nlu/"+ak;
    	JSONObject nlu_out=httpquest.httpPost(url_nlu, nlu_input);
        System.out.println( nlu_out);
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
