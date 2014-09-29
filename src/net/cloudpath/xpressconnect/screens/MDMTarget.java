package net.cloudpath.xpressconnect.screens;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.DeviceAdminReceiverChild;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;

public class MDMTarget extends ScreenBase
{
  public static final int DIALOG_PWD_CHANGE_WARNING = 9999;
  public static final String DIALOG_PWD_CHANGE_WARNING_TAG = "pwdChangeWarning";
  public static final int PASSWORD_EXPIRED = 2;
  public static final int PASSWORD_UPDATED = 1;
  public static final int RESULT_PASSWORD = 99999;
  private TextView mConfigText = null;
  private DevicePolicyManager mDPM = null;
  private ParcelHelper mHelp = null;
  private ImageView mSpinner = null;
  private ComponentName mXpcAdmin = null;
  private int toProcess = -1;

  private void updatePassword()
  {
    Log.d("XPC", "MDMTarget updating password.");
    this.mDPM = ((DevicePolicyManager)getSystemService("device_policy"));
    this.mXpcAdmin = new ComponentName(this, DeviceAdminReceiverChild.class);
    if (!this.mDPM.isAdminActive(this.mXpcAdmin))
    {
      Log.d("XPC", "XPC : Admin mode isn't active.  Ignoring password expiration notification.");
      return;
    }
    showMyDialog(MDMTargetFragments.newInstance(9999), "pwdChangeWarning");
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    finish();
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  protected void onCreate(Bundle paramBundle)
  {
    this.mHelp = new ParcelHelper("", this);
    preContentView();
    Log.d("XPC", "MDMTarget called.");
    setContentView(this.mHelp.getLayoutId("xpc_loadingconfig"));
    this.mSpinner = ((ImageView)findViewById(this.mHelp.getItemId("xpc_spinnerImage")));
    this.mSpinner.setVisibility(8);
    this.mConfigText = ((TextView)findViewById(this.mHelp.getItemId("xpc_configText")));
    this.mConfigText.setText(this.mHelp.getIdentifier("xpc_check_dev_settings", "string"));
    this.toProcess = getIntent().getIntExtra("result", -1);
    super.onCreate(paramBundle);
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    if (paramEncapsulationService == null)
    {
      finish();
      return;
    }
    super.onServiceBind(paramEncapsulationService);
    if (this.mParser == null)
      Util.log(this.mLogger, "Config was not properly bound!");
    Log.d("XPC", "MDMTarget service bound. toProcess = " + this.toProcess);
    switch (this.toProcess)
    {
    default:
      return;
    case 1:
      paramEncapsulationService.setPasswordSet(1);
      finish();
      return;
    case 2:
    }
    updatePassword();
  }

  public static class MDMTargetFragments extends DialogFragment
  {
    public static MDMTargetFragments newInstance(int paramInt)
    {
      MDMTargetFragments localMDMTargetFragments = new MDMTargetFragments();
      Bundle localBundle = new Bundle();
      localBundle.putInt("id", paramInt);
      localMDMTargetFragments.setArguments(localBundle);
      return localMDMTargetFragments;
    }

    public Dialog onCreateDialog(Bundle paramBundle)
    {
      int i = getArguments().getInt("id");
      final ScreenBase localScreenBase = (ScreenBase)getActivity();
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
      switch (i)
      {
      default:
        return null;
      case 9999:
      }
      localBuilder.setMessage(((MDMTarget)localScreenBase).mHelp.getIdentifier("xpc_need_to_change_pwd", "string"));
      localBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          Intent localIntent = new Intent("android.app.action.SET_NEW_PASSWORD");
          MDMTarget.MDMTargetFragments.this.startActivityForResult(localIntent, 99999);
        }
      });
      localBuilder.setOnCancelListener(new DialogInterface.OnCancelListener()
      {
        public void onCancel(DialogInterface paramAnonymousDialogInterface)
        {
          Util.log(localScreenBase.mLogger, "User hit the back button.");
          localScreenBase.finish();
          Toast.makeText(localScreenBase, ((MDMTarget)localScreenBase).mHelp.getIdentifier("xpc_pwd_change_later", "string"), 1).show();
        }
      });
      return localBuilder.create();
    }
  }
}