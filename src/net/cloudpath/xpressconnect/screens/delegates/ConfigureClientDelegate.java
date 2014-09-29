package net.cloudpath.xpressconnect.screens.delegates;

import android.content.Context;
import android.os.Build.VERSION;
import android.widget.TextView;
import net.cloudpath.xpressconnect.AndroidApiLevels;
import net.cloudpath.xpressconnect.AndroidVersion;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.ProfileElement;
import net.cloudpath.xpressconnect.retention.ConfigureClientRetention;
import net.cloudpath.xpressconnect.screens.ConfigureClient;
import net.cloudpath.xpressconnect.thread.ConfigureClientThread;
import net.cloudpath.xpressconnect.thread.ThreadCom;
import net.cloudpath.xpressconnect.thread.ThreadComCallbacks;

public class ConfigureClientDelegate extends DelegateBase
  implements ThreadComCallbacks
{
  private ConfigureClient mParent = null;
  private ThreadCom mThreadCom = null;
  private ConfigureClientThread mWorkerThread = null;

  public ConfigureClientDelegate(ConfigureClient paramConfigureClient)
  {
    super(paramConfigureClient);
    this.mParent = paramConfigureClient;
  }

  public ConfigureClientDelegate(ConfigureClient paramConfigureClient, Context paramContext)
  {
    super(paramConfigureClient, paramContext);
    this.mParent = paramConfigureClient;
  }

  public String getBasicInstallWarning()
  {
    if ((new AndroidVersion(this.mLogger).noCertHint()) && (Build.VERSION.SDK_INT >= AndroidApiLevels.ANDROID_4_0_TO_4_0_2))
      return "You will now be prompted by Android to install a certificate.  Please use a certificate name of <font color='red'>" + this.mParser.savedConfigInfo.mBundleAltCertName + "</font>.";
    return "You will now be prompted by Android to install a certificate.  Please leave the certificate name set to the default and tap the 'Ok' button.  If no certificate name is provided, please enter any name.";
  }

  public String getJellybeanUpCertHintForcePasswordCheck(AndroidVersion paramAndroidVersion)
  {
    if (paramAndroidVersion.shouldForceCertInstallPassword(this.mParser, this.mLogger))
    {
      Util.log(this.mLogger, "Warning the user to use 'password' when prompted for the second PKCS12 decryption key. (cert hint case)");
      return "You will now be prompted to 'Extract from " + Util.getSSID(this.mLogger, this.mParser) + "', please use a password of 'password' and use the default certificate name.";
    }
    Util.log(this.mLogger, "Warning the user to just tap 'ok' when prompted for the second PKCS12 decryption key.");
    return "You will now be prompted to 'Extract from " + Util.getSSID(this.mLogger, this.mParser) + "'.  <font color='red'>Please just tap the OK button</font> and use the default certificate name.";
  }

  public String getJellybeanUpCertHintForcePasswordCheckAlt(AndroidVersion paramAndroidVersion)
  {
    String str = Util.getSSID(this.mLogger, this.mParser) + "_altca";
    if (paramAndroidVersion.shouldForceCertInstallPassword(this.mParser, this.mLogger))
    {
      Util.log(this.mLogger, "Warning the user to use 'password' when prompted for the second PKCS12 decryption key. (cert hint case)");
      return "You will now be prompted to 'Extract from " + str + "'.  Again, use a password of 'password' and use the default certificate name.";
    }
    Util.log(this.mLogger, "Warning the user to just tap 'ok' when prompted for the second PKCS12 decryption key.");
    return "You will now be prompted to 'Extract from " + str + "'.  <font color='red'>Please just tap the OK button</font> and use the default certificate name.";
  }

  public String getJellybeanUpNoCertHintForcePasswordCheck(AndroidVersion paramAndroidVersion)
  {
    if (paramAndroidVersion.shouldForceCertInstallPassword(this.mParser, this.mLogger))
    {
      Util.log(this.mLogger, "Warning the user to use 'password' when prompted for the second PKCS12 decryption key. (no cert hint case, using name of " + this.mParser.savedConfigInfo.mBundleCertName + ")");
      return "You will now be prompted to 'Extract from " + this.mParser.savedConfigInfo.mBundleCertName + "', please use a password of 'password' and use the certificate name <font color='red'>" + this.mParser.savedConfigInfo.mBundleCertName + "</font>.";
    }
    Util.log(this.mLogger, "Warning the user to use the shortend cert name and just tap ok.");
    return "You will now be prompted to 'Extract from " + this.mParser.savedConfigInfo.mBundleCertName + "'.  <font color='red'>Please just tap the OK button</font> and use the certificate name <font color='red'>" + this.mParser.savedConfigInfo.mBundleCertName + "</font>.";
  }

  public String getJellybeanUpNoCertHintForcePasswordCheckAlt(AndroidVersion paramAndroidVersion)
  {
    if (paramAndroidVersion.shouldForceCertInstallPassword(this.mParser, this.mLogger))
    {
      Util.log(this.mLogger, "Warning the user to use 'password' when prompted for the second PKCS12 decryption key. (no cert hint case, using name of " + this.mParser.savedConfigInfo.mBundleAltCertName + ")");
      return "You will now be prompted to 'Extract from " + this.mParser.savedConfigInfo.mBundleAltCertName + "'.  Again, use a password of 'password' and use the certificate name <font color='red'>" + this.mParser.savedConfigInfo.mBundleAltCertName + "</font>.";
    }
    Util.log(this.mLogger, "Warning the user to use the shortend cert name and just tap ok.");
    return "You will now be prompted to 'Extract from " + this.mParser.savedConfigInfo.mBundleAltCertName + "'.  <font color='red'>Please just tap the OK button</font> and use the certificate name <font color='red'>" + this.mParser.savedConfigInfo.mBundleAltCertName + "</font>.";
  }

  public String getPkcs12AltCaWarningMsg()
  {
    return getPkcs12AltCaWarningMsg(Build.VERSION.SDK_INT);
  }

  public String getPkcs12AltCaWarningMsg(int paramInt)
  {
    if ((shouldUseUserPassword()) && (Util.stringIsNotEmpty(this.mParser.savedConfigInfo.password)))
      return getPkcs12AltCaWarningWithUserPassword(paramInt);
    return getPkcs12AltCaWarningWithDefaultPassword(paramInt);
  }

  public String getPkcs12AltCaWarningWithDefaultPassword(int paramInt)
  {
    return getPkcs12AltCaWarningWithDefaultPassword(paramInt, new AndroidVersion(this.mLogger));
  }

  public String getPkcs12AltCaWarningWithDefaultPassword(int paramInt, AndroidVersion paramAndroidVersion)
  {
    String str = Util.getSSID(this.mLogger, this.mParser) + "_altca";
    if ((paramInt >= 16) && (!paramAndroidVersion.noCertHint()))
      return getJellybeanUpCertHintForcePasswordCheckAlt(paramAndroidVersion);
    if ((paramInt >= 16) && (paramAndroidVersion.noCertHint()))
      return getJellybeanUpNoCertHintForcePasswordCheckAlt(paramAndroidVersion);
    Util.log(this.mLogger, "Warning the user to use 'password' when prompted for the second PKCS12 decryption key.");
    return "You will now be prompted to 'Extract from " + str + "'.  Again, use a password of 'password' and use the default certificate name or any name if it is blank.";
  }

  public String getPkcs12AltCaWarningWithUserPassword(int paramInt)
  {
    return getPkcs12AltCaWarningWithUserPassword(paramInt, new AndroidVersion(this.mLogger));
  }

  public String getPkcs12AltCaWarningWithUserPassword(int paramInt, AndroidVersion paramAndroidVersion)
  {
    String str = Util.getSSID(this.mLogger, this.mParser) + "_altca";
    if ((paramAndroidVersion.noCertHint()) && (paramInt >= 16))
    {
      Util.log(this.mLogger, "Warning the user to use their own password and short cert code when prompted for the second PKCS12 decryption key.");
      return "You will now be prompted to 'Extract from " + this.mParser.savedConfigInfo.mBundleAltCertName + "'.  Again, use <font color='red'>your user password</font> and use a certificate name of <font color='red'>" + this.mParser.savedConfigInfo.mBundleAltCertName + "</font>.";
    }
    if ((!paramAndroidVersion.noCertHint()) && (paramInt >= 16))
    {
      Util.log(this.mLogger, "Warning the user to use their own password and default cert name when prompted for the second PKCS12 decryption key.");
      return "You will now be prompted to 'Extract from " + str + "'.  Again, use <font color='red'>your user password</font> and use the default certificate name.";
    }
    Util.log(this.mLogger, "Warning user to use their user password and whatever cert name when prompted for the PKCS12 decryption key.");
    return "You will now be prompted to 'Extract from " + str + "'.  Again, use <font color='red'>your user password</font> and use the default certificate name.  If the default name is blank, enter any name.";
  }

  public String getPkcs12AltWarningMsg()
  {
    return getPkcs12WarningMsg(Build.VERSION.SDK_INT);
  }

  public String getPkcs12AltWarningMsg(int paramInt)
  {
    if ((this.mParser == null) || (this.mParser.savedConfigInfo == null))
    {
      Util.log(this.mLogger, "Parser or saved config is null in getPkcs12AltWarningMsg()!");
      return null;
    }
    if ((shouldUseUserPassword()) && (!Util.stringIsEmpty(this.mParser.savedConfigInfo.password)))
      return getPkcs12AltCaWarningWithUserPassword(paramInt);
    return getPkcs12AltCaWarningWithDefaultPassword(paramInt);
  }

  public String getPkcs12WarningMsg()
  {
    return getPkcs12WarningMsg(Build.VERSION.SDK_INT);
  }

  public String getPkcs12WarningMsg(int paramInt)
  {
    if ((this.mParser == null) || (this.mParser.savedConfigInfo == null))
    {
      Util.log(this.mLogger, "Parser or saved config is null in getPkcs12WarningMsg()!");
      return null;
    }
    if ((shouldUseUserPassword()) && (!Util.stringIsEmpty(this.mParser.savedConfigInfo.password)))
      return getPkcs12WarningWithUserPassword(paramInt);
    return getPkcs12WarningWithDefaultPassword(paramInt);
  }

  public String getPkcs12WarningWithDefaultPassword(int paramInt)
  {
    return getPkcs12WarningWithDefaultPassword(paramInt, new AndroidVersion(this.mLogger));
  }

  public String getPkcs12WarningWithDefaultPassword(int paramInt, AndroidVersion paramAndroidVersion)
  {
    String str = Util.getSSID(this.mLogger, this.mParser);
    if ((paramInt >= 16) && (!paramAndroidVersion.noCertHint()))
      return getJellybeanUpCertHintForcePasswordCheck(paramAndroidVersion);
    if ((paramInt >= 16) && (paramAndroidVersion.noCertHint()))
      return getJellybeanUpNoCertHintForcePasswordCheck(paramAndroidVersion);
    Util.log(this.mLogger, "Warning the user to use 'password' when prompted for the PKCS12 decryption key.");
    return "You will now be prompted to 'Extract from " + str + "', please use a password of 'password' and use the default certificate name.  If the certificate name is blank, enter any name.";
  }

  public String getPkcs12WarningWithUserPassword(int paramInt)
  {
    return getPkcs12WarningWithUserPassword(paramInt, new AndroidVersion(this.mLogger));
  }

  public String getPkcs12WarningWithUserPassword(int paramInt, AndroidVersion paramAndroidVersion)
  {
    String str1 = Util.getSSID(this.mLogger, this.mParser);
    String str2;
    if ((paramAndroidVersion.noCertHint()) && (paramInt >= 16))
    {
      Util.log(this.mLogger, "Warning the user to use their password and short cert code when prompted for the second PKCS12 decryption key.");
      if ((this.mParser == null) || (this.mParser.savedConfigInfo == null))
      {
        Util.log(this.mLogger, "Parser isn't set up properly!  Will return null.");
        return null;
      }
      str2 = "You will now be prompted to 'Extract from " + this.mParser.savedConfigInfo.mBundleCertName + "', please use <font color='red'>your user password</font> and use a certificate name of <font color='red'>" + this.mParser.savedConfigInfo.mBundleCertName + "</font>.";
    }
    while (true)
    {
      return str2;
      if ((!paramAndroidVersion.noCertHint()) && (paramInt >= 16))
      {
        Util.log(this.mLogger, "Warning the user to use their own password and default cert name when prompted for the second PKCS12 decryption key.");
        str2 = "You will now be prompted to 'Extract from " + str1 + "', please use <font color='red'>your user password</font> and use the default certificate name.";
      }
      else
      {
        Util.log(this.mLogger, "Warning user to use their user password and whatever cert cname when prompted for the PKCS12 decryption key.");
        str2 = "You will now be prompted to 'Extract from " + str1 + "', please use <font color='red'>your user password</font> and use the default certificate name.  If the default name is blank, enter any name.";
      }
    }
  }

  public void onBackPressed()
  {
    if (this.mWorkerThread != null)
    {
      this.mWorkerThread.die();
      this.mWorkerThread = null;
    }
    done(-2);
  }

  public void onPause()
  {
    if (this.mWorkerThread != null)
      this.mWorkerThread.spinLock();
    Util.log(this.mLogger, "Retaining configuration instance.");
    ConfigureClientRetention localConfigureClientRetention = new ConfigureClientRetention();
    localConfigureClientRetention.threadcom = this.mThreadCom;
    localConfigureClientRetention.worker = this.mWorkerThread;
    if (this.mParent != null)
      localConfigureClientRetention.responseThreadCallbacks = this.mParent.mResponseThreadCallbacks;
    if (this.mWorkerThread != null)
    {
      this.mWorkerThread.resume(this.mThreadCom, this.mParent);
      this.mWorkerThread.unlock();
    }
    if (this.mGlobals == null)
    {
      Util.log(this.mLogger, "Unable to store saved object.");
      return;
    }
    this.mGlobals.setSavedObject(localConfigureClientRetention);
  }

  public void onResume()
  {
    if (this.mWorkerThread != null)
    {
      this.mWorkerThread.resume(this.mThreadCom, this.mParent);
      this.mWorkerThread.unlock();
      return;
    }
    Util.log(this.mLogger, "Not resuming in onResume()!");
  }

  public void restoreSavedData()
  {
    if (this.mGlobals == null)
      Util.log(this.mLogger, "mGlobals is null in restoreSavedData()!");
    while (true)
    {
      return;
      ConfigureClientRetention localConfigureClientRetention;
      try
      {
        localConfigureClientRetention = (ConfigureClientRetention)this.mGlobals.getSavedObject();
        this.mGlobals.setSavedObject(null);
        if (localConfigureClientRetention == null)
        {
          this.mThreadCom = new ThreadCom();
          if (this.mThreadCom == null)
          {
            done(-3);
            return;
          }
        }
      }
      catch (ClassCastException localClassCastException1)
      {
        while (true)
          localConfigureClientRetention = null;
        this.mParent.mResponseThreadCallbacks = null;
      }
      while (this.mThreadCom != null)
      {
        this.mThreadCom.setCallbacks(this);
        return;
        this.mParent.mResponseThreadCallbacks = localConfigureClientRetention.responseThreadCallbacks;
        this.mThreadCom = localConfigureClientRetention.threadcom;
        try
        {
          this.mWorkerThread = ((ConfigureClientThread)localConfigureClientRetention.worker);
        }
        catch (ClassCastException localClassCastException2)
        {
          Util.log(this.mLogger, "Rebuilding thread....");
          this.mThreadCom = new ThreadCom();
          this.mWorkerThread = null;
        }
      }
    }
  }

  public void setTextView(TextView paramTextView)
  {
    if (this.mThreadCom == null)
      return;
    this.mThreadCom.setTextView(paramTextView);
  }

  protected boolean shouldUseUserPassword()
  {
    if ((this.mParser == null) || (this.mParser.selectedProfile == null));
    while ((this.mParser.selectedProfile.showPreCreds != 1) || (this.mParser.selectedProfile.getSetting(2, 80035) == null))
      return false;
    return true;
  }

  public void showDialog(int paramInt)
  {
    if (this.mParent == null)
    {
      Util.log(this.mLogger, "No parent defined in showDialog()!");
      return;
    }
    this.mParent.showMyDialog(paramInt);
  }

  public void signalWorker()
  {
    if (this.mWorkerThread == null)
    {
      Util.log(this.mLogger, "No worker thread to signal.  Ignoring.");
      return;
    }
    synchronized (this.mWorkerThread)
    {
      this.mWorkerThread.notifyAll();
      return;
    }
  }

  public void startWorkerThread()
  {
    if (this.mWorkerThread != null)
      return;
    if ((this.mGlobals == null) || (this.mParent == null))
    {
      Util.log(this.mLogger, "ConfigureClientDelegate was not set up properly!");
      return;
    }
    this.mWorkerThread = new ConfigureClientThread(this.mThreadCom, this.mParser, this.mLogger, this.mGlobals.getFailureReason(), this.mParent.mWifi, this.mParent, this.mGlobals);
    this.mWorkerThread.start();
  }

  public void terminateWorkerThread()
  {
    if (this.mWorkerThread != null)
    {
      this.mWorkerThread.die();
      this.mWorkerThread = null;
    }
  }

  public void threadChangeTab(int paramInt)
  {
  }

  public void threadFailed(Boolean paramBoolean, int paramInt, String paramString)
  {
    if (paramInt == -10)
      done(-3);
    while (true)
    {
      Util.log(this.mLogger, "--- Leaving ConfigureClient due to a failure in the thread.");
      this.mWorkerThread = null;
      return;
      if (paramInt == 15)
      {
        done(paramInt);
        this.mFailure.setFailReason(FailureReason.useWarningIcon, paramString, -1);
      }
      else
      {
        done(paramInt);
      }
    }
  }

  public void threadSuccess()
  {
    Util.log(this.mLogger, "--- Leaving ConfigureClient due to successful thread run.");
    done(1);
    this.mWorkerThread = null;
  }
}