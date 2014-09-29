package net.cloudpath.xpressconnect.thread;

import android.util.Log;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;

public class ThreadBase extends Thread
{
  public Logger mLogger = null;
  private volatile Thread mMonitor = null;
  protected volatile boolean mTerminate = false;
  protected volatile ReentrantLock myLock = null;

  public ThreadBase(Logger paramLogger)
  {
    this.mLogger = paramLogger;
    this.myLock = new ReentrantLock();
  }

  public void die()
  {
    Util.log(this.mLogger, "+-+-+-+  Requesting termination of worker thread.");
    this.mTerminate = true;
    Thread localThread = this.mMonitor;
    this.mMonitor = null;
    if (localThread != null)
      localThread.interrupt();
  }

  public boolean lock()
  {
    try
    {
      if (this.myLock.tryLock(2L, TimeUnit.SECONDS) == true)
      {
        Log.d("XPC", "+++++++++++++++++++++++ Locked.  (id = " + getId() + ")");
        return true;
      }
    }
    catch (InterruptedException localInterruptedException)
    {
      Util.log(this.mLogger, "******* InterruptedException : " + localInterruptedException.getMessage());
      localInterruptedException.printStackTrace();
    }
    return false;
  }

  public void run()
  {
    Log.d("XPC", " ** Starting thread : " + getId());
    this.mMonitor = Thread.currentThread();
  }

  public boolean shouldDie()
  {
    if (this.mMonitor == null)
    {
      System.out.println("Thread should die at :\n" + Util.getStackTrace(new Exception()));
      return true;
    }
    return this.mTerminate;
  }

  public void spinLock()
  {
    if (!isAlive())
    {
      Util.log(this.mLogger, "Thread is no longer alive while attempting to lock.  Ignoring.");
      return;
    }
    Log.d("XPC", "Spinlock entered.");
    while (!lock())
      if (shouldDie())
      {
        Log.d("XPC", "Bailing out of spin lock because the thread is terminating.");
        return;
      }
    Log.d("XPC", "Spinlock exited.");
  }

  public void unlock()
  {
    while (true)
    {
      try
      {
        Log.d("XPC", "--------------------- Unlocked.  (id = " + getId() + ")");
        boolean bool = this.myLock.isLocked();
        if (bool)
          try
          {
            this.myLock.unlock();
            return;
          }
          catch (IllegalMonitorStateException localIllegalMonitorStateException)
          {
            Util.log(this.mLogger, "Unable to unlock thread.  We don't hold the monitor.");
            continue;
          }
      }
      finally
      {
      }
      Log.d("XPC", "Not locked.. Skipping.");
    }
  }
}