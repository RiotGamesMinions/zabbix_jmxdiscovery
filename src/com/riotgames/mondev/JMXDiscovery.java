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

import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import org.json.*;

public class JMXDiscovery {
	private MBeanServerConnection mbsc;
	private JMXServiceURL jmxServerUrl;
	private JMXConnector jmxc;
	private String username, password;

	public JMXDiscovery(String hostname, int port, String usr, String pwd) {
		try
		{
			this.jmxServerUrl = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://" + hostname + ":" + port + "/jmxrmi");
			jmxc = null;
			mbsc = null;
			username = usr;
			password = pwd;

			if (username != null && password == null || username == null && password != null)
				throw new IllegalArgumentException("Both username and password must be set or omitted");
		}
		catch (Exception e)
		{
			// Print error
		}
	}

	protected String discoverMBeans(String key) throws Exception
	{
		try {
			HashMap<String, String[]> env = null;
			if (null != username && null != password)
			{
				env = new HashMap<String, String[]>();
				env.put(JMXConnector.CREDENTIALS, new String[] {username, password});
			}

			jmxc = JMXConnectorFactory.connect(jmxServerUrl, env);
			mbsc = jmxc.getMBeanServerConnection();

			ObjectName filter = new ObjectName(key);
			JSONArray beanList = new JSONArray();
			JSONObject mapping = new JSONObject();

			Set beans = mbsc.queryMBeans(filter, null);
			for (Object obj : beans) {
				JSONObject bean = new JSONObject();
				ObjectName beanName;

				// Return the ObjectName instance correctly for both Objects and Instances
				if (obj instanceof ObjectName)
					beanName = (ObjectName) obj;
				else if (obj instanceof ObjectInstance)
					beanName = ((ObjectInstance) obj).getObjectName();
				else
					throw new RuntimeException("Unexpected object type: " + obj);

				// Build the standing info, description and object path
				MBeanInfo mbi = mbsc.getMBeanInfo(beanName);
				bean.put("{#JMXDESC}", mbi.getDescription());
				bean.put("{#JMXOBJ}", beanName.toString());

				// Build a list of all the MBean properties as {#PROP<NAME>}
				Hashtable<String, String> pt = beanName.getKeyPropertyList();
				for (Map.Entry<String, String> prop : pt.entrySet())
					bean.put(String.format("{#PROP%s}", prop.getKey().toUpperCase()), prop.getValue());

				beanList.put(bean);
			}

			mapping.put("data", beanList);
			return mapping.toString();
		} catch (Exception e) {
			JSONArray data = new JSONArray();
			JSONObject out = new JSONObject();
			out.put("data", data);

			return out.toString();
		} finally {
			try { if (null != jmxc) jmxc.close(); } catch (java.io.IOException exception) { }

			jmxc = null;
			mbsc = null;
		}
	}
}
