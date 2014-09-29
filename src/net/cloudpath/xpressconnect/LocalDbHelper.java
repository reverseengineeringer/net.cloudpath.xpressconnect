package net.cloudpath.xpressconnect;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class LocalDbHelper extends SQLiteOpenHelper
{
  public LocalDbHelper(Context paramContext, String paramString, SQLiteDatabase.CursorFactory paramCursorFactory, int paramInt)
  {
    super(paramContext, paramString, paramCursorFactory, paramInt);
  }

  public static void dumpDbToSdCard(Activity paramActivity)
  {
    try
    {
      File localFile1 = Environment.getExternalStorageDirectory();
      File localFile2 = Environment.getDataDirectory();
      if (localFile1.canWrite())
      {
        File localFile3 = new File(localFile2, "/data/net.cloudpath.xpressconnect/databases/XPC_DB");
        File localFile4 = new File(localFile1, "xpc.db");
        if (localFile3.exists())
        {
          FileInputStream localFileInputStream = new FileInputStream(localFile3);
          FileOutputStream localFileOutputStream = new FileOutputStream(localFile4);
          FileChannel localFileChannel1 = localFileInputStream.getChannel();
          FileChannel localFileChannel2 = localFileOutputStream.getChannel();
          localFileChannel2.transferFrom(localFileChannel1, 0L, localFileChannel1.size());
          localFileChannel1.close();
          localFileChannel2.close();
          localFileInputStream.close();
          localFileOutputStream.close();
          if (!Testing.areTesting)
            Toast.makeText(paramActivity, "Database written to SD card.", 1).show();
        }
        else if (!Testing.areTesting)
        {
          Toast.makeText(paramActivity, "No database to copy to SD card.", 1).show();
          return;
        }
      }
    }
    catch (Exception localException)
    {
      if (!Testing.areTesting)
        Toast.makeText(paramActivity, "Failed to copy database to SD card.", 1).show();
    }
  }

  public void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
    paramSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS configuredNetworks (name VARCHAR, url VARCHAR, isEs INTEGER, lastConfig VARCHAR, systemConfig VARCHAR, brandImage BLOB);");
    paramSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS certificatesInstalled (name VARCHAR, pemCert VARCHAR);");
    paramSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS reportingData (id INTEGER PRIMARY KEY, signature VARCHAR, state INTEGER, failIssue INTEGER, version VARCHAR, connect_only INTEGER, time VARCHAR, result_string VARCHAR, workarounds INTEGER, deviceHash VARCHAR, organization VARCHAR, selectedNet VARCHAR, dd_board VARCHAR, dd_brand VARCHAR, dd_abi VARCHAR, dd_device VARCHAR, dd_display VARCHAR, dd_fingerprint VARCHAR, dd_host VARCHAR, dd_id VARCHAR, dd_manufacturer VARCHAR, dd_model VARCHAR, dd_product VARCHAR, dd_tags VARCHAR, dd_time VARCHAR, dd_type VARCHAR, dd_user VARCHAR, dd_codename VARCHAR, dd_incremental VARCHAR, dd_release VARCHAR, dd_sdk VARCHAR, logdata VARCHAR);");
  }

  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
  {
    if ((paramInt1 == 1) && (paramInt2 > 1))
      paramSQLiteDatabase.execSQL("ALTER TABLE configuredNetworks ADD COLUMN lastConfig VARCHAR;");
    if ((paramInt1 <= 2) && (paramInt2 > 2))
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE configuredNetworks ADD COLUMN systemConfig VARCHAR;");
      paramSQLiteDatabase.execSQL("ALTER TABLE configuredNetworks ADD COLUMN brandImage BLOB;");
    }
    if ((paramInt1 <= 3) && (paramInt2 > 3))
    {
      Log.d("XPC", "Upgrading DB from version 3.");
      paramSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS reportingData (id INTEGER PRIMARY KEY, signature VARCHAR, state INTEGER, version VARCHAR, connect_only INTEGER, time VARCHAR, result_string VARCHAR, workarounds INTEGER, deviceHash VARCHAR, organization VARCHAR, selectedNet VARCHAR, dd_board VARCHAR, dd_brand VARCHAR, dd_abi VARCHAR, dd_device VARCHAR, dd_display VARCHAR, dd_fingerprint VARCHAR, dd_host VARCHAR, dd_id VARCHAR, dd_manufacturer VARCHAR, dd_model VARCHAR, dd_product VARCHAR, dd_tags VARCHAR, dd_time VARCHAR, dd_type VARCHAR, dd_user VARCHAR, dd_codename VARCHAR, dd_incremental VARCHAR, dd_release VARCHAR, dd_sdk VARCHAR, logdata VARCHAR);");
    }
    if ((paramInt1 <= 4) && (paramInt2 > 4))
      Log.d("XPC", "Upgrading DB from version 4.");
    try
    {
      paramSQLiteDatabase.execSQL("ALTER TABLE reportingData ADD COLUMN workaround INTEGER after result_string;");
      if ((paramInt1 <= 5) && (paramInt2 > 5))
        Log.d("XPC", "Upgrading DB from version 5.");
    }
    catch (Exception localException2)
    {
      try
      {
        paramSQLiteDatabase.execSQL("ALTER TABLE reportingData ADD COLUMN failIssue INTEGER after state;");
        if ((paramInt1 <= 6) && (paramInt2 > 6))
          Log.d("XPC", "Upgrading DB from version 6.");
      }
      catch (Exception localException2)
      {
        try
        {
          paramSQLiteDatabase.execSQL("ALTER TABLE configuredNetworks ADD COLUMN isEs INTEGER after url;");
          if ((paramInt1 <= 7) && (paramInt2 > 7))
            Log.d("XPC", "Upgrading DB from version 7.");
        }
        catch (Exception localException2)
        {
          try
          {
            while (true)
            {
              paramSQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS certificatesInstalled (name VARCHAR, pemCert VARCHAR);");
              return;
              localException4 = localException4;
              localException4.printStackTrace();
              continue;
              localException3 = localException3;
              localException3.printStackTrace();
            }
            localException2 = localException2;
            localException2.printStackTrace();
          }
          catch (Exception localException1)
          {
            localException1.printStackTrace();
          }
        }
      }
    }
  }

  public class DBStatics
  {
    public static final String DB_NAME = "XPC_DB";
    public static final int DB_VERSION = 8;

    public DBStatics()
    {
    }
  }
}