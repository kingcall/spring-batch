[toc]
# spring-batch
## spring-batch 基础概念
### Job
### JobInstance
### JobExecution
### JobParameters
- 传递给job key-value 参数，传递需要的信息给runtime
### step
#### tasklet 形式的简单step
#### chunk-based step
##### itemReader
- read individually
###### JDBC
###### FlatFile
###### XML
###### multisource
##### writer
- write with chunk
###### FlatFile
###### Jdbc
###### xml 输出
###### 多输出
###### 分类输出
##### processor
- 实现业务逻辑，例如验证、过滤
- 如果输入为空，则不会被传输到writer
###### single processor
###### composite processor
#### 异常处理及重启机制
- 对于chunk-oriented step，Spring Batch提供了管理状态的工具。如何在一个步骤中管理状态是通过ItemStream接口为开发人员提供访问权限保持状态的组件。
这里提到的这个组件是ExecutionContext实际上它是键值对的映射。map存储特定步骤的状态。该ExecutionContext使重启步骤成为可能，因为状态在JobRepository中持久存在。
- 执行期间出现错误时，最后一个状态将更新为JobRepository。下次作业运行时，最后一个状态将用于填充ExecutionContext然后可以继续从上次离开的地方开始运行。
## spring-batch 简单任务
## spring-batch 复杂任务
### 嵌套任务
### 真实的批处理
## 监听器
### JobExecutionListener
#### 继承接口的使用方式
#### 注解的使用方式

