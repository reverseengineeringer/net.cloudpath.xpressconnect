package org.xbill.DNS;

import java.util.ArrayList;
import java.util.List;

public class SetResponse
{
  static final int CNAME = 4;
  static final int DELEGATION = 3;
  static final int DNAME = 5;
  static final int NXDOMAIN = 1;
  static final int NXRRSET = 2;
  static final int SUCCESSFUL = 6;
  static final int UNKNOWN;
  private static final SetResponse nxdomain = new SetResponse(1);
  private static final SetResponse nxrrset = new SetResponse(2);
  private static final SetResponse unknown = new SetResponse(0);
  private Object data;
  private int type;

  private SetResponse()
  {
  }

  SetResponse(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > 6))
      throw new IllegalArgumentException("invalid type");
    this.type = paramInt;
    this.data = null;
  }

  SetResponse(int paramInt, RRset paramRRset)
  {
    if ((paramInt < 0) || (paramInt > 6))
      throw new IllegalArgumentException("invalid type");
    this.type = paramInt;
    this.data = paramRRset;
  }

  static SetResponse ofType(int paramInt)
  {
    switch (paramInt)
    {
    default:
      throw new IllegalArgumentException("invalid type");
    case 0:
      return unknown;
    case 1:
      return nxdomain;
    case 2:
      return nxrrset;
    case 3:
    case 4:
    case 5:
    case 6:
    }
    SetResponse localSetResponse = new SetResponse();
    localSetResponse.type = paramInt;
    localSetResponse.data = null;
    return localSetResponse;
  }

  void addRRset(RRset paramRRset)
  {
    if (this.data == null)
      this.data = new ArrayList();
    ((List)this.data).add(paramRRset);
  }

  public RRset[] answers()
  {
    if (this.type != 6)
      return null;
    List localList = (List)this.data;
    return (RRset[])localList.toArray(new RRset[localList.size()]);
  }

  public CNAMERecord getCNAME()
  {
    return (CNAMERecord)((RRset)this.data).first();
  }

  public DNAMERecord getDNAME()
  {
    return (DNAMERecord)((RRset)this.data).first();
  }

  public RRset getNS()
  {
    return (RRset)this.data;
  }

  public boolean isCNAME()
  {
    return this.type == 4;
  }

  public boolean isDNAME()
  {
    return this.type == 5;
  }

  public boolean isDelegation()
  {
    return this.type == 3;
  }

  public boolean isNXDOMAIN()
  {
    return this.type == 1;
  }

  public boolean isNXRRSET()
  {
    return this.type == 2;
  }

  public boolean isSuccessful()
  {
    return this.type == 6;
  }

  public boolean isUnknown()
  {
    return this.type == 0;
  }

  public String toString()
  {
    switch (this.type)
    {
    default:
      throw new IllegalStateException();
    case 0:
      return "unknown";
    case 1:
      return "NXDOMAIN";
    case 2:
      return "NXRRSET";
    case 3:
      return "delegation: " + this.data;
    case 4:
      return "CNAME: " + this.data;
    case 5:
      return "DNAME: " + this.data;
    case 6:
    }
    return "successful";
  }
}