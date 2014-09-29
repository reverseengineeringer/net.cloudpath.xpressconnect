package org.bouncycastle2.asn1;

import java.io.IOException;

public class DERPrintableString extends ASN1Object
  implements DERString
{
  String string;

  public DERPrintableString(String paramString)
  {
    this(paramString, false);
  }

  public DERPrintableString(String paramString, boolean paramBoolean)
  {
    if ((paramBoolean) && (!isPrintableString(paramString)))
      throw new IllegalArgumentException("string contains illegal characters");
    this.string = paramString;
  }

  public DERPrintableString(byte[] paramArrayOfByte)
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

  public static DERPrintableString getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERPrintableString)))
      return (DERPrintableString)paramObject;
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERPrintableString getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    DERObject localDERObject = paramASN1TaggedObject.getObject();
    if ((paramBoolean) || ((localDERObject instanceof DERPrintableString)))
      return getInstance(localDERObject);
    return new DERPrintableString(ASN1OctetString.getInstance(localDERObject).getOctets());
  }

  public static boolean isPrintableString(String paramString)
  {
    int i = -1 + paramString.length();
    boolean bool;
    if (i < 0)
      bool = true;
    int j;
    do
    {
      return bool;
      j = paramString.charAt(i);
      bool = false;
    }
    while (j > 127);
    if ((97 <= j) && (j <= 122));
    while (((65 <= j) && (j <= 90)) || ((48 <= j) && (j <= 57)))
    {
      i--;
      break;
    }
    switch (j)
    {
    case 32:
    case 39:
    case 40:
    case 41:
    case 43:
    case 44:
    case 45:
    case 46:
    case 47:
    case 58:
    case 61:
    case 63:
    }
    return false;
  }

  boolean asn1Equals(DERObject paramDERObject)
  {
    if (!(paramDERObject instanceof DERPrintableString))
      return false;
    DERPrintableString localDERPrintableString = (DERPrintableString)paramDERObject;
    return getString().equals(localDERPrintableString.getString());
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.writeEncoded(19, getOctets());
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