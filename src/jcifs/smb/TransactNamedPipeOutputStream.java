package jcifs.smb;

import java.io.IOException;

class TransactNamedPipeOutputStream extends SmbFileOutputStream
{
  private boolean dcePipe;
  private String path;
  private SmbNamedPipe pipe;
  private byte[] tmp;

  TransactNamedPipeOutputStream(SmbNamedPipe paramSmbNamedPipe)
    throws IOException
  {
    super(paramSmbNamedPipe, false, 0x20 | 0xFFFF00FF & paramSmbNamedPipe.pipeType);
    this.tmp = new byte[bool];
    this.pipe = paramSmbNamedPipe;
    if ((0x600 & paramSmbNamedPipe.pipeType) == 1536);
    while (true)
    {
      this.dcePipe = bool;
      this.path = paramSmbNamedPipe.unc;
      return;
      bool = false;
    }
  }

  public void close()
    throws IOException
  {
    this.pipe.close();
  }

  public void write(int paramInt)
    throws IOException
  {
    this.tmp[0] = ((byte)paramInt);
    write(this.tmp, 0, 1);
  }

  public void write(byte[] paramArrayOfByte)
    throws IOException
  {
    write(paramArrayOfByte, 0, paramArrayOfByte.length);
  }

  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramInt2 < 0)
      paramInt2 = 0;
    if ((0x100 & this.pipe.pipeType) == 256)
    {
      this.pipe.send(new TransWaitNamedPipe(this.path), new TransWaitNamedPipeResponse());
      this.pipe.send(new TransCallNamedPipe(this.path, paramArrayOfByte, paramInt1, paramInt2), new TransCallNamedPipeResponse(this.pipe));
    }
    while ((0x200 & this.pipe.pipeType) != 512)
      return;
    ensureOpen();
    TransTransactNamedPipe localTransTransactNamedPipe = new TransTransactNamedPipe(this.pipe.fid, paramArrayOfByte, paramInt1, paramInt2);
    if (this.dcePipe)
      localTransTransactNamedPipe.maxDataCount = 1024;
    this.pipe.send(localTransTransactNamedPipe, new TransTransactNamedPipeResponse(this.pipe));
  }
}