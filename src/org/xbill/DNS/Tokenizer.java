package org.xbill.DNS;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.xbill.DNS.utils.base16;
import org.xbill.DNS.utils.base32;
import org.xbill.DNS.utils.base64;

public class Tokenizer
{
  public static final int COMMENT = 5;
  public static final int EOF = 0;
  public static final int EOL = 1;
  public static final int IDENTIFIER = 3;
  public static final int QUOTED_STRING = 4;
  public static final int WHITESPACE = 2;
  private static String delim = " \t\n;()\"";
  private static String quotes = "\"";
  private Token current;
  private String delimiters;
  private String filename;
  private PushbackInputStream is;
  private int line;
  private int multiline;
  private boolean quoting;
  private StringBuffer sb;
  private boolean ungottenToken;
  private boolean wantClose;

  public Tokenizer(File paramFile)
    throws FileNotFoundException
  {
    this(new FileInputStream(paramFile));
    this.wantClose = true;
    this.filename = paramFile.getName();
  }

  public Tokenizer(InputStream paramInputStream)
  {
    if (!(paramInputStream instanceof BufferedInputStream))
      paramInputStream = new BufferedInputStream(paramInputStream);
    this.is = new PushbackInputStream(paramInputStream, 2);
    this.ungottenToken = false;
    this.multiline = 0;
    this.quoting = false;
    this.delimiters = delim;
    this.current = new Token(null);
    this.sb = new StringBuffer();
    this.filename = "<none>";
    this.line = 1;
  }

  public Tokenizer(String paramString)
  {
    this(new ByteArrayInputStream(paramString.getBytes()));
  }

  private String _getIdentifier(String paramString)
    throws IOException
  {
    Token localToken = get();
    if (localToken.type != 3)
      throw exception("expected " + paramString);
    return localToken.value;
  }

  private void checkUnbalancedParens()
    throws TextParseException
  {
    if (this.multiline > 0)
      throw exception("unbalanced parentheses");
  }

  private int getChar()
    throws IOException
  {
    int i = this.is.read();
    if (i == 13)
    {
      int j = this.is.read();
      if (j != 10)
        this.is.unread(j);
      i = 10;
    }
    if (i == 10)
      this.line = (1 + this.line);
    return i;
  }

  private String remainingStrings()
    throws IOException
  {
    StringBuffer localStringBuffer = null;
    while (true)
    {
      Token localToken = get();
      if (!localToken.isString())
      {
        unget();
        if (localStringBuffer != null)
          break;
        return null;
      }
      if (localStringBuffer == null)
        localStringBuffer = new StringBuffer();
      localStringBuffer.append(localToken.value);
    }
    return localStringBuffer.toString();
  }

  private int skipWhitespace()
    throws IOException
  {
    for (int i = 0; ; i++)
    {
      int j = getChar();
      if ((j != 32) && (j != 9) && ((j != 10) || (this.multiline <= 0)))
      {
        ungetChar(j);
        return i;
      }
    }
  }

  private void ungetChar(int paramInt)
    throws IOException
  {
    if (paramInt == -1);
    do
    {
      return;
      this.is.unread(paramInt);
    }
    while (paramInt != 10);
    this.line = (-1 + this.line);
  }

  public void close()
  {
    if (this.wantClose);
    try
    {
      this.is.close();
      return;
    }
    catch (IOException localIOException)
    {
    }
  }

  public TextParseException exception(String paramString)
  {
    return new TokenizerException(this.filename, this.line, paramString);
  }

  protected void finalize()
  {
    close();
  }

  public Token get()
    throws IOException
  {
    return get(false, false);
  }

