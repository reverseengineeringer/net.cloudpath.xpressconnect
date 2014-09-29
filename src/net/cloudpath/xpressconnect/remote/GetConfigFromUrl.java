package net.cloudpath.xpressconnect.remote;

import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;

public class GetConfigFromUrl
{
  private Logger mLogger = null;
  private String mPageResult = null;
  private WebInterface mWebInterface = null;

  public GetConfigFromUrl(Logger paramLogger)
  {
    this.mLogger = paramLogger;
    this.mWebInterface = new WebInterface(this.mLogger);
  }

  public boolean findByUrl(String paramString, boolean paramBoolean)
  {
    boolean bool;
    if (Util.stringIsEmpty(paramString))
    {
      Util.log(this.mLogger, "Invalid URL provided for lookup.");
      bool = false;
    }
    do
    {
      return bool;
      bool = true;
      this.mPageResult = this.mWebInterface.getWebPage(paramString, false);
      if (this.mPageResult == null)
      {
        Util.log(this.mLogger, "Attempt to get configuration failed.  The result was null.");
        return false;
      }
      if (this.mWebInterface.resultCode != 200)
      {
        Util.log(this.mLogger, "Didn't get a result code of 200.  Result was : " + this.mWebInterface.resultCode + "  (" + this.mWebInterface.statusText + ")");
        bool = false;
      }
      if (!this.mPageResult.contains("</network>"))
      {
        Util.log(this.mLogger, "No network close tag found in results.");
        bool = false;
      }
    }
    while ((bool) || (!paramBoolean));
    if ((this.mPageResult != null) && (!this.mPageResult.contentEquals("")) && (this.mPageResult.length() > 0))
    {
      Util.log(this.mLogger, "Web result : \n" + this.mPageResult);
      return bool;
    }
    Util.log(this.mLogger, "Web result was null or empty.");
    return bool;
  }

  public String getPageResult()
  {
    return this.mPageResult;
  }
}