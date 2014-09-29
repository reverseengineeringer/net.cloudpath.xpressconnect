import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.PrintStream;
import java.net.SocketException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.xbill.DNS.DClass;
import org.xbill.DNS.Header;
import org.xbill.DNS.Message;
import org.xbill.DNS.Name;
import org.xbill.DNS.Rcode;
import org.xbill.DNS.Record;
import org.xbill.DNS.Resolver;
import org.xbill.DNS.SOARecord;
import org.xbill.DNS.Section;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TSIG;
import org.xbill.DNS.TTL;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Tokenizer;
import org.xbill.DNS.Tokenizer.Token;
import org.xbill.DNS.Type;

public class update
{
  int defaultClass = 1;
  long defaultTTL;
  PrintStream log = null;
  Message query;
  Resolver res;
  Message response;
  String server = null;
  Name zone = Name.root;

  public update(InputStream paramInputStream)
    throws IOException
  {
    LinkedList localLinkedList1 = new LinkedList();
    LinkedList localLinkedList2 = new LinkedList();
    this.query = newMessage();
    localLinkedList1.add(new BufferedReader(new InputStreamReader(paramInputStream)));
    localLinkedList2.add(paramInputStream);
    while (true)
    {
      String str1;
      Tokenizer localTokenizer;
      String str2;
      try
      {
        InputStream localInputStream = (InputStream)localLinkedList2.get(0);
        BufferedReader localBufferedReader = (BufferedReader)localLinkedList1.get(0);
        if (localInputStream == System.in)
          System.out.print("> ");
        str1 = localBufferedReader.readLine();
        if (str1 == null)
        {
          localBufferedReader.close();
          localLinkedList1.remove(0);
          localLinkedList2.remove(0);
          if (localLinkedList1.isEmpty())
            return;
        }
        if (str1 == null)
          continue;
        if (this.log != null)
          this.log.println("> " + str1);
        if ((str1.length() == 0) || (str1.charAt(0) == '#'))
          continue;
        if (str1.charAt(0) == '>')
          str1 = str1.substring(1);
        localTokenizer = new Tokenizer(str1);
        Tokenizer.Token localToken1 = localTokenizer.get();
        if (localToken1.isEOL())
          continue;
        str2 = localToken1.value;
        if (str2.equals("server"))
        {
          this.server = localTokenizer.getString();
          this.res = new SimpleResolver(this.server);
          Tokenizer.Token localToken4 = localTokenizer.get();
          if (!localToken4.isString())
            continue;
          String str6 = localToken4.value;
          this.res.setPort(Short.parseShort(str6));
          continue;
        }
      }
      catch (TextParseException localTextParseException)
      {
        System.out.println(localTextParseException.getMessage());
        continue;
        if (str2.equals("key"))
        {
          String str4 = localTokenizer.getString();
          String str5 = localTokenizer.getString();
          if (this.res == null)
            this.res = new SimpleResolver(this.server);
          Resolver localResolver = this.res;
          TSIG localTSIG = new TSIG(str4, str5);
          localResolver.setTSIGKey(localTSIG);
          continue;
        }
      }
      catch (InterruptedIOException localInterruptedIOException)
      {
        System.out.println("Operation timed out");
        continue;
        if (str2.equals("edns"))
        {
          if (this.res == null)
            this.res = new SimpleResolver(this.server);
          this.res.setEDNS(localTokenizer.getUInt16());
          continue;
        }
      }
      catch (SocketException localSocketException)
      {
        System.out.println("Socket error");
        continue;
        if (!str2.equals("port"))
          break label576;
        if (this.res == null)
          this.res = new SimpleResolver(this.server);
        this.res.setPort(localTokenizer.getUInt16());
        continue;
      }
      catch (IOException localIOException)
      {
        System.out.println(localIOException);
      }
      continue;
      label576: if (str2.equals("tcp"))
      {
        if (this.res == null)
          this.res = new SimpleResolver(this.server);
        this.res.setTCP(true);
      }
      else if (str2.equals("class"))
      {
        String str3 = localTokenizer.getString();
        int i = DClass.value(str3);
        if (i > 0)
          this.defaultClass = i;
        else
          print("Invalid class " + str3);
      }
      else if (str2.equals("ttl"))
      {
        this.defaultTTL = localTokenizer.getTTL();
      }
      else if ((str2.equals("origin")) || (str2.equals("zone")))
      {
        this.zone = localTokenizer.getName(Name.root);
      }
      else if (str2.equals("require"))
      {
        doRequire(localTokenizer);
      }
      else if (str2.equals("prohibit"))
      {
        doProhibit(localTokenizer);
      }
      else if (str2.equals("add"))
      {
        doAdd(localTokenizer);
      }
      else if (str2.equals("delete"))
      {
        doDelete(localTokenizer);
      }
      else if (str2.equals("glue"))
      {
        doGlue(localTokenizer);
      }
      else if ((str2.equals("help")) || (str2.equals("?")))
      {
        Tokenizer.Token localToken2 = localTokenizer.get();
        if (localToken2.isString())
          help(localToken2.value);
        else
          help(null);
      }
      else if (str2.equals("echo"))
      {
        print(str1.substring(4).trim());
      }
      else if (str2.equals("send"))
      {
        sendUpdate();
        this.query = newMessage();
      }
      else if (str2.equals("show"))
      {
        print(this.query);
      }
      else if (str2.equals("clear"))
      {
        this.query = newMessage();
      }
      else if (str2.equals("query"))
      {
        doQuery(localTokenizer);
      }
      else if ((str2.equals("quit")) || (str2.equals("q")))
      {
        if (this.log != null)
          this.log.close();
        Iterator localIterator = localLinkedList1.iterator();
        while (localIterator.hasNext())
          ((BufferedReader)localIterator.next()).close();
        System.exit(0);
      }
      else if (str2.equals("file"))
      {
        doFile(localTokenizer, localLinkedList1, localLinkedList2);
      }
      else if (str2.equals("log"))
      {
        doLog(localTokenizer);
      }
      else if (str2.equals("assert"))
      {
        if (doAssert(localTokenizer));
      }
      else if (str2.equals("sleep"))
      {
        long l = localTokenizer.getUInt32();
        try
        {
          Thread.sleep(l);
        }
        catch (InterruptedException localInterruptedException)
        {
        }
      }
      else if (str2.equals("date"))
      {
        Date localDate = new Date();
        Tokenizer.Token localToken3 = localTokenizer.get();
        if ((localToken3.isString()) && (localToken3.value.equals("-ms")))
          print(Long.toString(localDate.getTime()));
        else
          print(localDate);
      }
      else
      {
        print("invalid keyword: " + str2);
      }
    }
  }

