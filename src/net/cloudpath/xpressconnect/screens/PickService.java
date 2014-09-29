package net.cloudpath.xpressconnect.screens;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.util.ArrayList;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.GenericCallback;
import net.cloudpath.xpressconnect.GestureCallback;
import net.cloudpath.xpressconnect.GetManualUrl;
import net.cloudpath.xpressconnect.LoadConfigFromDb;
import net.cloudpath.xpressconnect.LocalDbHelper;
import net.cloudpath.xpressconnect.SingleLineList;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.remote.GetConfigFromUrl;
import net.cloudpath.xpressconnect.retention.MainDataRetention;
import net.cloudpath.xpressconnect.screens.delegates.PickServiceDelegate;

public class PickService extends ScreenBase
  implements AdapterView.OnItemClickListener, GenericCallback, GestureCallback
{
  private static final int DELETE_DB_ITEM = 1000;
  private static final int DIALOG_GET_MANUAL_URL = 1000;
  private static final String DIALOG_GET_MANUAL_URL_TAG = "manualUrl";
  private static final int MANUAL_URL = 5000;
  private String contextSelected = null;
  private ListView customerList = null;
  private PickServiceDelegate mDelegate = null;
  private SingleLineList mList = null;

  private void drawList()
  {
    if (this.mDelegate.getNetworkList() == null)
    {
      alertThenDie(this.mParcelHelper.getIdentifier("xpc_error_title", "string"), "The program attempted to draw a list without having any data in place.  Please report this.", this.mParcelHelper.getIdentifier("xpc_ok_string", "string"));
      return;
    }
    this.customerList.setAdapter(this.mList.getData());
    this.customerList.setOnItemClickListener(this);
  }

  private void gotoFlowControl(String paramString1, String paramString2)
  {
    GotoScreen.flowControl(this, paramString1, paramString2, null, null);
    this.mDelegate.closeDbIfNeeded();
  }

  private void populateList()
  {
    SQLiteDatabase localSQLiteDatabase;
    Cursor localCursor;
    try
    {
      localSQLiteDatabase = this.mDelegate.getDatabase();
      localCursor = localSQLiteDatabase.rawQuery("select * from configuredNetworks;", null);
      if (!localCursor.moveToFirst())
      {
        localCursor.close();
        localSQLiteDatabase.close();
        this.mDelegate.closeDbIfNeeded();
        Util.log(this.mLogger, "Configuration DB is empty.  Going to startup problem...");
        GotoScreen.startupProblem(this);
        return;
      }
    }
    catch (SQLiteException localSQLiteException)
    {
      alertThenDie(this.mParcelHelper.getIdentifier("xpc_no_db", "string"), "Database error", this.mParcelHelper.getIdentifier("xpc_ok_string", "string"));
      return;
    }
    int i = localCursor.getColumnIndex("name");
    int j = localCursor.getColumnIndex("url");
    if ((i < 0) || (j < 0))
    {
      Util.log(this.mLogger, "Database didn't return valid indicies!?");
      localCursor.close();
      localSQLiteDatabase.close();
      this.mDelegate.closeDbIfNeeded();
      alertThenDie(this.mParcelHelper.getIdentifier("xpc_no_data", "string"), "Your previously configured network database appears to be corrupt.  Please uninstall and reinstall this program.", this.mParcelHelper.getIdentifier("xpc_ok_string", "string"));
      return;
    }
    ArrayList localArrayList = new ArrayList();
    this.mList = new SingleLineList(this);
    do
    {
      MainDataRetention localMainDataRetention = new MainDataRetention();
      localMainDataRetention.orgName = localCursor.getString(i);
      localMainDataRetention.orgUrl = localCursor.getString(j);
      localArrayList.add(localMainDataRetention);
      this.mList.addItem(localCursor.getString(i));
    }
    while (localCursor.moveToNext());
    localCursor.close();
    localSQLiteDatabase.close();
    this.mDelegate.closeDbIfNeeded();
    this.mDelegate.setNetworkList(localArrayList);
    drawList();
  }

  public void handleDumpDatabase()
  {
    LocalDbHelper.dumpDbToSdCard(this);
  }

  public void handleNfcProgrammerGesture()
  {
  }

  public void onBackPressed()
  {
    quit();
    super.onBackPressed();
  }

  public boolean onContextItemSelected(MenuItem paramMenuItem)
  {
    SQLiteDatabase localSQLiteDatabase = this.mDelegate.getDatabase();
    Util.log(this.mLogger, "Deleting configuration for '" + this.contextSelected + "'.");
    try
    {
      localSQLiteDatabase.execSQL("delete from configuredNetworks where name=\"" + this.contextSelected + "\";");
      localSQLiteDatabase.close();
      this.mDelegate.closeDbIfNeeded();
      Util.log(this.mLogger, "Repopulating list.");
      this.mList.clear();
      populateList();
      return super.onContextItemSelected(paramMenuItem);
    }
    catch (IllegalStateException localIllegalStateException)
    {
      while (true)
        Toast.makeText(this, "Unable to delete the configuration from the database.", 1).show();
    }
  }

  protected void onCreate(Bundle paramBundle)
  {
    ParcelHelper localParcelHelper = new ParcelHelper("", this);
    preContentView();
    setContentView(localParcelHelper.getLayoutId("xpc_main"));
    super.onCreate(paramBundle);
    Util.log(this.mLogger, "+++ Showing PickService");
    this.mDelegate = new PickServiceDelegate(this);
    this.mDelegate.setAsLibrary(getIntent().getBooleanExtra("library_mode", false));
    if (haveTabs())
      setWelcomeActive();
    this.customerList = ((ListView)findViewById(this.mParcelHelper.getItemId("xpc_MainSchoolList")));
    this.customerList.requestFocus();
    this.customerList.setOnItemClickListener(this);
    registerForContextMenu(this.customerList);
    setGestureCallback(this);
    this.mShowNoParentMenu = false;
    this.mShowAboutMenu = false;
    this.mShowLogsMenu = true;
    this.mShowHelpdeskMenu = false;
    this.mShowEmailLogsMenu = true;
    this.mShowStartOverMenu = false;
    this.mShowQuitMenu = true;
    this.mDelegate.setNetworkList((ArrayList)getLastNonConfigurationInstance());
  }

  public void onCreateContextMenu(ContextMenu paramContextMenu, View paramView, ContextMenu.ContextMenuInfo paramContextMenuInfo)
  {
    this.contextSelected = ((TextView)((AdapterView.AdapterContextMenuInfo)paramContextMenuInfo).targetView.findViewById(this.mParcelHelper.getItemId("xpc_text1"))).getText().toString();
    Log.d("XPC", "Selected : " + this.contextSelected);
    paramContextMenu.add(0, 1000, 0, this.mParcelHelper.getIdentifier("xpc_delete_db_entry", "string"));
    super.onCreateContextMenu(paramContextMenu, paramView, paramContextMenuInfo);
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

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    if (paramView == null)
    {
      Util.log(this.mLogger, "Attempt to click on something that doesn't exist!?  (arg1 is null)");
      return;
    }
    String str1 = ((TextView)paramView.findViewById(this.mParcelHelper.getItemId("xpc_text1"))).getText().toString();
    if (str1 == null)
    {
      Util.log(this.mLogger, "Unable to locate the string that was clicked!?");
      return;
    }
    Util.log(this.mLogger, "Looking for : " + str1);
    int i = this.mDelegate.findIndexForNetwork(str1);
    String str2 = this.mDelegate.getCustomerUrlByIndex(i);
    String str3 = this.mDelegate.getCustomerNameByIndex(i);
    if (!new LoadConfigFromDb(this.mLogger, this.mGlobals, this).loadFromDb(str2, str3))
    {
      Util.log(this.mLogger, "FAILED to load config from database!");
      this.mFailure.setFailReason(FailureReason.useErrorIcon, getResources().getString(this.mParcelHelper.getIdentifier("xpc_config_load_failed", "string")), 5);
    }
    while (true)
    {
      this.mDelegate.closeDbIfNeeded();
      gotoFlowControl(str2, str3);
      return;
      Toast.makeText(this, "Loading the configuration from a cached copy.", 0).show();
    }
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    if (paramMenuItem == null)
      return super.onOptionsItemSelected(paramMenuItem);
    if (paramMenuItem.getItemId() == 5000)
    {
      showMyDialog(PickServiceFragments.newInstance(1000), "manualUrl");
      return true;
    }
    return super.onOptionsItemSelected(paramMenuItem);
  }

  protected void onPause()
  {
    this.mGlobals.setSavedObject(this.mDelegate.getNetworkList());
    super.onPause();
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    if (paramEncapsulationService == null)
    {
      Log.d("XPC", "Unable to bind to the local service!");
      return;
    }
    this.mDelegate.setRequiredSettings(paramEncapsulationService.getLogger());
    super.onServiceBind(paramEncapsulationService);
    if (this.mParser == null)
      Util.log(this.mLogger, "Config was not properly bound!");
    if (haveTabs())
      setWelcomeActive();
    if (this.mDelegate.getNetworkList() == null)
    {
      populateList();
      return;
    }
    this.mDelegate.clearNetworkList();
    populateList();
  }

  public void setCallbackData(int paramInt, String paramString)
  {
    if (paramInt >= 0)
    {
      String str = this.mDelegate.validatedUrl(paramString);
      new GetConfig(null).execute(new String[] { str });
      removeDialog(1000);
      showDialog(200);
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
      Thread.currentThread().setName("Get config task.");
      this.configUrl = paramArrayOfString[0];
      GetConfigFromUrl localGetConfigFromUrl = new GetConfigFromUrl(PickService.this.mLogger);
      if (!localGetConfigFromUrl.findByUrl(this.configUrl, true))
      {
        Util.log(PickService.this.mLogger, "Unable to load config from : " + this.configUrl);
        return null;
      }
      PickService.this.mGlobals.setXmlConfig(localGetConfigFromUrl.getPageResult());
      return null;
    }

    protected void onPostExecute(Void paramVoid)
    {
      super.onPostExecute(paramVoid);
      PickService.this.gotoFlowControl(this.configUrl, null);
      PickService.this.removeDialog(200);
    }
  }

  public static class PickServiceFragments extends DialogFragment
  {
    public static PickServiceFragments newInstance(int paramInt)
    {
      PickServiceFragments localPickServiceFragments = new PickServiceFragments();
      Bundle localBundle = new Bundle();
      localBundle.putInt("id", paramInt);
      localPickServiceFragments.setArguments(localBundle);
      return localPickServiceFragments;
    }

    public Dialog onCreateDialog(Bundle paramBundle)
    {
      int i = getArguments().getInt("id");
      ScreenBase localScreenBase = (ScreenBase)getActivity();
      if (i == 1000)
      {
        GetManualUrl localGetManualUrl = new GetManualUrl(localScreenBase);
        localGetManualUrl.setCallback((PickService)localScreenBase);
        return localGetManualUrl;
      }
      return null;
    }
  }
}