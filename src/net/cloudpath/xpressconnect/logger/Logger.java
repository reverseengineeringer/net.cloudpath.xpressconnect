package net.cloudpath.xpressconnect.logger;

import android.os.Environment;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.cloudpath.xpressconnect.Util;

public class Logger
{
  private List<String> banishedWords = new ArrayList();
  private StringBuffer logData = new StringBuffer();
  private File mLogFile = null;
  protected BufferedWriter mToLog = null;

  public Logger()
  {
    this.banishedWords.clear();
    openLogFile();
  }

  public void addBanishedWord(String paramString)
  {
    if (paramString != null)
      this.banishedWords.add(paramString);
  }

  public void addLine(String paramString)
  {
    Log.i("XPC", banishWords(paramString));
    addLineNoEcho(paramString);
  }

  public void addLineNoEcho(String paramString)
  {
    if (this.logData != null)
      this.logData.append(banishWords(paramString) + "\n");
    if (this.mToLog != null);
    try
    {
      this.mToLog.append(banishWords(paramString) + "\n");
      return;
    }
    catch (IOException localIOException)
    {
      Log.i("XPC", "Can't write log line.  Is the device full?");
      localIOException.printStackTrace();
    }
  }

  protected String banishWords(String paramString)
  {
    String str = paramString;
    for (int i = 0; i < this.banishedWords.size(); i++)
      if ((this.banishedWords.get(i) != null) && (((String)this.banishedWords.get(i)).length() > 0))
        str = str.replace((CharSequence)this.banishedWords.get(i), "_");
    return str;
  }

  public void clear()
  {
    if (this.mToLog != null);
    try
    {
      this.mToLog.flush();
      this.logData = new StringBuffer();
      if (this.banishedWords != null)
        this.banishedWords.clear();
      return;
    }
    catch (IOException localIOException)
    {
      while (true)
      {
        addLine("Can't flush log write buffer.");
        localIOException.printStackTrace();
      }
    }
  }

  public String getViewableLines()
  {
    return this.logData.toString();
  }

  protected void openLogFile()
  {
    openLogFile(Util.isStorageReady(null, null));
  }

  protected void openLogFile(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mLogFile = new File(Environment.getExternalStorageDirectory() + "/XpressConnect.log");
      try
      {
        if (!this.mLogFile.createNewFile())
          this.mLogFile.renameTo(new File(Environment.getExternalStorageDirectory() + "/xpc_old.log"));
        this.mToLog = new BufferedWriter(new FileWriter(Environment.getExternalStorageDirectory() + "/XpressConnect.log"));
        return;
      }
      catch (IOException localIOException)
      {
        Log.i("XPC", "Unable to create log file.  Will not store data to log file.");
        localIOException.printStackTrace();
        return;
      }
    }
    Log.i("XPC", "SD card not ready.   Won't store the log.");
  }
}