# zabbix_jmxdiscovery

This project aims to make auto discovery of JMX attributes easier for Zabbix monitoring, but allowing you to use JMX patterns when generating discovery rules. This program will expose the ObjectName, Description and any properties set on the JMX MBean to Zabbix.

## Usage
`java -jar JMXDiscovery <pattern> <host[:port]> [username] [password]`

### Pattern
The pattern is a valid JMX [ObjectName](http://docs.oracle.com/javase/7/docs/api/javax/management/ObjectName.html). Example: `org.hornetq:module=JMS,type=Queue,name=*`

### Hostname & Port
The hostname is required and can be either IP or DNS hostname. If not supplied the port will default to being over nine thousand, or in other words `9001`

### Username & Password
If you have authentication enabled on your JMX server, you can supply the username and password to authenticate with here. Both username and password must either be blank or set. An error will be raised if only one is supplied.

## Example output
```json
{
    "data": [
        {
            "{#PROPMODULE}": "JMS",
            "{#PROPTYPE}": "Queue",
            "{#JMXOBJ}": "org.hornetq:module=JMS,name=\"DLQ\",type=Queue",
            "{#JMXDESC}": "Information on the management interface of the MBean",
            "{#PROPNAME}": "\"DLQ\""
        },
        {
            "{#PROPMODULE}": "JMS",
            "{#PROPTYPE}": "Queue",
            "{#JMXOBJ}": "org.hornetq:module=JMS,name=\"platform\",type=Queue",
            "{#JMXDESC}": "Information on the management interface of the MBean",
            "{#PROPNAME}": "\"platform\""
        },
        {
            "{#PROPMODULE}": "JMS",
            "{#PROPTYPE}": "Queue",
            "{#JMXOBJ}": "org.hornetq:module=JMS,name=\"ExpiryQueue\",type=Queue",
            "{#JMXDESC}": "Information on the management interface of the MBean",
            "{#PROPNAME}": "\"ExpiryQueue\""
        }
    ]
}
```
