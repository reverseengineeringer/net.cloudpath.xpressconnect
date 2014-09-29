package org.bouncycastle2.jce;

import java.io.IOException;
import java.security.Principal;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.ASN1Sequence;
import org.bouncycastle2.asn1.x509.X509Name;

public class X509Principal extends X509Name
  implements Principal
{
  public X509Principal(String paramString)
  {
    super(paramString);
  }

  public X509Principal(Hashtable paramHashtable)
  {
    super(paramHashtable);
  }

  public X509Principal(Vector paramVector, Hashtable paramHashtable)
  {
    super(paramVector, paramHashtable);
  }

  public X509Principal(Vector paramVector1, Vector paramVector2)
  {
    super(paramVector1, paramVector2);
  }

  public X509Principal(X509Name paramX509Name)
  {
    super((ASN1Sequence)paramX509Name.getDERObject());
  }

  public X509Principal(boolean paramBoolean, String paramString)
  {
    super(paramBoolean, paramString);
  }

  public X509Principal(boolean paramBoolean, Hashtable paramHashtable, String paramString)
  {
    super(paramBoolean, paramHashtable, paramString);
  }

  public X509Principal(byte[] paramArrayOfByte)
    throws IOException
  {
    super(readSequence(new ASN1InputStream(paramArrayOfByte)));
  }

  private static ASN1Sequence readSequence(ASN1InputStream paramASN1InputStream)
    throws IOException
  {
    try
    {
      ASN1Sequence localASN1Sequence = ASN1Sequence.getInstance(paramASN1InputStream.readObject());
      return localASN1Sequence;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      throw new IOException("not an ASN.1 Sequence: " + localIllegalArgumentException);
    }
  }

  public byte[] getEncoded()
  {
    try
    {
      byte[] arrayOfByte = getEncoded("DER");
      return arrayOfByte;
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException(localIOException.toString());
    }
  }

  public String getName()
  {
    return toString();
  }
}