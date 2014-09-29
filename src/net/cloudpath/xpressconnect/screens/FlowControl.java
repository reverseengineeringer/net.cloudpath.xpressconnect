package net.cloudpath.xpressconnect.screens;

import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkItemElement;
import net.cloudpath.xpressconnect.screens.delegates.FlowControlDelegate;

@TargetApi(11)
public class FlowControl extends ScreenBase
{
  public static final int SHOW_CREDENTIALS_WARNING = 20000;
  public static final String SHOW_CREDENTIALS_WARNING_TAG = "showCredentialsWarning";
  private static final int SHOW_LOG = 1;
  private FlowControlDelegate mDelegate = null;

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    Util.log(this.mLogger, "onActivityResult = " + paramInt1);
    Util.log(this.mLogger, "result code = " + paramInt2);
    rebind();
    this.mDelegate.setRequiredSettings(this.mLogger, this.mParser, this.mGlobals, this.mFailure);
    if (paramInt2 == 29)
    {
      quit();
      return;
    }
    switch (paramInt1)
    {
    case 17:
    case 18:
    case 19:
    default:
      super.onActivityResult(paramInt1, paramInt2, paramIntent);
      return;
    case 11:
      Util.log(this.mLogger, "Returned from startup problem.");
      this.mDelegate.handleActivityStartupProblem(paramInt1, paramInt2, paramIntent);
      return;
    case 1:
      Util.log(this.mLogger, "Returned from config load.");
      if (haveTabs())
        setWelcomeActive();
      this.mDelegate.handleActivityLoadConfig(paramInt1, paramInt2, paramIntent);
      return;
    case 13:
      Util.log(this.mLogger, "Returned from bad configuration screen.");
      this.mDelegate.handleActivityBadConfig(paramInt1, paramInt2, paramIntent);
      return;
    case 2:
      Util.log(this.mLogger, "Returned from picking network.");
      if ((this.backgroundLayout != null) && (this.mGlobals != null) && (this.mGlobals.getBackgroundColor() != -1))
        this.backgroundLayout.setBackgroundColor(-16777216 + this.mGlobals.getBackgroundColor());
      if (haveTabs())
        setWelcomeActive();
      this.mDelegate.handleActivityPickNetwork(paramInt1, paramInt2, paramIntent);
      return;
    case 16:
      this.mDelegate.handleActivityNfcProgrammer(paramInt1, paramInt2, paramIntent);
      return;
    case 3:
      if (haveTabs())
        setWelcomeActive();
      this.mDelegate.handleActivityShowMessage(paramInt1, paramInt2, paramIntent);
      return;
    case 21:
      this.mDelegate.handleActivityShowChangesScreen(paramInt1, paramInt2, paramIntent);
      return;
    case 12:
      this.mDelegate.handleActivityAlreadyConfigured(paramInt1, paramInt2, paramIntent);
      return;
    case 4:
      if (haveTabs())
        setConfigureActive();
      this.mDelegate.handleActivityGetCredentials(paramInt1, paramInt2, paramIntent);
      return;
    case 9:
      Util.log(this.mLogger, "Returned from pre config URL check.");
      if (haveTabs())
        setConfigureActive();
      this.mDelegate.handleActivityPreConfigUrl(paramInt1, paramInt2, paramIntent);
      return;
    case 10:
      Util.log(this.mLogger, "Notifications have been checked.");
      if (haveTabs())
        setConfigureActive();
      this.mDelegate.handleActivityCheckNotifications(paramInt1, paramInt2, paramIntent);
      return;
    case 14:
      Util.log(this.mLogger, "MDM settings (if any) have been set.");
      if (haveTabs())
        setConfigureActive();
      this.mDelegate.handleActivityEnforceMdmSettings(paramInt1, paramInt2, paramIntent);
      return;
    case 15:
      Util.log(this.mLogger, "Any forced programs have been installed.");
      if (haveTabs())
        setConfigureActive();
      this.mDelegate.handleActivityMustInstall(paramInt1, paramInt2, paramIntent);
      return;
    case 5:
      Util.log(this.mLogger, "Client is configured.");
      if (haveTabs())
        setConnectActive();
      this.mDelegate.handleActivityConfigureClient(paramInt1, paramInt2, paramIntent);
      return;
    case 8:
      Util.log(this.mLogger, "Client is connected.");
      if (haveTabs())
        setConnectedActive();
      this.mDelegate.handleActivityConnectClient(paramInt1, paramInt2, paramIntent);
      return;
    case 7:
      this.mDelegate.handleActivityFailure(paramInt1, paramInt2, paramIntent);
      return;
    case 6:
      this.mDelegate.handleActivitySuccess(paramInt1, paramInt2, paramIntent);
      return;
    case 20:
    }
    this.mDelegate.handleActivityShowReportDataReturn(paramInt1, paramInt2, paramIntent);
  }

  public void onCreate(Bundle paramBundle)
  {
    Log.d("XPC", "onCreate() called! (flowcontrol)");
    ParcelHelper localParcelHelper = new ParcelHelper("", this);
    preContentView();
    setContentView(localParcelHelper.getLayoutId("xpc_processing"));
    super.onCreate(paramBundle);
    if (this.licensee != null)
      this.licensee.setVisibility(4);
    if (this.licensedToText != null)
      this.licensedToText.setVisibility(4);
    Log.d("XPC", "UI thread = " + Thread.currentThread().getId());
    this.mDelegate = new FlowControlDelegate(this);
    this.mDelegate.parseSavedInstance(paramBundle);
    this.mDelegate.setNetworkName(getIntent().getStringExtra("name"));
    this.mDelegate.setUsername(getIntent().getStringExtra("username"));
    this.mDelegate.setPassword(getIntent().getStringExtra("password"));
    if (this.mDelegate.getNetworkName() != null)
      Log.d("XPC", "Network name in FlowControl = " + this.mDelegate.getNetworkName());
    Log.d("XPC", "onCreate(flowcontrol) exits.");
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    if (paramMenu == null)
      return super.onCreateOptionsMenu(paramMenu);
    paramMenu.clear();
    paramMenu.add(0, 1, 0, this.mParcelHelper.getIdentifier("xpc_show_log_title", "string"));
    return super.onCreateOptionsMenu(paramMenu);
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    if (paramMenuItem == null)
      return super.onOptionsItemSelected(paramMenuItem);
    switch (paramMenuItem.getItemId())
    {
    default:
      Util.log(this.mLogger, "Unknown menu item selected!");
    case 1:
    }
    while (true)
    {
      return super.onOptionsItemSelected(paramMenuItem);
      GotoScreen.showLogs(this);
    }
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    this.mDelegate.onSaveInstanceState(paramBundle);
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    if (paramEncapsulationService == null)
    {
      Log.d("XPC", "No service information available in FlowControl!  Bailing.\n");
      this.mDelegate.setSavedFailReason(15);
      GotoScreen.configFailure(this, 15);
    }
    do
    {
      return;
      Log.d("XPC", "Service bound.");
      super.onServiceBind(paramEncapsulationService);
      this.mDelegate.serviceBound();
      this.mDelegate.setRequiredSettings(this.mLogger, this.mParser, this.mGlobals, this.mFailure);
      this.mDelegate.checkPushedIn();
      if ((Util.stringIsEmpty(this.mGlobals.getXmlConfig())) && (!this.mGlobals.getRunningAsLibrary()))
      {
        GotoScreen.badConfig(this, this.mGlobals.getLoadedFromUrl());
        return;
      }
    }
    while (this.mDelegate.initialRunChecks());
    this.mDelegate.handleToRun();
  }

  public static class FlowControlFragments extends DialogFragment
  {
    public static FlowControlFragments newInstance(int paramInt)
    {
      FlowControlFragments localFlowControlFragments = new FlowControlFragments();
      Bundle localBundle = new Bundle();
      localBundle.putInt("id", paramInt);
      localFlowControlFragments.setArguments(localBundle);
      return localFlowControlFragments;
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
      case 20000:
      }
      localBuilder.setMessage(localScreenBase.mParcelHelper.getIdentifier("xpc_bad_credential_config", "string"));
      localBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          if (localScreenBase.mParser.selectedNetwork == null)
          {
            Util.log(localScreenBase.mLogger, "No network selected in postProcessShowMessage().\n");
            ((FlowControl)localScreenBase).mDelegate.setSavedFailReason(15);
            GotoScreen.configFailure(localScreenBase, 15);
            return;
          }
          if ((localScreenBase.mParser.selectedNetwork.registrationUrl != null) && (localScreenBase.mParser.selectedNetwork.registrationUrl != null))
          {
            Util.log(localScreenBase.mLogger, "Going to pre config URL screen.");
            GotoScreen.preConfigUrl(localScreenBase);
            return;
          }
          Util.log(localScreenBase.mLogger, "Skipping credentials and going to configuration.");
          GotoScreen.notifyCheck(localScreenBase);
        }
      });
      localBuilder.setOnCancelListener(new DialogInterface.OnCancelListener()
      {
        public void onCancel(DialogInterface paramAnonymousDialogInterface)
        {
          Util.log(localScreenBase.mLogger, "User hit the back button.");
          if (localScreenBase.mParser.selectedNetwork == null)
          {
            Util.log(localScreenBase.mLogger, "No network selected in postProcessShowMessage().\n");
            ((FlowControl)localScreenBase).mDelegate.setSavedFailReason(15);
            GotoScreen.configFailure(localScreenBase, 15);
            return;
          }
          if ((localScreenBase.mParser.selectedNetwork.registrationUrl != null) && (localScreenBase.mParser.selectedNetwork.registrationUrl != null))
          {
            Util.log(localScreenBase.mLogger, "Going to pre config URL screen.");
            GotoScreen.preConfigUrl(localScreenBase);
            return;
          }
          Util.log(localScreenBase.mLogger, "Skipping credentials and going to configuration.");
          GotoScreen.notifyCheck(localScreenBase);
        }
      });
      return localBuilder.create();
    }
  }
}