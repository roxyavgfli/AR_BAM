package rmi.server;

import java.io.File;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rmi.utils._Annuaire;
import rmi.utils.Numero;

public class Annuaire extends UnicastRemoteObject implements _Annuaire{
	
	private HashMap<String,Numero> repertoire = new HashMap<String, Numero>();

	public Annuaire(String uri) throws RemoteException{
		/* Parser le XML et ajouter les entrées au HashMap */
		try{
			File fXmlFile = new File(uri);
//			System.out.println("Fichier : " + fXmlFile.getAbsolutePath());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();
			
			NodeList nList = doc.getElementsByTagName("Telephone");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				NamedNodeMap attributes = nNode.getAttributes();
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					
					Element eElement = (Element) nNode;
					
					Numero numero = new Numero(attributes.getNamedItem("numero").getNodeValue());
					String nom = attributes.getNamedItem("name").getNodeValue();
					
					this.repertoire.put(nom, numero);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Numero get(String abonne) {
		return this.repertoire.get(abonne);
	}
	
	public HashMap<String, Numero> getRepertoire(){
		return this.repertoire;
	}
	
	public static void main(String[] args) {
		
		int port = 1099;
		
		// Sécurity manager
		/*if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}*/
		
		try {
			// Déclaration du registry
			Registry reg;

			// Enregistrement des entrées de l'annuaire
			reg = LocateRegistry.createRegistry(port + 1);
			Annuaire a = new Annuaire("DataStore/Annuaire.xml");
			reg.bind("annuaire", a);
			System.out.println("Annuaire créé avec succès");


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
