package projectB;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VocManager {
	private String userName;
	List<Word> voc = new ArrayList<>();

	static Scanner scan = new Scanner(System.in);
	
	VocManager(){
		
	}
	
	void addWord(Word word) {
		voc.add(word);
	}
	
	void makeVoc(String fileName) {
		
		try(Scanner scan = new Scanner(new File(fileName))){
			while(scan.hasNextLine()) {
				String str = scan.nextLine();
				String[] temp = str.split("\t");
				this.addWord(new Word(temp[0].trim(), temp[1].trim()));
			}
			System.out.println("단어장이 생성되었습니다. \n");
			
		}catch(FileNotFoundException e) {
			System.out.println("단어장이 생성되지 않았습니다. \n파일명을 확인하세요.");
		}
		
	}
	
	Word getVoc(double rand) {
		try {
			return voc.get((int) rand);
		} catch (Exception e) {
			return null;
		}
	}
	
	public String getMeaning(String str) {
		String res = null;
		for (Word word : voc) {
			if (word.eng.equals(str)) {
				res = " (" + word.kor + ")";
			}
		}
		
		if (res == null) {
			res = " (등록되지 않은 단어!)";
			return res;
		}
		
		return res;
		
	}
	
	public boolean checkMeaning(String str) {
		boolean check = true;
		String res = null;
		
		for (Word word : voc) {
			if (word.eng.equals(str)) {
				res = word.kor;
			}
		}
		
		if (res == null) {
			check = false;
			return check;
		}
		
		return check;
	}

	public int getSize() {
		// TODO Auto-generated method stub
		return voc.size();
	}
}
