package jus.aor.mobilagent.kernel;

public class AgentServer extends Thread{

	String name;
	int port;
	_Service<T> service;
	
	
	public AgentServer(String name, int port) {
		this.name=name;
		this.port=port;
	}
	
	public void run(){ 
		//TODO
		
	}
	
	private _Agent getAgent (Socket sock){
		return null;
	}
	
	
	public String toString(){
		return name;
	}
	
	protected _Service<T> getService (String name){
		return service.get(name);
	}
	
	public URI site() {
		URI uri=null;
		try {
			uri= new URI("localhost:"+port+"/");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uri;
		
	}

}
