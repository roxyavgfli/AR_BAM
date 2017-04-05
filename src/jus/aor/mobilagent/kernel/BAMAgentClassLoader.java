
package jus.aor.mobilagent.kernel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarException;
import java.util.jar.JarOutputStream;

public class BAMAgentClassLoader extends ClassLoader {
	
	private Map<String, byte[]> pClasses;

	/**
	 *
	 * @param aParent
	 *            Parent classloader
	 */
	public BAMAgentClassLoader(ClassLoader aParent) {
		super(aParent);
		this.pClasses = new HashMap<String, byte[]>();
	}

	/**
	 * @param aParent
	 *            Parent classloader
	 * @throws IOException
	 * @throws JarException
	 */
	public BAMAgentClassLoader(String aJarFilePath, ClassLoader aParent) throws JarException, IOException {
		this(aParent);
		Jar wJar = new Jar(aJarFilePath);
		this.integrateCode(wJar);
	}

	/**
	 * Return the name of a class based on the given file path
	 *
	 * @param aClassFilePath
	 *            path to a file containing a class
	 * @return String
	 */
	private String className(String classFilePath) {
		return classFilePath.replace(".class", "").replace("/", ".");
	}

	/**
	 * Return a Jar object containing all the classes found in the
	 * BamAgentClassLoader
	 *
	 * To do this, a temporary Jar file is created.
	 *
	 * @return Jar containing all classes in the current BamAgentClassLoader
	 * @throws JarException
	 * @throws IOException
	 */
	public Jar extractCode() throws JarException, IOException {
		File tmpJar = File.createTempFile("temporaryJar", ".jar");

		// Try to create an OutputStream and JarOutputStream on the temporary
		// Jar file just created
		try (JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tmpJar))) {
			for (String className : this.pClasses.keySet()) {
				jarOutputStream.putNextEntry(new JarEntry(className));
				jarOutputStream.write(this.pClasses.get(className));
			}
			jarOutputStream.close();
		}
		// Create the Jar object using the Jar file
		return new Jar(tmpJar.getPath());
	}

	/**
	 * Load all the classes in the Jar aJar and into the ClassLoader
	 *
	 * @param aJar
	 *            Jar to integrate
	 */
	public void integrateCode(Jar jar) {
		for (Entry<String, byte[]> entree : jar) {
			String className = this.className(entree.getKey());
			this.pClasses.put(className, entree.getValue());

			// Define and resolve the class to be able to use it
			Class<?> wClass = this.defineClass(className, entree.getValue(), 0, entree.getValue().length);
			super.resolveClass(wClass);
		}
	}
}