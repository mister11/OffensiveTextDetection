package hr.fer.zemris.otd.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Serialize {

	public static void object(Object object, String name) {
		FileOutputStream fos1 = null;
		ObjectOutputStream oos = null;
		try {
			fos1 = new FileOutputStream(name);
			oos = new ObjectOutputStream(fos1);
			oos.writeObject(object);
		} catch (IOException ex) {
			System.err.println("Serialization failed!");
			return;
		} finally {
			if (fos1 != null) {
				try {
					fos1.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
