package org.bouncycastle2.crypto.tls;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class RecordStream
{
  private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
  private TlsProtocolHandler handler;
  private CombinedHash hash;
  private InputStream is;
  private OutputStream os;
  private TlsCipher readCipher = null;
  private TlsCompression readCompression = null;
  private TlsCipher writeCipher = null;
  private TlsCompression writeCompression = null;

  RecordStream(TlsProtocolHandler paramTlsProtocolHandler, InputStream paramInputStream, OutputStream paramOutputStream)
  {
    this.handler = paramTlsProtocolHandler;
    this.is = paramInputStream;
    this.os = paramOutputStream;
    this.hash = new CombinedHash();
    this.readCompression = new TlsNullCompression();
    this.writeCompression = this.readCompression;
    this.readCipher = new TlsNullCipher();
    this.writeCipher = this.readCipher;
  }

  private static byte[] doFinal(CombinedHash paramCombinedHash)
  {
    byte[] arrayOfByte = new byte[paramCombinedHash.getDigestSize()];
    paramCombinedHash.doFinal(arrayOfByte, 0);
    return arrayOfByte;
  }

  private byte[] getBufferContents()
  {
    byte[] arrayOfByte = this.buffer.toByteArray();
    this.buffer.reset();
    return arrayOfByte;
  }

  void clientCipherSpecDecided(TlsCompression paramTlsCompression, TlsCipher paramTlsCipher)
  {
    this.writeCompression = paramTlsCompression;
    this.writeCipher = paramTlsCipher;
  }

  protected void close()
    throws IOException
  {
    Object localObject = null;
    try
    {
      this.is.close();
    }
    catch (IOException localIOException1)
    {
      try
      {
        while (true)
        {
          this.os.close();
          if (localObject == null)
            break;
          throw localObject;
          localIOException1 = localIOException1;
          localObject = localIOException1;
        }
      }
      catch (IOException localIOException2)
      {
        while (true)
          localObject = localIOException2;
      }
    }
  }

  protected byte[] decodeAndVerify(short paramShort, InputStream paramInputStream, int paramInt)
    throws IOException
  {
    byte[] arrayOfByte1 = new byte[paramInt];
    TlsUtils.readFully(arrayOfByte1, paramInputStream);
    byte[] arrayOfByte2 = this.readCipher.decodeCiphertext(paramShort, arrayOfByte1, 0, arrayOfByte1.length);
    OutputStream localOutputStream = this.readCompression.decompress(this.buffer);
    if (localOutputStream == this.buffer)
      return arrayOfByte2;
    localOutputStream.write(arrayOfByte2, 0, arrayOfByte2.length);
    localOutputStream.flush();
    return getBufferContents();
  }

  protected void flush()
    throws IOException
  {
    this.os.flush();
  }

  byte[] getCurrentHash()
  {
    return doFinal(new CombinedHash(this.hash));
  }

  public void readData()
    throws IOException
  {
    short s = TlsUtils.readUint8(this.is);
    TlsUtils.checkVersion(this.is, this.handler);
    int i = TlsUtils.readUint16(this.is);
    byte[] arrayOfByte = decodeAndVerify(s, this.is, i);
    this.handler.processData(s, arrayOfByte, 0, arrayOfByte.length);
  }

  void serverClientSpecReceived()
  {
    this.readCompression = this.writeCompression;
    this.readCipher = this.writeCipher;
  }

  void updateHandshakeData(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.hash.update(paramArrayOfByte, paramInt1, paramInt2);
  }

  protected void writeMessage(short paramShort, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramShort == 22)
      updateHandshakeData(paramArrayOfByte, paramInt1, paramInt2);
    OutputStream localOutputStream = this.writeCompression.compress(this.buffer);
    if (localOutputStream == this.buffer);
    byte[] arrayOfByte1;
    for (byte[] arrayOfByte2 = this.writeCipher.encodePlaintext(paramShort, paramArrayOfByte, paramInt1, paramInt2); ; arrayOfByte2 = this.writeCipher.encodePlaintext(paramShort, arrayOfByte1, 0, arrayOfByte1.length))
    {
      byte[] arrayOfByte3 = new byte[5 + arrayOfByte2.length];
      TlsUtils.writeUint8(paramShort, arrayOfByte3, 0);
      TlsUtils.writeVersion(arrayOfByte3, 1);
      TlsUtils.writeUint16(arrayOfByte2.length, arrayOfByte3, 3);
      System.arraycopy(arrayOfByte2, 0, arrayOfByte3, 5, arrayOfByte2.length);
      this.os.write(arrayOfByte3);
      this.os.flush();
      return;
      localOutputStream.write(paramArrayOfByte, paramInt1, paramInt2);
      localOutputStream.flush();
      arrayOfByte1 = getBufferContents();
    }
  }
}