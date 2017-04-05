package jus.aor.mobilagent.kernel;

import java.net.URL;
import java.net.URLClassLoader;


public class BAMServerClassLoader extends URLClassLoader{

	public BAMServerClassLoader(URL[] urls, ClassLoader classLoader) {
		super(urls);
	}
	
	protected void addURL(URL url) {
		super.addURL(url);
	}

	public String toString() {
		return super.toString();
	}
	
}
