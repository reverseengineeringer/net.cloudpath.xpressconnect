package org.bouncycastle2.crypto.prng;

public class ReversedWindowGenerator
  implements RandomGenerator
{
  private final RandomGenerator generator;
  private byte[] window;
  private int windowCount;

  public ReversedWindowGenerator(RandomGenerator paramRandomGenerator, int paramInt)
  {
    if (paramRandomGenerator == null)
      throw new IllegalArgumentException("generator cannot be null");
    if (paramInt < 2)
      throw new IllegalArgumentException("windowSize must be at least 2");
    this.generator = paramRandomGenerator;
    this.window = new byte[paramInt];
  }

  // ERROR //
  private void doNextBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: istore 4
    //   5: iload 4
    //   7: iload_3
    //   8: if_icmplt +6 -> 14
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: aload_0
    //   15: getfield 34	org/bouncycastle2/crypto/prng/ReversedWindowGenerator:windowCount	I
    //   18: iconst_1
    //   19: if_icmpge +31 -> 50
    //   22: aload_0
    //   23: getfield 28	org/bouncycastle2/crypto/prng/ReversedWindowGenerator:generator	Lorg/bouncycastle2/crypto/prng/RandomGenerator;
    //   26: aload_0
    //   27: getfield 30	org/bouncycastle2/crypto/prng/ReversedWindowGenerator:window	[B
    //   30: iconst_0
    //   31: aload_0
    //   32: getfield 30	org/bouncycastle2/crypto/prng/ReversedWindowGenerator:window	[B
    //   35: arraylength
    //   36: invokeinterface 37 4 0
    //   41: aload_0
    //   42: aload_0
    //   43: getfield 30	org/bouncycastle2/crypto/prng/ReversedWindowGenerator:window	[B
    //   46: arraylength
    //   47: putfield 34	org/bouncycastle2/crypto/prng/ReversedWindowGenerator:windowCount	I
    //   50: iload 4
    //   52: iconst_1
    //   53: iadd
    //   54: istore 7
    //   56: iload_2
    //   57: iload 4
    //   59: iadd
    //   60: istore 8
    //   62: aload_0
    //   63: getfield 30	org/bouncycastle2/crypto/prng/ReversedWindowGenerator:window	[B
    //   66: astore 9
    //   68: iconst_m1
    //   69: aload_0
    //   70: getfield 34	org/bouncycastle2/crypto/prng/ReversedWindowGenerator:windowCount	I
    //   73: iadd
    //   74: istore 10
    //   76: aload_0
    //   77: iload 10
    //   79: putfield 34	org/bouncycastle2/crypto/prng/ReversedWindowGenerator:windowCount	I
    //   82: aload_1
    //   83: iload 8
    //   85: aload 9
    //   87: iload 10
    //   89: baload
    //   90: bastore
    //   91: iload 7
    //   93: istore 4
    //   95: goto -90 -> 5
    //   98: aload_0
    //   99: monitorexit
    //   100: aload 5
    //   102: athrow
    //   103: astore 5
    //   105: goto -7 -> 98
    //   108: astore 5
    //   110: iload 4
    //   112: pop
    //   113: goto -15 -> 98
    //
    // Exception table:
    //   from	to	target	type
    //   62	91	103	finally
    //   98	100	103	finally
    //   11	13	108	finally
    //   14	50	108	finally
  }

  public void addSeedMaterial(long paramLong)
  {
    try
    {
      this.windowCount = 0;
      this.generator.addSeedMaterial(paramLong);
      return;
    }
    finally
    {
    }
  }

  public void addSeedMaterial(byte[] paramArrayOfByte)
  {
    try
    {
      this.windowCount = 0;
      this.generator.addSeedMaterial(paramArrayOfByte);
      return;
    }
    finally
    {
    }
  }

  public void nextBytes(byte[] paramArrayOfByte)
  {
    doNextBytes(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public void nextBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    doNextBytes(paramArrayOfByte, paramInt1, paramInt2);
  }
}