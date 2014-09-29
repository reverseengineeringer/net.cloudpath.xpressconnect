package org.xbill.DNS;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;

public class Zone
  implements Serializable
{
  public static final int PRIMARY = 1;
  public static final int SECONDARY = 2;
  private static final long serialVersionUID = -9220510891189510942L;
  private RRset NS;
  private SOARecord SOA;
  private Map data;
  private int dclass = 1;
  private boolean hasWild;
  private Name origin;
  private Object originNode;

  public Zone(Name paramName, int paramInt, String paramString)
    throws IOException, ZoneTransferException
  {
    ZoneTransferIn localZoneTransferIn = ZoneTransferIn.newAXFR(paramName, paramString, null);
    localZoneTransferIn.setDClass(paramInt);
    fromXFR(localZoneTransferIn);
  }

  public Zone(Name paramName, String paramString)
    throws IOException
  {
    this.data = new TreeMap();
    if (paramName == null)
      throw new IllegalArgumentException("no zone name specified");
    Master localMaster = new Master(paramString, paramName);
    this.origin = paramName;
    while (true)
    {
      Record localRecord = localMaster.nextRecord();
      if (localRecord == null)
        break;
      maybeAddRecord(localRecord);
    }
    validate();
  }

  public Zone(Name paramName, Record[] paramArrayOfRecord)
    throws IOException
  {
    this.data = new TreeMap();
    if (paramName == null)
      throw new IllegalArgumentException("no zone name specified");
    this.origin = paramName;
    for (int i = 0; i < paramArrayOfRecord.length; i++)
      maybeAddRecord(paramArrayOfRecord[i]);
    validate();
  }

  public Zone(ZoneTransferIn paramZoneTransferIn)
    throws IOException, ZoneTransferException
  {
    fromXFR(paramZoneTransferIn);
  }

  private void addRRset(Name paramName, RRset paramRRset)
  {
    while (true)
    {
      Object localObject2;
      int i;
      List localList;
      int j;
      try
      {
        if ((!this.hasWild) && (paramName.isWild()))
          this.hasWild = true;
        localObject2 = this.data.get(paramName);
        if (localObject2 == null)
        {
          this.data.put(paramName, paramRRset);
          return;
        }
        i = paramRRset.getType();
        if (!(localObject2 instanceof List))
          break label146;
        localList = (List)localObject2;
        j = 0;
        if (j >= localList.size())
          break label134;
        if (((RRset)localList.get(j)).getType() == i)
        {
          localList.set(j, paramRRset);
          continue;
        }
      }
      finally
      {
      }
      j++;
      continue;
      label134: localList.add(paramRRset);
      continue;
      label146: RRset localRRset = (RRset)localObject2;
      if (localRRset.getType() == i)
      {
        this.data.put(paramName, paramRRset);
      }
      else
      {
        LinkedList localLinkedList = new LinkedList();
        localLinkedList.add(localRRset);
        localLinkedList.add(paramRRset);
        this.data.put(paramName, localLinkedList);
      }
    }
  }

  private RRset[] allRRsets(Object paramObject)
  {
    try
    {
      List localList;
      if ((paramObject instanceof List))
        localList = (List)paramObject;
      for (RRset[] arrayOfRRset = (RRset[])localList.toArray(new RRset[localList.size()]); ; arrayOfRRset = new RRset[] { (RRset)paramObject })
        return arrayOfRRset;
    }
    finally
    {
    }
  }

  private Object exactName(Name paramName)
  {
    try
    {
      Object localObject2 = this.data.get(paramName);
      return localObject2;
    }
    finally
    {
      localObject1 = finally;
      throw localObject1;
    }
  }

  private RRset findRRset(Name paramName, int paramInt)
  {
    try
    {
      Object localObject2 = exactName(paramName);
      if (localObject2 == null);
      RRset localRRset;
      for (Object localObject3 = null; ; localObject3 = localRRset)
      {
        return localObject3;
        localRRset = oneRRset(localObject2, paramInt);
      }
    }
    finally
    {
    }
  }

  private void fromXFR(ZoneTransferIn paramZoneTransferIn)
    throws IOException, ZoneTransferException
  {
    this.data = new TreeMap();
    this.origin = paramZoneTransferIn.getName();
    Iterator localIterator = paramZoneTransferIn.run().iterator();
    while (localIterator.hasNext())
      maybeAddRecord((Record)localIterator.next());
    if (!paramZoneTransferIn.isAXFR())
      throw new IllegalArgumentException("zones can only be created from AXFRs");
    validate();
  }

  private SetResponse lookup(Name paramName, int paramInt)
  {
    Object localObject3;
    int i;
    int j;
    int k;
    label50: int n;
    Name localName;
    label61: label77: int i1;
    while (true)
    {
      Object localObject4;
      try
      {
        if (!paramName.subdomain(this.origin))
        {
          SetResponse localSetResponse2 = SetResponse.ofType(1);
          localObject3 = localSetResponse2;
          return localObject3;
        }
        i = paramName.labels();
        j = this.origin.labels();
        k = j;
        break label395;
        if (n == 0)
          break label443;
        localName = this.origin;
        localObject4 = exactName(localName);
        if (localObject4 == null)
        {
          break label425;
          localName = new Name(paramName, i - k);
          continue;
        }
        if (n == 0)
        {
          RRset localRRset2 = oneRRset(localObject4, 2);
          if (localRRset2 != null)
          {
            localObject3 = new SetResponse(3, localRRset2);
            continue;
          }
        }
      }
      finally
      {
      }
      if ((i1 != 0) && (paramInt == 255))
      {
        localObject3 = new SetResponse(6);
        RRset[] arrayOfRRset = allRRsets(localObject4);
        for (int i2 = 0; i2 < arrayOfRRset.length; i2++)
          ((SetResponse)localObject3).addRRset(arrayOfRRset[i2]);
      }
      else if (i1 != 0)
      {
        RRset localRRset3 = oneRRset(localObject4, paramInt);
        if (localRRset3 != null)
        {
          localObject3 = new SetResponse(6);
          ((SetResponse)localObject3).addRRset(localRRset3);
        }
        else
        {
          RRset localRRset4 = oneRRset(localObject4, 5);
          if (localRRset4 != null)
            localObject3 = new SetResponse(4, localRRset4);
        }
      }
      else
      {
        RRset localRRset5 = oneRRset(localObject4, 39);
        if (localRRset5 != null)
        {
          localObject3 = new SetResponse(5, localRRset5);
        }
        else
        {
          if (i1 == 0)
            break label425;
          localObject3 = SetResponse.ofType(2);
          continue;
          label305: if (!this.hasWild)
            break;
        }
      }
    }
    for (int m = 0; ; m++)
    {
      if (m < i - j)
      {
        Object localObject2 = exactName(paramName.wild(m + 1));
        if (localObject2 == null)
          continue;
        RRset localRRset1 = oneRRset(localObject2, paramInt);
        if (localRRset1 == null)
          continue;
        localObject3 = new SetResponse(6);
        ((SetResponse)localObject3).addRRset(localRRset1);
        break;
      }
      SetResponse localSetResponse1 = SetResponse.ofType(1);
      localObject3 = localSetResponse1;
      break;
      label395: if (k <= i)
        if (k != j)
          break label431;
      label425: label431: for (n = 1; ; n = 0)
      {
        if (k != i)
          break label437;
        i1 = 1;
        break;
        k++;
        break label395;
        break label305;
      }
      label437: i1 = 0;
      break label50;
      label443: if (i1 == 0)
        break label77;
      localName = paramName;
      break label61;
    }
  }

  private final void maybeAddRecord(Record paramRecord)
    throws IOException
  {
    int i = paramRecord.getType();
    Name localName = paramRecord.getName();
    if ((i == 6) && (!localName.equals(this.origin)))
      throw new IOException("SOA owner " + localName + " does not match zone origin " + this.origin);
    if (localName.subdomain(this.origin))
      addRecord(paramRecord);
  }

  private void nodeToString(StringBuffer paramStringBuffer, Object paramObject)
  {
    RRset[] arrayOfRRset = allRRsets(paramObject);
    for (int i = 0; i < arrayOfRRset.length; i++)
    {
      RRset localRRset = arrayOfRRset[i];
      Iterator localIterator1 = localRRset.rrs();
      while (localIterator1.hasNext())
        paramStringBuffer.append(localIterator1.next() + "\n");
      Iterator localIterator2 = localRRset.sigs();
      while (localIterator2.hasNext())
        paramStringBuffer.append(localIterator2.next() + "\n");
    }
  }

  private RRset oneRRset(Object paramObject, int paramInt)
  {
    if (paramInt == 255)
      try
      {
        throw new IllegalArgumentException("oneRRset(ANY)");
      }
      finally
      {
      }
    int j;
    RRset localRRset;
    if ((paramObject instanceof List))
    {
      List localList = (List)paramObject;
      j = 0;
      if (j >= localList.size())
        break label107;
      localRRset = (RRset)localList.get(j);
      int k = localRRset.getType();
      if (k != paramInt);
    }
    while (true)
    {
      return localRRset;
      j++;
      break;
      localRRset = (RRset)paramObject;
      int i = localRRset.getType();
      if (i != paramInt)
        label107: localRRset = null;
    }
  }

  private void removeRRset(Name paramName, int paramInt)
  {
    while (true)
    {
      Object localObject2;
      int i;
      try
      {
        localObject2 = this.data.get(paramName);
        if (localObject2 == null)
          return;
        if (!(localObject2 instanceof List))
          break label116;
        List localList = (List)localObject2;
        i = 0;
        if (i >= localList.size())
          continue;
        if (((RRset)localList.get(i)).getType() == paramInt)
        {
          localList.remove(i);
          if (localList.size() != 0)
            continue;
          this.data.remove(paramName);
          continue;
        }
      }
      finally
      {
      }
      i++;
      continue;
      label116: if (((RRset)localObject2).getType() == paramInt)
        this.data.remove(paramName);
    }
  }

  private void validate()
    throws IOException
  {
    this.originNode = exactName(this.origin);
    if (this.originNode == null)
      throw new IOException(this.origin + ": no data specified");
    RRset localRRset = oneRRset(this.originNode, 6);
    if ((localRRset == null) || (localRRset.size() != 1))
      throw new IOException(this.origin + ": exactly 1 SOA must be specified");
    this.SOA = ((SOARecord)localRRset.rrs().next());
    this.NS = oneRRset(this.originNode, 2);
    if (this.NS == null)
      throw new IOException(this.origin + ": no NS set specified");
  }

  public Iterator AXFR()
  {
    return new ZoneIterator(true);
  }

  public void addRRset(RRset paramRRset)
  {
    addRRset(paramRRset.getName(), paramRRset);
  }

  public void addRecord(Record paramRecord)
  {
    Name localName = paramRecord.getName();
    int i = paramRecord.getRRsetType();
    try
    {
      RRset localRRset = findRRset(localName, i);
      if (localRRset == null)
        addRRset(localName, new RRset(paramRecord));
      while (true)
      {
        return;
        localRRset.addRR(paramRecord);
      }
    }
    finally
    {
    }
  }

  public RRset findExactMatch(Name paramName, int paramInt)
  {
    Object localObject = exactName(paramName);
    if (localObject == null)
      return null;
    return oneRRset(localObject, paramInt);
  }

  public SetResponse findRecords(Name paramName, int paramInt)
  {
    return lookup(paramName, paramInt);
  }

  public int getDClass()
  {
    return this.dclass;
  }

  public RRset getNS()
  {
    return this.NS;
  }

  public Name getOrigin()
  {
    return this.origin;
  }

  public SOARecord getSOA()
  {
    return this.SOA;
  }

  public Iterator iterator()
  {
    return new ZoneIterator(false);
  }

  public void removeRecord(Record paramRecord)
  {
    Name localName = paramRecord.getName();
    int i = paramRecord.getRRsetType();
    while (true)
    {
      RRset localRRset;
      try
      {
        localRRset = findRRset(localName, i);
        if (localRRset == null)
          return;
        if ((localRRset.size() == 1) && (localRRset.first().equals(paramRecord)))
        {
          removeRRset(localName, i);
          return;
        }
      }
      finally
      {
      }
      localRRset.deleteRR(paramRecord);
    }
  }

  public String toMasterFile()
  {
    StringBuffer localStringBuffer;
    try
    {
      Iterator localIterator = this.data.entrySet().iterator();
      localStringBuffer = new StringBuffer();
      nodeToString(localStringBuffer, this.originNode);
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        if (!this.origin.equals(localEntry.getKey()))
          nodeToString(localStringBuffer, localEntry.getValue());
      }
    }
    finally
    {
    }
    String str = localStringBuffer.toString();
    return str;
  }

  public String toString()
  {
    return toMasterFile();
  }

  class ZoneIterator
    implements Iterator
  {
    private int count;
    private RRset[] current;
    private boolean wantLastSOA;
    private Iterator zentries;

    ZoneIterator(boolean arg2)
    {
      while (true)
      {
        RRset[] arrayOfRRset1;
        int i;
        int j;
        int k;
        try
        {
          this.zentries = Zone.this.data.entrySet().iterator();
          boolean bool;
          this.wantLastSOA = bool;
          arrayOfRRset1 = Zone.this.allRRsets(Zone.this.originNode);
          this.current = new RRset[arrayOfRRset1.length];
          i = 0;
          j = 2;
          if (i >= arrayOfRRset1.length)
            break;
          k = arrayOfRRset1[i].getType();
          if (k == 6)
          {
            this.current[0] = arrayOfRRset1[i];
            i++;
            continue;
          }
        }
        finally
        {
        }
        if (k == 2)
        {
          this.current[1] = arrayOfRRset1[i];
        }
        else
        {
          RRset[] arrayOfRRset2 = this.current;
          int m = j + 1;
          arrayOfRRset2[j] = arrayOfRRset1[i];
          j = m;
        }
      }
    }

    public boolean hasNext()
    {
      return (this.current != null) || (this.wantLastSOA);
    }

    public Object next()
    {
      if (!hasNext())
        throw new NoSuchElementException();
      if (this.current == null)
        this.wantLastSOA = false;
      RRset localRRset;
      RRset[] arrayOfRRset2;
      do
      {
        Map.Entry localEntry;
        do
        {
          localRRset = Zone.this.oneRRset(Zone.this.originNode, 6);
          while (!this.zentries.hasNext())
          {
            do
            {
              return localRRset;
              RRset[] arrayOfRRset1 = this.current;
              int i = this.count;
              this.count = (i + 1);
              localRRset = arrayOfRRset1[i];
            }
            while (this.count != this.current.length);
            this.current = null;
          }
          localEntry = (Map.Entry)this.zentries.next();
        }
        while (localEntry.getKey().equals(Zone.this.origin));
        arrayOfRRset2 = Zone.this.allRRsets(localEntry.getValue());
      }
      while (arrayOfRRset2.length == 0);
      this.current = arrayOfRRset2;
      this.count = 0;
      return localRRset;
    }

    public void remove()
    {
      throw new UnsupportedOperationException();
    }
  }
}