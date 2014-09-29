package jcifs.http;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jcifs.Config;
import jcifs.UniAddress;
import jcifs.smb.NtlmChallenge;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbSession;
import jcifs.util.Base64;
import jcifs.util.Hexdump;
import jcifs.util.LogStream;

public class NtlmHttpFilter
  implements Filter
{
  private static LogStream log = LogStream.getInstance();
  private String defaultDomain;
  private String domainController;
  private boolean enableBasic;
  private boolean insecureBasic;
  private boolean loadBalance;
  private String realm;

  public void destroy()
  {
  }

  public void doFilter(ServletRequest paramServletRequest, ServletResponse paramServletResponse, FilterChain paramFilterChain)
    throws IOException, ServletException
  {
    HttpServletRequest localHttpServletRequest = (HttpServletRequest)paramServletRequest;
    NtlmPasswordAuthentication localNtlmPasswordAuthentication = negotiate(localHttpServletRequest, (HttpServletResponse)paramServletResponse, false);
    if (localNtlmPasswordAuthentication == null)
      return;
    paramFilterChain.doFilter(new NtlmHttpServletRequest(localHttpServletRequest, localNtlmPasswordAuthentication), paramServletResponse);
  }

  public FilterConfig getFilterConfig()
  {
    return null;
  }

  public void init(FilterConfig paramFilterConfig)
    throws ServletException
  {
    Config.setProperty("jcifs.smb.client.soTimeout", "300000");
    Config.setProperty("jcifs.netbios.cachePolicy", "1200");
    Enumeration localEnumeration = paramFilterConfig.getInitParameterNames();
    while (localEnumeration.hasMoreElements())
    {
      String str = (String)localEnumeration.nextElement();
      if (str.startsWith("jcifs."))
        Config.setProperty(str, paramFilterConfig.getInitParameter(str));
    }
    this.defaultDomain = Config.getProperty("jcifs.smb.client.domain");
    this.domainController = Config.getProperty("jcifs.http.domainController");
    if (this.domainController == null)
    {
      this.domainController = this.defaultDomain;
      this.loadBalance = Config.getBoolean("jcifs.http.loadBalance", true);
    }
    this.enableBasic = Boolean.valueOf(Config.getProperty("jcifs.http.enableBasic")).booleanValue();
    this.insecureBasic = Boolean.valueOf(Config.getProperty("jcifs.http.insecureBasic")).booleanValue();
    this.realm = Config.getProperty("jcifs.http.basicRealm");
    if (this.realm == null)
      this.realm = "jCIFS";
    int i = Config.getInt("jcifs.util.loglevel", -1);
    if (i != -1)
      LogStream.setLevel(i);
    if (LogStream.level > 2);
    try
    {
      Config.store(log, "JCIFS PROPERTIES");
      return;
    }
    catch (IOException localIOException)
    {
    }
  }

  protected NtlmPasswordAuthentication negotiate(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse, boolean paramBoolean)
    throws IOException, ServletException
  {
    String str1 = paramHttpServletRequest.getHeader("Authorization");
    int i;
    HttpSession localHttpSession3;
    NtlmChallenge localNtlmChallenge;
    UniAddress localUniAddress;
    if ((this.enableBasic) && ((this.insecureBasic) || (paramHttpServletRequest.isSecure())))
    {
      i = 1;
      if ((str1 == null) || ((!str1.startsWith("NTLM ")) && ((i == 0) || (!str1.startsWith("Basic ")))))
        break label621;
      if (!str1.startsWith("NTLM "))
        break label263;
      localHttpSession3 = paramHttpServletRequest.getSession();
      if (!this.loadBalance)
        break label162;
      localNtlmChallenge = (NtlmChallenge)localHttpSession3.getAttribute("NtlmHttpChal");
      if (localNtlmChallenge == null)
      {
        localNtlmChallenge = SmbSession.getChallengeForDomain();
        localHttpSession3.setAttribute("NtlmHttpChal", localNtlmChallenge);
      }
      localUniAddress = localNtlmChallenge.dc;
    }
    NtlmPasswordAuthentication localNtlmPasswordAuthentication;
    for (byte[] arrayOfByte = localNtlmChallenge.challenge; ; arrayOfByte = SmbSession.getChallenge(localUniAddress))
    {
      localNtlmPasswordAuthentication = NtlmSsp.authenticate(paramHttpServletRequest, paramHttpServletResponse, arrayOfByte);
      if (localNtlmPasswordAuthentication != null)
        break label182;
      return null;
      i = 0;
      break;
      label162: localUniAddress = UniAddress.getByName(this.domainController, true);
    }
    label182: localHttpSession3.removeAttribute("NtlmHttpChal");
    label263: label310: label327: label621: 
    do
    {
      do
      {
        try
        {
          SmbSession.logon(localUniAddress, localNtlmPasswordAuthentication);
          if (LogStream.level > 2)
            log.println("NtlmHttpFilter: " + localNtlmPasswordAuthentication + " successfully authenticated against " + localUniAddress);
          paramHttpServletRequest.getSession().setAttribute("NtlmHttpAuth", localNtlmPasswordAuthentication);
          return localNtlmPasswordAuthentication;
          String str2 = new String(Base64.decode(str1.substring(6)), "US-ASCII");
          int j = str2.indexOf(':');
          String str3;
          String str4;
          int k;
          if (j != -1)
          {
            str3 = str2.substring(0, j);
            if (j == -1)
              break label419;
            str4 = str2.substring(j + 1);
            k = str3.indexOf('\\');
            if (k == -1)
              k = str3.indexOf('/');
            if (k == -1)
              break label427;
          }
          for (String str5 = str3.substring(0, k); ; str5 = this.defaultDomain)
          {
            if (k != -1)
              str3 = str3.substring(k + 1);
            localNtlmPasswordAuthentication = new NtlmPasswordAuthentication(str5, str3, str4);
            localUniAddress = UniAddress.getByName(this.domainController, true);
            break;
            str3 = str2;
            break label310;
            str4 = "";
            break label327;
          }
        }
        catch (SmbAuthException localSmbAuthException)
        {
          if (LogStream.level > 1)
            log.println("NtlmHttpFilter: " + localNtlmPasswordAuthentication.getName() + ": 0x" + Hexdump.toHexString(localSmbAuthException.getNtStatus(), 8) + ": " + localSmbAuthException);
          if (localSmbAuthException.getNtStatus() == -1073741819)
          {
            HttpSession localHttpSession2 = paramHttpServletRequest.getSession(false);
            if (localHttpSession2 != null)
              localHttpSession2.removeAttribute("NtlmHttpAuth");
          }
          paramHttpServletResponse.setHeader("WWW-Authenticate", "NTLM");
          if (i != 0)
            paramHttpServletResponse.addHeader("WWW-Authenticate", "Basic realm=\"" + this.realm + "\"");
          paramHttpServletResponse.setStatus(401);
          paramHttpServletResponse.setContentLength(0);
          paramHttpServletResponse.flushBuffer();
          return null;
        }
        localNtlmPasswordAuthentication = null;
      }
      while (paramBoolean);
      HttpSession localHttpSession1 = paramHttpServletRequest.getSession(false);
      if (localHttpSession1 == null)
        break;
      localNtlmPasswordAuthentication = (NtlmPasswordAuthentication)localHttpSession1.getAttribute("NtlmHttpAuth");
    }
    while (localNtlmPasswordAuthentication != null);
    label419: label427: paramHttpServletResponse.setHeader("WWW-Authenticate", "NTLM");
    if (i != 0)
      paramHttpServletResponse.addHeader("WWW-Authenticate", "Basic realm=\"" + this.realm + "\"");
    paramHttpServletResponse.setStatus(401);
    paramHttpServletResponse.setContentLength(0);
    paramHttpServletResponse.flushBuffer();
    return null;
  }

  public void setFilterConfig(FilterConfig paramFilterConfig)
  {
    try
    {
      init(paramFilterConfig);
      return;
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
  }
}