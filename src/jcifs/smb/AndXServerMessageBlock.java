package jcifs.smb;

import jcifs.util.Hexdump;

abstract class AndXServerMessageBlock extends ServerMessageBlock
{
  private static final int ANDX_COMMAND_OFFSET = 1;
  private static final int ANDX_OFFSET_OFFSET = 3;
  private static final int ANDX_RESERVED_OFFSET = 2;
  ServerMessageBlock andx = null;
  private byte andxCommand = -1;
  private int andxOffset = 0;

  AndXServerMessageBlock()
  {
  }

  AndXServerMessageBlock(ServerMessageBlock paramServerMessageBlock)
  {
    if (paramServerMessageBlock != null)
    {
      this.andx = paramServerMessageBlock;
      this.andxCommand = paramServerMessageBlock.command;
    }
  }

  int decode(byte[] paramArrayOfByte, int paramInt)
  {
    this.headerStart = paramInt;
    int i = paramInt + readHeaderWireFormat(paramArrayOfByte, paramInt);
    this.length = (i + readAndXWireFormat(paramArrayOfByte, i) - paramInt);
    return this.length;
  }

  int encode(byte[] paramArrayOfByte, int paramInt)
  {
    this.headerStart = paramInt;
    int i = paramInt + writeHeaderWireFormat(paramArrayOfByte, paramInt);
    this.length = (i + writeAndXWireFormat(paramArrayOfByte, i) - paramInt);
    if (this.digest != null)
      this.digest.sign(paramArrayOfByte, this.headerStart, this.length, this, this.response);
    return this.length;
  }

  int getBatchLimit(byte paramByte)
  {
    return 0;
  }

  int readAndXWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    this.wordCount = paramArrayOfByte[paramInt];
    if (this.command == -94)
      this.wordCount = 42;
    int j;
    if (this.wordCount != 0)
    {
      this.andxCommand = paramArrayOfByte[i];
      int i2 = i + 2;
      this.andxOffset = readInt2(paramArrayOfByte, i2);
      j = i2 + 2;
      if (this.andxOffset == 0)
        this.andxCommand = -1;
      if (this.wordCount > 2)
      {
        j += readParameterWordsWireFormat(paramArrayOfByte, j);
        if ((this.command == -94) && (((SmbComNTCreateAndXResponse)this).isExtended))
          j += 32;
      }
    }
    while (true)
    {
      this.byteCount = readInt2(paramArrayOfByte, j);
      int k = j + 2;
      if (this.byteCount != 0)
      {
        readBytesWireFormat(paramArrayOfByte, k);
        k += this.byteCount;
      }
      if ((this.errorCode != 0) || (this.andxCommand == -1))
      {
        this.andxCommand = -1;
        this.andx = null;
      }
      int m;
      while (true)
      {
        return k - paramInt;
        if (this.andx == null)
        {
          this.andxCommand = -1;
          throw new RuntimeException("no andx command supplied with response");
        }
        m = this.headerStart + this.andxOffset;
        this.andx.headerStart = this.headerStart;
        this.andx.command = this.andxCommand;
        this.andx.errorCode = this.errorCode;
        this.andx.flags = this.flags;
        this.andx.flags2 = this.flags2;
        this.andx.tid = this.tid;
        this.andx.pid = this.pid;
        this.andx.uid = this.uid;
        this.andx.mid = this.mid;
        this.andx.useUnicode = this.useUnicode;
        if (!(this.andx instanceof AndXServerMessageBlock))
          break;
        k = m + ((AndXServerMessageBlock)this.andx).readAndXWireFormat(paramArrayOfByte, m);
        this.andx.received = true;
      }
      int n = m + 1;
      paramArrayOfByte[m] = ((byte)(0xFF & this.andx.wordCount));
      if ((this.andx.wordCount != 0) && (this.andx.wordCount > 2));
      for (int i1 = n + this.andx.readParameterWordsWireFormat(paramArrayOfByte, n); ; i1 = n)
      {
        this.andx.byteCount = readInt2(paramArrayOfByte, i1);
        k = i1 + 2;
        if (this.andx.byteCount == 0)
          break;
        this.andx.readBytesWireFormat(paramArrayOfByte, k);
        k += this.andx.byteCount;
        break;
      }
      j = i;
    }
  }

  public String toString()
  {
    return new String(super.toString() + ",andxCommand=0x" + Hexdump.toHexString(this.andxCommand, 2) + ",andxOffset=" + this.andxOffset);
  }

  int writeAndXWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    this.wordCount = writeParameterWordsWireFormat(paramArrayOfByte, 2 + (paramInt + 3));
    this.wordCount = (4 + this.wordCount);
    int i = paramInt + (1 + this.wordCount);
    this.wordCount /= 2;
    paramArrayOfByte[paramInt] = ((byte)(0xFF & this.wordCount));
    this.byteCount = writeBytesWireFormat(paramArrayOfByte, i + 2);
    int j = i + 1;
    paramArrayOfByte[i] = ((byte)(0xFF & this.byteCount));
    int k = j + 1;
    paramArrayOfByte[j] = ((byte)(0xFF & this.byteCount >> 8));
    int m = k + this.byteCount;
    if ((this.andx == null) || (!SmbConstants.USE_BATCHING) || (this.batchLevel >= getBatchLimit(this.andx.command)))
    {
      this.andxCommand = -1;
      this.andx = null;
      paramArrayOfByte[(paramInt + 1)] = -1;
      paramArrayOfByte[(paramInt + 2)] = 0;
      paramArrayOfByte[(paramInt + 3)] = -34;
      paramArrayOfByte[(1 + (paramInt + 3))] = -34;
      return m - paramInt;
    }
    this.andx.batchLevel = (1 + this.batchLevel);
    paramArrayOfByte[(paramInt + 1)] = this.andxCommand;
    paramArrayOfByte[(paramInt + 2)] = 0;
    this.andxOffset = (m - this.headerStart);
    writeInt2(this.andxOffset, paramArrayOfByte, paramInt + 3);
    this.andx.useUnicode = this.useUnicode;
    if ((this.andx instanceof AndXServerMessageBlock))
      this.andx.uid = this.uid;
    int i2;
    for (int i3 = m + ((AndXServerMessageBlock)this.andx).writeAndXWireFormat(paramArrayOfByte, m); ; i3 = i2 + this.andx.byteCount)
    {
      return i3 - paramInt;
      this.andx.wordCount = this.andx.writeParameterWordsWireFormat(paramArrayOfByte, m);
      int n = m + (1 + this.andx.wordCount);
      ServerMessageBlock localServerMessageBlock = this.andx;
      localServerMessageBlock.wordCount /= 2;
      paramArrayOfByte[m] = ((byte)(0xFF & this.andx.wordCount));
      this.andx.byteCount = this.andx.writeBytesWireFormat(paramArrayOfByte, n + 2);
      int i1 = n + 1;
      paramArrayOfByte[n] = ((byte)(0xFF & this.andx.byteCount));
      i2 = i1 + 1;
      paramArrayOfByte[i1] = ((byte)(0xFF & this.andx.byteCount >> 8));
    }
  }
}