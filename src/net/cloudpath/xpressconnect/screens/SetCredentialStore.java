package net.cloudpath.xpressconnect.screens;

import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.Html;
import android.text.Spannable;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;

public class SetCredentialStore extends ScreenBase
  implements View.OnClickListener
{
  private Button mContinueButton = null;
  private TextView mLowerText = null;
  private PowerManager.WakeLock wl = null;

  private void destroyLockDeviceOn()
  {
    if (this.wl != null)
    {
      if (this.wl.isHeld());
      try
      {
        this.wl.release();
        this.wl = null;
        Util.log(this.mLogger, "Terminating forced wake lock.");
        return;
      }
      catch (RuntimeException localRuntimeException)
      {
        while (true)
          Util.log(this.mLogger, "Unable to release/destroy screen on lock.  Skipping.");
      }
    }
    Log.d("XPC", "Attempted to destroy the wake lock when it wasn't being used!");
  }

  private void lockDeviceOn()
  {
    if ((this.wl != null) && (this.wl.isHeld()))
    {
      Log.d("XPC", "Attempted to lock device on when it was already locked.");
      return;
    }
    this.wl = ((PowerManager)getSystemService("power")).newWakeLock(26, "XPCConfiguring");
    this.wl.setReferenceCounted(true);
    Util.log(this.mLogger, "Initiating forced wake lock.");
    this.wl.acquire();
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    Log.d("XPC", "onActivityResult().");
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onBackPressed()
  {
    Util.log(this.mLogger, "--- Leaving SetCredentialStore because user pressed back.");
    super.onBackPressed();
  }

  public void onClick(View paramView)
  {
    done(0);
  }

  protected void onCreate(Bundle paramBundle)
  {
    preContentView();
    setContentView(new ParcelHelper("", this).getLayoutId("xpc_setcredentialstore"));
    super.onCreate(paramBundle);
    Log.d("XPC", "onCreate() (SetCredentialStore)");
    this.mContinueButton = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_ContinueBtn")));
    this.mContinueButton.setOnClickListener(this);
    this.mLowerText = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_LowerTextView")));
    Spannable localSpannable = (Spannable)Html.fromHtml("Please be sure to select <strong><font color=\"red\">\"Wi-Fi\"</font></strong> and not \"VPN and apps\".");
    localSpannable.setSpan(new RelativeSizeSpan(1.5F), 25, 33, 33);
    this.mLowerText.setText(localSpannable);
    if (haveTabs())
      setConfigureActive();
  }

  protected void onPause()
  {
    Util.log(this.mLogger, "onPause() (SetCredentialStore)");
    destroyLockDeviceOn();
    super.onPause();
  }

  protected void onResume()
  {
    super.onResume();
    Util.log(this.mLogger, "onResume() (SetCredentialStore)");
    lockDeviceOn();
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    if (paramEncapsulationService == null)
      Log.d("XPC", "Unable to bind to the local service!");
    do
    {
      return;
      super.onServiceBind(paramEncapsulationService);
      if (this.mParser == null)
        Util.log(this.mLogger, "Config was not properly bound!");
      Util.log(this.mLogger, "+++ Showing SetCredentialStore.");
    }
    while (!haveTabs());
    setConnectActive();
  }
}