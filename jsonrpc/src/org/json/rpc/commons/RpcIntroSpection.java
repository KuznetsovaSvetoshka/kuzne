package org.json.rpc.commons;

public abstract interface RpcIntroSpection
{
  public abstract String[] listMethods();

  public abstract String[] methodSignature(String paramString);
}

