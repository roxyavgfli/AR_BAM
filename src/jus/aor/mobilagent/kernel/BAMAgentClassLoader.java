package jus.aor.mobilagent.kernel;


public class BAMAgentClassLoader extends ClassLoader {
	
	// HashMap pour stocker le code des classes
	private Jar jar;


	/*
	 * Constructeur qui prend en paramètre un class loader parent
	 */
	public BAMAgentClassLoader(ClassLoader cl){
		super(cl); // Appel au constructeur de classloader
	}
	
	/*
	 * Constructeur qui prend en paramètre un class loader parent & un jar à stocker
	 */
	public BAMAgentClassLoader(ClassLoader cl, Jar jar){
		super(cl);
		this.jar = jar;
	}
	
	/*
	 * Méthode servant à stocker le code des classes récupérées dans le jar
	 */
	public void integrateCode (Jar jar){
		this.jar = jar;
	}
	
	/*
	 * Méthode servant à récupérer le code des classes précédemment stockées
	 */
	public Jar extractCode (){
		try {
			return this.jar;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}