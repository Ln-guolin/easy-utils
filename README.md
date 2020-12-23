<p align="center">
	<a href="https://github.com/Ln-guolin/common-utils"><img src="https://soilove.oss-cn-hangzhou.aliyuncs.com/32e/pro-mall/easy-utils.png" width="350px"></a>
</p>
<p align="center">
	<strong>一个整合了Java开发过程中，比较常用的简单工具类集合。</strong>
</p>
<p align="center">
	<a target="_blank" href="https://github.com/Ln-guolin/common-utils/blob/master/LICENSE">
		<img src="https://img.shields.io/:license-Apache2.0-blue.svg" />
	</a>
	<a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
		<img src="https://img.shields.io/badge/JDK-8+-green.svg" />
	</a>
	<a target="_blank" href="https://gitter.im/pro-32e/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge">
		<img src="https://badges.gitter.im/pro-32e/community.svg" />
	</a>
	<a href="https://github.com/Ln-guolin/common-utils">
        <img src="https://img.shields.io/github/languages/code-size/Ln-guolin/common-utils"/>
    </a>
	<a href="https://github.com/Ln-guolin/common-utils">
        <img src="https://img.shields.io/github/issues-raw/Ln-guolin/common-utils"/>
    </a>
    <a href="https://github.com/Ln-guolin/common-utils">
        <img src="https://img.shields.io/github/v/tag/Ln-guolin/common-utils?include_prereleases"/>
    </a>
	<a href="https://github.com/Ln-guolin/common-utils">
        <img src="https://img.shields.io/github/stars/Ln-guolin/common-utils?style=social"/>
    </a>
</p>



## 使用方法

Maven方式引入：直接在工程pom.xml文件中添加如下依赖，即可使用
```xml
<!-- 工具组件 -->
<dependency>
    <groupId>cn.soilove</groupId>
    <artifactId>easy-utils</artifactId>
    <version>1.1.8</version>
</dependency>
```

## 项目结构

```angular2
src
└── utils
    ├── AESUtils.java                 // AES加解密
    ├── AvatarUtils.java              // 文字头像生成工具
    ├── EmojiUtils.java               // Emoji表情转换
    ├── BloomFilterUtils.java         // 布隆过滤器工具
    ├── CaffeineCacheUtils.java       // 本地Caffeine缓存工具
    ├── ExcelUtils.java               // Ali EasyExcel文件操作
    ├── ForkJoinUtils.java            // 并行工具
    ├── ImageGraphicsDrawUtils.java   // 图片绘制工具
    ├── JWTUtils.java                 // JWT Token生成工具
    ├── JiebaUtils.java               // 结巴分词
    ├── KeyMapUtils.java              // 字符Map互转工具
    ├── LogUtils.java                 // 日志工具
    ├── OrikaMapperUtils.java         // 对象复制
    ├── PathUtils.java                // 项目路径
    ├── QLExpressUtils.java           // QL 规则引擎
    ├── QRCodeUtils.java              // 二维码生成解码
    ├── RSAUtils.java                 // RSA加解密
    ├── RandomUtils.java              // 随机元素生成
    ├── RegexUtils.java               // 正则表达式
    ├── SplitUtils.java               // 拆分工具
    ├── ThreadPoolUtils.java          // 线程池工具
    └── DateUtils.java                // 日期时间处理工具
```
