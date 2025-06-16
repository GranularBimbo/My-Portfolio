package com.managers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceManager {
	public void save(Serializable data,String filename) throws Exception {
		try(ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(filename)))){
			oos.writeObject(data);
		}
	}
	
	public Object load(String filename) throws Exception {
		try(ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(Paths.get(filename)))){
			return ois.readObject();
		}
	}
}
