package com.web.utils;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

public class GenerateKeyUtil {

	private static final Logger log = Logger.getLogger(GenerateKeyUtil.class);

	/**
	 * 生成激活码
	 * 
	 * @param batch
	 *            批次
	 * @param num
	 *            生成的数量
	 * @return
	 */
	public static Set<String> generateKey(short batch, short num) {
		Set<String> result = new HashSet<String>();
		SimpleRandom random = new SimpleRandom();
		for (short i = 1; i <= num; i++) {
			byte[] bytes = new byte[8];
			bytes[0] = (byte) (batch & 0xff);
			bytes[1] = (byte) ((batch & 0xff00) >> 8);

			bytes[2] = (byte) (i & 0xff);
			bytes[3] = (byte) ((i & 0xff00) >> 8);

			int randomNum = random.next(100, 10000);
			byte[] int_bytes = ByteUtil.getBytes(randomNum);

			bytes[4] = int_bytes[0];
			bytes[5] = int_bytes[1];
			bytes[6] = int_bytes[2];
			bytes[7] = int_bytes[3];
			// 转换为一个long型
			long l = ByteUtil.getLong(bytes);

			String key = Long.toString(l, 36);
			if (result.contains(key)) {
				log.info("生成的key重复了。key=" + key);
			} else {
				result.add(key);
				// log.info("key="+key);
			}
		}
		return result;
	}

	/**
	 * 通过激活码的Key计算出批次（batch）
	 * 
	 * @param key
	 * @return
	 */
	public static short getBatchByKey(String key) {
		try {
			// 将36进制的字符串转成十进制的long
			long flag = Long.valueOf(key, 36);
			// long转byte
			byte[] bytes = ByteUtil.getBytes(flag);
			// byte的前两位就是我们要得批次号了
			byte[] batch_byte = new byte[2];
			batch_byte[0] = bytes[0];
			batch_byte[1] = bytes[1];
			short batch = ByteUtil.getShort(batch_byte);
			if (batch > 0) {
				return batch;
			}
		} catch (NumberFormatException e) {
			log.error(e);
		}
		return -1;
	}

	public static void main(String[] args) {
		// generateKey((short)1, (short)1);
		log.info("batch=" + getBatchByKey("asdasdasdasd"));
	}
}
