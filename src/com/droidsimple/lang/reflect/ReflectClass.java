package com.droidsimple.lang.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 反射接口类
 * 
 * @author simplesome
 *
 */
public interface ReflectClass {

	/**
	 * @return
	 */
	public Class<?> getClazz();

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
	 * 得到名称
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 得到绝对名称
	 * @return
	 */
	public String getCanonicalName();

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
	public java.lang.annotation.Annotation[] getAnnotations();

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
	 * 是否有注释
	 * 
	 * @return
	 */
	public boolean isAnnotation();

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
	 * 打印
	 */
	public void print();

}
