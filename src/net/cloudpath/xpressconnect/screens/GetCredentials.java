package net.cloudpath.xpressconnect.screens;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.cloudpath.common.util.Encode;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.util.List;
import java.util.Locale;
import net.cloudpath.xpressconnect.CredentialRegexHandler;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.GestureCallback;
import net.cloudpath.xpressconnect.LocalDbHelper;
import net.cloudpath.xpressconnect.MapVariables;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.nativeproxy.WifiConfigurationProxy;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkElement;
import net.cloudpath.xpressconnect.parsers.config.NetworkItemElement;
import net.cloudpath.xpressconnect.parsers.config.ProfileElement;
import net.cloudpath.xpressconnect.parsers.config.SettingElement;
import net.cloudpath.xpressconnect.remote.RemoteUpload;
import net.cloudpath.xpressconnect.remote.WebInterface;

public class GetCredentials extends ScreenBase
  implements View.OnClickListener, View.OnFocusChangeListener, CompoundButton.OnCheckedChangeListener, GestureCallback
{
  private static final int GOT_CREDENTIALS = 1000;
  private static final int INVALID_USERNAME_FORMAT = 414;
  private static final int PROMPT_FOR_NO_PASSWORD = 10000;
  private static final String PROMPT_FOR_NO_PASSWORD_TAG = "promptForNoPassword";
  private TextView errorText = null;
  private TextView forgotPasswordText = null;
  private Button okayButton = null;
  private EditText passwordEdit = null;
  private TextView passwordText = null;
  private CredentialRegexHandler regex = null;
  private CheckBox showPassword = null;
  private TextView upwText = null;
  private EditText usernameEdit = null;
  private TextView usernameText = null;

  private void finishClick()
  {
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "No parser bound in onClick()!");
      return;
    }
    if (this.regex == null)
    {
      Util.log(this.mLogger, "regex is null in onClick()!");
      return;
    }
    if (this.mParser.selectedNetwork == null)
    {
      Util.log(this.mLogger, "No network selected in onClick()!");
      return;
    }
    if (this.regex.regexIsValid(this.usernameEdit.getText().toString(), this.mParser.selectedNetwork.preCredentialRegex))
    {
      stashUsernameAndPassword();
      if ((0x4 & this.mParser.selectedNetwork.remoteUpload) == 4)
      {
        new UploadDataTask().execute(new Void[] { null, null });
        return;
      }
      finishClickChecks(Boolean.valueOf(true));
      return;
    }
    String str = this.mParser.getOptionById(414);
    this.errorText.setText(str);
    this.errorText.setVisibility(0);
  }

  private void finishClickChecks(Boolean paramBoolean)
  {
    if (!paramBoolean.booleanValue())
    {
      String str = getResources().getString(this.mParcelHelper.getIdentifier("xpc_cant_contact_data_report_server", "string"));
      this.errorText.setText(str);
      this.errorText.setVisibility(0);
      return;
    }
    if ((this.mParser.selectedNetwork.registrationUrl != null) && (this.mParser.selectedNetwork.registrationUrl.length() > 0))
      done(19);
    while (true)
    {
      Util.log(this.mLogger, "--- Leaving GetCredentials after credentials were gathered and vetted.");
      return;
      done(1);
    }
  }

  private void getExistingCreds()
  {
    int i = isConnectionFound();
    if (i >= 0)
    {
      WifiConfigurationProxy localWifiConfigurationProxy = getExistingConfig(i);
      if ((localWifiConfigurationProxy.getId() != null) && (!localWifiConfigurationProxy.getId().contentEquals("")) && (this.mParser != null) && (this.mParser.savedConfigInfo != null) && (this.mParser.savedConfigInfo.username != null) && (!this.mParser.savedConfigInfo.username.startsWith("Enrollment")))
        this.usernameEdit.setText(localWifiConfigurationProxy.getId());
    }
  }

  private void stashUsernameAndPassword()
  {
    if (this.mParser == null)
      Util.log(this.mLogger, "No parser bound in stashUsernameAndPassword()!");
    do
    {
      return;
      if (this.mParser.savedConfigInfo == null)
      {
        Util.log(this.mLogger, "No saved configuration info in stashUsernameAndPassword()!");
        return;
      }
      this.mParser.savedConfigInfo.username = this.usernameEdit.getText().toString();
      this.mParser.savedConfigInfo.password = this.passwordEdit.getText().toString();
      if ((this.mParser.savedConfigInfo.username == null) || (this.mParser.savedConfigInfo.username.length() <= 0))
        Util.log(this.mLogger, "+++++ User name is blank.");
      if ((this.mParser.savedConfigInfo.password == null) || (this.mParser.savedConfigInfo.password.length() <= 0))
        Util.log(this.mLogger, "+++++ Password is blank.");
    }
    while (this.mLogger == null);
    this.mLogger.addBanishedWord(this.mParser.savedConfigInfo.username);
    this.mLogger.addBanishedWord(this.mParser.savedConfigInfo.password);
  }

  public WifiConfigurationProxy getExistingConfig(int paramInt)
  {
    List localList = ((WifiManager)getSystemService("wifi")).getConfiguredNetworks();
    for (int i = 0; i < localList.size(); i++)
      if (((WifiConfiguration)localList.get(i)).networkId == paramInt)
        try
        {
          WifiConfigurationProxy localWifiConfigurationProxy = new WifiConfigurationProxy((WifiConfiguration)localList.get(i), this.mLogger);
          return localWifiConfigurationProxy;
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          Util.log(this.mLogger, "IllegalArgumentException in getExistingConfig()!");
          localIllegalArgumentException.printStackTrace();
          return null;
        }
        catch (NoSuchFieldException localNoSuchFieldException)
        {
          Util.log(this.mLogger, "NoSuchFieldException in getExistingConfig()!");
          localNoSuchFieldException.printStackTrace();
          return null;
        }
        catch (ClassNotFoundException localClassNotFoundException)
        {
          Util.log(this.mLogger, "ClassNotFoundException in getExistingConfig()!");
          localClassNotFoundException.printStackTrace();
          return null;
        }
        catch (IllegalAccessException localIllegalAccessException)
        {
          Util.log(this.mLogger, "IllegalAccessException in getExistingConfig()!");
          localIllegalAccessException.printStackTrace();
          return null;
        }
    return null;
  }

  public void handleDumpDatabase()
  {
    LocalDbHelper.dumpDbToSdCard(this);
  }

  public void handleNfcProgrammerGesture()
  {
    Toast.makeText(this, "Starting NFC Programmer...", 0).show();
    done(39);
  }

  public int isConnectionFound()
  {
    List localList = ((WifiManager)getSystemService("wifi")).getConfiguredNetworks();
    if (localList == null)
    {
      Util.log(this.mLogger, "No networks in the network list.  (So the network isn't found.)");
      return -1;
    }
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "Parser isn't bound in isConnectionFound()!");
      return -2;
    }
    if (this.mParser.selectedProfile == null)
    {
      Util.log(this.mLogger, "No profile is selected in isConnectionFound()!");
      return -2;
    }
    if (this.mFailure == null)
    {
      Util.log(this.mLogger, "Failure class is null in isConnectionFound()!");
      return -2;
    }
    Util.log(this.mLogger, "Your device currently has " + localList.size() + " wireless network(s) configured.");
    int i = 0;
    if (i < localList.size())
    {
      WifiConfigurationProxy localWifiConfigurationProxy;
      SettingElement localSettingElement;
      try
      {
        localWifiConfigurationProxy = new WifiConfigurationProxy((WifiConfiguration)localList.get(i), this.mLogger);
        localSettingElement = this.mParser.selectedProfile.getSetting(1, 40001);
        if (localSettingElement == null)
        {
          Util.log(this.mLogger, "Unable to locate the SSID element!");
          this.mFailure.setFailReason(FailureReason.useErrorIcon, getResources().getString(this.mParcelHelper.getIdentifier("xpc_no_ssid_defined", "string")), 6);
          return -2;
        }
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        Util.log(this.mLogger, "IllegalArgumentException in isConnectionFound()!");
        localIllegalArgumentException.printStackTrace();
        return -2;
      }
      catch (NoSuchFieldException localNoSuchFieldException)
      {
        Util.log(this.mLogger, "NoSuchFieldException in isConnectionFound()!");
        localNoSuchFieldException.printStackTrace();
        return -2;
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        Util.log(this.mLogger, "ClassNotFoundException in isConnectionFound()!");
        localClassNotFoundException.printStackTrace();
        return -2;
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        Util.log(this.mLogger, "IllegalAccessException in isConnectionFound()!");
        localIllegalAccessException.printStackTrace();
        return -2;
      }
      String[] arrayOfString;
      if (localSettingElement.additionalValue != null)
      {
        arrayOfString = localSettingElement.additionalValue.split(":");
        if (arrayOfString.length != 3)
        {
          Util.log(this.mLogger, "The SSID element provided in the configuration file is invalid!");
          this.mFailure.setFailReason(FailureReason.useErrorIcon, getResources().getString(this.mParcelHelper.getIdentifier("xpc_ssid_element_invalid", "string")), 6);
          return -2;
        }
        if (this.mParser.savedConfigInfo != null)
          this.mParser.savedConfigInfo.ssid = arrayOfString[2];
        if (localWifiConfigurationProxy.getSsid() != null)
          break label429;
        Util.log(this.mLogger, "SSID was null parsing network string.");
      }
      label429: 
      while (!localWifiConfigurationProxy.getSsid().equals("\"" + arrayOfString[2] + "\""))
      {
        i++;
        break;
      }
      Util.log(this.mLogger, "Our SSID already exists.  It will be replaced.");
      return localWifiConfigurationProxy.getNetworkId();
    }
    return -1;
  }

  public void onBackPressed()
  {
    done(-2);
    Util.log(this.mLogger, "--- Leaving GetCredentials because user pressed the back button.");
    super.onBackPressed();
  }

  public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.passwordEdit.setInputType(0);
      this.passwordEdit.setTransformationMethod(null);
      this.passwordEdit.invalidate();
      return;
    }
    this.passwordEdit.setInputType(128);
    this.passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
    this.passwordEdit.invalidate();
  }

  public void onClick(View paramView)
  {
    if ((this.passwordEdit.getText().toString().length() <= 0) || (this.passwordEdit.getText().toString().equals("")))
    {
      showMyDialog(GetCredentialsFragments.newInstance(10000), "promptForNoPassword");
      return;
    }
    finishClick();
  }

  protected void onCreate(Bundle paramBundle)
  {
    ParcelHelper localParcelHelper = new ParcelHelper("", this);
    preContentView();
    setContentView(localParcelHelper.getLayoutId("xpc_get_credentials"));
    super.onCreate(paramBundle);
    this.okayButton = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_OkayButton")));
    this.okayButton.setOnClickListener(this);
    this.usernameText = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_UsernameText")));
    this.passwordText = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_PasswordText")));
    this.usernameEdit = ((EditText)findViewById(this.mParcelHelper.getItemId("xpc_UsernameEdit")));
    this.usernameEdit.setOnFocusChangeListener(this);
    this.usernameEdit.setSelectAllOnFocus(true);
    this.passwordEdit = ((EditText)findViewById(this.mParcelHelper.getItemId("xpc_PasswordEdit")));
    this.passwordEdit.setSelectAllOnFocus(true);
    this.errorText = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_RedErrorText")));
    this.errorText.setVisibility(4);
    this.forgotPasswordText = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_ForgotPasswordLink")));
    this.upwText = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_UpwDescription")));
    this.showPassword = ((CheckBox)findViewById(this.mParcelHelper.getItemId("xpc_showPassword")));
    this.showPassword.setOnCheckedChangeListener(this);
    if (paramBundle != null)
    {
      String str1 = paramBundle.getString("username");
      if (str1 != null)
        this.usernameEdit.setText(str1);
      String str2 = paramBundle.getString("password");
      if (str2 != null)
        this.passwordEdit.setText(str2);
    }
    this.okayButton.requestFocus();
    setGestureCallback(this);
    this.regex = new CredentialRegexHandler();
  }

  public void onFocusChange(View paramView, boolean paramBoolean)
  {
    if (!paramBoolean)
    {
      if (this.mParser == null)
        Util.log(this.mLogger, "No parser bound in onFocusChange()!");
    }
    else
      return;
    if (this.mParser.selectedNetwork == null)
    {
      Util.log(this.mLogger, "No network selected in onFocusChange()!");
      return;
    }
    this.usernameEdit.setText(this.regex.getCheckedUserName(this.usernameEdit.getText().toString(), this.mParser.selectedNetwork.defaultUsername, this.mParser.selectedNetwork.preCredentialSuffix, this.mParser.selectedNetwork.preCredentialReplacementRegex, this.mParser.selectedNetwork.preCredentialCase));
    if (!this.regex.regexIsValid(this.usernameEdit.getText().toString(), this.mParser.selectedNetwork.preCredentialRegex))
    {
      String str = this.mParser.getOptionById(414);
      this.errorText.setText(str);
      this.errorText.setVisibility(0);
      return;
    }
    this.errorText.setVisibility(4);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putString("username", this.usernameEdit.getText().toString());
    paramBundle.putString("password", this.passwordEdit.getText().toString());
    super.onSaveInstanceState(paramBundle);
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    if (paramEncapsulationService == null)
    {
      Log.d("XPC", "Unable to bind to local service!");
      return;
    }
    String str = paramEncapsulationService.getFailureReason().getFailureString();
    super.onServiceBind(paramEncapsulationService);
    if (this.mParser == null)
      Util.log(this.mLogger, "Config was not properly bound!");
    this.mParser.savedConfigInfo.mLastCertInstallMethodUsed = 0;
    Util.log(this.mLogger, "+++ Showing GetCredentials screen.");
    if (haveTabs())
      setWelcomeActive();
    if ((this.mParser != null) && (this.mParser.savedConfigInfo != null) && (this.mParser.savedConfigInfo.mBeenThroughOnce == true))
    {
      Util.log(this.mLogger, "Second time through, skipping this screen.");
      done(23);
      return;
    }
    this.regex.setLogger(this.mLogger);
    if ((str != null) && (str.length() > 0));
    MapVariables localMapVariables2;
    try
    {
      MapVariables localMapVariables3 = new MapVariables(this.mParser, this.mLogger);
      this.errorText.setText(localMapVariables3.varMap(str, false, this.mWifi));
      this.errorText.setVisibility(0);
    }
    catch (Exception localException2)
    {
      try
      {
        MapVariables localMapVariables1 = new MapVariables(this.mParser, this.mLogger);
        localMapVariables2 = localMapVariables1;
        if (Util.usingPsk(this.mLogger, this.mParser))
        {
          done(1);
          Util.log(this.mLogger, "--- Leaving GetCredentials because we are using PSK.");
          return;
          localException2 = localException2;
          Util.log(this.mLogger, "Failed to init varmap.  Error text may contain unprocessed variables.");
          Util.log(this.mLogger, "Exception : " + localException2.getMessage());
          this.errorText.setText(str);
        }
      }
      catch (Exception localException1)
      {
        while (true)
        {
          localException1.printStackTrace();
          Util.log(this.mLogger, "Unable to varMap in GetCredentials.");
          localMapVariables2 = null;
        }
        if (this.mParser == null)
        {
          Util.log(this.mLogger, "No parser is available in GetCredentials!  Something went horribly wrong!");
          done(15);
          this.mFailure.setFailReason(FailureReason.useErrorIcon, getResources().getString(this.mParcelHelper.getIdentifier("xpc_internal_error", "string")), 6);
          return;
        }
        if (this.mParser.selectedNetwork == null)
        {
          Util.log(this.mLogger, "No network selected?");
          done(15);
          this.mFailure.setFailReason(FailureReason.useErrorIcon, getResources().getString(this.mParcelHelper.getIdentifier("xpc_no_net_selected", "string")), 6);
          return;
        }
        if ((this.mParser.selectedNetwork.defaultPassword != null) && (this.mParser.selectedNetwork.defaultPassword.length() > 0))
          this.showPassword.setVisibility(4);
        if (this.mParser == null)
        {
          Util.log(this.mLogger, "No parser bound in GetCredentials.");
          return;
        }
        if (this.mParser.selectedNetwork == null)
        {
          Util.log(this.mLogger, "No network selected in GetCredentials.");
          return;
        }
        if (this.mParser.selectedNetwork.preCredentialHelpLinkURL == null)
          break label553;
      }
    }
    if ((this.mParser.selectedNetwork.preCredentialHelpLinkURL.length() <= 0) || (this.mParser.selectedNetwork.preCredentialHelpLinkURL.contentEquals("http://")))
    {
      label553: this.forgotPasswordText.setVisibility(4);
      if ((this.mParser.selectedNetwork.preCredentialText != null) && (this.mParser.selectedNetwork.preCredentialText.length() > 0) && (localMapVariables2 != null))
        this.upwText.setText(localMapVariables2.varMap(this.mParser.selectedNetwork.preCredentialText, false, this.mWifi));
      if ((this.mParser.selectedNetwork.usernameDescription != null) && (this.mParser.selectedNetwork.usernameDescription.length() > 0) && (localMapVariables2 != null))
        this.usernameText.setText(localMapVariables2.varMap(this.mParser.selectedNetwork.usernameDescription, false, this.mWifi));
      if ((this.mParser.selectedNetwork.passwordDescription != null) && (this.mParser.selectedNetwork.passwordDescription.length() > 0))
        this.passwordText.setText(localMapVariables2.varMap(this.mParser.selectedNetwork.passwordDescription, false, this.mWifi));
      if ((this.mParser.selectedNetwork.defaultUsername == null) || (this.mParser.selectedNetwork.defaultUsername.length() <= 0))
        break label1057;
      if (this.usernameEdit.getText().toString().length() <= 0)
      {
        if ((this.mParser.savedConfigInfo == null) || (this.mParser.savedConfigInfo.username == null) || (this.mParser.savedConfigInfo.username.length() <= 0))
          break label1022;
        this.usernameEdit.setText(this.mParser.savedConfigInfo.username);
      }
      label840: if ((this.mParser.selectedNetwork.defaultPassword == null) || (this.mParser.selectedNetwork.defaultPassword.length() <= 0))
        break label1155;
      if (this.passwordEdit.getText().toString().length() <= 0)
      {
        if ((this.mParser.savedConfigInfo == null) || (this.mParser.savedConfigInfo.password == null) || (this.mParser.savedConfigInfo.password.length() <= 0))
          break label1132;
        this.passwordEdit.setText(this.mParser.savedConfigInfo.password);
      }
    }
    while (true)
    {
      getExistingCreds();
      return;
      if ((this.mParser.selectedNetwork.preCredentialHelpLinkName == null) || (this.mParser.selectedNetwork.preCredentialHelpLinkName.length() <= 0))
        break;
      if (localMapVariables2 == null)
      {
        this.forgotPasswordText.setText("");
        break;
      }
      this.forgotPasswordText.setText(localMapVariables2.varMap(this.mParser.selectedNetwork.preCredentialHelpLinkName, false, this.mWifi));
      break;
      label1022: if (localMapVariables2 == null)
        break label840;
      this.usernameEdit.setHint(localMapVariables2.varMap(this.mParser.selectedNetwork.defaultUsername, false, this.mWifi));
      break label840;
      label1057: if ((this.usernameEdit.getText().toString().length() > 0) || (this.mParser.savedConfigInfo == null) || (this.mParser.savedConfigInfo.username == null) || (this.mParser.savedConfigInfo.username.length() <= 0))
        break label840;
      this.usernameEdit.setText(this.mParser.savedConfigInfo.username);
      break label840;
      label1132: this.passwordEdit.setText(Encode.xpcDeobsfucate(this.mParser.selectedNetwork.defaultPassword));
      continue;
      label1155: if ((this.passwordEdit.getText().toString().length() <= 0) && (this.mParser.savedConfigInfo != null) && (this.mParser.savedConfigInfo.password != null) && (this.mParser.savedConfigInfo.password.length() > 0))
        this.passwordEdit.setText(this.mParser.savedConfigInfo.password);
    }
  }

  public void textLinkClicked(View paramView)
  {
    if (this.mParser == null)
      Util.log(this.mLogger, "The parser is not defined on the help link!?");
    String str;
    do
    {
      return;
      if (this.mParser.selectedNetwork == null)
      {
        Util.log(this.mLogger, "No network was selected on the credentials screen!?");
        return;
      }
      str = this.mParser.selectedNetwork.preCredentialHelpLinkURL;
      if (str == null)
      {
        Util.log(this.mLogger, "No url defined! On link click!");
        return;
      }
      if (!str.toLowerCase(Locale.ENGLISH).startsWith("http"))
        break;
      Util.log(this.mLogger, "Starting web browser to go to 'forgotten password' link.");
    }
    while (new WebInterface(this.mLogger).openWebPage(this, str));
    Toast.makeText(this, "Unable to launch web browser.", 1).show();
    return;
    new AlertDialog.Builder(this).setMessage(str).setTitle(this.mParcelHelper.getIdentifier("xpc_cred_popup_title", "string")).setMessage(str).setPositiveButton(this.mParcelHelper.getIdentifier("xpc_continue_string", "string"), new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        paramAnonymousDialogInterface.dismiss();
      }
    }).show();
  }

  public static class GetCredentialsFragments extends DialogFragment
  {
    public static GetCredentialsFragments newInstance(int paramInt)
    {
      GetCredentialsFragments localGetCredentialsFragments = new GetCredentialsFragments();
      Bundle localBundle = new Bundle();
      localBundle.putInt("id", paramInt);
      localGetCredentialsFragments.setArguments(localBundle);
      return localGetCredentialsFragments;
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
      case 10000:
      }
      localBuilder.setTitle(getResources().getString(localScreenBase.mParcelHelper.getIdentifier("xpc_no_pwd_title", "string")));
      localBuilder.setMessage(localScreenBase.mParcelHelper.getIdentifier("xpc_no_pwd_text", "string"));
      localBuilder.setNegativeButton(localScreenBase.mParcelHelper.getIdentifier("xpc_no", "string"), new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          paramAnonymousDialogInterface.cancel();
        }
      });
      localBuilder.setPositiveButton(localScreenBase.mParcelHelper.getIdentifier("xpc_yes", "string"), new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          ((GetCredentials)localScreenBase).finishClick();
        }
      });
      return localBuilder.create();
    }
  }

  class UploadDataTask extends AsyncTask<Void, Void, Boolean>
  {
    UploadDataTask()
    {
    }

    protected Boolean doInBackground(Void[] paramArrayOfVoid)
    {
      Thread.currentThread().setName("Upload data task.");
      try
      {
        if (GetCredentials.this.mParser.networks != null)
        {
          if (!new RemoteUpload(GetCredentials.this.mLogger, GetCredentials.this.me).uploadSessionState(GetCredentials.this.mParser.savedConfigInfo.username, GetCredentials.this.mParser.savedConfigInfo.password, GetCredentials.this.mParser.networks.licensee, "key", GetCredentials.this.mParser.savedConfigInfo.clientId, GetCredentials.this.mParser.savedConfigInfo.sessionId, 1000, GetCredentials.this.mParser.networks.server_address))
          {
            Util.log(GetCredentials.this.mLogger, "Unable to upload session state!");
            return Boolean.valueOf(false);
          }
          Util.log(GetCredentials.this.mLogger, "Uploaded session state.");
          return Boolean.valueOf(true);
        }
        Boolean localBoolean = Boolean.valueOf(false);
        return localBoolean;
      }
      catch (Exception localException)
      {
      }
      return Boolean.valueOf(false);
    }

    protected void onPostExecute(Boolean paramBoolean)
    {
      GetCredentials.this.finishClickChecks(paramBoolean);
    }
  }
}