package org.bouncycastle2.asn1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.bouncycastle2.util.Arrays;

public class DERApplicationSpecific extends ASN1Object
{
  private final boolean isConstructed;
  private final byte[] octets;
  private final int tag;

  public DERApplicationSpecific(int paramInt, ASN1EncodableVector paramASN1EncodableVector)
  {
    this.tag = paramInt;
    this.isConstructed = true;
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    int i = 0;
    while (true)
    {
      if (i == paramASN1EncodableVector.size())
      {
        this.octets = localByteArrayOutputStream.toByteArray();
        return;
      }
      try
      {
        localByteArrayOutputStream.write(((ASN1Encodable)paramASN1EncodableVector.get(i)).getEncoded());
        i++;
      }
      catch (IOException localIOException)
      {
        throw new ASN1ParsingException("malformed object: " + localIOException, localIOException);
      }
    }
  }

  public DERApplicationSpecific(int paramInt, DEREncodable paramDEREncodable)
    throws IOException
  {
    this(true, paramInt, paramDEREncodable);
  }

  public DERApplicationSpecific(int paramInt, byte[] paramArrayOfByte)
  {
    this(false, paramInt, paramArrayOfByte);
  }

  public DERApplicationSpecific(boolean paramBoolean, int paramInt, DEREncodable paramDEREncodable)
    throws IOException
  {
    byte[] arrayOfByte1 = paramDEREncodable.getDERObject().getDEREncoded();
    this.isConstructed = paramBoolean;
    this.tag = paramInt;
    if (paramBoolean)
    {
      this.octets = arrayOfByte1;
      return;
    }
    int i = getLengthOfLength(arrayOfByte1);
    byte[] arrayOfByte2 = new byte[arrayOfByte1.length - i];
    System.arraycopy(arrayOfByte1, i, arrayOfByte2, 0, arrayOfByte2.length);
    this.octets = arrayOfByte2;
  }

  DERApplicationSpecific(boolean paramBoolean, int paramInt, byte[] paramArrayOfByte)
  {
    this.isConstructed = paramBoolean;
    this.tag = paramInt;
    this.octets = paramArrayOfByte;
  }

  private int getLengthOfLength(byte[] paramArrayOfByte)
  {
    for (int i = 2; ; i++)
      if ((0x80 & paramArrayOfByte[(i - 1)]) == 0)
        return i;
  }

  private byte[] replaceTagNumber(int paramInt, byte[] paramArrayOfByte)
    throws IOException
  {
    int i = 0x1F & paramArrayOfByte[0];
    int j = 1;
    if (i == 31)
    {
      int k = j + 1;
      int m = 0xFF & paramArrayOfByte[j];
      int n = m & 0x7F;
      int i1 = 0;
      if (n == 0)
        throw new ASN1ParsingException("corrupted stream - invalid high tag number found");
      while ((m >= 0) && ((m & 0x80) != 0))
      {
        i1 = (i1 | m & 0x7F) << 7;
        int i2 = k + 1;
        m = 0xFF & paramArrayOfByte[k];
        k = i2;
      }
      (i1 | m & 0x7F);
      j = k;
    }
    byte[] arrayOfByte = new byte[1 + (paramArrayOfByte.length - j)];
    System.arraycopy(paramArrayOfByte, j, arrayOfByte, 1, -1 + arrayOfByte.length);
    arrayOfByte[0] = ((byte)paramInt);
    return arrayOfByte;
  }

  boolean asn1Equals(DERObject paramDERObject)
  {
    if (!(paramDERObject instanceof DERApplicationSpecific));
    DERApplicationSpecific localDERApplicationSpecific;
    do
    {
      return false;
      localDERApplicationSpecific = (DERApplicationSpecific)paramDERObject;
    }
    while ((this.isConstructed != localDERApplicationSpecific.isConstructed) || (this.tag != localDERApplicationSpecific.tag) || (!Arrays.areEqual(this.octets, localDERApplicationSpecific.octets)));
    return true;
  }

  void encode(DEROutputStream paramDEROutputStream)
    throws IOException
  {
    int i = 64;
    if (this.isConstructed)
      i |= 32;
    paramDEROutputStream.writeEncoded(i, this.tag, this.octets);
  }

  public int getApplicationTag()
  {
    return this.tag;
  }

  public byte[] getContents()
  {
    return this.octets;
  }

  public DERObject getObject()
    throws IOException
  {
    return new ASN1InputStream(getContents()).readObject();
  }

  public DERObject getObject(int paramInt)
    throws IOException
  {
    if (paramInt >= 31)
      throw new IOException("unsupported tag number");
    byte[] arrayOfByte1 = getEncoded();
    byte[] arrayOfByte2 = replaceTagNumber(paramInt, arrayOfByte1);
    if ((0x20 & arrayOfByte1[0]) != 0)
      arrayOfByte2[0] = ((byte)(0x20 | arrayOfByte2[0]));
    return new ASN1InputStream(arrayOfByte2).readObject();
  }

  public int hashCode()
  {
    if (this.isConstructed);
    for (int i = 1; ; i = 0)
      return i ^ this.tag ^ Arrays.hashCode(this.octets);
  }

  public boolean isConstructed()
  {
    return this.isConstructed;
  }
}