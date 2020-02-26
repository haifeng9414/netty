# Netty学习

## 基础
### Netty核心构件
* Channel 
  
  代表一个实体（如一个硬件设备、一个文件、一个网络套接字或者一个能够执行一个或者多个不同的I/O操作的程序组件）的开放连接，如读操作和写操作，也可以看作是传入（入站）或传出（出站）数据的载体，可以被打开或者被关闭，连接或者断开连接。

* 回调 
  
  即各种ChannelHandler的实现

* Future 
 
  和JDK中的Future功能一样，不过JDK的Future只提供cancel、isDone和get等方法，对于Future是否完成需要异步操作调用方手动检查，所以netty扩展了JDK的Future，定义了ChannelFuture接口，该接口提供了添加监听器的方法，消除了异步操作调用方手动检查的必要，使得Future也具有回调能力。netty中的所有I/O操作都是异步的，所以返回值都会是Future。
  
* 事件和ChannelHandler