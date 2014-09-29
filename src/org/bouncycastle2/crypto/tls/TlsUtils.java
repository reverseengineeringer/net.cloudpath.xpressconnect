package org.bouncycastle2.crypto.tls;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle2.asn1.x509.TBSCertificateStructure;
import org.bouncycastle2.asn1.x509.X509CertificateStructure;
import org.bouncycastle2.asn1.x509.X509Extension;
import org.bouncycastle2.asn1.x509.X509Extensions;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.digests.MD5Digest;
import org.bouncycastle2.crypto.digests.SHA1Digest;
import org.bouncycastle2.crypto.macs.HMac;
import org.bouncycastle2.crypto.params.KeyParameter;
import org.bouncycastle2.util.Strings;
import org.bouncycastle2.util.io.Streams;

public class TlsUtils
{
  protected static byte[] PRF(byte[] paramArrayOfByte1, String paramString, byte[] paramArrayOfByte2, int paramInt)
  {
    byte[] arrayOfByte1 = Strings.toByteArray(paramString);
    int i = (1 + paramArrayOfByte1.length) / 2;
    byte[] arrayOfByte2 = new byte[i];
    byte[] arrayOfByte3 = new byte[i];
    System.arraycopy(paramArrayOfByte1, 0, arrayOfByte2, 0, i);
    System.arraycopy(paramArrayOfByte1, paramArrayOfByte1.length - i, arrayOfByte3, 0, i);
    byte[] arrayOfByte4 = concat(arrayOfByte1, paramArrayOfByte2);
    byte[] arrayOfByte5 = new byte[paramInt];
    byte[] arrayOfByte6 = new byte[paramInt];
    hmac_hash(new MD5Digest(), arrayOfByte2, arrayOfByte4, arrayOfByte6);
    hmac_hash(new SHA1Digest(), arrayOfByte3, arrayOfByte4, arrayOfByte5);
    for (int j = 0; ; j++)
    {
      if (j >= paramInt)
        return arrayOfByte5;
      arrayOfByte5[j] = ((byte)(arrayOfByte5[j] ^ arrayOfByte6[j]));
    }
  }

  protected static void checkVersion(InputStream paramInputStream, TlsProtocolHandler paramTlsProtocolHandler)
    throws IOException
  {
    int i = paramInputStream.read();
    int j = paramInputStream.read();
    if ((i != 3) || (j != 1))
      throw new TlsFatalAlert((short)70);
  }

  protected static void checkVersion(byte[] paramArrayOfByte, TlsProtocolHandler paramTlsProtocolHandler)
    throws IOException
  {
    if ((paramArrayOfByte[0] != 3) || (paramArrayOfByte[1] != 1))
      throw new TlsFatalAlert((short)70);
  }

