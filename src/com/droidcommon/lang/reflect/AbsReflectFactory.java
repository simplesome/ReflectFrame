package com.droidcommon.lang.reflect;

/**
 * 反射工厂抽象类
 * 
 * @author simplesome
 *
 */
public abstract class AbsReflectFactory {
	abstract public ReflectClass createClass(Class<?> clazz);
	abstract public ReflectClass createClass(Object object);
	abstract public ReflectClass createClass(String classPath);
	abstract public ReflectClass createClass(Class<?> clazz, ReflectMode mode);
	abstract public ReflectClass createClass(Object object, ReflectMode mode);
	abstract public ReflectClass createClass(String classPath, ReflectMode mode);
}
