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
			// ������ ����̹��� �޸𸮿� �ø�
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/encryption";
			// �����ͺ��̽� ����
			conn = DriverManager.getConnection(url, "jinjin", "1234");
			System.out.println("DB ���� ����");
		} catch(Exception e) {
			System.out.println("DB ���� ����" + e);
		}
	}
	public void select() {
		try {
			// SQL��
			String sql = "select * from encryption";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			String header[] = {"Before", "After"};
			model = new DefaultTableModel(header, 0) {
				
				@Override
				public boolean isCellEditable(int arg0, int arg1) {
					return false; // ���̺� ���� ����
				}
				
			};
			
			// encryption ���̺��� �ҷ�����
			while(rs.next()) {				
				Vector record = new Vector();
				record.add(rs.getString("befo"));
				record.add(rs.getString("aft"));
				model.addRow(record); // �� �߰�
			}
			System.out.println("select() ����");
		} catch (Exception e) {
			System.out.println("select() ����" + e);
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
		// Ű �Է� �̺�Ʈ
		textField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(keyField.getText().length() == 0) { // key�� ������
					after_label.setText(after(textField.getText(), 3)); // �⺻Ű : 3 ġȯ ����� ���
				} else {
					after_label.setText(after(textField.getText(), Integer.parseInt(keyField.getText()))); // key��ŭ �̷�
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
		// ��ư �̺�Ʈ
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Vector<String> record = new Vector<String>();
				record.add(textField.getText());
				record.add(after_label.getText());
				model.addRow(record); // �� �߰�
				
				// DB �߰�
				try {
					String sql = "insert into encryption(befo, aft) values(?, ?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, textField.getText());
					pstmt.setString(2, after_label.getText());
					pstmt.executeUpdate();
					textField.setText(""); // �ؽ�Ʈ�ʵ� �ʱ�ȭ
					after_label.setText("");
					keyField.setText("");
					System.out.println("DB �߰� ����");
				} catch (Exception e) {
					System.out.println("DB �߰� ����" + e);
				}
			}
		});
		frmSavePassword.getContentPane().add(btnAdd);
		
		
		table = new JTable(model);
		table.setBackground(Color.WHITE);
		table.setFont(new Font("Sitka Small", Font.PLAIN, 20));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN); // �÷� ���� �ڵ�
		table.setAutoCreateRowSorter(true); // �÷� Ŭ���� �� �ڵ� ���� Ȱ��ȭ
		table.setRowHeight(30); // �� ����
		table.addMouseListener(this); // copy event
		
		JScrollPane scroll = new JScrollPane();
		scroll.setBounds(17, 150, 776, 368);
		scroll.setViewportView(table); // ��ũ�� �ȿ� ���̺� �����ֱ�
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
				if(keyField.getText().length() == 0) { // key�� ������
					after_label.setText(after(textField.getText(), 3)); // �⺻Ű : 3 ġȯ ����� ���
				} else {
					after_label.setText(after(textField.getText(), Integer.parseInt(keyField.getText()))); // key��ŭ �̷�
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
		String copy = (String) table.getModel().getValueAt(row, 1); // ������ �������� after ��������
		
		StringSelection select = new StringSelection(copy);
	    Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
	    clpbrd.setContents(select, null); // Ŭ�����忡 ����
	    
	    System.out.println("copy ����");
	}

	// ���� ġȯ �ڵ�
	public String after(String s, int n) {
		String answer = "";
	    int z = (int)'z'; // 122
	    int Z = (int)'Z'; // 90
	    int a = (int)'a'; // 97
	    int A = (int)'A'; // 65

	    char[] temp = s.toCharArray(); // s�� �� �ܾ temp �迭�� ���
	      
	    for(int i =0; i<temp.length; i++){ // ���ڿ� ���̸�ŭ �ݺ�
	        if(temp[i]>=a && temp[i]<=z){ // �ҹ����� ��
	            temp[i] = (int)temp[i]+n > z ? (char)(a+(int)temp[i]+n-z-1) : (char)((int)temp[i]+n); // key��ŭ �ܾ� �̷��
	        }else if(temp[i]>=A && temp[i]<=Z){ // �빮���� ��
	            temp[i] = (int)temp[i]+n > Z ? (char)(A+(int)temp[i]+n-Z-1) : (char)((int)temp[i]+n); 
	        }else{ //������ ��
	            temp[i]=temp[i];
	        }
	    }
	    answer = new String(temp); // String���� �����
	    return answer;
	}
}
