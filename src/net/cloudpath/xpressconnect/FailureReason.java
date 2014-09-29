package net.cloudpath.xpressconnect;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build.VERSION;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.thread.SystemLogMonitor;

public class FailureReason
{
  public static int useErrorIcon = 2;
  public static int useWarningIcon = 1;
  private int failIssue = -1;
  private String failureString = null;
  private int iconToUse = 0;
  public boolean isTerminal = false;
  public boolean mAssocFailure = false;
  public boolean mCertCantLoad = false;
  public boolean mCertExpired = false;
  public boolean mCertNotYetValid = false;
  public boolean mConnectionAttempted = false;
  public boolean mCredsLikelyBad = false;
  protected boolean mEapFailure = false;
  public boolean mEnableNetworkFail = false;
  protected int mHighestEapState = 0;
  public boolean mKeyFailure = false;
  public int mLastWifiState = 0;
  protected boolean mMskFailure = false;
  public boolean mNoConnectionFound = false;
  protected boolean mPasswordBad = false;
  protected boolean mPasswordExpired = false;
  protected boolean mPeapV1RequiredFailure = false;
  public int mResult = -1;
  public boolean mSSIDIsInScanList = false;
  protected boolean mSelfSigned = false;
  protected boolean mServerFailure = false;
  private int mSuccessState = -1;
  public boolean mTestingConnectOnly = false;
  public boolean returnToCreds = false;

  public void clearFailureReason()
  {
    setFailReason(-1, "");
  }

  public void dumpVariables(Logger paramLogger)
  {
    Util.log(paramLogger, "Highest EAP state = " + this.mHighestEapState);
    Util.log(paramLogger, "EAP failure = " + this.mEapFailure);
    Util.log(paramLogger, "Server failure = " + this.mServerFailure);
    Util.log(paramLogger, "Self Signed Cert = " + this.mSelfSigned);
    Util.log(paramLogger, "Password expired = " + this.mPasswordExpired);
    Util.log(paramLogger, "MSK failure = " + this.mMskFailure);
    Util.log(paramLogger, "PEAP v1 required failure = " + this.mPeapV1RequiredFailure);
    Util.log(paramLogger, "Password is bad = " + this.mPasswordBad);
    Util.log(paramLogger, "Certificate not valid yet = " + this.mCertNotYetValid);
    Util.log(paramLogger, "Certificate expired = " + this.mCertExpired);
    Util.log(paramLogger, "Connection attempted = " + this.mConnectionAttempted);
    Util.log(paramLogger, "SSID is in scan list = " + this.mSSIDIsInScanList);
    Util.log(paramLogger, "Couldn't load CA cert = " + this.mCertCantLoad);
    Util.log(paramLogger, "ls -l : " + Util.cmdExec("ls -l /sdcard/xpc_certificates/"));
  }

  public void gatherFailData(SystemLogMonitor paramSystemLogMonitor)
  {
    this.mHighestEapState = paramSystemLogMonitor.mHighestEapState;
    this.mEapFailure = paramSystemLogMonitor.mEapFailure;
    this.mServerFailure = paramSystemLogMonitor.mServerFailure;
    this.mSelfSigned = paramSystemLogMonitor.mSelfSigned;
    this.mPasswordExpired = paramSystemLogMonitor.mPasswordExpired;
    this.mMskFailure = paramSystemLogMonitor.mMskFailure;
    this.mPeapV1RequiredFailure = paramSystemLogMonitor.mPeapV1RequiredFailure;
    this.mPasswordBad = paramSystemLogMonitor.mPasswordBad;
    this.mCertNotYetValid = paramSystemLogMonitor.mCertNotValidYet;
    this.mCertExpired = paramSystemLogMonitor.mCertExpired;
    this.mCertCantLoad = paramSystemLogMonitor.mCertCantLoad;
  }

  public void getConnectFailureReason(Context paramContext, NetworkConfigParser paramNetworkConfigParser)
  {
    getConnectFailureReason(paramContext, paramNetworkConfigParser, Build.VERSION.SDK_INT);
  }

