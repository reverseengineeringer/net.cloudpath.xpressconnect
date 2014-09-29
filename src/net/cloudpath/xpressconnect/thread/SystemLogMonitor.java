package net.cloudpath.xpressconnect.thread;

import android.os.Build.VERSION;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;

public class SystemLogMonitor extends ThreadBase
{
  public static final int EAP_FAILED = 3;
  public static final int EAP_STARTED = 1;
  public static final int EAP_TYPE_SELECTED = 2;
  private boolean mCare = false;
  public boolean mCertCantLoad = false;
  public boolean mCertExpired = false;
  public boolean mCertNotValidYet = false;
  public volatile boolean mEapFailure = false;
  private volatile int mEapState = 0;
  private volatile boolean mEnableKeyStore = false;
  private int mFailureCount = 0;
  public int mHighestEapState = 0;
  private Logger mLogger = null;
  public boolean mMskFailure = false;
  public boolean mPasswordBad = false;
  public boolean mPasswordExpired = false;
  public boolean mPeapV1RequiredFailure = false;
  private Process mProcess = null;
  public boolean mSelfSigned = false;
  public volatile boolean mServerFailure = false;
  private volatile boolean mSwitch = false;

  public SystemLogMonitor(Logger paramLogger)
  {
    super(paramLogger);
    this.mLogger = paramLogger;
    setName("XPCSystemLogMonitor");
  }

  private void processEapState(int paramInt)
  {
    switch (paramInt)
    {
    default:
      Util.log(this.mLogger, "Unknown EAP transition state : " + paramInt);
    case 1:
    case 2:
      do
      {
        do
        {
          return;
          if (this.mEapState != 0)
            break;
          Util.log(this.mLogger, "Initial EAP attempt started...");
          this.mEapState = paramInt;
        }
        while (this.mEapState <= this.mHighestEapState);
        this.mHighestEapState = this.mEapState;
        return;
        Util.log(this.mLogger, "Transitioning back to initial EAP startup.");
        this.mEapState = paramInt;
        return;
        if (this.mEapState != 1)
          break;
        Util.log(this.mLogger, "An EAP method has been selected.");
        this.mEapState = paramInt;
      }
      while (this.mEapState <= this.mHighestEapState);
      this.mHighestEapState = this.mEapState;
      return;
      Util.log(this.mLogger, "Got to EAP method selection without passing through a startup!?  Discarding!");
      return;
    case 3:
    }
    if (this.mEapState == 1)
    {
      Util.log(this.mLogger, "Got an EAP failure without selecting an EAP method.  The RADIUS server may be down.");
      this.mServerFailure = true;
      return;
    }
    if (this.mEapState == 2)
    {
      Util.log(this.mLogger, "Got an EAP failure after a method was selected.  Credentials are probably bad.");
      this.mEapFailure = true;
      if (this.mFailureCount >= 2)
        this.mPasswordBad = true;
      this.mFailureCount = (1 + this.mFailureCount);
      return;
    }
    Util.log(this.mLogger, "Got an EAP failure without transitioning through *ANY* other states!?");
  }

  public void die()
  {
    super.die();
  }

  public boolean enableKeystore()
  {
    return this.mEnableKeyStore;
  }

