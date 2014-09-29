import java.net.InetAddress;

class jnamed$3
  implements Runnable
{
  private final jnamed this$0;
  private final InetAddress val$addr;
  private final int val$port;

  jnamed$3(jnamed paramjnamed, InetAddress paramInetAddress, int paramInt)
  {
  }

  public void run()
  {
    this.this$0.serveUDP(this.val$addr, this.val$port);
  }
}