# 记录学习java（牛客网高级项目）的历程
## Day01
## 一、 学习利用idea将项目上传到github中去
### 1.创建本地仓库。
![创建本地仓库](https://github.com/CodeTxp/Pictures/blob/master/%EF%BC%88%E7%89%9B%E5%AE%A2%E7%BD%91%EF%BC%89%E9%A1%B9%E7%9B%AE%E5%AD%A6%E4%B9%A0/20160317093849090.png)

### 2.提交代码到git
![提交代码到git](https://github.com/CodeTxp/Pictures/blob/master/%EF%BC%88%E7%89%9B%E5%AE%A2%E7%BD%91%EF%BC%89%E9%A1%B9%E7%9B%AE%E5%AD%A6%E4%B9%A0/20160317093852101.png)

右键点击src或代码文件，Git -- Add -- Commit（先Add再Commit）

### 3.项目远程提交
#### 1.在Github上建立仓库
#### 2.在本地配置远程仓库
> * cd  本地仓库目录
> * git remote add origin 你项目的地址
> * git push -u origin master


# Day02
------
今天主要学习的知识点：
> * 如何建立Springboot工程
> * Velocity模板语言的学习
> * Cotroller
> * AOP的学习
> * IoC学习


##  1、来看怎么建立一个SpringBoot工程（有两种方式）
### （1）.可以通过Idea来创建一个SpringBoot项目
### 第一步
选择File –> New –> Project –>Spring Initialer –> 点击Next 
![第一步](https://github.com/CodeTxp/Pictures/blob/master/%EF%BC%88%E7%89%9B%E5%AE%A2%E7%BD%91%EF%BC%89%E9%A1%B9%E7%9B%AE%E5%AD%A6%E4%B9%A0/360%E6%A1%8C%E9%9D%A2%E6%88%AA%E5%9B%BE20190408163020.jpg)
### 第二步
自己修改 Group 和 Artifact 字段名 –>点击next
![第二步](https://github.com/CodeTxp/Pictures/blob/master/%EF%BC%88%E7%89%9B%E5%AE%A2%E7%BD%91%EF%BC%89%E9%A1%B9%E7%9B%AE%E5%AD%A6%E4%B9%A0/360%E6%A1%8C%E9%9D%A2%E6%88%AA%E5%9B%BE20190408163115.jpg)
### 第三步
选择需要你的依赖
![第三步](https://github.com/CodeTxp/Pictures/blob/master/%EF%BC%88%E7%89%9B%E5%AE%A2%E7%BD%91%EF%BC%89%E9%A1%B9%E7%9B%AE%E5%AD%A6%E4%B9%A0/360%E6%A1%8C%E9%9D%A2%E6%88%AA%E5%9B%BE20190408171117.jpg)
### 第四部
finish
![第4步](https://github.com/CodeTxp/Pictures/blob/master/%EF%BC%88%E7%89%9B%E5%AE%A2%E7%BD%91%EF%BC%89%E9%A1%B9%E7%9B%AE%E5%AD%A6%E4%B9%A0/360%E6%A1%8C%E9%9D%A2%E6%88%AA%E5%9B%BE20190408171127.jpg)

