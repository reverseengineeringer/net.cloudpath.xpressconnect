package net.cloudpath.xpressconnect;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build.VERSION;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.util.List;
import java.util.Locale;
import net.cloudpath.xpressconnect.certificates.PEMValidator;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.nativeproxy.WifiConfigurationProxy;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.ProfileElement;
import net.cloudpath.xpressconnect.parsers.config.SettingElement;

public class ConfigCheck
{
  private Context mContext = null;
  private FailureReason mFailure = null;
  private Logger mLogger = null;
  private NetworkConfigParser mParser = null;
  private int mSdkVer = -1;
  private WifiConfigurationProxy mWifiConfig = null;

  public ConfigCheck(NetworkConfigParser paramNetworkConfigParser, Logger paramLogger, WifiConfigurationProxy paramWifiConfigurationProxy, FailureReason paramFailureReason, Context paramContext)
  {
    this(paramNetworkConfigParser, paramLogger, paramWifiConfigurationProxy, paramFailureReason, paramContext, Build.VERSION.SDK_INT);
  }

  public ConfigCheck(NetworkConfigParser paramNetworkConfigParser, Logger paramLogger, WifiConfigurationProxy paramWifiConfigurationProxy, FailureReason paramFailureReason, Context paramContext, int paramInt)
  {
    this.mParser = paramNetworkConfigParser;
    this.mLogger = paramLogger;
    this.mWifiConfig = paramWifiConfigurationProxy;
    this.mFailure = paramFailureReason;
    this.mContext = paramContext;
    this.mSdkVer = paramInt;
  }

