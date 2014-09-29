package net.cloudpath.xpressconnect.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.screens.delegates.ShowReportDataDelegate;

public class ShowReportData extends ScreenBase
  implements View.OnClickListener
{
  private CheckBox mAlwaysUse = null;
  private Button mBtnNo = null;
  private Button mBtnYes = null;
  private ShowReportDataDelegate mDelegate = null;
  private TextView mReportData = null;

  public void onBackPressed()
  {
    done(1);
  }

  public void onClick(View paramView)
  {
    if (paramView == this.mBtnYes)
      this.mDelegate.buttonYesClicked(this.mAlwaysUse.isChecked(), this.mFailure.getFailIssue(), this.mWifi);
    while (paramView != this.mBtnNo)
      return;
    this.mDelegate.buttonNoClicked(this.mAlwaysUse.isChecked());
  }

  protected void onCreate(Bundle paramBundle)
  {
    preContentView();
    setContentView(new ParcelHelper("", this).getLayoutId("xpc_showreport_layout"));
    super.onCreate(paramBundle);
    this.mReportData = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_LogText")));
    this.mAlwaysUse = ((CheckBox)findViewById(this.mParcelHelper.getItemId("xpc_alwaysUse")));
    this.mBtnYes = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_yesButton")));
    this.mBtnYes.setOnClickListener(this);
    this.mBtnNo = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_noButton")));
    this.mBtnNo.setOnClickListener(this);
    if (haveTabs())
      setConnectedActive();
    this.mDelegate = new ShowReportDataDelegate(this);
    setGestureCallback(this.mDelegate);
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    if (paramEncapsulationService == null)
    {
      Log.d("XPC", "Unable to bind to the local service!");
      return;
    }
    super.onServiceBind(paramEncapsulationService);
    if (this.mParser == null)
      Util.log(this.mLogger, "Config was not properly bound!");
    Util.log(this.mLogger, "+++ Showing ShowReportData screen.");
    this.mDelegate.setRequiredSettings(this.mLogger, this.mParser);
    this.mDelegate.setResults(this.mFailure.getFailureString(), String.valueOf(this.mFailure.getSuccessState()), this.mFailure.getFailIssue());
    this.mDelegate.onServiceBind();
  }

  public void setLogData(String paramString)
  {
    this.mReportData.setText(paramString);
  }
}