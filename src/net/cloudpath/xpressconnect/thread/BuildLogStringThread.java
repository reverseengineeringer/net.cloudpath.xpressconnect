package net.cloudpath.xpressconnect.thread;

import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;

public class BuildLogStringThread extends ThreadBase
{
  private String logdata = null;
  private Logger mLogger = null;
  private ThreadCom mThreadCom = null;

  public BuildLogStringThread(Logger paramLogger, ThreadCom paramThreadCom)
  {
    super(paramLogger);
    this.mLogger = paramLogger;
    this.mThreadCom = paramThreadCom;
    setName("Build log string thread.");
  }

  public String getLogData()
  {
    return this.logdata;
  }

  public void resume(ThreadCom paramThreadCom)
  {
    this.mThreadCom = paramThreadCom;
    unlock();
  }

  public void run()
  {
    super.run();
    if (shouldDie());
    do
    {
      return;
      spinLock();
      if (this.mLogger != null)
        this.logdata = this.mLogger.getViewableLines();
      unlock();
    }
    while (shouldDie());
    if (this.mThreadCom != null)
    {
      this.mThreadCom.doSuccess();
      return;
    }
    Util.log(this.mLogger, "Log thread completed, but mThreadCom was null.");
  }
}