package helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import helper.DebugLog;
import com.iflytek.cloud.speech.LexiconListener;
import com.iflytek.cloud.speech.RecognizerListener;
import com.iflytek.cloud.speech.RecognizerResult;
import com.iflytek.cloud.speech.Setting;
import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechEvent;
import com.iflytek.cloud.speech.SpeechRecognizer;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SpeechUtility;
import com.iflytek.cloud.speech.SynthesizeToUriListener;
import com.iflytek.cloud.speech.UserWords;

public class MscTest {
	private static final String APPID = "59082d33";
	
	
	public RecognizerListener mRecoListener = new RecognizerListener(){
        //听写结果回调接口(返回Json格式结果，用户可参见附录)；
        //一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
        //关于解析Json的代码可参见MscDemo中JsonParser类；
        //isLast等于true时会话结束。
        public void onResult(RecognizerResult results, boolean isLast){
       	 System.out.println("in onResult");
            DebugLog.Log("Result:"+results.getResultString ());
            if(isLast){
           	 System.out.println("end");
           	 return ;
            }
        }
        //会话发生错误回调接口
        public void onError(SpeechError error) {
       	 System.out.println(error.getErrorDesc());
       	 System.out.println(error.getErrorCode());
            //error.getErrorDesc(); //获取错误码描述
        }
        //开始录音
        public void onBeginOfSpeech() {}
        //音量值0~30
        public void onVolumeChanged(int volume){
       	 if(volume==0){
       		 volume=1;
       	 }else if(volume >=6){
       		 volume=6;
       	 }
        }
        //结束录音
        public void onEndOfSpeech() {}
        //扩展用接口
        public void onEvent(int eventType,int arg1,int arg2,String msg) {}
		 
    };
	
	private boolean mIsEndOfSpeech = false;
	
	private static StringBuffer mResult = new StringBuffer();
	
	public void listen(){
		SpeechUtility.createUtility("appid=" + APPID);
		Recognize();
	}
	private void Recognize() {
		if (SpeechRecognizer.getRecognizer() == null)
			SpeechRecognizer.createRecognizer();
		mIsEndOfSpeech = false;
		RecognizePcmfileByte();
	}
	
	private RecognizerListener recListener = new RecognizerListener() {

		public void onBeginOfSpeech() {
			DebugLog.Log( "onBeginOfSpeech enter" );
			DebugLog.Log("*************开始录音*************");
		}

		public void onEndOfSpeech() {
			DebugLog.Log( "onEndOfSpeech enter" );
			mIsEndOfSpeech = true;
		}

		public void onVolumeChanged(int volume) {
			DebugLog.Log( "onVolumeChanged enter" );
			if (volume > 0)
				DebugLog.Log("*************音量值:" + volume + "*************");

		}

		public void onResult(RecognizerResult result, boolean islast) {
			DebugLog.Log( "onResult enter" );
			mResult.append(result.getResultString());
			
			if( islast ){
				DebugLog.Log("识别结果为:" + mResult.toString());
				mIsEndOfSpeech = true;
				mResult.delete(0, mResult.length());
				//waitupLoop();
			}
		}

		public void onError(SpeechError error) {
			mIsEndOfSpeech = true;
			DebugLog.Log("*************" + error.getErrorCode()
					+ "*************");
			//waitupLoop();
		}

		public void onEvent(int eventType, int arg1, int agr2, String msg) {
			DebugLog.Log( "onEvent enter" );
		}

	};
	
	public void RecognizePcmfileByte() {
		SpeechRecognizer recognizer = SpeechRecognizer.getRecognizer();
		recognizer.setParameter(SpeechConstant.AUDIO_SOURCE, "-1");
		//写音频流时，文件是应用层已有的，不必再保存
//		recognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH,
//				"./iat_test.pcm");
		recognizer.setParameter( SpeechConstant.RESULT_TYPE, "plain" );
		recognizer.startListening(recListener);
		
		FileInputStream fis = null;
		final byte[] buffer = new byte[64*1024];
		try {
			fis = new FileInputStream(new File("./test.pcm"));
			if (0 == fis.available()) {
				mResult.append("no audio avaible!");
				recognizer.cancel();
			} else {
				int lenRead = buffer.length;
				while( buffer.length==lenRead && !mIsEndOfSpeech ){
					lenRead = fis.read( buffer );
					recognizer.writeAudio( buffer, 0, lenRead );
				}//end of while
				
				recognizer.stopListening();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fis) {
					fis.close();
					fis = null;
				}
		} catch (IOException e) {
				e.printStackTrace();
			}
		}//end of try-catch-finally
		
	}
}
