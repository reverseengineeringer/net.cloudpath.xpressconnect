package net.cloudpath.xpressconnect.screens.delegates;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Build.VERSION;
import android.widget.Toast;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.io.IOException;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;
import net.cloudpath.xpressconnect.parsers.config.NetworkElement;
import net.cloudpath.xpressconnect.screens.NfcWriterScreen;
import net.cloudpath.xpressconnect.screens.ScreenBase;

public class NfcWriterScreenDelegate extends DelegateBase
{
  protected String mAndroidAppPath = null;
  protected String mAndroidConfigPath = null;
  protected String mAndroidPackageName = null;
  protected boolean mDoingFullConfig = false;
  protected NfcAdapter mNfcAdapter = null;
  protected boolean mSetReadOnly = false;
  protected String mWebServerPath = null;
  protected boolean mWritingTag = false;

  public NfcWriterScreenDelegate(Activity paramActivity)
  {
    super(paramActivity);
    this.mContext = paramActivity;
    this.mActivityParent = ((ScreenBase)paramActivity);
    if (this.mActivityParent != null)
      this.mNfcAdapter = NfcAdapter.getDefaultAdapter(this.mActivityParent);
  }

  public NfcWriterScreenDelegate(Activity paramActivity, Context paramContext)
  {
    super(paramContext);
    this.mContext = paramContext;
  }

  @SuppressLint({"NewApi"})
  @TargetApi(10)
  private void disableTagWriteMode()
  {
    if (this.mNfcAdapter == null)
    {
      Util.log(this.mLogger, "Unable to disable tag write mode.  NFC adapter object is null.");
      return;
    }
    this.mNfcAdapter.disableForegroundDispatch(this.mActivityParent);
  }

  @TargetApi(10)
  private void enableTagWriteMode()
  {
    IntentFilter[] arrayOfIntentFilter = { new IntentFilter("android.nfc.action.TAG_DISCOVERED") };
    if (this.mNfcAdapter == null)
    {
      Util.log(this.mLogger, "Unable to enable tag write mode.  NFC adapter object is null.");
      return;
    }
    this.mNfcAdapter.enableForegroundDispatch(this.mActivityParent, PendingIntent.getActivity(this.mActivityParent, 0, new Intent(this.mActivityParent, NfcWriterScreen.class).addFlags(536870912), 0), arrayOfIntentFilter, (String[][])null);
  }

  private String stripServerUrl(String paramString)
  {
    String str = paramString;
    if (paramString == null)
    {
      Util.log(this.mLogger, "inurl null in stripServerUrl().");
      return null;
    }
    if (paramString.endsWith("/pipe/postData.php"))
    {
      int i = "/pipe/postData.php".length();
      str = paramString.substring(0, paramString.length() - i);
    }
    if (!str.endsWith("/"))
      str = str + "/";
    return str;
  }

  public boolean canWriteNfcTags()
  {
    return canWriteNfcTags(Build.VERSION.SDK_INT);
  }

  @SuppressLint({"InlinedApi"})
  public boolean canWriteNfcTags(int paramInt)
  {
    if (paramInt <= 14)
      Util.log(this.mLogger, "Can't run on versions lower than 4.0");
    NfcAdapter localNfcAdapter;
    do
    {
      do
        return false;
      while (this.mContext == null);
      localNfcAdapter = ((NfcManager)this.mContext.getSystemService("nfc")).getDefaultAdapter();
    }
    while ((localNfcAdapter == null) || (!localNfcAdapter.isEnabled()));
    return true;
  }

  public void disableWriteTag()
  {
    disableTagWriteMode();
    this.mWritingTag = false;
  }

  public void enableWriteTag()
  {
    enableTagWriteMode();
    this.mWritingTag = true;
  }

  public boolean formatTag(NdefMessage paramNdefMessage, Tag paramTag)
    throws FormatException
  {
    if ((paramNdefMessage == null) || (paramTag == null))
    {
      Util.log(this.mLogger, "Invalid data passed to formatTag()!");
      return false;
    }
    return formatTag(NdefFormatable.get(paramTag), paramNdefMessage, paramTag);
  }

