package jus.aor.mobilagent.kernel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.logging.Level;

public class Agent implements _Agent{
	
	private AgentServer server;
	private Route route;
	private String serverName;
	private static final long serialVersionUID = 1L;
	private boolean first=false;
	protected transient BAMAgentClassLoader loader;
	protected transient Jar jar;

	protected _Action retour() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// On execute l'action a effectuer si on ne se trouve pas sur le serveur initial
		if(route.hasNext() && first){
			route.get().action.execute();
			route.next();
		}

		if(route.hasNext()){
			
			// Envoi de l'agent courant vers prochain serveur
				first=true;
				Socket socket;
				try {
					socket = new Socket(route.get().server.getHost(),route.get().server.getPort());
					OutputStream os=socket.getOutputStream();
					// Flux pour l'envoi de données
					ObjectOutputStream oosJar = new ObjectOutputStream(os);
					
					oosJar.writeObject(jar);
					

					ObjectOutputStream oosAg = new ObjectOutputStream(os);
					oosAg.writeObject(this);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchElementException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		
	}

	@Override
	public void init(AgentServer agentServer, String serverName) {
		// TODO Auto-generated method stub
		this.server = agentServer; //DONE
		this.serverName = serverName; //DONE
		if(route == null){
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
