package com.droidsimple.lang.reflect;

/**
 * 反射工厂静态类
 * 
 * @author simplesome
 *
 */
public class ReflectFactory {

	public static ReflectClass createClass(Class<?> clazz) {
		return new ReflectFactoryImpl().createClass(clazz);
	}

	public static ReflectClass createClass(Object object) {
		return new ReflectFactoryImpl().createClass(object);
	}

	public static ReflectClass createClass(String classPath) {
		return new ReflectFactoryImpl().createClass(classPath);
	}

	public static ReflectClass createClass(Class<?> clazz, ReflectMode mode) {
		return new ReflectFactoryImpl().createClass(clazz, mode);
	}

	public static ReflectClass createClass(Object object, ReflectMode mode) {
		return new ReflectFactoryImpl().createClass(object, mode);
	}

	public static ReflectClass createClass(String classPath, ReflectMode mode) {
		return new ReflectFactoryImpl().createClass(classPath, mode);
	}

}
