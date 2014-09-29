package net.cloudpath.xpressconnect;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import com.cloudpath.common.util.Encode;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.io.File;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Locale;
import net.cloudpath.xpressconnect.certificates.CertInstaller;
import net.cloudpath.xpressconnect.certificates.CertUtils;
import net.cloudpath.xpressconnect.certificates.CertValidityChecks;
import net.cloudpath.xpressconnect.certificates.CertificateFactory;
import net.cloudpath.xpressconnect.certificates.ConvertPkcs7Certificates;
import net.cloudpath.xpressconnect.certificates.PEMValidator;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.majorworkarounds.Android43and44TlsWorkaround;
import net.cloudpath.xpressconnect.nativeproxy.WifiConfigurationProxy;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkItemElement;
import net.cloudpath.xpressconnect.parsers.config.ProfileElement;
import net.cloudpath.xpressconnect.parsers.config.SettingElement;
import net.cloudpath.xpressconnect.remote.WebInterface;
import net.cloudpath.xpressconnect.thread.ConfigureClientThread;

public class CreateNewConfiguration
{
  private int failReason = 21;
  private String mCaCertPath = null;
  private ArrayList<String> mCaCertificates = new ArrayList();
  private CertInstaller mCertInstaller = null;
  private Context mContext = null;
  private FailureReason mFailure = null;
  private GetGlobals mGlobals = null;
  private ParcelHelper mHelp = null;
  private Logger mLogger = null;
  private int mNumCaInstalled = -1;
  private Activity mParent = null;
  private NetworkConfigParser mParser = null;
  private String mSanPrincipal = null;
  private String mUserCertPath = null;
  private String mUserPkeyPath = null;
  private WifiManager mWifi = null;
  private WifiConfigurationProxy mWifiConfig = null;
  public String outerId = null;
  private ConfigureClientThread tParent = null;

  public CreateNewConfiguration(NetworkConfigParser paramNetworkConfigParser, Logger paramLogger, FailureReason paramFailureReason, Activity paramActivity, WifiManager paramWifiManager, ConfigureClientThread paramConfigureClientThread, GetGlobals paramGetGlobals)
  {
    this.mParser = paramNetworkConfigParser;
    this.mLogger = paramLogger;
    this.mParent = paramActivity;
    this.mContext = paramActivity;
    this.mFailure = paramFailureReason;
    this.tParent = paramConfigureClientThread;
    this.mWifi = paramWifiManager;
    this.mGlobals = paramGetGlobals;
    this.mHelp = new ParcelHelper("", this.mContext);
  }

  public CreateNewConfiguration(NetworkConfigParser paramNetworkConfigParser, Logger paramLogger, FailureReason paramFailureReason, Activity paramActivity, WifiManager paramWifiManager, ConfigureClientThread paramConfigureClientThread, GetGlobals paramGetGlobals, Context paramContext)
  {
    this.mParser = paramNetworkConfigParser;
    this.mLogger = paramLogger;
    this.mParent = paramActivity;
    this.mContext = paramContext;
    this.mFailure = paramFailureReason;
    this.tParent = paramConfigureClientThread;
    this.mWifi = paramWifiManager;
    this.mGlobals = paramGetGlobals;
    this.mHelp = new ParcelHelper("", this.mContext);
  }

  private WifiConfigurationProxy getConfigStruct()
  {
    if (this.mWifiConfig != null)
      return this.mWifiConfig;
    try
    {
      this.mWifiConfig = new WifiConfigurationProxy(null, this.mLogger);
      return this.mWifiConfig;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      localIllegalArgumentException.printStackTrace();
      Util.log(this.mLogger, "IllegalArgumentException creating WifiConfigurationProxy().");
      return null;
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      localNoSuchFieldException.printStackTrace();
      Util.log(this.mLogger, "NoSuchFieldException creating WifiConfigurationProxy().");
      return null;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      localClassNotFoundException.printStackTrace();
      Util.log(this.mLogger, "ClassNotFoundExeption creating WifiConfigurationProxy().");
      return null;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      localIllegalAccessException.printStackTrace();
      Util.log(this.mLogger, "IllegalAccessException creating WifiConfigurationProxy().");
    }
    return null;
  }

  private boolean isGalaxyNexus()
  {
    return isGalaxyNexus(Build.MODEL);
  }

  private boolean shouldBlockRootedOrCustom()
  {
    if ((this.mParser == null) || (this.mParser.selectedProfile == null))
    {
      Util.log(this.mLogger, "Parser or selected profile is null in shouldBlockRooted()!");
      return true;
    }
    if (this.mParser.selectedProfile.getSetting(2, 80037) == null)
      return false;
    return Util.isDeviceRootedOrCustom(this.mLogger);
  }

