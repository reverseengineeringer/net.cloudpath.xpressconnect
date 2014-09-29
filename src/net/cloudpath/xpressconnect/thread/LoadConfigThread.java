package net.cloudpath.xpressconnect.thread;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.util.Log;
import net.cloudpath.xpressconnect.DumpAndroidData;
import net.cloudpath.xpressconnect.LoadConfigFromDb;
import net.cloudpath.xpressconnect.LocalDbHelper;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.kvp.ParseKvp;
import net.cloudpath.xpressconnect.remote.WebInterface;

public class LoadConfigThread extends ThreadBase
{
  private GetGlobals mGlobals = null;
  private String mNetworkName = null;
  private Activity mParent = null;
  private NetworkConfigParser mParser = null;
  private volatile ThreadCom mThreadCom = null;
  private WifiManager mWifi = null;

  public LoadConfigThread(ThreadCom paramThreadCom, NetworkConfigParser paramNetworkConfigParser, String paramString1, Logger paramLogger, WifiManager paramWifiManager, GetGlobals paramGetGlobals, String paramString2, Activity paramActivity)
  {
    super(paramLogger);
    setName("LoadConfig thread.");
    this.mThreadCom = paramThreadCom;
    this.mParent = paramActivity;
    this.mParser = paramNetworkConfigParser;
    this.mWifi = paramWifiManager;
    this.mNetworkName = paramString1;
    this.mGlobals = paramGetGlobals;
  }

  private void failLoading()
  {
    spinLock();
    this.mThreadCom.doFailure(Boolean.valueOf(false), -1, null);
    unlock();
  }

  @SuppressLint({"DefaultLocale"})
  private int getColorFromStr(String paramString)
  {
    if (paramString == null)
      return -1;
    if (paramString.length() == 6)
      try
      {
        int i = Integer.parseInt(paramString.toLowerCase(), 16);
        return i;
      }
      catch (NumberFormatException localNumberFormatException)
      {
        return 0;
      }
    Util.log(this.mLogger, "The string '" + paramString + "' is not a recognized color.");
    return -1;
  }

