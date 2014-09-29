package net.cloudpath.xpressconnect.retention;

import java.util.ArrayList;
import net.cloudpath.xpressconnect.parsers.config.NetworkItemElement;
import net.cloudpath.xpressconnect.thread.PickNetworkThread;
import net.cloudpath.xpressconnect.thread.ThreadCom;

public class PickNetworkRetention
{
  public boolean checksumDone;
  public ArrayList<NetworkItemElement> foundList = null;
  public PickNetworkThread thread;
  public ThreadCom threadcom;
}