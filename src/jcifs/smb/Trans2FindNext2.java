package jcifs.smb;

import jcifs.util.Hexdump;

class Trans2FindNext2 extends SmbComTransaction
{
  private String filename;
  private int flags;
  private int informationLevel;
  private int resumeKey;
  private int sid;

  Trans2FindNext2(int paramInt1, int paramInt2, String paramString)
  {
    this.sid = paramInt1;
    this.resumeKey = paramInt2;
    this.filename = paramString;
    this.command = 50;
    this.subCommand = 2;
    this.informationLevel = 260;
    this.flags = 0;
    this.maxParameterCount = 8;
    this.maxDataCount = Trans2FindFirst2.LIST_SIZE;
    this.maxSetupCount = 0;
  }

  int readDataWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return 0;
  }

  int readParametersWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return 0;
  }

  int readSetupWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return 0;
  }

  void reset(int paramInt, String paramString)
  {
    super.reset();
    this.resumeKey = paramInt;
    this.filename = paramString;
    this.flags2 = 0;
  }

  public String toString()
  {
    return new String("Trans2FindNext2[" + super.toString() + ",sid=" + this.sid + ",searchCount=" + Trans2FindFirst2.LIST_SIZE + ",informationLevel=0x" + Hexdump.toHexString(this.informationLevel, 3) + ",resumeKey=0x" + Hexdump.toHexString(this.resumeKey, 4) + ",flags=0x" + Hexdump.toHexString(this.flags, 2) + ",filename=" + this.filename + "]");
  }

  int writeDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeParametersWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    writeInt2(this.sid, paramArrayOfByte, paramInt);
    int i = paramInt + 2;
    writeInt2(Trans2FindFirst2.LIST_COUNT, paramArrayOfByte, i);
    int j = i + 2;
    writeInt2(this.informationLevel, paramArrayOfByte, j);
    int k = j + 2;
    writeInt4(this.resumeKey, paramArrayOfByte, k);
    int m = k + 4;
    writeInt2(this.flags, paramArrayOfByte, m);
    int n = m + 2;
    return n + writeString(this.filename, paramArrayOfByte, n) - paramInt;
  }

  int writeSetupWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    paramArrayOfByte[paramInt] = this.subCommand;
    (i + 1);
    paramArrayOfByte[i] = 0;
    return 2;
  }
}