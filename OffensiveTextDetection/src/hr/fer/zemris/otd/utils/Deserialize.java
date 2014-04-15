package hr.fer.zemris.otd.utils;

import hr.fer.zemris.otd.vectors.PostVector;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import org.apache.commons.math3.linear.RealMatrix;

public class Deserialize {

	public static List<PostVector> listPostVectors(String name) {
		FileInputStream fis1 = null;
		ObjectInputStream ois = null;
		try {
			fis1 = new FileInputStream(name);
			ois = new ObjectInputStream(fis1);
			@SuppressWarnings("unchecked")
			List<PostVector> vectors = (List<PostVector>) ois.readObject();
			return vectors;
		} catch (IOException | ClassNotFoundException ex) {
			return null;
		} finally {
			if (fis1 != null) {
				try {
					fis1.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static RealMatrix matrixData(String name) {
		FileInputStream fis1 = null;
		ObjectInputStream ois = null;
		try {
			fis1 = new FileInputStream(name);
			ois = new ObjectInputStream(fis1);
			RealMatrix theta = (RealMatrix) ois.readObject();
			return theta;
		} catch (IOException | ClassNotFoundException ex) {
			return null;
		} finally {
			if (fis1 != null) {
				try {
					fis1.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
