package org.bouncycastle2.asn1;

import java.io.IOException;
import org.bouncycastle2.util.Strings;

public class DERUTF8String extends ASN1Object
  implements DERString
{
  String string;

  public DERUTF8String(String paramString)
  {
    this.string = paramString;
  }

  public DERUTF8String(byte[] paramArrayOfByte)
  {
    try
    {
      this.string = Strings.fromUTF8ByteArray(paramArrayOfByte);
      return;
    }
    catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
    {
    }
    throw new IllegalArgumentException("UTF8 encoding invalid");
  }

  public static DERUTF8String getInstance(Object paramObject)
  {
    if ((paramObject == null) || ((paramObject instanceof DERUTF8String)))
      return (DERUTF8String)paramObject;
    throw new IllegalArgumentException("illegal object in getInstance: " + paramObject.getClass().getName());
  }

  public static DERUTF8String getInstance(ASN1TaggedObject paramASN1TaggedObject, boolean paramBoolean)
  {
    DERObject localDERObject = paramASN1TaggedObject.getObject();
    if ((paramBoolean) || ((localDERObject instanceof DERUTF8String)))
      return getInstance(localDERObject);
    return new DERUTF8String(ASN1OctetString.getInstance(localDERObject).getOctets());
  }

  boolean asn1Equals(DERObject paramDERObject)
  {
    if (!(paramDERObject instanceof DERUTF8String))
      return false;
    DERUTF8String localDERUTF8String = (DERUTF8String)paramDERObject;
    return getString().equals(localDERUTF8String.getString());
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    paramDEROutputStream.writeEncoded(12, Strings.toUTF8ByteArray(this.string));
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