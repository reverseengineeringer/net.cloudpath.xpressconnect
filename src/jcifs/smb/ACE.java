package jcifs.smb;

import jcifs.util.Hexdump;

public class ACE
{
  public static final int DELETE = 65536;
  public static final int FILE_APPEND_DATA = 4;
  public static final int FILE_DELETE = 64;
  public static final int FILE_EXECUTE = 32;
  public static final int FILE_READ_ATTRIBUTES = 128;
  public static final int FILE_READ_DATA = 1;
  public static final int FILE_READ_EA = 8;
  public static final int FILE_WRITE_ATTRIBUTES = 256;
  public static final int FILE_WRITE_DATA = 2;
  public static final int FILE_WRITE_EA = 16;
  public static final int FLAGS_CONTAINER_INHERIT = 2;
  public static final int FLAGS_INHERITED = 16;
  public static final int FLAGS_INHERIT_ONLY = 8;
  public static final int FLAGS_NO_PROPAGATE = 4;
  public static final int FLAGS_OBJECT_INHERIT = 1;
  public static final int GENERIC_ALL = 268435456;
  public static final int GENERIC_EXECUTE = 536870912;
  public static final int GENERIC_READ = -2147483648;
  public static final int GENERIC_WRITE = 1073741824;
  public static final int READ_CONTROL = 131072;
  public static final int SYNCHRONIZE = 1048576;
  public static final int WRITE_DAC = 262144;
  public static final int WRITE_OWNER = 524288;
  int access;
  boolean allow;
  int flags;
  SID sid;

  void appendCol(StringBuffer paramStringBuffer, String paramString, int paramInt)
  {
    paramStringBuffer.append(paramString);
    int i = paramInt - paramString.length();
    for (int j = 0; j < i; j++)
      paramStringBuffer.append(' ');
  }

  int decode(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramInt + 1;
    if (paramArrayOfByte[paramInt] == 0);
    for (boolean bool = true; ; bool = false)
    {
      this.allow = bool;
      int j = i + 1;
      this.flags = (0xFF & paramArrayOfByte[i]);
      int k = ServerMessageBlock.readInt2(paramArrayOfByte, j);
      int m = j + 2;
      this.access = ServerMessageBlock.readInt4(paramArrayOfByte, m);
      this.sid = new SID(paramArrayOfByte, m + 4);
      return k;
    }
  }

  public int getAccessMask()
  {
    return this.access;
  }

  public String getApplyToText()
  {
    switch (0xB & this.flags)
    {
    case 4:
    case 5:
    case 6:
    case 7:
    case 8:
    default:
      return "Invalid";
    case 0:
      return "This folder only";
    case 3:
      return "This folder, subfolders and files";
    case 11:
      return "Subfolders and files only";
    case 2:
      return "This folder and subfolders";
    case 10:
      return "Subfolders only";
    case 1:
      return "This folder and files";
    case 9:
    }
    return "Files only";
  }

  public int getFlags()
  {
    return this.flags;
  }

  public SID getSID()
  {
    return this.sid;
  }

  public boolean isAllow()
  {
    return this.allow;
  }

  public boolean isInherited()
  {
    return (0x10 & this.flags) != 0;
  }

  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    String str1;
    if (isAllow())
    {
      str1 = "Allow ";
      localStringBuffer.append(str1);
      appendCol(localStringBuffer, this.sid.toDisplayString(), 25);
      localStringBuffer.append(" 0x").append(Hexdump.toHexString(this.access, 8)).append(' ');
      if (!isInherited())
        break label102;
    }
    label102: for (String str2 = "Inherited "; ; str2 = "Direct    ")
    {
      localStringBuffer.append(str2);
      appendCol(localStringBuffer, getApplyToText(), 34);
      return localStringBuffer.toString();
      str1 = "Deny  ";
      break;
    }
  }
}