  // ERROR //
  private void getLogoFile(String paramString1, String paramString2)
  {
    // Byte code:
    //   0: new 115	java/net/URL
    //   3: dup
    //   4: aload_1
    //   5: invokespecial 117	java/net/URL:<init>	(Ljava/lang/String;)V
    //   8: astore_3
    //   9: aload_3
    //   10: invokevirtual 121	java/net/URL:openConnection	()Ljava/net/URLConnection;
    //   13: invokevirtual 127	java/net/URLConnection:getInputStream	()Ljava/io/InputStream;
    //   16: astore 5
    //   18: new 129	java/io/BufferedInputStream
    //   21: dup
    //   22: aload 5
    //   24: sipush 128
    //   27: invokespecial 132	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;I)V
    //   30: astore 6
    //   32: new 134	org/apache/http/util/ByteArrayBuffer
    //   35: dup
    //   36: sipush 128
    //   39: invokespecial 137	org/apache/http/util/ByteArrayBuffer:<init>	(I)V
    //   42: astore 7
    //   44: aload 6
    //   46: invokevirtual 140	java/io/BufferedInputStream:read	()I
    //   49: istore 9
    //   51: iload 9
    //   53: iconst_m1
    //   54: if_icmpeq +86 -> 140
    //   57: aload 7
    //   59: iload 9
    //   61: i2b
    //   62: invokevirtual 142	org/apache/http/util/ByteArrayBuffer:append	(I)V
    //   65: goto -21 -> 44
    //   68: astore 8
    //   70: aload 8
    //   72: invokevirtual 145	java/io/IOException:printStackTrace	()V
    //   75: aload_0
    //   76: getfield 86	net/cloudpath/xpressconnect/thread/LoadConfigThread:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   79: ldc 147
    //   81: invokestatic 107	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   84: return
    //   85: astore 15
    //   87: new 149	net/cloudpath/xpressconnect/LoadConfigFromDb
    //   90: dup
    //   91: aload_0
    //   92: getfield 86	net/cloudpath/xpressconnect/thread/LoadConfigThread:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   95: aload_0
    //   96: getfield 33	net/cloudpath/xpressconnect/thread/LoadConfigThread:mGlobals	Lnet/cloudpath/xpressconnect/localservice/GetGlobals;
    //   99: aload_0
    //   100: getfield 27	net/cloudpath/xpressconnect/thread/LoadConfigThread:mParent	Landroid/app/Activity;
    //   103: invokespecial 152	net/cloudpath/xpressconnect/LoadConfigFromDb:<init>	(Lnet/cloudpath/xpressconnect/logger/Logger;Lnet/cloudpath/xpressconnect/localservice/GetGlobals;Landroid/content/Context;)V
    //   106: aload_2
    //   107: invokevirtual 156	net/cloudpath/xpressconnect/LoadConfigFromDb:localLoadBrandingImage	(Ljava/lang/String;)Z
    //   110: ifne -26 -> 84
    //   113: aload_0
    //   114: getfield 86	net/cloudpath/xpressconnect/thread/LoadConfigThread:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   117: new 88	java/lang/StringBuilder
    //   120: dup
    //   121: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   124: ldc 158
    //   126: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   129: aload_1
    //   130: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   133: invokevirtual 101	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   136: invokestatic 107	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   139: return
    //   140: new 160	net/cloudpath/xpressconnect/LocalDbHelper
    //   143: dup
    //   144: aload_0
    //   145: getfield 27	net/cloudpath/xpressconnect/thread/LoadConfigThread:mParent	Landroid/app/Activity;
    //   148: ldc 162
    //   150: aconst_null
    //   151: bipush 8
    //   153: invokespecial 165	net/cloudpath/xpressconnect/LocalDbHelper:<init>	(Landroid/content/Context;Ljava/lang/String;Landroid/database/sqlite/SQLiteDatabase$CursorFactory;I)V
    //   156: astore 10
    //   158: aload 10
    //   160: invokevirtual 169	net/cloudpath/xpressconnect/LocalDbHelper:getWritableDatabase	()Landroid/database/sqlite/SQLiteDatabase;
    //   163: astore 11
    //   165: new 171	android/content/ContentValues
    //   168: dup
    //   169: invokespecial 172	android/content/ContentValues:<init>	()V
    //   172: astore 12
    //   174: aload 12
    //   176: ldc 174
    //   178: aload 7
    //   180: invokevirtual 178	org/apache/http/util/ByteArrayBuffer:toByteArray	()[B
    //   183: invokevirtual 182	android/content/ContentValues:put	(Ljava/lang/String;[B)V
    //   186: aload_0
    //   187: getfield 31	net/cloudpath/xpressconnect/thread/LoadConfigThread:mNetworkName	Ljava/lang/String;
    //   190: ifnonnull +102 -> 292
    //   193: aload 11
    //   195: ldc 184
    //   197: aload 12
    //   199: new 88	java/lang/StringBuilder
    //   202: dup
    //   203: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   206: ldc 186
    //   208: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   211: aload_0
    //   212: getfield 33	net/cloudpath/xpressconnect/thread/LoadConfigThread:mGlobals	Lnet/cloudpath/xpressconnect/localservice/GetGlobals;
    //   215: invokevirtual 191	net/cloudpath/xpressconnect/localservice/GetGlobals:getLoadedFromUrl	()Ljava/lang/String;
    //   218: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   221: ldc 193
    //   223: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   226: invokevirtual 101	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   229: aconst_null
    //   230: invokevirtual 199	android/database/sqlite/SQLiteDatabase:update	(Ljava/lang/String;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   233: iconst_1
    //   234: if_icmpeq +12 -> 246
    //   237: aload_0
    //   238: getfield 86	net/cloudpath/xpressconnect/thread/LoadConfigThread:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   241: ldc 201
    //   243: invokestatic 107	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   246: aload 11
    //   248: invokevirtual 204	android/database/sqlite/SQLiteDatabase:close	()V
    //   251: aload 10
    //   253: invokevirtual 205	net/cloudpath/xpressconnect/LocalDbHelper:close	()V
    //   256: new 207	java/io/ByteArrayInputStream
    //   259: dup
    //   260: aload 7
    //   262: invokevirtual 178	org/apache/http/util/ByteArrayBuffer:toByteArray	()[B
    //   265: invokespecial 210	java/io/ByteArrayInputStream:<init>	([B)V
    //   268: astore 14
    //   270: aload_0
    //   271: getfield 33	net/cloudpath/xpressconnect/thread/LoadConfigThread:mGlobals	Lnet/cloudpath/xpressconnect/localservice/GetGlobals;
    //   274: aload 14
    //   276: invokestatic 216	android/graphics/BitmapFactory:decodeStream	(Ljava/io/InputStream;)Landroid/graphics/Bitmap;
    //   279: invokevirtual 220	net/cloudpath/xpressconnect/localservice/GetGlobals:setBrandingImg	(Landroid/graphics/Bitmap;)V
    //   282: aload_0
    //   283: getfield 86	net/cloudpath/xpressconnect/thread/LoadConfigThread:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   286: ldc 222
    //   288: invokestatic 107	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   291: return
    //   292: ldc 224
    //   294: ldc 226
    //   296: invokestatic 232	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
    //   299: pop
    //   300: aload 11
    //   302: ldc 184
    //   304: aload 12
    //   306: new 88	java/lang/StringBuilder
    //   309: dup
    //   310: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   313: ldc 186
    //   315: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   318: aload_0
    //   319: getfield 33	net/cloudpath/xpressconnect/thread/LoadConfigThread:mGlobals	Lnet/cloudpath/xpressconnect/localservice/GetGlobals;
    //   322: invokevirtual 191	net/cloudpath/xpressconnect/localservice/GetGlobals:getLoadedFromUrl	()Ljava/lang/String;
    //   325: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   328: ldc 234
    //   330: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   333: aload_0
    //   334: getfield 31	net/cloudpath/xpressconnect/thread/LoadConfigThread:mNetworkName	Ljava/lang/String;
    //   337: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   340: ldc 193
    //   342: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   345: invokevirtual 101	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   348: aconst_null
    //   349: invokevirtual 199	android/database/sqlite/SQLiteDatabase:update	(Ljava/lang/String;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
    //   352: iconst_1
    //   353: if_icmpeq -107 -> 246
    //   356: aload_0
    //   357: getfield 86	net/cloudpath/xpressconnect/thread/LoadConfigThread:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   360: ldc 201
    //   362: invokestatic 107	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   365: goto -119 -> 246
    //   368: astore 4
    //   370: goto -283 -> 87
    //
    // Exception table:
    //   from	to	target	type
    //   44	51	68	java/io/IOException
    //   57	65	68	java/io/IOException
    //   0	9	85	java/lang/Exception
    //   9	18	368	java/lang/Exception
  }

