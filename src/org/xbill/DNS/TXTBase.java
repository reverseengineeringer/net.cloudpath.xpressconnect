package org.xbill.DNS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

abstract class TXTBase extends Record
{
  private static final long serialVersionUID = -4319510507246305931L;
  protected List strings;

  protected TXTBase()
  {
  }

  protected TXTBase(Name paramName, int paramInt1, int paramInt2, long paramLong)
  {
    super(paramName, paramInt1, paramInt2, paramLong);
  }

  protected TXTBase(Name paramName, int paramInt1, int paramInt2, long paramLong, String paramString)
  {
    this(paramName, paramInt1, paramInt2, paramLong, Collections.singletonList(paramString));
  }

  protected TXTBase(Name paramName, int paramInt1, int paramInt2, long paramLong, List paramList)
  {
    super(paramName, paramInt1, paramInt2, paramLong);
    if (paramList == null)
      throw new IllegalArgumentException("strings must not be null");
    this.strings = new ArrayList(paramList.size());
    Iterator localIterator = paramList.iterator();
    try
    {
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        this.strings.add(byteArrayFromString(str));
      }
    }
    catch (TextParseException localTextParseException)
    {
      throw new IllegalArgumentException(localTextParseException.getMessage());
    }
  }

  public List getStrings()
  {
    ArrayList localArrayList = new ArrayList(this.strings.size());
    for (int i = 0; i < this.strings.size(); i++)
      localArrayList.add(byteArrayToString((byte[])this.strings.get(i), false));
    return localArrayList;
  }

  public List getStringsAsByteArrays()
  {
    return this.strings;
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    this.strings = new ArrayList(2);
    while (true)
    {
      Tokenizer.Token localToken = paramTokenizer.get();
      if (!localToken.isString())
      {
        paramTokenizer.unget();
        return;
      }
      try
      {
        this.strings.add(byteArrayFromString(localToken.value));
      }
      catch (TextParseException localTextParseException)
      {
        throw paramTokenizer.exception(localTextParseException.getMessage());
      }
    }
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    this.strings = new ArrayList(2);
    while (paramDNSInput.remaining() > 0)
    {
      byte[] arrayOfByte = paramDNSInput.readCountedString();
      this.strings.add(arrayOfByte);
    }
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    Iterator localIterator = this.strings.iterator();
    while (localIterator.hasNext())
    {
      localStringBuffer.append(byteArrayToString((byte[])localIterator.next(), true));
      if (localIterator.hasNext())
        localStringBuffer.append(" ");
    }
    return localStringBuffer.toString();
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    Iterator localIterator = this.strings.iterator();
    while (localIterator.hasNext())
      paramDNSOutput.writeCountedString((byte[])localIterator.next());
  }
}