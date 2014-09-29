package org.bouncycastle2.util.io.pem;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import org.bouncycastle2.util.encoders.Base64;

public class PemWriter extends BufferedWriter
{
  private static final int LINE_LENGTH = 64;
  private char[] buf = new char[64];
  private final int nlLength;

  public PemWriter(Writer paramWriter)
  {
    super(paramWriter);
    String str = System.getProperty("line.separator");
    if (str != null)
    {
      this.nlLength = str.length();
      return;
    }
    this.nlLength = 2;
  }

  private void writeEncoded(byte[] paramArrayOfByte)
    throws IOException
  {
    byte[] arrayOfByte = Base64.encode(paramArrayOfByte);
    int i = 0;
    if (i >= arrayOfByte.length)
      return;
    for (int j = 0; ; j++)
    {
      if (j == this.buf.length);
      while (i + j >= arrayOfByte.length)
      {
        write(this.buf, 0, j);
        newLine();
        i += this.buf.length;
        break;
      }
      this.buf[j] = ((char)arrayOfByte[(i + j)]);
    }
  }

  private void writePostEncapsulationBoundary(String paramString)
    throws IOException
  {
    write("-----END " + paramString + "-----");
    newLine();
  }

  private void writePreEncapsulationBoundary(String paramString)
    throws IOException
  {
    write("-----BEGIN " + paramString + "-----");
    newLine();
  }

  public int getOutputSize(PemObject paramPemObject)
  {
    int i = 4 + (6 + 2 * (10 + paramPemObject.getType().length() + this.nlLength));
    Iterator localIterator;
    if (!paramPemObject.getHeaders().isEmpty())
      localIterator = paramPemObject.getHeaders().iterator();
    while (true)
    {
      if (!localIterator.hasNext())
      {
        i += this.nlLength;
        int j = 4 * ((2 + paramPemObject.getContent().length) / 3);
        return i + (j + (-1 + (j + 64)) / 64 * this.nlLength);
      }
      PemHeader localPemHeader = (PemHeader)localIterator.next();
      i += localPemHeader.getName().length() + ": ".length() + localPemHeader.getValue().length() + this.nlLength;
    }
  }

  public void writeObject(PemObjectGenerator paramPemObjectGenerator)
    throws IOException
  {
    PemObject localPemObject = paramPemObjectGenerator.generate();
    writePreEncapsulationBoundary(localPemObject.getType());
    Iterator localIterator;
    if (!localPemObject.getHeaders().isEmpty())
      localIterator = localPemObject.getHeaders().iterator();
    while (true)
    {
      if (!localIterator.hasNext())
      {
        newLine();
        writeEncoded(localPemObject.getContent());
        writePostEncapsulationBoundary(localPemObject.getType());
        return;
      }
      PemHeader localPemHeader = (PemHeader)localIterator.next();
      write(localPemHeader.getName());
      write(": ");
      write(localPemHeader.getValue());
      newLine();
    }
  }
}