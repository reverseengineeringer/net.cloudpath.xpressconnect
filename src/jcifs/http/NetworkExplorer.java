package jcifs.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.ListIterator;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jcifs.Config;
import jcifs.UniAddress;
import jcifs.netbios.NbtAddress;
import jcifs.smb.DfsReferral;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbSession;
import jcifs.util.Base64;
import jcifs.util.LogStream;
import jcifs.util.MimeMap;

public class NetworkExplorer extends HttpServlet
{
  private static LogStream log = LogStream.getInstance();
  private boolean credentialsSupplied;
  private String defaultDomain;
  private boolean enableBasic;
  private boolean insecureBasic;
  private MimeMap mimeMap;
  private NtlmSsp ntlmSsp;
  private String realm;
  private String style;

  private String parseServerAndShare(String paramString)
  {
    char[] arrayOfChar = new char[256];
    if (paramString == null);
    int i;
    int j;
    do
    {
      return null;
      i = paramString.length();
      for (j = 0; (j < i) && (paramString.charAt(j) == '/'); j++);
    }
    while (j == i);
    int i5;
    for (int k = 0; j < i; k = i5)
    {
      int i4 = paramString.charAt(j);
      if (i4 == 47)
        break;
      i5 = k + 1;
      arrayOfChar[k] = i4;
      j++;
    }
    while ((j < i) && (paramString.charAt(j) == '/'))
      j++;
    int n;
    int i1;
    int i2;
    if (j < i)
    {
      n = k + 1;
      arrayOfChar[k] = '/';
      i1 = n + 1;
      i2 = j + 1;
      int i3 = paramString.charAt(j);
      arrayOfChar[n] = i3;
      if ((i2 < i) && (i3 != 47));
    }
    for (int m = i1; ; m = k)
    {
      return new String(arrayOfChar, 0, m);
      n = i1;
      j = i2;
      break;
    }
  }

  protected int compareDates(SmbFile paramSmbFile1, String paramString, SmbFile paramSmbFile2)
    throws IOException
  {
    if (paramSmbFile1.isDirectory() != paramSmbFile2.isDirectory())
      if (!paramSmbFile1.isDirectory());
    do
    {
      return -1;
      return 1;
      if (paramSmbFile1.isDirectory())
        return paramString.compareToIgnoreCase(paramSmbFile2.getName());
    }
    while (paramSmbFile1.lastModified() > paramSmbFile2.lastModified());
    return 1;
  }

  protected int compareNames(SmbFile paramSmbFile1, String paramString, SmbFile paramSmbFile2)
    throws IOException
  {
    if (paramSmbFile1.isDirectory() != paramSmbFile2.isDirectory())
    {
      if (paramSmbFile1.isDirectory())
        return -1;
      return 1;
    }
    return paramString.compareToIgnoreCase(paramSmbFile2.getName());
  }

  protected int compareSizes(SmbFile paramSmbFile1, String paramString, SmbFile paramSmbFile2)
    throws IOException
  {
    if (paramSmbFile1.isDirectory() != paramSmbFile2.isDirectory())
      if (!paramSmbFile1.isDirectory());
    long l;
    do
    {
      return -1;
      return 1;
      if (paramSmbFile1.isDirectory())
        return paramString.compareToIgnoreCase(paramSmbFile2.getName());
      l = paramSmbFile1.length() - paramSmbFile2.length();
      if (l == 0L)
        return paramString.compareToIgnoreCase(paramSmbFile2.getName());
    }
    while (l > 0L);
    return 1;
  }

  protected int compareTypes(SmbFile paramSmbFile1, String paramString, SmbFile paramSmbFile2)
    throws IOException
  {
    if (paramSmbFile1.isDirectory() != paramSmbFile2.isDirectory())
    {
      if (paramSmbFile1.isDirectory())
        return -1;
      return 1;
    }
    String str1 = paramSmbFile2.getName();
    if (paramSmbFile1.isDirectory())
      return paramString.compareToIgnoreCase(str1);
    int i = paramString.lastIndexOf('.');
    String str2;
    int j;
    if (i == -1)
    {
      str2 = "";
      j = str1.lastIndexOf('.');
      if (j != -1)
        break label113;
    }
    int k;
    label113: for (String str3 = ""; ; str3 = str1.substring(j + 1))
    {
      k = str2.compareToIgnoreCase(str3);
      if (k != 0)
        break label127;
      return paramString.compareToIgnoreCase(str1);
      str2 = paramString.substring(i + 1);
      break;
    }
    label127: return k;
  }

