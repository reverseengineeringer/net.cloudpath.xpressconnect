package net.cloudpath.xpressconnect.localservice;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import java.security.PrivateKey;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.Testing;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.thread.SystemLogMonitor;

public class EncapsulationService extends Service
{
  private int activeTabBorderColor = -1;
  private int activeTabFontColor = -1;
  private int backgroundColor = -1;
  private Bitmap brandingImg = null;
  private int cornerStyle = -1;
  private int inactiveTabBorderColor = -1;
  private int inactiveTabFontColor = -1;
  private int licensed_to_font_color = -1;
  private int lineColor = -1;
  private final IBinder mBinder = new LocalBinder();
  private boolean mCredsPushedIn = false;
  private FailureReason mFailure = null;
  private boolean mIsEs = false;
  private String mLoadedFromUrl = null;
  private Logger mLogger = null;
  private NetworkConfigParser mParser = null;
  private PrivateKey mPkey = null;
  private boolean mRunningAsLibrary = false;
  private Object mSavedObject = null;
  private String mUserCert = null;
  private String mXmlConfig = null;
  private int outlineColor = -1;
  private int passwordSet = -1;
  private String selectedConfigName = null;
  private String selectedNetworkName = null;
  private SystemLogMonitor syslogmon = null;
  private WifiManager wifimgr = null;

  public void createConfigParser()
  {
    this.mParser = new NetworkConfigParser(this.mLogger);
  }

  public void destroyConfigParser()
  {
    this.mParser = null;
  }

  public void destroyLogger()
  {
    if (this.mLogger != null)
      this.mLogger.clear();
  }

  public int getActiveTabBorderColor()
  {
    return this.activeTabBorderColor;
  }

  public int getActiveTabFontColor()
  {
    return this.activeTabFontColor;
  }

  public int getBackgroundColor()
  {
    return this.backgroundColor;
  }

  public Bitmap getBrandingImg()
  {
    return this.brandingImg;
  }

  public NetworkConfigParser getConfigParser()
  {
    return this.mParser;
  }

  public int getCornerStyle()
  {
    return this.cornerStyle;
  }

  public boolean getCredsPushedIn()
  {
    return this.mCredsPushedIn;
  }

  public FailureReason getFailureReason()
  {
    return this.mFailure;
  }

  public int getInactiveTabBorderColor()
  {
    return this.inactiveTabBorderColor;
  }

  public int getInactiveTabFontColor()
  {
    return this.inactiveTabFontColor;
  }

  public boolean getIsEs()
  {
    return this.mIsEs;
  }

  public int getLicensedToFontColor()
  {
    return this.licensed_to_font_color;
  }

  public int getLineColor()
  {
    return this.lineColor;
  }

  public String getLoadedFromUrl()
  {
    return this.mLoadedFromUrl;
  }

  public Logger getLogger()
  {
    return this.mLogger;
  }

  public int getOutlineColor()
  {
    return this.outlineColor;
  }

  public int getPasswordSet()
  {
    return this.passwordSet;
  }

  public PrivateKey getPrivateKey()
  {
    return this.mPkey;
  }

  public boolean getRunningAsLibrary()
  {
    return this.mRunningAsLibrary;
  }

  public Object getSavedObject()
  {
    return this.mSavedObject;
  }

  public String getSelectedConfigName()
  {
    return this.selectedConfigName;
  }

  public String getSelectedNetworkName()
  {
    return this.selectedNetworkName;
  }

  public SystemLogMonitor getSysLogMon()
  {
    return this.syslogmon;
  }

  public String getUserCert()
  {
    return this.mUserCert;
  }

  public WifiManager getWifiManager()
  {
    return this.wifimgr;
  }

  public String getXmlConfig()
  {
    return this.mXmlConfig;
  }

  public IBinder onBind(Intent paramIntent)
  {
    Log.i("EncapsulationService", "Returning binder.");
    return this.mBinder;
  }

  public void onCreate()
  {
    Log.d("XPC", "Service onCreate()");
    this.mLogger = new Logger();
    this.mFailure = new FailureReason();
    this.syslogmon = new SystemLogMonitor(this.mLogger);
    this.syslogmon.start();
    if (!Testing.areTesting)
      this.wifimgr = ((WifiManager)getSystemService("wifi"));
  }

  public void onDestroy()
  {
    this.syslogmon.die();
    this.syslogmon = null;
    super.onDestroy();
  }

  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2)
  {
    Log.i("EncapsulationService", "Received start id " + paramInt2 + ": " + paramIntent);
    return 1;
  }

  public void setActiveTabBorderColor(int paramInt)
  {
    this.activeTabBorderColor = paramInt;
  }

  public void setActiveTabFontColor(int paramInt)
  {
    this.activeTabFontColor = paramInt;
  }

  public void setBackgroundColor(int paramInt)
  {
    this.backgroundColor = paramInt;
  }

  public void setBrandingImg(Bitmap paramBitmap)
  {
    this.brandingImg = paramBitmap;
  }

  public void setCornerStyle(int paramInt)
  {
    this.cornerStyle = paramInt;
  }

  public void setCredsPushedIn(boolean paramBoolean)
  {
    this.mCredsPushedIn = paramBoolean;
  }

  public void setInactiveTabBorderColor(int paramInt)
  {
    this.inactiveTabBorderColor = paramInt;
  }

  public void setInactiveTabFontColor(int paramInt)
  {
    this.inactiveTabFontColor = paramInt;
  }

  public void setIsEs(boolean paramBoolean)
  {
    this.mIsEs = paramBoolean;
  }

  public void setLicensedToFontColor(int paramInt)
  {
    this.licensed_to_font_color = paramInt;
  }

  public void setLineColor(int paramInt)
  {
    this.lineColor = paramInt;
  }

  public void setLoadedFromUrl(String paramString)
  {
    this.mLoadedFromUrl = paramString;
  }

  public void setOutlineColor(int paramInt)
  {
    this.outlineColor = paramInt;
  }

  public void setPasswordSet(int paramInt)
  {
    this.passwordSet = paramInt;
  }

  public void setPrivateKey(PrivateKey paramPrivateKey)
  {
    this.mPkey = paramPrivateKey;
  }

  public void setRunningAsLibrary(boolean paramBoolean)
  {
    this.mRunningAsLibrary = paramBoolean;
  }

  public void setSavedObject(Object paramObject)
  {
    this.mSavedObject = paramObject;
  }

  public void setSelectedConfigName(String paramString)
  {
    this.selectedConfigName = paramString;
  }

  public void setSelectedNetworkName(String paramString)
  {
    this.selectedNetworkName = paramString;
  }

  public void setUserCert(String paramString)
  {
    this.mUserCert = paramString;
  }

  public void setXmlConfig(String paramString)
  {
    this.mXmlConfig = paramString;
  }

  public class LocalBinder extends Binder
  {
    public LocalBinder()
    {
    }

    EncapsulationService getService()
    {
      return EncapsulationService.this;
    }
  }
}