  static void help(String paramString)
  {
    System.out.println();
    if (paramString == null)
    {
      System.out.println("The following are supported commands:\nadd      assert   class    clear    date     delete\necho     edns     file     glue     help     key\nlog      port     prohibit query    quit     require\nsend     server   show     sleep    tcp      ttl\nzone     #\n");
      return;
    }
    String str = paramString.toLowerCase();
    if (str.equals("add"))
    {
      System.out.println("add <name> [ttl] [class] <type> <data>\n\nspecify a record to be added\n");
      return;
    }
    if (str.equals("assert"))
    {
      System.out.println("assert <field> <value> [msg]\n\nasserts that the value of the field in the last\nresponse matches the value specified.  If not,\nthe message is printed (if present) and the\nprogram exits.  The field may be any of <rcode>,\n<serial>, <tsig>, <qu>, <an>, <au>, or <ad>.\n");
      return;
    }
    if (str.equals("class"))
    {
      System.out.println("class <class>\n\nclass of the zone to be updated (default: IN)\n");
      return;
    }
    if (str.equals("clear"))
    {
      System.out.println("clear\n\nclears the current update packet\n");
      return;
    }
    if (str.equals("date"))
    {
      System.out.println("date [-ms]\n\nprints the current date and time in human readable\nformat or as the number of milliseconds since the\nepoch");
      return;
    }
    if (str.equals("delete"))
    {
      System.out.println("delete <name> [ttl] [class] <type> <data> \ndelete <name> <type> \ndelete <name>\n\nspecify a record or set to be deleted, or that\nall records at a name should be deleted\n");
      return;
    }
    if (str.equals("echo"))
    {
      System.out.println("echo <text>\n\nprints the text\n");
      return;
    }
    if (str.equals("edns"))
    {
      System.out.println("edns <level>\n\nEDNS level specified when sending messages\n");
      return;
    }
    if (str.equals("file"))
    {
      System.out.println("file <file>\n\nopens the specified file as the new input source\n(- represents stdin)\n");
      return;
    }
    if (str.equals("glue"))
    {
      System.out.println("glue <name> [ttl] [class] <type> <data>\n\nspecify an additional record\n");
      return;
    }
    if (str.equals("help"))
    {
      System.out.println("help\nhelp [topic]\n\nprints a list of commands or help about a specific\ncommand\n");
      return;
    }
    if (str.equals("key"))
    {
      System.out.println("key <name> <data>\n\nTSIG key used to sign messages\n");
      return;
    }
    if (str.equals("log"))
    {
      System.out.println("log <file>\n\nopens the specified file and uses it to log output\n");
      return;
    }
    if (str.equals("port"))
    {
      System.out.println("port <port>\n\nUDP/TCP port messages are sent to (default: 53)\n");
      return;
    }
    if (str.equals("prohibit"))
    {
      System.out.println("prohibit <name> <type> \nprohibit <name>\n\nrequire that a set or name is not present\n");
      return;
    }
    if (str.equals("query"))
    {
      System.out.println("query <name> [type [class]] \n\nissues a query\n");
      return;
    }
    if ((str.equals("q")) || (str.equals("quit")))
    {
      System.out.println("quit\n\nquits the program\n");
      return;
    }
    if (str.equals("require"))
    {
      System.out.println("require <name> [ttl] [class] <type> <data> \nrequire <name> <type> \nrequire <name>\n\nrequire that a record, set, or name is present\n");
      return;
    }
    if (str.equals("send"))
    {
      System.out.println("send\n\nsends and resets the current update packet\n");
      return;
    }
    if (str.equals("server"))
    {
      System.out.println("server <name> [port]\n\nserver that receives send updates/queries\n");
      return;
    }
    if (str.equals("show"))
    {
      System.out.println("show\n\nshows the current update packet\n");
      return;
    }
    if (str.equals("sleep"))
    {
      System.out.println("sleep <milliseconds>\n\npause for interval before next command\n");
      return;
    }
    if (str.equals("tcp"))
    {
      System.out.println("tcp\n\nTCP should be used to send all messages\n");
      return;
    }
    if (str.equals("ttl"))
    {
      System.out.println("ttl <ttl>\n\ndefault ttl of added records (default: 0)\n");
      return;
    }
    if ((str.equals("zone")) || (str.equals("origin")))
    {
      System.out.println("zone <zone>\n\nzone to update (default: .\n");
      return;
    }
    if (str.equals("#"))
    {
      System.out.println("# <text>\n\na comment\n");
      return;
    }
    System.out.println("Topic '" + str + "' unrecognized\n");
  }

