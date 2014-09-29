package jcifs.smb;

import jcifs.util.Hexdump;

public class SmbShareInfo
  implements FileEntry
{
  protected String netName;
  protected String remark;
  protected int type;

  public SmbShareInfo()
  {
  }

  public SmbShareInfo(String paramString1, int paramInt, String paramString2)
  {
    this.netName = paramString1;
    this.type = paramInt;
    this.remark = paramString2;
  }

  public long createTime()
  {
    return 0L;
  }

  public boolean equals(Object paramObject)
  {
    if ((paramObject instanceof SmbShareInfo))
    {
      SmbShareInfo localSmbShareInfo = (SmbShareInfo)paramObject;
      return this.netName.equals(localSmbShareInfo.netName);
    }
    return false;
  }

  public int getAttributes()
  {
    return 17;
  }

  public String getName()
  {
    return this.netName;
  }

  public int getType()
  {
    switch (0xFFFF & this.type)
    {
    case 2:
    default:
      return 8;
    case 1:
      return 32;
    case 3:
    }
    return 16;
  }

  public int hashCode()
  {
    return this.netName.hashCode();
  }

  public long lastModified()
  {
    return 0L;
  }

  public long length()
  {
    return 0L;
  }

  public String toString()
  {
    return new String("SmbShareInfo[netName=" + this.netName + ",type=0x" + Hexdump.toHexString(this.type, 8) + ",remark=" + this.remark + "]");
  }
}