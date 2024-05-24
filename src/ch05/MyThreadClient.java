package ch05;

import java.io.IOException;
import java.net.Socket;

// 2 - 1 단계 - 상속을 활용한 구현 클래스 설계 하기
public class MyThreadClient extends AbstractClient {

	@Override
	protected void setupClient() throws IOException {
		setSocket(new Socket("localhost", 5000));
		System.out.println(">>> connected to the server <<<");
	}

	public static void main(String[] args) {
		MyThreadClient myThreadClient = new MyThreadClient();
		myThreadClient.run();
	} // end of main

} // end of class
