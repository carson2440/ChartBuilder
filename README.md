[**ChartBuilder**](https://github.com/carson2440/ChartBuilder) is a powerful & easy to use chart library for Android. It runs on [API level 14](http://developer.android.com/guide/topics/manifest/uses-sdk-element.html#ApiLevels) and upwards. 

[![images](https://github.com/carson2440/ChartBuilder/blob/master/images/device-2017-10-19-101634.png)](https://github.com/carson2440/ChartBuilder)

ChartBuilder 底层使用[**MPAndroidChart**](https://github.com/PhilJay/MPAndroidChart)库，在其基础上进行扩展：

1：支持分段显示with one LineDataSet。
（MPAndroidChart分段显示需要使用多个LineDataSet，并且在分段之间滑动选择数据点存在跳点的bug）

2：仿雪球交互效果，ChartLine图形缩放后，长按移动显示高亮点。

3：优化长按事件的响应速度更灵敏。

Usage
-----

In order to use the library, there are 2 different options:

**Gradle dependency** (recommended)

  -  Add the following to your project level `build.gradle`:
 
```gradle
allprojects {
	repositories {
		maven { url "https://jitpack.io" }
	}
}
```
  -  Add this to your app `build.gradle`:
 
```gradle
dependencies {
	compile 'com.github.carson2440:ChartBuilder:1.0.0'
}
```

License
=======
Copyright 2017 carson2440

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0