  public static void main(String[] paramArrayOfString)
    throws IOException
  {
    if (paramArrayOfString.length >= 1);
    while (true)
    {
      try
      {
        FileInputStream localFileInputStream = new FileInputStream(paramArrayOfString[0]);
        localObject = localFileInputStream;
        new update((InputStream)localObject);
        return;
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        System.out.println(paramArrayOfString[0] + " not found.");
        System.exit(1);
        localObject = null;
        continue;
      }
      Object localObject = System.in;
    }
  }

  void doAdd(Tokenizer paramTokenizer)
    throws IOException
  {
    Record localRecord = parseRR(paramTokenizer, this.defaultClass, this.defaultTTL);
    this.query.addRecord(localRecord, 2);
    print(localRecord);
  }

  boolean doAssert(Tokenizer paramTokenizer)
    throws IOException
  {
    String str1 = paramTokenizer.getString();
    String str2 = paramTokenizer.getString();
    boolean bool1 = true;
    if (this.response == null)
    {
      print("No response has been received");
      return true;
    }
    String str3;
    if (str1.equalsIgnoreCase("rcode"))
    {
      int m = this.response.getHeader().getRcode();
      int n = Rcode.value(str2);
      str3 = null;
      if (m != n)
      {
        str3 = Rcode.string(m);
        bool1 = false;
      }
      if (!bool1)
        print("Expected " + str1 + " " + str2 + ", received " + str3);
    }
    while (true)
    {
      Tokenizer.Token localToken = paramTokenizer.get();
      if (!localToken.isString())
      {
        paramTokenizer.unget();
        return bool1;
        if (str1.equalsIgnoreCase("serial"))
        {
          Record[] arrayOfRecord = this.response.getSectionArray(1);
          if ((arrayOfRecord.length < 1) || (!(arrayOfRecord[0] instanceof SOARecord)))
          {
            print("Invalid response (no SOA)");
            str3 = null;
            break;
          }
          long l = ((SOARecord)arrayOfRecord[0]).getSerial();
          boolean bool2 = l < Long.parseLong(str2);
          str3 = null;
          if (!bool2)
            break;
          str3 = Long.toString(l);
          bool1 = false;
          break;
        }
        if (str1.equalsIgnoreCase("tsig"))
        {
          if (this.response.isSigned())
            if (this.response.isVerified())
              str3 = "ok";
          while (true)
          {
            if (str3.equalsIgnoreCase(str2))
              break label304;
            bool1 = false;
            break;
            str3 = "failed";
            continue;
            str3 = "unsigned";
          }
          label304: break;
        }
        int i = Section.value(str1);
        if (i >= 0)
        {
          int j = this.response.getHeader().getCount(i);
          int k = Integer.parseInt(str2);
          str3 = null;
          if (j == k)
            break;
          str3 = new Integer(j).toString();
          bool1 = false;
          break;
        }
        print("Invalid assertion keyword: " + str1);
        str3 = null;
        break;
      }
      print(localToken.value);
    }
  }

