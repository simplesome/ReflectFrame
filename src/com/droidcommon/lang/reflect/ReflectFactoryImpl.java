package com.droidcommon.lang.reflect;

/**
 * 反射工厂实现类
 * 
 * @author simplesome
 *
 */
public class ReflectFactoryImpl extends AbsReflectFactory {

	@Override
	public ReflectClass createClass(Class<?> clazz) {
		return ReflectClassCache.get(clazz);
	}

	@Override
	public ReflectClass createClass(Object object) {
		return ReflectClassCache.get(object);
	}

	@Override
	public ReflectClass createClass(String classPath) {
		return ReflectClassCache.get(classPath);
	}

	@Override
	public ReflectClass createClass(Class<?> clazz, ReflectMode mode) {
		return ReflectClassCache.get(clazz, mode);
	}

	@Override
	public ReflectClass createClass(Object object, ReflectMode mode) {
		return ReflectClassCache.get(object, mode);
	}

	@Override
	public ReflectClass createClass(String classPath, ReflectMode mode) {
		return ReflectClassCache.get(classPath, mode);
	}

}
