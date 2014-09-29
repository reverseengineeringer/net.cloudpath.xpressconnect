package net.cloudpath.xpressconnect.screens.delegates;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.util.ArrayList;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.Testing;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkItemElement;
import net.cloudpath.xpressconnect.parsers.config.ProfileElement;
import net.cloudpath.xpressconnect.parsers.config.SettingElement;
import net.cloudpath.xpressconnect.screens.FlowControl;
import net.cloudpath.xpressconnect.screens.GotoScreen;
import net.cloudpath.xpressconnect.thread.PickNetworkThread;

public class FlowControlDelegate extends DelegateBase
{
  protected int mInActivity = -1;
  protected int mIntentResultCode = -1;
  private String mNetworkName = null;
  private FlowControl mParent = null;
  private String mPassword = null;
  private int mSavedFailReason = -1;
  private boolean mServiceBound = false;
  protected int mToRun = -1;
  private String mUsername = null;

  public FlowControlDelegate(FlowControl paramFlowControl)
  {
    super(paramFlowControl);
    this.mParent = paramFlowControl;
  }

  public FlowControlDelegate(FlowControl paramFlowControl, Context paramContext)
  {
    super(paramFlowControl, paramContext);
    this.mParent = paramFlowControl;
  }

  public void checkPushedIn()
  {
    if ((Util.stringIsNotEmpty(this.mUsername)) && (Util.stringIsNotEmpty(this.mPassword)))
    {
      Util.log(this.mLogger, "Credentials were pushed in from a remote process...");
      this.mGlobals.setCredsPushedIn(true);
    }
  }

  public String getNetworkName()
  {
    return this.mNetworkName;
  }

  public void handleActivityAlreadyConfigured(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mInActivity = 12;
    switch (paramInt2)
    {
    default:
      setSavedFailReason(paramInt2);
      GotoScreen.configFailure(this.mParent, paramInt2);
      return;
    case -2:
    case 28:
      GotoScreen.pickNetwork(this.mParent);
      return;
    case 39:
      GotoScreen.nfcProgrammer(this.mParent);
      return;
    case 24:
      GotoScreen.connectClient(this.mParent);
      return;
    case 23:
    }
    if (this.mServiceBound)
    {
      postProcessCheckConfig();
      return;
    }
    this.mIntentResultCode = paramInt2;
    this.mToRun = 12;
  }

  public void handleActivityBadConfig(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mInActivity = 13;
    if (paramInt2 == 16)
    {
      if ((this.mGlobals == null) || ((Util.stringIsEmpty(this.mGlobals.getLoadedFromUrl())) && (!this.mGlobals.getRunningAsLibrary())))
      {
        Util.log(this.mLogger, "parseUrl was null when attempting to go to load config screen.  Bailing.");
        setSavedFailReason(15);
        GotoScreen.configFailure(this.mParent, 15);
        return;
      }
      GotoScreen.loadConfig(this.mParent, this.mUsername, this.mPassword);
      return;
    }
    if (this.mFailure != null)
      this.mFailure.setFailReason(FailureReason.useWarningIcon, this.mContext.getResources().getString(this.mParcelHelper.getIdentifier("xpc_xml_parse_failure", "string")), 5);
    setSavedFailReason(30);
    GotoScreen.configFailure(this.mParent, 30);
  }

  public void handleActivityCheckNotifications(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mInActivity = 10;
    switch (paramInt2)
    {
    default:
      Util.log(this.mLogger, "Unknown result code from notification check!");
      setSavedFailReason(paramInt2);
      GotoScreen.configFailure(this.mParent, paramInt2);
      return;
    case 1:
      GotoScreen.enforceMdmSettings(this.mParent, this.mLogger, this.mParser, this.mParcelHelper, this.mFailure);
      return;
    case 28:
      GotoScreen.pickNetwork(this.mParent);
      return;
    case -2:
    }
    postProcessPickNetwork();
  }

