package jcifs.smb;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import jcifs.util.Encdec;

public class SmbRandomAccessFile
  implements DataOutput, DataInput
{
  private static final int WRITE_OPTIONS = 2114;
  private int access = 0;
  private int ch;
  private SmbFile file;
  private long fp;
  private int openFlags;
  private int options = 0;
  private int readSize;
  private byte[] tmp = new byte[8];
  private int writeSize;
  private SmbComWriteAndXResponse write_andx_resp = null;

  public SmbRandomAccessFile(String paramString1, String paramString2, int paramInt)
    throws SmbException, MalformedURLException, UnknownHostException
  {
    this(new SmbFile(paramString1, "", null, paramInt), paramString2);
  }

  public SmbRandomAccessFile(SmbFile paramSmbFile, String paramString)
    throws SmbException, MalformedURLException, UnknownHostException
  {
    this.file = paramSmbFile;
    if (paramString.equals("r"))
      this.openFlags = 17;
    while (true)
    {
      paramSmbFile.open(this.openFlags, this.access, 128, this.options);
      this.readSize = (-70 + paramSmbFile.tree.session.transport.rcv_buf_size);
      this.writeSize = (-70 + paramSmbFile.tree.session.transport.snd_buf_size);
      this.fp = 0L;
      return;
      if (!paramString.equals("rw"))
        break;
      this.openFlags = 23;
      this.write_andx_resp = new SmbComWriteAndXResponse();
      this.options = 2114;
      this.access = 3;
    }
    throw new IllegalArgumentException("Invalid mode");
  }

  public void close()
    throws SmbException
  {
    this.file.close();
  }

  public long getFilePointer()
    throws SmbException
  {
    return this.fp;
  }

  public long length()
    throws SmbException
  {
    return this.file.length();
  }

  public int read()
    throws SmbException
  {
    if (read(this.tmp, 0, 1) == -1)
      return -1;
    return 0xFF & this.tmp[0];
  }

  public int read(byte[] paramArrayOfByte)
    throws SmbException
  {
    return read(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SmbException
  {
    if (paramInt2 <= 0)
      return 0;
    long l1 = this.fp;
    if (!this.file.isOpen())
      this.file.open(this.openFlags, 0, 128, this.options);
    SmbComReadAndXResponse localSmbComReadAndXResponse = new SmbComReadAndXResponse(paramArrayOfByte, paramInt1);
    int i;
    int j;
    label139: label147: 
    do
    {
      if (paramInt2 > this.readSize)
      {
        i = this.readSize;
        this.file.send(new SmbComReadAndX(this.file.fid, this.fp, i, null), localSmbComReadAndXResponse);
        j = localSmbComReadAndXResponse.dataLength;
        if (j > 0)
          break label147;
        if (this.fp - l1 <= 0L)
          break label139;
      }
      for (long l2 = this.fp - l1; ; l2 = -1L)
      {
        return (int)l2;
        i = paramInt2;
        break;
      }
      this.fp += j;
      paramInt2 -= j;
      localSmbComReadAndXResponse.off = (j + localSmbComReadAndXResponse.off);
    }
    while ((paramInt2 > 0) && (j == i));
    return (int)(this.fp - l1);
  }

  public final boolean readBoolean()
    throws SmbException
  {
    if (read(this.tmp, 0, 1) < 0)
      throw new SmbException("EOF");
    return this.tmp[0] != 0;
  }

  public final byte readByte()
    throws SmbException
  {
    if (read(this.tmp, 0, 1) < 0)
      throw new SmbException("EOF");
    return this.tmp[0];
  }

  public final char readChar()
    throws SmbException
  {
    if (read(this.tmp, 0, 2) < 0)
      throw new SmbException("EOF");
    return (char)Encdec.dec_uint16be(this.tmp, 0);
  }

  public final double readDouble()
    throws SmbException
  {
    if (read(this.tmp, 0, 8) < 0)
      throw new SmbException("EOF");
    return Encdec.dec_doublebe(this.tmp, 0);
  }

  public final float readFloat()
    throws SmbException
  {
    if (read(this.tmp, 0, 4) < 0)
      throw new SmbException("EOF");
    return Encdec.dec_floatbe(this.tmp, 0);
  }

  public final void readFully(byte[] paramArrayOfByte)
    throws SmbException
  {
    readFully(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public final void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SmbException
  {
    int i = 0;
    do
    {
      int j = read(paramArrayOfByte, paramInt1 + i, paramInt2 - i);
      if (j < 0)
        throw new SmbException("EOF");
      i += j;
      this.fp += j;
    }
    while (i < paramInt2);
  }

  public final int readInt()
    throws SmbException
  {
    if (read(this.tmp, 0, 4) < 0)
      throw new SmbException("EOF");
    return Encdec.dec_uint32be(this.tmp, 0);
  }

  public final String readLine()
    throws SmbException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = -1;
    int j = 0;
    while (j == 0)
    {
      i = read();
      switch (i)
      {
      default:
        localStringBuffer.append((char)i);
        break;
      case -1:
      case 10:
        j = 1;
        break;
      case 13:
        j = 1;
        long l = this.fp;
        if (read() != 10)
          this.fp = l;
        break;
      }
    }
    if ((i == -1) && (localStringBuffer.length() == 0))
      return null;
    return localStringBuffer.toString();
  }

  public final long readLong()
    throws SmbException
  {
    if (read(this.tmp, 0, 8) < 0)
      throw new SmbException("EOF");
    return Encdec.dec_uint64be(this.tmp, 0);
  }

  public final short readShort()
    throws SmbException
  {
    if (read(this.tmp, 0, 2) < 0)
      throw new SmbException("EOF");
    return Encdec.dec_uint16be(this.tmp, 0);
  }

  public final String readUTF()
    throws SmbException
  {
    int i = readUnsignedShort();
    byte[] arrayOfByte = new byte[i];
    read(arrayOfByte, 0, i);
    try
    {
      String str = Encdec.dec_utf8(arrayOfByte, 0, i);
      return str;
    }
    catch (IOException localIOException)
    {
      throw new SmbException("", localIOException);
    }
  }

  public final int readUnsignedByte()
    throws SmbException
  {
    if (read(this.tmp, 0, 1) < 0)
      throw new SmbException("EOF");
    return 0xFF & this.tmp[0];
  }

  public final int readUnsignedShort()
    throws SmbException
  {
    if (read(this.tmp, 0, 2) < 0)
      throw new SmbException("EOF");
    return 0xFFFF & Encdec.dec_uint16be(this.tmp, 0);
  }

  public void seek(long paramLong)
    throws SmbException
  {
    this.fp = paramLong;
  }

  public void setLength(long paramLong)
    throws SmbException
  {
    if (!this.file.isOpen())
      this.file.open(this.openFlags, 0, 128, this.options);
    SmbComWriteResponse localSmbComWriteResponse = new SmbComWriteResponse();
    this.file.send(new SmbComWrite(this.file.fid, (int)(0xFFFFFFFF & paramLong), 0, this.tmp, 0, 0), localSmbComWriteResponse);
  }

  public int skipBytes(int paramInt)
    throws SmbException
  {
    if (paramInt > 0)
    {
      this.fp += paramInt;
      return paramInt;
    }
    return 0;
  }

  public void write(int paramInt)
    throws SmbException
  {
    this.tmp[0] = ((byte)paramInt);
    write(this.tmp, 0, 1);
  }

  public void write(byte[] paramArrayOfByte)
    throws SmbException
  {
    write(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws SmbException
  {
    if (paramInt2 <= 0)
      return;
    if (!this.file.isOpen())
      this.file.open(this.openFlags, 0, 128, this.options);
    if (paramInt2 > this.writeSize);
    for (int i = this.writeSize; ; i = paramInt2)
    {
      this.file.send(new SmbComWriteAndX(this.file.fid, this.fp, paramInt2 - i, paramArrayOfByte, paramInt1, i, null), this.write_andx_resp);
      this.fp += this.write_andx_resp.count;
      paramInt2 = (int)(paramInt2 - this.write_andx_resp.count);
      paramInt1 = (int)(paramInt1 + this.write_andx_resp.count);
      if (paramInt2 > 0)
        break;
      return;
    }
  }

  public final void writeBoolean(boolean paramBoolean)
    throws SmbException
  {
    byte[] arrayOfByte = this.tmp;
    if (paramBoolean);
    for (int i = 1; ; i = 0)
    {
      arrayOfByte[0] = ((byte)i);
      write(this.tmp, 0, 1);
      return;
    }
  }

  public final void writeByte(int paramInt)
    throws SmbException
  {
    this.tmp[0] = ((byte)paramInt);
    write(this.tmp, 0, 1);
  }

  public final void writeBytes(String paramString)
    throws SmbException
  {
    byte[] arrayOfByte = paramString.getBytes();
    write(arrayOfByte, 0, arrayOfByte.length);
  }

  public final void writeChar(int paramInt)
    throws SmbException
  {
    Encdec.enc_uint16be((short)paramInt, this.tmp, 0);
    write(this.tmp, 0, 2);
  }

  public final void writeChars(String paramString)
    throws SmbException
  {
    int i = paramString.length();
    int j = i * 2;
    byte[] arrayOfByte = new byte[j];
    char[] arrayOfChar = new char[i];
    paramString.getChars(0, i, arrayOfChar, 0);
    int k = 0;
    int m = 0;
    while (k < i)
    {
      int n = m + 1;
      arrayOfByte[m] = ((byte)(arrayOfChar[k] >>> '\b'));
      m = n + 1;
      arrayOfByte[n] = ((byte)(arrayOfChar[k] >>> '\000'));
      k++;
    }
    write(arrayOfByte, 0, j);
  }

  public final void writeDouble(double paramDouble)
    throws SmbException
  {
    Encdec.enc_doublebe(paramDouble, this.tmp, 0);
    write(this.tmp, 0, 8);
  }

  public final void writeFloat(float paramFloat)
    throws SmbException
  {
    Encdec.enc_floatbe(paramFloat, this.tmp, 0);
    write(this.tmp, 0, 4);
  }

  public final void writeInt(int paramInt)
    throws SmbException
  {
    Encdec.enc_uint32be(paramInt, this.tmp, 0);
    write(this.tmp, 0, 4);
  }

  public final void writeLong(long paramLong)
    throws SmbException
  {
    Encdec.enc_uint64be(paramLong, this.tmp, 0);
    write(this.tmp, 0, 8);
  }

  public final void writeShort(int paramInt)
    throws SmbException
  {
    Encdec.enc_uint16be((short)paramInt, this.tmp, 0);
    write(this.tmp, 0, 2);
  }

  public final void writeUTF(String paramString)
    throws SmbException
  {
    int i = paramString.length();
    int j = 0;
    int k = 0;
    if (k < i)
    {
      int m = paramString.charAt(k);
      int n;
      if (m > 127)
        if (m > 2047)
          n = 3;
      while (true)
      {
        j += n;
        k++;
        break;
        n = 2;
        continue;
        n = 1;
      }
    }
    byte[] arrayOfByte = new byte[j];
    writeShort(j);
    try
    {
      Encdec.enc_utf8(paramString, arrayOfByte, 0, j);
      write(arrayOfByte, 0, j);
      return;
    }
    catch (IOException localIOException)
    {
      throw new SmbException("", localIOException);
    }
  }
}