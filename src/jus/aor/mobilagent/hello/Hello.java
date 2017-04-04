package jus.aor.mobilagent.hello;

import java.net.URI;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import jus.aor.mobilagent.kernel._Action;
import jus.aor.mobilagent.kernel.Agent;
import jus.aor.mobilagent.kernel.Starter;

/**
 * Classe de test élémentaire pour le bus à agents mobiles
 * @author  Morat
 */
public class Hello extends Agent{

	 /**
	  * construction d'un agent de type hello.
	  * @param args aucun argument n'est requis
	  */
	 public Hello(Object... args) {
		 super();
	 }
	 /**
	 * l'action à entreprendre sur les serveurs visités  
	 */
	
	protected _Action doIt = new _Action(){
		private static final long serialVersionUID = 4102055378099993883L; //545282842L
		
		public void execute() {
			Starter.get_logger().log(Level.FINE,"Action -> Hello ~");
		}
	};
	
}
