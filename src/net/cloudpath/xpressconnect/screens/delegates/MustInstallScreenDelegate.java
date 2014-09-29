package net.cloudpath.xpressconnect.screens.delegates;

import android.content.Context;
import android.widget.Toast;
import java.util.List;
import net.cloudpath.xpressconnect.Testing;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.checks.AppDetection;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.config.SettingElement;

public class MustInstallScreenDelegate
{
  private int listIndex = -1;
  private String mAppName = null;
  private Context mContext = null;
  private String mDownloadLink = null;
  private List<SettingElement> mElements = null;
  private Logger mLogger = null;
  private boolean mRequired = true;
  private String mSoftwareDescription = null;

  public MustInstallScreenDelegate(Context paramContext)
  {
    this.mContext = paramContext;
  }

  public void errorToast(String paramString)
  {
    if (!Testing.areTesting)
      Toast.makeText(this.mContext, paramString, 1).show();
  }

  public String getAppName()
  {
    return this.mAppName;
  }

  public String getDescription()
  {
    return this.mSoftwareDescription;
  }

  public String getDownloadLink()
  {
    return this.mDownloadLink;
  }

  public boolean getRequired()
  {
    return this.mRequired;
  }

  public boolean isAppInstalled()
  {
    AppDetection localAppDetection = new AppDetection();
    return (localAppDetection.isAppInstalledByClass(this.mContext.getPackageManager(), getAppName())) || (localAppDetection.isAppInstalledByName(this.mContext.getPackageManager(), getAppName()));
  }

  public boolean nextSetting()
  {
    if (this.mElements == null);
    do
    {
      return false;
      this.listIndex = (1 + this.listIndex);
    }
    while (this.listIndex >= this.mElements.size());
    SettingElement localSettingElement = (SettingElement)this.mElements.get(this.listIndex);
    setDescription(localSettingElement.installText);
    if (localSettingElement.requiredValue.contentEquals("1"))
      setRequired(true);
    while (true)
    {
      setAppName(localSettingElement.additionalValue);
      setDownloadLink(localSettingElement.installCabName);
      return true;
      setRequired(false);
    }
  }

  public void setAppName(String paramString)
  {
    if (paramString == null)
    {
      errorToast("App name is NULL while attempting to set!");
      this.mAppName = "";
      return;
    }
    this.mAppName = paramString;
  }

  public void setDescription(String paramString)
  {
    if (paramString == null)
    {
      errorToast("Description is NULL while attempting to set!");
      this.mSoftwareDescription = "";
      return;
    }
    this.mSoftwareDescription = paramString;
  }

  public void setDownloadLink(String paramString)
  {
    if (paramString == null)
    {
      Util.log(this.mLogger, "Download link is NULL while attempting to set!");
      this.mDownloadLink = "";
      return;
    }
    this.mDownloadLink = paramString;
  }

  public void setInstallList(List<SettingElement> paramList)
  {
    this.mElements = paramList;
  }

  public void setLogger(Logger paramLogger)
  {
    this.mLogger = paramLogger;
  }

  public void setRequired(boolean paramBoolean)
  {
    this.mRequired = paramBoolean;
  }
}