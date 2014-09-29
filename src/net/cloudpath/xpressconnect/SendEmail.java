package net.cloudpath.xpressconnect;

import android.content.Intent;
import android.net.Uri;

public class SendEmail
{
  public static Intent sendEmailTo(String paramString1, String paramString2, String paramString3, Uri paramUri)
  {
    Intent localIntent = new Intent("android.intent.action.SEND");
    localIntent.setType("message/rfc822");
    localIntent.putExtra("android.intent.extra.EMAIL", new String[] { paramString1 });
    localIntent.putExtra("android.intent.extra.SUBJECT", paramString2);
    localIntent.putExtra("android.intent.extra.TEXT", paramString3);
    if (paramUri != null)
      localIntent.putExtra("android.intent.extra.STREAM", paramUri);
    return localIntent;
  }

  public static Intent sendLog(String paramString1, String paramString2)
  {
    return sendLogAttached(paramString1, paramString2, null);
  }

  public static Intent sendLogAttached(String paramString1, String paramString2, Uri paramUri)
  {
    String str = "Android log data";
    if (paramString1 != null)
      str = str + " - " + paramString1;
    return sendEmailTo("logs@cloudpath.net", str, paramString2, paramUri);
  }
}