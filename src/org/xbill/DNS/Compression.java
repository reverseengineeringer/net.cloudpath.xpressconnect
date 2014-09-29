package org.xbill.DNS;

import java.io.PrintStream;

public class Compression
{
  private static final int MAX_POINTER = 16383;
  private static final int TABLE_SIZE = 17;
  private Entry[] table = new Entry[17];
  private boolean verbose = Options.check("verbosecompression");

  public void add(int paramInt, Name paramName)
  {
    if (paramInt > 16383);
    do
    {
      return;
      int i = (0x7FFFFFFF & paramName.hashCode()) % 17;
      Entry localEntry = new Entry(null);
      localEntry.name = paramName;
      localEntry.pos = paramInt;
      localEntry.next = this.table[i];
      this.table[i] = localEntry;
    }
    while (!this.verbose);
    System.err.println("Adding " + paramName + " at " + paramInt);
  }

  public int get(Name paramName)
  {
    int i = (0x7FFFFFFF & paramName.hashCode()) % 17;
    int j = -1;
    for (Entry localEntry = this.table[i]; localEntry != null; localEntry = localEntry.next)
      if (localEntry.name.equals(paramName))
        j = localEntry.pos;
    if (this.verbose)
      System.err.println("Looking for " + paramName + ", found " + j);
    return j;
  }

  private static class Entry
  {
    Name name;
    Entry next;
    int pos;

    private Entry()
    {
    }

    Entry(Compression.1 param1)
    {
      this();
    }
  }
}