  protected void doDirectory(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, SmbFile paramSmbFile)
    throws IOException
  {
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("MM/d/yy h:mm a");
    localSimpleDateFormat.setCalendar(new GregorianCalendar());
    SmbFile[] arrayOfSmbFile = paramSmbFile.listFiles();
    if (LogStream.level > 2)
      log.println(arrayOfSmbFile.length + " items listed");
    LinkedList localLinkedList = new LinkedList();
    String str1 = paramHttpServletRequest.getParameter("fmt");
    if (str1 == null)
      str1 = "col";
    String str2 = paramHttpServletRequest.getParameter("sort");
    int i;
    int j;
    int k;
    int m;
    int n;
    if ((str2 == null) || (str2.equals("name")))
    {
      i = 0;
      j = 0;
      k = 0;
      m = 28;
      n = 0;
      label136: if (n >= arrayOfSmbFile.length);
    }
    else
    {
      while (true)
      {
        String str6;
        ListIterator localListIterator2;
        int i4;
        try
        {
          int i5 = arrayOfSmbFile[n].getType();
          if (i5 == 16)
          {
            n++;
            break label136;
            if (str2.equals("size"))
            {
              i = 1;
              break;
            }
            if (str2.equals("type"))
            {
              i = 2;
              break;
            }
            boolean bool = str2.equals("date");
            i = 0;
            if (!bool)
              break;
            i = 3;
          }
        }
        catch (SmbAuthException localSmbAuthException)
        {
          if (LogStream.level > 2)
            localSmbAuthException.printStackTrace(log);
          if (arrayOfSmbFile[n].isDirectory())
          {
            k++;
            str6 = arrayOfSmbFile[n].getName();
            if (LogStream.level > 3)
              log.println(n + ": " + str6);
            int i3 = str6.length();
            if (i3 > m)
              m = i3;
            localListIterator2 = localLinkedList.listIterator();
            i4 = 0;
            if (localListIterator2.hasNext())
            {
              if (i != 0)
                break label431;
              if (compareNames(arrayOfSmbFile[n], str6, (SmbFile)localListIterator2.next()) >= 0)
                break label461;
            }
            localLinkedList.add(i4, arrayOfSmbFile[n]);
            continue;
          }
        }
        catch (SmbException localSmbException)
        {
          label376: if (LogStream.level > 2)
            localSmbException.printStackTrace(log);
          if (localSmbException.getNtStatus() == -1073741823)
            continue;
          throw localSmbException;
        }
        j++;
        continue;
        label431: if (i == 1)
        {
          if (compareSizes(arrayOfSmbFile[n], str6, (SmbFile)localListIterator2.next()) < 0);
        }
        else
          label461: label500: 
          do
          {
            do
            {
              i4++;
              break;
              if (i != 2)
                break label500;
            }
            while (compareTypes(arrayOfSmbFile[n], str6, (SmbFile)localListIterator2.next()) >= 0);
            break label376;
          }
          while ((i != 3) || (compareDates(arrayOfSmbFile[n], str6, (SmbFile)localListIterator2.next()) >= 0));
      }
    }
    if (m > 50)
      m = 50;
    int i1 = m * 9;
    PrintWriter localPrintWriter = paramHttpServletResponse.getWriter();
    paramHttpServletResponse.setContentType("text/html");
    localPrintWriter.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
    localPrintWriter.println("<html><head><title>Network Explorer</title>");
    localPrintWriter.println("<meta HTTP-EQUIV=\"Pragma\" CONTENT=\"no-cache\">");
    localPrintWriter.println("<style TYPE=\"text/css\">");
    localPrintWriter.println(this.style);
    if (arrayOfSmbFile.length < 200)
    {
      localPrintWriter.println("    a:hover {");
      localPrintWriter.println("        background: #a2ff01;");
      localPrintWriter.println("    }");
    }
    localPrintWriter.println("</STYLE>");
    localPrintWriter.println("</head><body>");
    localPrintWriter.print("<a class=\"sort\" style=\"width: " + i1 + ";\" href=\"?fmt=detail&sort=name\">Name</a>");
    localPrintWriter.println("<a class=\"sort\" href=\"?fmt=detail&sort=size\">Size</a>");
    localPrintWriter.println("<a class=\"sort\" href=\"?fmt=detail&sort=type\">Type</a>");
    localPrintWriter.println("<a class=\"sort\" style=\"width: 180\" href=\"?fmt=detail&sort=date\">Modified</a><br clear='all'><p>");
    String str3 = paramSmbFile.getCanonicalPath();
    String str4;
    ListIterator localListIterator1;
    if (str3.length() < 7)
    {
      localPrintWriter.println("<b><big>smb://</big></b><br>");
      str4 = ".";
      localPrintWriter.println(k + j + " objects (" + k + " directories, " + j + " files)<br>");
      localPrintWriter.println("<b><a class=\"plain\" href=\".\">normal</a> | <a class=\"plain\" href=\"?fmt=detail\">detailed</a></b>");
      localPrintWriter.println("<p><table border='0' cellspacing='0' cellpadding='0'><tr><td>");
      localPrintWriter.print("<A style=\"width: " + i1);
      localPrintWriter.print("; height: 18;\" HREF=\"");
      localPrintWriter.print(str4);
      localPrintWriter.println("\"><b>&uarr;</b></a>");
      if (str1.equals("detail"))
        localPrintWriter.println("<br clear='all'>");
      if ((str4.length() == 1) || (paramSmbFile.getType() != 2))
        str4 = "";
      localListIterator1 = localLinkedList.listIterator();
    }
    while (true)
    {
      if (!localListIterator1.hasNext())
        break label1429;
      SmbFile localSmbFile = (SmbFile)localListIterator1.next();
      String str5 = localSmbFile.getName();
      if (str1.equals("detail"))
      {
        localPrintWriter.print("<A style=\"width: " + i1);
        localPrintWriter.print("; height: 18;\" HREF=\"");
        localPrintWriter.print(str4);
        localPrintWriter.print(str5);
        if (localSmbFile.isDirectory())
        {
          localPrintWriter.print("?fmt=detail\"><b>");
          localPrintWriter.print(str5);
          localPrintWriter.print("</b></a>");
          localPrintWriter.println("<br clear='all'>");
          continue;
          localPrintWriter.println("<b><big>" + str3 + "</big></b><br>");
          str4 = "../";
          break;
        }
        localPrintWriter.print("\"><b>");
        localPrintWriter.print(str5);
        localPrintWriter.print("</b></a><div align='right'>");
        localPrintWriter.print(localSmbFile.length() / 1024L + " KB </div><div>");
        int i2 = 1 + str5.lastIndexOf('.');
        if ((i2 > 1) && (str5.length() - i2 < 6))
          localPrintWriter.print(str5.substring(i2).toUpperCase() + "</div class='ext'>");
        while (true)
        {
          localPrintWriter.print("<div style='width: 180'>");
          localPrintWriter.print(localSimpleDateFormat.format(new Date(localSmbFile.lastModified())));
          localPrintWriter.print("</div>");
          break;
          localPrintWriter.print("&nbsp;</div>");
        }
      }
      localPrintWriter.print("<A style=\"width: " + i1);
      if (localSmbFile.isDirectory())
      {
        localPrintWriter.print("; height: 18;\" HREF=\"");
        localPrintWriter.print(str4);
        localPrintWriter.print(str5);
        localPrintWriter.print("\"><b>");
        localPrintWriter.print(str5);
        localPrintWriter.print("</b></a>");
      }
      else
      {
        localPrintWriter.print(";\" HREF=\"");
        localPrintWriter.print(str4);
        localPrintWriter.print(str5);
        localPrintWriter.print("\"><b>");
        localPrintWriter.print(str5);
        localPrintWriter.print("</b><br><small>");
        localPrintWriter.print(localSmbFile.length() / 1024L + "KB <br>");
        localPrintWriter.print(localSimpleDateFormat.format(new Date(localSmbFile.lastModified())));
        localPrintWriter.print("</small>");
        localPrintWriter.println("</a>");
      }
    }
    label1429: localPrintWriter.println("</td></tr></table>");
    localPrintWriter.println("</BODY></HTML>");
    localPrintWriter.close();
  }

