package net.cloudpath.xpressconnect.screens;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.io.PrintStream;
import net.cloudpath.xpressconnect.AndroidVersion;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.Testing;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.screens.delegates.EntryScreenDelegate;
import net.cloudpath.xpressconnect.screens.delegates.WebMainNfcDelegate;
import net.cloudpath.xpressconnect.thread.SystemLogMonitor;

public class EntryScreen extends ScreenBase
{
  private EntryScreenDelegate mDelegate = null;
  private WebMainNfcDelegate mNfcDelegate = null;
  private ImageView mSpinner = null;
  private AnimationDrawable mSpinnerAnim = null;

  public String getPassword()
  {
    return this.mDelegate.getPassword();
  }

  public String getUsername()
  {
    return this.mDelegate.getUsername();
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    Util.log(this.mLogger, "Requesting termination at entry point.");
    if (this.mGlobals != null)
    {
      SystemLogMonitor localSystemLogMonitor = this.mGlobals.getSysLogMon();
      if (localSystemLogMonitor != null)
      {
        Util.log(this.mLogger, "Signaling syslog monitor thread to terminate.");
        localSystemLogMonitor.die();
      }
    }
    done(0);
  }

  protected void onCreate(Bundle paramBundle)
  {
    ParcelHelper localParcelHelper = new ParcelHelper("", this);
    if (Testing.clearKeyguard)
    {
      System.out.println("Will disable keyguard if needed.");
      getWindow().addFlags(4194304);
    }
    preContentView();
    setContentView(localParcelHelper.getLayoutId("xpc_processing"));
    super.onCreate(paramBundle);
    this.mDelegate = new EntryScreenDelegate(this);
    this.mDelegate.gatherOptionalSettings(getIntent());
    if (haveTabs())
      setWelcomeActive();
    this.mSpinner = ((ImageView)findViewById(this.mParcelHelper.getItemId("xpc_spinnerImage")));
    if (this.mSpinner != null)
    {
      this.mSpinnerAnim = ((AnimationDrawable)this.mSpinner.getDrawable());
      this.mSpinnerAnim.setCallback(this.mSpinner);
      this.mSpinnerAnim.setVisible(true, true);
      this.mSpinner.post(new Runnable()
      {
        public void run()
        {
          EntryScreen.this.mSpinnerAnim.start();
        }
      });
    }
    ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_configText"))).setText(this.mParcelHelper.getIdentifier("xpc_locate_config", "string"));
    if (Build.VERSION.SDK_INT >= 9)
      this.mNfcDelegate = new WebMainNfcDelegate(getIntent(), this.mLogger);
  }

  protected void onResume()
  {
    super.onResume();
    if ((this.mNfcDelegate != null) && (this.mNfcDelegate.checkIntent()))
    {
      this.mDelegate.setTargetUrl(this.mNfcDelegate.getUrl());
      Toast.makeText(this, "Starting via NFC tag...", 1).show();
      this.mDelegate.logVersion();
      this.mDelegate.findConfiguration();
    }
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    AndroidVersion localAndroidVersion = new AndroidVersion(null);
    if (paramEncapsulationService == null)
      Log.d("XPC", "Unable to bind to the local service!");
    do
    {
      return;
      if (this.mGlobals != null)
        this.mGlobals.destroyServiceData();
      if (paramEncapsulationService.getFailureReason().getFailureString() != null)
        paramEncapsulationService.getFailureReason().setFailReason(FailureReason.useWarningIcon, null);
      super.onServiceBind(paramEncapsulationService);
      this.mDelegate.setRequiredValue(this.mGlobals, paramEncapsulationService.getLogger());
      if (this.mDelegate.isBlacklistedDevice())
      {
        alertThenDie(this.mParcelHelper.getIdentifier("xpc_dev_not_supported", "string"), getString(this.mParcelHelper.getIdentifier("xpc_dev_not_supported_no_eap_text", "string")), this.mParcelHelper.getIdentifier("xpc_ok_string", "string"));
        return;
      }
      if (!this.mDelegate.versionIsValid(localAndroidVersion))
      {
        alertThenDie(this.mParcelHelper.getIdentifier("xpc_ver_not_supported", "string"), getString(this.mParcelHelper.getIdentifier("xpc_ver_not_supported_text", "string")) + "\nYour version is : " + localAndroidVersion.fullVersion(), this.mParcelHelper.getIdentifier("xpc_ok_string", "string"));
        return;
      }
      paramEncapsulationService.setRunningAsLibrary(this.mDelegate.getAsLibrary());
      if (this.mDelegate.getAsLibrary() == true)
      {
        Util.log(this.mLogger, "Running in library mode.");
        this.mDelegate.gotoFlowControl(null, null);
        return;
      }
      if (haveTabs())
        setWelcomeActive();
    }
    while ((this.mNfcDelegate != null) && (this.mNfcDelegate.checkIntent() == true));
    this.mDelegate.logVersion();
    this.mDelegate.findConfiguration();
  }
}