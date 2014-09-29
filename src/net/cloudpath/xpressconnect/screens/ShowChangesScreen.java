package net.cloudpath.xpressconnect.screens;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.util.ArrayList;
import java.util.List;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkItemElement;
import net.cloudpath.xpressconnect.parsers.config.ProfileElement;
import net.cloudpath.xpressconnect.parsers.config.SettingElement;
import net.cloudpath.xpressconnect.parsers.config.SettingId;
import net.cloudpath.xpressconnect.thread.PickNetworkThread;

public class ShowChangesScreen extends ScreenBase
  implements View.OnClickListener
{
  private static final int PLEASE_WAIT = 1001;
  private static final String PLEASE_WAIT_TAG = "pleaseWait";
  private Button mCancelButton = null;
  private boolean mCasHandled = false;
  private WebView mChangesToMakeView = null;
  private String mConnectingSsid = "";
  private Button mContinueButton = null;
  private InitialGatherChanges mGathering = null;
  private boolean mHaveNextNetwork = false;
  private Button mMoreInfoButton = null;
  private boolean mShowingMoreInfo = false;

  private String buildCurrentNetworkHtml(NetworkItemElement paramNetworkItemElement, ProfileElement paramProfileElement)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    for (int i = 0; i < SettingId.allOptions.length; i++)
    {
      String str = getSettingText(paramNetworkItemElement, paramProfileElement, SettingId.allOptions[i]);
      if (str != null)
        localStringBuilder.append(str);
    }
    return localStringBuilder.toString();
  }

  private String getFormattedConfigs(List<String> paramList)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    for (int i = 0; i < paramList.size(); i++)
    {
      localStringBuilder.append("<b><i>Network configuration to be installed : </i></b><br/><br/><ul>");
      localStringBuilder.append((String)paramList.get(i));
      localStringBuilder.append("</ul><br/><br/><br/>");
    }
    return localStringBuilder.toString();
  }

  private String getNextNetworkChanges(int paramInt)
  {
    PickNetworkThread localPickNetworkThread = new PickNetworkThread(this.mLogger, this.mParser, null, null);
    if (!localPickNetworkThread.findNetworks(false))
    {
      Util.log(this.mLogger, "No next network found!?  Won't display any info.");
      return null;
    }
    ArrayList localArrayList = localPickNetworkThread.getFoundList();
    if (localArrayList.size() < 1)
    {
      Util.log(this.mLogger, "Found list contains only " + localArrayList.size() + " entry(s).  Not processing next network.");
      return null;
    }
    NetworkItemElement localNetworkItemElement = (NetworkItemElement)localArrayList.get(paramInt);
    if (localNetworkItemElement == null)
    {
      Util.log(this.mLogger, "There is no next network to go to?");
      return null;
    }
    ProfileElement localProfileElement = localNetworkItemElement.getProfile();
    if (localProfileElement == null)
    {
      Util.log(this.mLogger, "No profile could be selected for next network.");
      return null;
    }
    if ((localNetworkItemElement.completionBehavior == 4) || (localNetworkItemElement.completionBehavior == 5));
    for (this.mHaveNextNetwork = true; ; this.mHaveNextNetwork = false)
    {
      this.mConnectingSsid = Util.getSSID(this.mLogger, localProfileElement);
      return buildCurrentNetworkHtml(localNetworkItemElement, localProfileElement);
    }
  }

  private String getSettingText(NetworkItemElement paramNetworkItemElement, ProfileElement paramProfileElement, int paramInt)
  {
    if ((paramNetworkItemElement == null) || (paramProfileElement == null))
    {
      Util.log(this.mLogger, "Parser or profile is null in ShowChangesScreen::getSettingText()!");
      return null;
    }
    String str1 = null;
    switch (paramInt)
    {
    default:
    case 40001:
    case 61301:
    case 61306:
    case 80037:
    case 40074:
    }
    while (true)
    {
      return str1;
      SettingElement localSettingElement4 = paramProfileElement.getSetting(1, paramInt);
      str1 = null;
      if (localSettingElement4 != null)
      {
        StringBuilder localStringBuilder2 = new StringBuilder().append("<li>");
        String str8 = getResources().getString(this.mParcelHelper.getIdentifier("xpc_configuring_ssid", "string"));
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = Util.getSSID(this.mLogger, paramProfileElement);
        str1 = String.format(str8, arrayOfObject2) + "</li>";
      }
      if (this.mShowingMoreInfo)
      {
        String str7 = str1 + "<ul>";
        if (!Util.isOpenNetwork(this.mLogger, paramProfileElement))
          str7 = str7 + "<li>" + getResources().getString(this.mParcelHelper.getIdentifier("xpc_configuring_detail_ssid_to_auth", "string")) + "</li>";
        if (Util.usingCaValidation(this.mLogger, paramProfileElement))
          str7 = str7 + "<li>" + getResources().getString(this.mParcelHelper.getIdentifier("xpc_configuring_detail_using_root_ca", "string")) + "</li>";
        if (paramProfileElement.getSetting(0, 61306) != null)
          str7 = str7 + "<li>" + getResources().getString(this.mParcelHelper.getIdentifier("xpc_configuring_detail_using_user_cert", "string")) + "</li>";
        str1 = str7 + "</ul>";
        continue;
        if (this.mCasHandled == true)
          break;
        List localList = paramProfileElement.getSettingMulti(0, paramInt);
        if (localList == null)
          break;
        int i = localList.size();
        str1 = null;
        if (i > 0)
        {
          StringBuilder localStringBuilder1 = new StringBuilder().append("<li>");
          String str6 = getResources().getString(this.mParcelHelper.getIdentifier("xpc_configuring_root_cas", "string"));
          Object[] arrayOfObject1 = new Object[1];
          arrayOfObject1[0] = Integer.valueOf(localList.size());
          str1 = String.format(str6, arrayOfObject1) + "</li>";
        }
        if (this.mShowingMoreInfo)
        {
          String str5 = str1 + "<ul>";
          for (int j = 0; j < localList.size(); j++)
            str5 = str5 + "<li>" + ((SettingElement)localList.get(j)).incorrectText + "</li>";
          str1 = str5 + "</ul>";
          continue;
          SettingElement localSettingElement3 = paramProfileElement.getSetting(0, paramInt);
          str1 = null;
          if (localSettingElement3 != null)
          {
            str1 = "<li>" + getResources().getString(this.mParcelHelper.getIdentifier("xpc_configuring_user_cert", "string")) + "</li>";
            if (this.mShowingMoreInfo)
            {
              String str2 = str1 + "<ul>";
              String str3 = str2 + "<li>" + getResources().getString(this.mParcelHelper.getIdentifier("xpc_configuring_detail_user_generated", "string")) + "</li>";
              String str4 = str3 + "<li>" + getResources().getString(this.mParcelHelper.getIdentifier("xpc_configuring_detail_generated_installed", "string")) + "</li>";
              str1 = str4 + "</ul>";
              continue;
              SettingElement localSettingElement2 = paramProfileElement.getSetting(2, paramInt);
              str1 = null;
              if (localSettingElement2 != null)
              {
                str1 = "<li>" + getResources().getString(this.mParcelHelper.getIdentifier("xpc_configuring_block_rooted", "string")) + "</li>";
                continue;
                SettingElement localSettingElement1 = paramProfileElement.getSetting(0, paramInt);
                str1 = null;
                if (localSettingElement1 != null)
                  str1 = "<li>" + getResources().getString(this.mParcelHelper.getIdentifier("xpc_configuring_proxy_settings", "string")) + "</li>";
              }
            }
          }
        }
      }
    }
  }

  private String populateChanges()
  {
    StringBuilder localStringBuilder1 = new StringBuilder();
    int i = this.mParser.selectedNetworkIdx;
    ArrayList localArrayList = new ArrayList();
    int j = 0;
    localStringBuilder1.append("<html><body><ul>");
    while (j == 0)
    {
      localArrayList.add(getNextNetworkChanges(i));
      i++;
      if (!this.mHaveNextNetwork)
        j = 1;
    }
    if (localArrayList.size() > 1)
      localStringBuilder1.append(getFormattedConfigs(localArrayList));
    while (true)
    {
      StringBuilder localStringBuilder2 = new StringBuilder().append("<li>");
      Resources localResources = getResources();
      int k = this.mParcelHelper.getIdentifier("xpc_configuring_connecting", "string");
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = this.mConnectingSsid;
      localStringBuilder1.append(String.format(localResources.getString(k, arrayOfObject), new Object[0]) + "</li>");
      localStringBuilder1.append("</ul></body></html>");
      return localStringBuilder1.toString();
      localStringBuilder1.append((String)localArrayList.get(0));
    }
  }

  public void onBackPressed()
  {
    done(-2);
  }

  public void onClick(View paramView)
  {
    if (paramView == this.mCancelButton)
      quit();
    do
    {
      return;
      if (paramView == this.mContinueButton)
      {
        done(23);
        return;
      }
    }
    while (paramView != this.mMoreInfoButton);
    boolean bool;
    if (!this.mShowingMoreInfo)
    {
      bool = true;
      this.mShowingMoreInfo = bool;
      if (!this.mShowingMoreInfo)
        break label107;
      this.mMoreInfoButton.setText(this.mParcelHelper.getIdentifier("xpc_less_information", "string"));
    }
    while (true)
    {
      this.mGathering = new InitialGatherChanges(null);
      this.mGathering.execute(new Void[0]);
      return;
      bool = false;
      break;
      label107: this.mMoreInfoButton.setText(this.mParcelHelper.getIdentifier("xpc_more_information", "string"));
    }
  }

  protected void onCreate(Bundle paramBundle)
  {
    preContentView();
    setContentView(new ParcelHelper("", this).getLayoutId("xpc_show_changes"));
    super.onCreate(paramBundle);
    this.mCancelButton = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_CancelButton")));
    this.mCancelButton.setOnClickListener(this);
    this.mContinueButton = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_OkayButton")));
    this.mContinueButton.setOnClickListener(this);
    this.mMoreInfoButton = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_MoreInfoButton")));
    this.mMoreInfoButton.setOnClickListener(this);
    this.mChangesToMakeView = ((WebView)findViewById(this.mParcelHelper.getItemId("xpc_ChangesToMake")));
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    if (paramEncapsulationService == null)
    {
      Log.d("XPC", "Unable to bind to local service!\n");
      return;
    }
    super.onServiceBind(paramEncapsulationService);
    if (this.mParser == null)
      Util.log(this.mLogger, "Config was not properly bound!");
    Util.log(this.mLogger, "+++ Showing ShowChangesScreen.");
    if (haveTabs())
      setWelcomeActive();
    if ((this.mParser != null) && (this.mParser.savedConfigInfo != null) && (this.mParser.savedConfigInfo.mBeenThroughOnce == true))
    {
      done(23);
      return;
    }
    this.mGathering = new InitialGatherChanges(null);
    this.mGathering.execute(new Void[0]);
  }

  private class InitialGatherChanges extends AsyncTask<Void, Void, String>
  {
    private InitialGatherChanges()
    {
    }

    protected String doInBackground(Void[] paramArrayOfVoid)
    {
      return ShowChangesScreen.this.populateChanges();
    }

    protected void onCancelled()
    {
      super.onCancelled();
      ShowChangesScreen.this.removeMyDialog("pleaseWait");
      ShowChangesScreen.access$202(ShowChangesScreen.this, null);
    }

    protected void onPostExecute(String paramString)
    {
      super.onPostExecute(paramString);
      ShowChangesScreen.this.mChangesToMakeView.loadData(paramString, "text/html", null);
      ShowChangesScreen.this.removeMyDialog("pleaseWait");
      ShowChangesScreen.access$202(ShowChangesScreen.this, null);
    }

    protected void onPreExecute()
    {
      super.onPreExecute();
      ShowChangesScreen.this.showMyDialog(ShowChangesScreen.ShowChangesFragments.newInstance(1001), "pleaseWait");
    }
  }

  public static class ShowChangesFragments extends DialogFragment
  {
    public static ShowChangesFragments newInstance(int paramInt)
    {
      ShowChangesFragments localShowChangesFragments = new ShowChangesFragments();
      Bundle localBundle = new Bundle();
      localBundle.putInt("id", paramInt);
      localShowChangesFragments.setArguments(localBundle);
      return localShowChangesFragments;
    }

    public Dialog onCreateDialog(Bundle paramBundle)
    {
      int i = getArguments().getInt("id");
      ScreenBase localScreenBase = (ScreenBase)getActivity();
      switch (i)
      {
      default:
        return null;
      case 1001:
      }
      ProgressDialog localProgressDialog = new ProgressDialog(localScreenBase);
      localProgressDialog.setMessage(getResources().getString(((ShowChangesScreen)localScreenBase).mParcelHelper.getIdentifier("xpc_please_wait", "string")));
      localProgressDialog.setCancelable(false);
      return localProgressDialog;
    }
  }
}