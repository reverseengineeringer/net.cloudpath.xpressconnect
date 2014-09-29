package net.cloudpath.xpressconnect;

import android.net.wifi.WifiManager;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;

public class MapVariables
{
  private AndroidVersion av = null;
  private Logger mLogger = null;
  private NetworkConfigParser mParser = null;

  public MapVariables(NetworkConfigParser paramNetworkConfigParser, Logger paramLogger)
    throws Exception
  {
    this.mParser = paramNetworkConfigParser;
    if (this.mParser == null)
      throw new Exception("No parser was defined!");
    this.mLogger = paramLogger;
    if (this.mLogger == null)
      throw new Exception("No logger was defined!");
    this.av = new AndroidVersion(this.mLogger);
  }

  protected String getSettingValue(int paramInt, WifiManager paramWifiManager)
  {
    ReadSetting localReadSetting = new ReadSetting(this.mParser, this.mLogger, paramWifiManager);
    if (!localReadSetting.desiredNetworkConfigured())
      return "unknown";
    return localReadSetting.getSetting(paramInt);
  }

  protected String processSetting(String paramString, boolean paramBoolean, WifiManager paramWifiManager)
  {
    if ((paramString == null) || (paramWifiManager == null))
    {
      Util.log(this.mLogger, "Invalid data passed to processSetting()!");
      return null;
    }
    int i = paramString.indexOf("${SETTING_");
    int j = paramString.indexOf("}");
    String str1 = paramString.substring(i + 10, j);
    Util.log(this.mLogger, "Replacing Setting Number : " + str1);
    String str2 = getSettingValue(Util.parseInt(str1, 0), paramWifiManager);
    if (paramBoolean)
      try
      {
        String str4 = safeReplace(paramString, "${SETTING_" + str1 + "}", URLEncoder.encode(str2, "UTF-8"));
        return str4;
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        String str3 = safeReplace(paramString, "${SETTING_" + str1 + "}", "");
        localUnsupportedEncodingException.printStackTrace();
        return str3;
      }
    return safeReplace(paramString, "${SETTING_" + str1 + "}", str2);
  }

  protected String safeReplace(String paramString1, String paramString2, String paramString3)
  {
    if (paramString1 == null)
    {
      Util.log(this.mLogger, "instr is null when attempting replacements.");
      return new String("");
    }
    if (paramString2 == null)
    {
      Util.log(this.mLogger, "Replacements values are null when attempting to map.");
      return paramString1;
    }
    if (paramString3 == null)
    {
      Util.log(this.mLogger, "Null value to replace with.  Using empty string.");
      return paramString1.replace(paramString2, "");
    }
    return paramString1.replace(paramString2, paramString3);
  }

