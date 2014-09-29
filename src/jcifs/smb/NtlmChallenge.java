package jcifs.smb;

import java.io.Serializable;
import jcifs.UniAddress;
import jcifs.util.Hexdump;

public final class NtlmChallenge
  implements Serializable
{
  public byte[] challenge;
  public UniAddress dc;

  NtlmChallenge(byte[] paramArrayOfByte, UniAddress paramUniAddress)
  {
    this.challenge = paramArrayOfByte;
    this.dc = paramUniAddress;
  }

  public String toString()
  {
    return "NtlmChallenge[challenge=0x" + Hexdump.toHexString(this.challenge, 0, 2 * this.challenge.length) + ",dc=" + this.dc.toString() + "]";
  }
}