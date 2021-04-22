import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.Vector;

public class Main extends MouseAdapter {

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
					
					/**
					 * DataBase
					 */
					Connection conn = null;
					PreparedStatement pstmt = null;
					ResultSet rs = null;
					
					if (rs != null) try { rs.close(); } catch(SQLException ex) {}
			        if (pstmt != null) try { pstmt.close(); } catch(SQLException ex) {}
			        if (conn != null) try { conn.close(); } catch(SQLException ex) {}
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
	
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	private JTextField keyField;

	public void connect() {
		try {
			// 접속할 드라이버를 메모리에 올림
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/encryption";
			// 데이터베이스 접속
			conn = DriverManager.getConnection(url, "jinjin", "1234");
			System.out.println("DB 접속 성공");
		} catch(Exception e) {
			System.out.println("DB 접속 오류" + e);
		}
	}
	public void select() {
		try {
			// SQL문
			String sql = "select * from encryption";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			String header[] = {"Before", "After"};
			model = new DefaultTableModel(header, 0) {
				
				@Override
				public boolean isCellEditable(int arg0, int arg1) {
					return false; // 테이블 수정 막기
				}
				
			};
			
			// encryption 테이블에서 불러오기
			while(rs.next()) {				
				Vector record = new Vector();
				record.add(rs.getString("befo"));
				record.add(rs.getString("aft"));
				model.addRow(record); // 열 추가
			}
			System.out.println("select() 성공");
		} catch (Exception e) {
			System.out.println("select() 오류" + e);
		}
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		connect();
		select();
		
		frmSavePassword = new JFrame();
		frmSavePassword.getContentPane().setBackground(Color.WHITE);
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
				if(keyField.getText().length() == 0) { // key가 없으면
					after_label.setText(after(textField.getText(), 3)); // 기본키 : 3 치환 결과로 출력
				} else {
					after_label.setText(after(textField.getText(), Integer.parseInt(keyField.getText()))); // key만큼 미룸
				}
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
				Vector<String> record = new Vector<String>();
				record.add(textField.getText());
				record.add(after_label.getText());
				model.addRow(record); // 열 추가
				
				// DB 추가
				try {
					String sql = "insert into encryption(befo, aft) values(?, ?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, textField.getText());
					pstmt.setString(2, after_label.getText());
					pstmt.executeUpdate();
					textField.setText(""); // 텍스트필드 초기화
					after_label.setText("");
					keyField.setText("");
					System.out.println("DB 추가 성공");
				} catch (Exception e) {
					System.out.println("DB 추가 오류" + e);
				}
			}
		});
		frmSavePassword.getContentPane().add(btnAdd);
		
		
		table = new JTable(model);
		table.setBackground(Color.WHITE);
		table.setFont(new Font("Sitka Small", Font.PLAIN, 20));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN); // 컬럼 길이 자동
		table.setAutoCreateRowSorter(true); // 컬럼 클릭시 행 자동 정렬 활성화
		table.setRowHeight(30); // 열 높이
		table.addMouseListener(this); // copy event
		
		JScrollPane scroll = new JScrollPane();
		scroll.setBounds(17, 150, 776, 368);
		scroll.setViewportView(table); // 스크롤 안에 테이블 보여주기
		frmSavePassword.getContentPane().add(scroll);
		
		keyField = new JTextField();
		keyField.setBounds(753, 31, 40, 27);
		keyField.setColumns(10);
		frmSavePassword.getContentPane().add(keyField);
		
		keyField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(keyField.getText().length() == 0) { // key가 없으면
					after_label.setText(after(textField.getText(), 3)); // 기본키 : 3 치환 결과로 출력
				} else {
					after_label.setText(after(textField.getText(), Integer.parseInt(keyField.getText()))); // key만큼 미룸
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		JLabel lblKey = new JLabel("Key");
		lblKey.setBounds(703, 34, 40, 21);
		frmSavePassword.getContentPane().add(lblKey);
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		int row = table.getSelectedRow();
		String copy = (String) table.getModel().getValueAt(row, 1); // 선택한 데이터의 after 가져오기
		
		StringSelection select = new StringSelection(copy);
	    Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clpbrd.setContents(select, null); // 클립보드에 저장
	    
	    System.out.println("copy 성공");
	}

	// 단일 치환 코드
	public String after(String s, int n) {
		String answer = "";
	    int z = (int)'z'; // 122
	    int Z = (int)'Z'; // 90
	    int a = (int)'a'; // 97
	    int A = (int)'A'; // 65

	    char[] temp = s.toCharArray(); // s를 한 단어씩 temp 배열에 담기
	      
	    for(int i =0; i<temp.length; i++){ // 문자열 길이만큼 반복
	        if(temp[i]>=a && temp[i]<=z){ // 소문자일 때
	            temp[i] = (int)temp[i]+n > z ? (char)(a+(int)temp[i]+n-z-1) : (char)((int)temp[i]+n); // key만큼 단어 미루기
	        }else if(temp[i]>=A && temp[i]<=Z){ // 대문자일 때
	            temp[i] = (int)temp[i]+n > Z ? (char)(A+(int)temp[i]+n-Z-1) : (char)((int)temp[i]+n); 
	        }else{ //공백일 때
	            temp[i]=temp[i];
	        }
	    }
	    answer = new String(temp); // String으로 만들기
	    return answer;
	}
}
