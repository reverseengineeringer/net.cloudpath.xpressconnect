package jcifs.smb;

import jcifs.util.LogStream;

class SmbTree
{
  private static int tree_conn_counter;
  boolean inDfs;
  boolean inDomainDfs;
  String service = "?????";
  String service0;
  SmbSession session;
  String share;
  private int tid;
  boolean treeConnected;
  int tree_num;

  SmbTree(SmbSession paramSmbSession, String paramString1, String paramString2)
  {
    this.session = paramSmbSession;
    this.share = paramString1.toUpperCase();
    if ((paramString2 != null) && (!paramString2.startsWith("??")))
      this.service = paramString2;
    this.service0 = this.service;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof SmbTree))
    {
      SmbTree localSmbTree = (SmbTree)paramObject;
      return matches(localSmbTree.share, localSmbTree.service);
    }
    return false;
  }

  boolean matches(String paramString1, String paramString2)
  {
    return (this.share.equalsIgnoreCase(paramString1)) && ((paramString2 == null) || (paramString2.startsWith("??")) || (this.service.equalsIgnoreCase(paramString2)));
  }

  void send(ServerMessageBlock paramServerMessageBlock1, ServerMessageBlock paramServerMessageBlock2)
    throws SmbException
  {
    if (paramServerMessageBlock2 != null)
      paramServerMessageBlock2.received = false;
    treeConnect(paramServerMessageBlock1, paramServerMessageBlock2);
    if ((paramServerMessageBlock1 == null) || ((paramServerMessageBlock2 != null) && (paramServerMessageBlock2.received)))
      return;
    if (!this.service.equals("A:"))
      switch (paramServerMessageBlock1.command)
      {
      default:
        throw new SmbException("Invalid operation for " + this.service + " service" + paramServerMessageBlock1);
      case 37:
      case 50:
        switch (0xFF & ((SmbComTransaction)paramServerMessageBlock1).subCommand)
        {
        default:
          throw new SmbException("Invalid operation for " + this.service + " service");
        case 0:
        case 16:
        case 35:
        case 38:
        case 83:
        case 84:
        case 104:
        case 215:
        }
        break;
      case -94:
      case 4:
      case 45:
      case 46:
      case 47:
      case 113:
      }
    paramServerMessageBlock1.tid = this.tid;
    if ((this.inDfs) && (!this.service.equals("IPC")) && (paramServerMessageBlock1.path != null) && (paramServerMessageBlock1.path.length() > 0))
    {
      paramServerMessageBlock1.flags2 = 4096;
      paramServerMessageBlock1.path = ('\\' + this.session.transport().tconHostName + '\\' + this.share + paramServerMessageBlock1.path);
    }
    try
    {
      this.session.send(paramServerMessageBlock1, paramServerMessageBlock2);
      return;
    }
    catch (SmbException localSmbException)
    {
      if (localSmbException.getNtStatus() == -1073741623)
        treeDisconnect(true);
      throw localSmbException;
    }
  }

  public String toString()
  {
    return "SmbTree[share=" + this.share + ",service=" + this.service + ",tid=" + this.tid + ",inDfs=" + this.inDfs + ",inDomainDfs=" + this.inDomainDfs + ",treeConnected=" + this.treeConnected + "]";
  }

  void treeConnect(ServerMessageBlock paramServerMessageBlock1, ServerMessageBlock paramServerMessageBlock2)
    throws SmbException
  {
    SmbTransport localSmbTransport = this.session.transport();
    synchronized (localSmbTransport.setupDiscoLock)
    {
    }
    try
    {
      if (this.treeConnected)
        return;
      this.session.transport.connect();
      String str = "\\\\" + this.session.transport.tconHostName + '\\' + this.share;
      this.service = this.service0;
      if (LogStream.level >= 4)
        SmbTransport.log.println("treeConnect: unc=" + str + ",service=" + this.service);
      SmbComTreeConnectAndXResponse localSmbComTreeConnectAndXResponse = new SmbComTreeConnectAndXResponse(paramServerMessageBlock2);
      SmbComTreeConnectAndX localSmbComTreeConnectAndX = new SmbComTreeConnectAndX(this.session, str, this.service, paramServerMessageBlock1);
      this.session.send(localSmbComTreeConnectAndX, localSmbComTreeConnectAndXResponse);
      this.tid = localSmbComTreeConnectAndXResponse.tid;
      this.service = localSmbComTreeConnectAndXResponse.service;
      this.inDfs = localSmbComTreeConnectAndXResponse.shareIsInDfs;
      this.treeConnected = true;
      int i = tree_conn_counter;
      tree_conn_counter = i + 1;
      this.tree_num = i;
      return;
      localObject2 = finally;
      throw localObject2;
    }
    finally
    {
    }
  }

  void treeDisconnect(boolean paramBoolean)
  {
    synchronized (this.session.transport)
    {
      if ((this.treeConnected) && (!paramBoolean))
      {
        int i = this.tid;
        if (i == 0);
      }
      try
      {
        send(new SmbComTreeDisconnect(), null);
        this.treeConnected = false;
        this.inDfs = false;
        this.inDomainDfs = false;
        return;
      }
      catch (SmbException localSmbException)
      {
        while (true)
          if (LogStream.level > 1)
            localSmbException.printStackTrace(SmbTransport.log);
      }
    }
  }
}