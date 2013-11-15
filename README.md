lite-framework
==============

java lite framework

想写一个轻量级的mvc框架，尽可能的达到简洁的目的，尽可能的减少类
不是做一个大而全的东西，而是做一个可用的小东西 。

要求：
1、支持restful
2、支持多视图，并可以自动转换
3、省略dao,通过提供通用dao
4、支持多数据库切换
5、支持中小项目，而不是大型项目
6、基于注解
7、试着加入DDD的理论？（当前还不会用呢）




1、提供通用的BaseAction,在Action继承时，提供数据获取等操作，不用再每次手动的getParameter()
2、提供通用的BaseModel，封装基本的CRUD操作，与数据库进行交互
3、Intercepter机制，提供全局和局部的拦截器实现
