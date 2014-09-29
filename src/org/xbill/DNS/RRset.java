package org.xbill.DNS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class RRset
  implements Serializable
{
  private static final long serialVersionUID = -3270249290171239695L;
  private short nsigs;
  private short position;
  private List rrs;

  public RRset()
  {
    this.rrs = new ArrayList(1);
    this.nsigs = 0;
    this.position = 0;
  }

  public RRset(RRset paramRRset)
  {
    try
    {
      this.rrs = ((List)((ArrayList)paramRRset.rrs).clone());
      this.nsigs = paramRRset.nsigs;
      this.position = paramRRset.position;
      return;
    }
    finally
    {
    }
  }

  public RRset(Record paramRecord)
  {
    this();
    safeAddRR(paramRecord);
  }

  private Iterator iterator(boolean paramBoolean1, boolean paramBoolean2)
  {
    while (true)
    {
      int k;
      try
      {
        int i = this.rrs.size();
        int j;
        Iterator localIterator2;
        if (paramBoolean1)
        {
          j = i - this.nsigs;
          if (j == 0)
          {
            Iterator localIterator1 = Collections.EMPTY_LIST.iterator();
            localIterator2 = localIterator1;
            return localIterator2;
          }
        }
        else
        {
          j = this.nsigs;
          continue;
          ArrayList localArrayList = new ArrayList(j);
          if (paramBoolean1)
          {
            localArrayList.addAll(this.rrs.subList(k, j));
            if (k != 0)
              localArrayList.addAll(this.rrs.subList(0, k));
            localIterator2 = localArrayList.iterator();
            continue;
            if (this.position >= j)
              this.position = 0;
            k = this.position;
            this.position = ((short)(k + 1));
            continue;
            k = i - this.nsigs;
            continue;
          }
          localArrayList.addAll(this.rrs.subList(k, i));
          continue;
        }
      }
      finally
      {
      }
      if (paramBoolean1)
        if (!paramBoolean2)
          k = 0;
    }
  }

  private String iteratorToString(Iterator paramIterator)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    while (paramIterator.hasNext())
    {
      Record localRecord = (Record)paramIterator.next();
      localStringBuffer.append("[");
      localStringBuffer.append(localRecord.rdataToString());
      localStringBuffer.append("]");
      if (paramIterator.hasNext())
        localStringBuffer.append(" ");
    }
    return localStringBuffer.toString();
  }

  private void safeAddRR(Record paramRecord)
  {
    if (!(paramRecord instanceof RRSIGRecord))
    {
      if (this.nsigs == 0)
      {
        this.rrs.add(paramRecord);
        return;
      }
      this.rrs.add(this.rrs.size() - this.nsigs, paramRecord);
      return;
    }
    this.rrs.add(paramRecord);
    this.nsigs = ((short)(1 + this.nsigs));
  }

  public void addRR(Record paramRecord)
  {
    Record localRecord1;
    try
    {
      if (this.rrs.size() == 0)
      {
        safeAddRR(paramRecord);
        return;
      }
      localRecord1 = first();
      if (!paramRecord.sameRRset(localRecord1))
        throw new IllegalArgumentException("record does not match rrset");
    }
    finally
    {
    }
    if (paramRecord.getTTL() != localRecord1.getTTL())
    {
      if (paramRecord.getTTL() <= localRecord1.getTTL())
        break label170;
      paramRecord = paramRecord.cloneRecord();
      paramRecord.setTTL(localRecord1.getTTL());
    }
    while (!this.rrs.contains(paramRecord))
    {
      safeAddRR(paramRecord);
      break;
      int i;
      while (i < this.rrs.size())
      {
        Record localRecord2 = ((Record)this.rrs.get(i)).cloneRecord();
        localRecord2.setTTL(paramRecord.getTTL());
        this.rrs.set(i, localRecord2);
        i++;
        continue;
        label170: i = 0;
      }
    }
  }

  public void clear()
  {
    try
    {
      this.rrs.clear();
      this.position = 0;
      this.nsigs = 0;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public void deleteRR(Record paramRecord)
  {
    try
    {
      if ((this.rrs.remove(paramRecord)) && ((paramRecord instanceof RRSIGRecord)))
        this.nsigs = ((short)(-1 + this.nsigs));
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public Record first()
  {
    try
    {
      if (this.rrs.size() == 0)
        throw new IllegalStateException("rrset is empty");
    }
    finally
    {
    }
    Record localRecord = (Record)this.rrs.get(0);
    return localRecord;
  }

  public int getDClass()
  {
    return first().getDClass();
  }

  public Name getName()
  {
    return first().getName();
  }

  public long getTTL()
  {
    try
    {
      long l = first().getTTL();
      return l;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public int getType()
  {
    return first().getRRsetType();
  }

  public Iterator rrs()
  {
    try
    {
      Iterator localIterator = iterator(true, true);
      return localIterator;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public Iterator rrs(boolean paramBoolean)
  {
    try
    {
      Iterator localIterator = iterator(true, paramBoolean);
      return localIterator;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public Iterator sigs()
  {
    try
    {
      Iterator localIterator = iterator(false, false);
      return localIterator;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public int size()
  {
    try
    {
      int i = this.rrs.size();
      int j = this.nsigs;
      int k = i - j;
      return k;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public String toString()
  {
    if (this.rrs == null)
      return "{empty}";
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("{ ");
    localStringBuffer.append(getName() + " ");
    localStringBuffer.append(getTTL() + " ");
    localStringBuffer.append(DClass.string(getDClass()) + " ");
    localStringBuffer.append(Type.string(getType()) + " ");
    localStringBuffer.append(iteratorToString(iterator(true, false)));
    if (this.nsigs > 0)
    {
      localStringBuffer.append(" sigs: ");
      localStringBuffer.append(iteratorToString(iterator(false, false)));
    }
    localStringBuffer.append(" }");
    return localStringBuffer.toString();
  }
}