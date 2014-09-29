package net.cloudpath.xpressconnect.screens.delegates;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.Environment;
import android.widget.Toast;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import net.cloudpath.xpressconnect.AndroidVersion;
import net.cloudpath.xpressconnect.DeviceBlacklist;
import net.cloudpath.xpressconnect.LoadConfigFromDb;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.remote.GetConfigFromUrl;
import net.cloudpath.xpressconnect.remote.WebInterface;
import net.cloudpath.xpressconnect.screens.GotoScreen;
import net.cloudpath.xpressconnect.thread.FindByDnsName;
import net.cloudpath.xpressconnect.thread.FindByDnsSearchDomain;
import net.cloudpath.xpressconnect.thread.ThreadBase;

public class EntryScreenDelegate extends ThreadBase
{
  public static final int GOTO_FLOW_CONTROL = 1;
  public static final int GOTO_XPCES_MESSAGE = 2;
  public static final int NO_OP;
  private static boolean VERBOSE_LOGGING = false;
  private volatile Activity mActivity = null;
  private Context mContext = null;
  private GetGlobals mGlobals = null;
  private String mIntentData = null;
  private volatile Logger mLogger = null;
  private String mPassword = null;
  private boolean mRunningAsLibrary = false;
  private String mTargetUrl = null;
  private String mUsername = null;

  public EntryScreenDelegate(Activity paramActivity)
  {
    super(null);
    this.mActivity = paramActivity;
    this.mContext = paramActivity;
  }

  public EntryScreenDelegate(Activity paramActivity, Context paramContext)
  {
    super(null);
    this.mActivity = paramActivity;
    this.mContext = paramContext;
  }

  public void findConfiguration()
  {
    start();
  }

  public void gatherOptionalSettings(Intent paramIntent)
  {
    if (paramIntent == null)
    {
      Util.log(this.mLogger, "Null intent passed to gatherOptionalSettings().");
      return;
    }
    setAsLibrary(paramIntent.getBooleanExtra("library_mode", false));
    this.mIntentData = paramIntent.getDataString();
    Util.log(this.mLogger, "URI : " + this.mIntentData);
    setTargetUrl(paramIntent.getDataString());
  }

  public boolean getAsLibrary()
  {
    return this.mRunningAsLibrary;
  }

  public String getPassword()
  {
    return this.mPassword;
  }

  public String getUsername()
  {
    return this.mUsername;
  }

  public void gotoFlowControl(String paramString1, String paramString2)
  {
    Util.log(this.mLogger, "Transitioning to FlowControl screen.");
    GotoScreen.flowControl(this.mActivity, paramString1, paramString2, this.mUsername, this.mPassword);
  }

  public void gotoFlowControlOnUi()
  {
    this.mActivity.runOnUiThread(new Runnable()
    {
      public void run()
      {
        EntryScreenDelegate.this.gotoFlowControl(null, null);
      }
    });
  }

  public boolean isBlacklistedDevice()
  {
    return isBlacklistedDevice(new DeviceBlacklist());
  }

  protected boolean isBlacklistedDevice(DeviceBlacklist paramDeviceBlacklist)
  {
    return paramDeviceBlacklist.thisDeviceIsUsable() != 0;
  }