  public boolean formatTag(NdefFormatable paramNdefFormatable, NdefMessage paramNdefMessage, Tag paramTag)
    throws FormatException
  {
    boolean bool = false;
    if (paramNdefFormatable != null);
    try
    {
      paramNdefFormatable.connect();
      paramNdefFormatable.format(paramNdefMessage);
      Util.log(this.mLogger, "Tag was unformatted..  It is now formatted.");
      paramNdefFormatable.close();
      bool = true;
      return bool;
    }
    catch (IOException localIOException)
    {
    }
    return false;
  }

  public boolean gatherTagData()
  {
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "Parser data was null.  Won't be able to gather tag data!");
      return false;
    }
    if (this.mParser.networks == null)
    {
      Util.log(this.mLogger, "No networks loaded?!  Won't be able to gather tag data!");
      return false;
    }
    if (this.mContext == null)
    {
      Util.log(this.mLogger, "Context is invalid.  Can't determine app name!");
      return false;
    }
    this.mWebServerPath = stripServerUrl(this.mParser.networks.server_address);
    if (Util.stringIsEmpty(this.mWebServerPath))
    {
      Util.log(this.mLogger, "Invalid web server path in NfcWriterScreenDelegate.");
      return false;
    }
    this.mAndroidConfigPath = (this.mWebServerPath + "network_config_android.xml");
    try
    {
      PackageInfo localPackageInfo = this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0);
      this.mAndroidAppPath = ("market://details?id=" + localPackageInfo.packageName);
      this.mAndroidPackageName = localPackageInfo.packageName;
      return true;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      localNameNotFoundException.printStackTrace();
    }
    return false;
  }

  public String getAndroidConfigPath()
  {
    return this.mAndroidConfigPath;
  }

  public String getAppPath()
  {
    return this.mAndroidAppPath;
  }

  public boolean getDoingFullConfig()
  {
    return this.mDoingFullConfig;
  }

  @TargetApi(15)
  public NdefMessage getNdefRecord()
  {
    if (this.mDoingFullConfig)
    {
      NdefRecord localNdefRecord = NdefRecord.createApplicationRecord(this.mAndroidPackageName);
      return new NdefMessage(new NdefRecord[] { NdefRecord.createMime("cloudpathnetworks/xpressconnect", this.mAndroidConfigPath.getBytes()), localNdefRecord });
    }
    return new NdefMessage(new NdefRecord[] { NdefRecord.createUri(this.mWebServerPath) });
  }

  public boolean getNfcReadOnly()
  {
    return this.mSetReadOnly;
  }

  public String getPackageName()
  {
    return this.mAndroidPackageName;
  }

  public String getWebServerPath()
  {
    return this.mWebServerPath;
  }

  public boolean getWritingTag()
  {
    return this.mWritingTag;
  }

  public void setDoingFullConfig(boolean paramBoolean)
  {
    this.mDoingFullConfig = paramBoolean;
  }

  public void setNfcReadOnly(boolean paramBoolean)
  {
    this.mSetReadOnly = paramBoolean;
  }

  public void setRequiredValues(Logger paramLogger, NetworkConfigParser paramNetworkConfigParser)
  {
    this.mLogger = paramLogger;
    this.mParser = paramNetworkConfigParser;
  }

  public boolean writeTag(NdefMessage paramNdefMessage, Tag paramTag)
  {
    int i = paramNdefMessage.toByteArray().length;
    try
    {
      Ndef localNdef = Ndef.get(paramTag);
      if ((localNdef == null) && (!formatTag(paramNdefMessage, paramTag)))
      {
        Toast.makeText(this.mActivityParent, this.mParcelHelper.getIdentifier("xpc_nfc_cant_format", "string"), 1).show();
        return false;
      }
      localNdef.connect();
      if (!localNdef.isWritable())
      {
        localNdef.close();
        return false;
      }
      if (localNdef.getMaxSize() < i)
      {
        localNdef.close();
        return false;
      }
      if (this.mSetReadOnly)
      {
        if (!localNdef.makeReadOnly())
        {
          Toast.makeText(this.mActivityParent, this.mParcelHelper.getIdentifier("xpc_nfc_readonly_error", "string"), 1).show();
          localNdef.close();
          return false;
        }
      }
      else
        localNdef.writeNdefMessage(paramNdefMessage);
      localNdef.close();
      return true;
    }
    catch (Exception localException)
    {
    }
    return false;
  }
}