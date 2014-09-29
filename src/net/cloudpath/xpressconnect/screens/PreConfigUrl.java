package net.cloudpath.xpressconnect.screens;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.ProfileElement;
import net.cloudpath.xpressconnect.retention.ConfigureClientRetention;
import net.cloudpath.xpressconnect.thread.PreConfigUrlThread;
import net.cloudpath.xpressconnect.thread.ThreadCom;
import net.cloudpath.xpressconnect.thread.ThreadComCallbacks;

public class PreConfigUrl extends ScreenBase
  implements ThreadComCallbacks, DialogInterface.OnCancelListener
{
  private ThreadCom mThreadCom = null;
  private ImageView spinner = null;
  private AnimationDrawable spinnerAnim = null;
  private PreConfigUrlThread worker = null;

  public void onBackPressed()
  {
    Util.log(this.mLogger, "--- Leaving PreConfigUrl because user pressed back button.");
    if (this.worker != null)
      this.worker.die();
    super.onBackPressed();
  }

  public void onCancel(DialogInterface paramDialogInterface)
  {
    this.mFailure.setFailReason(FailureReason.useWarningIcon, getApplicationContext().getString(this.mParcelHelper.getIdentifier("xpc_cant_auth_pre_config_url", "string")), 1);
    Util.log(this.mLogger, "--- Leaving PreConfigUrl because authentication to pre-config URL server failed.");
    done(20);
  }

  protected void onCreate(Bundle paramBundle)
  {
    ParcelHelper localParcelHelper = new ParcelHelper("", this);
    preContentView();
    setContentView(localParcelHelper.getLayoutId("xpc_processing"));
    super.onCreate(paramBundle);
    Log.d("XPC", "onCreate() (PreConfigUrl)");
    if (haveTabs())
      setConfigureActive();
    this.spinner = ((ImageView)findViewById(this.mParcelHelper.getItemId("xpc_spinnerImage")));
    if (this.spinner != null)
    {
      this.spinnerAnim = ((AnimationDrawable)this.spinner.getDrawable());
      this.spinnerAnim.setCallback(this.spinner);
      this.spinnerAnim.setVisible(true, true);
      this.spinner.post(new Runnable()
      {
        public void run()
        {
          Log.d("XPC", "*** Starting animation!");
          PreConfigUrl.this.spinnerAnim.start();
        }
      });
    }
  }

  protected void onDestroy()
  {
    Util.log(this.mLogger, "onDestroy() (PreConfigUrl)");
    this.worker = null;
    this.mThreadCom = null;
    super.onDestroy();
  }

  protected void onPause()
  {
    Util.log(this.mLogger, "PreConfigUrl -- onPause()");
    if (this.mGlobals != null)
    {
      ConfigureClientRetention localConfigureClientRetention = new ConfigureClientRetention();
      localConfigureClientRetention.worker = this.worker;
      localConfigureClientRetention.threadcom = this.mThreadCom;
      this.mGlobals.setSavedObject(localConfigureClientRetention);
    }
    this.worker = null;
    this.mThreadCom = null;
    super.onPause();
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    if (paramEncapsulationService == null)
    {
      Log.d("XPC", "mBound was null in onServiceBind()!");
      return;
    }
    super.onServiceBind(paramEncapsulationService);
    if (this.mParser == null)
      Util.log(this.mLogger, "Config was not properly bound!");
    Util.log(this.mLogger, "+++ Showing PreConfigUrl.");
    if (haveTabs())
      setConfigureActive();
    try
    {
      localConfigureClientRetention = (ConfigureClientRetention)this.mGlobals.getSavedObject();
      if (localConfigureClientRetention == null)
      {
        this.mThreadCom = new ThreadCom();
        if (this.mThreadCom != null)
          this.mThreadCom.setCallbacks(this);
        TextView localTextView = (TextView)findViewById(this.mParcelHelper.getItemId("xpc_configText"));
        localTextView.setText(this.mParcelHelper.getIdentifier("xpc_contacting_url", "string"));
        this.mThreadCom.setTextView(localTextView);
        processUrl();
        return;
      }
    }
    catch (ClassCastException localClassCastException)
    {
      while (true)
      {
        ConfigureClientRetention localConfigureClientRetention = null;
        continue;
        this.mThreadCom = localConfigureClientRetention.threadcom;
        this.worker = ((PreConfigUrlThread)localConfigureClientRetention.worker);
        if (this.mThreadCom == null)
        {
          this.mThreadCom = new ThreadCom();
          if (this.worker != null)
            this.worker.setThreadCom(this.mThreadCom);
        }
      }
    }
  }

  public void processUrl()
  {
    if (this.worker != null)
      return;
    this.worker = new PreConfigUrlThread(this.mThreadCom, this.mParser, this.mLogger, this.mWifi);
    this.worker.start();
  }

  public void threadChangeTab(int paramInt)
  {
  }

  public void threadFailed(Boolean paramBoolean, int paramInt, String paramString)
  {
    Util.log(this.mLogger, "Failure, error string = " + paramString);
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "Parser isn't bound in popupFailed()!");
      return;
    }
    if (this.mParser.selectedProfile == null)
    {
      Util.log(this.mLogger, "No profile selected in popupFailed()!");
      return;
    }
    if (this.mParser.selectedProfile.showPreCreds != 1)
    {
      Util.log(this.mLogger, "Pre-creds wasn't shown.  Showing popup.");
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
      localBuilder.setMessage(paramString);
      localBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          PreConfigUrl.access$102(PreConfigUrl.this, null);
          PreConfigUrl.this.processUrl();
        }
      });
      localBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          PreConfigUrl.this.mFailure.setFailReason(FailureReason.useWarningIcon, PreConfigUrl.this.getApplicationContext().getString(PreConfigUrl.this.mParcelHelper.getIdentifier("xpc_cant_auth_pre_config_url", "string")), 1);
          Util.log(PreConfigUrl.this.mLogger, "--- Leaving PreConfigUrl because attempt to connect to the server failed, and the user selected Cancel.");
          PreConfigUrl.this.done(20);
        }
      });
      localBuilder.setOnCancelListener(this);
      localBuilder.show();
      return;
    }
    if ((this.mGlobals != null) && (this.mGlobals.getFailureReason() != null))
      this.mFailure.setFailReason(FailureReason.useWarningIcon, paramString, 1);
    Util.log(this.mLogger, "--- Leaving PreConfigUrl because thread failed.");
    done(paramInt);
  }

  public void threadSuccess()
  {
    Util.log(this.mLogger, "--- Leaving PreConfigUrl because thread was a success.");
    done(1);
  }
}