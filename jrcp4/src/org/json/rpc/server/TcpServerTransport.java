package org.json.rpc.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TcpServerTransport
  implements JsonRpcServerTransport
{
  private static final int BUFF_LENGTH = 1024;
  private Socket clientSocket;

  public TcpServerTransport(int port)
  {
    ServerSocket serverSocket = null;
    try {
      serverSocket = new ServerSocket(port);
      this.clientSocket = serverSocket.accept();
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Unable to create the server", e);
    }
  }

  public String readRequest() throws Exception
  {
    InputStream in = this.clientSocket.getInputStream();

    byte[] reqLenBytes = new byte[4];

    int lenRemaining = reqLenBytes.length;
    int i = 0;
    int n;
    while ((lenRemaining > 0) && ((n = in.read(reqLenBytes, i, lenRemaining)) > 0)) {
      System.out.println("Read " + n);
      i += n;
      lenRemaining -= n;
    }
    if (lenRemaining != 0) throw new RuntimeException("Error with the remote request");

    int reqLen = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).put(reqLenBytes).getInt(0);

    ByteArrayOutputStream bos = new ByteArrayOutputStream();

    byte[] buff = new byte[1024];
    int remaining = reqLen;
    int n1;
    while ((remaining > 0) && ((n1 = in.read(buff, 0, Math.min(remaining, buff.length))) > 0)) {
      remaining -= n1;
      bos.write(buff, 0, n1);
    }

    return bos.toString();
  }

  public void writeResponse(String responseData) throws Exception
  {
    byte[] data = responseData.getBytes();

    byte[] lenBytes = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(data.length).array();

    OutputStream out = this.clientSocket.getOutputStream();
    out.write(lenBytes);
    out.write(data);
    out.flush();
  }
}

