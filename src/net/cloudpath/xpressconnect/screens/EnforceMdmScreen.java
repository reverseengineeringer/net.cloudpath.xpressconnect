package net.cloudpath.xpressconnect.screens;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.DeviceAdminReceiverChild;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.MdmDetection;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.ProfileElement;
import net.cloudpath.xpressconnect.parsers.config.SettingElement;

public class EnforceMdmScreen extends ScreenBase
{
  private static final int RESULT_ENABLE = 100;
  private static final int RESULT_PASSWORD = 101;
  private static final int SHOW_RETRY_WARNING = 1000;
  private static final String SHOW_RETRY_WARNING_TAG = "retryWarning";
  private TextView mConfigText = null;
  private DevicePolicyManager mDPM = null;
  private ImageView mSpinner = null;
  private AnimationDrawable mSpinnerAnim = null;
  private ComponentName mXpcAdmin = null;

  private void activateAdminMode()
  {
    if (!this.mDPM.isAdminActive(this.mXpcAdmin))
    {
      this.mConfigText.setText(this.mParcelHelper.getIdentifier("xpc_activate_admin", "string"));
      Intent localIntent = new Intent("android.app.action.ADD_DEVICE_ADMIN");
      localIntent.putExtra("android.app.extra.DEVICE_ADMIN", this.mXpcAdmin);
      localIntent.putExtra("android.app.extra.ADD_EXPLANATION", getResources().getString(this.mParcelHelper.getIdentifier("xpc_admin_mode_reason", "string")));
      try
      {
        startActivityForResult(localIntent, 100);
        return;
      }
      catch (ActivityNotFoundException localActivityNotFoundException)
      {
        Util.log(this.mLogger, "Device doesn't support administrator mode!");
        if (allOptional())
        {
          success();
          return;
        }
        done(35);
        this.mFailure.setFailReason(FailureReason.useWarningIcon, getResources().getString(this.mParcelHelper.getIdentifier("xpc_mdm_password_invalid", "string")), 9);
        return;
      }
    }
    checkSettingsState();
  }

  private boolean allOptional()
  {
    if (!checkOptional(40073));
    while ((!checkOptional(40072)) || (!checkOptional(40071)) || (!checkOptional(40070)) || (!checkOptional(40069)) || (!checkOptional(40068)) || (!checkOptional(40067)) || (!checkOptional(40066)) || (!checkOptional(40065)) || (!checkOptional(40064)) || (!checkOptional(40063)) || (!checkOptional(40062)) || (!checkOptional(40061)))
      return false;
    return true;
  }