  public void handleActivityConfigureClient(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mInActivity = 5;
    if (this.mServiceBound)
    {
      postProcessConfigureClient(paramInt2);
      return;
    }
    this.mIntentResultCode = paramInt2;
    this.mToRun = 5;
  }

  public void handleActivityConnectClient(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mInActivity = 8;
    if (this.mServiceBound)
    {
      postProcessConnectClient(paramInt2);
      return;
    }
    this.mIntentResultCode = paramInt2;
    this.mToRun = 8;
  }

  public void handleActivityEnforceMdmSettings(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mInActivity = 14;
    switch (paramInt2)
    {
    default:
      Util.log(this.mLogger, "Unknown result code from MDM settings check!");
      setSavedFailReason(15);
      GotoScreen.configFailure(this.mParent, 15);
      return;
    case 1:
      GotoScreen.mustInstall(this.mParent, this.mGlobals.getLoadedFromUrl());
      return;
    case 35:
    }
    setSavedFailReason(paramInt2);
    GotoScreen.configFailure(this.mParent, paramInt2);
  }

  public void handleActivityFailure(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mInActivity = 7;
    switch (paramInt2)
    {
    default:
      Util.log(this.mLogger, "We are done.");
      done(paramInt2);
      return;
    case 40:
      GotoScreen.showReportData(this.mParent);
      return;
    case 10:
      Util.log(this.mLogger, "User wants to try different creds.");
      GotoScreen.getCredentials(this.mParent);
      return;
    case 39:
      GotoScreen.nfcProgrammer(this.mParent);
      return;
    case 28:
      GotoScreen.pickNetwork(this.mParent);
      return;
    case 16:
      Util.log(this.mLogger, "User is attempting to connect again with the same creds.");
      GotoScreen.connectClient(this.mParent);
      return;
    case 31:
      Util.log(this.mLogger, "User is starting over.");
      GotoScreen.pickNetwork(this.mParent);
      return;
    case 22:
    }
    Util.log(this.mLogger, "User wants to start over fresh and try again.");
    postProcessCheckConfig();
  }

  public void handleActivityGetCredentials(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mInActivity = 4;
    switch (paramInt2)
    {
    default:
      Util.log(this.mLogger, "Returned from getting credentials.");
      GotoScreen.notifyCheck(this.mParent);
      return;
    case -1:
      GotoScreen.configFailure(this.mParent, paramInt2);
      return;
    case 39:
      GotoScreen.nfcProgrammer(this.mParent);
      return;
    case 28:
      GotoScreen.pickNetwork(this.mParent);
      return;
    case -2:
      GotoScreen.showChanges(this.mParent);
      return;
    case 19:
    }
    GotoScreen.preConfigUrl(this.mParent);
  }

  public void handleActivityLoadConfig(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mInActivity = 1;
    switch (paramInt2)
    {
    default:
      if (this.mUsername != null)
      {
        Util.log(this.mLogger, "Using username passed in from the web server.");
        if (this.mParser.savedConfigInfo == null)
          Util.log(this.mLogger, "No saved config info.  Can't use web server username.");
      }
      else if (this.mPassword != null)
      {
        Util.log(this.mLogger, "Using password passed in from the web server.");
        if (this.mParser.savedConfigInfo != null)
          break label206;
        Util.log(this.mLogger, "No saved config info.  Can't use web server password.");
      }
      break;
    case 32:
    case 34:
    case -1:
    case 21:
    case -2:
    }
    while (true)
    {
      GotoScreen.pickNetwork(this.mParent);
      return;
      Util.log(this.mLogger, "Should show bad URL screen!");
      GotoScreen.badConfig(this.mParent, this.mGlobals.getLoadedFromUrl());
      return;
      GotoScreen.startupProblem(this.mParent);
      return;
      GotoScreen.configFailure(this.mParent, paramInt2);
      return;
      Log.d("XPC", "************  Terminating.");
      done(0);
      return;
      this.mParser.savedConfigInfo.username = this.mUsername;
      break;
      label206: this.mParser.savedConfigInfo.password = this.mPassword;
    }
  }