  private boolean shouldFailForBadTlsConfig()
  {
    if ((!Util.usingCaValidation(this.mParser, this.mLogger)) && (Build.VERSION.SDK_INT >= 17) && (Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).contentEquals("samsung")))
    {
      if ((Build.MODEL.toLowerCase(Locale.ENGLISH).contentEquals("gt-i9505g")) || (isGalaxyNexus()))
        Util.log(this.mLogger, "Device appears to be Galaxy Nexus of Galaxy S4 Google Play edition.  Skipping TLS error.");
    }
    else
      return false;
    if (this.mFailure != null)
      this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mContext.getString(this.mHelp.getIdentifier("xpc_device_requires_ca_cert_for_tls", "string")), 9);
    return true;
  }

  private boolean userCertGetFailed(WebInterface paramWebInterface, String paramString)
  {
    if ((paramWebInterface == null) || (paramString == null))
    {
      Util.log(this.mLogger, "Unable to check web result.  The web class or result string was null.");
      return true;
    }
    if (paramWebInterface.resultCode != 200)
    {
      Util.log(this.mLogger, "Server Error : " + paramWebInterface.resultCode + " " + paramWebInterface.statusText);
      Util.log(this.mLogger, "Result : " + paramString);
      if ((paramWebInterface.resultCode < 0) && (paramWebInterface.statusText == null))
        this.mFailure.setFailReason(FailureReason.useWarningIcon, paramString, 2);
      while (true)
      {
        if (this.mFailure != null)
          this.mFailure.returnToCreds = true;
        if (this.mParser.selectedProfile.getSetting(2, 80034) != null)
          break;
        this.failReason = -2;
        return true;
        if ((paramWebInterface.resultCode == 401) && (paramWebInterface.statusText.equals("Unauthorized")))
        {
          if (this.mParser != null)
            this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mParser.getOptionById(416) + "  (401 - Unauthorized)", 2);
          else
            this.mFailure.setFailReason(FailureReason.useWarningIcon, "Authentication failed.  (401 - Unauthorized)", 2);
        }
        else if ((paramWebInterface.statusText == null) || (paramWebInterface.statusText.contentEquals("")))
        {
          if ((paramString == null) || (paramString.contentEquals("")))
            this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_cant_contact_web_server", "string")), 2);
          else
            this.mFailure.setFailReason(FailureReason.useWarningIcon, paramString, 2);
        }
        else if (this.mFailure != null)
          this.mFailure.setFailReason(FailureReason.useWarningIcon, paramWebInterface.statusText + " (" + paramWebInterface.resultCode + ")", 2);
      }
      if (this.mFailure != null)
      {
        this.mFailure.returnToCreds = false;
        this.mFailure.setFailReason(FailureReason.useWarningIcon, "Unable to request client certificate.  Error was : " + paramWebInterface.statusText + " (" + paramWebInterface.resultCode + ")", 2);
      }
      this.failReason = 21;
      return true;
    }
    return false;
  }

  protected boolean acquireCaCertificates()
  {
    int i = 0;
    if ((this.mParser == null) || (this.mParser.selectedProfile == null))
    {
      Util.log(this.mLogger, "No configuration information available in acquireCaCertificates()!");
      return false;
    }
    List localList1 = this.mParser.selectedProfile.getSettingMulti(0, 61301);
    if ((localList1 == null) || (localList1.size() <= 0))
    {
      Util.log(this.mLogger, "Unable to locate the CA cert install element!");
      if (this.mFailure != null)
        this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_no_cert_usable", "string")), 5);
      return false;
    }
    List localList2 = this.mParser.selectedProfile.getSettingMulti(0, 61308);
    if (localList2 != null)
      localList1.addAll(localList2);
    this.mCaCertificates.clear();
    CertValidityChecks localCertValidityChecks = new CertValidityChecks(this.mLogger, this.mContext);
    for (int j = 0; j < localList1.size(); j++)
      if (((SettingElement)localList1.get(j)).unenforcedValue != null)
      {
        i++;
        new PEMValidator(this.mLogger, ((SettingElement)localList1.get(j)).unenforcedValue).isValid();
        CertUtils localCertUtils = new CertUtils(this.mLogger);
        if (localCertUtils.certIsCa(localCertUtils.getX509Cert(((SettingElement)localList1.get(j)).unenforcedValue)))
        {
          if (!localCertValidityChecks.checkValidity(((SettingElement)localList1.get(j)).unenforcedValue, this.mFailure))
            return false;
        }
        else
          Util.log(this.mLogger, "Certificate was not a CA cert, but was in the CA chain!  Won't check validity.");
        this.mCaCertificates.add(((SettingElement)localList1.get(j)).unenforcedValue);
      }
    if (i < 1)
    {
      Util.log(this.mLogger, "No CA certificates defined in the configuration file?!");
      return false;
    }
    Util.log(this.mLogger, "Configuration contained " + i + " certificate(s).");
    return true;
  }

  protected boolean acquireUserCertificate()
  {
    Util.log(this.mLogger, "Installing user certificate.");
    if (this.mGlobals == null)
    {
      Util.log(this.mLogger, "Service context is null.");
      return false;
    }
    if (!Util.stringIsEmpty(this.mGlobals.getUserCert()))
    {
      Util.log(this.mLogger, "Already have user certificate.. Won't request another..");
      return true;
    }
    if (this.mParser.savedConfigInfo == null)
    {
      Util.log(this.mLogger, "No saved configuration information available in acquireUserCertificate()!");
      return false;
    }
    String str1 = getUserCertificateUrl().replace(" ", "");
    if (this.mParser.savedConfigInfo.username == null)
      this.mParser.savedConfigInfo.username = this.mParser.selectedNetwork.defaultUsername;
    if (this.mParser.savedConfigInfo.username == null)
      this.mParser.savedConfigInfo.username = "";
    if ((this.mParser.savedConfigInfo.password == null) && (this.mParser.selectedNetwork.defaultPassword != null))
      this.mParser.savedConfigInfo.password = Encode.xpcDeobsfucate(this.mParser.selectedNetwork.defaultPassword);
    CertificateFactory localCertificateFactory = createCertificateData();
    this.mGlobals.setPrivateKey(localCertificateFactory.getPrivateKey());
    String[] arrayOfString = getMappedFullUrl(str1, localCertificateFactory.getCsrPEMencoded());
    if ((arrayOfString == null) || (arrayOfString.length < 2))
    {
      Util.log(this.mLogger, "Insufficient information to attempt to request a user certificate.");
      return false;
    }
    WebInterface localWebInterface = new WebInterface(this.mLogger);
    String str2 = localWebInterface.getCertWebPageMultiAuth(arrayOfString[0], arrayOfString[1], this.mParser.savedConfigInfo.username, this.mParser.savedConfigInfo.password, true);
    if (userCertGetFailed(localWebInterface, str2))
      return false;
    Util.log(this.mLogger, "Server Result : " + localWebInterface.resultCode + " " + localWebInterface.statusText);
    Util.log(this.mLogger, "Web CSR Result : " + filterIdentityTagData(str2));
    if (!resultContainsCertificate(str2))
      return false;
    if (str2.contains("-----BEGIN PKCS7-----"))
      return handlePkcs7UserCert(str2);
    return handlePemUserCert(str2);
  }

  protected int bounceWifi()
  {
    Util.log(this.mLogger, "Bouncing the interface to try to get it to connect.");
    if (!this.mWifi.setWifiEnabled(false))
      Util.log(this.mLogger, "Couldn't disable the wireless interface.  Perhaps it is locked?");
    int i = 0;
    if (i < 30)
      if (this.tParent.shouldDie())
        this.mWifi.setWifiEnabled(true);
    while (true)
    {
      return -1;
      try
      {
        Thread.sleep(1000L);
        if (!this.mWifi.isWifiEnabled())
        {
          Util.log(this.mLogger, "Wifi interface has been disabled.  Moving on.");
          if (this.mWifi.setWifiEnabled(true))
            break label126;
          Util.log(this.mLogger, "Couldn't enable the wireless interface!  Cannot continue.");
          return 14;
        }
      }
      catch (InterruptedException localInterruptedException2)
      {
        while (true)
          localInterruptedException2.printStackTrace();
        i++;
      }
      break;
      label126: int j = 0;
      while ((j >= 30) || (!this.tParent.shouldDie()))
        try
        {
          Thread.sleep(1000L);
          if (this.mWifi.isWifiEnabled())
          {
            Util.log(this.mLogger, "Wifi interface has been enabled.  Moving on.");
            if (this.mWifi.isWifiEnabled())
              break label197;
            return 14;
          }
        }
        catch (InterruptedException localInterruptedException1)
        {
          while (true)
            localInterruptedException1.printStackTrace();
          j++;
        }
    }
    label197: return 1;
  }

  public boolean buildConfiguration()
  {
    if (this.tParent.shouldDie());
    do
    {
      do
      {
        do
        {
          do
          {
            return false;
            if (!shouldBlockRootedOrCustom())
              break;
          }
          while (this.mFailure == null);
          this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mContext.getString(this.mHelp.getIdentifier("xpc_rooted_or_custom", "string")), 9);
          return false;
          if (!Util.isTls(this.mLogger, this.mParser, this.mContext, this.mFailure))
            break;
        }
        while (shouldFailForBadTlsConfig());
        if ((Build.VERSION.SDK_INT >= AndroidApiLevels.ANDROID_4_3) && (!new Android43and44TlsWorkaround(this.mLogger).doWorkaround(this.mParser, this.mWifi)))
        {
          this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mContext.getString(this.mHelp.getIdentifier("xpc_android43_and_44_workaround_failed", "string")), 4);
          return false;
        }
        if ((Util.usingPsk(this.mLogger, this.mParser)) || (Util.isOpenNetwork(this.mLogger, this.mParser)))
          break;
      }
      while (this.tParent.shouldDie());
      if (!installCertificates())
      {
        Util.log(this.mLogger, "Couldn't install root CA certificate!  (No methods remain.)");
        return false;
      }
    }
    while (this.tParent.shouldDie());
    if (!installProfile())
    {
      Util.log(this.mLogger, "Couldn't install profile!");
      return false;
    }
    Util.log(this.mLogger, "Configuration was built correctly.");
    return true;
  }

  protected String checkForIdentity(String paramString)
  {
    if ((paramString.contains("<identity>")) && (paramString.contains("</identity>")))
      return paramString.substring(paramString.indexOf("<identity>") + "<identity>".length(), paramString.indexOf("</identity>"));
    return null;
  }

  // ERROR //
  protected CertificateFactory createCertificateData()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 45	net/cloudpath/xpressconnect/CreateNewConfiguration:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   4: ifnull +26 -> 30
    //   7: aload_0
    //   8: getfield 45	net/cloudpath/xpressconnect/CreateNewConfiguration:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   11: getfield 379	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:savedConfigInfo	Lnet/cloudpath/xpressconnect/parsers/SavedConfigInfo;
    //   14: ifnull +16 -> 30
    //   17: aload_0
    //   18: getfield 45	net/cloudpath/xpressconnect/CreateNewConfiguration:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   21: getfield 379	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:savedConfigInfo	Lnet/cloudpath/xpressconnect/parsers/SavedConfigInfo;
    //   24: getfield 393	net/cloudpath/xpressconnect/parsers/SavedConfigInfo:username	Ljava/lang/String;
    //   27: ifnonnull +15 -> 42
    //   30: aload_0
    //   31: getfield 47	net/cloudpath/xpressconnect/CreateNewConfiguration:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   34: ldc_w 571
    //   37: invokestatic 118	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   40: aconst_null
    //   41: areturn
    //   42: aload_0
    //   43: aload_0
    //   44: getfield 45	net/cloudpath/xpressconnect/CreateNewConfiguration:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   47: getfield 379	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:savedConfigInfo	Lnet/cloudpath/xpressconnect/parsers/SavedConfigInfo;
    //   50: getfield 393	net/cloudpath/xpressconnect/parsers/SavedConfigInfo:username	Ljava/lang/String;
    //   53: invokevirtual 574	net/cloudpath/xpressconnect/CreateNewConfiguration:rfc1779and2253escapeString	(Ljava/lang/String;)Ljava/lang/String;
    //   56: astore_1
    //   57: new 576	javax/security/auth/x500/X500Principal
    //   60: dup
    //   61: new 228	java/lang/StringBuilder
    //   64: dup
    //   65: invokespecial 229	java/lang/StringBuilder:<init>	()V
    //   68: ldc_w 578
    //   71: invokevirtual 235	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   74: aload_1
    //   75: invokevirtual 235	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   78: invokevirtual 247	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   81: invokespecial 581	javax/security/auth/x500/X500Principal:<init>	(Ljava/lang/String;)V
    //   84: astore_2
    //   85: new 420	net/cloudpath/xpressconnect/certificates/CertificateFactory
    //   88: dup
    //   89: aload_2
    //   90: aload_0
    //   91: getfield 47	net/cloudpath/xpressconnect/CreateNewConfiguration:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   94: invokespecial 584	net/cloudpath/xpressconnect/certificates/CertificateFactory:<init>	(Ljavax/security/auth/x500/X500Principal;Lnet/cloudpath/xpressconnect/logger/Logger;)V
    //   97: astore_3
    //   98: aload_3
    //   99: invokevirtual 587	net/cloudpath/xpressconnect/certificates/CertificateFactory:generateCSR	()V
    //   102: aload_3
    //   103: areturn
    //   104: astore 7
    //   106: aload 7
    //   108: invokevirtual 588	java/security/InvalidKeyException:printStackTrace	()V
    //   111: aload_0
    //   112: getfield 47	net/cloudpath/xpressconnect/CreateNewConfiguration:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   115: ldc_w 590
    //   118: invokestatic 118	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   121: aload_0
    //   122: getfield 53	net/cloudpath/xpressconnect/CreateNewConfiguration:mFailure	Lnet/cloudpath/xpressconnect/FailureReason;
    //   125: ifnull +36 -> 161
    //   128: aload_0
    //   129: getfield 53	net/cloudpath/xpressconnect/CreateNewConfiguration:mFailure	Lnet/cloudpath/xpressconnect/FailureReason;
    //   132: getstatic 270	net/cloudpath/xpressconnect/FailureReason:useErrorIcon	I
    //   135: aload_0
    //   136: getfield 51	net/cloudpath/xpressconnect/CreateNewConfiguration:mContext	Landroid/content/Context;
    //   139: invokevirtual 274	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   142: aload_0
    //   143: getfield 67	net/cloudpath/xpressconnect/CreateNewConfiguration:mHelp	Lcom/commonsware/cwac/parcel/ParcelHelper;
    //   146: ldc_w 592
    //   149: ldc 203
    //   151: invokevirtual 207	com/commonsware/cwac/parcel/ParcelHelper:getIdentifier	(Ljava/lang/String;Ljava/lang/String;)I
    //   154: invokevirtual 279	android/content/res/Resources:getString	(I)Ljava/lang/String;
    //   157: iconst_2
    //   158: invokevirtual 217	net/cloudpath/xpressconnect/FailureReason:setFailReason	(ILjava/lang/String;I)V
    //   161: aconst_null
    //   162: areturn
    //   163: astore 8
    //   165: aload 8
    //   167: invokevirtual 110	java/lang/IllegalArgumentException:printStackTrace	()V
    //   170: aload_0
    //   171: getfield 47	net/cloudpath/xpressconnect/CreateNewConfiguration:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   174: new 228	java/lang/StringBuilder
    //   177: dup
    //   178: invokespecial 229	java/lang/StringBuilder:<init>	()V
    //   181: ldc_w 594
    //   184: invokevirtual 235	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   187: aload_0
    //   188: getfield 45	net/cloudpath/xpressconnect/CreateNewConfiguration:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   191: getfield 379	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:savedConfigInfo	Lnet/cloudpath/xpressconnect/parsers/SavedConfigInfo;
    //   194: getfield 393	net/cloudpath/xpressconnect/parsers/SavedConfigInfo:username	Ljava/lang/String;
    //   197: invokevirtual 235	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   200: invokevirtual 247	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   203: invokestatic 118	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   206: aconst_null
    //   207: areturn
    //   208: astore 6
    //   210: aload 6
    //   212: invokevirtual 595	java/security/NoSuchAlgorithmException:printStackTrace	()V
    //   215: aload_0
    //   216: getfield 47	net/cloudpath/xpressconnect/CreateNewConfiguration:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   219: ldc_w 597
    //   222: invokestatic 118	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   225: aload_0
    //   226: getfield 53	net/cloudpath/xpressconnect/CreateNewConfiguration:mFailure	Lnet/cloudpath/xpressconnect/FailureReason;
    //   229: ifnull +36 -> 265
    //   232: aload_0
    //   233: getfield 53	net/cloudpath/xpressconnect/CreateNewConfiguration:mFailure	Lnet/cloudpath/xpressconnect/FailureReason;
    //   236: getstatic 270	net/cloudpath/xpressconnect/FailureReason:useErrorIcon	I
    //   239: aload_0
    //   240: getfield 51	net/cloudpath/xpressconnect/CreateNewConfiguration:mContext	Landroid/content/Context;
    //   243: invokevirtual 274	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   246: aload_0
    //   247: getfield 67	net/cloudpath/xpressconnect/CreateNewConfiguration:mHelp	Lcom/commonsware/cwac/parcel/ParcelHelper;
    //   250: ldc_w 592
    //   253: ldc 203
    //   255: invokevirtual 207	com/commonsware/cwac/parcel/ParcelHelper:getIdentifier	(Ljava/lang/String;Ljava/lang/String;)I
    //   258: invokevirtual 279	android/content/res/Resources:getString	(I)Ljava/lang/String;
    //   261: iconst_2
    //   262: invokevirtual 217	net/cloudpath/xpressconnect/FailureReason:setFailReason	(ILjava/lang/String;I)V
    //   265: aconst_null
    //   266: areturn
    //   267: astore 5
    //   269: aload 5
    //   271: invokevirtual 598	java/security/SignatureException:printStackTrace	()V
    //   274: aload_0
    //   275: getfield 47	net/cloudpath/xpressconnect/CreateNewConfiguration:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   278: ldc_w 600
    //   281: invokestatic 118	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   284: aload_0
    //   285: getfield 53	net/cloudpath/xpressconnect/CreateNewConfiguration:mFailure	Lnet/cloudpath/xpressconnect/FailureReason;
    //   288: ifnull +36 -> 324
    //   291: aload_0
    //   292: getfield 53	net/cloudpath/xpressconnect/CreateNewConfiguration:mFailure	Lnet/cloudpath/xpressconnect/FailureReason;
    //   295: getstatic 270	net/cloudpath/xpressconnect/FailureReason:useErrorIcon	I
    //   298: aload_0
    //   299: getfield 51	net/cloudpath/xpressconnect/CreateNewConfiguration:mContext	Landroid/content/Context;
    //   302: invokevirtual 274	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   305: aload_0
    //   306: getfield 67	net/cloudpath/xpressconnect/CreateNewConfiguration:mHelp	Lcom/commonsware/cwac/parcel/ParcelHelper;
    //   309: ldc_w 592
    //   312: ldc 203
    //   314: invokevirtual 207	com/commonsware/cwac/parcel/ParcelHelper:getIdentifier	(Ljava/lang/String;Ljava/lang/String;)I
    //   317: invokevirtual 279	android/content/res/Resources:getString	(I)Ljava/lang/String;
    //   320: iconst_2
    //   321: invokevirtual 217	net/cloudpath/xpressconnect/FailureReason:setFailReason	(ILjava/lang/String;I)V
    //   324: aconst_null
    //   325: areturn
    //   326: astore 4
    //   328: aload 4
    //   330: invokevirtual 601	java/lang/Exception:printStackTrace	()V
    //   333: aload_0
    //   334: getfield 47	net/cloudpath/xpressconnect/CreateNewConfiguration:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   337: ldc_w 603
    //   340: invokestatic 118	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   343: aload_0
    //   344: getfield 53	net/cloudpath/xpressconnect/CreateNewConfiguration:mFailure	Lnet/cloudpath/xpressconnect/FailureReason;
    //   347: ifnull +36 -> 383
    //   350: aload_0
    //   351: getfield 53	net/cloudpath/xpressconnect/CreateNewConfiguration:mFailure	Lnet/cloudpath/xpressconnect/FailureReason;
    //   354: getstatic 270	net/cloudpath/xpressconnect/FailureReason:useErrorIcon	I
    //   357: aload_0
    //   358: getfield 51	net/cloudpath/xpressconnect/CreateNewConfiguration:mContext	Landroid/content/Context;
    //   361: invokevirtual 274	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   364: aload_0
    //   365: getfield 67	net/cloudpath/xpressconnect/CreateNewConfiguration:mHelp	Lcom/commonsware/cwac/parcel/ParcelHelper;
    //   368: ldc_w 592
    //   371: ldc 203
    //   373: invokevirtual 207	com/commonsware/cwac/parcel/ParcelHelper:getIdentifier	(Ljava/lang/String;Ljava/lang/String;)I
    //   376: invokevirtual 279	android/content/res/Resources:getString	(I)Ljava/lang/String;
    //   379: iconst_2
    //   380: invokevirtual 217	net/cloudpath/xpressconnect/FailureReason:setFailReason	(ILjava/lang/String;I)V
    //   383: aconst_null
    //   384: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   98	102	104	java/security/InvalidKeyException
    //   57	85	163	java/lang/IllegalArgumentException
    //   98	102	208	java/security/NoSuchAlgorithmException
    //   98	102	267	java/security/SignatureException
    //   98	102	326	java/lang/Exception
  }

  protected String filterIdentityTagData(String paramString)
  {
    if (paramString == null)
      return null;
    if ((paramString.contains("<identity>")) && (paramString.contains("</identity>")))
      return paramString.replace(paramString.substring(paramString.indexOf("<identity>") + "<identity>".length(), paramString.indexOf("</identity>")), "<identity tag data filtered/>");
    return paramString;
  }

  protected String getCertInstallerPassword()
  {
    String str = "";
    if ((this.mParser == null) || (this.mParser.selectedProfile == null))
    {
      Util.log(this.mLogger, "No parser or profile bound in getCertInstallerPassword()!");
      return str;
    }
    if ((this.mParser.selectedProfile.showPreCreds == 1) && (this.mParser.selectedProfile.getSetting(2, 80035) != null) && (this.mParser.savedConfigInfo != null) && (!Util.stringIsEmpty(this.mParser.savedConfigInfo.password)))
    {
      Util.log(this.mLogger, "Will use user specific password for installing certificates.\n");
      return this.mParser.savedConfigInfo.password;
    }
    if ((Build.VERSION.SDK_INT < 16) || (Build.BRAND.toLowerCase(Locale.ENGLISH).contentEquals("htc")))
    {
      Util.log(this.mLogger, "Using default password of 'password' for installing certificates.\n");
      str = "password";
    }
    return str;
  }

  public int getFailReason()
  {
    return this.failReason;
  }

  protected String[] getMappedFullUrl(String paramString1, String paramString2)
  {
    String[] arrayOfString = paramString1.split("\\?");
    Util.log(this.mLogger, "Attempting to get the user certificate...");
    if (arrayOfString.length <= 1)
    {
      this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_invalid_cert_url", "string")), 2);
      return null;
    }
    try
    {
      arrayOfString[1] = new MapVariables(this.mParser, this.mLogger).varMap(arrayOfString[1], false, this.mWifi);
      arrayOfString[1] = arrayOfString[1].replace("${CSR}", paramString2);
      return arrayOfString;
    }
    catch (Exception localException)
    {
      while (true)
      {
        localException.printStackTrace();
        Util.log(this.mLogger, "Failed to var map post data.  Attempt will probably fail.");
      }
    }
  }

  protected String getUserCertificateUrl()
  {
    if ((this.mParser == null) || (this.mParser.selectedProfile == null))
      Util.log(this.mLogger, "Structures not properly populated in getUserCertificateUrl()!");
    SettingElement localSettingElement;
    do
    {
      return null;
      localSettingElement = this.mParser.selectedProfile.getSetting(0, 61306);
    }
    while (localSettingElement == null);
    return localSettingElement.unenforcedValue;
  }

  protected boolean handlePemUserCert(String paramString)
  {
    String str = CertUtils.stripCertData(paramString, false);
    Util.log(this.mLogger, "Stripped cert : " + str);
    this.mSanPrincipal = new CertUtils(this.mLogger).getSanPrincipalString(str);
    this.mGlobals.setUserCert(str);
    return true;
  }

  protected boolean handlePkcs7UserCert(String paramString)
  {
    String str = CertUtils.stripCertData(paramString, true);
    try
    {
      ConvertPkcs7Certificates localConvertPkcs7Certificates = new ConvertPkcs7Certificates(str);
      if (localConvertPkcs7Certificates.findCertificateByCn(this.mParser.savedConfigInfo.username) != null)
      {
        Util.log(this.mLogger, "Client cert found!");
        return handlePemUserCert(new CertUtils(this.mLogger).getPemCert(localConvertPkcs7Certificates.findCertificateByCn(this.mParser.savedConfigInfo.username)));
      }
    }
    catch (CertificateException localCertificateException)
    {
      Util.log(this.mLogger, "Unable to parse PKCS#7 certificate data to get user certificate.");
      localCertificateException.printStackTrace();
      this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_cant_parse_pkcs7", "string")), 2);
      return false;
    }
    catch (Exception localException)
    {
      Util.log(this.mLogger, "Null data passed in to ConvertPkcs7Certificates ctor.");
      localException.printStackTrace();
      this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_cant_convert_null_pkcs7", "string")), 2);
      return false;
    }
    Util.log(this.mLogger, "Unable to locate a client certificate for this user name.");
    this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_cant_locate_client_cert", "string")) + this.mParser.savedConfigInfo.username, 2);
    return false;
  }

  protected boolean installAllCertificates()
  {
    if (this.mCertInstaller != null)
    {
      Util.log(this.mLogger, "CertInstaller wasn't null in installAllCertificates()!  Something is wrong with the internal state!");
      return false;
    }
    if (this.mGlobals == null)
    {
      Util.log(this.mLogger, "Global context in null in installAllCertificates()!");
      return false;
    }
    this.mCertInstaller = new CertInstaller(this.mLogger, this.mParser, getConfigStruct(), this.mFailure, this.tParent, this.mParent);
    if ((Util.stringIsEmpty(this.mGlobals.getUserCert())) && (this.mCaCertificates.size() <= 0))
    {
      if (new AndroidVersion(this.mLogger).forceOpenKeystore(this.mParser, Build.VERSION.SDK_INT, Build.MANUFACTURER))
        return this.mCertInstaller.forceOpenKeystore();
      Util.log(this.mLogger, "No certificates to install...");
      return true;
    }
    boolean bool;
    if (Util.stringIsEmpty(this.mGlobals.getUserCert()))
      bool = this.mCertInstaller.installCaCerts(this.mCaCertificates, getCertInstallerPassword());
    while (true)
    {
      this.mCaCertPath = this.mCertInstaller.getCaCertAlias();
      this.mUserCertPath = this.mCertInstaller.getUserCertAlias();
      this.mUserPkeyPath = this.mCertInstaller.getPkeyCertAlias();
      this.mNumCaInstalled = this.mCertInstaller.getNumCaInstalled();
      this.mCertInstaller = null;
      return bool;
      if (this.mCaCertificates.size() <= 0)
        bool = this.mCertInstaller.installUserCert(this.mGlobals.getUserCert(), this.mGlobals.getPrivateKey(), getCertInstallerPassword());
      else
        bool = this.mCertInstaller.installAllCerts(this.mCaCertificates, this.mGlobals.getUserCert(), this.mGlobals.getPrivateKey(), getCertInstallerPassword());
    }
  }

  protected boolean installCertificates()
  {
    if ((getUserCertificateUrl() != null) && (!acquireUserCertificate()));
    while ((this.tParent.shouldDie()) || ((needCaCertificates()) && (!acquireCaCertificates())) || (this.tParent.shouldDie()))
      return false;
    return installAllCertificates();
  }

  protected boolean installProfile()
  {
    int i = 0;
    boolean bool = false;
    WifiConfigurationProxy localWifiConfigurationProxy = getConfigStruct();
    if (localWifiConfigurationProxy == null)
      Util.log(this.mLogger, "Couldn't get valid configuration structure!  Can't install the profile!");
    label883: 
    do
    {
      int j;
      do
      {
        do
        {
          do
          {
            return false;
            if ((this.mParser == null) || (this.mParser.savedConfigInfo == null))
            {
              Util.log(this.mLogger, "Parser data is null!");
              return false;
            }
          }
          while (!localWifiConfigurationProxy.setNetworkId(this.mParser.savedConfigInfo.targetNetId));
          if (localWifiConfigurationProxy.getNetworkId() != -1)
            Util.log(this.mLogger, "********  Updating network id " + localWifiConfigurationProxy.getNetworkId() + ".");
          if (!setEssid(localWifiConfigurationProxy))
          {
            Util.log(this.mLogger, "Couldn't write SSID.");
            return false;
          }
          if ((!Util.usingPsk(this.mLogger, this.mParser)) && (!Util.isOpenNetwork(this.mLogger, this.mParser)))
          {
            if (!setOuterId(localWifiConfigurationProxy))
            {
              Util.log(this.mLogger, "Unable to set the outer identity.");
              return false;
            }
            if (!setUsernameAndPassword(localWifiConfigurationProxy))
            {
              Util.log(this.mLogger, "Unable to write username and password.");
              return false;
            }
            if (!setEapMethod(localWifiConfigurationProxy))
            {
              Util.log(this.mLogger, "Unable to set the EAP method.");
              return false;
            }
            if (!setInnerMethod(localWifiConfigurationProxy))
            {
              Util.log(this.mLogger, "Unable to set inner method.");
              return false;
            }
            if (!setRootCA(localWifiConfigurationProxy))
            {
              Util.log(this.mLogger, "Unable to set root CA!");
              return false;
            }
            if (!setUserCert(localWifiConfigurationProxy))
            {
              Util.log(this.mLogger, "Unable to set user cert!");
              return false;
            }
          }
          if (!setProxy(localWifiConfigurationProxy))
          {
            Util.log(this.mLogger, "Unable to configure device proxy settings.");
            return false;
          }
          Util.log(this.mLogger, "Writing the following configuration :");
          Util.log(this.mLogger, localWifiConfigurationProxy.toFilteredString());
          if (this.mWifi == null)
          {
            Util.log(this.mLogger, "The wireless manager is null in installProfile().");
            return false;
          }
          if (localWifiConfigurationProxy.getNetworkId() == -1)
            break;
          int i1 = 0;
          j = -1;
          while (i == 0)
          {
            Util.log(this.mLogger, "Attempting to update the configuration.");
            j = this.mWifi.updateNetwork(localWifiConfigurationProxy.getWifiConfig());
            if (j != localWifiConfigurationProxy.getNetworkId())
            {
              Util.log(this.mLogger, "Unable to update the network profile!  Attempt : " + (i1 + 1) + "  (Result : " + j + ")");
              if (!this.mWifi.isWifiEnabled())
              {
                Util.log(this.mLogger, "Wireless isn't enabled?  (Enabling it.)");
                this.mWifi.setWifiEnabled(true);
              }
              try
              {
                while (true)
                {
                  Thread.sleep(2000L);
                  i1++;
                  if (i1 < 5)
                    break;
                  i = 1;
                  break;
                  Util.log(this.mLogger, "Wireless is enabled.");
                  WifiInfo localWifiInfo = this.mWifi.getConnectionInfo();
                  if (localWifiConfigurationProxy.getNetworkId() == localWifiInfo.getNetworkId())
                  {
                    this.mWifi.disableNetwork(localWifiConfigurationProxy.getNetworkId());
                    bounceWifi();
                    this.mWifi.disableNetwork(localWifiConfigurationProxy.getNetworkId());
                    this.mWifi.disconnect();
                  }
                }
              }
              catch (InterruptedException localInterruptedException3)
              {
                while (true)
                {
                  Util.log(this.mLogger, "Unable to sleep.");
                  localInterruptedException3.printStackTrace();
                }
              }
            }
            else
            {
              Util.log(this.mLogger, "Configuration id = " + j + " has been updated.");
              i = 1;
            }
          }
          if (j == localWifiConfigurationProxy.getNetworkId())
            break label883;
        }
        while (this.mFailure == null);
        this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_cant_update_network", "string")), 6);
        this.failReason = 13;
        return false;
        j = -1;
        int k = 0;
        if (i == 0)
        {
          if (localWifiConfigurationProxy.getWifiConfig() == null)
            Util.log(this.mLogger, "null");
          j = this.mWifi.addNetwork(localWifiConfigurationProxy.getWifiConfig());
          if (j < 0)
          {
            Util.log(this.mLogger, "Unable to create the network profile!");
            if (!this.mWifi.isWifiEnabled())
            {
              Util.log(this.mLogger, "Wireless isn't enabled?  (Will enable.)");
              this.mWifi.setWifiEnabled(true);
            }
          }
          while (true)
          {
            try
            {
              Thread.sleep(2000L);
              k++;
              if (k < 5)
                break;
              i = 1;
              break;
              Util.log(this.mLogger, "Wireless is enabled.");
              continue;
            }
            catch (InterruptedException localInterruptedException2)
            {
              Util.log(this.mLogger, "Can't sleep in config add attempt.");
              localInterruptedException2.printStackTrace();
              continue;
            }
            Util.log(this.mLogger, "Added network configuration.");
            i = 1;
          }
        }
        if (j >= 0)
          break;
      }
      while (this.mFailure == null);
      this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_cant_add_network", "string")), 6);
      this.failReason = 13;
      return false;
      this.mParser.savedConfigInfo.targetNetId = j;
      int m = 0;
      int n = 0;
      if (m == 0)
      {
        Util.log(this.mLogger, "Attempting to save the config.");
        bool = new WifiIoctl(this.mWifi).saveConfig(this.mParent, this.mLogger);
        if (!bool)
          Util.log(this.mLogger, "Unable to save the configuration!");
        while (true)
        {
          try
          {
            Thread.sleep(2000L);
            n++;
            if (n < 5)
              break;
            m = 1;
          }
          catch (InterruptedException localInterruptedException1)
          {
            Util.log(this.mLogger, "Unable to sleep in write failure loop.");
            localInterruptedException1.printStackTrace();
            continue;
          }
          Util.log(this.mLogger, "Configuration saved. (" + j + ")");
          m = 1;
        }
      }
      if (bool)
        break;
      Util.log(this.mLogger, "Unable to save the configuration!");
    }
    while (this.mFailure == null);
    this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_unable_to_save_network", "string")), 6);
    return false;
    Util.log(this.mLogger, "Wrote config successfully.");
    return true;
  }

  protected boolean isGalaxyNexus(String paramString)
  {
    if (paramString == null);
    while ((!paramString.toLowerCase(Locale.ENGLISH).contentEquals("sch-i515")) && (!paramString.toLowerCase(Locale.ENGLISH).contentEquals("sch-i516")) && (!paramString.toLowerCase(Locale.ENGLISH).contentEquals("gt-i9250")) && (!paramString.toLowerCase(Locale.ENGLISH).contentEquals("sph-l700")) && (!paramString.toLowerCase(Locale.ENGLISH).contentEquals("gt-i9250t")) && (!paramString.toLowerCase(Locale.ENGLISH).contentEquals("shw-m420s")) && (!paramString.toLowerCase(Locale.ENGLISH).contentEquals("shw-m420k")) && (!paramString.toLowerCase(Locale.ENGLISH).contentEquals("shw-m420s/k")))
      return false;
    return true;
  }

  protected boolean needCaCertificates()
  {
    if (!Util.usingCaValidation(this.mParser, this.mLogger))
    {
      Util.log(this.mLogger, "Certificate validation is disabled.  Not installing any CA certificates.");
      return false;
    }
    return true;
  }

  protected boolean resultContainsCertificate(String paramString)
  {
    if ((!paramString.contains("-----BEGIN CERTIFICATE-----")) && (!paramString.contains("-----BEGIN PKCS7-----")))
    {
      Util.log(this.mLogger, "Result didn't contain certificate header.");
      if (this.mFailure != null)
      {
        if (Util.stringIsEmpty(paramString))
          paramString = "Server returned an empty response.";
        this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_web_server_cert_fail", "string")) + "  (Error : " + paramString + ")", 2);
      }
    }
    do
    {
      return false;
      if ((paramString.contains("-----END CERTIFICATE-----")) || (paramString.contains("-----END PKCS7-----")))
        break;
      Util.log(this.mLogger, "Result didn't contain certificate footer.");
    }
    while (this.mFailure == null);
    if (Util.stringIsEmpty(paramString))
      paramString = "Server returned an empty response.";
    this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_web_server_cert_fail", "string")) + "  (Error : " + paramString + ")", 2);
    return false;
    String str = checkForIdentity(paramString);
    if (str != null)
      this.mParser.savedConfigInfo.username = str;
    return true;
  }

  public void resume(Activity paramActivity)
  {
    this.mParent = paramActivity;
    this.mContext = paramActivity;
  }

  protected String rfc1779and2253escapeString(String paramString)
  {
    if (paramString == null)
      return null;
    return paramString.replace("\\", "\\\\").replace(",", "\\,").replace("+", "\\+").replace("=", "\\=").replace("\"", "\\\"").replace("<", "\\<").replace(">", "\\>").replace("#", "\\#").replace(";", "\\;");
  }

  protected boolean setEapMethod(WifiConfigurationProxy paramWifiConfigurationProxy)
  {
    if (this.mParser == null)
      Util.log(this.mLogger, "No parser bound in setEapMethod()!");
    SettingElement localSettingElement;
    do
    {
      return false;
      if (this.mParser.selectedProfile == null)
      {
        Util.log(this.mLogger, "No profile selected in setEapMethod()!");
        return false;
      }
      localSettingElement = this.mParser.selectedProfile.getSetting(2, 65001);
      if (localSettingElement != null)
        break;
      Util.log(this.mLogger, "No outer EAP method was defined in the configuration file!");
    }
    while (this.mFailure == null);
    this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_no_eap_method_defined", "string")), 5);
    return false;
    if (paramWifiConfigurationProxy == null)
    {
      Util.log(this.mLogger, "No wireless context available in setEapMethod()!");
      return false;
    }
    if (!paramWifiConfigurationProxy.setEap(localSettingElement.requiredValue))
    {
      Util.log(this.mLogger, "Unable to set EAP method.");
      return false;
    }
    return true;
  }

  protected boolean setEssid(WifiConfigurationProxy paramWifiConfigurationProxy)
  {
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "No parser bound in setEssid()!");
      return false;
    }
    if (this.mParser.selectedProfile == null)
    {
      Util.log(this.mLogger, "No profile selected in setEssid()!");
      return false;
    }
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(1, 40001);
    if (localSettingElement == null)
    {
      Util.log(this.mLogger, "SSID and security element not found in the configuration file!");
      if (this.mFailure != null)
        this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_no_ssid_defined", "string")), 5);
      return false;
    }
    if (localSettingElement.additionalValue == null)
    {
      Util.log(this.mLogger, "No SSID defined in setEssid()!?");
      return false;
    }
    String[] arrayOfString = localSettingElement.additionalValue.split(":");
    if (arrayOfString.length != 3)
    {
      Util.log(this.mLogger, "The SSID element provided in the configuration file is invalid!");
      return false;
    }
    if (!arrayOfString[0].equals("CP"))
    {
      Util.log(this.mLogger, "Invalid signature on the SSID string!");
      return false;
    }
    if ((arrayOfString[2] == null) || (arrayOfString[2].length() <= 0))
    {
      Util.log(this.mLogger, "There isn't enough information about the SSID to build the profile.");
      return false;
    }
    if (!paramWifiConfigurationProxy.setSsid("\"" + arrayOfString[2] + "\""))
    {
      Util.log(this.mLogger, "Unable to set SSID.");
      return false;
    }
    Util.log(this.mLogger, "Using configuration settings : " + arrayOfString[1]);
    String str = arrayOfString[1];
    BitSet localBitSet1 = new BitSet();
    BitSet localBitSet2 = new BitSet();
    switch (Util.parseInt(String.valueOf(str.charAt(4)), 0))
    {
    case 2:
    case 3:
    case 5:
    case 7:
    case 8:
    default:
      Util.log(this.mLogger, "Unknown encryption type : " + String.valueOf(str.charAt(4)));
      return false;
    case 4:
      localBitSet2.set(2);
      localBitSet2.set(1);
      localBitSet2.set(0);
      localBitSet1.set(1);
    case 0:
    case 1:
    case 6:
    case 9:
    }
    while (!Util.isTls(this.mLogger, this.mParser, this.mContext, this.mFailure))
      if (!paramWifiConfigurationProxy.setAllowedGroupCiphers(localBitSet2))
      {
        Util.log(this.mLogger, "Unable to set allowed group ciphers.");
        return false;
        localBitSet2.set(3);
        localBitSet2.set(2);
        localBitSet2.set(1);
        localBitSet2.set(0);
        localBitSet1.set(2);
        continue;
        localBitSet2.set(3);
        localBitSet2.set(2);
        localBitSet2.set(1);
        localBitSet2.set(0);
        localBitSet1.set(2);
        localBitSet1.set(1);
      }
      else
      {
        if (paramWifiConfigurationProxy.setAllowedPairwiseCiphers(localBitSet1))
          break label583;
        Util.log(this.mLogger, "Unable to set pairwise ciphers.");
        return false;
      }
    Util.log(this.mLogger, "Either the pair or group cipher wasn't set!  Not setting any ciphers!");
    label583: BitSet localBitSet3 = new BitSet();
    BitSet localBitSet4 = new BitSet();
    BitSet localBitSet5 = new BitSet();
    switch (Util.parseInt(String.valueOf(str.charAt(7)), 0))
    {
    default:
      Util.log(this.mLogger, "Unknown auth/key managment values!  (Value = " + String.valueOf(str.charAt(7)) + ")");
      this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_invalid_keyauth", "string")), 5);
      return false;
    case 0:
    case 1:
      localBitSet3.set(0);
      localBitSet4.set(0);
    case 2:
    case 5:
    case 3:
    case 6:
    case 9:
    case 4:
    case 7:
    case 8:
    }
    while (!paramWifiConfigurationProxy.setAllowedKeyManagement(localBitSet4))
    {
      Util.log(this.mLogger, "Unable to set allowed key management methods.");
      return false;
      localBitSet3.set(0);
      localBitSet4.set(3);
      continue;
      localBitSet4.set(2);
      localBitSet5.set(1);
      localBitSet5.set(0);
      localBitSet4.set(3);
      continue;
      localBitSet4.set(1);
      localBitSet5.set(1);
      localBitSet5.set(0);
      setPsk(paramWifiConfigurationProxy, localSettingElement.unenforcedValue);
    }
    if (!paramWifiConfigurationProxy.setAllowedAuthAlgorithms(localBitSet3))
    {
      Util.log(this.mLogger, "Unable to set allowed auth algorithms.");
      return false;
    }
    if (!paramWifiConfigurationProxy.setAllowedProtocols(localBitSet5))
    {
      Util.log(this.mLogger, "Unable to set allowed protocols.");
      return false;
    }
    if (str.charAt(5) == '1')
    {
      Util.log(this.mLogger, "****  SSID is marked hidden!");
      this.mParser.savedConfigInfo.isHidden = true;
      if (!paramWifiConfigurationProxy.setHiddenSsid(true))
      {
        Util.log(this.mLogger, "Unable to set hidden SSID.");
        return false;
      }
    }
    else
    {
      if (!paramWifiConfigurationProxy.setHiddenSsid(false))
      {
        Util.log(this.mLogger, "Unable to set hidden SSID.");
        return false;
      }
      this.mParser.savedConfigInfo.isHidden = false;
    }
    Util.log(this.mLogger, "Setting network to priority " + (1 + this.mParser.savedConfigInfo.higestPriority) + ".");
    if (!paramWifiConfigurationProxy.setPriority(1 + this.mParser.savedConfigInfo.higestPriority))
    {
      Util.log(this.mLogger, "Unable to set priority.");
      return false;
    }
    return true;
  }

  protected boolean setInnerMethod(WifiConfigurationProxy paramWifiConfigurationProxy)
  {
    if (this.mParser == null)
      Util.log(this.mLogger, "No parser bound in setInnerMethod()!");
    SettingElement localSettingElement1;
    do
    {
      return false;
      if (this.mParser.selectedProfile == null)
      {
        Util.log(this.mLogger, "No profile selected in setInnerMethod()!");
        return false;
      }
      localSettingElement1 = this.mParser.selectedProfile.getSetting(2, 65001);
      if (localSettingElement1 != null)
        break;
      Util.log(this.mLogger, "No outer EAP method was defined in the configuration file!");
    }
    while (this.mFailure == null);
    this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_no_eap_method_defined", "string")), 5);
    return false;
    if (localSettingElement1.requiredValue == null)
    {
      Util.log(this.mLogger, "No known EAP method to use in setInnerMethod()!");
      return false;
    }
    SettingElement localSettingElement2;
    if (localSettingElement1.requiredValue.toLowerCase(Locale.ENGLISH).equals("peap"))
      localSettingElement2 = this.mParser.selectedProfile.getSetting(2, 65003);
    while (true)
      if (localSettingElement2 == null)
      {
        Util.log(this.mLogger, "No inner method was defined in the configuration file!");
        if (this.mFailure == null)
          break;
        this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_no_inner_auth_defined", "string")), 5);
        return false;
        if (localSettingElement1.requiredValue.toLowerCase(Locale.ENGLISH).equals("ttls"))
        {
          localSettingElement2 = this.mParser.selectedProfile.getSetting(2, 65002);
        }
        else
        {
          boolean bool = localSettingElement1.requiredValue.toLowerCase(Locale.ENGLISH).equals("tls");
          localSettingElement2 = null;
          if (bool)
            return true;
        }
      }
    if (paramWifiConfigurationProxy == null)
    {
      Util.log(this.mLogger, "No wireless context available in setInnerMethod()!");
      return false;
    }
    if (Build.VERSION.SDK_INT >= 8)
    {
      Util.log(this.mLogger, "Using Android 2.2 auth= line.");
      if (!paramWifiConfigurationProxy.setPhase2("auth=" + localSettingElement2.requiredValue))
      {
        Util.log(this.mLogger, "Unable to set phase 2 method.");
        return false;
      }
    }
    else
    {
      Util.log(this.mLogger, "Using Android 2.1 auth= line.");
      if (!paramWifiConfigurationProxy.setPhase2("\"auth=" + localSettingElement2.requiredValue + "\""))
      {
        Util.log(this.mLogger, "Unable to set phase 2 method.");
        return false;
      }
    }
    return true;
  }

  protected boolean setOuterId(WifiConfigurationProxy paramWifiConfigurationProxy)
  {
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "No parser bound in setOuterId()!");
      return false;
    }
    if (this.mParser.selectedProfile == null)
    {
      Util.log(this.mLogger, "No profile selected in setOuterId()!");
      return false;
    }
    if (paramWifiConfigurationProxy == null)
    {
      Util.log(this.mLogger, "No wireless context available in setOuterId()!");
      return false;
    }
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(2, 40015);
    if (localSettingElement == null)
    {
      Util.log(this.mLogger, "Outer ID setting is NOT present!");
      return true;
    }
    Util.log(this.mLogger, "Outer ID setting is present.");
    this.outerId = localSettingElement.additionalValue;
    if (this.outerId.contains("${"))
      Util.log(this.mLogger, "Outer ID is variable mapped : " + this.outerId);
    if (Util.isTls(this.mLogger, this.mParser, this.mContext, this.mFailure))
    {
      Util.log(this.mLogger, "TLS is being used.  Will try to map SAN Principal.");
      if (this.outerId.contains("${SAN_PRINCIPAL}"))
        if (this.mSanPrincipal != null)
          break label282;
    }
    label282: for (this.outerId = this.mParser.savedConfigInfo.username; ; this.outerId = this.outerId.replace("${SAN_PRINCIPAL}", this.mSanPrincipal))
    {
      Util.log(this.mLogger, "Using SAN Principal of : " + this.mSanPrincipal);
      this.mParser.savedConfigInfo.mOuterId = this.outerId;
      if (paramWifiConfigurationProxy.setAnonId(this.outerId))
        break;
      Util.log(this.mLogger, "Couldn't set outer ID.");
      return false;
    }
    return true;
  }

  // ERROR //
  protected boolean setProxy(WifiConfigurationProxy paramWifiConfigurationProxy)
  {
    // Byte code:
    //   0: ldc 88
    //   2: astore_2
    //   3: aload_1
    //   4: ifnonnull +15 -> 19
    //   7: aload_0
    //   8: getfield 47	net/cloudpath/xpressconnect/CreateNewConfiguration:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   11: ldc_w 1161
    //   14: invokestatic 118	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   17: iconst_0
    //   18: ireturn
    //   19: aload_0
    //   20: getfield 45	net/cloudpath/xpressconnect/CreateNewConfiguration:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   23: ifnull +13 -> 36
    //   26: aload_0
    //   27: getfield 45	net/cloudpath/xpressconnect/CreateNewConfiguration:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   30: getfield 144	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:selectedProfile	Lnet/cloudpath/xpressconnect/parsers/config/ProfileElement;
    //   33: ifnonnull +15 -> 48
    //   36: aload_0
    //   37: getfield 47	net/cloudpath/xpressconnect/CreateNewConfiguration:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   40: ldc_w 1163
    //   43: invokestatic 118	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   46: iconst_0
    //   47: ireturn
    //   48: aload_0
    //   49: getfield 45	net/cloudpath/xpressconnect/CreateNewConfiguration:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   52: getfield 144	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:selectedProfile	Lnet/cloudpath/xpressconnect/parsers/config/ProfileElement;
    //   55: iconst_0
    //   56: ldc_w 1164
    //   59: invokevirtual 153	net/cloudpath/xpressconnect/parsers/config/ProfileElement:getSetting	(II)Lnet/cloudpath/xpressconnect/parsers/config/SettingElement;
    //   62: astore_3
    //   63: aload_0
    //   64: getfield 45	net/cloudpath/xpressconnect/CreateNewConfiguration:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   67: getfield 144	net/cloudpath/xpressconnect/parsers/config/NetworkConfigParser:selectedProfile	Lnet/cloudpath/xpressconnect/parsers/config/ProfileElement;
    //   70: iconst_0
    //   71: ldc_w 1165
    //   74: invokevirtual 153	net/cloudpath/xpressconnect/parsers/config/ProfileElement:getSetting	(II)Lnet/cloudpath/xpressconnect/parsers/config/SettingElement;
    //   77: astore 4
    //   79: aload_3
    //   80: ifnonnull +5 -> 85
    //   83: iconst_1
    //   84: ireturn
    //   85: aload 4
    //   87: ifnull +38 -> 125
    //   90: aload 4
    //   92: getfield 980	net/cloudpath/xpressconnect/parsers/config/SettingElement:requiredValue	Ljava/lang/String;
    //   95: ldc_w 1167
    //   98: invokevirtual 188	java/lang/String:contentEquals	(Ljava/lang/CharSequence;)Z
    //   101: ifne +14 -> 115
    //   104: aload 4
    //   106: getfield 1171	net/cloudpath/xpressconnect/parsers/config/SettingElement:isRequired	Ljava/lang/Boolean;
    //   109: invokevirtual 1176	java/lang/Boolean:booleanValue	()Z
    //   112: ifne +13 -> 125
    //   115: getstatic 167	android/os/Build$VERSION:SDK_INT	I
    //   118: bipush 11
    //   120: if_icmpge +5 -> 125
    //   123: iconst_1
    //   124: ireturn
    //   125: getstatic 167	android/os/Build$VERSION:SDK_INT	I
    //   128: bipush 11
    //   130: if_icmpge +39 -> 169
    //   133: aload_0
    //   134: getfield 53	net/cloudpath/xpressconnect/CreateNewConfiguration:mFailure	Lnet/cloudpath/xpressconnect/FailureReason;
    //   137: getstatic 199	net/cloudpath/xpressconnect/FailureReason:useWarningIcon	I
    //   140: aload_0
    //   141: getfield 51	net/cloudpath/xpressconnect/CreateNewConfiguration:mContext	Landroid/content/Context;
    //   144: invokevirtual 274	android/content/Context:getResources	()Landroid/content/res/Resources;
    //   147: aload_0
    //   148: getfield 67	net/cloudpath/xpressconnect/CreateNewConfiguration:mHelp	Lcom/commonsware/cwac/parcel/ParcelHelper;
    //   151: ldc_w 1178
    //   154: ldc 203
    //   156: invokevirtual 207	com/commonsware/cwac/parcel/ParcelHelper:getIdentifier	(Ljava/lang/String;Ljava/lang/String;)I
    //   159: invokevirtual 279	android/content/res/Resources:getString	(I)Ljava/lang/String;
    //   162: bipush 8
    //   164: invokevirtual 217	net/cloudpath/xpressconnect/FailureReason:setFailReason	(ILjava/lang/String;I)V
    //   167: iconst_0
    //   168: ireturn
    //   169: aload_3
    //   170: getfield 980	net/cloudpath/xpressconnect/parsers/config/SettingElement:requiredValue	Ljava/lang/String;
    //   173: iconst_0
    //   174: invokestatic 1031	net/cloudpath/xpressconnect/Util:parseInt	(Ljava/lang/String;I)I
    //   177: ifne +10 -> 187
    //   180: aload_0
    //   181: aload_1
    //   182: iconst_0
    //   183: invokevirtual 1182	net/cloudpath/xpressconnect/CreateNewConfiguration:setProxyState	(Lnet/cloudpath/xpressconnect/nativeproxy/WifiConfigurationProxy;I)Z
    //   186: ireturn
    //   187: aload_3
    //   188: getfield 997	net/cloudpath/xpressconnect/parsers/config/SettingElement:additionalValue	Ljava/lang/String;
    //   191: ldc_w 1001
    //   194: invokevirtual 629	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   197: astore 5
    //   199: aload 5
    //   201: arraylength
    //   202: iconst_2
    //   203: if_icmpge +15 -> 218
    //   206: aload_0
    //   207: getfield 47	net/cloudpath/xpressconnect/CreateNewConfiguration:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   210: ldc_w 1184
    //   213: invokestatic 118	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   216: iconst_0
    //   217: ireturn
    //   218: aload 5
    //   220: iconst_0
    //   221: aaload
    //   222: astore 6
    //   224: aload 5
    //   226: iconst_1
    //   227: aaload
    //   228: bipush 80
    //   230: invokestatic 1031	net/cloudpath/xpressconnect/Util:parseInt	(Ljava/lang/String;I)I
    //   233: istore 7
    //   235: aload_0
    //   236: getfield 47	net/cloudpath/xpressconnect/CreateNewConfiguration:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   239: new 228	java/lang/StringBuilder
    //   242: dup
    //   243: invokespecial 229	java/lang/StringBuilder:<init>	()V
    //   246: ldc_w 1186
    //   249: invokevirtual 235	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   252: aload 6
    //   254: invokevirtual 235	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   257: ldc_w 1188
    //   260: invokevirtual 235	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   263: iload 7
    //   265: invokevirtual 238	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   268: invokevirtual 247	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   271: invokestatic 118	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   274: aload 4
    //   276: ifnull +36 -> 312
    //   279: aload 4
    //   281: getfield 997	net/cloudpath/xpressconnect/parsers/config/SettingElement:additionalValue	Ljava/lang/String;
    //   284: astore_2
    //   285: aload_0
    //   286: getfield 47	net/cloudpath/xpressconnect/CreateNewConfiguration:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   289: new 228	java/lang/StringBuilder
    //   292: dup
    //   293: invokespecial 229	java/lang/StringBuilder:<init>	()V
    //   296: ldc_w 1190
    //   299: invokevirtual 235	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   302: aload_2
    //   303: invokevirtual 235	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   306: invokevirtual 247	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   309: invokestatic 118	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   312: aload_1
    //   313: invokevirtual 1194	net/cloudpath/xpressconnect/nativeproxy/WifiConfigurationProxy:getLinkProperties	()Lnet/cloudpath/xpressconnect/nativeproxy/LinkPropertiesProxy;
    //   316: astore 8
    //   318: aload 8
    //   320: ifnonnull +15 -> 335
    //   323: aload_0
    //   324: getfield 47	net/cloudpath/xpressconnect/CreateNewConfiguration:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   327: ldc_w 1196
    //   330: invokestatic 118	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   333: iconst_0
    //   334: ireturn
    //   335: new 1198	net/cloudpath/xpressconnect/nativeproxy/ProxyPropertiesProxy
    //   338: dup
    //   339: aload 6
    //   341: iload 7
    //   343: aload_2
    //   344: invokespecial 1201	net/cloudpath/xpressconnect/nativeproxy/ProxyPropertiesProxy:<init>	(Ljava/lang/String;ILjava/lang/String;)V
    //   347: astore 9
    //   349: aload 8
    //   351: aload 9
    //   353: invokevirtual 1205	net/cloudpath/xpressconnect/nativeproxy/ProxyPropertiesProxy:getObject	()Ljava/lang/Object;
    //   356: invokevirtual 1211	net/cloudpath/xpressconnect/nativeproxy/LinkPropertiesProxy:setHttpProxy	(Ljava/lang/Object;)V
    //   359: aload_0
    //   360: aload_1
    //   361: iconst_1
    //   362: invokevirtual 1182	net/cloudpath/xpressconnect/CreateNewConfiguration:setProxyState	(Lnet/cloudpath/xpressconnect/nativeproxy/WifiConfigurationProxy;I)Z
    //   365: ireturn
    //   366: astore 16
    //   368: aload 16
    //   370: invokevirtual 122	java/lang/ClassNotFoundException:printStackTrace	()V
    //   373: aload_0
    //   374: getfield 47	net/cloudpath/xpressconnect/CreateNewConfiguration:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   377: ldc_w 1213
    //   380: invokestatic 118	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   383: iconst_0
    //   384: ireturn
    //   385: astore 15
    //   387: aload 15
    //   389: invokevirtual 1214	java/lang/NoSuchMethodException:printStackTrace	()V
    //   392: aload_0
    //   393: getfield 47	net/cloudpath/xpressconnect/CreateNewConfiguration:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   396: ldc_w 1216
    //   399: invokestatic 118	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   402: iconst_0
    //   403: ireturn
    //   404: astore 14
    //   406: aload 14
    //   408: invokevirtual 110	java/lang/IllegalArgumentException:printStackTrace	()V
    //   411: aload_0
    //   412: getfield 47	net/cloudpath/xpressconnect/CreateNewConfiguration:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   415: ldc_w 1218
    //   418: invokestatic 118	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   421: iconst_0
    //   422: ireturn
    //   423: astore 13
    //   425: aload 13
    //   427: invokevirtual 1214	java/lang/NoSuchMethodException:printStackTrace	()V
    //   430: aload_0
    //   431: getfield 47	net/cloudpath/xpressconnect/CreateNewConfiguration:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   434: ldc_w 1216
    //   437: invokestatic 118	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   440: iconst_0
    //   441: ireturn
    //   442: astore 12
    //   444: aload 12
    //   446: invokevirtual 125	java/lang/IllegalAccessException:printStackTrace	()V
    //   449: aload_0
    //   450: getfield 47	net/cloudpath/xpressconnect/CreateNewConfiguration:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   453: ldc_w 1220
    //   456: invokestatic 118	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   459: iconst_0
    //   460: ireturn
    //   461: astore 11
    //   463: aload 11
    //   465: invokevirtual 1221	java/lang/reflect/InvocationTargetException:printStackTrace	()V
    //   468: aload_0
    //   469: getfield 47	net/cloudpath/xpressconnect/CreateNewConfiguration:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   472: ldc_w 1223
    //   475: invokestatic 118	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   478: iconst_0
    //   479: ireturn
    //   480: astore 10
    //   482: aload 10
    //   484: invokevirtual 122	java/lang/ClassNotFoundException:printStackTrace	()V
    //   487: aload_0
    //   488: getfield 47	net/cloudpath/xpressconnect/CreateNewConfiguration:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   491: ldc_w 1225
    //   494: invokestatic 118	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   497: iconst_0
    //   498: ireturn
    //
    // Exception table:
    //   from	to	target	type
    //   335	349	366	java/lang/ClassNotFoundException
    //   335	349	385	java/lang/NoSuchMethodException
    //   349	359	404	java/lang/IllegalArgumentException
    //   349	359	423	java/lang/NoSuchMethodException
    //   349	359	442	java/lang/IllegalAccessException
    //   349	359	461	java/lang/reflect/InvocationTargetException
    //   349	359	480	java/lang/ClassNotFoundException
  }

  protected boolean setProxyState(WifiConfigurationProxy paramWifiConfigurationProxy, int paramInt)
  {
    if (paramWifiConfigurationProxy == null)
    {
      Util.log(this.mLogger, "WifiConfigProxy is null in setProxyState()!");
      return false;
    }
    if (!paramWifiConfigurationProxy.setProxyState(paramInt))
    {
      Util.log(this.mLogger, "Unable to set proxy state.");
      return false;
    }
    return true;
  }

  protected void setPsk(WifiConfigurationProxy paramWifiConfigurationProxy, String paramString)
  {
    if (paramWifiConfigurationProxy == null)
      Util.log(this.mLogger, "No wireless context available in setPsk()!");
    do
    {
      return;
      if (paramString == null)
      {
        Util.log(this.mLogger, "unenforced value is NULL!");
        return;
      }
    }
    while (paramWifiConfigurationProxy.setPsk(Encode.xpcDeobsfucate(paramString)));
    Util.log(this.mLogger, "Unable to set PSK.");
  }

  protected boolean setRootCA(WifiConfigurationProxy paramWifiConfigurationProxy)
  {
    if (this.mParser == null)
      Util.log(this.mLogger, "No parser bound in setRootCA()!");
    label214: 
    do
    {
      do
      {
        return false;
        if (this.mParser.selectedProfile == null)
        {
          Util.log(this.mLogger, "No profile selected in setRootCA()!");
          return false;
        }
        if (paramWifiConfigurationProxy == null)
        {
          Util.log(this.mLogger, "No wireless configuration context available in setRootCA()!");
          return false;
        }
        List localList1 = this.mParser.selectedProfile.getSettingMulti(0, 61301);
        List localList2 = this.mParser.selectedProfile.getSettingMulti(0, 61308);
        if (localList2 != null)
          localList1.addAll(localList2);
        if (!Util.usingCaValidation(this.mParser, this.mLogger))
        {
          Util.log(this.mLogger, "No certificate should be used.  Skipping that part of the configuration.");
          paramWifiConfigurationProxy.setCaCert("");
          return true;
        }
        if ((localList1 != null) && (localList1.size() > 0))
          break label214;
        if (Util.usingCaValidation(this.mParser, this.mLogger) != true)
          break;
        Util.log(this.mLogger, "No CA certificate was defined in the configuration file!");
      }
      while (this.mFailure == null);
      this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_ca_not_defined_but_needed", "string")), 5);
      return false;
      return true;
      if ((!Util.stringIsEmpty(this.mCaCertPath)) || (Build.VERSION.SDK_INT >= AndroidApiLevels.ANDROID_4_3))
        break;
      Util.log(this.mLogger, "CA certificate path was empty or null.  Cannot continue!");
    }
    while (this.mFailure == null);
    this.mFailure.setFailReason(FailureReason.useErrorIcon, "An internal error occurred while storing the CA certificate data.  Please tap the menu button and select 'E-mail log...'.", 6);
    return false;
    if (!paramWifiConfigurationProxy.setCaCert(this.mCaCertPath))
    {
      Util.log(this.mLogger, "Unable to set CA certificate.");
      return false;
    }
    if ((this.mCaCertPath != null) && (!this.mCaCertPath.toLowerCase(Locale.ENGLISH).startsWith("keystore://")))
    {
      if (!new File(this.mCaCertPath).exists())
      {
        Util.log(this.mLogger, "Certificate doesn't exist in the location it was stored!!!!");
        Util.log(this.mLogger, "CA path : " + this.mCaCertPath);
        return false;
      }
      PEMValidator localPEMValidator = new PEMValidator(this.mLogger, Util.readFile(this.mCaCertPath));
      localPEMValidator.setExpectedCerts(this.mNumCaInstalled);
      if (!localPEMValidator.isValid())
      {
        Util.log(this.mLogger, "************************ CERTIFICATE DATA FAILED VALIDATION!");
        Util.log(this.mLogger, "Data : \n" + Util.readFile(this.mCaCertPath));
        this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_internal_cert_validation_failed", "string")), 2);
        return false;
      }
    }
    return true;
  }

  protected boolean setUserCert(WifiConfigurationProxy paramWifiConfigurationProxy)
  {
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "The parser is not defined in setUserCert()!");
      return false;
    }
    if (this.mParser.selectedProfile == null)
    {
      Util.log(this.mLogger, "No profile selected in setUserCert()!");
      return false;
    }
    if (paramWifiConfigurationProxy == null)
    {
      Util.log(this.mLogger, "No wireless configuration proxy available in setUserCert().");
      return false;
    }
    if (!Util.isTls(this.mLogger, this.mParser, this.mContext, this.mFailure))
    {
      if (Testing.areTesting)
        return true;
      if (!paramWifiConfigurationProxy.setClientCert(""))
      {
        Util.log(this.mLogger, "Unable to set client cert.");
        return false;
      }
      if (!paramWifiConfigurationProxy.setClientPrivateKey(""))
      {
        Util.log(this.mLogger, "Unable to set client private key.");
        return false;
      }
      return true;
    }
    if ((this.mUserCertPath == null) || (this.mUserPkeyPath == null))
      Util.log(this.mLogger, "Either the user cert or pkey paths were null.  (If using Wifi API 2 this may be okay.)");
    if (!paramWifiConfigurationProxy.setClientCert(this.mUserCertPath))
    {
      Util.log(this.mLogger, "Unable to set client cert.");
      return false;
    }
    if (!paramWifiConfigurationProxy.setClientPrivateKey(this.mUserPkeyPath))
    {
      Util.log(this.mLogger, "Unable to set client private key.");
      return false;
    }
    return true;
  }

  protected boolean setUsernameAndPassword(WifiConfigurationProxy paramWifiConfigurationProxy)
  {
    if (paramWifiConfigurationProxy == null)
      Util.log(this.mLogger, "WifiConfigProxy is null in setUsernameAndPassword()!");
    label379: label510: 
    do
    {
      return false;
      if (this.mParser == null)
      {
        Util.log(this.mLogger, "No parser bound in setUsernameAndPassword()!");
        return false;
      }
      if (this.mParser.savedConfigInfo == null)
      {
        Util.log(this.mLogger, "There was no username and password information saved!?  This shouldn't happen.");
        return false;
      }
      if ((this.mParser.savedConfigInfo.username == null) || (this.mParser.savedConfigInfo.username.length() <= 0))
      {
        if ((this.mParser.selectedNetwork.defaultUsername != null) && (this.mParser.selectedNetwork.defaultUsername.length() > 0))
          this.mParser.savedConfigInfo.username = this.mParser.selectedNetwork.defaultUsername;
      }
      else if ((this.mParser.savedConfigInfo.password == null) || (this.mParser.savedConfigInfo.password.length() <= 0))
      {
        if ((this.mParser.selectedNetwork.defaultPassword == null) || (this.mParser.selectedNetwork.defaultPassword.length() <= 0))
          break label379;
        this.mParser.savedConfigInfo.password = Encode.xpcDeobsfucate(this.mParser.selectedNetwork.defaultPassword);
      }
      while (true)
        if (Build.VERSION.SDK_INT >= 8)
        {
          try
          {
            if ((!Util.isTls(this.mLogger, this.mParser, this.mContext, this.mFailure)) || (this.mParser.savedConfigInfo.mOuterId == null))
              break label481;
            if (paramWifiConfigurationProxy.setId(this.mParser.savedConfigInfo.mOuterId))
              break label510;
            Util.log(this.mLogger, "Unable to set identity.");
            return false;
          }
          catch (NullPointerException localNullPointerException2)
          {
            Util.log(this.mLogger, "Null pointer exception writing username or password.");
            localNullPointerException2.printStackTrace();
          }
          if (!Build.BOARD.contentEquals("OpusOne"))
            break;
          this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_device_lacking", "string")), 9);
          return false;
          Util.log(this.mLogger, "No username was defined.  Can't continue.");
          this.mFailure.setFailReason(FailureReason.useErrorIcon, "No user name was defined.  Unable to continue.", 5);
          return false;
          if (this.mParser.selectedProfile == null)
          {
            Util.log(this.mLogger, "No profile selected in setUsernameAndPassword()!");
            return false;
          }
          SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(2, 65001);
          if ((localSettingElement != null) && (localSettingElement.requiredValue != null) && (localSettingElement.requiredValue.contentEquals("TLS")))
          {
            Util.log(this.mLogger, "Doing TLS, no password necessarily needed.");
            this.mParser.savedConfigInfo.password = "whatever";
          }
          else
          {
            this.mParser.savedConfigInfo.password = "";
            continue;
            if (!paramWifiConfigurationProxy.setId(this.mParser.savedConfigInfo.username))
            {
              Util.log(this.mLogger, "Unable to set identity.");
              return false;
            }
            if (this.mParser.savedConfigInfo.password == null)
              break label747;
            Util.log(this.mLogger, "Password is set.");
            if (paramWifiConfigurationProxy.setPassword(this.mParser.savedConfigInfo.password))
              break label747;
            Util.log(this.mLogger, "Unable to set password.");
            return false;
          }
        }
      try
      {
        if (!Util.isTls(this.mLogger, this.mParser, this.mContext, this.mFailure))
          break;
        if (paramWifiConfigurationProxy.setId(this.mParser.savedConfigInfo.mOuterId))
          break label705;
        Util.log(this.mLogger, "Failed to set identity.");
        return false;
      }
      catch (NullPointerException localNullPointerException1)
      {
        Util.log(this.mLogger, "Null pointer exception writing username or password.");
        localNullPointerException1.printStackTrace();
      }
    }
    while (!Build.BOARD.contentEquals("OpusOne"));
    label481: this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_device_lacking", "string")), 9);
    return false;
    if (!paramWifiConfigurationProxy.setId(this.mParser.savedConfigInfo.username))
    {
      Util.log(this.mLogger, "Failed to set identity.");
      return false;
    }
    label705: if ((this.mParser.savedConfigInfo.password != null) && (!paramWifiConfigurationProxy.setPassword(this.mParser.savedConfigInfo.password)))
    {
      Util.log(this.mLogger, "Failed to set password.");
      return false;
    }
    label747: return true;
  }
}