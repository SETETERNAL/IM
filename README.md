# IM

#### 介绍
使用netty实现的一个简单的通信服务demo

#### 技术栈


| 运行环境 | 版本 |
| --- | --- |
| JDK | 1.8+ |
| redis | 5.0 |
| zookeeper | 3.5.9 |

#### 软件架构

后续补充...

#### 关于配置文件（优先级由上到下）

1.  读取路径：/data/im/config.properties
2.  resource/config.properties  


#### 安装教程

1.  修改server中的redis和zookeeper配置
2.  根据ServerMain.java启动server服务（如果需要集群可使用nginx做负载均衡）
3.  修改ClientMain.host和ClientMain.port，启动client服务
4.  运行RedisSingleUtil初始化用户数据

**ps: idea需要安装lombok插件**
