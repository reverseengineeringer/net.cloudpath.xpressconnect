package jcifs.smb;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class Handler extends URLStreamHandler
{
  static final URLStreamHandler SMB_HANDLER = new Handler();

  protected int getDefaultPort()
  {
    return 445;
  }

  public URLConnection openConnection(URL paramURL)
    throws IOException
  {
    return new SmbFile(paramURL);
  }

  protected void parseURL(URL paramURL, String paramString, int paramInt1, int paramInt2)
  {
    String str1 = paramURL.getHost();
    if (paramString.equals("smb://"))
    {
      paramString = "smb:////";
      paramInt2 += 2;
    }
    while (true)
    {
      super.parseURL(paramURL, paramString, paramInt1, paramInt2);
      String str2 = paramURL.getPath();
      String str3 = paramURL.getRef();
      if (str3 != null)
        str2 = str2 + '#' + str3;
      int i = paramURL.getPort();
      if (i == -1)
        i = getDefaultPort();
      setURL(paramURL, "smb", paramURL.getHost(), i, paramURL.getAuthority(), paramURL.getUserInfo(), str2, paramURL.getQuery(), null);
      return;
      if ((!paramString.startsWith("smb://")) && (str1 != null) && (str1.length() == 0))
      {
        paramString = "//" + paramString;
        paramInt2 += 2;
      }
    }
  }
}