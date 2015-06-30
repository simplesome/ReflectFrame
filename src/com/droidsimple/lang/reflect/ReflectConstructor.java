package com.droidsimple.lang.reflect;

import com.droidsimple.util.Log;


/**
 * 反射构造函数类
 * 
 * constructor.getName()得到的内部类构造函数包含$，所以用constructor.getDeclaringClass()代替.
 * 
 * @author simplesome
 *
 */
public class ReflectConstructor {

	private static final String TAG = ReflectConstructor.class.getSimpleName();
	private static boolean DEBUG = false;

	// 自身构造
	private java.lang.reflect.Constructor<?> constructor = null;

	// 修饰符
	private int modifier = java.lang.reflect.Modifier.PUBLIC;

	// 名称
	private String name = null;

//	private String canonicalName = null;

	// 枚举模式
	private ReflectMode reflectMode = ReflectMode.Self_Exclude_Extends;

	private boolean isSynthetic = false;
	private boolean isVarArgs = false;

	// 全部注解(不分解)
	private java.lang.annotation.Annotation[] annotations = null;

	/**
	 * 设置调试日志开关
	 * 
	 * @param debug
	 */
	public static void setDebugEnable(boolean debug) {
		DEBUG = debug;
	}

	public ReflectConstructor(java.lang.reflect.Constructor<?> constructor) {
		init(constructor, ReflectMode.Self_Exclude_Extends);
	}

	public ReflectConstructor(java.lang.reflect.Constructor<?> constructor, ReflectMode mode) {
		init(constructor, mode);
	}

	/**
	 * 初始化
	 * 
	 * @param constructor
	 */
	private void init(java.lang.reflect.Constructor<?> constructor, ReflectMode mode) {

		this.constructor = constructor;
		this.reflectMode = mode;

		if (constructor.getDeclaringClass() != null) {
			this.name = constructor.getDeclaringClass().getSimpleName();
//			this.canonicalName = constructor.getDeclaringClass().getCanonicalName();
		}else{
			this.name = constructor.getName();
		}

		this.modifier = constructor.getModifiers();

		this.isSynthetic = constructor.isSynthetic();
		this.isVarArgs = constructor.isVarArgs();

		// +++ 分解注解
		if (this.reflectMode == ReflectMode.Self_Exclude_Extends) {
			this.annotations = this.constructor.getDeclaredAnnotations(); // 自身注解
		}else if (this.reflectMode == ReflectMode.Public_And_Extends || this.reflectMode == ReflectMode.Public_And_Extends_Exclude_Object) {
			this.annotations = this.constructor.getAnnotations(); // 共有注解
		}

	}

	public java.lang.reflect.Constructor<?> getConstructor() {
		return this.constructor;
	}

	public java.lang.annotation.Annotation[] getAnnotations() {
		return this.annotations;
	}

	public int getModifier() {
		return this.modifier;
	}

	public String getName() {
		return this.name;
	}

//	public String getCanonicalName() {
//		return this.canonicalName;
//	}

	public ReflectMode getReflectMode() {
		return this.reflectMode;
	}

	public boolean isSynthetic() {
		return this.isSynthetic;
	}

	public boolean isVarArgs() {
		return this.isVarArgs;
	}

	/**
	 * 打印
	 */
	public void print() {
		if(DEBUG) Log.w(TAG, "------ ReflectConstructor print() start ------");

		if(DEBUG) Log.w(TAG, "getName:"+getName());
//		if(DEBUG) LogUtil.w(TAG, "getCanonicalName:"+getCanonicalName());
		if(DEBUG) Log.w(TAG, "toString:"+this.constructor.toString());
		if(DEBUG) Log.w(TAG, "toGenericString:"+this.constructor.toGenericString());

		if (this.constructor.getDeclaringClass() != null) {
			if(DEBUG) Log.w(TAG, "getDeclaringClass:"+this.constructor.getDeclaringClass().getCanonicalName());
		}

		if(DEBUG) Log.w(TAG, "getModifier:"+getModifier());
		if(DEBUG) Log.w(TAG, "Modifier.toString:"+java.lang.reflect.Modifier.toString( getModifier() ));

		if (getAnnotations() != null) {
			if(DEBUG) Log.w(TAG, "getAnnotations size:"+getAnnotations().length);
		}

		if(DEBUG) Log.w(TAG, "isSynthetic:"+isSynthetic());
		if(DEBUG) Log.w(TAG, "isVarArgs:"+isVarArgs());
		
		if (this.reflectMode != null) {
			if(DEBUG) Log.w(TAG, "mode:"+this.reflectMode.name());
		}
		if(DEBUG) Log.w(TAG, "------ ReflectConstructor print() end ------");
	}

}
