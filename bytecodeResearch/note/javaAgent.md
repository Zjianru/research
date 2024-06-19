# bytebuddy 04 - javaAgent

## 统计耗时
1. pom 文件引入依赖并处理打包逻辑
2. MANIFEST.MF文件 编写 
3. 编写委托处理入口和逻辑 
4. 测试类

agent 入口 `com.cz.agent.MethodCostTimeAgent`

```manifest
Manifest-Version: 1.0
Premain-Class: com.cz.agent.MethodCostTimeAgent
Can-Redefine-Classes: true

```

## 内存参数及信息收集

agent 入口 `com.cz.agent.JvmInfoReportAgent`

步骤同上


```manifest
Manifest-Version: 1.0
Premain-Class: com.cz.agent.JvmInfoReportAgent
Can-Redefine-Classes: true

```