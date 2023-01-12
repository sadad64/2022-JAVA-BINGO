package projectB;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener {
	/*
	 * 초기 입력값 받아오는 다이얼로그 띄우기
	 */
	static String inputSize = JOptionPane.showInputDialog("크기를 입력하세요.");
	static int externalFile = JOptionPane.showConfirmDialog(null, "외부 단어장을 사용하시겠습니까?", "외부 단어장 사용", JOptionPane.YES_NO_OPTION);
	static int size = getSize(inputSize);
	
	/*
	 * Swing UI 세팅
	 */
	Container contentPane = this.getContentPane();
	
	
	JPanel bingoPanel = new JPanel(new GridLayout(size,size));
	JPanel topPanel = new JPanel();
	JPanel bottomPanel = new JPanel(new GridLayout(3,1));
	JPanel leftPanel = new JPanel();
	JPanel rightPanel = new JPanel();
	JPanel consolePanel = new JPanel(new BorderLayout());
	
	JMenuBar bar = new JMenuBar();
	JMenu menu = new JMenu("메뉴");
	JMenuItem item2 = new JMenuItem("프로그램 종료");
	
	JMenu menu2 = new JMenu("설정");
	JMenuItem item22 = new JMenuItem("승률 확인");
	
	JPanel inputPanel = new JPanel();
	JPanel prevPanel = new JPanel();
	JTextField inputField = new JTextField(20);
	JLabel prevField = new JLabel("이전 단어 : -");
	
	JLabel turn = new JLabel("플레이어의 순서");
	JLabel pCountField = new JLabel("플레이어의 빙고 : -");
	JLabel cCountField = new JLabel("CPU의 빙고 : -");
	
	/*
	 * 전역변수 선언
	 */
	int pBingoCount, cBingoCount; // 빙고갯수 카운터 선언
	static Player player = new Player(size, size); // 플레이어
	static CPU cpu = new CPU(size, size); // CPU 플레이어
	
	VocManager voc = new VocManager(); // 단어 관리자
	File jsonFile = new File("BingoRecord.json"); // 기록이 저장될 json 파일
	RateManager rate = new RateManager(); // 기록 관리자
	
	static String[][] pBingo; // 플레이어 빙고판
	static String[][] cBingo; // CPU 빙고판
	
	MainFrame(String title) {
		/*
		 * default setting
		 */
		super(title);
		this.setSize(1000, 1000);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		/*
		 * Set container size
		 */
		bottomPanel.setPreferredSize(new Dimension(1000, 100));
		topPanel.setPreferredSize(new Dimension(500, 70));
		leftPanel.setPreferredSize(new Dimension(150, 1000));
		rightPanel.setPreferredSize(new Dimension(150, 1000));
		/*
		 * Player setting
		 */
		if(externalFile == JOptionPane.NO_OPTION) {
			voc.makeVoc("txt/test.txt"); // 기본 단어장 생성
		} else if(externalFile == JOptionPane.YES_OPTION) {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"TXT File", "txt");
					chooser.setFileFilter(filter);
					
					int res = chooser.showOpenDialog(null);
					
					try {
						if(res != JFileChooser.APPROVE_OPTION) {
							JOptionPane.showMessageDialog(null,
							"파일을 선택하지 않았습니다.",
							"경고", JOptionPane.WARNING_MESSAGE);
							return;
						}
						String filePath = chooser.getSelectedFile().getPath();
						voc.makeVoc(filePath);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(this, "올바른 파일을 선택해주십시오.");
					}
			
		}
		
		pBingo = player.generateStr(voc); // 단어장으로부터 단어 랜덤하게 생성해서 빙고판에 채우기
		cBingo = cpu.generateStr(voc);
		
		if (pBingo == null || cBingo == null) {
			JOptionPane.showMessageDialog(this, "단어장이 유효하지 않습니다. 올바른 양식의 파일을 선택해주십시오.");
			System.exit(0); // 단어장이 유효하지 않을 시 다이얼로그 띄운 후 프로그램 종료
		}
		
		bingoPanel.removeAll(); // 패널 초기화
		
		// 빙고판에 단어 채우기
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				String text = pBingo[i][j];
				JButton button = new JButton(text);
				bingoPanel.add(button);
			}
		}
		
		init();
		paneAdd();
		
		try {
			rate.checkFile(jsonFile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		this.inputField.addActionListener(e -> {
			pTurn();
		});
		
		this.setVisible(true);
	}

	private void cTurn() {
		// TODO Auto-generated method stub
		String res = cpu.callBingoCPU();
		
		System.out.println(res);
		cBingo = cpu.callBingo(res);
		pBingo = player.callBingo(res);
		prevField.setText("이전 단어 : " + res + voc.getMeaning(res));

		bingoPanel.removeAll();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				String text = pBingo[i][j];
				JButton button = new JButton(text);
				bingoPanel.add(button);
			}
		}
		pBingoCount = player.checkBingo();
		pCountField.setText("플레이어의 빙고 : " + pBingoCount);
		cBingoCount = cpu.checkBingo();
		cCountField.setText("CPU의 빙고 : " + cBingoCount);

		bingoPanel.revalidate();
		bingoPanel.repaint();
		
	}

	private void pTurn() {
		// TODO Auto-generated method stub
		pBingo = player.callBingo(inputField.getText());
		cBingo = cpu.callBingo(inputField.getText());
		System.out.println(inputField.getText());
		
		if(!voc.checkMeaning(inputField.getText())) {
			prevField.setForeground(Color.RED);
		}
		
		prevField.setText("이전 단어 : " + inputField.getText() + voc.getMeaning(inputField.getText()));
		inputField.setText("");
		
		bingoPanel.removeAll();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				String text = pBingo[i][j];
				JButton button = new JButton(text);
				bingoPanel.add(button);
			}
		}
		
		pBingoCount = player.checkBingo();
		pCountField.setText("플레이어의 빙고 : " + pBingoCount);
		cBingoCount = cpu.checkBingo();
		cCountField.setText("CPU의 빙고 : " + cBingoCount);
		turn.setText("CPU의 순서");
		
		bingoPanel.revalidate();
		bingoPanel.repaint();
		
		inputField.setEnabled(false);
		
		try {
			new java.util.Timer().schedule(
			        new java.util.TimerTask() {
			            @Override
			            public void run() {
			                // your code here
			            	prevField.setForeground(Color.black);
			            	cTurn();
			            	if (!checkWinner()) {
				            	turn.setText("Player의 순서");
				            	inputField.setEnabled(true);
			            	}
			            }
			        },
			        3000
			);
		} catch (Exception e) {
			
		}

	}

	private boolean checkWinner() {
		// TODO Auto-generated method stub
		if (pBingoCount > 0 && pBingoCount > cBingoCount) {
			rate.saveRecord(jsonFile, 1);
			cCountField.setText("<html><body>" + cpu.getField());
			turn.setText(
					"<html><body>Player 승리<hr>" + 
					"플레이어의 전적 : " + rate.getInfo(jsonFile) + 
					"<br>" + rate.calcRate(jsonFile) +
					"</body></html>");
			
			JOptionPane.showMessageDialog(null, "Player (" + rate.calcRate(jsonFile) + ")\n" + rate.getInfo(jsonFile), "승리!", JOptionPane.INFORMATION_MESSAGE);
			
			return true;
		}
		
		if (cBingoCount > 0 && cBingoCount > pBingoCount) {
			rate.saveRecord(jsonFile, 2);
			cCountField.setText("<html><body>" + cpu.getField());
			turn.setText(
					"<html><body>CPU 승리<hr>" + 
					"플레이어의 전적 : " + rate.getInfo(jsonFile) + 
					"<br>" + rate.calcRate(jsonFile) +
					"</body></html>");
			
			JOptionPane.showMessageDialog(null, "Player (" + rate.calcRate(jsonFile) + ")\n" + rate.getInfo(jsonFile), "패배!", JOptionPane.INFORMATION_MESSAGE);
			
			return true;
		}
		
		if (player.checkDraw() && cpu.checkDraw()) {
			rate.saveRecord(jsonFile, 3);
			turn.setText(
					"<html><body>빙고판이 전부 오픈되었습니다! 무승부!<hr>" + 
					"플레이어의 전적 : " + rate.getInfo(jsonFile) + 
					"<br>" + rate.calcRate(jsonFile) +
					"</body></html>");

			JOptionPane.showMessageDialog(null, "Player (" + rate.calcRate(jsonFile) + ")\n" + rate.getInfo(jsonFile), "무승부!", JOptionPane.INFORMATION_MESSAGE);
			
			return true;
		}
		
		return false;
		
	}
	
	/**
	 * 다이얼로그 창으로부터 입력된 빙고판의 크기를 받아옵니다.
	 * <p>
	 * @param
	 */
	public static int getSize(String inputSize) {
		int size = 0;
		boolean pass = true;
		
		while(pass) {
			try {
				size = Integer.parseInt(inputSize);
				if (size > 7 || size < 3) {
					inputSize = JOptionPane.showInputDialog( "빙고판의 범위는 3~7 이내여야 합니다.");
					pass = true;
				} else {
					pass = false;
				}
			} catch (Exception e) {
				inputSize = JOptionPane.showInputDialog("잘못된 입력입니다. 숫자로 입력하세요.");
				if (inputSize == null) {
					System.exit(0);
				}
				pass = true;
			}
		}
		
		return size;
	};

	private void paneAdd() {
		// TODO Auto-generated method stub
		contentPane.add(bingoPanel, BorderLayout.CENTER);
		contentPane.add(topPanel, BorderLayout.NORTH);
		contentPane.add(bottomPanel, BorderLayout.SOUTH);
		contentPane.add(leftPanel, BorderLayout.WEST);
		contentPane.add(rightPanel, BorderLayout.EAST);
		
		bottomPanel.add(consolePanel);
	}
	
	public void init() {
		prevPanel.add(prevField);
		inputPanel.add(new JLabel("단어 입력 : "));
		inputPanel.add(inputField);
		bottomPanel.add(prevPanel);
		bottomPanel.add(inputPanel);
		topPanel.add(turn, BorderLayout.CENTER);
		leftPanel.add(pCountField);
		rightPanel.add(cCountField);
		
		menu.add(item2);
		menu2.add(item22);
		
		
		item2.addActionListener(e -> {
			int result = JOptionPane.showConfirmDialog(null,
					"정말 종료하시겠습니까?", "게임에서 나가기",
					JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.OK_OPTION) {
						System.exit(0);
					} else {
						
					}
		});
		
		item22.addActionListener(e -> {
			int result = JOptionPane.showConfirmDialog(null,
					"Player (" + rate.calcRate(jsonFile) + ")\n" + rate.getInfo(jsonFile), "승률정보",
					JOptionPane.HEIGHT, -1);
		});

		bar.add(menu);
		bar.add(menu2);
		this.setJMenuBar(bar);

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainFrame mf = new MainFrame("202212188 서정권");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
