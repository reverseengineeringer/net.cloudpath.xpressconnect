package net.cloudpath.xpressconnect.nativeproxy;

import android.net.wifi.WifiConfiguration;
import android.os.Build.VERSION;
import java.lang.reflect.Field;
import java.util.Locale;
import net.cloudpath.xpressconnect.AndroidApiLevels;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;

public class WifiConfigurationV2 extends WifiConfigurationV1
{
  private Field enterpriseConfigField = null;
  private Object enterpriseConfigObject = null;

  public WifiConfigurationV2(WifiConfiguration paramWifiConfiguration, Logger paramLogger)
    throws IllegalArgumentException, NoSuchFieldException, ClassNotFoundException, IllegalAccessException
  {
    super(paramWifiConfiguration, paramLogger);
    this.mApiLevel = 2;
    bindNewApi();
  }

  private void bindNewApi()
    throws NoSuchFieldException, IllegalArgumentException, ClassNotFoundException, IllegalAccessException
  {
    this.enterpriseConfigField = this.mWifiConfig.getClass().getField("enterpriseConfig");
    this.enterpriseConfigObject = this.enterpriseConfigField.get(this.mWifiConfig);
  }

  // ERROR //
  private int callMethodGetInt(String paramString, int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 24	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:enterpriseConfigObject	Ljava/lang/Object;
    //   4: invokevirtual 42	java/lang/Object:getClass	()Ljava/lang/Class;
    //   7: aload_1
    //   8: iconst_0
    //   9: anewarray 46	java/lang/Class
    //   12: invokevirtual 66	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   15: astore 4
    //   17: aload 4
    //   19: aload_0
    //   20: getfield 24	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:enterpriseConfigObject	Ljava/lang/Object;
    //   23: iconst_0
    //   24: anewarray 38	java/lang/Object
    //   27: invokevirtual 72	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   30: astore 8
    //   32: aload 8
    //   34: checkcast 74	java/lang/Integer
    //   37: invokevirtual 78	java/lang/Integer:intValue	()I
    //   40: ireturn
    //   41: astore_3
    //   42: aload_0
    //   43: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   46: new 84	java/lang/StringBuilder
    //   49: dup
    //   50: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   53: ldc 88
    //   55: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   58: aload_1
    //   59: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   62: ldc 94
    //   64: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   67: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   70: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   73: aload_3
    //   74: invokevirtual 107	java/lang/NoSuchMethodException:printStackTrace	()V
    //   77: iload_2
    //   78: ireturn
    //   79: astore 7
    //   81: aload_0
    //   82: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   85: new 84	java/lang/StringBuilder
    //   88: dup
    //   89: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   92: ldc 109
    //   94: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   97: aload_1
    //   98: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: ldc 94
    //   103: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   106: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   109: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   112: aload 7
    //   114: invokevirtual 110	java/lang/IllegalArgumentException:printStackTrace	()V
    //   117: iload_2
    //   118: ireturn
    //   119: astore 6
    //   121: aload_0
    //   122: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   125: new 84	java/lang/StringBuilder
    //   128: dup
    //   129: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   132: ldc 112
    //   134: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   137: aload_1
    //   138: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   141: ldc 94
    //   143: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   146: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   149: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   152: aload 6
    //   154: invokevirtual 113	java/lang/IllegalAccessException:printStackTrace	()V
    //   157: iload_2
    //   158: ireturn
    //   159: astore 5
    //   161: aload_0
    //   162: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   165: new 84	java/lang/StringBuilder
    //   168: dup
    //   169: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   172: ldc 115
    //   174: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   177: aload_1
    //   178: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   181: ldc 94
    //   183: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   186: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   189: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   192: aload 5
    //   194: invokevirtual 116	java/lang/reflect/InvocationTargetException:printStackTrace	()V
    //   197: iload_2
    //   198: ireturn
    //
    // Exception table:
    //   from	to	target	type
    //   0	17	41	java/lang/NoSuchMethodException
    //   17	32	79	java/lang/IllegalArgumentException
    //   17	32	119	java/lang/IllegalAccessException
    //   17	32	159	java/lang/reflect/InvocationTargetException
  }

