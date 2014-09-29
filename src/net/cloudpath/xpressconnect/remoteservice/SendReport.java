package net.cloudpath.xpressconnect.remoteservice;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.SystemClock;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;

public class SendReport
{
  private Context mContext = null;
  private Logger mLogger = null;

  public SendReport(Logger paramLogger, Context paramContext)
  {
    this.mLogger = paramLogger;
    this.mContext = paramContext;
  }

  private String delimit(String paramString)
  {
    return paramString.replace("\"", "\"\"");
  }

  public static void saveAlwaysUsePref(Logger paramLogger, Activity paramActivity, int paramInt)
  {
    if (paramActivity == null)
    {
      Util.log(paramLogger, "No activity specified in saveAlwaysUsePref()!");
      return;
    }
    SharedPreferences localSharedPreferences = paramActivity.getPreferences(0);
    Util.log(paramLogger, "**** Will always use value of " + paramInt + " in the future.");
    SharedPreferences.Editor localEditor = localSharedPreferences.edit();
    localEditor.putBoolean("alwaysuse", true);
    localEditor.putInt("touse", paramInt);
    localEditor.commit();
  }

  // ERROR //
  public static void startReportSendingAttempts(Logger paramLogger, Context paramContext, net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser paramNetworkConfigParser, int paramInt1, int paramInt2, String paramString, android.net.wifi.WifiManager paramWifiManager)
  {
    // Byte code:
    //   0: new 2	net/cloudpath/xpressconnect/remoteservice/SendReport
    //   3: dup
    //   4: aload_0
    //   5: aload_1
    //   6: invokespecial 93	net/cloudpath/xpressconnect/remoteservice/SendReport:<init>	(Lnet/cloudpath/xpressconnect/logger/Logger;Landroid/content/Context;)V
    //   9: astore 7
    //   11: new 95	net/cloudpath/xpressconnect/MapVariables
    //   14: dup
    //   15: aload_2
    //   16: aload_0
    //   17: invokespecial 98	net/cloudpath/xpressconnect/MapVariables:<init>	(Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;Lnet/cloudpath/xpressconnect/logger/Logger;)V
    //   20: astore 8
    //   22: aload 7
    //   24: iload_3
    //   25: iload 4
    //   27: aload_2
    //   28: getfield 104	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:savedConfigInfo	Lnet/cloudpath/xpressconnect/parsers/SavedConfigInfo;
    //   31: getfield 110	net/cloudpath/xpressconnect/parsers/SavedConfigInfo:connectOnly	I
    //   34: aload_2
    //   35: getfield 104	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:savedConfigInfo	Lnet/cloudpath/xpressconnect/parsers/SavedConfigInfo;
    //   38: getfield 114	net/cloudpath/xpressconnect/parsers/SavedConfigInfo:clientId	Ljava/lang/String;
    //   41: aload_2
    //   42: getfield 118	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:networks	Lnet/cloudpath/xpressconnect/parsers/config/NetworkElement;
    //   45: getfield 123	net/cloudpath/xpressconnect/parsers/config/NetworkElement:licensee	Ljava/lang/String;
    //   48: aload_2
    //   49: getfield 127	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:selectedNetwork	Lnet/cloudpath/xpressconnect/parsers/config/NetworkItemElement;
    //   52: getfield 132	net/cloudpath/xpressconnect/parsers/config/NetworkItemElement:name	Ljava/lang/String;
    //   55: aload 8
    //   57: aload 5
    //   59: iconst_0
    //   60: aload 6
    //   62: invokevirtual 136	net/cloudpath/xpressconnect/MapVariables:varMap	(Ljava/lang/String;ZLandroid/net/wifi/WifiManager;)Ljava/lang/String;
    //   65: iconst_0
    //   66: invokevirtual 140	net/cloudpath/xpressconnect/remoteservice/SendReport:buildReport	(IIILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V
    //   69: aload 7
    //   71: invokevirtual 143	net/cloudpath/xpressconnect/remoteservice/SendReport:startSendAttempts	()V
    //   74: return
    //   75: astore 9
    //   77: aload 9
    //   79: invokevirtual 146	java/lang/Exception:printStackTrace	()V
    //   82: aload_0
    //   83: ldc 148
    //   85: invokestatic 39	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   88: aload 7
    //   90: iload_3
    //   91: iload 4
    //   93: aload_2
    //   94: getfield 104	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:savedConfigInfo	Lnet/cloudpath/xpressconnect/parsers/SavedConfigInfo;
    //   97: getfield 110	net/cloudpath/xpressconnect/parsers/SavedConfigInfo:connectOnly	I
    //   100: aload_2
    //   101: getfield 104	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:savedConfigInfo	Lnet/cloudpath/xpressconnect/parsers/SavedConfigInfo;
    //   104: getfield 114	net/cloudpath/xpressconnect/parsers/SavedConfigInfo:clientId	Ljava/lang/String;
    //   107: aload_2
    //   108: getfield 118	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:networks	Lnet/cloudpath/xpressconnect/parsers/config/NetworkElement;
    //   111: getfield 123	net/cloudpath/xpressconnect/parsers/config/NetworkElement:licensee	Ljava/lang/String;
    //   114: aload_2
    //   115: getfield 127	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:selectedNetwork	Lnet/cloudpath/xpressconnect/parsers/config/NetworkItemElement;
    //   118: getfield 132	net/cloudpath/xpressconnect/parsers/config/NetworkItemElement:name	Ljava/lang/String;
    //   121: aload 5
    //   123: iconst_0
    //   124: invokevirtual 140	net/cloudpath/xpressconnect/remoteservice/SendReport:buildReport	(IIILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V
    //   127: goto -58 -> 69
    //   130: astore 9
    //   132: goto -55 -> 77
    //
    // Exception table:
    //   from	to	target	type
    //   11	22	75	java/lang/Exception
    //   22	69	130	java/lang/Exception
  }