  private boolean checkOptional(int paramInt)
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, paramInt);
    if (localSettingElement == null);
    while (!localSettingElement.isRequired.booleanValue())
      return true;
    return false;
  }

  private void checkSettingsState()
  {
    this.mConfigText.setText(this.mParcelHelper.getIdentifier("xpc_checking_mdm_settings", "string"));
    this.mGlobals.resetPasswordSet();
    if ((needLockScreenAndPasswordStrength()) && (!setLockScreenAndPasswordStrength()));
    while (((needMaximumFailedPasswordsToWipe()) && (!setMaximumFailedPasswordsToWipe())) || ((needMaximumDeviceLockTime()) && (!setMaximumDeviceLockTime())) || ((needMaximumPasswordExpireTime()) && (!setMaximumPasswordExpireTime())) || ((needMaximumHistoryLength()) && (!setMaximumHistoryLength())) || ((needMinimumPasswordLength()) && (!setMinimumPasswordLength())) || ((needMinimumNumLetters()) && (!setMinimumNumLetters())) || ((needMinimumLowerCaseLetters()) && (!setMinimumLowerCaseLetters())) || ((needMinimumNonLetters()) && (!setMinimumNonLetters())) || ((needMinimumNumerics()) && (!setMinimumNumerics())) || ((needMinimumSymbols()) && (!setMinimumSymbols())) || ((needMinimumUppercase()) && (!setMinimumUppercase())))
      return;
    if (needToForcePasswordChange())
    {
      forcePasswordChange();
      return;
    }
    success();
  }

  private boolean forcePasswordChange()
  {
    if ((this.mParser.selectedProfile.getSetting(0, 40062) == null) && (this.mDPM.isActivePasswordSufficient() == true))
    {
      Util.log(this.mLogger, "Attempted to force a password change when the setting element wasn't defined!?");
      return true;
    }
    Util.log(this.mLogger, "Password change is required.  Forcing..");
    startActivityForResult(new Intent("android.app.action.SET_NEW_PASSWORD"), 101);
    return true;
  }

  private boolean isVersion11OrHigher()
  {
    if (Build.VERSION.SDK_INT < 11)
    {
      done(35);
      this.mFailure.setFailReason(FailureReason.useWarningIcon, getResources().getString(this.mParcelHelper.getIdentifier("xpc_mdm_password_invalid", "string")), 9);
      return false;
    }
    return true;
  }

  private boolean needLockScreenAndPasswordStrength()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40061);
    if (localSettingElement == null)
      return false;
    int i;
    switch (this.mDPM.getPasswordQuality(this.mXpcAdmin))
    {
    default:
      i = 0;
    case 65536:
    case 262144:
    case 327680:
    case 32768:
    case 393216:
    case 131072:
    }
    while (i < Util.parseInt(localSettingElement.requiredValue, 0))
    {
      return true;
      i = 10;
      continue;
      i = 40;
      continue;
      i = 50;
      continue;
      i = 20;
      continue;
      i = 60;
      continue;
      i = 30;
    }
  }

  private boolean needMaximumDeviceLockTime()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40064);
    if (localSettingElement == null);
    do
    {
      return false;
      Util.log(this.mLogger, "Current lock time : " + this.mDPM.getMaximumTimeToLock(this.mXpcAdmin) + "    Desired : " + 60 * (1000 * Util.parseInt(localSettingElement.additionalValue, 0)));
    }
    while (this.mDPM.getMaximumTimeToLock(this.mXpcAdmin) == 60 * (1000 * Util.parseInt(localSettingElement.additionalValue, 0)));
    return true;
  }

  private boolean needMaximumFailedPasswordsToWipe()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40063);
    if (localSettingElement == null);
    do
    {
      do
        return false;
      while (!isVersion11OrHigher());
      Util.log(this.mLogger, "Max failed pwd for wipe : " + this.mDPM.getMaximumFailedPasswordsForWipe(this.mXpcAdmin) + "   Desired : " + localSettingElement.additionalValue);
    }
    while (this.mDPM.getMaximumFailedPasswordsForWipe(this.mXpcAdmin) == Util.parseInt(localSettingElement.additionalValue, 0));
    return true;
  }

  private boolean needMaximumHistoryLength()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40066);
    if (localSettingElement == null);
    do
    {
      do
        return false;
      while (!isVersion11OrHigher());
      Util.log(this.mLogger, "Max pwd history length : " + this.mDPM.getPasswordHistoryLength(this.mXpcAdmin) + "  Desired : " + localSettingElement.additionalValue);
    }
    while (this.mDPM.getPasswordHistoryLength(this.mXpcAdmin) == Util.parseInt(localSettingElement.additionalValue, 0));
    return true;
  }

  private boolean needMaximumPasswordExpireTime()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40065);
    if (localSettingElement == null);
    long l;
    do
    {
      do
        return false;
      while (!isVersion11OrHigher());
      l = Long.parseLong(localSettingElement.additionalValue);
      Util.log(this.mLogger, "pwd expiration : " + this.mDPM.getPasswordExpirationTimeout(this.mXpcAdmin) + "   Desired : " + 1000L * (l * 86400L));
    }
    while (this.mDPM.getPasswordExpirationTimeout(this.mXpcAdmin) == 1000L * (l * 86400L));
    return true;
  }

  private boolean needMinimumLowerCaseLetters()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40069);
    if (localSettingElement == null);
    do
    {
      do
        return false;
      while (!isVersion11OrHigher());
      Util.log(this.mLogger, "Min lower case : " + this.mDPM.getPasswordMinimumLowerCase(this.mXpcAdmin) + "   Desired : " + localSettingElement.additionalValue);
    }
    while (this.mDPM.getPasswordMinimumLowerCase(this.mXpcAdmin) == Util.parseInt(localSettingElement.additionalValue, 0));
    return true;
  }

  private boolean needMinimumNonLetters()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40070);
    if (localSettingElement == null);
    do
    {
      do
        return false;
      while (!isVersion11OrHigher());
      Util.log(this.mLogger, "Min pwd non-letter : " + this.mDPM.getPasswordMinimumNonLetter(this.mXpcAdmin) + "  Desired : " + localSettingElement.additionalValue);
    }
    while (this.mDPM.getPasswordMinimumNonLetter(this.mXpcAdmin) == Util.parseInt(localSettingElement.additionalValue, 0));
    return true;
  }

  private boolean needMinimumNumLetters()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40068);
    if (localSettingElement == null);
    do
    {
      do
        return false;
      while (!isVersion11OrHigher());
      Util.log(this.mLogger, "Min num letters : " + this.mDPM.getPasswordMinimumLetters(this.mXpcAdmin) + "   Desired : " + localSettingElement.additionalValue);
    }
    while (this.mDPM.getPasswordMinimumLetters(this.mXpcAdmin) == Util.parseInt(localSettingElement.additionalValue, 0));
    return true;
  }

  private boolean needMinimumNumerics()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40071);
    if (localSettingElement == null);
    do
    {
      do
        return false;
      while (!isVersion11OrHigher());
      Util.log(this.mLogger, "Min pwd numeric : " + this.mDPM.getPasswordMinimumNumeric(this.mXpcAdmin) + "  Desired : " + localSettingElement.additionalValue);
    }
    while (this.mDPM.getPasswordMinimumNumeric(this.mXpcAdmin) == Util.parseInt(localSettingElement.additionalValue, 0));
    return true;
  }

  private boolean needMinimumPasswordLength()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40067);
    if (localSettingElement == null);
    do
    {
      return false;
      Util.log(this.mLogger, "Min pwd length : " + this.mDPM.getPasswordMinimumLength(this.mXpcAdmin) + "  Desired : " + localSettingElement.additionalValue);
    }
    while (this.mDPM.getPasswordMinimumLength(this.mXpcAdmin) == Util.parseInt(localSettingElement.additionalValue, 0));
    return true;
  }

  private boolean needMinimumSymbols()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40072);
    if (localSettingElement == null);
    do
    {
      do
        return false;
      while (!isVersion11OrHigher());
      Util.log(this.mLogger, "Min symbols : " + this.mDPM.getPasswordMinimumSymbols(this.mXpcAdmin) + "   Desired : " + localSettingElement.additionalValue);
    }
    while (this.mDPM.getPasswordMinimumSymbols(this.mXpcAdmin) == Util.parseInt(localSettingElement.additionalValue, 0));
    return true;
  }

  private boolean needMinimumUppercase()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40073);
    if (localSettingElement == null);
    do
    {
      do
        return false;
      while (!isVersion11OrHigher());
      Util.log(this.mLogger, "Min upper case : " + this.mDPM.getPasswordMinimumUpperCase(this.mXpcAdmin) + "   Desired : " + localSettingElement.additionalValue);
    }
    while (this.mDPM.getPasswordMinimumUpperCase(this.mXpcAdmin) == Util.parseInt(localSettingElement.additionalValue, 0));
    return true;
  }

  private boolean needToForcePasswordChange()
  {
    if (!this.mDPM.isActivePasswordSufficient())
      Util.log(this.mLogger, "Forcing new password because the active one isn't sufficient!");
    SettingElement localSettingElement;
    do
    {
      return true;
      localSettingElement = this.mParser.selectedProfile.getSetting(0, 40062);
      if (localSettingElement == null)
        return false;
    }
    while (Util.parseInt(localSettingElement.requiredValue, 0) == 1);
    return false;
  }

  private boolean setLockScreenAndPasswordStrength()
  {
    if ((this.mParser == null) || (this.mParser.selectedProfile == null))
    {
      Util.log(this.mLogger, "Parser data is null in setLockScreenAndPasswordStrength().");
      return false;
    }
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40061);
    if (localSettingElement == null)
    {
      Util.log(this.mLogger, "Attempted to set a screen lock and password stength when the setting element wasn't defined!?");
      return true;
    }
    switch (Util.parseInt(localSettingElement.requiredValue, 0))
    {
    default:
      Util.log(this.mLogger, "Attempt to set the password complexity to something unknown!");
      done(35);
      this.mFailure.setFailReason(FailureReason.useWarningIcon, getResources().getString(this.mParcelHelper.getIdentifier("xpc_mdm_password_invalid_type", "string")), 9);
      return false;
    case 0:
      Util.log(this.mLogger, "Attempted to set no passcode being required.  (This shouldn't happen.)");
      return true;
    case 10:
      Util.log(this.mLogger, "Setting password to 'something'");
      this.mDPM.setPasswordQuality(this.mXpcAdmin, 65536);
      return true;
    case 40:
      Util.log(this.mLogger, "Setting password to 'alphabetic'");
      this.mDPM.setPasswordQuality(this.mXpcAdmin, 262144);
      return true;
    case 50:
      Util.log(this.mLogger, "Setting password to 'alphanumeric'");
      this.mDPM.setPasswordQuality(this.mXpcAdmin, 327680);
      return true;
    case 20:
      Util.log(this.mLogger, "Setting password to 'weak biometric'");
      if (Build.VERSION.SDK_INT < 14)
      {
        Util.log(this.mLogger, "Can't use 'weak biometric' password on this OS version as it isn't supported!");
        done(35);
        this.mFailure.setFailReason(FailureReason.useWarningIcon, getResources().getString(this.mParcelHelper.getIdentifier("xpc_mdm_password_invalid", "string")), 9);
        return false;
      }
      try
      {
        this.mDPM.setPasswordQuality(this.mXpcAdmin, 32768);
        return true;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        localIllegalArgumentException.printStackTrace();
        Util.log(this.mLogger, "Exception while trying to set password requirement to 'weak biometric'.  Exception : " + localIllegalArgumentException.getMessage());
        done(35);
        this.mFailure.setFailReason(FailureReason.useWarningIcon, getResources().getString(this.mParcelHelper.getIdentifier("xpc_mdm_password_invalid", "string")), 9);
        return false;
      }
    case 60:
      Util.log(this.mLogger, "Setting password to 'complex'");
      if (Build.VERSION.SDK_INT < 11)
      {
        Util.log(this.mLogger, "Unable to set password type to 'complex' because it isn't supported by this version of the OS!");
        done(35);
        this.mFailure.setFailReason(FailureReason.useWarningIcon, getResources().getString(this.mParcelHelper.getIdentifier("xpc_mdm_password_invalid", "string")), 9);
        return false;
      }
      this.mDPM.setPasswordQuality(this.mXpcAdmin, 393216);
      return true;
    case 30:
    }
    Util.log(this.mLogger, "Setting password to 'numeric'");
    this.mDPM.setPasswordQuality(this.mXpcAdmin, 131072);
    return true;
  }

  private boolean setMaximumDeviceLockTime()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40064);
    if (localSettingElement == null)
    {
      Util.log(this.mLogger, "Attempted to set a maximum idle time when the setting element wasn't defined!?");
      return true;
    }
    int i = Util.parseInt(localSettingElement.additionalValue, 0);
    Util.log(this.mLogger, "Setting maximum device lock time to " + i + " minute(s).");
    this.mDPM.setMaximumTimeToLock(this.mXpcAdmin, 60 * (i * 1000));
    return true;
  }

  private boolean setMaximumFailedPasswordsToWipe()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40063);
    if (localSettingElement == null)
    {
      Util.log(this.mLogger, "Attempted to set a maximum number of failed password attempts before wipe when the setting element wasn't defined!?");
      return true;
    }
    if (!isVersion11OrHigher())
    {
      Util.log(this.mLogger, "Unable to set the maximum failed passwords before wiping.  It isn't supported by the OS.");
      return false;
    }
    int i = Util.parseInt(localSettingElement.additionalValue, 0);
    Util.log(this.mLogger, "Setting maximum failed passwords for wipe to " + i + ".");
    this.mDPM.setMaximumFailedPasswordsForWipe(this.mXpcAdmin, i);
    return true;
  }

  private boolean setMaximumHistoryLength()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40066);
    if (localSettingElement == null)
    {
      Util.log(this.mLogger, "Attempted to set a maximum password history when the setting element wasn't defined!?");
      return true;
    }
    if (!isVersion11OrHigher())
    {
      Util.log(this.mLogger, "Unable to set maximum password history length because it isn't supported by the OS!");
      return false;
    }
    int i = Util.parseInt(localSettingElement.additionalValue, 0);
    Util.log(this.mLogger, "Setting maximum password history length to " + i + ".");
    this.mDPM.setPasswordHistoryLength(this.mXpcAdmin, i);
    return true;
  }

  private boolean setMaximumPasswordExpireTime()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40065);
    if (localSettingElement == null)
    {
      Util.log(this.mLogger, "Attempted to set a maximum password expire time when the setting element wasn't defined!?");
      return true;
    }
    if (!isVersion11OrHigher())
    {
      Util.log(this.mLogger, "Unable to set the maximum password expire time because it isn't supported by the OS!");
      return false;
    }
    long l = Long.parseLong(localSettingElement.additionalValue);
    Util.log(this.mLogger, "Setting maximum password expire time to " + l + ".");
    this.mDPM.setPasswordExpirationTimeout(this.mXpcAdmin, 1000L * (86400L * l));
    return true;
  }

  private boolean setMinimumLowerCaseLetters()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40069);
    if (localSettingElement == null)
    {
      Util.log(this.mLogger, "Attempted to set a minimum number of lower case letters when the setting element wasn't defined!?");
      return true;
    }
    if (!isVersion11OrHigher())
    {
      Util.log(this.mLogger, "Unable to set the minimum number of lower case letters because it isn't supported by the OS!");
      return false;
    }
    int i = Util.parseInt(localSettingElement.additionalValue, 0);
    this.mDPM.setPasswordMinimumLowerCase(this.mXpcAdmin, i);
    return true;
  }

  private boolean setMinimumNonLetters()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40070);
    if (localSettingElement == null)
    {
      Util.log(this.mLogger, "Attempted to set a minimum number of non-letters when the setting element wasn't defined!?");
      return true;
    }
    if (!isVersion11OrHigher())
    {
      Util.log(this.mLogger, "Unable to set the minimum of non-letters in the password because the underlying OS doesn't support it!");
      return false;
    }
    int i = Util.parseInt(localSettingElement.additionalValue, 0);
    Util.log(this.mLogger, "Setting the minimum number of non-letters in the password to " + i + ".");
    this.mDPM.setPasswordMinimumNonLetter(this.mXpcAdmin, i);
    return true;
  }

  private boolean setMinimumNumLetters()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40068);
    if (localSettingElement == null)
    {
      Util.log(this.mLogger, "Attempted to set a minimum number of letters in a password when the setting element wasn't defined!?");
      return true;
    }
    if (!isVersion11OrHigher())
    {
      Util.log(this.mLogger, "Unable to set the minimum number of letters for the password because the OS doesn't support it!");
      return false;
    }
    int i = Util.parseInt(localSettingElement.additionalValue, 0);
    Util.log(this.mLogger, "Setting minimum number of letters in the password to " + i + ".");
    this.mDPM.setPasswordMinimumLetters(this.mXpcAdmin, i);
    return true;
  }

  private boolean setMinimumNumerics()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40071);
    if (localSettingElement == null)
    {
      Util.log(this.mLogger, "Attempted to set a minimum number of numerics when the setting element wasn't defined!?");
      return true;
    }
    if (!isVersion11OrHigher())
    {
      Util.log(this.mLogger, "Unable to set the minimum number of numerics in the password because the underlying OS doesn't support it!");
      return false;
    }
    int i = Util.parseInt(localSettingElement.additionalValue, 0);
    Util.log(this.mLogger, "Setting minimum number of numeric values in the password to " + i + ".");
    this.mDPM.setPasswordMinimumNumeric(this.mXpcAdmin, i);
    return true;
  }

  private boolean setMinimumPasswordLength()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40067);
    if (localSettingElement == null)
    {
      Util.log(this.mLogger, "Attempted to set a minimum password length when the setting element wasn't defined!?");
      return true;
    }
    int i = Util.parseInt(localSettingElement.additionalValue, 0);
    Util.log(this.mLogger, "Setting minimum password length to " + i + ".");
    this.mDPM.setPasswordMinimumLength(this.mXpcAdmin, i);
    return true;
  }

  private boolean setMinimumSymbols()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40072);
    if (localSettingElement == null)
    {
      Util.log(this.mLogger, "Attempted to set a minimum number of symbols when the setting element wasn't defined!?");
      return true;
    }
    if (!isVersion11OrHigher())
    {
      Util.log(this.mLogger, "Unable to set the minimum number of symbols because the underlying OS doesn't support that setting!");
      return false;
    }
    int i = Util.parseInt(localSettingElement.additionalValue, 0);
    Util.log(this.mLogger, "Setting minimum number of required symbols in the password to " + i + ".");
    this.mDPM.setPasswordMinimumSymbols(this.mXpcAdmin, i);
    return true;
  }

  private boolean setMinimumUppercase()
  {
    SettingElement localSettingElement = this.mParser.selectedProfile.getSetting(0, 40073);
    if (localSettingElement == null)
    {
      Util.log(this.mLogger, "Attempted to set a minimum number of uppercase letters in password when the setting element wasn't defined!?");
      return true;
    }
    if (!isVersion11OrHigher())
    {
      Util.log(this.mLogger, "Unable to set minimum required uppercase characters because the underlying OS doesn't support that setting!");
      return false;
    }
    int i = Util.parseInt(localSettingElement.additionalValue, 0);
    Util.log(this.mLogger, "Setting minimum number of uppercase characters to " + i + ".");
    this.mDPM.setPasswordMinimumUpperCase(this.mXpcAdmin, i);
    return true;
  }

  private void success()
  {
    done(1);
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (paramInt1 == 100)
    {
      if (paramInt2 == -1)
      {
        checkSettingsState();
        return;
      }
      if (allOptional())
      {
        success();
        return;
      }
      done(35);
      this.mFailure.setFailReason(FailureReason.useWarningIcon, getResources().getString(this.mParcelHelper.getIdentifier("xpc_mdm_failed", "string")), 12);
      return;
    }
    if (paramInt1 == 101)
    {
      if ((this.mParser == null) || (this.mParser.selectedProfile == null))
      {
        Util.log(this.mLogger, "Parser data is null in onActivityResult().");
        done(35);
        this.mFailure.setFailReason(FailureReason.useWarningIcon, getResources().getString(this.mParcelHelper.getIdentifier("xpc_mdm_failed", "string")), 12);
        return;
      }
      if ((this.mGlobals.getPasswordSet() == 1) || (this.mDPM.isActivePasswordSufficient() == true))
      {
        success();
        return;
      }
      if (allOptional())
      {
        showMyDialog(EnforceMdmFragments.newInstance(1000), "retryWarning");
        return;
      }
      done(35);
      this.mFailure.setFailReason(FailureReason.useWarningIcon, getResources().getString(this.mParcelHelper.getIdentifier("xpc_mdm_failed", "string")), 12);
      return;
    }
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  protected void onCreate(Bundle paramBundle)
  {
    Log.d("XPC", "onCreate() called! (enforcemdmscreen)");
    preContentView();
    setContentView(new ParcelHelper("", this).getLayoutId("xpc_enforcemdm"));
    super.onCreate(paramBundle);
    this.mDPM = ((DevicePolicyManager)getSystemService("device_policy"));
    this.mXpcAdmin = new ComponentName(this, DeviceAdminReceiverChild.class);
    this.mConfigText = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_configText")));
    if (haveTabs())
      setConfigureActive();
    this.mSpinner = ((ImageView)findViewById(this.mParcelHelper.getItemId("xpc_spinnerImage")));
    if (this.mSpinner != null)
    {
      this.mSpinnerAnim = ((AnimationDrawable)this.mSpinner.getDrawable());
      this.mSpinnerAnim.setCallback(this.mSpinner);
      this.mSpinnerAnim.setVisible(true, true);
      this.mSpinner.post(new Runnable()
      {
        public void run()
        {
          Log.d("XPC", "*** Starting animation!");
          EnforceMdmScreen.this.mSpinnerAnim.start();
        }
      });
    }
    this.mConfigText.setText(this.mParcelHelper.getIdentifier("xpc_checking_mdm_state", "string"));
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    if (paramEncapsulationService == null)
    {
      Log.d("XPC", "Unable to bind to the local service!");
      return;
    }
    Util.log(this.mLogger, "Service bound in EnforceMDM.");
    super.onServiceBind(paramEncapsulationService);
    if (this.mParser == null)
      Util.log(this.mLogger, "Config was not properly bound!");
    Util.log(this.mLogger, "+++ Showing EnforceMDM screen.");
    if (haveTabs())
      setConfigureActive();
    if ((this.mParser != null) && (this.mParser.savedConfigInfo != null) && (this.mParser.savedConfigInfo.mBeenThroughOnce == true))
    {
      Util.log(this.mLogger, "Second time through, skipping this screen.");
      success();
      return;
    }
    if (MdmDetection.needToConfigureMdm(this.mParser))
    {
      if (Build.VERSION.SDK_INT < 8)
      {
        Util.log(this.mLogger, "Running on a pre-2.2 OS version, but config wants MDM settings.  Failing.");
        done(35);
        this.mFailure.setFailReason(FailureReason.useWarningIcon, getResources().getString(this.mParcelHelper.getIdentifier("xpc_mdm_password_invalid", "string")), 9);
        return;
      }
      activateAdminMode();
      return;
    }
    success();
  }

  public static class EnforceMdmFragments extends DialogFragment
  {
    public static EnforceMdmFragments newInstance(int paramInt)
    {
      EnforceMdmFragments localEnforceMdmFragments = new EnforceMdmFragments();
      Bundle localBundle = new Bundle();
      localBundle.putInt("id", paramInt);
      localEnforceMdmFragments.setArguments(localBundle);
      return localEnforceMdmFragments;
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
      case 1000:
      }
      localBuilder.setMessage(localScreenBase.mParcelHelper.getIdentifier("xpc_optional_mdm", "string"));
      localBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          ((EnforceMdmScreen)localScreenBase).activateAdminMode();
        }
      });
      localBuilder.setNegativeButton("Skip", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          ((EnforceMdmScreen)localScreenBase).success();
        }
      });
      localBuilder.setOnCancelListener(new DialogInterface.OnCancelListener()
      {
        public void onCancel(DialogInterface paramAnonymousDialogInterface)
        {
          ((EnforceMdmScreen)localScreenBase).success();
        }
      });
      return localBuilder.create();
    }
  }
}