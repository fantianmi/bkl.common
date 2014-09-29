package com.km.common.client;



public class ClientMain {
	public static void main(String[] args) throws Exception {
		HttpObjectClient hoc = new HttpObjectClient();
		String host = "ingchat.com";
		int port = 8080;
		String path = "weiqiche/api/helloservlet";
		
		String out = hoc.post(host, port, path, "input", String.class);
		System.out.println(out);
	}
}
