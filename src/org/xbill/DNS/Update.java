package org.xbill.DNS;

import java.io.IOException;
import java.util.Iterator;

public class Update extends Message
{
  private int dclass;
  private Name origin;

  public Update(Name paramName)
  {
    this(paramName, 1);
  }

  public Update(Name paramName, int paramInt)
  {
    if (!paramName.isAbsolute())
      throw new RelativeNameException(paramName);
    DClass.check(paramInt);
    getHeader().setOpcode(5);
    addRecord(Record.newRecord(paramName, 6, 1), 0);
    this.origin = paramName;
    this.dclass = paramInt;
  }

  private void newPrereq(Record paramRecord)
  {
    addRecord(paramRecord, 1);
  }

  private void newUpdate(Record paramRecord)
  {
    addRecord(paramRecord, 2);
  }

  public void absent(Name paramName)
  {
    newPrereq(Record.newRecord(paramName, 255, 254, 0L));
  }

  public void absent(Name paramName, int paramInt)
  {
    newPrereq(Record.newRecord(paramName, paramInt, 254, 0L));
  }

  public void add(Name paramName, int paramInt, long paramLong, String paramString)
    throws IOException
  {
    newUpdate(Record.fromString(paramName, paramInt, this.dclass, paramLong, paramString, this.origin));
  }

  public void add(Name paramName, int paramInt, long paramLong, Tokenizer paramTokenizer)
    throws IOException
  {
    newUpdate(Record.fromString(paramName, paramInt, this.dclass, paramLong, paramTokenizer, this.origin));
  }

  public void add(RRset paramRRset)
  {
    Iterator localIterator = paramRRset.rrs();
    while (localIterator.hasNext())
      add((Record)localIterator.next());
  }

  public void add(Record paramRecord)
  {
    newUpdate(paramRecord);
  }

  public void add(Record[] paramArrayOfRecord)
  {
    for (int i = 0; i < paramArrayOfRecord.length; i++)
      add(paramArrayOfRecord[i]);
  }

  public void delete(Name paramName)
  {
    newUpdate(Record.newRecord(paramName, 255, 255, 0L));
  }

  public void delete(Name paramName, int paramInt)
  {
    newUpdate(Record.newRecord(paramName, paramInt, 255, 0L));
  }

  public void delete(Name paramName, int paramInt, String paramString)
    throws IOException
  {
    newUpdate(Record.fromString(paramName, paramInt, 254, 0L, paramString, this.origin));
  }

  public void delete(Name paramName, int paramInt, Tokenizer paramTokenizer)
    throws IOException
  {
    newUpdate(Record.fromString(paramName, paramInt, 254, 0L, paramTokenizer, this.origin));
  }

  public void delete(RRset paramRRset)
  {
    Iterator localIterator = paramRRset.rrs();
    while (localIterator.hasNext())
      delete((Record)localIterator.next());
  }

  public void delete(Record paramRecord)
  {
    newUpdate(paramRecord.withDClass(254, 0L));
  }

  public void delete(Record[] paramArrayOfRecord)
  {
    for (int i = 0; i < paramArrayOfRecord.length; i++)
      delete(paramArrayOfRecord[i]);
  }

  public void present(Name paramName)
  {
    newPrereq(Record.newRecord(paramName, 255, 255, 0L));
  }

  public void present(Name paramName, int paramInt)
  {
    newPrereq(Record.newRecord(paramName, paramInt, 255, 0L));
  }

  public void present(Name paramName, int paramInt, String paramString)
    throws IOException
  {
    newPrereq(Record.fromString(paramName, paramInt, this.dclass, 0L, paramString, this.origin));
  }

  public void present(Name paramName, int paramInt, Tokenizer paramTokenizer)
    throws IOException
  {
    newPrereq(Record.fromString(paramName, paramInt, this.dclass, 0L, paramTokenizer, this.origin));
  }

  public void present(Record paramRecord)
  {
    newPrereq(paramRecord);
  }

  public void replace(Name paramName, int paramInt, long paramLong, String paramString)
    throws IOException
  {
    delete(paramName, paramInt);
    add(paramName, paramInt, paramLong, paramString);
  }

  public void replace(Name paramName, int paramInt, long paramLong, Tokenizer paramTokenizer)
    throws IOException
  {
    delete(paramName, paramInt);
    add(paramName, paramInt, paramLong, paramTokenizer);
  }

  public void replace(RRset paramRRset)
  {
    delete(paramRRset.getName(), paramRRset.getType());
    Iterator localIterator = paramRRset.rrs();
    while (localIterator.hasNext())
      add((Record)localIterator.next());
  }

  public void replace(Record paramRecord)
  {
    delete(paramRecord.getName(), paramRecord.getType());
    add(paramRecord);
  }

  public void replace(Record[] paramArrayOfRecord)
  {
    for (int i = 0; i < paramArrayOfRecord.length; i++)
      replace(paramArrayOfRecord[i]);
  }
}