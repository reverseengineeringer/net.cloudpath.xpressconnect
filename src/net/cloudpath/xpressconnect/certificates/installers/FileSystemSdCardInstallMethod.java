package net.cloudpath.xpressconnect.certificates.installers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Environment;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Iterator;
import net.cloudpath.xpressconnect.AndroidVersion;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.certificates.PEMValidator;
import net.cloudpath.xpressconnect.checks.LocalFileHandler;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkItemElement;
import net.cloudpath.xpressconnect.parsers.config.ProfileElement;
import net.cloudpath.xpressconnect.parsers.config.SettingElement;
import net.cloudpath.xpressconnect.thread.ConfigureClientThread;

public class FileSystemSdCardInstallMethod extends CertInstallerBase
{
  protected String mCaCertPath = null;
  protected Context mContext = null;
  protected FailureReason mFailure = null;
  protected LocalFileHandler mFileHandler = null;
  protected ParcelHelper mHelp = null;
  protected String mUserCertPath = null;
  protected String mUserPkeyPath = null;

  public FileSystemSdCardInstallMethod(Logger paramLogger, NetworkConfigParser paramNetworkConfigParser, FailureReason paramFailureReason, ConfigureClientThread paramConfigureClientThread, Activity paramActivity, Context paramContext)
  {
    super(paramLogger, paramNetworkConfigParser, paramConfigureClientThread, paramFailureReason, paramActivity);
    this.mFailure = paramFailureReason;
    this.mContext = paramContext;
    this.mFileHandler = new LocalFileHandler(paramContext, paramLogger);
    this.mHelp = new ParcelHelper("", paramContext);
  }

  @SuppressLint({"NewApi"})
  private boolean fsPermissionsAreValid(String paramString)
  {
    File localFile = new File(paramString);
    if (Build.VERSION.SDK_INT >= 9)
      localFile.setReadable(true, false);
    while (true)
    {
      if (!this.mFileHandler.isWorldReadable(paramString))
        Util.log(this.mLogger, "(warning) " + paramString + " is not world readable, or state can't be determined.");
      return true;
      LocalFileHandler.setWorldReadable(paramString);
    }
  }

  protected boolean blockKeyStoreLackingDevices()
  {
    if ((this.mParser == null) || (this.mParser.selectedProfile == null))
      Util.log(this.mLogger, "No parser or profile selected.  Returning false in blockKeyStoreLackingDevices()!");
    SettingElement localSettingElement;
    do
    {
      return false;
      localSettingElement = this.mParser.selectedProfile.getSetting(2, 80033);
    }
    while ((localSettingElement == null) || (localSettingElement.requiredValue == null) || (Util.parseInt(localSettingElement.requiredValue, 0) != 1));
    if (this.mFailure != null)
      this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_invalid_install_method", "string")), 2);
    return true;
  }

  public boolean canInstallCa()
  {
    return (isFsInstallAllowed()) && (!blockKeyStoreLackingDevices()) && (!Util.isTls(this.mLogger, this.mParser, this.mContext, this.mFailure));
  }

  public boolean canInstallMultiCa()
  {
    return canInstallCa();
  }

  public boolean canInstallUser()
  {
    return canInstallCa();
  }

  public String getCaAlias()
  {
    return this.mCaCertPath;
  }

  public String getInstallTypeName()
  {
    return "Using SDC certificate install method.";
  }

  public String getPrivateKeyAlias()
  {
    return this.mUserPkeyPath;
  }

  public String getUserAlias()
  {
    return this.mUserCertPath;
  }

  public String getWarningString()
  {
    return "<font color='blue'>" + this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_no_rerun_need", "string")) + "</font>";
  }

  public boolean installCaCert(String paramString, int paramInt, AndroidVersion paramAndroidVersion)
  {
    return installCaCert(paramString, paramInt, paramAndroidVersion, Util.isStorageReady(this.mLogger, this.mFailure));
  }

