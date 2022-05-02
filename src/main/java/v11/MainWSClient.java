package v11;

import java.io.IOException;
import java.net.URI;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
public class MainWSClient {

	public static void main(String[] args) throws DeploymentException, IOException {
		URI uri = URI.create("ws://localhost:18080/kabusapi/websocket");
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		Session session = container.connectToServer(new MainWSClient(), uri);
//		session.getBasicRemote().sendText("sample text");
		while (true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
	}

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("onOpen:" + session);
	}

	@OnMessage
	public void onMessage(String message) {
		System.out.println("onMessge：" + message);
	}

	@OnError
	public void onError(Throwable th) {
		System.out.println("onError：" + th.getMessage());
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("onClose:" + session);
	}

}
