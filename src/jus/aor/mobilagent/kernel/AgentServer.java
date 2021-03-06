package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class AgentServer extends Thread{

	String name;
	int port;
	_Service<?> service;
	ServerSocket servListener;
	private HashMap<String,_Service<?>> myServices;

	
	
	public AgentServer(int port, String name) {
		this.name=name;
		this.port=port;
		myServices = new HashMap<String, _Service<?>>();
	}
	

	public void run(){ 

		System.out.println("Run agent serveur");
		Jar jar;
		Socket socketClient;
		_Agent agent;
		
		try {
			servListener = new ServerSocket(port);
			
		} catch (IOException e) {
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
				agent = (_Agent) ais.readObject();
				ais.close();
				agent.reInit(this, this.name);
				Thread t = new Thread(agent);
				t.start();

				
			}catch (ClassNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
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
		return myServices.get(name);
	}
	
	public URI site() {
		URI uri=null;
		try {
			uri= new URI("mobilagent://localhost:"+port+"/");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return uri;
		
	}

}
