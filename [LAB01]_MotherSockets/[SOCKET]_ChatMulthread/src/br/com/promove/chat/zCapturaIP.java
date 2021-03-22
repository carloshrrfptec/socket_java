package br.com.promove.chat;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

@SuppressWarnings("unused")
public class zCapturaIP {

	@SuppressWarnings({ "rawtypes", "unused" })
	public static void main(String[] args) {
		try {
			Enumeration nis = null;

			nis = NetworkInterface.getNetworkInterfaces();

			String ip = null;
			while (nis.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) nis.nextElement();
				Enumeration ias = ni.getInetAddresses();

				while (ias.hasMoreElements()) {
					InetAddress ia = (InetAddress) ias.nextElement();

					if (ia.getHostAddress().contains("10.132")) {// Nesse if está a charada, sendo que eu sei que meu ip
																	// começa com 10.132, por exemplo
						ip = ia.getHostAddress();
					}
					System.out.println(ia.getHostAddress());
					if (!ni.getName().equals("lo")) {
						// System.out.println(ia.getHostAddress());
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
}
