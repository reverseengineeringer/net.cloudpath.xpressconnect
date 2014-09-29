package net.cloudpath.xpressconnect.screens;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.GestureCallback;
import net.cloudpath.xpressconnect.MapVariables;
import net.cloudpath.xpressconnect.SendEmail;
import net.cloudpath.xpressconnect.Testing;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.localservice.ServiceBoundCallback;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkElement;
import net.cloudpath.xpressconnect.thread.ResponseThreadComCallbacks;
import net.cloudpath.xpressconnect.view.TabTextView;

@SuppressLint({"NewApi"})
public class ScreenBase extends FragmentActivity
  implements ServiceBoundCallback, GestureOverlayView.OnGesturePerformedListener, ResponseThreadComCallbacks
{
  private static final int ABOUT_DIALOG = 1;
  protected static final int BLANK_ICON = 0;
  protected static final int CHECK_ICON = 2;
  protected static final int DIALOG_PLEASE_WAIT = 200;
  protected static final String DIALOG_PLEASE_WAIT_TAG = "pleaseWaitDialogTag";
  public static final int DIALOG_UNLOCK_KEYSTORE = 100;
  public static final String DIALOG_UNLOCK_KEYSTORE_TAG = "unlockKeystoreDialog";
  private static final int EMAIL_LOG = 4;
  protected static final int EMAIL_SENT = 2222;
  protected static final int POINTER_ICON = 1;
  protected static final int QUIT = 6;
  private static final int SHOW_HELPDESK = 3;
  private static final int SHOW_LOG = 2;
  private static final int START_OVER = 5;
  protected static final int TAB_COLOR_DARK = -10656136;
  protected static final int TAB_COLOR_LIGHT = -1;
  protected static final int UNLOCK_KEYSTORE = 1001;
  protected ViewGroup backgroundLayout = null;
  protected ImageView bottomSeperator = null;
  protected ImageView brandingImg = null;
  protected GestureOverlayView gestures = null;
  protected TextView licensedToText = null;
  protected TextView licensee = null;
  protected TabTextView mAuthenticate = null;
  protected TabTextView mConfigure = null;
  protected TabTextView mConnect = null;
  protected TabTextView mConnected = null;
  protected FailureReason mFailure = null;
  public boolean mFinishCalled = false;
  protected GestureCallback mGestureCallback = null;
  public GetGlobals mGlobals = null;
  public int mGoingToScreen = -1;
  private volatile boolean mInstanceSaved = false;
  protected GestureLibrary mLibrary = null;
  protected Logger mLogger = null;
  public ParcelHelper mParcelHelper = null;
  protected NetworkConfigParser mParser = null;
  public boolean mQuitCalled = false;
  public ResponseThreadComCallbacks mResponseThreadCallbacks = null;
  public int mResultCode = -1;
  protected boolean mServiceBound = false;
  public boolean mShowAboutMenu = true;
  public boolean mShowEmailLogsMenu = true;
  public boolean mShowHelpdeskMenu = true;
  public boolean mShowLogsMenu = true;
  public boolean mShowNoParentMenu = false;
  public boolean mShowQuitMenu = true;
  public boolean mShowStartOverMenu = true;
  protected TabTextView mValidate = null;
  protected TabTextView mWelcome = null;
  public WifiManager mWifi = null;
  protected Activity me = null;
  protected TextView networkTitle = null;
  protected ImageView topSeperator = null;
  protected ViewGroup workareaLayout = null;

  private void setIcon(int paramInt, TextView paramTextView)
  {
    switch (paramInt)
    {
    default:
      Drawable localDrawable3 = getResources().getDrawable(this.mParcelHelper.getDrawableId("xpc_blank"));
      localDrawable3.setBounds(0, 0, 20, 20);
      paramTextView.setCompoundDrawables(localDrawable3, null, null, null);
      return;
    case 1:
      Drawable localDrawable2 = getResources().getDrawable(this.mParcelHelper.getDrawableId("xpc_applicationtabarrow"));
      localDrawable2.setBounds(0, 0, 20, 20);
      paramTextView.setCompoundDrawables(localDrawable2, null, null, null);
      return;
    case 2:
    }
    Drawable localDrawable1 = getResources().getDrawable(this.mParcelHelper.getDrawableId("xpc_applicationtabcheck"));
    localDrawable1.setBounds(0, 0, 20, 20);
    paramTextView.setCompoundDrawables(localDrawable1, null, null, null);
  }

  private void setTabActive(TabTextView paramTabTextView)
  {
    if (this.mGlobals.getActiveTabFontColor() != -1)
    {
      paramTabTextView.setTextColor(-16777216 + this.mGlobals.getActiveTabFontColor());
      paramTabTextView.setBackgroundColor(-1);
      if (this.mGlobals.getActiveTabBorderColor() == -1)
        break label85;
      paramTabTextView.setBorderColor(-16777216 + this.mGlobals.getActiveTabBorderColor());
    }
    while (true)
    {
      paramTabTextView.invalidate();
      return;
      paramTabTextView.setTextColor(getResources().getColorStateList(this.mParcelHelper.getIdentifier("xpc_active_tab_font_color", "color")));
      break;
      label85: paramTabTextView.setBorderColor(-10656136);
    }
  }

  private void setTabInactive(TabTextView paramTabTextView)
  {
    if (this.mGlobals.getInactiveTabFontColor() != -1)
    {
      paramTabTextView.setTextColor(-16777216 + this.mGlobals.getInactiveTabFontColor());
      paramTabTextView.setBackgroundColor(-10656136);
      if (this.mGlobals.getInactiveTabBorderColor() == -1)
        break label87;
      paramTabTextView.setBorderColor(-16777216 + this.mGlobals.getInactiveTabBorderColor());
    }
    while (true)
    {
      paramTabTextView.invalidate();
      return;
      paramTabTextView.setTextColor(getResources().getColorStateList(this.mParcelHelper.getIdentifier("xpc_inactive_tab_font_color", "color")));
      break;
      label87: paramTabTextView.setBorderColor(-1);
    }
  }

  private void setTabText()
  {
    if (this.mParser == null);
    String str6;
    do
    {
      return;
      String str1 = this.mParser.getOptionById(300);
      if ((str1 != null) && (str1.length() > 0))
        this.mWelcome.setText(str1);
      String str2 = this.mParser.getOptionById(302);
      if ((str2 != null) && (str2.length() > 0))
        this.mConfigure.setText(str2);
      String str3 = this.mParser.getOptionById(305);
      if ((str3 != null) && (str3.length() > 0))
        this.mConnect.setText(str3);
      String str4 = this.mParser.getOptionById(306);
      if ((str4 != null) && (str4.length() > 0))
        this.mAuthenticate.setText(str4);
      String str5 = this.mParser.getOptionById(307);
      if ((str5 != null) && (str5.length() > 0))
        this.mValidate.setText(str5);
      str6 = this.mParser.getOptionById(308);
    }
    while ((str6 == null) || (str6.length() <= 0));
    this.mConnected.setText(str6);
  }

  protected void alertThenDie(int paramInt1, String paramString, int paramInt2)
  {
    AlertDialog localAlertDialog = new AlertDialog.Builder(this).create();
    if (localAlertDialog == null)
    {
      done(0);
      return;
    }
    String str1 = getString(paramInt1);
    if (str1 == null)
      str1 = new String("Alert!");
    localAlertDialog.setTitle(str1);
    localAlertDialog.setMessage(paramString);
    String str2 = getString(paramInt2);
    if (str2 == null)
      str2 = new String("Okay");
    localAlertDialog.setButton(-2, str2, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        ScreenBase.this.quit();
      }
    });
    localAlertDialog.show();
  }

  public void dialogComplete(int paramInt, boolean paramBoolean)
  {
  }

  public void done(int paramInt)
  {
    setMyResult(paramInt);
    if (!Testing.areTesting)
    {
      finish();
      overridePendingTransition(0, 0);
      return;
    }
    this.mFinishCalled = true;
  }

  public void done(int paramInt, Intent paramIntent)
  {
    setMyResult(paramInt, paramIntent);
    if (!Testing.areTesting)
    {
      finish();
      overridePendingTransition(0, 0);
      return;
    }
    this.mFinishCalled = true;
  }

  public void emailLog()
  {
    Util.log(this.mLogger, "Starting popup and log builder thread.");
    new WaitingDialog(null).execute(new Void[0]);
  }

  public void finish()
  {
    System.out.println("Finish called for : " + getLocalClassName());
    this.mFinishCalled = true;
    super.finish();
    overridePendingTransition(0, 0);
  }

  public boolean haveTabs()
  {
    return this.mWelcome != null;
  }

  public void intentComplete(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (this.mResponseThreadCallbacks != null)
      this.mResponseThreadCallbacks.intentComplete(paramInt1, paramInt2, paramIntent);
  }

  public boolean isServiceBound()
  {
    return this.mServiceBound;
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if ((paramInt1 == 0) && (paramInt2 == 0));
    do
    {
      return;
      if (paramInt2 == 29)
      {
        quit();
        return;
      }
      if (paramInt1 != 2222)
        break;
      Util.log(this.mLogger, "E-mail program returned.");
    }
    while (new File(Environment.getExternalStorageDirectory(), "xpc_to_send.log").delete());
    Util.log(this.mLogger, "Unable to delete sent log file!");
    return;
    Util.log(this.mLogger, "Request code : " + paramInt1 + "    Result Code : " + paramInt2);
    intentComplete(paramInt1, paramInt2, paramIntent);
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  public void onBackPressed()
  {
    Util.log(this.mLogger, "Back pressed...");
    super.onBackPressed();
  }

  protected void onCreate(Bundle paramBundle)
  {
    this.mInstanceSaved = false;
    this.me = this;
    this.mParcelHelper = new ParcelHelper("", this);
    this.mWelcome = ((TabTextView)findViewById(this.mParcelHelper.getItemId("xpc_welcomeText")));
    this.mConfigure = ((TabTextView)findViewById(this.mParcelHelper.getItemId("xpc_configureText")));
    this.mConnect = ((TabTextView)findViewById(this.mParcelHelper.getItemId("xpc_connectText")));
    this.mAuthenticate = ((TabTextView)findViewById(this.mParcelHelper.getItemId("xpc_authenticateText")));
    this.mValidate = ((TabTextView)findViewById(this.mParcelHelper.getItemId("xpc_validateText")));
    this.mConnected = ((TabTextView)findViewById(this.mParcelHelper.getItemId("xpc_connectedText")));
    setTitle(getResources().getString(this.mParcelHelper.getIdentifier("xpc_app_name", "string")) + "  (" + Build.MODEL + ")");
    this.backgroundLayout = ((ViewGroup)findViewById(this.mParcelHelper.getItemId("xpc_BackgroundLayout")));
    this.workareaLayout = ((ViewGroup)findViewById(this.mParcelHelper.getItemId("xpc_rightColumnInner")));
    this.networkTitle = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_networkWelcomeText")));
    this.brandingImg = ((ImageView)findViewById(this.mParcelHelper.getItemId("xpc_BrandingImg")));
    this.licensee = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_licensee")));
    this.licensedToText = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_licensedToText")));
    this.topSeperator = ((ImageView)findViewById(this.mParcelHelper.getItemId("xpc_topSeperator")));
    this.bottomSeperator = ((ImageView)findViewById(this.mParcelHelper.getItemId("xpc_bottomSeperator")));
    if (this.licensee != null)
      this.licensee.setVisibility(4);
    if (this.licensedToText != null)
      this.licensedToText.setVisibility(4);
    this.mGlobals = new GetGlobals(this);
    if (this.mGlobals != null)
    {
      this.mGlobals.setCallbacks(this);
      this.mGlobals.bindService();
    }
    if (haveTabs())
      setWelcomeActive();
    this.mServiceBound = false;
    super.onCreate(paramBundle);
    this.mLibrary = GestureLibraries.fromRawResource(this, this.mParcelHelper.getIdentifier("gestures", "raw"));
    if (this.mLibrary.load())
    {
      this.gestures = ((GestureOverlayView)findViewById(this.mParcelHelper.getItemId("xpc_gestures")));
      if (this.gestures != null)
        this.gestures.addOnGesturePerformedListener(this);
    }
    while (true)
    {
      Log.d("XPC", "++++ Startup complete.");
      return;
      this.gestures = null;
      Util.log(this.mLogger, "Couldn't load gesture library.  Gestures will be disabled.");
    }
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    if ((paramMenu == null) || (this.mShowNoParentMenu == true))
      return super.onCreateOptionsMenu(paramMenu);
    if ((this.mShowAboutMenu) && (!this.mGlobals.getRunningAsLibrary()))
      paramMenu.add(0, 1, 0, this.mParcelHelper.getIdentifier("xpc_about", "string"));
    if (this.mShowLogsMenu)
      paramMenu.add(0, 2, 0, this.mParcelHelper.getIdentifier("xpc_show_log_title", "string"));
    if (this.mShowHelpdeskMenu)
      paramMenu.add(0, 3, 0, this.mParcelHelper.getIdentifier("xpc_show_helpdesk", "string"));
    if (this.mShowEmailLogsMenu)
      paramMenu.add(0, 4, 0, this.mParcelHelper.getIdentifier("xpc_email_log", "string"));
    if (this.mShowStartOverMenu)
      paramMenu.add(0, 5, 0, this.mParcelHelper.getIdentifier("xpc_start_over", "string"));
    if (this.mShowQuitMenu)
      paramMenu.add(0, 6, 0, this.mParcelHelper.getIdentifier("xpc_quit", "string"));
    return super.onCreateOptionsMenu(paramMenu);
  }

  protected void onDestroy()
  {
    if (this.mGlobals != null)
      this.mGlobals.unbindService();
    super.onDestroy();
  }

  public void onGesturePerformed(GestureOverlayView paramGestureOverlayView, Gesture paramGesture)
  {
    if ((paramGestureOverlayView == null) || (paramGesture == null))
      Util.log(this.mLogger, "Parameters passed in are null in onGesturePerformed().");
    while (true)
    {
      return;
      ArrayList localArrayList = this.mLibrary.recognize(paramGesture);
      if ((localArrayList == null) || (localArrayList.get(0) == null))
      {
        Util.log(this.mLogger, "Invalid prediction data!");
        return;
      }
      if ((localArrayList.size() > 0) && (((Prediction)localArrayList.get(0)).score > 3.0D))
      {
        String str = ((Prediction)localArrayList.get(0)).name;
        if (Build.VERSION.SDK_INT < 14)
          Toast.makeText(this, "NFC writing is not available on this OS version.", 1).show();
        while ((str.equalsIgnoreCase("Dump Database")) && (this.mGestureCallback != null))
        {
          this.mGestureCallback.handleDumpDatabase();
          return;
          if ((str.equalsIgnoreCase("Start NFC programmer")) && (this.mGestureCallback != null))
            this.mGestureCallback.handleNfcProgrammerGesture();
        }
      }
    }
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    if (paramMenuItem == null)
      return super.onOptionsItemSelected(paramMenuItem);
    switch (paramMenuItem.getItemId())
    {
    default:
      return super.onOptionsItemSelected(paramMenuItem);
    case 1:
      Util.log(this.mLogger, "About dialog selected...");
      startActivity(new Intent(this, AboutScreen.class));
      overridePendingTransition(0, 0);
      return true;
    case 2:
      Util.log(this.mLogger, "Show log selected...");
      startActivity(new Intent(this, ShowLogs.class));
      overridePendingTransition(0, 0);
      return true;
    case 3:
      Util.log(this.mLogger, "Show helpdesk selected...");
      startActivity(new Intent(this, ShowHelpdesk.class));
      overridePendingTransition(0, 0);
      return true;
    case 4:
      Util.log(this.mLogger, "Emailing log...");
      emailLog();
      return true;
    case 5:
      Util.log(this.mLogger, "Starting over...");
      done(28);
      return true;
    case 6:
    }
    Util.log(this.mLogger, "Quitting...");
    quit();
    return true;
  }

  protected void onSaveInstanceState(Bundle paramBundle)
  {
    this.mInstanceSaved = true;
    super.onSaveInstanceState(paramBundle);
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    if (paramEncapsulationService == null)
      Log.d("XPC", "mBound is null!");
    while (true)
    {
      return;
      this.mServiceBound = true;
      this.mLogger = paramEncapsulationService.getLogger();
      if (this.mLogger == null)
        Util.log(this.mLogger, "Logger was not properly bound!");
      this.mParser = paramEncapsulationService.getConfigParser();
      if (this.mParser != null)
      {
        if (this.licensee != null)
          this.licensee.setVisibility(0);
        if (this.licensedToText != null)
          this.licensedToText.setVisibility(0);
      }
      this.mWifi = paramEncapsulationService.getWifiManager();
      if (haveTabs())
        setTabText();
      this.mFailure = paramEncapsulationService.getFailureReason();
      if ((this.backgroundLayout != null) && (paramEncapsulationService.getBackgroundColor() != -1))
        this.backgroundLayout.setBackgroundColor(-16777216 + paramEncapsulationService.getBackgroundColor());
      if ((this.topSeperator != null) && (paramEncapsulationService.getLineColor() != -1))
        this.topSeperator.setBackgroundColor(-16777216 + paramEncapsulationService.getLineColor());
      if ((this.bottomSeperator != null) && (paramEncapsulationService.getLineColor() != -1))
        this.bottomSeperator.setBackgroundColor(-16777216 + paramEncapsulationService.getLineColor());
      if ((this.workareaLayout != null) && (paramEncapsulationService.getOutlineColor() != -1))
      {
        GradientDrawable localGradientDrawable = (GradientDrawable)getResources().getDrawable(this.mParcelHelper.getDrawableId("xpc_roundrect"));
        localGradientDrawable.setStroke(2, -16777216 + paramEncapsulationService.getOutlineColor());
        if (paramEncapsulationService.getCornerStyle() == 0)
        {
          localGradientDrawable.setCornerRadius(0.0F);
          Util.log(this.mLogger, "Using square corners.");
        }
        this.workareaLayout.setBackgroundDrawable(localGradientDrawable);
      }
      if (this.licensee != null)
      {
        if ((this.mParser != null) && (this.mParser.networks != null) && (this.mParser.networks.licensee != null))
          break label444;
        this.licensee.setText("UNKNOWN");
        this.licensee.setTextColor(getResources().getColorStateList(this.mParcelHelper.getIdentifier("xpc_red", "color")));
      }
      while (true)
      {
        this.licensee.invalidate();
        if ((this.brandingImg != null) && (paramEncapsulationService.getBrandingImg() != null))
        {
          this.brandingImg.invalidate();
          this.brandingImg.setImageDrawable(new BitmapDrawable(getResources(), paramEncapsulationService.getBrandingImg()));
        }
        if (this.networkTitle == null)
          break;
        if (this.mParser == null)
          break label532;
        if (this.mParser.networks != null)
          break label498;
        Util.log(this.mLogger, "Networks data is null!?");
        return;
        label444: this.licensee.setText(this.mParser.networks.licensee);
        if (paramEncapsulationService.getLicensedToFontColor() != -1)
          this.licensee.setTextColor(-16777216 + paramEncapsulationService.getLicensedToFontColor());
        else
          this.licensee.setTextColor(-16777216);
      }
      label498: if (this.mParser.networks.welcome_title == null)
      {
        Util.log(this.mLogger, "There doesn't appear to be a title to set.  Clearing it.");
        this.networkTitle.setText("");
        return;
      }
      label532: NetworkConfigParser localNetworkConfigParser = this.mParser;
      Object localObject = null;
      if (localNetworkConfigParser != null);
      try
      {
        MapVariables localMapVariables = new MapVariables(this.mParser, this.mLogger);
        localObject = localMapVariables;
        if (localObject == null)
          if ((this.mParser != null) && (this.mParser.networks != null) && (this.mParser.networks.welcome_title != null))
          {
            this.networkTitle.setText(this.mParser.networks.welcome_title);
            if ((paramEncapsulationService.getLicensedToFontColor() == -1) || (haveTabs()))
              continue;
            this.networkTitle.setTextColor(-16777216 + paramEncapsulationService.getLicensedToFontColor());
            return;
          }
      }
      catch (Exception localException)
      {
        while (true)
        {
          localException.printStackTrace();
          localObject = null;
          continue;
          this.networkTitle.setText("");
          continue;
          this.networkTitle.setText(localObject.varMap(this.mParser.networks.welcome_title, false, null));
        }
      }
    }
  }

  public void onServiceUnbind()
  {
    this.mServiceBound = false;
  }

  protected void preContentView()
  {
    if (Build.VERSION.SDK_INT >= 11)
      requestWindowFeature(8);
  }

  public void quit()
  {
    Util.log(this.mLogger, ".... Quit called ....");
    Util.log(this.mLogger, Util.getStackTrace(new Exception()));
    this.mQuitCalled = true;
    done(29);
  }

  protected void rebind()
  {
    if (this.mGlobals == null)
      Util.log(this.mLogger, "Globals weren't bound in rebind attempt!");
    do
    {
      return;
      if (this.mLogger == null)
        this.mLogger = this.mGlobals.getLogger();
      if (this.mParser == null)
        this.mParser = this.mGlobals.getConfigParser();
      if (this.mFailure == null)
        this.mFailure = this.mGlobals.getFailureReason();
    }
    while ((this.brandingImg == null) || (this.mGlobals.getBrandingImg() == null));
    this.brandingImg.invalidate();
    this.brandingImg.setImageDrawable(new BitmapDrawable(getResources(), this.mGlobals.getBrandingImg()));
  }

  protected void removeMyDialog(String paramString)
  {
    if (this.mInstanceSaved)
    {
      Util.log(this.mLogger, "Instance has already been saved.  Won't attempt to remove fragment.");
      return;
    }
    DialogFragment localDialogFragment = (DialogFragment)getSupportFragmentManager().findFragmentByTag(paramString);
    if (localDialogFragment != null)
    {
      localDialogFragment.dismiss();
      return;
    }
    Util.log(this.mLogger, "Couldn't locate dialog for tag '" + paramString + "'.");
  }

  public void setAuthenticateActive()
  {
    setTabInactive(this.mWelcome);
    setIcon(2, this.mWelcome);
    setTabInactive(this.mConfigure);
    setIcon(2, this.mConfigure);
    setTabInactive(this.mConnect);
    setIcon(2, this.mConnect);
    setTabActive(this.mAuthenticate);
    setIcon(1, this.mAuthenticate);
    setTabInactive(this.mValidate);
    setIcon(0, this.mValidate);
    setTabInactive(this.mConnected);
    setIcon(0, this.mConnected);
  }

  public void setConfigureActive()
  {
    setTabInactive(this.mWelcome);
    setIcon(2, this.mWelcome);
    setTabActive(this.mConfigure);
    setIcon(1, this.mConfigure);
    setTabInactive(this.mConnect);
    setIcon(0, this.mConnect);
    setTabInactive(this.mAuthenticate);
    setIcon(0, this.mAuthenticate);
    setTabInactive(this.mValidate);
    setIcon(0, this.mValidate);
    setTabInactive(this.mConnected);
    setIcon(0, this.mConnected);
  }

  public void setConnectActive()
  {
    setTabInactive(this.mWelcome);
    setIcon(2, this.mWelcome);
    setTabInactive(this.mConfigure);
    setIcon(2, this.mConfigure);
    setTabActive(this.mConnect);
    setIcon(1, this.mConnect);
    setTabInactive(this.mAuthenticate);
    setIcon(0, this.mAuthenticate);
    setTabInactive(this.mValidate);
    setIcon(0, this.mValidate);
    setTabInactive(this.mConnected);
    setIcon(0, this.mConnected);
  }

  public void setConnectedActive()
  {
    setTabInactive(this.mWelcome);
    setIcon(2, this.mWelcome);
    setTabInactive(this.mConfigure);
    setIcon(2, this.mConfigure);
    setTabInactive(this.mConnect);
    setIcon(2, this.mConnect);
    setTabInactive(this.mAuthenticate);
    setIcon(2, this.mAuthenticate);
    setTabInactive(this.mValidate);
    setIcon(2, this.mValidate);
    setTabActive(this.mConnected);
    setIcon(1, this.mConnected);
  }

  public void setGestureCallback(GestureCallback paramGestureCallback)
  {
    this.mGestureCallback = paramGestureCallback;
  }

  public void setMyResult(int paramInt)
  {
    this.mResultCode = paramInt;
    setResult(paramInt);
  }

  public void setMyResult(int paramInt, Intent paramIntent)
  {
    this.mResultCode = paramInt;
    setResult(paramInt, paramIntent);
  }

  public void setResponseCallbacks(ResponseThreadComCallbacks paramResponseThreadComCallbacks)
  {
    Util.log(this.mLogger, "Thread callbacks set.");
    this.mResponseThreadCallbacks = paramResponseThreadComCallbacks;
  }

  public void setValidateActive()
  {
    setTabInactive(this.mWelcome);
    setIcon(2, this.mWelcome);
    setTabInactive(this.mConfigure);
    setIcon(2, this.mConfigure);
    setTabInactive(this.mConnect);
    setIcon(2, this.mConnect);
    setTabInactive(this.mAuthenticate);
    setIcon(2, this.mAuthenticate);
    setTabActive(this.mValidate);
    setIcon(1, this.mValidate);
    setTabInactive(this.mConnected);
    setIcon(0, this.mConnected);
  }

  public void setWelcomeActive()
  {
    setTabActive(this.mWelcome);
    setIcon(1, this.mWelcome);
    setTabInactive(this.mConfigure);
    setIcon(0, this.mConfigure);
    setTabInactive(this.mConnect);
    setIcon(0, this.mConnect);
    setTabInactive(this.mAuthenticate);
    setIcon(0, this.mAuthenticate);
    setTabInactive(this.mValidate);
    setIcon(0, this.mValidate);
    setTabInactive(this.mConnected);
    setIcon(0, this.mConnected);
  }

  public void showLongToast(String paramString)
  {
    Toast.makeText(this, paramString, 0).show();
  }

  public void showMyDialog(int paramInt)
  {
    switch (paramInt)
    {
    default:
      Util.log(this.mLogger, "Unknown dialog " + paramInt);
      return;
    case 100:
    case 200:
    }
    for (String str = "unlockKeystoreDialog"; ; str = "pleaseWaitDialogTag")
    {
      showMyDialog(ScreenBaseFragments.newInstance(paramInt), str);
      return;
    }
  }

  public void showMyDialog(DialogFragment paramDialogFragment, String paramString)
  {
    if ((DialogFragment)getSupportFragmentManager().findFragmentByTag(paramString) != null)
    {
      Util.log(this.mLogger, "*** Dialog fragment already exists.  Won't show again.");
      return;
    }
    try
    {
      paramDialogFragment.show(getSupportFragmentManager(), paramString);
      return;
    }
    catch (IllegalStateException localIllegalStateException)
    {
      Util.log(this.mLogger, "Illegal state exception while trying to create a dialog.  The activity was probably in the process of closing.");
    }
  }

  public void showShortToast(String paramString)
  {
    Toast.makeText(this, paramString, 0).show();
  }

  public void transitionTo(int paramInt)
  {
    this.mGoingToScreen = paramInt;
    if (!Testing.areTesting)
      overridePendingTransition(0, 0);
  }

  public static class ScreenBaseFragments extends DialogFragment
  {
    public static ScreenBaseFragments newInstance(int paramInt)
    {
      ScreenBaseFragments localScreenBaseFragments = new ScreenBaseFragments();
      Bundle localBundle = new Bundle();
      localBundle.putInt("id", paramInt);
      localScreenBaseFragments.setArguments(localBundle);
      return localScreenBaseFragments;
    }

    public Dialog onCreateDialog(Bundle paramBundle)
    {
      int i = getArguments().getInt("id");
      final ScreenBase localScreenBase = (ScreenBase)getActivity();
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
      if (i == 200)
      {
        ProgressDialog localProgressDialog = new ProgressDialog(localScreenBase);
        localProgressDialog.setMessage(localScreenBase.getResources().getString(localScreenBase.mParcelHelper.getIdentifier("xpc_please_wait", "string")));
        localProgressDialog.setCancelable(false);
        return localProgressDialog;
      }
      if (i == 100)
      {
        localBuilder.setMessage(localScreenBase.mParcelHelper.getIdentifier("xpc_unlock_keystore_only", "string"));
        localBuilder.setNeutralButton("Ok", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
          {
            try
            {
              Intent localIntent = new Intent("android.credentials.UNLOCK");
              ScreenBase.ScreenBaseFragments.this.startActivityForResult(localIntent, 1001);
              return;
            }
            catch (Exception localException)
            {
              do
                Util.log(localScreenBase.mLogger, "Error attempting to unlock the keystore.");
              while ((localScreenBase.mParser != null) && (localScreenBase.mParser.savedConfigInfo != null));
              Util.log(localScreenBase.mLogger, "No saved config info available.  Skipping.");
            }
          }
        });
        localBuilder.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
          public void onCancel(DialogInterface paramAnonymousDialogInterface)
          {
            Util.log(localScreenBase.mLogger, "User hit the back button.");
            localScreenBase.done(-2);
          }
        });
        return localBuilder.create();
      }
      return null;
    }
  }

  private class WaitingDialog extends AsyncTask<Void, Void, Void>
  {
    String logdata = null;

    private WaitingDialog()
    {
    }

    // ERROR //
    private String oldLog()
    {
      // Byte code:
      //   0: new 29	java/lang/StringBuilder
      //   3: dup
      //   4: invokespecial 30	java/lang/StringBuilder:<init>	()V
      //   7: astore_1
      //   8: new 32	java/io/BufferedReader
      //   11: dup
      //   12: new 34	java/io/FileReader
      //   15: dup
      //   16: new 29	java/lang/StringBuilder
      //   19: dup
      //   20: invokespecial 30	java/lang/StringBuilder:<init>	()V
      //   23: invokestatic 40	android/os/Environment:getExternalStorageDirectory	()Ljava/io/File;
      //   26: invokevirtual 44	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
      //   29: ldc 46
      //   31: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   34: invokevirtual 52	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   37: invokespecial 55	java/io/FileReader:<init>	(Ljava/lang/String;)V
      //   40: invokespecial 58	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
      //   43: astore_2
      //   44: aload_2
      //   45: invokevirtual 61	java/io/BufferedReader:readLine	()Ljava/lang/String;
      //   48: astore 7
      //   50: aload 7
      //   52: ifnull +82 -> 134
      //   55: aload_1
      //   56: new 29	java/lang/StringBuilder
      //   59: dup
      //   60: invokespecial 30	java/lang/StringBuilder:<init>	()V
      //   63: aload 7
      //   65: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   68: ldc 63
      //   70: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   73: invokevirtual 52	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   76: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   79: pop
      //   80: aload_2
      //   81: invokevirtual 61	java/io/BufferedReader:readLine	()Ljava/lang/String;
      //   84: astore 9
      //   86: aload 9
      //   88: astore 7
      //   90: goto -40 -> 50
      //   93: astore 10
      //   95: aload_0
      //   96: getfield 13	net/cloudpath/xpressconnect/screens/ScreenBase$WaitingDialog:this$0	Lnet/cloudpath/xpressconnect/screens/ScreenBase;
      //   99: getfield 69	net/cloudpath/xpressconnect/screens/ScreenBase:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
      //   102: ldc 71
      //   104: invokestatic 77	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
      //   107: aload 10
      //   109: invokevirtual 80	java/io/FileNotFoundException:printStackTrace	()V
      //   112: aconst_null
      //   113: areturn
      //   114: astore_3
      //   115: ldc 82
      //   117: ldc 84
      //   119: invokestatic 90	android/util/Log:i	(Ljava/lang/String;Ljava/lang/String;)I
      //   122: pop
      //   123: aload_1
      //   124: ldc 92
      //   126: invokevirtual 49	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   129: pop
      //   130: aload_3
      //   131: invokevirtual 93	java/io/IOException:printStackTrace	()V
      //   134: aload_2
      //   135: invokevirtual 96	java/io/BufferedReader:close	()V
      //   138: aload_1
      //   139: invokevirtual 52	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   142: areturn
      //   143: astore 6
      //   145: aload 6
      //   147: invokevirtual 93	java/io/IOException:printStackTrace	()V
      //   150: goto -12 -> 138
      //
      // Exception table:
      //   from	to	target	type
      //   8	44	93	java/io/FileNotFoundException
      //   44	50	114	java/io/IOException
      //   55	86	114	java/io/IOException
      //   134	138	143	java/io/IOException
    }

    protected Void doInBackground(Void[] paramArrayOfVoid)
    {
      Thread.currentThread().setName("Log cycle task");
      StringBuilder localStringBuilder = new StringBuilder();
      if (ScreenBase.this.mLogger != null)
        localStringBuilder.append(ScreenBase.this.mLogger.getViewableLines());
      localStringBuilder.append("\n\n >>>>>>  Previous Log <<<<<<\n\n");
      localStringBuilder.append(oldLog());
      this.logdata = localStringBuilder.toString();
      return null;
    }

    protected void onCancelled()
    {
      ScreenBase.this.removeMyDialog("pleaseWaitDialogTag");
      super.onCancelled();
    }

    protected void onPostExecute(Void paramVoid)
    {
      ScreenBase.this.removeMyDialog("pleaseWaitDialogTag");
      NetworkConfigParser localNetworkConfigParser = ScreenBase.this.mParser;
      String str = null;
      if (localNetworkConfigParser != null)
      {
        NetworkElement localNetworkElement = ScreenBase.this.mParser.networks;
        str = null;
        if (localNetworkElement != null)
          str = ScreenBase.this.mParser.networks.licensee;
      }
      Intent localIntent = SendEmail.sendLog(str, this.logdata);
      ScreenBase.this.startActivityForResult(Intent.createChooser(localIntent, "Send log via e-mail..."), 2222);
      ScreenBase.this.overridePendingTransition(0, 0);
      super.onPostExecute(paramVoid);
    }

    protected void onPreExecute()
    {
      try
      {
        ScreenBase.this.showMyDialog(ScreenBase.ScreenBaseFragments.newInstance(200), "pleaseWaitDialogTag");
        super.onPreExecute();
        return;
      }
      catch (IllegalStateException localIllegalStateException)
      {
        while (true)
          Util.log(ScreenBase.this.mLogger, "Caught illegal state exception.  Won't show dialog.");
      }
    }
  }
}