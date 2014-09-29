package org.bouncycastle2.crypto.tls;

import java.io.IOException;

public class TlsFatalAlert extends IOException
{
  private static final long serialVersionUID = 3584313123679111168L;
  private short alertDescription;

  public TlsFatalAlert(short paramShort)
  {
    this.alertDescription = paramShort;
  }

  public short getAlertDescription()
  {
    return this.alertDescription;
  }
}