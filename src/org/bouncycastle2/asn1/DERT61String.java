package org.bouncycastle2.asn1;

import java.io.IOException;

public class DERT61String extends ASN1Object
  implements DERString
{
  String string;

  public DERT61String(String paramString)
  {
    this.string = paramString;
  }

  public DERT61String(byte[] paramArrayOfByte)
  {
    char[] arrayOfChar = new char[paramArrayOfByte.length];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfChar.length)
      {
        this.string = new String(arrayOfChar);
        return;
      }
      arrayOfChar[i] = ((char)(0xFF & paramArrayOfByte[i]));
    }
  }

  public static DERT61String getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERT61String)))
      return (DERT61String)paramObject;
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERT61String getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    DERObject localDERObject = paramASN1TaggedObject.getObject();
    if (paramBoolean)
      return getInstance(localDERObject);
    return new DERT61String(ASN1OctetString.getInstance(localDERObject).getOctets());
  }

  boolean asn1Equals(DERObject paramDERObject)
  {
    if (!(paramDERObject instanceof DERT61String))
      return false;
    return getString().equals(((DERT61String)paramDERObject).getString());
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.writeEncoded(20, getOctets());
  }

  public byte[] getOctets()
  {
    char[] arrayOfChar = this.string.toCharArray();
    byte[] arrayOfByte = new byte[arrayOfChar.length];
    for (int i = 0; ; i++)
    {
      if (i == arrayOfChar.length)
        return arrayOfByte;
      arrayOfByte[i] = ((byte)arrayOfChar[i]);
    }
  }

  public String getString()
  {
    return this.string;
  }

  public int hashCode()
  {
    return getString().hashCode();
  }

  public String toString()
  {
    return this.string;
  }
}