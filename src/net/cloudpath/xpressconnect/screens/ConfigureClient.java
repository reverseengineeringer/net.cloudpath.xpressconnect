package net.cloudpath.xpressconnect.screens;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.GenericCallback;
import net.cloudpath.xpressconnect.MultiColorWarningDialog;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.screens.delegates.ConfigureClientDelegate;
import net.cloudpath.xpressconnect.thread.ResponseThreadComCallbacks;

public class ConfigureClient extends ScreenBase
  implements GenericCallback
{
  public static final int SHOW_BASIC_INSTALL_WARNING = 10007;
  public static final String SHOW_BASIC_INSTALL_WARNING_TAG = "keystoreBasicInstallWarning";
  public static final int SHOW_KEYSTORE_ERROR = 10003;
  public static final String SHOW_KEYSTORE_ERROR_TAG = "keystoreErrorTag";
  public static final int SHOW_KEYSTORE_WARNING = 10000;
  public static final String SHOW_KEYSTORE_WARNING_TAG = "keystoreWarningTag";
  public static final int SHOW_LOCK_SCREEN_WARNING = 10001;
  public static final String SHOW_LOCK_SCREEN_WARNING_TAG = "lockScreenWarningTag";
  public static final int SHOW_PKCS12_ALT_CA_WARNING = 10004;
  public static final String SHOW_PKCS12_ALT_CA_WARNING_TAG = "pkcs12AltCaWarning";
  public static final int SHOW_PKCS12_ALT_PASSWORD_WARNING = 10005;
  public static final String SHOW_PKCS12_ALT_PASSWORD_WARNING_TAG = "pkcs12ShowAltPasswordWarning";
  public static final int SHOW_PKCS12_PASSWORD_WARNING = 10002;
  public static final String SHOW_PKCS12_PASSWORD_WARNING_TAG = "pkcs12ShowPasswordWarning";
  public static final int SHOW_RESET_CREDENTIAL_STORE = 10008;
  public static final int SHOW_SET_CREDENTIAL_STORE = 10006;
  private ConfigureClientDelegate mDelegate = null;
  public int mDialogShowing = -1;
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
        Util.log(this.mLogger, "Unable to release screen lock!  We may not have the lock.");
        return;
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
    if (paramInt1 == 23)
    {
      setCallbackData(paramInt2, null);
      return;
    }
    if (paramInt1 == 24)
    {
      setCallbackData(paramInt2, null);
      return;
    }
    if ((paramInt2 == 0) && (paramInt1 != 1002))
    {
      super.onActivityResult(paramInt1, paramInt2, paramIntent);
      return;
    }
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onBackPressed()
  {
    Util.log(this.mLogger, "--- Leaving ConfigureClient because user pressed back.");
    this.mDelegate.onBackPressed();
    super.onBackPressed();
  }

  protected void onCreate(Bundle paramBundle)
  {
    ParcelHelper localParcelHelper = new ParcelHelper("", this);
    preContentView();
    setContentView(localParcelHelper.getLayoutId("xpc_processing"));
    super.onCreate(paramBundle);
    Log.d("XPC", "onCreate() (ConfigClient)");
    this.mDelegate = new ConfigureClientDelegate(this);
    if (haveTabs())
      setConfigureActive();
    ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_configText"))).setText(this.mParcelHelper.getIdentifier("xpc_checking_config", "string"));
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
          ConfigureClient.this.spinnerAnim.start();
        }
      });
    }
    if (paramBundle != null)
      this.mDialogShowing = paramBundle.getInt("dialog", -1);
  }

  protected void onDestroy()
  {
    Log.d("XPC", "onDestroy() (ConfigClient)");
    Util.log(this.mLogger, "--- Leaving ConfigureClient via onDestroy().");
    this.mDelegate = null;
    super.onDestroy();
  }

  protected void onPause()
  {
    Util.log(this.mLogger, "onPause() (ConfigClient)");
    this.mDelegate.onPause();
    destroyLockDeviceOn();
    super.onPause();
  }

  protected void onResume()
  {
    super.onResume();
    Util.log(this.mLogger, "onResume() (ConfigClient)");
    lockDeviceOn();
    this.mDelegate.onResume();
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    if ((DialogFragment)getSupportFragmentManager().findFragmentByTag("pkcs12ShowPasswordWarning") != null)
    {
      paramBundle.putInt("dialog", this.mDialogShowing);
      removeMyDialog("pkcs12ShowPasswordWarning");
    }
    super.onSaveInstanceState(paramBundle);
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    if (paramEncapsulationService == null)
    {
      Log.d("XPC", "Unable to bind to the local service!");
      return;
    }
    Util.log(this.mLogger, "Service bound in ConfigureClient.");
    super.onServiceBind(paramEncapsulationService);
    if (this.mParser == null)
      Util.log(this.mLogger, "Config was not properly bound!");
    Util.log(this.mLogger, "+++ Showing ConfigureClient screen.");
    this.mDelegate.setRequiredSettings(this.mLogger, this.mParser, this.mGlobals, this.mFailure);
    this.mDelegate.restoreSavedData();
    if (haveTabs())
      setConfigureActive();
    if ((this.mDialogShowing != -1) && ((DialogFragment)getSupportFragmentManager().findFragmentByTag("pkcs12ShowPasswordWarning") == null))
      showMyDialog(10002);
    this.mDelegate.startWorkerThread();
  }

  public void setCallbackData(int paramInt, String paramString)
  {
    this.mResponseThreadCallbacks.dialogComplete(this.mDialogShowing, true);
    this.mDialogShowing = -1;
    this.mDelegate.signalWorker();
  }

  public void showMyDialog(int paramInt)
  {
    String str = null;
    this.mDialogShowing = paramInt;
    switch (paramInt)
    {
    case 10003:
    default:
      super.showMyDialog(paramInt);
    case 10000:
    case 10001:
    case 10002:
    case 10004:
    case 10005:
    case 10007:
      while (true)
      {
        showMyDialog(ConfigureClientFragments.newInstance(paramInt), str);
        return;
        str = "keystoreWarningTag";
        continue;
        str = "lockScreenWarningTag";
        continue;
        str = "pkcs12ShowPasswordWarning";
        continue;
        str = "pkcs12AltCaWarning";
        continue;
        str = "pkcs12ShowAltPasswordWarning";
        continue;
        str = "keystoreBasicInstallWarning";
      }
    case 10006:
      GotoScreen.setCredentialStore(this);
      return;
    case 10008:
    }
    GotoScreen.resetCredentialStore(this);
  }

  public static class ConfigureClientFragments extends DialogFragment
  {
    public static ConfigureClientFragments newInstance(int paramInt)
    {
      ConfigureClientFragments localConfigureClientFragments = new ConfigureClientFragments();
      Bundle localBundle = new Bundle();
      localBundle.putInt("id", paramInt);
      localConfigureClientFragments.setArguments(localBundle);
      return localConfigureClientFragments;
    }

    public Dialog onCreateDialog(Bundle paramBundle)
    {
      final int i = getArguments().getInt("id");
      final ConfigureClient localConfigureClient = (ConfigureClient)getActivity();
      MultiColorWarningDialog localMultiColorWarningDialog5;
      switch (i)
      {
      case 10006:
      default:
        localMultiColorWarningDialog5 = new MultiColorWarningDialog(localConfigureClient, i);
        if (i == 10000)
          localMultiColorWarningDialog5.setHtmlText(localConfigureClient.getResources().getString(localConfigureClient.mParcelHelper.getIdentifier("xpc_warn_credentials_string", "string")));
        break;
      case 10004:
      case 10002:
      case 10005:
      case 10007:
      case 10003:
      }
      while (true)
      {
        localMultiColorWarningDialog5.setCallback(localConfigureClient);
        return localMultiColorWarningDialog5;
        MultiColorWarningDialog localMultiColorWarningDialog4 = new MultiColorWarningDialog(localConfigureClient, 10004);
        localMultiColorWarningDialog4.setHtmlText(localConfigureClient.mDelegate.getPkcs12AltCaWarningMsg());
        localMultiColorWarningDialog4.setCallback(localConfigureClient);
        return localMultiColorWarningDialog4;
        MultiColorWarningDialog localMultiColorWarningDialog3 = new MultiColorWarningDialog(localConfigureClient, 10002);
        localMultiColorWarningDialog3.setHtmlText(localConfigureClient.mDelegate.getPkcs12WarningMsg());
        localMultiColorWarningDialog3.setCallback(localConfigureClient);
        return localMultiColorWarningDialog3;
        MultiColorWarningDialog localMultiColorWarningDialog2 = new MultiColorWarningDialog(localConfigureClient, 10005);
        localMultiColorWarningDialog2.setHtmlText(localConfigureClient.mDelegate.getPkcs12AltWarningMsg());
        localMultiColorWarningDialog2.setCallback(localConfigureClient);
        return localMultiColorWarningDialog2;
        MultiColorWarningDialog localMultiColorWarningDialog1 = new MultiColorWarningDialog(localConfigureClient, 10007);
        localMultiColorWarningDialog1.setHtmlText(localConfigureClient.mDelegate.getBasicInstallWarning());
        localMultiColorWarningDialog1.setCallback(localConfigureClient);
        return localMultiColorWarningDialog1;
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
        localBuilder.setMessage(localConfigureClient.mParcelHelper.getIdentifier("xpc_keystore_error_string", "string"));
        localBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
          {
            localConfigureClient.setCallbackData(i, null);
            ConfigureClient.ConfigureClientFragments.this.dismiss();
          }
        });
        setCancelable(false);
        return localBuilder.create();
        localMultiColorWarningDialog5.setHtmlText(localConfigureClient.getResources().getString(localConfigureClient.mParcelHelper.getIdentifier("xpc_warn_credentials4_string", "string")));
      }
    }
  }
}