/*
Copyright 2014 Riot Games

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.riotgames.mondev;

public class Main {

    public static void main(String[] args) {
		JMXDiscovery jmx;
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

		jmx = new JMXDiscovery(host, port, usr, pwd);

		try {
			System.out.println(jmx.discoverMBeans(pattern));
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static void usage() {
		System.out.println("Usage: java -jar JMXDiscovery.jar <pattern> <hostname[:port]> [username] [password]\n");
		System.exit(-1);
	}
}
