package net.cloudpath.xpressconnect.certificates.installers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Build.VERSION;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import net.cloudpath.xpressconnect.AndroidVersion;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.certificates.CertUtils;
import net.cloudpath.xpressconnect.certificates.PEMValidator;
import net.cloudpath.xpressconnect.certificates.PKCS12CertFactory;
import net.cloudpath.xpressconnect.certificates.PRNGFixes;
import net.cloudpath.xpressconnect.certificates.SortCertChains;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.thread.ConfigureClientThread;

public class Pkcs12IntentInstallMethod extends SystemInstallIntentMethod
{
  protected boolean mAltDialogShown = false;
  protected int mCaCertCount = -1;
  protected String mCaCertStore = null;
  protected boolean mDialogShown = false;
  protected boolean mMultiChains = false;
  protected SortCertChains mSorter = null;

  public Pkcs12IntentInstallMethod(Logger paramLogger, NetworkConfigParser paramNetworkConfigParser, FailureReason paramFailureReason, ConfigureClientThread paramConfigureClientThread, Context paramContext, Activity paramActivity)
  {
    super(paramLogger, paramNetworkConfigParser, paramFailureReason, paramConfigureClientThread, paramContext, paramActivity);
    PRNGFixes.apply();
  }

  private String generateTwoLetterName()
  {
    Random localRandom = new Random();
    char[] arrayOfChar = new char[2];
    for (int i = 0; i < 2; i++)
      arrayOfChar[i] = "abcdefghijklmnopqrstuvwxyz".charAt(localRandom.nextInt("abcdefghijklmnopqrstuvwxyz".length()));
    return new String(arrayOfChar);
  }

  public boolean canInstallAtOnce()
  {
    boolean bool = true;
    if (blacklistedDevice(Build.BOARD, Build.MODEL, Build.VERSION.SDK_INT) == bool)
    {
      Util.log(this.mLogger, "Disallowing keystore installs because device is on the black list.");
      bool = false;
    }
    return bool;
  }

  public boolean canInstallCa()
  {
    if (disallowKeystoreUse())
      return false;
    if (blacklistedDevice(Build.BOARD, Build.MODEL, Build.VERSION.SDK_INT) == true)
    {
      Util.log(this.mLogger, "Disallowing keystore installs because device is on the black list.");
      return false;
    }
    return true;
  }

  public boolean canInstallMultiCa()
  {
    if (disallowKeystoreUse())
      return false;
    if (blacklistedDevice(Build.BOARD, Build.MODEL, Build.VERSION.SDK_INT) == true)
    {
      Util.log(this.mLogger, "Disallowing keystore installs because device is on the black list.");
      return false;
    }
    return true;
  }

  public boolean canInstallUser()
  {
    if (disallowKeystoreUse())
      return false;
    if (blacklistedDevice(Build.BOARD, Build.MODEL, Build.VERSION.SDK_INT) == true)
    {
      Util.log(this.mLogger, "Disallowing keystore installs because device is on the black list.");
      return false;
    }
    return true;
  }

