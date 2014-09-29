package net.cloudpath.xpressconnect;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.commonsware.cwac.parcel.ParcelHelper;
import java.io.ByteArrayInputStream;
import net.cloudpath.xpressconnect.localservice.GetGlobals;
import net.cloudpath.xpressconnect.logger.Logger;

public class LoadConfigFromDb
{
  private Context mContext = null;
  private GetGlobals mGlobals = null;
  private Logger mLogger = null;
  public String mResultText = null;

  public LoadConfigFromDb(Logger paramLogger, GetGlobals paramGetGlobals, Context paramContext)
  {
    this.mLogger = paramLogger;
    this.mContext = paramContext;
    this.mGlobals = paramGetGlobals;
  }

  public String getSystemConfigXml(String paramString)
  {
    Util.log(this.mLogger, "--- Attempting to read system config from local database. (url = " + this.mGlobals.getLoadedFromUrl() + "     name = " + paramString + ")");
    LocalDbHelper localLocalDbHelper = new LocalDbHelper(this.mContext, "XPC_DB", null, 8);
    SQLiteDatabase localSQLiteDatabase = localLocalDbHelper.getReadableDatabase();
    Cursor localCursor;
    int i;
    String str;
    if (paramString == null)
    {
      localCursor = localSQLiteDatabase.rawQuery("select * from configuredNetworks where url=\"" + this.mGlobals.getLoadedFromUrl() + "\";", null);
      i = localCursor.getColumnIndex("systemConfig");
      Util.log(this.mLogger, "--- Found " + localCursor.getCount() + " row(s)");
      if (localCursor.isBeforeFirst() != true)
        break label298;
      if (localCursor.moveToNext())
        break label284;
      if (localCursor.getCount() > 0)
        Util.log(this.mLogger, "Couldn't access one of the returned rows from the database.");
      Util.log(this.mLogger, "No configuration data read from the local database.");
      str = null;
    }
    while (true)
    {
      localCursor.close();
      localSQLiteDatabase.close();
      localLocalDbHelper.close();
      return str;
      Log.d("XPC", "Looking with a name in mind.");
      localCursor = localSQLiteDatabase.rawQuery("select * from configuredNetworks where url=\"" + this.mGlobals.getLoadedFromUrl() + "\" and name=\"" + paramString + "\";", null);
      break;
      label284: str = localCursor.getString(i);
      continue;
      label298: Util.log(this.mLogger, "No results returned from local query.");
      str = null;
    }
  }

  public boolean loadFromDb(String paramString1, String paramString2)
  {
    if (paramString1 == null)
    {
      Util.log(this.mLogger, "No target URL defined.  Won't be able to load config by URL.");
      return false;
    }
    Util.log(this.mLogger, "--- Attempting to read config from local database.");
    LocalDbHelper localLocalDbHelper = new LocalDbHelper(this.mContext, "XPC_DB", null, 8);
    SQLiteDatabase localSQLiteDatabase = localLocalDbHelper.getReadableDatabase();
    Cursor localCursor;
    int i;
    boolean bool2;
    if (paramString2 == null)
    {
      localCursor = localSQLiteDatabase.rawQuery("select * from configuredNetworks where url=\"" + paramString1 + "\";", null);
      i = localCursor.getColumnIndex("lastConfig");
      boolean bool1 = localCursor.isBeforeFirst();
      bool2 = false;
      if (bool1 == true)
      {
        if (localCursor.moveToNext())
          break label262;
        if (localCursor.getCount() > 0)
          Util.log(this.mLogger, "Couldn't access one of the returned rows from the database.");
        Util.log(this.mLogger, "No configuration data read from the local database.");
        ParcelHelper localParcelHelper2 = new ParcelHelper("", this.mContext);
        this.mResultText = this.mContext.getResources().getString(localParcelHelper2.getIdentifier("xpc_no_data_to_read", "string"));
        bool2 = false;
      }
    }
    while (true)
    {
      localCursor.close();
      localSQLiteDatabase.close();
      localLocalDbHelper.close();
      return bool2;
      Log.d("XPC", "Looking with a name in mind.");
      localCursor = localSQLiteDatabase.rawQuery("select * from configuredNetworks where url=\"" + paramString1 + "\" and name=\"" + paramString2 + "\";", null);
      break;
      label262: String str1 = localCursor.getString(i);
      if (str1 != null)
      {
        this.mGlobals.setXmlConfig(str1);
        Util.log(this.mLogger, "Pulled configuration data from the database.");
        String str2 = localCursor.getString(localCursor.getColumnIndex("name"));
        Util.log(this.mLogger, "Found netname : " + str2);
        int j = localCursor.getColumnIndex("url");
        this.mGlobals.setLoadedFromUrl(localCursor.getString(j));
        int k = localCursor.getColumnIndex("isEs");
        if ((k >= 0) && (localCursor.getInt(k) == 1))
          this.mGlobals.setIsEs(true);
        if (getSystemConfigXml(str2) == null)
          Util.log(this.mLogger, "No system config xml stored in the local database.  Skipping customization.");
        while (true)
        {
          bool2 = true;
          break;
          if (!localLoadBrandingImage(str2))
            Util.log(this.mLogger, "Unable to load cached branding image.");
          else
            Util.log(this.mLogger, "Cached branding image loaded.");
        }
      }
      ParcelHelper localParcelHelper1 = new ParcelHelper("", this.mContext);
      this.mResultText = this.mContext.getResources().getString(localParcelHelper1.getIdentifier("xpc_no_data_to_read", "string"));
      bool2 = false;
    }
  }

