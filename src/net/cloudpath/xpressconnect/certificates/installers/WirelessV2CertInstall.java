package net.cloudpath.xpressconnect.certificates.installers;

import android.app.Activity;
import android.content.Context;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import net.cloudpath.xpressconnect.AndroidVersion;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.certificates.CertUtils;
import net.cloudpath.xpressconnect.certificates.PEMValidator;
import net.cloudpath.xpressconnect.certificates.SortCertChains;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.nativeproxy.WifiConfigurationProxy;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.thread.ConfigureClientThread;

public class WirelessV2CertInstall extends CertInstallerBase
{
  protected Context mContext;
  protected FailureReason mFailure;
  protected SortCertChains mSorter = null;
  protected WifiConfigurationProxy mWifiConfigProxy = null;

  public WirelessV2CertInstall(Logger paramLogger, NetworkConfigParser paramNetworkConfigParser, FailureReason paramFailureReason, ConfigureClientThread paramConfigureClientThread, Activity paramActivity, WifiConfigurationProxy paramWifiConfigurationProxy)
  {
    super(paramLogger, paramNetworkConfigParser, paramConfigureClientThread, paramFailureReason, paramActivity);
    this.mWifiConfigProxy = paramWifiConfigurationProxy;
    this.mFailure = paramFailureReason;
    this.mContext = paramActivity;
    this.mSorter = new SortCertChains(paramLogger);
  }

  private boolean configureClientCert(PEMValidator paramPEMValidator, PrivateKey paramPrivateKey)
  {
    CertUtils localCertUtils = new CertUtils(this.mLogger);
    ArrayList localArrayList = paramPEMValidator.getCertData();
    if (localArrayList.size() > 1)
    {
      Util.log(this.mLogger, "Invalid internal state.  More than one user certificate in the validator object!?");
      return false;
    }
    X509Certificate localX509Certificate = localCertUtils.getX509Cert(getCertAsSingleString((String[])localArrayList.get(0)));
    return this.mWifiConfigProxy.setClientKeyEntry(paramPrivateKey, localX509Certificate);
  }

  private String getCertAsSingleString(String[] paramArrayOfString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    for (int i = 0; i < paramArrayOfString.length; i++)
      localStringBuilder.append(paramArrayOfString[i] + "\n");
    return localStringBuilder.toString();
  }

  private boolean installTypeValid()
  {
    return this.mWifiConfigProxy.getApiLevel() == 2;
  }

  public boolean canInstallAtOnce()
  {
    return installTypeValid();
  }

  public boolean canInstallCa()
  {
    return installTypeValid();
  }

  public boolean canInstallMultiCa()
  {
    return installTypeValid();
  }

  public boolean canInstallUser()
  {
    return installTypeValid();
  }

  public String getCaAlias()
  {
    return null;
  }

