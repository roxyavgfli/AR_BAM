/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */
package jus.aor.mobilagent.kernel;

import java.io.Serializable;
import java.util.logging.Level;

/**
 * Définit une action à exécuter par un agent.
 * @author  Morat
 */
public interface _Action extends Serializable{
	/** l'action vide */
	public static final _Action NIHIL = new _Action(){
		private static final long serialVersionUID = 4102055378099993883L; //DONE
		public void execute(){
			Starter.get_logger().log(Level.FINE,"Action -> NIHIL");
		}
	};
	/**
	 * Exécute l'action
	 */
	public void execute();
}
