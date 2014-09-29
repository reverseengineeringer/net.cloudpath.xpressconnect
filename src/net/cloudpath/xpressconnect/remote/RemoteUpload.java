package net.cloudpath.xpressconnect.remote;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Build;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import net.cloudpath.xpressconnect.AndroidVersion;
import net.cloudpath.xpressconnect.Util;
import net.cloudpath.xpressconnect.logger.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class RemoteUpload
{
  private Logger mLogger;
  private Context mParent;

  public RemoteUpload(Logger paramLogger, Context paramContext)
  {
    this.mLogger = paramLogger;
    this.mParent = paramContext;
  }

  private String clientApiStartTag(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6)
  {
    String str = paramString4;
    if ((paramString1.startsWith("Enrollment-")) && (paramString2.startsWith("TOKEN-")))
      str = paramString1;
    return "<ClientApi cid=\"" + paramString5 + "\" key=\"" + str + "\" session=\"" + paramString6 + "\" sid=\"" + paramString3 + "\">";
  }

  private boolean uploadData(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8)
  {
    if (paramString8 == null)
      return false;
    DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
    HttpPost localHttpPost = new HttpPost(paramString8);
    try
    {
      ArrayList localArrayList = new ArrayList(1);
      String str = clientApiStartTag(paramString1, paramString2, paramString3, paramString4, paramString5, paramString6);
      localArrayList.add(new BasicNameValuePair("xmlData", str + paramString7 + "</ClientApi>"));
      localHttpPost.setEntity(new UrlEncodedFormEntity(localArrayList));
      HttpResponse localHttpResponse = localDefaultHttpClient.execute(localHttpPost);
      if (localHttpResponse.getStatusLine().getStatusCode() == 200)
        return true;
      Util.log(this.mLogger, "Data was posted, but status code returned " + localHttpResponse.getStatusLine().getStatusCode());
      return false;
    }
    catch (Exception localException)
    {
      while (true)
      {
        Util.log(this.mLogger, "Exception while trying to post data.  (Exception : " + localException.getMessage() + ")");
        Util.log(this.mLogger, "Exception data : " + localException.toString());
      }
    }
  }

  public boolean uploadInitialData(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8)
  {
    AndroidVersion localAndroidVersion = new AndroidVersion(this.mLogger);
    if (this.mParent == null)
    {
      Util.log(this.mLogger, "Unable to reference parent class.  Won't upload initial data.");
      return false;
    }
    ActivityManager localActivityManager = (ActivityManager)this.mParent.getSystemService("activity");
    ActivityManager.MemoryInfo localMemoryInfo = new ActivityManager.MemoryInfo();
    localActivityManager.getMemoryInfo(localMemoryInfo);
    String str1 = "<Session><NId>0</NId>" + "<Os><Nm>Android</Nm><Fm>Android</Fm><Vr>" + localAndroidVersion.getBaseAndroidVerAsString() + "</Vr>";
    String str2 = str1 + "<Sp>" + localAndroidVersion.patchLevel() + "</Sp><Bt>0</Bt></Os>";
    String str3 = str2 + "<Hdw><Dm>0</Dm><Mf>" + Build.MANUFACTURER + "</Mf><Md>" + Build.MODEL + "</Md>";
    String str4 = str3 + "<Cp>" + Build.CPU_ABI + "</Cp><Rm>" + localMemoryInfo.availMem / 1024L + "</Rm>";
    String str5 = str4 + "<Nic><Id>" + paramString8 + "</Id><Dc>Android Wireless Interface</Dc><Mf/><Tp>2</Tp><En/><Au/><Py/><Driver>";
    return uploadData(paramString1, paramString2, paramString3, paramString4, paramString5, paramString6, str5 + "<Nm/><Pd/><Vs/><Dt/><En/><Au/><Py/></Driver></Nic></Hdw><NId>" + paramString8 + "</NId></Session>", paramString7);
  }

  public boolean uploadLogData(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8)
  {
    if (this.mLogger == null)
      return false;
    try
    {
      String str2 = "<Log><Cm>" + paramString7 + "</Cm><Rw>" + URLEncoder.encode(this.mLogger.getViewableLines(), "UTF-8") + "</Rw></Log>";
      str1 = str2;
      return uploadData(paramString1, paramString2, paramString3, paramString4, paramString5, paramString6, str1, paramString8);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      while (true)
      {
        String str1 = "<Log><Cm>" + paramString7 + "</Cm><Rw></Rw></Log>";
        localUnsupportedEncodingException.printStackTrace();
      }
    }
  }

  public boolean uploadSessionState(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, int paramInt, String paramString7)
  {
    return uploadData(paramString1, paramString2, paramString3, paramString4, paramString5, paramString6, "<SessionUpdate status=\"" + paramInt + "\"/>", paramString7);
  }
}