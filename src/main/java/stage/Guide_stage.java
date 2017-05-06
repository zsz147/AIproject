package stage;

public class Guide_stage {
		public String src_addr="";
		public String dst_addr="";
		public String mode="";
		public Guide_stage(String src,String dst,String Mode){
			src_addr=src;
			dst_addr=dst;
			mode=Mode;
		}
		public Guide_stage(){};
		public boolean IsReady()
		{
			if(src_addr!=""&&dst_addr!=""&&mode!=""){
				return true;
			}else{
				return false;
			}
		}		
}
