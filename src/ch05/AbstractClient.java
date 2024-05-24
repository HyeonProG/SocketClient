package ch05;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// 2단계 - 상속 활용 리팩토링 단계
public abstract class AbstractClient {

	private Socket socket;
	private BufferedReader readerStream;
	private PrintWriter writerStream;
	private BufferedReader keyboardReader;

	// 실행의 흐름
	public final void run() {
		try {
			setupClient();
			setupStream();
			startService();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cleanup();
		}
	}

	// 클라이언트 연결
	protected abstract void setupClient() throws IOException;

	// 스트림 초기화
	private void setupStream() throws IOException {
		readerStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writerStream = new PrintWriter(socket.getOutputStream(), true);
		keyboardReader = new BufferedReader(new InputStreamReader(System.in));
	}

	// 서비스 시작
	private void startService() throws InterruptedException {
		Thread readThread = createReadThread();
		Thread writeThread = createWriteThread();

		readThread.start();
		writeThread.start();

		writeThread.join();
		readThread.join();

	}

	// 캡슐화
	private Thread createReadThread() {
		return new Thread(() -> {
			try {
				String msg;
				while ((msg = readerStream.readLine()) != null) {
					System.out.println("server 측 메세지 : " + msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	// 캡슐화
	private Thread createWriteThread() {
		return new Thread(() -> {
			try {
				String msg;
				while ((msg = keyboardReader.readLine()) != null) {
					writerStream.println(msg);
					writerStream.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

	private void cleanup() {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// set 메서드
	protected void setSocket(Socket socket) {
		this.socket = socket;
	}

}
