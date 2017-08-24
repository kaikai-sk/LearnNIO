package com.sk.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

public class TestChannel
{
	//字符集的编码和解码
	@Test
	public void test6() throws Exception
	{
		Charset cs1=Charset.forName("GBK");
		//获取编码解码器
		CharsetEncoder ce=cs1.newEncoder();
		CharsetDecoder cd=cs1.newDecoder();
		
		CharBuffer cBuf=CharBuffer.allocate(1024);
		cBuf.put("我叫单凯");
		cBuf.flip();
		
		ByteBuffer bBuf=ce.encode(cBuf);
		for(int i=0;i<bBuf.limit();i++)
		{
			System.out.println(bBuf.get());
		}
		
		bBuf.flip();
		CharBuffer cBuf2=cd.decode(bBuf);
		System.out.println(cBuf2.toString());			
	
		System.out.println("-----------------------------------");
		bBuf.flip();
		Charset cs2=Charset.forName("UTF-8");
		CharBuffer cBuf3=cs2.decode(bBuf);
		System.out.println(cBuf3.toString());
	}
	
	//显示nio所支持的字符集
	@Test
	public void test5()
	{
		Map<String, Charset> map=Charset.availableCharsets();
		Set<Entry<String,Charset>> set=map.entrySet();
		for(Entry<String, Charset> entry:set)
		{
			System.out.println(entry.getKey()+"="+entry.getValue());
		}
	}
	
	
	//分散读取和聚集写入
	@Test
	public void test4() throws Exception
	{
		RandomAccessFile raf1=new RandomAccessFile(new File("1.txt"), "rw");
		//获取通道
		FileChannel channel1=raf1.getChannel();
		//分配指定大小的缓冲区
		ByteBuffer buf1=ByteBuffer.allocate(100);
		ByteBuffer buf2=ByteBuffer.allocate(1024);
		
		ByteBuffer[] bufs={buf1,buf2};
		channel1.read(bufs);
		
		for(ByteBuffer byteBuffer:bufs)
		{
			byteBuffer.flip();
		}
		
		System.out.println(new String(bufs[0].array(),0,bufs[0].limit()));
		System.out.println("-----------------------------------");
		System.out.println(new String(bufs[1].array(),0,bufs[1].limit()));
		
		//聚集写入
		RandomAccessFile raf2=new RandomAccessFile(new File("2.txt"), "rw");
		FileChannel channel2=raf2.getChannel();
		channel2.write(bufs);	
		
	}
	
	
	//通道之间的数据传输（直接缓冲区）
	@Test
	public void test3() throws Exception
	{
		FileChannel inChannel=FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
		FileChannel outChannel=FileChannel.open(Paths.get("4.jpg"), StandardOpenOption.WRITE,
				StandardOpenOption.READ,StandardOpenOption.CREATE);
		
		//inChannel.transferTo(0, inChannel.size(), outChannel);
		outChannel.transferFrom(inChannel, 0, inChannel.size());
		
		inChannel.close();
		outChannel.close();
	}
	
	@Test
	public void test2() throws Exception
	{
		FileChannel inChannel=FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
		FileChannel outChannel=FileChannel.open(Paths.get("3.jpg"), StandardOpenOption.WRITE,StandardOpenOption.READ,
				StandardOpenOption.CREATE);
		
		MappedByteBuffer inMappedByteBuffer=inChannel.map(MapMode.READ_ONLY, 0, inChannel.size());
		MappedByteBuffer outMappedByteBuffer=outChannel.map(MapMode.READ_WRITE, 0, inChannel.size());
		
		byte[] dst=new byte[inMappedByteBuffer.limit()];
		inMappedByteBuffer.get(dst);
		outMappedByteBuffer.put(dst);
		
		inChannel.close();
		outChannel.close();
	}
	
	@Test
	public void test1() throws Exception
	{
		FileInputStream fis=new FileInputStream(new File("1.jpg"));
		FileOutputStream fos=new FileOutputStream(new File("2.jpg"));
		
		FileChannel inChannel = fis.getChannel();
		FileChannel outChannel=fos.getChannel();
		
		ByteBuffer buf=ByteBuffer.allocate(1024);
		while((inChannel.read(buf))!=-1)
		{
			buf.flip();
			outChannel.write(buf);
			buf.clear();
		}
		
		outChannel.close();
		inChannel.close();
		fos.close();
		fis.close();
	}
}
