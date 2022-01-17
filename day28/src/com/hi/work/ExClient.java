package com.hi.work;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

public class ExClient extends JFrame implements ActionListener, MouseListener {
	static BufferedReader br = null;
	static BufferedWriter bw = null;
	static TextArea tbox;
	static TextField tbar;
	static JButton btn;
	MenuItem mi0, mi1;
	Font font = new Font("나눔고딕", Font.BOLD, 14);
	Color color = Color.WHITE;
	JDialog dia;
	static JTextField namebar;
	static JButton diabtn;
	static String name = "user";
	
	public ExClient() {
		Color bgc = Color.DARK_GRAY;
		Panel p = new Panel();
		p.setLayout(new BorderLayout());
		p.setBackground(bgc);
		Panel p1 = new Panel();
		Panel p2 = new Panel();
		p2.setLayout(new GridLayout(1, 0));
		
		MenuBar menubar = new MenuBar();
		Menu menu = new Menu("설정");
		mi0 = new MenuItem("닉네임 변경");
		mi0.addActionListener(this);
		mi1 = new MenuItem("종료");
		mi1.addActionListener(this);
		
		menubar.add(menu);
		menu.add(mi0);
		menu.addSeparator();
		menu.add(mi1);
		
		tbox = new TextArea("", 35, 68, TextArea.SCROLLBARS_VERTICAL_ONLY);
		tbox.setEditable(false);
		tbox.setBackground(color);
		tbox.setFont(font);
		tbar = new TextField();
		tbar.setFont(font);
		tbar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = tbar.getText();
				try {
					bw.write(name + ": " + msg);
					bw.newLine();
					bw.flush();
					tbar.setText("");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btn = new JButton("전송");
		btn.addMouseListener(this);
		btn.setFont(font);
		btn.setBackground(color);
		p1.add(tbox);
		p2.add(tbar);
		p2.add(btn);
		
		p.add(p1, BorderLayout.CENTER);
		p.add(p2, BorderLayout.SOUTH);
		setMenuBar(menubar);
		add(p);
		setTitle("채팅 프로그램");
		setResizable(false);
		setSize(600, 700);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj==mi0){
			dia = new JDialog(this, "닉네임 변경", true);
			Panel p = new Panel();
			p.setLayout(new BorderLayout());
			Panel p1 = new Panel();
			Label lb = new Label("사용할 이름을 적어주세요");
			lb.setFont(font);
			p1.add(lb);
			lb.setAlignment(Label.CENTER);
			namebar = new JTextField(20);
			p1.add(namebar);
			diabtn = new JButton("확인");
			p1.add(diabtn);
			diabtn.setBackground(color);
			diabtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					dia.dispose();
					String temp = namebar.getText();
					name = temp.replaceAll("\\s", "");
					try {
						bw.write("닉네임이 " + "[" + name + "]" + "으로 변경되었습니다.");
						bw.newLine();
						bw.flush();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			
			p.add(p1, BorderLayout.CENTER);
			dia.add(p);
			dia.setResizable(false);
			dia.setSize(250, 200);
			dia.setLocationRelativeTo(tbox);
			dia.setVisible(true);
		}else if(obj==mi1){dispose();}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		String msg = tbar.getText();
		try {
			bw.write(name + ": " + msg);
			bw.newLine();
			bw.flush();
			tbar.setText("");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ExClient client = new ExClient();
		Socket socket = null;
		InputStream is = null;
		OutputStream os = null;
		InputStreamReader isr = null;
		OutputStreamWriter osw = null;
		br = null;
		
		try {
			socket = new Socket("localhost", 5000);
			is = socket.getInputStream();
			os = socket.getOutputStream();
			isr = new InputStreamReader(is);
			osw = new OutputStreamWriter(os);
			br = new BufferedReader(isr);
			bw = new BufferedWriter(osw);
			
			while(true){
				String chat = br.readLine();
				tbox.append(chat + "\n");
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				bw.close();
				br.close();
				osw.close();
				isr.close();
				os.close();
				is.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

}