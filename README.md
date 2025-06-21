## 运行说明
1. 启动MySQL数据库，并创建数据库lease，导入lease.sql文件。
2. 启动Redis数据库。
3. 启动RabbitMQ消息队列。
4. 启动MinIO对象存储服务。
5. 将`application.yml`文件中的mysql、Redis、RabbitMQ、MinIO等根据自己的服务器地址、端口、账号密码等进行配置。
6. 阿里云AI服务的密钥`DASHSCOPE_API_KEY`和阿里云的短信服务的`ACCESS_KEY_ID`和`ACCESS_KEY_SECRET`需要自己在阿里云的控制台上申请，然后加入到环境变量中。
7. 两个服务都需要加入以下启动参数：
```
--add-opens java.base/java.lang=ALL-UNNAMED 
```

## 项目结构
```
lease
├── common（公共模块——工具类、公用配置等）
│   ├── pom.xml
│   └── src
├── model（数据模型——与数据库相对应的实体类）
│   ├── pom.xml
│   └── src
├── web（Web模块）
│   ├── pom.xml
│   ├── web-admin（后台管理系统Web模块）
│   │   ├── pom.xml
│   │   └── src
│   └── web-app（移动端Web模块）
│       ├── pom.xml
│       └── src
├── lease.sql（数据库脚本文件）
└── pom.xml（父模块POM文件）
```