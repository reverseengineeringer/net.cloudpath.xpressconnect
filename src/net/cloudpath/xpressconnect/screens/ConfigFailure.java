package net.cloudpath.xpressconnect.screens;

import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.MapVariables;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.ProfileElement;

public class ConfigFailure extends ConfigResultBase
{
  private static final int CHANGE_CREDS = 4000;
  private int failReason = -10;
  private boolean mSyslogWritten = false;

  private void dumpLogCat()
  {
    Util.log(this.mLogger, " -+-+-+-+- System Log Dump -+-+-+-+- ");
    try
    {
      ArrayList localArrayList = new ArrayList();
      localArrayList.add("logcat");
      localArrayList.add("-d");
      localArrayList.add("-s");
      localArrayList.add("NetworkStateTracker:*");
      localArrayList.add("wpa_supplicant:*");
      localArrayList.add("keystore:*");
      localArrayList.add("System.err:*");
      localArrayList.add("XPC:*");
      localArrayList.add("CredentialInstaller:*");
      localArrayList.add("CertInstaller:*");
      localArrayList.add("ActivityManager:*");
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec((String[])localArrayList.toArray(new String[0])).getInputStream()));
      while (true)
      {
        String str = localBufferedReader.readLine();
        if (str == null)
          break;
        log_ne("SysLog : " + str);
      }
    }
    catch (IOException localIOException)
    {
      Util.log(this.mLogger, "I/O Exception : " + localIOException.getMessage());
      return;
    }
    this.mSyslogWritten = true;
  }

  private void log_ne(String paramString)
  {
    if (this.mLogger == null)
    {
      Log.d("XPC", "Logger is null in configure thread!");
      return;
    }
    this.mLogger.addLineNoEcho(paramString);
  }

  public void onClick(View paramView)
  {
    if (paramView == this.doneButton)
      if ((this.mParser == null) || (this.mParser.selectedNetwork == null))
      {
        if (this.mGlobals != null)
          this.mGlobals.destroyServiceData();
        Util.log(this.mLogger, "--- Leaving ConfigFailure by way of Done button.");
        done(this.mFinalResult);
      }
    do
    {
      return;
      super.onClick(paramView);
      return;
      if (paramView == this.retryButton)
      {
        this.mFailure.clearFailureReason();
        if (this.failReason == 22)
        {
          Util.log(this.mLogger, "--- Leaving ConfigFailure by way of Retry button, but with a keystore unlock failure.");
          done(22);
          return;
        }
        if (this.failReason == 26)
        {
          Util.log(this.mLogger, "--- Leaving ConfigFailure by way of Retry button, but with no network available.");
          done(31);
          return;
        }
        Util.log(this.mLogger, "--- Leaving ConfigFailure by way of Retry button.");
        done(16);
        return;
      }
    }
    while (paramView != this.changeCredsButton);
    Util.log(this.mLogger, "--- Leaving ConfigFailure by way of the Change Creds Button.");
    done(10);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.rateMe.setVisibility(8);
    this.successImage.setImageResource(this.mParcelHelper.getDrawableId("xpc_error"));
    if (getIntent() != null)
      this.failReason = getIntent().getIntExtra("failReason", 15);
    if (this.failReason == 30)
      this.mShowNoParentMenu = true;
    if ((this.failReason == 21) || (this.failReason == -1) || (this.failReason == 30) || (this.failReason == 15) || (this.failReason == 35))
    {
      this.retryButton.setVisibility(8);
      if (haveTabs())
        setConnectedActive();
      if ((this.failReason != 11) && (this.failReason != 22))
        break label287;
      this.changeCredsButton.setOnClickListener(this);
      this.changeCredsButton.setVisibility(0);
    }
    while (true)
    {
      this.finalStatus.setText(this.mParcelHelper.getIdentifier("xpc_ultimate_fail", "string"));
      if (this.failReason == 26)
        this.finalStatus.setText(this.mParcelHelper.getIdentifier("xpc_no_networks", "string"));
      if (this.failReason == 33)
      {
        this.doneButton.setText(this.mParcelHelper.getIdentifier("xpc_skip", "string"));
        this.finalStatus.setText(this.mParcelHelper.getIdentifier("xpc_site_check_fail", "string"));
      }
      this.ipAddress.setVisibility(4);
      this.mFinalResult = 0;
      return;
      this.retryButton.setOnClickListener(this);
      break;
      label287: this.changeCredsButton.setVisibility(8);
    }
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    if (paramMenu == null)
      return super.onCreateOptionsMenu(paramMenu);
    if ((this.failReason != 21) && (this.failReason != -1) && (this.failReason != 30) && (this.failReason != 15) && (!Util.isOpenNetwork(this.mLogger, this.mParser)))
      paramMenu.add(0, 4000, 0, this.mParcelHelper.getIdentifier("xpc_change_creds", "string"));
    return super.onCreateOptionsMenu(paramMenu);
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    if (paramMenuItem == null)
      return super.onOptionsItemSelected(paramMenuItem);
    if (paramMenuItem.getItemId() == 4000)
    {
      Util.log(this.mLogger, "--- Leaving ConfigFailure by way of Change Creds button.");
      done(10);
      return true;
    }
    return super.onOptionsItemSelected(paramMenuItem);
  }

  protected void onRestoreInstanceState(Bundle paramBundle)
  {
    this.mSyslogWritten = paramBundle.getBoolean("logwritten");
    super.onRestoreInstanceState(paramBundle);
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putBoolean("logwritten", this.mSyslogWritten);
    super.onSaveInstanceState(paramBundle);
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    if (paramEncapsulationService == null)
      Log.d("XPC", "Unable to bind to the local service!");
    while (true)
    {
      return;
      super.onServiceBind(paramEncapsulationService);
      if (this.mParser == null)
        Util.log(this.mLogger, "Config was not properly bound!");
      Util.log(this.mLogger, "+++ Showing ConfigFailure screen.");
      try
      {
        MapVariables localMapVariables1 = new MapVariables(this.mParser, this.mLogger);
        localMapVariables2 = localMapVariables1;
        if ((this.mFailure != null) && (this.mFailure.getFailureString() != null) && (this.mFailure.getFailureString().length() > 0))
        {
          Util.log(this.mLogger, "Final failure string : " + this.mFailure.getFailureString());
          if (localMapVariables2 == null)
          {
            this.smallStatus.setText(this.mFailure.getFailureString());
            if (this.mFailure.getIconToUse() == FailureReason.useWarningIcon)
              this.successImage.setImageResource(this.mParcelHelper.getDrawableId("xpc_warning"));
            if ((this.mParser != null) && (this.mParser.selectedProfile != null) && (this.mParser.selectedProfile.showPreCreds != 1))
              this.changeCredsButton.setVisibility(4);
            if ((this.mSyslogWritten) || (Build.VERSION.SDK_INT >= 16))
              continue;
            dumpLogCat();
            return;
          }
        }
      }
      catch (Exception localException)
      {
        while (true)
        {
          localException.printStackTrace();
          Util.log(this.mLogger, "Unable to create MapVariables in ConfigFailure.");
          MapVariables localMapVariables2 = null;
          continue;
          this.smallStatus.setText(localMapVariables2.varMap(this.mFailure.getFailureString(), false, this.mWifi));
          continue;
          this.smallStatus.setText(this.mParcelHelper.getIdentifier("xpc_unknown_fail", "string"));
        }
      }
    }
  }
}