  static byte[] concat(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    byte[] arrayOfByte = new byte[paramArrayOfByte1.length + paramArrayOfByte2.length];
    System.arraycopy(paramArrayOfByte1, 0, arrayOfByte, 0, paramArrayOfByte1.length);
    System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, paramArrayOfByte1.length, paramArrayOfByte2.length);
    return arrayOfByte;
  }

  private static void hmac_hash(Digest paramDigest, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
  {
    HMac localHMac = new HMac(paramDigest);
    KeyParameter localKeyParameter = new KeyParameter(paramArrayOfByte1);
    Object localObject = paramArrayOfByte2;
    int i = paramDigest.getDigestSize();
    int j = (-1 + (i + paramArrayOfByte3.length)) / i;
    byte[] arrayOfByte1 = new byte[localHMac.getMacSize()];
    byte[] arrayOfByte2 = new byte[localHMac.getMacSize()];
    for (int k = 0; ; k++)
    {
      if (k >= j)
        return;
      localHMac.init(localKeyParameter);
      localHMac.update((byte[])localObject, 0, localObject.length);
      localHMac.doFinal(arrayOfByte1, 0);
      localObject = arrayOfByte1;
      localHMac.init(localKeyParameter);
      localHMac.update((byte[])localObject, 0, localObject.length);
      localHMac.update(paramArrayOfByte2, 0, paramArrayOfByte2.length);
      localHMac.doFinal(arrayOfByte2, 0);
      System.arraycopy(arrayOfByte2, 0, paramArrayOfByte3, i * k, Math.min(i, paramArrayOfByte3.length - i * k));
    }
  }

  protected static void readFully(byte[] paramArrayOfByte, InputStream paramInputStream)
    throws IOException
  {
    if (Streams.readFully(paramInputStream, paramArrayOfByte) != paramArrayOfByte.length)
      throw new EOFException();
  }

  protected static byte[] readOpaque16(InputStream paramInputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[readUint16(paramInputStream)];
    readFully(arrayOfByte, paramInputStream);
    return arrayOfByte;
  }

  protected static byte[] readOpaque8(InputStream paramInputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[readUint8(paramInputStream)];
    readFully(arrayOfByte, paramInputStream);
    return arrayOfByte;
  }

  protected static int readUint16(InputStream paramInputStream)
    throws IOException
  {
    int i = paramInputStream.read();
    int j = paramInputStream.read();
    if ((i | j) < 0)
      throw new EOFException();
    return j | i << 8;
  }

  protected static int readUint24(InputStream paramInputStream)
    throws IOException
  {
    int i = paramInputStream.read();
    int j = paramInputStream.read();
    int k = paramInputStream.read();
    if ((k | (i | j)) < 0)
      throw new EOFException();
    return k | (i << 16 | j << 8);
  }

  protected static long readUint32(InputStream paramInputStream)
    throws IOException
  {
    int i = paramInputStream.read();
    int j = paramInputStream.read();
    int k = paramInputStream.read();
    int m = paramInputStream.read();
    if ((m | (k | (i | j))) < 0)
      throw new EOFException();
    return i << 24 | j << 16 | k << 8 | m;
  }

  protected static short readUint8(InputStream paramInputStream)
    throws IOException
  {
    int i = paramInputStream.read();
    if (i == -1)
      throw new EOFException();
    return (short)i;
  }

  static void validateKeyUsage(X509CertificateStructure paramX509CertificateStructure, int paramInt)
    throws IOException
  {
    X509Extensions localX509Extensions = paramX509CertificateStructure.getTBSCertificate().getExtensions();
    if (localX509Extensions != null)
    {
      X509Extension localX509Extension = localX509Extensions.getExtension(X509Extension.keyUsage);
      if ((localX509Extension != null) && ((paramInt & (0xFF & org.bouncycastle2.asn1.x509.KeyUsage.getInstance(localX509Extension).getBytes()[0])) != paramInt))
        throw new TlsFatalAlert((short)46);
    }
  }

  protected static void writeGMTUnixTime(byte[] paramArrayOfByte, int paramInt)
  {
    int i = (int)(System.currentTimeMillis() / 1000L);
    paramArrayOfByte[paramInt] = ((byte)(i >> 24));
    paramArrayOfByte[(paramInt + 1)] = ((byte)(i >> 16));
    paramArrayOfByte[(paramInt + 2)] = ((byte)(i >> 8));
    paramArrayOfByte[(paramInt + 3)] = ((byte)i);
  }

  protected static void writeOpaque16(byte[] paramArrayOfByte, OutputStream paramOutputStream)
    throws IOException
  {
    writeUint16(paramArrayOfByte.length, paramOutputStream);
    paramOutputStream.write(paramArrayOfByte);
  }

  protected static void writeOpaque24(byte[] paramArrayOfByte, OutputStream paramOutputStream)
    throws IOException
  {
    writeUint24(paramArrayOfByte.length, paramOutputStream);
    paramOutputStream.write(paramArrayOfByte);
  }

  protected static void writeOpaque8(byte[] paramArrayOfByte, OutputStream paramOutputStream)
    throws IOException
  {
    writeUint8((short)paramArrayOfByte.length, paramOutputStream);
    paramOutputStream.write(paramArrayOfByte);
  }

  protected static void writeUint16(int paramInt, OutputStream paramOutputStream)
    throws IOException
  {
    paramOutputStream.write(paramInt >> 8);
    paramOutputStream.write(paramInt);
  }

  protected static void writeUint16(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    paramArrayOfByte[paramInt2] = ((byte)(paramInt1 >> 8));
    paramArrayOfByte[(paramInt2 + 1)] = ((byte)paramInt1);
  }

  protected static void writeUint16Array(int[] paramArrayOfInt, OutputStream paramOutputStream)
    throws IOException
  {
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfInt.length)
        return;
      writeUint16(paramArrayOfInt[i], paramOutputStream);
    }
  }

  protected static void writeUint24(int paramInt, OutputStream paramOutputStream)
    throws IOException
  {
    paramOutputStream.write(paramInt >> 16);
    paramOutputStream.write(paramInt >> 8);
    paramOutputStream.write(paramInt);
  }

  protected static void writeUint24(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
  {
    paramArrayOfByte[paramInt2] = ((byte)(paramInt1 >> 16));
    paramArrayOfByte[(paramInt2 + 1)] = ((byte)(paramInt1 >> 8));
    paramArrayOfByte[(paramInt2 + 2)] = ((byte)paramInt1);
  }

  protected static void writeUint32(long paramLong, OutputStream paramOutputStream)
    throws IOException
  {
    paramOutputStream.write((int)(paramLong >> 24));
    paramOutputStream.write((int)(paramLong >> 16));
    paramOutputStream.write((int)(paramLong >> 8));
    paramOutputStream.write((int)paramLong);
  }

  protected static void writeUint32(long paramLong, byte[] paramArrayOfByte, int paramInt)
  {
    paramArrayOfByte[paramInt] = ((byte)(int)(paramLong >> 24));
    paramArrayOfByte[(paramInt + 1)] = ((byte)(int)(paramLong >> 16));
    paramArrayOfByte[(paramInt + 2)] = ((byte)(int)(paramLong >> 8));
    paramArrayOfByte[(paramInt + 3)] = ((byte)(int)paramLong);
  }

  protected static void writeUint64(long paramLong, OutputStream paramOutputStream)
    throws IOException
  {
    paramOutputStream.write((int)(paramLong >> 56));
    paramOutputStream.write((int)(paramLong >> 48));
    paramOutputStream.write((int)(paramLong >> 40));
    paramOutputStream.write((int)(paramLong >> 32));
    paramOutputStream.write((int)(paramLong >> 24));
    paramOutputStream.write((int)(paramLong >> 16));
    paramOutputStream.write((int)(paramLong >> 8));
    paramOutputStream.write((int)paramLong);
  }

  protected static void writeUint64(long paramLong, byte[] paramArrayOfByte, int paramInt)
  {
    paramArrayOfByte[paramInt] = ((byte)(int)(paramLong >> 56));
    paramArrayOfByte[(paramInt + 1)] = ((byte)(int)(paramLong >> 48));
    paramArrayOfByte[(paramInt + 2)] = ((byte)(int)(paramLong >> 40));
    paramArrayOfByte[(paramInt + 3)] = ((byte)(int)(paramLong >> 32));
    paramArrayOfByte[(paramInt + 4)] = ((byte)(int)(paramLong >> 24));
    paramArrayOfByte[(paramInt + 5)] = ((byte)(int)(paramLong >> 16));
    paramArrayOfByte[(paramInt + 6)] = ((byte)(int)(paramLong >> 8));
    paramArrayOfByte[(paramInt + 7)] = ((byte)(int)paramLong);
  }

  protected static void writeUint8(short paramShort, OutputStream paramOutputStream)
    throws IOException
  {
    paramOutputStream.write(paramShort);
  }

  protected static void writeUint8(short paramShort, byte[] paramArrayOfByte, int paramInt)
  {
    paramArrayOfByte[paramInt] = ((byte)paramShort);
  }

  protected static void writeUint8Array(short[] paramArrayOfShort, OutputStream paramOutputStream)
    throws IOException
  {
    for (int i = 0; ; i++)
    {
      if (i >= paramArrayOfShort.length)
        return;
      writeUint8(paramArrayOfShort[i], paramOutputStream);
    }
  }

  protected static void writeVersion(OutputStream paramOutputStream)
    throws IOException
  {
    paramOutputStream.write(3);
    paramOutputStream.write(1);
  }

  protected static void writeVersion(byte[] paramArrayOfByte, int paramInt)
    throws IOException
  {
    paramArrayOfByte[paramInt] = 3;
    paramArrayOfByte[(paramInt + 1)] = 1;
  }
}