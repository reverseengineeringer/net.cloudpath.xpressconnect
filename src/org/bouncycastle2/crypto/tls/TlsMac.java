package org.bouncycastle2.crypto.tls;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.bouncycastle2.crypto.Digest;
import org.bouncycastle2.crypto.macs.HMac;
import org.bouncycastle2.crypto.params.KeyParameter;

public class TlsMac
{
  protected HMac mac;
  protected long seqNo;

  public TlsMac(Digest paramDigest, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.mac = new HMac(paramDigest);
    KeyParameter localKeyParameter = new KeyParameter(paramArrayOfByte, paramInt1, paramInt2);
    this.mac.init(localKeyParameter);
    this.seqNo = 0L;
  }

  public byte[] calculateMac(short paramShort, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(13);
    try
    {
      long l = this.seqNo;
      this.seqNo = (1L + l);
      TlsUtils.writeUint64(l, localByteArrayOutputStream);
      TlsUtils.writeUint8(paramShort, localByteArrayOutputStream);
      TlsUtils.writeVersion(localByteArrayOutputStream);
      TlsUtils.writeUint16(paramInt2, localByteArrayOutputStream);
      byte[] arrayOfByte1 = localByteArrayOutputStream.toByteArray();
      this.mac.update(arrayOfByte1, 0, arrayOfByte1.length);
      this.mac.update(paramArrayOfByte, paramInt1, paramInt2);
      byte[] arrayOfByte2 = new byte[this.mac.getMacSize()];
      this.mac.doFinal(arrayOfByte2, 0);
      return arrayOfByte2;
    }
    catch (IOException localIOException)
    {
    }
    throw new IllegalStateException("Internal error during mac calculation");
  }

  public int getSize()
  {
    return this.mac.getMacSize();
  }
}