package com.sk.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

public class TestBlockingNIO
{
	@Test
	public void server() throws Exception
	{
		ServerSocketChannel ssChannel=ServerSocketChannel.open();
		FileChannel outChannel=FileChannel.open(Paths.get("2_1.jpg"), 
				StandardOpenOption.WRITE,StandardOpenOption.CREATE);
		ssChannel.bind(new InetSocketAddress(9898));
		SocketChannel sChannel=ssChannel.accept();
		ByteBuffer buf=ByteBuffer.allocate(1024);
		while(sChannel.read(buf)!=-1)
		{
			buf.flip();
			outChannel.write(buf);
			buf.clear();
		}
		
		sChannel.close();
		outChannel.close();
		ssChannel.close();
	}
	
	@Test
	public void client() throws Exception
	{
		SocketChannel sChannel=SocketChannel.open(
				new InetSocketAddress("127.0.0.1", 9898));
		
		FileChannel inChannel=FileChannel.open(Paths.get("1.jpg"),
				StandardOpenOption.READ);
		
		ByteBuffer bBuf=ByteBuffer.allocate(1024);
		while(inChannel.read(bBuf)!=-1)
		{
			bBuf.flip();
			sChannel.write(bBuf);
			bBuf.clear();
		}
		
		inChannel.close();
		sChannel.close();
				
	}
}
