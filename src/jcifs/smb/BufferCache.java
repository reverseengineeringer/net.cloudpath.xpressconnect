package jcifs.smb;

import jcifs.Config;

public class BufferCache
{
  private static final int MAX_BUFFERS = Config.getInt("jcifs.smb.maxBuffers", 16);
  static Object[] cache = new Object[MAX_BUFFERS];
  private static int freeBuffers = 0;
  private static int numBuffers = 0;

  public static byte[] getBuffer()
    throws InterruptedException
  {
    synchronized (cache)
    {
      if (freeBuffers + (MAX_BUFFERS - numBuffers) < 1)
        cache.wait();
    }
    byte[] arrayOfByte = getBuffer0();
    return arrayOfByte;
  }

  private static byte[] getBuffer0()
  {
    if (freeBuffers > 0)
      for (int i = 0; i < MAX_BUFFERS; i++)
        if (cache[i] != null)
        {
          byte[] arrayOfByte2 = (byte[])cache[i];
          cache[i] = null;
          freeBuffers = -1 + freeBuffers;
          return arrayOfByte2;
        }
    byte[] arrayOfByte1 = new byte[65535];
    numBuffers = 1 + numBuffers;
    return arrayOfByte1;
  }

  static void getBuffers(SmbComTransaction paramSmbComTransaction, SmbComTransactionResponse paramSmbComTransactionResponse)
    throws InterruptedException
  {
    synchronized (cache)
    {
      if (freeBuffers + (MAX_BUFFERS - numBuffers) < 2)
        cache.wait();
    }
    paramSmbComTransaction.txn_buf = getBuffer0();
    paramSmbComTransactionResponse.txn_buf = getBuffer0();
  }

  public static void releaseBuffer(byte[] paramArrayOfByte)
  {
    Object[] arrayOfObject = cache;
    for (int i = 0; ; i++)
      try
      {
        if (i < MAX_BUFFERS)
        {
          if (cache[i] == null)
          {
            cache[i] = paramArrayOfByte;
            freeBuffers = 1 + freeBuffers;
            cache.notify();
          }
        }
        else
          return;
      }
      finally
      {
      }
  }
}