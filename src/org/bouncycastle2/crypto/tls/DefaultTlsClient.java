package org.bouncycastle2.crypto.tls;

import java.io.IOException;
import java.util.Hashtable;

public abstract class DefaultTlsClient
  implements TlsClient
{
  protected TlsCipherFactory cipherFactory;
  protected TlsClientContext context;
  protected int selectedCipherSuite;
  protected int selectedCompressionMethod;

  public DefaultTlsClient()
  {
    this(new DefaultTlsCipherFactory());
  }

  public DefaultTlsClient(TlsCipherFactory paramTlsCipherFactory)
  {
    this.cipherFactory = paramTlsCipherFactory;
  }

  protected TlsKeyExchange createDHEKeyExchange(int paramInt)
  {
    return new TlsDHEKeyExchange(this.context, paramInt);
  }

  protected TlsKeyExchange createDHKeyExchange(int paramInt)
  {
    return new TlsDHKeyExchange(this.context, paramInt);
  }

  protected TlsKeyExchange createECDHEKeyExchange(int paramInt)
  {
    return new TlsECDHEKeyExchange(this.context, paramInt);
  }

  protected TlsKeyExchange createECDHKeyExchange(int paramInt)
  {
    return new TlsECDHKeyExchange(this.context, paramInt);
  }

  protected TlsKeyExchange createRSAKeyExchange()
  {
    return new TlsRSAKeyExchange(this.context);
  }

  public TlsCipher getCipher()
    throws IOException
  {
    switch (this.selectedCipherSuite)
    {
    default:
      throw new TlsFatalAlert((short)80);
    case 10:
    case 13:
    case 16:
    case 19:
    case 22:
    case 49155:
    case 49160:
    case 49165:
    case 49170:
      return this.cipherFactory.createCipher(this.context, 7, 2);
    case 47:
    case 48:
    case 49:
    case 50:
    case 51:
    case 49156:
    case 49161:
    case 49166:
    case 49171:
      return this.cipherFactory.createCipher(this.context, 8, 2);
    case 53:
    case 54:
    case 55:
    case 56:
    case 57:
    case 49157:
    case 49162:
    case 49167:
    case 49172:
    }
    return this.cipherFactory.createCipher(this.context, 9, 2);
  }

  public int[] getCipherSuites()
  {
    return new int[] { 57, 56, 51, 50, 22, 19, 53, 47, 10 };
  }

  public Hashtable getClientExtensions()
  {
    return null;
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
    case 10:
    case 47:
    case 53:
      return createRSAKeyExchange();
    case 13:
    case 48:
    case 54:
      return createDHKeyExchange(7);
    case 16:
    case 49:
    case 55:
      return createDHKeyExchange(9);
    case 19:
    case 50:
    case 56:
      return createDHEKeyExchange(3);
    case 22:
    case 51:
    case 57:
      return createDHEKeyExchange(5);
    case 49155:
    case 49156:
    case 49157:
      return createECDHKeyExchange(16);
    case 49160:
    case 49161:
    case 49162:
      return createECDHEKeyExchange(17);
    case 49165:
    case 49166:
    case 49167:
      return createECDHKeyExchange(18);
    case 49170:
    case 49171:
    case 49172:
    }
    return createECDHEKeyExchange(19);
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