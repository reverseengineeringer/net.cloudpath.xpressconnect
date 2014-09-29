package jcifs.smb;

import java.util.Enumeration;
import jcifs.Config;
import jcifs.util.Hexdump;

abstract class SmbComTransaction extends ServerMessageBlock
  implements Enumeration
{
  private static final int DEFAULT_MAX_DATA_COUNT = 0;
  private static final int DISCONNECT_TID = 1;
  static final int NET_SERVER_ENUM2 = 104;
  static final int NET_SERVER_ENUM3 = 215;
  static final int NET_SHARE_ENUM = 0;
  private static final int ONE_WAY_TRANSACTION = 2;
  private static final int PADDING_SIZE = 2;
  private static final int PRIMARY_SETUP_OFFSET = 61;
  private static final int SECONDARY_PARAMETER_OFFSET = 51;
  static final byte TRANS2_FIND_FIRST2 = 1;
  static final byte TRANS2_FIND_NEXT2 = 2;
  static final byte TRANS2_GET_DFS_REFERRAL = 16;
  static final byte TRANS2_QUERY_FS_INFORMATION = 3;
  static final byte TRANS2_QUERY_PATH_INFORMATION = 5;
  static final byte TRANS2_SET_FILE_INFORMATION = 8;
  static final int TRANSACTION_BUF_SIZE = 65535;
  static final byte TRANS_CALL_NAMED_PIPE = 84;
  static final byte TRANS_PEEK_NAMED_PIPE = 35;
  static final byte TRANS_TRANSACT_NAMED_PIPE = 38;
  static final byte TRANS_WAIT_NAMED_PIPE = 83;
  private int bufDataOffset;
  private int bufParameterOffset;
  protected int dataCount;
  protected int dataDisplacement;
  protected int dataOffset;
  private int fid;
  private int flags = 0;
  private boolean hasMore = true;
  private boolean isPrimary = true;
  int maxBufferSize;
  int maxDataCount = DEFAULT_MAX_DATA_COUNT;
  int maxParameterCount = 1024;
  byte maxSetupCount;
  String name = "";
  private int pad = 0;
  private int pad1 = 0;
  protected int parameterCount;
  protected int parameterDisplacement;
  protected int parameterOffset;
  protected int primarySetupOffset = 61;
  protected int secondaryParameterOffset = 51;
  int setupCount = 1;
  byte subCommand;
  int timeout = 0;
  int totalDataCount;
  int totalParameterCount;
  byte[] txn_buf;

  public boolean hasMoreElements()
  {
    return this.hasMore;
  }

  public Object nextElement()
  {
    if (this.isPrimary)
    {
      this.isPrimary = false;
      this.parameterOffset = (2 + (this.primarySetupOffset + 2 * this.setupCount));
      int i1;
      label95: int i3;
      if (this.command != -96)
      {
        if ((this.command == 37) && (!isResponse()))
          this.parameterOffset += stringWireLength(this.name, this.parameterOffset);
        this.pad = (this.parameterOffset % 2);
        if (this.pad != 0)
          break label313;
        i1 = 0;
        this.pad = i1;
        this.parameterOffset += this.pad;
        this.totalParameterCount = writeParametersWireFormat(this.txn_buf, this.bufParameterOffset);
        this.bufDataOffset = this.totalParameterCount;
        int i2 = this.maxBufferSize - this.parameterOffset;
        this.parameterCount = Math.min(this.totalParameterCount, i2);
        i3 = i2 - this.parameterCount;
        this.dataOffset = (this.parameterOffset + this.parameterCount);
        this.pad1 = (this.dataOffset % 2);
        if (this.pad1 != 0)
          break label324;
      }
      label313: label324: for (int i4 = 0; ; i4 = 2 - this.pad1)
      {
        this.pad1 = i4;
        this.dataOffset += this.pad1;
        this.totalDataCount = writeDataWireFormat(this.txn_buf, this.bufDataOffset);
        this.dataCount = Math.min(this.totalDataCount, i3);
        if ((this.parameterDisplacement + this.parameterCount >= this.totalParameterCount) && (this.dataDisplacement + this.dataCount >= this.totalDataCount))
          this.hasMore = false;
        return this;
        if (this.command != -96)
          break;
        this.parameterOffset = (2 + this.parameterOffset);
        break;
        i1 = 2 - this.pad;
        break label95;
      }
    }
    label350: int n;
    label388: int j;
    if (this.command != -96)
    {
      this.command = 38;
      this.parameterOffset = 51;
      if (this.totalParameterCount - this.parameterDisplacement > 0)
      {
        this.pad = (this.parameterOffset % 2);
        if (this.pad != 0)
          break label560;
        n = 0;
        this.pad = n;
        this.parameterOffset += this.pad;
      }
      this.parameterDisplacement += this.parameterCount;
      int i = this.maxBufferSize - this.parameterOffset - this.pad;
      this.parameterCount = Math.min(this.totalParameterCount - this.parameterDisplacement, i);
      j = i - this.parameterCount;
      this.dataOffset = (this.parameterOffset + this.parameterCount);
      this.pad1 = (this.dataOffset % 2);
      if (this.pad1 != 0)
        break label571;
    }
    label560: label571: for (int k = 0; ; k = 2 - this.pad1)
    {
      this.pad1 = k;
      this.dataOffset += this.pad1;
      this.dataDisplacement += this.dataCount;
      int m = j - this.pad1;
      this.dataCount = Math.min(this.totalDataCount - this.dataDisplacement, m);
      break;
      this.command = -95;
      break label350;
      n = 2 - this.pad;
      break label388;
    }
  }

  int readBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  abstract int readDataWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

  int readParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  abstract int readParametersWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

  abstract int readSetupWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

  void reset()
  {
    super.reset();
    this.hasMore = true;
    this.isPrimary = true;
  }

  void reset(int paramInt, String paramString)
  {
    reset();
  }

  public String toString()
  {
    return new String(super.toString() + ",totalParameterCount=" + this.totalParameterCount + ",totalDataCount=" + this.totalDataCount + ",maxParameterCount=" + this.maxParameterCount + ",maxDataCount=" + this.maxDataCount + ",maxSetupCount=" + this.maxSetupCount + ",flags=0x" + Hexdump.toHexString(this.flags, 2) + ",timeout=" + this.timeout + ",parameterCount=" + this.parameterCount + ",parameterOffset=" + this.parameterOffset + ",parameterDisplacement=" + this.parameterDisplacement + ",dataCount=" + this.dataCount + ",dataOffset=" + this.dataOffset + ",dataDisplacement=" + this.dataDisplacement + ",setupCount=" + this.setupCount + ",pad=" + this.pad + ",pad1=" + this.pad1);
  }

  int writeBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt;
    int j = this.pad;
    if ((this.command == 37) && (!isResponse()))
      paramInt += writeString(this.name, paramArrayOfByte, paramInt);
    if (this.parameterCount > 0)
    {
      int i2 = j;
      int i5;
      for (int i3 = paramInt; ; i3 = i5)
      {
        int i4 = i2 - 1;
        if (i2 <= 0)
          break;
        i5 = i3 + 1;
        paramArrayOfByte[i3] = 0;
        i2 = i4;
      }
      System.arraycopy(this.txn_buf, this.bufParameterOffset, paramArrayOfByte, i3, this.parameterCount);
      paramInt = i3 + this.parameterCount;
    }
    if (this.dataCount > 0)
    {
      int k = this.pad1;
      int i1;
      for (int m = paramInt; ; m = i1)
      {
        int n = k - 1;
        if (k <= 0)
          break;
        i1 = m + 1;
        paramArrayOfByte[m] = 0;
        k = n;
      }
      System.arraycopy(this.txn_buf, this.bufDataOffset, paramArrayOfByte, m, this.dataCount);
      this.bufDataOffset += this.dataCount;
      paramInt = m + this.dataCount;
    }
    return paramInt - i;
  }

  abstract int writeDataWireFormat(byte[] paramArrayOfByte, int paramInt);

  int writeParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    writeInt2(this.totalParameterCount, paramArrayOfByte, paramInt);
    int i = paramInt + 2;
    writeInt2(this.totalDataCount, paramArrayOfByte, i);
    int j = i + 2;
    if (this.command != 38)
    {
      writeInt2(this.maxParameterCount, paramArrayOfByte, j);
      int i6 = j + 2;
      writeInt2(this.maxDataCount, paramArrayOfByte, i6);
      int i7 = i6 + 2;
      int i8 = i7 + 1;
      paramArrayOfByte[i7] = this.maxSetupCount;
      int i9 = i8 + 1;
      paramArrayOfByte[i8] = 0;
      writeInt2(this.flags, paramArrayOfByte, i9);
      int i10 = i9 + 2;
      writeInt4(this.timeout, paramArrayOfByte, i10);
      int i11 = i10 + 4;
      int i12 = i11 + 1;
      paramArrayOfByte[i11] = 0;
      j = i12 + 1;
      paramArrayOfByte[i12] = 0;
    }
    writeInt2(this.parameterCount, paramArrayOfByte, j);
    int k = j + 2;
    writeInt2(this.parameterOffset, paramArrayOfByte, k);
    int m = k + 2;
    if (this.command == 38)
    {
      writeInt2(this.parameterDisplacement, paramArrayOfByte, m);
      m += 2;
    }
    writeInt2(this.dataCount, paramArrayOfByte, m);
    int n = m + 2;
    int i1;
    int i2;
    if (this.dataCount == 0)
    {
      i1 = 0;
      writeInt2(i1, paramArrayOfByte, n);
      i2 = n + 2;
      if (this.command != 38)
        break label292;
      writeInt2(this.dataDisplacement, paramArrayOfByte, i2);
    }
    label292: int i4;
    for (int i5 = i2 + 2; ; i5 = i4 + writeSetupWireFormat(paramArrayOfByte, i4))
    {
      return i5 - paramInt;
      i1 = this.dataOffset;
      break;
      int i3 = i2 + 1;
      paramArrayOfByte[i2] = ((byte)this.setupCount);
      i4 = i3 + 1;
      paramArrayOfByte[i3] = 0;
    }
  }

  abstract int writeParametersWireFormat(byte[] paramArrayOfByte, int paramInt);

  abstract int writeSetupWireFormat(byte[] paramArrayOfByte, int paramInt);
}