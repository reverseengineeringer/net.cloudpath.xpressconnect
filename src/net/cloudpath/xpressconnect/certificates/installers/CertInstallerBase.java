package net.cloudpath.xpressconnect.certificates.installers;

import android.app.Activity;
import android.content.Intent;
import android.os.Build.VERSION;
import java.security.PrivateKey;
import net.cloudpath.xpressconnect.AndroidApiLevels;
import net.cloudpath.xpressconnect.AndroidVersion;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.Testing;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.ProfileElement;
import net.cloudpath.xpressconnect.parsers.config.SettingElement;
import net.cloudpath.xpressconnect.screens.ScreenBase;
import net.cloudpath.xpressconnect.thread.ConfigureClientThread;
import net.cloudpath.xpressconnect.thread.ResponseThreadComCallbacks;
import net.cloudpath.xpressconnect.thread.ThreadCom;

public class CertInstallerBase
  implements ResponseThreadComCallbacks
{
  protected static final int INSTALL_CERTIFICATE = 1002;
  protected static final int INSTALL_PKEY_WORKAROUND = 1003;
  protected static final int UNLOCK_KEYSTORE = 1004;
  protected int mActiveDialog = -1;
  protected AndroidVersion mAndroidVersion = null;
  protected volatile boolean mDialogDone = false;
  protected FailureReason mFailure = null;
  protected String mInstallPassword = null;
  protected volatile int mIntentResult = -1;
  protected boolean mKeystoreDialogShown = false;
  protected Logger mLogger = null;
  protected Activity mParent = null;
  protected NetworkConfigParser mParser = null;
  protected boolean mTerminalFailure = false;
  protected ConfigureClientThread mThread = null;

  public CertInstallerBase(Logger paramLogger, NetworkConfigParser paramNetworkConfigParser, ConfigureClientThread paramConfigureClientThread, FailureReason paramFailureReason, Activity paramActivity)
  {
    this.mLogger = paramLogger;
    this.mParser = paramNetworkConfigParser;
    this.mParent = paramActivity;
    this.mThread = paramConfigureClientThread;
    this.mFailure = paramFailureReason;
    this.mAndroidVersion = new AndroidVersion(this.mLogger);
  }

  // ERROR //
  private int getKeyStoreState(int paramInt, AndroidVersion paramAndroidVersion)
  {
    // Byte code:
    //   0: iload_1
    //   1: bipush 14
    //   3: if_icmplt +9 -> 12
    //   6: aload_0
    //   7: aload_2
    //   8: invokespecial 75	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:getKeyStoreStateSdk14Up	(Lnet/cloudpath/xpressconnect/AndroidVersion;)I
    //   11: ireturn
    //   12: new 77	net/cloudpath/xpressconnect/KeyStore
    //   15: dup
    //   16: invokespecial 78	net/cloudpath/xpressconnect/KeyStore:<init>	()V
    //   19: astore_3
    //   20: aload_3
    //   21: invokevirtual 82	net/cloudpath/xpressconnect/KeyStore:test	()I
    //   24: istore 5
    //   26: iload 5
    //   28: iconst_1
    //   29: if_icmpeq +64 -> 93
    //   32: iload 5
    //   34: tableswitch	default:+50 -> 84, 2:+98->132, 3:+110->144, 4:+122->156, 5:+134->168, 6:+146->180, 7:+158->192, 8:+170->204, 9:+182->216, 10:+194->228
    //   85: getfield 40	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   88: ldc 84
    //   90: invokestatic 90	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   93: iload 5
    //   95: ireturn
    //   96: astore 6
    //   98: aload_0
    //   99: getfield 40	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   102: ldc 92
    //   104: invokestatic 90	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   107: aload 6
    //   109: invokevirtual 95	java/lang/Exception:printStackTrace	()V
    //   112: iconst_1
    //   113: ireturn
    //   114: astore 4
    //   116: aload_0
    //   117: getfield 40	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   120: ldc 97
    //   122: invokestatic 90	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   125: aload 4
    //   127: invokevirtual 95	java/lang/Exception:printStackTrace	()V
    //   130: iconst_m1
    //   131: ireturn
    //   132: aload_0
    //   133: getfield 40	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   136: ldc 99
    //   138: invokestatic 90	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   141: goto -48 -> 93
    //   144: aload_0
    //   145: getfield 40	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   148: ldc 101
    //   150: invokestatic 90	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   153: goto -60 -> 93
    //   156: aload_0
    //   157: getfield 40	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   160: ldc 103
    //   162: invokestatic 90	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   165: goto -72 -> 93
    //   168: aload_0
    //   169: getfield 40	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   172: ldc 105
    //   174: invokestatic 90	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   177: goto -84 -> 93
    //   180: aload_0
    //   181: getfield 40	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   184: ldc 107
    //   186: invokestatic 90	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   189: goto -96 -> 93
    //   192: aload_0
    //   193: getfield 40	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   196: ldc 109
    //   198: invokestatic 90	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   201: goto -108 -> 93
    //   204: aload_0
    //   205: getfield 40	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   208: ldc 111
    //   210: invokestatic 90	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   213: goto -120 -> 93
    //   216: aload_0
    //   217: getfield 40	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   220: ldc 113
    //   222: invokestatic 90	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   225: goto -132 -> 93
    //   228: aload_0
    //   229: getfield 40	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   232: ldc 115
    //   234: invokestatic 90	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   237: goto -144 -> 93
    //
    // Exception table:
    //   from	to	target	type
    //   12	20	96	java/lang/Exception
    //   20	26	114	java/lang/Exception
  }

  // ERROR //
  private int getKeyStoreStateSdk14Up(AndroidVersion paramAndroidVersion)
  {
    // Byte code:
    //   0: new 77	net/cloudpath/xpressconnect/KeyStore
    //   3: dup
    //   4: invokespecial 78	net/cloudpath/xpressconnect/KeyStore:<init>	()V
    //   7: astore_2
    //   8: aload_2
    //   9: invokevirtual 129	net/cloudpath/xpressconnect/KeyStore:state	()Ljava/lang/String;
    //   12: astore 8
    //   14: aload_0
    //   15: getfield 40	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   18: new 131	java/lang/StringBuilder
    //   21: dup
    //   22: invokespecial 132	java/lang/StringBuilder:<init>	()V
    //   25: ldc 134
    //   27: invokevirtual 138	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   30: aload 8
    //   32: invokevirtual 138	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   35: invokevirtual 141	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   38: invokestatic 90	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   41: aload 8
    //   43: getstatic 147	java/util/Locale:ENGLISH	Ljava/util/Locale;
    //   46: invokevirtual 153	java/lang/String:toLowerCase	(Ljava/util/Locale;)Ljava/lang/String;
    //   49: ldc 155
    //   51: invokevirtual 159	java/lang/String:contentEquals	(Ljava/lang/CharSequence;)Z
    //   54: ifeq +111 -> 165
    //   57: iconst_3
    //   58: ireturn
    //   59: astore 9
    //   61: aload_0
    //   62: getfield 40	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   65: ldc 92
    //   67: invokestatic 90	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   70: aload 9
    //   72: invokevirtual 95	java/lang/Exception:printStackTrace	()V
    //   75: iconst_m1
    //   76: ireturn
    //   77: astore 7
    //   79: aload 7
    //   81: invokevirtual 160	java/lang/SecurityException:printStackTrace	()V
    //   84: aload_0
    //   85: getfield 40	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   88: ldc 162
    //   90: invokestatic 90	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   93: iconst_m1
    //   94: ireturn
    //   95: astore 6
    //   97: aload 6
    //   99: invokevirtual 163	java/lang/IllegalArgumentException:printStackTrace	()V
    //   102: aload_0
    //   103: getfield 40	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   106: ldc 165
    //   108: invokestatic 90	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   111: iconst_m1
    //   112: ireturn
    //   113: astore 5
    //   115: aload 5
    //   117: invokevirtual 166	java/lang/NoSuchMethodException:printStackTrace	()V
    //   120: aload_0
    //   121: getfield 40	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   124: ldc 168
    //   126: invokestatic 90	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   129: iconst_m1
    //   130: ireturn
    //   131: astore 4
    //   133: aload 4
    //   135: invokevirtual 169	java/lang/IllegalAccessException:printStackTrace	()V
    //   138: aload_0
    //   139: getfield 40	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   142: ldc 171
    //   144: invokestatic 90	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   147: iconst_m1
    //   148: ireturn
    //   149: astore_3
    //   150: aload_3
    //   151: invokevirtual 172	java/lang/reflect/InvocationTargetException:printStackTrace	()V
    //   154: aload_0
    //   155: getfield 40	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   158: ldc 174
    //   160: invokestatic 90	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   163: iconst_m1
    //   164: ireturn
    //   165: aload 8
    //   167: getstatic 147	java/util/Locale:ENGLISH	Ljava/util/Locale;
    //   170: invokevirtual 153	java/lang/String:toLowerCase	(Ljava/util/Locale;)Ljava/lang/String;
    //   173: ldc 176
    //   175: invokevirtual 159	java/lang/String:contentEquals	(Ljava/lang/CharSequence;)Z
    //   178: ifeq +5 -> 183
    //   181: iconst_2
    //   182: ireturn
    //   183: iconst_1
    //   184: ireturn
    //
    // Exception table:
    //   from	to	target	type
    //   0	8	59	java/lang/Exception
    //   8	14	77	java/lang/SecurityException
    //   8	14	95	java/lang/IllegalArgumentException
    //   8	14	113	java/lang/NoSuchMethodException
    //   8	14	131	java/lang/IllegalAccessException
    //   8	14	149	java/lang/reflect/InvocationTargetException
  }

  private boolean intentResultUpdated()
  {
    try
    {
      if (this.mIntentResult != -1)
      {
        Util.log(this.mLogger, "Finished waiting for intent.  Result : " + this.mIntentResult);
        return true;
      }
      return false;
    }
    finally
    {
    }
  }

  public boolean canInstallAtOnce()
  {
    return (canInstallUser()) && (canInstallCa());
  }

  public boolean canInstallCa()
  {
    return false;
  }

  public boolean canInstallMultiCa()
  {
    return false;
  }

  public boolean canInstallUser()
  {
    return false;
  }

  protected boolean checkKeyStoreState()
  {
    if (Testing.areTesting == true);
    int i;
    do
    {
      return true;
      i = getKeyStoreState();
    }
    while (i == 1);
    if (i == 3)
    {
      Util.log(this.mLogger, "Keystore is not initialized.  Warning user.");
      if (Build.VERSION.SDK_INT >= 14)
      {
        Util.log(this.mLogger, "Showing lock screen warning.");
        showDialog(10001);
        return unlockKeystore();
      }
      showKeystoreWarningDialog();
      return unlockKeystore();
    }
    if ((i == 2) || (this.mAndroidVersion.forceOpenKeystore(this.mParser)))
    {
      if (Build.VERSION.SDK_INT >= AndroidApiLevels.ANDROID_4_0_TO_4_0_2)
      {
        Util.log(this.mLogger, "********  Device keystore is in LOCKED state on Android 4.0 or greater!  Attempting workaround.");
        return workAroundBadKeystoreState();
      }
      Util.log(this.mLogger, "Keystore is locked.  Warning user.");
      showKeystoreWarningDialog();
      return unlockKeystore();
    }
    Util.log(this.mLogger, "Users keystore returned an error of " + i + ".  Notifying user of failure and remediation action.");
    Util.log(this.mLogger, "Showing keystore error dialog.");
    showDialog(10003);
    return false;
  }

  public void dialogComplete(int paramInt, boolean paramBoolean)
  {
    if (paramInt == this.mActiveDialog)
      this.mDialogDone = true;
  }

  protected boolean disallowKeystoreUse()
  {
    if ((this.mParser == null) || (this.mParser.selectedProfile == null))
      Util.log(this.mLogger, "Not configured to determine keystore usage state!");
    SettingElement localSettingElement;
    do
    {
      return false;
      localSettingElement = this.mParser.selectedProfile.getSetting(2, 80032);
    }
    while ((localSettingElement == null) || (localSettingElement.requiredValue == null) || (!localSettingElement.requiredValue.contentEquals("1")));
    return true;
  }

  public boolean forceOpenKeystore()
  {
    return checkKeyStoreState();
  }

  public String getCaAlias()
  {
    return null;
  }

  public String getInstallTypeName()
  {
    return "Base!  Internal application error!!!";
  }

  protected int getKeyStoreState()
  {
    return getKeyStoreState(Build.VERSION.SDK_INT, this.mAndroidVersion);
  }

  public String getPrivateKeyAlias()
  {
    return null;
  }

  public String getUserAlias()
  {
    return null;
  }

  public String getWarningString()
  {
    return "";
  }

  public boolean installAllCerts(String paramString1, int paramInt, String paramString2, PrivateKey paramPrivateKey)
  {
    return installAllCerts(paramString1, paramInt, paramString2, paramPrivateKey, this.mAndroidVersion);
  }

  public boolean installAllCerts(String paramString1, int paramInt, String paramString2, PrivateKey paramPrivateKey, AndroidVersion paramAndroidVersion)
  {
    if (installCaCert(paramString1, paramInt, paramAndroidVersion) == true)
      return installClientCert(paramString2, paramPrivateKey, paramAndroidVersion) == true;
    return false;
  }

  public boolean installCaCert(String paramString, int paramInt)
  {
    return installCaCert(paramString, paramInt, this.mAndroidVersion);
  }

  public boolean installCaCert(String paramString, int paramInt, AndroidVersion paramAndroidVersion)
  {
    return false;
  }

  public boolean installClientCert(String paramString, PrivateKey paramPrivateKey)
  {
    return installClientCert(paramString, paramPrivateKey, this.mAndroidVersion);
  }

  public boolean installClientCert(String paramString, PrivateKey paramPrivateKey, AndroidVersion paramAndroidVersion)
  {
    return false;
  }

  // ERROR //
  public void intentComplete(int paramInt1, int paramInt2, Intent paramIntent)
  {
    // Byte code:
    //   0: iload_1
    //   1: sipush 1004
    //   4: if_icmpeq +17 -> 21
    //   7: iload_1
    //   8: sipush 1002
    //   11: if_icmpeq +10 -> 21
    //   14: iload_1
    //   15: sipush 1003
    //   18: if_icmpne +36 -> 54
    //   21: aload_0
    //   22: monitorenter
    //   23: iload_2
    //   24: iconst_m1
    //   25: if_icmpne +30 -> 55
    //   28: aload_0
    //   29: iconst_1
    //   30: putfield 58	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mIntentResult	I
    //   33: aload_0
    //   34: monitorexit
    //   35: aload_0
    //   36: getfield 52	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mThread	Lnet/cloudpath/xpressconnect/thread/ConfigureClientThread;
    //   39: astore 5
    //   41: aload 5
    //   43: monitorenter
    //   44: aload_0
    //   45: getfield 52	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mThread	Lnet/cloudpath/xpressconnect/thread/ConfigureClientThread;
    //   48: invokevirtual 298	java/lang/Object:notifyAll	()V
    //   51: aload 5
    //   53: monitorexit
    //   54: return
    //   55: aload_0
    //   56: iload_2
    //   57: putfield 58	net/cloudpath/xpressconnect/certificates/installers/CertInstallerBase:mIntentResult	I
    //   60: goto -27 -> 33
    //   63: astore 4
    //   65: aload_0
    //   66: monitorexit
    //   67: aload 4
    //   69: athrow
    //   70: astore 6
    //   72: aload 5
    //   74: monitorexit
    //   75: aload 6
    //   77: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   28	33	63	finally
    //   33	35	63	finally
    //   55	60	63	finally
    //   65	67	63	finally
    //   44	54	70	finally
    //   72	75	70	finally
  }

  public boolean isTerminalFailure()
  {
    return this.mTerminalFailure;
  }

  protected String sanitize(String paramString)
  {
    if (paramString == null)
      return null;
    return paramString.replace(" ", "").replace("&", "").replace("/", "").replace("-", "");
  }

  protected void showDialog(int paramInt)
  {
    if (this.mParent == null)
      return;
    ScreenBase localScreenBase = (ScreenBase)this.mParent;
    localScreenBase.setResponseCallbacks(this);
    this.mDialogDone = false;
    this.mActiveDialog = paramInt;
    this.mThread.getThreadCom().showDialog(paramInt);
    if (!this.mDialogDone)
      try
      {
        synchronized (this.mThread)
        {
          this.mThread.wait();
        }
      }
      catch (InterruptedException localInterruptedException)
      {
        while (true)
          localInterruptedException.printStackTrace();
      }
    localScreenBase.setResponseCallbacks(null);
  }

  protected void showKeystoreWarningDialog()
  {
    if (this.mKeystoreDialogShown == true)
      return;
    Util.log(this.mLogger, "Showing keystore dialog.");
    showDialog(10000);
    this.mKeystoreDialogShown = true;
  }

  protected boolean unlockKeystore()
  {
    ScreenBase localScreenBase = (ScreenBase)this.mParent;
    this.mIntentResult = -1;
    try
    {
      if (Build.VERSION.SDK_INT < 14);
      for (Intent localIntent = new Intent("android.credentials.UNLOCK"); ; localIntent = new Intent("com.android.credentials.UNLOCK"))
      {
        this.mActiveDialog = 1004;
        localScreenBase.setResponseCallbacks(this);
        this.mParent.startActivityForResult(localIntent, 1004);
        waitForIntentReturn();
        localScreenBase.setResponseCallbacks(this);
        return true;
      }
    }
    catch (Exception localException)
    {
      Util.log(this.mLogger, "Error attempting to unlock the keystore.");
    }
    return false;
  }

  public void useInstallPassword(String paramString)
  {
    this.mInstallPassword = paramString;
  }

  protected boolean waitForIntentReturn()
  {
    int i = 0;
    Util.log(this.mLogger, "Waiting for intent to return.");
    while (true)
      if (i == 0)
        if (intentResultUpdated())
          i = 1;
        else
          try
          {
            synchronized (this.mThread)
            {
              this.mThread.wait();
            }
          }
          catch (InterruptedException localInterruptedException)
          {
            while (true)
              localInterruptedException.printStackTrace();
          }
    Util.log(this.mLogger, "Intent returned.");
    return true;
  }

  protected boolean workAroundBadKeystoreState()
  {
    showDialog(10008);
    if (getKeyStoreState() != 3)
    {
      Util.log(this.mLogger, "The attempt to clear the keystore failed.  We have no other choice but to fail the configuration attempt.");
      this.mFailure.setFailReason(FailureReason.useWarningIcon, "The attempt to correct the issue with the Credential Store either failed, or was cancelled.   Unable to continue.", 22);
      this.mTerminalFailure = true;
      return false;
    }
    return checkKeyStoreState();
  }
}