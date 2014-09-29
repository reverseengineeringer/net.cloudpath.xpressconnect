package org.bouncycastle2.crypto.tls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle2.asn1.ASN1Object;
import org.bouncycastle2.asn1.x500.X500Name;
import org.bouncycastle2.crypto.prng.ThreadedSeedGenerator;
import org.bouncycastle2.util.Arrays;

public class TlsProtocolHandler
{
  private static final short CS_CERTIFICATE_REQUEST_RECEIVED = 5;
  private static final short CS_CERTIFICATE_VERIFY_SEND = 8;
  private static final short CS_CLIENT_CHANGE_CIPHER_SPEC_SEND = 9;
  private static final short CS_CLIENT_FINISHED_SEND = 10;
  private static final short CS_CLIENT_HELLO_SEND = 1;
  private static final short CS_CLIENT_KEY_EXCHANGE_SEND = 7;
  private static final short CS_DONE = 12;
  private static final short CS_SERVER_CERTIFICATE_RECEIVED = 3;
  private static final short CS_SERVER_CHANGE_CIPHER_SPEC_RECEIVED = 11;
  private static final short CS_SERVER_HELLO_DONE_RECEIVED = 6;
  private static final short CS_SERVER_HELLO_RECEIVED = 2;
  private static final short CS_SERVER_KEY_EXCHANGE_RECEIVED = 4;
  private static final Integer EXT_RenegotiationInfo = new Integer(65281);
  private static final String TLS_ERROR_MESSAGE = "Internal TLS error, this could be an attack";
  private static final byte[] emptybuf = new byte[0];
  private ByteQueue alertQueue = new ByteQueue();
  private boolean appDataReady = false;
  private ByteQueue applicationDataQueue = new ByteQueue();
  private TlsAuthentication authentication = null;
  private CertificateRequest certificateRequest = null;
  private ByteQueue changeCipherSpecQueue = new ByteQueue();
  private Hashtable clientExtensions;
  private boolean closed = false;
  private short connection_state = 0;
  private boolean failedWithError = false;
  private ByteQueue handshakeQueue = new ByteQueue();
  private TlsKeyExchange keyExchange = null;
  private int[] offeredCipherSuites = null;
  private short[] offeredCompressionMethods = null;
  private SecureRandom random;
  private RecordStream rs;
  private SecurityParameters securityParameters = null;
  private TlsClient tlsClient = null;
  private TlsClientContextImpl tlsClientContext = null;
  private TlsInputStream tlsInputStream = null;
  private TlsOutputStream tlsOutputStream = null;

  public TlsProtocolHandler(InputStream paramInputStream, OutputStream paramOutputStream)
  {
    this(paramInputStream, paramOutputStream, createSecureRandom());
  }

  public TlsProtocolHandler(InputStream paramInputStream, OutputStream paramOutputStream, SecureRandom paramSecureRandom)
  {
    this.rs = new RecordStream(this, paramInputStream, paramOutputStream);
    this.random = paramSecureRandom;
  }

