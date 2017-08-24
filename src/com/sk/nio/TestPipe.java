package com.sk.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

import org.junit.Test;

public class TestPipe
{
	@Test
	public void test1() throws Exception
	{
		//1. 获取管道
		Pipe pipe=Pipe.open();
		
		ByteBuffer buf=ByteBuffer.allocate(1024);
		
		Pipe.SinkChannel sinkChannel=pipe.sink();
		buf.put("向管道中发送数据".getBytes());
		buf.flip();
		sinkChannel.write(buf);
		
		Pipe.SourceChannel sourceChannel=pipe.source();
		buf.flip();
		int len=sourceChannel.read(buf);
		System.out.println(new String(buf.array(),0,len));
		
		
		sourceChannel.close();
		sinkChannel.close();
		
		
	}
}
