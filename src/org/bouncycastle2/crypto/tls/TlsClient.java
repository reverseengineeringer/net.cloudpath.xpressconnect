package org.bouncycastle2.crypto.tls;

import java.io.IOException;
import java.util.Hashtable;

public abstract interface TlsClient
{
  public abstract TlsAuthentication getAuthentication()
    throws IOException;

  public abstract TlsCipher getCipher()
    throws IOException;

  public abstract int[] getCipherSuites();

  public abstract Hashtable getClientExtensions()
    throws IOException;

  public abstract TlsCompression getCompression()
    throws IOException;

  public abstract short[] getCompressionMethods();

  public abstract TlsKeyExchange getKeyExchange()
    throws IOException;

  public abstract void init(TlsClientContext paramTlsClientContext);

  public abstract void notifySecureRenegotiation(boolean paramBoolean)
    throws IOException;

  public abstract void notifySelectedCipherSuite(int paramInt);

  public abstract void notifySelectedCompressionMethod(short paramShort);

  public abstract void notifySessionID(byte[] paramArrayOfByte);

  public abstract void processServerExtensions(Hashtable paramHashtable);
}