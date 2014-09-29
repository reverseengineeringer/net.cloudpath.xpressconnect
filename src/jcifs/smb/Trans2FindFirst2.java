package jcifs.smb;

import jcifs.Config;
import jcifs.util.Hexdump;

class Trans2FindFirst2 extends SmbComTransaction
{
  private static final int DEFAULT_LIST_COUNT = 200;
  private static final int DEFAULT_LIST_SIZE = 65535;
  private static final int FLAGS_CLOSE_AFTER_THIS_REQUEST = 1;
  private static final int FLAGS_CLOSE_IF_END_REACHED = 2;
  private static final int FLAGS_FIND_WITH_BACKUP_INTENT = 16;
  private static final int FLAGS_RESUME_FROM_PREVIOUS_END = 8;
  private static final int FLAGS_RETURN_RESUME_KEYS = 4;
  static final int LIST_COUNT = 0;
  static final int LIST_SIZE = 0;
  static final int SMB_FILE_BOTH_DIRECTORY_INFO = 260;
  static final int SMB_FILE_NAMES_INFO = 259;
  static final int SMB_FIND_FILE_DIRECTORY_INFO = 257;
  static final int SMB_FIND_FILE_FULL_DIRECTORY_INFO = 258;
  static final int SMB_INFO_QUERY_EAS_FROM_LIST = 3;
  static final int SMB_INFO_QUERY_EA_SIZE = 2;
  static final int SMB_INFO_STANDARD = 1;
  private int flags;
  private int informationLevel;
  private int searchAttributes;
  private int searchStorageType = 0;
  private String wildcard;

  Trans2FindFirst2(String paramString1, String paramString2, int paramInt)
  {
    if (paramString1.equals("\\"));
    for (this.path = paramString1; ; this.path = (paramString1 + "\\"))
    {
      this.wildcard = paramString2;
      this.searchAttributes = (paramInt & 0x37);
      this.command = 50;
      this.subCommand = 1;
      this.flags = 0;
      this.informationLevel = 260;
      this.totalDataCount = 0;
      this.maxParameterCount = 10;
      this.maxDataCount = LIST_SIZE;
      this.maxSetupCount = 0;
      return;
    }
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

  public String toString()
  {
    return new String("Trans2FindFirst2[" + super.toString() + ",searchAttributes=0x" + Hexdump.toHexString(this.searchAttributes, 2) + ",searchCount=" + LIST_COUNT + ",flags=0x" + Hexdump.toHexString(this.flags, 2) + ",informationLevel=0x" + Hexdump.toHexString(this.informationLevel, 3) + ",searchStorageType=" + this.searchStorageType + ",filename=" + this.path + "]");
  }

  int writeDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeParametersWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    writeInt2(this.searchAttributes, paramArrayOfByte, paramInt);
    int i = paramInt + 2;
    writeInt2(LIST_COUNT, paramArrayOfByte, i);
    int j = i + 2;
    writeInt2(this.flags, paramArrayOfByte, j);
    int k = j + 2;
    writeInt2(this.informationLevel, paramArrayOfByte, k);
    int m = k + 2;
    writeInt4(this.searchStorageType, paramArrayOfByte, m);
    int n = m + 4;
    return n + writeString(this.path + this.wildcard, paramArrayOfByte, n) - paramInt;
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