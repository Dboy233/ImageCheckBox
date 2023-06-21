## ImageCheckBox

[![](https://jitpack.io/v/Dboy233/ImageCheckBox.svg)](https://jitpack.io/#Dboy233/ImageCheckBox)

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

dependencies {
	implementation 'com.github.Dboy233:ImageCheckBox:${last-version}'
}
```

## 可以设置三种状态图片的CheckBox

```xml
           
	<ImageCheckBox
         app:onDrawable="@mipmap/ic_image_check_on" 开启/选中状态
         app:offDrawable="@mipmap/ic_image_check_off" 关闭/未选中状态
         app:waitDrawable="@mipmap/ic_image_wait" 介于开启和关闭之间的状态，等待/未全选状态
         app:checked="true"/>
   
```

```java
    /**
     * @param wait           等待状态
     * @param notifyListener 是否通知回调；只有在wait=false时进行判断
     */
    public void setWait(boolean wait, boolean notifyListener)
        
    /**
     * 是否是等待状态
     */
    public boolean isWait()
	
    /**
     *是否选中
     */
    public boolean isCheck() 
        
    /**
     * 设置选中状态不通知回调方法
     *
     * @param isCheck 选中状态
     */
    public void setCheck(boolean isCheck) 
        
    /**
     * 设置选中状态不通知回调方法
     *
     * @param isCheck        选中状态
     * @param notifyListener 是否通知回调方法
     */
    public void setCheck(boolean isCheck, boolean notifyListener) 
```




| 未选中 | 选中 | 等待 |
| :--: | :--: | :--: |
|   <img src="img_off.png" style="zoom: 25%;" />   |    <img src="img_check.png" style="zoom: 25%;" />    |   <img src="img_wait.png" style="zoom:25%;" />   |

