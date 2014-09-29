package net.cloudpath.xpressconnect;

import android.os.Build;
import android.os.Build.VERSION;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.nativeproxy.WifiConfigurationProxy;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.ProfileElement;
import net.cloudpath.xpressconnect.parsers.config.SettingElement;

public class AndroidVersion
{
  private Logger mLogger = null;

  public AndroidVersion(Logger paramLogger)
  {
    this.mLogger = paramLogger;
  }

  public static Map<String, Integer> getVersionMap()
  {
    HashMap localHashMap = new HashMap();
    localHashMap.put("1.0", Integer.valueOf(1));
    localHashMap.put("1.1", Integer.valueOf(2));
    localHashMap.put("1.5", Integer.valueOf(3));
    localHashMap.put("1.6", Integer.valueOf(4));
    localHashMap.put("2.0", Integer.valueOf(5));
    localHashMap.put("2.0.1", Integer.valueOf(6));
    localHashMap.put("2.1", Integer.valueOf(7));
    localHashMap.put("2.2", Integer.valueOf(8));
    localHashMap.put("2.3", Integer.valueOf(9));
    localHashMap.put("2.3.3+", Integer.valueOf(10));
    localHashMap.put("3.0", Integer.valueOf(11));
    localHashMap.put("3.1", Integer.valueOf(12));
    localHashMap.put("3.2", Integer.valueOf(13));
    localHashMap.put("4.0", Integer.valueOf(14));
    localHashMap.put("4.0.3+", Integer.valueOf(15));
    localHashMap.put("4.1", Integer.valueOf(16));
    localHashMap.put("4.2", Integer.valueOf(17));
    localHashMap.put("4.3", Integer.valueOf(18));
    localHashMap.put("4.4", Integer.valueOf(19));
    localHashMap.put("4.5", Integer.valueOf(20));
    return localHashMap;
  }

  public boolean allowCertlessDevices(NetworkConfigParser paramNetworkConfigParser)
  {
    if ((paramNetworkConfigParser == null) || (paramNetworkConfigParser.selectedProfile == null))
      Util.log(this.mLogger, "No parser or profile selected.  Returning false in allowCertlessDevices()!");
    SettingElement localSettingElement;
    do
    {
      return false;
      localSettingElement = paramNetworkConfigParser.selectedProfile.getSetting(0, 61307);
    }
    while ((localSettingElement == null) || (localSettingElement.requiredValue == null) || (Util.parseInt(localSettingElement.requiredValue, 0) != 1));
    return true;
  }

  public boolean cantUseCertificates()
  {
    return cantUseCertificates(Build.BRAND, Build.MANUFACTURER, Build.VERSION.SDK_INT);
  }

  public boolean cantUseCertificates(String paramString1, String paramString2, int paramInt)
  {
    if (paramString1 == null);
    do
    {
      return false;
      if (paramString1.toLowerCase(Locale.ENGLISH).contentEquals("nook"))
        return true;
    }
    while ((!paramString2.toLowerCase(Locale.ENGLISH).contentEquals("htc")) || (paramInt > 7));
    return true;
  }

  public boolean forceOpenKeystore(NetworkConfigParser paramNetworkConfigParser)
  {
    return forceOpenKeystore(paramNetworkConfigParser, Build.VERSION.SDK_INT, Build.MANUFACTURER);
  }

  public boolean forceOpenKeystore(NetworkConfigParser paramNetworkConfigParser, int paramInt, String paramString)
  {
    if (paramInt >= 14);
    do
    {
      return false;
      if ((paramNetworkConfigParser != null) && (paramNetworkConfigParser.savedConfigInfo != null) && (paramNetworkConfigParser.savedConfigInfo.forceOpenKeystore))
      {
        Util.log(this.mLogger, "Returning that keystore needs to be opened as a result of a log detection.");
        return true;
      }
    }
    while ((!paramString.toLowerCase(Locale.ENGLISH).contentEquals("htc")) || (paramInt < 8));
    Util.log(this.mLogger, "Running on an HTC device with SDK 8 or higher.  Need keystore workaround.");
    return true;
  }

  public boolean forceUsingPkcs12()
  {
    try
    {
      WifiConfigurationProxy localWifiConfigurationProxy = new WifiConfigurationProxy(null, null);
      if (localWifiConfigurationProxy.getApiLevel() >= 2)
        return true;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      Util.log(this.mLogger, "IllegalArgumentException in forceUsingPkcs12()");
      localIllegalArgumentException.printStackTrace();
      return false;
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      Util.log(this.mLogger, "NoSuchFieldException in forceUsingPkcs12()");
      localNoSuchFieldException.printStackTrace();
      return false;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      Util.log(this.mLogger, "ClassNotFoundException in forceUsingPkcs12()");
      localClassNotFoundException.printStackTrace();
      return false;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      Util.log(this.mLogger, "IllegalAccessException in forceUsingPkcs12()");
      localIllegalAccessException.printStackTrace();
      return false;
    }
    return false;
  }

  public String fullVersion()
  {
    return Build.VERSION.RELEASE;
  }

  public float getBaseAndroidVerAsFloat()
  {
    return Float.parseFloat(getBaseAndroidVerAsString());
  }

  public String getBaseAndroidVerAsString()
  {
    return parseAndroidVerString(Build.VERSION.RELEASE);
  }

  public boolean isValidVersion()
  {
    return isValidVersion(Build.VERSION.SDK_INT);
  }

  public boolean isValidVersion(int paramInt)
  {
    return paramInt >= 7;
  }

  public boolean noCertHint()
  {
    return Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).contentEquals("htc");
  }

  public CharSequence parseAndroidPatchLevel(String paramString)
  {
    Matcher localMatcher = Pattern.compile("\\d+\\.\\d+-(.*)").matcher(paramString);
    String str = null;
    if (localMatcher == null)
      return "0";
    while (localMatcher.find())
      if (localMatcher.groupCount() >= 1)
        str = localMatcher.group(1);
    if (str == null)
      return "0";
    return str;
  }

  public String parseAndroidVerString(String paramString)
  {
    Matcher localMatcher = Pattern.compile("(\\d+\\.\\d+)").matcher(paramString);
    String str = null;
    if (localMatcher == null)
      return "";
    while (localMatcher.find())
      if (localMatcher.groupCount() >= 1)
        str = localMatcher.group(1);
    if (str == null)
      return "";
    return str;
  }

  public CharSequence patchLevel()
  {
    return parseAndroidPatchLevel(Build.VERSION.RELEASE);
  }

  public boolean shouldForceCertInstallPassword(NetworkConfigParser paramNetworkConfigParser, Logger paramLogger)
  {
    if (shouldForceCertInstallPasswordByDevice())
      return true;
    return Util.getBooleanSetting(paramNetworkConfigParser, paramLogger, 0, 80038, false);
  }

  public boolean shouldForceCertInstallPasswordByDevice()
  {
    if (Build.BRAND.toLowerCase(Locale.ENGLISH).contentEquals("lge"));
    while (Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).contentEquals("htc"))
      return true;
    return false;
  }
}