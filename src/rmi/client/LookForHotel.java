package rmi.client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rmi.utils._Annuaire;
import rmi.utils._Chaine;
import rmi.utils.Hotel;
import rmi.utils.Numero;

/**
 * Représente un client effectuant une requête lui permettant d'obtenir les numéros de téléphone des hôtels répondant à son critère de choix.
 * @author  Morat
 */
public class LookForHotel{
	/** le critère de localisaton choisi */
	private String localisation;
	private int port = 1099;
	private int nbChaines = 4;
	private _Annuaire annuaire;
	private List<_Chaine> chaineList = new ArrayList<_Chaine>();;
	private List<Hotel> hotelList = new ArrayList<Hotel>();
	private HashMap<String, Numero> numberMap = new HashMap<String, Numero>();
	
	// ...
	/**
	 * Définition de l'objet représentant l'interrogation.
	 * @param args les arguments n'en comportant qu'un seul qui indique le critère
	 *          de localisation
	 */
	public LookForHotel(String... args){
		localisation = args[0];
		
		/*if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}*/

		Registry reg;

		// On récupère d'abord toutes les chaines
		for (int i = 1; i <= nbChaines; i++) {
			try {
				reg = LocateRegistry.getRegistry(port + i + 1);
				//System.out.println(reg.list().toString());
				chaineList.add((_Chaine) reg.lookup("chain" + i));
			} catch (Exception e) {
				System.out.println("Le serveur chain" + i + " n'est pas démarré !");
			}
		}
		

		// Puis on récup l'annuaire
		try {
			reg = LocateRegistry.getRegistry(port + 1);
			//System.out.println(reg.list().toString());
			annuaire = (_Annuaire) reg.lookup("annuaire");
		} catch (Exception e) {
			System.out.println("Le serveur annuaire n'est pas démarré !");
		}
	}
	/**
	 * réalise une intérrogation
	 * @return la durée de l'interrogation
	 * @throws RemoteException
	 */
	public long call() {
		long to_return = System.currentTimeMillis();
		System.out.println(chaineList.size());
		for (int i = 0; i < chaineList.size(); i++){
			try {
				hotelList.addAll(chaineList.get(i).get(localisation));
			} catch (RemoteException e) {
				System.out.println("error 3");
				e.printStackTrace();
			}
		}
		
		System.out.println(hotelList.size() + " hotel trouvé a la localisation " + localisation);
		for(Hotel hotel : hotelList){
			try {
				numberMap.put(hotel.name, annuaire.get(hotel.name));
			} catch (RemoteException e) {
				System.out.println("error 4");
				e.printStackTrace();
			}
		}
		
		System.out.println(numberMap.size() + " numéro trouvé a la localisation " + localisation);
		
		return (System.currentTimeMillis() - to_return);
	}

	public static void main(String[] args) {
		if (args.length != 1){
			System.out.println("Veuillez mettre une localisation en argument");
			System.exit(1);
		}
		
		LookForHotel lookForHotel = new LookForHotel(args[0]);
		
		long time = lookForHotel.call();
		
		System.out.println("La recherche a prise : " + time + "ms");
	}
}

