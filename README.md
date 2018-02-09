# 秀趣
**请下载修改的朋友把bugly账号换成自己的，现在所有你们出现的问题都报到我这边了，我自己的问题都区分不了了。

秀趣——文艺的名字下是一款实用的休闲类App。新闻视频和技术应有尽有，不管是学习技术知识还是了解社会国家大事，这款App都会是你的选择。<br>
[app下载地址](https://fir.im/6s7z)（时时下载量1080）<br>
##### 下面是部分截图
<img src="https://github.com/liulingfeng/Common/blob/master/screenshot/S70425-105042.jpg" width="300" height="530">
<img src="https://github.com/liulingfeng/Common/blob/master/screenshot/S70425-105106.jpg" width="300" height="530">
<img src="https://github.com/liulingfeng/Common/blob/master/screenshot/S70425-105115.jpg" width="300" height="530">
<img src="https://github.com/liulingfeng/Common/blob/master/screenshot/S70425-105216.jpg" width="300" height="530">
<img src="https://github.com/liulingfeng/Common/blob/master/screenshot/S70425-105121.jpg" width="300" height="530">
<img src="https://github.com/liulingfeng/Common/blob/master/screenshot/S70425-105125.jpg" width="300" height="530">
<img src="https://github.com/liulingfeng/Common/blob/master/screenshot/S70425-105129.jpg" width="300" height="530">
<img src="https://github.com/liulingfeng/Common/blob/master/screenshot/S70425-105203.jpg" width="300" height="530">
<img src="https://github.com/liulingfeng/Common/blob/master/screenshot/S70425-105247.jpg" width="300" height="530">
<img src="https://github.com/liulingfeng/Common/blob/master/screenshot/S70425-105914.jpg" width="300" height="530">

### 设计和美工
遵照Metrial design的设计风格，简约平面化的设计。图标部分使用vector格式（适配性更好）。由于本人不是美工，图标来自iconfont和Metrial design图标库。

### 用到的Api
发现页数据是通过Jsoup抓取的网页数据，不懂jsoup的建议学习一下；其中详情页是用WebView加载的网页（通过js隐藏掉了头部和尾部）。新闻页和视频页用的是网易新闻的api，是用Fiddler抓取的接口。其中视频接口需要注意添加请求头伪装成浏览器。

### 懒人福音
这边封装了基础module（每个app所需要的基本上都有），极大的降低了耦合性。另外还封装了视频播放module和图片选择器module，可拔可插，十分方便。设置页有别于传统做法，用了PreferenceFragment，建议可以学习一下。

总结
-
喜欢的朋友轻轻右上角赏个star，您的鼓励会给我持续更新的动力。有问题可以反馈给我，感谢各位。另外有私活可以找我，一定要找我哦。

凑点咖啡钱，不过分吧
-
<img src="https://github.com/liulingfeng/Common/blob/master/screenshot/S70508-102659.jpg" width="300" height="530">
<img src="https://github.com/liulingfeng/Common/blob/master/screenshot/S70508-102740.jpg" width="300" height="530">
