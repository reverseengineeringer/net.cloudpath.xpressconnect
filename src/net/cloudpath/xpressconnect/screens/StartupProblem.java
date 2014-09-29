package net.cloudpath.xpressconnect.screens;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.net.MalformedURLException;
import java.net.URL;
import net.cloudpath.xpressconnect.GenericCallback;
import net.cloudpath.xpressconnect.GestureCallback;
import net.cloudpath.xpressconnect.GetManualUrl;
import net.cloudpath.xpressconnect.LocalDbHelper;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.remote.GetConfigFromUrl;

public class StartupProblem extends ScreenBase
  implements GenericCallback, GestureCallback
{
  private static final int GET_MANUAL_URL = 10000;
  private static final String GET_MANUAL_URL_TAG = "manualUrl";
  private static final int MANUAL_URL = 5000;
  private Logger mLogger = null;
  private String mResultUrl = null;
  private TextView message = null;
  private Button okayButton = null;

  private void gotoFlowControl(String paramString1, String paramString2)
  {
    GotoScreen.flowControl(this, paramString1, paramString2, null, null);
  }

  private String validateUrl(String paramString)
  {
    Util.log(this.mLogger, "Initial input URL : " + paramString);
    if ((!paramString.startsWith("http")) && (!paramString.startsWith("https")) && (!paramString.contains("://")))
      paramString = "http://" + paramString;
    if (!paramString.endsWith("/network_config_android.xml"))
      paramString = paramString + "/network_config_android.xml";
    try
    {
      URL localURL = new URL(paramString);
      Util.log(this.mLogger, "Returning mapped URL of : " + localURL.toString());
      return localURL.toString();
    }
    catch (MalformedURLException localMalformedURLException)
    {
      localMalformedURLException.printStackTrace();
      Util.log(this.mLogger, "URL is in a strange format : " + null);
    }
    return null;
  }

  public void handleDumpDatabase()
  {
    LocalDbHelper.dumpDbToSdCard(this);
  }

  public void handleNfcProgrammerGesture()
  {
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt2 == 29) || (paramInt2 == 2222))
      super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onBackPressed()
  {
    quit();
  }

  protected void onCreate(Bundle paramBundle)
  {
    ParcelHelper localParcelHelper = new ParcelHelper("", this);
    preContentView();
    setContentView(localParcelHelper.getLayoutId("xpc_show_startup_problem"));
    super.onCreate(paramBundle);
    this.message = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_MessageText")));
    this.message.setText(this.mParcelHelper.getIdentifier("xpc_no_configured_networks", "string"));
    this.okayButton = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_OkayButton")));
    this.okayButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Util.log(StartupProblem.this.mLogger, "--- Leaving StartupProblem because user clicked 'okay' button.");
        StartupProblem.this.quit();
      }
    });
    setGestureCallback(this);
    this.mShowNoParentMenu = false;
    this.mShowAboutMenu = false;
    this.mShowLogsMenu = true;
    this.mShowHelpdeskMenu = false;
    this.mShowEmailLogsMenu = true;
    this.mShowStartOverMenu = false;
    this.mShowQuitMenu = true;
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    if (paramMenu == null)
    {
      Util.log(this.mLogger, "Menu value is null in onCreateOptionsMenu().\n");
      return super.onCreateOptionsMenu(paramMenu);
    }
    paramMenu.add(0, 5000, 0, this.mParcelHelper.getIdentifier("xpc_manual_url", "string"));
    return super.onCreateOptionsMenu(paramMenu);
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    if (paramMenuItem == null)
      return super.onOptionsItemSelected(paramMenuItem);
    if (paramMenuItem.getItemId() == 5000)
    {
      showMyDialog(StartupProblemFragments.newInstance(10000), "manualUrl");
      return true;
    }
    return super.onOptionsItemSelected(paramMenuItem);
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    super.onServiceBind(paramEncapsulationService);
    Util.log(this.mLogger, "+++ Showing StartupProblem.");
  }

  public void setCallbackData(int paramInt, String paramString)
  {
    if (paramInt >= 0)
    {
      Util.log(this.mLogger, "Setting URL value!");
      this.mResultUrl = paramString;
      this.mGlobals.setLoadedFromUrl(validateUrl(this.mResultUrl));
      removeMyDialog("manualUrl");
      showMyDialog(200);
      GetConfig localGetConfig = new GetConfig(null);
      String[] arrayOfString = new String[1];
      arrayOfString[0] = validateUrl(this.mResultUrl);
      localGetConfig.execute(arrayOfString);
    }
  }

  private class GetConfig extends AsyncTask<String, Void, Void>
  {
    private String configUrl = null;

    private GetConfig()
    {
    }

    protected Void doInBackground(String[] paramArrayOfString)
    {
      Thread.currentThread().setName("Get config in background task.");
      this.configUrl = paramArrayOfString[0];
      GetConfigFromUrl localGetConfigFromUrl = new GetConfigFromUrl(StartupProblem.this.mLogger);
      if (!localGetConfigFromUrl.findByUrl(this.configUrl, true))
      {
        Util.log(StartupProblem.this.mLogger, "Unable to load config from : " + this.configUrl);
        return null;
      }
      StartupProblem.this.mGlobals.setXmlConfig(localGetConfigFromUrl.getPageResult());
      return null;
    }

    protected void onPostExecute(Void paramVoid)
    {
      Util.log(StartupProblem.this.mLogger, "--- Leaving StartupProblem because a URL was set manually.");
      StartupProblem.this.gotoFlowControl(this.configUrl, null);
      this.configUrl = null;
      StartupProblem.this.removeMyDialog("pleaseWaitDialogTag");
      super.onPostExecute(paramVoid);
    }
  }

  public static class StartupProblemFragments extends DialogFragment
  {
    public static StartupProblemFragments newInstance(int paramInt)
    {
      StartupProblemFragments localStartupProblemFragments = new StartupProblemFragments();
      Bundle localBundle = new Bundle();
      localBundle.putInt("id", paramInt);
      localStartupProblemFragments.setArguments(localBundle);
      return localStartupProblemFragments;
    }

    public Dialog onCreateDialog(Bundle paramBundle)
    {
      ScreenBase localScreenBase = (ScreenBase)getActivity();
      GetManualUrl localGetManualUrl = new GetManualUrl(localScreenBase);
      localGetManualUrl.setCallback((StartupProblem)localScreenBase);
      return localGetManualUrl;
    }
  }
}