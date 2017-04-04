package jus.aor.mobilagent.kernel;

import java.net.ServerSocker;

public class AgentServer extends Thread{

	String name;
	int port;
	_Service<T> service;
	HashMap<String,_Service<?>> myService;
	ServerSocket servListener;

	
	
	public AgentServer(String name, int port) {
		this.name=name;
		this.port=port;
	}
	

	public void run(){ 

		System.out.println("Run agent serveur");
		Jar jar;
		Socket socketClient;
		_Agent ag;
		
		try {
			servListener = new ServerSocket(port);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		
		while(true){
			try {
				socketClient = servListener.accept();
				ObjectInputStream ois = new ObjectInputStream(socketClient.getInputStream());
				jar = (Jar) ois.readObject();
				BAMAgentClassLoader BAMAgent = new BAMAgentClassLoader(this.getClass().getClassLoader());
				BAMAgent.integrateCode(jar);
				AgentInputStream ais = new AgentInputStream(socketClient.getInputStream(),BAMAgent);
				ag = (_Agent) ais.readObject();
				ais.close();
				ag.reInit(this, this.name, BAMAgent);
				Thread t = new Thread(ag);
				t.start();

				
			}catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private _Agent getAgent (Socket sock){
		return null;
	}
	
	
	public String toString(){
		return name;
	}
	
	protected _Service<?> getService (String name){
		return myService.get(name);
	}
	
	public URI site() {
		URI uri=null;
		try {
			uri= new URI("mobilagent://localhost:"+port+"/");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uri;
		
	}

}
