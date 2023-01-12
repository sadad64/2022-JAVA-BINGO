package projectB;

import java.util.Scanner;

public class CPU {
	private int COL;
	private int ROW;

	String[][] bingoFloor; // 빙고 단어를 저장합니다.
	boolean[][] checkMap; // 빙고 체크 여부를 저장합니다.
	int[][] weight; // CPU의 전략 가중치를 저장합니다.
	
	
	/**
	 * Constructor, 생성자 인자로 테이블의 크기를 받아야 합니다.
	 * @param column 
	 * @param row
	 */
	public CPU(int COL, int ROW) {
		this.COL = COL;
		this.ROW = ROW;
		
		this.bingoFloor = new String[COL][ROW];
		this.checkMap = new boolean[COL][ROW];
		this.weight = new int[COL][ROW];
		System.out.println("CPU : " + COL + " " + ROW + "빙고판이 성공적으로 생성되었습니다.");
	}
	
	/**
	 * 빙고판을 생성하고, 가중치 테이블의 초기값을 조정합니다.
	 * @param
	 */
	public void makeBingo(VocManager voc) {
		generateStr(voc); // 단어 채우기
		setWeight(); // 가중치 세팅
		printBingoCPU();
		
	};
	
	public String[][] generateStr(VocManager voc) {
		// TODO Auto-generated method stub
		for (int i = 0; i < COL; i++) {
			for (int j = 0; j < ROW ; j++) {
				double rand = 0;
				boolean check = false;
				
				while (check == false) {
					check = true;
					rand = Math.random() * voc.getSize();
					
					for (int ia = 0; ia < COL; ia++) {
						for (int ja = 0; ja < ROW; ja++) {
							if (bingoFloor[ia][ja] != null && bingoFloor[ia][ja].equals(voc.getVoc(rand).eng)) {
								check = false;
							}
						}
					}
				}
				
				try {
					bingoFloor[i][j] = voc.getVoc(rand).eng;
					checkMap[i][j] = false;
				} catch (Exception e) {
					return null;
				}

			}
		}
		setWeight();
		printBingoCPU();
		
		return bingoFloor;
	}
	
	/**
	 * CPU의 초기 가중치를 설정합니다.
	 */
	private void setWeight() {
		// TODO Auto-generated method stub
		// 정중앙 가중치
		weight[(int) Math.round((double) COL / 2) - 1][(int) Math.round((double) ROW / 2) - 1] += 50;

		int point = 3; // 모서리
		int edge = 2; // 가장자리
		int line = 1; // 대각선

		// 모서리 가중치
		weight[0][0] += point;
		weight[0][ROW - 1] += point;
		weight[ROW - 1][0] += point;
		weight[COL - 1][ROW - 1] += point;

		// 우측 가중치
		for (int i = 0; i < ROW; i++) {
			weight[0][i] += edge;
		}

		// 좌측 가중치
		for (int i = 0; i < ROW; i++) {
			weight[i][0] += edge;
		}

		// 우측 가중치
		for (int i = 0; i < ROW; i++) {
			weight[i][COL - 1] += edge;
		}

		// 하단 가중치
		for (int i = 0; i < ROW; i++) {
			weight[COL - 1][i] += edge;
		}

		// 좌측 대각선 가중치
		for (int i = 0; i < ROW; i++) {
			weight[i][i] += line;
		}

		// 우측 대각선 가중치
		for (int i = 0; i < ROW; i++) {
			weight[i][(COL - 1) - i] += line;
		}
	}

