package org.xbill.DNS;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.xbill.DNS.utils.base16;

public class APLRecord extends Record
{
  private static final long serialVersionUID = -1348173791712935864L;
  private List elements;

  APLRecord()
  {
  }

  public APLRecord(Name paramName, int paramInt, long paramLong, List paramList)
  {
    super(paramName, 42, paramInt, paramLong);
    this.elements = new ArrayList(paramList.size());
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      if (!(localObject instanceof Element))
        throw new IllegalArgumentException("illegal element");
      Element localElement = (Element)localObject;
      if ((localElement.family != 1) && (localElement.family != 2))
        throw new IllegalArgumentException("unknown family");
      this.elements.add(localElement);
    }
  }

  private static int addressLength(byte[] paramArrayOfByte)
  {
    for (int i = -1 + paramArrayOfByte.length; i >= 0; i--)
      if (paramArrayOfByte[i] != 0)
        return i + 1;
    return 0;
  }

  private static byte[] parseAddress(byte[] paramArrayOfByte, int paramInt)
    throws WireParseException
  {
    if (paramArrayOfByte.length > paramInt)
      throw new WireParseException("invalid address length");
    if (paramArrayOfByte.length == paramInt)
      return paramArrayOfByte;
    byte[] arrayOfByte = new byte[paramInt];
    System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, paramArrayOfByte.length);
    return arrayOfByte;
  }

  private static boolean validatePrefixLength(int paramInt1, int paramInt2)
  {
    if ((paramInt2 < 0) || (paramInt2 >= 256));
    while (((paramInt1 == 1) && (paramInt2 > 32)) || ((paramInt1 == 2) && (paramInt2 > 128)))
      return false;
    return true;
  }

  public List getElements()
  {
    return this.elements;
  }

  Record getObject()
  {
    return new APLRecord();
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.elements = new ArrayList(1);
    while (true)
    {
      Tokenizer.Token localToken = paramTokenizer.get();
      if (!localToken.isString())
      {
        paramTokenizer.unget();
        return;
      }
      String str1 = localToken.value;
      boolean bool1 = str1.startsWith("!");
      boolean bool2 = false;
      int i = 0;
      if (bool1)
      {
        bool2 = true;
        i = 1;
      }
      int j = str1.indexOf(':', i);
      if (j < 0)
        throw paramTokenizer.exception("invalid address prefix element");
      int k = str1.indexOf('/', j);
      if (k < 0)
        throw paramTokenizer.exception("invalid address prefix element");
      String str2 = str1.substring(i, j);
      String str3 = str1.substring(j + 1, k);
      String str4 = str1.substring(k + 1);
      int m;
      try
      {
        m = Integer.parseInt(str2);
        if ((m != 1) && (m != 2))
          throw paramTokenizer.exception("unknown family");
      }
      catch (NumberFormatException localNumberFormatException1)
      {
        throw paramTokenizer.exception("invalid family");
      }
      int n;
      try
      {
        n = Integer.parseInt(str4);
        if (!validatePrefixLength(m, n))
          throw paramTokenizer.exception("invalid prefix length");
      }
      catch (NumberFormatException localNumberFormatException2)
      {
        throw paramTokenizer.exception("invalid prefix length");
      }
      byte[] arrayOfByte = Address.toByteArray(str3, m);
      if (arrayOfByte == null)
        throw paramTokenizer.exception("invalid IP address " + str3);
      InetAddress localInetAddress = InetAddress.getByAddress(arrayOfByte);
      List localList = this.elements;
      Element localElement = new Element(bool2, localInetAddress, n);
      localList.add(localElement);
    }
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.elements = new ArrayList(1);
    if (paramDNSInput.remaining() != 0)
    {
      int i = paramDNSInput.readU16();
      int j = paramDNSInput.readU8();
      int k = paramDNSInput.readU8();
      if ((k & 0x80) != 0);
      byte[] arrayOfByte;
      for (boolean bool = true; ; bool = false)
      {
        arrayOfByte = paramDNSInput.readByteArray(k & 0xFFFFFF7F);
        if (validatePrefixLength(i, j))
          break;
        throw new WireParseException("invalid prefix length");
      }
      if ((i == 1) || (i == 2));
      for (Element localElement = new Element(bool, InetAddress.getByAddress(parseAddress(arrayOfByte, Address.addressLength(i))), j); ; localElement = new Element(i, bool, arrayOfByte, j, null))
      {
        this.elements.add(localElement);
        break;
      }
    }
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    Iterator localIterator = this.elements.iterator();
    while (localIterator.hasNext())
    {
      localStringBuffer.append((Element)localIterator.next());
      if (localIterator.hasNext())
        localStringBuffer.append(" ");
    }
    return localStringBuffer.toString();
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    Iterator localIterator = this.elements.iterator();
    if (localIterator.hasNext())
    {
      Element localElement = (Element)localIterator.next();
      byte[] arrayOfByte;
      if ((localElement.family == 1) || (localElement.family == 2))
        arrayOfByte = ((InetAddress)localElement.address).getAddress();
      for (int i = addressLength(arrayOfByte); ; i = arrayOfByte.length)
      {
        int j = i;
        if (localElement.negative)
          j |= 128;
        paramDNSOutput.writeU16(localElement.family);
        paramDNSOutput.writeU8(localElement.prefixLength);
        paramDNSOutput.writeU8(j);
        paramDNSOutput.writeByteArray(arrayOfByte, 0, i);
        break;
        arrayOfByte = (byte[])localElement.address;
      }
    }
  }

  public static class Element
  {
    public final Object address;
    public final int family;
    public final boolean negative;
    public final int prefixLength;

    private Element(int paramInt1, boolean paramBoolean, Object paramObject, int paramInt2)
    {
      this.family = paramInt1;
      this.negative = paramBoolean;
      this.address = paramObject;
      this.prefixLength = paramInt2;
      if (!APLRecord.validatePrefixLength(paramInt1, paramInt2))
        throw new IllegalArgumentException("invalid prefix length");
    }

    Element(int paramInt1, boolean paramBoolean, Object paramObject, int paramInt2, APLRecord.1 param1)
    {
      this(paramInt1, paramBoolean, paramObject, paramInt2);
    }

    public Element(boolean paramBoolean, InetAddress paramInetAddress, int paramInt)
    {
      this(Address.familyOf(paramInetAddress), paramBoolean, paramInetAddress, paramInt);
    }

    public boolean equals(Object paramObject)
    {
      if ((paramObject == null) || (!(paramObject instanceof Element)));
      Element localElement;
      do
      {
        return false;
        localElement = (Element)paramObject;
      }
      while ((this.family != localElement.family) || (this.negative != localElement.negative) || (this.prefixLength != localElement.prefixLength) || (!this.address.equals(localElement.address)));
      return true;
    }

    public int hashCode()
    {
      int i = this.address.hashCode() + this.prefixLength;
      if (this.negative);
      for (int j = 1; ; j = 0)
        return j + i;
    }

    public String toString()
    {
      StringBuffer localStringBuffer = new StringBuffer();
      if (this.negative)
        localStringBuffer.append("!");
      localStringBuffer.append(this.family);
      localStringBuffer.append(":");
      if ((this.family == 1) || (this.family == 2))
        localStringBuffer.append(((InetAddress)this.address).getHostAddress());
      while (true)
      {
        localStringBuffer.append("/");
        localStringBuffer.append(this.prefixLength);
        return localStringBuffer.toString();
        localStringBuffer.append(base16.toString((byte[])this.address));
      }
    }
  }
}