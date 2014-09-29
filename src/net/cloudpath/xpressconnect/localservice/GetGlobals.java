package net.cloudpath.xpressconnect.localservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;
import java.security.PrivateKey;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.thread.SystemLogMonitor;

public class GetGlobals
{
  private boolean isBound = false;
  private EncapsulationService mBoundService;
  private ServiceBoundCallback mCallback = null;
  private Context mParent = null;
  private ServiceConnection onService = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      GetGlobals.access$002(GetGlobals.this, ((EncapsulationService.LocalBinder)paramAnonymousIBinder).getService());
      GetGlobals.access$102(GetGlobals.this, true);
      if (GetGlobals.this.mCallback != null)
        GetGlobals.this.mCallback.onServiceBind(GetGlobals.this.mBoundService);
    }

    public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
    {
      GetGlobals.access$002(GetGlobals.this, null);
      GetGlobals.access$102(GetGlobals.this, false);
      if (GetGlobals.this.mCallback != null)
        GetGlobals.this.mCallback.onServiceUnbind();
    }
  };

  public GetGlobals(Context paramContext)
  {
    this.mParent = paramContext;
  }

  public void bindService()
  {
    if (this.mParent == null)
    {
      Log.d("XPC", "Parent is null in bindService().\n");
      return;
    }
    this.mParent.bindService(new Intent(this.mParent, EncapsulationService.class), this.onService, 1);
  }

  public void createConfigParser()
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in createConfigParser().\n");
      return;
    }
    this.mBoundService.createConfigParser();
  }

  public void destroyServiceData()
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in destroyServiceData().\n");
      return;
    }
    this.mBoundService.destroyConfigParser();
    this.mBoundService.destroyLogger();
  }

  public int getActiveTabBorderColor()
  {
    if (this.mBoundService == null)
      return -1;
    return this.mBoundService.getActiveTabBorderColor();
  }

  public int getActiveTabFontColor()
  {
    if (this.mBoundService == null)
      return -1;
    return this.mBoundService.getActiveTabFontColor();
  }

  public int getBackgroundColor()
  {
    if (this.mBoundService == null)
      return -1;
    return this.mBoundService.getBackgroundColor();
  }

  public Bitmap getBrandingImg()
  {
    if (this.mBoundService == null)
      return null;
    return this.mBoundService.getBrandingImg();
  }

  public NetworkConfigParser getConfigParser()
  {
    if (this.mBoundService != null)
      return this.mBoundService.getConfigParser();
    return null;
  }

  public int getCornerStyle()
  {
    if (this.mBoundService == null)
      return -1;
    return this.mBoundService.getCornerStyle();
  }

  public boolean getCredsPushedIn()
  {
    if (this.mBoundService == null)
      return false;
    return this.mBoundService.getCredsPushedIn();
  }

  public FailureReason getFailureReason()
  {
    if (!this.isBound)
      return null;
    return this.mBoundService.getFailureReason();
  }

  public int getInactiveTabBorderColor()
  {
    if (this.mBoundService == null)
      return -1;
    return this.mBoundService.getInactiveTabBorderColor();
  }

  public int getInactiveTabFontColor()
  {
    if (this.mBoundService == null)
      return -1;
    return this.mBoundService.getInactiveTabFontColor();
  }

  public boolean getIsEs()
  {
    if (this.mBoundService == null)
      return false;
    return this.mBoundService.getIsEs();
  }

  public int getLicensedToFontColor()
  {
    if (this.mBoundService == null)
      return -1;
    return this.mBoundService.getLicensedToFontColor();
  }

  public int getLineColor()
  {
    if (this.mBoundService == null)
      return -1;
    return this.mBoundService.getLineColor();
  }

  public String getLoadedFromUrl()
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in getLoadedFromUrl!");
      return null;
    }
    return this.mBoundService.getLoadedFromUrl();
  }

  public Logger getLogger()
  {
    if (!this.isBound)
      return null;
    return this.mBoundService.getLogger();
  }

  public int getOutlineColor()
  {
    if (this.mBoundService == null)
      return -1;
    return this.mBoundService.getOutlineColor();
  }

  public int getPasswordSet()
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in getPasswordSet!");
      return -1;
    }
    return this.mBoundService.getPasswordSet();
  }

  public PrivateKey getPrivateKey()
  {
    if (this.mBoundService == null)
      return null;
    return this.mBoundService.getPrivateKey();
  }

  public boolean getRunningAsLibrary()
  {
    if (this.mBoundService == null)
      return false;
    return this.mBoundService.getRunningAsLibrary();
  }

  public Object getSavedObject()
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in getSavedObject!");
      return null;
    }
    return this.mBoundService.getSavedObject();
  }

  public String getSelectedConfigName()
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in getSelectedConfigName().\n");
      return "";
    }
    return this.mBoundService.getSelectedConfigName();
  }

  public String getSelectedNetworkName()
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in setSelectedNetworkName().\n");
      return "";
    }
    return this.mBoundService.getSelectedNetworkName();
  }

  public SystemLogMonitor getSysLogMon()
  {
    if (!this.isBound)
      return null;
    return this.mBoundService.getSysLogMon();
  }

  public String getUserCert()
  {
    if (this.mBoundService == null)
      return null;
    return this.mBoundService.getUserCert();
  }

  public String getXmlConfig()
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in getHaveConfigFile().\n");
      return null;
    }
    return this.mBoundService.getXmlConfig();
  }

  public void overrideBoundService(EncapsulationService paramEncapsulationService)
  {
    this.mBoundService = paramEncapsulationService;
    this.isBound = true;
  }

  public void resetPasswordSet()
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in resetPasswordSet!");
      return;
    }
    this.mBoundService.setPasswordSet(-1);
  }

  public void setActiveTabBorderColor(int paramInt)
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in setActiveTabBorderColor!");
      return;
    }
    this.mBoundService.setActiveTabBorderColor(paramInt);
  }

  public void setActiveTabFontColor(int paramInt)
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in setActiveTabFontColor!");
      return;
    }
    this.mBoundService.setActiveTabFontColor(paramInt);
  }

  public void setBackgroundColor(int paramInt)
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in setBackgroundColor!");
      return;
    }
    this.mBoundService.setBackgroundColor(paramInt);
  }

  public void setBrandingImg(Bitmap paramBitmap)
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in setBrandingImg!");
      return;
    }
    this.mBoundService.setBrandingImg(paramBitmap);
  }

  public void setCallbacks(ServiceBoundCallback paramServiceBoundCallback)
  {
    this.mCallback = paramServiceBoundCallback;
  }

  public void setCornerStyle(int paramInt)
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in setCornerStyle!");
      return;
    }
    this.mBoundService.setCornerStyle(paramInt);
  }

  public void setCredsPushedIn(boolean paramBoolean)
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in setCredsPushedIn!");
      return;
    }
    this.mBoundService.setCredsPushedIn(paramBoolean);
  }

  public void setInactiveTabBorderColor(int paramInt)
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in setInactiveTabBorderColor!");
      return;
    }
    this.mBoundService.setInactiveTabBorderColor(paramInt);
  }

  public void setInactiveTabFontColor(int paramInt)
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in setInactiveTabFontColor!");
      return;
    }
    this.mBoundService.setInactiveTabFontColor(paramInt);
  }

  public void setIsEs(boolean paramBoolean)
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in setIsEs!");
      return;
    }
    this.mBoundService.setIsEs(paramBoolean);
  }

  public void setLicensedToFontColor(int paramInt)
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in setLicensedToFontColor!");
      return;
    }
    this.mBoundService.setLicensedToFontColor(paramInt);
  }

  public void setLineColor(int paramInt)
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in setLineColor!");
      return;
    }
    this.mBoundService.setLineColor(paramInt);
  }

  public void setLoadedFromUrl(String paramString)
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in setLoadedFromUrl!");
      return;
    }
    this.mBoundService.setLoadedFromUrl(paramString);
  }

  public void setOutlineColor(int paramInt)
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in setOutlineColor!");
      return;
    }
    this.mBoundService.setOutlineColor(paramInt);
  }

  public void setPrivateKey(PrivateKey paramPrivateKey)
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundServer is null in setPrivateKey()!");
      return;
    }
    this.mBoundService.setPrivateKey(paramPrivateKey);
  }

  public void setRunningAsLibrary(boolean paramBoolean)
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in setRunningAsLibrary!");
      return;
    }
    this.mBoundService.setRunningAsLibrary(paramBoolean);
  }

  public void setSavedObject(Object paramObject)
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in setSavedObject!");
      return;
    }
    this.mBoundService.setSavedObject(paramObject);
  }

  public void setSelectedConfigName(String paramString)
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in setSelectedConfigName().\n");
      return;
    }
    this.mBoundService.setSelectedConfigName(paramString);
  }

  public void setSelectedNetworkName(String paramString)
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in setSelectedNetworkName().\n");
      return;
    }
    this.mBoundService.setSelectedNetworkName(paramString);
  }

  public void setUserCert(String paramString)
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundServer is null in setUserCert!");
      return;
    }
    this.mBoundService.setUserCert(paramString);
  }

  public void setXmlConfig(String paramString)
  {
    if (this.mBoundService == null)
    {
      Log.d("XPC", "mBoundService is null in setXmlConfig().\n");
      return;
    }
    this.mBoundService.setXmlConfig(paramString);
  }

  public void unbindService()
  {
    if (!this.isBound)
      return;
    if (this.mParent == null)
    {
      Log.d("XPC", "Parent is null in unbindService().\n");
      return;
    }
    this.mParent.unbindService(this.onService);
  }
}