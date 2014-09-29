package net.cloudpath.xpressconnect.nativeproxy;

import android.annotation.SuppressLint;
import android.net.wifi.WifiConfiguration;
import android.os.Build.VERSION;
import java.lang.reflect.Field;
import java.util.BitSet;
import java.util.Locale;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;

public class WifiConfigurationV1 extends WifiConfigTemplate
{
  public WifiConfigurationV1(WifiConfiguration paramWifiConfiguration, Logger paramLogger)
  {
    super(paramWifiConfiguration, paramLogger);
    this.mApiLevel = 1;
  }

  // ERROR //
  private boolean setValue(String paramString1, String paramString2)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 28	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mWifiConfig	Landroid/net/wifi/WifiConfiguration;
    //   4: invokevirtual 34	java/lang/Object:getClass	()Ljava/lang/Class;
    //   7: astore_3
    //   8: aload_3
    //   9: aload_1
    //   10: invokevirtual 40	java/lang/Class:getField	(Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   13: aload_0
    //   14: getfield 28	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mWifiConfig	Landroid/net/wifi/WifiConfiguration;
    //   17: invokevirtual 46	java/lang/reflect/Field:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   20: invokevirtual 34	java/lang/Object:getClass	()Ljava/lang/Class;
    //   23: astore 7
    //   25: aload 7
    //   27: ldc 47
    //   29: iconst_1
    //   30: anewarray 36	java/lang/Class
    //   33: dup
    //   34: iconst_0
    //   35: ldc 49
    //   37: aastore
    //   38: invokevirtual 53	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   41: astore 9
    //   43: aload 9
    //   45: aload_3
    //   46: aload_1
    //   47: invokevirtual 40	java/lang/Class:getField	(Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   50: aload_0
    //   51: getfield 28	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mWifiConfig	Landroid/net/wifi/WifiConfiguration;
    //   54: invokevirtual 46	java/lang/reflect/Field:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   57: iconst_1
    //   58: anewarray 30	java/lang/Object
    //   61: dup
    //   62: iconst_0
    //   63: aload_2
    //   64: aastore
    //   65: invokevirtual 59	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   68: pop
    //   69: iconst_1
    //   70: ireturn
    //   71: astore 6
    //   73: aload_0
    //   74: getfield 63	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   77: new 65	java/lang/StringBuilder
    //   80: dup
    //   81: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   84: ldc 70
    //   86: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   89: aload_1
    //   90: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   93: ldc 76
    //   95: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   98: invokevirtual 80	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   101: invokestatic 86	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   104: aload 6
    //   106: invokevirtual 89	java/lang/IllegalArgumentException:printStackTrace	()V
    //   109: iconst_0
    //   110: ireturn
    //   111: astore 5
    //   113: aload_0
    //   114: getfield 63	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   117: new 65	java/lang/StringBuilder
    //   120: dup
    //   121: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   124: ldc 91
    //   126: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   129: aload_1
    //   130: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   133: ldc 76
    //   135: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   138: invokevirtual 80	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   141: invokestatic 86	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   144: aload 5
    //   146: invokevirtual 92	java/lang/IllegalAccessException:printStackTrace	()V
    //   149: iconst_0
    //   150: ireturn
    //   151: astore 4
    //   153: aload_0
    //   154: getfield 63	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   157: new 65	java/lang/StringBuilder
    //   160: dup
    //   161: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   164: ldc 94
    //   166: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   169: aload_1
    //   170: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   173: ldc 76
    //   175: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   178: invokevirtual 80	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   181: invokestatic 86	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   184: aload 4
    //   186: invokevirtual 95	java/lang/NoSuchFieldException:printStackTrace	()V
    //   189: iconst_0
    //   190: ireturn
    //   191: astore 8
    //   193: aload_0
    //   194: getfield 63	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   197: new 65	java/lang/StringBuilder
    //   200: dup
    //   201: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   204: ldc 97
    //   206: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   209: aload_1
    //   210: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   213: ldc 76
    //   215: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   218: invokevirtual 80	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   221: invokestatic 86	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   224: aload 8
    //   226: invokevirtual 98	java/lang/NoSuchMethodException:printStackTrace	()V
    //   229: iconst_0
    //   230: ireturn
    //   231: astore 13
    //   233: aload_0
    //   234: getfield 63	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   237: new 65	java/lang/StringBuilder
    //   240: dup
    //   241: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   244: ldc 100
    //   246: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   249: aload_1
    //   250: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   253: ldc 76
    //   255: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   258: invokevirtual 80	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   261: invokestatic 86	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   264: aload 13
    //   266: invokevirtual 89	java/lang/IllegalArgumentException:printStackTrace	()V
    //   269: iconst_0
    //   270: ireturn
    //   271: astore 12
    //   273: aload_0
    //   274: getfield 63	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   277: new 65	java/lang/StringBuilder
    //   280: dup
    //   281: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   284: ldc 102
    //   286: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   289: aload_1
    //   290: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   293: ldc 76
    //   295: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   298: invokevirtual 80	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   301: invokestatic 86	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   304: aload 12
    //   306: invokevirtual 92	java/lang/IllegalAccessException:printStackTrace	()V
    //   309: iconst_0
    //   310: ireturn
    //   311: astore 11
    //   313: aload_0
    //   314: getfield 63	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   317: new 65	java/lang/StringBuilder
    //   320: dup
    //   321: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   324: ldc 104
    //   326: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   329: aload_1
    //   330: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   333: ldc 76
    //   335: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   338: invokevirtual 80	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   341: invokestatic 86	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   344: aload 11
    //   346: invokevirtual 105	java/lang/reflect/InvocationTargetException:printStackTrace	()V
    //   349: iconst_0
    //   350: ireturn
    //   351: astore 10
    //   353: aload_0
    //   354: getfield 63	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   357: new 65	java/lang/StringBuilder
    //   360: dup
    //   361: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   364: ldc 107
    //   366: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   369: aload_1
    //   370: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   373: ldc 76
    //   375: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   378: invokevirtual 80	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   381: invokestatic 86	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   384: aload 10
    //   386: invokevirtual 95	java/lang/NoSuchFieldException:printStackTrace	()V
    //   389: iconst_0
    //   390: ireturn
    //
    // Exception table:
    //   from	to	target	type
    //   8	25	71	java/lang/IllegalArgumentException
    //   8	25	111	java/lang/IllegalAccessException
    //   8	25	151	java/lang/NoSuchFieldException
    //   25	43	191	java/lang/NoSuchMethodException
    //   43	69	231	java/lang/IllegalArgumentException
    //   43	69	271	java/lang/IllegalAccessException
    //   43	69	311	java/lang/reflect/InvocationTargetException
    //   43	69	351	java/lang/NoSuchFieldException
  }

  // ERROR //
  private String value(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 28	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mWifiConfig	Landroid/net/wifi/WifiConfiguration;
    //   4: invokevirtual 34	java/lang/Object:getClass	()Ljava/lang/Class;
    //   7: astore_2
    //   8: aload_2
    //   9: aload_1
    //   10: invokevirtual 40	java/lang/Class:getField	(Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   13: aload_0
    //   14: getfield 28	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mWifiConfig	Landroid/net/wifi/WifiConfiguration;
    //   17: invokevirtual 46	java/lang/reflect/Field:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   20: invokevirtual 34	java/lang/Object:getClass	()Ljava/lang/Class;
    //   23: astore 6
    //   25: aload 6
    //   27: ldc 110
    //   29: iconst_0
    //   30: anewarray 36	java/lang/Class
    //   33: invokevirtual 53	java/lang/Class:getMethod	(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   36: astore 8
    //   38: aload 8
    //   40: aload_2
    //   41: aload_1
    //   42: invokevirtual 40	java/lang/Class:getField	(Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   45: aload_0
    //   46: getfield 28	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mWifiConfig	Landroid/net/wifi/WifiConfiguration;
    //   49: invokevirtual 46	java/lang/reflect/Field:get	(Ljava/lang/Object;)Ljava/lang/Object;
    //   52: iconst_0
    //   53: anewarray 30	java/lang/Object
    //   56: invokevirtual 59	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   59: astore 13
    //   61: aload 13
    //   63: checkcast 49	java/lang/String
    //   66: areturn
    //   67: astore 5
    //   69: aload_0
    //   70: getfield 63	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   73: new 65	java/lang/StringBuilder
    //   76: dup
    //   77: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   80: ldc 70
    //   82: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   85: aload_1
    //   86: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   89: ldc 76
    //   91: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   94: invokevirtual 80	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   97: invokestatic 86	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   100: aload 5
    //   102: invokevirtual 89	java/lang/IllegalArgumentException:printStackTrace	()V
    //   105: aconst_null
    //   106: areturn
    //   107: astore 4
    //   109: aload_0
    //   110: getfield 63	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   113: new 65	java/lang/StringBuilder
    //   116: dup
    //   117: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   120: ldc 91
    //   122: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   125: aload_1
    //   126: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   129: ldc 76
    //   131: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   134: invokevirtual 80	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   137: invokestatic 86	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   140: aload 4
    //   142: invokevirtual 92	java/lang/IllegalAccessException:printStackTrace	()V
    //   145: aconst_null
    //   146: areturn
    //   147: astore_3
    //   148: aload_0
    //   149: getfield 63	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   152: new 65	java/lang/StringBuilder
    //   155: dup
    //   156: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   159: ldc 94
    //   161: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   164: aload_1
    //   165: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   168: ldc 76
    //   170: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   173: invokevirtual 80	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   176: invokestatic 86	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   179: aload_3
    //   180: invokevirtual 95	java/lang/NoSuchFieldException:printStackTrace	()V
    //   183: aconst_null
    //   184: areturn
    //   185: astore 7
    //   187: aload_0
    //   188: getfield 63	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   191: new 65	java/lang/StringBuilder
    //   194: dup
    //   195: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   198: ldc 97
    //   200: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   203: aload_1
    //   204: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   207: ldc 76
    //   209: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   212: invokevirtual 80	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   215: invokestatic 86	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   218: aload 7
    //   220: invokevirtual 98	java/lang/NoSuchMethodException:printStackTrace	()V
    //   223: aconst_null
    //   224: areturn
    //   225: astore 12
    //   227: aload_0
    //   228: getfield 63	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   231: new 65	java/lang/StringBuilder
    //   234: dup
    //   235: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   238: ldc 100
    //   240: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   243: aload_1
    //   244: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   247: ldc 76
    //   249: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   252: invokevirtual 80	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   255: invokestatic 86	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   258: aload 12
    //   260: invokevirtual 89	java/lang/IllegalArgumentException:printStackTrace	()V
    //   263: aconst_null
    //   264: areturn
    //   265: astore 11
    //   267: aload_0
    //   268: getfield 63	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   271: new 65	java/lang/StringBuilder
    //   274: dup
    //   275: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   278: ldc 102
    //   280: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   283: aload_1
    //   284: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   287: ldc 76
    //   289: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   292: invokevirtual 80	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   295: invokestatic 86	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   298: aload 11
    //   300: invokevirtual 92	java/lang/IllegalAccessException:printStackTrace	()V
    //   303: aconst_null
    //   304: areturn
    //   305: astore 10
    //   307: aload_0
    //   308: getfield 63	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   311: new 65	java/lang/StringBuilder
    //   314: dup
    //   315: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   318: ldc 112
    //   320: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   323: aload_1
    //   324: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   327: ldc 76
    //   329: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   332: invokevirtual 80	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   335: invokestatic 86	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   338: aload 10
    //   340: invokevirtual 105	java/lang/reflect/InvocationTargetException:printStackTrace	()V
    //   343: aconst_null
    //   344: areturn
    //   345: astore 9
    //   347: aload_0
    //   348: getfield 63	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   351: new 65	java/lang/StringBuilder
    //   354: dup
    //   355: invokespecial 68	java/lang/StringBuilder:<init>	()V
    //   358: ldc 107
    //   360: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   363: aload_1
    //   364: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   367: ldc 76
    //   369: invokevirtual 74	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   372: invokevirtual 80	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   375: invokestatic 86	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   378: aload 9
    //   380: invokevirtual 95	java/lang/NoSuchFieldException:printStackTrace	()V
    //   383: aconst_null
    //   384: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   8	25	67	java/lang/IllegalArgumentException
    //   8	25	107	java/lang/IllegalAccessException
    //   8	25	147	java/lang/NoSuchFieldException
    //   25	38	185	java/lang/NoSuchMethodException
    //   38	61	225	java/lang/IllegalArgumentException
    //   38	61	265	java/lang/IllegalAccessException
    //   38	61	305	java/lang/reflect/InvocationTargetException
    //   38	61	345	java/lang/NoSuchFieldException
  }

  public BitSet getAllowedAuthAlgorithms()
  {
    return this.mWifiConfig.allowedAuthAlgorithms;
  }

  public BitSet getAllowedGroupCiphers()
  {
    return this.mWifiConfig.allowedGroupCiphers;
  }

  public BitSet getAllowedKeyManagement()
  {
    return this.mWifiConfig.allowedKeyManagement;
  }

  public BitSet getAllowedPairwiseCiphers()
  {
    return this.mWifiConfig.allowedPairwiseCiphers;
  }

  public BitSet getAllowedProtocols()
  {
    return this.mWifiConfig.allowedProtocols;
  }

  public String getAnonId()
  {
    return getUnquotedString(value("anonymous_identity"));
  }

  public String getCaCert()
  {
    return getUnquotedString(value("ca_cert"));
  }

  public String getClientCertAlias()
  {
    return getUnquotedString(value("client_cert"));
  }

  public String getClientPrivateKey()
  {
    return value("private_key");
  }

  public String getEap()
  {
    return value("eap");
  }

  public String getEngine()
  {
    return value("engine");
  }

  public String getEngineId()
  {
    return value("engine_id");
  }

  public boolean getHiddenSsid()
  {
    return this.mWifiConfig.hiddenSSID;
  }

  public String getId()
  {
    return getUnquotedString(value("identity"));
  }

  public String getKeyId()
  {
    return value("key_id");
  }

  public LinkPropertiesProxy getLinkProperties()
    throws IllegalAccessException, NoSuchFieldException, IllegalArgumentException, ClassNotFoundException
  {
    return new LinkPropertiesProxy(this.mWifiConfig.getClass().getField("linkProperties").get(this.mWifiConfig));
  }

  public int getNetworkId()
  {
    return this.mWifiConfig.networkId;
  }

  public String getPassword()
  {
    return getUnquotedString(value("password"));
  }

  public String getPhase2()
  {
    return value("phase2");
  }

  public int getPriority()
  {
    return this.mWifiConfig.priority;
  }

  public String getPsk()
  {
    return forceUnquotedString(this.mWifiConfig.preSharedKey);
  }

  public String getSsid()
  {
    return this.mWifiConfig.SSID;
  }

  public int getStatus()
  {
    return this.mWifiConfig.status;
  }

  public boolean setAllowedAuthAlgorithms(BitSet paramBitSet)
  {
    this.mWifiConfig.allowedAuthAlgorithms = paramBitSet;
    return true;
  }

  public boolean setAllowedGroupCiphers(BitSet paramBitSet)
  {
    this.mWifiConfig.allowedGroupCiphers = paramBitSet;
    return true;
  }

  public boolean setAllowedKeyManagement(BitSet paramBitSet)
  {
    this.mWifiConfig.allowedKeyManagement = paramBitSet;
    return true;
  }

  public boolean setAllowedPairwiseCiphers(BitSet paramBitSet)
  {
    this.mWifiConfig.allowedPairwiseCiphers = paramBitSet;
    return true;
  }

  public boolean setAllowedProtocols(BitSet paramBitSet)
  {
    this.mWifiConfig.allowedProtocols = paramBitSet;
    return true;
  }

  public boolean setAnonId(String paramString)
  {
    return setValue("anonymous_identity", getQuotedString(paramString));
  }

  public boolean setCaCert(String paramString)
  {
    return setValue("ca_cert", paramString);
  }

  @SuppressLint({"DefaultLocale"})
  public boolean setClientCert(String paramString)
  {
    if (paramString == null)
    {
      Util.log(this.mLogger, "Attempt to set null client cert!");
      return false;
    }
    if ((!Util.stringIsEmpty(paramString)) && (!paramString.toLowerCase(Locale.ENGLISH).startsWith("keystore://usrcert_")))
      paramString = "keystore://USRCERT_" + paramString;
    return setValue("client_cert", getQuotedString(paramString));
  }

  @SuppressLint({"DefaultLocale"})
  public boolean setClientPrivateKey(String paramString)
  {
    boolean bool2;
    if (Build.VERSION.SDK_INT < 16)
      bool2 = setValue("private_key", paramString);
    boolean bool4;
    do
    {
      boolean bool3;
      do
      {
        boolean bool1;
        do
        {
          return bool2;
          if (Util.stringIsEmpty(paramString))
            break;
          bool1 = setValue("engine", "1");
          bool2 = false;
        }
        while (!bool1);
        bool3 = setValue("engine_id", "keystore");
        bool2 = false;
      }
      while (!bool3);
      String str = paramString.replace("keystore://", "");
      if (!str.toLowerCase(Locale.ENGLISH).startsWith("usrpkey_"))
        str = "USRPKEY_" + str;
      bool4 = setValue("key_id", str);
      bool2 = false;
    }
    while (!bool4);
    return true;
  }

  public boolean setEap(String paramString)
  {
    return setValue("eap", paramString);
  }

  public boolean setHiddenSsid(boolean paramBoolean)
  {
    this.mWifiConfig.hiddenSSID = paramBoolean;
    return true;
  }

  public boolean setId(String paramString)
  {
    return setValue("identity", getQuotedString(paramString));
  }

  public boolean setNetworkId(int paramInt)
  {
    this.mWifiConfig.networkId = paramInt;
    return true;
  }

  public boolean setPassword(String paramString)
  {
    return setValue("password", getQuotedString(paramString));
  }

  public boolean setPhase2(String paramString)
  {
    return setValue("phase2", paramString);
  }

  public boolean setPriority(int paramInt)
  {
    this.mWifiConfig.priority = paramInt;
    return true;
  }

  // ERROR //
  public boolean setProxyState(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 28	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mWifiConfig	Landroid/net/wifi/WifiConfiguration;
    //   4: invokevirtual 34	java/lang/Object:getClass	()Ljava/lang/Class;
    //   7: astore_2
    //   8: aload_2
    //   9: ldc_w 289
    //   12: invokevirtual 40	java/lang/Class:getField	(Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   15: astore 4
    //   17: iload_1
    //   18: tableswitch	default:+22 -> 40, 0:+65->83, 1:+73->91
    //   41: aconst_null
    //   42: fload_1
    //   43: astore 5
    //   45: aload 4
    //   47: aload_0
    //   48: getfield 28	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mWifiConfig	Landroid/net/wifi/WifiConfiguration;
    //   51: aload 4
    //   53: invokevirtual 294	java/lang/reflect/Field:getType	()Ljava/lang/Class;
    //   56: aload 5
    //   58: invokestatic 300	java/lang/Enum:valueOf	(Ljava/lang/Class;Ljava/lang/String;)Ljava/lang/Enum;
    //   61: invokevirtual 304	java/lang/reflect/Field:set	(Ljava/lang/Object;Ljava/lang/Object;)V
    //   64: iconst_1
    //   65: ireturn
    //   66: astore_3
    //   67: aload_0
    //   68: getfield 63	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   71: ldc_w 306
    //   74: invokestatic 86	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   77: aload_3
    //   78: invokevirtual 95	java/lang/NoSuchFieldException:printStackTrace	()V
    //   81: iconst_0
    //   82: ireturn
    //   83: ldc_w 308
    //   86: astore 5
    //   88: goto -43 -> 45
    //   91: ldc_w 310
    //   94: astore 5
    //   96: goto -51 -> 45
    //   99: astore 7
    //   101: aload_0
    //   102: getfield 63	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   105: ldc_w 312
    //   108: invokestatic 86	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   111: aload 7
    //   113: invokevirtual 89	java/lang/IllegalArgumentException:printStackTrace	()V
    //   116: iconst_0
    //   117: ireturn
    //   118: astore 6
    //   120: aload_0
    //   121: getfield 63	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationV1:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   124: ldc_w 314
    //   127: invokestatic 86	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   130: aload 6
    //   132: invokevirtual 92	java/lang/IllegalAccessException:printStackTrace	()V
    //   135: iconst_0
    //   136: ireturn
    //
    // Exception table:
    //   from	to	target	type
    //   8	17	66	java/lang/NoSuchFieldException
    //   45	64	99	java/lang/IllegalArgumentException
    //   45	64	118	java/lang/IllegalAccessException
  }

  public boolean setPsk(String paramString)
  {
    this.mWifiConfig.preSharedKey = paramString;
    return true;
  }

  public boolean setSsid(String paramString)
  {
    this.mWifiConfig.SSID = paramString;
    return true;
  }

  public boolean setStatus(int paramInt)
  {
    this.mWifiConfig.status = paramInt;
    return true;
  }

  public String toFilteredString()
  {
    String str = this.mWifiConfig.toString();
    if (!mStripValues)
      return str;
    String[] arrayOfString = str.split("\n");
    StringBuilder localStringBuilder = new StringBuilder();
    for (int i = 0; i < arrayOfString.length; i++)
    {
      if ((arrayOfString[i].startsWith(" identity")) || (arrayOfString[i].startsWith("identity")))
        arrayOfString[i] = " Identity stripped.";
      if ((arrayOfString[i].startsWith(" password")) || (arrayOfString[i].startsWith("password")))
        arrayOfString[i] = " Password stripped.";
      if ((arrayOfString[i].startsWith(" anonymous_identity")) || (arrayOfString[i].startsWith("anonymous_identity")))
        arrayOfString[i] = " Anon ID stripped.";
    }
    for (int j = 0; j < arrayOfString.length; j++)
      localStringBuilder.append(arrayOfString[j] + "\n");
    return localStringBuilder.toString();
  }
}