  void doDelete(Tokenizer paramTokenizer)
    throws IOException
  {
    Name localName = paramTokenizer.getName(this.zone);
    Tokenizer.Token localToken = paramTokenizer.get();
    int i;
    Record localRecord;
    if (localToken.isString())
    {
      String str = localToken.value;
      if (DClass.value(str) >= 0)
        str = paramTokenizer.getString();
      i = Type.value(str);
      if (i < 0)
        throw new IOException("Invalid type: " + str);
      boolean bool = paramTokenizer.get().isEOL();
      paramTokenizer.unget();
      if (!bool)
        localRecord = Record.fromString(localName, i, 254, 0L, paramTokenizer, this.zone);
    }
    while (true)
    {
      this.query.addRecord(localRecord, 2);
      print(localRecord);
      return;
      localRecord = Record.newRecord(localName, i, 255, 0L);
      continue;
      localRecord = Record.newRecord(localName, 255, 255, 0L);
    }
  }

  void doFile(Tokenizer paramTokenizer, List paramList1, List paramList2)
    throws IOException
  {
    String str = paramTokenizer.getString();
    try
    {
      if (str.equals("-"));
      for (Object localObject = System.in; ; localObject = new FileInputStream(str))
      {
        paramList2.add(0, localObject);
        paramList1.add(0, new BufferedReader(new InputStreamReader((InputStream)localObject)));
        return;
      }
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      print(str + " not found");
    }
  }

  void doGlue(Tokenizer paramTokenizer)
    throws IOException
  {
    Record localRecord = parseRR(paramTokenizer, this.defaultClass, this.defaultTTL);
    this.query.addRecord(localRecord, 3);
    print(localRecord);
  }

  void doLog(Tokenizer paramTokenizer)
    throws IOException
  {
    String str = paramTokenizer.getString();
    try
    {
      this.log = new PrintStream(new FileOutputStream(str));
      return;
    }
    catch (Exception localException)
    {
      print("Error opening " + str);
    }
  }

  void doProhibit(Tokenizer paramTokenizer)
    throws IOException
  {
    Name localName = paramTokenizer.getName(this.zone);
    Tokenizer.Token localToken = paramTokenizer.get();
    int i;
    if (localToken.isString())
    {
      i = Type.value(localToken.value);
      if (i < 0)
        throw new IOException("Invalid type: " + localToken.value);
    }
    else
    {
      i = 255;
    }
    Record localRecord = Record.newRecord(localName, i, 254, 0L);
    this.query.addRecord(localRecord, 1);
    print(localRecord);
  }