  // ERROR //
  public boolean loadConfigFromFile(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 39	net/cloudpath/xpressconnect/screens/delegates/EntryScreenDelegate:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   4: new 88	java/lang/StringBuilder
    //   7: dup
    //   8: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   11: ldc 148
    //   13: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   16: aload_1
    //   17: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   20: invokevirtual 99	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   23: invokestatic 70	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   26: new 150	java/io/File
    //   29: dup
    //   30: aload_1
    //   31: invokespecial 152	java/io/File:<init>	(Ljava/lang/String;)V
    //   34: invokevirtual 155	java/io/File:exists	()Z
    //   37: ifne +5 -> 42
    //   40: iconst_0
    //   41: ireturn
    //   42: new 157	java/lang/StringBuffer
    //   45: dup
    //   46: invokespecial 158	java/lang/StringBuffer:<init>	()V
    //   49: astore_2
    //   50: new 160	java/io/BufferedReader
    //   53: dup
    //   54: new 162	java/io/FileReader
    //   57: dup
    //   58: aload_1
    //   59: invokespecial 163	java/io/FileReader:<init>	(Ljava/lang/String;)V
    //   62: invokespecial 166	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   65: astore_3
    //   66: aload_3
    //   67: invokevirtual 169	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   70: astore 6
    //   72: aload 6
    //   74: astore 7
    //   76: aload 7
    //   78: ifnull +94 -> 172
    //   81: aload_2
    //   82: new 88	java/lang/StringBuilder
    //   85: dup
    //   86: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   89: aload 7
    //   91: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   94: ldc 171
    //   96: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   99: invokevirtual 99	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   102: invokevirtual 174	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   105: pop
    //   106: aload_3
    //   107: invokevirtual 169	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   110: astore 12
    //   112: aload 12
    //   114: astore 7
    //   116: goto -40 -> 76
    //   119: astore 13
    //   121: aload 13
    //   123: invokevirtual 177	java/io/FileNotFoundException:printStackTrace	()V
    //   126: iconst_0
    //   127: ireturn
    //   128: astore 4
    //   130: aload 4
    //   132: invokevirtual 178	java/io/IOException:printStackTrace	()V
    //   135: aload_3
    //   136: invokevirtual 181	java/io/BufferedReader:close	()V
    //   139: iconst_0
    //   140: ireturn
    //   141: astore 5
    //   143: aload 5
    //   145: invokevirtual 178	java/io/IOException:printStackTrace	()V
    //   148: iconst_0
    //   149: ireturn
    //   150: astore 10
    //   152: aload 10
    //   154: invokevirtual 178	java/io/IOException:printStackTrace	()V
    //   157: aload_3
    //   158: invokevirtual 181	java/io/BufferedReader:close	()V
    //   161: iconst_0
    //   162: ireturn
    //   163: astore 11
    //   165: aload 11
    //   167: invokevirtual 178	java/io/IOException:printStackTrace	()V
    //   170: iconst_0
    //   171: ireturn
    //   172: aload_3
    //   173: invokevirtual 181	java/io/BufferedReader:close	()V
    //   176: aload_0
    //   177: getfield 51	net/cloudpath/xpressconnect/screens/delegates/EntryScreenDelegate:mGlobals	Lnet/cloudpath/xpressconnect/localservice/GetGlobals;
    //   180: aload_2
    //   181: invokevirtual 182	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   184: invokevirtual 187	net/cloudpath/xpressconnect/localservice/GetGlobals:setXmlConfig	(Ljava/lang/String;)V
    //   187: new 150	java/io/File
    //   190: dup
    //   191: aload_1
    //   192: invokespecial 152	java/io/File:<init>	(Ljava/lang/String;)V
    //   195: invokevirtual 190	java/io/File:delete	()Z
    //   198: ifeq +56 -> 254
    //   201: aload_0
    //   202: getfield 39	net/cloudpath/xpressconnect/screens/delegates/EntryScreenDelegate:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   205: new 88	java/lang/StringBuilder
    //   208: dup
    //   209: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   212: ldc 192
    //   214: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   217: aload_1
    //   218: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   221: invokevirtual 99	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   224: invokestatic 70	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   227: aload_0
    //   228: getfield 37	net/cloudpath/xpressconnect/screens/delegates/EntryScreenDelegate:mActivity	Landroid/app/Activity;
    //   231: new 194	net/cloudpath/xpressconnect/screens/delegates/EntryScreenDelegate$1
    //   234: dup
    //   235: aload_0
    //   236: invokespecial 195	net/cloudpath/xpressconnect/screens/delegates/EntryScreenDelegate$1:<init>	(Lnet/cloudpath/xpressconnect/screens/delegates/EntryScreenDelegate;)V
    //   239: invokevirtual 129	android/app/Activity:runOnUiThread	(Ljava/lang/Runnable;)V
    //   242: iconst_1
    //   243: ireturn
    //   244: astore 8
    //   246: aload 8
    //   248: invokevirtual 178	java/io/IOException:printStackTrace	()V
    //   251: goto -75 -> 176
    //   254: aload_0
    //   255: getfield 39	net/cloudpath/xpressconnect/screens/delegates/EntryScreenDelegate:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   258: new 88	java/lang/StringBuilder
    //   261: dup
    //   262: invokespecial 90	java/lang/StringBuilder:<init>	()V
    //   265: ldc 197
    //   267: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   270: aload_1
    //   271: invokevirtual 96	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   274: invokevirtual 99	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   277: invokestatic 70	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   280: goto -53 -> 227
    //
    // Exception table:
    //   from	to	target	type
    //   50	66	119	java/io/FileNotFoundException
    //   66	72	128	java/io/IOException
    //   135	139	141	java/io/IOException
    //   106	112	150	java/io/IOException
    //   157	161	163	java/io/IOException
    //   172	176	244	java/io/IOException
  }

