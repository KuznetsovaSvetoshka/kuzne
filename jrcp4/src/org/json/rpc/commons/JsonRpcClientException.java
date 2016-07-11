package org.json.rpc.commons;

public class JsonRpcClientException extends JsonRpcException
{
  public JsonRpcClientException(String message)
  {
   super(message);
  }

  public JsonRpcClientException(String message, Throwable cause) {
   super(message, cause);
   }
 }

