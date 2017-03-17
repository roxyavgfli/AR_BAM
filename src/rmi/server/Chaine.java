package rmi.server;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rmi.sources._Chaine;
import rmi.utils.Hotel;

public class Chaine implements _Chaine {
	
	private List<Hotel> ListeHotels = new ArrayList<Hotel>();
	
	public Chaine (String uri){
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
					
					this.ListeHotels.add(new Hotel(nom, localisation));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Hotel> get(String localisation) {
		return this.ListeHotels;
	}
	
}
