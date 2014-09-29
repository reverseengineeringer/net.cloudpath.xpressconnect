package org.bouncycastle2.util.io.pem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle2.util.encoders.Base64;

public class PemReader extends BufferedReader
{
  private static final String BEGIN = "-----BEGIN ";
  private static final String END = "-----END ";

  public PemReader(Reader paramReader)
  {
    super(paramReader);
  }

  private PemObject loadObject(String paramString)
    throws IOException
  {
    String str1 = "-----END " + paramString;
    StringBuffer localStringBuffer = new StringBuffer();
    ArrayList localArrayList = new ArrayList();
    while (true)
    {
      String str2 = readLine();
      if (str2 == null);
      do
      {
        if (str2 != null)
          break label157;
        throw new IOException(str1 + " not found");
        if (str2.indexOf(":") >= 0)
        {
          int i = str2.indexOf(':');
          localArrayList.add(new PemHeader(str2.substring(0, i), str2.substring(i + 1).trim()));
          break;
        }
      }
      while (str2.indexOf(str1) != -1);
      localStringBuffer.append(str2.trim());
    }
    label157: return new PemObject(paramString, localArrayList, Base64.decode(localStringBuffer.toString()));
  }

  public PemObject readPemObject()
    throws IOException
  {
    String str1 = readLine();
    if ((str1 != null) && (str1.startsWith("-----BEGIN ")))
    {
      String str2 = str1.substring("-----BEGIN ".length());
      int i = str2.indexOf('-');
      String str3 = str2.substring(0, i);
      if (i > 0)
        return loadObject(str3);
    }
    return null;
  }
}