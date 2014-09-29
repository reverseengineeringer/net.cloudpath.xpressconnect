package jcifs.smb;

import java.util.Date;
import jcifs.Config;
import jcifs.util.Hexdump;

class SmbComOpenAndX extends AndXServerMessageBlock
{
  private static final int BATCH_LIMIT = 0;
  private static final int DO_NOT_CACHE = 4096;
  private static final int FLAGS_REQUEST_BATCH_OPLOCK = 4;
  private static final int FLAGS_REQUEST_OPLOCK = 2;
  private static final int FLAGS_RETURN_ADDITIONAL_INFO = 1;
  private static final int OPEN_FN_CREATE = 16;
  private static final int OPEN_FN_FAIL_IF_EXISTS = 0;
  private static final int OPEN_FN_OPEN = 1;
  private static final int OPEN_FN_TRUNC = 2;
  private static final int SHARING_COMPATIBILITY = 0;
  private static final int SHARING_DENY_NONE = 64;
  private static final int SHARING_DENY_READ_EXECUTE = 48;
  private static final int SHARING_DENY_READ_WRITE_EXECUTE = 16;
  private static final int SHARING_DENY_WRITE = 32;
  private static final int WRITE_THROUGH = 16384;
  int allocationSize;
  int creationTime;
  int desiredAccess;
  int fileAttributes;
  int flags;
  int openFunction;
  int searchAttributes;

  SmbComOpenAndX(String paramString, int paramInt1, int paramInt2, ServerMessageBlock paramServerMessageBlock)
  {
    super(paramServerMessageBlock);
    this.path = paramString;
    this.command = 45;
    this.desiredAccess = (paramInt1 & 0x3);
    if (this.desiredAccess == 3)
      this.desiredAccess = 2;
    this.desiredAccess = (0x40 | this.desiredAccess);
    this.desiredAccess = (0xFFFFFFFE & this.desiredAccess);
    this.searchAttributes = 22;
    this.fileAttributes = 0;
    if ((paramInt2 & 0x40) == 64)
    {
      if ((paramInt2 & 0x10) == 16)
      {
        this.openFunction = 18;
        return;
      }
      this.openFunction = 2;
      return;
    }
    if ((paramInt2 & 0x10) == 16)
    {
      if ((paramInt2 & 0x20) == 32)
      {
        this.openFunction = 16;
        return;
      }
      this.openFunction = 17;
      return;
    }
    this.openFunction = 1;
  }

  int getBatchLimit(byte paramByte)
  {
    if (paramByte == 46)
      return BATCH_LIMIT;
    return 0;
  }

  int readBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int readParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  public String toString()
  {
    return new String("SmbComOpenAndX[" + super.toString() + ",flags=0x" + Hexdump.toHexString(this.flags, 2) + ",desiredAccess=0x" + Hexdump.toHexString(this.desiredAccess, 4) + ",searchAttributes=0x" + Hexdump.toHexString(this.searchAttributes, 4) + ",fileAttributes=0x" + Hexdump.toHexString(this.fileAttributes, 4) + ",creationTime=" + new Date(this.creationTime) + ",openFunction=0x" + Hexdump.toHexString(this.openFunction, 2) + ",allocationSize=" + this.allocationSize + ",fileName=" + this.path + "]");
  }

  int writeBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt;
    if (this.useUnicode)
    {
      int j = paramInt + 1;
      paramArrayOfByte[paramInt] = 0;
      paramInt = j;
    }
    return paramInt + writeString(this.path, paramArrayOfByte, paramInt) - i;
  }

  int writeParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    writeInt2(this.flags, paramArrayOfByte, paramInt);
    int i = paramInt + 2;
    writeInt2(this.desiredAccess, paramArrayOfByte, i);
    int j = i + 2;
    writeInt2(this.searchAttributes, paramArrayOfByte, j);
    int k = j + 2;
    writeInt2(this.fileAttributes, paramArrayOfByte, k);
    int m = k + 2;
    this.creationTime = 0;
    writeInt4(this.creationTime, paramArrayOfByte, m);
    int n = m + 4;
    writeInt2(this.openFunction, paramArrayOfByte, n);
    int i1 = n + 2;
    writeInt4(this.allocationSize, paramArrayOfByte, i1);
    int i2 = i1 + 4;
    int i3 = 0;
    int i5;
    for (int i4 = i2; i3 < 8; i4 = i5)
    {
      i5 = i4 + 1;
      paramArrayOfByte[i4] = 0;
      i3++;
    }
    return i4 - paramInt;
  }
}