package com.droidcommon.lang.reflect;

import java.util.HashMap;
import java.util.Map;

/**
 * 反射内存缓存类。
 * 
 * @author simplesome
 *
 */
public class ReflectClassCache {

	private static final String TAG = ReflectClassCache.class.getSimpleName();
	private static boolean DEBUG = false;

	private static Map<String, ReflectClass> mapClass = new HashMap<String, ReflectClass>();

	/**
	 * 设置调试日志开关
	 * 
	 * @param debug
	 */
	public static void setDebugEnable(boolean debug) {
		DEBUG = debug;
	}

	public static ReflectClass get(Class<?> clazz) {
		return get(clazz, ReflectMode.Self_Exclude_Extends);
	}

	public static ReflectClass get(Object object) {
		return get(object, ReflectMode.Self_Exclude_Extends);
	}

	public static ReflectClass get(String classPath) {
		return get(classPath, ReflectMode.Self_Exclude_Extends);
	}

	public static ReflectClass get(Class<?> clazz, ReflectMode mode) {
		String key = clazz.getCanonicalName() + "+" + mode.name();

		ReflectClass c = null;
		c = mapClass.get(key);
		if (c == null) {
			// 没有，则放入
			c = new ReflectClassImpl(clazz, mode);
			mapClass.put(key, c);
		}else{
			// 比较mode，如果mode不相同，则替换
			//			if (c.getReflectMode() != mode) {
			//				Log.w(TAG, "c.getReflectMode() != mode");
			//				mapClass.remove(key);
			//				mapClass.put(key, c);
			//			}
		}

		return c;
	}

	public static ReflectClass get(Object object, ReflectMode mode) {
		String key = object.getClass().getCanonicalName() + "+" + mode.name();

		ReflectClass c = null;
		c = mapClass.get(key);
		if (c == null) {
			// 没有，则放入
			c = new ReflectClassImpl(object, mode);
			mapClass.put(key, c);
		}else{
			// 比较mode，如果mode不相同，则替换
			//			if (c.getReflectMode() != mode) {
			//				Log.w(TAG, "c.getReflectMode() != mode");
			//				mapClass.remove(key);
			//				mapClass.put(key, c);
			//			}
		}

		return c;
	}

	public static ReflectClass get(String classPath, ReflectMode mode) {
		String key = classPath + "+" + mode.name();

		ReflectClass c = null;
		c = mapClass.get(key);
		if (c == null) {
			// 没有，则放入
			c = new ReflectClassImpl(classPath, mode);
			mapClass.put(key, c);
		}else{
			// 比较mode，如果mode不相同，则替换
			//			if (c.getReflectMode() != mode) {
			//				Log.w(TAG, "c.getReflectMode() != mode");
			//				mapClass.remove(key);
			//				mapClass.put(key, c);
			//			}
		}

		return c;
	}

	/**
	 * 加入缓存
	 * 
	 * @param clazz
	 */
	public static void put(Class<?> clazz) {
		put(clazz, ReflectMode.Self_Exclude_Extends);
	}

	/**
	 * 加入缓存
	 * 
	 * @param object
	 */
	public static void put(Object object) {
		put(object, ReflectMode.Self_Exclude_Extends);
	}

	/**
	 * 加入缓存
	 * 
	 * @param classPath
	 */
	public static void put(String classPath) {
		put(classPath, ReflectMode.Self_Exclude_Extends);
	}

	/**
	 * 加入缓存
	 * 
	 * @param clazz
	 */
	public static void put(Class<?> clazz, ReflectMode mode) {
		String key = clazz.getCanonicalName() + "+" + mode.name();
		mapClass.put(key, new ReflectClassImpl(clazz, mode));
	}

	/**
	 * 加入缓存
	 * 
	 * @param object
	 */
	public static void put(Object object, ReflectMode mode) {
		String key = object.getClass().getCanonicalName() + "+" + mode.name();
		mapClass.put(key, new ReflectClassImpl(object, mode));
	}

	/**
	 * 加入缓存
	 * 
	 * @param classPath
	 */
	public static void put(String classPath, ReflectMode mode) {
		String key = classPath + "+" + mode.name();
		mapClass.put(key, new ReflectClassImpl(classPath, mode));
	}

	/**
	 * 移除
	 * 
	 * @param classPath
	 * @param mode
	 */
	public static void remove(Class<?> clazz) {
		remove(clazz, ReflectMode.Self_Exclude_Extends);
	}

	/**
	 * 移除
	 * 
	 * @param classPath
	 * @param mode
	 */
	public static void remove(Object object) {
		remove(object, ReflectMode.Self_Exclude_Extends);
	}

	/**
	 * 移除
	 * 
	 * @param classPath
	 * @param mode
	 */
	public static void remove(String classPath) {
		remove(classPath, ReflectMode.Self_Exclude_Extends);
	}

	/**
	 * 移除
	 * 
	 * @param classPath
	 * @param mode
	 */
	public static void remove(Class<?> clazz, ReflectMode mode) {
		String key = clazz.getCanonicalName() + "+" + mode.name();
		mapClass.remove(key);
	}

	/**
	 * 移除
	 * 
	 * @param classPath
	 * @param mode
	 */
	public static void remove(Object object, ReflectMode mode) {
		String key = object.getClass().getCanonicalName() + "+" + mode.name();
		mapClass.remove(key);
	}

	/**
	 * 移除
	 * 
	 * @param classPath
	 * @param mode
	 */
	public static void remove(String classPath, ReflectMode mode) {
		String key = classPath + "+" + mode.name();
		mapClass.remove(key);
	}

	/**
	 * 清除全部
	 */
	public static void clear() {
		mapClass.clear();
	}

}
