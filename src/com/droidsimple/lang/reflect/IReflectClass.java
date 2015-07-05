package com.droidsimple.lang.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 反射类接口类
 * 
 * @author simplesome
 *
 */
public interface IReflectClass {

	/**
	 * 得到Class<?>
	 * 
	 * @return
	 */
	public Class<?> getClazz();

	/**
	 * 得到绝对路径，内部类用$表示
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 得到简称
	 * 
	 * @return
	 */
	public String getSimpleName();

	/**
	 * 得到绝对路径，内部类用.表示 TODO 这个考虑省略。
	 * 
	 * @return
	 */
	public String getCanonicalName();

	/**
	 * 得到方式
	 * 
	 * @return
	 */
	public ReflectMode getReflectMode();

	/**
	 * 得到修饰符
	 * 
	 * @return
	 */
	public int getModifier();

	/**
	 * 得到构造列表
	 * 
	 * @return
	 */
	public List<ReflectConstructor> getConstructors();

	/**
	 * 得到属性列表
	 * 
	 * @return
	 */
	public List<ReflectField> getFields();

	/**
	 * 得到方法列表
	 * 
	 * @return
	 */
	public List<ReflectMethod> getMethods();

	/**
	 * 得到注释列表
	 * 
	 * @return
	 */
	public Annotation[] getAnnotations();

	/**
	 * 得到父Class
	 * 
	 * @return
	 */
	public Class<?> getSuperClass();

	/**
	 * 得到实现的接口列表
	 * 
	 * @return
	 */
	public Class<?>[] getInterfaces();

	/**
	 * 得到属性
	 * 
	 * @param name
	 * @return
	 */
	public Field getField(String name);

	/**
	 * 得到方法
	 * 
	 * @param name
	 * @return
	 */
	public Method getMethod(String name, Class<?>[] methodParame);

	/**
	 * 是否存在
	 * 
	 * @return
	 */
	public boolean isExists();

	/**
	 * 是否有注释
	 * 
	 * @return
	 */
	public boolean isAnnotation();

	/**
	 * 是否存在此注释
	 * 
	 * @param annotationClass
	 * @return
	 */
	public boolean hasAnnotation(Class<? extends Annotation> annotationClass);

	/**
	 * 是否为匿名类
	 * 
	 * @return
	 */
	public boolean isAnonymousClass();

	/**
	 * 是否为数组
	 * @return
	 */
	public boolean isArray();

	/**
	 * 是否为枚举
	 * 
	 * @return
	 */
	public boolean isEnum();

	/**
	 * 是否为接口类
	 * 
	 * @return
	 */
	public boolean isInterface();

	/**
	 * 是否存在此接口
	 * 
	 * @param interfaceClass
	 * @return
	 */
	public boolean hasInterface(Class<?> interfaceClass);

	/**
	 * 是否为本地类
	 * 
	 * @return
	 */
	public boolean isLocalClass();

	/**
	 * 是否为成员类
	 * 
	 * @return
	 */
	public boolean isMemberClass();

	/**
	 * 是否为基本类型的类
	 * 
	 * @return
	 */
	public boolean isPrimitive();

	/**
	 * 是否为复合类
	 * 
	 * @return
	 */
	public boolean isSynthetic();

	/**
	 * 比较相等
	 * 
	 * @param obj
	 * @return
	 */
	public boolean equals(Object obj);

	/**
	 * 得到hash值
	 * 
	 * @return
	 */
	public int hashCode();

	/**
	 * 打印
	 */
	public void print();

}
