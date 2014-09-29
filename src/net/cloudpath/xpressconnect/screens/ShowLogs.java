package net.cloudpath.xpressconnect.screens;

import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.GenericCallback;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.localservice.ServiceBoundCallback;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkElement;
import net.cloudpath.xpressconnect.remote.RemoteUpload;
import net.cloudpath.xpressconnect.thread.BuildLogStringThread;
import net.cloudpath.xpressconnect.thread.ThreadCom;
import net.cloudpath.xpressconnect.thread.ThreadComCallbacks;

public class ShowLogs extends ScreenBase
  implements ServiceBoundCallback, GenericCallback, ThreadComCallbacks
{
  private static final int COPY_TO_CLIPBOARD = 4001;
  private static final int REFRESH = 4000;
  private TextView logText;
  private String logdata = null;
  private BuildLogStringThread mThread = null;
  private ThreadCom mThreadCom = null;

  protected void onCreate(Bundle paramBundle)
  {
    ParcelHelper localParcelHelper = new ParcelHelper("", this);
    preContentView();
    setContentView(localParcelHelper.getLayoutId("xpc_showlog_layout"));
    super.onCreate(paramBundle);
    this.logText = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_LogText")));
    this.logText.setVisibility(8);
    this.mShowAboutMenu = false;
    this.mShowLogsMenu = false;
    this.mShowHelpdeskMenu = false;
    this.mShowEmailLogsMenu = true;
    this.mShowStartOverMenu = false;
    this.mShowQuitMenu = true;
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    if (paramMenu == null)
      return super.onCreateOptionsMenu(paramMenu);
    paramMenu.add(0, 4000, 0, this.mParcelHelper.getIdentifier("xpc_refresh_log", "string"));
    paramMenu.add(0, 4001, 0, this.mParcelHelper.getIdentifier("xpc_copy_to_clipboard", "string"));
    return super.onCreateOptionsMenu(paramMenu);
  }

  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    boolean bool = true;
    if (paramMenuItem == null)
      bool = super.onOptionsItemSelected(paramMenuItem);
    do
    {
      return bool;
      switch (paramMenuItem.getItemId())
      {
      default:
        return super.onOptionsItemSelected(paramMenuItem);
      case 4000:
      case 4001:
      }
    }
    while (this.logText == null);
    Util.log(this.mLogger, "(Refresh) Starting popup and log builder thread.");
    this.mThread = new BuildLogStringThread(this.mLogger, this.mThreadCom);
    this.mThread.start();
    return bool;
    ((ClipboardManager)getSystemService("clipboard")).setText(this.logdata);
    Toast.makeText(this, "Log data copied to the clipboard.", 0).show();
    return bool;
  }

  protected void onPause()
  {
    super.onPause();
    Util.log(this.mLogger, "(ShowLogs) onPause().");
    if (this.mThread != null)
      this.mThread.spinLock();
  }

  protected void onResume()
  {
    super.onResume();
    Util.log(this.mLogger, "(ShowLogs) onResume().");
    if (this.mThread != null)
      this.mThread.resume(this.mThreadCom);
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    if (paramEncapsulationService == null)
    {
      Log.d("XPC", "mBound is null in onServiceBind()!");
      return;
    }
    super.onServiceBind(paramEncapsulationService);
    Util.log(this.mLogger, "+++ Showing ShowLogs.");
    if (this.logText != null)
    {
      Util.log(this.mLogger, "Starting log builder thread.");
      this.mThreadCom = new ThreadCom();
      this.mThreadCom.setCallbacks(this);
      this.mThread = new BuildLogStringThread(this.mLogger, this.mThreadCom);
      this.mThread.start();
      return;
    }
    Util.log(this.mLogger, "--- Leaving ShowLogs because logText is null.");
    done(0);
  }

  public void setCallbackData(int paramInt, String paramString)
  {
    if (paramInt >= 0)
    {
      if (!new RemoteUpload(this.mLogger, this).uploadLogData(this.mParser.savedConfigInfo.username, this.mParser.savedConfigInfo.password, this.mParser.networks.licensee, "key", this.mParser.savedConfigInfo.clientId, this.mParser.savedConfigInfo.sessionId, paramString, this.mParser.networks.server_address))
      {
        AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this);
        localBuilder1.setMessage("Unable to send your log data to the remote server.");
        localBuilder1.setTitle("Error");
        localBuilder1.show();
        Util.log(this.mLogger, "Uploading log data failed.");
        return;
      }
      AlertDialog.Builder localBuilder2 = new AlertDialog.Builder(this);
      localBuilder2.setMessage("Your log has been sent to the remote server.");
      localBuilder2.setTitle("Success");
      localBuilder2.show();
      Util.log(this.mLogger, "Uploading log data was successful.");
      return;
    }
    Util.log(this.mLogger, "Uploading log data was cancelled.");
  }

  public void threadChangeTab(int paramInt)
  {
  }

  public void threadFailed(Boolean paramBoolean, int paramInt, String paramString)
  {
  }

  public void threadSuccess()
  {
    if (this.mThread == null)
    {
      Util.log(this.mLogger, "mThread is null in popupSuccess()!");
      return;
    }
    this.logdata = this.mThread.getLogData();
    this.logText.setText(this.logdata);
    this.logText.setVisibility(0);
    this.mThread = null;
  }
}