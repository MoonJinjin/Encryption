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
		// Ű �Է� �̺�Ʈ
		textField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				after_label.setText(after(textField.getText())); // ���� ġȯ ����� ���
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
				String input[] = new String[2];
				input[0] = textField.getText(); // before �Է� �� ��������
				input[1] = after_label.getText(); // after �Է� �� ��������
				model.addRow(input); // �� �߰�
				textField.setText(""); // �ؽ�Ʈ�ʵ� �ʱ�ȭ
				after_label.setText("");
			}
		});
		frmSavePassword.getContentPane().add(btnAdd);
		
		String header[] = {"Before", "After"};
		String contents[][] = {};
		model = new DefaultTableModel(contents, header);
		table = new JTable(model);
		table.setFont(new Font("Sitka Small", Font.PLAIN, 20));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN); // �÷� ���� �ڵ�
		table.setAutoCreateRowSorter(true); // �÷� Ŭ���� �� �ڵ� ���� Ȱ��ȭ
		table.setRowHeight(30); // �� ����
		
		JScrollPane scroll = new JScrollPane();
		scroll.setBounds(17, 150, 776, 368);
		scroll.setViewportView(table); // ��ũ�� �ȿ� ���̺� �����ֱ�
		frmSavePassword.getContentPane().add(scroll);
		
	}
	// ���� ġȯ �ڵ�
	public String after(String s) {
		String answer = "";
	    int z = (int)'z';
	    int Z = (int)'Z';
	    int a = (int)'a';
	    int A = (int)'A';
	    char[] temp = s.toCharArray();
	      
	    for(int i =0; i<temp.length; i++){
	        if(temp[i]>=a&& temp[i]<=z){ // �ҹ���
	            temp[i] = (int)temp[i]+3 > z ? (char)(a+(int)temp[i]+3-z-1) : (char)((int)temp[i]+3); 
	        }else if(temp[i]>=A&& temp[i]<=Z){ // �빮��
	            temp[i] = (int)temp[i]+3 > Z ? (char)(A+(int)temp[i]+3-Z-1) : (char)((int)temp[i]+3);
	        }else{
	            //����
	            temp[i]=temp[i];
	        }
	    }
	    answer = new String(temp);
	    return answer;
	}
}
