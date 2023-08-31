import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ChatServerV2 {

  List clientOutputStreams;

  List<ChatClient> chatClients;

  class ClientHandler implements Runnable {

    BufferedReader bufferedReader;
    Socket socket;

    public ClientHandler(Socket socket) {
      this.socket = socket;
      try {
        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        bufferedReader = new BufferedReader(inputStreamReader);
      } catch (IOException ex) {
        ex.getMessage();
      }
    }

    @Override
    public void run() {
      String message;
      try {
        while ((message = bufferedReader.readLine()) != null) {
          System.out.println("Got a message from client: " + message);
          tellEveryone(message);
        }
      } catch (IOException e) {
      }

    }
  }

  public static void main(String[] args) {
    new ChatServerV2().go();
  }

  private void go() {
    clientOutputStreams = new ArrayList<>();
    try {
      ServerSocket serverSocket = new ServerSocket(5000);
      while (true) {
        Socket clientSocket = serverSocket.accept();
        PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream());
        clientOutputStreams.add(printWriter);
        Thread thread = new Thread(new ClientHandler(clientSocket));
        thread.start();
        System.out.println("got a connection");
      }
    } catch (IOException e) {
    }
  }

  private void tellEveryone(String message) {
    Iterator iterator = clientOutputStreams.iterator();
    while (iterator.hasNext()) {
      PrintWriter printWriter = (PrintWriter) iterator.next();
      printWriter.println(message);
      printWriter.flush();
    }
  }
}
