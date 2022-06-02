# zkNet

the network for zk-rollup powerd blockchain services

**dependency**
- Spring Boot 2.5.13
- jsonrpc4j 1.6
- web3j 4.8.9
- log4j2 2.17.2

**env**
- JDK 8
- MySQL 5.7+
- Redis

 **zknet api run**
 ```
$git clone https://github.com/HappyDAO/zknet-service.git
cd zknet-service
mvn clean install -Ptest
cd gateway/target
java -jar zknet-test.jar --spring.profiles.active=test
curl -X POST -H 'x-api-key:333' --data '{"jsonrpc":"2.0","method":"zkn_getTokenById","id":"10","params":[1]}' http://localhost:8088/zknet

code entrance class:com.zknet.gateway.jsonrpc.api.ZkNetServerApi

the full flow test see:com.zknet.gateway.biz.impl.ZkSync2TxBizImplTest.fullFlowTest()
```

**message server run**
```
cd zknet-service
mvn clean install -Ptest
cd message/target
java -jar zknet-message-test.jar --spring.profiles.active=test
```
**modules**
```
zknet-service
|-gateway JSON-RPC api server,depends on engine module
|-message message server,include MQ client、WebSocket client and WebSocket Server,depends on engine module
|-engine core module,DB、Redis、web3 operation 
|-doc
| |-db.sql sql statement
```
