package org.json.rpc.commons;

import java.lang.reflect.Method;

public class AllowAllTypeChecker extends TypeChecker
{
  public boolean isValidType(Class<?> clazz)
  {
    return true;
  }

  public boolean isValidType(Class<?> clazz, boolean throwException) {
    return true;
  }

  public String getTypeName(Class<?> clazz) {
    return clazz.getName();
  }

  public boolean isValidMethod(Method method) {
    return true;
  }

  public boolean isValidInterface(Class<?> clazz) {
    return true;
  }

  public boolean isValidMethod(Method method, boolean throwException) {
    return true;
  }

  public boolean isValidInterface(Class<?> clazz, boolean throwException) {
    return true;
  }
}

