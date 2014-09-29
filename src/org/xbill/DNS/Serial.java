package org.xbill.DNS;

public final class Serial
{
  private static final long MAX32 = 4294967295L;

  public static int compare(long paramLong1, long paramLong2)
  {
    if ((paramLong1 < 0L) || (paramLong1 > 4294967295L))
      throw new IllegalArgumentException(paramLong1 + " out of range");
    if ((paramLong2 < 0L) || (paramLong2 > 4294967295L))
      throw new IllegalArgumentException(paramLong2 + " out of range");
    long l = paramLong1 - paramLong2;
    if (l >= 4294967295L)
      l -= 4294967296L;
    while (true)
    {
      return (int)l;
      if (l < -4294967295L)
        l += 4294967296L;
    }
  }

  public static long increment(long paramLong)
  {
    if ((paramLong < 0L) || (paramLong > 4294967295L))
      throw new IllegalArgumentException(paramLong + " out of range");
    if (paramLong == 4294967295L)
      return 0L;
    return 1L + paramLong;
  }
}