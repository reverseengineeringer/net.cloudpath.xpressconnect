package net.cloudpath.xpressconnect.certificates.installers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Build.VERSION;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.util.ArrayList;
import net.cloudpath.xpressconnect.AndroidApiLevels;
import net.cloudpath.xpressconnect.AndroidVersion;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.certificates.PEMValidator;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkItemElement;
import net.cloudpath.xpressconnect.screens.ScreenBase;
import net.cloudpath.xpressconnect.thread.ConfigureClientThread;
import net.cloudpath.xpressconnect.thread.ResponseThreadComCallbacks;

public class SystemInstallIntentMethod extends CertInstallerBase
  implements ResponseThreadComCallbacks
{
  protected static final int INSTALL_TYPE_CA = 0;
  protected static final int INSTALL_TYPE_PKCS12_CA_ONLY = 3;
  protected static final int INSTALL_TYPE_PKCS12_FULL_BUNDLE = 2;
  protected static final int INSTALL_TYPE_PKCS12_USER_ONLY = 4;
  protected static final int INSTALL_TYPE_USER = 1;
  protected String mCaCertPath = null;
  protected boolean mCertInstalled = false;
  protected int mCertNameMethod = 0;
  protected Context mContext = null;
  protected FailureReason mFailure = null;
  protected String mFinalCertName = null;
  protected ParcelHelper mHelp = null;
  protected String mUserCertPath = null;
  protected String mUserPkeyPath = null;

  public SystemInstallIntentMethod(Logger paramLogger, NetworkConfigParser paramNetworkConfigParser, FailureReason paramFailureReason, ConfigureClientThread paramConfigureClientThread, Context paramContext, Activity paramActivity)
  {
    super(paramLogger, paramNetworkConfigParser, paramConfigureClientThread, paramFailureReason, paramActivity);
    this.mFailure = paramFailureReason;
    this.mContext = paramContext;
    this.mHelp = new ParcelHelper("", this.mContext);
  }

  @SuppressLint({"DefaultLocale"})
  private boolean cantUseSystemInstallIntent(String paramString, int paramInt)
  {
    if (paramString == null)
      Util.log(this.mLogger, "Manufacturer was null in cantUseSystemInstallIntent()!");
    while ((!paramString.toLowerCase().contentEquals("motorola")) || (paramInt < AndroidApiLevels.ANDROID_2_3_3_UP))
      return false;
    return true;
  }

  @SuppressLint({"DefaultLocale"})
  protected boolean blacklistedDevice(String paramString1, String paramString2, int paramInt)
  {
    if ((paramString1 == null) || (paramString2 == null))
    {
      Util.log(this.mLogger, "Board or model was null in blacklisted device check!");
      return false;
    }
    if ((paramString1.toLowerCase().contentEquals("shooter")) && (paramInt == AndroidApiLevels.ANDROID_2_3_3_UP))
    {
      Util.log(this.mLogger, "Running on HTC shooter (EVO 3D).  Will use special cert handling.");
      return true;
    }
    if ((paramString1.toLowerCase().contentEquals("gt-i9100")) && (paramInt == AndroidApiLevels.ANDROID_2_3_3_UP))
    {
      Util.log(this.mLogger, "Running on Samsung GT-I9100.  We will use special cert handling.");
      return true;
    }
    if ((paramString1.toLowerCase().contentEquals("m860")) && (paramInt == AndroidApiLevels.ANDROID_2_1))
    {
      Util.log(this.mLogger, "Running on Huawei M860.  We will use special cert handling.");
      return true;
    }
    if (paramString2.toLowerCase().startsWith("upc300"))
    {
      Util.log(this.mLogger, "Running on Viewsonic gTablet.  We will use special cert handling.");
      return true;
    }
    if (paramString2.toLowerCase().startsWith("gtablet"))
    {
      Util.log(this.mLogger, "Running on Viewsonic gTablet.  We will use special cert handling.");
      return true;
    }
    return false;
  }

  protected void buildCaCertName(String paramString)
  {
    if (this.mCertNameMethod == 1)
      this.mCaCertPath = ("keystore://CACERT_" + this.mFinalCertName);
    while (true)
    {
      Util.log(this.mLogger, "Certificate installed : " + this.mCaCertPath);
      return;
      if (this.mFinalCertName != null)
      {
        String str = splitPrefix(this.mFinalCertName);
        this.mCaCertPath = ("keystore://CACERT_" + str);
      }
      else
      {
        this.mCaCertPath = ("keystore://CACERT_" + paramString);
      }
    }
  }

  protected boolean buildUserCertName(String paramString)
  {
    if (this.mCertNameMethod == 1)
    {
      this.mUserCertPath = ("keystore://USRCERT_" + this.mFinalCertName);
      this.mUserPkeyPath = ("keystore://USRPKEY_" + this.mFinalCertName);
      return true;
    }
    if (this.mFinalCertName != null)
    {
      String str = splitPrefix(this.mFinalCertName);
      this.mUserCertPath = ("keystore://USRCERT_" + str);
      this.mUserPkeyPath = ("keystore://USRPKEY_" + str);
      return true;
    }
    this.mUserCertPath = ("keystore://USRCERT_" + paramString);
    this.mUserPkeyPath = ("keystore://USRPKEY_" + paramString);
    return true;
  }

  public boolean canInstallCa()
  {
    return canInstallCa(Build.VERSION.SDK_INT);
  }

  protected boolean canInstallCa(int paramInt)
  {
    if (blacklistedDevice(Build.BOARD, Build.MODEL, Build.VERSION.SDK_INT) == true)
      Util.log(this.mLogger, "Disallowing keystore installs because device is on the black list.");
    do
    {
      return false;
      if (cantUseSystemInstallIntent(Build.MANUFACTURER, Build.VERSION.SDK_INT) == true)
      {
        Util.log(this.mLogger, "Disallowing direct install because device doesn't allow it.");
        return false;
      }
    }
    while ((disallowKeystoreUse()) || (Util.isTls(this.mLogger, this.mParser, this.mContext, this.mFailure)) || (paramInt > AndroidApiLevels.ANDROID_2_3_3_UP));
    return true;
  }

  public boolean canInstallMultiCa()
  {
    return canInstallCa(Build.VERSION.SDK_INT);
  }

  public boolean canInstallUser()
  {
    return canInstallCa(Build.VERSION.SDK_INT);
  }

  @SuppressLint({"DefaultLocale"})
  protected void dumpLogCat()
  {
    try
    {
      ArrayList localArrayList = new ArrayList();
      localArrayList.add("logcat");
      localArrayList.add("-d");
      localArrayList.add("-s");
      if ((Build.VERSION.SDK_INT >= 8) && (!Build.BRAND.toLowerCase().contentEquals("samsung")))
        localArrayList.add("-t");
      localArrayList.add("CredentialInstaller:*");
      localArrayList.add("CertInstaller:*");
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec((String[])localArrayList.toArray(new String[0])).getInputStream()));
      while (true)
      {
        String str = localBufferedReader.readLine();
        if (str == null)
          break;
        processCertInstallLine(str);
        processCredentialInstallLine(str);
      }
    }
    catch (IOException localIOException)
    {
      Util.log(this.mLogger, "I/O Exception : " + localIOException.getMessage());
    }
  }

  protected boolean finishCertInstall(Intent paramIntent, int paramInt, String paramString)
  {
    this.mIntentResult = -1;
    ScreenBase localScreenBase = (ScreenBase)this.mParent;
    if (localScreenBase == null)
      Util.log(this.mLogger, "Unable to notify parent that we want a callback when a cert activity happens!");
    label205: 
    do
    {
      do
      {
        do
          while (true)
          {
            return false;
            localScreenBase.setResponseCallbacks(this);
            try
            {
              this.mThread.spinLock();
              Util.log(this.mLogger, "Starting install certificate intent.");
              this.mParent.startActivityForResult(paramIntent, 1002);
              this.mThread.unlock();
              if (waitForIntentReturn())
                break label205;
              if (this.mFailure != null)
              {
                this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_cert_install_timeout", "string")), 2);
                return false;
              }
            }
            catch (Exception localException)
            {
              Util.log(this.mLogger, "Exception : " + localException.getMessage());
              this.mThread.unlock();
            }
          }
        while (this.mFailure == null);
        this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_unable_to_install_cert", "string")), 2);
        return false;
        localScreenBase.setResponseCallbacks(null);
        if (Build.VERSION.SDK_INT > 15)
          break label303;
        dumpLogCat();
        if (this.mCertInstalled)
          break;
        Util.log(this.mLogger, "Unable to install the certificate.");
      }
      while (this.mFailure == null);
      this.mFailure.setFailReason(FailureReason.useErrorIcon, "Unable to install the certificate(s) needed by this network.", 2);
      return false;
      if (this.mFinalCertName != null)
        break label376;
      Util.log(this.mLogger, "Certificate claimed to be installed, but we didn't get a cert name back?");
    }
    while (this.mFailure == null);
    this.mFailure.setFailReason(FailureReason.useErrorIcon, "The attempt to install the certificate was successful, but no certificate name was returned.  Unable to continue.", 2);
    return false;
    label303: if (Build.VERSION.SDK_INT >= 14)
    {
      if (this.mIntentResult != 1)
      {
        Util.log(this.mLogger, "PKCS#12 certificate install attempt either failed, or was cancelled.");
        this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_certificate_cancelled", "string")), 13);
        return false;
      }
    }
    else
      Util.log(this.mLogger, "Device is older than 4.0, can't trust the result codes.  Moving on.");
    switch (paramInt)
    {
    default:
      label376: Util.log(this.mLogger, "ERROR : Certificate install type is unknown!  The certificate name not be named properly!");
    case 0:
    case 3:
    case 1:
    case 4:
    case 2:
    }
    do
    {
      do
        while (true)
        {
          return true;
          buildCaCertName(paramString);
        }
      while (buildUserCertName(paramString));
      return false;
      buildCaCertName(paramString);
    }
    while (buildUserCertName(paramString));
    return false;
  }

  public String getCaAlias()
  {
    return this.mCaCertPath;
  }

  // ERROR //
  protected String getClientPrivateKey(PrivateKey paramPrivateKey)
  {
    // Byte code:
    //   0: new 376	java/io/StringWriter
    //   3: dup
    //   4: invokespecial 377	java/io/StringWriter:<init>	()V
    //   7: astore_2
    //   8: new 379	org/bouncycastle2/openssl/PEMWriter
    //   11: dup
    //   12: aload_2
    //   13: invokespecial 382	org/bouncycastle2/openssl/PEMWriter:<init>	(Ljava/io/Writer;)V
    //   16: astore_3
    //   17: aload_3
    //   18: aload_1
    //   19: invokevirtual 386	org/bouncycastle2/openssl/PEMWriter:writeObject	(Ljava/lang/Object;)V
    //   22: aload_3
    //   23: invokevirtual 389	org/bouncycastle2/openssl/PEMWriter:close	()V
    //   26: aload_2
    //   27: invokevirtual 390	java/io/StringWriter:close	()V
    //   30: aload_2
    //   31: invokevirtual 391	java/io/StringWriter:toString	()Ljava/lang/String;
    //   34: areturn
    //   35: astore 10
    //   37: aload_0
    //   38: getfield 70	net/cloudpath/xpressconnect/certificates/installers/SystemInstallIntentMethod:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   41: ldc_w 393
    //   44: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   47: aload 10
    //   49: invokevirtual 396	java/lang/SecurityException:printStackTrace	()V
    //   52: ldc 58
    //   54: areturn
    //   55: astore 9
    //   57: aload_0
    //   58: getfield 70	net/cloudpath/xpressconnect/certificates/installers/SystemInstallIntentMethod:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   61: ldc_w 398
    //   64: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   67: aload 9
    //   69: invokevirtual 399	java/lang/IllegalArgumentException:printStackTrace	()V
    //   72: ldc 58
    //   74: areturn
    //   75: astore 4
    //   77: aload_0
    //   78: getfield 70	net/cloudpath/xpressconnect/certificates/installers/SystemInstallIntentMethod:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   81: new 128	java/lang/StringBuilder
    //   84: dup
    //   85: invokespecial 131	java/lang/StringBuilder:<init>	()V
    //   88: ldc_w 339
    //   91: invokevirtual 137	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   94: aload 4
    //   96: invokevirtual 340	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   99: invokevirtual 137	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   102: ldc_w 401
    //   105: invokevirtual 137	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   108: invokevirtual 140	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   111: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   114: aload 4
    //   116: invokevirtual 402	java/lang/Exception:printStackTrace	()V
    //   119: aload_3
    //   120: invokevirtual 389	org/bouncycastle2/openssl/PEMWriter:close	()V
    //   123: ldc 58
    //   125: areturn
    //   126: astore 5
    //   128: aload 5
    //   130: invokevirtual 403	java/io/IOException:printStackTrace	()V
    //   133: goto -10 -> 123
    //   136: astore 7
    //   138: aload_0
    //   139: getfield 70	net/cloudpath/xpressconnect/certificates/installers/SystemInstallIntentMethod:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   142: ldc_w 405
    //   145: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   148: aload 7
    //   150: invokevirtual 403	java/io/IOException:printStackTrace	()V
    //   153: ldc 58
    //   155: areturn
    //   156: astore 6
    //   158: aload_0
    //   159: getfield 70	net/cloudpath/xpressconnect/certificates/installers/SystemInstallIntentMethod:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   162: new 128	java/lang/StringBuilder
    //   165: dup
    //   166: invokespecial 131	java/lang/StringBuilder:<init>	()V
    //   169: ldc_w 407
    //   172: invokevirtual 137	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   175: aload 6
    //   177: invokevirtual 340	java/lang/Exception:getMessage	()Ljava/lang/String;
    //   180: invokevirtual 137	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   183: ldc_w 409
    //   186: invokevirtual 137	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   189: invokevirtual 140	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   192: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   195: aload 6
    //   197: invokevirtual 402	java/lang/Exception:printStackTrace	()V
    //   200: ldc 58
    //   202: areturn
    //   203: astore 8
    //   205: aload_0
    //   206: getfield 70	net/cloudpath/xpressconnect/certificates/installers/SystemInstallIntentMethod:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   209: ldc_w 411
    //   212: invokestatic 78	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   215: aload 8
    //   217: invokevirtual 403	java/io/IOException:printStackTrace	()V
    //   220: ldc 58
    //   222: areturn
    //
    // Exception table:
    //   from	to	target	type
    //   8	17	35	java/lang/SecurityException
    //   8	17	55	java/lang/IllegalArgumentException
    //   17	22	75	java/lang/Exception
    //   119	123	126	java/io/IOException
    //   22	26	136	java/io/IOException
    //   22	26	156	java/lang/Exception
    //   26	30	203	java/io/IOException
  }

  protected String getFormattedUserCert()
  {
    return getFormattedUserCert(Build.VERSION.SDK_INT);
  }

  protected String getFormattedUserCert(int paramInt)
  {
    if (this.mUserCertPath == null)
      return null;
    if (this.mUserCertPath.startsWith("USRPKEY_"))
    {
      Util.log(this.mLogger, "Fixing up certificate name.");
      this.mUserCertPath = this.mUserCertPath.replace("USRPKEY_", "USRCERT_");
    }
    String str;
    if (paramInt >= 18)
    {
      Util.log(this.mLogger, "Using Android 4.3+ cert format.");
      this.mUserCertPath = this.mUserCertPath.replace("keystore://", "");
      this.mUserCertPath = this.mUserCertPath.replace("USRCERT_", "");
      str = this.mUserCertPath;
    }
    while (true)
    {
      return str;
      if (paramInt >= 8)
      {
        Util.log(this.mLogger, "Using Android 2.2+ cert format.");
        str = this.mUserCertPath;
      }
      else
      {
        Util.log(this.mLogger, "Using Android 2.1 cert format.");
        str = "\"" + this.mUserCertPath + "\"";
      }
    }
  }

  protected String getFormattedUserPKey()
  {
    return getFormattedUserPKey(Build.VERSION.SDK_INT);
  }

  protected String getFormattedUserPKey(int paramInt)
  {
    if (this.mUserPkeyPath == null)
      return null;
    if (this.mUserPkeyPath.startsWith("USRCERT_"))
    {
      Util.log(this.mLogger, "Fixing up private key name.");
      this.mUserPkeyPath = this.mUserPkeyPath.replace("USRCERT_", "USRPKEY_");
    }
    String str;
    if (paramInt >= AndroidApiLevels.ANDROID_4_3)
    {
      Util.log(this.mLogger, "Using Android 4.3+ cert format.");
      this.mUserCertPath = this.mUserCertPath.replace("keystore://", "");
      this.mUserCertPath = this.mUserCertPath.replace("USRPKEY_", "");
      str = this.mUserCertPath;
    }
    while (true)
    {
      return str;
      if (paramInt >= AndroidApiLevels.ANDROID_2_2)
      {
        Util.log(this.mLogger, "Using Android 2.2+ cert format.");
        str = this.mUserPkeyPath;
      }
      else
      {
        Util.log(this.mLogger, "Using Android 2.1 cert format.");
        str = "\"" + this.mUserPkeyPath + "\"";
      }
    }
  }

  public String getInstallTypeName()
  {
    return "Using System certificate install method.";
  }

  public String getPrivateKeyAlias()
  {
    return getFormattedUserPKey();
  }

  public String getUserAlias()
  {
    return getFormattedUserCert();
  }

  public String getWarningString()
  {
    return "<font color='blue'>" + this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_no_rerun_need", "string")) + "</font>";
  }

  public boolean installCaCert(String paramString, int paramInt, AndroidVersion paramAndroidVersion)
  {
    if (paramString == null)
      Util.log(this.mLogger, "No certificates available in system certificate installer.");
    Intent localIntent;
    PEMValidator localPEMValidator;
    do
    {
      do
      {
        return false;
        if ((this.mParser == null) || (this.mParser.selectedNetwork == null) || (this.mParser.selectedNetwork.name == null))
        {
          Util.log(this.mLogger, "Network configuration is missing, or network name is invalid in cert system installer.");
          return false;
        }
      }
      while (!checkKeyStoreState());
      resetCertCatcher();
      localIntent = new Intent("android.credentials.SYSTEM_INSTALL");
      localPEMValidator = new PEMValidator(this.mLogger, paramString);
      localPEMValidator.setExpectedCerts(paramInt);
      if ((localPEMValidator.isValid()) || (localPEMValidator.attemptToFix()))
        break;
      Util.log(this.mLogger, "Certificate validation failed, and could not be repaired.");
    }
    while (this.mFailure == null);
    this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_cant_load_ca_cert", "string")), 2);
    return false;
    localIntent.putExtra("CACERT_" + sanitize(this.mParser.selectedNetwork.name), localPEMValidator.getAsBytes());
    Util.log(this.mLogger, "Using pre-3.0 cert install method.");
    return finishCertInstall(localIntent, 0, sanitize(this.mParser.selectedNetwork.name));
  }

  public boolean installClientCert(String paramString, PrivateKey paramPrivateKey, AndroidVersion paramAndroidVersion)
  {
    if ((paramString == null) || (paramPrivateKey == null))
      Util.log(this.mLogger, "No certificate/private key available in user system certificate installer.");
    do
    {
      return false;
      if ((this.mParser == null) || (this.mParser.selectedNetwork == null) || (this.mParser.selectedNetwork.name == null))
      {
        Util.log(this.mLogger, "Network configuration is missing, or network name is invalid in user cert system installer.");
        return false;
      }
    }
    while (!checkKeyStoreState());
    resetCertCatcher();
    Intent localIntent = new Intent("android.credentials.SYSTEM_INSTALL");
    String str = getClientPrivateKey(paramPrivateKey);
    localIntent.putExtra("USRPKEY_" + sanitize(this.mParser.selectedNetwork.name), str.getBytes());
    localIntent.putExtra("USRCERT_" + sanitize(this.mParser.selectedNetwork.name), paramString.getBytes());
    Util.log(this.mLogger, "Using pre-3.0 method.");
    return finishCertInstall(localIntent, 1, sanitize(this.mParser.selectedNetwork.name));
  }

  public void processCertInstallLine(String paramString)
  {
    if (!paramString.contains("credential is added:"))
      return;
    if ((this.mCertNameMethod != 0) && (this.mCertNameMethod != 1))
    {
      Util.log(this.mLogger, "Already have better data.  Discarding.");
      return;
    }
    this.mCertNameMethod = 1;
    this.mFinalCertName = paramString.substring(1 + (paramString.indexOf("credential is added:") + "credential is added:".length())).replace(" ", "");
    this.mCertInstalled = true;
    Util.log(this.mLogger, "Acquired cert name : " + this.mFinalCertName);
  }

  public void processCredentialInstallLine(String paramString)
  {
    if ((!paramString.contains("install ")) || (!paramString.contains("success? ")))
      return;
    this.mCertNameMethod = 2;
    String str1 = paramString.substring(paramString.indexOf("install ") + "install ".length());
    int i = str1.lastIndexOf(":");
    this.mFinalCertName = str1.substring(0, i);
    String str2 = str1.substring(i + 2);
    String str3 = str2.substring(str2.indexOf(" "));
    if (str3.substring(str3.indexOf("success? ")).contains("true"))
    {
      this.mCertInstalled = true;
      Util.log(this.mLogger, "Read back certificate name of : " + this.mFinalCertName);
      return;
    }
    this.mCertInstalled = false;
    this.mCertNameMethod = 0;
  }

  protected void resetCertCatcher()
  {
    this.mFinalCertName = null;
    this.mCertInstalled = false;
    this.mCertNameMethod = 0;
  }

  public void setCertInstalled(boolean paramBoolean)
  {
    this.mCertInstalled = paramBoolean;
  }

  public void setFinalCertName(String paramString)
  {
    this.mFinalCertName = paramString;
  }

  protected String splitPrefix(String paramString)
  {
    String[] arrayOfString = paramString.split("_");
    if (arrayOfString.length < 2)
      return paramString;
    if ((!arrayOfString[0].startsWith("CACERT")) && (!arrayOfString[0].startsWith("USRCERT")) && (!arrayOfString[0].startsWith("USRPKEY")))
      Util.log(this.mLogger, "Unknown prefix on certificate data : " + arrayOfString[0]);
    String str = arrayOfString[1];
    if (arrayOfString.length > 2)
      for (int i = 2; i < arrayOfString.length; i++)
        str = str + "_" + arrayOfString[i];
    return str;
  }

  protected void targetCertInstallerViaIntent(Intent paramIntent)
  {
    if (Build.VERSION.SDK_INT >= 8)
      paramIntent.setClassName("com.android.certinstaller", "com.android.certinstaller.CertInstallerMain");
  }
}