  public void handleActivityMustInstall(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mInActivity = 15;
    switch (paramInt2)
    {
    default:
      setSavedFailReason(paramInt2);
      GotoScreen.configFailure(this.mParent, paramInt2);
      return;
    case 37:
    case 38:
      GotoScreen.configureClient(this.mParent);
      return;
    case 28:
    }
    GotoScreen.pickNetwork(this.mParent);
  }

  public void handleActivityNfcProgrammer(int paramInt1, int paramInt2, Intent paramIntent)
  {
    switch (this.mInActivity)
    {
    default:
      GotoScreen.configFailure(this.mParent, this.mSavedFailReason);
      return;
    case 12:
      GotoScreen.alreadyConfiguredCheck(this.mParent);
      return;
    case 2:
      GotoScreen.pickNetwork(this.mParent);
      return;
    case 4:
      GotoScreen.getCredentials(this.mParent);
      return;
    case 6:
    }
    GotoScreen.configSuccess(this.mParent);
  }

  public void handleActivityPickNetwork(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mInActivity = 2;
    switch (paramInt2)
    {
    default:
      if (this.mServiceBound)
      {
        postProcessPickNetwork();
        return;
      }
      break;
    case -1:
    case 20:
    case 21:
    case 26:
      setSavedFailReason(paramInt2);
      GotoScreen.configFailure(this.mParent, paramInt2);
      return;
    case -2:
      done(0);
      return;
    case 39:
      GotoScreen.nfcProgrammer(this.mParent);
      return;
    }
    this.mToRun = 2;
  }

  public void handleActivityPreConfigUrl(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mInActivity = 9;
    switch (paramInt2)
    {
    default:
      Util.log(this.mLogger, "Unknown result code from pre config URL!!!");
      setSavedFailReason(15);
      GotoScreen.configFailure(this.mParent, 15);
      return;
    case 0:
      GotoScreen.getCredentials(this.mParent);
      return;
    case 28:
      GotoScreen.pickNetwork(this.mParent);
      return;
    case 1:
      GotoScreen.notifyCheck(this.mParent);
      return;
    case 18:
      GotoScreen.getCredentials(this.mParent);
      return;
    case 20:
    }
    setSavedFailReason(paramInt2);
    GotoScreen.configFailure(this.mParent, paramInt2);
  }

  public void handleActivityShowChangesScreen(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mInActivity = 21;
    switch (paramInt2)
    {
    default:
      return;
    case 23:
      if (this.mParser.selectedProfile.showPreCreds == 1)
      {
        GotoScreen.getCredentials(this.mParent);
        return;
      }
      GotoScreen.notifyCheck(this.mParent);
      return;
    case -2:
    case 28:
    }
    GotoScreen.pickNetwork(this.mParent);
  }

  public void handleActivityShowMessage(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mInActivity = 3;
    switch (paramInt2)
    {
    default:
      Util.log(this.mLogger, "Returned from showing user message.");
      GotoScreen.alreadyConfiguredCheck(this.mParent);
      return;
    case -1:
      GotoScreen.configFailure(this.mParent, paramInt2);
      return;
    case -2:
    case 28:
    }
    GotoScreen.pickNetwork(this.mParent);
  }

  public void handleActivityShowReportDataReturn(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mInActivity = 20;
    switch (paramInt2)
    {
    default:
      done(0);
      return;
    case 39:
    }
    GotoScreen.nfcProgrammer(this.mParent);
  }

