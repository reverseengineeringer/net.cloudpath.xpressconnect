package org.xbill.DNS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class OPTRecord extends Record
{
  private static final long serialVersionUID = -6254521894809367938L;
  private List options;

  OPTRecord()
  {
  }

  public OPTRecord(int paramInt1, int paramInt2, int paramInt3)
  {
    this(paramInt1, paramInt2, paramInt3, 0, null);
  }

  public OPTRecord(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this(paramInt1, paramInt2, paramInt3, paramInt4, null);
  }

  public OPTRecord(int paramInt1, int paramInt2, int paramInt3, int paramInt4, List paramList)
  {
    super(Name.root, 41, paramInt1, 0L);
    checkU16("payloadSize", paramInt1);
    checkU8("xrcode", paramInt2);
    checkU8("version", paramInt3);
    checkU16("flags", paramInt4);
    this.ttl = ((paramInt2 << 24) + (paramInt3 << 16) + paramInt4);
    if (paramList != null)
      this.options = new ArrayList(paramList);
  }

  public int getExtendedRcode()
  {
    return (int)(this.ttl >>> 24);
  }

  public int getFlags()
  {
    return (int)(0xFFFF & this.ttl);
  }

  Record getObject()
  {
    return new OPTRecord();
  }

  public List getOptions()
  {
    if (this.options == null)
      return Collections.EMPTY_LIST;
    return Collections.unmodifiableList(this.options);
  }

  public List getOptions(int paramInt)
  {
    Object localObject;
    if (this.options == null)
      localObject = Collections.EMPTY_LIST;
    while (true)
    {
      return localObject;
      localObject = Collections.EMPTY_LIST;
      Iterator localIterator = this.options.iterator();
      while (localIterator.hasNext())
      {
        EDNSOption localEDNSOption = (EDNSOption)localIterator.next();
        if (localEDNSOption.getCode() == paramInt)
        {
          if (localObject == Collections.EMPTY_LIST)
            localObject = new ArrayList();
          ((List)localObject).add(localEDNSOption);
        }
      }
    }
  }

  public int getPayloadSize()
  {
    return this.dclass;
  }

  public int getVersion()
  {
    return (int)(0xFF & this.ttl >>> 16);
  }

  void rdataFromString(Tokenizer paramTokenizer, Name paramName)
    throws IOException
  {
    throw paramTokenizer.exception("no text format defined for OPT");
  }

  void rrFromWire(DNSInput paramDNSInput)
    throws IOException
  {
    if (paramDNSInput.remaining() > 0)
      this.options = new ArrayList();
    while (paramDNSInput.remaining() > 0)
    {
      EDNSOption localEDNSOption = EDNSOption.fromWire(paramDNSInput);
      this.options.add(localEDNSOption);
    }
  }

  String rrToString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    if (this.options != null)
    {
      localStringBuffer.append(this.options);
      localStringBuffer.append(" ");
    }
    localStringBuffer.append(" ; payload ");
    localStringBuffer.append(getPayloadSize());
    localStringBuffer.append(", xrcode ");
    localStringBuffer.append(getExtendedRcode());
    localStringBuffer.append(", version ");
    localStringBuffer.append(getVersion());
    localStringBuffer.append(", flags ");
    localStringBuffer.append(getFlags());
    return localStringBuffer.toString();
  }

  void rrToWire(DNSOutput paramDNSOutput, Compression paramCompression, boolean paramBoolean)
  {
    if (this.options == null);
    while (true)
    {
      return;
      Iterator localIterator = this.options.iterator();
      while (localIterator.hasNext())
        ((EDNSOption)localIterator.next()).toWire(paramDNSOutput);
    }
  }
}