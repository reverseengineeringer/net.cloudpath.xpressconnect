package net.cloudpath.xpressconnect.certificates.installers;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build.VERSION;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.security.PrivateKey;
import net.cloudpath.xpressconnect.AndroidVersion;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.checks.LocalFileHandler;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkItemElement;
import net.cloudpath.xpressconnect.thread.ConfigureClientThread;

public class FileSystemAppDirInstallMethod extends FileSystemSdCardInstallMethod
{
  public FileSystemAppDirInstallMethod(Logger paramLogger, NetworkConfigParser paramNetworkConfigParser, FailureReason paramFailureReason, ConfigureClientThread paramConfigureClientThread, Activity paramActivity, Context paramContext)
  {
    super(paramLogger, paramNetworkConfigParser, paramFailureReason, paramConfigureClientThread, paramActivity, paramContext);
  }

  public String getInstallTypeName()
  {
    return "Using FSAD certificate install method.";
  }

  public String getWarningString()
  {
    return "<font color='red'>" + this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_stay_installed", "string")) + "</font><br/><br/><font color='blue'>" + this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_no_rerun_need", "string")) + "</font>";
  }

  public boolean installCaCert(String paramString, int paramInt, AndroidVersion paramAndroidVersion)
  {
    return installCaCert(paramString, paramInt, paramAndroidVersion, Build.VERSION.SDK_INT);
  }

  protected boolean installCaCert(String paramString, int paramInt1, AndroidVersion paramAndroidVersion, int paramInt2)
  {
    if ((this.mParser == null) || (this.mParser.selectedNetwork == null))
      Util.log(this.mLogger, "Parser is null in workaround 2!");
    do
    {
      return false;
      if (paramString == null)
      {
        Util.log(this.mLogger, "No certificate data provided in workaround 2!");
        return false;
      }
      if (paramInt2 < 8)
      {
        Util.log(this.mLogger, "Running Android version that doesn't support workaround #2.");
        return false;
      }
      if (paramAndroidVersion == null)
      {
        Util.log(this.mLogger, "av was null in installCaCert() for alt method 2.");
        return false;
      }
      if ((paramAndroidVersion.cantUseCertificates()) && (paramAndroidVersion.allowCertlessDevices(this.mParser)))
      {
        Util.log(this.mLogger, "Device will be allowed on, even though it doesn't support certificates.");
        this.mCaCertPath = null;
        return true;
      }
      String str = "CACERT_" + sanitize(this.mParser.selectedNetwork.name);
      if (installToFs(str, paramString, paramInt1, true))
      {
        Util.log(this.mLogger, "Installed certificate using alternate method 2.");
        this.mCaCertPath = (this.mFileHandler.getPathToAppDirectory() + "/" + str + ".pem");
        return true;
      }
      this.mCaCertPath = null;
    }
    while (this.mFailure == null);
    this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_cant_install_cert", "string")), 2);
    return false;
  }

  public boolean installClientCert(String paramString, PrivateKey paramPrivateKey, AndroidVersion paramAndroidVersion)
  {
    if ((paramString == null) || (paramPrivateKey == null))
      Util.log(this.mLogger, "Invalid user certificate or private key when attempting to install with workaround 2.");
    do
    {
      do
      {
        do
          return false;
        while (blockKeyStoreLackingDevices() == true);
        if ((this.mParser == null) || (this.mParser.selectedNetwork == null) || (this.mParser.selectedNetwork.name == null))
        {
          Util.log(this.mLogger, "Configuration isn't valid while trying to install user certificates with workaround 2.");
          return false;
        }
        if (!installToFs("USER_" + sanitize(this.mParser.selectedNetwork.name), paramString, 1, false))
          break;
        Util.log(this.mLogger, "Installed user certificate to alternate location.");
        this.mUserCertPath = (this.mFileHandler.getPathToAppDirectory() + "/USER_" + sanitize(this.mParser.selectedNetwork.name) + ".pem");
      }
      while (!writePrivateKey(paramPrivateKey, this.mFileHandler.getPathToAppDirectory() + "/USRPKEY_" + sanitize(this.mParser.selectedNetwork.name) + ".pem"));
      Util.log(this.mLogger, "Installed user private key to alternate location.");
      this.mUserPkeyPath = (this.mFileHandler.getPathToAppDirectory() + "/USRPKEY_" + sanitize(this.mParser.selectedNetwork.name) + ".pem");
      return true;
    }
    while (this.mFailure == null);
    this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_device_cant_store_certs", "string")), 2);
    return false;
  }

  protected boolean isFsInstallAllowed()
  {
    boolean bool = true;
    if (this.mAndroidVersion.forceUsingPkcs12() == bool)
      bool = false;
    return bool;
  }
}