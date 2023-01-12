package projectB;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RateManager {

	// Constructor
	public RateManager() {
		super();
	}
	

	/*
	 * calcRate()
	 * 승률 계산함수, json 파일정보를 인자로 받습니다.
	 */
    public String calcRate(File file) {
		// TODO Auto-generated method stub
    	Object obj = null;
    	double calcRes = 0;
		try {
			// json에서 데이터 읽어오기
			obj = new JSONParser().parse(new FileReader(file.getName()));
	        JSONObject js = (JSONObject) obj;

	        String w1 = (String) js.get("win");
	        String l1 = (String) js.get("lose");
	        String d1 = (String) js.get("draw");
	        
	        // Double 형으로 변환
	        double w2 = Double.parseDouble(w1);
	        double l2 = Double.parseDouble(l1);
	        double d2 = Double.parseDouble(d1);
	        
	        calcRes = w2 / (w2 + l2 + d2) * 100; 
	        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("기록이 저장될 json 파일이 감지되지 않았습니다.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("승률계산 중에 오류가 발생했습니다.");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("데이터를 읽어들이는데 실패했습니다!");
		}
		
		String str = String.format("승률 : %.2f%%", calcRes);
		return str;
	}

    /*
     * json 파일의 유무를 확인하는 함수.
     * 파일이 없으면 새로운 파일을 생성한다.
     */
	@SuppressWarnings("unchecked")
	public void checkFile(File file) throws IOException {
		// TODO Auto-generated method stub
    	if (!file.exists()) {
    		System.out.println("파일생성!");
            JSONObject jo = new JSONObject();
            jo.put("name", "Player");
            jo.put("win", "0");
            jo.put("lose", "0");
            jo.put("draw", "0");

            String jsonStr = jo.toString();
            writeStringToFile(jsonStr, file);
    	}
	}

	/*
	 * 기록 저장을 수행하는 함수
	 */
	public void writeStringToFile(String str, File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file.getName()));
        System.out.println(file.getName() + "에 기록 저장하였습니다.");
        System.out.println(str);
        writer.write(str);
        writer.close();
    }

	/*
	 * 승리횟수, 패배횟수, 무승부횟수를 불러와 출력하는 함수 (개발용)
	 */
    public void loadFile(File file) {
    	Object obj = null;
		try {
			obj = new JSONParser().parse(new FileReader(file.getName()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        JSONObject js = (JSONObject) obj;

        String name = (String) js.get("name");
        String w1 = (String) js.get("win");
        String l1 = (String) js.get("lose");
        String d1 = (String) js.get("draw");

        System.out.println("이름: " + name);
        System.out.println("Win " + w1);
        System.out.println("lose: " + l1);
        System.out.println("draw: " + d1);
    }
    
    /*
     * 기록을 저장하는 함수
     */
    @SuppressWarnings("unchecked")
	public void saveRecord(File file, int type) {
    	Object obj = null;
    	int tempCount;
    	
    	try {
			obj = new JSONParser().parse(new FileReader(file.getName()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	JSONObject js = (JSONObject) obj;

    	String name = (String) js.get("name");
    	String win = (String) js.get("win");
    	String lose = (String) js.get("lose");
    	String draw = (String) js.get("draw");
    	
    	// 승리한 경우 1, 패배한 경우 2, 무승부인 경우 3
		switch (type) {
		case 1:
			tempCount = Integer.parseInt(win);
			tempCount += 1;
			win = Integer.toString(tempCount);
			break;
		case 2:
			tempCount = Integer.parseInt(lose);
			tempCount += 1;
			lose = Integer.toString(tempCount);
			break;
		case 3:
			tempCount = Integer.parseInt(draw);
			tempCount += 1;
			draw = Integer.toString(tempCount);
			break;
		}
    	
        JSONObject jo = new JSONObject();
        jo.put("name", name);
        jo.put("win", win);
        jo.put("lose", lose);
        jo.put("draw", draw);

        String jsonStr = jo.toString();

        try {
			writeStringToFile(jsonStr, file);
			calcRate(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public String getInfo(File file) {
    	Object obj = null;
    	int tempCount;
    	
    	try {
			obj = new JSONParser().parse(new FileReader(file.getName()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	JSONObject js = (JSONObject) obj;
    	
    	String str = "승리 횟수 : " + (String) js.get("win") + "\n";
    	str += "패배 횟수 : " + (String) js.get("lose") + "\n";
    	str += "무승부 횟수 : " + (String) js.get("draw") + "\n";
    	
    	return str;
    }
}
