package net.cloudpath.xpressconnect;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import net.cloudpath.xpressconnect.screens.MDMTarget;

public class DeviceAdminReceiverChild extends DeviceAdminReceiver
{
  private void fireIntent(Context paramContext, int paramInt)
  {
    Intent localIntent = new Intent(paramContext, MDMTarget.class);
    localIntent.putExtra("result", paramInt);
    localIntent.setFlags(268435456);
    paramContext.startActivity(localIntent);
  }

  public CharSequence onDisableRequested(Context paramContext, Intent paramIntent)
  {
    return "Disabling this application may prevent you from accessing some of your wifi networks.";
  }

  public void onDisabled(Context paramContext, Intent paramIntent)
  {
    super.onDisabled(paramContext, paramIntent);
  }

  public void onEnabled(Context paramContext, Intent paramIntent)
  {
    super.onEnabled(paramContext, paramIntent);
  }

  public void onPasswordChanged(Context paramContext, Intent paramIntent)
  {
    fireIntent(paramContext, 1);
    super.onPasswordChanged(paramContext, paramIntent);
  }

  public void onPasswordExpiring(Context paramContext, Intent paramIntent)
  {
    fireIntent(paramContext, 2);
    super.onPasswordExpiring(paramContext, paramIntent);
  }

  public void onPasswordFailed(Context paramContext, Intent paramIntent)
  {
    super.onPasswordFailed(paramContext, paramIntent);
  }

  public void onPasswordSucceeded(Context paramContext, Intent paramIntent)
  {
    super.onPasswordSucceeded(paramContext, paramIntent);
  }

  void showToast(Context paramContext, CharSequence paramCharSequence)
  {
    Toast.makeText(paramContext, paramCharSequence, 1).show();
  }
}