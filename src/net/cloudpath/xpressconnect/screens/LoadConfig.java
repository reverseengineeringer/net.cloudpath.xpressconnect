package net.cloudpath.xpressconnect.screens;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.LocalDbHelper;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkElement;
import net.cloudpath.xpressconnect.retention.LoadConfigRetention;
import net.cloudpath.xpressconnect.thread.LoadConfigThread;
import net.cloudpath.xpressconnect.thread.ThreadCom;
import net.cloudpath.xpressconnect.thread.ThreadComCallbacks;

public class LoadConfig extends ScreenBase
  implements ThreadComCallbacks
{
  private static final int DIALOG_PROMPT_EXIT = 1000;
  private static final String DIALOG_PROMPT_EXIT_TAG = "dialogPromptExit";
  protected LoadConfigThread activeThread = null;
  private AlertDialog ad = null;
  private String errorstr = null;
  private String mInConfig = null;
  private String mPassword = null;
  private volatile ThreadCom mThreadCom = null;
  private String mUsername = null;
  private LocalDbHelper myDbHelper = null;
  private int otherId = -1;
  private int retId = -1;
  private ImageView spinner = null;
  private AnimationDrawable spinnerAnim = null;
  private int titleId = -1;

  private String readFile()
    throws IOException
  {
    InputStream localInputStream = getResources().openRawResource(this.mParcelHelper.getIdentifier("network_config_android", "raw"));
    BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
    StringBuffer localStringBuffer = new StringBuffer();
    String str = "";
    while (str != null)
      try
      {
        str = localBufferedReader.readLine();
        if (str != null)
          localStringBuffer.append(str);
      }
      finally
      {
        localBufferedReader.close();
        localInputStream.close();
      }
    localBufferedReader.close();
    localInputStream.close();
    return localStringBuffer.toString();
  }

  private void saveConfigToDatabase()
  {
    this.myDbHelper = new LocalDbHelper(getApplicationContext(), "XPC_DB", null, 8);
    SQLiteDatabase localSQLiteDatabase;
    try
    {
      localSQLiteDatabase = this.myDbHelper.getWritableDatabase();
      if (localSQLiteDatabase == null)
      {
        Util.log(this.mLogger, "Unable to get handle to writeable database.");
        this.myDbHelper.close();
        return;
      }
    }
    catch (Exception localException)
    {
      Util.log(this.mLogger, "Unable to get writable database.  Skipping.");
      this.myDbHelper.close();
      return;
    }
    if (this.mGlobals == null)
    {
      Util.log(this.mLogger, "Globals interface unavailable in LoadConfig!");
      localSQLiteDatabase.close();
      this.myDbHelper.close();
      return;
    }
    if (this.mGlobals.getSelectedConfigName() == null)
    {
      Util.log(this.mLogger, "No configuration name selected.  Skipping.");
      localSQLiteDatabase.close();
      this.myDbHelper.close();
      return;
    }
    Util.log(this.mLogger, "Name : " + this.mGlobals.getSelectedConfigName());
    if (this.mGlobals.getLoadedFromUrl() == null)
    {
      Util.log(this.mLogger, "No configuration URL selected.  Will use the value from the config.");
      if (!setLoadedUrlFromConfig())
      {
        localSQLiteDatabase.close();
        this.myDbHelper.close();
        return;
      }
    }
    Util.log(this.mLogger, "URL  : " + this.mGlobals.getLoadedFromUrl());
    if ((this.mGlobals.getConfigParser() == null) || (this.mGlobals.getXmlConfig() == null))
    {
      Util.log(this.mLogger, "Config parser information not found.");
      localSQLiteDatabase.close();
      this.myDbHelper.close();
      return;
    }
    String str = this.mGlobals.getXmlConfig().replace("\"", "\"\"");
    boolean bool1 = this.mGlobals.getCredsPushedIn();
    int i = 0;
    if (bool1)
    {
      boolean bool2 = Util.stringIsNotEmpty(this.mUsername);
      i = 0;
      if (bool2)
      {
        boolean bool3 = Util.stringIsNotEmpty(this.mPassword);
        i = 0;
        if (bool3)
        {
          boolean bool4 = this.mUsername.startsWith("Enrollment-");
          i = 0;
          if (bool4)
          {
            boolean bool5 = this.mPassword.startsWith("TOKEN-");
            i = 0;
            if (bool5)
            {
              Util.log(this.mLogger, "Configuration appears to be from XPC ES.");
              i = 1;
              this.mGlobals.setIsEs(true);
            }
          }
        }
      }
    }
    Cursor localCursor = localSQLiteDatabase.rawQuery("select * from configuredNetworks where name=\"" + this.mGlobals.getSelectedConfigName().replace("\"", "\"\"") + "\"", null);
    if (!localCursor.moveToFirst())
      localSQLiteDatabase.execSQL("insert into configuredNetworks (name, url, isEs, lastConfig) VALUES (\"" + this.mGlobals.getSelectedConfigName().replace("\"", "\"\"") + "\", \"" + this.mGlobals.getLoadedFromUrl() + "\", \"" + String.valueOf(i) + "\", \"" + str + "\")");
    while (true)
    {
      localCursor.close();
      localSQLiteDatabase.close();
      this.myDbHelper.close();
      return;
      localSQLiteDatabase.execSQL("update configuredNetworks set url=\"" + this.mGlobals.getLoadedFromUrl() + "\" where name=\"" + this.mGlobals.getSelectedConfigName().replace("\"", "\"\"") + "\";");
      localSQLiteDatabase.execSQL("update configuredNetworks set lastConfig=\"" + str + "\" where name=\"" + this.mGlobals.getSelectedConfigName().replace("\"", "\"\"") + "\";");
      if (i == 1)
        localSQLiteDatabase.execSQL("update configuredNetworks set isEs=\"" + String.valueOf(i) + "\" where name=\"" + this.mGlobals.getSelectedConfigName().replace("\"", "\"\"") + "\";");
    }
  }

  private boolean setLoadedUrlFromConfig()
  {
    int i = this.mParser.networks.server_address.indexOf("/pipe/postData.php");
    String str1 = this.mParser.networks.server_address.substring(0, i);
    String str2 = str1 + "/network_config_android.xml";
    this.mGlobals.setLoadedFromUrl(str2);
    return true;
  }

  private void startParser()
  {
    if (this.activeThread == null)
    {
      this.activeThread = new LoadConfigThread(this.mThreadCom, this.mGlobals.getConfigParser(), this.mGlobals.getSelectedNetworkName(), this.mLogger, this.mWifi, this.mGlobals, this.mInConfig, this);
      this.activeThread.start();
      return;
    }
    this.activeThread.resume(this.mThreadCom, this.mGlobals, this);
    this.activeThread.unlock();
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    Log.d("XPC", "onActivityResult() : " + paramInt2);
    if (paramInt2 == 0)
    {
      done(0);
      return;
    }
    Log.d("XPC", "onActivityResult().");
    this.activeThread.unlock();
  }

  public void onBackPressed()
  {
    Util.log(this.mLogger, "User pressed back on the load config screen.");
    showMyDialog(LoadConfigFragments.newInstance(1000), "dialogPromptExit");
  }

  protected void onCreate(Bundle paramBundle)
  {
    ParcelHelper localParcelHelper = new ParcelHelper("", this);
    preContentView();
    setContentView(localParcelHelper.getLayoutId("xpc_loadingconfig"));
    super.onCreate(paramBundle);
    Log.d("XPC", "onCreate (LoadConfig)");
    this.spinner = ((ImageView)findViewById(this.mParcelHelper.getItemId("xpc_spinnerImage")));
    if (this.spinner != null)
    {
      this.spinnerAnim = ((AnimationDrawable)this.spinner.getDrawable());
      this.spinnerAnim.setCallback(this.spinner);
      this.spinnerAnim.setVisible(true, true);
      this.spinner.post(new Runnable()
      {
        public void run()
        {
          Log.d("XPC", "*** Starting animation!");
          LoadConfig.this.spinnerAnim.start();
        }
      });
    }
    if (this.errorstr != null)
      alertThenDie(this.titleId, this.errorstr, this.otherId);
    this.mShowAboutMenu = false;
    this.mShowLogsMenu = true;
    this.mShowHelpdeskMenu = false;
    this.mShowEmailLogsMenu = true;
    this.mShowStartOverMenu = false;
    this.mShowQuitMenu = true;
    this.mUsername = getIntent().getStringExtra("username");
    this.mPassword = getIntent().getStringExtra("password");
  }

  protected void onDestroy()
  {
    if (this.ad != null)
    {
      Util.log(this.mLogger, "Destroying alert dialog.");
      this.ad.dismiss();
      this.ad = null;
    }
    super.onDestroy();
  }

  protected void onPause()
  {
    Util.log(this.mLogger, "(LoadConfig) onPause()");
    if (this.activeThread != null)
      this.activeThread.spinLock();
    LoadConfigRetention localLoadConfigRetention = new LoadConfigRetention();
    localLoadConfigRetention.threadcom = this.mThreadCom;
    localLoadConfigRetention.thread = this.activeThread;
    localLoadConfigRetention.titleId = this.titleId;
    localLoadConfigRetention.alert = this.errorstr;
    localLoadConfigRetention.retryId = this.retId;
    localLoadConfigRetention.otherId = this.otherId;
    this.mGlobals.setSavedObject(localLoadConfigRetention);
    super.onPause();
  }

  protected void onResume()
  {
    Util.log(this.mLogger, "(LoadConfig) onResume()");
    if (this.activeThread != null)
    {
      this.activeThread.resume(this.mThreadCom, this.mGlobals, this);
      this.activeThread.unlock();
    }
    super.onResume();
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    if (this.mGlobals == null)
    {
      Util.log(this.mLogger, "Globals are null in LoadConfig!");
      return;
    }
    if (this.mGlobals.getConfigParser() == null)
      this.mGlobals.createConfigParser();
    super.onServiceBind(paramEncapsulationService);
    Util.log(this.mLogger, "+++ Showing LoadConfig.");
    try
    {
      localLoadConfigRetention = (LoadConfigRetention)this.mGlobals.getSavedObject();
      this.mGlobals.setSavedObject(null);
      if (localLoadConfigRetention != null)
      {
        this.mThreadCom = localLoadConfigRetention.threadcom;
        this.activeThread = localLoadConfigRetention.thread;
        this.errorstr = localLoadConfigRetention.alert;
        this.otherId = localLoadConfigRetention.otherId;
        this.retId = localLoadConfigRetention.retryId;
        this.titleId = localLoadConfigRetention.titleId;
      }
      while (true)
      {
        TextView localTextView = (TextView)findViewById(this.mParcelHelper.getItemId("xpc_configText"));
        localTextView.setText(this.mParcelHelper.getIdentifier("xpc_loading_config", "string"));
        this.mThreadCom.setTextView(localTextView);
        if (this.mThreadCom != null)
          this.mThreadCom.setCallbacks(this);
        if (haveTabs())
          setWelcomeActive();
        if ((this.activeThread == null) && (paramEncapsulationService.getRunningAsLibrary() == true))
          Util.log(this.mLogger, "Running as library.  Attempting to load config from resources.");
        try
        {
          this.mInConfig = readFile();
          if (this.mInConfig != null)
          {
            Util.log(this.mLogger, "!*!*!*!*!*!*!*!*! Got built in configuration!");
            startParser();
            return;
            this.mThreadCom = new ThreadCom();
          }
        }
        catch (IOException localIOException)
        {
          while (true)
          {
            localIOException.printStackTrace();
            continue;
            Util.log(this.mLogger, "***************** Built in configuration couldn't be retrieved.");
          }
        }
      }
    }
    catch (ClassCastException localClassCastException)
    {
      while (true)
        LoadConfigRetention localLoadConfigRetention = null;
    }
  }

  public void threadChangeTab(int paramInt)
  {
  }

  public void threadFailed(Boolean paramBoolean, int paramInt, String paramString)
  {
    this.activeThread = null;
    this.mFailure.setFailReason(FailureReason.useWarningIcon, paramString, 5);
    Util.log(this.mLogger, "Attempt to parse the configuration failed.");
    if (paramInt == 32)
      setResult(32);
    while (true)
    {
      Util.log(this.mLogger, "--- Leaving LoadConfig because thread failed.");
      return;
      if (paramInt == 34)
        done(34);
      else
        done(30);
    }
  }

  public void threadSuccess()
  {
    if ((this.mGlobals == null) || (this.mGlobals.getConfigParser() == null))
    {
      Util.log(this.mLogger, "--- Leaving LoadConfig because configuration parser is unavailable.");
      done(30);
      return;
    }
    Util.log(this.mLogger, "Setting selected config to '" + this.mGlobals.getConfigParser().getConfigName() + ".");
    this.mGlobals.setSelectedConfigName(this.mGlobals.getConfigParser().getConfigName());
    saveConfigToDatabase();
    this.activeThread = null;
    Util.log(this.mLogger, "--- Leaving LoadConfig after parsing configuration data. (Success)");
    done(1);
  }

  public static class LoadConfigFragments extends DialogFragment
  {
    public static LoadConfigFragments newInstance(int paramInt)
    {
      LoadConfigFragments localLoadConfigFragments = new LoadConfigFragments();
      Bundle localBundle = new Bundle();
      localBundle.putInt("id", paramInt);
      localLoadConfigFragments.setArguments(localBundle);
      return localLoadConfigFragments;
    }

    public Dialog onCreateDialog(Bundle paramBundle)
    {
      int i = getArguments().getInt("id");
      final ScreenBase localScreenBase = (ScreenBase)getActivity();
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
      if (i == 1000)
      {
        localBuilder.setMessage(localScreenBase.mParcelHelper.getIdentifier("xpc_exit_prompt", "string")).setCancelable(false).setPositiveButton(localScreenBase.mParcelHelper.getIdentifier("xpc_yes", "string"), new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
          {
            if (((LoadConfig)localScreenBase).activeThread != null)
              ((LoadConfig)localScreenBase).activeThread.die();
            Util.log(localScreenBase.mLogger, "--- Leaving LoadConfig because user pressed back.");
            localScreenBase.done(-2);
          }
        }).setNegativeButton(localScreenBase.mParcelHelper.getIdentifier("xpc_no", "string"), new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
          {
            paramAnonymousDialogInterface.cancel();
          }
        });
        return localBuilder.create();
      }
      return null;
    }
  }
}