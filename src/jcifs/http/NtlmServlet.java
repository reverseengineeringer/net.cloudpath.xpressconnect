package jcifs.http;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jcifs.Config;
import jcifs.UniAddress;
import jcifs.netbios.NbtAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbAuthException;
import jcifs.smb.SmbSession;
import jcifs.util.Base64;

public abstract class NtlmServlet extends HttpServlet
{
  private String defaultDomain;
  private String domainController;
  private boolean enableBasic;
  private boolean insecureBasic;
  private boolean loadBalance;
  private String realm;

  public void init(ServletConfig paramServletConfig)
    throws ServletException
  {
    super.init(paramServletConfig);
    Config.setProperty("jcifs.smb.client.soTimeout", "300000");
    Config.setProperty("jcifs.netbios.cachePolicy", "600");
    Enumeration localEnumeration = paramServletConfig.getInitParameterNames();
    while (localEnumeration.hasMoreElements())
    {
      String str = (String)localEnumeration.nextElement();
      if (str.startsWith("jcifs."))
        Config.setProperty(str, paramServletConfig.getInitParameter(str));
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
  }

  protected void service(HttpServletRequest paramHttpServletRequest, HttpServletResponse paramHttpServletResponse)
    throws ServletException, IOException
  {
    int i;
    String str1;
    if ((this.enableBasic) && ((this.insecureBasic) || (paramHttpServletRequest.isSecure())))
    {
      i = 1;
      str1 = paramHttpServletRequest.getHeader("Authorization");
      if ((str1 == null) || ((!str1.startsWith("NTLM ")) && ((i == 0) || (!str1.startsWith("Basic ")))))
        break label432;
      if (!this.loadBalance)
        break label123;
    }
    NtlmPasswordAuthentication localNtlmPasswordAuthentication;
    label123: for (UniAddress localUniAddress = new UniAddress(NbtAddress.getByName(this.domainController, 28, null)); ; localUniAddress = UniAddress.getByName(this.domainController, true))
    {
      if (!str1.startsWith("NTLM "))
        break label136;
      localNtlmPasswordAuthentication = NtlmSsp.authenticate(paramHttpServletRequest, paramHttpServletResponse, SmbSession.getChallenge(localUniAddress));
      if (localNtlmPasswordAuthentication != null)
        break label271;
      return;
      i = 0;
      break;
    }
    label136: String str2 = new String(Base64.decode(str1.substring(6)), "US-ASCII");
    int j = str2.indexOf(':');
    String str3;
    String str4;
    label199: String str5;
    if (j != -1)
    {
      str3 = str2.substring(0, j);
      if (j == -1)
        break label339;
      str4 = str2.substring(j + 1);
      int k = str3.indexOf('\\');
      if (k == -1)
        k = str3.indexOf('/');
      if (k == -1)
        break label346;
      str5 = str3.substring(0, k);
      if (k != -1)
        str3 = str3.substring(k + 1);
      localNtlmPasswordAuthentication = new NtlmPasswordAuthentication(str5, str3, str4);
    }
    label271: label339: label346: HttpSession localHttpSession1;
    label432: 
    do
    {
      try
      {
        SmbSession.logon(localUniAddress, localNtlmPasswordAuthentication);
        HttpSession localHttpSession2 = paramHttpServletRequest.getSession();
        localHttpSession2.setAttribute("NtlmHttpAuth", localNtlmPasswordAuthentication);
        localHttpSession2.setAttribute("ntlmdomain", localNtlmPasswordAuthentication.getDomain());
        localHttpSession2.setAttribute("ntlmuser", localNtlmPasswordAuthentication.getUsername());
        super.service(paramHttpServletRequest, paramHttpServletResponse);
        return;
        str3 = str2;
        break;
        str4 = "";
        break label199;
        str5 = this.defaultDomain;
      }
      catch (SmbAuthException localSmbAuthException)
      {
        paramHttpServletResponse.setHeader("WWW-Authenticate", "NTLM");
        if (i != 0)
          paramHttpServletResponse.addHeader("WWW-Authenticate", "Basic realm=\"" + this.realm + "\"");
        paramHttpServletResponse.setHeader("Connection", "close");
        paramHttpServletResponse.setStatus(401);
        paramHttpServletResponse.flushBuffer();
        return;
      }
      localHttpSession1 = paramHttpServletRequest.getSession(false);
    }
    while ((localHttpSession1 != null) && (localHttpSession1.getAttribute("NtlmHttpAuth") != null));
    paramHttpServletResponse.setHeader("WWW-Authenticate", "NTLM");
    if (i != 0)
      paramHttpServletResponse.addHeader("WWW-Authenticate", "Basic realm=\"" + this.realm + "\"");
    paramHttpServletResponse.setStatus(401);
    paramHttpServletResponse.flushBuffer();
  }
}