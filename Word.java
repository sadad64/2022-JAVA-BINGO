package projectB;

public class Word implements Comparable<Word> {
	String eng;
	String kor;
	int count;
	
	public Word(String eng, String kor) {
		super();
		this.eng = eng;
		this.kor = kor;
		this.count = 0;
	}
		
	public int getCount() {
		return count;
	}

	public void upCount() {
		this.count++;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub		
		return eng + " : " + kor;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		Word tmp = (Word)obj;
		boolean result = this.eng.equals(tmp.eng) && this.kor.equals(tmp.kor);
		return result;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.eng.hashCode() + this.kor.hashCode();
 	}
	
	@Override
	public int compareTo(Word o) {
		return (this.count - o.count)*-1;
	}
	
}
