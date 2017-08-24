package com.sk.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.junit.Test;

public class TestBlockingNIO2
{
	@Test
	public void server() throws Exception
	{
		ServerSocketChannel sschaChannel=ServerSocketChannel.open();
		sschaChannel.bind(new InetSocketAddress(9898));
		SocketChannel sChannel=sschaChannel.accept();
		
		FileChannel outchaChannel=FileChannel.open(Paths.get("2_2.jpg"), StandardOpenOption.CREATE,
				StandardOpenOption.WRITE);
		ByteBuffer buf=ByteBuffer.allocate(1024);
		
		
		while(sChannel.read(buf)!=-1)
		{
			buf.flip();
			outchaChannel.write(buf);
			buf.clear();
		}
		
		buf.put("我是服务器端，接收数据成功，特此反馈!".getBytes());
		buf.flip();
		sChannel.write(buf);
		
		sChannel.close();
		outchaChannel.close();
		sschaChannel.close();
	}
	
	@Test
	public void client() throws Exception
	{
		SocketChannel sChannel=SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
		
		FileChannel inChannel=FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
		
		ByteBuffer buf=ByteBuffer.allocate(1024);
		while(inChannel.read(buf)!=-1)
		{
			buf.flip();
			sChannel.write(buf);
			buf.clear();
		}
		
		sChannel.shutdownOutput();
		
		int len=0;
		while((len=sChannel.read(buf))!=-1)
		{
			buf.flip();
			System.out.println(new String(buf.array(),0,len));
			buf.clear();
		}
		
		inChannel.close();
		sChannel.close();		
	}
}
