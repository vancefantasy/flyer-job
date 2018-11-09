---
title: 客户端配置
type: docs
bookShowToC: true
---

# 客户端配置

## 配置列表

配置项       |类型       |配置名  |是否必选  |备注
------------|-----------|-----------|--------|-----------
appCode       |string        |应用编码|是|例：my-app
disable       |boolean        |是否开启flyerjob|是|true关闭 false开启，默认false（开启）
depend       |boolean        |是否强依赖|否|true是 false否，默认true(强依赖)
servers       |string        |flyer job服务器|是|例：1.1.1.1:20180,2.2.2.2:20180
vhost       |string        |vhost|否|环境设置
corePoolSize       |int        |核心线程数|否|默认值：3
maxPoolSize       |int        |最大线程数|否|默认值：10
keepAliveTime       |int        |线程空闲超时时间|否|单位：s，默认值：300s

## 进一步解释

- disable

- depend

- servers

- vhost

