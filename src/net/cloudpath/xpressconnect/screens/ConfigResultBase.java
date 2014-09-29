package net.cloudpath.xpressconnect.screens;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.GenericCallback;
import net.cloudpath.xpressconnect.GestureCallback;
import net.cloudpath.xpressconnect.LocalDbHelper;
import net.cloudpath.xpressconnect.SendDataPrompt;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkItemElement;
import net.cloudpath.xpressconnect.remoteservice.SendReport;

public class ConfigResultBase extends ScreenBase
  implements View.OnClickListener, GenericCallback, DialogInterface.OnDismissListener, GestureCallback
{
  protected Button changeCredsButton = null;
  protected Button doneButton = null;
  protected TextView finalStatus = null;
  protected TextView ipAddress = null;
  protected boolean mAlwaysUse = false;
  protected SendDataPrompt mDataPrompt = null;
  protected int mFinalResult = -1;
  protected int mSendReport = -1;
  protected Button rateMe = null;
  protected Button retryButton = null;
  protected TextView smallStatus = null;
  protected ImageView successImage = null;

  public void handleDumpDatabase()
  {
    LocalDbHelper.dumpDbToSdCard(this);
  }

  public void handleNfcProgrammerGesture()
  {
    Toast.makeText(this, "Starting NFC Programmer...", 0).show();
    done(39);
  }

  public void onClick(View paramView)
  {
    if ((this.mParser == null) || (this.mParser.selectedNetwork == null))
    {
      Util.log(this.mLogger, "--- Leaving ConfigResultBase because of a button click, but no available configuration data.");
      done(this.mFinalResult);
      return;
    }
    switch (this.mParser.selectedNetwork.androidReporting)
    {
    default:
      Util.log(this.mLogger, "Prompting to see if we can send reporting data.");
      requestReport();
      return;
    case 1:
      Util.log(this.mLogger, "Forcing send of reporting data.");
      Util.log(this.mLogger, "--- Leaving ConfigResultBase because of a button click, with forced report sending.");
      SendReport.startReportSendingAttempts(this.mLogger, this, this.mParser, this.mFinalResult, this.mFinalResult, this.finalStatus.getText().toString(), this.mWifi);
      if (this.mGlobals != null)
        this.mGlobals.destroyServiceData();
      done(this.mFinalResult);
      return;
    case 2:
    }
    Util.log(this.mLogger, "Not sending report data.");
    this.mLogger.addLine("--- Leaving ConfigResultBase because of a button click, without forced report sending.");
    if (this.mGlobals != null)
      this.mGlobals.destroyServiceData();
    done(this.mFinalResult);
  }

  protected void onCreate(Bundle paramBundle)
  {
    preContentView();
    setContentView(new ParcelHelper("", this).getLayoutId("xpc_success_or_fail"));
    super.onCreate(paramBundle);
    this.doneButton = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_DoneButton")));
    this.doneButton.setOnClickListener(this);
    this.rateMe = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_rateMe")));
    this.retryButton = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_Retry")));
    this.changeCredsButton = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_ChangeCreds")));
    this.successImage = ((ImageView)findViewById(this.mParcelHelper.getItemId("xpc_StatusIcon")));
    this.finalStatus = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_FinalStatusLabel")));
    this.smallStatus = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_SmallStatusLabel")));
    this.ipAddress = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_IPAddressLabel")));
    if (haveTabs())
      setConnectedActive();
    setGestureCallback(this);
  }

  public void onDismiss(DialogInterface paramDialogInterface)
  {
    this.mDataPrompt = null;
    if (this.mAlwaysUse == true)
      SendReport.saveAlwaysUsePref(this.mLogger, this, this.mSendReport);
    if (this.mSendReport == 1)
      SendReport.startReportSendingAttempts(this.mLogger, this, this.mParser, this.mFinalResult, this.mFailure.getFailIssue(), this.finalStatus.getText().toString(), this.mWifi);
    Util.log(this.mLogger, "--- Leaving ConfigResultBase via onDismiss().");
    if (this.mSendReport == 2)
    {
      this.mFailure.setSuccessState(this.mFinalResult);
      done(40);
      return;
    }
    if (this.mGlobals != null)
      this.mGlobals.destroyServiceData();
    quit();
  }

  protected void onPause()
  {
    this.mGlobals.setSavedObject(this.mDataPrompt);
    super.onPause();
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    if (paramEncapsulationService == null)
      Log.d("XPC", "Unable to bind to the local service!");
    while (true)
    {
      return;
      super.onServiceBind(paramEncapsulationService);
      Util.log(this.mLogger, "+++ Showing ConfigResultBase");
      try
      {
        this.mDataPrompt = ((SendDataPrompt)this.mGlobals.getSavedObject());
        this.mGlobals.setSavedObject(null);
        if (haveTabs())
          setConnectedActive();
        if (this.mDataPrompt == null)
          continue;
        requestReport();
        return;
      }
      catch (ClassCastException localClassCastException)
      {
        while (true)
          this.mDataPrompt = null;
      }
    }
  }

  protected int prefsShouldPrompt()
  {
    SharedPreferences localSharedPreferences = getPreferences(0);
    Util.log(this.mLogger, "alwaysuse = " + localSharedPreferences.getBoolean("alwaysuse", false));
    Util.log(this.mLogger, "touse = " + localSharedPreferences.getInt("touse", -1));
    if (!localSharedPreferences.getBoolean("alwaysuse", false))
    {
      Util.log(this.mLogger, "Not set to always use a default setting.  Returning -1.");
      return -1;
    }
    return localSharedPreferences.getInt("touse", -1);
  }

  public void requestReport()
  {
    int i = prefsShouldPrompt();
    if (i < 0)
    {
      this.mDataPrompt = new SendDataPrompt(this);
      this.mDataPrompt.setCallback(this);
      this.mDataPrompt.setOnDismissListener(this);
      this.mDataPrompt.setStateValues(this.mLogger, this.mFinalResult);
      return;
    }
    if (i == 1)
    {
      Util.log(this.mLogger, "Sending data based on previous check box set.");
      SendReport.startReportSendingAttempts(this.mLogger, this, this.mParser, this.mFinalResult, this.mFailure.getFailIssue(), this.finalStatus.getText().toString(), this.mWifi);
    }
    while (true)
    {
      Util.log(this.mLogger, "--- Leaving ConfigResultBase after processing report data.");
      if (this.mGlobals != null)
        this.mGlobals.destroyServiceData();
      done(this.mFinalResult);
      return;
      Util.log(this.mLogger, "*NOT* sending data based on previous check box set.");
    }
  }

  public void setCallbackData(int paramInt, String paramString)
  {
    this.mSendReport = paramInt;
    if (paramString != null)
    {
      this.mAlwaysUse = true;
      Util.log(this.mLogger, "Will always use value of " + paramInt + " for reporting.");
    }
  }
}