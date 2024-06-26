package ch02;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientFile {

	public static void main(String[] args) {

		// 클라이언트 측 준비물
		// 1. 서버 측 IP 주소 + 포트 번호가 필요하다.
		// 2. 서버 측 소켓과 연결 될 소켓이 필요하다.
		Socket socket = null;
		try {
			socket = new Socket("localhost", 5001);

			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			writer.println("안녕 반가워~"); // 줄바꿈 처리를 하자
			// PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			// true - auto flush 설정
			// writer.flush();

			socket.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	} // end of main

} // end of class
