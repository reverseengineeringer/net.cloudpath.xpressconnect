package net.cloudpath.xpressconnect;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.commonsware.cwac.parcel.ParcelHelper;

public class MultiColorWarningDialog extends Dialog
  implements View.OnClickListener
{
  private GenericCallback mCallback = null;
  private Context mCtx = null;
  private int mId;
  private String mTextBoxText = null;
  private Button okayBtn = null;
  private TextView textBox = null;

  public MultiColorWarningDialog(Context paramContext, int paramInt)
  {
    super(paramContext);
    this.mCtx = paramContext;
    this.mId = paramInt;
  }

  public void cancel()
  {
  }

  public void onClick(View paramView)
  {
    Log.d("XPC", "Okay clicked.");
    if (this.mCallback != null)
      this.mCallback.setCallbackData(this.mId, null);
    while (true)
    {
      dismiss();
      return;
      Log.d("XPC", "NO CALLBACK WAS SET!  DATA IS LOST!");
    }
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    ParcelHelper localParcelHelper = new ParcelHelper("", this.mCtx);
    requestWindowFeature(1);
    setContentView(localParcelHelper.getLayoutId("xpc_multicolor_dialog"));
    this.okayBtn = ((Button)findViewById(localParcelHelper.getItemId("xpc_OkayBtn")));
    this.okayBtn.setOnClickListener(this);
    this.textBox = ((TextView)findViewById(localParcelHelper.getItemId("xpc_ExplainerText")));
    this.textBox.setText(Html.fromHtml(this.mTextBoxText), TextView.BufferType.SPANNABLE);
  }

  public void setCallback(GenericCallback paramGenericCallback)
  {
    this.mCallback = paramGenericCallback;
  }

  public void setHtmlText(String paramString)
  {
    this.mTextBoxText = paramString;
  }
}