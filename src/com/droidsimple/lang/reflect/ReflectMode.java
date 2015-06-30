package com.droidsimple.lang.reflect;

/**
 * 反射模式类
 * 
 * TODO 建议Self_And_Extends_Exclude_Object为默认
 * 
 * @author simplesome
 *
 */
public enum ReflectMode {
	
	Self_Exclude_Extends, // 自身+排除继承(已支持)
	
	Public_And_Extends, // 全部Public公有的，包括继承(已支持)
	Public_And_Extends_Exclude_Object, // 全部Public公有的，包括继承，排除java.lang.Object(已支持)
	
//	Self_And_Extends, // 自身+继承 TODO 暂不支持
//	Self_And_Extends_Exclude_Object // 自身+继承，排除java.lang.Object TODO 暂不支持
}