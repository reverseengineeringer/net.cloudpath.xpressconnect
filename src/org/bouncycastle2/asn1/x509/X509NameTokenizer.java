package org.bouncycastle2.asn1.x509;

public class X509NameTokenizer
{
  private StringBuffer buf = new StringBuffer();
  private int index;
  private char seperator;
  private String value;

  public X509NameTokenizer(String paramString)
  {
    this(paramString, ',');
  }

  public X509NameTokenizer(String paramString, char paramChar)
  {
    this.value = paramString;
    this.index = -1;
    this.seperator = paramChar;
  }

  public boolean hasMoreTokens()
  {
    return this.index != this.value.length();
  }

  public String nextToken()
  {
    if (this.index == this.value.length())
      return null;
    int i = 1 + this.index;
    int j = 0;
    int k = 0;
    this.buf.setLength(0);
    if (i == this.value.length())
    {
      label46: this.index = i;
      return this.buf.toString().trim();
    }
    char c = this.value.charAt(i);
    if (c == '"')
      if (k == 0)
        if (j != 0)
        {
          j = 0;
          label89: k = 0;
        }
    while (true)
    {
      i++;
      break;
      j = 1;
      break label89;
      this.buf.append(c);
      break label89;
      if ((k != 0) || (j != 0))
      {
        if ((c == '#') && (this.buf.charAt(-1 + this.buf.length()) == '='))
          this.buf.append('\\');
        while (true)
        {
          this.buf.append(c);
          k = 0;
          break;
          if ((c == '+') && (this.seperator != '+'))
            this.buf.append('\\');
        }
      }
      if (c == '\\')
      {
        k = 1;
      }
      else
      {
        if (c == this.seperator)
          break label46;
        this.buf.append(c);
      }
    }
  }
}