  private static boolean arrayContains(int[] paramArrayOfInt, int paramInt)
  {
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfInt.length)
        return false;
      if (paramArrayOfInt[i] == paramInt)
        return true;
    }
  }

  private static boolean arrayContains(short[] paramArrayOfShort, short paramShort)
  {
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfShort.length)
        return false;
      if (paramArrayOfShort[i] == paramShort)
        return true;
    }
  }

  private static byte[] createRenegotiationInfo(byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    TlsUtils.writeOpaque8(paramArrayOfByte, localByteArrayOutputStream);
    return localByteArrayOutputStream.toByteArray();
  }

  private static SecureRandom createSecureRandom()
  {
    ThreadedSeedGenerator localThreadedSeedGenerator = new ThreadedSeedGenerator();
    SecureRandom localSecureRandom = new SecureRandom();
    localSecureRandom.setSeed(localThreadedSeedGenerator.generateSeed(20, true));
    return localSecureRandom;
  }

  private void failWithError(short paramShort1, short paramShort2)
    throws IOException
  {
    if (!this.closed)
    {
      this.closed = true;
      if (paramShort1 == 2)
        this.failedWithError = true;
      sendAlert(paramShort1, paramShort2);
      this.rs.close();
      if (paramShort1 == 2)
        throw new IOException("Internal TLS error, this could be an attack");
    }
    else
    {
      throw new IOException("Internal TLS error, this could be an attack");
    }
  }

  private void processAlert()
    throws IOException
  {
    while (true)
    {
      if (this.alertQueue.size() < 2)
        return;
      byte[] arrayOfByte = new byte[2];
      this.alertQueue.read(arrayOfByte, 0, 2, 0);
      this.alertQueue.removeData(2);
      int i = arrayOfByte[0];
      int j = arrayOfByte[1];
      if (i == 2)
      {
        this.failedWithError = true;
        this.closed = true;
      }
      try
      {
        this.rs.close();
        label65: throw new IOException("Internal TLS error, this could be an attack");
        if (j != 0)
          continue;
        failWithError((short)1, (short)0);
      }
      catch (Exception localException)
      {
        break label65;
      }
    }
  }

  private void processApplicationData()
  {
  }

  private void processChangeCipherSpec()
    throws IOException
  {
    while (true)
    {
      if (this.changeCipherSpecQueue.size() <= 0)
        return;
      byte[] arrayOfByte = new byte[1];
      this.changeCipherSpecQueue.read(arrayOfByte, 0, 1, 0);
      this.changeCipherSpecQueue.removeData(1);
      if (arrayOfByte[0] != 1)
        failWithError((short)2, (short)10);
      if (this.connection_state != 10)
        failWithError((short)2, (short)40);
      this.rs.serverClientSpecReceived();
      this.connection_state = 11;
    }
  }

  private void processHandshake()
    throws IOException
  {
    int j;
    do
    {
      int i = this.handshakeQueue.size();
      j = 0;
      if (i >= 4)
      {
        byte[] arrayOfByte1 = new byte[4];
        this.handshakeQueue.read(arrayOfByte1, 0, 4, 0);
        ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte1);
        short s = TlsUtils.readUint8(localByteArrayInputStream);
        int k = TlsUtils.readUint24(localByteArrayInputStream);
        int m = this.handshakeQueue.size();
        int n = k + 4;
        j = 0;
        if (m >= n)
        {
          byte[] arrayOfByte2 = new byte[k];
          this.handshakeQueue.read(arrayOfByte2, 0, k, 4);
          this.handshakeQueue.removeData(k + 4);
          switch (s)
          {
          default:
            this.rs.updateHandshakeData(arrayOfByte1, 0, 4);
            this.rs.updateHandshakeData(arrayOfByte2, 0, k);
          case 0:
          case 20:
          }
          processHandshakeMessage(s, arrayOfByte2);
          j = 1;
        }
      }
    }
    while (j != 0);
  }

  private void processHandshakeMessage(short paramShort, byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayInputStream localByteArrayInputStream1 = new ByteArrayInputStream(paramArrayOfByte);
    switch (paramShort)
    {
    default:
      failWithError((short)2, (short)10);
    case 11:
    case 20:
    case 2:
    case 14:
    case 12:
    case 13:
    case 0:
    }
    label1175: 
    do
    {
      return;
      switch (this.connection_state)
      {
      default:
        failWithError((short)2, (short)10);
      case 2:
      }
      while (true)
      {
        this.connection_state = 3;
        return;
        Certificate localCertificate2 = Certificate.parse(localByteArrayInputStream1);
        assertEmpty(localByteArrayInputStream1);
        this.keyExchange.processServerCertificate(localCertificate2);
        this.authentication = this.tlsClient.getAuthentication();
        this.authentication.notifyServerCertificate(localCertificate2);
      }
      switch (this.connection_state)
      {
      default:
        failWithError((short)2, (short)10);
        return;
      case 11:
      }
      byte[] arrayOfByte8 = new byte[12];
      TlsUtils.readFully(arrayOfByte8, localByteArrayInputStream1);
      assertEmpty(localByteArrayInputStream1);
      if (!Arrays.constantTimeAreEqual(TlsUtils.PRF(this.securityParameters.masterSecret, "server finished", this.rs.getCurrentHash(), 12), arrayOfByte8))
        failWithError((short)2, (short)40);
      this.connection_state = 12;
      this.appDataReady = true;
      return;
      switch (this.connection_state)
      {
      default:
        failWithError((short)2, (short)10);
        return;
      case 1:
      }
      TlsUtils.checkVersion(localByteArrayInputStream1, this);
      this.securityParameters.serverRandom = new byte[32];
      TlsUtils.readFully(this.securityParameters.serverRandom, localByteArrayInputStream1);
      byte[] arrayOfByte6 = TlsUtils.readOpaque8(localByteArrayInputStream1);
      if (arrayOfByte6.length > 32)
        failWithError((short)2, (short)47);
      this.tlsClient.notifySessionID(arrayOfByte6);
      int k = TlsUtils.readUint16(localByteArrayInputStream1);
      if ((!arrayContains(this.offeredCipherSuites, k)) || (k == 255))
        failWithError((short)2, (short)47);
      this.tlsClient.notifySelectedCipherSuite(k);
      short s = TlsUtils.readUint8(localByteArrayInputStream1);
      if (!arrayContains(this.offeredCompressionMethods, s))
        failWithError((short)2, (short)47);
      this.tlsClient.notifySelectedCompressionMethod(s);
      Hashtable localHashtable = new Hashtable();
      ByteArrayInputStream localByteArrayInputStream3;
      if (localByteArrayInputStream1.available() > 0)
        localByteArrayInputStream3 = new ByteArrayInputStream(TlsUtils.readOpaque16(localByteArrayInputStream1));
      while (true)
      {
        if (localByteArrayInputStream3.available() <= 0)
        {
          assertEmpty(localByteArrayInputStream1);
          boolean bool = localHashtable.containsKey(EXT_RenegotiationInfo);
          if ((bool) && (!Arrays.constantTimeAreEqual((byte[])localHashtable.get(EXT_RenegotiationInfo), createRenegotiationInfo(emptybuf))))
            failWithError((short)2, (short)40);
          this.tlsClient.notifySecureRenegotiation(bool);
          if (this.clientExtensions != null)
            this.tlsClient.processServerExtensions(localHashtable);
          this.keyExchange = this.tlsClient.getKeyExchange();
          this.connection_state = 2;
          return;
        }
        Integer localInteger = new Integer(TlsUtils.readUint16(localByteArrayInputStream3));
        byte[] arrayOfByte7 = TlsUtils.readOpaque16(localByteArrayInputStream3);
        if ((!localInteger.equals(EXT_RenegotiationInfo)) && (this.clientExtensions.get(localInteger) == null))
          failWithError((short)2, (short)110);
        if (localHashtable.containsKey(localInteger))
          failWithError((short)2, (short)47);
        localHashtable.put(localInteger, arrayOfByte7);
      }
      switch (this.connection_state)
      {
      default:
        failWithError((short)2, (short)40);
        return;
      case 3:
        this.keyExchange.skipServerKeyExchange();
      case 4:
      case 5:
      }
      assertEmpty(localByteArrayInputStream1);
      this.connection_state = 6;
      TlsCredentials localTlsCredentials = null;
      if (this.certificateRequest == null)
      {
        this.keyExchange.skipClientCredentials();
        sendClientKeyExchange();
        this.connection_state = 7;
        if ((localTlsCredentials != null) && ((localTlsCredentials instanceof TlsSignerCredentials)))
        {
          sendCertificateVerify(((TlsSignerCredentials)localTlsCredentials).generateCertificateSignature(this.rs.getCurrentHash()));
          this.connection_state = 8;
        }
        byte[] arrayOfByte2 = { 1 };
        this.rs.writeMessage((short)20, arrayOfByte2, 0, arrayOfByte2.length);
        this.connection_state = 9;
        byte[] arrayOfByte3 = this.keyExchange.generatePremasterSecret();
        this.securityParameters.masterSecret = TlsUtils.PRF(arrayOfByte3, "master secret", TlsUtils.concat(this.securityParameters.clientRandom, this.securityParameters.serverRandom), 48);
        Arrays.fill(arrayOfByte3, (byte)0);
        this.rs.clientCipherSpecDecided(this.tlsClient.getCompression(), this.tlsClient.getCipher());
        byte[] arrayOfByte4 = TlsUtils.PRF(this.securityParameters.masterSecret, "client finished", this.rs.getCurrentHash(), 12);
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        TlsUtils.writeUint8((short)20, localByteArrayOutputStream);
        TlsUtils.writeOpaque24(arrayOfByte4, localByteArrayOutputStream);
        byte[] arrayOfByte5 = localByteArrayOutputStream.toByteArray();
        this.rs.writeMessage((short)22, arrayOfByte5, 0, arrayOfByte5.length);
        this.connection_state = 10;
        return;
      }
      localTlsCredentials = this.authentication.getClientCredentials(this.certificateRequest);
      if (localTlsCredentials == null)
        this.keyExchange.skipClientCredentials();
      for (Certificate localCertificate1 = Certificate.EMPTY_CHAIN; ; localCertificate1 = localTlsCredentials.getCertificate())
      {
        sendClientCertificate(localCertificate1);
        break;
        this.keyExchange.processClientCredentials(localTlsCredentials);
      }
      switch (this.connection_state)
      {
      default:
        failWithError((short)2, (short)10);
      case 2:
      case 3:
      }
      while (true)
      {
        this.connection_state = 4;
        return;
        this.keyExchange.skipServerCertificate();
        this.authentication = null;
        this.keyExchange.processServerKeyExchange(localByteArrayInputStream1);
        assertEmpty(localByteArrayInputStream1);
      }
      switch (this.connection_state)
      {
      default:
        failWithError((short)2, (short)10);
        this.connection_state = 5;
        return;
      case 3:
        this.keyExchange.skipServerKeyExchange();
      case 4:
      }
      if (this.authentication == null)
        failWithError((short)2, (short)40);
      int i = TlsUtils.readUint8(localByteArrayInputStream1);
      short[] arrayOfShort = new short[i];
      int j = 0;
      Vector localVector;
      ByteArrayInputStream localByteArrayInputStream2;
      if (j >= i)
      {
        byte[] arrayOfByte1 = TlsUtils.readOpaque16(localByteArrayInputStream1);
        assertEmpty(localByteArrayInputStream1);
        localVector = new Vector();
        localByteArrayInputStream2 = new ByteArrayInputStream(arrayOfByte1);
      }
      while (true)
      {
        if (localByteArrayInputStream2.available() <= 0)
        {
          CertificateRequest localCertificateRequest = new CertificateRequest(arrayOfShort, localVector);
          this.certificateRequest = localCertificateRequest;
          this.keyExchange.validateCertificateRequest(this.certificateRequest);
          break;
          arrayOfShort[j] = TlsUtils.readUint8(localByteArrayInputStream1);
          j++;
          break label1175;
        }
        localVector.addElement(X500Name.getInstance(ASN1Object.fromByteArray(TlsUtils.readOpaque16(localByteArrayInputStream2))));
      }
    }
    while (this.connection_state != 12);
    sendAlert((short)1, (short)100);
  }

  private void safeReadData()
    throws IOException
  {
    try
    {
      this.rs.readData();
      return;
    }
    catch (TlsFatalAlert localTlsFatalAlert)
    {
      if (!this.closed)
        failWithError((short)2, localTlsFatalAlert.getAlertDescription());
      throw localTlsFatalAlert;
    }
    catch (IOException localIOException)
    {
      if (!this.closed)
        failWithError((short)2, (short)80);
      throw localIOException;
    }
    catch (RuntimeException localRuntimeException)
    {
      if (!this.closed)
        failWithError((short)2, (short)80);
      throw localRuntimeException;
    }
  }

  private void safeWriteMessage(short paramShort, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    try
    {
      this.rs.writeMessage(paramShort, paramArrayOfByte, paramInt1, paramInt2);
      return;
    }
    catch (TlsFatalAlert localTlsFatalAlert)
    {
      if (!this.closed)
        failWithError((short)2, localTlsFatalAlert.getAlertDescription());
      throw localTlsFatalAlert;
    }
    catch (IOException localIOException)
    {
      if (!this.closed)
        failWithError((short)2, (short)80);
      throw localIOException;
    }
    catch (RuntimeException localRuntimeException)
    {
      if (!this.closed)
        failWithError((short)2, (short)80);
      throw localRuntimeException;
    }
  }

  private void sendAlert(short paramShort1, short paramShort2)
    throws IOException
  {
    byte[] arrayOfByte = new byte[2];
    arrayOfByte[0] = ((byte)paramShort1);
    arrayOfByte[1] = ((byte)paramShort2);
    this.rs.writeMessage((short)21, arrayOfByte, 0, 2);
  }

  private void sendCertificateVerify(byte[] paramArrayOfByte)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    TlsUtils.writeUint8((short)15, localByteArrayOutputStream);
    TlsUtils.writeUint24(2 + paramArrayOfByte.length, localByteArrayOutputStream);
    TlsUtils.writeOpaque16(paramArrayOfByte, localByteArrayOutputStream);
    byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
    this.rs.writeMessage((short)22, arrayOfByte, 0, arrayOfByte.length);
  }

  private void sendClientCertificate(Certificate paramCertificate)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    TlsUtils.writeUint8((short)11, localByteArrayOutputStream);
    paramCertificate.encode(localByteArrayOutputStream);
    byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
    this.rs.writeMessage((short)22, arrayOfByte, 0, arrayOfByte.length);
  }

  private void sendClientKeyExchange()
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    TlsUtils.writeUint8((short)16, localByteArrayOutputStream);
    this.keyExchange.generateClientKeyExchange(localByteArrayOutputStream);
    byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
    this.rs.writeMessage((short)22, arrayOfByte, 0, arrayOfByte.length);
  }

  private static void writeExtension(OutputStream paramOutputStream, Integer paramInteger, byte[] paramArrayOfByte)
    throws IOException
  {
    TlsUtils.writeUint16(paramInteger.intValue(), paramOutputStream);
    TlsUtils.writeOpaque16(paramArrayOfByte, paramOutputStream);
  }

  protected void assertEmpty(ByteArrayInputStream paramByteArrayInputStream)
    throws IOException
  {
    if (paramByteArrayInputStream.available() > 0)
      throw new TlsFatalAlert((short)50);
  }

  public void close()
    throws IOException
  {
    if (!this.closed)
      failWithError((short)1, (short)0);
  }

  public void connect(CertificateVerifyer paramCertificateVerifyer)
    throws IOException
  {
    connect(new LegacyTlsClient(paramCertificateVerifyer));
  }

  public void connect(TlsClient paramTlsClient)
    throws IOException
  {
    if (paramTlsClient == null)
      throw new IllegalArgumentException("'tlsClient' cannot be null");
    if (this.tlsClient != null)
      throw new IllegalStateException("connect can only be called once");
    this.securityParameters = new SecurityParameters();
    this.securityParameters.clientRandom = new byte[32];
    this.random.nextBytes(this.securityParameters.clientRandom);
    TlsUtils.writeGMTUnixTime(this.securityParameters.clientRandom, 0);
    this.tlsClientContext = new TlsClientContextImpl(this.random, this.securityParameters);
    this.tlsClient = paramTlsClient;
    this.tlsClient.init(this.tlsClientContext);
    ByteArrayOutputStream localByteArrayOutputStream1 = new ByteArrayOutputStream();
    TlsUtils.writeVersion(localByteArrayOutputStream1);
    localByteArrayOutputStream1.write(this.securityParameters.clientRandom);
    TlsUtils.writeUint8((short)0, localByteArrayOutputStream1);
    this.offeredCipherSuites = this.tlsClient.getCipherSuites();
    this.clientExtensions = this.tlsClient.getClientExtensions();
    int i;
    ByteArrayOutputStream localByteArrayOutputStream2;
    Enumeration localEnumeration;
    if ((this.clientExtensions != null) && (this.clientExtensions.get(EXT_RenegotiationInfo) != null))
    {
      i = 0;
      int j = this.offeredCipherSuites.length;
      if (i != 0)
        j++;
      TlsUtils.writeUint16(j * 2, localByteArrayOutputStream1);
      TlsUtils.writeUint16Array(this.offeredCipherSuites, localByteArrayOutputStream1);
      if (i != 0)
        TlsUtils.writeUint16(255, localByteArrayOutputStream1);
      this.offeredCompressionMethods = this.tlsClient.getCompressionMethods();
      TlsUtils.writeUint8((short)this.offeredCompressionMethods.length, localByteArrayOutputStream1);
      TlsUtils.writeUint8Array(this.offeredCompressionMethods, localByteArrayOutputStream1);
      if (this.clientExtensions != null)
      {
        localByteArrayOutputStream2 = new ByteArrayOutputStream();
        localEnumeration = this.clientExtensions.keys();
        label290: if (localEnumeration.hasMoreElements())
          break label405;
        TlsUtils.writeOpaque16(localByteArrayOutputStream2.toByteArray(), localByteArrayOutputStream1);
      }
      ByteArrayOutputStream localByteArrayOutputStream3 = new ByteArrayOutputStream();
      TlsUtils.writeUint8((short)1, localByteArrayOutputStream3);
      TlsUtils.writeUint24(localByteArrayOutputStream1.size(), localByteArrayOutputStream3);
      localByteArrayOutputStream3.write(localByteArrayOutputStream1.toByteArray());
      byte[] arrayOfByte = localByteArrayOutputStream3.toByteArray();
      safeWriteMessage((short)22, arrayOfByte, 0, arrayOfByte.length);
      this.connection_state = 1;
    }
    while (true)
    {
      if (this.connection_state == 12)
      {
        this.tlsInputStream = new TlsInputStream(this);
        this.tlsOutputStream = new TlsOutputStream(this);
        return;
        i = 1;
        break;
        label405: Integer localInteger = (Integer)localEnumeration.nextElement();
        writeExtension(localByteArrayOutputStream2, localInteger, (byte[])this.clientExtensions.get(localInteger));
        break label290;
      }
      safeReadData();
    }
  }

  protected void flush()
    throws IOException
  {
    this.rs.flush();
  }

  public InputStream getInputStream()
  {
    return this.tlsInputStream;
  }

  public OutputStream getOutputStream()
  {
    return this.tlsOutputStream;
  }

  protected void processData(short paramShort, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    switch (paramShort)
    {
    default:
      return;
    case 20:
      this.changeCipherSpecQueue.addData(paramArrayOfByte, paramInt1, paramInt2);
      processChangeCipherSpec();
      return;
    case 21:
      this.alertQueue.addData(paramArrayOfByte, paramInt1, paramInt2);
      processAlert();
      return;
    case 22:
      this.handshakeQueue.addData(paramArrayOfByte, paramInt1, paramInt2);
      processHandshake();
      return;
    case 23:
    }
    if (!this.appDataReady)
      failWithError((short)2, (short)10);
    this.applicationDataQueue.addData(paramArrayOfByte, paramInt1, paramInt2);
    processApplicationData();
  }

  protected int readApplicationData(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    while (true)
    {
      if (this.applicationDataQueue.size() != 0)
      {
        int i = Math.min(paramInt2, this.applicationDataQueue.size());
        this.applicationDataQueue.read(paramArrayOfByte, paramInt1, i, 0);
        this.applicationDataQueue.removeData(i);
        return i;
      }
      if (this.closed)
      {
        if (this.failedWithError)
          throw new IOException("Internal TLS error, this could be an attack");
        return -1;
      }
      safeReadData();
    }
  }

  protected void writeData(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (this.closed)
    {
      if (this.failedWithError)
        throw new IOException("Internal TLS error, this could be an attack");
      throw new IOException("Sorry, connection has been closed, you cannot write more data");
    }
    safeWriteMessage((short)23, emptybuf, 0, 0);
    do
    {
      int i = Math.min(paramInt2, 16384);
      safeWriteMessage((short)23, paramArrayOfByte, paramInt1, i);
      paramInt1 += i;
      paramInt2 -= i;
    }
    while (paramInt2 > 0);
  }
}