  private void logColorInfo(String paramString1, String paramString2)
  {
    if (Util.stringIsEmpty(paramString1))
    {
      Util.log(this.mLogger, "No color variable name specified.  (Programming error!)");
      return;
    }
    if (paramString2 == null)
    {
      Util.log(this.mLogger, paramString1 + " : " + "<undefined>");
      return;
    }
    Util.log(this.mLogger, paramString1 + " : " + paramString2);
  }

  private void pullSystemConfigXml()
  {
    WebInterface localWebInterface = new WebInterface(this.mLogger);
    ParseKvp localParseKvp = new ParseKvp(this.mLogger);
    Util.log(this.mLogger, "URL : " + this.mGlobals.getLoadedFromUrl());
    String str1 = this.mGlobals.getLoadedFromUrl();
    if ((str1 == null) || (str1.contentEquals("null")))
    {
      Util.log(this.mLogger, "No config URL available.  Skinning won't happen.");
      return;
    }
    String str2;
    String str3;
    try
    {
      str2 = str1.substring(0, str1.indexOf("/network_config_android.xml"));
      Util.log(this.mLogger, "Stripped URL : " + str2);
      str3 = localWebInterface.getWebPage(str2 + "/system_config.xml", true);
      Util.log(this.mLogger, "Status Code : " + localWebInterface.resultCode);
      Util.log(this.mLogger, "Status Text : " + localWebInterface.statusText);
      if (localWebInterface.resultCode != 200)
      {
        Util.log(this.mLogger, "Unable to parse remote configuration.  Trying local.");
        str3 = new LoadConfigFromDb(this.mLogger, this.mGlobals, this.mParent).getSystemConfigXml(this.mNetworkName);
        if (str3 == null)
        {
          Util.log(this.mLogger, "Unable to find local system configuration.  Customization won't be available.");
          return;
        }
      }
    }
    catch (StringIndexOutOfBoundsException localStringIndexOutOfBoundsException)
    {
      Util.log(this.mLogger, "Unable to gather skinning information.  Skipping.");
      return;
    }
    if (!localParseKvp.parse(str3))
    {
      Util.log(this.mLogger, "Unable to parse system configuration data.");
      return;
    }
    saveConfigDataToDb(str3);
    String str4 = localParseKvp.getValueFor("logo_file");
    if (str4 == null)
      Util.log(this.mLogger, "Logo path : <undefined>");
    String str5;
    while (true)
    {
      getLogoFile(str2 + "/" + str4, this.mNetworkName);
      logColorInfo("Background Color", localParseKvp.getValueFor("scheme.background_color"));
      this.mGlobals.setBackgroundColor(getColorFromStr(localParseKvp.getValueFor("scheme.background_color")));
      logColorInfo("Inactive Tab Border Color", localParseKvp.getValueFor("scheme.inactive_tab_border_color"));
      this.mGlobals.setInactiveTabBorderColor(getColorFromStr(localParseKvp.getValueFor("scheme.inactive_tab_border_color")));
      logColorInfo("Active Tab Border Color", localParseKvp.getValueFor("scheme.active_tab_border_color"));
      this.mGlobals.setActiveTabBorderColor(getColorFromStr(localParseKvp.getValueFor("scheme.active_tab_border_color")));
      logColorInfo("Line Color", localParseKvp.getValueFor("scheme.line_color"));
      this.mGlobals.setLineColor(getColorFromStr(localParseKvp.getValueFor("scheme.line_color")));
      logColorInfo("Outline Color", localParseKvp.getValueFor("scheme.outline_color"));
      this.mGlobals.setOutlineColor(getColorFromStr(localParseKvp.getValueFor("scheme.outline_color")));
      logColorInfo("Licensed To Font Color", localParseKvp.getValueFor("scheme.licensed_to_font_color"));
      this.mGlobals.setLicensedToFontColor(getColorFromStr(localParseKvp.getValueFor("scheme.licensed_to_font_color")));
      logColorInfo("Active Tab Font Color", localParseKvp.getValueFor("scheme.active_tab_font_color"));
      this.mGlobals.setActiveTabFontColor(getColorFromStr(localParseKvp.getValueFor("scheme.active_tab_font_color")));
      logColorInfo("Inactive Tab Font Color", localParseKvp.getValueFor("scheme.inactive_tab_font_color"));
      this.mGlobals.setInactiveTabFontColor(getColorFromStr(localParseKvp.getValueFor("scheme.inactive_tab_font_color")));
      str5 = localParseKvp.getValueFor("scheme.corner_style");
      if (str5 != null)
        break;
      Util.log(this.mLogger, "Corner Style : <undefined>");
      return;
      Util.log(this.mLogger, "Logo path : " + str4);
    }
    Util.log(this.mLogger, "Corner Style : " + str5);
    this.mGlobals.setCornerStyle(Util.parseInt(str5, 0));
  }

