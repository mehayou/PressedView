# PressedView
[![API](https://img.shields.io/badge/API-15%2B-brightgreen.svg)](https://android-arsenal.com/api?level=15)
[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![](https://img.shields.io/github/release/mehayou/pressedview.svg?color=red)](https://github.com/mehayou/PressedView/releases)

#### 介绍
用简单原理实现TextView、ImageView自带按下效果，包含透明度、颜色遮罩、水波纹效果。
* 简单、实用、方便
* 适配已知系统版本问题，水波纹效果仅支持API21以上
* TextView支持选择使用 text|compound_drawable|background
* ImageView支持选择使用 src|background

## 依赖
参照最终版本名称替换下面的“releases”
#### Gradle
```java
implementation 'com.mehayou:pressedview:releases'
```
#### Maven
```
<dependency>
  <groupId>com.mehayou</groupId>
  <artifactId>pressedview</artifactId>
  <version>releases</version>
  <type>pom</type>
</dependency>
```

## 属性说明
#### TextPressedView
|属性名称|参数格式|说明|
|----|----|----|
|textRippleColor|reference&#124;color|水波纹颜色值，设置后在API21以上其他配置不起作用，默认未设置|
|textPressedTarget|none&#124;text&#124;compound_drawable&#124;background|按下目标，可设置 无目标&#124;文字&#124;图标&#124;背景，默认无目标|
|textPressedMode|color&#124;alpha|按下效果模式，颜色遮罩&#124;透明度 二选一，默认颜色遮罩|
|textPressedColor|reference&#124;color|按下遮罩颜色值，默认Color.DKGRAY（即0xFF444444）|
|textPressedAlpha|float|按下透明度值，0.0~1.0之间取值，默认0.5|

#### ImagePressedView
|属性名称|参数格式|说明|
|----|----|----|
|imageRippleColor|reference&#124;color|水波纹颜色值，设置后在API21以上其他配置不起作用，默认未设置|
|imagePressedTarget|none&#124;src&#124;background|按下目标，可设置 无目标&#124;图片&#124;背景，默认无目标|
|imagePressedMode|color&#124;alpha|按下效果模式，颜色遮罩&#124;透明度 二选一，默认颜色遮罩|
|imagePressedColor|reference&#124;color|按下遮罩颜色值，默认Color.DKGRAY（即0xFF444444）|
|imagePressedAlpha|float|按下透明度值，0.0~1.0之间取值，默认0.5|

## 使用方法
#### TextPressedView
```xml
<com.mehayou.pressedview.TextPressedView
    style="@style/TextStyle"
    android:text="透明度效果"
    android:drawableLeft="@android:drawable/ic_delete"
    android:background="@color/colorAccent"
    app:textPressedAlpha="0.25"
    app:textPressedMode="alpha"
    app:textPressedTarget="text|compound_drawable|background" />

<com.mehayou.pressedview.TextPressedView
    style="@style/TextStyle"
    android:text="颜色遮罩效果"
    android:drawableLeft="@android:drawable/ic_delete"
    android:background="@color/colorAccent"
    app:textPressedColor="@color/colorPrimary"
    app:textPressedMode="color"
    app:textPressedTarget="text|compound_drawable|background" />

<com.mehayou.pressedview.TextPressedView
    style="@style/TextStyle"
    android:text="水波纹效果"
    android:background="@color/colorAccent"
    app:textRippleColor="@color/colorPrimary" />
```

#### ImagePressedView
```xml
<!--透明度效果-->
<com.mehayou.pressedview.ImagePressedView
    style="@style/ImageStyle"
    android:src="@android:mipmap/sym_def_app_icon"
    android:background="@color/colorAccent"
    app:imagePressedAlpha="0.25"
    app:imagePressedMode="alpha"
    app:imagePressedTarget="src|background" />

<!--颜色遮罩效果-->
<com.mehayou.pressedview.ImagePressedView
    style="@style/ImageStyle"
    android:src="@android:mipmap/sym_def_app_icon"
    android:background="@color/colorAccent"
    app:imagePressedColor="@color/colorPrimary"
    app:imagePressedMode="color"
    app:imagePressedTarget="src|background" />

<!--水波纹效果，不建议src|background一起使用-->
<com.mehayou.pressedview.ImagePressedView
    style="@style/ImageStyle"
    android:src="@android:mipmap/sym_def_app_icon"
    app:imagePressedTarget="src"
    app:imageRippleColor="@color/colorPrimary" />
<com.mehayou.pressedview.ImagePressedView
    style="@style/ImageStyle"
    android:background="@color/colorAccent"
    app:imagePressedTarget="background"
    app:imageRippleColor="@color/colorPrimary" />
```