  public Token get(boolean paramBoolean1, boolean paramBoolean2)
    throws IOException
  {
    if (this.ungottenToken)
    {
      this.ungottenToken = false;
      if (this.current.type == 2)
      {
        if (paramBoolean1)
          return this.current;
      }
      else if (this.current.type == 5)
      {
        if (paramBoolean2)
          return this.current;
      }
      else
      {
        if (this.current.type == 1)
          this.line = (1 + this.line);
        return this.current;
      }
    }
    if ((skipWhitespace() > 0) && (paramBoolean1))
      return this.current.set(2, null);
    int i = 3;
    this.sb.setLength(0);
    int j;
    while (true)
    {
      j = getChar();
      if ((j != -1) && (this.delimiters.indexOf(j) == -1))
        break label498;
      if (j == -1)
      {
        if (this.quoting)
          throw exception("EOF in quoted string");
        if (this.sb.length() == 0)
          return this.current.set(0, null);
        return this.current.set(i, this.sb);
      }
      if ((this.sb.length() != 0) || (i == 4))
        break label463;
      if (j == 40)
      {
        this.multiline = (1 + this.multiline);
        skipWhitespace();
      }
      else if (j == 41)
      {
        if (this.multiline <= 0)
          throw exception("invalid close parenthesis");
        this.multiline = (-1 + this.multiline);
        skipWhitespace();
      }
      else if (j == 34)
      {
        if (!this.quoting)
        {
          this.quoting = true;
          this.delimiters = quotes;
          i = 4;
        }
        else
        {
          this.quoting = false;
          this.delimiters = delim;
          skipWhitespace();
        }
      }
      else
      {
        if (j == 10)
          return this.current.set(1, null);
        if (j != 59)
          break label455;
        int k;
        while (true)
        {
          k = getChar();
          if ((k == 10) || (k == -1))
          {
            if (!paramBoolean2)
              break;
            ungetChar(k);
            return this.current.set(5, this.sb);
          }
          this.sb.append((char)k);
        }
        if ((k == -1) && (i != 4))
        {
          checkUnbalancedParens();
          return this.current.set(0, null);
        }
        if (this.multiline <= 0)
          break;
        skipWhitespace();
        this.sb.setLength(0);
      }
    }
    return this.current.set(1, null);
    label455: throw new IllegalStateException();
    label463: ungetChar(j);
    if ((this.sb.length() == 0) && (i != 4))
    {
      checkUnbalancedParens();
      return this.current.set(0, null);
      label498: if (j == 92)
      {
        j = getChar();
        if (j == -1)
          throw exception("unterminated escape sequence");
        this.sb.append('\\');
      }
      while ((!this.quoting) || (j != 10))
      {
        this.sb.append((char)j);
        break;
      }
      throw exception("newline in quoted string");
    }
    return this.current.set(i, this.sb);
  }

  public InetAddress getAddress(int paramInt)
    throws IOException
  {
    String str = _getIdentifier("an address");
    try
    {
      InetAddress localInetAddress = Address.getByAddress(str, paramInt);
      return localInetAddress;
    }
    catch (UnknownHostException localUnknownHostException)
    {
      throw exception(localUnknownHostException.getMessage());
    }
  }

  public byte[] getBase32String(base32 parambase32)
    throws IOException
  {
    byte[] arrayOfByte = parambase32.fromString(_getIdentifier("a base32 string"));
    if (arrayOfByte == null)
      throw exception("invalid base32 encoding");
    return arrayOfByte;
  }

  public byte[] getBase64()
    throws IOException
  {
    return getBase64(false);
  }

  public byte[] getBase64(boolean paramBoolean)
    throws IOException
  {
    String str = remainingStrings();
    byte[] arrayOfByte;
    if (str == null)
    {
      if (paramBoolean)
        throw exception("expected base64 encoded string");
      arrayOfByte = null;
    }
    do
    {
      return arrayOfByte;
      arrayOfByte = base64.fromString(str);
    }
    while (arrayOfByte != null);
    throw exception("invalid base64 encoding");
  }

  public void getEOL()
    throws IOException
  {
    Token localToken = get();
    if ((localToken.type != 1) && (localToken.type != 0))
      throw exception("expected EOL or EOF");
  }

  public byte[] getHex()
    throws IOException
  {
    return getHex(false);
  }

