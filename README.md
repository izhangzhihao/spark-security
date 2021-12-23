
Submarine Spark Security Plugin is built using [Apache Maven](http://maven.apache.org). To build it, `cd` to the root direct of submarine project and run:

```bash
mvn clean package -Dmaven.javadoc.skip=true -DskipTests -pl :submarine-spark-security
```

By default, Submarine Spark Security Plugin is built against Apache Spark `2.3.x` and Apache Ranger `1.1.0`, which may be incompatible with other Apache Spark or Apache Ranger releases.

Currently, available profiles are:

**Spark**: `-Pspark-2.3`, `-Pspark-2.4`, `-Pspark-3.0`

**Ranger**: `-Pranger-1.2`, `-Pranger-2.0`, `-Pranger-2.1`


ACL Management for Apache Spark SQL with Apache Ranger, enabling:

- Table/Column level authorization
- Row level filtering
- Data masking


Security is one of fundamental features for enterprise adoption. [Apache Ranger™](https://ranger.apache.org) offers many security plugins for many Hadoop ecosystem components, 
such as HDFS, Hive, HBase, Solr and Sqoop2. However, [Apache Spark™](http://spark.apache.org) is not counted in yet. 
When a secured HDFS cluster is used as a data warehouse accessed by various users and groups via different applications wrote by Spark and Hive, 
it is very difficult to guarantee data management in a consistent way.  Apache Spark users visit data warehouse only 
with Storage based access controls offered by HDFS. This library enables Spark with SQL Standard Based Authorization. 

## Build

Please refer to the online documentation - [Building submarine spark security plguin](build-submarine-spark-security-plugin.md)

## Quick Start

Three steps to integrate Apache Spark and Apache Ranger.

### Installation

Place the submarine-spark-security-&lt;version&gt;.jar into `$SPARK_HOME/jars`.

### Configurations

#### Settings for Apache Ranger

Create `ranger-spark-security.xml` in `$SPARK_HOME/conf` and add the following configurations
for pointing to the right Apache Ranger admin server.


```xml

<configuration>

    <property>
        <name>ranger.plugin.spark.policy.rest.url</name>
        <value>https://10.102.13.36:6182</value>
    </property>

    <property>
        <name>ranger.plugin.spark.policy.rest.ssl.config.file</name>
        <value>/etc/spark/conf/ranger-spark-policymgr-ssl.xml</value>
    </property>

    <property>
        <name>ranger.plugin.spark.service.name</name>
        <value>cm_hive</value>
    </property>

    <property>
        <name>ranger.plugin.spark.policy.cache.dir</name>
        <value>/tmp</value>
    </property>

    <property>
        <name>ranger.plugin.spark.policy.pollIntervalMs</name>
        <value>5000</value>
    </property>

    <property>
        <name>ranger.plugin.spark.policy.source.impl</name>
        <value>org.apache.ranger.admin.client.RangerAdminRESTClient</value>
    </property>

</configuration>
```

Create `ranger-spark-audit.xml` in `$SPARK_HOME/conf` and add the following configurations
to enable/disable auditing.

```xml
<configuration>
    <property>
        <name>xasecure.audit.is.enabled</name>
        <value>true</value>
    </property>
</configuration>


ranger-spark-policymgr-ssl.xml

<configuration xmlns:xi="http://www.w3.org/2001/XInclude">
	<property>
    <name>xasecure.policymgr.clientssl.truststore</name>
    <value>/home/bigdatauser/cm-auto-global_truststore.jks</value>
  </property>
  <property>
    <name>xasecure.policymgr.clientssl.truststore.credential.file</name>
    <value>jceks://file/home/bigdatauser/ranger-truststore.jceks</value>
  </property>
  <property>
    <name>xasecure.policymgr.clientssl.keystore</name>
    <value>/home/bigdatauser/cm-auto-host_keystore.jks</value>
  </property>
  <property>
    <name>xasecure.policymgr.clientssl.keystore.credential.file</name>
    <value>jceks://file/home/bigdatauser/ranger-keystore.jceks</value>
  </property>
  <property>
    <name>xasecure.policymgr.clientssl.keystore.type</name>
    <value>jks</value>
  </property>
  <property>
    <name>xasecure.policymgr.clientssl.truststore.type</name>
    <value>jks</value>
  </property>
</configuration>

```

#### Settings for Apache Spark-

You can configure `spark.sql.extensions` with the `*Extension` we provided.
For example, `spark.sql.extensions=org.apache.submarine.spark.security.api.RangerSparkAuthzExtension`

Currently, you can set the following options to `spark.sql.extensions` to choose authorization w/ or w/o
extra functions.

| option | authorization | row filtering | data masking |
|---|---|---|---|
|org.apache.submarine.spark.security.api.RangerSparkAuthzExtension| √ | × | × |
|org.apache.submarine.spark.security.api.RangerSparkSQLExtension| √ | √ | √ |



## Apache Submarine Community

Read the [Apache Submarine Community Guide](https://submarine.apache.org/docs/community/README)

How to contribute [Contributing Guide](https://submarine.apache.org/docs/community/contributing)

Issue Tracking: https://issues.apache.org/jira/projects/SUBMARINE

## User Document

See [User Guide Home Page](https://submarine.apache.org/docs/)

## Developer Document

See [Developer Guide Home Page](https://submarine.apache.org/docs/devDocs/Development/)

## Roadmap

What to know more about what's coming for Submarine? Please check the roadmap out: https://cwiki.apache.org/confluence/display/SUBMARINE/Roadmap

## License

The Apache Submarine project is licensed under the Apache 2.0 License. See the [LICENSE](./LICENSE) file for details.
