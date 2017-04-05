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
		return null;
	}

	@Override
	public void run() {

		if(route.hasNext() && first){
			route.get().action.execute();
			route.next();
		}

		if(route.hasNext()){
			
				first=true;
				Socket socket;
				try {
					socket = new Socket(route.get().server.getHost(),route.get().server.getPort());
					OutputStream os=socket.getOutputStream();

					ObjectOutputStream oosJar = new ObjectOutputStream(os);
					
					oosJar.writeObject(jar);
					

					ObjectOutputStream oosAg = new ObjectOutputStream(os);
					oosAg.writeObject(this);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (NoSuchElementException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
System.out.println();
		
	}

	@Override
	public void init(AgentServer agentServer, String serverName) {
		this.server = agentServer; //DONE
		this.serverName = serverName; //DONE
		if(route == null){
			route = new Route(new Etape(server.site(), _Action.NIHIL)); //DONE
		}
	}

	@Override
	public void reInit(AgentServer server, String serverName) {
		this.server = server; //DONE
		this.serverName = serverName; //DONE
		System.out.println("reInit"); //DONE
	}

	@Override
	public void addEtape(Etape etape) {
		this.route.add(etape); //DONE
	}
	
	public void goNext (){
		URI destination = this.route.get().server;
		try {

			Socket destinationSocket = new Socket(destination.getHost(), destination.getPort());
			OutputStream out = destinationSocket.getOutputStream();
			ObjectOutputStream objet = new ObjectOutputStream(out);
			BAMAgentClassLoader classLoader = (BAMAgentClassLoader) this.getClass().getClassLoader();
		} catch (Exception e){
			System.out.println(e);
		}
	}

}
