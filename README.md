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
#### 第一步
选择File –> New –> Project –>Spring Initialer –> 点击Next 
![第一步](https://github.com/CodeTxp/Pictures/blob/master/%EF%BC%88%E7%89%9B%E5%AE%A2%E7%BD%91%EF%BC%89%E9%A1%B9%E7%9B%AE%E5%AD%A6%E4%B9%A0/360%E6%A1%8C%E9%9D%A2%E6%88%AA%E5%9B%BE20190408163020.jpg)
#### 第二步
自己修改 Group 和 Artifact 字段名 –>点击next
![第二步](https://github.com/CodeTxp/Pictures/blob/master/%EF%BC%88%E7%89%9B%E5%AE%A2%E7%BD%91%EF%BC%89%E9%A1%B9%E7%9B%AE%E5%AD%A6%E4%B9%A0/360%E6%A1%8C%E9%9D%A2%E6%88%AA%E5%9B%BE20190408163115.jpg)
#### 第三步
选择需要你的依赖
![第三步](https://github.com/CodeTxp/Pictures/blob/master/%EF%BC%88%E7%89%9B%E5%AE%A2%E7%BD%91%EF%BC%89%E9%A1%B9%E7%9B%AE%E5%AD%A6%E4%B9%A0/360%E6%A1%8C%E9%9D%A2%E6%88%AA%E5%9B%BE20190408171117.jpg)
#### 第四部
finish
![第4步](https://github.com/CodeTxp/Pictures/blob/master/%EF%BC%88%E7%89%9B%E5%AE%A2%E7%BD%91%EF%BC%89%E9%A1%B9%E7%9B%AE%E5%AD%A6%E4%B9%A0/360%E6%A1%8C%E9%9D%A2%E6%88%AA%E5%9B%BE20190408171127.jpg)
### （2）.也可以通过这种方式
![第4步](https://github.com/CodeTxp/Pictures/blob/master/%EF%BC%88%E7%89%9B%E5%AE%A2%E7%BD%91%EF%BC%89%E9%A1%B9%E7%9B%AE%E5%AD%A6%E4%B9%A0/1316820-10b71e8e5cdfffc1.png)


## 2、Velocity模板语言的学习以及Springboot的使用(这里学习的是thymeleaf语言，因为Springboot从1.4之后开始就不再支持Velocity)
### 1.先看看SpringBoot的使用
#### @Controller 处理http请求
例如
```java
    @RequestMapping(path = {"/","/index"}, method = {RequestMethod.GET})
    @ResponseBody
    public String index(HttpSession session) {
        return "hello world ";
    }
```
> @Controller 用来响应页面，@Controller必须配合模版来使用spring-boot 支持多种模版引擎包括： 
1，FreeMarker 
2，Groovy 
3，Thymeleaf （Spring 官网使用这个） 
4，Velocity 
5，JSP 

> @ResponseBody返回的是json数据
> Spring4之后新加入的注解，原来返回json需要@ResponseBody和@Controller配合。即@RestController是@ResponseBody和@Controller的组合注解。


#### RequestMapping的作用是：配置url映射
- @RequestMapping此注解即可以作用在控制器的某个方法上，也可以作用在此控制器类上。
- 当控制器在类级别上添加@RequestMapping注解时，这个注解会应用到控制器的所有处理器方法上。处理器方法上的@RequestMapping注解会对类级别上的@RequestMapping的声明进行补充。
  
#### 其他需要注意的是

``` 
    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") String groupId,
                          @RequestParam(value = "type", defaultValue = "1") int type,
                          @RequestParam(value = "key", defaultValue = "zz", required = false) String key) {
        return String.format("Profile page of %s, %d, t:%d, k:%s ", groupId, userId, type, key);
    }
```
这段代码中出现了@@RequestParam  @RequestParam
- @RequestParam  获取url中的数据, 当使用@RequestMapping URI template 样式映射时，即 someUrl/{paramId}, 这时的paramId可通过 @Pathvariable注解绑定它传过来的值到方法的参数上，是从一个URI模板里面来填充。
- @RequestParam  获取请求参数的值，该注解有三个属性： value、defaultValue、required； value用来指定要传入值的id名称，defaultValue指定默认值，required用来指示参数是否必须绑定；是从request里面拿取值。
##### 上面的请求地址  
- 是 http://localhost:8080/profile/user/2  返回的就是
Profile page of user, 2, t:1, k:zz
因为指定了默认值

- 是 http://localhost:8080/profile/user/2?type=9&key=9 返回的就是
Profile page of user, 2, t:9, k:9


### 2.借着来看看thymeleaf的简单的使用
#### 首先是加入需要额依赖
```
       <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
```

#### 再来看一段代码
```
    @RequestMapping("/vmstart")
    public String template(Model model) {
        model.addAttribute("value1", "111111");
        
        ArrayList<String> colors = new ArrayList<>();
        colors.add("RED");
        colors.add("BLUE");
        colors.add("GREEN");
        model.addAttribute("colors", colors);

        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            map.put(String.valueOf(i), String.valueOf(i * i));
        }
        model.addAttribute("map", map);

        model.addAttribute("user", new User((long) 1, "张三", "ShangHai,China", 20));

        return "/start/index";
    }
```

- model 这个参数主要是用于向模板传递数据，我们这里使用的模板是thymeleaf。

- 首先传递一个普通的value， 如上代码
```
     <p  th:text="*{value1}"/>
     或者
     <p  th:text="${value2}"/>
```
- 然后传递一个List类型，如上代码，这里在模板采用表格来接受
```
Colors表
<table>
    <tr><td>No.</td><td>Color</td><td>Count</td></tr>
    <tr th:each="color,colorStat:${colors}">
        <td th:text="${colorStat.index + 1}"/>
        <td th:text="${color}"/>
        <td th:text="${colorStat.count}"/>
    </tr>
</table>
```
<br/>

- 传递一个Map类型数据，如上代码，这里用表格来接受
```
<!--map输出-->
Map
<table>
    <tr><td>No.</td><td>Key</td><td>Value</td><td>Count</td></tr>
    <tr th:each="m,mStat:${map}">
        <td th:text="${mStat.index + 1}"/>
        <td th:text="${m.key}"/>
        <td th:text="${m.value}"/>
        <td th:text="${mStat.count}"/>
    </tr>
</table>
```

- 传递一个javaBean类型数据 比如User类
```
<!--User输出 1-->
<div>
    <p th:text="'用户编号：' + ${user.id}"/>
    <p th:text="'用户姓名：' + ${user.username}"/>
    <p th:text="'用户地址：' + ${user.address}"/>
    <p th:text="'用户年龄：' + ${user.age}"/>
    <p th:text="'用户描述：' + ${user.description}"/>
</div>

============================
<!--User输出 2-->
<div th:object="${user}">
    <p th:text="'用户编号：' + *{id}"/>
    <p th:text="'用户姓名：' + *{username}"/>
    <p th:text="'用户地址：' + *{address}"/>
    <p th:text="'用户年龄：' + *{age}"/>
    <p th:text="'用户描述：' + *{description}"/>
</div>
```

其中description并不是User的字段，知识我们在User中定义的一个方法：

```
public String getDescription(){
        return "This is "+username;
}
```
- 还可以通过引入其他的html文件作为header和footer，方法如下：
```
<div th:replace="start/header :: myheader"></div>
=================================================
<div th:include="start/header :: myheader"></div>
```
被引入的html是start下的header.html文件中的定义th:fragment标签中的内容：如下：
```
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<html lang="en">
<div th:fragment="myheader">
   学习中
</div>
</html>
```
到此为止，thymelaf差不多就这样简单的介绍完了，常用的，还有一些其他的标签后面用到的通过网上学习。

## 3、Cotroller的学习：上面已经介绍了这里不再介绍了

## 4、AOP和IoC的学习
### IoC：控制反转 -> 在面向对象设计的软件系统中，它的底层都是由N个对象构成的，各个对象之间通过相互合作，最终实现系统地业务逻辑。
![耦合](https://github.com/CodeTxp/Pictures/blob/master/%EF%BC%88%E7%89%9B%E5%AE%A2%E7%BD%91%EF%BC%89%E9%A1%B9%E7%9B%AE%E5%AD%A6%E4%B9%A0/360%E6%A1%8C%E9%9D%A2%E6%88%AA%E5%9B%BE20190409143325.jpg)
> 图1中描述的就是这样的一个齿轮组，它拥有多个独立的齿轮，这些齿轮相互啮合在一起，协同工作，共同完成某项任务。我们可以看到，在这样的齿轮组中，如果有一个齿轮出了问题，就可能会影响到整个齿轮组的正常运转。齿轮组中齿轮之间的啮合关系,与软件系统中对象之间的耦合关系非常相似。对象之间的耦合关系是无法避免的，也是必要的，这是协同工作的基础。

#### 1.什么是IOC:Inversion of Control的缩写，多数书籍翻译成“控制反转”,它的借助于“第三方”实现具有依赖关系的对象之间的解耦。
![解耦](https://github.com/CodeTxp/Pictures/blob/master/%EF%BC%88%E7%89%9B%E5%AE%A2%E7%BD%91%EF%BC%89%E9%A1%B9%E7%9B%AE%E5%AD%A6%E4%B9%A0/360%E6%A1%8C%E9%9D%A2%E6%88%AA%E5%9B%BE20190409143406.jpg)
> 由于引进了中间位置的“第三方”，也就是IOC容器，使得A、B、C、D这4个对象没有了耦合关系，齿轮之间的传动全部依靠“第三方”了，全部对象的控制权全部上缴给“第三方”IOC容器，所以，IOC容器成了整个系统的关键核心，它起到了一种类似“粘合剂”的作用，把系统中的所有对象粘合在一起发挥作用，如果没有这个“粘合剂”，对象与对象之间会彼此失去联系，这就是有人把IOC容器比喻成“粘合剂”的由来。 软件系统在引入IOC容器之后，如图所示，由于IOC容器的加入，对象A与对象B之间失去了直接联系，所以，当对象A运行到需要对象B的时候，IOC容器会主动创建一个对象B注入到对象A需要的地方。
### 对象A获得依赖对象B的过程,由主动行为变为了被动行为，控制权颠倒过来了，这就是“控制反转”这个名称的由来。

#### 2.IOC也叫依赖注入(DI)
>  所谓依赖注入，就是由IOC容器在运行期间，动态地将某种依赖关系注入到对象之中。

在这个课程的学习过程中：
传统的软件关系：
 ```
   WendaService wendaService=new WendaService();
```
在需要WendaService对象的地方直接new一个对象出来，变为
为被**被依赖对象**加上个 @Service注解
在需要的地方直接 **@Autowired**自动注解进去就好了
```
被依赖对象
@Service
public class WendaService {
    public String getMessage(int userId){
        return "Hello Message: "+String.valueOf(userId);
    }
}

需要的地方
@Autowired
WendaService wendaService;
```



