package net.cloudpath.xpressconnect.screens.delegates;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Handler;
import java.util.Date;
import net.cloudpath.xpressconnect.GestureCallback;
import net.cloudpath.xpressconnect.LocalDbHelper;
import net.cloudpath.xpressconnect.MapVariables;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkElement;
import net.cloudpath.xpressconnect.parsers.config.NetworkItemElement;
import net.cloudpath.xpressconnect.remoteservice.SendReport;
import net.cloudpath.xpressconnect.screens.ShowReportData;

public class ShowReportDataDelegate extends DelegateBase
  implements GestureCallback
{
  private int mFailIssue = -1;
  private ShowReportData mParent = null;
  private String mResultString = null;
  private String mResults = null;
  private String mState = null;
  private ProgressDialog pd = null;

  public ShowReportDataDelegate(ShowReportData paramShowReportData)
  {
    super(paramShowReportData);
    this.mParent = paramShowReportData;
  }

  public ShowReportDataDelegate(ShowReportData paramShowReportData, Context paramContext)
  {
    super(paramContext);
    this.mParent = paramShowReportData;
  }

  public void buttonNoClicked(boolean paramBoolean)
  {
    if (paramBoolean)
      SendReport.saveAlwaysUsePref(this.mLogger, this.mParent, 0);
    if (this.mParent == null)
    {
      Util.log(this.mLogger, "mParent is null in buttonNoClicked()!");
      return;
    }
    this.mParent.done(1);
  }

  public void buttonYesClicked(boolean paramBoolean, int paramInt, WifiManager paramWifiManager)
  {
    if (paramBoolean)
      SendReport.saveAlwaysUsePref(this.mLogger, this.mParent, 1);
    SendReport.startReportSendingAttempts(this.mLogger, this.mParent, this.mParser, Integer.parseInt(this.mState), paramInt, this.mResultString, paramWifiManager);
    this.mParent.done(1);
  }

  public String getResultString()
  {
    return this.mResultString;
  }

  public String getState()
  {
    return this.mState;
  }

  public void handleDumpDatabase()
  {
    LocalDbHelper.dumpDbToSdCard(this.mParent);
  }

  public void handleNfcProgrammerGesture()
  {
    if (this.mParent == null)
    {
      Util.log(this.mLogger, "mParent is null in handleNfcProgrammerGesture!");
      return;
    }
    this.mParent.showLongToast("Starting NFC Programmer...");
    this.mParent.done(39);
  }

  public void onServiceBind()
  {
    new BuildDialogData(null).execute(new Void[0]);
  }

  public void setResults(String paramString1, String paramString2, int paramInt)
  {
    this.mResultString = paramString1;
    this.mState = paramString2;
    this.mFailIssue = paramInt;
  }

  private class BuildDialogData extends AsyncTask<Void, Void, Void>
  {
    private BuildDialogData()
    {
    }

    protected Void doInBackground(Void[] paramArrayOfVoid)
    {
      Thread.currentThread().setName("ShowReportData task.");
      String str1 = "Unknown";
      try
      {
        str1 = ShowReportDataDelegate.this.mContext.getPackageManager().getPackageInfo(ShowReportDataDelegate.this.mContext.getPackageName(), 0).versionName;
      }
      catch (Exception localException1)
      {
        try
        {
          while (true)
          {
            String str4 = new MapVariables(ShowReportDataDelegate.this.mParser, ShowReportDataDelegate.this.mLogger).varMap(ShowReportDataDelegate.this.mResultString, false, null);
            str2 = str4;
            String str3 = "State : " + ShowReportDataDelegate.this.mState + "\n" + "Fail Issue : " + ShowReportDataDelegate.this.mFailIssue + "\n" + "Version : " + str1 + "\n" + "Time : " + new Date().toString() + "\n" + "Result String : " + str2 + "\n" + "Device Hash : " + ShowReportDataDelegate.this.mParser.savedConfigInfo.clientId + "\n" + "Organization : " + ShowReportDataDelegate.this.mParser.networks.licensee + "\n" + "Selected Network : " + ShowReportDataDelegate.this.mParser.selectedNetwork.name + "\n" + "Board : " + Build.BOARD + "\n" + "Brand : " + Build.BRAND + "\n" + "ABI : " + Build.CPU_ABI + "\n" + "Device : " + Build.DEVICE + "\n" + "Display : " + Build.DISPLAY + "\n" + "Fingerprint : " + Build.FINGERPRINT + "\n" + "Host : " + Build.HOST + "\n" + "ID : " + Build.ID + "\n" + "Manufacturer : " + Build.MANUFACTURER + "\n" + "Model : " + Build.MODEL + "\n" + "Product : " + Build.PRODUCT + "\n" + "Tags : " + Build.TAGS + "\n" + "Time : " + Build.TIME + "\n" + "Type : " + Build.TYPE + "\n" + "Builder : " + Build.USER + "\n" + "Codename : " + Build.VERSION.CODENAME + "\n" + "Incremental : " + Build.VERSION.INCREMENTAL + "\n" + "Release : " + Build.VERSION.RELEASE + "\n" + "SDK : " + Build.VERSION.SDK_INT + "\n\n" + "\nLog Data : " + ShowReportDataDelegate.this.mLogger.getViewableLines() + "\n";
            ShowReportDataDelegate.access$402(ShowReportDataDelegate.this, str3);
            return null;
            localException1 = localException1;
            Util.log(ShowReportDataDelegate.this.mLogger, "Exception : " + localException1.getMessage());
            Util.log(ShowReportDataDelegate.this.mLogger, "Unable to determine my version information.");
          }
        }
        catch (Exception localException2)
        {
          while (true)
          {
            localException2.printStackTrace();
            String str2 = ShowReportDataDelegate.this.mResultString;
          }
        }
      }
    }

    protected void onPostExecute(Void paramVoid)
    {
      super.onPostExecute(paramVoid);
      ShowReportDataDelegate.this.mParent.setLogData(ShowReportDataDelegate.this.mResults);
      new Handler().post(new Runnable()
      {
        public void run()
        {
          ShowReportDataDelegate.this.pd.cancel();
        }
      });
    }

    protected void onPreExecute()
    {
      super.onPreExecute();
      ShowReportDataDelegate.access$602(ShowReportDataDelegate.this, ProgressDialog.show(ShowReportDataDelegate.this.mContext, null, "Please Wait. . ."));
    }
  }
}