package org.json.rpc.client;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TcpRpcClientTransport
  implements JsonRpcClientTransport
{
  private URL url;
  private InputStream in;
  private OutputStream out;

  public TcpRpcClientTransport(URL url)
  {
    this.url = url;
    /*try {
 Socket kkSocket = new Socket(url.getHost(), url.getPort());
 this.in = kkSocket.getInputStream();
  this.out = kkSocket.getOutputStream();
}
catch (Exception e) {
  throw new RuntimeException("Error connecting to the server, check permission and server status", e);
}*/
  }

  public String call(String data)
    throws IOException
  {
    byte[] outData = data.getBytes();
    byte[] dataLen = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(outData.length).array();

    this.out.write(dataLen);
    this.out.write(outData);
    this.out.flush();
    ByteArrayOutputStream bos = new ByteArrayOutputStream();

    byte[] resLenBytes = new byte[4];

    int lenRemaining = resLenBytes.length;
    int i = 0;
    int n;
    while ((lenRemaining > 0) && ((n = this.in.read(resLenBytes, i, lenRemaining)) > 0)) {
      i += n;
      lenRemaining -= n;
    }
    if (lenRemaining != 0) throw new RuntimeException("Error with the remote request");

    int resLen = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).put(resLenBytes).getInt(0);

    this.in = new BufferedInputStream(this.in);
    byte[] buff = new byte[1024];

    int remaining = resLen;
    int n1;
    while ((remaining > 0) && ((n1 = this.in.read(buff, 0, Math.min(remaining, buff.length))) > 0)) {
      bos.write(buff, 0, n1);
      remaining -= n1;
    }
    bos.flush();
    bos.close();

    return bos.toString();
  }
}

