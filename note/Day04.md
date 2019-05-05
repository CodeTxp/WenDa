##  Day04
--------------
## <font size=6> Cookie 与 Session </font>
### <font size=4>什么是Cookie？</font>
在网站中，**http请求是无状态的**。也就是说即使第一次和服务器连接后并且登录成功后，第二次请求服务器依然不能知道当前请求是哪个用户。cookie的出现就是为了解决这个问题，第一次登录后服务器返回一些数据（cookie）给浏览器，然后浏览器保存在本地，当该用户发送第二次请求的时候，就会自动的把上次请求存储的cookie数据自动的携带给服务器，服务器通过浏览器携带的数据就能判断当前用户是哪个了。cookie存储的数据量有限，不同的浏览器有不同的存储大小，但一般不超过4KB。因此使用cookie只能存储一些小量的数据。

### 什么是Session？
session和cookie的作用有点类似，都是为了存储用户相关的信息。不同的是，**cookie是存储在本地浏览器**，而**session存储在服务器**。存储在服务器的数据会更加的安全，不容易被窃取。但存储在服务器也有一定的弊端，就是会占用服务器的资源，但现在服务器已经发展至今，一些session信息还是绰绰有余的。

### cookie和session结合使用
web开发发展至今，cookie和session的使用已经出现了一些非常成熟的方案。在如今的市场或者企业里，一般有两种存储方式：

  - 1、存储在服务端：通过cookie存储一个session_id，然后具体的数据则是保存在session中。如果用户已经登录，则服务器会在cookie中保存一个session_id，下次  再次请求的时候，会把该session_id携带上来，服务器根据session_id在session库中获取用户的session数据。就能知道该用户到底是谁，以及之前保存的一些状态信息。这种专业术语叫做server side session。

 - 2、将session数据加密，然后存储在cookie中。这种专业术语叫做client side session。flask采用的就是这种方式，但是也可以替换成其他形式。
 
 
 ## <font size=6> 注册的实现 </font>
