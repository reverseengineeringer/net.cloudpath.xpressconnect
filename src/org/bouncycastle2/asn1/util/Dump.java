package org.bouncycastle2.asn1.util;

import java.io.FileInputStream;
import java.io.PrintStream;
import org.bouncycastle2.asn1.ASN1InputStream;
import org.bouncycastle2.asn1.DERObject;

public class Dump
{
  public static void main(String[] paramArrayOfString)
    throws Exception
  {
    ASN1InputStream localASN1InputStream = new ASN1InputStream(new FileInputStream(paramArrayOfString[0]));
    while (true)
    {
      DERObject localDERObject = localASN1InputStream.readObject();
      if (localDERObject == null)
        return;
      System.out.println(ASN1Dump.dumpAsString(localDERObject));
    }
  }
}