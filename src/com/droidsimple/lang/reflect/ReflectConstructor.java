package com.droidsimple.lang.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

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

	// 得到声明类<lass<?>，就是此方法所在的类。
	private Class<?> clazz = null;

	// 自身构造
	private Constructor<?> constructor = null;

	// 修饰符
	private int modifier = Modifier.PUBLIC;

	// 名称
	private String name = null;
//	private String simpleName = null;

	//	private String canonicalName = null;

	// 枚举模式
	private ReflectMode reflectMode = ReflectMode.Self_Exclude_Extends;

	private boolean isSynthetic = false;
	private boolean isVarArgs = false;

	// 全部注解(不分解)
	private Annotation[] annotations = null;

	/**
	 * 设置调试日志开关
	 * 
	 * @param debug
	 */
	public static void setDebugEnable(boolean debug) {
		DEBUG = debug;
	}

	public ReflectConstructor(Constructor<?> constructor) {
		init(constructor, ReflectMode.Self_Exclude_Extends);
	}

	public ReflectConstructor(Constructor<?> constructor, ReflectMode mode) {
		init(constructor, mode);
	}

	/**
	 * 初始化
	 * 
	 * @param constructor
	 */
	private void init(Constructor<?> constructor, ReflectMode mode) {

		this.constructor = constructor;
		this.reflectMode = mode;

		// 得到声明类<lass<?>，就是此方法所在的类。
		this.clazz = this.constructor.getDeclaringClass();

		this.name = this.constructor.getName();

		if (constructor.getDeclaringClass() != null) {
//			this.simpleName = constructor.getDeclaringClass().getSimpleName();
		}else{
			
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

	/**
	 * 得到声明类<lass<?>，就是此构造所在的类。
	 * 
	 * @return
	 */
	public Class<?> getClazz() {
		return this.clazz;
	}

	public String getName() {
		return this.name;
	}

//	public String getSimpleName() {
//		return this.simpleName;
//	}

	public Constructor<?> getConstructor() {
		return this.constructor;
	}

	public Annotation[] getAnnotations() {
		return this.annotations;
	}

	public int getModifier() {
		return this.modifier;
	}

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
	 * 比较相等
	 * 
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		return this.constructor.equals(obj);
	}

	/**
	 * 得到hash值
	 * 
	 * @return
	 */
	@Override
	public int hashCode() {
		return this.constructor.hashCode();
	}

	/**
	 * 打印
	 */
	public void print() {
		if(DEBUG) Log.w(TAG, "------ ReflectConstructor print() start ------");

		if(DEBUG) Log.w(TAG, "this.getName():"+this.getName());
//		if(DEBUG) Log.w(TAG, "this.getSimpleName():"+this.getSimpleName());
		if(DEBUG) Log.w(TAG, "toString:"+this.constructor.toString());
		if(DEBUG) Log.w(TAG, "toGenericString:"+this.constructor.toGenericString());

		if (this.constructor.getDeclaringClass() != null) {
			if(DEBUG) Log.w(TAG, "getDeclaringClass:"+this.constructor.getDeclaringClass().getName());
		}

		Class<?>[] exceptionTypes = this.constructor.getExceptionTypes();
		if (exceptionTypes != null) {
			if(DEBUG) Log.w(TAG, "getExceptionTypes() size:"+exceptionTypes.length);
			for (Class<?> c : exceptionTypes) {
				if(DEBUG) Log.w(TAG, "getExceptionTypes():"+c.getName());
			}
		}else{
			if(DEBUG) Log.w(TAG, "getExceptionTypes: == null");
		}

		Class<?>[] parameterTypes = this.constructor.getParameterTypes();
		if (parameterTypes != null) {
			if(DEBUG) Log.w(TAG, "getParameterTypes() size:"+parameterTypes.length);
			for (Class<?> c : parameterTypes) {
				if(DEBUG) Log.w(TAG, "getParameterTypes():"+c.getName());
			}
		}else{
			if(DEBUG) Log.w(TAG, "getParameterTypes: == null");
		}

		Type[] genericExceptionTypes = this.constructor.getGenericExceptionTypes();
		if (genericExceptionTypes != null) {
			if(DEBUG) Log.w(TAG, "getGenericExceptionTypes() size:"+genericExceptionTypes.length);
			for (Type t : genericExceptionTypes) {
				if(DEBUG) Log.w(TAG, "getGenericExceptionTypes():"+t.toString());
			}
		}else{
			if(DEBUG) Log.w(TAG, "getGenericExceptionTypes: == null");
		}

		Type[] genericParameterTypes = this.constructor.getGenericParameterTypes();
		if (genericParameterTypes != null) {
			if(DEBUG) Log.w(TAG, "getGenericParameterTypes() size:"+genericParameterTypes.length);
			for (Type t : genericParameterTypes) {
				if(DEBUG) Log.w(TAG, "getGenericParameterTypes():"+t.toString());
			}
		}else{
			if(DEBUG) Log.w(TAG, "getGenericParameterTypes: == null");
		}

		if(DEBUG) Log.w(TAG, "getModifier:"+getModifier());
		if(DEBUG) Log.w(TAG, "Modifier.toString:"+Modifier.toString( getModifier() ));

		if (getAnnotations() != null) {
			if(DEBUG) Log.w(TAG, "getAnnotations size:"+getAnnotations().length);
			for (Annotation a : getAnnotations()) {
				if(DEBUG) Log.w(TAG, "getAnnotations():annotationType():"+a.annotationType().getName());
				if(DEBUG) Log.w(TAG, "getAnnotations():getClass()"+a.getClass().getName());
				if(DEBUG) Log.w(TAG, "getAnnotations():toString()"+a.toString());
			}
		}

		if(DEBUG) Log.w(TAG, "isSynthetic:"+isSynthetic());
		if(DEBUG) Log.w(TAG, "isVarArgs:"+isVarArgs());

		if (this.getReflectMode() != null) {
			if(DEBUG) Log.w(TAG, "mode:"+this.getReflectMode().name());
		}

		if(DEBUG) Log.w(TAG, "------ ReflectConstructor print() end ------");
	}

}
