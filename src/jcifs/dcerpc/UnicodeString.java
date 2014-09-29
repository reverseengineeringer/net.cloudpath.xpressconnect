package jcifs.dcerpc;

public class UnicodeString extends rpc.unicode_string
{
  boolean zterm;

  public UnicodeString(String paramString, boolean paramBoolean)
  {
    this.zterm = paramBoolean;
    int i = paramString.length();
    if (paramBoolean);
    int k;
    for (int j = 1; ; j = 0)
    {
      short s = (short)(2 * (i + j));
      this.maximum_length = s;
      this.length = s;
      this.buffer = new short[i + j];
      for (k = 0; k < i; k++)
        this.buffer[k] = ((short)paramString.charAt(k));
    }
    if (paramBoolean)
      this.buffer[k] = 0;
  }

  public UnicodeString(rpc.unicode_string paramunicode_string, boolean paramBoolean)
  {
    this.length = paramunicode_string.length;
    this.maximum_length = paramunicode_string.maximum_length;
    this.buffer = paramunicode_string.buffer;
    this.zterm = paramBoolean;
  }

  public UnicodeString(boolean paramBoolean)
  {
    this.zterm = paramBoolean;
  }

  public String toString()
  {
    int i = this.length / 2;
    if (this.zterm);
    int k;
    char[] arrayOfChar;
    for (int j = 1; ; j = 0)
    {
      k = i - j;
      arrayOfChar = new char[k];
      for (int m = 0; m < k; m++)
        arrayOfChar[m] = ((char)this.buffer[m]);
    }
    return new String(arrayOfChar, 0, k);
  }
}