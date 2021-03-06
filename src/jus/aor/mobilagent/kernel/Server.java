/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jus.aor.mobilagent.kernel.BAMAgentClassLoader;
import jus.aor.mobilagent.kernel._Agent;

/**
 * Le serveur principal permettant le lancement d'un serveur d'agents mobiles et les fonctions permettant de déployer des services et des agents.
 * @author     Morat
 */
public final class Server implements _Server{
	/** le nom logique du serveur */
	protected String name;
	/** le port où sera ataché le service du bus à agents mobiles. Pafr défaut on prendra le port 10140 */
	protected int port=10140;
	/** le server d'agent démarré sur ce noeud */
	protected AgentServer agentServer;
	/** le nom du logger */
	protected String loggerName;
	/** le logger de ce serveur */
	protected Logger logger=null;

	public Server(final int port, final String name){
		this.name=name;
		try {
			this.port=port;
			loggerName = "jus/aor/mobilagent/"+InetAddress.getLocalHost().getHostName()+"/"+this.name;
			logger=Logger.getLogger(loggerName);
			this.agentServer = new AgentServer(this.port, this.name); //done
			logger.log(Level.INFO, String.format("Starting AgentServer %s", this)); //done
			new Thread(this.agentServer).start(); //done
			Thread.sleep(1000);
		}catch(Exception ex){
			logger.log(Level.FINE," erreur durant le lancement du serveur"+this,ex);
			return;
		}
	}

	public final void addService(String name, String classeName, String codeBase, Object... args) {
		try {
			//A COMPLETER
		}catch(Exception ex){
			logger.log(Level.FINE," erreur durant le lancement du serveur"+this,ex);
			return;
		}
	}

	public final void deployAgent(String classeName, Object[] args, String codeBase, List<String> etapeAddress, List<String> etapeAction) {
		_Agent agent = null; //done
		try {
			BAMAgentClassLoader classLoader = new BAMAgentClassLoader(new URI(codeBase).getPath(), this.getClass().getClassLoader());


			Class<?> classAgent = Class.forName(classeName, true, classLoader);

			Constructor<?> construct = classAgent.getConstructor(Object[].class);
			agent = (_Agent) construct.newInstance(new Object[] { args });
			agent.init(this.agentServer, this.name);
			if (etapeAction.size() != etapeAction.size()) {
				this.logger.log(Level.INFO, " Problème de cohérence, le nombre d'action de d'adresse sont différents");
			} else {
				int size = etapeAction.size();
				for (int i = 0; i < size; i++) {
					Field champ = classAgent.getDeclaredField(etapeAction.get(i));
					champ.setAccessible(true);
					_Action action = (_Action) champ.get(agent);
					agent.addEtape(new Etape(new URI(etapeAddress.get(i)), action));
				}
				this.startAgent(agent, classLoader);
			}
		}catch(Exception ex){
			logger.log(Level.FINE," erreur durant le lancement du serveur"+this,ex);
			return;
		}
	}
	
	protected void startAgent(_Agent agent, BAMAgentClassLoader loader) throws Exception {
		//A COMPLETER
		URI agentServerSite = this.agentServer.site();

		Socket socket = new Socket(agentServerSite.getHost(), agentServerSite.getPort());

		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(outputStream);

		Jar baseCode = loader.extractCode();

		objectOutputStream.writeObject(baseCode);
		objectOutputStream2.writeObject(agent);

		objectOutputStream2.close();
		objectOutputStream.close();
		socket.close();
	}
}

