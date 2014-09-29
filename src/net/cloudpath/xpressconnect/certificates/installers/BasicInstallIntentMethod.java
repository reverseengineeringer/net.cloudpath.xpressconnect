package net.cloudpath.xpressconnect.certificates.installers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.cloudpath.xpressconnect.AndroidApiLevels;
import net.cloudpath.xpressconnect.AndroidVersion;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.certificates.CertUtils;
import net.cloudpath.xpressconnect.certificates.PEMValidator;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkItemElement;
import net.cloudpath.xpressconnect.thread.ConfigureClientThread;

public class BasicInstallIntentMethod extends SystemInstallIntentMethod
  implements FileFilter
{
  private boolean mResetting = false;

  public BasicInstallIntentMethod(Logger paramLogger, NetworkConfigParser paramNetworkConfigParser, FailureReason paramFailureReason, ConfigureClientThread paramConfigureClientThread, Context paramContext, Activity paramActivity)
  {
    super(paramLogger, paramNetworkConfigParser, paramFailureReason, paramConfigureClientThread, paramContext, paramActivity);
  }

  private List<File> getCertFileList()
  {
    ArrayList localArrayList = new ArrayList();
    File localFile = new File(Environment.getExternalStorageDirectory(), "download");
    if (localFile != null)
    {
      File[] arrayOfFile2 = localFile.listFiles(this);
      if (arrayOfFile2 != null)
        Collections.addAll(localArrayList, arrayOfFile2);
    }
    File[] arrayOfFile1 = Environment.getExternalStorageDirectory().listFiles(this);
    if (arrayOfFile1 != null)
      Collections.addAll(localArrayList, arrayOfFile1);
    return localArrayList;
  }

  private void undoFilenameChanges()
  {
    this.mResetting = true;
    List localList = getCertFileList();
    Util.log(this.mLogger, "Resetting " + localList.size() + " certificate file conflict(s).");
    if ((localList != null) && (!localList.isEmpty()))
    {
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        File localFile = (File)localIterator.next();
        localFile.renameTo(new File(localFile.getAbsoluteFile().toString().substring(0, -4 + localFile.getAbsoluteFile().toString().length())));
      }
    }
  }

  public boolean accept(File paramFile)
  {
    if (!this.mResetting)
      if (paramFile.isDirectory() != true);
    do
    {
      do
      {
        do
        {
          return false;
          if (paramFile.getName().endsWith(".p12"))
            return true;
          if (paramFile.getName().endsWith(".crt"))
            return true;
        }
        while (!paramFile.getName().endsWith(".pfx"));
        return true;
      }
      while (paramFile.isDirectory() == true);
      if (paramFile.getName().endsWith(".p12.xpc"))
        return true;
      if (paramFile.getName().endsWith(".crt.xpc"))
        return true;
    }
    while (!paramFile.getName().endsWith(".pfx.xpc"));
    return true;
  }

  protected boolean canInstallCa(int paramInt)
  {
    if (paramInt < AndroidApiLevels.ANDROID_2_2);
    while ((paramInt >= AndroidApiLevels.ANDROID_4_3) || (disallowKeystoreUse()))
      return false;
    if (blacklistedDevice(Build.BOARD, Build.MODEL, paramInt) == true)
    {
      Util.log(this.mLogger, "Disallowing keystore installs because device is on the black list.");
      return false;
    }
    return true;
  }

  public boolean canInstallMultiCa()
  {
    return (canInstallCa(Build.VERSION.SDK_INT)) && (Build.VERSION.SDK_INT >= AndroidApiLevels.ANDROID_4_0_3_UP);
  }

  public boolean canInstallUser()
  {
    return false;
  }

  protected boolean createCertFileForImport(String paramString1, String paramString2)
  {
    return createCertFileForImport(paramString1, paramString2, Environment.getExternalStorageDirectory().getAbsolutePath());
  }

  protected boolean createCertFileForImport(String paramString1, String paramString2, String paramString3)
  {
    if ((paramString1 == null) || (paramString2 == null) || (paramString3 == null))
      Util.log(this.mLogger, "Invalid data passed in to createCertFileForImport()!");
    while (!Util.isStorageReady(this.mLogger, this.mFailure))
      return false;
    this.mResetting = false;
    List localList = getCertFileList();
    Util.log(this.mLogger, "Found " + localList.size() + " certificate file conflict(s).");
    if ((localList != null) && (!localList.isEmpty()))
    {
      Iterator localIterator = localList.iterator();
      while (localIterator.hasNext())
      {
        File localFile = (File)localIterator.next();
        localFile.renameTo(new File(localFile.getAbsoluteFile() + ".xpc"));
      }
    }
    try
    {
      PrintWriter localPrintWriter = new PrintWriter(new FileWriter(paramString3 + "/" + paramString1 + ".crt"));
      localPrintWriter.println(paramString2);
      localPrintWriter.close();
      return true;
    }
    catch (IOException localIOException)
    {
      Util.log(this.mLogger, "I/O Exception writing the temporary certificate file.  Is there an SD card in the device?");
      localIOException.printStackTrace();
    }
    return false;
  }

  public String getInstallTypeName()
  {
    return "Using Basic certificate install method.";
  }

  protected String getRootCert(String paramString, int paramInt)
  {
    PEMValidator localPEMValidator = new PEMValidator(this.mLogger, paramString);
    CertUtils localCertUtils = new CertUtils(this.mLogger);
    localPEMValidator.setExpectedCerts(paramInt);
    String str;
    if ((!localPEMValidator.isValid()) && (!localPEMValidator.attemptToFix()))
    {
      Util.log(this.mLogger, "Unable to parse the certificates passed to getRootCert()!");
      str = null;
      return str;
    }
    ArrayList localArrayList = localPEMValidator.getCertData();
    for (int i = 0; ; i++)
    {
      if (i >= localArrayList.size())
        break label130;
      str = CertUtils.certStringArrayToString((String[])localArrayList.get(i));
      X509Certificate localX509Certificate = localCertUtils.getX509Cert(str);
      if ((localCertUtils.certIsCa(localX509Certificate)) && (localCertUtils.certIsRoot(localX509Certificate)))
        break;
    }
    label130: Util.log(this.mLogger, "Unable to find a root CA certificate in the bundle provided.");
    return null;
  }

  public String getWarningString()
  {
    String str = "<font color='blue'>" + this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_no_rerun_need", "string")) + "</font>";
    if (Build.VERSION.SDK_INT >= 14)
      str = "<font color='blue'>" + this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_uninstall_warning", "string")) + "</font><br/><br/>" + str;
    return str;
  }

  public boolean installCaCert(String paramString, int paramInt, AndroidVersion paramAndroidVersion)
  {
    String str = paramString;
    if (paramInt > 1)
      str = getRootCert(paramString, paramInt);
    return installCertBasic(str, true);
  }

  protected boolean installCertBasic(String paramString, boolean paramBoolean)
  {
    if (paramString == null)
      Util.log(this.mLogger, "No certificate specified in install basic!");
    do
    {
      do
      {
        return false;
        if ((this.mParser == null) || (this.mParser.selectedNetwork == null) || (this.mParser.selectedNetwork.name == null))
        {
          Util.log(this.mLogger, "Invalid configuration in basic certificate installation!");
          return false;
        }
      }
      while (!checkKeyStoreState());
      resetCertCatcher();
      if (createCertFileForImport(sanitize(this.mParser.selectedNetwork.name), paramString))
        break;
    }
    while (this.mFailure == null);
    this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mContext.getResources().getString(this.mHelp.getIdentifier("xpc_cant_write_to_storage_device", "string")), 2);
    return false;
    showDialog(10007);
    Intent localIntent;
    if (Build.VERSION.SDK_INT < AndroidApiLevels.ANDROID_4_3)
    {
      localIntent = new Intent("android.credentials.INSTALL");
      targetCertInstallerViaIntent(localIntent);
      if (!paramBoolean)
        break label230;
    }
    label230: for (int i = 0; ; i = 1)
    {
      Util.log(this.mLogger, "Using post-3.0 method.");
      return finishCertInstall(localIntent, i, sanitize(this.mParser.selectedNetwork.name));
      localIntent = new Intent("android.credentials.INSTALL_AS_USER");
      localIntent.putExtra("install_as_uid", 1010);
      break;
    }
  }

  protected boolean waitForIntentReturn()
  {
    boolean bool = super.waitForIntentReturn();
    undoFilenameChanges();
    return bool;
  }
}