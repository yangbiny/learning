package com.impassive.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author impassive
 */
public class SocketService {

  public static void main(String[] args) throws InterruptedException {

    new Thread(SocketService::startService).start();

    Thread.sleep(3000);
    startClient();

    Thread.sleep(1000000000);
  }


  private static void startClient() {

    try (Socket socket = new Socket()) {
      // 读取数据超时 设置
      socket.setSoTimeout(10000);

      socket.connect(new InetSocketAddress("127.0.0.1", 8080), 3000);

      readAndWriteWithClient(socket);

    } catch (Exception e) {
      System.err.println("client error " + System.currentTimeMillis());
      e.printStackTrace();
    }


  }

  private static void startService() {

    System.err.println("service started");
    try (ServerSocket serverSocket = new ServerSocket(8080)) {
      Socket accept = serverSocket.accept();
      ClientHandler clientHandler = new ClientHandler(accept);
      clientHandler.start();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private static void readAndWriteWithClient(Socket socket) throws IOException {

    BufferedReader brr = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

    int n = 0;
    do {

      String line = System.currentTimeMillis() + ": client";
      writer.write(line);
      writer.newLine();
      writer.flush();

      String response = brr.readLine();
      System.err.println("客户端 read : " + response);
      n++;
    } while (n < 10);

    socket.close();
  }


  static class ClientHandler extends Thread {

    private final Socket socket;

    public ClientHandler(Socket socket) {
      this.socket = socket;
    }

    @Override
    public void run() {
      try {
        InputStream inputStream = socket.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        BufferedWriter writer = new BufferedWriter(
            new OutputStreamWriter(socket.getOutputStream()));

        do {
          if (socket.isClosed()) {
            break;
          }
          String line = bufferedReader.readLine();
          System.err.println("服务器 read : " + line);

          String s = System.currentTimeMillis() + ": service";
          writer.write(s);
          writer.newLine();
          writer.flush();
        } while (true);

      } catch (Exception e) {
        System.err.println("service error ");
        e.printStackTrace();
      }
    }
  }
}
