package net.cloudpath.xpressconnect.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.MapVariables;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.ProfileElement;
import net.cloudpath.xpressconnect.parsers.config.SettingElement;
import net.cloudpath.xpressconnect.parsers.config.SettingId;

public class Notify extends ScreenBase
  implements View.OnClickListener
{
  private Button cancelButton = null;
  private Button continueButton = null;
  private ListView list = null;
  private ArrayAdapter<String> notifyList = null;
  private boolean showCancel = false;

  private void buildNotifications()
  {
    try
    {
      MapVariables localMapVariables = new MapVariables(this.mParser, this.mLogger);
      this.notifyList.clear();
      for (int i = 0; i < SettingId.allOptions.length; i++)
      {
        Log.d("XPC", "Checking notification for id = " + SettingId.allOptions[i]);
        String str = hasNotification(SettingId.allOptions[i]);
        if (str != null)
          this.notifyList.add(localMapVariables.varMap(str, false, this.mWifi));
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      Util.log(this.mLogger, "--- Leaving Notify because we were unable to init varmap!");
      done(1);
      return;
    }
    if (this.notifyList.getCount() <= 0)
    {
      Util.log(this.mLogger, "--- Leaving Notify because there is nothing to do.");
      done(1);
      return;
    }
    if (this.showCancel)
      this.cancelButton.setVisibility(0);
    while (true)
    {
      this.list.setAdapter(this.notifyList);
      this.list.invalidate();
      return;
      this.cancelButton.setVisibility(4);
    }
  }

  private String hasNotification(int paramInt)
  {
    String str1 = null;
    if ((this.mParser == null) || (this.mParser.selectedProfile == null))
    {
      Util.log(this.mLogger, "Invalid configuration information in hasNotification().");
      return str1;
    }
    SettingElement localSettingElement = null;
    switch (paramInt)
    {
    default:
    case 40001:
    case 40061:
    case 40062:
    case 40064:
    case 40065:
    case 40066:
    case 40067:
    case 40068:
    case 40069:
    case 40070:
    case 40071:
    case 40072:
    case 40073:
    case 40074:
    case 40078:
    case 61301:
    case 61307:
    case 61308:
    case 80025:
    case 40015:
    case 61306:
    case 65000:
    case 65001:
    case 65002:
    case 65003:
    case 80032:
    case 80033:
    case 80037:
    }
    while (true)
    {
      str1 = null;
      if (localSettingElement == null)
        break;
      String str2 = localSettingElement.helpLink;
      str1 = null;
      if (str2 == null)
        break;
      boolean bool = localSettingElement.helpLink.startsWith("NOTIFY:");
      str1 = null;
      if (!bool)
        break;
      str1 = localSettingElement.helpLink.substring(7);
      if ((localSettingElement.isRequired == null) || (localSettingElement.isRequired.booleanValue()))
        break;
      this.showCancel = true;
      return str1;
      localSettingElement = this.mParser.selectedProfile.getSetting(1, paramInt);
      continue;
      localSettingElement = this.mParser.selectedProfile.getSetting(0, paramInt);
      continue;
      localSettingElement = this.mParser.selectedProfile.getSetting(2, paramInt);
    }
  }

  public void onBackPressed()
  {
    Util.log(this.mLogger, "--- Leaving Notify because user pressed back.");
    done(-2);
    super.onBackPressed();
  }

  public void onClick(View paramView)
  {
    if (paramView == this.cancelButton)
    {
      Util.log(this.mLogger, "--- Leaving Notify because user clicked Cancel.");
      done(20);
      return;
    }
    if (paramView == this.continueButton)
    {
      Util.log(this.mLogger, "--- Leaving Notify because user clicked Continue.");
      done(1);
      return;
    }
    Util.log(this.mLogger, "Unknown button clicked on the notification screen!");
  }

  public void onCreate(Bundle paramBundle)
  {
    Log.d("XPC", "onCreate() called! (notify)");
    preContentView();
    setContentView(new ParcelHelper("", this).getLayoutId("xpc_notify"));
    super.onCreate(paramBundle);
    this.list = ((ListView)findViewById(this.mParcelHelper.getItemId("xpc_notifyList")));
    this.cancelButton = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_cancelBtn")));
    this.cancelButton.setOnClickListener(this);
    this.continueButton = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_continueBtn")));
    this.continueButton.setOnClickListener(this);
    if (haveTabs())
      setConfigureActive();
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    if (paramEncapsulationService == null)
    {
      Log.d("XPC", "Unable to bind to local service!");
      return;
    }
    if (this.mParser == null)
      Util.log(this.mLogger, "Config was not properly bound!");
    super.onServiceBind(paramEncapsulationService);
    Util.log(this.mLogger, "+++ Showing Notify.");
    if (haveTabs())
      setConfigureActive();
    if ((this.mParser != null) && (this.mParser.savedConfigInfo != null) && (this.mParser.savedConfigInfo.mBeenThroughOnce == true))
    {
      Util.log(this.mLogger, "Second time through, skipping this screen.");
      done(1);
      return;
    }
    this.notifyList = new ArrayAdapter(this, this.mParcelHelper.getLayoutId("xpc_notifylayout"), this.mParcelHelper.getItemId("xpc_text1"));
    buildNotifications();
  }
}