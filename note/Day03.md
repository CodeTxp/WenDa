# Day03



--------------
### MyBatis的集成
### 1. 步骤
 1. application.properties增加spring配置数据库链接地址
  ```
 # url path
server.port=8080
server.servlet.context-path=/


# thymeleaf
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
spring.thymeleaf.mode=HTML
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.servlet.content-type=text/html
# 开发阶段务必关闭缓存 (=false)
#spring.thymeleaf.cache=false

# 数据库连接
spring.datasource.url=jdbc:mysql://localhost:3306/wenda?serverTimezone=UTC  
spring.datasource.username=root
spring.datasource.password=root

# mybatis
mybatis.config-location=classpath:mybatis-config.xml
mybatis.mapperLocations=classpath:mapper/*.xml
 ```
 这里需要一个mybatis-config.xml，如下
 
  ```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
        <!-- Globally enables or disables any caches configured in any mapper under this configuration -->
        <setting name="cacheEnabled" value="true"/>
        <!-- Sets the number of seconds the driver will wait for a response from the database -->
        <setting name="defaultStatementTimeout" value="3000"/>
        <!-- Enables automatic mapping from classic database column names A_COLUMN to camel case classic Java property names aColumn -->
        <setting name="mapUnderscoreToCamelCase" value="true"/>
        <!-- Allows JDBC support for generated keys. A compatible driver is required.
        This setting forces generated keys to be used if set to true,
         as some drivers deny compatibility but still work -->
        <setting name="useGeneratedKeys" value="true"/>
    </settings>
</configuration>
 ```
 
 2. pom.xml引入mybatis-spring-boot-starter和mysql-connector-java
 ```
       <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.0.0</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>6.0.6</version>
        </dependency>
 ```
 
### 2.JDBC数据库操作
1. 加载驱动
2. 创建数据库链接
3. 创建Statement对象
4. 执行SQL获取数据（关注这里）
5. 数据转化
6. 资源释放

### 3.具体步骤
1. JavaBean的编写

User：
 ```
public class User {
    private int id;
    private String name;
    private String password;
    private String salt;
    private String headUrl;

    public User() {

    }
    public User(String name) {
        this.name = name;
        this.password = "";
        this.salt = "";
        this.headUrl = "";
    }
    
    getter and setter
}  
 ```
 
 Question：
 ```
 public class Question {
    private int id;
    private String title;
    private String content;
    private Date createdDate;
    private int userId;
    private int commentCount;
    
    getter and setter
 }

 ```
 
 为了方便传参给前台，ViewObject得以编写
 ``` 
package com.txp.wenda.model;

import java.util.HashMap;
import java.util.Map;

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

 2. DAO的编写
 
 UserDAO：
 ```
@Mapper
@Component
public interface UserDAO {
    // 注意空格
    String TABLE_NAME = " user ";
    String INSERT_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{name},#{password},#{salt},#{headUrl})"})
    int addUser(User user);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    User selectById(int id);

    @Update({"update ", TABLE_NAME, " set password=#{password} where id=#{id}"})
    void updatePassword(@Param("password")String password, @Param("id")int  id);

    @Delete({"delete from ", TABLE_NAME, " where id=#{id}"})
    void deleteById(int id);
}
 ```
 
 QuestionDAO：
 ``` 
@Mapper
@Component//和mybatis关联的一个dao
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    List<Question> selectLatestQuestions(@Param("userId") int userId, @Param("offset") int offset,
                                         @Param("limit") int limit);

    @Select({"select ", SELECT_FIELDS, " from ", TABLE_NAME, " where id=#{id}"})
    Question getById(int id);

    @Update({"update ", TABLE_NAME, " set comment_count = #{commentCount} where id=#{id}"})
    int updateCommentCount(@Param("id") int id, @Param("commentCount") int commentCount);

}
 ```
 **其中selectLatestQuestions方法，结合了mapper.xml文件**
 QuestionDAO.xml
  ```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.txp.wenda.dao.QuestionDAO">
    <sql id="table">question</sql>
    <sql id="selectFields">id, title, content, comment_count,created_date,user_id
    </sql>
    <select id="selectLatestQuestions" resultType="com.txp.wenda.model.Question">
        SELECT
        <include refid="selectFields"/>
        FROM
        <include refid="table"/>

        <if test="userId != 0">
            WHERE user_id = #{userId}
        </if>
        ORDER BY id DESC
        LIMIT #{offset},#{limit}
    </select>
</mapper>
 ```
 
3. Service的编写
UserService:
 ```
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDAO userDAO;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }
}
 ```
 
QuestionService:
 ```
@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;

    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }
}
 ```

 4. Controller的编写
```
@Controller
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    private List<ViewObject> getQuestions(int userId, int offset, int limit) {
        List<Question> questionList = questionService.getLatestQuestions(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<>();
        for (Question question : questionList) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user", userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model,
                        @RequestParam(value = "pop", defaultValue = "0") int pop) {
//        List<Question> questionList = questionService.getLatestQuestions(0, 0, 10);
//        model.addAttribute("questions",questionList);
        model.addAttribute("vos", getQuestions(0, 0, 10));
        return "index";
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getQuestions(userId, 0, 10));
        return "index";
    }
}
```

5.静态文件的传入（css文件、文件）

------------------------------

## 注意
1、@controller 控制器（注入服务）
用于标注控制层，相当于struts中的action层

2、@service 服务（注入dao）
用于标注服务层，主要用来进行业务的逻辑处理

3、@repository（实现dao访问）
用于标注数据访问层，也可以说用于标注数据访问组件，即DAO组件.

4、@component （把普通pojo实例化到spring容器中，相当于配置文件中的 
<bean id="" class=""/>）
泛指各种组件，就是说当我们的类不属于各种归类的时候（不属于@Controller、@Services等的时候），我们就可以使用@Component来标注这个类。


