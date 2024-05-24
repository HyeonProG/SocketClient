package ch05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// 1단계 - 함수로 분리해서 리팩토링 진행
public class MultiThreadClientExercise {

	public static void main(String[] args) {

		System.out.println(">>> 클라이언트 실행 <<<");
		// 서버에 연결을 하기 위한 작업
		try (Socket socket = new Socket("localhost", 5000)) {

			System.out.println(">>> connected to the Server <<<");
			// 서버와 통신을 위한 스트림 설정
			PrintWriter writerStream = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader readerStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			BufferedReader keyboardReader = new BufferedReader(new InputStreamReader(System.in));
			
			// 스레드 시작
			startWriteThread(writerStream, keyboardReader); // join
			startReadThread(readerStream); // 메인 스레드 대기 <-- 안에서

		} catch (Exception e) {

			e.printStackTrace();
		}

	} // end of main
	
	// 서버로 부터 데이터를 읽는 스레드 분리
	private static void startReadThread(BufferedReader bufferedReader) {
		Thread readThread = new Thread(() -> {
			try {
				String serverMessage;
				while ((serverMessage = bufferedReader.readLine()) != null) {
					System.out.println("서버 측에서 온 메세지 : " + serverMessage);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		readThread.start();
		// 메인 스레드 대기
		waitForThreadToEnd(readThread);
	}
	// 클라이언트 측에서 서버로 데이터를 보내는 기능
	private static void startWriteThread(PrintWriter printWriter, BufferedReader keyboardReader) {
		Thread writeThread = new Thread(() -> {
			try {
				String clientMessage;
				while ((clientMessage = keyboardReader.readLine()) != null) {
					printWriter.println(clientMessage);
					printWriter.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		writeThread.start();
	}
	// 워커 스레드가 종료될 때 까지 기다리는 메서드
	private static void waitForThreadToEnd(Thread thread) {
		try {
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

} // end of class
