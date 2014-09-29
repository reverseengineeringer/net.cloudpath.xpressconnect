package net.cloudpath.xpressconnect.screens;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build.VERSION;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.io.PrintStream;
import net.cloudpath.xpressconnect.FailureReason;
import net.cloudpath.xpressconnect.MdmDetection;
import net.cloudpath.xpressconnect.Testing;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import net.cloudpath.xpressconnect.parsers.config.NetworkConfigParser;

public class GotoScreen
{
  public static final int ACTIVITY_ALREADY_CONFIGURED = 12;
  public static final int ACTIVITY_BAD_CONFIG = 13;
  public static final int ACTIVITY_CHECK_NOTIFICATIONS = 10;
  public static final int ACTIVITY_CONFIGURE_CLIENT = 5;
  public static final int ACTIVITY_CONNECT_CLIENT = 8;
  public static final int ACTIVITY_ENFORCE_MDM_SETTINGS = 14;
  public static final int ACTIVITY_FAILURE = 7;
  public static final int ACTIVITY_FLOW_CONTROL = 19;
  public static final int ACTIVITY_GET_CREDENTIALS = 4;
  public static final int ACTIVITY_LOAD_CONFIG = 1;
  public static final int ACTIVITY_MAX = 25;
  public static final int ACTIVITY_MIN = 1;
  public static final int ACTIVITY_MUST_INSTALL = 15;
  public static final int ACTIVITY_NFC_PROGRAMMER = 16;
  public static final int ACTIVITY_PICK_CUSTOMER = 1;
  public static final int ACTIVITY_PICK_NETWORK = 2;
  public static final int ACTIVITY_PICK_SERVICE = 18;
  public static final int ACTIVITY_PRECONFIGURL = 9;
  public static final int ACTIVITY_RESET_CREDENTIAL_STORE = 24;
  public static final int ACTIVITY_SET_CREDENTIAL_STORE = 23;
  public static final int ACTIVITY_SHOW_CHANGES = 21;
  public static final int ACTIVITY_SHOW_LOGS = 17;
  public static final int ACTIVITY_SHOW_MESSAGE = 3;
  public static final int ACTIVITY_SHOW_REPORT_DATA = 20;
  public static final int ACTIVITY_STARTUP_PROBLEM = 11;
  public static final int ACTIVITY_SUCCESS = 6;

  public static void alreadyConfiguredCheck(Activity paramActivity)
  {
    fireIntent(paramActivity, ShowAlreadyConfigured.class, 12);
  }

  public static void badConfig(Activity paramActivity, String paramString)
  {
    if (!Testing.areTesting)
    {
      Intent localIntent = new Intent(paramActivity, BadConfigScreen.class);
      localIntent.putExtra("selected", paramString);
      System.out.println(".... Starting BadConfig activity ....");
      paramActivity.startActivityForResult(localIntent, 13);
    }
    ((ScreenBase)paramActivity).transitionTo(13);
  }

  public static void browserScreen(Activity paramActivity, String paramString)
  {
    if (!Testing.areTesting)
    {
      Intent localIntent = new Intent(paramActivity, BrowserScreen.class);
      if (paramString != null)
        localIntent.putExtra("url", paramString);
      System.out.println(".... Starting BrowserScreen activity ....");
      paramActivity.startActivityForResult(localIntent, 19);
    }
    ((ScreenBase)paramActivity).transitionTo(19);
  }

  public static void configFailure(Activity paramActivity, int paramInt)
  {
    if (!Testing.areTesting)
    {
      Intent localIntent = new Intent(paramActivity, ConfigFailure.class);
      localIntent.putExtra("failReason", paramInt);
      System.out.println(".... Starting ConfigFailure activity ....");
      paramActivity.startActivityForResult(localIntent, 7);
    }
    ((ScreenBase)paramActivity).transitionTo(7);
  }

  public static void configSuccess(Activity paramActivity)
  {
    fireIntent(paramActivity, ConfigSuccess.class, 6);
  }

  public static void configureClient(Activity paramActivity)
  {
    fireIntent(paramActivity, ConfigureClient.class, 5);
  }

  public static void connectClient(Activity paramActivity)
  {
    fireIntent(paramActivity, ConnectClient.class, 8);
  }

  public static void enforceMdmSettings(Activity paramActivity, Logger paramLogger, NetworkConfigParser paramNetworkConfigParser, ParcelHelper paramParcelHelper, FailureReason paramFailureReason)
  {
    if (Build.VERSION.SDK_INT < 8)
    {
      if (MdmDetection.needToConfigureMdm(paramNetworkConfigParser) == true)
      {
        paramFailureReason.setFailReason(FailureReason.useErrorIcon, paramActivity.getResources().getString(paramParcelHelper.getIdentifier("xpc_mdm_password_invalid", "string")), 12);
        configFailure(paramActivity, 35);
        return;
      }
      Util.log(paramLogger, "Running on a pre-2.2 OS version.  Skipping MDM portion.");
      configureClient(paramActivity);
      return;
    }
    fireIntent(paramActivity, EnforceMdmScreen.class, 14);
  }

