package org.json.rpc.commons;

public final class JsonRpcErrorCodes
{
  public static final int PARSE_ERROR_CODE = -32700;
  public static final int INVALID_REQUEST_ERROR_CODE = -32600;
  public static final int METHOD_NOT_FOUND_ERROR_CODE = -32601;
  public static final int INVALID_PARAMS_ERROR_CODE = -32602;
  public static final int INTERNAL_ERROR_CODE = -32603;
  private static final int SERVER_ERROR_START = -32000;

  public static int getServerError(int n)
  {
    return -32000 - n;
  }

  private JsonRpcErrorCodes() {
    throw new AssertionError();
  }
}