  // ERROR //
  private String callMethodGetString(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 24	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:enterpriseConfigObject	Ljava/lang/Object;
    //   4: invokevirtual 42	java/lang/Object:getClass	()Ljava/lang/Class;
    //   7: aload_1
    //   8: iconst_0
    //   9: anewarray 46	java/lang/Class
    //   12: invokevirtual 66	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   15: astore_3
    //   16: aload_3
    //   17: aload_0
    //   18: getfield 24	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:enterpriseConfigObject	Ljava/lang/Object;
    //   21: iconst_0
    //   22: anewarray 38	java/lang/Object
    //   25: invokevirtual 72	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   28: astore 7
    //   30: aload 7
    //   32: checkcast 120	java/lang/String
    //   35: areturn
    //   36: astore_2
    //   37: aload_0
    //   38: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   41: new 84	java/lang/StringBuilder
    //   44: dup
    //   45: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   48: ldc 88
    //   50: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   53: aload_1
    //   54: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   57: ldc 94
    //   59: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   62: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   65: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   68: aload_2
    //   69: invokevirtual 107	java/lang/NoSuchMethodException:printStackTrace	()V
    //   72: aconst_null
    //   73: areturn
    //   74: astore 6
    //   76: aload_0
    //   77: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   80: new 84	java/lang/StringBuilder
    //   83: dup
    //   84: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   87: ldc 109
    //   89: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   92: aload_1
    //   93: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   96: ldc 94
    //   98: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   104: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   107: aload 6
    //   109: invokevirtual 110	java/lang/IllegalArgumentException:printStackTrace	()V
    //   112: aconst_null
    //   113: areturn
    //   114: astore 5
    //   116: aload_0
    //   117: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   120: new 84	java/lang/StringBuilder
    //   123: dup
    //   124: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   127: ldc 112
    //   129: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   132: aload_1
    //   133: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   136: ldc 94
    //   138: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   141: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   144: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   147: aload 5
    //   149: invokevirtual 113	java/lang/IllegalAccessException:printStackTrace	()V
    //   152: aconst_null
    //   153: areturn
    //   154: astore 4
    //   156: aload_0
    //   157: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   160: new 84	java/lang/StringBuilder
    //   163: dup
    //   164: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   167: ldc 115
    //   169: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   172: aload_1
    //   173: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   176: ldc 94
    //   178: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   181: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   184: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   187: aload 4
    //   189: invokevirtual 116	java/lang/reflect/InvocationTargetException:printStackTrace	()V
    //   192: aconst_null
    //   193: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   0	16	36	java/lang/NoSuchMethodException
    //   16	30	74	java/lang/IllegalArgumentException
    //   16	30	114	java/lang/IllegalAccessException
    //   16	30	154	java/lang/reflect/InvocationTargetException
  }

