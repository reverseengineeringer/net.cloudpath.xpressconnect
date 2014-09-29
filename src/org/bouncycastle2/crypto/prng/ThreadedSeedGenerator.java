package org.bouncycastle2.crypto.prng;

public class ThreadedSeedGenerator
{
  public byte[] generateSeed(int paramInt, boolean paramBoolean)
  {
    return new SeedGenerator(null).generateSeed(paramInt, paramBoolean);
  }

  private class SeedGenerator
    implements Runnable
  {
    private volatile int counter = 0;
    private volatile boolean stop = false;

    private SeedGenerator()
    {
    }

    public byte[] generateSeed(int paramInt, boolean paramBoolean)
    {
      Thread localThread = new Thread(this);
      byte[] arrayOfByte = new byte[paramInt];
      this.counter = 0;
      this.stop = false;
      int i = 0;
      localThread.start();
      int j;
      int k;
      if (paramBoolean)
      {
        j = paramInt;
        k = 0;
      }
      while (true)
      {
        if (k >= j)
        {
          this.stop = true;
          return arrayOfByte;
          j = paramInt * 8;
          break;
        }
        try
        {
          label69: 
          do
            Thread.sleep(1L);
          while (this.counter == i);
          i = this.counter;
          if (paramBoolean)
            arrayOfByte[k] = ((byte)(i & 0xFF));
          while (true)
          {
            k++;
            break;
            int m = k / 8;
            arrayOfByte[m] = ((byte)(arrayOfByte[m] << 1 | i & 0x1));
          }
        }
        catch (InterruptedException localInterruptedException)
        {
          break label69;
        }
      }
    }

    public void run()
    {
      while (true)
      {
        if (this.stop)
          return;
        this.counter = (1 + this.counter);
      }
    }
  }
}