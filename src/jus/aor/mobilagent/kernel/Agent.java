package jus.aor.mobilagent.kernel;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.util.logging.Level;

public class Agent implements _Agent{
	
	private AgentServer server;
	private Route route;
	private String serverName;

	protected _Action retour() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(route.hasNext()){
			
			// On envoie l'agent courant vers le prochain serveur
		}
		
	}

	@Override
	public void init(AgentServer agentServer, String serverName) {
		// TODO Auto-generated method stub
		this.server = agentServer; //DONE
		this.serverName = serverName; //DONE
		if(route=null){
			route = new Route(new Etape(server.site(), _Action.NIHIL)); //DONE
		}
	}

	@Override
	public void reInit(AgentServer server, String serverName) {
		// TODO Auto-generated method stub
		this.server = server; //DONE
		this.serverName = serverName; //DONE
		System.out.println("reInit"); //DONE
	}

	@Override
	public void addEtape(Etape etape) {
		// TODO Auto-generated method stub
		this.route.add(etape); //DONE
	}
	
	public void goNext (){
		URI destination = this.route.get().server;
		try {
			// nouveau socket de destination
			Socket destinationSocket = new Socket(destination.getHost(), destination.getPort());
			// récupération de l'output stream de destination
			OutputStream out = destinationSocket.getOutputStream();
			// création de l'objet à transmettre sur l'output stream créé au dessus
			ObjectOutputStream objet = new ObjectOutputStream(out);
			// récupération de la classe (code)
			BAMAgentClassLoader classLoader = (BAMAgentClassLoader) this.getClass().getClassLoader();
		} catch (Exception e){
			System.out.println(e);
		}
	}

}
