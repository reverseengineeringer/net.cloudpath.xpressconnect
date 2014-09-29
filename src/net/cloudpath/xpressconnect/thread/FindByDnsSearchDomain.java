package net.cloudpath.xpressconnect.thread;

import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;

public class FindByDnsSearchDomain extends FindByDns
{
  public FindByDnsSearchDomain(Logger paramLogger)
  {
    super(paramLogger);
  }

  private boolean foundViaHttp(boolean paramBoolean)
  {
    Util.log(this.mLogger, "Checking search domain using HTTP...");
    this.mTargetUrl = "http://cloudpath-xpc";
    if (!findByUrl(this.mTargetUrl, paramBoolean))
    {
      Util.log(this.mLogger, "Not found by HTTP.");
      return false;
    }
    Util.log(this.mLogger, "FOUND by HTTP!");
    return true;
  }

  private boolean foundViaHttps(boolean paramBoolean)
  {
    this.mTargetUrl = "https://cloudpath-xpc";
    if (!findByUrl(this.mTargetUrl, paramBoolean))
    {
      Util.log(this.mLogger, "Not found by HTTPS.");
      return false;
    }
    Util.log(this.mLogger, "FOUND by HTTPS!");
    return true;
  }

  public boolean findByDnsSearchDomain(boolean paramBoolean)
  {
    if (foundViaHttp(paramBoolean));
    while (foundViaHttps(paramBoolean))
      return true;
    this.mTargetUrl = null;
    return false;
  }
}