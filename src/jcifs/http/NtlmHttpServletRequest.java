package jcifs.http;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

class NtlmHttpServletRequest extends HttpServletRequestWrapper
{
  Principal principal;

  NtlmHttpServletRequest(HttpServletRequest paramHttpServletRequest, Principal paramPrincipal)
  {
    super(paramHttpServletRequest);
    this.principal = paramPrincipal;
  }

  public String getAuthType()
  {
    return "NTLM";
  }

  public String getRemoteUser()
  {
    return this.principal.getName();
  }

  public Principal getUserPrincipal()
  {
    return this.principal;
  }
}