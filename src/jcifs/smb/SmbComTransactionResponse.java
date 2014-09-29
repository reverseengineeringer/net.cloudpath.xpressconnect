package jcifs.smb;

import java.util.Enumeration;
import jcifs.util.LogStream;

abstract class SmbComTransactionResponse extends ServerMessageBlock
  implements Enumeration
{
  private static final int DISCONNECT_TID = 1;
  private static final int ONE_WAY_TRANSACTION = 2;
  private static final int SETUP_OFFSET = 61;
  protected int bufDataStart;
  protected int bufParameterStart;
  int dataCount;
  protected int dataDisplacement;
  private boolean dataDone;
  protected int dataOffset;
  boolean hasMore = true;
  boolean isPrimary = true;
  int numEntries;
  private int pad;
  private int pad1;
  protected int parameterCount;
  protected int parameterDisplacement;
  protected int parameterOffset;
  private boolean parametersDone;
  FileEntry[] results;
  protected int setupCount;
  int status;
  byte subCommand;
  protected int totalDataCount;
  protected int totalParameterCount;
  byte[] txn_buf = null;

  public boolean hasMoreElements()
  {
    return (this.errorCode == 0) && (this.hasMore);
  }

  public Object nextElement()
  {
    if (this.isPrimary)
      this.isPrimary = false;
    return this;
  }

  int readBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    this.pad1 = 0;
    this.pad = 0;
    if (this.parameterCount > 0)
    {
      int k = this.parameterOffset - (paramInt - this.headerStart);
      this.pad = k;
      int m = paramInt + k;
      System.arraycopy(paramArrayOfByte, m, this.txn_buf, this.bufParameterStart + this.parameterDisplacement, this.parameterCount);
      paramInt = m + this.parameterCount;
    }
    if (this.dataCount > 0)
    {
      int i = this.dataOffset - (paramInt - this.headerStart);
      this.pad1 = i;
      int j = paramInt + i;
      System.arraycopy(paramArrayOfByte, j, this.txn_buf, this.bufDataStart + this.dataDisplacement, this.dataCount);
      (j + this.dataCount);
    }
    if ((!this.parametersDone) && (this.parameterDisplacement + this.parameterCount == this.totalParameterCount))
      this.parametersDone = true;
    if ((!this.dataDone) && (this.dataDisplacement + this.dataCount == this.totalDataCount))
      this.dataDone = true;
    if ((this.parametersDone) && (this.dataDone))
    {
      this.hasMore = false;
      readParametersWireFormat(this.txn_buf, this.bufParameterStart, this.totalParameterCount);
      readDataWireFormat(this.txn_buf, this.bufDataStart, this.totalDataCount);
    }
    return this.pad + this.parameterCount + this.pad1 + this.dataCount;
  }

  abstract int readDataWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

  int readParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    this.totalParameterCount = readInt2(paramArrayOfByte, paramInt);
    if (this.bufDataStart == 0)
      this.bufDataStart = this.totalParameterCount;
    int i = paramInt + 2;
    this.totalDataCount = readInt2(paramArrayOfByte, i);
    int j = i + 4;
    this.parameterCount = readInt2(paramArrayOfByte, j);
    int k = j + 2;
    this.parameterOffset = readInt2(paramArrayOfByte, k);
    int m = k + 2;
    this.parameterDisplacement = readInt2(paramArrayOfByte, m);
    int n = m + 2;
    this.dataCount = readInt2(paramArrayOfByte, n);
    int i1 = n + 2;
    this.dataOffset = readInt2(paramArrayOfByte, i1);
    int i2 = i1 + 2;
    this.dataDisplacement = readInt2(paramArrayOfByte, i2);
    int i3 = i2 + 2;
    this.setupCount = (0xFF & paramArrayOfByte[i3]);
    int i4 = i3 + 2;
    if ((this.setupCount != 0) && (LogStream.level > 2))
      log.println("setupCount is not zero: " + this.setupCount);
    return i4 - paramInt;
  }

  abstract int readParametersWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

  abstract int readSetupWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2);

  void reset()
  {
    super.reset();
    this.bufDataStart = 0;
    this.hasMore = true;
    this.isPrimary = true;
    this.dataDone = false;
    this.parametersDone = false;
  }

  public String toString()
  {
    return new String(super.toString() + ",totalParameterCount=" + this.totalParameterCount + ",totalDataCount=" + this.totalDataCount + ",parameterCount=" + this.parameterCount + ",parameterOffset=" + this.parameterOffset + ",parameterDisplacement=" + this.parameterDisplacement + ",dataCount=" + this.dataCount + ",dataOffset=" + this.dataOffset + ",dataDisplacement=" + this.dataDisplacement + ",setupCount=" + this.setupCount + ",pad=" + this.pad + ",pad1=" + this.pad1);
  }

  int writeBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  abstract int writeDataWireFormat(byte[] paramArrayOfByte, int paramInt);

  int writeParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  abstract int writeParametersWireFormat(byte[] paramArrayOfByte, int paramInt);

  abstract int writeSetupWireFormat(byte[] paramArrayOfByte, int paramInt);
}