  protected ArrayList<X509Certificate> getChain(PEMValidator paramPEMValidator1, PEMValidator paramPEMValidator2, boolean paramBoolean)
  {
    if (paramPEMValidator1 == null)
    {
      Util.log(this.mLogger, "Invalid data passed to getChain()!");
      return null;
    }
    ArrayList localArrayList1 = paramPEMValidator1.getCertData();
    if (localArrayList1 == null)
    {
      Util.log(this.mLogger, "No data returned from getChain()!");
      return null;
    }
    this.mSorter.clear();
    for (int i = 0; i < localArrayList1.size(); i++)
      this.mSorter.addCert(getCertAsSingleString((String[])localArrayList1.get(i)));
    if (!this.mSorter.findChains())
    {
      Util.log(this.mLogger, "Cert data appears to only have a single chain.");
      return this.mSorter.getChain(0);
    }
    if (this.mSorter.size() >= 2)
    {
      if (paramPEMValidator2 == null)
      {
        Util.log(this.mLogger, "No user cert specified, but more than one chain found!?");
        if (this.mFailure != null)
          this.mFailure.setFailReason(FailureReason.useWarningIcon, "Unable to install the certificate data.  This is usually the result of a server misconfiguration.  Please report it to your helpdesk.", 2);
        return null;
      }
      ArrayList localArrayList2 = paramPEMValidator2.getCertData();
      if (localArrayList2.size() > 1)
      {
        Util.log(this.mLogger, "More than one user certificate found after validating certs?!");
        if (this.mFailure != null)
          this.mFailure.setFailReason(FailureReason.useWarningIcon, "Unable to install the certificate data.  This is usually the result of a server misconfiguration.  Please report it to your helpdesk.", 2);
        return null;
      }
      String[] arrayOfString = (String[])localArrayList2.get(0);
      StringBuilder localStringBuilder = new StringBuilder();
      for (int j = 0; j < arrayOfString.length; j++)
        localStringBuilder.append(arrayOfString[j] + "\n");
      int k = this.mSorter.chainForUserCert(localStringBuilder.toString());
      if (this.mSorter.size() > 2)
      {
        Util.log(this.mLogger, "+++ There are more than two CA roots in the configuration.  The authentication may fail.");
        if (this.mFailure != null)
          this.mFailure.setFailReason(FailureReason.useWarningIcon, "There were too many certificate chains found.  This app can handle one chain for the user certificate and one chain for the RADIUS server certificate.  However, " + this.mSorter.size() + " chain(s) were found!", 2);
        return null;
      }
      if (paramBoolean)
      {
        if (k < 0)
        {
          Util.log(this.mLogger, "We couldn't locate the user cert in the array of available certificates!");
          if (this.mFailure != null)
            this.mFailure.setFailReason(FailureReason.useErrorIcon, "The user certificate couldn't be mapped to an available certificate chain.  Please contact support.");
          return null;
        }
        return this.mSorter.getChain(k);
      }
      for (int m = 0; m < this.mSorter.size(); m++)
        if (m != k)
          return this.mSorter.getChain(m);
    }
    if ((this.mSorter.size() == 1) && (!paramBoolean))
      return this.mSorter.getChain(0);
    if (this.mFailure != null)
      this.mFailure.setFailReason(FailureReason.useWarningIcon, "There were too many certificate chains found.  This app can handle one chain for the user certificate and one chain for the RADIUS server certificate.  However, " + this.mSorter.size() + " chain(s) were found!", 2);
    return null;
  }

  protected int getChainCount(PEMValidator paramPEMValidator)
  {
    if (paramPEMValidator == null)
    {
      Util.log(this.mLogger, "Unable to check cert chains because none were defined!");
      return 0;
    }
    ArrayList localArrayList = paramPEMValidator.getCertData();
    if (localArrayList == null)
    {
      Util.log(this.mLogger, "No data returned from getCertData() in hasMultiChains()!");
      return 0;
    }
    this.mSorter.clear();
    for (int i = 0; i < localArrayList.size(); i++)
      this.mSorter.addCert(getCertAsSingleString((String[])localArrayList.get(i)));
    if (!this.mSorter.findChains())
    {
      Util.log(this.mLogger, "Cert data appears to only have a single chain.");
      return 0;
    }
    return this.mSorter.size();
  }

  public String getInstallTypeName()
  {
    return "Using V2 certificate install method.";
  }

  protected X509Certificate getRootForChain(ArrayList<X509Certificate> paramArrayList)
  {
    CertUtils localCertUtils = new CertUtils(this.mLogger);
    for (int i = 0; ; i++)
    {
      int j = paramArrayList.size();
      X509Certificate localX509Certificate = null;
      if (i < j)
      {
        if ((localCertUtils.certIsCa((Certificate)paramArrayList.get(i))) && (localCertUtils.certIsRoot((Certificate)paramArrayList.get(i))))
          localX509Certificate = (X509Certificate)paramArrayList.get(i);
      }
      else
      {
        if (localX509Certificate == null)
        {
          Util.log(this.mLogger, "No root CA certificate was found in the chain?!");
          localX509Certificate = null;
        }
        return localX509Certificate;
      }
    }
  }

