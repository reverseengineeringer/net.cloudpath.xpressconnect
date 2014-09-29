package net.cloudpath.xpressconnect.certificates;

import android.app.Activity;
import android.content.Context;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.Testing;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.certificates.installers.BasicInstallIntentMethod;
import net.cloudpath.xpressconnect.certificates.installers.CertInstallerBase;
import net.cloudpath.xpressconnect.certificates.installers.FileSystemAppDirInstallMethod;
import net.cloudpath.xpressconnect.certificates.installers.FileSystemSdCardInstallMethod;
import net.cloudpath.xpressconnect.certificates.installers.Pkcs12IntentInstallMethod;
import net.cloudpath.xpressconnect.certificates.installers.SystemInstallIntentMethod;
import net.cloudpath.xpressconnect.certificates.installers.WirelessV2CertInstall;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.nativeproxy.WifiConfigurationProxy;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.thread.ConfigureClientThread;

public class CertInstaller
{
  protected CertInstallerBase mActiveMethod = null;
  protected String mCaCertPath = null;
  protected Context mContext = null;
  protected FailureReason mFailure = null;
  protected ArrayList<CertInstallerBase> mInstallMethods = new ArrayList();
  protected Logger mLogger = null;
  protected int mNumCaInstalled = -1;
  protected Activity mParent = null;
  protected NetworkConfigParser mParser = null;
  protected String mUserCertPath = null;
  protected String mUserPkeyPath = null;
  protected WifiConfigurationProxy mWifiConfig = null;
  protected ConfigureClientThread tParent = null;

  public CertInstaller(Logger paramLogger, NetworkConfigParser paramNetworkConfigParser, WifiConfigurationProxy paramWifiConfigurationProxy, FailureReason paramFailureReason, ConfigureClientThread paramConfigureClientThread, Activity paramActivity)
  {
    this.mLogger = paramLogger;
    this.mParser = paramNetworkConfigParser;
    this.mParent = paramActivity;
    this.mContext = this.mParent;
    this.mFailure = paramFailureReason;
    this.tParent = paramConfigureClientThread;
    this.mWifiConfig = paramWifiConfigurationProxy;
    populateInstallerMethods();
  }

  public CertInstaller(Logger paramLogger, NetworkConfigParser paramNetworkConfigParser, WifiConfigurationProxy paramWifiConfigurationProxy, FailureReason paramFailureReason, ConfigureClientThread paramConfigureClientThread, Activity paramActivity, Context paramContext)
  {
    this.mLogger = paramLogger;
    this.mParser = paramNetworkConfigParser;
    this.mParent = paramActivity;
    this.mContext = paramContext;
    this.mFailure = paramFailureReason;
    this.tParent = paramConfigureClientThread;
    this.mWifiConfig = paramWifiConfigurationProxy;
    populateInstallerMethods();
  }

  private void populateInstallerMethods()
  {
    if (Testing.areTesting)
    {
      this.mInstallMethods.add(new CertInstallerBase(this.mLogger, this.mParser, this.tParent, this.mFailure, this.mParent));
      return;
    }
    this.mInstallMethods.add(new SystemInstallIntentMethod(this.mLogger, this.mParser, this.mFailure, this.tParent, this.mContext, this.mParent));
    this.mInstallMethods.add(new WirelessV2CertInstall(this.mLogger, this.mParser, this.mFailure, this.tParent, this.mParent, this.mWifiConfig));
    this.mInstallMethods.add(new BasicInstallIntentMethod(this.mLogger, this.mParser, this.mFailure, this.tParent, this.mContext, this.mParent));
    this.mInstallMethods.add(new FileSystemSdCardInstallMethod(this.mLogger, this.mParser, this.mFailure, this.tParent, this.mParent, this.mContext));
    this.mInstallMethods.add(new FileSystemAppDirInstallMethod(this.mLogger, this.mParser, this.mFailure, this.tParent, this.mParent, this.mContext));
    this.mInstallMethods.add(new Pkcs12IntentInstallMethod(this.mLogger, this.mParser, this.mFailure, this.tParent, this.mContext, this.mParent));
  }

  protected ArrayList<String> filterCaChain(ArrayList<String> paramArrayList)
  {
    ArrayList localArrayList = new ArrayList();
    CertUtils localCertUtils = new CertUtils(this.mLogger);
    for (int i = 0; i < paramArrayList.size(); i++)
    {
      X509Certificate localX509Certificate = localCertUtils.getX509Cert((String)paramArrayList.get(i));
      if ((localX509Certificate != null) && (localCertUtils.certIsCa(localX509Certificate)))
        localArrayList.add(paramArrayList.get(i));
    }
    this.mNumCaInstalled = localArrayList.size();
    return localArrayList;
  }

  public boolean forceOpenKeystore()
  {
    return new CertInstallerBase(this.mLogger, this.mParser, this.tParent, this.mFailure, this.mParent).forceOpenKeystore();
  }

  public String getCaCertAlias()
  {
    return this.mCaCertPath;
  }

