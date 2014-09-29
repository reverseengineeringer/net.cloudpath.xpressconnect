package jcifs.smb;

public abstract class NtlmAuthenticator
{
  private static NtlmAuthenticator auth;
  private SmbAuthException sae;
  private String url;

  public static NtlmPasswordAuthentication requestNtlmPasswordAuthentication(String paramString, SmbAuthException paramSmbAuthException)
  {
    if (auth == null)
      return null;
    synchronized (auth)
    {
      auth.url = paramString;
      auth.sae = paramSmbAuthException;
      NtlmPasswordAuthentication localNtlmPasswordAuthentication = auth.getNtlmPasswordAuthentication();
      return localNtlmPasswordAuthentication;
    }
  }

  private void reset()
  {
    this.url = null;
    this.sae = null;
  }

  public static void setDefault(NtlmAuthenticator paramNtlmAuthenticator)
  {
    try
    {
      NtlmAuthenticator localNtlmAuthenticator = auth;
      if (localNtlmAuthenticator != null);
      while (true)
      {
        return;
        auth = paramNtlmAuthenticator;
      }
    }
    finally
    {
    }
  }

  protected NtlmPasswordAuthentication getNtlmPasswordAuthentication()
  {
    return null;
  }

  protected final SmbAuthException getRequestingException()
  {
    return this.sae;
  }

  protected final String getRequestingURL()
  {
    return this.url;
  }
}