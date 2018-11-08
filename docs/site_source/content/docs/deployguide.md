---
title: 部署指南
type: docs
---

# 部署指南

## 前置环境

1. 安装MySQL
	
	(过程略)

2. 准备数据库

		//创建DB
		CREATE DATABASE flyer_job CHARACTER SET utf8 COLLATE utf8_general_ci;	
		//导入建表脚本
		cat /path/to/flyer_job.sql | mysql -uroot -pxxx 

		//创建用户
		create user flyer_job_w identified by '123456';

		//权限
		grant all privileges on flyer_job.* to flyer_job_w;
		flush privileges;



## 获取软件安装包

1. 修改启动脚本
	
	//

2. 启动
	
	//

## 静态资源单独部署(可选)






