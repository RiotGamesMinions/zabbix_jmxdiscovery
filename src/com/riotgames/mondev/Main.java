package com.riotgames.mondev;

public class Main {

    public static void main(String[] args) {
		JMXChecker jmx;
		String pattern, host, usr, pwd;
		int port = 9001;
		pattern = host = usr = pwd = null;

		if (args.length == 2 || args.length == 4) {
			pattern = args[0];

			if (args[1].contains(":")) {
				String[] parts = args[1].split(":");
				host = parts[0];
				port = Integer.parseInt(parts[1]);
			} else
				host = args[1];

            if (args.length == 4) {
				usr = args[2];
				pwd = args[3];
			}
		} else
			usage();

		jmx = new JMXChecker(host, port, usr, pwd);

		try {
			System.out.println(jmx.discoverMBeans(pattern));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static void usage() {
		System.out.println("Usage: java -jar JMXChecker.jar <pattern> <hostname[:port]> [username] [password]\n");
		System.exit(-1);
	}
}
