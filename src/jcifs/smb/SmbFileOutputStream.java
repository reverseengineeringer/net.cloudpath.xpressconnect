package jcifs.smb;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import jcifs.util.LogStream;

public class SmbFileOutputStream extends OutputStream
{
  private int access;
  private boolean append;
  private SmbFile file;
  private long fp;
  private int openFlags;
  private SmbComWrite req;
  private SmbComWriteAndX reqx;
  private SmbComWriteResponse rsp;
  private SmbComWriteAndXResponse rspx;
  private byte[] tmp = new byte[1];
  private boolean useNTSmbs;
  private int writeSize;

  public SmbFileOutputStream(String paramString)
    throws SmbException, MalformedURLException, UnknownHostException
  {
    this(paramString, false);
  }

  public SmbFileOutputStream(String paramString, int paramInt)
    throws SmbException, MalformedURLException, UnknownHostException
  {
    this(new SmbFile(paramString, "", null, paramInt), false);
  }

  public SmbFileOutputStream(String paramString, boolean paramBoolean)
    throws SmbException, MalformedURLException, UnknownHostException
  {
    this(new SmbFile(paramString), paramBoolean);
  }

  public SmbFileOutputStream(SmbFile paramSmbFile)
    throws SmbException, MalformedURLException, UnknownHostException
  {
    this(paramSmbFile, false);
  }

  public SmbFileOutputStream(SmbFile paramSmbFile, boolean paramBoolean)
    throws SmbException, MalformedURLException, UnknownHostException
  {
  }

  SmbFileOutputStream(SmbFile paramSmbFile, boolean paramBoolean, int paramInt)
    throws SmbException, MalformedURLException, UnknownHostException
  {
    this.file = paramSmbFile;
    this.append = paramBoolean;
    this.openFlags = paramInt;
    this.access = (0xFFFF & paramInt >>> 16);
    if (paramBoolean);
    try
    {
      this.fp = paramSmbFile.length();
      if (((paramSmbFile instanceof SmbNamedPipe)) && (paramSmbFile.unc.startsWith("\\pipe\\")))
      {
        paramSmbFile.unc = paramSmbFile.unc.substring(5);
        paramSmbFile.send(new TransWaitNamedPipe("\\pipe" + paramSmbFile.unc), new TransWaitNamedPipeResponse());
      }
      paramSmbFile.open(paramInt, 0x2 | this.access, 128, 0);
      this.openFlags = (0xFFFFFFAF & this.openFlags);
      this.writeSize = (-70 + paramSmbFile.tree.session.transport.snd_buf_size);
      this.useNTSmbs = paramSmbFile.tree.session.transport.hasCapability(16);
      if (this.useNTSmbs)
      {
        this.reqx = new SmbComWriteAndX();
        this.rspx = new SmbComWriteAndXResponse();
        return;
      }
    }
    catch (SmbAuthException localSmbAuthException)
    {
      throw localSmbAuthException;
    }
    catch (SmbException localSmbException)
    {
      while (true)
        this.fp = 0L;
      this.req = new SmbComWrite();
      this.rsp = new SmbComWriteResponse();
    }
  }

  public void close()
    throws IOException
  {
    this.file.close();
    this.tmp = null;
  }

  void ensureOpen()
    throws IOException
  {
    if (!this.file.isOpen())
    {
      this.file.open(this.openFlags, 0x2 | this.access, 128, 0);
      if (this.append)
        this.fp = this.file.length();
    }
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
    if ((!this.file.isOpen()) && ((this.file instanceof SmbNamedPipe)))
      this.file.send(new TransWaitNamedPipe("\\pipe" + this.file.unc), new TransWaitNamedPipeResponse());
    writeDirect(paramArrayOfByte, paramInt1, paramInt2, 0);
  }

  public void writeDirect(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3)
    throws IOException
  {
    if (paramInt2 <= 0)
      return;
    if (this.tmp == null)
      throw new IOException("Bad file descriptor");
    ensureOpen();
    if (LogStream.level >= 4)
      SmbFile.log.println("write: fid=" + this.file.fid + ",off=" + paramInt1 + ",len=" + paramInt2);
    label182: label341: 
    while (true)
    {
      int i;
      if (paramInt2 > this.writeSize)
      {
        i = this.writeSize;
        if (!this.useNTSmbs)
          break label259;
        this.reqx.setParam(this.file.fid, this.fp, paramInt2 - i, paramArrayOfByte, paramInt1, i);
        if ((paramInt3 & 0x1) == 0)
          break label248;
        this.reqx.setParam(this.file.fid, this.fp, paramInt2, paramArrayOfByte, paramInt1, i);
        this.reqx.writeMode = 8;
        this.file.send(this.reqx, this.rspx);
        this.fp += this.rspx.count;
        paramInt2 = (int)(paramInt2 - this.rspx.count);
        paramInt1 = (int)(paramInt1 + this.rspx.count);
      }
      while (true)
      {
        if (paramInt2 > 0)
          break label341;
        return;
        i = paramInt2;
        break;
        label248: this.reqx.writeMode = 0;
        break label182;
        this.req.setParam(this.file.fid, this.fp, paramInt2 - i, paramArrayOfByte, paramInt1, i);
        this.fp += this.rsp.count;
        paramInt2 = (int)(paramInt2 - this.rsp.count);
        paramInt1 = (int)(paramInt1 + this.rsp.count);
        this.file.send(this.req, this.rsp);
      }
    }
  }
}