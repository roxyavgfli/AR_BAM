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
	/**
	 * Démarre un serveur de type mobilagent 
	 * @param port le port d'écoute du serveur d'agent 
	 * @param name le nom du serveur
	 */
	public Server(final int port, final String name){
		this.name=name;
		try {
			this.port=port;
			/* mise en place du logger pour tracer l'application */
			loggerName = "jus/aor/mobilagent/"+InetAddress.getLocalHost().getHostName()+"/"+this.name;
			logger=Logger.getLogger(loggerName);
			/* démarrage du server d'agents mobiles attaché à cette machine */
			this.agentServer = new AgentServer(this.port, this.name); //done
			logger.log(Level.INFO, String.format("Starting AgentServer %s", this)); //done
			new Thread(this.agentServer).start(); //done
			/* temporisation de mise en place du server d'agents */
			Thread.sleep(1000);
		}catch(Exception ex){
			logger.log(Level.FINE," erreur durant le lancement du serveur"+this,ex);
			return;
		}
	}
	/**
	 * Ajoute le service caractérisé par les arguments
	 * @param name nom du service
	 * @param classeName classe du service
	 * @param codeBase codebase du service
	 * @param args arguments de construction du service
	 */
	public final void addService(String name, String classeName, String codeBase, Object... args) {
		try {
			//A COMPLETER
		}catch(Exception ex){
			logger.log(Level.FINE," erreur durant le lancement du serveur"+this,ex);
			return;
		}
	}
	/**
	 * deploie l'agent caractérisé par les arguments sur le serveur
	 * @param classeName classe du service
	 * @param args arguments de construction de l'agent
	 * @param codeBase codebase du service
	 * @param etapeAddress la liste des adresse des étapes
	 * @param etapeAction la liste des actions des étapes
	 */
	public final void deployAgent(String classeName, Object[] args, String codeBase, List<String> etapeAddress, List<String> etapeAction) {
		_Agent agent = null; //done
		try {//donne ...
			BAMAgentClassLoader classLoader = new BAMAgentClassLoader(new URI(codeBase).getPath(), this.getClass().getClassLoader());

			// Récupère la classe héritant _Agent
			Class<?> classAgent = Class.forName(classeName, true, classLoader);

			// Récupère le constructeur de cette classe
			Constructor<?> construct = classAgent.getConstructor(Object[].class);
			// Instantie l'object
			// TODO: Fix problem here
			agent = (_Agent) construct.newInstance(new Object[] { args });
			// Initialise l'Agent
			agent.init(this.agentServer, this.name);
			if (etapeAction.size() != etapeAction.size()) {
				this.logger.log(Level.INFO, " Problème de cohérence, le nombre d'action de d'adresse sont différents");
			} else {
				int size = etapeAction.size();
				for (int i = 0; i < size; i++) {
					// Ajoute une étape dans l'agent pour chaque étape dans les
					// listes
					// Récupère les champs de la classe
					Field champ = classAgent.getDeclaredField(etapeAction.get(i));
					// Assure que le champs soit accessible
					champ.setAccessible(true);
					// Récupère l'action
					_Action action = (_Action) champ.get(agent);
					// Ajout de l'étape
					agent.addEtape(new Etape(new URI(etapeAddress.get(i)), action));
				}
				// Démarre l'Agent
				this.startAgent(agent, classLoader);
				// new Thread(wAgent).start();
			} // ... done
		}catch(Exception ex){
			logger.log(Level.FINE," erreur durant le lancement du serveur"+this,ex);
			return;
		}
	}
	/**
	 * Primitive permettant de "mover" un agent sur ce serveur en vue de son exécution
	 * immédiate.
	 * @param agent l'agent devant être exécuté
	 * @param loader le loader à utiliser pour charger les classes.
	 * @throws Exception
	 */
	protected void startAgent(_Agent agent, BAMAgentClassLoader loader) throws Exception {
		//A COMPLETER
		URI agentServerSite = this.agentServer.site();

		Socket socket = new Socket(agentServerSite.getHost(), agentServerSite.getPort());

		// Creation of a Stream and a ObjectOutputStream to destination
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(outputStream);

		// Retrieve byte code to send
		Jar baseCode = loader.extractCode();

		// Send Jar in BAMAgentClassLoader
		objectOutputStream.writeObject(baseCode);
		// Send Agent (this)
		objectOutputStream2.writeObject(agent);

		// Close the sockets
		objectOutputStream2.close();
		objectOutputStream.close();
		socket.close();
	}
}