  void doQuery(Tokenizer paramTokenizer)
    throws IOException
  {
    int i = 1;
    int j = this.defaultClass;
    Name localName = paramTokenizer.getName(this.zone);
    Tokenizer.Token localToken1 = paramTokenizer.get();
    if (localToken1.isString())
    {
      i = Type.value(localToken1.value);
      if (i < 0)
        throw new IOException("Invalid type");
      Tokenizer.Token localToken2 = paramTokenizer.get();
      if (localToken2.isString())
      {
        j = DClass.value(localToken2.value);
        if (j < 0)
          throw new IOException("Invalid class");
      }
    }
    Message localMessage = Message.newQuery(Record.newRecord(localName, i, j));
    if (this.res == null)
      this.res = new SimpleResolver(this.server);
    this.response = this.res.send(localMessage);
    print(this.response);
  }

  void doRequire(Tokenizer paramTokenizer)
    throws IOException
  {
    Name localName = paramTokenizer.getName(this.zone);
    Tokenizer.Token localToken = paramTokenizer.get();
    int i;
    Record localRecord;
    if (localToken.isString())
    {
      i = Type.value(localToken.value);
      if (i < 0)
        throw new IOException("Invalid type: " + localToken.value);
      boolean bool = paramTokenizer.get().isEOL();
      paramTokenizer.unget();
      if (!bool)
        localRecord = Record.fromString(localName, i, this.defaultClass, 0L, paramTokenizer, this.zone);
    }
    while (true)
    {
      this.query.addRecord(localRecord, 1);
      print(localRecord);
      return;
      localRecord = Record.newRecord(localName, i, 255, 0L);
      continue;
      localRecord = Record.newRecord(localName, 255, 255, 0L);
    }
  }

  public Message newMessage()
  {
    Message localMessage = new Message();
    localMessage.getHeader().setOpcode(5);
    return localMessage;
  }

  Record parseRR(Tokenizer paramTokenizer, int paramInt, long paramLong)
    throws IOException
  {
    Name localName1 = paramTokenizer.getName(this.zone);
    Object localObject = paramTokenizer.getString();
    try
    {
      l = TTL.parseTTL((String)localObject);
      String str = paramTokenizer.getString();
      localObject = str;
      if (DClass.value((String)localObject) >= 0)
      {
        paramInt = DClass.value((String)localObject);
        localObject = paramTokenizer.getString();
      }
      i = Type.value((String)localObject);
      if (i < 0)
        throw new IOException("Invalid type: " + (String)localObject);
    }
    catch (NumberFormatException localNumberFormatException)
    {
      long l;
      int i;
      while (true)
        l = paramLong;
      Name localName2 = this.zone;
      Record localRecord = Record.fromString(localName1, i, paramInt, l, paramTokenizer, localName2);
      if (localRecord != null)
        return localRecord;
    }
    throw new IOException("Parse error");
  }

  void print(Object paramObject)
  {
    System.out.println(paramObject);
    if (this.log != null)
      this.log.println(paramObject);
  }

  void sendUpdate()
    throws IOException
  {
    if (this.query.getHeader().getCount(2) == 0)
    {
      print("Empty update message.  Ignoring.");
      return;
    }
    Name localName;
    int i;
    Record[] arrayOfRecord;
    if (this.query.getHeader().getCount(0) == 0)
    {
      localName = this.zone;
      i = this.defaultClass;
      if (localName == null)
        arrayOfRecord = this.query.getSectionArray(2);
    }
    for (int j = 0; ; j++)
      if (j < arrayOfRecord.length)
      {
        if (localName == null)
          localName = new Name(arrayOfRecord[j].getName(), 1);
        if ((arrayOfRecord[j].getDClass() != 254) && (arrayOfRecord[j].getDClass() != 255))
          i = arrayOfRecord[j].getDClass();
      }
      else
      {
        Record localRecord = Record.newRecord(localName, 6, i);
        this.query.addRecord(localRecord, 0);
        if (this.res == null)
          this.res = new SimpleResolver(this.server);
        this.response = this.res.send(this.query);
        print(this.response);
        return;
      }
  }
}