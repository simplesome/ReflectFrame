package com.droidcommon.lang.reflect;

import android.util.Log;

/**
 * 反射方法类
 * 
 * @author simplesome
 *
 */
public class ReflectMethod {

	private static final String TAG = ReflectMethod.class.getSimpleName();
	private static boolean DEBUG = false;

	// 修饰符
	private int modifier = java.lang.reflect.Modifier.PRIVATE;

	// 类型
	private Class<?> returnType = null;

	// 名称
	private String name = null;

	// 参数
	//	private Object[] parameter = null;

	// 自身
	private java.lang.reflect.Method method = null;

	// 注释
	private java.lang.annotation.Annotation[] annotations = null;

	// 枚举模式
	private ReflectMode reflectMode = ReflectMode.Self_Exclude_Extends;

	/**
	 * 设置调试日志开关
	 * 
	 * @param debug
	 */
	public static void setDebugEnable(boolean debug) {
		DEBUG = debug;
	}

	public ReflectMethod(java.lang.reflect.Method method) {
		init(method, ReflectMode.Self_Exclude_Extends);
	}

	public ReflectMethod(java.lang.reflect.Method method, ReflectMode mode) {
		init(method, mode);
	}

	/**
	 * 初始化
	 * 
	 * @param method
	 */
	private void init(java.lang.reflect.Method method, ReflectMode mode) {
		this.method = method;
		this.name = method.getName();

		this.returnType = method.getReturnType();
		this.reflectMode = mode;

		this.modifier = this.method.getModifiers();

		// +++ 分解注解
		if (this.reflectMode == ReflectMode.Self_Exclude_Extends) {
			this.annotations = this.method.getDeclaredAnnotations(); // 自身注解
		}else if (this.reflectMode == ReflectMode.Public_And_Extends || this.reflectMode == ReflectMode.Public_And_Extends_Exclude_Object) {
			this.annotations = this.method.getAnnotations(); // 共有注解
		}

	}

	public String getName() {
		return this.name;
	}

	public java.lang.reflect.Method getMethod() {
		return this.method;
	}

	public int getModifier() {
		return this.modifier;
	}

	public Class<?> getReturnType() {
		return this.returnType;
	}

	public java.lang.annotation.Annotation[] getAnnotations() {
		return this.annotations;
	}

	public boolean isBridge() {
		return this.method.isBridge();
	}

	public boolean isAccessible() {
		return this.method.isAccessible();
	}

	public boolean isSynthetic() {
		return this.method.isSynthetic();
	}

	public boolean isVarArgs() {
		return this.method.isVarArgs();
	}

	/**
	 * 打印
	 */
	public void print() {
		if(DEBUG) Log.w(TAG, "------ ReflectMethod print() start ------");

		if(DEBUG) Log.w(TAG, "getName:"+getName());
		//		if(DEBUG) Log.w(TAG, "getCanonicalName:"+getCanonicalName());
		if(DEBUG) Log.w(TAG, "toString:"+toString());

		if(DEBUG) Log.w(TAG, "getModifier:"+getModifier());
		if(DEBUG) Log.w(TAG, "Modifier.toString:"+java.lang.reflect.Modifier.toString( getModifier() ));

		if (getAnnotations() != null) {
			if(DEBUG) Log.w(TAG, "getAnnotations size:"+getAnnotations().length);
		}

		if(DEBUG) Log.w(TAG, "getReturnType:"+getReturnType().getCanonicalName());

		if(DEBUG) Log.w(TAG, "isBridge:"+isBridge());
		if(DEBUG) Log.w(TAG, "isAccessible:"+isAccessible());
		if(DEBUG) Log.w(TAG, "isSynthetic:"+isSynthetic());
		if(DEBUG) Log.w(TAG, "isVarArgs:"+isVarArgs());

		if (this.reflectMode != null) {
			if(DEBUG) Log.w(TAG, "mode:"+this.reflectMode.name());
		}
		if(DEBUG) Log.w(TAG, "------ ReflectMethod print() end ------");
	}

}
