package net.cloudpath.xpressconnect.remote;

import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.impl.auth.NTLMScheme;
import org.apache.http.params.HttpParams;

public class NTLMSchemeFactory
  implements AuthSchemeFactory
{
  public AuthScheme newInstance(HttpParams paramHttpParams)
  {
    return new NTLMScheme(new JCIFSEngine());
  }
}