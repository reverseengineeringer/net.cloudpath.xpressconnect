package net.cloudpath.xpressconnect;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.ProfileElement;
import net.cloudpath.xpressconnect.parsers.config.SettingElement;

public class Util
{
  public static final String DATE_FORMAT_NOW = "HH:mm:ss";

  public static String cmdExec(String paramString)
  {
    String str1 = "";
    try
    {
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(paramString).getInputStream()));
      while (true)
      {
        String str2 = localBufferedReader.readLine();
        if (str2 == null)
          break;
        str1 = str1 + str2 + '\n';
      }
      localBufferedReader.close();
      return str1;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return str1;
  }

  public static String dumpByteValues(String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    for (int i = 0; i < paramString.length(); i++)
    {
      int j = paramString.charAt(i);
      localStringBuilder.append("(" + j + ") " + paramString.charAt(i) + " ");
    }
    return localStringBuilder.toString();
  }

  @SuppressLint({"DefaultLocale"})
  public static boolean getBoolFromString(String paramString)
  {
    if (paramString == null);
    while (true)
    {
      return false;
      try
      {
        if (!paramString.toUpperCase().contentEquals("TRUE"))
        {
          int i = Integer.parseInt(paramString);
          if (i != 1);
        }
        else
        {
          return true;
        }
      }
      catch (NumberFormatException localNumberFormatException)
      {
      }
    }
    return false;
  }

  public static boolean getBooleanSetting(NetworkConfigParser paramNetworkConfigParser, Logger paramLogger, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if ((paramNetworkConfigParser == null) || (paramNetworkConfigParser.selectedProfile == null))
    {
      log(paramLogger, "Parser data was invalid in getBooleanSetting()!");
      return false;
    }
    return getBooleanSetting(paramNetworkConfigParser.selectedProfile, paramLogger, paramInt1, paramInt2, paramBoolean);
  }

  public static boolean getBooleanSetting(ProfileElement paramProfileElement, Logger paramLogger, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (paramProfileElement == null)
    {
      log(paramLogger, "Profile data was invalid in getBooleanSetting()!");
      paramBoolean = false;
    }
    SettingElement localSettingElement;
    do
    {
      return paramBoolean;
      localSettingElement = paramProfileElement.getSetting(paramInt1, paramInt2);
    }
    while (localSettingElement == null);
    return localSettingElement.requiredValue.contentEquals("1");
  }

  public static String getMac(Logger paramLogger, WifiManager paramWifiManager)
  {
    if (paramWifiManager == null)
      return new String("");
    if (paramWifiManager.getConnectionInfo() == null)
    {
      log(paramLogger, "getConnectionInfo is null.");
      return new String("");
    }
    return paramWifiManager.getConnectionInfo().getMacAddress();
  }

  public static String getSSID(Logger paramLogger, NetworkConfigParser paramNetworkConfigParser)
  {
    if (paramNetworkConfigParser == null)
    {
      log(paramLogger, "Parser isn't bound.  Can't get SSID.");
      return null;
    }
    if (paramNetworkConfigParser.selectedProfile == null)
    {
      log(paramLogger, "No profile selected.  Can't get SSID.");
      return null;
    }
    return getSSID(paramLogger, paramNetworkConfigParser.selectedProfile);
  }

  public static String getSSID(Logger paramLogger, ProfileElement paramProfileElement)
  {
    if (paramProfileElement == null)
    {
      log(paramLogger, "Invalid profile passed in to getSSID()!");
      return null;
    }
    SettingElement localSettingElement = paramProfileElement.getSetting(1, 40001);
    if (localSettingElement == null)
    {
      log(paramLogger, "No SSID information available!");
      return null;
    }
    if (localSettingElement.additionalValue == null)
    {
      log(paramLogger, "SSID element additional value is not defined in getSSID()!");
      return null;
    }
    String[] arrayOfString = localSettingElement.additionalValue.split(":");
    if (arrayOfString.length != 3)
    {
      log(paramLogger, "The SSID element provided in the configuration file is invalid!");
      return null;
    }
    if (!arrayOfString[0].equals("CP"))
    {
      log(paramLogger, "Invalid signature on the SSID string!");
      return null;
    }
    if (stringIsEmpty(arrayOfString[2]))
    {
      log(paramLogger, "There isn't enough information about the SSID to build the profile.");
      return null;
    }
    return arrayOfString[2];
  }

  public static String getStackTrace(Throwable paramThrowable)
  {
    StringWriter localStringWriter = new StringWriter();
    paramThrowable.printStackTrace(new PrintWriter(localStringWriter));
    return localStringWriter.toString();
  }

  public static String intToIp(int paramInt)
  {
    return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "." + (0xFF & paramInt >> 24);
  }

  public static boolean isDeviceRootedOrCustom(Logger paramLogger)
  {
    String str = Build.TAGS;
    if ((str != null) && (str.contains("test-keys")))
    {
      log(paramLogger, "Device is running a ROM signed with test keys.  ROM appears to be custom.");
      return true;
    }
    try
    {
      if (new File("/system/app/Superuser.apk").exists())
      {
        log(paramLogger, "Device appears to have a superuser APK installed.  Device is rooted.");
        return true;
      }
    }
    catch (Throwable localThrowable)
    {
      log(paramLogger, "Device appears to have a stock ROM.");
    }
    return false;
  }

  public static boolean isOpenNetwork(Logger paramLogger, NetworkConfigParser paramNetworkConfigParser)
  {
    if (paramNetworkConfigParser == null)
    {
      log(paramLogger, "No parser bound in isOpenNetwork()!");
      return false;
    }
    if (paramNetworkConfigParser.selectedProfile == null)
    {
      log(paramLogger, "No profile selected in isOpenNetwork()!");
      return false;
    }
    return isOpenNetwork(paramLogger, paramNetworkConfigParser.selectedProfile);
  }

  public static boolean isOpenNetwork(Logger paramLogger, ProfileElement paramProfileElement)
  {
    if (paramProfileElement == null)
      log(paramLogger, "Invalid profile specified in isOpenNetwork()!");
    String str;
    do
    {
      return false;
      SettingElement localSettingElement = paramProfileElement.getSetting(1, 40001);
      if (localSettingElement == null)
      {
        log(paramLogger, "Couldn't get SSID info in installCertificate!?");
        return false;
      }
      if (localSettingElement.additionalValue == null)
      {
        log(paramLogger, "No SSID defined in setEssid()!?");
        return false;
      }
      String[] arrayOfString = localSettingElement.additionalValue.split(":");
      if (arrayOfString.length != 3)
      {
        log(paramLogger, "The SSID element provided in the configuration file is invalid!");
        return false;
      }
      if (!arrayOfString[0].equals("CP"))
      {
        log(paramLogger, "Invalid signature on the SSID string!");
        return false;
      }
      str = arrayOfString[1];
    }
    while ((parseInt(String.valueOf(str.charAt(7)), 0) != 1) && (parseInt(String.valueOf(str.charAt(7)), 0) != 0));
    return true;
  }

  public static boolean isStorageReady(String paramString, Logger paramLogger, FailureReason paramFailureReason)
  {
    if ("mounted".equals(paramString))
    {
      log(paramLogger, "The external storage is ready.");
      return true;
    }
    log(paramLogger, "!!!!! External storage is not ready.  State : " + paramString);
    if (paramFailureReason != null)
      paramFailureReason.setFailReason(FailureReason.useErrorIcon, "Unable to install certificate(s) for this network.   The SD card on your device isn't available.  It's current state is : " + mediaStateToHumanReadable(paramString), 2);
    return false;
  }

  public static boolean isStorageReady(Logger paramLogger, FailureReason paramFailureReason)
  {
    return isStorageReady(Environment.getExternalStorageState(), paramLogger, paramFailureReason);
  }

  @SuppressLint({"DefaultLocale"})
  public static boolean isTls(Logger paramLogger, NetworkConfigParser paramNetworkConfigParser, Context paramContext, FailureReason paramFailureReason)
  {
    if ((paramNetworkConfigParser == null) || (paramContext == null))
      log(paramLogger, "No parser or context defined in isTls()!");
    SettingElement localSettingElement;
    do
    {
      return false;
      if (paramNetworkConfigParser.selectedProfile == null)
      {
        log(paramLogger, "No profile selected in isTls()!");
        return false;
      }
      localSettingElement = paramNetworkConfigParser.selectedProfile.getSetting(2, 65001);
      if ((localSettingElement != null) && (localSettingElement.requiredValue != null))
        break;
      log(paramLogger, "No outer EAP method was defined in the configuration file!");
    }
    while (paramFailureReason == null);
    ParcelHelper localParcelHelper = new ParcelHelper("", paramContext);
    paramFailureReason.setFailReason(FailureReason.useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_no_eap_method_defined", "string")), 5);
    return false;
    return localSettingElement.requiredValue.toLowerCase().equals("tls");
  }

  public static void log(Logger paramLogger, String paramString)
  {
    if (paramLogger == null)
    {
      Log.d("XPC", "Logger is null.  Can't log a message. (Message : " + paramString + ")");
      return;
    }
    paramLogger.addLine("[" + now() + "] " + paramString);
  }

  public static String mediaStateToHumanReadable(String paramString)
  {
    if ("bad_removal".equals(paramString))
      return "Media was removed before it was unmounted.";
    if ("checking".equals(paramString))
      return "Media is still being checked.";
    if ("mounted_ro".equals(paramString))
      return "Media is read only.";
    if ("nofs".equals(paramString))
      return "The media is not formatted.";
    if ("removed".equals(paramString))
      return "Media is not present.";
    if ("shared".equals(paramString))
      return "Device is currently in USB mass storage mode.";
    if ("unmountable".equals(paramString))
      return "Media is unmountable.";
    if ("unmounted".equals(paramString))
      return "Media is not mounted.";
    return "Media is in an unknown state.";
  }

  @SuppressLint({"SimpleDateFormat"})
  public static String now()
  {
    try
    {
      Calendar localCalendar = Calendar.getInstance();
      String str = new SimpleDateFormat("HH:mm:ss").format(localCalendar.getTime());
      return str;
    }
    catch (Exception localException)
    {
    }
    return "Unknown";
  }

  public static int parseInt(String paramString, int paramInt)
  {
    try
    {
      int i = Integer.parseInt(paramString);
      return i;
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    return paramInt;
  }

  public static String readFile(String paramString)
  {
    try
    {
      FileInputStream localFileInputStream = new FileInputStream(paramString);
      DataInputStream localDataInputStream = new DataInputStream(localFileInputStream);
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localDataInputStream));
      StringBuilder localStringBuilder = new StringBuilder();
      while (true)
      {
        String str1 = localBufferedReader.readLine();
        if (str1 == null)
          break;
        localStringBuilder.append(str1 + "\n");
      }
      localBufferedReader.close();
      localDataInputStream.close();
      localFileInputStream.close();
      String str2 = localStringBuilder.toString();
      return str2;
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  public static boolean stringIsEmpty(String paramString)
  {
    return (paramString == null) || (paramString.contentEquals("")) || (paramString.length() <= 0) || (paramString.contentEquals("\n"));
  }

  public static boolean stringIsNotEmpty(String paramString)
  {
    return !stringIsEmpty(paramString);
  }

  public static boolean usingCaValidation(Logger paramLogger, NetworkConfigParser paramNetworkConfigParser)
  {
    return usingCaValidation(paramNetworkConfigParser, paramLogger);
  }

  public static boolean usingCaValidation(Logger paramLogger, ProfileElement paramProfileElement)
  {
    if (paramProfileElement == null)
    {
      log(paramLogger, "Profile element is invalid in usingCaValidation()!");
      return false;
    }
    return getBooleanSetting(paramProfileElement, paramLogger, 2, 65000, false);
  }

  public static boolean usingCaValidation(NetworkConfigParser paramNetworkConfigParser, Logger paramLogger)
  {
    if ((paramNetworkConfigParser == null) || (paramNetworkConfigParser.selectedProfile == null))
    {
      log(paramLogger, "Parser data was invalid in usingCaValidation()!");
      return false;
    }
    return usingCaValidation(paramLogger, paramNetworkConfigParser.selectedProfile);
  }

  public static boolean usingPsk(Logger paramLogger, NetworkConfigParser paramNetworkConfigParser)
  {
    if (paramNetworkConfigParser == null)
    {
      log(paramLogger, "Parser isn't bound.  Assuming we aren't using PSK.");
      return false;
    }
    if (paramNetworkConfigParser.selectedProfile == null)
    {
      log(paramLogger, "No profile selected.  Assuming we aren't using PSK.");
      return false;
    }
    SettingElement localSettingElement = paramNetworkConfigParser.selectedProfile.getSetting(1, 40001);
    if (localSettingElement == null)
    {
      log(paramLogger, "PSK is configured, but no PSK is in our configuration!");
      return false;
    }
    if (localSettingElement.additionalValue == null)
    {
      log(paramLogger, "SSID element additional value is not defined in usingPSK()!");
      return false;
    }
    String[] arrayOfString = localSettingElement.additionalValue.split(":");
    if (arrayOfString.length != 3)
    {
      log(paramLogger, "The SSID element provided in the configuration file is invalid!");
      return false;
    }
    if (!arrayOfString[0].equals("CP"))
    {
      log(paramLogger, "Invalid signature on the SSID string!");
      return false;
    }
    if (stringIsEmpty(arrayOfString[2]))
    {
      log(paramLogger, "There isn't enough information about the SSID to build the profile.");
      return false;
    }
    log(paramLogger, "Using configuration settings : " + arrayOfString[1]);
    switch (parseInt(String.valueOf(arrayOfString[1].charAt(7)), 0))
    {
    case 5:
    case 6:
    default:
      log(paramLogger, "Network is not PSK.");
      return false;
    case 4:
    case 7:
    case 8:
    }
    return true;
  }

  // ERROR //
  public static boolean writeFile(Logger paramLogger, String paramString1, String paramString2)
  {
    // Byte code:
    //   0: new 463	java/io/OutputStreamWriter
    //   3: dup
    //   4: new 465	java/io/FileOutputStream
    //   7: dup
    //   8: aload_1
    //   9: invokespecial 466	java/io/FileOutputStream:<init>	(Ljava/lang/String;)V
    //   12: ldc_w 468
    //   15: invokespecial 471	java/io/OutputStreamWriter:<init>	(Ljava/io/OutputStream;Ljava/lang/String;)V
    //   18: astore_3
    //   19: aload_3
    //   20: aload_2
    //   21: invokevirtual 476	java/io/Writer:write	(Ljava/lang/String;)V
    //   24: aload_3
    //   25: invokevirtual 477	java/io/Writer:close	()V
    //   28: iconst_1
    //   29: ireturn
    //   30: astore 10
    //   32: aload 10
    //   34: invokevirtual 478	java/io/UnsupportedEncodingException:printStackTrace	()V
    //   37: aload_0
    //   38: ldc_w 480
    //   41: invokestatic 123	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   44: iconst_0
    //   45: ireturn
    //   46: astore 9
    //   48: aload 9
    //   50: invokevirtual 481	java/io/FileNotFoundException:printStackTrace	()V
    //   53: aload_0
    //   54: ldc_w 483
    //   57: invokestatic 123	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   60: iconst_0
    //   61: ireturn
    //   62: astore 8
    //   64: aload 8
    //   66: invokevirtual 484	java/io/IOException:printStackTrace	()V
    //   69: aload_0
    //   70: ldc_w 486
    //   73: invokestatic 123	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   76: iconst_0
    //   77: ireturn
    //   78: astore 6
    //   80: aload 6
    //   82: invokevirtual 484	java/io/IOException:printStackTrace	()V
    //   85: aload_0
    //   86: ldc_w 488
    //   89: invokestatic 123	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   92: aload_3
    //   93: invokevirtual 477	java/io/Writer:close	()V
    //   96: iconst_0
    //   97: ireturn
    //   98: astore 7
    //   100: aload 7
    //   102: invokevirtual 484	java/io/IOException:printStackTrace	()V
    //   105: aload_0
    //   106: ldc_w 486
    //   109: invokestatic 123	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   112: iconst_0
    //   113: ireturn
    //   114: astore 4
    //   116: aload_3
    //   117: invokevirtual 477	java/io/Writer:close	()V
    //   120: aload 4
    //   122: athrow
    //   123: astore 5
    //   125: aload 5
    //   127: invokevirtual 484	java/io/IOException:printStackTrace	()V
    //   130: aload_0
    //   131: ldc_w 486
    //   134: invokestatic 123	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   137: iconst_0
    //   138: ireturn
    //
    // Exception table:
    //   from	to	target	type
    //   0	19	30	java/io/UnsupportedEncodingException
    //   0	19	46	java/io/FileNotFoundException
    //   24	28	62	java/io/IOException
    //   19	24	78	java/io/IOException
    //   92	96	98	java/io/IOException
    //   19	24	114	finally
    //   80	92	114	finally
    //   116	120	123	java/io/IOException
  }
}