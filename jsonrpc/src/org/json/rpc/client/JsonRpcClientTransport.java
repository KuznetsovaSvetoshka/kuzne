package org.json.rpc.client;

public abstract interface JsonRpcClientTransport
{
  public abstract String call(String paramString)
    throws Exception;
}

