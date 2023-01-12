package projectB;

import java.util.Scanner;

public class Player {
	private int COL;
	private int ROW;
	
	String[][] bingoFloor; // 빙고 단어를 저장합니다.
	boolean[][] checkMap; // 빙고 체크 여부를 저장합니다.
	
	
	/**
	 * Constructor, 생성자 인자로 테이블의 크기를 받아야 합니다.
	 * @param column 
	 * @param row
	 */
	public Player(int COL, int ROW) {
		this.COL = COL;
		this.ROW = ROW;
		
		this.bingoFloor = new String[COL][ROW];
		this.checkMap = new boolean[COL][ROW];
		System.out.println("Player : " + COL + " " + ROW + "빙고판이 성공적으로 생성되었습니다.");
	}
	
	public void makeBingo(VocManager voc) {
		
		generateStr(voc);
		printBingo();
		
		while(true) {
			Scanner scan = new Scanner(System.in);
			System.out.print("단어를 입력하세요 : ");
			String str = scan.nextLine();
			callBingo(str);
			printBingo();
			checkBingo();
		}
		
	}
	
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
		
		return bingoFloor;
	}

	public void printBingo() {
		
		for (int i = 0; i < COL; i++) {
			for (int j = 0; j < ROW ; j++) {
				System.out.print(bingoFloor[i][j] + " | ");
				if (j == COL - 1) {
					System.out.println();
				}
			}
		}
		
//		System.out.println();
		
//		for (int i = 0; i < COL; i++) {
//			for (int j = 0; j < ROW ; j++) {
//				System.out.print(checkMap[i][j] + " | ");
//				if (j == COL - 1) {
//					System.out.println();
//				}
//			}
//		}
	}
	
	public String[][] callBingo(String str) {
		for (int i = 0; i < COL; i++) {
			for (int j = 0; j < ROW ; j++) {
				if (bingoFloor[i][j].equals(str) ) {
					this.bingoFloor[i][j] = " O ";
					this.checkMap[i][j] = true;
				}
			}
		}
		
		return bingoFloor;
	}

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
