package jcifs.smb;

import jcifs.util.Hexdump;

class SmbComNTCreateAndX extends AndXServerMessageBlock
{
  static final int FILE_CREATE = 2;
  static final int FILE_OPEN = 1;
  static final int FILE_OPEN_IF = 3;
  static final int FILE_OVERWRITE = 4;
  static final int FILE_OVERWRITE_IF = 5;
  static final int FILE_SEQUENTIAL_ONLY = 4;
  static final int FILE_SUPERSEDE = 0;
  static final int FILE_SYNCHRONOUS_IO_ALERT = 16;
  static final int FILE_SYNCHRONOUS_IO_NONALERT = 32;
  static final int FILE_WRITE_THROUGH = 2;
  static final int SECURITY_CONTEXT_TRACKING = 1;
  static final int SECURITY_EFFECTIVE_ONLY = 2;
  private long allocationSize;
  private int createDisposition;
  private int createOptions;
  int desiredAccess;
  private int extFileAttributes;
  int flags0;
  private int impersonationLevel;
  private int namelen_index;
  private int rootDirectoryFid;
  private byte securityFlags;
  private int shareAccess;

  SmbComNTCreateAndX(String paramString, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, ServerMessageBlock paramServerMessageBlock)
  {
    super(paramServerMessageBlock);
    this.path = paramString;
    this.command = -94;
    this.desiredAccess = paramInt2;
    this.desiredAccess = (0x89 | this.desiredAccess);
    this.extFileAttributes = paramInt4;
    this.shareAccess = paramInt3;
    if ((paramInt1 & 0x40) == 64)
      if ((paramInt1 & 0x10) == 16)
      {
        this.createDisposition = 5;
        if ((paramInt5 & 0x1) != 0)
          break label146;
      }
    label146: for (this.createOptions = (paramInt5 | 0x40); ; this.createOptions = paramInt5)
    {
      this.impersonationLevel = 2;
      this.securityFlags = 3;
      return;
      this.createDisposition = 4;
      break;
      if ((paramInt1 & 0x10) == 16)
      {
        if ((paramInt1 & 0x20) == 32)
        {
          this.createDisposition = 2;
          break;
        }
        this.createDisposition = 3;
        break;
      }
      this.createDisposition = 1;
      break;
    }
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
    return new String("SmbComNTCreateAndX[" + super.toString() + ",flags=0x" + Hexdump.toHexString(this.flags0, 2) + ",rootDirectoryFid=" + this.rootDirectoryFid + ",desiredAccess=0x" + Hexdump.toHexString(this.desiredAccess, 4) + ",allocationSize=" + this.allocationSize + ",extFileAttributes=0x" + Hexdump.toHexString(this.extFileAttributes, 4) + ",shareAccess=0x" + Hexdump.toHexString(this.shareAccess, 4) + ",createDisposition=0x" + Hexdump.toHexString(this.createDisposition, 4) + ",createOptions=0x" + Hexdump.toHexString(this.createOptions, 8) + ",impersonationLevel=0x" + Hexdump.toHexString(this.impersonationLevel, 4) + ",securityFlags=0x" + Hexdump.toHexString(this.securityFlags, 2) + ",name=" + this.path + "]");
  }

  int writeBytesWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = writeString(this.path, paramArrayOfByte, paramInt);
    if (this.useUnicode);
    for (int j = 2 * this.path.length(); ; j = i)
    {
      writeInt2(j, paramArrayOfByte, this.namelen_index);
      return i;
    }
  }

  int writeParameterWordsWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    paramArrayOfByte[paramInt] = 0;
    this.namelen_index = i;
    int j = i + 2;
    writeInt4(this.flags0, paramArrayOfByte, j);
    int k = j + 4;
    writeInt4(this.rootDirectoryFid, paramArrayOfByte, k);
    int m = k + 4;
    writeInt4(this.desiredAccess, paramArrayOfByte, m);
    int n = m + 4;
    writeInt8(this.allocationSize, paramArrayOfByte, n);
    int i1 = n + 8;
    writeInt4(this.extFileAttributes, paramArrayOfByte, i1);
    int i2 = i1 + 4;
    writeInt4(this.shareAccess, paramArrayOfByte, i2);
    int i3 = i2 + 4;
    writeInt4(this.createDisposition, paramArrayOfByte, i3);
    int i4 = i3 + 4;
    writeInt4(this.createOptions, paramArrayOfByte, i4);
    int i5 = i4 + 4;
    writeInt4(this.impersonationLevel, paramArrayOfByte, i5);
    int i6 = i5 + 4;
    int i7 = i6 + 1;
    paramArrayOfByte[i6] = this.securityFlags;
    return i7 - paramInt;
  }
}