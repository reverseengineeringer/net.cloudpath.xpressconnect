package net.cloudpath.xpressconnect.screens;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.cloudpath.common.pojo.Content;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.util.ArrayList;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.GestureCallback;
import net.cloudpath.xpressconnect.LocalDbHelper;
import net.cloudpath.xpressconnect.MultiLineList;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.ConditionElement;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkElement;
import net.cloudpath.xpressconnect.parsers.config.NetworkItemElement;
import net.cloudpath.xpressconnect.remote.RemoteUpload;
import net.cloudpath.xpressconnect.retention.PickNetworkRetention;
import net.cloudpath.xpressconnect.thread.PickNetworkThread;
import net.cloudpath.xpressconnect.thread.ThreadCom;
import net.cloudpath.xpressconnect.thread.ThreadComCallbacks;

public class PickNetwork extends ScreenBase
  implements ThreadComCallbacks, AdapterView.OnItemClickListener, DialogInterface.OnClickListener, GestureCallback
{
  private static final int NETWORK_SELECTION = 1100;
  private static final String NETWORK_SELECTION_TAG = "networkSelection";
  private PickNetworkThread activeThread = null;
  private boolean checksumDone = false;
  private ArrayList<NetworkItemElement> foundList = null;
  private MultiLineList mList = null;
  private ThreadCom mThreadCom = null;
  private ListView pickList = null;
  private AlertDialog promptUser = null;

  private boolean checkUploadInitialData()
  {
    if ((this.mParser == null) || (this.mParser.selectedNetwork == null))
    {
      Util.log(this.mLogger, "Configuration is invalid in checkUploadInitialData()!");
      return false;
    }
    if ((0x2 & this.mParser.selectedNetwork.remoteUpload) == 2)
    {
      new UploadDataTask().execute(new Void[] { null, null });
      return true;
    }
    return false;
  }

  private void finishActivity(boolean paramBoolean)
  {
    removeMyDialog("networkSelection");
    Util.log(this.mLogger, "--- Leaving PickNetwork because an auto network configuration was found.");
    done(1);
  }

  private void noNetsFound()
  {
    Util.log(this.mLogger, "No networks are available for this device.");
    NetworkConfigParser localNetworkConfigParser = this.mParser;
    String str1 = null;
    String str2;
    if (localNetworkConfigParser != null)
    {
      str2 = this.mParser.getOptionById(400);
      if (new ConditionElement(this.mLogger).isKnownVersion())
        str1 = str2 + "\n\n\nPlease note : This version of the Android OS is supported by this app.  Your system administrator has chosen not to enable it yet.";
    }
    else
    {
      if ((this.mParser != null) && (str1 != null))
        break label147;
      this.mFailure.setFailReason(FailureReason.useWarningIcon, getApplicationContext().getString(this.mParcelHelper.getIdentifier("xpc_no_nets_desc", "string")), 5);
    }
    while (true)
    {
      Util.log(this.mLogger, "--- Leaving PickNetwork because no networks were available.");
      done(26);
      return;
      str1 = str2 + "\n\n\nPlease note : This version of the Android OS is not yet supported by this app.   Please be sure you are running the latest available version.";
      break;
      label147: this.mFailure.setFailReason(FailureReason.useWarningIcon, str1, 5);
    }
  }

  private void populateList()
  {
    this.mList = new MultiLineList(this);
    if ((this.foundList == null) || (this.foundList.size() <= 0))
    {
      noNetsFound();
      return;
    }
    int i = 0;
    if (i < this.foundList.size())
    {
      if (this.foundList.get(i) == null);
      while (true)
      {
        i++;
        break;
        if (!((NetworkItemElement)this.foundList.get(i)).isHidden.booleanValue())
          this.mList.addItem(((NetworkItemElement)this.foundList.get(i)).name, ((NetworkItemElement)this.foundList.get(i)).description);
      }
    }
    this.pickList.setAdapter(this.mList.getData());
    this.pickList.setOnItemClickListener(this);
  }

  private void process()
  {
    if (this.activeThread != null)
      return;
    this.activeThread = new PickNetworkThread(this.mLogger, this.mParser, this.mThreadCom, this.mWifi);
    this.activeThread.start();
  }

  private void showUserWaitPrompt()
  {
    if (this.mThreadCom == null)
      this.mThreadCom = new ThreadCom();
    if (this.mThreadCom != null)
    {
      this.mThreadCom.setCallbacks(this);
      showMyDialog(PickNetworkFragments.newInstance(1100), "networkSelection");
    }
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

  public void onBackPressed()
  {
    Util.log(this.mLogger, "--- Leaving PickNetwork because user pressed back.");
    if (this.activeThread != null)
    {
      this.activeThread.die();
      this.activeThread = null;
    }
    this.mGlobals.setSavedObject(null);
    done(-2);
    super.onBackPressed();
  }

  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    if (paramInt == -2)
    {
      Util.log(this.mLogger, "--- Leaving PickNetwork because checksum was invalid, and user cancelled.");
      quit();
      return;
    }
    this.checksumDone = true;
    showUserWaitPrompt();
    process();
  }

  protected void onCreate(Bundle paramBundle)
  {
    Log.d("XPC", "onCreate() (PickNetwork)");
    ParcelHelper localParcelHelper = new ParcelHelper("", this);
    preContentView();
    setContentView(localParcelHelper.getLayoutId("xpc_network_select"));
    super.onCreate(paramBundle);
    if (haveTabs())
      setWelcomeActive();
    this.pickList = ((ListView)findViewById(this.mParcelHelper.getItemId("xpc_NetworkPickList")));
    this.mShowStartOverMenu = false;
    setGestureCallback(this);
    Log.d("XPC", "onCreate() ends....");
  }

  protected void onDestroy()
  {
    Util.log(this.mLogger, "(PickNetwork) onDestroy()");
    if (this.promptUser != null)
    {
      this.promptUser.cancel();
      this.promptUser = null;
    }
    if (this.activeThread != null)
      this.activeThread.die();
    this.activeThread = null;
    this.mGlobals.setSavedObject(null);
    super.onDestroy();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    TextView localTextView = (TextView)paramView.findViewById(this.mParcelHelper.getItemId("xpc_LargeText"));
    Util.log(this.mLogger, "User selected : " + localTextView.getText());
    if (this.foundList == null)
    {
      Util.log(this.mLogger, "Managed to click on something that doesn't exist in onItemClick()!");
      return;
    }
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "Configuration is not bound in onItemClick()!");
      done(-1);
      return;
    }
    switch (this.activeThread.selectNetworkByName(localTextView.getText().toString()))
    {
    default:
      if (this.mParser.selectedNetwork == null)
      {
        Util.log(this.mLogger, "Unable to locate a network, even after the user clicked on it?!");
        done(-1);
        return;
      }
      break;
    case 1:
      this.mFailure.setFailReason(FailureReason.useWarningIcon, getApplicationContext().getString(this.mParcelHelper.getIdentifier("xpc_cant_locate_net_config", "string")), 5);
      done(26);
      return;
    case 2:
      this.mFailure.setFailReason(FailureReason.useWarningIcon, getApplicationContext().getString(this.mParcelHelper.getIdentifier("xpc_cant_locate_profile_config", "string")), 5);
      done(26);
      return;
    }
    checkUploadInitialData();
    if (this.mParser.selectedProfile == null)
    {
      Util.log(this.mLogger, "Unable to locate profile, event after the user clicked on a network!?");
      done(-1);
      return;
    }
    Util.log(this.mLogger, "--- Leaving PickNetwork because user picked a network to configure.");
    done(1);
  }

  protected void onPause()
  {
    super.onPause();
    Util.log(this.mLogger, "(PickNetwork) onPause().");
    if (this.activeThread != null)
      this.activeThread.spinLock();
    PickNetworkRetention localPickNetworkRetention = new PickNetworkRetention();
    localPickNetworkRetention.checksumDone = this.checksumDone;
    localPickNetworkRetention.foundList = this.foundList;
    localPickNetworkRetention.threadcom = this.mThreadCom;
    localPickNetworkRetention.thread = this.activeThread;
    Util.log(this.mLogger, "Retaining configuration instance.");
    this.mGlobals.setSavedObject(localPickNetworkRetention);
  }

  protected void onResume()
  {
    super.onResume();
    Util.log(this.mLogger, "(PickNetwork) onResume().");
    if (this.activeThread != null)
      this.activeThread.resume(this.mThreadCom);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    removeMyDialog("networkSelection");
    super.onSaveInstanceState(paramBundle);
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
    Util.log(this.mLogger, "+++ Showing PickNetwork.");
    try
    {
      localPickNetworkRetention = (PickNetworkRetention)this.mGlobals.getSavedObject();
      this.mGlobals.setSavedObject(null);
      if (localPickNetworkRetention != null)
      {
        this.foundList = localPickNetworkRetention.foundList;
        this.checksumDone = localPickNetworkRetention.checksumDone;
        this.mThreadCom = localPickNetworkRetention.threadcom;
        this.activeThread = localPickNetworkRetention.thread;
        if (this.activeThread != null)
          this.activeThread.resume(this.mThreadCom);
        if (this.mThreadCom != null)
          this.mThreadCom.setCallbacks(this);
      }
      if (haveTabs())
        setWelcomeActive();
      if (!this.checksumDone)
      {
        localContent = new Content();
        if ((this.mParser != null) && (this.mParser.networks != null))
        {
          localContent.setChecksum(this.mParser.networks.checksum);
          if (this.mGlobals.getXmlConfig() == null)
          {
            Util.log(this.mLogger, "--- Leaving PickNetwork because no data was available to pick the network from.");
            this.mFailure.setFailReason(FailureReason.useWarningIcon, getApplicationContext().getString(this.mParcelHelper.getIdentifier("xpc_no_config_available", "string")), 5);
            done(21);
            return;
          }
        }
      }
    }
    catch (ClassCastException localClassCastException)
    {
      Content localContent;
      while (true)
        PickNetworkRetention localPickNetworkRetention = null;
      int i = 9 + this.mGlobals.getXmlConfig().indexOf("<content>");
      int j = this.mGlobals.getXmlConfig().indexOf("</content>");
      if ((j < i) || (j - i <= 0))
      {
        this.promptUser = new AlertDialog.Builder(this).setPositiveButton(this.mParcelHelper.getIdentifier("xpc_yes", "string"), this).setNegativeButton(this.mParcelHelper.getIdentifier("xpc_no", "string"), this).create();
        this.promptUser.setCancelable(false);
        this.promptUser.setTitle(this.mParcelHelper.getIdentifier("xpc_invalid_checksum", "string"));
        this.promptUser.setMessage(getResources().getString(this.mParcelHelper.getIdentifier("xpc_allow_invalid_checksum", "string")));
        this.promptUser.show();
        return;
      }
      localContent.setXml(this.mGlobals.getXmlConfig().substring(i, j));
      if (!localContent.isValid())
      {
        Util.log(this.mLogger, "The checksum isn't valid for this network!");
        this.promptUser = new AlertDialog.Builder(this).setPositiveButton(this.mParcelHelper.getIdentifier("xpc_yes", "string"), this).setNegativeButton(this.mParcelHelper.getIdentifier("xpc_no", "string"), this).create();
        this.promptUser.setCancelable(false);
        this.promptUser.setTitle(this.mParcelHelper.getIdentifier("xpc_invalid_checksum", "string"));
        this.promptUser.setMessage(getResources().getString(this.mParcelHelper.getIdentifier("xpc_allow_invalid_checksum", "string")));
        this.promptUser.show();
        return;
        Util.log(this.mLogger, "Configuration wasn't properly bound in PickNetwork!");
        done(-1);
        return;
      }
      this.checksumDone = true;
      if (this.foundList == null)
      {
        showUserWaitPrompt();
        process();
        return;
      }
      populateList();
    }
  }

  public void threadChangeTab(int paramInt)
  {
  }

  public void threadFailed(Boolean paramBoolean, int paramInt, String paramString)
  {
    if (this.activeThread == null)
    {
      Util.log(this.mLogger, "activeThread is null in threadFailed()!");
      done(-1);
      return;
    }
    this.foundList = this.activeThread.getFoundList();
    if (paramString == null)
      populateList();
    while (true)
    {
      try
      {
        removeMyDialog("networkSelection");
        return;
      }
      catch (IllegalStateException localIllegalStateException)
      {
        return;
      }
      alertThenDie(this.mParcelHelper.getIdentifier("xpc_error_title", "string"), paramString, this.mParcelHelper.getIdentifier("xpc_ok_string", "string"));
    }
  }

  public void threadSuccess()
  {
    Util.log(this.mLogger, "Success...");
    if (!checkUploadInitialData())
      finishActivity(true);
  }

  public static class PickNetworkFragments extends DialogFragment
  {
    public static PickNetworkFragments newInstance(int paramInt)
    {
      PickNetworkFragments localPickNetworkFragments = new PickNetworkFragments();
      Bundle localBundle = new Bundle();
      localBundle.putInt("id", paramInt);
      localPickNetworkFragments.setArguments(localBundle);
      return localPickNetworkFragments;
    }

    public Dialog onCreateDialog(Bundle paramBundle)
    {
      int i = getArguments().getInt("id");
      ScreenBase localScreenBase = (ScreenBase)getActivity();
      switch (i)
      {
      default:
        return null;
      case 1100:
      }
      ProgressDialog localProgressDialog = new ProgressDialog(localScreenBase);
      localProgressDialog.setTitle(((PickNetwork)localScreenBase).mParcelHelper.getIdentifier("xpc_network_selection", "string"));
      localProgressDialog.setMessage(getResources().getString(((PickNetwork)localScreenBase).mParcelHelper.getIdentifier("xpc_auto_select_network", "string")));
      localProgressDialog.setCancelable(false);
      return localProgressDialog;
    }
  }

  class UploadDataTask extends AsyncTask<Void, Void, Boolean>
  {
    UploadDataTask()
    {
    }

    protected Boolean doInBackground(Void[] paramArrayOfVoid)
    {
      Thread.currentThread().setName("PickNetwork upload data task.");
      try
      {
        if (PickNetwork.this.mParser.networks != null)
        {
          Util.log(PickNetwork.this.mLogger, "Uploading configuration data...");
          if (!new RemoteUpload(PickNetwork.this.mLogger, PickNetwork.this.me).uploadInitialData(PickNetwork.this.mParser.savedConfigInfo.username, PickNetwork.this.mParser.savedConfigInfo.password, PickNetwork.this.mParser.networks.licensee, "key", PickNetwork.this.mParser.savedConfigInfo.clientId, PickNetwork.this.mParser.savedConfigInfo.sessionId, PickNetwork.this.mParser.networks.server_address, Util.getMac(PickNetwork.this.mLogger, PickNetwork.this.mWifi)))
          {
            Util.log(PickNetwork.this.mLogger, "Unable to upload initial session data!");
            return Boolean.valueOf(false);
          }
          Util.log(PickNetwork.this.mLogger, "Sent configuration data.");
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
      PickNetwork.this.finishActivity(paramBoolean.booleanValue());
    }
  }
}