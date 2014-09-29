package net.cloudpath.xpressconnect.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.MapVariables;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkElement;

public class ShowHelpdesk extends ScreenBase
  implements View.OnClickListener
{
  private TextView helpdeskEmail = null;
  private TextView helpdeskIm = null;
  private TextView helpdeskMessage = null;
  private TextView helpdeskName = null;
  private TextView helpdeskPhone = null;
  private TextView helpdeskUrl = null;
  private Button okayButton = null;

  public void onClick(View paramView)
  {
    Util.log(this.mLogger, "--- Leaving ShowHelpdesk because user clicked the okay button.");
    done(0);
  }

  protected void onCreate(Bundle paramBundle)
  {
    ParcelHelper localParcelHelper = new ParcelHelper("", this);
    preContentView();
    setContentView(localParcelHelper.getLayoutId("xpc_helpdesk"));
    super.onCreate(paramBundle);
    this.helpdeskMessage = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_HelpdeskMessage")));
    this.helpdeskName = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_HelpdeskNameField")));
    this.helpdeskPhone = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_HelpdeskPhoneField")));
    this.helpdeskEmail = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_HelpdeskEmailField")));
    this.helpdeskIm = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_HelpdeskIMField")));
    this.helpdeskUrl = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_HelpdeskURLField")));
    this.okayButton = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_okayButton")));
    if (this.okayButton != null)
      this.okayButton.setOnClickListener(this);
    this.mShowNoParentMenu = true;
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    if (paramEncapsulationService == null)
    {
      Log.d("XPC", "mBound is null in onServiceBind()!");
      return;
    }
    super.onServiceBind(paramEncapsulationService);
    if (this.mParser == null)
      Util.log(this.mLogger, "Config was not properly bound!");
    Util.log(this.mLogger, "+++ Showing ShowHelpdesk.");
    MapVariables localMapVariables2;
    try
    {
      MapVariables localMapVariables1 = new MapVariables(paramEncapsulationService.getConfigParser(), paramEncapsulationService.getLogger());
      localMapVariables2 = localMapVariables1;
      if (this.mParser == null)
      {
        Util.log(this.mLogger, "No parser bound in onServiceBind()!");
        return;
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        localException.printStackTrace();
        Util.log(this.mLogger, "Unable to varMap in ShowHelpdesk.");
        localMapVariables2 = null;
      }
      if (this.mParser.networks == null)
      {
        Util.log(this.mLogger, "No network selected in onServiceBind()!");
        return;
      }
      if (localMapVariables2 != null)
        break label226;
    }
    this.helpdeskMessage.setText(this.mParser.networks.contact_message);
    while (true)
    {
      this.helpdeskName.setText(this.mParser.networks.contact_name);
      this.helpdeskPhone.setText(this.mParser.networks.contact_phone);
      this.helpdeskEmail.setText(this.mParser.networks.contact_email);
      this.helpdeskIm.setText(this.mParser.networks.contact_im);
      this.helpdeskUrl.setText(this.mParser.networks.contact_url);
      return;
      label226: this.helpdeskMessage.setText(localMapVariables2.varMap(this.mParser.networks.contact_message, false, this.mWifi));
    }
  }
}