  private void saveConfigDataToDb(String paramString)
  {
    LocalDbHelper localLocalDbHelper = new LocalDbHelper(this.mParent, "XPC_DB", null, 8);
    SQLiteDatabase localSQLiteDatabase = localLocalDbHelper.getWritableDatabase();
    String str = paramString.replace("\"", "\"\"");
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("systemConfig", str);
    Util.log(this.mLogger, "Writing configuration to the local DB.  (Name : " + this.mNetworkName + "  URL : " + this.mGlobals.getLoadedFromUrl() + ")");
    if (this.mNetworkName == null)
      if (localSQLiteDatabase.update("configuredNetworks", localContentValues, "url='" + this.mGlobals.getLoadedFromUrl() + "'", null) < 1)
        Util.log(this.mLogger, "Unable to update the sys config information.  Branding may not be available when using a cached config.");
    while (true)
    {
      localSQLiteDatabase.close();
      localLocalDbHelper.close();
      return;
      Log.d("XPC", "Looking with a name in mind.");
      if (localSQLiteDatabase.update("configuredNetworks", localContentValues, "url='" + this.mGlobals.getLoadedFromUrl() + "' and name='" + this.mNetworkName + "'", null) < 1)
        Util.log(this.mLogger, "Unable to update the sys config information.  Branding may not be available when using a cached config.");
    }
  }

  public void resume(ThreadCom paramThreadCom, GetGlobals paramGetGlobals, Activity paramActivity)
  {
    this.mThreadCom = paramThreadCom;
    this.mParent = paramActivity;
    this.mGlobals = paramGetGlobals;
  }

  public void run()
  {
    super.run();
    new DumpAndroidData(this.mLogger, this.mWifi);
    if (shouldDie());
    while (true)
    {
      return;
      Util.log(this.mLogger, "***** Starting config parser thread *****");
      if (this.mGlobals == null)
      {
        Util.log(this.mLogger, "Globals null in load config thread.");
        failLoading();
        return;
      }
      if (shouldDie())
        continue;
      if (this.mGlobals.getXmlConfig() == null)
      {
        Util.log(this.mLogger, "XML config null in load config thread.");
        failLoading();
        return;
      }
      if (shouldDie())
        continue;
      try
      {
        this.mParser.parseFromString(this.mGlobals.getXmlConfig());
        if (shouldDie())
          continue;
        pullSystemConfigXml();
        if (shouldDie())
          continue;
        spinLock();
        this.mThreadCom.doSuccess();
        unlock();
        return;
      }
      catch (Exception localException)
      {
        while (true)
        {
          localException.printStackTrace();
          failLoading();
        }
      }
    }
  }
}