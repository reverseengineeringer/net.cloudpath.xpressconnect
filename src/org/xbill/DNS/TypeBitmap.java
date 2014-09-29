package org.xbill.DNS;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.TreeSet;

final class TypeBitmap
  implements Serializable
{
  private static final long serialVersionUID = -125354057735389003L;
  private TreeSet types = new TreeSet();

  private TypeBitmap()
  {
  }

  public TypeBitmap(DNSInput paramDNSInput)
    throws WireParseException
  {
    this();
    while (paramDNSInput.remaining() > 0)
    {
      if (paramDNSInput.remaining() < 2)
        throw new WireParseException("invalid bitmap descriptor");
      int i = paramDNSInput.readU8();
      if (i < -1)
        throw new WireParseException("invalid ordering");
      int j = paramDNSInput.readU8();
      if (j > paramDNSInput.remaining())
        throw new WireParseException("invalid bitmap");
      int m;
      for (int k = 0; k < j; k++)
      {
        m = paramDNSInput.readU8();
        if (m != 0)
          break label98;
      }
      continue;
      label98: int n = 0;
      label101: if (n < 8)
        if ((m & 1 << 7 - n) != 0)
          break label127;
      while (true)
      {
        n++;
        break label101;
        break;
        label127: int i1 = n + (i * 256 + k * 8);
        this.types.add(Mnemonic.toInteger(i1));
      }
    }
  }

  public TypeBitmap(Tokenizer paramTokenizer)
    throws IOException
  {
    this();
    while (true)
    {
      Tokenizer.Token localToken = paramTokenizer.get();
      if (!localToken.isString())
      {
        paramTokenizer.unget();
        return;
      }
      int i = Type.value(localToken.value);
      if (i < 0)
        throw paramTokenizer.exception("Invalid type: " + localToken.value);
      this.types.add(Mnemonic.toInteger(i));
    }
  }

  public TypeBitmap(int[] paramArrayOfInt)
  {
    this();
    for (int i = 0; i < paramArrayOfInt.length; i++)
    {
      Type.check(paramArrayOfInt[i]);
      this.types.add(new Integer(paramArrayOfInt[i]));
    }
  }

  private static void mapToWire(DNSOutput paramDNSOutput, TreeSet paramTreeSet, int paramInt)
  {
    int i = 1 + (0xFF & ((Integer)paramTreeSet.last()).intValue()) / 8;
    int[] arrayOfInt = new int[i];
    paramDNSOutput.writeU8(paramInt);
    paramDNSOutput.writeU8(i);
    Iterator localIterator = paramTreeSet.iterator();
    while (localIterator.hasNext())
    {
      int k = ((Integer)localIterator.next()).intValue();
      int m = (k & 0xFF) / 8;
      arrayOfInt[m] |= 1 << 7 - k % 8;
    }
    for (int j = 0; j < i; j++)
      paramDNSOutput.writeU8(arrayOfInt[j]);
  }

  public boolean contains(int paramInt)
  {
    return this.types.contains(Mnemonic.toInteger(paramInt));
  }

  public boolean empty()
  {
    return this.types.isEmpty();
  }

  public int[] toArray()
  {
    int[] arrayOfInt = new int[this.types.size()];
    int i = 0;
    Iterator localIterator = this.types.iterator();
    while (localIterator.hasNext())
    {
      int j = i + 1;
      arrayOfInt[i] = ((Integer)localIterator.next()).intValue();
      i = j;
    }
    return arrayOfInt;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    Iterator localIterator = this.types.iterator();
    while (localIterator.hasNext())
    {
      localStringBuffer.append(Type.string(((Integer)localIterator.next()).intValue()));
      if (localIterator.hasNext())
        localStringBuffer.append(' ');
    }
    return localStringBuffer.toString();
  }

  public void toWire(DNSOutput paramDNSOutput)
  {
    if (this.types.size() == 0)
      return;
    int i = -1;
    TreeSet localTreeSet = new TreeSet();
    Iterator localIterator = this.types.iterator();
    while (localIterator.hasNext())
    {
      int j = ((Integer)localIterator.next()).intValue();
      int k = j >> 8;
      if (k != i)
      {
        if (localTreeSet.size() > 0)
        {
          mapToWire(paramDNSOutput, localTreeSet, i);
          localTreeSet.clear();
        }
        i = k;
      }
      localTreeSet.add(new Integer(j));
    }
    mapToWire(paramDNSOutput, localTreeSet, i);
  }
}