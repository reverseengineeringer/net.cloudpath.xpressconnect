package net.cloudpath.xpressconnect.screens;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.commonsware.cwac.parcel.ParcelHelper;
import net.cloudpath.xpressconnect.AndroidVersion;
import net.cloudpath.xpressconnect.ConfigCheck;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.GestureCallback;
import net.cloudpath.xpressconnect.LocalDbHelper;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.localservice.EncapsulationService;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.localservice.ServiceBoundCallback;
import net.cloudpath.xpressconnect.nativeproxy.WifiConfigurationProxy;
import net.cloudpath.xpressconnect.parsers.SavedConfigInfo;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;

public class ShowAlreadyConfigured extends ScreenBase
  implements ServiceBoundCallback, View.OnClickListener, GestureCallback
{
  private Button configButton = null;
  private Button connectButton = null;
  private int intentResult = -1;
  private TextView mMessageText = null;
  private WifiConfigurationProxy mNetwork = null;

  private void checkHidden()
  {
    String str = Util.getSSID(this.mLogger, this.mParser);
    WifiConfigurationProxy localWifiConfigurationProxy;
    try
    {
      localWifiConfigurationProxy = WifiConfigurationProxy.findNetworkBySsid(str, this.mWifi, this.mLogger);
      if (localWifiConfigurationProxy == null)
      {
        Util.log(this.mLogger, "Attempt to check if an SSID is hidden when the SSID isn't already configured but should be?");
        return;
      }
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      localIllegalArgumentException.printStackTrace();
      Util.log(this.mLogger, "Illegal argument exception in ShowAlreadyConfigured::checkhidden().");
      return;
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      localNoSuchFieldException.printStackTrace();
      Util.log(this.mLogger, "No such field excepotion in ShowAlreadyConfigured::checkHidden().");
      return;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      localClassNotFoundException.printStackTrace();
      Util.log(this.mLogger, "Class not found exception in ShowAlreadyConfigured::checkHidden().");
      return;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      localIllegalAccessException.printStackTrace();
      Util.log(this.mLogger, "Illegal access exception in ShowAlreadyConfigured::checkHidden().");
      return;
    }
    if (localWifiConfigurationProxy.getHiddenSsid() == true)
    {
      Util.log(this.mLogger, "**** Target SSID is hidden!");
      return;
    }
    Util.log(this.mLogger, "**** Unable to find target SSID.  The hidden state of this SSID is unknown.");
  }

  private void checkKeyStoreResult(int paramInt)
  {
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "Parser is null in checkKeyStoreResult().");
      return;
    }
    if ((paramInt != 0) || (needToUnlockKeyStore() == true))
    {
      Toast.makeText(this, "The keystore could not be unlocked.", 1).show();
      return;
    }
    if (this.mParser == null)
    {
      Util.log(this.mLogger, "The parser was invalid.");
      if (this.mFailure != null)
        this.mFailure.setFailReason(FailureReason.useWarningIcon, getApplicationContext().getString(this.mParcelHelper.getIdentifier("xpc_parser_invalid", "string")), 5);
      done(15);
      return;
    }
    if (this.mParser.savedConfigInfo == null)
    {
      Util.log(this.mLogger, "The config info was invalid.");
      if (this.mFailure != null)
        this.mFailure.setFailReason(FailureReason.useWarningIcon, getApplicationContext().getString(this.mParcelHelper.getIdentifier("xpc_config_invalid", "string")), 5);
      done(15);
      return;
    }
    this.mParser.savedConfigInfo.connectOnly = 1;
    Util.log(this.mLogger, "--- Leaving ShowAlreadyConfigured following unlock of the keystore.");
    done(24);
  }

  private void doConnectOnly()
  {
    if (((new AndroidVersion(this.mLogger).forceOpenKeystore(this.mParser)) || ((needToUnlockKeyStore()) && (this.mNetwork.getCaCert() != null) && (this.mNetwork.getCaCert().length() > 0))) && (needToUnlockKeyStore()))
    {
      Util.log(this.mLogger, "Showing the 'you need to unlock the keystore' dialog.");
      showMyDialog(ScreenBase.ScreenBaseFragments.newInstance(100), "unlockKeystoreDialog");
      return;
    }
    this.mParser.savedConfigInfo.connectOnly = 1;
    Util.log(this.mLogger, "--- Leaving ShowAlreadyConfigured because we just want to connect.");
    done(24);
  }

  public boolean areConfigured()
  {
    if (!isSsidConfigured())
      return false;
    ConfigCheck localConfigCheck = new ConfigCheck(this.mParser, this.mLogger, this.mNetwork, this.mFailure, this);
    if (!localConfigCheck.ssidSettingsAreCorrect())
      return false;
    if (localConfigCheck.isTls())
    {
      if (this.mGlobals.getCredsPushedIn())
      {
        Util.log(this.mLogger, "Already configured, but credentials were passed in.  Will reconfigure.");
        return false;
      }
      this.connectButton.setText(getResources().getString(this.mParcelHelper.getIdentifier("xpc_use_existing_creds", "string")));
      if (this.mGlobals.getIsEs() != true)
        break label144;
      this.configButton.setVisibility(8);
      this.mMessageText.setText(getResources().getString(this.mParcelHelper.getIdentifier("xpc_cant_generate_credentials", "string")));
    }
    while (true)
    {
      return true;
      label144: this.configButton.setText(getResources().getString(this.mParcelHelper.getIdentifier("xpc_generate_new_creds", "string")));
      this.mMessageText.setText(getResources().getString(this.mParcelHelper.getIdentifier("xpc_already_configured_message_with_gen_creds", "string")));
    }
  }

  public void handleDumpDatabase()
  {
    LocalDbHelper.dumpDbToSdCard(this);
  }

  public void handleNfcProgrammerGesture()
  {
    Toast.makeText(this, "Starting NFC Programmer...", 0).show();
    done(39);
  }

  public boolean isSsidConfigured()
  {
    String str = Util.getSSID(this.mLogger, this.mParser);
    if (str == null);
    while (true)
    {
      return false;
      try
      {
        WifiConfigurationProxy localWifiConfigurationProxy = WifiConfigurationProxy.findNetworkBySsid(str, this.mWifi, this.mLogger);
        if (localWifiConfigurationProxy != null)
        {
          this.mNetwork = localWifiConfigurationProxy;
          return true;
        }
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        localIllegalArgumentException.printStackTrace();
        Util.log(this.mLogger, "Illegal argument exception in ShowAlreadyConfigured::isSsidConfigured()!");
        return false;
      }
      catch (NoSuchFieldException localNoSuchFieldException)
      {
        localNoSuchFieldException.printStackTrace();
        Util.log(this.mLogger, "No such field exception in ShowAlreadyConfigured::isSsidConfigured()!");
        return false;
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        localClassNotFoundException.printStackTrace();
        Util.log(this.mLogger, "Class not found exception in ShowAlreadyConfigured::isSsidConfigured()!");
        return false;
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        localIllegalAccessException.printStackTrace();
        Util.log(this.mLogger, "Illegal access exception in ShowAlreadyConfigured::isSsidConfigured()!");
      }
    }
    return false;
  }

  public boolean needToUnlockKeyStore()
  {
    AndroidVersion localAndroidVersion = new AndroidVersion(this.mLogger);
    return needToUnlockKeyStore(Build.VERSION.SDK_INT, localAndroidVersion);
  }

  // ERROR //
  public boolean needToUnlockKeyStore(int paramInt, AndroidVersion paramAndroidVersion)
  {
    // Byte code:
    //   0: iload_1
    //   1: bipush 14
    //   3: if_icmplt +15 -> 18
    //   6: aload_0
    //   7: getfield 46	net/cloudpath/xpressconnect/screens/ShowAlreadyConfigured:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   10: ldc_w 293
    //   13: invokestatic 72	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   16: iconst_0
    //   17: ireturn
    //   18: aload_0
    //   19: getfield 46	net/cloudpath/xpressconnect/screens/ShowAlreadyConfigured:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   22: aload_0
    //   23: getfield 50	net/cloudpath/xpressconnect/screens/ShowAlreadyConfigured:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   26: invokestatic 297	net/cloudpath/xpressconnect/Util:usingPsk	(Lnet/cloudpath/xpressconnect/logger/Logger;Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;)Z
    //   29: ifne -13 -> 16
    //   32: aload_0
    //   33: getfield 46	net/cloudpath/xpressconnect/screens/ShowAlreadyConfigured:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   36: aload_0
    //   37: getfield 50	net/cloudpath/xpressconnect/screens/ShowAlreadyConfigured:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   40: invokestatic 300	net/cloudpath/xpressconnect/Util:isOpenNetwork	(Lnet/cloudpath/xpressconnect/logger/Logger;Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;)Z
    //   43: ifne -27 -> 16
    //   46: aload_2
    //   47: aload_0
    //   48: getfield 50	net/cloudpath/xpressconnect/screens/ShowAlreadyConfigured:mParser	Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;
    //   51: invokevirtual 181	net/cloudpath/xpressconnect/AndroidVersion:forceOpenKeystore	(Lnet/cloudpath/xpressconnect/parsers/config/NetworkConfigParser;)Z
    //   54: ifeq -38 -> 16
    //   57: new 302	net/cloudpath/xpressconnect/KeyStore
    //   60: dup
    //   61: invokespecial 303	net/cloudpath/xpressconnect/KeyStore:<init>	()V
    //   64: astore_3
    //   65: aload_3
    //   66: invokevirtual 306	net/cloudpath/xpressconnect/KeyStore:test	()I
    //   69: istore 5
    //   71: iload 5
    //   73: iconst_1
    //   74: if_icmpeq +221 -> 295
    //   77: iload 5
    //   79: tableswitch	default:+49 -> 128, 2:+80->159, 3:+112->191, 4:+125->204, 5:+138->217, 6:+151->230, 7:+164->243, 8:+177->256, 9:+190->269, 10:+203->282
    //   129: getfield 46	net/cloudpath/xpressconnect/screens/ShowAlreadyConfigured:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   132: ldc_w 308
    //   135: invokestatic 72	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   138: iconst_1
    //   139: ireturn
    //   140: astore 6
    //   142: aload_0
    //   143: getfield 46	net/cloudpath/xpressconnect/screens/ShowAlreadyConfigured:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   146: ldc_w 310
    //   149: invokestatic 72	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   152: aload 6
    //   154: invokevirtual 311	java/lang/Exception:printStackTrace	()V
    //   157: iconst_0
    //   158: ireturn
    //   159: aload_0
    //   160: getfield 46	net/cloudpath/xpressconnect/screens/ShowAlreadyConfigured:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   163: ldc_w 313
    //   166: invokestatic 72	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   169: goto -31 -> 138
    //   172: astore 4
    //   174: aload_0
    //   175: getfield 46	net/cloudpath/xpressconnect/screens/ShowAlreadyConfigured:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   178: ldc_w 315
    //   181: invokestatic 72	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   184: aload 4
    //   186: invokevirtual 311	java/lang/Exception:printStackTrace	()V
    //   189: iconst_0
    //   190: ireturn
    //   191: aload_0
    //   192: getfield 46	net/cloudpath/xpressconnect/screens/ShowAlreadyConfigured:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   195: ldc_w 317
    //   198: invokestatic 72	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   201: goto -63 -> 138
    //   204: aload_0
    //   205: getfield 46	net/cloudpath/xpressconnect/screens/ShowAlreadyConfigured:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   208: ldc_w 319
    //   211: invokestatic 72	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   214: goto -76 -> 138
    //   217: aload_0
    //   218: getfield 46	net/cloudpath/xpressconnect/screens/ShowAlreadyConfigured:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   221: ldc_w 321
    //   224: invokestatic 72	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   227: goto -89 -> 138
    //   230: aload_0
    //   231: getfield 46	net/cloudpath/xpressconnect/screens/ShowAlreadyConfigured:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   234: ldc_w 323
    //   237: invokestatic 72	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   240: goto -102 -> 138
    //   243: aload_0
    //   244: getfield 46	net/cloudpath/xpressconnect/screens/ShowAlreadyConfigured:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   247: ldc_w 325
    //   250: invokestatic 72	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   253: goto -115 -> 138
    //   256: aload_0
    //   257: getfield 46	net/cloudpath/xpressconnect/screens/ShowAlreadyConfigured:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   260: ldc_w 327
    //   263: invokestatic 72	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   266: goto -128 -> 138
    //   269: aload_0
    //   270: getfield 46	net/cloudpath/xpressconnect/screens/ShowAlreadyConfigured:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   273: ldc_w 329
    //   276: invokestatic 72	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   279: goto -141 -> 138
    //   282: aload_0
    //   283: getfield 46	net/cloudpath/xpressconnect/screens/ShowAlreadyConfigured:mLogger	Lnet/cloudpath/xpressconnect/logger/Logger;
    //   286: ldc_w 331
    //   289: invokestatic 72	net/cloudpath/xpressconnect/Util:log	(Lnet/cloudpath/xpressconnect/logger/Logger;Ljava/lang/String;)V
    //   292: goto -154 -> 138
    //   295: iconst_0
    //   296: ireturn
    //
    // Exception table:
    //   from	to	target	type
    //   57	65	140	java/lang/Exception
    //   65	71	172	java/lang/Exception
    //   128	138	172	java/lang/Exception
    //   159	169	172	java/lang/Exception
    //   191	201	172	java/lang/Exception
    //   204	214	172	java/lang/Exception
    //   217	227	172	java/lang/Exception
    //   230	240	172	java/lang/Exception
    //   243	253	172	java/lang/Exception
    //   256	266	172	java/lang/Exception
    //   269	279	172	java/lang/Exception
    //   282	292	172	java/lang/Exception
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt2 == 29);
    while (paramInt1 != 1001)
      return;
    if (this.mServiceBound)
    {
      checkKeyStoreResult(paramInt2);
      return;
    }
    this.intentResult = paramInt2;
  }

  public void onBackPressed()
  {
    Util.log(this.mLogger, "--- Leaving ShowAlreadyConfigured screen because user pressed back.");
    done(-2);
    super.onBackPressed();
  }

  public void onClick(View paramView)
  {
    if (paramView == this.connectButton)
    {
      Util.log(this.mLogger, "(Connect button pressed) Connecting to an already configured network.");
      checkHidden();
      doConnectOnly();
    }
    while (paramView != this.configButton)
      return;
    Util.log(this.mLogger, "(Reconfig button pressed) Replacing the existing configuration with a new one.");
    done(23);
  }

  protected void onCreate(Bundle paramBundle)
  {
    ParcelHelper localParcelHelper = new ParcelHelper("", this);
    preContentView();
    setContentView(localParcelHelper.getLayoutId("xpc_already_configured"));
    super.onCreate(paramBundle);
    if (haveTabs())
      setWelcomeActive();
    this.connectButton = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_ConnectButton")));
    this.connectButton.setOnClickListener(this);
    this.configButton = ((Button)findViewById(this.mParcelHelper.getItemId("xpc_ContinueConfig")));
    this.configButton.setOnClickListener(this);
    this.mMessageText = ((TextView)findViewById(this.mParcelHelper.getItemId("xpc_MessageText")));
    setGestureCallback(this);
  }

  public void onServiceBind(EncapsulationService paramEncapsulationService)
  {
    if (paramEncapsulationService == null)
      Log.d("XPC", "Unable to bind to local service!\n");
    do
    {
      return;
      super.onServiceBind(paramEncapsulationService);
      if (this.mParser == null)
        Util.log(this.mLogger, "Config was not properly bound!");
      Util.log(this.mLogger, "+++ Showing ShowAlreadyConfigured screen.");
      if (haveTabs())
        setWelcomeActive();
      if (this.intentResult != -1)
        checkKeyStoreResult(this.intentResult);
    }
    while ((areConfigured()) && (this.mParser.savedConfigInfo.mBeenThroughOnce != true));
    Util.log(this.mLogger, "This network isn't configured yet. (Or we are using a chained network.)  Will configure it.");
    setMyResult(23);
    finish();
  }
}