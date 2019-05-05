##  Day04
--------------
## <font size=6> Cookie 与 Session </font>
### <font size=4>什么是Cookie？</font>
[Cookie与Session的区别和联系](https://www.jianshu.com/p/9a561b36e9f3)
在网站中，**http请求是无状态的**。也就是说即使第一次和服务器连接后并且登录成功后，第二次请求服务器依然不能知道当前请求是哪个用户。cookie的出现就是为了解决这个问题，第一次登录后服务器返回一些数据（cookie）给浏览器，然后浏览器保存在本地，当该用户发送第二次请求的时候，就会自动的把上次请求存储的cookie数据自动的携带给服务器，服务器通过浏览器携带的数据就能判断当前用户是哪个了。cookie存储的数据量有限，不同的浏览器有不同的存储大小，但一般不超过4KB。因此使用cookie只能存储一些小量的数据。

### 什么是Session？
session和cookie的作用有点类似，都是为了存储用户相关的信息。不同的是，**cookie是存储在本地浏览器**，而**session存储在服务器**。存储在服务器的数据会更加的安全，不容易被窃取。但存储在服务器也有一定的弊端，就是会占用服务器的资源，但现在服务器已经发展至今，一些session信息还是绰绰有余的。

### cookie和session结合使用
web开发发展至今，cookie和session的使用已经出现了一些非常成熟的方案。在如今的市场或者企业里，一般有两种存储方式：

  - 1、存储在服务端：通过cookie存储一个session_id，然后具体的数据则是保存在session中。如果用户已经登录，则服务器会在cookie中保存一个session_id，下次  再次请求的时候，会把该session_id携带上来，服务器根据session_id在session库中获取用户的session数据。就能知道该用户到底是谁，以及之前保存的一些状态信息。这种专业术语叫做server side session。

  - 2、将session数据加密，然后存储在cookie中。这种专业术语叫做client side session。flask采用的就是这种方式，但是也可以替换成其他形式。
 
 ## <font size=6> ViewObject </font>
 方便传递任何数据到thymeleaf
 
 ```sh
 public class ViewObject {
    private Map<String, Object> objs = new HashMap<String, Object>();
    public void set(String key, Object value) {
        objs.put(key, value);
    }
    public Object get(String key) {
        return objs.get(key);
    }
}
 ```
 
 ## <font size=6> 注册的实现 </font>
   * 用户名与密码是否为空
   * 用户名是否已经被注册
   * 密码加盐操作
   * 注册后自动登录
  
这里就把token当做是sessionid


## <font size=6> 登录的实现 </font>
  * 1.服务器密码校验/三方校验回调，token登记
     * 1.1服务器端token关联userid
     * 1.2客户端存储token(app存储本地，浏览器存储cookie)
  * 2.服务端/客户端token有效期设置（记住登陆）
    注:token可以是sessionid，或者是cookie里的一个key

## <font size=6> 登出的实现 </font>
  * 服务端/客户端token删除
  * session清理
  
  
## 页面访问
* 1.客户端：带token的HTTP请求
* 2.服务端：
  * 1.根据token获取用户id
  * 2.根据用户id获取用户的具体信息
  * 3.用户和页面访问权限处理
  * 4.渲染页面/跳转页面
  
 
## 拦截器的作用
  
UserService的实现（包含了登录和注册的相关服务）
 ```sh
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private LoginTicketDAO loginTicketDAO;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    //注册
    public Map<String, Object> register(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if (user != null) {
            map.put("msg", "用户名已经被注册");
            return map;
        }
        // 密码强度
        user = new User();
        user.setName(username);
        //加盐
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        user.setPassword(WendaUtil.MD5(password + user.getSalt()));
        userDAO.addUser(user);
        // 登陆 实现注册后自动登录
        int userId = userDAO.selectByName(username).getId();
        String ticket = addLoginTicket(userId);
        map.put("ticket", ticket);
        return map;
    }
    //登录
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }
        User user = userDAO.selectByName(username);
        if (user == null) {
            map.put("msg", "用户名不存在");
            return map;
        }
        if (!WendaUtil.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "密码不正确");
            return map;
        }
        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
       // map.put("userId", user.getId());
        return map;
    }
    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }
    //增加一个ticket
    private String addLoginTicket(int userId) {
        LoginTicket ticket = new LoginTicket();
        ticket.setUserId(userId);
        Date date = new Date();
        date.setTime(date.getTime() + 1000 * 3600 * 24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }
}
 ```
 
 
