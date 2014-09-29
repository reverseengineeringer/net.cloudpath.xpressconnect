package org.bouncycastle2.crypto.tls;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import org.bouncycastle2.util.Arrays;

public abstract class SRPTlsClient
  implements TlsClient
{
  public static final Integer EXT_SRP = new Integer(12);
  protected TlsCipherFactory cipherFactory;
  protected TlsClientContext context;
  protected byte[] identity;
  protected byte[] password;
  protected int selectedCipherSuite;
  protected int selectedCompressionMethod;

  public SRPTlsClient(TlsCipherFactory paramTlsCipherFactory, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    this.cipherFactory = paramTlsCipherFactory;
    this.identity = Arrays.clone(paramArrayOfByte1);
    this.password = Arrays.clone(paramArrayOfByte2);
  }

  public SRPTlsClient(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    this(new DefaultTlsCipherFactory(), paramArrayOfByte1, paramArrayOfByte2);
  }

  protected TlsKeyExchange createSRPKeyExchange(int paramInt)
  {
    return new TlsSRPKeyExchange(this.context, paramInt, this.identity, this.password);
  }

  public TlsCipher getCipher()
    throws IOException
  {
    switch (this.selectedCipherSuite)
    {
    default:
      throw new TlsFatalAlert((short)80);
    case 49178:
    case 49179:
    case 49180:
      return this.cipherFactory.createCipher(this.context, 7, 2);
    case 49181:
    case 49182:
    case 49183:
      return this.cipherFactory.createCipher(this.context, 8, 2);
    case 49184:
    case 49185:
    case 49186:
    }
    return this.cipherFactory.createCipher(this.context, 9, 2);
  }

  public int[] getCipherSuites()
  {
    return new int[] { 49186, 49183, 49180, 49185, 49182, 49179, 49184, 49181, 49178 };
  }

  public Hashtable getClientExtensions()
    throws IOException
  {
    Hashtable localHashtable = new Hashtable();
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    TlsUtils.writeOpaque8(this.identity, localByteArrayOutputStream);
    localHashtable.put(EXT_SRP, localByteArrayOutputStream.toByteArray());
    return localHashtable;
  }

  public TlsCompression getCompression()
    throws IOException
  {
    switch (this.selectedCompressionMethod)
    {
    default:
      throw new TlsFatalAlert((short)80);
    case 0:
    }
    return new TlsNullCompression();
  }

  public short[] getCompressionMethods()
  {
    return new short[1];
  }

  public TlsKeyExchange getKeyExchange()
    throws IOException
  {
    switch (this.selectedCipherSuite)
    {
    default:
      throw new TlsFatalAlert((short)80);
    case 49178:
    case 49181:
    case 49184:
      return createSRPKeyExchange(21);
    case 49179:
    case 49182:
    case 49185:
      return createSRPKeyExchange(23);
    case 49180:
    case 49183:
    case 49186:
    }
    return createSRPKeyExchange(22);
  }

  public void init(TlsClientContext paramTlsClientContext)
  {
    this.context = paramTlsClientContext;
  }

  public void notifySecureRenegotiation(boolean paramBoolean)
    throws IOException
  {
  }

  public void notifySelectedCipherSuite(int paramInt)
  {
    this.selectedCipherSuite = paramInt;
  }

  public void notifySelectedCompressionMethod(short paramShort)
  {
    this.selectedCompressionMethod = paramShort;
  }

  public void notifySessionID(byte[] paramArrayOfByte)
  {
  }

  public void processServerExtensions(Hashtable paramHashtable)
  {
  }
}