  protected String getCertsAsString(ArrayList<String> paramArrayList)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if ((paramArrayList == null) || (paramArrayList.size() <= 0))
    {
      Util.log(this.mLogger, "No certificates to convert to a string!");
      return null;
    }
    int i = 0;
    if (i < paramArrayList.size())
    {
      if (((String)paramArrayList.get(i)).endsWith("\n"))
        localStringBuilder.append((String)paramArrayList.get(i));
      while (true)
      {
        i++;
        break;
        localStringBuilder.append((String)paramArrayList.get(i) + "\n");
      }
    }
    return localStringBuilder.toString();
  }

  public int getNumCaInstalled()
  {
    return this.mNumCaInstalled;
  }

  public String getPkeyCertAlias()
  {
    return this.mUserPkeyPath;
  }

  public String getUserCertAlias()
  {
    return this.mUserCertPath;
  }

  public boolean installAllCerts(ArrayList<String> paramArrayList, String paramString1, PrivateKey paramPrivateKey, String paramString2)
  {
    boolean bool = false;
    if (this.mActiveMethod != null)
    {
      Util.log(this.mLogger, "Invalid internal state in installAllCerts()!");
      return false;
    }
    if ((paramArrayList == null) || (paramArrayList.size() <= 0) || (paramString1 == null) || (paramPrivateKey == null))
    {
      Util.log(this.mLogger, "Insufficient data to install certificates in installAllCerts()!");
      return false;
    }
    int i = this.mParser.savedConfigInfo.mLastCertInstallMethodUsed;
    if (i < this.mInstallMethods.size())
      if ((((CertInstallerBase)this.mInstallMethods.get(i)).canInstallAtOnce()) && ((paramArrayList.size() <= 1) || (((CertInstallerBase)this.mInstallMethods.get(i)).canInstallMultiCa())));
    do
    {
      i++;
      break;
      Util.log(this.mLogger, ((CertInstallerBase)this.mInstallMethods.get(i)).getInstallTypeName());
      this.mActiveMethod = ((CertInstallerBase)this.mInstallMethods.get(i));
      this.mActiveMethod.useInstallPassword(paramString2);
      bool = this.mActiveMethod.installAllCerts(getCertsAsString(paramArrayList), paramArrayList.size(), paramString1, paramPrivateKey);
      if (bool == true)
      {
        this.mParser.savedConfigInfo.mCertInstallWarning = this.mActiveMethod.getWarningString();
        this.mCaCertPath = this.mActiveMethod.getCaAlias();
        this.mUserCertPath = this.mActiveMethod.getUserAlias();
        this.mUserPkeyPath = this.mActiveMethod.getPrivateKeyAlias();
        this.mActiveMethod = null;
        return bool;
      }
    }
    while (this.mActiveMethod.isTerminalFailure() != true);
    return false;
  }

  public boolean installCaCerts(ArrayList<String> paramArrayList, String paramString)
  {
    boolean bool = false;
    if (this.mActiveMethod != null)
    {
      Util.log(this.mLogger, "Invalid internal state in installCaCerts()!");
      return false;
    }
    if ((paramArrayList == null) || (paramArrayList.size() <= 0))
    {
      Util.log(this.mLogger, "No CA certificates to install in installCaCerts()!");
      return false;
    }
    ArrayList localArrayList = filterCaChain(paramArrayList);
    String str = getCertsAsString(localArrayList);
    if (str == null)
    {
      Util.log(this.mLogger, "Couldn't convert certificate array in a flat string!");
      return false;
    }
    for (int i = this.mParser.savedConfigInfo.mLastCertInstallMethodUsed; ; i++)
    {
      if (i < this.mInstallMethods.size())
      {
        if (!((CertInstallerBase)this.mInstallMethods.get(i)).canInstallCa())
          continue;
        if (localArrayList.size() <= 1)
          break label243;
        if (!((CertInstallerBase)this.mInstallMethods.get(i)).canInstallMultiCa())
          continue;
        Util.log(this.mLogger, ((CertInstallerBase)this.mInstallMethods.get(i)).getInstallTypeName());
        this.mActiveMethod = ((CertInstallerBase)this.mInstallMethods.get(i));
        this.mActiveMethod.useInstallPassword(paramString);
        bool = this.mActiveMethod.installCaCert(str, localArrayList.size());
        if (bool != true)
          continue;
      }
      label243: 
      do
      {
        this.mParser.savedConfigInfo.mCertInstallWarning = this.mActiveMethod.getWarningString();
        this.mCaCertPath = this.mActiveMethod.getCaAlias();
        this.mActiveMethod = null;
        return bool;
        Util.log(this.mLogger, ((CertInstallerBase)this.mInstallMethods.get(i)).getInstallTypeName());
        this.mActiveMethod = ((CertInstallerBase)this.mInstallMethods.get(i));
        this.mActiveMethod.useInstallPassword(paramString);
        bool = this.mActiveMethod.installCaCert(str, localArrayList.size());
      }
      while (bool == true);
    }
  }

  public boolean installUserCert(String paramString1, PrivateKey paramPrivateKey, String paramString2)
  {
    boolean bool = false;
    if (this.mActiveMethod != null)
    {
      Util.log(this.mLogger, "Invalid internal state in installUserCert()!");
      return false;
    }
    if ((paramString1 == null) || (paramPrivateKey == null))
    {
      Util.log(this.mLogger, "Insufficient data to install a user certificate in installUserCert()!");
      return false;
    }
    for (int i = this.mParser.savedConfigInfo.mLastCertInstallMethodUsed; ; i++)
      if (i < this.mInstallMethods.size())
      {
        if (((CertInstallerBase)this.mInstallMethods.get(i)).canInstallUser())
        {
          Util.log(this.mLogger, ((CertInstallerBase)this.mInstallMethods.get(i)).getInstallTypeName());
          this.mActiveMethod = ((CertInstallerBase)this.mInstallMethods.get(i));
          this.mActiveMethod.useInstallPassword(paramString2);
          bool = this.mActiveMethod.installClientCert(paramString1, paramPrivateKey);
          if (bool != true);
        }
      }
      else
      {
        this.mParser.savedConfigInfo.mCertInstallWarning = this.mActiveMethod.getWarningString();
        this.mUserCertPath = this.mActiveMethod.getUserAlias();
        this.mUserPkeyPath = this.mActiveMethod.getPrivateKeyAlias();
        this.mActiveMethod = null;
        return bool;
      }
  }
}