package jcifs.smb;

public class DosFileFilter
  implements SmbFileFilter
{
  protected int attributes;
  protected String wildcard;

  public DosFileFilter(String paramString, int paramInt)
  {
    this.wildcard = paramString;
    this.attributes = paramInt;
  }

  public boolean accept(SmbFile paramSmbFile)
    throws SmbException
  {
    return (paramSmbFile.getAttributes() & this.attributes) != 0;
  }
}