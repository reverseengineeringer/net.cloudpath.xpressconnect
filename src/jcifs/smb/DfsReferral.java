package jcifs.smb;

public class DfsReferral extends SmbException
{
  public long expiration;
  public String link;
  public String path;
  public int pathConsumed;
  public boolean resolveHashes;
  public String server;
  public String share;
  public long ttl;

  public String toString()
  {
    return "DfsReferral[pathConsumed=" + this.pathConsumed + ",server=" + this.server + ",share=" + this.share + ",link=" + this.link + ",path=" + this.path + ",ttl=" + this.ttl + ",expiration=" + this.expiration + ",resolveHashes=" + this.resolveHashes + "]";
  }
}