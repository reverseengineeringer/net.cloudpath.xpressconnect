package org.xbill.DNS;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class Cache
{
  private static final int defaultMaxEntries = 50000;
  private CacheMap data;
  private int dclass;
  private int maxcache = -1;
  private int maxncache = -1;

  public Cache()
  {
    this(1);
  }

  public Cache(int paramInt)
  {
    this.dclass = paramInt;
    this.data = new CacheMap(50000);
  }

  public Cache(String paramString)
    throws IOException
  {
    this.data = new CacheMap(50000);
    Master localMaster = new Master(paramString);
    while (true)
    {
      Record localRecord = localMaster.nextRecord();
      if (localRecord == null)
        break;
      addRecord(localRecord, 0, localMaster);
    }
  }

  private void addElement(Name paramName, Element paramElement)
  {
    while (true)
    {
      Object localObject2;
      int i;
      List localList;
      int j;
      try
      {
        localObject2 = this.data.get(paramName);
        if (localObject2 == null)
        {
          this.data.put(paramName, paramElement);
          return;
        }
        i = paramElement.getType();
        if (!(localObject2 instanceof List))
          break label127;
        localList = (List)localObject2;
        j = 0;
        if (j >= localList.size())
          break label115;
        if (((Element)localList.get(j)).getType() == i)
        {
          localList.set(j, paramElement);
          continue;
        }
      }
      finally
      {
      }
      j++;
      continue;
      label115: localList.add(paramElement);
      continue;
      label127: Element localElement = (Element)localObject2;
      if (localElement.getType() == i)
      {
        this.data.put(paramName, paramElement);
      }
      else
      {
        LinkedList localLinkedList = new LinkedList();
        localLinkedList.add(localElement);
        localLinkedList.add(paramElement);
        this.data.put(paramName, localLinkedList);
      }
    }
  }

  private Element[] allElements(Object paramObject)
  {
    try
    {
      List localList;
      if ((paramObject instanceof List))
        localList = (List)paramObject;
      for (Element[] arrayOfElement = (Element[])localList.toArray(new Element[localList.size()]); ; arrayOfElement = new Element[] { (Element)paramObject })
        return arrayOfElement;
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

  private Element findElement(Name paramName, int paramInt1, int paramInt2)
  {
    try
    {
      Object localObject2 = exactName(paramName);
      if (localObject2 == null);
      Element localElement;
      for (Object localObject3 = null; ; localObject3 = localElement)
      {
        return localObject3;
        localElement = oneElement(paramName, localObject2, paramInt1, paramInt2);
      }
    }
    finally
    {
    }
  }

  private RRset[] findRecords(Name paramName, int paramInt1, int paramInt2)
  {
    SetResponse localSetResponse = lookupRecords(paramName, paramInt1, paramInt2);
    if (localSetResponse.isSuccessful())
      return localSetResponse.answers();
    return null;
  }

  private final int getCred(int paramInt, boolean paramBoolean)
  {
    if (paramInt == 1)
      if (!paramBoolean);
    do
    {
      return 4;
      return 3;
      if (paramInt != 2)
        break;
    }
    while (paramBoolean);
    return 3;
    if (paramInt == 3)
      return 1;
    throw new IllegalArgumentException("getCred: invalid section");
  }

  private static int limitExpire(long paramLong1, long paramLong2)
  {
    if ((paramLong2 >= 0L) && (paramLong2 < paramLong1))
      paramLong1 = paramLong2;
    long l = paramLong1 + System.currentTimeMillis() / 1000L;
    if ((l < 0L) || (l > 2147483647L))
      return 2147483647;
    return (int)l;
  }

  private static void markAdditional(RRset paramRRset, Set paramSet)
  {
    if (paramRRset.first().getAdditionalName() == null);
    while (true)
    {
      return;
      Iterator localIterator = paramRRset.rrs();
      while (localIterator.hasNext())
      {
        Name localName = ((Record)localIterator.next()).getAdditionalName();
        if (localName != null)
          paramSet.add(localName);
      }
    }
  }

  private Element oneElement(Name paramName, Object paramObject, int paramInt1, int paramInt2)
  {
    if (paramInt1 == 255)
      try
      {
        throw new IllegalArgumentException("oneElement(ANY)");
      }
      finally
      {
      }
    int k;
    Object localObject1;
    if ((paramObject instanceof List))
    {
      List localList = (List)paramObject;
      k = 0;
      int m = localList.size();
      localObject1 = null;
      if (k < m)
      {
        Element localElement2 = (Element)localList.get(k);
        int n = localElement2.getType();
        if (n == paramInt1)
          localObject1 = localElement2;
      }
      else
      {
        label94: if (localObject1 != null)
          break label144;
        localObject1 = null;
      }
    }
    while (true)
    {
      return localObject1;
      k++;
      break;
      Element localElement1 = (Element)paramObject;
      int i = localElement1.getType();
      localObject1 = null;
      if (i != paramInt1)
        break label94;
      localObject1 = localElement1;
      break label94;
      label144: if (localObject1.expired())
      {
        removeElement(paramName, paramInt1);
        localObject1 = null;
      }
      else
      {
        int j = localObject1.compareCredibility(paramInt2);
        if (j < 0)
          localObject1 = null;
      }
    }
  }

  private void removeElement(Name paramName, int paramInt)
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
          break label114;
        List localList = (List)localObject2;
        i = 0;
        if (i >= localList.size())
          continue;
        if (((Element)localList.get(i)).getType() == paramInt)
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
      label114: if (((Element)localObject2).getType() == paramInt)
        this.data.remove(paramName);
    }
  }

  private void removeName(Name paramName)
  {
    try
    {
      this.data.remove(paramName);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public SetResponse addMessage(Message paramMessage)
  {
    boolean bool1 = paramMessage.getHeader().getFlag(5);
    Record localRecord = paramMessage.getQuestion();
    int i = paramMessage.getHeader().getRcode();
    int j = 0;
    SetResponse localSetResponse = null;
    boolean bool2 = Options.check("verbosecache");
    if (((i != 0) && (i != 3)) || (localRecord == null))
      return null;
    Name localName1 = localRecord.getName();
    int k = localRecord.getType();
    int m = localRecord.getDClass();
    Object localObject = localName1;
    HashSet localHashSet = new HashSet();
    RRset[] arrayOfRRset1 = paramMessage.getSectionRRsets(1);
    int n = 0;
    if (n < arrayOfRRset1.length)
    {
      if (arrayOfRRset1[n].getDClass() != m);
      while (true)
      {
        n++;
        break;
        int i10 = arrayOfRRset1[n].getType();
        Name localName2 = arrayOfRRset1[n].getName();
        int i11 = getCred(1, bool1);
        if (((i10 == k) || (k == 255)) && (localName2.equals(localObject)))
        {
          addRRset(arrayOfRRset1[n], i11);
          j = 1;
          if (localObject == localName1)
          {
            if (localSetResponse == null)
              localSetResponse = new SetResponse(6);
            RRset localRRset5 = arrayOfRRset1[n];
            localSetResponse.addRRset(localRRset5);
          }
          markAdditional(arrayOfRRset1[n], localHashSet);
        }
        else if ((i10 == 5) && (localName2.equals(localObject)))
        {
          addRRset(arrayOfRRset1[n], i11);
          if (localObject == localName1)
          {
            RRset localRRset4 = arrayOfRRset1[n];
            localSetResponse = new SetResponse(4, localRRset4);
          }
          localObject = ((CNAMERecord)arrayOfRRset1[n].first()).getTarget();
        }
        else if ((i10 == 39) && (((Name)localObject).subdomain(localName2)))
        {
          addRRset(arrayOfRRset1[n], i11);
          if (localObject == localName1)
          {
            RRset localRRset3 = arrayOfRRset1[n];
            localSetResponse = new SetResponse(5, localRRset3);
          }
          DNAMERecord localDNAMERecord = (DNAMERecord)arrayOfRRset1[n].first();
          try
          {
            Name localName3 = ((Name)localObject).fromDNAME(localDNAMERecord);
            localObject = localName3;
          }
          catch (NameTooLongException localNameTooLongException)
          {
          }
        }
      }
    }
    RRset[] arrayOfRRset2 = paramMessage.getSectionRRsets(2);
    RRset localRRset1 = null;
    RRset localRRset2 = null;
    int i1 = 0;
    if (i1 < arrayOfRRset2.length)
    {
      if ((arrayOfRRset2[i1].getType() == 6) && (((Name)localObject).subdomain(arrayOfRRset2[i1].getName())))
        localRRset1 = arrayOfRRset2[i1];
      while (true)
      {
        i1++;
        break;
        if ((arrayOfRRset2[i1].getType() == 2) && (((Name)localObject).subdomain(arrayOfRRset2[i1].getName())))
          localRRset2 = arrayOfRRset2[i1];
      }
    }
    int i6;
    int i8;
    label581: label588: RRset[] arrayOfRRset3;
    int i3;
    if (j == 0)
      if (i == 3)
      {
        i6 = 0;
        if ((i != 3) && (localRRset1 == null) && (localRRset2 != null))
          break label655;
        int i7 = getCred(2, bool1);
        SOARecord localSOARecord = null;
        if (localRRset1 != null)
          localSOARecord = (SOARecord)localRRset1.first();
        addNegative((Name)localObject, i6, localSOARecord, i7);
        if (localSetResponse == null)
        {
          if (i != 3)
            break label649;
          i8 = 1;
          localSetResponse = SetResponse.ofType(i8);
        }
        arrayOfRRset3 = paramMessage.getSectionRRsets(3);
        i3 = 0;
        label598: if (i3 >= arrayOfRRset3.length)
          break label772;
        int i4 = arrayOfRRset3[i3].getType();
        if ((i4 == 1) || (i4 == 28) || (i4 == 38))
          break label734;
      }
    while (true)
    {
      i3++;
      break label598;
      i6 = k;
      break;
      label649: i8 = 2;
      break label581;
      label655: int i9 = getCred(2, bool1);
      addRRset(localRRset2, i9);
      markAdditional(localRRset2, localHashSet);
      if (localSetResponse != null)
        break label588;
      localSetResponse = new SetResponse(3, localRRset2);
      break label588;
      if ((i != 0) || (localRRset2 == null))
        break label588;
      int i2 = getCred(2, bool1);
      addRRset(localRRset2, i2);
      markAdditional(localRRset2, localHashSet);
      break label588;
      label734: if (localHashSet.contains(arrayOfRRset3[i3].getName()))
      {
        int i5 = getCred(3, bool1);
        addRRset(arrayOfRRset3[i3], i5);
      }
    }
    label772: if (bool2)
      System.out.println("addMessage: " + localSetResponse);
    return localSetResponse;
  }

  public void addNegative(Name paramName, int paramInt1, SOARecord paramSOARecord, int paramInt2)
  {
    long l = 0L;
    if (paramSOARecord != null);
    try
    {
      l = paramSOARecord.getTTL();
      Element localElement = findElement(paramName, paramInt1, 0);
      if (l == 0L)
        if ((localElement != null) && (localElement.compareCredibility(paramInt2) <= 0))
          removeElement(paramName, paramInt1);
      while (true)
      {
        return;
        if ((localElement != null) && (localElement.compareCredibility(paramInt2) <= 0))
          localElement = null;
        if (localElement == null)
          addElement(paramName, new NegativeElement(paramName, paramInt1, paramSOARecord, paramInt2, this.maxncache));
      }
    }
    finally
    {
    }
  }

  public void addRRset(RRset paramRRset, int paramInt)
  {
    while (true)
    {
      try
      {
        long l = paramRRset.getTTL();
        Name localName = paramRRset.getName();
        int i = paramRRset.getType();
        Element localElement = findElement(localName, i, 0);
        if (l == 0L)
        {
          if ((localElement != null) && (localElement.compareCredibility(paramInt) <= 0))
            removeElement(localName, i);
          return;
        }
        if ((localElement != null) && (localElement.compareCredibility(paramInt) <= 0))
          localElement = null;
        if (localElement != null)
          continue;
        if ((paramRRset instanceof CacheRRset))
        {
          localCacheRRset = (CacheRRset)paramRRset;
          addElement(localName, localCacheRRset);
          continue;
        }
      }
      finally
      {
      }
      CacheRRset localCacheRRset = new CacheRRset(paramRRset, paramInt, this.maxcache);
    }
  }

  public void addRecord(Record paramRecord, int paramInt, Object paramObject)
  {
    while (true)
    {
      Element localElement;
      try
      {
        Name localName = paramRecord.getName();
        int i = paramRecord.getRRsetType();
        boolean bool = Type.isRR(i);
        if (!bool)
          return;
        localElement = findElement(localName, i, paramInt);
        if (localElement == null)
        {
          addRRset(new CacheRRset(paramRecord, paramInt, this.maxcache), paramInt);
          continue;
        }
      }
      finally
      {
      }
      if ((localElement.compareCredibility(paramInt) == 0) && ((localElement instanceof CacheRRset)))
        ((CacheRRset)localElement).addRR(paramRecord);
    }
  }

  public void clearCache()
  {
    try
    {
      this.data.clear();
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  public RRset[] findAnyRecords(Name paramName, int paramInt)
  {
    return findRecords(paramName, paramInt, 2);
  }

  public RRset[] findRecords(Name paramName, int paramInt)
  {
    return findRecords(paramName, paramInt, 3);
  }

  public void flushName(Name paramName)
  {
    removeName(paramName);
  }

  public void flushSet(Name paramName, int paramInt)
  {
    removeElement(paramName, paramInt);
  }

  public int getDClass()
  {
    return this.dclass;
  }

  public int getMaxCache()
  {
    return this.maxcache;
  }

  public int getMaxEntries()
  {
    return this.data.getMaxSize();
  }

  public int getMaxNCache()
  {
    return this.maxncache;
  }

  public int getSize()
  {
    return this.data.size();
  }

  protected SetResponse lookup(Name paramName, int paramInt1, int paramInt2)
  {
    while (true)
    {
      int i;
      int j;
      int k;
      Name localName;
      Object localObject3;
      int m;
      Object localObject2;
      int i1;
      Element localElement3;
      try
      {
        i = paramName.labels();
        j = i;
        break label434;
        if (k == 0)
          break label480;
        localName = Name.root;
        localObject3 = this.data.get(localName);
        if (localObject3 == null)
        {
          break label462;
          localName = new Name(paramName, i - j);
          continue;
        }
        if ((m != 0) && (paramInt1 == 255))
        {
          localObject2 = new SetResponse(6);
          Element[] arrayOfElement = allElements(localObject3);
          int n = 0;
          i1 = 0;
          if (i1 < arrayOfElement.length)
          {
            Element localElement5 = arrayOfElement[i1];
            if (localElement5.expired())
            {
              removeElement(localName, localElement5.getType());
              break label491;
            }
            if ((!(localElement5 instanceof CacheRRset)) || (localElement5.compareCredibility(paramInt2) < 0))
              break label491;
            ((SetResponse)localObject2).addRRset((CacheRRset)localElement5);
            n++;
            break label491;
          }
          if (n <= 0)
            break label351;
          return localObject2;
        }
        if (m == 0)
          break label307;
        localElement3 = oneElement(localName, localObject3, paramInt1, paramInt2);
        if ((localElement3 != null) && ((localElement3 instanceof CacheRRset)))
        {
          localObject2 = new SetResponse(6);
          ((SetResponse)localObject2).addRRset((CacheRRset)localElement3);
          continue;
        }
      }
      finally
      {
      }
      if (localElement3 != null)
      {
        localObject2 = new SetResponse(2);
      }
      else
      {
        Element localElement4 = oneElement(localName, localObject3, 5, paramInt2);
        if ((localElement4 != null) && ((localElement4 instanceof CacheRRset)))
        {
          localObject2 = new SetResponse(4, (CacheRRset)localElement4);
          continue;
          label307: Element localElement1 = oneElement(localName, localObject3, 39, paramInt2);
          if ((localElement1 != null) && ((localElement1 instanceof CacheRRset)))
            localObject2 = new SetResponse(5, (CacheRRset)localElement1);
        }
        else
        {
          label351: Element localElement2 = oneElement(localName, localObject3, 2, paramInt2);
          if ((localElement2 != null) && ((localElement2 instanceof CacheRRset)))
          {
            localObject2 = new SetResponse(3, (CacheRRset)localElement2);
          }
          else if ((m != 0) && (oneElement(localName, localObject3, 0, paramInt2) != null))
          {
            localObject2 = SetResponse.ofType(1);
            continue;
            label421: SetResponse localSetResponse = SetResponse.ofType(0);
            localObject2 = localSetResponse;
            continue;
            label434: if (j >= 1)
              if (j != 1)
                break label468;
          }
          else
          {
            label462: label468: for (k = 1; ; k = 0)
            {
              if (j != i)
                break label474;
              m = 1;
              break;
              j--;
              break label434;
              break label421;
            }
            label474: m = 0;
            continue;
            label480: if (m != 0)
            {
              localName = paramName;
              continue;
              label491: i1++;
            }
          }
        }
      }
    }
  }

  public SetResponse lookupRecords(Name paramName, int paramInt1, int paramInt2)
  {
    return lookup(paramName, paramInt1, paramInt2);
  }

  public void setMaxCache(int paramInt)
  {
    this.maxcache = paramInt;
  }

  public void setMaxEntries(int paramInt)
  {
    this.data.setMaxSize(paramInt);
  }

  public void setMaxNCache(int paramInt)
  {
    this.maxncache = paramInt;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    try
    {
      Iterator localIterator = this.data.values().iterator();
      while (localIterator.hasNext())
      {
        Element[] arrayOfElement = allElements(localIterator.next());
        for (int i = 0; i < arrayOfElement.length; i++)
        {
          localStringBuffer.append(arrayOfElement[i]);
          localStringBuffer.append("\n");
        }
      }
      return localStringBuffer.toString();
    }
    finally
    {
    }
  }

  private static class CacheMap extends LinkedHashMap
  {
    private int maxsize = -1;

    CacheMap(int paramInt)
    {
      super(0.75F, true);
      this.maxsize = paramInt;
    }

    int getMaxSize()
    {
      return this.maxsize;
    }

    protected boolean removeEldestEntry(Map.Entry paramEntry)
    {
      return (this.maxsize >= 0) && (size() > this.maxsize);
    }

    void setMaxSize(int paramInt)
    {
      this.maxsize = paramInt;
    }
  }

  private static class CacheRRset extends RRset
    implements Cache.Element
  {
    private static final long serialVersionUID = 5971755205903597024L;
    int credibility;
    int expire;

    public CacheRRset(RRset paramRRset, int paramInt, long paramLong)
    {
      super();
      this.credibility = paramInt;
      this.expire = Cache.limitExpire(paramRRset.getTTL(), paramLong);
    }

    public CacheRRset(Record paramRecord, int paramInt, long paramLong)
    {
      this.credibility = paramInt;
      this.expire = Cache.limitExpire(paramRecord.getTTL(), paramLong);
      addRR(paramRecord);
    }

    public final int compareCredibility(int paramInt)
    {
      return this.credibility - paramInt;
    }

    public final boolean expired()
    {
      return (int)(System.currentTimeMillis() / 1000L) >= this.expire;
    }

    public String toString()
    {
      StringBuffer localStringBuffer = new StringBuffer();
      localStringBuffer.append(super.toString());
      localStringBuffer.append(" cl = ");
      localStringBuffer.append(this.credibility);
      return localStringBuffer.toString();
    }
  }

  private static abstract interface Element
  {
    public abstract int compareCredibility(int paramInt);

    public abstract boolean expired();

    public abstract int getType();
  }

  private static class NegativeElement
    implements Cache.Element
  {
    int credibility;
    int expire;
    Name name;
    int type;

    public NegativeElement(Name paramName, int paramInt1, SOARecord paramSOARecord, int paramInt2, long paramLong)
    {
      this.name = paramName;
      this.type = paramInt1;
      long l = 0L;
      if (paramSOARecord != null)
        l = paramSOARecord.getMinimum();
      this.credibility = paramInt2;
      this.expire = Cache.limitExpire(l, paramLong);
    }

    public final int compareCredibility(int paramInt)
    {
      return this.credibility - paramInt;
    }

    public final boolean expired()
    {
      return (int)(System.currentTimeMillis() / 1000L) >= this.expire;
    }

    public int getType()
    {
      return this.type;
    }

    public String toString()
    {
      StringBuffer localStringBuffer = new StringBuffer();
      if (this.type == 0)
        localStringBuffer.append("NXDOMAIN " + this.name);
      while (true)
      {
        localStringBuffer.append(" cl = ");
        localStringBuffer.append(this.credibility);
        return localStringBuffer.toString();
        localStringBuffer.append("NXRRSET " + this.name + " " + Type.string(this.type));
      }
    }
  }
}