	/**
	 * 빙고판의 정보를 콘솔에 출력합니다.
	 */
	public void printBingoCPU() {
		for (int i = 0; i < COL; i++) {
			for (int j = 0; j < ROW ; j++) {
				System.out.print(bingoFloor[i][j] + " | ");
				if (j == COL - 1) {
					System.out.println();
				}
			}
		}
		
		System.out.println();
		
		for (int i = 0; i < COL; i++) {
			for (int j = 0; j < ROW ; j++) {
				System.out.print(checkMap[i][j] + " | ");
				if (j == COL - 1) {
					System.out.println();
				}
			}
		}
		
		System.out.println();
		
		for (int i = 0; i < COL; i++) {
			for (int j = 0; j < ROW ; j++) {
				System.out.print(weight[i][j] + " | ");
				if (j == COL - 1) {
					System.out.println();
				}
			}
		}
	}
	
	
	/**
	 * 인자로 받은 값을 탐색하여 빙고판에 체크합니다.
	 * @param
	 */
	public String[][] callBingo(String str) {
		int temp = 0;
		int x = 0, y = 0;
		
		for (int i = 0; i < COL; i++) {
			for (int j = 0; j < ROW ; j++) {
				if (checkMap[i][j] == true) {
					weight[i][j] = 0;
				}
			}
		}
		
		for (int i = 0; i < COL; i++) {
			for (int j = 0; j < ROW ; j++) {
				if (weight[i][j] > temp) {
					temp = weight[i][j];
					x = i;
					y = j;
				}
			}
		}
		
		for (int i = 0; i < COL; i++) {
			for (int j = 0; j < ROW ; j++) {
				if (bingoFloor[i][j].equals(str) ) {
					bingoFloor[i][j] = " O ";
					checkMap[i][j] = true;
					weightUp(i, j);
				}
			}
		}
		
		printBingoCPU();
		return bingoFloor;
	}
	
	/**
	 * 컴퓨터가 빙고를 선택합니다. 선택한 결과를 리턴합니다.
	 * @return CPU의 선택결과
	 */
	public String callBingoCPU() {
		int temp = 0;
		int x = 0, y = 0;
		
		for (int i = 0; i < COL; i++) {
			for (int j = 0; j < ROW ; j++) {
				if (checkMap[i][j] == true) {
					weight[i][j] = 0;
				}
			}
		}
		
		for (int i = 0; i < COL; i++) {
			for (int j = 0; j < ROW ; j++) {
				if (weight[i][j] > temp) {
					temp = weight[i][j];
					x = i;
					y = j;
				}
			}
		}
		
		String str = bingoFloor[x][y];
		
		for (int i = 0; i < COL; i++) {
			for (int j = 0; j < ROW ; j++) {
				if (bingoFloor[i][j].equals(str) ) {
					weightUp(i, j);
				}
			}
		}
		
		return bingoFloor[x][y];
	}

	/**
	 * 빙고판 상태를 확인하고 CPU의 가중치를 추가합니다.
	 */
	private void weightUp(int x, int y) {
		// TODO Auto-generated method stub
		
		/*
		 * 빙고판에 체크가 된 경우 가중치 추가
		 * checkMap 배열에서 T/F 여부를 판단하여 가중치를 추가함.
		 * 
		 * 추가되는 가중치 수치 : upweight
		 * 체크된 빙고 갯수 : weightCount
		 * 가장 많이 체크된 빙고 갯수 : topWeight
		 * 가장 많은 빙고가 체크된 방향 : location
		 * 1 : 가로 / 2 : 세로 / 3 : 대각선 좌 / 4 : 대각선 우
		 * 
		 * 가장 많은 체크가 된 방향을 탐색해 해당 방향 / 위치에 있는 빙고칸들에 가중치 10을 더합니다.
		 */
		
		int upweight = 10;
		int weightCount;
		int topWeight = 0;
		int location = 0;
		
		/*
		 * 1. 좌측 대각선 가중치
		 */
		weightCount = 0;
		if (x == y) {
			for (int i = 0; i < ROW; i++) {
				if (checkMap[i][i]) {
					weightCount++;
				}
				weight[i][i] += upweight;
			}
		}
		
		if (topWeight < weightCount) {
			topWeight = weightCount;
			location = 3;
		}
		System.out.println("wc(Left) : " + weightCount);
		
		/*
		 * 2. 우측 대각선 가중치
		 */
		weightCount = 0;
		if ((x+y) == (ROW - 1)) {
			for (int i = 0; i < ROW; i++) {
				if (checkMap[i][COL-1 - i]) {
					weightCount++;
				}
				weight[i][COL-1 - i] += upweight;
			}
		}
		
		if (topWeight < weightCount) {
			topWeight = weightCount;
			location = 4;
		}
		System.out.println("wc(Rignt) : " + weightCount);
		
		/*
		 * 3. 가로 가중치
		 */
		weightCount = 0;
		for (int i = 0; i < ROW; i++) {
			
			if (checkMap[x][i]) {
				weightCount++;
			}
			weight[x][i] += upweight;
		}

		if (topWeight < weightCount) {
			topWeight = weightCount;
			location = 1;
		}
		System.out.println("wc(GARO) : " + weightCount);

		/*
		 * 4. 세로 가중치
		 */
		weightCount = 0;
		for (int i = 0; i < ROW; i++) {
			if (checkMap[i][y]) {
				weightCount++;
			}
			weight[i][y] += upweight;
		}
		
		if (topWeight < weightCount) {
			topWeight = weightCount;
			location = 2;
		}
		System.out.println("wc(SERO) : " + weightCount);
		

		
		System.out.println("tc : " + topWeight + "  /  location : " + location);
		
		switch(location) {
		case 1 :
			for (int i = 0; i < ROW; i++) {
				weight[x][i] += topWeight * 10;
			}
			break;
			
		case 2 :
			for (int i = 0; i < ROW; i++) {
				weight[i][y] += topWeight * 10;
			}
			break;
			
		case 3 :
			for (int i = 0; i < ROW; i++) {
				weight[i][i] += topWeight * 10;
			}
			break;
		
		case 4 :
			for (int i = 0; i < ROW; i++) {
				weight[i][COL-1 - i] += topWeight * 10;
			}
			break;
		}	
	}

