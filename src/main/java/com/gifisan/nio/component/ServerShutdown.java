package com.gifisan.nio.component;

import java.io.IOException;
import java.util.Scanner;

import com.gifisan.nio.client.ClientSession;
import com.gifisan.nio.client.ClientTCPConnector;
import com.gifisan.nio.common.CloseUtil;
import com.gifisan.nio.component.future.ReadFuture;

public class ServerShutdown {

	public static void main(String[] args) throws IOException {

		ServerShutdown shutdown = new ServerShutdown();
		
		shutdown.shutdown(args);
		
	}

	public void shutdown(String [] args) throws IOException {
		
		if (args == null || args.length < 3) {

			System.out.print("参数不正确，按回车键退出>>");

			Scanner scanner = new Scanner(System.in);

			scanner.nextLine();

			return;
		}

		int port = Integer.valueOf(args[0]);

		String username = args[1];

		String passwrod = args[2];

		String serviceName = "stop-server";

		ClientTCPConnector connector = new ClientTCPConnector("localhost", port);
		
		connector.connect();
		
		ClientSession session = connector.getClientSession();

		String param = "{username:\"" + username + "\",password:\"" + passwrod + "\"}";

		ReadFuture future = session.request(serviceName, param);

		System.out.println(future.getText());

		CloseUtil.close(connector);

		System.out.print("按回车键退出>>");

		Scanner scanner = new Scanner(System.in);

		scanner.nextLine();
	}

}
