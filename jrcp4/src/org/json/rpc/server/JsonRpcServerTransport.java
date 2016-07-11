package org.json.rpc.server;

public abstract interface JsonRpcServerTransport
{
  public abstract String readRequest()
    throws Exception;

  public abstract void writeResponse(String paramString)
    throws Exception;
}