  public boolean loadConfigFromUrl(String paramString)
  {
    GetConfigFromUrl localGetConfigFromUrl = new GetConfigFromUrl(this.mLogger);
    if (localGetConfigFromUrl.findByUrl(paramString, VERBOSE_LOGGING))
    {
      this.mGlobals.setXmlConfig(localGetConfigFromUrl.getPageResult());
      return true;
    }
    return false;
  }

  public void logVersion()
  {
    try
    {
      PackageInfo localPackageInfo = this.mActivity.getPackageManager().getPackageInfo(this.mActivity.getPackageName(), 0);
      Util.log(this.mLogger, "I am " + localPackageInfo.packageName + " version " + localPackageInfo.versionName);
      if (!AndroidVersion.getVersionMap().containsValue(Integer.valueOf(Build.VERSION.SDK_INT)))
        Util.log(this.mLogger, "*******  Android version " + Build.VERSION.SDK_INT + " is not in our list of known versions!   We can't be sure everything will work!");
      return;
    }
    catch (Exception localException)
    {
      Util.log(this.mLogger, "Exception : " + localException.getMessage());
      Util.log(this.mLogger, "Unable to determine my version information.");
    }
  }

  public void run()
  {
    Util.log(this.mLogger, "Intent data passed in : " + this.mIntentData);
    if (this.mTargetUrl != null)
    {
      if (tryTargetUrlLaunch())
      {
        Util.log(this.mLogger, "---- Launching by URL.");
        gotoFlowControlOnUi();
      }
    }
    else
      Util.log(this.mLogger, "No target URL passed in through Intent data.  Will attempt other methods of locating the configuration.");
    switch (tryConfigByDnsHints())
    {
    case 2:
    default:
      if (tryConfigByNetConfig())
      {
        Util.log(this.mLogger, "---- Launching by NetConfig.");
        gotoFlowControlOnUi();
        return;
      }
      break;
    case 1:
      Util.log(this.mLogger, "---- Launching by DNS hints.");
      gotoFlowControlOnUi();
      return;
    }
    if (tryConfigByCachedConfig())
    {
      Util.log(this.mLogger, "---- Launching by cached config.");
      gotoFlowControlOnUi();
      return;
    }
    Util.log(this.mLogger, "---- Couldn't detect a config.");
    GotoScreen.pickService(this.mActivity);
  }

  public void setAsLibrary(boolean paramBoolean)
  {
    this.mRunningAsLibrary = paramBoolean;
  }

  public void setPassword(String paramString)
  {
    this.mPassword = paramString;
  }

  public void setRequiredValue(GetGlobals paramGetGlobals, Logger paramLogger)
  {
    this.mGlobals = paramGetGlobals;
    this.mLogger = paramLogger;
  }

  public void setTargetUrl(String paramString)
  {
    if (paramString == null)
      return;
    if (paramString.contains("?"))
    {
      String[] arrayOfString1 = paramString.split("\\?");
      if (arrayOfString1.length < 2)
      {
        Util.log(this.mLogger, "URL appeared to contain extras, but splitting didn't return what was expected.");
        this.mTargetUrl = paramString;
        return;
      }
      this.mTargetUrl = arrayOfString1[0];
      String[] arrayOfString2 = arrayOfString1[1].split("&");
      int i = 0;
      label65: String[] arrayOfString3;
      if (i < arrayOfString2.length)
      {
        arrayOfString3 = arrayOfString2[i].split("=");
        if (arrayOfString3.length <= 2)
          break label107;
        Util.log(this.mLogger, "URL parameter contained too much information!?  (Ignoring)");
      }
      while (true)
      {
        i++;
        break label65;
        break;
        label107: if ((arrayOfString3[0].contentEquals("username")) || (arrayOfString3[0].contentEquals("uname")))
          setUsername(arrayOfString3[1]);
        else if ((arrayOfString3[0].contentEquals("password")) || (arrayOfString3[0].contentEquals("pwd")))
          setPassword(arrayOfString3[1]);
      }
    }
    this.mTargetUrl = paramString;
  }

