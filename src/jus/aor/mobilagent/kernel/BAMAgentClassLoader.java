
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
	
	private Map<String, byte[]> classes;

	public BAMAgentClassLoader(ClassLoader parent) {
		super(parent);
		this.classes = new HashMap<String, byte[]>();
	}

	public BAMAgentClassLoader(String jarFilePath, ClassLoader parent) throws JarException, IOException {
		this(parent);
		Jar jar = new Jar(jarFilePath);
		this.integrateCode(jar);
	}

	private String className(String classFilePath) {
		return classFilePath.replace(".class", "").replace("/", ".");
	}

	public Jar extractCode() throws JarException, IOException {
		File tmpJar = File.createTempFile("temporaryJar", ".jar");


		try (JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tmpJar))) {
			for (String className : this.classes.keySet()) {
				jarOutputStream.putNextEntry(new JarEntry(className));
				jarOutputStream.write(this.classes.get(className));
			}
			jarOutputStream.close();
		}
		return new Jar(tmpJar.getPath());
	}


	public void integrateCode(Jar jar) {
		for (Entry<String, byte[]> entree : jar) {
			String className = this.className(entree.getKey());
			this.classes.put(className, entree.getValue());

			// Define and resolve the class to be able to use it
			Class<?> classe = this.defineClass(className, entree.getValue(), 0, entree.getValue().length);
			super.resolveClass(classe);
		}
	}
}