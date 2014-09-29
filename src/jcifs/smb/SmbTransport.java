package jcifs.smb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import jcifs.UniAddress;
import jcifs.dcerpc.DcerpcHandle;
import jcifs.dcerpc.msrpc.MsrpcDfsRootEnum;
import jcifs.netbios.Name;
import jcifs.netbios.NbtAddress;
import jcifs.netbios.NbtException;
import jcifs.netbios.SessionRequestPacket;
import jcifs.netbios.SessionServicePacket;
import jcifs.util.Encdec;
import jcifs.util.Hexdump;
import jcifs.util.LogStream;
import jcifs.util.transport.Request;
import jcifs.util.transport.Response;
import jcifs.util.transport.Transport;
import jcifs.util.transport.TransportException;

public class SmbTransport extends Transport
  implements SmbConstants
{
  static final byte[] BUF = new byte[65535];
  static final SmbComNegotiate NEGOTIATE_REQUEST = new SmbComNegotiate();
  static HashMap dfsRoots = null;
  static LogStream log = LogStream.getInstance();
  UniAddress address;
  int capabilities = SmbConstants.CAPABILITIES;
  SigningDigest digest = null;
  int flags2 = SmbConstants.FLAGS2;
  InputStream in;
  SmbComBlankResponse key = new SmbComBlankResponse();
  InetAddress localAddr;
  int localPort;
  int maxMpxCount = SmbConstants.MAX_MPX_COUNT;
  int mid;
  OutputStream out;
  int port;
  int rcv_buf_size = SmbConstants.RCV_BUF_SIZE;
  LinkedList referrals = new LinkedList();
  byte[] sbuf = new byte['ÿ'];
  ServerData server = new ServerData();
  long sessionExpiration = System.currentTimeMillis() + SmbConstants.SO_TIMEOUT;
  int sessionKey = 0;
  LinkedList sessions = new LinkedList();
  int snd_buf_size = SmbConstants.SND_BUF_SIZE;
  Socket socket;
  String tconHostName;
  boolean useUnicode = SmbConstants.USE_UNICODE;

  SmbTransport(UniAddress paramUniAddress, int paramInt1, InetAddress paramInetAddress, int paramInt2)
  {
    this.address = paramUniAddress;
    this.port = paramInt1;
    this.localAddr = paramInetAddress;
    this.localPort = paramInt2;
  }

  static SmbTransport getSmbTransport(UniAddress paramUniAddress, int paramInt)
  {
    try
    {
      SmbTransport localSmbTransport = getSmbTransport(paramUniAddress, paramInt, SmbConstants.LADDR, SmbConstants.LPORT);
      return localSmbTransport;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  static SmbTransport getSmbTransport(UniAddress paramUniAddress, int paramInt1, InetAddress paramInetAddress, int paramInt2)
  {
    try
    {
      synchronized (SmbConstants.CONNECTIONS)
      {
        if (SmbConstants.SSN_LIMIT != 1)
        {
          ListIterator localListIterator = SmbConstants.CONNECTIONS.listIterator();
          while (localListIterator.hasNext())
          {
            SmbTransport localSmbTransport2 = (SmbTransport)localListIterator.next();
            if ((localSmbTransport2.matches(paramUniAddress, paramInt1, paramInetAddress, paramInt2)) && ((SmbConstants.SSN_LIMIT == 0) || (localSmbTransport2.sessions.size() < SmbConstants.SSN_LIMIT)))
            {
              localObject3 = localSmbTransport2;
              return localObject3;
            }
          }
        }
        SmbTransport localSmbTransport1 = new SmbTransport(paramUniAddress, paramInt1, paramInetAddress, paramInt2);
        SmbConstants.CONNECTIONS.add(0, localSmbTransport1);
        Object localObject3 = localSmbTransport1;
      }
    }
    finally
    {
    }
  }

  private void negotiate(int paramInt, ServerMessageBlock paramServerMessageBlock)
    throws IOException
  {
    byte[] arrayOfByte = this.sbuf;
    int j;
    if (paramInt == 139)
    {
      try
      {
        ssn139();
        int i = 1 + this.mid;
        this.mid = i;
        if (i == 32000)
          this.mid = 1;
        NEGOTIATE_REQUEST.mid = this.mid;
        j = NEGOTIATE_REQUEST.encode(this.sbuf, 4);
        Encdec.enc_uint32be(j & 0xFFFF, this.sbuf, 0);
        if (LogStream.level >= 4)
        {
          log.println(NEGOTIATE_REQUEST);
          if (LogStream.level >= 6)
            Hexdump.hexdump(log, this.sbuf, 4, j);
        }
        this.out.write(this.sbuf, 0, j + 4);
        this.out.flush();
        if (peekKey() != null)
          break label273;
        throw new IOException("transport closed in negotiate");
      }
      finally
      {
      }
    }
    else
    {
      if (paramInt == 0)
        paramInt = 445;
      if (this.localAddr == null);
      for (this.socket = new Socket(this.address.getHostAddress(), paramInt); ; this.socket = new Socket(this.address.getHostAddress(), paramInt, this.localAddr, this.localPort))
      {
        this.socket.setSoTimeout(SmbConstants.SO_TIMEOUT);
        this.out = this.socket.getOutputStream();
        this.in = this.socket.getInputStream();
        break;
      }
    }
    label273: int k = 0xFFFF & Encdec.dec_uint16be(this.sbuf, 2);
    if ((k < 33) || (k + 4 > this.sbuf.length))
      throw new IOException("Invalid payload size: " + k);
    readn(this.in, this.sbuf, 36, k - 32);
    paramServerMessageBlock.decode(this.sbuf, 4);
    if (LogStream.level >= 4)
    {
      log.println(paramServerMessageBlock);
      if (LogStream.level >= 6)
        Hexdump.hexdump(log, this.sbuf, 4, j);
    }
  }

  void checkStatus(ServerMessageBlock paramServerMessageBlock1, ServerMessageBlock paramServerMessageBlock2)
    throws SmbException
  {
    paramServerMessageBlock2.errorCode = SmbException.getStatusByCode(paramServerMessageBlock2.errorCode);
    switch (paramServerMessageBlock2.errorCode)
    {
    default:
      throw new SmbException(paramServerMessageBlock2.errorCode, null);
    case -1073741790:
    case -1073741718:
    case -1073741715:
    case -1073741714:
    case -1073741713:
    case -1073741712:
    case -1073741711:
    case -1073741710:
    case -1073741428:
    case -1073741260:
      throw new SmbAuthException(paramServerMessageBlock2.errorCode);
    case -1073741225:
      if (paramServerMessageBlock1.auth == null)
        throw new SmbException(paramServerMessageBlock2.errorCode, null);
      DfsReferral[] arrayOfDfsReferral = getDfsReferrals(paramServerMessageBlock1.auth, paramServerMessageBlock1.path, 1);
      SmbFile.dfs.insert(paramServerMessageBlock1.path, arrayOfDfsReferral[0]);
      throw arrayOfDfsReferral[0];
    case -2147483643:
    case 0:
    }
    if (paramServerMessageBlock2.verifyFailed)
      throw new SmbException("Signature verification failed.");
  }

  public void connect()
    throws SmbException
  {
    try
    {
      super.connect(SmbConstants.RESPONSE_TIMEOUT);
      return;
    }
    catch (TransportException localTransportException)
    {
      throw new SmbException(localTransportException.getMessage(), localTransportException);
    }
  }

  void dfsPathSplit(String paramString, String[] paramArrayOfString)
  {
    int i = -1 + paramArrayOfString.length;
    int j = 0;
    int k = paramString.length();
    int m = 0;
    int n = 0;
    if (n == i)
    {
      paramArrayOfString[i] = paramString.substring(j);
      label41: return;
    }
    int i1;
    if ((m == k) || (paramString.charAt(m) == '\\'))
    {
      i1 = n + 1;
      paramArrayOfString[n] = paramString.substring(j, m);
      j = m + 1;
    }
    while (true)
    {
      int i2 = m + 1;
      if (m >= k)
      {
        while (i1 < paramArrayOfString.length)
        {
          int i3 = i1 + 1;
          paramArrayOfString[i1] = "";
          i1 = i3;
        }
        break label41;
      }
      m = i2;
      n = i1;
      break;
      i1 = n;
    }
  }

  protected void doConnect()
    throws IOException
  {
    int i = 445;
    SmbComNegotiateResponse localSmbComNegotiateResponse = new SmbComNegotiateResponse(this.server);
    try
    {
      negotiate(this.port, localSmbComNegotiateResponse);
      if (localSmbComNegotiateResponse.dialectIndex > 10)
        throw new SmbException("This client does not support the negotiated dialect.");
    }
    catch (ConnectException localConnectException)
    {
      while (true)
      {
        if ((this.port == 0) || (this.port == i))
          i = 139;
        this.port = i;
        negotiate(this.port, localSmbComNegotiateResponse);
      }
    }
    catch (NoRouteToHostException localNoRouteToHostException)
    {
      while (true)
      {
        if ((this.port == 0) || (this.port == i))
          i = 139;
        this.port = i;
        negotiate(this.port, localSmbComNegotiateResponse);
      }
      if ((this.server.encryptionKeyLength != 8) && (SmbConstants.LM_COMPATIBILITY == 0))
        throw new SmbException("Encryption key length is not 8 as expected. This could indicate that the server requires NTLMv2. JCIFS does not fully support NTLMv2 but you can try setting jcifs.smb.lmCompatibility = 3.");
      this.tconHostName = this.address.getHostName();
      if ((this.server.signaturesRequired) || ((this.server.signaturesEnabled) && (SmbConstants.SIGNPREF)));
      for (this.flags2 = (0x4 | this.flags2); ; this.flags2 = (0xFFFB & this.flags2))
      {
        this.maxMpxCount = Math.min(this.maxMpxCount, this.server.maxMpxCount);
        if (this.maxMpxCount < 1)
          this.maxMpxCount = 1;
        this.snd_buf_size = Math.min(this.snd_buf_size, this.server.maxBufferSize);
        this.capabilities &= this.server.capabilities;
        if ((0x4 & this.capabilities) == 0)
        {
          if (!SmbConstants.FORCE_UNICODE)
            break;
          this.capabilities = (0x4 | this.capabilities);
        }
        return;
      }
      this.useUnicode = false;
      this.flags2 = (0x7FFF & this.flags2);
    }
  }

  protected void doDisconnect(boolean paramBoolean)
    throws IOException
  {
    ListIterator localListIterator = this.sessions.listIterator();
    while (localListIterator.hasNext())
      ((SmbSession)localListIterator.next()).logoff(paramBoolean);
    this.socket.shutdownOutput();
    this.out.close();
    this.in.close();
    this.socket.close();
    this.digest = null;
  }

  protected void doRecv(Response paramResponse)
    throws IOException
  {
    ServerMessageBlock localServerMessageBlock = (ServerMessageBlock)paramResponse;
    localServerMessageBlock.useUnicode = this.useUnicode;
    int i;
    synchronized (BUF)
    {
      System.arraycopy(this.sbuf, 0, BUF, 0, 36);
      i = 0xFFFF & Encdec.dec_uint16be(BUF, 2);
      if ((i < 33) || (i + 4 > this.rcv_buf_size))
        throw new IOException("Invalid payload size: " + i);
    }
    int j = 0xFFFFFFFF & Encdec.dec_uint32le(BUF, 9);
    if ((localServerMessageBlock.command == 46) && ((j == 0) || (j == -2147483643)))
    {
      SmbComReadAndXResponse localSmbComReadAndXResponse = (SmbComReadAndXResponse)localServerMessageBlock;
      readn(this.in, BUF, 36, 27);
      (32 + 27);
      localServerMessageBlock.decode(BUF, 4);
      if (localSmbComReadAndXResponse.dataLength > 0)
      {
        readn(this.in, BUF, 63, -59 + localSmbComReadAndXResponse.dataOffset);
        readn(this.in, localSmbComReadAndXResponse.b, localSmbComReadAndXResponse.off, localSmbComReadAndXResponse.dataLength);
      }
    }
    while (true)
    {
      if ((this.digest != null) && (localServerMessageBlock.errorCode == 0))
        this.digest.verify(BUF, 4, localServerMessageBlock);
      if (LogStream.level >= 4)
      {
        log.println(paramResponse);
        if (LogStream.level >= 6)
          Hexdump.hexdump(log, BUF, 4, i);
      }
      return;
      readn(this.in, BUF, 36, i - 32);
      localServerMessageBlock.decode(BUF, 4);
      if ((localServerMessageBlock instanceof SmbComTransactionResponse))
        ((SmbComTransactionResponse)localServerMessageBlock).nextElement();
    }
  }

  protected void doSend(Request paramRequest)
    throws IOException
  {
    synchronized (BUF)
    {
      ServerMessageBlock localServerMessageBlock = (ServerMessageBlock)paramRequest;
      int i = localServerMessageBlock.encode(BUF, 4);
      Encdec.enc_uint32be(0xFFFF & i, BUF, 0);
      if (LogStream.level >= 4)
      {
        do
        {
          log.println(localServerMessageBlock);
          if (!(localServerMessageBlock instanceof AndXServerMessageBlock))
            break;
          localServerMessageBlock = ((AndXServerMessageBlock)localServerMessageBlock).andx;
        }
        while (localServerMessageBlock != null);
        if (LogStream.level >= 6)
          Hexdump.hexdump(log, BUF, 4, i);
      }
      this.out.write(BUF, 0, i + 4);
      return;
    }
  }

  protected void doSend0(Request paramRequest)
    throws IOException
  {
    try
    {
      doSend(paramRequest);
      return;
    }
    catch (IOException localIOException1)
    {
      if (LogStream.level > 2)
        localIOException1.printStackTrace(log);
    }
    try
    {
      disconnect(true);
      throw localIOException1;
    }
    catch (IOException localIOException2)
    {
      while (true)
        localIOException2.printStackTrace(log);
    }
  }

  protected void doSkip()
    throws IOException
  {
    int i = 0xFFFF & Encdec.dec_uint16be(this.sbuf, 2);
    if ((i < 33) || (i + 4 > this.rcv_buf_size))
    {
      this.in.skip(this.in.available());
      return;
    }
    this.in.skip(i - 32);
  }

  DfsReferral[] getDfsReferrals(NtlmPasswordAuthentication paramNtlmPasswordAuthentication, String paramString, int paramInt)
    throws SmbException
  {
    SmbTree localSmbTree = getSmbSession(paramNtlmPasswordAuthentication).getSmbTree("IPC$", null);
    Trans2GetDfsReferralResponse localTrans2GetDfsReferralResponse = new Trans2GetDfsReferralResponse();
    localSmbTree.send(new Trans2GetDfsReferral(paramString), localTrans2GetDfsReferralResponse);
    if ((paramInt == 0) || (localTrans2GetDfsReferralResponse.numReferrals < paramInt))
      paramInt = localTrans2GetDfsReferralResponse.numReferrals;
    DfsReferral[] arrayOfDfsReferral = new DfsReferral[paramInt];
    String[] arrayOfString = new String[4];
    long l = System.currentTimeMillis() + 1000L * Dfs.TTL;
    int i = 0;
    if (i < arrayOfDfsReferral.length)
    {
      DfsReferral localDfsReferral = new DfsReferral();
      localDfsReferral.resolveHashes = paramNtlmPasswordAuthentication.hashesExternal;
      localDfsReferral.ttl = localTrans2GetDfsReferralResponse.referrals[i].ttl;
      localDfsReferral.expiration = l;
      if (paramString.equals(""))
        localDfsReferral.server = localTrans2GetDfsReferralResponse.referrals[i].path.substring(1).toLowerCase();
      while (true)
      {
        localDfsReferral.pathConsumed = localTrans2GetDfsReferralResponse.pathConsumed;
        arrayOfDfsReferral[i] = localDfsReferral;
        i++;
        break;
        dfsPathSplit(localTrans2GetDfsReferralResponse.referrals[i].node, arrayOfString);
        localDfsReferral.server = arrayOfString[1];
        localDfsReferral.share = arrayOfString[2];
        localDfsReferral.path = arrayOfString[3];
      }
    }
    return arrayOfDfsReferral;
  }

  FileEntry[] getDfsRoots(String paramString, NtlmPasswordAuthentication paramNtlmPasswordAuthentication)
    throws IOException
  {
    DfsReferral[] arrayOfDfsReferral = getSmbTransport(UniAddress.getByName(paramString), 0).getDfsReferrals(paramNtlmPasswordAuthentication, "\\" + paramString, 1);
    DcerpcHandle localDcerpcHandle = DcerpcHandle.getHandle("ncacn_np:" + UniAddress.getByName(arrayOfDfsReferral[0].server).getHostAddress() + "[\\PIPE\\netdfs]", paramNtlmPasswordAuthentication);
    try
    {
      localMsrpcDfsRootEnum = new MsrpcDfsRootEnum(paramString);
      localDcerpcHandle.sendrecv(localMsrpcDfsRootEnum);
      if (localMsrpcDfsRootEnum.retval != 0)
        throw new SmbException(localMsrpcDfsRootEnum.retval, true);
    }
    finally
    {
      FileEntry[] arrayOfFileEntry;
      try
      {
        MsrpcDfsRootEnum localMsrpcDfsRootEnum;
        localDcerpcHandle.close();
        throw localObject;
        arrayOfFileEntry = localMsrpcDfsRootEnum.getEntries();
      }
      catch (IOException localIOException1)
      {
        try
        {
          do
          {
            localDcerpcHandle.close();
            return arrayOfFileEntry;
            localIOException1 = localIOException1;
          }
          while (LogStream.level < 4);
          localIOException1.printStackTrace(log);
        }
        catch (IOException localIOException2)
        {
          while (LogStream.level < 4);
          localIOException2.printStackTrace(log);
        }
      }
      return arrayOfFileEntry;
    }
  }

  SmbSession getSmbSession()
  {
    try
    {
      SmbSession localSmbSession = getSmbSession(new NtlmPasswordAuthentication(null, null, null));
      return localSmbSession;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }

  SmbSession getSmbSession(NtlmPasswordAuthentication paramNtlmPasswordAuthentication)
  {
    while (true)
    {
      try
      {
        ListIterator localListIterator1 = this.sessions.listIterator();
        Object localObject2;
        if (localListIterator1.hasNext())
        {
          SmbSession localSmbSession3 = (SmbSession)localListIterator1.next();
          if (!localSmbSession3.matches(paramNtlmPasswordAuthentication))
            continue;
          localSmbSession3.auth = paramNtlmPasswordAuthentication;
          localObject2 = localSmbSession3;
          return localObject2;
        }
        if (SmbConstants.SO_TIMEOUT > 0)
        {
          long l1 = this.sessionExpiration;
          long l2 = System.currentTimeMillis();
          SmbSession localSmbSession1;
          if (l1 < l2)
          {
            this.sessionExpiration = (l2 + SmbConstants.SO_TIMEOUT);
            ListIterator localListIterator2 = this.sessions.listIterator();
            if (localListIterator2.hasNext())
            {
              SmbSession localSmbSession2 = (SmbSession)localListIterator2.next();
              if (localSmbSession2.expiration >= l2)
                continue;
              localSmbSession2.logoff(false);
              continue;
            }
          }
        }
      }
      finally
      {
      }
      localSmbSession1 = new SmbSession(this.address, this.port, this.localAddr, this.localPort, paramNtlmPasswordAuthentication);
      localSmbSession1.transport = this;
      this.sessions.add(localSmbSession1);
      localObject2 = localSmbSession1;
    }
  }

  boolean hasCapability(int paramInt)
    throws SmbException
  {
    try
    {
      connect(SmbConstants.RESPONSE_TIMEOUT);
      if ((paramInt & this.capabilities) == paramInt)
        return true;
    }
    catch (IOException localIOException)
    {
      throw new SmbException(localIOException.getMessage(), localIOException);
    }
    return false;
  }

  boolean isSignatureSetupRequired(NtlmPasswordAuthentication paramNtlmPasswordAuthentication)
  {
    return ((0x4 & this.flags2) != 0) && (this.digest == null) && (paramNtlmPasswordAuthentication != NtlmPasswordAuthentication.NULL) && (!NtlmPasswordAuthentication.NULL.equals(paramNtlmPasswordAuthentication));
  }

  protected void makeKey(Request paramRequest)
    throws IOException
  {
    int i = 1 + this.mid;
    this.mid = i;
    if (i == 32000)
      this.mid = 1;
    ((ServerMessageBlock)paramRequest).mid = this.mid;
  }

  boolean matches(UniAddress paramUniAddress, int paramInt1, InetAddress paramInetAddress, int paramInt2)
  {
    return (paramUniAddress.equals(this.address)) && ((paramInt1 == 0) || (paramInt1 == this.port) || ((paramInt1 == 445) && (this.port == 139))) && ((paramInetAddress == this.localAddr) || ((paramInetAddress != null) && (paramInetAddress.equals(this.localAddr)))) && (paramInt2 == this.localPort);
  }

  protected Request peekKey()
    throws IOException
  {
    if (readn(this.in, this.sbuf, 0, 4) < 4);
    do
    {
      return null;
      if (this.sbuf[0] == -123)
        break;
    }
    while (readn(this.in, this.sbuf, 4, 32) < 32);
    if (LogStream.level >= 4)
    {
      log.println("New data read: " + this);
      Hexdump.hexdump(log, this.sbuf, 4, 32);
    }
    while (true)
    {
      if ((this.sbuf[0] == 0) && (this.sbuf[1] == 0) && (this.sbuf[4] == -1) && (this.sbuf[5] == 83) && (this.sbuf[6] == 77) && (this.sbuf[7] == 66))
      {
        this.key.mid = (0xFFFF & Encdec.dec_uint16le(this.sbuf, 34));
        return this.key;
      }
      for (int i = 0; i < 35; i++)
        this.sbuf[i] = this.sbuf[(i + 1)];
      int j = this.in.read();
      if (j == -1)
        break;
      this.sbuf[35] = ((byte)j);
    }
  }

  void send(ServerMessageBlock paramServerMessageBlock1, ServerMessageBlock paramServerMessageBlock2)
    throws SmbException
  {
    connect();
    paramServerMessageBlock1.flags2 |= this.flags2;
    paramServerMessageBlock1.useUnicode = this.useUnicode;
    paramServerMessageBlock1.response = paramServerMessageBlock2;
    if (paramServerMessageBlock1.digest == null)
      paramServerMessageBlock1.digest = this.digest;
    if (paramServerMessageBlock2 == null);
    try
    {
      doSend0(paramServerMessageBlock1);
      return;
      if ((paramServerMessageBlock1 instanceof SmbComTransaction))
      {
        paramServerMessageBlock2.command = paramServerMessageBlock1.command;
        localSmbComTransaction = (SmbComTransaction)paramServerMessageBlock1;
        localSmbComTransactionResponse = (SmbComTransactionResponse)paramServerMessageBlock2;
        localSmbComTransaction.maxBufferSize = this.snd_buf_size;
        localSmbComTransactionResponse.reset();
        try
        {
          BufferCache.getBuffers(localSmbComTransaction, localSmbComTransactionResponse);
          localSmbComTransaction.nextElement();
          if (localSmbComTransaction.hasMoreElements())
          {
            SmbComBlankResponse localSmbComBlankResponse = new SmbComBlankResponse();
            super.sendrecv(localSmbComTransaction, localSmbComBlankResponse, SmbConstants.RESPONSE_TIMEOUT);
            if (localSmbComBlankResponse.errorCode != 0)
              checkStatus(localSmbComTransaction, localSmbComBlankResponse);
            localSmbComTransaction.nextElement();
            synchronized (this.response_map)
            {
              paramServerMessageBlock2.received = false;
              localSmbComTransactionResponse.isReceived = false;
              try
              {
                this.response_map.put(localSmbComTransaction, localSmbComTransactionResponse);
                do
                  doSend0(localSmbComTransaction);
                while ((localSmbComTransaction.hasMoreElements()) && (localSmbComTransaction.nextElement() != null));
                long l = SmbConstants.RESPONSE_TIMEOUT;
                localSmbComTransactionResponse.expiration = (l + System.currentTimeMillis());
                do
                {
                  if (!localSmbComTransactionResponse.hasMoreElements())
                    break;
                  this.response_map.wait(l);
                  l = localSmbComTransactionResponse.expiration - System.currentTimeMillis();
                }
                while (l > 0L);
                throw new TransportException(this + " timedout waiting for response to " + localSmbComTransaction);
              }
              catch (InterruptedException localInterruptedException2)
              {
                throw new TransportException(localInterruptedException2);
              }
              finally
              {
                this.response_map.remove(localSmbComTransaction);
              }
            }
          }
        }
        finally
        {
          BufferCache.releaseBuffer(localSmbComTransaction.txn_buf);
          BufferCache.releaseBuffer(localSmbComTransactionResponse.txn_buf);
        }
      }
    }
    catch (SmbException localSmbException)
    {
      SmbComTransaction localSmbComTransaction;
      SmbComTransactionResponse localSmbComTransactionResponse;
      while (true)
      {
        throw localSmbException;
        makeKey(localSmbComTransaction);
      }
      if (paramServerMessageBlock2.errorCode != 0)
        checkStatus(localSmbComTransaction, localSmbComTransactionResponse);
      this.response_map.remove(localSmbComTransaction);
      BufferCache.releaseBuffer(localSmbComTransaction.txn_buf);
      BufferCache.releaseBuffer(localSmbComTransactionResponse.txn_buf);
      while (true)
      {
        checkStatus(paramServerMessageBlock1, paramServerMessageBlock2);
        return;
        paramServerMessageBlock2.command = paramServerMessageBlock1.command;
        super.sendrecv(paramServerMessageBlock1, paramServerMessageBlock2, SmbConstants.RESPONSE_TIMEOUT);
      }
    }
    catch (InterruptedException localInterruptedException1)
    {
      throw new SmbException(localInterruptedException1.getMessage(), localInterruptedException1);
    }
    catch (IOException localIOException)
    {
      throw new SmbException(localIOException.getMessage(), localIOException);
    }
  }

  void ssn139()
    throws IOException
  {
    Name localName = new Name(this.address.firstCalledName(), 32, null);
    while (true)
    {
      if (this.localAddr == null)
      {
        this.socket = new Socket(this.address.getHostAddress(), 139);
        label46: this.socket.setSoTimeout(SmbConstants.SO_TIMEOUT);
        this.out = this.socket.getOutputStream();
        this.in = this.socket.getInputStream();
        SessionRequestPacket localSessionRequestPacket = new SessionRequestPacket(localName, NbtAddress.getLocalName());
        this.out.write(this.sbuf, 0, localSessionRequestPacket.writeWireFormat(this.sbuf, 0));
        if (readn(this.in, this.sbuf, 0, 4) >= 4)
          break label178;
      }
      try
      {
        this.socket.close();
        label135: throw new SmbException("EOF during NetBIOS session request");
        this.socket = new Socket(this.address.getHostAddress(), 139, this.localAddr, this.localPort);
        break label46;
        label178: switch (0xFF & this.sbuf[0])
        {
        default:
          disconnect(true);
          throw new NbtException(2, 0);
        case 130:
          if (LogStream.level >= 4)
            log.println("session established ok with " + this.address);
          return;
        case 131:
          int i = 0xFF & this.in.read();
          switch (i)
          {
          case 129:
          default:
            disconnect(true);
            throw new NbtException(2, i);
          case 128:
          case 130:
          }
          this.socket.close();
          String str = this.address.nextCalledName();
          localName.name = str;
          if (str != null)
            continue;
          throw new IOException("Failed to establish session with " + this.address);
        case -1:
          disconnect(true);
          throw new NbtException(2, -1);
        }
      }
      catch (IOException localIOException)
      {
        break label135;
      }
    }
  }

  public String toString()
  {
    return super.toString() + "[" + this.address + ":" + this.port + "]";
  }

  class ServerData
  {
    int capabilities;
    boolean encryptedPasswords;
    byte[] encryptionKey;
    int encryptionKeyLength;
    byte flags;
    int flags2;
    int maxBufferSize;
    int maxMpxCount;
    int maxNumberVcs;
    int maxRawSize;
    String oemDomainName;
    int security;
    int securityMode;
    long serverTime;
    int serverTimeZone;
    int sessionKey;
    boolean signaturesEnabled;
    boolean signaturesRequired;

    ServerData()
    {
    }
  }
}