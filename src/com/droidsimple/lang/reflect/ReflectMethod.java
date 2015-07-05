package com.droidsimple.lang.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import com.droidsimple.util.Log;


/**
 * 反射方法类
 * 
 * @author simplesome
 *
 */
public class ReflectMethod {

	private static final String TAG = ReflectMethod.class.getSimpleName();
	private static boolean DEBUG = false;

	// 得到声明类<lass<?>，就是此方法所在的类。
	private Class<?> clazz = null;

	// 修饰符
	private int modifier = Modifier.PUBLIC;

	// 返回类型Class<?>
	private Class<?> returnType = null;

	// 名称
	private String name = null;

	// 自身方法Method
	private Method method = null;

	// 注释
	private Annotation[] annotations = null;

	// 枚举模式
	private ReflectMode reflectMode = ReflectMode.Self_Exclude_Extends;

	// 参数
	//	private Object[] parameter = null;

	private boolean isBridge = false;
	private boolean isAccessible = false;
	private boolean isSynthetic = false;
	private boolean isVarArgs = false;

	/**
	 * 设置调试日志开关
	 * 
	 * @param debug
	 */
	public static void setDebugEnable(boolean debug) {
		DEBUG = debug;
	}

	public ReflectMethod(Method method) {
		init(method, ReflectMode.Self_Exclude_Extends);
	}

	public ReflectMethod(Method method, ReflectMode mode) {
		init(method, mode);
	}

	// TODO 未实现,另一种表示method的方式 .
	public ReflectMethod(Class<?> clazz, String methodName, Class<?>... methodParams) {

	}

	// TODO 
	public ReflectMethod(Object entity, String methodName, Class<?>... methodParams) {

	}

	// TODO 
	public ReflectMethod(String classPath, String methodName, Class<?>... methodParams) {

	}

	/**
	 * 初始化
	 * 
	 * @param method
	 */
	private void init(Method method, ReflectMode mode) {

		this.method = method;
		this.reflectMode = mode;

		// 得到声明类<lass<?>，就是此方法所在的类。
		this.clazz = this.method.getDeclaringClass();

		this.name = this.method.getName();

		this.modifier = this.method.getModifiers();

		this.returnType = this.method.getReturnType();

		this.isBridge = this.method.isBridge();
		this.isAccessible = this.method.isAccessible();
		this.isSynthetic = this.method.isSynthetic();
		this.isVarArgs = this.method.isVarArgs();

		// +++ 分解注解
		if (this.reflectMode == ReflectMode.Self_Exclude_Extends) {
			this.annotations = this.method.getDeclaredAnnotations(); // 自身注解
		}else if (this.reflectMode == ReflectMode.Public_And_Extends || this.reflectMode == ReflectMode.Public_And_Extends_Exclude_Object) {
			this.annotations = this.method.getAnnotations(); // 共有注解
		}

	}

	/**
	 * 得到声明类<lass<?>，就是此方法所在的类。
	 * 
	 * @return
	 */
	public Class<?> getClazz() {
		return this.clazz;
	}

	public String getName() {
		return this.name;
	}

	public Method getMethod() {
		return this.method;
	}

	public int getModifier() {
		return this.modifier;
	}

	public Annotation[] getAnnotations() {
		return this.annotations;
	}

	public Class<?> getReturnType() {
		return this.returnType;
	}

	public ReflectMode getReflectMode() {
		return this.reflectMode;
	}