  public void getConnectFailureReason(Context paramContext, NetworkConfigParser paramNetworkConfigParser, int paramInt)
  {
    ParcelHelper localParcelHelper = new ParcelHelper("", paramContext);
    if (paramInt >= 16)
    {
      setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_vague_unable_to_connect", "string")), 10);
      this.mResult = -3;
    }
    do
    {
      return;
      if ((this.mLastWifiState < 0) || (this.mLastWifiState > 3))
      {
        setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_unknown_wifi_state", "string")), 4);
        this.mResult = 21;
        this.isTerminal = true;
        return;
      }
      if (this.mHighestEapState != 0)
        break;
      if (this.mAssocFailure == true)
      {
        if (paramNetworkConfigParser != null)
          if (paramNetworkConfigParser.savedConfigInfo.connectOnly == 1)
            setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_cant_reconnect", "string")), 10);
        while (true)
        {
          this.iconToUse = useWarningIcon;
          this.mResult = -3;
          return;
          setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_configed_not_connected", "string")), 10);
          continue;
          if (this.mTestingConnectOnly == true)
            setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_cant_reconnect", "string")), 10);
          else
            setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_configed_not_connected", "string")), 10);
        }
      }
      if (this.mEnableNetworkFail == true)
      {
        setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_cant_enable_network", "string")), 10);
        this.isTerminal = true;
        this.mResult = 14;
        return;
      }
      if (this.mNoConnectionFound == true)
      {
        setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_unable_to_locate_net", "string")), 10);
        this.isTerminal = true;
        this.mResult = 21;
        return;
      }
      if (!this.mConnectionAttempted)
      {
        if (this.mSSIDIsInScanList == true)
        {
          setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_ap_too_busy", "string")), 10);
          this.mResult = -3;
          return;
        }
        setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_no_assoc_no_ssid", "string")), 10);
        this.mResult = -3;
        return;
      }
    }
    while (this.mConnectionAttempted != true);
    if (this.mMskFailure == true)
    {
      setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_failed_okc", "string")), 11);
      this.mResult = -3;
      return;
    }
    setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_no_auth_req", "string")), 10);
    this.mResult = -3;
    return;
    if (this.mHighestEapState == 1)
    {
      if (this.mCertCantLoad == true)
      {
        setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_cant_load_ca_cert", "string")), 2);
        this.mResult = -3;
        return;
      }
      if ((this.mEapFailure == true) || (this.mServerFailure == true))
      {
        if (paramInt >= 16)
        {
          setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_vague_unable_to_connect", "string")), 10);
          this.mResult = -3;
          return;
        }
        setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_radius_server_down", "string")), 10);
        this.mResult = -3;
        return;
      }
      if (paramInt >= 16)
      {
        setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_vague_unable_to_connect", "string")), 10);
        this.mResult = -3;
        return;
      }
      setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_no_auth_req", "string")), 10);
      this.mResult = -3;
      return;
    }
    if (this.mHighestEapState == 2)
    {
      if (this.mMskFailure == true)
      {
        setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_msk_failure", "string")), 11);
        this.mResult = -3;
        return;
      }
      if (this.mKeyFailure == true)
      {
        setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_bad_encryption_keys", "string")), 11);
        this.mResult = -3;
        return;
      }
      if (this.mSelfSigned == true)
      {
        setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_self_signed_cert", "string")), 2);
        this.mResult = 21;
        this.isTerminal = true;
        return;
      }
      if (this.mPeapV1RequiredFailure == true)
      {
        setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_peapv1_required", "string")), 10);
        this.isTerminal = true;
        this.mResult = 21;
        return;
      }
      if (this.mCertNotYetValid == true)
      {
        setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_cert_not_valid_yet", "string")), 2);
        this.isTerminal = true;
        this.mResult = -3;
        return;
      }
      if (this.mCertExpired == true)
      {
        setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_cert_expired", "string")), 2);
        this.isTerminal = true;
        this.mResult = -3;
        return;
      }
      if (this.mPasswordExpired == true)
      {
        setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_password_expired", "string")), 10);
        this.isTerminal = true;
        this.mResult = 11;
        return;
      }
      if ((this.mEapFailure == true) || (this.mPasswordBad == true))
      {
        if (paramInt >= 16)
        {
          setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_vague_unable_to_connect", "string")), 10);
          this.mResult = -3;
          return;
        }
        if (this.mPasswordBad == true)
          this.isTerminal = true;
        setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_upw_failure_error", "string")), 10);
        this.mResult = 11;
        return;
      }
      if (this.mCredsLikelyBad == true)
      {
        setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_vague_unable_to_connect", "string")), 10);
        this.mResult = 11;
        return;
      }
      setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_unknown_fail", "string")), 10);
      this.mResult = 15;
      return;
    }
    if (paramInt >= 16)
    {
      setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_vague_unable_to_connect", "string")), 10);
      this.mResult = -3;
      return;
    }
    setFailReason(useWarningIcon, paramContext.getResources().getString(localParcelHelper.getIdentifier("xpc_unknown_fail", "string")), 10);
    this.mResult = 15;
  }

  public int getFailIssue()
  {
    return this.failIssue;
  }

  public String getFailureString()
  {
    return this.failureString;
  }

  public int getIconToUse()
  {
    return this.iconToUse;
  }

  public int getSuccessState()
  {
    return this.mSuccessState;
  }

  public void setFailReason(int paramInt, String paramString)
  {
    setFailReason(paramInt, paramString, -1);
  }

  public void setFailReason(int paramInt1, String paramString, int paramInt2)
  {
    this.iconToUse = paramInt1;
    this.failureString = paramString;
    this.failIssue = paramInt2;
  }

  public void setSuccessState(int paramInt)
  {
    this.mSuccessState = paramInt;
  }
}