  protected void processLine(String paramString)
  {
    if (paramString.contains("TLS: Certificate verification failed, error 20"))
    {
      Util.log(this.mLogger, "Couldn't find the certificate to use.  Probably need to switch storage types.");
      Util.log(this.mLogger, "Triggering log line : " + paramString);
      this.mSwitch = true;
    }
    do
    {
      return;
      if (paramString.contains("TLS: Failed to set TLS connection parameters"))
      {
        Util.log(this.mLogger, "Couldn't store certificate to use.  May switch to alternate method.");
        Util.log(this.mLogger, "Triggering log line : " + paramString);
        this.mSwitch = true;
        return;
      }
      if (paramString.contains("(unable to get issuer certificate)"))
      {
        Util.log(this.mLogger, "Couldn't get the issuer certificate.  Switching to non-cert store method.");
        Util.log(this.mLogger, "Triggering log line : " + paramString);
        this.mSwitch = true;
        return;
      }
      if (paramString.contains("(self signed certificate in certificate chain)"))
      {
        Util.log(this.mLogger, "Self signed cert in the chain, and this device doesn't like it.");
        Util.log(this.mLogger, "Triggering log line : " + paramString);
        this.mSelfSigned = true;
        return;
      }
      if (paramString.contains("CTRL-EVENT-EAP-STARTED"))
      {
        Util.log(this.mLogger, "EAP authentication started.");
        processEapState(1);
        return;
      }
      if (paramString.contains("CTRL-EVENT-EAP-METHOD"))
      {
        Util.log(this.mLogger, "EAP method selected. (RADIUS server seems alive.)");
        processEapState(2);
        return;
      }
      if ((paramString.contains("CTRL-EVENT-EAP-FAILURE")) || (paramString.contains("EAP-MSCHAPV2: failure message:")) || (paramString.contains("EAP-MSCHAPV2: Password not configured")))
      {
        Util.log(this.mLogger, "EAP method failed.  (Bad credentials?)");
        processEapState(3);
        return;
      }
      if (paramString.contains("RX EAPOL:return ,due to keystore locked"))
      {
        Util.log(this.mLogger, "Device expects keystore to be unlocked even if certificates aren't being used.");
        this.mEnableKeyStore = true;
        return;
      }
      if (paramString.contains("EAP-MSCHAPV2: Password expired"))
      {
        Util.log(this.mLogger, "Server indicated the user's password has expired.");
        this.mPasswordExpired = true;
        return;
      }
      if (paramString.contains("Password not configured"))
      {
        Util.log(this.mLogger, "Supplicant indicated that the password was not configured/bad.");
        this.mPasswordBad = true;
        return;
      }
      if (paramString.contains("Failed to get master session key from EAPOL state machines"))
      {
        Util.log(this.mLogger, "The supplicant failed to get the MSK.");
        this.mMskFailure = true;
      }
      if (paramString.contains("EAP-PEAP: Failed to select forced PEAP version 1"))
      {
        Util.log(this.mLogger, "The supplicant is demanding PEAPv1, but the server is using PEAPv0.");
        this.mPeapV1RequiredFailure = true;
      }
      if (paramString.contains("certificate is not yet valid"))
      {
        Util.log(this.mLogger, "The certificate is not yet valid.  The clock on this device may be incorrect.");
        this.mCertNotValidYet = true;
      }
      if (paramString.contains("certificate has expired"))
      {
        Util.log(this.mLogger, "The certificate has expired.");
        this.mCertExpired = true;
      }
    }
    while (!paramString.contains("tls_connection_ca_cert - Failed to load root certificates"));
    Util.log(this.mLogger, "The certificate couldn't be loaded.");
    this.mCertCantLoad = true;
  }

  public void reset()
  {
    this.mCare = true;
    this.mSwitch = false;
    this.mEapFailure = false;
    this.mEapState = 0;
    this.mHighestEapState = 0;
    this.mServerFailure = false;
    this.mSelfSigned = false;
    this.mPasswordExpired = false;
    this.mMskFailure = false;
    this.mPeapV1RequiredFailure = false;
  }

  public void run()
  {
    int i = 0;
    super.run();
    if (Build.VERSION.SDK_INT >= 16)
      return;
    while (true)
    {
      BufferedReader localBufferedReader;
      try
      {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add("logcat");
        localArrayList.add("-s");
        localArrayList.add("wpa_supplicant:*");
        localArrayList.add("CredentialInstaller:*");
        localArrayList.add("CertInstaller:*");
        this.mProcess = Runtime.getRuntime().exec((String[])localArrayList.toArray(new String[5]));
        localBufferedReader = new BufferedReader(new InputStreamReader(this.mProcess.getInputStream()));
        Util.log(this.mLogger, "** Started log monitoring.");
        if (!localBufferedReader.ready())
          break label205;
        if (shouldDie())
        {
          Util.log(this.mLogger, "Terminating logging thread.");
          return;
        }
      }
      catch (IOException localIOException)
      {
        this.mProcess.destroy();
        Util.log(this.mLogger, "I/O Exception : " + localIOException.getMessage());
        return;
      }
      String str = localBufferedReader.readLine();
      if (i > 2)
      {
        if (this.mCare)
        {
          processLine(str);
          continue;
          label205: if (shouldDie())
            Util.log(this.mLogger, "Terminating logging thread.");
        }
      }
      else
        i++;
    }
  }

  public boolean switchCertType()
  {
    return this.mSwitch;
  }
}