  public static void fireIntent(Activity paramActivity, Class<?> paramClass, int paramInt)
  {
    if (!Testing.areTesting)
    {
      Intent localIntent = new Intent(paramActivity, paramClass);
      System.out.println(".... Starting " + paramClass.getName() + " activity ....");
      paramActivity.startActivityForResult(localIntent, paramInt);
    }
    try
    {
      ((ScreenBase)paramActivity).transitionTo(paramInt);
      return;
    }
    catch (NullPointerException localNullPointerException)
    {
      Util.log(null, "Unable to call transitionTo() method.");
    }
  }

  @SuppressLint({"InlinedApi"})
  @TargetApi(11)
  public static void flowControl(Activity paramActivity, String paramString1, String paramString2, String paramString3, String paramString4)
  {
    if (!Testing.areTesting)
    {
      Intent localIntent = new Intent(paramActivity, FlowControl.class);
      localIntent.setFlags(32768);
      localIntent.setFlags(67108864);
      if (paramString1 != null)
        localIntent.putExtra("url", paramString1);
      if (paramString2 != null)
        localIntent.putExtra("name", paramString2);
      if (paramString3 != null)
        localIntent.putExtra("username", paramString3);
      if (paramString4 != null)
        localIntent.putExtra("password", paramString4);
      System.out.println(".... Starting FlowControl activity ....");
      paramActivity.startActivityForResult(localIntent, 19);
    }
    ((ScreenBase)paramActivity).transitionTo(19);
  }

  public static void getCredentials(Activity paramActivity)
  {
    fireIntent(paramActivity, GetCredentials.class, 4);
  }

  public static void loadConfig(Activity paramActivity, String paramString1, String paramString2)
  {
    if (!Testing.areTesting)
    {
      Intent localIntent = new Intent(paramActivity, LoadConfig.class);
      if (paramString1 != null)
        localIntent.putExtra("username", paramString1);
      if (paramString2 != null)
        localIntent.putExtra("password", paramString2);
      System.out.println(".... Starting LoadConfig activity ....");
      paramActivity.startActivityForResult(localIntent, 1);
    }
    ((ScreenBase)paramActivity).transitionTo(1);
  }

  public static void mustInstall(Activity paramActivity, String paramString)
  {
    if (!Testing.areTesting)
    {
      Intent localIntent = new Intent(paramActivity, MustInstallScreen.class);
      localIntent.putExtra("selected", paramString);
      System.out.println(".... Starting MustInstall activity ....");
      paramActivity.startActivityForResult(localIntent, 15);
    }
    ((ScreenBase)paramActivity).transitionTo(15);
  }

  public static void nfcProgrammer(Activity paramActivity)
  {
    fireIntent(paramActivity, NfcWriterScreen.class, 16);
  }

  public static void notifyCheck(Activity paramActivity)
  {
    fireIntent(paramActivity, Notify.class, 10);
  }

  public static void pickNetwork(Activity paramActivity)
  {
    fireIntent(paramActivity, PickNetwork.class, 2);
  }

  public static void pickService(Activity paramActivity)
  {
    fireIntent(paramActivity, PickService.class, 18);
  }

  public static void preConfigUrl(Activity paramActivity)
  {
    fireIntent(paramActivity, PreConfigUrl.class, 9);
  }

  public static void resetCredentialStore(Activity paramActivity)
  {
    fireIntent(paramActivity, ResetCredentialStore.class, 24);
  }

  public static void setCredentialStore(Activity paramActivity)
  {
    fireIntent(paramActivity, SetCredentialStore.class, 23);
  }

  public static void showChanges(Activity paramActivity)
  {
    fireIntent(paramActivity, ShowChangesScreen.class, 21);
  }

  public static void showLogs(Activity paramActivity)
  {
    fireIntent(paramActivity, ShowLogs.class, 17);
  }

  public static void showMessage(Activity paramActivity)
  {
    fireIntent(paramActivity, ShowMessage.class, 3);
  }

  public static void showMessage(Activity paramActivity, String paramString)
  {
    Intent localIntent = new Intent(paramActivity, ShowMessage.class);
    localIntent.putExtra("static_message", paramString);
    System.out.println(".... Starting ShowMessage activity ....");
    paramActivity.startActivityForResult(localIntent, 3);
    ((ScreenBase)paramActivity).transitionTo(3);
  }

  public static void showReportData(Activity paramActivity)
  {
    fireIntent(paramActivity, ShowReportData.class, 20);
  }

  public static void startupProblem(Activity paramActivity)
  {
    fireIntent(paramActivity, StartupProblem.class, 11);
  }
}