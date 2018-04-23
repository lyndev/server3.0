package game.core.net.codec.websocket;

public class MinaBean {
	private String content;
	private boolean isWebAccept=false;
        private byte[] buffer = null;

	public String getContent() {
		return content;
	}
        
        public byte[] getBuff(){
            return this.buffer;
        }
        
        public void setBuff(byte[] buffer){
            this.buffer = buffer;
        }
                
	public void setContent(String content) {
		this.content = content;
	}

	public boolean isWebAccept() {
		return isWebAccept;
	}

	public void setWebAccept(boolean isWebAccept) {
		this.isWebAccept = isWebAccept;
	}

	@Override
	public String toString() {
		return "MinaBean [content=" + content + "]";
	}

}