  public byte[] getHex(boolean paramBoolean)
    throws IOException
  {
    String str = remainingStrings();
    byte[] arrayOfByte;
    if (str == null)
    {
      if (paramBoolean)
        throw exception("expected hex encoded string");
      arrayOfByte = null;
    }
    do
    {
      return arrayOfByte;
      arrayOfByte = base16.fromString(str);
    }
    while (arrayOfByte != null);
    throw exception("invalid hex encoding");
  }

  public byte[] getHexString()
    throws IOException
  {
    byte[] arrayOfByte = base16.fromString(_getIdentifier("a hex string"));
    if (arrayOfByte == null)
      throw exception("invalid hex encoding");
    return arrayOfByte;
  }

  public String getIdentifier()
    throws IOException
  {
    return _getIdentifier("an identifier");
  }

  public long getLong()
    throws IOException
  {
    String str = _getIdentifier("an integer");
    if (!Character.isDigit(str.charAt(0)))
      throw exception("expected an integer");
    try
    {
      long l = Long.parseLong(str);
      return l;
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    throw exception("expected an integer");
  }

  public Name getName(Name paramName)
    throws IOException
  {
    String str = _getIdentifier("a name");
    Name localName;
    try
    {
      localName = Name.fromString(str, paramName);
      if (!localName.isAbsolute())
        throw new RelativeNameException(localName);
    }
    catch (TextParseException localTextParseException)
    {
      throw exception(localTextParseException.getMessage());
    }
    return localName;
  }

  public String getString()
    throws IOException
  {
    Token localToken = get();
    if (!localToken.isString())
      throw exception("expected a string");
    return localToken.value;
  }

  public long getTTL()
    throws IOException
  {
    String str = _getIdentifier("a TTL value");
    try
    {
      long l = TTL.parseTTL(str);
      return l;
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    throw exception("expected a TTL value");
  }

  public long getTTLLike()
    throws IOException
  {
    String str = _getIdentifier("a TTL-like value");
    try
    {
      long l = TTL.parse(str, false);
      return l;
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    throw exception("expected a TTL-like value");
  }

  public int getUInt16()
    throws IOException
  {
    long l = getLong();
    if ((l < 0L) || (l > 65535L))
      throw exception("expected an 16 bit unsigned integer");
    return (int)l;
  }

  public long getUInt32()
    throws IOException
  {
    long l = getLong();
    if ((l < 0L) || (l > 4294967295L))
      throw exception("expected an 32 bit unsigned integer");
    return l;
  }

  public int getUInt8()
    throws IOException
  {
    long l = getLong();
    if ((l < 0L) || (l > 255L))
      throw exception("expected an 8 bit unsigned integer");
    return (int)l;
  }

  public void unget()
  {
    if (this.ungottenToken)
      throw new IllegalStateException("Cannot unget multiple tokens");
    if (this.current.type == 1)
      this.line = (-1 + this.line);
    this.ungottenToken = true;
  }

  public static class Token
  {
    public int type = -1;
    public String value = null;

    private Token()
    {
    }

    Token(Tokenizer.1 param1)
    {
      this();
    }

    private Token set(int paramInt, StringBuffer paramStringBuffer)
    {
      if (paramInt < 0)
        throw new IllegalArgumentException();
      this.type = paramInt;
      if (paramStringBuffer == null);
      for (String str = null; ; str = paramStringBuffer.toString())
      {
        this.value = str;
        return this;
      }
    }

    public boolean isEOL()
    {
      return (this.type == 1) || (this.type == 0);
    }

    public boolean isString()
    {
      return (this.type == 3) || (this.type == 4);
    }

    public String toString()
    {
      switch (this.type)
      {
      default:
        return "<unknown>";
      case 0:
        return "<eof>";
      case 1:
        return "<eol>";
      case 2:
        return "<whitespace>";
      case 3:
        return "<identifier: " + this.value + ">";
      case 4:
        return "<quoted_string: " + this.value + ">";
      case 5:
      }
      return "<comment: " + this.value + ">";
    }
  }

  static class TokenizerException extends TextParseException
  {
    String message;

    public TokenizerException(String paramString1, int paramInt, String paramString2)
    {
      super();
      this.message = paramString2;
    }

    public String getBaseMessage()
    {
      return this.message;
    }
  }
}