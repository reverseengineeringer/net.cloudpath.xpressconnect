package net.cloudpath.xpressconnect.screens;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkElement;

public class AboutScreen extends ScreenBase
{
  private TextView licenseText = null;
  private TextView mEula = null;
  private TextView versionText = null;

  public void onBackPressed()
  {
    Util.log(this.mLogger, "--- Leaving about screen.");
    super.onBackPressed();
  }

  protected void onCreate(Bundle paramBundle)
  {
    preContentView();
    setContentView(new ParcelHelper("", this).getLayoutId("xpc_about"));
    super.onCreate(paramBundle);
    this.versionText = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_VersionString")));
    this.licenseText = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_LicenseString")));
    this.mEula = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_CopyrightView")));
    StringBuilder localStringBuilder = new StringBuilder();
    InputStreamReader localInputStreamReader;
    BufferedReader localBufferedReader;
    try
    {
      localInputStreamReader = new InputStreamReader(getAssets().open("eula.txt"));
      localBufferedReader = new BufferedReader(localInputStreamReader);
      while (true)
      {
        String str = localBufferedReader.readLine();
        if (str == null)
          break;
        localStringBuilder.append(str + "\n");
      }
    }
    catch (IOException localIOException)
    {
      this.mEula.setText("Error reading eula.txt from the assets folder!");
      localIOException.printStackTrace();
    }
    while (true)
    {
      this.mShowNoParentMenu = true;
      return;
      localBufferedReader.close();
      localInputStreamReader.close();
      this.mEula.setText(localStringBuilder.toString());
    }
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    if (paramEncapsulationService == null)
    {
      Log.d("XPC", "Unable to bind to the local service!");
      return;
    }
    super.onServiceBind(paramEncapsulationService);
    Util.log(this.mLogger, "+++ Showing about screen.");
    PackageManager localPackageManager = getPackageManager();
    try
    {
      str = localPackageManager.getPackageInfo(getPackageName(), 0).versionName;
      this.versionText.setText(getResources().getString(this.mParcelHelper.getIdentifier("xpc_version", "string")) + " " + str);
      if (this.mParser != null)
        this.licenseText.setText(getResources().getString(this.mParcelHelper.getIdentifier("xpc_licensed_to", "string")) + " " + this.mParser.networks.licensee + " (" + this.mParser.networks.license + ")");
      this.licenseText.setTextColor(-16777216 + this.mGlobals.getLicensedToFontColor());
      this.versionText.invalidate();
      this.licenseText.invalidate();
      return;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      while (true)
      {
        String str = "Unknown";
        localNameNotFoundException.printStackTrace();
      }
    }
  }
}