  // ERROR //
  private boolean callMethodWithInt(String paramString, int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 24	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:enterpriseConfigObject	Ljava/lang/Object;
    //   4: invokevirtual 42	java/lang/Object:getClass	()Ljava/lang/Class;
    //   7: astore 4
    //   9: iconst_1
    //   10: anewarray 46	java/lang/Class
    //   13: astore 5
    //   15: aload 5
    //   17: iconst_0
    //   18: getstatic 126	java/lang/Integer:TYPE	Ljava/lang/Class;
    //   21: aastore
    //   22: aload 4
    //   24: aload_1
    //   25: aload 5
    //   27: invokevirtual 66	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   30: astore 6
    //   32: aload_0
    //   33: getfield 24	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:enterpriseConfigObject	Ljava/lang/Object;
    //   36: astore 10
    //   38: iconst_1
    //   39: anewarray 38	java/lang/Object
    //   42: astore 11
    //   44: aload 11
    //   46: iconst_0
    //   47: iload_2
    //   48: invokestatic 130	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
    //   51: aastore
    //   52: aload 6
    //   54: aload 10
    //   56: aload 11
    //   58: invokevirtual 72	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   61: pop
    //   62: iconst_1
    //   63: ireturn
    //   64: astore_3
    //   65: aload_0
    //   66: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   69: new 84	java/lang/StringBuilder
    //   72: dup
    //   73: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   76: ldc 88
    //   78: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   81: aload_1
    //   82: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   85: ldc 94
    //   87: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   90: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   93: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   96: aload_3
    //   97: invokevirtual 107	java/lang/NoSuchMethodException:printStackTrace	()V
    //   100: iconst_0
    //   101: ireturn
    //   102: astore 9
    //   104: aload_0
    //   105: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   108: new 84	java/lang/StringBuilder
    //   111: dup
    //   112: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   115: ldc 109
    //   117: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   120: aload_1
    //   121: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   124: ldc 94
    //   126: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   129: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   132: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   135: aload 9
    //   137: invokevirtual 110	java/lang/IllegalArgumentException:printStackTrace	()V
    //   140: iconst_0
    //   141: ireturn
    //   142: astore 8
    //   144: aload_0
    //   145: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   148: new 84	java/lang/StringBuilder
    //   151: dup
    //   152: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   155: ldc 112
    //   157: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   160: aload_1
    //   161: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   164: ldc 94
    //   166: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   169: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   172: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   175: aload 8
    //   177: invokevirtual 113	java/lang/IllegalAccessException:printStackTrace	()V
    //   180: iconst_0
    //   181: ireturn
    //   182: astore 7
    //   184: aload_0
    //   185: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   188: new 84	java/lang/StringBuilder
    //   191: dup
    //   192: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   195: ldc 115
    //   197: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   200: aload_1
    //   201: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   204: ldc 94
    //   206: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   209: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   212: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   215: aload 7
    //   217: invokevirtual 116	java/lang/reflect/InvocationTargetException:printStackTrace	()V
    //   220: iconst_0
    //   221: ireturn
    //
    // Exception table:
    //   from	to	target	type
    //   0	32	64	java/lang/NoSuchMethodException
    //   32	62	102	java/lang/IllegalArgumentException
    //   32	62	142	java/lang/IllegalAccessException
    //   32	62	182	java/lang/reflect/InvocationTargetException
  }

