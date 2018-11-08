---
title: 架构
type: docs
---
## 架构图

![arch](../../imgs/flyerjob_architecture.png)

## 说明

1. 尽可能的轻量级，不依赖第三方有状态存储的组件(只依赖MySQL做任务持久化)
2. 由quartz cluster保证任务高可用。
3. 每一个job client都会和每一个server node建立socket通道。
4. server node可以扩容，心跳协议检测到版本号变化后会重建连接。
5. 如果任务数量较大，可建立多个scheduler，然后对任务进行sharding。这也来自quartz官方的建议。