  protected boolean checkEapMethod()
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
      if (localSettingElement == null)
      {
        Util.log(this.mLogger, "No outer EAP method was defined in the configuration file!");
        return false;
      }
      if (this.mWifiConfig.getEap() != null)
        Util.log(this.mLogger, "Configured EAP method is : " + this.mWifiConfig.getEap());
      if (localSettingElement.requiredValue != null)
        Util.log(this.mLogger, "Expected EAP method is : " + localSettingElement.requiredValue);
    }
    while ((this.mWifiConfig.getEap() == null) || (localSettingElement.requiredValue == null) || (!this.mWifiConfig.getEap().toLowerCase(Locale.ENGLISH).equals(localSettingElement.requiredValue.toLowerCase())));
    return true;
  }

  protected boolean checkInnerMethod()
  {
    if (this.mParser == null)
      Util.log(this.mLogger, "No parser bound in setInnerMethod()!");
    SettingElement localSettingElement2;
    do
    {
      do
      {
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
        ParcelHelper localParcelHelper = new ParcelHelper("", this.mContext);
        this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(localParcelHelper.getIdentifier("xpc_no_eap_method_defined", "string")), 5);
        return false;
        if (localSettingElement1.requiredValue == null)
        {
          Util.log(this.mLogger, "No known EAP method to use in checkInnerMethod()!");
          return false;
        }
        if (localSettingElement1.requiredValue.toLowerCase(Locale.ENGLISH).equals("peap"))
          localSettingElement2 = this.mParser.selectedProfile.getSetting(2, 65003);
        while ((localSettingElement2 == null) || (localSettingElement2.requiredValue == null))
        {
          Util.log(this.mLogger, "No inner method was defined in the configuration file!");
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
        Util.log(this.mLogger, "Expected inner method : " + localSettingElement2.requiredValue);
      }
      while (this.mWifiConfig.getPhase2() == null);
      Util.log(this.mLogger, "Found inner method : " + this.mWifiConfig.getPhase2());
    }
    while (!this.mWifiConfig.getPhase2().toLowerCase(Locale.ENGLISH).contains(localSettingElement2.requiredValue.toLowerCase()));
    return true;
  }

  protected boolean checkOuterId()
  {
    if (this.mParser == null)
      Util.log(this.mLogger, "No parser bound in setOuterId()!");
    do
    {
      return false;
      if (this.mParser.selectedProfile == null)
      {
        Util.log(this.mLogger, "No profile selected in setOuterId()!");
        return false;
      }
      if (this.mParser.savedConfigInfo == null)
      {
        Util.log(this.mLogger, "No saved configuration data in checkOuterId()!");
        return false;
      }
      if (this.mParser.selectedProfile.getSetting(2, 40015) == null)
        return true;
    }
    while ((this.mWifiConfig == null) || (this.mWifiConfig.getAnonId() == null) || (!this.mWifiConfig.getAnonId().equals(this.mParser.savedConfigInfo.mOuterId)));
    return true;
  }

  protected boolean checkPsk()
  {
    String str = this.mWifiConfig.getPsk();
    if (str == null);
    while (!str.startsWith("*"))
      return false;
    return true;
  }

  protected boolean checkRootCA()
  {
    return checkRootCA(new AndroidVersion(this.mLogger));
  }

  protected boolean checkRootCA(AndroidVersion paramAndroidVersion)
  {
    if (this.mParser == null)
      Util.log(this.mLogger, "No parser bound in checkRootCA()!");
    do
    {
      String str;
      do
      {
        do
        {
          return false;
          if (this.mParser.selectedProfile == null)
          {
            Util.log(this.mLogger, "No profile selected in checkRootCA()!");
            return false;
          }
          if (!Util.usingCaValidation(this.mParser, this.mLogger))
          {
            Util.log(this.mLogger, "No certificate should be used.  Skipping that part of the configuration.");
            return true;
          }
          List localList = this.mParser.selectedProfile.getSettingMulti(0, 61301);
          if ((localList == null) || (localList.size() <= 0))
          {
            Util.log(this.mLogger, "No CA certificate was defined in the configuration file!");
            return false;
          }
          if (!Util.stringIsNotEmpty(this.mWifiConfig.getCaCert()))
            break;
        }
        while ((this.mSdkVer < 8) && (this.mWifiConfig.getCaCert().startsWith("\"/data/data")));
        if (this.mWifiConfig.getCaCert().toLowerCase(Locale.ENGLISH).startsWith("keystore"))
          break;
        str = Util.readFile(this.mWifiConfig.getCaCert());
      }
      while (str == null);
      if (!new PEMValidator(this.mLogger, str).isValid())
      {
        Util.log(this.mLogger, "Stored cert is invalid.  Will force reconfiguration...");
        return false;
      }
      return true;
    }
    while ((!paramAndroidVersion.allowCertlessDevices(this.mParser)) || (!paramAndroidVersion.cantUseCertificates()));
    Util.log(this.mLogger, "Skipping cert check because we allow devices that don't support certs.");
    return true;
  }

  protected boolean checkUserCert()
  {
    if (!this.mWifiConfig.getEap().toLowerCase(Locale.ENGLISH).equals("tls"));
    do
    {
      do
      {
        return true;
        if (this.mSdkVer >= 16)
          break;
      }
      while ((!Util.stringIsEmpty(this.mWifiConfig.getClientCertAlias())) && (!Util.stringIsEmpty(this.mWifiConfig.getClientPrivateKey())));
      do
      {
        return false;
        if (this.mWifiConfig.getApiLevel() >= 2)
          break;
      }
      while ((Util.stringIsEmpty(this.mWifiConfig.getEngine())) || (Util.stringIsEmpty(this.mWifiConfig.getEngineId())) || (Util.stringIsEmpty(this.mWifiConfig.getKeyId())) || (Util.stringIsEmpty(this.mWifiConfig.getClientCertAlias())));
      if (!this.mWifiConfig.getEngine().contentEquals("1"))
        return false;
      if (!this.mWifiConfig.getEngineId().toLowerCase(Locale.ENGLISH).contentEquals("keystore"))
        return false;
      if (!this.mWifiConfig.getKeyId().toLowerCase(Locale.ENGLISH).startsWith("usrpkey_"))
        return false;
    }
    while (this.mWifiConfig.getClientCertAlias().toLowerCase(Locale.ENGLISH).startsWith("keystore://usrcert_"));
    return false;
  }

  protected boolean checkUsernameAndPassword()
  {
    if (isTls());
    while (!Util.stringIsEmpty(this.mWifiConfig.getId()))
      return true;
    return false;
  }

  public boolean isTls()
  {
    if ((this.mWifiConfig == null) || (this.mWifiConfig.getEap() == null))
      Util.log(this.mLogger, "Invalid settings in isTls()!");
    while (!this.mWifiConfig.getEap().toLowerCase(Locale.ENGLISH).equals("tls"))
      return false;
    return true;
  }

  public boolean ssidSettingsAreCorrect()
  {
    if (!ssidsMatch())
    {
      Util.log(this.mLogger, "SSIDs didn't match!");
      return false;
    }
    if (!Util.usingPsk(this.mLogger, this.mParser))
    {
      if (!checkUsernameAndPassword())
      {
        Util.log(this.mLogger, "Username and password aren't set.");
        return false;
      }
      if (!checkOuterId())
      {
        Util.log(this.mLogger, "Outer identity doesn't match.");
        return false;
      }
      if (!checkEapMethod())
      {
        Util.log(this.mLogger, "EAP method doesn't match.");
        return false;
      }
      if (!checkInnerMethod())
      {
        Util.log(this.mLogger, "Inner method doesn't match.");
        return false;
      }
      if (!checkRootCA())
      {
        Util.log(this.mLogger, "Root CA doesn't match.");
        return false;
      }
      if (!checkUserCert())
      {
        Util.log(this.mLogger, "User cert/key doesn't match.");
        return false;
      }
    }
    else if (!checkPsk())
    {
      Util.log(this.mLogger, "PSK isn't set.");
      return false;
    }
    return true;
  }

  protected boolean ssidsMatch()
  {
    String str = Util.getSSID(this.mLogger, this.mParser);
    if (str == null);
    while ((this.mWifiConfig == null) || (this.mWifiConfig.getSsid() == null) || ((!this.mWifiConfig.getSsid().equals(str)) && (!this.mWifiConfig.getSsid().equals("\"" + str + "\""))))
      return false;
    return true;
  }
}