  protected ArrayList<X509Certificate> getUnsortedCertList(PEMValidator paramPEMValidator)
  {
    ArrayList localArrayList1 = paramPEMValidator.getCertData();
    CertUtils localCertUtils = new CertUtils(this.mLogger);
    ArrayList localArrayList2 = new ArrayList();
    for (int i = 0; ; i++)
    {
      X509Certificate localX509Certificate;
      if (i < localArrayList1.size())
      {
        localX509Certificate = localCertUtils.getX509Cert(getCertAsSingleString((String[])localArrayList1.get(i)));
        if (localX509Certificate == null)
        {
          Util.log(this.mLogger, "Unable to convert PEM cert to x.509 class.");
          localArrayList2 = null;
        }
      }
      else
      {
        return localArrayList2;
      }
      localArrayList2.add(localX509Certificate);
    }
  }

  public String getUserAlias()
  {
    return null;
  }

  public boolean installAllCerts(String paramString1, int paramInt, String paramString2, PrivateKey paramPrivateKey)
  {
    boolean bool1 = Util.usingCaValidation(this.mParser, this.mLogger);
    if (!checkKeyStoreState())
      return false;
    if (Util.stringIsEmpty(paramString2))
    {
      Util.log(this.mLogger, "The cert string is empty. (v2)");
      return false;
    }
    PEMValidator localPEMValidator1 = validateRootCertificates(paramString1, paramInt);
    if (localPEMValidator1 == null)
    {
      Util.log(this.mLogger, "Failed to validate the root/intermediate certificate(s)...");
      this.mFailure.setFailReason(FailureReason.useErrorIcon, "A root or intermediate certificate authority isn't in the proper format. Please contact your help desk.", 2);
      return false;
    }
    PEMValidator localPEMValidator2 = validateUserCertificate(paramString2);
    if (localPEMValidator2 == null)
    {
      Util.log(this.mLogger, "Failed to validate the user certificate(s)...");
      this.mFailure.setFailReason(FailureReason.useErrorIcon, "The user certificate that was provided is not in a valid format for installing.  Please contact your help desk for additional support.", 2);
      return false;
    }
    Util.log(this.mLogger, "Certificate(s) validated...");
    switch (getChainCount(localPEMValidator1))
    {
    default:
      return installMultipleChains(localPEMValidator1, localPEMValidator2, paramPrivateKey);
    case 0:
      if (bool1)
      {
        Util.log(this.mLogger, "No certificate chains were found, but CA validation is configured to be required.");
        this.mFailure.setFailReason(FailureReason.useErrorIcon, "The configuration indicated that we should validate the server certificate,  but no validation information was provided.  Please contact your help desk.", 2);
        return false;
      }
      return true;
    case 1:
    }
    boolean bool2 = installRadiusValidationChain(localPEMValidator1, localPEMValidator2);
    if (bool2 == true)
      bool2 = installClientCert(paramString2, paramPrivateKey);
    return bool2;
  }

  public boolean installCaCert(String paramString, int paramInt, AndroidVersion paramAndroidVersion)
  {
    if (!checkKeyStoreState());
    X509Certificate localX509Certificate;
    do
    {
      ArrayList localArrayList;
      do
      {
        return false;
        PEMValidator localPEMValidator = new PEMValidator(this.mLogger, paramString);
        localPEMValidator.setExpectedCerts(paramInt);
        if (!localPEMValidator.isValid())
        {
          Util.log(this.mLogger, "Certificate data isn't in a valid format.  Will attempt to fix.");
          if (!localPEMValidator.attemptToFix())
          {
            Util.log(this.mLogger, "The certificate data provided couldn't be reformatted in to something we can use.  Cannot continue.");
            return false;
          }
        }
        switch (getChainCount(localPEMValidator))
        {
        default:
          Util.log(this.mLogger, "Too many certificate chains were provided to installCaCert().  We cannot support more than 1 chain with this call.");
          this.mFailure.setFailReason(FailureReason.useWarningIcon, "Unable to determine the correct certificates to install to validate the RADIUS server certificate.   Please contact your help desk.", 5);
          return false;
        case 0:
          if (Util.usingCaValidation(this.mLogger, this.mParser))
          {
            Util.log(this.mLogger, "RADIUS server certificate validation was configured to be required, but no certificates are available for doing the validation.");
            this.mFailure.setFailReason(FailureReason.useWarningIcon, "RADIUS server certificate validation was configured to be required, but no certificates are available for doing the validation.", 5);
            return false;
          }
          break;
        case 1:
        }
        localArrayList = getUnsortedCertList(localPEMValidator);
      }
      while (localArrayList == null);
      Util.log(this.mLogger, "Using wifi api 2 method.");
      localX509Certificate = getRootForChain(localArrayList);
    }
    while (localX509Certificate == null);
    return this.mWifiConfigProxy.setCaCertificate(localX509Certificate);
  }