  protected boolean installCaCert(String paramString, int paramInt, AndroidVersion paramAndroidVersion, boolean paramBoolean)
  {
    if ((this.mParser == null) || (this.mParser.selectedNetwork == null) || (this.mParser.selectedNetwork.name == null))
      Util.log(this.mLogger, "Parser is NULL in install workaround 1!");
    do
    {
      do
        return false;
      while (blockKeyStoreLackingDevices() == true);
      if (!paramBoolean)
      {
        Util.log(this.mLogger, "The storage device is unavailable, or cannot be written!");
        if ((this.mAndroidVersion.allowCertlessDevices(this.mParser)) && (this.mAndroidVersion.cantUseCertificates()))
        {
          Util.log(this.mLogger, "Device will be allowed on, even though it doesn't support certificates.");
          return true;
        }
        Util.log(this.mLogger, "Device will NOT be allowed on the network because it either supports certificates, or 'allow certless devices' isn't enabled.");
        return false;
      }
      if (paramString == null)
      {
        Util.log(this.mLogger, "No certificate passed in to install in workaround 1!");
        return false;
      }
      String str = "CACERT_" + sanitize(this.mParser.selectedNetwork.name);
      if (installToFs(str, paramString, paramInt, false))
      {
        Util.log(this.mLogger, "Installed certificate using alternate method.");
        this.mCaCertPath = (Environment.getExternalStorageDirectory() + "/xpc_certificates/" + str + ".pem");
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
      Util.log(this.mLogger, "Invalid user certificate or private key when attempting to install with workaround 1.");
    do
    {
      do
      {
        do
          return false;
        while (blockKeyStoreLackingDevices() == true);
        if ((this.mParser == null) || (this.mParser.selectedNetwork == null) || (this.mParser.selectedNetwork.name == null))
        {
          Util.log(this.mLogger, "Configuration isn't valid while trying to install user certificates with workaround 1.");
          return false;
        }
        if (!installToFs("USER_" + sanitize(this.mParser.selectedNetwork.name), paramString, 1, false))
          break;
        Util.log(this.mLogger, "Installed user certificate to alternate location.");
        this.mUserCertPath = (Environment.getExternalStorageDirectory() + "/xpc_certificates/USER_" + sanitize(this.mParser.selectedNetwork.name) + ".pem");
      }
      while (!writePrivateKey(paramPrivateKey, Environment.getExternalStorageDirectory() + "/xpc_certificates/USRPKEY_" + sanitize(this.mParser.selectedNetwork.name) + ".pem"));
      Util.log(this.mLogger, "Installed user private key to alternate location.");
      this.mUserPkeyPath = (Environment.getExternalStorageDirectory() + "/xpc_certificates/USRPKEY_" + sanitize(this.mParser.selectedNetwork.name) + ".pem");
      return true;
    }
    while (this.mFailure == null);
    this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_device_cant_store_certs", "string")), 2);
    return false;
  }

  protected boolean installToFs(String paramString1, String paramString2, int paramInt, boolean paramBoolean)
  {
    if (blockKeyStoreLackingDevices() == true)
      return false;
    if (!paramBoolean)
      if (!new File(Environment.getExternalStorageDirectory() + "/xpc_certificates").mkdir())
        Util.log(this.mLogger, "!*!*! Unable to create directory to store certificates.  It may already exist, or the SD card may be read only.");
    PEMValidator localPEMValidator;
    for (String str = Environment.getExternalStorageDirectory() + "/xpc_certificates/" + paramString1 + ".pem"; ; str = this.mFileHandler.getPathToAppDirectory() + "/" + paramString1 + ".pem")
    {
      localPEMValidator = new PEMValidator(this.mLogger, paramString2);
      localPEMValidator.setExpectedCerts(paramInt);
      if ((localPEMValidator.isValid()) || (localPEMValidator.attemptToFix()))
        break;
      Util.log(this.mLogger, "Certificate validation failed, and could not be repaired.");
      if (this.mFailure != null)
        this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_cant_load_ca_cert", "string")), 2);
      return false;
    }
    PrintWriter localPrintWriter;
    try
    {
      localPrintWriter = new PrintWriter(new FileWriter(str));
      Iterator localIterator = localPEMValidator.getCertData().iterator();
      while (localIterator.hasNext())
      {
        String[] arrayOfString = (String[])localIterator.next();
        for (int i = 0; i < arrayOfString.length; i++)
          localPrintWriter.println(arrayOfString[i]);
      }
    }
    catch (IOException localIOException)
    {
      Util.log(this.mLogger, "I/O Exception writing the certificate file.  Is there an SD card in the device?");
      localIOException.printStackTrace();
      return false;
    }
    localPrintWriter.close();
    return fsPermissionsAreValid(str);
  }

  protected boolean isFsInstallAllowed()
  {
    boolean bool = true;
    if ((this.mAndroidVersion.forceUsingPkcs12() == bool) || (Build.VERSION.SDK_INT >= 16))
      bool = false;
    return bool;
  }

  // ERROR //
  protected boolean writePrivateKey(PrivateKey paramPrivateKey, String paramString)
  {
    // Byte code:
    //   0: new 312	java/io/FileWriter
    //   3: dup
    //   4: aload_2
    //   5: invokespecial 313	java/io/FileWriter:<init>	(Ljava/lang/String;)V
    //   8: astore_3
    //   9: new 361	org/bouncycastle2/openssl/PEMWriter
    //   12: dup
    //   13: aload_3
    //   14: invokespecial 362	org/bouncycastle2/openssl/PEMWriter:<init>	(Ljava/io/Writer;)V
    //   17: astore 4
    //   19: aload 4
    //   21: aload_1
    //   22: invokevirtual 366	org/bouncycastle2/openssl/PEMWriter:writeObject	(Ljava/lang/Object;)V
    //   25: aload 4
    //   27: invokevirtual 367	org/bouncycastle2/openssl/PEMWriter:close	()V
    //   30: aload_3
    //   31: invokevirtual 368	java/io/FileWriter:close	()V
    //   34: iconst_1
    //   35: ireturn
    //   36: astore 12
    //   38: aload 12
    //   40: invokevirtual 345	java/io/IOException:printStackTrace	()V
    //   43: aload_0
    //   44: getfield 74	net/cloudpath/xpressconnect/certificates/installers/FileSystemSdCardInstallMethod:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   47: ldc_w 370
    //   50: invokestatic 97	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   53: iconst_0
    //   54: ireturn
    //   55: astore 11
    //   57: aload 11
    //   59: invokevirtual 371	java/lang/SecurityException:printStackTrace	()V
    //   62: aload_0
    //   63: getfield 74	net/cloudpath/xpressconnect/certificates/installers/FileSystemSdCardInstallMethod:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   66: ldc_w 373
    //   69: invokestatic 97	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   72: iconst_0
    //   73: ireturn
    //   74: astore 10
    //   76: aload 10
    //   78: invokevirtual 374	java/lang/IllegalArgumentException:printStackTrace	()V
    //   81: aload_0
    //   82: getfield 74	net/cloudpath/xpressconnect/certificates/installers/FileSystemSdCardInstallMethod:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   85: ldc_w 376
    //   88: invokestatic 97	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   91: iconst_0
    //   92: ireturn
    //   93: astore 5
    //   95: aload 5
    //   97: invokevirtual 377	java/lang/Exception:printStackTrace	()V
    //   100: aload_0
    //   101: getfield 74	net/cloudpath/xpressconnect/certificates/installers/FileSystemSdCardInstallMethod:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   104: ldc_w 379
    //   107: invokestatic 97	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   110: aload 4
    //   112: invokevirtual 367	org/bouncycastle2/openssl/PEMWriter:close	()V
    //   115: iconst_0
    //   116: ireturn
    //   117: astore 6
    //   119: aload 6
    //   121: invokevirtual 345	java/io/IOException:printStackTrace	()V
    //   124: goto -9 -> 115
    //   127: astore 8
    //   129: aload 8
    //   131: invokevirtual 345	java/io/IOException:printStackTrace	()V
    //   134: aload_0
    //   135: getfield 74	net/cloudpath/xpressconnect/certificates/installers/FileSystemSdCardInstallMethod:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   138: ldc_w 381
    //   141: invokestatic 97	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   144: iconst_0
    //   145: ireturn
    //   146: astore 7
    //   148: aload 7
    //   150: invokevirtual 377	java/lang/Exception:printStackTrace	()V
    //   153: aload_0
    //   154: getfield 74	net/cloudpath/xpressconnect/certificates/installers/FileSystemSdCardInstallMethod:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   157: ldc_w 383
    //   160: invokestatic 97	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   163: iconst_0
    //   164: ireturn
    //   165: astore 9
    //   167: aload 9
    //   169: invokevirtual 345	java/io/IOException:printStackTrace	()V
    //   172: aload_0
    //   173: getfield 74	net/cloudpath/xpressconnect/certificates/installers/FileSystemSdCardInstallMethod:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   176: ldc_w 385
    //   179: invokestatic 97	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   182: iconst_0
    //   183: ireturn
    //
    // Exception table:
    //   from	to	target	type
    //   0	9	36	java/io/IOException
    //   9	19	55	java/lang/SecurityException
    //   9	19	74	java/lang/IllegalArgumentException
    //   19	25	93	java/lang/Exception
    //   110	115	117	java/io/IOException
    //   25	30	127	java/io/IOException
    //   25	30	146	java/lang/Exception
    //   30	34	165	java/io/IOException
  }
}