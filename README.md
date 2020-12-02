# Online Port Scanner

### Overview

这是一个Material Design的端口扫描器，前端使用mdui框架开发，后台使用Springboot+SQLite开发。支持单IP单端口快速扫描，指定IP地址段和端口范围批量扫描，使用多线程提高扫描性能。用于检测指定的端口是否开放，并给出开放端口的相关信息。

### Quick Start

[在线体验地址-由于服务器资源有限，高级扫描功能具有使用限制](http://portscanner.hellochaos.cn/)

### Features

##### 快速扫描：

在输入域名/IP地址和端口，点击右下角开始扫描按钮即可获得扫描结果

![image-20200621232844125](imgs/20201202095605)

##### 高级扫描：

分别输入起始IP，终止IP，起始端口，终止端口即可提交高级扫描任务

![image-20200621233115859](imgs/20201202105321)

##### 任务列表：

为防止请求阻塞，高级扫描中提交的任务将在这里显示

![image-20200621233303559](imgs/20201202112516)

##### 扫描报告：

点击任务列表中某一个任务的结果中的扫描报告链接（result）可以查看扫描结果

![image-20200621233430810](imgs/20201202105348)

### Install

你可以下载源代码并自行部署进行使用，本地部署之后访问项目的地址是http://localhost:8080

### License

在遵循CPL3协议相关规定的前提下，你可以自由地使用本项目源代码。

### Author

[@Yuchao Huang- Origin Author](https://github.com/misterchaos/)

Email: misterchaos@163.com

### About

欢迎star/fork/issue等各种方式参与[本项目](https://github.com/misterchaos/online-port-scanner)的开发~