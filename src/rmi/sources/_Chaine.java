package rmi.sources;
/**
 * J<i>ava</i> U<i>tilities</i> for S<i>tudents</i>
 */

import java.util.List;

import rmi.utils.Hotel;

/**
 * Définit une chaine d'hôtels et une fonctionnalité permettant d'obtenir l'ensemble des hotels de cette chaine
 * pour une localisation donnée.
 * @author Morat 
 */
public interface _Chaine {
	/**
	 * Restitue la liste des hotels situés dans la localisation.
	 * @param localisation le lieu où l'on recherche des hotels
	 * @return la liste des hotels trouvés
	 */
	public abstract List<Hotel> get(String localisation) ;
}