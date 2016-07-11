package org.json.rpc.commons;

public class JsonRpcException extends RuntimeException
{
  public JsonRpcException(String message)
  {
    super(message);
  }

  public JsonRpcException(String message, Throwable cause) {
    super(message, cause);
  }
}

