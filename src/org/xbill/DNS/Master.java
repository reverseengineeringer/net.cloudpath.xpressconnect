package org.xbill.DNS;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Master
{
  private int currentDClass;
  private long currentTTL;
  private int currentType;
  private long defaultTTL;
  private File file;
  private Generator generator;
  private List generators;
  private Master included = null;
  private Record last = null;
  private boolean needSOATTL;
  private boolean noExpandGenerate;
  private Name origin;
  private Tokenizer st;

  Master(File paramFile, Name paramName, long paramLong)
    throws IOException
  {
    if ((paramName != null) && (!paramName.isAbsolute()))
      throw new RelativeNameException(paramName);
    this.file = paramFile;
    this.st = new Tokenizer(paramFile);
    this.origin = paramName;
    this.defaultTTL = paramLong;
  }

  public Master(InputStream paramInputStream)
  {
    this(paramInputStream, null, -1L);
  }

  public Master(InputStream paramInputStream, Name paramName)
  {
    this(paramInputStream, paramName, -1L);
  }

  public Master(InputStream paramInputStream, Name paramName, long paramLong)
  {
    if ((paramName != null) && (!paramName.isAbsolute()))
      throw new RelativeNameException(paramName);
    this.st = new Tokenizer(paramInputStream);
    this.origin = paramName;
    this.defaultTTL = paramLong;
  }

  public Master(String paramString)
    throws IOException
  {
    this(new File(paramString), null, -1L);
  }

  public Master(String paramString, Name paramName)
    throws IOException
  {
    this(new File(paramString), paramName, -1L);
  }

  public Master(String paramString, Name paramName, long paramLong)
    throws IOException
  {
    this(new File(paramString), paramName, paramLong);
  }

  private void endGenerate()
    throws IOException
  {
    this.st.getEOL();
    this.generator = null;
  }

  private Record nextGenerated()
    throws IOException
  {
    try
    {
      Record localRecord = this.generator.nextRecord();
      return localRecord;
    }
    catch (Tokenizer.TokenizerException localTokenizerException)
    {
      throw this.st.exception("Parsing $GENERATE: " + localTokenizerException.getBaseMessage());
    }
    catch (TextParseException localTextParseException)
    {
      throw this.st.exception("Parsing $GENERATE: " + localTextParseException.getMessage());
    }
  }

  private Name parseName(String paramString, Name paramName)
    throws TextParseException
  {
    try
    {
      Name localName = Name.fromString(paramString, paramName);
      return localName;
    }
    catch (TextParseException localTextParseException)
    {
      throw this.st.exception(localTextParseException.getMessage());
    }
  }

  private void parseTTLClassAndType()
    throws IOException
  {
    Object localObject = this.st.getString();
    int i = DClass.value((String)localObject);
    this.currentDClass = i;
    int j = 0;
    if (i >= 0)
    {
      localObject = this.st.getString();
      j = 1;
    }
    this.currentTTL = -1L;
    try
    {
      this.currentTTL = TTL.parseTTL((String)localObject);
      String str = this.st.getString();
      localObject = str;
      if (j == 0)
      {
        int m = DClass.value((String)localObject);
        this.currentDClass = m;
        if (m >= 0)
          localObject = this.st.getString();
      }
      else
      {
        int k = Type.value((String)localObject);
        this.currentType = k;
        if (k >= 0)
          break label190;
        throw this.st.exception("Invalid type '" + (String)localObject + "'");
      }
    }
    catch (NumberFormatException localNumberFormatException)
    {
      while (true)
        if (this.defaultTTL >= 0L)
        {
          this.currentTTL = this.defaultTTL;
        }
        else if (this.last != null)
        {
          this.currentTTL = this.last.getTTL();
          continue;
          this.currentDClass = 1;
        }
      label190: if (this.currentTTL < 0L)
      {
        if (this.currentType != 6)
          throw this.st.exception("missing TTL");
        this.needSOATTL = true;
        this.currentTTL = 0L;
      }
    }
  }

  private long parseUInt32(String paramString)
  {
    long l2;
    if (!Character.isDigit(paramString.charAt(0)))
      l2 = -1L;
    while (true)
    {
      return l2;
      try
      {
        long l1 = Long.parseLong(paramString);
        l2 = l1;
        if ((l2 < 0L) || (l2 > 4294967295L))
          return -1L;
      }
      catch (NumberFormatException localNumberFormatException)
      {
      }
    }
    return -1L;
  }

  private void startGenerate()
    throws IOException
  {
    String str1 = this.st.getIdentifier();
    int i = str1.indexOf("-");
    if (i < 0)
      throw this.st.exception("Invalid $GENERATE range specifier: " + str1);
    String str2 = str1.substring(0, i);
    String str3 = str1.substring(i + 1);
    int j = str3.indexOf("/");
    String str4 = null;
    if (j >= 0)
    {
      int k = j + 1;
      str4 = str3.substring(k);
      str3 = str3.substring(0, j);
    }
    long l1 = parseUInt32(str2);
    long l2 = parseUInt32(str3);
    if (str4 != null);
    for (long l3 = parseUInt32(str4); (l1 < 0L) || (l2 < 0L) || (l1 > l2) || (l3 <= 0L); l3 = 1L)
      throw this.st.exception("Invalid $GENERATE range specifier: " + str1);
    String str5 = this.st.getIdentifier();
    parseTTLClassAndType();
    if (!Generator.supportedType(this.currentType))
      throw this.st.exception("$GENERATE does not support " + Type.string(this.currentType) + " records");
    String str6 = this.st.getIdentifier();
    this.st.getEOL();
    this.st.unget();
    this.generator = new Generator(l1, l2, l3, str5, this.currentType, this.currentDClass, this.currentTTL, str6, this.origin);
    if (this.generators == null)
      this.generators = new ArrayList(1);
    this.generators.add(this.generator);
  }

  public Record _nextRecord()
    throws IOException
  {
    Record localRecord;
    if (this.included != null)
    {
      localRecord = this.included.nextRecord();
      if (localRecord == null);
    }
    do
    {
      return localRecord;
      this.included = null;
      if (this.generator == null)
        break;
      localRecord = nextGenerated();
    }
    while (localRecord != null);
    endGenerate();
    Tokenizer.Token localToken1;
    Tokenizer.Token localToken3;
    do
    {
      localToken1 = this.st.get(true, false);
      if (localToken1.type != 2)
        break;
      localToken3 = this.st.get();
    }
    while (localToken3.type == 1);
    if (localToken3.type == 0)
      return null;
    this.st.unget();
    if (this.last == null)
      throw this.st.exception("no owner");
    for (Name localName1 = this.last.getName(); ; localName1 = this.last.getName())
      do
      {
        parseTTLClassAndType();
        this.last = Record.fromString(localName1, this.currentType, this.currentDClass, this.currentTTL, this.st, this.origin);
        if (this.needSOATTL)
        {
          long l = ((SOARecord)this.last).getMinimum();
          this.last.setTTL(l);
          this.defaultTTL = l;
          this.needSOATTL = false;
        }
        return this.last;
        if (localToken1.type == 1)
          break;
        if (localToken1.type == 0)
          return null;
        if (localToken1.value.charAt(0) == '$')
        {
          String str1 = localToken1.value;
          if (str1.equalsIgnoreCase("$ORIGIN"))
          {
            this.origin = this.st.getName(Name.root);
            this.st.getEOL();
            break;
          }
          if (str1.equalsIgnoreCase("$TTL"))
          {
            this.defaultTTL = this.st.getTTL();
            this.st.getEOL();
            break;
          }
          if (str1.equalsIgnoreCase("$INCLUDE"))
          {
            String str2 = this.st.getString();
            if (this.file != null);
            for (File localFile = new File(this.file.getParent(), str2); ; localFile = new File(str2))
            {
              Name localName2 = this.origin;
              Tokenizer.Token localToken2 = this.st.get();
              if (localToken2.isString())
              {
                localName2 = parseName(localToken2.value, Name.root);
                this.st.getEOL();
              }
              this.included = new Master(localFile, localName2, this.defaultTTL);
              return nextRecord();
            }
          }
          if (str1.equalsIgnoreCase("$GENERATE"))
          {
            if (this.generator != null)
              throw new IllegalStateException("cannot nest $GENERATE");
            startGenerate();
            if (this.noExpandGenerate)
            {
              endGenerate();
              break;
            }
            return nextGenerated();
          }
          throw this.st.exception("Invalid directive: " + str1);
        }
        localName1 = parseName(localToken1.value, this.origin);
      }
      while ((this.last == null) || (!localName1.equals(this.last.getName())));
  }

  public void expandGenerate(boolean paramBoolean)
  {
    if (!paramBoolean);
    for (boolean bool = true; ; bool = false)
    {
      this.noExpandGenerate = bool;
      return;
    }
  }

  protected void finalize()
  {
    this.st.close();
  }

  public Iterator generators()
  {
    if (this.generators != null)
      return Collections.unmodifiableList(this.generators).iterator();
    return Collections.EMPTY_LIST.iterator();
  }

  public Record nextRecord()
    throws IOException
  {
    try
    {
      Record localRecord = _nextRecord();
      if (localRecord == null)
        this.st.close();
      return localRecord;
    }
    finally
    {
      if (0 == 0)
        this.st.close();
    }
  }
}