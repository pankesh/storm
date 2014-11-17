Storm POC
=========

Setting up a cluster
--------------------
* Details here [Setting-up-a-Storm-cluster](https://storm.apache.org/documentation/Setting-up-a-Storm-cluster.html)
* Set up a Zookeeper cluster
 * I used the Zookeeper instance that I setup with Kafka
 * Started using ```
project/libs/kafka_2.9.2-0.8.1.1/bin/zookeeper-server-start.sh project/libs/kafka_2.9.2-0.8.1.config/zookeeper.properties
```
```
* Update the storm cluster configuration in ```
storm.yaml
```
 * ```
storm.zookeeper.servers: - "localhost"
```
 * Use default for ```
storm.zookeeper.port
```
 * Use default for ```
storm.local.dir
```. This default to the storm-local in the /bin folder.
 * ```
nimbus.host: "localhost"
```
 * ```
supervisor.slots.ports:     - 6700 - 6701
```
* Launch the various storm deamon processes
 * ''Nimbus'': Run the command ```
bin/storm nimbus
``` under supervision on the master machine.
 * ''Supervisor'': Run the command ```
bin/storm supervisor
``` under supervision on each worker machine. The supervisor daemon is responsible for starting and stopping worker processes on that machine.
 * ''UI'': Run the Storm UI (a site you can access from the browser that gives diagnostics on the cluster and topologies) by running the command ```
bin/storm ui
``` under supervision. The UI can be accessed by navigating your web browser to http://{nimbus_host}:8080.

Creating Topologies
-------------------
* Refer to code at TaxAndDiscountTopology.java [https://github.com/pankesh/storm/blob/master/src/main/java/org/panksdmz/storm/] as an example for creating & running local topologies.
* Refer to code at TaxAndDiscountTopologyProduction.java [https://github.com/pankesh/storm/blob/master/src/main/java/org/panksdmz/storm/] as an example for creating & running topologies in production.
 * This step requires to create the jar & submit the jar file to storm using
  * ```
storm jar <path/to/allmycode.jar> <org.me.MyTopology> [arg1] [arg2] [arg3]
```
  * ```
project/libs/apache-storm-0.9.2-incubating/bin/storm jar project/workspace/StormPOC/build/libs/StormPOC-1.0.jar org.panksdmz.storm.TaxAndDiscountTopologyProduction
```

