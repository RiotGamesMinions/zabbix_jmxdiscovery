# zabbix_jmxdiscovery

This project aims to make auto discovery of JMX attributes easier for Zabbix monitoring by allowing you to use JMX patterns when generating discovery rules. This program will expose the ObjectName, Description and any properties set on the JMX MBean to Zabbix. Properties will be exposed as `{#PROP<MBean Property named in all uppercase}`. For example, if your MBean has a property called `UPTIME` it will be exposed to Zabbix as `{#PROPUPTIME}`.

## Usage
`java -jar JMXDiscovery <pattern> <host[:port]> [username] [password]`

### Pattern
The pattern is a valid JMX [ObjectName](http://docs.oracle.com/javase/7/docs/api/javax/management/ObjectName.html). Example: `org.hornetq:module=JMS,type=Queue,name=*`

### Hostname & Port
The hostname is required and can be either IP or DNS hostname. If not supplied the port will default to being over nine thousand, or in other words `9001`

### Username & Password
If you have authentication enabled on your JMX server, you can supply the username and password to authenticate with here. Both username and password must either be blank or set. An error will be raised if only one is supplied.

## Compile and install
This project uses Apache Ant to compile the source code into a Jar file. By default just running *ant* in the root of the will output both the Jar file and the appropriate wrapper script into a folder called `install`, which is located in the root of the project. If however you would like to have ant install the files directly to your External Scripts folder, you can override the install location parameter during the *ant* invocation.

Example: `ant -Ddir.install=/usr/local/zabbix/externalscripts`

## Example output
The output below is an example of what would be returned using the pattern `org.hornetq:module=JMS,type=Queue,name=*` on a HornetQ server.
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
## Zabbix integration
To use this program in Zabbix, place the `jmx_discovery` script in the External Checks ( *extchk* here-after) folder configured in Zabbix. Next, create a new folder called lib under the *extchk* folder and copy the `JMXDiscovery.jar` file into the newly created folder. Ensure that the `jmx_discovery` script is executable (`chmod 755 jmx_discovery`).

### Creating discovery rules
Once the files have been copied to the *extchk* folder, you can then access it from any check in Zabbix by defining the the type to be `External Check` and the key must be in the format of 
```
jmx_discovery[<pattern>,<hostname[:port]>,<optional username>,<optional password>]
```
To generate the output shown in the example above, on a HornetQ JMX server with no authentication and a JMX remote port set to 6445 you would use the key `jmx_discovery["org.hornetq:module=JMS,type=Queue,name=*",{HOST.IP}:6445]`

## Contributors
 * Asbjorn Kjaer (http://github.com/bunjiboys/)

## How to contribute
Please refer to `CONTRIBUTING.md` for information on how to contribute to this project.

If you have any general questions or concerns please contact Asbjorn Kjaer <akjaer@riotgames.com>.
