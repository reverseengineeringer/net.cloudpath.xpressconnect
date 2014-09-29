package org.bouncycastle2.x509;

import java.io.InputStream;
import java.util.Collection;
import org.bouncycastle2.x509.util.StreamParsingException;

public abstract class X509StreamParserSpi
{
  public abstract void engineInit(InputStream paramInputStream);

  public abstract Object engineRead()
    throws StreamParsingException;

  public abstract Collection engineReadAll()
    throws StreamParsingException;
}