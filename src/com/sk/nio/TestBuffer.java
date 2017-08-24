package com.sk.nio;

import java.nio.ByteBuffer;

import org.junit.Test;

public class TestBuffer
{
	@Test
	public void test2()
	{
		String str="abcde";
		ByteBuffer buf=ByteBuffer.allocate(1024);
		buf.put(str.getBytes());
		buf.flip();
		byte[] dst=new byte[buf.limit()];
		buf.get(dst, 0, 2);
		System.out.println(new String(dst,0,2));
		System.out.println(buf.position());
		
		buf.mark();
		
		buf.get(dst,2,2);
		System.out.println(new String(dst,2,2));
		System.out.println(buf.position());
		
		
		buf.reset();
		System.out.println(buf.position());
		
		if(buf.hasRemaining())
		{
			System.out.println(buf.remaining());
		}
		
	}
	
	@Test
	public void test1()
	{
		String str="abcde";
		
		ByteBuffer buffer=ByteBuffer.allocate(1024);
		
		System.out.println("----------------------allocate()-------------------------");
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
		
		buffer.put(str.getBytes());
		
		System.out.println("----------------------put()-------------------------");
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
		
		buffer.flip();
		
		System.out.println("----------------------flip()-------------------------");
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
		
		byte[] dst=new byte[buffer.limit()];
		buffer.get(dst);
		
		System.out.println("----------------------get()-------------------------");
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
		
		buffer.rewind();
		
		System.out.println("----------------------rewind()-------------------------");
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
		
		
		buffer.clear();
		
		System.out.println("----------------------clear()-------------------------");
		System.out.println(buffer.position());
		System.out.println(buffer.limit());
		System.out.println(buffer.capacity());
	}
}
