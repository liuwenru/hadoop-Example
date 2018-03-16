# Hive2数据仓库环境搭建

本篇主要介绍`Hive2`数据仓库的搭建过程。



# 一、基本环境信息说明

`Hive2`数据仓库是建立在`Hadoop`基础之上的，`Hive`是解决了`MapReduce`难以编程以及难以复用的问题，将一些`MapReduce`的通用操作分装成`HSQL`类似`RDBMS`关系型数据库的方式访问数据。本篇所属的环境信息基于前面目录中搭建好的`HDFS`目录


# 二、环境搭建步骤

我们选用`Hive2`作为实验环境，详细的版本是`apache-hive-2.3.2`。关于`Hive2`的详细安装步骤可以参照[官方配置说明](https://cwiki.apache.org/confluence/display/Hive/GettingStarted#GettingStarted-InstallationandConfiguration)


## 2.1、 安装配置Hive2

基于之前`HDFS`的环境，我们选取`datanode3`作为`Hive2`运行的服务器，从[此处下载Hive2](http://192.168.203.10/hadoop/apache-hive-2.3.2-bin.tar.gz)。
```bash
datanode3 Shell> curl  -O http://192.168.203.10/hadoop/apache-hive-2.3.2-bin.tar.gz #因为基于之前`HDFS`环境所以不需要安装JDK
datanode3 Shell> tar -zxvf apache-hive-2.3.2-bin.tar.gz -C /opt/   #解压HIVE
datanode3 Shell> ln -s /opt/apache-hive-2.3.2-bin /usr/local/hive  
datanode3 Shell> vim /etc/profile
.......省略部分输出......
# Hive2 Install Setting
export HIVE_HOME=/usr/local/hive
export PATH=$PATH:$HIVE_HOME/bin
```

至此`Hive2`的实验环境部署已经完成，笔者大致的看了一下`Hive2`的启动脚本，`Hive`在启动时会根据环境变量中指定的`HADOOP_HOME`等环境变量自动的检测`hadoop`中的配置，比如如何调用`Yarn`等资源


## 2.2、 初始化配置Hive2

通过学习`Hive2`的构架知识我们可以知道，对于`Hive2`是一个`写模式数据库`，`Hive`在工作时会将`表中的元数据信息放置在关系型数据库(MySQL,笔者在H3C的平台上还看到了使用PG作为元数据管理的)或者自己实现的本地derby中`，两者在功能和性能上有很大的推荐，官方也推荐对于测试过程中可以使用`derby`进行测试，且该数据库下启动的`Hive2`只支持一个会话连接。如果配置使用`MySQL`作为元数据管理，则需要一些特殊配置，下面介绍如何配置使用`MySQL`作为元数据管理。
在配置`Hive2`前请确认当前环境变量中是否指定存在`HADOOP_HOME`该变量，在使用`MySQL`作为元数据管理时，需要先提前安装一个`MySQL`实例，这里不再进行赘述。
```bash
Shell> cd ${HIVE_HOME}/conf #Hive主要的配置在此目录
Shell> cp hive-env.sh.template hive-env.sh #配置hive中使用的特殊的环境变量可以配置在此处
Shell> vim hive-site.xml #新增配置文件，配置hive的元数据存储信息
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?><!--
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
--><configuration>
  <!-- WARNING!!! This file is auto generated for documentation purposes ONLY! -->
  <!-- WARNING!!! Any changes you make to this file will be ignored by Hive.   -->
  <!-- WARNING!!! You must make your changes in hive-site.xml instead.         -->
  <!-- Hive Execution Parameters -->
  <property>
      <name>hive.metastore.warehouse.dir</name>
      <value>/user/hive/warehouse</value>
  </property>
  <property>
      <name>hive.querylog.location</name>
      <value>/user/hive/log</value>
  </property>
  <property>
    <name>javax.jdo.option.ConnectionURL</name>
    <value>jdbc:mysql://192.168.208.60:3306/hive?createDatabaseIfNotExist=true&amp;characterEncoding=UTF-8&amp;useSSL=false</value>
  </property>
  <property>
    <name>javax.jdo.option.ConnectionDriverName</name>
    <value>com.mysql.jdbc.Driver</value>
  </property>
  <property>
    <name>javax.jdo.option.ConnectionUserName</name>
    <value>root</value>
  </property>
  <property>
    <name>javax.jdo.option.ConnectionPassword</name>
    <value>Gepoint</value>
  </property>
</configuration>
```
关于`hive2`的配置项的作用等等可以参考此处[官方文档配置说明](https://cwiki.apache.org/confluence/display/Hive/Configuration+Properties#ConfigurationProperties-HiveServer2)，配置完成以上节点信息后，在使用`Hive`前需要先初始化元数据信息，以及文件系统目录，命令如下：
```bash
Shell> schematool -dbType mysql -initSchema
Shell> hdfs dfs -mkdir /tmp
Shell> hdfs dfs -mkdir -p /user/hive/warehouse
Shell> hdfs dfs -chmod g+w   /user/hive/warehouse
Shell> hdfs dfs -chmod -R g+w   /user/hive/warehouse
```

执行完如上信息后即可完成`Hive`环境的初始化工作。下面就可以启动`HiveServer2`环境。

```bash
Shell> hiveserver2 #启动hiveserver2,默认监听端口为10000


```

如何自定义自己的机器环境配置可以参考[官方文档的配置说明](https://cwiki.apache.org/confluence/display/Hive/Setting+Up+HiveServer2)




## 2.3、 运行Hive2并使用JDBC方式访问hive2

