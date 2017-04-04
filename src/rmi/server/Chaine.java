package rmi.server;

import java.io.File;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rmi.utils._Chaine;
import rmi.utils.Hotel;

public class Chaine extends UnicastRemoteObject implements _Chaine{
	
	private List<Hotel> listeHotels = new ArrayList<Hotel>();
	
	public Chaine (String uri) throws RemoteException {
		try{
			File fXmlFile = new File(uri);
			System.out.println("Fichier : " + fXmlFile.getAbsolutePath());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("Hotel");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				NamedNodeMap attributes = nNode.getAttributes();
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					
					Element eElement = (Element) nNode;
					
					String localisation = attributes.getNamedItem("localisation").getNodeValue();;
					String nom = attributes.getNamedItem("name").getNodeValue();
					
					this.listeHotels.add(new Hotel(nom, localisation));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Hotel> get(String localisation) {
		List<Hotel> res = new ArrayList<Hotel>();

		for (Hotel hotel : listeHotels) {
			if (localisation.equals(hotel.localisation)) {
				res.add(hotel);
			}
		}

		return res;
	}
	
	public static void main(String[] args) {
		
		int port = 1099;
		int numeroChaine;
		
		
		//Récupération du paramètre
		try {
			numeroChaine = Integer.parseInt(args[0]);
			
			// Sécurity manager : charge certaines classes dynamiquement
			/*if (System.getSecurityManager() == null) {
				System.setSecurityManager(new SecurityManager());
			}*/
			
			try {
				// Déclaration du registry
				Registry reg;
			
				// Enregistrement des chaines dans le registry
				reg = LocateRegistry.createRegistry(port + numeroChaine + 1);
				
				// Bind de chaine dans registry
				Chaine c = new Chaine("DataStore/Hotels" + numeroChaine + ".xml");
				reg.bind("chain" + numeroChaine, c);
				System.out.println("Chemin " + numeroChaine + " bind avec succès.");
			
			} catch (Exception e) {
				System.out.println("Server err: " + e.getMessage()); 
	            e.printStackTrace(); 
			}
		} catch (Exception e) {
			System.out.println("Entrez le numéro de la chaine d'hotel.");
			System.exit(1);
		}
		
		

	}
}
