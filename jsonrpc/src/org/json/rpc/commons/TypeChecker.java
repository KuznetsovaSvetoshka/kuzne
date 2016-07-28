 package org.json.rpc.commons;
 
 import java.lang.reflect.Method;
 import java.lang.reflect.Modifier;
 
 public abstract class TypeChecker
 {
   public boolean isValidType(Class<?> clazz)
   {
     return isValidType(clazz, false);
   }
 
   public abstract boolean isValidType(Class<?> paramClass, boolean paramBoolean);
 
   public abstract String getTypeName(Class<?> paramClass);
 
   public boolean isValidMethod(Method method) {
     return isValidMethod(method, false);
   }
 
   public boolean isValidInterface(Class<?> clazz) {
     return isValidInterface(clazz, false);
   }
 
   public boolean isValidMethod(Method method, boolean throwException) {
     Class returnType = method.getReturnType();
     boolean result = false;
     try {
       result = isValidType(returnType, throwException);
       if (!result) {
         if (throwException) {
           throw new IllegalArgumentException("invalid return type : " + returnType);
         }
         return false;
       }
     } catch (RuntimeException e) {
       if (!result) {
         if (throwException) {
           throw new IllegalArgumentException("invalid return type : " + returnType, e);
         }
         return false;
       }
     }
 
     for (Class paramType : method.getParameterTypes()) {
       result = false;
       try {
         result = isValidType(paramType, throwException);
         if (!result) {
           if (throwException) {
             throw new IllegalArgumentException("invalid parameter type : " + paramType);
           }
           return false;
         }
       } catch (RuntimeException e) {
         if (!result) {
           if (throwException) {
             throw new IllegalArgumentException("invalid parameter type : " + paramType, e);
           }
           return false;
         }
       }
     }
 
     return true;
   }
 
   public boolean isValidInterface(Class<?> clazz, boolean throwException) {
     if (!clazz.isInterface()) {
       if (throwException) {
         throw new IllegalArgumentException("not an interface : " + clazz);
       }
       return false;
     }
 
     for (Method method : clazz.getDeclaredMethods()) {
       int m = method.getModifiers();
       if (!Modifier.isStatic(m))
       {
         boolean result = false;
         try {
           result = isValidMethod(method, throwException);
           if (!result) {
             if (throwException) {
               throw new IllegalArgumentException("invalid method : " + method);
             }
             return false;
           }
         } catch (RuntimeException e) {
           if (!result) {
             if (throwException) {
               throw new IllegalArgumentException("invalid method : " + method, e);
             }
             return false;
           }
         }
       }
     }
     return true;
   }
 }
