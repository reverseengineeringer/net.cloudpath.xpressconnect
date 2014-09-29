package jcifs.smb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class SmbNamedPipe extends SmbFile
{
  public static final int PIPE_TYPE_CALL = 256;
  public static final int PIPE_TYPE_DCE_TRANSACT = 1536;
  public static final int PIPE_TYPE_RDONLY = 1;
  public static final int PIPE_TYPE_RDWR = 3;
  public static final int PIPE_TYPE_TRANSACT = 512;
  public static final int PIPE_TYPE_WRONLY = 2;
  InputStream pipeIn;
  OutputStream pipeOut;
  int pipeType;

  public SmbNamedPipe(String paramString, int paramInt)
    throws MalformedURLException, UnknownHostException
  {
    super(paramString);
    this.pipeType = paramInt;
    this.type = 16;
  }

  public SmbNamedPipe(String paramString, int paramInt, NtlmPasswordAuthentication paramNtlmPasswordAuthentication)
    throws MalformedURLException, UnknownHostException
  {
    super(paramString, paramNtlmPasswordAuthentication);
    this.pipeType = paramInt;
    this.type = 16;
  }

  public SmbNamedPipe(URL paramURL, int paramInt, NtlmPasswordAuthentication paramNtlmPasswordAuthentication)
    throws MalformedURLException, UnknownHostException
  {
    super(paramURL, paramNtlmPasswordAuthentication);
    this.pipeType = paramInt;
    this.type = 16;
  }

  public InputStream getNamedPipeInputStream()
    throws IOException
  {
    if (this.pipeIn == null)
      if (((0x100 & this.pipeType) != 256) && ((0x200 & this.pipeType) != 512))
        break label52;
    label52: for (this.pipeIn = new TransactNamedPipeInputStream(this); ; this.pipeIn = new SmbFileInputStream(this, 0x20 | 0xFFFF00FF & this.pipeType))
      return this.pipeIn;
  }

  public OutputStream getNamedPipeOutputStream()
    throws IOException
  {
    if (this.pipeOut == null)
      if (((0x100 & this.pipeType) != 256) && ((0x200 & this.pipeType) != 512))
        break label52;
    label52: for (this.pipeOut = new TransactNamedPipeOutputStream(this); ; this.pipeOut = new SmbFileOutputStream(this, false, 0x20 | 0xFFFF00FF & this.pipeType))
      return this.pipeOut;
  }
}