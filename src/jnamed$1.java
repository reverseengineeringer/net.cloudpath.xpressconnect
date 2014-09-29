import java.net.Socket;

class jnamed$1
  implements Runnable
{
  private final jnamed this$0;
  private final Socket val$s;

  jnamed$1(jnamed paramjnamed, Socket paramSocket)
  {
  }

  public void run()
  {
    this.this$0.TCPclient(this.val$s);
  }
}