	/**
	 * 调用方法
	 * 
	 * @param receiver
	 * @param args
	 * @return
	 */
	public Object invoke(Object receiver, Object... args) {

		// 私有方法被调用，需要设置setAccessible(true)
		this.method.setAccessible(true);

		try {
			return this.method.invoke(receiver, args);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 是否有注释 
	 * 
	 * @return
	 */
	public boolean hasAnnotation() {

		if (this.annotations == null) {
			return false;
		}

		if (this.annotations.length > 0) {
			return true;
		}

		return false;
	}

	/*
	 * 判断此注释是否存在（已验证）
	 */
	public boolean hasAnnotation(Class<? extends Annotation> annotationClass) {
		return this.method.isAnnotationPresent(annotationClass);
	}

	public boolean isBridge() {
		return this.isBridge;
	}

	public boolean isAccessible() {
		return this.isAccessible;
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
		return this.method.equals(obj);
	}

	/**
	 * 得到hash值
	 * 
	 * @return
	 */
	@Override
	public int hashCode() {
		return this.method.hashCode();
	}

	/**
	 * 打印
	 */
	public void print() {
		if(DEBUG) Log.w(TAG, "------ ReflectMethod print() start ------");

		if(DEBUG) Log.w(TAG, "getName:"+this.getName());
		if(DEBUG) Log.w(TAG, "toGenericString:"+this.method.toGenericString());
		if(DEBUG) Log.w(TAG, "toString:"+this.method.toString());

		if(DEBUG) Log.w(TAG, "getModifier:"+this.getModifier());
		if(DEBUG) Log.w(TAG, "Modifier.toString:"+Modifier.toString( getModifier() ));

		if (this.method.getDeclaringClass() != null) {
			if(DEBUG) Log.w(TAG, "getDeclaringClass():"+this.method.getDeclaringClass().getName());
		}

		if (getAnnotations() != null) {
			if(DEBUG) Log.w(TAG, "getAnnotations size:"+getAnnotations().length);
			for (Annotation a : getAnnotations()) {
				if(DEBUG) Log.w(TAG, "getAnnotations():annotationType():"+a.annotationType().getName());
				if(DEBUG) Log.w(TAG, "getAnnotations():getClass()"+a.getClass().getName());
				if(DEBUG) Log.w(TAG, "getAnnotations():toString()"+a.toString());
			}
		}

		Class<?>[] exceptionTypes = this.method.getExceptionTypes();
		if (exceptionTypes != null) {
			if(DEBUG) Log.w(TAG, "getExceptionTypes() size:"+exceptionTypes.length);
			for (Class<?> c : exceptionTypes) {
				if(DEBUG) Log.w(TAG, "getExceptionTypes():"+c.getName());
			}
		}else{
			if(DEBUG) Log.w(TAG, "getExceptionTypes: == null");
		}

		Class<?>[] parameterTypes = this.method.getParameterTypes();
		if (parameterTypes != null) {
			if(DEBUG) Log.w(TAG, "getParameterTypes() size:"+parameterTypes.length);
			for (Class<?> c : parameterTypes) {
				if(DEBUG) Log.w(TAG, "getParameterTypes():"+c.getName());
			}
		}else{
			if(DEBUG) Log.w(TAG, "getParameterTypes: == null");
		}

		Type[] genericExceptionTypes = this.method.getGenericExceptionTypes();
		if (genericExceptionTypes != null) {
			if(DEBUG) Log.w(TAG, "getGenericExceptionTypes() size:"+genericExceptionTypes.length);
			for (Type t : genericExceptionTypes) {
				if(DEBUG) Log.w(TAG, "getGenericExceptionTypes():"+t.toString());
			}
		}else{
			if(DEBUG) Log.w(TAG, "getGenericExceptionTypes: == null");
		}

		Type[] genericParameterTypes = this.method.getGenericParameterTypes();
		if (genericParameterTypes != null) {
			if(DEBUG) Log.w(TAG, "getGenericParameterTypes() size:"+genericParameterTypes.length);
			for (Type t : genericParameterTypes) {
				if(DEBUG) Log.w(TAG, "getGenericParameterTypes():"+t.toString());
			}
		}else{
			if(DEBUG) Log.w(TAG, "getGenericParameterTypes: == null");
		}

		if(DEBUG) Log.w(TAG, "hasAnnotation(Override.class):"+this.hasAnnotation(Override.class));

		if (this.getReturnType() != null) {
			if(DEBUG) Log.w(TAG, "getReturnType:"+this.getReturnType().getName());
		}else{
			if(DEBUG) Log.w(TAG, "getReturnType: == null");
		}

		if (this.method.getGenericReturnType() != null) {
			if(DEBUG) Log.w(TAG, "getGenericReturnType:"+this.method.getGenericReturnType().toString());
		}else{
			if(DEBUG) Log.w(TAG, "getGenericReturnType: == null");
		}

		if(DEBUG) Log.w(TAG, "isBridge:"+this.isBridge());
		if(DEBUG) Log.w(TAG, "isAccessible:"+this.isAccessible());
		if(DEBUG) Log.w(TAG, "isSynthetic:"+this.isSynthetic());
		if(DEBUG) Log.w(TAG, "isVarArgs:"+this.isVarArgs());

		if (this.getReflectMode() != null) {
			if(DEBUG) Log.w(TAG, "mode:"+this.getReflectMode().name());
		}

		if(DEBUG) Log.w(TAG, "------ ReflectMethod print() end ------");
	}

}
