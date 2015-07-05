package com.droidsimple.lang.reflect;

/**
 * 反射类工厂实现类
 * 
 * @author simplesome
 *
 */
public class ReflectClassFactory extends AbsReflectClassFactory {

	@Override
	public IReflectClass createClass(Class<?> clazz) {
		return ReflectClassCache.get(clazz);
	}

	@Override
	public IReflectClass createClass(Object object) {
		return ReflectClassCache.get(object);
	}

	@Override
	public IReflectClass createClass(String classPath) {
		return ReflectClassCache.get(classPath);
	}

	@Override
	public IReflectClass createClass(Class<?> clazz, ReflectMode mode) {
		return ReflectClassCache.get(clazz, mode);
	}

	@Override
	public IReflectClass createClass(Object object, ReflectMode mode) {
		return ReflectClassCache.get(object, mode);
	}

	@Override
	public IReflectClass createClass(String classPath, ReflectMode mode) {
		return ReflectClassCache.get(classPath, mode);
	}

}