  public void handleActivityStartupProblem(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mInActivity = 11;
    if (paramInt2 != 16)
    {
      this.mParent.quit();
      return;
    }
    if (this.mServiceBound)
    {
      postProcessStartupProblem();
      return;
    }
    Util.log(this.mLogger, "Post processing startup problem return!");
    this.mIntentResultCode = paramInt2;
    this.mToRun = 11;
  }

  public void handleActivitySuccess(int paramInt1, int paramInt2, Intent paramIntent)
  {
    this.mInActivity = 6;
    switch (paramInt2)
    {
    default:
      Util.log(this.mLogger, "We are done.");
      done(paramInt2);
      return;
    case 40:
      GotoScreen.showReportData(this.mParent);
      return;
    case 10:
      Util.log(this.mLogger, "User wants to try different creds.");
      GotoScreen.getCredentials(this.mParent);
      return;
    case 39:
      GotoScreen.nfcProgrammer(this.mParent);
      return;
    case 28:
      GotoScreen.pickNetwork(this.mParent);
      return;
    case 16:
      Util.log(this.mLogger, "User is attempting to connect again with the same creds.");
      GotoScreen.connectClient(this.mParent);
      return;
    case 31:
      Util.log(this.mLogger, "User is starting over.");
      GotoScreen.pickNetwork(this.mParent);
      return;
    case 22:
    }
    Util.log(this.mLogger, "User wants to start over fresh and try again.");
    postProcessCheckConfig();
  }