  public void setUsername(String paramString)
  {
    this.mUsername = paramString;
  }

  public void threadChangeTab(int paramInt)
  {
  }

  public boolean tryConfigByCachedConfig()
  {
    LoadConfigFromDb localLoadConfigFromDb = new LoadConfigFromDb(this.mLogger, this.mGlobals, this.mContext);
    if ((!localLoadConfigFromDb.loadFromDb(this.mTargetUrl, null)) && (!localLoadConfigFromDb.loadOnlyConfig()))
      return false;
    this.mActivity.runOnUiThread(new Runnable()
    {
      public void run()
      {
        Toast.makeText(EntryScreenDelegate.this.mActivity, "Loading the configuration from a cached copy.", 0).show();
      }
    });
    return true;
  }

  public int tryConfigByDnsHints()
  {
    FindByDnsSearchDomain localFindByDnsSearchDomain = new FindByDnsSearchDomain(this.mLogger);
    FindByDnsName localFindByDnsName = new FindByDnsName(this.mLogger, this.mActivity);
    Util.log(this.mLogger, "Attempting to locate the configuration via DNS hints...");
    if (localFindByDnsName.findByDnsName(VERBOSE_LOGGING))
    {
      if (localFindByDnsName.mIsXpcEs)
      {
        if (!Util.stringIsEmpty(localFindByDnsName.getRawUrl()))
        {
          Util.log(this.mLogger, "URL provided is an ES URL.  Going to mini-browser.");
          GotoScreen.browserScreen(this.mActivity, localFindByDnsName.getRawUrl());
          return 2;
        }
        Util.log(this.mLogger, "Hint provided by DNS wasn't in the correct format or was invalid in other ways.");
        return 0;
      }
      if (localFindByDnsName.findByUrl(localFindByDnsName.mTargetUrl, VERBOSE_LOGGING))
      {
        Util.log(this.mLogger, "Found URL via TXT record : " + localFindByDnsName.mTargetUrl);
        this.mGlobals.setXmlConfig(localFindByDnsName.getPageResult());
        return 1;
      }
      Util.log(this.mLogger, "Hint provided by TXT record in the wrong format or was invalid in other ways.");
    }
    Util.log(this.mLogger, "Attempting to find configuration by DNS search domain information...");
    if ((localFindByDnsSearchDomain.findByDnsSearchDomain(VERBOSE_LOGGING)) && (localFindByDnsSearchDomain.mTargetUrl != null))
    {
      Util.log(this.mLogger, "Found URL via DNS Search domain : " + localFindByDnsSearchDomain.mTargetUrl);
      this.mGlobals.setXmlConfig(localFindByDnsSearchDomain.getPageResult());
      return 1;
    }
    return validDataReturned();
  }

  public boolean tryConfigByNetConfig()
  {
    if (!Util.isStorageReady(this.mLogger, null));
    do
    {
      return false;
      if (loadConfigFromFile(Environment.getExternalStorageDirectory() + "/Download/android.netconfig"))
        return true;
      if (loadConfigFromFile(Environment.getExternalStorageDirectory() + "/Downloads/android.netconfig"))
        return true;
    }
    while (!loadConfigFromFile(Environment.getExternalStorageDirectory() + "/android.netconfig"));
    return true;
  }

  public boolean tryTargetUrlLaunch()
  {
    String str = validatedUrl();
    if (str == null)
    {
      Util.log(this.mLogger, "Failed to validate the Intent provided URL : " + str);
      return false;
    }
    Util.log(this.mLogger, "network_config_android.xml URL : " + str);
    if (!loadConfigFromUrl(str))
    {
      Util.log(this.mLogger, "Unable to load configuration from URL.  Moving on.");
      return false;
    }
    this.mGlobals.setLoadedFromUrl(str);
    return true;
  }

