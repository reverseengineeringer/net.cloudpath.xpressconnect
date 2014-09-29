package net.cloudpath.xpressconnect.thread;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

public class ThreadCom extends Handler
{
  public static final int CHANGE_TABS = 12350;
  public static final int SHOW_DIALOG = 12351;
  public static final int TAB_AUTHENTICATE = 3;
  public static final int TAB_CONFIGURE = 1;
  public static final int TAB_CONNECT = 2;
  public static final int TAB_CONNECTED = 5;
  public static final int TAB_VALIDATE = 4;
  public static final int TAB_WELCOME = 0;
  public static final int THREAD_FAILURE = 12345;
  public static final int THREAD_SUCCESS = 12347;
  public static final int UPDATE_TEXT = 12349;
  private volatile TextView myText = null;
  private ThreadComCallbacks threadCallbacks = null;

  protected int boolToInt(boolean paramBoolean)
  {
    if (paramBoolean)
      return 1;
    return 0;
  }

  public void changeTab(int paramInt)
  {
    Message localMessage = new Message();
    localMessage.what = 12350;
    localMessage.arg1 = paramInt;
    sendMessage(localMessage);
  }

  public void doFailure(Boolean paramBoolean, int paramInt, String paramString)
  {
    Message localMessage = new Message();
    localMessage.what = 12345;
    localMessage.arg1 = paramInt;
    localMessage.arg2 = boolToInt(paramBoolean.booleanValue());
    localMessage.obj = paramString;
    sendMessage(localMessage);
  }

  public void doSuccess()
  {
    Message localMessage = new Message();
    localMessage.what = 12347;
    sendMessage(localMessage);
  }

  public void handleMessage(Message paramMessage)
  {
    switch (paramMessage.what)
    {
    case 12346:
    case 12348:
    default:
    case 12345:
    case 12347:
    case 12349:
    case 12351:
    case 12350:
    }
    while (true)
    {
      super.handleMessage(paramMessage);
      return;
      if (this.threadCallbacks != null)
      {
        this.threadCallbacks.threadFailed(Boolean.valueOf(intToBool(paramMessage.arg2)), paramMessage.arg1, (String)paramMessage.obj);
        continue;
        if (this.threadCallbacks != null)
        {
          this.threadCallbacks.threadSuccess();
          continue;
          if (this.myText != null)
          {
            this.myText.setText((String)paramMessage.obj);
          }
          else
          {
            Log.d("XPC", "Got an update event when the dialog didn't exist.  Discarding.");
            continue;
            this.threadCallbacks.showDialog(paramMessage.arg1);
            continue;
            if (this.threadCallbacks != null)
              this.threadCallbacks.threadChangeTab(paramMessage.arg1);
          }
        }
      }
    }
  }

  protected boolean intToBool(int paramInt)
  {
    return paramInt != 0;
  }

  public void setCallbacks(ThreadComCallbacks paramThreadComCallbacks)
  {
    this.threadCallbacks = paramThreadComCallbacks;
  }

  public void setTextView(TextView paramTextView)
  {
    this.myText = paramTextView;
  }

  public void showDialog(int paramInt)
  {
    Message localMessage = new Message();
    localMessage.what = 12351;
    localMessage.arg1 = paramInt;
    sendMessage(localMessage);
  }

  public void update(String paramString)
  {
    Message localMessage = new Message();
    localMessage.what = 12349;
    localMessage.obj = paramString;
    sendMessage(localMessage);
  }
}