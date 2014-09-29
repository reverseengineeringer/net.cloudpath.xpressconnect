package net.cloudpath.xpressconnect.screens;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.io.File;
import java.io.PrintStream;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.Testing;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.ProfileElement;
import net.cloudpath.xpressconnect.remote.WebInterface;
import net.cloudpath.xpressconnect.screens.delegates.MustInstallScreenDelegate;

public class MustInstallScreen extends ScreenBase
  implements View.OnClickListener
{
  private static final int PLEASE_WAIT = 1001;
  private static final int PLEASE_WAIT_DOWNLOADING = 1000;
  private static final String PLEASE_WAIT_DOWNLOADING_TAG = "pleaseWaitDownloading";
  private static final String PLEASE_WAIT_TAG = "pleaseWait";
  private InitialCheckAppState mChecking = null;
  private MustInstallScreenDelegate mDelegate = null;
  private Button mInstallButton = null;
  private TextView mMessageText = null;
  private Button mSkipButton = null;
  private TextView mSoftwareDescription = null;

  private void allInstalled()
  {
    done(38);
  }

  private void skip()
  {
    done(37);
  }

  public void applySettings()
  {
    this.mMessageText.setText(getResources().getString(this.mParcelHelper.getIdentifier("xpc_additional_software", "string")));
    this.mSoftwareDescription.setText(this.mDelegate.getDescription());
    if (this.mDelegate.getRequired())
      this.mSkipButton.setVisibility(4);
    while (true)
    {
      this.mSkipButton.setEnabled(true);
      this.mInstallButton.setEnabled(true);
      return;
      this.mSkipButton.setVisibility(0);
    }
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    System.out.println("resultCode = " + paramInt2);
    if (29 == paramInt2)
    {
      done(29);
      return;
    }
    new ContinueCheckAppState(null).execute(new Void[0]);
  }

  public void onBackPressed()
  {
    if (this.mDelegate.getRequired())
    {
      this.mFailure.setFailReason(FailureReason.useWarningIcon, getApplicationContext().getString(this.mParcelHelper.getIdentifier("xpc_install_failure", "string")), 12);
      done(15);
      return;
    }
    skip();
  }

  public void onClick(View paramView)
  {
    if (paramView == this.mInstallButton)
      if ((this.mDelegate.getDownloadLink() != null) && (!this.mDelegate.getDownloadLink().contentEquals("")))
        if (Testing.areTesting)
          Testing.testhit();
    while (paramView != this.mSkipButton)
    {
      return;
      if (this.mDelegate.getDownloadLink().startsWith("market://"))
      {
        startActivityForResult(new Intent("android.intent.action.VIEW").setData(Uri.parse(this.mDelegate.getDownloadLink())), 0);
        return;
      }
      showMyDialog(MustInstallFragments.newInstance(1000), "pleaseWaitDownloading");
      if (!Util.isStorageReady(this.mLogger, this.mFailure))
      {
        quit();
        return;
      }
      DownloadApp localDownloadApp = new DownloadApp(null);
      String[] arrayOfString = new String[1];
      arrayOfString[0] = this.mDelegate.getDownloadLink();
      localDownloadApp.execute(arrayOfString);
      return;
      Toast.makeText(getApplicationContext(), "Invalid download link specified.", 1).show();
      onBackPressed();
      return;
    }
    if (Testing.areTesting)
    {
      Testing.testhit();
      return;
    }
    skip();
  }

  protected void onCreate(Bundle paramBundle)
  {
    Log.d("XPC", "onCreate() called! (mustinstallscreen)");
    preContentView();
    setContentView(new ParcelHelper("", this).getLayoutId("xpc_must_install"));
    super.onCreate(paramBundle);
    this.mMessageText = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_MessageText")));
    this.mSoftwareDescription = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_softwareDescription")));
    this.mSkipButton = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_SkipButton")));
    this.mInstallButton = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_InstallButton")));
    this.mInstallButton.setOnClickListener(this);
    this.mSkipButton.setEnabled(false);
    this.mInstallButton.setEnabled(false);
    this.mMessageText.setText("");
    this.mSoftwareDescription.setText("");
    this.mDelegate = new MustInstallScreenDelegate(this);
    if (haveTabs())
      setConfigureActive();
  }

  protected void onPause()
  {
    if (this.mChecking != null)
    {
      this.mChecking.cancel(true);
      this.mChecking = null;
    }
    super.onPause();
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    super.onServiceBind(paramEncapsulationService);
    if (this.mParser == null)
      Util.log(this.mLogger, "Config was not properly bound!");
    Util.log(this.mLogger, "+++ Showing must install screen.");
    if (haveTabs())
      setWelcomeActive();
    if ((this.mParser == null) || (this.mParser.selectedProfile == null))
    {
      Util.log(this.mLogger, "No profile selected?!  Terminating.");
      done(29);
      return;
    }
    this.mDelegate.setLogger(this.mLogger);
    this.mDelegate.setInstallList(this.mParser.selectedProfile.getSettingMulti(0, 80025));
    this.mChecking = new InitialCheckAppState(null);
    this.mChecking.execute(new Void[0]);
  }

  private class ContinueCheckAppState extends AsyncTask<Void, Void, Integer>
  {
    private ContinueCheckAppState()
    {
    }

    protected Integer doInBackground(Void[] paramArrayOfVoid)
    {
      Thread.currentThread().setName("Check app install state task.");
      if (MustInstallScreen.this.mDelegate.isAppInstalled())
      {
        if (MustInstallScreen.this.mDelegate.nextSetting())
          return Integer.valueOf(0);
        return Integer.valueOf(1);
      }
      return Integer.valueOf(2);
    }

    protected void onPostExecute(Integer paramInteger)
    {
      super.onPostExecute(paramInteger);
      switch (paramInteger.intValue())
      {
      default:
        return;
      case 0:
        MustInstallScreen.this.applySettings();
        return;
      case 1:
      }
      MustInstallScreen.this.allInstalled();
    }
  }

  private class DownloadApp extends AsyncTask<String, Void, Boolean>
  {
    private DownloadApp()
    {
    }

    protected Boolean doInBackground(String[] paramArrayOfString)
    {
      if (new WebInterface(MustInstallScreen.this.mLogger).download(paramArrayOfString[0], Environment.getExternalStorageDirectory().getAbsolutePath() + "/app.apk"))
        return Boolean.valueOf(true);
      return Boolean.valueOf(false);
    }

    protected void onPostExecute(Boolean paramBoolean)
    {
      super.onPostExecute(paramBoolean);
      MustInstallScreen.this.removeMyDialog("pleaseWaitDownloading");
      if (paramBoolean.booleanValue())
      {
        Util.log(MustInstallScreen.this.mLogger, "Installing : file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/app.apk");
        try
        {
          Intent localIntent = new Intent("android.intent.action.VIEW");
          localIntent.setDataAndType(Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/app.apk"), "application/vnd.android.package-archive");
          MustInstallScreen.this.startActivityForResult(localIntent, 0);
          return;
        }
        catch (Exception localException)
        {
          Toast.makeText(MustInstallScreen.this.me, "Unable to install the required application!", 1).show();
          return;
        }
      }
      Toast.makeText(MustInstallScreen.this.me, "Unable to download and install the required application!", 1).show();
    }
  }

  private class InitialCheckAppState extends AsyncTask<Void, Void, Boolean>
  {
    private InitialCheckAppState()
    {
    }

    protected Boolean doInBackground(Void[] paramArrayOfVoid)
    {
      for (boolean bool = MustInstallScreen.this.mDelegate.nextSetting(); (bool == true) && (MustInstallScreen.this.mDelegate.isAppInstalled()); bool = MustInstallScreen.this.mDelegate.nextSetting());
      return Boolean.valueOf(bool);
    }

    protected void onCancelled()
    {
      super.onCancelled();
      MustInstallScreen.this.removeMyDialog("pleaseWait");
      MustInstallScreen.access$502(MustInstallScreen.this, null);
    }

    protected void onCancelled(Boolean paramBoolean)
    {
      super.onCancelled(paramBoolean);
      onCancelled();
    }

    protected void onPostExecute(Boolean paramBoolean)
    {
      super.onPostExecute(paramBoolean);
      MustInstallScreen.this.removeMyDialog("pleaseWait");
      MustInstallScreen.access$502(MustInstallScreen.this, null);
      if (!paramBoolean.booleanValue())
      {
        MustInstallScreen.this.allInstalled();
        return;
      }
      MustInstallScreen.this.applySettings();
    }

    protected void onPreExecute()
    {
      super.onPreExecute();
      MustInstallScreen.this.showMyDialog(MustInstallScreen.MustInstallFragments.newInstance(1001), "pleaseWait");
    }
  }

  public static class MustInstallFragments extends DialogFragment
  {
    public static MustInstallFragments newInstance(int paramInt)
    {
      MustInstallFragments localMustInstallFragments = new MustInstallFragments();
      Bundle localBundle = new Bundle();
      localBundle.putInt("id", paramInt);
      localMustInstallFragments.setArguments(localBundle);
      return localMustInstallFragments;
    }

    public Dialog onCreateDialog(Bundle paramBundle)
    {
      int i = getArguments().getInt("id");
      ScreenBase localScreenBase = (ScreenBase)getActivity();
      switch (i)
      {
      default:
        return null;
      case 1000:
        ProgressDialog localProgressDialog2 = new ProgressDialog(localScreenBase);
        localProgressDialog2.setTitle(((MustInstallScreen)localScreenBase).mParcelHelper.getIdentifier("xpc_please_wait", "string"));
        localProgressDialog2.setMessage(getResources().getString(((MustInstallScreen)localScreenBase).mParcelHelper.getIdentifier("xpc_downloading", "string")));
        localProgressDialog2.setCancelable(false);
        return localProgressDialog2;
      case 1001:
      }
      ProgressDialog localProgressDialog1 = new ProgressDialog(localScreenBase);
      localProgressDialog1.setMessage(getResources().getString(((MustInstallScreen)localScreenBase).mParcelHelper.getIdentifier("xpc_please_wait", "string")));
      localProgressDialog1.setCancelable(false);
      return localProgressDialog1;
    }
  }
}