  protected void doFile(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, SmbFile paramSmbFile)
    throws IOException
  {
    byte[] arrayOfByte = new byte[8192];
    SmbFileInputStream localSmbFileInputStream = new SmbFileInputStream(paramSmbFile);
    ServletOutputStream localServletOutputStream = paramHttpServletResponse.getOutputStream();
    String str1 = paramSmbFile.getPath();
    paramHttpServletResponse.setContentType("text/plain");
    int i = str1.lastIndexOf('.');
    if (i > 0)
    {
      String str2 = str1.substring(i + 1);
      if ((str2 != null) && (str2.length() > 1) && (str2.length() < 6))
        paramHttpServletResponse.setContentType(this.mimeMap.getMimeType(str2));
    }
    paramHttpServletResponse.setHeader("Content-Length", paramSmbFile.length() + "");
    paramHttpServletResponse.setHeader("Accept-Ranges", "Bytes");
    while (true)
    {
      int j = localSmbFileInputStream.read(arrayOfByte);
      if (j == -1)
        break;
      localServletOutputStream.write(arrayOfByte, 0, j);
    }
  }

  public void doGet(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
    throws IOException, ServletException
  {
    boolean bool1 = true;
    HttpSession localHttpSession = paramHttpServletRequest.getSession(false);
    String str1 = paramHttpServletRequest.getPathInfo();
    String str2 = null;
    if (str1 != null)
    {
      str2 = parseServerAndShare(str1);
      if (str2 != null)
      {
        int m = str2.indexOf('/');
        if (m > 0)
        {
          str2 = str2.substring(0, m).toLowerCase();
          bool1 = false;
        }
      }
    }
    String str3 = paramHttpServletRequest.getHeader("Authorization");
    int i;
    if ((this.enableBasic) && ((this.insecureBasic) || (paramHttpServletRequest.isSecure())))
    {
      i = 1;
      if ((str3 == null) || ((!str3.startsWith("NTLM ")) && ((i == 0) || (!str3.startsWith("Basic ")))))
        break label526;
      if (!str3.startsWith("NTLM "))
        break label217;
      if ((str1 != null) && (str2 != null))
        break label206;
    }
    NtlmPasswordAuthentication localNtlmPasswordAuthentication;
    label206: for (UniAddress localUniAddress = UniAddress.getByName(NbtAddress.getByName("\001\002__MSBROWSE__\002", 1, null).getHostAddress()); ; localUniAddress = UniAddress.getByName(str2, bool1))
    {
      paramHttpServletRequest.getSession();
      localNtlmPasswordAuthentication = NtlmSsp.authenticate(paramHttpServletRequest, paramHttpServletResponse, SmbSession.getChallenge(localUniAddress));
      if (localNtlmPasswordAuthentication != null)
        break label353;
      return;
      i = 0;
      break;
    }
    label217: String str5 = new String(Base64.decode(str3.substring(6)), "US-ASCII");
    int j = str5.indexOf(':');
    String str6;
    if (j != -1)
      str6 = str5.substring(0, j);
    while (true)
    {
      String str7;
      label281: String str8;
      if (j != -1)
      {
        str7 = str5.substring(j + 1);
        int k = str6.indexOf('\\');
        if (k == -1)
          k = str6.indexOf('/');
        if (k == -1)
          break label517;
        str8 = str6.substring(0, k);
        label321: if (k != -1)
          str6 = str6.substring(k + 1);
        localNtlmPasswordAuthentication = new NtlmPasswordAuthentication(str8, str6, str7);
        label353: paramHttpServletRequest.getSession().setAttribute("npa-" + str2, localNtlmPasswordAuthentication);
        label387: if (localNtlmPasswordAuthentication == null)
          break label669;
      }
      try
      {
        localSmbFile = new SmbFile("smb:/" + str1, localNtlmPasswordAuthentication);
        if (localSmbFile.isDirectory())
        {
          doDirectory(paramHttpServletRequest, paramHttpServletResponse, localSmbFile);
          return;
        }
      }
      catch (SmbAuthException localSmbAuthException)
      {
        SmbFile localSmbFile;
        while (true)
        {
          if (localHttpSession != null)
            localHttpSession.removeAttribute("npa-" + str2);
          if (localSmbAuthException.getNtStatus() != -1073741819)
            break label860;
          paramHttpServletResponse.sendRedirect(paramHttpServletRequest.getRequestURL().toString());
          return;
          str6 = str5;
          break;
          str7 = "";
          break label281;
          str8 = this.defaultDomain;
          break label321;
          boolean bool2 = this.credentialsSupplied;
          localNtlmPasswordAuthentication = null;
          if (bool2)
            break label387;
          localNtlmPasswordAuthentication = null;
          if (localHttpSession != null)
            localNtlmPasswordAuthentication = (NtlmPasswordAuthentication)localHttpSession.getAttribute("npa-" + str2);
          if (localNtlmPasswordAuthentication != null)
            break label387;
          paramHttpServletResponse.setHeader("WWW-Authenticate", "NTLM");
          if (i != 0)
            paramHttpServletResponse.addHeader("WWW-Authenticate", "Basic realm=\"" + this.realm + "\"");
          paramHttpServletResponse.setHeader("Connection", "close");
          paramHttpServletResponse.setStatus(401);
          paramHttpServletResponse.flushBuffer();
          return;
          if (str2 == null)
            localSmbFile = new SmbFile("smb://");
          else
            localSmbFile = new SmbFile("smb:/" + str1);
        }
        doFile(paramHttpServletRequest, paramHttpServletResponse, localSmbFile);
        return;
      }
      catch (DfsReferral localDfsReferral)
      {
        label517: label526: label669: StringBuffer localStringBuffer1 = paramHttpServletRequest.getRequestURL();
        String str4 = paramHttpServletRequest.getQueryString();
        StringBuffer localStringBuffer2 = new StringBuffer(localStringBuffer1.substring(0, localStringBuffer1.length() - paramHttpServletRequest.getPathInfo().length()));
        localStringBuffer2.append('/');
        localStringBuffer2.append(localDfsReferral.server);
        localStringBuffer2.append('/');
        localStringBuffer2.append(localDfsReferral.share);
        localStringBuffer2.append('/');
        if (str4 != null)
          localStringBuffer2.append(paramHttpServletRequest.getQueryString());
        paramHttpServletResponse.sendRedirect(localStringBuffer2.toString());
        paramHttpServletResponse.flushBuffer();
        return;
      }
    }
    label860: paramHttpServletResponse.setHeader("WWW-Authenticate", "NTLM");
    if (i != 0)
      paramHttpServletResponse.addHeader("WWW-Authenticate", "Basic realm=\"" + this.realm + "\"");
    paramHttpServletResponse.setHeader("Connection", "close");
    paramHttpServletResponse.setStatus(401);
    paramHttpServletResponse.flushBuffer();
  }

  public void init()
    throws ServletException
  {
    StringBuffer localStringBuffer = new StringBuffer();
    byte[] arrayOfByte = new byte[1024];
    Config.setProperty("jcifs.smb.client.soTimeout", "600000");
    Config.setProperty("jcifs.smb.client.attrExpirationPeriod", "300000");
    Enumeration localEnumeration = getInitParameterNames();
    while (localEnumeration.hasMoreElements())
    {
      String str = (String)localEnumeration.nextElement();
      if (str.startsWith("jcifs."))
        Config.setProperty(str, getInitParameter(str));
    }
    if (Config.getProperty("jcifs.smb.client.username") == null)
      this.ntlmSsp = new NtlmSsp();
    while (true)
    {
      try
      {
        this.mimeMap = new MimeMap();
        InputStream localInputStream = getClass().getClassLoader().getResourceAsStream("jcifs/http/ne.css");
        int i = localInputStream.read(arrayOfByte);
        if (i == -1)
          break;
        localStringBuffer.append(new String(arrayOfByte, 0, i, "ISO8859_1"));
        continue;
      }
      catch (IOException localIOException1)
      {
        throw new ServletException(localIOException1.getMessage());
      }
      this.credentialsSupplied = true;
    }
    this.style = localStringBuffer.toString();
    this.enableBasic = Config.getBoolean("jcifs.http.enableBasic", false);
    this.insecureBasic = Config.getBoolean("jcifs.http.insecureBasic", false);
    this.realm = Config.getProperty("jcifs.http.basicRealm");
    if (this.realm == null)
      this.realm = "jCIFS";
    this.defaultDomain = Config.getProperty("jcifs.smb.client.domain");
    int j = Config.getInt("jcifs.util.loglevel", -1);
    if (j != -1)
      LogStream.setLevel(j);
    if (LogStream.level > 2);
    try
    {
      Config.store(log, "JCIFS PROPERTIES");
      return;
    }
    catch (IOException localIOException2)
    {
    }
  }
}