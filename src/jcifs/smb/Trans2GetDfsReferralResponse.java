package jcifs.smb;

class Trans2GetDfsReferralResponse extends SmbComTransactionResponse
{
  int flags;
  int numReferrals;
  int pathConsumed;
  Referral[] referrals;

  Trans2GetDfsReferralResponse()
  {
    this.subCommand = 16;
  }

  int readDataWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    this.pathConsumed = readInt2(paramArrayOfByte, paramInt1);
    int i = paramInt1 + 2;
    if ((0x8000 & this.flags2) != 0)
      this.pathConsumed /= 2;
    this.numReferrals = readInt2(paramArrayOfByte, i);
    int j = i + 2;
    this.flags = readInt2(paramArrayOfByte, j);
    int k = j + 4;
    this.referrals = new Referral[this.numReferrals];
    for (int m = 0; m < this.numReferrals; m++)
    {
      this.referrals[m] = new Referral();
      k += this.referrals[m].readWireFormat(paramArrayOfByte, k, paramInt2);
    }
    return k - paramInt1;
  }

  int readParametersWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return 0;
  }

  int readSetupWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    return 0;
  }

  public String toString()
  {
    return new String("Trans2GetDfsReferralResponse[" + super.toString() + ",pathConsumed=" + this.pathConsumed + ",numReferrals=" + this.numReferrals + ",flags=" + this.flags + "]");
  }

  int writeDataWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeParametersWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  int writeSetupWireFormat(byte[] paramArrayOfByte, int paramInt)
  {
    return 0;
  }

  class Referral
  {
    private String altPath;
    private int altPathOffset;
    private int flags;
    String node = null;
    private int nodeOffset;
    String path = null;
    private int pathOffset;
    private int proximity;
    private int serverType;
    private int size;
    int ttl;
    private int version;

    Referral()
    {
    }

    int readWireFormat(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      int i = 1;
      this.version = ServerMessageBlock.readInt2(paramArrayOfByte, paramInt1);
      if ((this.version != 3) && (this.version != i))
        throw new RuntimeException("Version " + this.version + " referral not supported. Please report this to jcifs at samba dot org.");
      int m = paramInt1 + 2;
      this.size = ServerMessageBlock.readInt2(paramArrayOfByte, m);
      int n = m + 2;
      this.serverType = ServerMessageBlock.readInt2(paramArrayOfByte, n);
      int i1 = n + 2;
      this.flags = ServerMessageBlock.readInt2(paramArrayOfByte, i1);
      int i2 = i1 + 2;
      if (this.version == 3)
      {
        this.proximity = ServerMessageBlock.readInt2(paramArrayOfByte, i2);
        i3 = i2 + 2;
        this.ttl = ServerMessageBlock.readInt2(paramArrayOfByte, i3);
        i4 = i3 + 2;
        this.pathOffset = ServerMessageBlock.readInt2(paramArrayOfByte, i4);
        i5 = i4 + 2;
        this.altPathOffset = ServerMessageBlock.readInt2(paramArrayOfByte, i5);
        i6 = i5 + 2;
        this.nodeOffset = ServerMessageBlock.readInt2(paramArrayOfByte, i6);
        (i6 + 2);
        localTrans2GetDfsReferralResponse2 = Trans2GetDfsReferralResponse.this;
        i7 = paramInt1 + this.pathOffset;
        if ((0x8000 & Trans2GetDfsReferralResponse.this.flags2) != 0)
        {
          i8 = i;
          this.path = localTrans2GetDfsReferralResponse2.readString(paramArrayOfByte, i7, paramInt2, i8);
          if (this.nodeOffset > 0)
          {
            localTrans2GetDfsReferralResponse3 = Trans2GetDfsReferralResponse.this;
            i10 = paramInt1 + this.nodeOffset;
            if ((0x8000 & Trans2GetDfsReferralResponse.this.flags2) == 0)
              break label310;
            this.node = localTrans2GetDfsReferralResponse3.readString(paramArrayOfByte, i10, paramInt2, i);
          }
        }
      }
      label310: int j;
      while (this.version != j)
        while (true)
        {
          int i3;
          int i4;
          int i5;
          int i6;
          Trans2GetDfsReferralResponse localTrans2GetDfsReferralResponse2;
          int i7;
          int i8;
          Trans2GetDfsReferralResponse localTrans2GetDfsReferralResponse3;
          int i10;
          return this.size;
          int i9 = 0;
          continue;
          j = 0;
        }
      Trans2GetDfsReferralResponse localTrans2GetDfsReferralResponse1 = Trans2GetDfsReferralResponse.this;
      if ((0x8000 & Trans2GetDfsReferralResponse.this.flags2) != 0);
      while (true)
      {
        this.node = localTrans2GetDfsReferralResponse1.readString(paramArrayOfByte, i2, paramInt2, j);
        break;
        int k = 0;
      }
    }

    public String toString()
    {
      return new String("Referral[version=" + this.version + ",size=" + this.size + ",serverType=" + this.serverType + ",flags=" + this.flags + ",proximity=" + this.proximity + ",ttl=" + this.ttl + ",pathOffset=" + this.pathOffset + ",altPathOffset=" + this.altPathOffset + ",nodeOffset=" + this.nodeOffset + ",path=" + this.path + ",altPath=" + this.altPath + ",node=" + this.node + "]");
    }
  }
}