  // ERROR //
  public String varMap(String paramString, boolean paramBoolean, WifiManager paramWifiManager)
  {
    // Byte code:
    //   0: aload_1
    //   1: ifnonnull +13 -> 14
    //   4: new 68	java/lang/String
    //   7: dup
    //   8: ldc 111
    //   10: invokespecial 117	java/lang/String:<init>	(Ljava/lang/String;)V
    //   13: areturn
    //   14: aload_1
    //   15: ldc 128
    //   17: ldc 130
    //   19: invokevirtual 125	java/lang/String:replace	(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   22: ldc 132
    //   24: ldc 130
    //   26: invokevirtual 125	java/lang/String:replace	(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   29: astore 4
    //   31: aload_0
    //   32: getfield 19	net/cloudpath/xpressconnect/MapVariables:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   35: ifnull +134 -> 169
    //   38: aload_0
    //   39: getfield 19	net/cloudpath/xpressconnect/MapVariables:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   42: getfield 138	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:savedConfigInfo	Lnet/cloudpath/xpressconnect/parsers/SavedConfigInfo;
    //   45: ifnull +995 -> 1040
    //   48: aload_0
    //   49: getfield 19	net/cloudpath/xpressconnect/MapVariables:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   52: getfield 138	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:savedConfigInfo	Lnet/cloudpath/xpressconnect/parsers/SavedConfigInfo;
    //   55: getfield 144	net/cloudpath/xpressconnect/parsers/SavedConfigInfo:username	Ljava/lang/String;
    //   58: ifnull +899 -> 957
    //   61: iload_2
    //   62: ifeq +856 -> 918
    //   65: aload_0
    //   66: aload 4
    //   68: ldc 146
    //   70: aload_0
    //   71: getfield 19	net/cloudpath/xpressconnect/MapVariables:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   74: getfield 138	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:savedConfigInfo	Lnet/cloudpath/xpressconnect/parsers/SavedConfigInfo;
    //   77: getfield 144	net/cloudpath/xpressconnect/parsers/SavedConfigInfo:username	Ljava/lang/String;
    //   80: ldc 99
    //   82: invokestatic 105	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   85: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   88: astore 45
    //   90: aload 45
    //   92: astore 42
    //   94: aload_0
    //   95: aload 42
    //   97: ldc 148
    //   99: aload_0
    //   100: getfield 19	net/cloudpath/xpressconnect/MapVariables:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   103: getfield 138	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:savedConfigInfo	Lnet/cloudpath/xpressconnect/parsers/SavedConfigInfo;
    //   106: getfield 144	net/cloudpath/xpressconnect/parsers/SavedConfigInfo:username	Ljava/lang/String;
    //   109: ldc 99
    //   111: invokestatic 105	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   114: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   117: astore 44
    //   119: aload 44
    //   121: astore 38
    //   123: aload_0
    //   124: getfield 19	net/cloudpath/xpressconnect/MapVariables:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   127: getfield 138	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:savedConfigInfo	Lnet/cloudpath/xpressconnect/parsers/SavedConfigInfo;
    //   130: getfield 151	net/cloudpath/xpressconnect/parsers/SavedConfigInfo:password	Ljava/lang/String;
    //   133: ifnull +892 -> 1025
    //   136: iload_2
    //   137: ifeq +865 -> 1002
    //   140: aload_0
    //   141: aload 38
    //   143: ldc 153
    //   145: aload_0
    //   146: getfield 19	net/cloudpath/xpressconnect/MapVariables:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   149: getfield 138	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:savedConfigInfo	Lnet/cloudpath/xpressconnect/parsers/SavedConfigInfo;
    //   152: getfield 151	net/cloudpath/xpressconnect/parsers/SavedConfigInfo:password	Ljava/lang/String;
    //   155: ldc 99
    //   157: invokestatic 105	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   160: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   163: astore 40
    //   165: aload 40
    //   167: astore 4
    //   169: aload_0
    //   170: aload_0
    //   171: aload_0
    //   172: aload 4
    //   174: ldc 155
    //   176: ldc 111
    //   178: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   181: ldc 157
    //   183: ldc 111
    //   185: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   188: ldc 159
    //   190: ldc 111
    //   192: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   195: astore 5
    //   197: iload_2
    //   198: ifeq +906 -> 1104
    //   201: aload_0
    //   202: aload 5
    //   204: ldc 161
    //   206: getstatic 166	android/os/Build:BOARD	Ljava/lang/String;
    //   209: ldc 99
    //   211: invokestatic 105	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   214: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   217: astore 5
    //   219: aload_0
    //   220: aload 5
    //   222: ldc 168
    //   224: getstatic 171	android/os/Build:MANUFACTURER	Ljava/lang/String;
    //   227: ldc 99
    //   229: invokestatic 105	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   232: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   235: astore 5
    //   237: aload_0
    //   238: aload 5
    //   240: ldc 173
    //   242: getstatic 176	android/os/Build:MODEL	Ljava/lang/String;
    //   245: ldc 99
    //   247: invokestatic 105	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   250: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   253: astore 37
    //   255: aload 37
    //   257: astore 6
    //   259: aload_0
    //   260: aload_0
    //   261: aload_0
    //   262: aload_0
    //   263: aload_0
    //   264: aload_0
    //   265: aload_0
    //   266: aload 6
    //   268: ldc 178
    //   270: ldc 111
    //   272: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   275: ldc 180
    //   277: ldc 111
    //   279: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   282: ldc 182
    //   284: ldc 111
    //   286: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   289: ldc 184
    //   291: ldc 111
    //   293: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   296: ldc 186
    //   298: ldc 111
    //   300: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   303: ldc 188
    //   305: ldc 111
    //   307: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   310: ldc 190
    //   312: ldc 111
    //   314: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   317: astore 7
    //   319: iload_2
    //   320: ifeq +891 -> 1211
    //   323: aload_0
    //   324: aload 7
    //   326: ldc 192
    //   328: invokestatic 198	java/util/Locale:getDefault	()Ljava/util/Locale;
    //   331: invokevirtual 201	java/util/Locale:getLanguage	()Ljava/lang/String;
    //   334: invokevirtual 204	java/lang/String:toLowerCase	()Ljava/lang/String;
    //   337: ldc 99
    //   339: invokestatic 105	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   342: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   345: astore 7
    //   347: aload_0
    //   348: aload 7
    //   350: ldc 206
    //   352: invokestatic 198	java/util/Locale:getDefault	()Ljava/util/Locale;
    //   355: invokevirtual 201	java/util/Locale:getLanguage	()Ljava/lang/String;
    //   358: invokevirtual 209	java/lang/String:toUpperCase	()Ljava/lang/String;
    //   361: ldc 99
    //   363: invokestatic 105	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   366: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   369: astore 7
    //   371: aload_0
    //   372: aload 7
    //   374: ldc 211
    //   376: invokestatic 198	java/util/Locale:getDefault	()Ljava/util/Locale;
    //   379: invokevirtual 214	java/util/Locale:getISO3Language	()Ljava/lang/String;
    //   382: invokevirtual 204	java/lang/String:toLowerCase	()Ljava/lang/String;
    //   385: ldc 99
    //   387: invokestatic 105	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   390: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   393: astore 7
    //   395: aload_0
    //   396: aload 7
    //   398: ldc 216
    //   400: invokestatic 198	java/util/Locale:getDefault	()Ljava/util/Locale;
    //   403: invokevirtual 214	java/util/Locale:getISO3Language	()Ljava/lang/String;
    //   406: invokevirtual 209	java/lang/String:toUpperCase	()Ljava/lang/String;
    //   409: ldc 99
    //   411: invokestatic 105	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   414: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   417: astore 7
    //   419: aload_0
    //   420: aload 7
    //   422: ldc 218
    //   424: invokestatic 198	java/util/Locale:getDefault	()Ljava/util/Locale;
    //   427: invokevirtual 221	java/util/Locale:getCountry	()Ljava/lang/String;
    //   430: invokevirtual 204	java/lang/String:toLowerCase	()Ljava/lang/String;
    //   433: ldc 99
    //   435: invokestatic 105	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   438: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   441: astore 7
    //   443: aload_0
    //   444: aload 7
    //   446: ldc 223
    //   448: invokestatic 198	java/util/Locale:getDefault	()Ljava/util/Locale;
    //   451: invokevirtual 221	java/util/Locale:getCountry	()Ljava/lang/String;
    //   454: invokevirtual 209	java/lang/String:toUpperCase	()Ljava/lang/String;
    //   457: ldc 99
    //   459: invokestatic 105	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   462: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   465: astore 7
    //   467: aload_0
    //   468: aload 7
    //   470: ldc 225
    //   472: invokestatic 198	java/util/Locale:getDefault	()Ljava/util/Locale;
    //   475: invokevirtual 228	java/util/Locale:getISO3Country	()Ljava/lang/String;
    //   478: invokevirtual 204	java/lang/String:toLowerCase	()Ljava/lang/String;
    //   481: ldc 99
    //   483: invokestatic 105	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   486: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   489: astore 7
    //   491: aload_0
    //   492: aload 7
    //   494: ldc 230
    //   496: invokestatic 198	java/util/Locale:getDefault	()Ljava/util/Locale;
    //   499: invokevirtual 228	java/util/Locale:getISO3Country	()Ljava/lang/String;
    //   502: invokevirtual 209	java/lang/String:toUpperCase	()Ljava/lang/String;
    //   505: ldc 99
    //   507: invokestatic 105	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   510: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   513: astore 35
    //   515: aload 35
    //   517: astore 8
    //   519: aload_0
    //   520: aload_0
    //   521: aload 8
    //   523: ldc 232
    //   525: ldc 111
    //   527: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   530: ldc 234
    //   532: ldc 111
    //   534: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   537: astore 9
    //   539: aload_0
    //   540: getfield 19	net/cloudpath/xpressconnect/MapVariables:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   543: getfield 138	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:savedConfigInfo	Lnet/cloudpath/xpressconnect/parsers/SavedConfigInfo;
    //   546: ifnull +837 -> 1383
    //   549: aload_0
    //   550: getfield 19	net/cloudpath/xpressconnect/MapVariables:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   553: getfield 138	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:savedConfigInfo	Lnet/cloudpath/xpressconnect/parsers/SavedConfigInfo;
    //   556: getfield 237	net/cloudpath/xpressconnect/parsers/SavedConfigInfo:ssid	Ljava/lang/String;
    //   559: ifnull +824 -> 1383
    //   562: iload_2
    //   563: ifeq +797 -> 1360
    //   566: aload_0
    //   567: aload 9
    //   569: ldc 239
    //   571: aload_0
    //   572: getfield 19	net/cloudpath/xpressconnect/MapVariables:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   575: getfield 138	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:savedConfigInfo	Lnet/cloudpath/xpressconnect/parsers/SavedConfigInfo;
    //   578: getfield 237	net/cloudpath/xpressconnect/parsers/SavedConfigInfo:ssid	Ljava/lang/String;
    //   581: ldc 99
    //   583: invokestatic 105	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   586: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   589: astore 33
    //   591: aload 33
    //   593: astore 10
    //   595: aload_0
    //   596: aload_0
    //   597: aload_0
    //   598: aload_0
    //   599: aload_0
    //   600: aload 10
    //   602: ldc 241
    //   604: ldc 243
    //   606: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   609: ldc 178
    //   611: ldc 111
    //   613: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   616: ldc 180
    //   618: ldc 111
    //   620: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   623: ldc 188
    //   625: ldc 111
    //   627: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   630: ldc 245
    //   632: ldc 247
    //   634: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   637: astore 11
    //   639: iload_2
    //   640: ifeq +953 -> 1593
    //   643: aload_0
    //   644: aload 11
    //   646: ldc 249
    //   648: aload_0
    //   649: getfield 21	net/cloudpath/xpressconnect/MapVariables:av	Lnet/cloudpath/xpressconnect/AndroidVersion;
    //   652: invokevirtual 253	net/cloudpath/xpressconnect/AndroidVersion:patchLevel	()Ljava/lang/CharSequence;
    //   655: checkcast 68	java/lang/String
    //   658: ldc 99
    //   660: invokestatic 105	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   663: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   666: astore 27
    //   668: aload 27
    //   670: astore 12
    //   672: aload_0
    //   673: aload 12
    //   675: ldc 161
    //   677: ldc 111
    //   679: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   682: astore 13
    //   684: iload_2
    //   685: ifeq +953 -> 1638
    //   688: aload_0
    //   689: aload 13
    //   691: ldc 255
    //   693: aload_0
    //   694: getfield 21	net/cloudpath/xpressconnect/MapVariables:av	Lnet/cloudpath/xpressconnect/AndroidVersion;
    //   697: invokevirtual 258	net/cloudpath/xpressconnect/AndroidVersion:fullVersion	()Ljava/lang/String;
    //   700: invokevirtual 259	java/lang/String:toString	()Ljava/lang/String;
    //   703: ldc 99
    //   705: invokestatic 105	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   708: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   711: astore 25
    //   713: aload 25
    //   715: astore 14
    //   717: aload_0
    //   718: aload 14
    //   720: ldc 190
    //   722: ldc 111
    //   724: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   727: astore 15
    //   729: iload_2
    //   730: ifeq +958 -> 1688
    //   733: aload_0
    //   734: getfield 23	net/cloudpath/xpressconnect/MapVariables:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   737: aload_3
    //   738: invokestatic 263	net/cloudpath/xpressconnect/Util:getMac	(Lnet/cloudpath/xpressconnect/logger/Logger;Landroid/net/wifi/WifiManager;)Ljava/lang/String;
    //   741: astore 22
    //   743: aload 22
    //   745: ifnonnull +7 -> 752
    //   748: ldc 111
    //   750: astore 22
    //   752: aload_0
    //   753: aload 15
    //   755: ldc_w 265
    //   758: aload 22
    //   760: ldc 99
    //   762: invokestatic 105	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   765: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   768: astore 15
    //   770: aload_0
    //   771: aload 15
    //   773: ldc_w 267
    //   776: aload_0
    //   777: getfield 21	net/cloudpath/xpressconnect/MapVariables:av	Lnet/cloudpath/xpressconnect/AndroidVersion;
    //   780: invokevirtual 270	net/cloudpath/xpressconnect/AndroidVersion:getBaseAndroidVerAsString	()Ljava/lang/String;
    //   783: ldc 99
    //   785: invokestatic 105	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   788: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   791: astore 23
    //   793: aload 23
    //   795: astore 16
    //   797: aload_0
    //   798: aload 16
    //   800: ldc_w 272
    //   803: ldc 111
    //   805: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   808: astore 17
    //   810: aload_0
    //   811: getfield 19	net/cloudpath/xpressconnect/MapVariables:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   814: getfield 275	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:ipAddress	Ljava/lang/String;
    //   817: ifnull +951 -> 1768
    //   820: iload_2
    //   821: ifeq +926 -> 1747
    //   824: aload_0
    //   825: aload 17
    //   827: ldc_w 277
    //   830: aload_0
    //   831: getfield 19	net/cloudpath/xpressconnect/MapVariables:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   834: getfield 275	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:ipAddress	Ljava/lang/String;
    //   837: ldc 99
    //   839: invokestatic 105	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   842: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   845: astore 20
    //   847: aload 20
    //   849: astore 18
    //   851: aload 18
    //   853: ldc 66
    //   855: invokevirtual 281	java/lang/String:contains	(Ljava/lang/CharSequence;)Z
    //   858: ifeq +926 -> 1784
    //   861: aload_0
    //   862: aload 18
    //   864: iload_2
    //   865: aload_3
    //   866: invokevirtual 283	net/cloudpath/xpressconnect/MapVariables:processSetting	(Ljava/lang/String;ZLandroid/net/wifi/WifiManager;)Ljava/lang/String;
    //   869: astore 18
    //   871: goto -20 -> 851
    //   874: astore 41
    //   876: aload_0
    //   877: aload 4
    //   879: ldc 146
    //   881: ldc 111
    //   883: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   886: astore 42
    //   888: aload 41
    //   890: invokevirtual 114	java/io/UnsupportedEncodingException:printStackTrace	()V
    //   893: goto -799 -> 94
    //   896: astore 43
    //   898: aload_0
    //   899: aload 42
    //   901: ldc 148
    //   903: ldc 111
    //   905: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   908: astore 38
    //   910: aload 43
    //   912: invokevirtual 114	java/io/UnsupportedEncodingException:printStackTrace	()V
    //   915: goto -792 -> 123
    //   918: aload_0
    //   919: aload_0
    //   920: aload 4
    //   922: ldc 146
    //   924: aload_0
    //   925: getfield 19	net/cloudpath/xpressconnect/MapVariables:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   928: getfield 138	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:savedConfigInfo	Lnet/cloudpath/xpressconnect/parsers/SavedConfigInfo;
    //   931: getfield 144	net/cloudpath/xpressconnect/parsers/SavedConfigInfo:username	Ljava/lang/String;
    //   934: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   937: ldc 148
    //   939: aload_0
    //   940: getfield 19	net/cloudpath/xpressconnect/MapVariables:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   943: getfield 138	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:savedConfigInfo	Lnet/cloudpath/xpressconnect/parsers/SavedConfigInfo;
    //   946: getfield 144	net/cloudpath/xpressconnect/parsers/SavedConfigInfo:username	Ljava/lang/String;
    //   949: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   952: astore 38
    //   954: goto -831 -> 123
    //   957: aload_0
    //   958: aload_0
    //   959: aload 4
    //   961: ldc 146
    //   963: ldc 111
    //   965: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   968: ldc 148
    //   970: ldc 111
    //   972: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   975: astore 38
    //   977: goto -854 -> 123
    //   980: astore 39
    //   982: aload_0
    //   983: aload 38
    //   985: ldc 153
    //   987: ldc 111
    //   989: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   992: astore 4
    //   994: aload 39
    //   996: invokevirtual 114	java/io/UnsupportedEncodingException:printStackTrace	()V
    //   999: goto -830 -> 169
    //   1002: aload_0
    //   1003: aload 38
    //   1005: ldc 153
    //   1007: aload_0
    //   1008: getfield 19	net/cloudpath/xpressconnect/MapVariables:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   1011: getfield 138	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:savedConfigInfo	Lnet/cloudpath/xpressconnect/parsers/SavedConfigInfo;
    //   1014: getfield 151	net/cloudpath/xpressconnect/parsers/SavedConfigInfo:password	Ljava/lang/String;
    //   1017: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1020: astore 4
    //   1022: goto -853 -> 169
    //   1025: aload_0
    //   1026: aload 38
    //   1028: ldc 153
    //   1030: ldc 111
    //   1032: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1035: astore 4
    //   1037: goto -868 -> 169
    //   1040: aload_0
    //   1041: aload_0
    //   1042: aload_0
    //   1043: aload 4
    //   1045: ldc 146
    //   1047: ldc 111
    //   1049: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1052: ldc 148
    //   1054: ldc 111
    //   1056: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1059: ldc 153
    //   1061: ldc 111
    //   1063: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1066: astore 4
    //   1068: goto -899 -> 169
    //   1071: astore 36
    //   1073: aload_0
    //   1074: aload_0
    //   1075: aload_0
    //   1076: aload 5
    //   1078: ldc 161
    //   1080: ldc 111
    //   1082: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1085: ldc 168
    //   1087: ldc 111
    //   1089: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1092: ldc 173
    //   1094: ldc 111
    //   1096: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1099: astore 6
    //   1101: goto -842 -> 259
    //   1104: aload_0
    //   1105: aload_0
    //   1106: aload_0
    //   1107: aload 5
    //   1109: ldc 161
    //   1111: getstatic 166	android/os/Build:BOARD	Ljava/lang/String;
    //   1114: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1117: ldc 168
    //   1119: getstatic 171	android/os/Build:MANUFACTURER	Ljava/lang/String;
    //   1122: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1125: ldc 173
    //   1127: getstatic 176	android/os/Build:MODEL	Ljava/lang/String;
    //   1130: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1133: astore 6
    //   1135: goto -876 -> 259
    //   1138: astore 34
    //   1140: aload_0
    //   1141: aload_0
    //   1142: aload_0
    //   1143: aload_0
    //   1144: aload_0
    //   1145: aload_0
    //   1146: aload_0
    //   1147: aload_0
    //   1148: aload 7
    //   1150: ldc 192
    //   1152: ldc 111
    //   1154: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1157: ldc 206
    //   1159: ldc 111
    //   1161: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1164: ldc 211
    //   1166: ldc 111
    //   1168: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1171: ldc 216
    //   1173: ldc 111
    //   1175: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1178: ldc 218
    //   1180: ldc 111
    //   1182: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1185: ldc 223
    //   1187: ldc 111
    //   1189: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1192: ldc 225
    //   1194: ldc 111
    //   1196: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1199: ldc 230
    //   1201: ldc 111
    //   1203: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1206: astore 8
    //   1208: goto -689 -> 519
    //   1211: aload_0
    //   1212: aload_0
    //   1213: aload_0
    //   1214: aload_0
    //   1215: aload_0
    //   1216: aload_0
    //   1217: aload_0
    //   1218: aload_0
    //   1219: aload 7
    //   1221: ldc 192
    //   1223: invokestatic 198	java/util/Locale:getDefault	()Ljava/util/Locale;
    //   1226: invokevirtual 201	java/util/Locale:getLanguage	()Ljava/lang/String;
    //   1229: invokevirtual 204	java/lang/String:toLowerCase	()Ljava/lang/String;
    //   1232: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1235: ldc 206
    //   1237: invokestatic 198	java/util/Locale:getDefault	()Ljava/util/Locale;
    //   1240: invokevirtual 201	java/util/Locale:getLanguage	()Ljava/lang/String;
    //   1243: invokevirtual 209	java/lang/String:toUpperCase	()Ljava/lang/String;
    //   1246: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1249: ldc 211
    //   1251: invokestatic 198	java/util/Locale:getDefault	()Ljava/util/Locale;
    //   1254: invokevirtual 214	java/util/Locale:getISO3Language	()Ljava/lang/String;
    //   1257: invokevirtual 204	java/lang/String:toLowerCase	()Ljava/lang/String;
    //   1260: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1263: ldc 216
    //   1265: invokestatic 198	java/util/Locale:getDefault	()Ljava/util/Locale;
    //   1268: invokevirtual 214	java/util/Locale:getISO3Language	()Ljava/lang/String;
    //   1271: invokevirtual 209	java/lang/String:toUpperCase	()Ljava/lang/String;
    //   1274: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1277: ldc 218
    //   1279: invokestatic 198	java/util/Locale:getDefault	()Ljava/util/Locale;
    //   1282: invokevirtual 221	java/util/Locale:getCountry	()Ljava/lang/String;
    //   1285: invokevirtual 204	java/lang/String:toLowerCase	()Ljava/lang/String;
    //   1288: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1291: ldc 223
    //   1293: invokestatic 198	java/util/Locale:getDefault	()Ljava/util/Locale;
    //   1296: invokevirtual 221	java/util/Locale:getCountry	()Ljava/lang/String;
    //   1299: invokevirtual 209	java/lang/String:toUpperCase	()Ljava/lang/String;
    //   1302: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1305: ldc 225
    //   1307: invokestatic 198	java/util/Locale:getDefault	()Ljava/util/Locale;
    //   1310: invokevirtual 228	java/util/Locale:getISO3Country	()Ljava/lang/String;
    //   1313: invokevirtual 204	java/lang/String:toLowerCase	()Ljava/lang/String;
    //   1316: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1319: ldc 230
    //   1321: invokestatic 198	java/util/Locale:getDefault	()Ljava/util/Locale;
    //   1324: invokevirtual 228	java/util/Locale:getISO3Country	()Ljava/lang/String;
    //   1327: invokevirtual 209	java/lang/String:toUpperCase	()Ljava/lang/String;
    //   1330: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1333: astore 8
    //   1335: goto -816 -> 519
    //   1338: astore 32
    //   1340: aload_0
    //   1341: aload 9
    //   1343: ldc 239
    //   1345: ldc 111
    //   1347: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1350: astore 10
    //   1352: aload 32
    //   1354: invokevirtual 114	java/io/UnsupportedEncodingException:printStackTrace	()V
    //   1357: goto -762 -> 595
    //   1360: aload_0
    //   1361: aload 9
    //   1363: ldc 239
    //   1365: aload_0
    //   1366: getfield 19	net/cloudpath/xpressconnect/MapVariables:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   1369: getfield 138	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:savedConfigInfo	Lnet/cloudpath/xpressconnect/parsers/SavedConfigInfo;
    //   1372: getfield 237	net/cloudpath/xpressconnect/parsers/SavedConfigInfo:ssid	Ljava/lang/String;
    //   1375: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1378: astore 10
    //   1380: goto -785 -> 595
    //   1383: aload_0
    //   1384: getfield 19	net/cloudpath/xpressconnect/MapVariables:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   1387: getfield 287	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:selectedProfile	Lnet/cloudpath/xpressconnect/parsers/config/ProfileElement;
    //   1390: ifnull +166 -> 1556
    //   1393: aload_0
    //   1394: getfield 19	net/cloudpath/xpressconnect/MapVariables:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   1397: getfield 287	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:selectedProfile	Lnet/cloudpath/xpressconnect/parsers/config/ProfileElement;
    //   1400: iconst_1
    //   1401: ldc_w 288
    //   1404: invokevirtual 293	net/cloudpath/xpressconnect/parsers/config/ProfileElement:getSetting	(II)Lnet/cloudpath/xpressconnect/parsers/config/SettingElement;
    //   1407: astore 28
    //   1409: aload 28
    //   1411: ifnonnull +18 -> 1429
    //   1414: aload_0
    //   1415: aload 9
    //   1417: ldc 239
    //   1419: ldc 111
    //   1421: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1424: astore 10
    //   1426: goto -831 -> 595
    //   1429: aload 28
    //   1431: getfield 298	net/cloudpath/xpressconnect/parsers/config/SettingElement:additionalValue	Ljava/lang/String;
    //   1434: ifnull +107 -> 1541
    //   1437: aload 28
    //   1439: getfield 298	net/cloudpath/xpressconnect/parsers/config/SettingElement:additionalValue	Ljava/lang/String;
    //   1442: ldc_w 300
    //   1445: invokevirtual 304	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   1448: astore 29
    //   1450: aload 29
    //   1452: arraylength
    //   1453: iconst_3
    //   1454: if_icmpge +18 -> 1472
    //   1457: aload_0
    //   1458: aload 9
    //   1460: ldc 239
    //   1462: ldc 111
    //   1464: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1467: astore 10
    //   1469: goto -874 -> 595
    //   1472: iload_2
    //   1473: ifeq +51 -> 1524
    //   1476: aload_0
    //   1477: aload 9
    //   1479: ldc 239
    //   1481: aload 29
    //   1483: iconst_2
    //   1484: aaload
    //   1485: ldc 99
    //   1487: invokestatic 105	java/net/URLEncoder:encode	(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1490: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1493: astore 31
    //   1495: aload 31
    //   1497: astore 10
    //   1499: goto -904 -> 595
    //   1502: astore 30
    //   1504: aload_0
    //   1505: aload 9
    //   1507: ldc 239
    //   1509: ldc 111
    //   1511: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1514: astore 10
    //   1516: aload 30
    //   1518: invokevirtual 114	java/io/UnsupportedEncodingException:printStackTrace	()V
    //   1521: goto -926 -> 595
    //   1524: aload_0
    //   1525: aload 9
    //   1527: ldc 239
    //   1529: aload 29
    //   1531: iconst_2
    //   1532: aaload
    //   1533: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1536: astore 10
    //   1538: goto -943 -> 595
    //   1541: aload_0
    //   1542: aload 9
    //   1544: ldc 239
    //   1546: ldc 111
    //   1548: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1551: astore 10
    //   1553: goto -958 -> 595
    //   1556: aload_0
    //   1557: aload 9
    //   1559: ldc 239
    //   1561: ldc 111
    //   1563: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1566: astore 10
    //   1568: goto -973 -> 595
    //   1571: astore 26
    //   1573: aload_0
    //   1574: aload 11
    //   1576: ldc 249
    //   1578: ldc 111
    //   1580: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1583: astore 12
    //   1585: aload 26
    //   1587: invokevirtual 114	java/io/UnsupportedEncodingException:printStackTrace	()V
    //   1590: goto -918 -> 672
    //   1593: aload_0
    //   1594: aload 11
    //   1596: ldc 249
    //   1598: aload_0
    //   1599: getfield 21	net/cloudpath/xpressconnect/MapVariables:av	Lnet/cloudpath/xpressconnect/AndroidVersion;
    //   1602: invokevirtual 253	net/cloudpath/xpressconnect/AndroidVersion:patchLevel	()Ljava/lang/CharSequence;
    //   1605: checkcast 68	java/lang/String
    //   1608: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1611: astore 12
    //   1613: goto -941 -> 672
    //   1616: astore 24
    //   1618: aload_0
    //   1619: aload 13
    //   1621: ldc 255
    //   1623: ldc 111
    //   1625: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1628: astore 14
    //   1630: aload 24
    //   1632: invokevirtual 114	java/io/UnsupportedEncodingException:printStackTrace	()V
    //   1635: goto -918 -> 717
    //   1638: aload_0
    //   1639: aload 13
    //   1641: ldc 255
    //   1643: aload_0
    //   1644: getfield 21	net/cloudpath/xpressconnect/MapVariables:av	Lnet/cloudpath/xpressconnect/AndroidVersion;
    //   1647: invokevirtual 258	net/cloudpath/xpressconnect/AndroidVersion:fullVersion	()Ljava/lang/String;
    //   1650: invokevirtual 259	java/lang/String:toString	()Ljava/lang/String;
    //   1653: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1656: astore 14
    //   1658: goto -941 -> 717
    //   1661: astore 21
    //   1663: aload_0
    //   1664: aload_0
    //   1665: aload 15
    //   1667: ldc_w 265
    //   1670: ldc 111
    //   1672: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1675: ldc_w 267
    //   1678: ldc 111
    //   1680: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1683: astore 16
    //   1685: goto -888 -> 797
    //   1688: aload_0
    //   1689: aload_0
    //   1690: aload 15
    //   1692: ldc_w 265
    //   1695: aload_0
    //   1696: getfield 23	net/cloudpath/xpressconnect/MapVariables:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   1699: aload_3
    //   1700: invokestatic 263	net/cloudpath/xpressconnect/Util:getMac	(Lnet/cloudpath/xpressconnect/logger/Logger;Landroid/net/wifi/WifiManager;)Ljava/lang/String;
    //   1703: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1706: ldc_w 267
    //   1709: aload_0
    //   1710: getfield 21	net/cloudpath/xpressconnect/MapVariables:av	Lnet/cloudpath/xpressconnect/AndroidVersion;
    //   1713: invokevirtual 270	net/cloudpath/xpressconnect/AndroidVersion:getBaseAndroidVerAsString	()Ljava/lang/String;
    //   1716: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1719: astore 16
    //   1721: goto -924 -> 797
    //   1724: astore 19
    //   1726: aload_0
    //   1727: aload 17
    //   1729: ldc_w 277
    //   1732: ldc 111
    //   1734: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1737: astore 18
    //   1739: aload 19
    //   1741: invokevirtual 114	java/io/UnsupportedEncodingException:printStackTrace	()V
    //   1744: goto -893 -> 851
    //   1747: aload_0
    //   1748: aload 17
    //   1750: ldc_w 277
    //   1753: aload_0
    //   1754: getfield 19	net/cloudpath/xpressconnect/MapVariables:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   1757: getfield 275	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:ipAddress	Ljava/lang/String;
    //   1760: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1763: astore 18
    //   1765: goto -914 -> 851
    //   1768: aload_0
    //   1769: aload 17
    //   1771: ldc_w 277
    //   1774: ldc 111
    //   1776: invokevirtual 109	net/cloudpath/xpressconnect/MapVariables:safeReplace	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   1779: astore 18
    //   1781: goto -930 -> 851
    //   1784: aload 18
    //   1786: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   65	90	874	java/io/UnsupportedEncodingException
    //   94	119	896	java/io/UnsupportedEncodingException
    //   140	165	980	java/io/UnsupportedEncodingException
    //   201	255	1071	java/io/UnsupportedEncodingException
    //   323	515	1138	java/io/UnsupportedEncodingException
    //   566	591	1338	java/io/UnsupportedEncodingException
    //   1476	1495	1502	java/io/UnsupportedEncodingException
    //   643	668	1571	java/io/UnsupportedEncodingException
    //   688	713	1616	java/io/UnsupportedEncodingException
    //   733	743	1661	java/io/UnsupportedEncodingException
    //   752	793	1661	java/io/UnsupportedEncodingException
    //   824	847	1724	java/io/UnsupportedEncodingException
  }
}