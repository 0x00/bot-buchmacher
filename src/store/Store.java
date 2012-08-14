package store;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Store implements Serializable{

	private static final long serialVersionUID = 1L;

	public static Object load(String name){
		try {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(name);
			} catch (FileNotFoundException e1) {
				//e1.printStackTrace();
			}
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(fis);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Object out = null;
			try {
				out =  in.readObject();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return out;
		} catch (NullPointerException e) {
		}
		return null;
	}
	
	public void save(String name){
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(name);
			out = new ObjectOutputStream(fos);
			out.writeObject(this);
			out.close();
		} catch (IOException e) {
		}
	}
	public static void save(Object ob, String name){
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try {
			fos = new FileOutputStream(name);
			out = new ObjectOutputStream(fos);
			out.writeObject(ob);
			out.close();
		} catch (IOException e) {
		}
	}
}
