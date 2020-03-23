### Kylin集群运维工具
1、不同集群间数据查询一致性检测
```shell script
java -cp KylinCheck-1.0.jar com.ly.check.CheckMain /home/kylin/kylin.log >> check.log
```
2、对比两个集群的cube名称
```shell script
java -cp KylinCheck-1.0.jar com.ly.meta.CubeCompareMain >> compare.log
```