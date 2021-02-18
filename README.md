# WebSpider
腾讯新闻网网页爬虫核心代码

&emsp;&emsp;通过爬虫以及对tx新闻网的分析获取指定新闻信息，将其持久化到Mysql数据库并同步地将其url作为唯一标识存入缓存数据库Redis中实现去重操作，减轻Mysql压力。由于最初获取到的数据为Json样式的字符串，需要使用阿里巴巴开发的fastjson包进行多次数据转换最终获取到想要的数据存入News对象进行持久化
