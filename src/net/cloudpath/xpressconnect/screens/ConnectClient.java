package net.cloudpath.xpressconnect.screens;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.screens.delegates.ConnectClientDelegate;

public class ConnectClient extends ScreenBase
{
  private ConnectClientDelegate mDelegate = null;
  private ImageView spinner = null;
  private AnimationDrawable spinnerAnim = null;
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
    this.wl = ((PowerManager)getSystemService("power")).newWakeLock(26, "XPCConnecting");
    this.wl.setReferenceCounted(true);
    Util.log(this.mLogger, "Initiating forced wake lock.");
    this.wl.acquire();
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    Log.d("XPC", "onActivityResult().");
    this.mDelegate.onActivityResult(paramInt1, paramInt2);
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onBackPressed()
  {
    Util.log(this.mLogger, "--- Leaving ConnectClient because user pressed back.");
    this.mDelegate.onBackPressed();
    super.onBackPressed();
  }

  protected void onCreate(Bundle paramBundle)
  {
    preContentView();
    setContentView(new ParcelHelper("", this).getLayoutId("xpc_processing"));
    super.onCreate(paramBundle);
    Log.d("XPC", "onCreate() (ConnectClient)");
    if (haveTabs())
      setConnectActive();
    this.mDelegate = new ConnectClientDelegate(this);
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
          ConnectClient.this.spinnerAnim.start();
        }
      });
    }
  }

  protected void onDestroy()
  {
    if (this.mDelegate != null)
      this.mDelegate.terminateWorker();
    super.onDestroy();
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    default:
    case 6:
    }
    while (true)
    {
      return super.onOptionsItemSelected(paramMenuItem);
      this.mDelegate.terminateWorker();
    }
  }

  protected void onPause()
  {
    Util.log(this.mLogger, "onPause() (ConnectClient)");
    this.mDelegate.onPause();
    destroyLockDeviceOn();
    super.onPause();
  }

  protected void onResume()
  {
    super.onResume();
    Util.log(this.mLogger, "onResume() (ConnectClient)");
    lockDeviceOn();
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
    Util.log(this.mLogger, "+++ Showing ConnectClient.");
    if (haveTabs())
      setConnectActive();
    this.mDelegate.setRequiredSettings(this.mLogger, this.mParser, this.mGlobals, this.mFailure);
    this.mDelegate.onServiceBind();
  }

  public void threadChangeTab(int paramInt)
  {
    switch (paramInt)
    {
    default:
    case 2:
    case 3:
    case 4:
    }
    do
    {
      do
      {
        do
          return;
        while (!haveTabs());
        setConnectActive();
        return;
      }
      while (!haveTabs());
      setAuthenticateActive();
      return;
    }
    while (!haveTabs());
    setValidateActive();
  }
}