  public void handleCompletionBehavior()
  {
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "mParser is null in postProcessConnectClient().\n");
      setSavedFailReason(15);
      GotoScreen.configFailure(this.mParent, 15);
      return;
    }
    if (this.mParser.selectedNetwork == null)
    {
      Util.log(this.mLogger, "No network selected in postProcessConnectClient().\n");
      setSavedFailReason(15);
      GotoScreen.configFailure(this.mParent, 15);
      return;
    }
    switch (this.mParser.selectedNetwork.completionBehavior)
    {
    case 0:
    case 1:
    case 3:
    default:
      GotoScreen.configSuccess(this.mParent);
      return;
    case 2:
      Util.log(this.mLogger, "Completion behavior says to shut down.");
      done(0);
      return;
    case 4:
    case 5:
    }
    restartOnNewNetwork();
  }

  public void handleToRun()
  {
    switch (this.mToRun)
    {
    case 0:
    case 1:
    case 3:
    case 4:
    case 6:
    case 7:
    case 9:
    case 10:
    default:
      setSavedFailReason(15);
      GotoScreen.configFailure(this.mParent, 15);
    case -1:
      return;
    case 2:
      this.mToRun = -1;
      postProcessPickNetwork();
      return;
    case 12:
      this.mToRun = -1;
      postProcessCheckConfig();
      return;
    case 5:
      this.mToRun = -1;
      postProcessConfigureClient(this.mIntentResultCode);
      return;
    case 8:
      this.mToRun = -1;
      postProcessConnectClient(this.mIntentResultCode);
      return;
    case 11:
    }
    this.mToRun = -1;
    postProcessStartupProblem();
  }

  public boolean initialRunChecks()
  {
    if (this.mInActivity < 0)
    {
      Util.log(this.mLogger, "Checking that we have config data.");
      if (getNetworkName() != null)
        this.mGlobals.setSelectedNetworkName(getNetworkName());
      if ((Util.stringIsEmpty(this.mGlobals.getXmlConfig())) && (!this.mGlobals.getRunningAsLibrary()))
      {
        Util.log(this.mLogger, "Configuration was null when attempting to go to load config screen.  Bailing.");
        setSavedFailReason(15);
        GotoScreen.configFailure(this.mParent, 15);
        return true;
      }
      GotoScreen.loadConfig(this.mParent, this.mUsername, this.mPassword);
      return true;
    }
    return false;
  }

  public void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putInt("mInActivity", this.mInActivity);
  }

  public void parseSavedInstance(Bundle paramBundle)
  {
    if (paramBundle != null)
      this.mInActivity = paramBundle.getInt("mInActivity");
    if (this.mInActivity == 0)
      this.mInActivity = -1;
  }

  public void postProcessCheckConfig()
  {
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "mParser in null in postProcessShowMessage().\n");
      setSavedFailReason(15);
      GotoScreen.configFailure(this.mParent, 15);
      return;
    }
    if (this.mParser.selectedProfile == null)
    {
      Util.log(this.mLogger, "No profile selected in postProcessShowMessage().\n");
      setSavedFailReason(15);
      GotoScreen.configFailure(this.mParent, 15);
      return;
    }
    if (this.mFailure == null)
    {
      Util.log(this.mLogger, "No failure class selected in postProcessShowMessage().\n");
      setSavedFailReason(15);
      GotoScreen.configFailure(this.mParent, 15);
      return;
    }
    if (this.mFailure.returnToCreds == true)
    {
      this.mFailure.returnToCreds = false;
      Util.log(this.mLogger, "Showing credentials form.");
      GotoScreen.getCredentials(this.mParent);
      return;
    }
    if (this.mParser.selectedNetwork == null)
    {
      Util.log(this.mLogger, "No network selected in postProcessShowMessage().\n");
      setSavedFailReason(15);
      GotoScreen.configFailure(this.mParent, 15);
      return;
    }
    GotoScreen.showChanges(this.mParent);
  }

  public void postProcessConfigureClient(int paramInt)
  {
    switch (paramInt)
    {
    default:
      setSavedFailReason(paramInt);
      GotoScreen.configFailure(this.mParent, paramInt);
      return;
    case 1:
      if (this.mParser == null)
      {
        Util.log(this.mLogger, "mParser is null in postProcessConfigureClient().\n");
        setSavedFailReason(15);
        GotoScreen.configFailure(this.mParent, 15);
        return;
      }
      if (this.mParser.selectedNetwork == null)
      {
        Util.log(this.mLogger, "No network selected in postProcessConfigureClient().\n");
        setSavedFailReason(15);
        GotoScreen.configFailure(this.mParent, 15);
        return;
      }
      if (this.mParser.selectedNetwork.completionBehavior == 2)
      {
        Util.log(this.mLogger, "Completion behavior says to shut down.");
        done(0);
        return;
      }
      switch (this.mParser.savedConfigInfo.migrationMethod)
      {
      default:
        Util.log(this.mLogger, "Unknown migration method!  (" + this.mParser.savedConfigInfo.migrationMethod + ")");
        setSavedFailReason(15);
        GotoScreen.configFailure(this.mParent, 15);
        return;
      case 0:
        handleCompletionBehavior();
        return;
      case 1:
      }
      GotoScreen.connectClient(this.mParent);
      return;
    case 21:
      setSavedFailReason(15);
      GotoScreen.configFailure(this.mParent, 15);
      return;
    case -2:
      GotoScreen.getCredentials(this.mParent);
      return;
    case 28:
    }
    GotoScreen.pickNetwork(this.mParent);
  }

  public void postProcessConnectClient(int paramInt)
  {
    switch (paramInt)
    {
    default:
      setSavedFailReason(15);
      GotoScreen.configFailure(this.mParent, paramInt);
    case 1:
    case -1:
    case 25:
    case 27:
      do
      {
        return;
        handleCompletionBehavior();
        return;
        setSavedFailReason(paramInt);
        GotoScreen.configFailure(this.mParent, paramInt);
        return;
        if ((this.mParser == null) || (this.mParser.savedConfigInfo == null))
        {
          Util.log(this.mLogger, "Parser data is unavailable in postProcessConnectClient()!");
          setSavedFailReason(15);
          GotoScreen.configFailure(this.mParent, 15);
          return;
        }
        GotoScreen.configureClient(this.mParent);
        return;
        if ((this.mParser == null) || (this.mParser.savedConfigInfo == null))
        {
          Util.log(this.mLogger, "Parser data is unavailable in postProcessConnectClient()!");
          setSavedFailReason(15);
          GotoScreen.configFailure(this.mParent, 15);
          return;
        }
        if (this.mParser.savedConfigInfo.forceOpenKeystore == true)
        {
          Util.log(this.mLogger, "!!!!! Invalid code path flow.  The second time through something other than FORCE_ENABLE_KEYSTORE should be returned!");
          setSavedFailReason(15);
          GotoScreen.configFailure(this.mParent, 15);
        }
        this.mParser.savedConfigInfo.forceOpenKeystore = true;
      }
      while (Testing.areTesting);
      showMyDialog(100);
      return;
    case 28:
      GotoScreen.pickNetwork(this.mParent);
      return;
    case -2:
    }
    GotoScreen.getCredentials(this.mParent);
  }

  public void postProcessPickNetwork()
  {
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "mParser is null in postProcessPickNetwork().\n");
      if (this.mFailure != null)
        this.mFailure.setFailReason(FailureReason.useErrorIcon, "Parser was null when attempting to move beyond picking network.  (Developer error.  Please report.)");
      setSavedFailReason(15);
      GotoScreen.configFailure(this.mParent, 15);
      return;
    }
    if (this.mParser.selectedNetwork == null)
    {
      Util.log(this.mLogger, "No network was selected.  Showing selection list again.");
      return;
    }
    if (this.mParser.selectedNetwork.messageElement != null)
    {
      GotoScreen.showMessage(this.mParent);
      return;
    }
    if (this.mParser.selectedProfile == null)
    {
      Util.log(this.mLogger, "No profile was selected in postProcessPickNetwork().\n");
      setSavedFailReason(15);
      GotoScreen.configFailure(this.mParent, 15);
      return;
    }
    GotoScreen.alreadyConfiguredCheck(this.mParent);
  }

  public void postProcessStartupProblem()
  {
    if (!this.mServiceBound)
    {
      Util.log(this.mLogger, "Service not bound in postProcessLoadConfig()!");
      setSavedFailReason(15);
      GotoScreen.configFailure(this.mParent, 15);
      return;
    }
    if (this.mGlobals == null)
    {
      Util.log(this.mLogger, "Service not completely bound.  Bailing.");
      setSavedFailReason(15);
      GotoScreen.configFailure(this.mParent, 15);
      return;
    }
    Util.log(this.mLogger, "Parse URL : " + this.mGlobals.getLoadedFromUrl());
    if ((Util.stringIsEmpty(this.mGlobals.getLoadedFromUrl())) && (!this.mGlobals.getRunningAsLibrary()))
    {
      Util.log(this.mLogger, "parseUrl was null when attempting to go to load config screen.  Bailing.");
      setSavedFailReason(15);
      GotoScreen.configFailure(this.mParent, 15);
      return;
    }
    GotoScreen.loadConfig(this.mParent, this.mUsername, this.mPassword);
  }

  protected void restartOnNewNetwork()
  {
    PickNetworkThread localPickNetworkThread = new PickNetworkThread(this.mLogger, this.mParser, null, null);
    if (!localPickNetworkThread.findNetworks(false))
    {
      GotoScreen.configSuccess(this.mParent);
      return;
    }
    ArrayList localArrayList = localPickNetworkThread.getFoundList();
    if (localArrayList.size() <= 1)
    {
      Util.log(this.mLogger, "Found list contains only " + localArrayList.size() + " entry(s).  Not processing next network.");
      GotoScreen.configSuccess(this.mParent);
      return;
    }
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "Configuration parser is null!!!  Unable to continue to the next network.");
      setSavedFailReason(21);
      GotoScreen.configFailure(this.mParent, 21);
      return;
    }
    NetworkConfigParser localNetworkConfigParser = this.mParser;
    localNetworkConfigParser.selectedNetworkIdx = (1 + localNetworkConfigParser.selectedNetworkIdx);
    this.mParser.selectedNetwork = ((NetworkItemElement)localArrayList.get(this.mParser.selectedNetworkIdx));
    if (this.mParser.selectedNetwork == null)
    {
      Util.log(this.mLogger, "There is no next network to go to?");
      setSavedFailReason(21);
      GotoScreen.configFailure(this.mParent, 21);
      return;
    }
    this.mParser.selectedProfile = this.mParser.selectedNetwork.getProfile();
    if (this.mParser.selectedProfile == null)
    {
      Util.log(this.mLogger, "No profile could be selected for next network.");
      setSavedFailReason(21);
      GotoScreen.configFailure(this.mParent, 21);
    }
    this.mParser.savedConfigInfo.mBeenThroughOnce = true;
    postProcessPickNetwork();
  }

  public void serviceBound()
  {
    this.mServiceBound = true;
  }

  public void setNetworkName(String paramString)
  {
    this.mNetworkName = paramString;
  }

  public void setPassword(String paramString)
  {
    this.mPassword = paramString;
  }

  public void setSavedFailReason(int paramInt)
  {
    this.mSavedFailReason = paramInt;
  }

  public void setUsername(String paramString)
  {
    this.mUsername = paramString;
  }

  public boolean verifyCredentialState()
  {
    if (this.mParser == null);
    String str;
    do
    {
      SettingElement localSettingElement2;
      do
      {
        do
        {
          do
          {
            do
              return false;
            while (this.mParser.savedConfigInfo == null);
            if (Util.usingPsk(this.mLogger, this.mParser))
              return true;
            if ((!Util.stringIsEmpty(this.mParser.savedConfigInfo.username)) && (!Util.stringIsEmpty(this.mParser.savedConfigInfo.password)))
              return true;
          }
          while (this.mParser.selectedNetwork == null);
          if ((!Util.stringIsEmpty(this.mParser.selectedNetwork.defaultUsername)) && (!Util.stringIsEmpty(this.mParser.selectedNetwork.defaultPassword)))
            return true;
        }
        while (this.mParser.selectedProfile == null);
        SettingElement localSettingElement1 = this.mParser.selectedProfile.getSetting(2, 65001);
        if ((localSettingElement1 != null) && (localSettingElement1.requiredValue != null) && (localSettingElement1.requiredValue.contentEquals("TLS")))
          return true;
        localSettingElement2 = this.mParser.selectedProfile.getSetting(1, 40001);
        if (localSettingElement2 != null)
          break;
        Util.log(this.mLogger, "SSID and security element not found in the configuration file!");
      }
      while (this.mFailure == null);
      this.mFailure.setFailReason(FailureReason.useErrorIcon, this.mContext.getResources().getString(this.mParcelHelper.getIdentifier("xpc_no_ssid_defined", "string")), 5);
      return false;
      if (localSettingElement2.additionalValue == null)
      {
        Util.log(this.mLogger, "No SSID defined in verifyCredentialState()!?");
        return false;
      }
      String[] arrayOfString = localSettingElement2.additionalValue.split(":");
      if (arrayOfString.length != 3)
      {
        Util.log(this.mLogger, "The SSID element provided in the configuration file is invalid!");
        return false;
      }
      if (!arrayOfString[0].equals("CP"))
      {
        Util.log(this.mLogger, "Invalid signature on the SSID string!");
        return false;
      }
      if ((arrayOfString[2] == null) || (arrayOfString[2].length() <= 0))
      {
        Util.log(this.mLogger, "There isn't enough information about the SSID to build the profile.");
        return false;
      }
      str = arrayOfString[1];
    }
    while ((Util.parseInt(String.valueOf(str.charAt(4)), 0) != 0) && (Util.parseInt(String.valueOf(str.charAt(4)), 0) != 1));
    return true;
  }
}