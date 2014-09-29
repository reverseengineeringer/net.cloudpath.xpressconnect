package org.xbill.DNS;

import java.io.IOException;
import java.io.PrintStream;
import java.net.SocketTimeoutException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import org.xbill.DNS.utils.hexdump;

class Client
{
  protected long endTime;
  protected SelectionKey key;

  protected Client(SelectableChannel paramSelectableChannel, long paramLong)
    throws IOException
  {
    Selector localSelector = null;
    this.endTime = paramLong;
    try
    {
      localSelector = Selector.open();
      paramSelectableChannel.configureBlocking(false);
      this.key = paramSelectableChannel.register(localSelector, 1);
      if ((1 == 0) && (localSelector != null))
        localSelector.close();
      if (1 == 0)
        paramSelectableChannel.close();
      return;
    }
    finally
    {
      if ((0 == 0) && (localSelector != null))
        localSelector.close();
      if (0 == 0)
        paramSelectableChannel.close();
    }
  }

  protected static void blockUntil(SelectionKey paramSelectionKey, long paramLong)
    throws IOException
  {
    long l = paramLong - System.currentTimeMillis();
    int i;
    if (l > 0L)
      i = paramSelectionKey.selector().select(l);
    while (i == 0)
    {
      throw new SocketTimeoutException();
      boolean bool = l < 0L;
      i = 0;
      if (!bool)
        i = paramSelectionKey.selector().selectNow();
    }
  }

  protected static void verboseLog(String paramString, byte[] paramArrayOfByte)
  {
    if (Options.check("verbosemsg"))
      System.err.println(hexdump.dump(paramString, paramArrayOfByte));
  }

  void cleanup()
    throws IOException
  {
    this.key.selector().close();
    this.key.channel().close();
  }
}