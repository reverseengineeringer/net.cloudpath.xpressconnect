package net.cloudpath.xpressconnect;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.commonsware.cwac.parcel.ParcelHelper;

public class GetManualUrl extends Dialog
  implements View.OnClickListener
{
  private Button cancelBtn = null;
  private GenericCallback mCallback = null;
  private Context mCtx = null;
  private Button okayBtn = null;
  private EditText textBox = null;

  public GetManualUrl(Context paramContext)
  {
    super(paramContext);
    this.mCtx = paramContext;
  }

  public void onClick(View paramView)
  {
    if (paramView == this.okayBtn)
    {
      Log.d("XPC", "Okay clicked.");
      if (this.mCallback != null)
        this.mCallback.setCallbackData(0, this.textBox.getText().toString());
    }
    while (true)
    {
      dismiss();
      return;
      Log.d("XPC", "NO CALLBACK WAS SET!  DATA IS LOST!");
      continue;
      if (paramView == this.cancelBtn)
      {
        Log.d("XPC", "Cancel clicked.");
        if (this.mCallback != null)
          this.mCallback.setCallbackData(-1, null);
      }
      else
      {
        Log.d("XPC", "Unknown item tapped!");
      }
    }
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    ParcelHelper localParcelHelper = new ParcelHelper("", this.mCtx);
    setContentView(localParcelHelper.getLayoutId("xpc_get_manual_url"));
    setTitle(localParcelHelper.getIdentifier("xpc_manual_url", "string"));
    this.okayBtn = ((Button)findViewById(localParcelHelper.getItemId("xpc_OkayBtn")));
    this.okayBtn.setOnClickListener(this);
    this.cancelBtn = ((Button)findViewById(localParcelHelper.getItemId("xpc_CancelBtn")));
    this.cancelBtn.setOnClickListener(this);
    this.textBox = ((EditText)findViewById(localParcelHelper.getItemId("xpc_Description")));
  }

  public void setCallback(GenericCallback paramGenericCallback)
  {
    this.mCallback = paramGenericCallback;
  }
}