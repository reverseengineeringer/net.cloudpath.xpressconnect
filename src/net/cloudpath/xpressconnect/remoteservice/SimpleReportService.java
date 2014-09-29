package net.cloudpath.xpressconnect.remoteservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import net.cloudpath.xpressconnect.LocalDbHelper;
import net.cloudpath.xpressconnect.remote.WebInterface;

public class SimpleReportService extends WakefulIntentService
{
  private String destinationServer = "https://reporting.cloudpath.net";

  public SimpleReportService()
  {
    super("XPCReportService");
  }

  private void cancelService()
  {
    Log.i("XPCReportService", "Cancelling service.");
    PendingIntent localPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, OnAlarmReceiver.class), 0);
    ((AlarmManager)getSystemService("alarm")).cancel(localPendingIntent);
  }

  private boolean getDataToSend()
  {
    int i = -1;
    LocalDbHelper localLocalDbHelper = new LocalDbHelper(this, "XPC_DB", null, 8);
    SQLiteDatabase localSQLiteDatabase = localLocalDbHelper.getWritableDatabase();
    Cursor localCursor1 = localSQLiteDatabase.rawQuery("SELECT * from reportingData;", null);
    boolean bool1 = localCursor1.moveToFirst();
    Object localObject = null;
    if (bool1 == true);
    try
    {
      i = localCursor1.getInt(localCursor1.getColumnIndexOrThrow("id"));
      String str1 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("signature"));
      int j = localCursor1.getInt(localCursor1.getColumnIndexOrThrow("state"));
      int k = localCursor1.getInt(localCursor1.getColumnIndexOrThrow("failIssue"));
      String str2 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("version"));
      int m = localCursor1.getInt(localCursor1.getColumnIndexOrThrow("connect_only"));
      String str3 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("time"));
      String str4 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("result_string"));
      int n = localCursor1.getInt(localCursor1.getColumnIndexOrThrow("workarounds"));
      String str5 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("deviceHash"));
      String str6 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("organization"));
      String str7 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("selectedNet"));
      String str8 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("dd_board"));
      String str9 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("dd_brand"));
      String str10 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("dd_abi"));
      String str11 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("dd_device"));
      String str12 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("dd_display"));
      String str13 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("dd_fingerprint"));
      String str14 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("dd_host"));
      String str15 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("dd_id"));
      String str16 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("dd_manufacturer"));
      String str17 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("dd_model"));
      String str18 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("dd_product"));
      String str19 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("dd_tags"));
      String str20 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("dd_time"));
      String str21 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("dd_type"));
      String str22 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("dd_user"));
      String str23 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("dd_codename"));
      String str24 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("dd_incremental"));
      String str25 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("dd_release"));
      String str26 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("dd_sdk"));
      String str27 = localCursor1.getString(localCursor1.getColumnIndexOrThrow("logdata"));
      String str28 = "<AndroidReport><Signature>" + str1 + "</Signature>" + "<State>" + j + "</State>" + "<FailIssue>" + k + "</FailIssue>" + "<Version>" + str2 + "</Version>" + "<ConnectOnly>" + m + "</ConnectOnly>" + "<Time>" + str3 + "</Time>" + "<Result_Str>" + str4 + "</Result_Str>" + "<Workarounds>" + n + "</Workarounds>" + "<Device_Hash>" + str5 + "</Device_Hash>" + "<Org>" + str6 + "</Org>" + "<SelectedNet>" + str7 + "</SelectedNet>" + "<DD_Board>" + str8 + "</DD_Board>" + "<DD_Brand>" + str9 + "</DD_Brand>" + "<DD_ABI>" + str10 + "</DD_ABI>" + "<DD_Device>" + str11 + "</DD_Device>" + "<DD_Display>" + str12 + "</DD_Display>" + "<DD_Fingerprint>" + str13 + "</DD_Fingerprint>" + "<DD_Host>" + str14 + "</DD_Host>" + "<DD_ID>" + str15 + "</DD_ID>" + "<DD_Manufacturer>" + str16 + "</DD_Manufacturer>" + "<DD_Model>" + str17 + "</DD_Model>" + "<DD_Product>" + str18 + "</DD_Product>" + "<DD_Tags>" + str19 + "</DD_Tags>" + "<DD_Time>" + str20 + "</DD_Time>" + "<DD_Type>" + str21 + "</DD_Type>" + "<DD_User>" + str22 + "</DD_User>" + "<DD_Codename>" + str23 + "</DD_Codename>" + "<DD_Incremental>" + str24 + "</DD_Incremental>" + "<DD_Release>" + str25 + "</DD_Release>" + "<DD_SDK>" + str26 + "</DD_SDK>" + "<LogData>" + str27 + "</LogData>" + "</AndroidReport>";
      localObject = str28;
      localCursor1.close();
      boolean bool2 = uploadData(localObject);
      if (bool2 == true)
      {
        localSQLiteDatabase.execSQL("DELETE FROM reportingData WHERE id=" + i + ";");
        Cursor localCursor2 = localSQLiteDatabase.rawQuery("SELECT * from reportingData;", null);
        if (localCursor2.moveToFirst() == true)
        {
          bool2 = false;
          Log.i("XPCReportService", "More data in the database to send.  Not terminating the service.");
        }
        localCursor2.close();
      }
      localSQLiteDatabase.close();
      localLocalDbHelper.close();
      return bool2;
    }
    catch (Exception localException)
    {
      while (true)
      {
        Log.e("XPCReportService", "Exception while gathering database info... Exception : " + localException.getMessage());
        localObject = null;
      }
    }
  }

  private boolean uploadData(String paramString)
  {
    WebInterface localWebInterface = new WebInterface(null);
    localWebInterface.getWebPage(this.destinationServer + "/pipe/storeAndroidData.php", "xmlData=" + paramString, "", "", false, true, null, true);
    if (localWebInterface.resultCode != 200)
    {
      Log.e("XPCReportService", "Unable to send data to https server.");
      return false;
    }
    return true;
  }

  protected void onHandleIntent(Intent paramIntent)
  {
    Log.i("XPCReportService", "Attempting to send reporting data...");
    if (getDataToSend())
    {
      Log.i("XPCReportService", "Reporting data sent.");
      cancelService();
    }
    super.onHandleIntent(paramIntent);
  }
}