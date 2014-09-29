package net.cloudpath.xpressconnect;

import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class KeyStore
{
  public int KEY_NOT_FOUND;
  public int LOCKED;
  public int NO_ERROR;
  public int PERMISSION_DENIED;
  public int PROTOCOL_ERROR;
  public int SYSTEM_ERROR;
  public int UNDEFINED_ACTION;
  public int UNINITIALIZED;
  public int VALUE_CORRUPTED;
  public int WRONG_PASSWORD;
  private Class<?> keyStore = Class.forName("android.security.KeyStore");
  private Object keyStoreObject = this.keyStore.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);

  public KeyStore()
    throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException
  {
    getStatics();
  }

  private void getStatics()
    throws IllegalArgumentException, SecurityException, IllegalAccessException, NoSuchFieldException
  {
    this.KEY_NOT_FOUND = this.keyStore.getField("KEY_NOT_FOUND").getInt(this.keyStoreObject);
    Log.d("XPC", "KEY_NOT_FOUND = " + this.KEY_NOT_FOUND);
    this.LOCKED = this.keyStore.getField("LOCKED").getInt(this.keyStoreObject);
    Log.d("XPC", "LOCKED = " + this.LOCKED);
    this.NO_ERROR = this.keyStore.getField("NO_ERROR").getInt(this.keyStoreObject);
    Log.d("XPC", "NO_ERROR = " + this.NO_ERROR);
    this.PERMISSION_DENIED = this.keyStore.getField("PERMISSION_DENIED").getInt(this.keyStoreObject);
    Log.d("XPC", "PERMISSION_DENIED = " + this.PERMISSION_DENIED);
    this.PROTOCOL_ERROR = this.keyStore.getField("PROTOCOL_ERROR").getInt(this.keyStoreObject);
    Log.d("XPC", "PROTOCOL_ERROR = " + this.PROTOCOL_ERROR);
    this.SYSTEM_ERROR = this.keyStore.getField("SYSTEM_ERROR").getInt(this.keyStoreObject);
    Log.d("XPC", "SYSTEM_ERROR = " + this.SYSTEM_ERROR);
    this.UNDEFINED_ACTION = this.keyStore.getField("UNDEFINED_ACTION").getInt(this.keyStoreObject);
    Log.d("XPC", "UNDEFINED_ACTION = " + this.UNDEFINED_ACTION);
    this.UNINITIALIZED = this.keyStore.getField("UNINITIALIZED").getInt(this.keyStoreObject);
    Log.d("XPC", "UNINITIALIZED = " + this.UNINITIALIZED);
    this.VALUE_CORRUPTED = this.keyStore.getField("VALUE_CORRUPTED").getInt(this.keyStoreObject);
    Log.d("XPC", "VALUE_CORRUPTED = " + this.VALUE_CORRUPTED);
    this.WRONG_PASSWORD = this.keyStore.getField("WRONG_PASSWORD").getInt(this.keyStoreObject);
    Log.d("XPC", "WRONG_PASSWORD = " + this.WRONG_PASSWORD);
  }

  public Object getEnumValue()
    throws ClassNotFoundException
  {
    Class.forName("android.security.KeyStore.State").getEnumConstants();
    return new Object();
  }

  public String state()
    throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
  {
    return ((Enum)this.keyStore.getMethod("state", new Class[0]).invoke(this.keyStoreObject, new Object[0])).name();
  }

  public int test()
    throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
  {
    return ((Number)this.keyStore.getMethod("test", new Class[0]).invoke(this.keyStoreObject, new Object[0])).intValue();
  }
}