	/**
	 * 컴퓨터 빙고판의 빙고를 체크합니다. 결과값으로 빙고 갯수를 리턴합니다.
	 * @return CPU의 빙고갯수
	 */
	public int checkBingo() {
		int count = 0;
		int bingoCount = 0;
		
		/*
		 * 1. 가로 빙고 체크
		 * x 값은 그대로, y 값이 늘어나는 경우
		 */
		for (int x = 0; x < COL; x++) {
			count = 0;
			for (int y = 0; y < ROW ; y++) {
				if(checkMap[x][y] == true) {
					count++;
				}
				
				if (count == ROW) {
					bingoCount++;
				}
			}
		}
		
		/*
		 * 2. 세로 빙고 체크
		 * y 값은 그대로, x 값이 늘어나는 경우
		 */
		for (int x = 0; x < COL; x++) {
			count = 0;
			for (int y = 0; y < ROW ; y++) {
				if(checkMap[y][x] == true) {
					count++;
				}
				
				if (count == COL) {
					bingoCount++;
				}
			}
		}
		
		/*
		 * 3. 대각선 빙고 체크 
		 * (x, y)가 모두 같은 경우
		 */
		count = 0;
		for (int x = 0; x < COL; x++) {
			if(checkMap[x][x] == true) {
				count++;
			}
				
			if (count == COL) {
				bingoCount++;
			}
		}
		
		/*
		 * 4.역방향 대각선 빙고 체크
		 * 0,4 1,3 2,2 3,1 4,0 -> (x+1, y-1)
		 */
		count = 0;
		for (int x = 0; x < COL; x++) {
			if(checkMap[x][(COL-1)-x] == true) {
				count++;
			}
				
			if (count == COL) {
				bingoCount++;
			}
		}
		
		System.out.println(bingoCount);
		return bingoCount;
	}

	/**
	 * CPU의 빙고판 정보를 O/X로 리턴합니다.
	 * @return CPU의 빙고판
	 */
	public String getField() {
		// TODO Auto-generated method stub
		String str = "* CPU의 빙고판 <hr>";
		
		for (int i = 0; i < COL; i++) {
			str += "| ";
			for (int j = 0; j < ROW ; j++) {
				if (checkMap[i][j]) {
					str += (" O " + " | ");
				} else {
					str += (" X " + " | ");
				}
				
				if (j == COL - 1) {
					str += "<br>";
				}
			}
		}
		str += "<hr>";
		
		return str;
	}
	
	public boolean checkDraw() {
		int unOpen = 0;

		for (int i = 0; i < COL; i++) {
			for (int j = 0; j < ROW ; j++) {
				if (!checkMap[i][j]) {
					unOpen++;
				}
			}
		}
		
		if (unOpen == 0) {
			return true;
		}
		return false;
	}
}
