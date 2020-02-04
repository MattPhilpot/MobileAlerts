package com.alerts.mobile;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


class Crypted
{
	String bytes;
	int bytelength;

	Crypted(String s, int i)
	{
		bytes = s;
		bytelength = i;
	}	
	
	static Crypted encode(String s) throws Exception
	{
		String d = "";
		String key = "1898465718984657";
		SecretKeySpec key1 = new SecretKeySpec(key.getBytes(), "AES");
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.ENCRYPT_MODE, key1);

		byte[] enc = c.doFinal(s.getBytes());

		for (byte e : enc)
		{
			String o = e + "";
			if (o.length() == 1)
				d += "000" + e;
			else
			if (o.length() == 2)
				d += "00" + e;
			else
			if (o.length () == 3)
				d += "0" + e;
			else
				d += e;
		}
		return new Crypted(d, enc.length);
	}

	static String decode(Crypted d) throws Exception
	{
		String key = "1898465718984657";
		SecretKeySpec key1 = new SecretKeySpec(key.getBytes(), "AES");
		Cipher c = Cipher.getInstance("AES");
		String s = d.bytes;
		byte[] enc2 = new byte[d.bytelength];

		for (int i = 0, x = 0; x < d.bytelength; i += 4, x++)
		{
			String f = s.substring(i, i + 4);
			if (f.contains("-"))
			{
				while(f.charAt(0)  == '0')
				{
					f = f.substring(1, f.length());
				}
			}
			enc2[x] = Byte.parseByte(f) ;
		}

		c.init(Cipher.DECRYPT_MODE, key1);
		byte[] a = c.doFinal(enc2);

		return new String(a);
	}	
}
