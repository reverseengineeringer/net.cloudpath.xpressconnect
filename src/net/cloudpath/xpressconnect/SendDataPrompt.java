package net.cloudpath.xpressconnect;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.logger.Logger;

public class SendDataPrompt extends Dialog
  implements View.OnClickListener
{
  public static final int DO_NOT_SEND_REPORT = 0;
  public static final int SEND_REPORT = 1;
  public static final int SHOW_REPORT = 2;
  private CheckBox mAlwaysUse = null;
  private GenericCallback mCallback = null;
  private Context mContext = null;
  private Logger mLogger = null;
  private Button mNoButton = null;
  private TextView mReportText = null;
  private Button mShowData = null;
  private TextView mSmallReason = null;
  private int mState = 0;
  private Button mYesButton = null;

  public SendDataPrompt(Context paramContext)
  {
    super(paramContext);
    this.mContext = paramContext;
  }

  public void onBackPressed()
  {
    super.onBackPressed();
    if (this.mCallback != null)
      this.mCallback.setCallbackData(0, null);
    dismiss();
  }

  public void onClick(View paramView)
  {
    boolean bool = this.mAlwaysUse.isChecked();
    String str = null;
    if (bool == true)
      str = "yes";
    if (paramView == this.mYesButton)
    {
      Util.log(this.mLogger, "User will allow us to send data.  Checkbox state : " + str);
      if (this.mCallback != null)
        this.mCallback.setCallbackData(1, str);
    }
    while (true)
    {
      dismiss();
      return;
      Log.d("XPC", "NO CALLBACK WAS SET!  DATA IS LOST!");
      continue;
      if (paramView == this.mNoButton)
      {
        Util.log(this.mLogger, "User will *NOT* allow us to send data.  Checkbox state : " + str);
        if (this.mCallback != null)
          this.mCallback.setCallbackData(0, str);
      }
      else if (paramView == this.mShowData)
      {
        Util.log(this.mLogger, "User wants to see the data.  Checkbox state : " + str);
        if (this.mCallback != null)
          this.mCallback.setCallbackData(2, str);
      }
    }
  }

  protected void onCreate(Bundle paramBundle)
  {
    ParcelHelper localParcelHelper = new ParcelHelper("", this.mContext);
    super.onCreate(paramBundle);
    setContentView(localParcelHelper.getLayoutId("xpc_submit_report_data"));
    setTitle(localParcelHelper.getIdentifier("xpc_report_dialog_title", "string"));
    this.mYesButton = ((Button)findViewById(localParcelHelper.getItemId("xpc_yesBtn")));
    this.mYesButton.setOnClickListener(this);
    this.mNoButton = ((Button)findViewById(localParcelHelper.getItemId("xpc_noBtn")));
    this.mNoButton.setOnClickListener(this);
    this.mReportText = ((TextView)findViewById(localParcelHelper.getItemId("xpc_reportText")));
    this.mSmallReason = ((TextView)findViewById(localParcelHelper.getItemId("xpc_smallExplain")));
    this.mAlwaysUse = ((CheckBox)findViewById(localParcelHelper.getItemId("xpc_alwaysDoThis")));
    this.mShowData = ((Button)findViewById(localParcelHelper.getItemId("xpc_ViewData")));
    this.mShowData.setOnClickListener(this);
    if (this.mState == 1)
    {
      this.mReportText.setText(this.mContext.getResources().getString(localParcelHelper.getIdentifier("xpc_report_success_large", "string")));
      this.mSmallReason.setText(this.mContext.getResources().getString(localParcelHelper.getIdentifier("xpc_improve_for_success", "string")));
      return;
    }
    this.mReportText.setText(this.mContext.getResources().getString(localParcelHelper.getIdentifier("xpc_report_fail_large", "string")));
    this.mSmallReason.setText(this.mContext.getResources().getString(localParcelHelper.getIdentifier("xpc_improve_for_fail", "string")));
  }

  public void setCallback(GenericCallback paramGenericCallback)
  {
    this.mCallback = paramGenericCallback;
  }

  public void setStateValues(Logger paramLogger, int paramInt)
  {
    this.mState = paramInt;
    this.mLogger = paramLogger;
    show();
  }
}