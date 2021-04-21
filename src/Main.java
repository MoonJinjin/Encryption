import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;

public class Main {

	private JFrame frmSavePassword;
	private JTable table;
	private DefaultTableModel model;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmSavePassword.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSavePassword = new JFrame();
		frmSavePassword.setTitle("Save Password");
		frmSavePassword.setBounds(100, 100, 832, 563);
		frmSavePassword.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSavePassword.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Before");
		lblNewLabel.setFont(new Font("Sitka Small", Font.PLAIN, 20));
		lblNewLabel.setBounds(17, 26, 365, 40);
		frmSavePassword.getContentPane().add(lblNewLabel);
		
		JLabel after_label = new JLabel("");
		after_label.setBackground(Color.WHITE);
		after_label.setFont(new Font("Sitka Small", Font.PLAIN, 21));
		after_label.setOpaque(true);
		after_label.setBounds(378, 69, 310, 40);
		after_label.setBorder(new EtchedBorder());
		frmSavePassword.getContentPane().add(after_label);
		
		JTextField textField = new JTextField();
		textField.setFont(new Font("Sitka Small", Font.PLAIN, 21));
		textField.setBounds(17, 69, 310, 40);
		textField.setColumns(10);
		// 키 입력 이벤트
		textField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				after_label.setText(after(textField.getText())); // 단일 치환 결과로 출력
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		frmSavePassword.getContentPane().add(textField);
		
		JLabel after_title = new JLabel("After");
		after_title.setFont(new Font("Sitka Small", Font.PLAIN, 20));
		after_title.setBounds(378, 26, 365, 40);
		frmSavePassword.getContentPane().add(after_title);
		
		JLabel mid_label = new JLabel("->");
		mid_label.setHorizontalAlignment(SwingConstants.CENTER);
		mid_label.setFont(new Font("Sitka Small", Font.PLAIN, 20));
		mid_label.setBounds(332, 69, 40, 40);
		frmSavePassword.getContentPane().add(mid_label);

		JButton btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Sitka Small", Font.PLAIN, 20));
		btnAdd.setBackground(Color.WHITE);
		btnAdd.setBounds(705, 69, 88, 40);
		// 버튼 이벤트
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String input[] = new String[2];
				input[0] = textField.getText(); // before 입력 값 가져오기
				input[1] = after_label.getText(); // after 입력 값 가져오기
				model.addRow(input); // 열 추가
				textField.setText(""); // 텍스트필드 초기화
				after_label.setText("");
			}
		});
		frmSavePassword.getContentPane().add(btnAdd);
		
		String header[] = {"Before", "After"};
		String contents[][] = {};
		model = new DefaultTableModel(contents, header);
		table = new JTable(model);
		table.setFont(new Font("Sitka Small", Font.PLAIN, 20));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN); // 컬럼 길이 자동
		table.setAutoCreateRowSorter(true); // 컬럼 클릭시 행 자동 정렬 활성화
		table.setRowHeight(30); // 열 높이
		
		JScrollPane scroll = new JScrollPane();
		scroll.setBounds(17, 150, 776, 368);
		scroll.setViewportView(table); // 스크롤 안에 테이블 보여주기
		frmSavePassword.getContentPane().add(scroll);
		
	}
	// 단일 치환 코드
	public String after(String s) {
		String answer = "";
	    int z = (int)'z';
	    int Z = (int)'Z';
	    int a = (int)'a';
	    int A = (int)'A';
	    char[] temp = s.toCharArray();
	      
	    for(int i =0; i<temp.length; i++){
	        if(temp[i]>=a&& temp[i]<=z){ // 소문자
	            temp[i] = (int)temp[i]+3 > z ? (char)(a+(int)temp[i]+3-z-1) : (char)((int)temp[i]+3); 
	        }else if(temp[i]>=A&& temp[i]<=Z){ // 대문자
	            temp[i] = (int)temp[i]+3 > Z ? (char)(A+(int)temp[i]+3-Z-1) : (char)((int)temp[i]+3);
	        }else{
	            //공백
	            temp[i]=temp[i];
	        }
	    }
	    answer = new String(temp);
	    return answer;
	}
}
