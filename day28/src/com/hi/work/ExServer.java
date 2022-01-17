package com.hi.work;

import java.net.*;
import java.util.*;
import java.io.*;

public class ExServer extends Thread {
	static ArrayList<Socket> list = new ArrayList<>();
	Socket socket = null;
	
	public void getServer(String msg) throws IOException {
		OutputStream os = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		
		for(int i=0; i<list.size(); i++){
			socket = list.get(i);
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os);
			bw = new BufferedWriter(osw);
			bw.write(msg);
			bw.newLine();
			bw.flush();
		}
	}
	
	@Override
	public void run() {
		InputStream is = null;
		OutputStream os = null;
		InputStreamReader isr = null;
		OutputStreamWriter osw = null;
		BufferedReader br = null;
		BufferedWriter bw = null;
		
		try {
			is = socket.getInputStream();
			os = socket.getOutputStream();
			isr = new InputStreamReader(is);
			osw = new OutputStreamWriter(os);
			br = new BufferedReader(isr);
			bw = new BufferedWriter(osw);
			
			while(true){
				String msg = br.readLine();
				getServer(msg);
			}
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

	public static void main(String[] args) {
		ServerSocket serversocket = null;
		
		try {
			serversocket = new ServerSocket(5000);
			
			while(true){
				ExServer server = new ExServer();
				server.socket = serversocket.accept();
				server.start();
				list.add(server.socket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				serversocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

}