  // ERROR //
  private boolean callMethodWithString(String paramString1, String paramString2)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 24	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:enterpriseConfigObject	Ljava/lang/Object;
    //   4: invokevirtual 42	java/lang/Object:getClass	()Ljava/lang/Class;
    //   7: aload_1
    //   8: iconst_1
    //   9: anewarray 46	java/lang/Class
    //   12: dup
    //   13: iconst_0
    //   14: ldc 120
    //   16: aastore
    //   17: invokevirtual 66	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   20: astore 4
    //   22: aload 4
    //   24: aload_0
    //   25: getfield 24	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:enterpriseConfigObject	Ljava/lang/Object;
    //   28: iconst_1
    //   29: anewarray 38	java/lang/Object
    //   32: dup
    //   33: iconst_0
    //   34: aload_2
    //   35: aastore
    //   36: invokevirtual 72	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   39: pop
    //   40: iconst_1
    //   41: ireturn
    //   42: astore_3
    //   43: aload_0
    //   44: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   47: new 84	java/lang/StringBuilder
    //   50: dup
    //   51: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   54: ldc 88
    //   56: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   59: aload_1
    //   60: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   63: ldc 94
    //   65: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   68: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   71: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   74: aload_3
    //   75: invokevirtual 107	java/lang/NoSuchMethodException:printStackTrace	()V
    //   78: iconst_0
    //   79: ireturn
    //   80: astore 7
    //   82: aload_0
    //   83: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   86: new 84	java/lang/StringBuilder
    //   89: dup
    //   90: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   93: ldc 109
    //   95: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   98: aload_1
    //   99: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   102: ldc 94
    //   104: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   107: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   110: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   113: aload 7
    //   115: invokevirtual 110	java/lang/IllegalArgumentException:printStackTrace	()V
    //   118: iconst_0
    //   119: ireturn
    //   120: astore 6
    //   122: aload_0
    //   123: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   126: new 84	java/lang/StringBuilder
    //   129: dup
    //   130: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   133: ldc 112
    //   135: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   138: aload_1
    //   139: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   142: ldc 94
    //   144: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   147: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   150: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   153: aload 6
    //   155: invokevirtual 113	java/lang/IllegalAccessException:printStackTrace	()V
    //   158: iconst_0
    //   159: ireturn
    //   160: astore 5
    //   162: aload_0
    //   163: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   166: new 84	java/lang/StringBuilder
    //   169: dup
    //   170: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   173: ldc 115
    //   175: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   178: aload_1
    //   179: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   182: ldc 94
    //   184: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   187: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   190: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   193: aload 5
    //   195: invokevirtual 116	java/lang/reflect/InvocationTargetException:printStackTrace	()V
    //   198: iconst_0
    //   199: ireturn
    //
    // Exception table:
    //   from	to	target	type
    //   0	22	42	java/lang/NoSuchMethodException
    //   22	40	80	java/lang/IllegalArgumentException
    //   22	40	120	java/lang/IllegalAccessException
    //   22	40	160	java/lang/reflect/InvocationTargetException
  }

  public String getAnonId()
  {
    return callMethodGetString("getAnonymousIdentity");
  }

  public String getCaCert()
  {
    String str = callMethodGetString("getCaCertificateAlias");
    return "keystore://CACERT_" + str;
  }

  // ERROR //
  public java.security.cert.X509Certificate getCaCertificate()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 24	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:enterpriseConfigObject	Ljava/lang/Object;
    //   4: invokevirtual 42	java/lang/Object:getClass	()Ljava/lang/Class;
    //   7: ldc 145
    //   9: iconst_0
    //   10: anewarray 46	java/lang/Class
    //   13: invokevirtual 66	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   16: astore_2
    //   17: aload_2
    //   18: aload_0
    //   19: getfield 24	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:enterpriseConfigObject	Ljava/lang/Object;
    //   22: iconst_0
    //   23: anewarray 38	java/lang/Object
    //   26: invokevirtual 72	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   29: astore 6
    //   31: aload 6
    //   33: checkcast 147	java/security/cert/X509Certificate
    //   36: areturn
    //   37: astore_1
    //   38: aload_0
    //   39: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   42: new 84	java/lang/StringBuilder
    //   45: dup
    //   46: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   49: ldc 88
    //   51: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   54: ldc 145
    //   56: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   59: ldc 94
    //   61: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   64: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   67: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   70: aload_1
    //   71: invokevirtual 107	java/lang/NoSuchMethodException:printStackTrace	()V
    //   74: aconst_null
    //   75: areturn
    //   76: astore 5
    //   78: aload_0
    //   79: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   82: new 84	java/lang/StringBuilder
    //   85: dup
    //   86: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   89: ldc 109
    //   91: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   94: ldc 145
    //   96: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   99: ldc 94
    //   101: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   104: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   107: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   110: aload 5
    //   112: invokevirtual 110	java/lang/IllegalArgumentException:printStackTrace	()V
    //   115: aconst_null
    //   116: areturn
    //   117: astore 4
    //   119: aload_0
    //   120: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   123: new 84	java/lang/StringBuilder
    //   126: dup
    //   127: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   130: ldc 112
    //   132: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   135: ldc 145
    //   137: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   140: ldc 94
    //   142: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   145: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   148: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   151: aload 4
    //   153: invokevirtual 113	java/lang/IllegalAccessException:printStackTrace	()V
    //   156: aconst_null
    //   157: areturn
    //   158: astore_3
    //   159: aload_0
    //   160: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   163: new 84	java/lang/StringBuilder
    //   166: dup
    //   167: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   170: ldc 115
    //   172: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   175: ldc 145
    //   177: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   180: ldc 94
    //   182: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   185: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   188: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   191: aload_3
    //   192: invokevirtual 116	java/lang/reflect/InvocationTargetException:printStackTrace	()V
    //   195: aconst_null
    //   196: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   0	17	37	java/lang/NoSuchMethodException
    //   17	31	76	java/lang/IllegalArgumentException
    //   17	31	117	java/lang/IllegalAccessException
    //   17	31	158	java/lang/reflect/InvocationTargetException
  }

  public String getClientCertAlias()
  {
    return callMethodGetString("getClientCertificateAlias");
  }

  // ERROR //
  public java.security.cert.X509Certificate getClientCertificate()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 24	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:enterpriseConfigObject	Ljava/lang/Object;
    //   4: invokevirtual 42	java/lang/Object:getClass	()Ljava/lang/Class;
    //   7: ldc 152
    //   9: iconst_0
    //   10: anewarray 46	java/lang/Class
    //   13: invokevirtual 66	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   16: astore_2
    //   17: aload_2
    //   18: aload_0
    //   19: getfield 24	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:enterpriseConfigObject	Ljava/lang/Object;
    //   22: iconst_0
    //   23: anewarray 38	java/lang/Object
    //   26: invokevirtual 72	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   29: astore 6
    //   31: aload 6
    //   33: checkcast 147	java/security/cert/X509Certificate
    //   36: areturn
    //   37: astore_1
    //   38: aload_0
    //   39: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   42: new 84	java/lang/StringBuilder
    //   45: dup
    //   46: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   49: ldc 88
    //   51: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   54: ldc 152
    //   56: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   59: ldc 94
    //   61: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   64: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   67: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   70: aload_1
    //   71: invokevirtual 107	java/lang/NoSuchMethodException:printStackTrace	()V
    //   74: aconst_null
    //   75: areturn
    //   76: astore 5
    //   78: aload_0
    //   79: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   82: new 84	java/lang/StringBuilder
    //   85: dup
    //   86: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   89: ldc 109
    //   91: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   94: ldc 152
    //   96: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   99: ldc 94
    //   101: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   104: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   107: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   110: aload 5
    //   112: invokevirtual 110	java/lang/IllegalArgumentException:printStackTrace	()V
    //   115: aconst_null
    //   116: areturn
    //   117: astore 4
    //   119: aload_0
    //   120: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   123: new 84	java/lang/StringBuilder
    //   126: dup
    //   127: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   130: ldc 112
    //   132: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   135: ldc 152
    //   137: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   140: ldc 94
    //   142: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   145: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   148: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   151: aload 4
    //   153: invokevirtual 113	java/lang/IllegalAccessException:printStackTrace	()V
    //   156: aconst_null
    //   157: areturn
    //   158: astore_3
    //   159: aload_0
    //   160: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   163: new 84	java/lang/StringBuilder
    //   166: dup
    //   167: invokespecial 86	java/lang/StringBuilder:<init>	()V
    //   170: ldc 115
    //   172: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   175: ldc 152
    //   177: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   180: ldc 94
    //   182: invokevirtual 92	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   185: invokevirtual 98	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   188: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   191: aload_3
    //   192: invokevirtual 116	java/lang/reflect/InvocationTargetException:printStackTrace	()V
    //   195: aconst_null
    //   196: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   0	17	37	java/lang/NoSuchMethodException
    //   17	31	76	java/lang/IllegalArgumentException
    //   17	31	117	java/lang/IllegalAccessException
    //   17	31	158	java/lang/reflect/InvocationTargetException
  }

  public String getClientPrivateKey()
  {
    return getClientCertAlias();
  }

  public String getEap()
  {
    switch (callMethodGetInt("getEapMethod", -1))
    {
    default:
      return null;
    case 0:
      return "PEAP";
    case 1:
      return "TLS";
    case 2:
      return "TTLS";
    case 3:
    }
    return "PWD";
  }

  public String getEngine()
  {
    return "";
  }

  public String getEngineId()
  {
    return "";
  }

  public String getId()
  {
    return callMethodGetString("getIdentity");
  }

  public String getKeyId()
  {
    return "";
  }

  public String getPhase2()
  {
    switch (callMethodGetInt("getPhase2Method", -1))
    {
    default:
      return null;
    case 4:
      return "auth=GTC";
    case 2:
      return "auth=MSCHAP";
    case 3:
      return "auth=MSCHAPV2";
    case 0:
      return "";
    case 1:
    }
    return "auth=PAP";
  }

  public String getSubjectMatch()
  {
    return callMethodGetString("getSubjectMatch");
  }

  public boolean setAnonId(String paramString)
  {
    return callMethodWithString("setAnonymousIdentity", paramString);
  }

  public boolean setCaCert(String paramString)
  {
    if ((paramString == null) && (Build.VERSION.SDK_INT >= AndroidApiLevels.ANDROID_4_3))
      return true;
    if (paramString.startsWith("keystore://"))
      paramString = paramString.replace("keystore://", "");
    if (paramString.startsWith("CACERT_"))
      paramString = paramString.replace("CACERT_", "");
    return callMethodWithString("setCaCertificateAlias", paramString);
  }

  // ERROR //
  public boolean setCaCertificate(java.security.cert.X509Certificate paramX509Certificate)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 24	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:enterpriseConfigObject	Ljava/lang/Object;
    //   4: invokevirtual 42	java/lang/Object:getClass	()Ljava/lang/Class;
    //   7: ldc 222
    //   9: iconst_1
    //   10: anewarray 46	java/lang/Class
    //   13: dup
    //   14: iconst_0
    //   15: ldc 147
    //   17: aastore
    //   18: invokevirtual 66	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   21: astore_3
    //   22: aload_3
    //   23: aload_0
    //   24: getfield 24	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:enterpriseConfigObject	Ljava/lang/Object;
    //   27: iconst_1
    //   28: anewarray 38	java/lang/Object
    //   31: dup
    //   32: iconst_0
    //   33: aload_1
    //   34: aastore
    //   35: invokevirtual 72	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   38: pop
    //   39: iconst_1
    //   40: ireturn
    //   41: astore_2
    //   42: aload_0
    //   43: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   46: ldc 224
    //   48: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   51: aload_2
    //   52: invokevirtual 107	java/lang/NoSuchMethodException:printStackTrace	()V
    //   55: iconst_0
    //   56: ireturn
    //   57: astore 6
    //   59: aload_0
    //   60: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   63: ldc 226
    //   65: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   68: aload 6
    //   70: invokevirtual 110	java/lang/IllegalArgumentException:printStackTrace	()V
    //   73: iconst_0
    //   74: ireturn
    //   75: astore 5
    //   77: aload_0
    //   78: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   81: ldc 228
    //   83: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   86: aload 5
    //   88: invokevirtual 113	java/lang/IllegalAccessException:printStackTrace	()V
    //   91: iconst_0
    //   92: ireturn
    //   93: astore 4
    //   95: aload_0
    //   96: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   99: ldc 230
    //   101: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   104: aload 4
    //   106: invokevirtual 116	java/lang/reflect/InvocationTargetException:printStackTrace	()V
    //   109: iconst_0
    //   110: ireturn
    //
    // Exception table:
    //   from	to	target	type
    //   0	22	41	java/lang/NoSuchMethodException
    //   22	39	57	java/lang/IllegalArgumentException
    //   22	39	75	java/lang/IllegalAccessException
    //   22	39	93	java/lang/reflect/InvocationTargetException
  }

  public boolean setClientCert(String paramString)
  {
    if (!callMethodWithString("setClientCertificateAlias", paramString))
    {
      Util.log(this.mLogger, "Unable to write the client certificate alias to the configuration structure.");
      return false;
    }
    return true;
  }

  // ERROR //
  public boolean setClientKeyEntry(java.security.PrivateKey paramPrivateKey, java.security.cert.X509Certificate paramX509Certificate)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 24	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:enterpriseConfigObject	Ljava/lang/Object;
    //   4: invokevirtual 42	java/lang/Object:getClass	()Ljava/lang/Class;
    //   7: ldc 238
    //   9: iconst_2
    //   10: anewarray 46	java/lang/Class
    //   13: dup
    //   14: iconst_0
    //   15: ldc 240
    //   17: aastore
    //   18: dup
    //   19: iconst_1
    //   20: ldc 147
    //   22: aastore
    //   23: invokevirtual 66	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   26: astore 4
    //   28: aload 4
    //   30: aload_0
    //   31: getfield 24	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:enterpriseConfigObject	Ljava/lang/Object;
    //   34: iconst_2
    //   35: anewarray 38	java/lang/Object
    //   38: dup
    //   39: iconst_0
    //   40: aload_1
    //   41: aastore
    //   42: dup
    //   43: iconst_1
    //   44: aload_2
    //   45: aastore
    //   46: invokevirtual 72	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   49: pop
    //   50: iconst_1
    //   51: ireturn
    //   52: astore_3
    //   53: aload_0
    //   54: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   57: ldc 242
    //   59: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   62: aload_3
    //   63: invokevirtual 107	java/lang/NoSuchMethodException:printStackTrace	()V
    //   66: iconst_0
    //   67: ireturn
    //   68: astore 7
    //   70: aload_0
    //   71: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   74: ldc 244
    //   76: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   79: aload 7
    //   81: invokevirtual 110	java/lang/IllegalArgumentException:printStackTrace	()V
    //   84: iconst_0
    //   85: ireturn
    //   86: astore 6
    //   88: aload_0
    //   89: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   92: ldc 246
    //   94: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   97: aload 6
    //   99: invokevirtual 113	java/lang/IllegalAccessException:printStackTrace	()V
    //   102: iconst_0
    //   103: ireturn
    //   104: astore 5
    //   106: aload_0
    //   107: getfield 82	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV2:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   110: ldc 248
    //   112: invokestatic 104	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   115: aload 5
    //   117: invokevirtual 116	java/lang/reflect/InvocationTargetException:printStackTrace	()V
    //   120: iconst_0
    //   121: ireturn
    //
    // Exception table:
    //   from	to	target	type
    //   0	28	52	java/lang/NoSuchMethodException
    //   28	50	68	java/lang/IllegalArgumentException
    //   28	50	86	java/lang/IllegalAccessException
    //   28	50	104	java/lang/reflect/InvocationTargetException
  }

  public boolean setClientPrivateKey(String paramString)
  {
    return true;
  }

  public boolean setEap(String paramString)
  {
    int i = -1;
    if (paramString.toLowerCase(Locale.ENGLISH).equals("peap"))
      i = 0;
    while (true)
    {
      return callMethodWithInt("setEapMethod", i);
      if (paramString.toLowerCase(Locale.ENGLISH).equals("ttls"))
        i = 2;
      else if (paramString.toLowerCase(Locale.ENGLISH).equals("tls"))
        i = 1;
      else if (paramString.toLowerCase(Locale.ENGLISH).equals("pwd"))
        i = 3;
    }
  }

  public boolean setId(String paramString)
  {
    return callMethodWithString("setIdentity", paramString);
  }

  public boolean setPassword(String paramString)
  {
    return callMethodWithString("setPassword", paramString);
  }

  public boolean setPhase2(String paramString)
  {
    int i = -1;
    if (paramString.toLowerCase(Locale.ENGLISH).equals("auth=pap"))
      i = 1;
    while (true)
    {
      return callMethodWithInt("setPhase2Method", i);
      if (paramString.toLowerCase(Locale.ENGLISH).equals("auth=mschap"))
        i = 2;
      else if (paramString.toLowerCase(Locale.ENGLISH).equals("auth=mschapv2"))
        i = 3;
      else if (paramString.toLowerCase(Locale.ENGLISH).equals("auth=gtc"))
        i = 4;
    }
  }

  public boolean setSubjectMatch(String paramString)
  {
    return callMethodWithString("setSubjectMatch", paramString);
  }

  public String toFilteredString()
  {
    String str1 = super.toFilteredString();
    if (Build.VERSION.SDK_INT >= AndroidApiLevels.ANDROID_4_3)
      if (getCaCertificate() == null)
        break label72;
    label72: for (String str2 = str1 + "++ A CA certificate is set.\n"; getClientCertificate() != null; str2 = str1 + "++ A CA certificate is *NOT* set.\n")
    {
      str1 = str2 + "-- A client certificate is set.\n";
      return str1;
    }
    return str2 + "-- A client certificate is *NOT* set.\n";
  }

  public static final class Eap
  {
    public static final int NONE = -1;
    public static final int PEAP = 0;
    public static final int PWD = 3;
    public static final int TLS = 1;
    public static final int TTLS = 2;
  }

  public static final class Phase2
  {
    public static final int GTC = 4;
    public static final int MSCHAP = 2;
    public static final int MSCHAPV2 = 3;
    public static final int NONE = 0;
    public static final int PAP = 1;
  }
}