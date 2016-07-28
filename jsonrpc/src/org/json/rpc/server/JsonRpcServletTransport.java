package org.json.rpc.server;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JsonRpcServletTransport
  implements JsonRpcServerTransport
{
  private static final int BUFF_LENGTH = 1024;
  private final HttpServletRequest req;
  private final HttpServletResponse resp;

  public JsonRpcServletTransport(HttpServletRequest req, HttpServletResponse resp)
  {
    this.req = req;
    this.resp = resp;
  }

  public String readRequest() throws Exception {
    InputStream in = null;
    try {
      in = this.req.getInputStream();
      ByteArrayOutputStream bos = new ByteArrayOutputStream();

      byte[] buff = new byte[1024];
      int n;
      while ((n = in.read(buff)) > 0) {
        bos.write(buff, 0, n);
      }

      return bos.toString();
    } finally {
      if (in != null)
        in.close();
    }
  }

  public void writeResponse(String responseData) throws Exception
  {
    byte[] data = responseData.getBytes(this.resp.getCharacterEncoding());
 this.resp.addHeader("Content-Type", "application/json; charset=UTF-8");
    this.resp.setHeader("Content-Length", Integer.toString(data.length));
  // this.resp.addHeader("Content-Type", "application/json");
    PrintWriter out = null;
    try {
      out = this.resp.getWriter();
      out.write(responseData);
      out.flush();
    } finally {
      if (out != null)
        out.close();
    }
  }
}

