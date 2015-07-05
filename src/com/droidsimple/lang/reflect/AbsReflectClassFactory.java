package com.droidsimple.lang.reflect;

/**
 * 反射类工厂抽象类
 * 
 * @author simplesome
 *
 */
public abstract class AbsReflectClassFactory {
	abstract public IReflectClass createClass(Class<?> clazz);
	abstract public IReflectClass createClass(Object object);
	abstract public IReflectClass createClass(String classPath);
	abstract public IReflectClass createClass(Class<?> clazz, ReflectMode mode);
	abstract public IReflectClass createClass(Object object, ReflectMode mode);
	abstract public IReflectClass createClass(String classPath, ReflectMode mode);
}
