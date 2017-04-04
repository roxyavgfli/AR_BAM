package jus.aor.mobilagent.kernel;

class AgentInputStream extends ObjectInputStream{
   /**
    * le classLoader à utiliser
    */
   BAMAgentClassLoader loader;
   AgentInputStream(InputStream is, BAMAgentClassLoader cl) throws IOException{super(is); loader = cl;}
   protected Class<?> resolveClass(ObjectStreamClass cl) throws IOException,ClassNotFoundException{return loader.loadClass(cl.getName());}
}