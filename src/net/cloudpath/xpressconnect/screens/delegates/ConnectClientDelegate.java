package net.cloudpath.xpressconnect.screens.delegates;

import android.content.Context;
import android.widget.TextView;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.screens.ConnectClient;
import net.cloudpath.xpressconnect.screens.ScreenBase;
import net.cloudpath.xpressconnect.thread.ConnectClientThread;
import net.cloudpath.xpressconnect.thread.SystemLogMonitor;
import net.cloudpath.xpressconnect.thread.ThreadCom;
import net.cloudpath.xpressconnect.thread.ThreadComCallbacks;

public class ConnectClientDelegate extends DelegateBase
  implements ThreadComCallbacks
{
  private SystemLogMonitor mLogMon = null;
  private ThreadCom mThreadCom = null;
  private ConnectClientThread mWorkerThread = null;

  public ConnectClientDelegate(ScreenBase paramScreenBase)
  {
    super(paramScreenBase);
  }

  public ConnectClientDelegate(ScreenBase paramScreenBase, Context paramContext)
  {
    super(paramScreenBase, paramContext);
  }

  public void onBackPressed()
  {
    if (this.mWorkerThread != null)
    {
      this.mWorkerThread.die();
      this.mWorkerThread = null;
    }
    done(-2);
  }

  public void onPause()
  {
    if (this.mWorkerThread != null)
    {
      this.mWorkerThread.spinLock();
      terminateWorker();
      this.mWorkerThread = null;
    }
  }

  public void onServiceBind()
  {
    if (this.mGlobals == null)
    {
      Util.log(this.mLogger, "mGlobals is null in onServiceBind()!");
      return;
    }
    restoreSavedData();
    this.mLogMon = this.mGlobals.getSysLogMon();
    startWorkerThread();
  }

  public void restoreSavedData()
  {
    if (this.mGlobals == null)
      Util.log(this.mLogger, "Globals structure is null in restoreSavedData()!");
    do
    {
      return;
      this.mThreadCom = new ThreadCom();
      TextView localTextView = (TextView)this.mActivityParent.findViewById(this.mParcelHelper.getItemId("xpc_configText"));
      localTextView.setText(this.mParcelHelper.getIdentifier("xpc_checking_config", "string"));
      this.mThreadCom.setTextView(localTextView);
    }
    while (this.mThreadCom == null);
    this.mThreadCom.setCallbacks(this);
  }

  public void showDialog(int paramInt)
  {
  }

  public void startWorkerThread()
  {
    if (this.mWorkerThread != null);
    do
    {
      return;
      this.mWorkerThread = new ConnectClientThread(this.mThreadCom, this.mParser, this.mLogger, this.mGlobals.getFailureReason(), this.mActivityParent, this.mLogMon, this.mActivityParent.mWifi);
    }
    while (this.mWorkerThread == null);
    this.mWorkerThread.start();
  }

  public void terminateWorker()
  {
    if (this.mWorkerThread != null)
      this.mWorkerThread.die();
  }

  public void threadChangeTab(int paramInt)
  {
    if (this.mActivityParent == null)
    {
      Util.log(this.mLogger, "Unable to change tab.  mActivityParent is null.");
      return;
    }
    ((ConnectClient)this.mActivityParent).threadChangeTab(paramInt);
  }

  public void threadFailed(Boolean paramBoolean, int paramInt, String paramString)
  {
    if (paramInt == -10)
      done(-3);
    while (true)
    {
      Util.log(this.mLogger, "--- Leaving ConnectClient because the thread failed.");
      return;
      done(paramInt);
    }
  }

  public void threadSuccess()
  {
    Util.log(this.mLogger, "--- Leaving ConnectClient because the thread was a success.");
    done(1);
  }
}