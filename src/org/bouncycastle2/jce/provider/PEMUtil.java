package org.bouncycastle2.jce.provider;

import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.DERObject;
import org.bouncycastle2.util.encoders.Base64;

public class PEMUtil
{
  private final String _footer1;
  private final String _footer2;
  private final String _header1;
  private final String _header2;

  PEMUtil(String paramString)
  {
    this._header1 = ("-----BEGIN " + paramString + "-----");
    this._header2 = ("-----BEGIN X509 " + paramString + "-----");
    this._footer1 = ("-----END " + paramString + "-----");
    this._footer2 = ("-----END X509 " + paramString + "-----");
  }

  private String readLine(InputStream paramInputStream)
    throws IOException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    while (true)
    {
      int i = paramInputStream.read();
      if ((i == 13) || (i == 10) || (i < 0))
      {
        if ((i < 0) || (localStringBuffer.length() != 0))
        {
          if (i >= 0)
            break;
          return null;
        }
      }
      else if (i != 13)
        localStringBuffer.append((char)i);
    }
    return localStringBuffer.toString();
  }

  ASN1Sequence readPEMObject(InputStream paramInputStream)
    throws IOException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str1 = readLine(paramInputStream);
    if (str1 == null);
    label18: DERObject localDERObject;
    while (true)
    {
      String str2 = readLine(paramInputStream);
      if (str2 == null);
      while ((str2.startsWith(this._footer1)) || (str2.startsWith(this._footer2)))
      {
        if (localStringBuffer.length() == 0)
          break label139;
        localDERObject = new ASN1InputStream(Base64.decode(localStringBuffer.toString())).readObject();
        if ((localDERObject instanceof ASN1Sequence))
          break label133;
        throw new IOException("malformed PEM data encountered");
        if (str1.startsWith(this._header1))
          break label18;
        if (!str1.startsWith(this._header2))
          break;
        break label18;
      }
      localStringBuffer.append(str2);
    }
    label133: return (ASN1Sequence)localDERObject;
    label139: return null;
  }
}