  public void checkBlankCertName()
  {
    if (new AndroidVersion(this.mLogger).noCertHint())
    {
      Util.log(this.mLogger, "----> Running on a device that blanks the certificate hint.  Will generate short cert names.");
      this.mParser.savedConfigInfo.mBundleCertName = generateTwoLetterName();
      for (this.mParser.savedConfigInfo.mBundleAltCertName = null; (this.mParser.savedConfigInfo.mBundleAltCertName == null) || (this.mParser.savedConfigInfo.mBundleAltCertName.contentEquals(this.mParser.savedConfigInfo.mBundleCertName)); this.mParser.savedConfigInfo.mBundleAltCertName = generateTwoLetterName());
      Util.log(this.mLogger, "Primary : " + this.mParser.savedConfigInfo.mBundleCertName + "    Alt : " + this.mParser.savedConfigInfo.mBundleAltCertName);
    }
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
      this.mSorter.addCert(CertUtils.certStringArrayToString((String[])localArrayList1.get(i)));
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
      int j = this.mSorter.chainForUserCert(CertUtils.certStringArrayToString((String[])localArrayList2.get(0)));
      if (this.mSorter.size() > 2)
      {
        Util.log(this.mLogger, "+++ There are more than two CA roots in the configuration.  The authentication may fail.");
        if (this.mFailure != null)
          this.mFailure.setFailReason(FailureReason.useWarningIcon, "There were too many certificate chains found.  This app can handle one chain for the user certificate and one chain for the RADIUS server certificate.  However, " + this.mSorter.size() + " chain(s) were found!", 2);
        return null;
      }
      if (paramBoolean)
      {
        if (j < 0)
        {
          Util.log(this.mLogger, "We couldn't locate the user cert in the array of available certificates!");
          if (this.mFailure != null)
            this.mFailure.setFailReason(FailureReason.useErrorIcon, "The user certificate couldn't be mapped to an available certificate chain.  Please contact support.");
          return null;
        }
        return this.mSorter.getChain(j);
      }
      for (int k = 0; k < this.mSorter.size(); k++)
        if (k != j)
          return this.mSorter.getChain(k);
    }
    if ((this.mSorter.size() == 1) && (!paramBoolean))
      return this.mSorter.getChain(0);
    if (this.mFailure != null)
      this.mFailure.setFailReason(FailureReason.useWarningIcon, "There were too many certificate chains found.  This app can handle one chain for the user certificate and one chain for the RADIUS server certificate.  However, " + this.mSorter.size() + " chain(s) were found!", 2);
    return null;
  }

  public String getInstallTypeName()
  {
    return "Using PKCS#12 certificate install method.";
  }

  public String getWarningString()
  {
    String str = "<font color='blue'>" + this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_no_rerun_need", "string")) + "</font>";
    if (Build.VERSION.SDK_INT >= 14)
      str = "<font color='blue'>" + this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_uninstall_warning", "string")) + "</font><br/><br/>" + str;
    return str;
  }

  protected boolean handleDualCertChains(PEMValidator paramPEMValidator1, PEMValidator paramPEMValidator2)
  {
    ArrayList localArrayList = getChain(paramPEMValidator1, paramPEMValidator2, false);
    if (localArrayList == null);
    while (!installPkcs12NoUser(localArrayList))
      return false;
    return true;
  }

  protected int hasMultiChains(PEMValidator paramPEMValidator)
  {
    this.mMultiChains = false;
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
      this.mSorter.addCert(CertUtils.certStringArrayToString((String[])localArrayList.get(i)));
    if (!this.mSorter.findChains())
    {
      Util.log(this.mLogger, "Cert data appears to only have a single chain.");
      return 0;
    }
    if (this.mSorter.size() >= 2)
      this.mMultiChains = true;
    return this.mSorter.size();
  }

  protected boolean importCaCertsFromValidator(PEMValidator paramPEMValidator, PKCS12CertFactory paramPKCS12CertFactory)
  {
    boolean bool = true;
    if ((paramPEMValidator == null) || (paramPKCS12CertFactory == null))
    {
      Util.log(this.mLogger, "Null parameters in importCaCertsFromValidator()!");
      return false;
    }
    ArrayList localArrayList = paramPEMValidator.getCertData();
    if (localArrayList == null)
    {
      Util.log(this.mLogger, "Null data returned from getCertData() in importCaCertsFromValidator()!");
      return false;
    }
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
      if (!paramPKCS12CertFactory.addCertToStore(CertUtils.certStringArrayToString((String[])localIterator.next())))
        bool = false;
    return bool;
  }

  protected boolean importUserChainFromValidator(PEMValidator paramPEMValidator1, PEMValidator paramPEMValidator2, PKCS12CertFactory paramPKCS12CertFactory)
  {
    ArrayList localArrayList = getChain(paramPEMValidator1, paramPEMValidator2, true);
    if (localArrayList == null)
      return false;
    for (int i = 0; i < localArrayList.size(); i++)
      paramPKCS12CertFactory.addCertToStore((Certificate)localArrayList.get(i));
    return true;
  }

  protected boolean importUsrCertsFromValidator(PEMValidator paramPEMValidator, PrivateKey paramPrivateKey, PKCS12CertFactory paramPKCS12CertFactory)
  {
    ArrayList localArrayList = paramPEMValidator.getCertData();
    if (localArrayList.size() > 1)
    {
      Util.log(this.mLogger, "More than one user cert defined!");
      return false;
    }
    String[] arrayOfString = (String[])localArrayList.get(0);
    if (arrayOfString == null)
    {
      Util.log(this.mLogger, "No certificate found in user validator!");
      return false;
    }
    return paramPKCS12CertFactory.addUserCertToStore(CertUtils.certStringArrayToString(arrayOfString), paramPrivateKey);
  }

  public boolean installAllCerts(String paramString1, int paramInt, String paramString2, PrivateKey paramPrivateKey, AndroidVersion paramAndroidVersion)
  {
    this.mCaCertStore = paramString1;
    this.mCaCertCount = paramInt;
    if (!checkKeyStoreState())
      return false;
    checkBlankCertName();
    return installClientAsPkcs12(paramString2, paramPrivateKey, Util.usingCaValidation(this.mParser, this.mLogger), 2);
  }

  public boolean installCaCert(String paramString, int paramInt, AndroidVersion paramAndroidVersion)
  {
    if (paramString == null)
      Util.log(this.mLogger, "The cert data is null! (PKCS#12)");
    ArrayList localArrayList;
    do
    {
      PEMValidator localPEMValidator;
      do
      {
        return false;
        if (Util.stringIsEmpty(paramString))
        {
          Util.log(this.mLogger, "The cert string is empty. (PKCS#12)");
          return false;
        }
        if (paramInt <= 0)
        {
          Util.log(this.mLogger, "Number of certs to install was <= 0!  Invalid input!");
          return false;
        }
        Util.log(this.mLogger, "Validating CA cert(s)...");
        localPEMValidator = validateCerts(paramString, paramInt);
        if (localPEMValidator != null)
          break;
        Util.log(this.mLogger, "No root specified when one is required.");
      }
      while (this.mFailure == null);
      this.mFailure.setFailReason(FailureReason.useErrorIcon, "No root CA was defined in the configuration, but the configuration requires one.  Please contact the help desk with this error.", 2);
      return false;
      Util.log(this.mLogger, "Cert validation complete.");
      localArrayList = getChain(localPEMValidator, null, false);
    }
    while (!checkKeyStoreState());
    checkBlankCertName();
    showPkcs12WarningDialog();
    return installPkcs12NoUser(localArrayList);
  }

  protected boolean installClientAsPkcs12(String paramString, PrivateKey paramPrivateKey, boolean paramBoolean, int paramInt)
  {
    PKCS12CertFactory localPKCS12CertFactory = new PKCS12CertFactory(this.mLogger);
    AndroidVersion localAndroidVersion = new AndroidVersion(this.mLogger);
    if (Util.stringIsEmpty(paramString))
    {
      Util.log(this.mLogger, "The cert string is empty. (PKCS#12)");
      return false;
    }
    Util.log(this.mLogger, "Validating client cert...");
    PEMValidator localPEMValidator1 = validateCerts(paramString, 1);
    if (paramBoolean == true)
      Util.log(this.mLogger, "Validating CA cert(s)...");
    for (PEMValidator localPEMValidator2 = validateCerts(this.mCaCertStore, this.mCaCertCount); (localPEMValidator2 == null) && (paramBoolean == true); localPEMValidator2 = null)
    {
      Util.log(this.mLogger, "No root specified when one is required.");
      if (this.mFailure != null)
        this.mFailure.setFailReason(FailureReason.useErrorIcon, "No root CA was defined in the configuration, but the configuration requires one.  Please contact the help desk with this error.", 2);
      return false;
      Util.log(this.mLogger, "Not validating CA cert(s) because we shouldn't be using them.");
    }
    if (localPEMValidator1 == null)
    {
      Util.log(this.mLogger, "User certificate data couldn't be validated.");
      if (this.mFailure != null)
        this.mFailure.setFailReason(FailureReason.useErrorIcon, "The certificate data provided by the server appears to be invalid.  Please contact the help desk.", 2);
      return false;
    }
    Util.log(this.mLogger, "Cert validation complete.");
    int i = hasMultiChains(localPEMValidator2);
    if (i > 1)
    {
      if (!Util.isTls(this.mLogger, this.mParser, this.mContext, this.mFailure))
      {
        Util.log(this.mLogger, "Multiple chains configured, but can't be supported by Android.");
        if (this.mFailure != null)
          this.mFailure.setFailReason(FailureReason.useErrorIcon, "Multiple certificate chains were defined.  However, Android only supports a single chain for the configured EAP method.  Please contact your help desk for additional information.", 2);
        return false;
      }
      if (i > 2)
      {
        Util.log(this.mLogger, "More than 2 chains were found for a TLS configuration.  This isn't supported by Android.");
        if (this.mFailure != null)
          this.mFailure.setFailReason(FailureReason.useErrorIcon, "Too many certificate chains were defined.  Android will only support 2 certificate chains when used with EAP-TLS.  Please contact your help desk for additional information.", 2);
        return false;
      }
      if (paramBoolean)
      {
        if (!handleDualCertChains(localPEMValidator2, localPEMValidator1))
          return false;
        paramInt = 4;
      }
      if (!importUserChainFromValidator(localPEMValidator2, localPEMValidator1, localPKCS12CertFactory))
        return false;
      showPkcs12AltCaWarningDialog();
    }
    while (!importUsrCertsFromValidator(localPEMValidator1, paramPrivateKey, localPKCS12CertFactory))
    {
      return false;
      if ((this.mParser != null) && (this.mParser.savedConfigInfo != null))
        this.mParser.savedConfigInfo.mBundleAltCertName = this.mParser.savedConfigInfo.mBundleCertName;
      showPkcs12WarningDialog();
      if ((paramBoolean) && (!importCaCertsFromValidator(localPEMValidator2, localPKCS12CertFactory)))
        return false;
    }
    Intent localIntent;
    if (Build.VERSION.SDK_INT < 18)
    {
      localIntent = new Intent("android.credentials.INSTALL");
      targetCertInstallerViaIntent(localIntent);
      if ((this.mParser == null) || (this.mParser.savedConfigInfo == null) || (!Util.stringIsNotEmpty(this.mParser.savedConfigInfo.mBundleAltCertName)))
        break label565;
    }
    label565: for (String str = sanitize(this.mParser.savedConfigInfo.mBundleAltCertName); ; str = sanitize(Util.getSSID(this.mLogger, this.mParser) + "_altca"))
    {
      Util.log(this.mLogger, "Setting cert bundle name to : " + str);
      if (str != null)
        break label604;
      Util.log(this.mLogger, "Unable to determine a certificate name for this network in installClientAsPkcs12()!");
      return false;
      localIntent = new Intent("android.credentials.INSTALL_AS_USER");
      localIntent.putExtra("install_as_uid", 1010);
      break;
    }
    label604: if (Build.VERSION.SDK_INT >= 14)
    {
      localIntent.putExtra("name", str);
      if (Util.stringIsEmpty(this.mInstallPassword))
      {
        Util.log(this.mLogger, "No password set for generating PKCS#12 certificates.  Will use the default.");
        if ((Build.VERSION.SDK_INT >= 16) && (!localAndroidVersion.shouldForceCertInstallPassword(this.mParser, this.mLogger)))
          break label770;
      }
    }
    byte[] arrayOfByte;
    label770: for (this.mInstallPassword = "password"; ; this.mInstallPassword = "")
    {
      while (true)
      {
        arrayOfByte = localPKCS12CertFactory.getPkcs12Stream(this.mInstallPassword, str);
        if ((arrayOfByte != null) && (arrayOfByte.length > 0))
          break label780;
        Util.log(this.mLogger, "Unable to install certificate data.");
        if (this.mFailure != null)
          this.mFailure.setFailReason(FailureReason.useWarningIcon, "Unable to install the certificate data.  This is usually the result of a server misconfiguration.  Please report it to your helpdesk.", 2);
        return false;
        try
        {
          localIntent.putExtra("name", str.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException localUnsupportedEncodingException)
        {
          localUnsupportedEncodingException.printStackTrace();
          Util.log(this.mLogger, "Couldn't convert string to byte[].  No certificate name hint will be provided.");
        }
      }
      break;
    }
    label780: localIntent.putExtra("PKCS12", arrayOfByte);
    Util.log(this.mLogger, "Using PKCS#12 install method. (Type = " + paramInt + ")");
    return finishCertInstall(localIntent, paramInt, str);
  }

  public boolean installClientCert(String paramString, PrivateKey paramPrivateKey, AndroidVersion paramAndroidVersion)
  {
    if (!checkKeyStoreState())
      return false;
    checkBlankCertName();
    showPkcs12WarningDialog();
    return installClientAsPkcs12(paramString, paramPrivateKey, Util.usingCaValidation(this.mParser, this.mLogger), 4);
  }

  @SuppressLint({"TrulyRandom"})
  protected boolean installPkcs12NoUser(ArrayList<X509Certificate> paramArrayList)
  {
    PKCS12CertFactory localPKCS12CertFactory = new PKCS12CertFactory(this.mLogger);
    AndroidVersion localAndroidVersion = new AndroidVersion(this.mLogger);
    if (paramArrayList == null)
    {
      Util.log(this.mLogger, "No chain provided in installPkcs12NoUser()!");
      return false;
    }
    for (int i = 0; i < -1 + paramArrayList.size(); i++)
      if (!localPKCS12CertFactory.addCertToStore((Certificate)paramArrayList.get(i)))
      {
        Util.log(this.mLogger, "Unable to add certificate to the PKCS#12 chain.");
        return false;
      }
    Intent localIntent;
    String str;
    byte[] arrayOfByte;
    while (true)
    {
      try
      {
        localPKCS12CertFactory.addUserCertToStore((Certificate)paramArrayList.get(-1 + paramArrayList.size()), KeyPairGenerator.getInstance("RSA").generateKeyPair().getPrivate());
        showPkcs12WarningDialog();
        if (Build.VERSION.SDK_INT < 18)
        {
          localIntent = new Intent("android.credentials.INSTALL");
          targetCertInstallerViaIntent(localIntent);
          if (!Util.stringIsNotEmpty(this.mParser.savedConfigInfo.mBundleCertName))
            break label419;
          str = sanitize(this.mParser.savedConfigInfo.mBundleCertName);
          Util.log(this.mLogger, "Setting cert name to : " + str);
          this.mCaCertPath = str;
          if (Build.VERSION.SDK_INT < 14)
            break label439;
          localIntent.putExtra("name", str);
          if (Util.stringIsEmpty(this.mInstallPassword))
          {
            Util.log(this.mLogger, "No password set for generating PKCS#12 certificates.  Will use the default.");
            if ((Build.VERSION.SDK_INT >= 16) && (!localAndroidVersion.shouldForceCertInstallPassword(this.mParser, this.mLogger)))
              break label479;
            this.mInstallPassword = "password";
          }
          arrayOfByte = localPKCS12CertFactory.getPkcs12Stream(this.mInstallPassword, str);
          if (arrayOfByte != null)
            break;
          Util.log(this.mLogger, "Unable to install certificate data.");
          if (this.mFailure != null)
            this.mFailure.setFailReason(FailureReason.useWarningIcon, "Unable to install the certificate data.  This is usually the result of a server misconfiguration.  Please report it to your helpdesk.", 2);
          return false;
        }
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
      {
        Util.log(this.mLogger, "Unable to install certificate chain!  Error : " + localNoSuchAlgorithmException.getLocalizedMessage());
        localNoSuchAlgorithmException.printStackTrace();
        if (this.mFailure != null)
          this.mFailure.setFailReason(FailureReason.useWarningIcon, "Unable to install the certificate data.  This is usually the result of a server misconfiguration.  Please report it to your helpdesk.", 2);
        return false;
      }
      localIntent = new Intent("android.credentials.INSTALL_AS_USER");
      localIntent.putExtra("install_as_uid", 1010);
      continue;
      label419: str = sanitize(Util.getSSID(this.mLogger, this.mParser));
      continue;
      try
      {
        label439: localIntent.putExtra("name", str.getBytes("UTF-8"));
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        localUnsupportedEncodingException.printStackTrace();
        Util.log(this.mLogger, "Couldn't convert string to byte[].  No certificate name hint will be provided.");
      }
      continue;
      label479: this.mInstallPassword = "";
    }
    localIntent.putExtra("PKCS12", arrayOfByte);
    this.mCaCertPath = ("keystore://CACERT_" + str);
    Util.log(this.mLogger, "Using tweaked PKCS#12 method.");
    return finishCertInstall(localIntent, 3, sanitize(str));
  }

  protected void showPkcs12AltCaWarningDialog()
  {
    if (this.mAltDialogShown == true)
      return;
    Util.log(this.mLogger, "Showing pkcs12 alt ca warning.");
    showDialog(10004);
    this.mAltDialogShown = true;
  }

  protected void showPkcs12WarningDialog()
  {
    if (this.mDialogShown == true)
      return;
    if (this.mMultiChains == true)
    {
      Util.log(this.mLogger, "Showing alt password warning dialog.");
      showDialog(10005);
    }
    while (true)
    {
      this.mDialogShown = true;
      return;
      Util.log(this.mLogger, "Showing std password warning dialog.");
      if ((Build.MANUFACTURER.toLowerCase(Locale.ENGLISH).contentEquals("samsung")) && (Build.VERSION.SDK_INT == 18))
        showDialog(10006);
      else
        showDialog(10002);
    }
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
}