package org.xbill.DNS;

import java.io.IOException;
import java.util.Random;

public class Header
  implements Cloneable
{
  public static final int LENGTH = 12;
  private static Random random = new Random();
  private int[] counts;
  private int flags;
  private int id;

  public Header()
  {
    init();
  }

  public Header(int paramInt)
  {
    init();
    setID(paramInt);
  }

  Header(DNSInput paramDNSInput)
    throws IOException
  {
    this(paramDNSInput.readU16());
    this.flags = paramDNSInput.readU16();
    for (int i = 0; i < this.counts.length; i++)
      this.counts[i] = paramDNSInput.readU16();
  }

  public Header(byte[] paramArrayOfByte)
    throws IOException
  {
    this(new DNSInput(paramArrayOfByte));
  }

  private static void checkFlag(int paramInt)
  {
    if (!validFlag(paramInt))
      throw new IllegalArgumentException("invalid flag bit " + paramInt);
  }

  private void init()
  {
    this.counts = new int[4];
    this.flags = 0;
    this.id = -1;
  }

  private static boolean validFlag(int paramInt)
  {
    return (paramInt >= 0) && (paramInt <= 15) && (Flags.isFlag(paramInt));
  }

  public Object clone()
  {
    Header localHeader = new Header();
    localHeader.id = this.id;
    localHeader.flags = this.flags;
    System.arraycopy(this.counts, 0, localHeader.counts, 0, this.counts.length);
    return localHeader;
  }

  void decCount(int paramInt)
  {
    if (this.counts[paramInt] == 0)
      throw new IllegalStateException("DNS section count cannot be decremented");
    int[] arrayOfInt = this.counts;
    arrayOfInt[paramInt] = (-1 + arrayOfInt[paramInt]);
  }

  public int getCount(int paramInt)
  {
    return this.counts[paramInt];
  }

  public boolean getFlag(int paramInt)
  {
    checkFlag(paramInt);
    return (this.flags & 1 << 15 - paramInt) != 0;
  }

  boolean[] getFlags()
  {
    boolean[] arrayOfBoolean = new boolean[16];
    for (int i = 0; i < arrayOfBoolean.length; i++)
      if (validFlag(i))
        arrayOfBoolean[i] = getFlag(i);
    return arrayOfBoolean;
  }

  public int getID()
  {
    if (this.id >= 0)
      return this.id;
    try
    {
      if (this.id < 0)
        this.id = random.nextInt(65535);
      int i = this.id;
      return i;
    }
    finally
    {
    }
  }

  public int getOpcode()
  {
    return 0xF & this.flags >> 11;
  }

  public int getRcode()
  {
    return 0xF & this.flags;
  }

  void incCount(int paramInt)
  {
    if (this.counts[paramInt] == 65535)
      throw new IllegalStateException("DNS section count cannot be incremented");
    int[] arrayOfInt = this.counts;
    arrayOfInt[paramInt] = (1 + arrayOfInt[paramInt]);
  }

  public String printFlags()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < 16; i++)
      if ((validFlag(i)) && (getFlag(i)))
      {
        localStringBuffer.append(Flags.string(i));
        localStringBuffer.append(" ");
      }
    return localStringBuffer.toString();
  }

  void setCount(int paramInt1, int paramInt2)
  {
    if ((paramInt2 < 0) || (paramInt2 > 65535))
      throw new IllegalArgumentException("DNS section count " + paramInt2 + " is out of range");
    this.counts[paramInt1] = paramInt2;
  }

  public void setFlag(int paramInt)
  {
    checkFlag(paramInt);
    this.flags |= 1 << 15 - paramInt;
  }

  public void setID(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > 65535))
      throw new IllegalArgumentException("DNS message ID " + paramInt + " is out of range");
    this.id = paramInt;
  }

  public void setOpcode(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > 15))
      throw new IllegalArgumentException("DNS Opcode " + paramInt + "is out of range");
    this.flags = (0x87FF & this.flags);
    this.flags |= paramInt << 11;
  }

  public void setRcode(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > 15))
      throw new IllegalArgumentException("DNS Rcode " + paramInt + " is out of range");
    this.flags = (0xFFFFFFF0 & this.flags);
    this.flags = (paramInt | this.flags);
  }

  public String toString()
  {
    return toStringWithRcode(getRcode());
  }

  String toStringWithRcode(int paramInt)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(";; ->>HEADER<<- ");
    localStringBuffer.append("opcode: " + Opcode.string(getOpcode()));
    localStringBuffer.append(", status: " + Rcode.string(paramInt));
    localStringBuffer.append(", id: " + getID());
    localStringBuffer.append("\n");
    localStringBuffer.append(";; flags: " + printFlags());
    localStringBuffer.append("; ");
    for (int i = 0; i < 4; i++)
      localStringBuffer.append(Section.string(i) + ": " + getCount(i) + " ");
    return localStringBuffer.toString();
  }

  void toWire(DNSOutput paramDNSOutput)
  {
    paramDNSOutput.writeU16(getID());
    paramDNSOutput.writeU16(this.flags);
    for (int i = 0; i < this.counts.length; i++)
      paramDNSOutput.writeU16(this.counts[i]);
  }

  public byte[] toWire()
  {
    DNSOutput localDNSOutput = new DNSOutput();
    toWire(localDNSOutput);
    return localDNSOutput.toByteArray();
  }

  public void unsetFlag(int paramInt)
  {
    checkFlag(paramInt);
    this.flags &= (0xFFFFFFFF ^ 1 << 15 - paramInt);
  }
}