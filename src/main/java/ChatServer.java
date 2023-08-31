import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {

  String[] adviceList = new String[]{"Take smaller bites",
      "Go for the tight jeans.No the do not make you FAT", "One word: inappropriate",
      "You want to rethink that haircut", "IDK", "Should do something useful", "Mindfulness"};
  List<ChatClient> clientList = new ArrayList<>();

  public void go() {
    try (ServerSocket server = new ServerSocket(4242)) {
      while (true) {
        Socket socket = server.accept(); // this line will hang on until a client connect to server
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        String line;


        String advice = bufferedReader.readLine();
        printWriter.println(advice);
        printWriter.close();
        System.out.println(socket.getPort());
      }
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
  }


  public String getAdvice() {
    int random = (int) (Math.random() * adviceList.length);
    return adviceList[random];
  }

  public static void main(String[] args) {
    ChatServer chatServer = new ChatServer();
    chatServer.go();


  }
}
