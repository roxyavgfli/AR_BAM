package rmi;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rmi.server.Annuaire;
import rmi.utils.Numero;

public class Test {
	
	public static void main(String[] args){
		String uri = "src/rmi/data/Annuaire.xml";
		Annuaire annuaire = new Annuaire(uri);
		HashMap<String, Numero> rep = annuaire.getRepertoire();
		Iterator it = rep.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
		}
	}
}
