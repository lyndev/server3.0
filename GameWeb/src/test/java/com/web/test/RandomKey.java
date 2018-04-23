package com.web.test;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

import com.web.utils.ByteUtil;

public class RandomKey {
	
	
	public static void random(){
		short batch = 1000;
		short num = 30000;
		byte[] bytes = new byte[8];
		bytes[0] = (byte) (batch & 0xff);
		bytes[1] = (byte) ((batch & 0xff00) >> 8);

		bytes[2] = (byte) (num & 0xff);
		bytes[3] = (byte) ((num & 0xff00) >> 8);
		
		int time = 100;
		byte[] int_bytes = ByteUtil.getBytes(time);
		
		bytes[4] = int_bytes[0];
		bytes[5] = int_bytes[1];
		bytes[6] = int_bytes[2];
		bytes[7] = int_bytes[3];
		
		System.out.println("bytes:"+Arrays.toString(bytes));
		
		long l = ByteUtil.getLong(bytes);
		
		System.out.println("l:"+l);
		System.out.println("16L:"+Long.toString(l, 36));
		
	}
	

	public static void main(String[] args) {
		random();
//		short batch = 1;
//		short num = 1;
//		byte[] bytes = new byte[8];
//		bytes[0] = (byte) (batch & 0xff);
//		bytes[1] = (byte) ((batch & 0xff00) >> 8);
//
//		bytes[2] = (byte) (num & 0xff);
//		bytes[3] = (byte) ((num & 0xff00) >> 8);
//		
//		
//		long n = ByteUtil.getLong(bytes);
////		n = n & (0xffffffff << 32);
//		System.out.println("n:"+n);
//		
//		System.out.println("0X:"+Long.toString(n, 36));

//		short batch = 1;
//		short attrID = 1;
//		short time = 1;
//		short n = -1;
//		System.out.println("time = " + time);
//
//		byte[] bytes = new byte[8];
//		bytes[0] = (byte) (batch & 0xff);
//		bytes[1] = (byte) ((batch & 0xff00) >> 8);
//
//		bytes[2] = (byte) (attrID & 0xff);
//		bytes[3] = (byte) ((attrID & 0xff00) >> 8);
//
//		bytes[4] = (byte) (time & 0xff);
//		bytes[5] = (byte) ((time & 0xff00) >> 8);
//
//		bytes[6] = (byte) (n & 0xff);
//		bytes[7] = (byte) ((n & 0xff00) >> 8);
//		System.err.println(bytes[7]);
//		System.err.println(Arrays.toString(bytes));
//		long key0 = ByteUtil.getLong(bytes);
//		BigDecimal bigDecimal = null;
//		try {
//			bigDecimal = readUnsignedLong(key0);
//			System.out.println("key0:" + key0);
//			System.out.println("big:"+bigDecimal);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
////		long key = Math.abs(ByteUtil.getLong(bytes));
//		long key = bigDecimal.longValue();
//		System.out.println("key:"+key);
//		byte[] bs = ByteUtil.getBytes(key);
//		System.err.println(Arrays.toString(bs));
//		byte[] bBatch = new byte[2];
//		bBatch[0] = bs[2];
//		bBatch[1] = bs[3];
//		// bBatch[2] = bs[6];
//		// bBatch[3] = bs[7];
//		System.out.println(ByteUtil.getShort(bBatch));
//		String key_36 = Long.toString(key, 36);
//		System.out.println("::::" + key_36);
	}

	public static BigDecimal readUnsignedLong(long value) throws IOException {
		if (value >= 0)
			return new BigDecimal(value);
		long lowValue = value & 0x7fffffffffffffffL;
		return BigDecimal.valueOf(lowValue)
				.add(BigDecimal.valueOf(Long.MAX_VALUE))
				.add(BigDecimal.valueOf(1));
	}
}
