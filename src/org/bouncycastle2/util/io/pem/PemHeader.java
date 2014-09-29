package org.bouncycastle2.util.io.pem;

public class PemHeader
{
  private String name;
  private String value;

  public PemHeader(String paramString1, String paramString2)
  {
    this.name = paramString1;
    this.value = paramString2;
  }

  private int getHashCode(String paramString)
  {
    if (paramString == null)
      return 1;
    return paramString.hashCode();
  }

  private boolean isEqual(String paramString1, String paramString2)
  {
    if (paramString1 == paramString2)
      return true;
    if ((paramString1 == null) || (paramString2 == null))
      return false;
    return paramString1.equals(paramString2);
  }

  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof PemHeader));
    PemHeader localPemHeader;
    do
    {
      return false;
      localPemHeader = (PemHeader)paramObject;
    }
    while ((localPemHeader != this) && ((!isEqual(this.name, localPemHeader.name)) || (!isEqual(this.value, localPemHeader.value))));
    return true;
  }

  public String getName()
  {
    return this.name;
  }

  public String getValue()
  {
    return this.value;
  }

  public int hashCode()
  {
    return getHashCode(this.name) + 31 * getHashCode(this.value);
  }
}