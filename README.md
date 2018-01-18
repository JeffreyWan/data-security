# data-security

# 数据安全

## 基于http过滤器和springboot框架对有使用@DataSecurity注解的接口进行拦截并校验

## 使用说明

### 1.在项目pom文件引入依赖包

```
        <dependency>
            <groupId>com.tulingframework</groupId>
            <artifactId>security-data-web</artifactId>
            <version>0.1.7</version>
        </dependency>
```
``仓库地址：http://mvn.zhengjiao.org/content/repositories/releases``

### 2.在配置启动类加上@EnableDataSecurity注解开启数据校验功能。

### 3.在需要进行接口校验的类或者方法上标注@DataSecurity

```
        @RestController
        @RequestMapping("xx")
        public class xxxController {
           
           @DataSecurity(types={xxxx})
           @PostMapping("xxx")
           ResponseEntity decrypt(xxxxx){
                ....
                ....
                ....
           }
           
        }
```