  public boolean loadOnlyConfig()
  {
    LocalDbHelper localLocalDbHelper = new LocalDbHelper(this.mContext, "XPC_DB", null, 8);
    try
    {
      SQLiteDatabase localSQLiteDatabase = localLocalDbHelper.getReadableDatabase();
      Cursor localCursor = localSQLiteDatabase.rawQuery("select url from configuredNetworks;", null);
      int i = localCursor.getCount();
      boolean bool1 = false;
      if (i == 1)
      {
        boolean bool2 = localCursor.moveToFirst();
        bool1 = false;
        if (bool2 == true)
          bool1 = loadFromDb(localCursor.getString(localCursor.getColumnIndex("url")), null);
      }
      localCursor.close();
      localSQLiteDatabase.close();
      localLocalDbHelper.close();
      return bool1;
    }
    catch (NullPointerException localNullPointerException)
    {
      localNullPointerException.printStackTrace();
      Util.log(this.mLogger, "NPE attempting to get a readable database.");
    }
    return false;
  }

  public boolean localLoadBrandingImage(String paramString)
  {
    Util.log(this.mLogger, "--- Attempting to read branding image from local database.");
    LocalDbHelper localLocalDbHelper = new LocalDbHelper(this.mContext, "XPC_DB", null, 8);
    SQLiteDatabase localSQLiteDatabase = localLocalDbHelper.getReadableDatabase();
    if (paramString == null);
    int i;
    for (Cursor localCursor = localSQLiteDatabase.rawQuery("select * from configuredNetworks where url=\"" + this.mGlobals.getLoadedFromUrl() + "\";", null); ; localCursor = localSQLiteDatabase.rawQuery("select * from configuredNetworks where url=\"" + this.mGlobals.getLoadedFromUrl() + "\" and name=\"" + paramString + "\";", null))
    {
      i = localCursor.getColumnIndex("brandImage");
      Util.log(this.mLogger, "--- Found " + localCursor.getCount() + " row(s)");
      if (localCursor.isBeforeFirst() != true)
        break label325;
      if (localCursor.moveToNext())
        break;
      if (localCursor.getCount() > 0)
        Util.log(this.mLogger, "Couldn't access one of the returned rows from the database.");
      Util.log(this.mLogger, "No configuration data read from the local database.");
      localCursor.close();
      localSQLiteDatabase.close();
      localLocalDbHelper.close();
      return false;
      Log.d("XPC", "Looking with a name in mind.");
    }
    byte[] arrayOfByte = localCursor.getBlob(i);
    if (arrayOfByte == null)
    {
      localCursor.close();
      localSQLiteDatabase.close();
      localLocalDbHelper.close();
      return false;
    }
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
    this.mGlobals.setBrandingImg(BitmapFactory.decodeStream(localByteArrayInputStream));
    Util.log(this.mLogger, "*** Branding image set (from local).");
    localCursor.close();
    localSQLiteDatabase.close();
    localLocalDbHelper.close();
    return true;
    label325: Util.log(this.mLogger, "No results returned from local query.");
    localCursor.close();
    localSQLiteDatabase.close();
    localLocalDbHelper.close();
    return false;
  }
}