  public int validDataReturned()
  {
    WebInterface localWebInterface = new WebInterface(this.mLogger);
    Util.log(this.mLogger, "Doing portal check to try to locate the configuration...");
    String str1 = localWebInterface.getWebPage("http://dot1x.com/portalcheck.html", false);
    if (localWebInterface.resultCode != 200)
    {
      Util.log(this.mLogger, "Got a result code of " + localWebInterface.resultCode + " we are either not behind a portal, or on a 'dead end' network.");
      return 0;
    }
    if (Util.stringIsNotEmpty(str1))
      Util.log(this.mLogger, "Result string was not empty.");
    URL localURL;
    String[] arrayOfString1;
    try
    {
      localURL = new URL(localWebInterface.finalUrl);
      arrayOfString1 = localWebInterface.finalUrl.split("\\?");
      Util.log(this.mLogger, "Attempting to load the portal URL with the configuration file attached...");
      if (arrayOfString1[0].endsWith("/"))
      {
        if (!loadConfigFromUrl(arrayOfString1[0] + "network_config_android.xml"))
          break label247;
        return 1;
      }
    }
    catch (MalformedURLException localMalformedURLException)
    {
      Util.log(this.mLogger, "Invalid URL in finalUrl.");
      localMalformedURLException.printStackTrace();
      Util.log(this.mLogger, "URL wasn't what we were looking for.  Will go to the 'mini-browser' screen...");
      GotoScreen.browserScreen(this.mActivity, localWebInterface.finalUrl);
      return 2;
    }
    if (loadConfigFromUrl(arrayOfString1[0] + "/network_config_android.xml"))
      return 1;
    label247: Util.log(this.mLogger, "... Failed to load the config ...");
    Util.log(this.mLogger, "Attempting to load the URL with a reconstructed URL...");
    String str2 = localURL.getProtocol() + "://" + localURL.getHost();
    if (Util.stringIsEmpty(localURL.getPath()))
      str2 = str2 + "/network_config_android.xml";
    while (loadConfigFromUrl(str2 + "/network_config_android.xml"))
    {
      return 1;
      String[] arrayOfString2 = localURL.getPath().split("/");
      if (arrayOfString2.length > 0)
      {
        String str3 = "";
        for (int i = 0; i < -1 + arrayOfString2.length; i++)
          if (arrayOfString2[i] != null)
            str3 = str3 + "/" + arrayOfString2[i];
        if (str3 != null)
          str2 = str2 + str3;
      }
    }
    Util.log(this.mLogger, "... Failed to load the config ...");
    Util.log(this.mLogger, "Checking to see if a web page exists that we can direct the 'mini-browser' to...");
    localWebInterface.getWebPage(str2, false);
    if (localWebInterface.resultCode == 200)
    {
      GotoScreen.browserScreen(this.mActivity, str2);
      return 2;
    }
    Util.log(this.mLogger, "No web page to send to the 'mini-browser'...");
    return 0;
  }

  public String validatedUrl()
  {
    return validatedUrl(this.mTargetUrl);
  }

  public String validatedUrl(String paramString)
  {
    if (paramString == null)
      return null;
    Util.log(this.mLogger, "Initial input URL : " + paramString);
    if (paramString.endsWith("/android.netconfig"))
    {
      int i = "/android.netconfig".length();
      paramString = paramString.substring(0, paramString.length() - i);
      Util.log(this.mLogger, "Stripped URL : " + paramString);
    }
    try
    {
      localURL1 = new URL(paramString);
    }
    catch (MalformedURLException localMalformedURLException3)
    {
      try
      {
        if (!paramString.endsWith("/network_config_android.xml"))
        {
          localURL2 = new URL(paramString + "/network_config_android.xml");
          while (true)
          {
            Util.log(this.mLogger, "Returning mapped URL of : " + localURL2.toString());
            return localURL2.toString();
            localMalformedURLException3 = localMalformedURLException3;
            localURL1 = null;
            String str;
            if ((!paramString.startsWith("http")) && (!paramString.startsWith("https")))
            {
              str = "http://" + paramString;
              if (!paramString.endsWith("/network_config_android.xml"))
                str = str + "/network_config_android.xml";
            }
            try
            {
              localURL2 = new URL(str);
              continue;
              str = paramString;
            }
            catch (MalformedURLException localMalformedURLException2)
            {
              localMalformedURLException2.printStackTrace();
              Util.log(this.mLogger, "URL is in a strange format : " + str);
              return str;
            }
          }
        }
      }
      catch (MalformedURLException localMalformedURLException1)
      {
        while (true)
        {
          URL localURL1;
          continue;
          URL localURL2 = localURL1;
        }
      }
    }
  }

  public boolean versionIsValid(AndroidVersion paramAndroidVersion)
  {
    return paramAndroidVersion.isValidVersion();
  }
}