  // ERROR //
  public void buildReport(int paramInt1, int paramInt2, int paramInt3, String paramString1, String paramString2, String paramString3, String paramString4, int paramInt4)
  {
    // Byte code:
    //   0: new 47	java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial 48	java/lang/StringBuilder:<init>	()V
    //   7: ldc 156
    //   9: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   12: new 158	java/util/Date
    //   15: dup
    //   16: invokespecial 159	java/util/Date:<init>	()V
    //   19: invokevirtual 163	java/util/Date:getTime	()J
    //   22: invokevirtual 166	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   25: invokevirtual 63	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   28: astore 9
    //   30: ldc 168
    //   32: astore 10
    //   34: aload_0
    //   35: getfield 15	net/cloudpath/xpressconnect/remoteservice/SendReport:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   38: astore 11
    //   40: aconst_null
    //   41: astore 12
    //   43: aload 11
    //   45: ifnull +12 -> 57
    //   48: aload_0
    //   49: getfield 15	net/cloudpath/xpressconnect/remoteservice/SendReport:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   52: invokevirtual 173	net/cloudpath/xpressconnect/logger/Logger:getViewableLines	()Ljava/lang/String;
    //   55: astore 12
    //   57: aload_0
    //   58: getfield 17	net/cloudpath/xpressconnect/remoteservice/SendReport:mContext	Landroid/content/Context;
    //   61: invokevirtual 179	android/content/Context:getPackageManager	()Landroid/content/pm/PackageManager;
    //   64: aload_0
    //   65: getfield 17	net/cloudpath/xpressconnect/remoteservice/SendReport:mContext	Landroid/content/Context;
    //   68: invokevirtual 182	android/content/Context:getPackageName	()Ljava/lang/String;
    //   71: iconst_0
    //   72: invokevirtual 188	android/content/pm/PackageManager:getPackageInfo	(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;
    //   75: getfield 193	android/content/pm/PackageInfo:versionName	Ljava/lang/String;
    //   78: astore 10
    //   80: new 47	java/lang/StringBuilder
    //   83: dup
    //   84: invokespecial 48	java/lang/StringBuilder:<init>	()V
    //   87: ldc 195
    //   89: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   92: iload_1
    //   93: invokevirtual 57	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   96: ldc 197
    //   98: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   101: ldc 199
    //   103: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   106: iload_2
    //   107: invokevirtual 57	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   110: ldc 201
    //   112: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   115: ldc 203
    //   117: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   120: aload 10
    //   122: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   125: ldc 205
    //   127: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   130: ldc 207
    //   132: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   135: iload_3
    //   136: invokevirtual 57	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   139: ldc 209
    //   141: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   144: ldc 211
    //   146: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   149: aload 9
    //   151: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   154: ldc 213
    //   156: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   159: ldc 215
    //   161: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   164: aload 7
    //   166: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   169: ldc 217
    //   171: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: ldc 219
    //   176: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   179: iload 8
    //   181: invokevirtual 57	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   184: ldc 221
    //   186: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   189: ldc 223
    //   191: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   194: aload 4
    //   196: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   199: ldc 225
    //   201: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   204: ldc 227
    //   206: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   209: aload_0
    //   210: aload 5
    //   212: invokespecial 229	net/cloudpath/xpressconnect/remoteservice/SendReport:delimit	(Ljava/lang/String;)Ljava/lang/String;
    //   215: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   218: ldc 231
    //   220: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   223: ldc 233
    //   225: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   228: aload_0
    //   229: aload 6
    //   231: invokespecial 229	net/cloudpath/xpressconnect/remoteservice/SendReport:delimit	(Ljava/lang/String;)Ljava/lang/String;
    //   234: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   237: ldc 235
    //   239: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   242: ldc 237
    //   244: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   247: aload_0
    //   248: getstatic 242	android/os/Build:BOARD	Ljava/lang/String;
    //   251: invokespecial 229	net/cloudpath/xpressconnect/remoteservice/SendReport:delimit	(Ljava/lang/String;)Ljava/lang/String;
    //   254: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   257: ldc 244
    //   259: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   262: ldc 246
    //   264: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   267: aload_0
    //   268: getstatic 249	android/os/Build:BRAND	Ljava/lang/String;
    //   271: invokespecial 229	net/cloudpath/xpressconnect/remoteservice/SendReport:delimit	(Ljava/lang/String;)Ljava/lang/String;
    //   274: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   277: ldc 251
    //   279: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   282: ldc 253
    //   284: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   287: getstatic 256	android/os/Build:CPU_ABI	Ljava/lang/String;
    //   290: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   293: ldc_w 258
    //   296: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   299: ldc_w 260
    //   302: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   305: aload_0
    //   306: getstatic 263	android/os/Build:DEVICE	Ljava/lang/String;
    //   309: invokespecial 229	net/cloudpath/xpressconnect/remoteservice/SendReport:delimit	(Ljava/lang/String;)Ljava/lang/String;
    //   312: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   315: ldc_w 265
    //   318: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   321: ldc_w 267
    //   324: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   327: aload_0
    //   328: getstatic 270	android/os/Build:DISPLAY	Ljava/lang/String;
    //   331: invokespecial 229	net/cloudpath/xpressconnect/remoteservice/SendReport:delimit	(Ljava/lang/String;)Ljava/lang/String;
    //   334: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   337: ldc_w 272
    //   340: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   343: ldc_w 274
    //   346: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   349: getstatic 277	android/os/Build:FINGERPRINT	Ljava/lang/String;
    //   352: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   355: ldc_w 279
    //   358: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   361: ldc_w 281
    //   364: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   367: getstatic 284	android/os/Build:HOST	Ljava/lang/String;
    //   370: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   373: ldc_w 286
    //   376: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   379: ldc_w 288
    //   382: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   385: aload_0
    //   386: getstatic 291	android/os/Build:ID	Ljava/lang/String;
    //   389: invokespecial 229	net/cloudpath/xpressconnect/remoteservice/SendReport:delimit	(Ljava/lang/String;)Ljava/lang/String;
    //   392: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   395: ldc_w 293
    //   398: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   401: ldc_w 295
    //   404: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   407: aload_0
    //   408: getstatic 298	android/os/Build:MANUFACTURER	Ljava/lang/String;
    //   411: invokespecial 229	net/cloudpath/xpressconnect/remoteservice/SendReport:delimit	(Ljava/lang/String;)Ljava/lang/String;
    //   414: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   417: ldc_w 300
    //   420: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   423: ldc_w 302
    //   426: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   429: aload_0
    //   430: getstatic 305	android/os/Build:MODEL	Ljava/lang/String;
    //   433: invokespecial 229	net/cloudpath/xpressconnect/remoteservice/SendReport:delimit	(Ljava/lang/String;)Ljava/lang/String;
    //   436: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   439: ldc_w 307
    //   442: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   445: ldc_w 309
    //   448: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   451: aload_0
    //   452: getstatic 312	android/os/Build:PRODUCT	Ljava/lang/String;
    //   455: invokespecial 229	net/cloudpath/xpressconnect/remoteservice/SendReport:delimit	(Ljava/lang/String;)Ljava/lang/String;
    //   458: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   461: ldc_w 314
    //   464: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   467: ldc_w 316
    //   470: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   473: getstatic 319	android/os/Build:TAGS	Ljava/lang/String;
    //   476: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   479: ldc_w 321
    //   482: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   485: ldc_w 323
    //   488: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   491: getstatic 327	android/os/Build:TIME	J
    //   494: invokevirtual 166	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   497: ldc_w 329
    //   500: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   503: ldc_w 331
    //   506: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   509: getstatic 334	android/os/Build:TYPE	Ljava/lang/String;
    //   512: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   515: ldc_w 336
    //   518: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   521: ldc_w 338
    //   524: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   527: aload_0
    //   528: getstatic 341	android/os/Build:USER	Ljava/lang/String;
    //   531: invokespecial 229	net/cloudpath/xpressconnect/remoteservice/SendReport:delimit	(Ljava/lang/String;)Ljava/lang/String;
    //   534: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   537: ldc_w 343
    //   540: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   543: ldc_w 345
    //   546: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   549: aload_0
    //   550: getstatic 350	android/os/Build$VERSION:CODENAME	Ljava/lang/String;
    //   553: invokespecial 229	net/cloudpath/xpressconnect/remoteservice/SendReport:delimit	(Ljava/lang/String;)Ljava/lang/String;
    //   556: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   559: ldc_w 352
    //   562: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   565: ldc_w 354
    //   568: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   571: aload_0
    //   572: getstatic 357	android/os/Build$VERSION:INCREMENTAL	Ljava/lang/String;
    //   575: invokespecial 229	net/cloudpath/xpressconnect/remoteservice/SendReport:delimit	(Ljava/lang/String;)Ljava/lang/String;
    //   578: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   581: ldc_w 359
    //   584: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   587: ldc_w 361
    //   590: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   593: aload_0
    //   594: getstatic 364	android/os/Build$VERSION:RELEASE	Ljava/lang/String;
    //   597: invokespecial 229	net/cloudpath/xpressconnect/remoteservice/SendReport:delimit	(Ljava/lang/String;)Ljava/lang/String;
    //   600: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   603: ldc_w 366
    //   606: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   609: ldc_w 368
    //   612: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   615: getstatic 371	android/os/Build$VERSION:SDK_INT	I
    //   618: invokevirtual 57	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   621: ldc_w 373
    //   624: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   627: ldc_w 375
    //   630: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   633: aload 12
    //   635: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   638: ldc_w 377
    //   641: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   644: ldc_w 379
    //   647: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   650: invokevirtual 63	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   653: astore 15
    //   655: aload 15
    //   657: invokestatic 384	com/cloudpath/common/util/Encode:encode	(Ljava/lang/String;)Ljava/lang/String;
    //   660: astore 16
    //   662: new 386	net/cloudpath/xpressconnect/LocalDbHelper
    //   665: dup
    //   666: aload_0
    //   667: getfield 17	net/cloudpath/xpressconnect/remoteservice/SendReport:mContext	Landroid/content/Context;
    //   670: ldc_w 388
    //   673: aconst_null
    //   674: bipush 8
    //   676: invokespecial 391	net/cloudpath/xpressconnect/LocalDbHelper:<init>	(Landroid/content/Context;Ljava/lang/String;Landroid/database/sqlite/SQLiteDatabase$CursorFactory;I)V
    //   679: astore 17
    //   681: aload 17
    //   683: invokevirtual 395	net/cloudpath/xpressconnect/LocalDbHelper:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   686: astore 18
    //   688: aload 18
    //   690: new 47	java/lang/StringBuilder
    //   693: dup
    //   694: invokespecial 48	java/lang/StringBuilder:<init>	()V
    //   697: ldc_w 397
    //   700: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   703: aload 16
    //   705: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   708: ldc_w 399
    //   711: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   714: iload_1
    //   715: invokevirtual 57	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   718: ldc_w 401
    //   721: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   724: iload_2
    //   725: invokevirtual 57	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   728: ldc_w 403
    //   731: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   734: aload 10
    //   736: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   739: ldc_w 399
    //   742: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   745: iload_3
    //   746: invokevirtual 57	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   749: ldc_w 403
    //   752: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   755: aload 9
    //   757: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   760: ldc_w 405
    //   763: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   766: aload 7
    //   768: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   771: ldc_w 399
    //   774: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   777: iload 8
    //   779: invokevirtual 57	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   782: ldc_w 403
    //   785: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   788: aload 4
    //   790: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   793: ldc_w 405
    //   796: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   799: aload 5
    //   801: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   804: ldc_w 405
    //   807: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   810: aload 6
    //   812: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   815: ldc_w 405
    //   818: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   821: getstatic 242	android/os/Build:BOARD	Ljava/lang/String;
    //   824: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   827: ldc_w 405
    //   830: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   833: getstatic 249	android/os/Build:BRAND	Ljava/lang/String;
    //   836: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   839: ldc_w 405
    //   842: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   845: getstatic 256	android/os/Build:CPU_ABI	Ljava/lang/String;
    //   848: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   851: ldc_w 405
    //   854: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   857: getstatic 263	android/os/Build:DEVICE	Ljava/lang/String;
    //   860: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   863: ldc_w 405
    //   866: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   869: getstatic 270	android/os/Build:DISPLAY	Ljava/lang/String;
    //   872: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   875: ldc_w 405
    //   878: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   881: getstatic 277	android/os/Build:FINGERPRINT	Ljava/lang/String;
    //   884: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   887: ldc_w 405
    //   890: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   893: getstatic 284	android/os/Build:HOST	Ljava/lang/String;
    //   896: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   899: ldc_w 405
    //   902: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   905: getstatic 291	android/os/Build:ID	Ljava/lang/String;
    //   908: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   911: ldc_w 405
    //   914: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   917: getstatic 298	android/os/Build:MANUFACTURER	Ljava/lang/String;
    //   920: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   923: ldc_w 405
    //   926: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   929: getstatic 305	android/os/Build:MODEL	Ljava/lang/String;
    //   932: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   935: ldc_w 405
    //   938: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   941: getstatic 312	android/os/Build:PRODUCT	Ljava/lang/String;
    //   944: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   947: ldc_w 405
    //   950: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   953: getstatic 319	android/os/Build:TAGS	Ljava/lang/String;
    //   956: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   959: ldc_w 405
    //   962: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   965: getstatic 327	android/os/Build:TIME	J
    //   968: invokevirtual 166	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   971: ldc_w 405
    //   974: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   977: getstatic 334	android/os/Build:TYPE	Ljava/lang/String;
    //   980: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   983: ldc_w 405
    //   986: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   989: getstatic 341	android/os/Build:USER	Ljava/lang/String;
    //   992: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   995: ldc_w 405
    //   998: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1001: getstatic 350	android/os/Build$VERSION:CODENAME	Ljava/lang/String;
    //   1004: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1007: ldc_w 405
    //   1010: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1013: getstatic 357	android/os/Build$VERSION:INCREMENTAL	Ljava/lang/String;
    //   1016: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1019: ldc_w 405
    //   1022: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1025: getstatic 364	android/os/Build$VERSION:RELEASE	Ljava/lang/String;
    //   1028: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1031: ldc_w 405
    //   1034: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1037: getstatic 371	android/os/Build$VERSION:SDK_INT	I
    //   1040: invokevirtual 57	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   1043: ldc_w 405
    //   1046: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1049: aload 12
    //   1051: ldc_w 407
    //   1054: invokestatic 412	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1057: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1060: ldc_w 414
    //   1063: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1066: invokevirtual 63	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1069: invokevirtual 420	android/database/sqlite/SQLiteDatabase:execSQL	(Ljava/lang/String;)V
    //   1072: aload 18
    //   1074: invokevirtual 423	android/database/sqlite/SQLiteDatabase:close	()V
    //   1077: aload 17
    //   1079: invokevirtual 424	net/cloudpath/xpressconnect/LocalDbHelper:close	()V
    //   1082: return
    //   1083: astore 13
    //   1085: aload_0
    //   1086: getfield 15	net/cloudpath/xpressconnect/remoteservice/SendReport:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   1089: new 47	java/lang/StringBuilder
    //   1092: dup
    //   1093: invokespecial 48	java/lang/StringBuilder:<init>	()V
    //   1096: ldc_w 426
    //   1099: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1102: aload 13
    //   1104: invokevirtual 429	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   1107: invokevirtual 54	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1110: invokevirtual 63	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   1113: invokestatic 39	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   1116: aload_0
    //   1117: getfield 15	net/cloudpath/xpressconnect/remoteservice/SendReport:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   1120: ldc_w 431
    //   1123: invokestatic 39	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   1126: goto -1046 -> 80
    //   1129: astore 14
    //   1131: return
    //   1132: astore 20
    //   1134: aload 20
    //   1136: invokevirtual 432	android/database/SQLException:printStackTrace	()V
    //   1139: return
    //   1140: astore 19
    //   1142: aload 19
    //   1144: invokevirtual 433	java/io/UnsupportedEncodingException:printStackTrace	()V
    //   1147: return
    //
    // Exception table:
    //   from	to	target	type
    //   57	80	1083	java/lang/Exception
    //   80	655	1129	java/lang/OutOfMemoryError
    //   688	1072	1132	android/database/SQLException
    //   688	1072	1140	java/io/UnsupportedEncodingException
  }

  public void startSendAttempts()
  {
    Util.log(this.mLogger, "Starting status reporting service...");
    AlarmManager localAlarmManager = (AlarmManager)this.mContext.getSystemService("alarm");
    Intent localIntent = new Intent(this.mContext, OnAlarmReceiver.class);
    PendingIntent localPendingIntent = PendingIntent.getBroadcast(this.mContext, 0, localIntent, 0);
    localAlarmManager.setInexactRepeating(3, SystemClock.elapsedRealtime(), 900000L, localPendingIntent);
  }
}