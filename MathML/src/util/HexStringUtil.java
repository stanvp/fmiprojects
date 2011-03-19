package util;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public final class HexStringUtil {
	public static String toHex(String str) {
		char[] chars = str.toCharArray();

		StringBuffer hex = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			hex.append(Integer.toHexString((int) chars[i]));
		}

		return hex.toString();
	}

	static CharsetEncoder asciiEncoder = Charset.forName("US-ASCII")
			.newEncoder(); // or "ISO-8859-1" for ISO Latin 1

	public static boolean isPureAscii(String v) {
		return asciiEncoder.canEncode(v);
	}
}