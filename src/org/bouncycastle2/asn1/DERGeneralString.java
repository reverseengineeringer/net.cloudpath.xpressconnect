package org.bouncycastle2.asn1;

import java.io.IOException;

public class DERGeneralString extends ASN1Object
  implements DERString
{
  private String string;

  public DERGeneralString(String paramString)
  {
    this.string = paramString;
  }

  public DERGeneralString(byte[] paramArrayOfByte)
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

  public static DERGeneralString getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERGeneralString)))
      return (DERGeneralString)paramObject;
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERGeneralString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    DERObject localDERObject = paramASN1TaggedObject.getObject();
    if ((paramBoolean) || ((localDERObject instanceof DERGeneralString)))
      return getInstance(localDERObject);
    return new DERGeneralString(((ASN1OctetString)localDERObject).getOctets());
  }

  boolean asn1Equals(DERObject paramDERObject)
  {
    if (!(paramDERObject instanceof DERGeneralString))
      return false;
    DERGeneralString localDERGeneralString = (DERGeneralString)paramDERObject;
    return getString().equals(localDERGeneralString.getString());
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.writeEncoded(27, getOctets());
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