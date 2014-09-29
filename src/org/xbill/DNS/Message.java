package org.xbill.DNS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Message
  implements Cloneable
{
  public static final int MAXLENGTH = 65535;
  static final int TSIG_FAILED = 4;
  static final int TSIG_INTERMEDIATE = 2;
  static final int TSIG_SIGNED = 3;
  static final int TSIG_UNSIGNED = 0;
  static final int TSIG_VERIFIED = 1;
  private static RRset[] emptyRRsetArray = new RRset[0];
  private static Record[] emptyRecordArray = new Record[0];
  private Header header;
  private TSIGRecord querytsig;
  private List[] sections = new List[4];
  int sig0start;
  private int size;
  int tsigState;
  private int tsigerror;
  private TSIG tsigkey;
  int tsigstart;

  public Message()
  {
    this(new Header());
  }

  public Message(int paramInt)
  {
    this(new Header(paramInt));
  }

  Message(DNSInput paramDNSInput)
    throws IOException
  {
    this(new Header(paramDNSInput));
    boolean bool1;
    boolean bool2;
    int i;
    if (this.header.getOpcode() == 5)
    {
      bool1 = true;
      bool2 = this.header.getFlag(6);
      i = 0;
      if (i >= 4)
        break label186;
    }
    while (true)
    {
      try
      {
        int j = this.header.getCount(i);
        if (j <= 0)
          break label195;
        this.sections[i] = new ArrayList(j);
        break label195;
        if (k < j)
        {
          int m = paramDNSInput.current();
          Record localRecord = Record.fromWire(paramDNSInput, i, bool1);
          this.sections[i].add(localRecord);
          if (localRecord.getType() == 250)
            this.tsigstart = m;
          if ((localRecord.getType() == 24) && (((SIGRecord)localRecord).getTypeCovered() == 0))
            this.sig0start = m;
          k++;
          continue;
          bool1 = false;
          break;
        }
        i++;
      }
      catch (WireParseException localWireParseException)
      {
        if (!bool2)
          throw localWireParseException;
      }
      label186: this.size = paramDNSInput.current();
      return;
      label195: int k = 0;
    }
  }

  private Message(Header paramHeader)
  {
    this.header = paramHeader;
  }

  public Message(byte[] paramArrayOfByte)
    throws IOException
  {
    this(new DNSInput(paramArrayOfByte));
  }

  public static Message newQuery(Record paramRecord)
  {
    Message localMessage = new Message();
    localMessage.header.setOpcode(0);
    localMessage.header.setFlag(7);
    localMessage.addRecord(paramRecord, 0);
    return localMessage;
  }

  public static Message newUpdate(Name paramName)
  {
    return new Update(paramName);
  }

  private static boolean sameSet(Record paramRecord1, Record paramRecord2)
  {
    return (paramRecord1.getRRsetType() == paramRecord2.getRRsetType()) && (paramRecord1.getDClass() == paramRecord2.getDClass()) && (paramRecord1.getName().equals(paramRecord2.getName()));
  }

  private int sectionToWire(DNSOutput paramDNSOutput, int paramInt1, Compression paramCompression, int paramInt2)
  {
    int i = this.sections[paramInt1].size();
    int j = paramDNSOutput.current();
    int k = 0;
    Object localObject = null;
    for (int m = 0; m < i; m++)
    {
      Record localRecord = (Record)this.sections[paramInt1].get(m);
      if ((localObject != null) && (!sameSet(localRecord, (Record)localObject)))
      {
        j = paramDNSOutput.current();
        k = m;
      }
      localObject = localRecord;
      localRecord.toWire(paramDNSOutput, paramInt1, paramCompression);
      if (paramDNSOutput.current() > paramInt2)
      {
        paramDNSOutput.jump(j);
        return i - k;
      }
    }
    return 0;
  }

  private boolean toWire(DNSOutput paramDNSOutput, int paramInt)
  {
    if (paramInt < 12)
      return false;
    int i = paramInt;
    if (this.tsigkey != null)
      i -= this.tsigkey.recordLength();
    int j = paramDNSOutput.current();
    this.header.toWire(paramDNSOutput);
    Compression localCompression = new Compression();
    int k = 0;
    Header localHeader = null;
    if (k < 4)
    {
      if (this.sections[k] == null);
      int m;
      do
      {
        k++;
        break;
        m = sectionToWire(paramDNSOutput, k, localCompression, i);
      }
      while (m == 0);
      localHeader = null;
      if (0 == 0)
        localHeader = (Header)this.header.clone();
      if (k != 3)
        localHeader.setFlag(6);
      localHeader.setCount(k, localHeader.getCount(k) - m);
      for (int n = k + 1; n < 4; n++)
        localHeader.setCount(n, 0);
      paramDNSOutput.save();
      paramDNSOutput.jump(j);
      localHeader.toWire(paramDNSOutput);
      paramDNSOutput.restore();
    }
    if (this.tsigkey != null)
    {
      TSIGRecord localTSIGRecord = this.tsigkey.generate(this, paramDNSOutput.toByteArray(), this.tsigerror, this.querytsig);
      if (localHeader == null)
        localHeader = (Header)this.header.clone();
      localTSIGRecord.toWire(paramDNSOutput, 3, localCompression);
      localHeader.incCount(3);
      paramDNSOutput.save();
      paramDNSOutput.jump(j);
      localHeader.toWire(paramDNSOutput);
      paramDNSOutput.restore();
    }
    return true;
  }

  public void addRecord(Record paramRecord, int paramInt)
  {
    if (this.sections[paramInt] == null)
      this.sections[paramInt] = new LinkedList();
    this.header.incCount(paramInt);
    this.sections[paramInt].add(paramRecord);
  }

  public Object clone()
  {
    Message localMessage = new Message();
    for (int i = 0; i < this.sections.length; i++)
      if (this.sections[i] != null)
        localMessage.sections[i] = new LinkedList(this.sections[i]);
    localMessage.header = ((Header)this.header.clone());
    localMessage.size = this.size;
    return localMessage;
  }

  public boolean findRRset(Name paramName, int paramInt)
  {
    return (findRRset(paramName, paramInt, 1)) || (findRRset(paramName, paramInt, 2)) || (findRRset(paramName, paramInt, 3));
  }

  public boolean findRRset(Name paramName, int paramInt1, int paramInt2)
  {
    if (this.sections[paramInt2] == null);
    while (true)
    {
      return false;
      for (int i = 0; i < this.sections[paramInt2].size(); i++)
      {
        Record localRecord = (Record)this.sections[paramInt2].get(i);
        if ((localRecord.getType() == paramInt1) && (paramName.equals(localRecord.getName())))
          return true;
      }
    }
  }

  public boolean findRecord(Record paramRecord)
  {
    for (int i = 1; i <= 3; i++)
      if ((this.sections[i] != null) && (this.sections[i].contains(paramRecord)))
        return true;
    return false;
  }

  public boolean findRecord(Record paramRecord, int paramInt)
  {
    return (this.sections[paramInt] != null) && (this.sections[paramInt].contains(paramRecord));
  }

  public Header getHeader()
  {
    return this.header;
  }

  public OPTRecord getOPT()
  {
    Record[] arrayOfRecord = getSectionArray(3);
    for (int i = 0; i < arrayOfRecord.length; i++)
      if ((arrayOfRecord[i] instanceof OPTRecord))
        return (OPTRecord)arrayOfRecord[i];
    return null;
  }

  public Record getQuestion()
  {
    List localList = this.sections[0];
    if ((localList == null) || (localList.size() == 0))
      return null;
    return (Record)localList.get(0);
  }

  public int getRcode()
  {
    int i = this.header.getRcode();
    OPTRecord localOPTRecord = getOPT();
    if (localOPTRecord != null)
      i += (localOPTRecord.getExtendedRcode() << 4);
    return i;
  }

  public Record[] getSectionArray(int paramInt)
  {
    if (this.sections[paramInt] == null)
      return emptyRecordArray;
    List localList = this.sections[paramInt];
    return (Record[])localList.toArray(new Record[localList.size()]);
  }

  public RRset[] getSectionRRsets(int paramInt)
  {
    if (this.sections[paramInt] == null)
      return emptyRRsetArray;
    LinkedList localLinkedList = new LinkedList();
    Record[] arrayOfRecord = getSectionArray(paramInt);
    HashSet localHashSet = new HashSet();
    int i = 0;
    if (i < arrayOfRecord.length)
    {
      Name localName = arrayOfRecord[i].getName();
      int j = 1;
      if (localHashSet.contains(localName));
      for (int k = -1 + localLinkedList.size(); ; k--)
        if (k >= 0)
        {
          RRset localRRset = (RRset)localLinkedList.get(k);
          if ((localRRset.getType() == arrayOfRecord[i].getRRsetType()) && (localRRset.getDClass() == arrayOfRecord[i].getDClass()) && (localRRset.getName().equals(localName)))
          {
            localRRset.addRR(arrayOfRecord[i]);
            j = 0;
          }
        }
        else
        {
          if (j != 0)
          {
            localLinkedList.add(new RRset(arrayOfRecord[i]));
            localHashSet.add(localName);
          }
          i++;
          break;
        }
    }
    return (RRset[])localLinkedList.toArray(new RRset[localLinkedList.size()]);
  }

  public TSIGRecord getTSIG()
  {
    int i = this.header.getCount(3);
    if (i == 0)
      return null;
    Record localRecord = (Record)this.sections[3].get(i - 1);
    if (localRecord.type != 250)
      return null;
    return (TSIGRecord)localRecord;
  }

  public boolean isSigned()
  {
    return (this.tsigState == 3) || (this.tsigState == 1) || (this.tsigState == 4);
  }

  public boolean isVerified()
  {
    return this.tsigState == 1;
  }

  public int numBytes()
  {
    return this.size;
  }

  public void removeAllRecords(int paramInt)
  {
    this.sections[paramInt] = null;
    this.header.setCount(paramInt, 0);
  }

  public boolean removeRecord(Record paramRecord, int paramInt)
  {
    if ((this.sections[paramInt] != null) && (this.sections[paramInt].remove(paramRecord)))
    {
      this.header.decCount(paramInt);
      return true;
    }
    return false;
  }

  public String sectionToString(int paramInt)
  {
    if (paramInt > 3)
      return null;
    StringBuffer localStringBuffer = new StringBuffer();
    Record[] arrayOfRecord = getSectionArray(paramInt);
    int i = 0;
    if (i < arrayOfRecord.length)
    {
      Record localRecord = arrayOfRecord[i];
      if (paramInt == 0)
      {
        localStringBuffer.append(";;\t" + localRecord.name);
        localStringBuffer.append(", type = " + Type.string(localRecord.type));
        localStringBuffer.append(", class = " + DClass.string(localRecord.dclass));
      }
      while (true)
      {
        localStringBuffer.append("\n");
        i++;
        break;
        localStringBuffer.append(localRecord);
      }
    }
    return localStringBuffer.toString();
  }

  public void setHeader(Header paramHeader)
  {
    this.header = paramHeader;
  }

  public void setTSIG(TSIG paramTSIG, int paramInt, TSIGRecord paramTSIGRecord)
  {
    this.tsigkey = paramTSIG;
    this.tsigerror = paramInt;
    this.querytsig = paramTSIGRecord;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    label80: int i;
    if (getOPT() != null)
    {
      localStringBuffer.append(this.header.toStringWithRcode(getRcode()) + "\n");
      if (isSigned())
      {
        localStringBuffer.append(";; TSIG ");
        if (!isVerified())
          break label205;
        localStringBuffer.append("ok");
        localStringBuffer.append('\n');
      }
      i = 0;
      label89: if (i >= 4)
        break label253;
      if (this.header.getOpcode() == 5)
        break label216;
      localStringBuffer.append(";; " + Section.longString(i) + ":\n");
    }
    while (true)
    {
      localStringBuffer.append(sectionToString(i) + "\n");
      i++;
      break label89;
      localStringBuffer.append(this.header + "\n");
      break;
      label205: localStringBuffer.append("invalid");
      break label80;
      label216: localStringBuffer.append(";; " + Section.updString(i) + ":\n");
    }
    label253: localStringBuffer.append(";; Message size: " + numBytes() + " bytes");
    return localStringBuffer.toString();
  }

  void toWire(DNSOutput paramDNSOutput)
  {
    this.header.toWire(paramDNSOutput);
    Compression localCompression = new Compression();
    int i = 0;
    if (i < 4)
    {
      if (this.sections[i] == null);
      while (true)
      {
        i++;
        break;
        for (int j = 0; j < this.sections[i].size(); j++)
          ((Record)this.sections[i].get(j)).toWire(paramDNSOutput, i, localCompression);
      }
    }
  }

  public byte[] toWire()
  {
    DNSOutput localDNSOutput = new DNSOutput();
    toWire(localDNSOutput);
    this.size = localDNSOutput.current();
    return localDNSOutput.toByteArray();
  }

  public byte[] toWire(int paramInt)
  {
    DNSOutput localDNSOutput = new DNSOutput();
    toWire(localDNSOutput, paramInt);
    this.size = localDNSOutput.current();
    return localDNSOutput.toByteArray();
  }
}