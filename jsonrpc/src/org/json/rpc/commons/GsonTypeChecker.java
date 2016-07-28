 package org.json.rpc.commons;
 
 import java.lang.reflect.Constructor;
 import java.lang.reflect.Field;
 import java.lang.reflect.Modifier;
 import java.util.Date;
 import java.util.HashSet;
 import java.util.Set;
 
 public class GsonTypeChecker extends TypeChecker
 {
   public boolean isValidType(Class<?> clazz, boolean throwException)
   {
     return isValidType(clazz, throwException, null);
   }
 
   private boolean isValidType(Class<?> clazz, boolean throwException, Set<Class<?>> visited) {
     if (clazz.isPrimitive()) {
       return true;
     }
 
     if (Boolean.class == clazz) {
       return true;
     }
 
     if (Number.class.isAssignableFrom(clazz)) {
       return true;
     }
 
     if (String.class == clazz) {
       return true;
     }
 
     if (Character.class == clazz) {
       return true;
     }
 
     if (Date.class == clazz) {
       return true;
     }
 
     if (clazz.isArray()) {
       return isValidType(clazz.getComponentType(), throwException, visited);
     }
 
     if (clazz.isAnonymousClass()) {
       if (throwException) {
         throw new IllegalArgumentException("anonymous class not allowed : " + clazz);
       }
       return false;
     }
 
     if ((Modifier.isInterface(clazz.getModifiers())) || (Modifier.isAbstract(clazz.getModifiers()))) {
       if (throwException) {
         throw new IllegalArgumentException("abstract class or interface not allowed : " + clazz);
       }
       return false;
     }
 
     if (clazz.getTypeParameters().length > 0) {
       if (throwException) {
         throw new IllegalArgumentException("parametrized classes not allowed : " + clazz);
       }
       return false;
     }
 
     boolean zeroArgConstructor = clazz.getConstructors().length == 0;
     for (Constructor c : clazz.getConstructors()) {
       if (c.getParameterTypes().length == 0) {
         zeroArgConstructor = true;
         break;
       }
     }
 
     if (!zeroArgConstructor) {
       if (throwException) {
         throw new IllegalArgumentException("no zero-arg constructor found : " + clazz);
       }
       return false;
     }
 
     visited = visited == null ? new HashSet() : visited;
     if (visited.contains(clazz)) {
       return true;
     }
     visited.add(clazz);
 
     for (Field f : clazz.getDeclaredFields()) {
       int m = f.getModifiers();
       if ((!Modifier.isStatic(m)) && (!Modifier.isTransient(m)))
       {
         if (Modifier.isFinal(m)) {
           if (throwException) {
             throw new IllegalArgumentException("final field found : " + f);
           }
           return false;
         }
 
         boolean result = false;
         try {
           result = isValidType(f.getType(), throwException, visited);
           if (!result) {
             if (throwException) {
               throw new IllegalArgumentException("invalid field found : " + f);
             }
             return false;
           }
         } catch (RuntimeException e) {
           if (!result) {
             if (throwException) {
               throw new IllegalArgumentException("invalid field found : " + f, e);
             }
             return false;
           }
         }
       }
     }
 
     return true;
   }
 
   public String getTypeName(Class<?> clazz)
   {
     if ((clazz == Void.TYPE) || (clazz == Void.class)) {
       return Void.TYPE.getName();
     }
 
     if ((clazz == Boolean.TYPE) || (Boolean.class == clazz)) {
       return Boolean.TYPE.getName();
     }
 
     if ((clazz == Double.TYPE) || (clazz == Float.TYPE) || (Double.class == clazz) || (Float.class == clazz))
     {
       return Double.TYPE.getName();
     }
 
     if ((clazz == Byte.TYPE) || (clazz == Character.TYPE) || (clazz == Integer.TYPE) || (clazz == Short.TYPE) || (clazz == Long.TYPE) || (clazz == Character.class) || (Number.class.isAssignableFrom(clazz)))
     {
       return Integer.TYPE.getName();
     }
 
     if (clazz == String.class) {
       return "string";
     }
 
     if (clazz.isArray()) {
       return "array";
     }
 
     return "struct";
   }
 }

