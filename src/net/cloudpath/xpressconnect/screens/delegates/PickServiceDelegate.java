package net.cloudpath.xpressconnect.screens.delegates;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import net.cloudpath.xpressconnect.LocalDbHelper;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.retention.MainDataRetention;

public class PickServiceDelegate extends DelegateBase
{
  public ArrayList<MainDataRetention> data = null;
  private boolean mAsLibrary = false;
  private SQLiteDatabase mMyDb = null;
  private LocalDbHelper myDbHelper = null;

  public PickServiceDelegate(Context paramContext)
  {
    super(paramContext);
  }

  // ERROR //
  public boolean checkNetconfigFileAvailable()
  {
    // Byte code:
    //   0: new 44	java/io/BufferedReader
    //   3: dup
    //   4: new 46	java/io/FileReader
    //   7: dup
    //   8: new 48	java/lang/StringBuilder
    //   11: dup
    //   12: invokespecial 51	java/lang/StringBuilder:<init>	()V
    //   15: invokestatic 57	android/os/Environment:getExternalStorageDirectory	()Ljava/io/File;
    //   18: invokevirtual 61	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   21: ldc 63
    //   23: invokevirtual 66	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   26: invokevirtual 70	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   29: invokespecial 73	java/io/FileReader:<init>	(Ljava/lang/String;)V
    //   32: invokespecial 76	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   35: astore_1
    //   36: aload_1
    //   37: invokevirtual 79	java/io/BufferedReader:close	()V
    //   40: iconst_1
    //   41: ireturn
    //   42: astore_3
    //   43: aload_3
    //   44: invokevirtual 82	java/io/FileNotFoundException:printStackTrace	()V
    //   47: iconst_0
    //   48: ireturn
    //   49: astore_2
    //   50: aload_2
    //   51: invokevirtual 83	java/io/IOException:printStackTrace	()V
    //   54: goto -14 -> 40
    //
    // Exception table:
    //   from	to	target	type
    //   0	36	42	java/io/FileNotFoundException
    //   36	40	49	java/io/IOException
  }

  public void clearNetworkList()
  {
    if (this.data == null)
      return;
    this.data.clear();
  }

  public void closeDbIfNeeded()
  {
    if (this.mMyDb != null)
    {
      this.mMyDb.close();
      this.mMyDb = null;
    }
  }

  public int findIndexForNetwork(String paramString)
  {
    if (this.data != null)
      for (int i = 0; i < this.data.size(); i++)
        if (((MainDataRetention)this.data.get(i)).orgName.equals(paramString))
        {
          Util.log(this.mLogger, "Found index for network : " + paramString);
          return i;
        }
    return -1;
  }

  public boolean getAsLibrary()
  {
    return this.mAsLibrary;
  }

  public String getCustomerNameByIndex(int paramInt)
  {
    if ((this.data == null) || (this.data.get(paramInt) == null))
      return null;
    return ((MainDataRetention)this.data.get(paramInt)).orgName;
  }

  public String getCustomerUrlByIndex(int paramInt)
  {
    if ((this.data == null) || (this.data.get(paramInt) == null))
      return null;
    return ((MainDataRetention)this.data.get(paramInt)).orgUrl;
  }

  public SQLiteDatabase getDatabase()
  {
    if (this.mMyDb != null)
    {
      this.mMyDb.close();
      this.mMyDb = null;
    }
    this.mMyDb = this.myDbHelper.getWritableDatabase();
    return this.mMyDb;
  }

  public ArrayList<MainDataRetention> getNetworkList()
  {
    return this.data;
  }

  public void setAsLibrary(boolean paramBoolean)
  {
    this.mAsLibrary = paramBoolean;
  }

  public void setNetworkList(ArrayList<MainDataRetention> paramArrayList)
  {
    this.data = paramArrayList;
  }

  public String validatedUrl(String paramString)
  {
    if (paramString == null)
      return null;
    Util.log(this.mLogger, "Initial input URL : " + paramString);
    if (paramString.endsWith("/android.netconfig"))
    {
      int i = "/android.netconfig".length();
      paramString = paramString.substring(0, paramString.length() - i);
      Util.log(this.mLogger, "Stripped URL : " + paramString);
    }
    try
    {
      localURL1 = new URL(paramString);
    }
    catch (MalformedURLException localMalformedURLException3)
    {
      try
      {
        if (!paramString.endsWith("/network_config_android.xml"))
        {
          localURL2 = new URL(paramString + "/network_config_android.xml");
          while (true)
          {
            Util.log(this.mLogger, "Returning mapped URL of : " + localURL2.toString());
            return localURL2.toString();
            localMalformedURLException3 = localMalformedURLException3;
            localURL1 = null;
            String str;
            if ((!paramString.startsWith("http")) && (!paramString.startsWith("https")))
            {
              str = "http://" + paramString;
              if (!paramString.endsWith("/network_config_android.xml"))
                str = str + "/network_config_android.xml";
            }
            try
            {
              localURL2 = new URL(str);
              continue;
              str = paramString;
            }
            catch (MalformedURLException localMalformedURLException2)
            {
              localMalformedURLException2.printStackTrace();
              Util.log(this.mLogger, "URL is in a strange format : " + str);
              return str;
            }
          }
        }
      }
      catch (MalformedURLException localMalformedURLException1)
      {
        while (true)
        {
          URL localURL1;
          continue;
          URL localURL2 = localURL1;
        }
      }
    }
  }
}