  public boolean installClientCert(String paramString, PrivateKey paramPrivateKey, AndroidVersion paramAndroidVersion)
  {
    if (!checkKeyStoreState())
      return false;
    PEMValidator localPEMValidator = validateUserCertificate(paramString);
    if (localPEMValidator == null)
    {
      Util.log(this.mLogger, "Failed to validate the user certificate(s)...");
      this.mFailure.setFailReason(FailureReason.useErrorIcon, "The user certificate that was provided is not in a valid format for installing.  Please contact your help desk for additional support.", 2);
      return false;
    }
    return configureClientCert(localPEMValidator, paramPrivateKey);
  }

  protected boolean installMultipleChains(PEMValidator paramPEMValidator1, PEMValidator paramPEMValidator2, PrivateKey paramPrivateKey)
  {
    if (installRadiusValidationChain(paramPEMValidator1, paramPEMValidator2))
      return configureClientCert(paramPEMValidator2, paramPrivateKey);
    return false;
  }

  protected boolean installRadiusValidationChain(PEMValidator paramPEMValidator1, PEMValidator paramPEMValidator2)
  {
    X509Certificate localX509Certificate = getRootForChain(getChain(paramPEMValidator1, paramPEMValidator2, false));
    if (localX509Certificate == null)
    {
      Util.log(this.mLogger, "No root CA certificate was found in the chain?!");
      return false;
    }
    return this.mWifiConfigProxy.setCaCertificate(localX509Certificate);
  }

  protected PEMValidator validateCerts(String paramString, int paramInt)
  {
    if ((Util.stringIsEmpty(paramString)) && (paramInt > 0))
      Util.log(this.mLogger, "Cert is empty, but numcerts > 0!?");
    PEMValidator localPEMValidator = new PEMValidator(this.mLogger, paramString);
    localPEMValidator.setExpectedCerts(paramInt);
    if ((!localPEMValidator.isValid()) && (!localPEMValidator.attemptToFix()))
    {
      Util.log(this.mLogger, "Unable to correct PEM encoding on certificates.");
      localPEMValidator = null;
    }
    return localPEMValidator;
  }

  protected PEMValidator validateRootCertificates(String paramString, int paramInt)
  {
    Util.log(this.mLogger, "Validating CA certificate(s)...");
    PEMValidator localPEMValidator = validateCerts(paramString, paramInt);
    if (localPEMValidator == null)
    {
      Util.log(this.mLogger, "No root specified when one is required.");
      if (this.mFailure != null)
        this.mFailure.setFailReason(FailureReason.useErrorIcon, "No root CA was defined in the configuration, but the configuration requires one.  Please contact the help desk with this error.", 2);
      localPEMValidator = null;
    }
    return localPEMValidator;
  }

  protected PEMValidator validateUserCertificate(String paramString)
  {
    Util.log(this.mLogger, "Validating client cert...");
    PEMValidator localPEMValidator = validateCerts(paramString, 1);
    if (localPEMValidator == null)
    {
      Util.log(this.mLogger, "User certificate data couldn't be validated.");
      if (this.mFailure != null)
        this.mFailure.setFailReason(FailureReason.useErrorIcon, "The certificate data provided by the server appears to be invalid.  Please contact the help desk.", 2);
      localPEMValidator = null;
    }
    return localPEMValidator;
  }
}