package it.univaq.disim.oop.noleggioveicoliunivaq.business.impl.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Utility {

	public static final String SEPARATORE_COLONNA = ",";

	public static final String[] trim(String[] s) {
		for (int i = 0; i < s.length; i++) {
			s[i] = s[i].trim();
		}
		return s;
	}

	public static FileData readAllRows(String filename) throws IOException {

		FileData result = new FileData();

		try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(filename)))) {
			List<String[]> righe = new ArrayList<>();
			long contatore = Long.parseLong(scanner.nextLine());
			result.setContatore(contatore);
			while (scanner.hasNextLine()) {
				righe.add(trim(scanner.nextLine().split(SEPARATORE_COLONNA)));
			}
			result.setRighe(righe);
		}

		return result;
	}

}