package jcifs.dcerpc;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
import jcifs.smb.SmbNamedPipe;
import jcifs.util.Encdec;

public class DcerpcPipeHandle extends DcerpcHandle
{
  SmbFileInputStream in = null;
  boolean isStart = true;
  SmbFileOutputStream out = null;
  SmbNamedPipe pipe;

  public DcerpcPipeHandle(String paramString, NtlmPasswordAuthentication paramNtlmPasswordAuthentication)
    throws UnknownHostException, MalformedURLException, DcerpcException
  {
    this.binding = DcerpcHandle.parseBinding(paramString);
    this.pipe = new SmbNamedPipe("smb://" + this.binding.server + "/IPC$/" + this.binding.endpoint.substring(6), 27198979, paramNtlmPasswordAuthentication);
  }

  public void close()
    throws IOException
  {
    if (this.out != null)
      this.out.close();
  }

  protected void doReceiveFragment(byte[] paramArrayOfByte, boolean paramBoolean)
    throws IOException
  {
    boolean bool = true;
    if (paramArrayOfByte.length < this.max_recv)
      throw new IllegalArgumentException("buffer too small");
    if ((this.isStart) && (!paramBoolean));
    for (int i = this.in.read(paramArrayOfByte, 0, 1024); (paramArrayOfByte[0] != 5) && (paramArrayOfByte[bool] != 0); i = this.in.readDirect(paramArrayOfByte, 0, paramArrayOfByte.length))
      throw new IOException("Unexpected DCERPC PDU header");
    if ((0x2 & (0xFF & paramArrayOfByte[3])) == 2);
    int j;
    while (true)
    {
      this.isStart = bool;
      j = Encdec.dec_uint16le(paramArrayOfByte, 8);
      if (j <= this.max_recv)
        break;
      throw new IOException("Unexpected fragment length: " + j);
      bool = false;
    }
    while (i < j)
      i += this.in.readDirect(paramArrayOfByte, i, j - i);
  }

  protected void doSendFragment(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
    throws IOException
  {
    if (this.in == null)
      this.in = ((SmbFileInputStream)this.pipe.getNamedPipeInputStream());
    if (this.out == null)
      this.out = ((SmbFileOutputStream)this.pipe.getNamedPipeOutputStream());
    if (paramBoolean)
    {
      this.out.writeDirect(paramArrayOfByte, paramInt1, paramInt2, 1);
      return;
    }
    this.out.write(paramArrayOfByte, paramInt1, paramInt2);
  }
}