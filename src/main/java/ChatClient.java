import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

  private Socket socket;
  private BufferedReader bufferedReader;
  private PrintWriter printWriter;

  private Scanner scanner;


  public void sendMessage() {
    String line;
    while (true) {
      line = scanner.nextLine();
      if (line.equals("exit")) {
        break;
      }
      printWriter.println(line);
      printWriter.flush();
    }
  }

  private void setUpNetworking() {
    try {
      socket = new Socket("127.0.0.1", 5000);
      InputStreamReader inputStreamReader = new InputStreamReader(
          socket.getInputStream());
      bufferedReader = new BufferedReader(
          inputStreamReader);
      printWriter = new PrintWriter(socket.getOutputStream());
      scanner = new Scanner(System.in);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  private void go() {
    Thread incomingReader = new Thread(new IncomingReader());
    incomingReader.start();
  }

  public static void main(String[] args) throws IOException {
    ChatClient chatClient = new ChatClient();
    chatClient.setUpNetworking();

    chatClient.go();
    chatClient.sendMessage();


  }


  class IncomingReader implements Runnable {

    @Override
    public void run() {
      String message;
      try {
        while ((message = bufferedReader.readLine()) != null) {
          System.out.println("message sent from server: " + message);
        }
      } catch (IOException ioException) {
        System.out.println("Error happened in incomingReader " + ioException.getMessage());
      }

    }
  }
}
