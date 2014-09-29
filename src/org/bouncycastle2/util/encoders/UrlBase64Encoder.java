package org.bouncycastle2.util.encoders;

public class UrlBase64Encoder extends Base64Encoder
{
  public UrlBase64Encoder()
  {
    this.encodingTable[(-2 + this.encodingTable.length)] = 45;
    this.encodingTable[(-1 + this.encodingTable.length)] = 95;
    this.padding = 46;
    initialiseDecodingTable();
  }
}