package com.droidsimple.lang.reflect;


import java.lang.reflect.InvocationTargetException;

import com.droidsimple.util.Log;


/**
 * 反射工具类
 * 
 * getDeclaredMethod()与getMethod()之间的区别：
 * 由此可见，getDeclaredMethod*()获取的是类自身声明的所有方法，包含public、protected和private方法。
 * getMethod*()获取的是类的所有共有方法，这就包括自身的所有public方法，和从基类继承的、从接口实现的所有public方法。
 * 
 * 注意：
 * 因此用反射调用私有方法，必须用getDeclaredMethod方法，同时注意调用私有方法和改变私有变量一样，
 * 必须在调用前设置 method.setAccessible(true)。
 * 
 * 2.2是否可以找到android.app.DownloadManager？？？
 * 答：找不到。
 * 
 * getStatic***()方法可以获取下面属性：
 * 
 * public static final int aaaa = 23;
	public static int bbbb = 47;

	protected static final int cccc = 56;
	protected static int dddd = 95;

	private static final int eeee = 90;
	private static int ffff = 28;
 * 
 * @author simplesome
 *
 */
public class ReflectTool {

	private static final String TAG = ReflectTool.class.getSimpleName();
	private static boolean DEBUG = false;

	/**
	 * 设置调试日志开关
	 * 
	 * @param debug
	 */
	public static void setDebugEnable(boolean debug) {
		DEBUG = debug;
		ReflectClassImpl.setDebugEnable(debug);
		ReflectClassCache.setDebugEnable(debug);
		ReflectClassExclude.setDebugEnable(debug);
		ReflectConstructor.setDebugEnable(debug);
		ReflectField.setDebugEnable(debug);
		ReflectMethod.setDebugEnable(debug);
	}

	/**
	 * 判断字符串是否为空，等于null或者长度不大于零都视为空字符串
	 * 
	 * @param src
	 * @return
	 */
	protected static boolean isEmptyString(String src) {
		if (src == null) {
			return true;
		}

		if (src.length() <= 0) {
			return true;
		}

		return false;
	}

	/**
	 * 转成类
	 * 
	 * @param clazz
	 * @return
	 */
	public static ReflectClassImpl parseClass(Class<?> clazz) {
		return new ReflectClassImpl(clazz);
	}

	/**
	 * 转成类
	 * 
	 * @param object
	 * @return
	 */
	public static ReflectClassImpl parseClass(Object object) {
		return new ReflectClassImpl(object);
	}

	/**
	 * 转成类
	 * 
	 * @param classPath
	 * @return
	 */
	public static ReflectClassImpl parseClass(String classPath) {
		return new ReflectClassImpl(classPath);
	}

	/**
	 * 转成类
	 * 
	 * @param clazz
	 * @param declared
	 * @return
	 */
	public static ReflectClassImpl parseClass(Class<?> clazz, ReflectMode declared) {
		return new ReflectClassImpl(clazz, declared);
	}

	/**
	 * 转成类
	 * 
	 * @param object
	 * @param declared
	 * @return
	 */
	public static ReflectClassImpl parseClass(Object object, ReflectMode declared) {
		return new ReflectClassImpl(object, declared);
	}

	/**
	 * 转成类
	 * 
	 * @param classPath
	 * @param declared
	 * @return
	 */
	public static ReflectClassImpl parseCLass(String classPath, ReflectMode declared) {
		return new ReflectClassImpl(classPath, declared);
	}

	/**
	 * 判断类是否存在
	 * 
	 * @param classPath
	 * @return
	 */
	public static boolean isClassExists(String classPath) {
		if (isEmptyString(classPath)) {
			if(DEBUG) Log.e(TAG, "isEmptyString(classPath) == true");
			return false;
		}

		boolean isExists = false;

		try {
			Class<?> clazz = Class.forName(classPath);
			if (clazz != null) {
				isExists = true;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
			isExists = true;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			isExists = true;
		}

		if(DEBUG) Log.e(TAG, "isClassExists():classPath:"+classPath+" == "+isExists);
		return isExists;
	}

	/**
	 * 判断方法是否存在（不管方法是private还是继承过来的，都适用）
	 * 
	 * 原理：通过得到方法的数组，再遍历比较。
	 * 
	 * @param classPath
	 * @param methodName
	 * @return
	 */
	public static boolean isMethodExists(String classPath, String methodName) {
		if (isEmptyString(classPath)) {
			if(DEBUG) Log.e(TAG, "isEmptyString(classPath) == true");
			return false;
		}

		if (isEmptyString(methodName)) {
			if(DEBUG) Log.e(TAG, "isEmptyString(methodName) == true");
			return false;
		}

		boolean isExists = false;

		try {
			Class<?> clazz = Class.forName(classPath);
			if (clazz != null) {
				java.lang.reflect.Method[] method = clazz.getMethods();
				for (java.lang.reflect.Method m : method) {
					// 比较
					if (methodName.equals(m.getName())) {
						isExists = true;
						if(DEBUG) Log.e(TAG, "在getMethods()里找到:"+methodName);
						break;
					}
				}
				// 没在getDeclaredMethods里找到，则使用getMethods找。
				if (isExists == false) {
					java.lang.reflect.Method[] method2 = clazz.getDeclaredMethods();
					for (java.lang.reflect.Method m : method2) {
						// 比较
						if (methodName.equals(m.getName())) {
							isExists = true;
							if(DEBUG) Log.e(TAG, "在getDeclaredMethods里找到:"+methodName);
							break;
						}
					}
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		if(DEBUG) Log.e(TAG, "isMethodExists():classPath:"+classPath+",methodName:"+methodName+" == "+isExists);
		return isExists;
	}

	/**
	 * 判断属性是否存在
	 * 
	 * @param classPath
	 * @param fieldName
	 * @return
	 */
	public static boolean isFieldExists(String classPath, String fieldName) {
		if (isEmptyString(classPath)) {
			if(DEBUG) Log.e(TAG, "isEmptyString(classPath) == true");
			return false;
		}

		if (isEmptyString(fieldName)) {
			if(DEBUG) Log.e(TAG, "isEmptyString(fieldName) == true");
			return false;
		}

		boolean isExists = false;

		try {
			Class<?> clazz = Class.forName(classPath);
			if (clazz != null) {
				java.lang.reflect.Field[] field = clazz.getFields();
				for (java.lang.reflect.Field m : field) {
					// 比较
					if (fieldName.equals(m.getName())) {
						isExists = true;
						if(DEBUG) Log.e(TAG, "在getFields()里找到:"+fieldName);
						break;
					}
				}
				// 没在getDeclaredMethods里找到，则使用getMethods找。
				if (isExists == false) {
					java.lang.reflect.Field[] field2 = clazz.getDeclaredFields();
					for (java.lang.reflect.Field m : field2) {
						// 比较
						if (fieldName.equals(m.getName())) {
							isExists = true;
							if(DEBUG) Log.e(TAG, "在getDeclaredFields里找到:"+fieldName);
							break;
						}
					}
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		if(DEBUG) Log.e(TAG, "isFieldExists():classPath:"+classPath+",fieldName:"+fieldName+" == "+isExists);
		return isExists;
	}

	/**
	 * 得到构造函数
	 * 
	 * 例子：
	 * +++无参数
	 * // 得到构造函数
	 * java.lang.reflect.Constructor<?> c = ReflectTool.getConstructor("com.droidsimple.lang.reflect.ReflectBBB");
	 * // 创建类的对象
	 * Object objectClass = ReflectTool.createObject(c);
	 * 
	 * +++有参数
	 * // 得到构造函数
	 * java.lang.reflect.Constructor<?> c = ReflectTool.getConstructor("com.droidsimple.lang.reflect.ReflectBBB", int.class, String.class);
	 * // 创建类的对象
	 * Object objectClass = ReflectTool.createObject(c, 34, "abvnnnn");
	 * 
	 * @param classPath
	 * @param parame
	 * @return
	 */
	public static java.lang.reflect.Constructor<?> getConstructor(String classPath, Class<?>... parame) {
		if (isEmptyString(classPath)) {
			if(DEBUG) Log.e(TAG, "isEmptyString(classPath) == true");
			return null;
		}

		boolean isFinded = false;

		Class<?> clazz = null;
		try {
			clazz = Class.forName( classPath );
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		java.lang.reflect.Constructor<?> constructor = null;

		if (clazz != null) {
			try {
				constructor = clazz.getConstructor(parame);
				isFinded = (constructor != null)?true:false;
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}

		if (isFinded == false && clazz != null) { // 如果getDeclaredMethod()方法找不到，则使用getMethod()找。
			try {
				constructor = clazz.getDeclaredConstructor(parame);
				isFinded = (constructor != null)?true:false;
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}

		if (isFinded && constructor != null) {
			constructor.setAccessible(true); // 调用私有的方法要setAccessible(true)
		}

		if(DEBUG) Log.e(TAG, "getConstructor():classPath:"+classPath+",isFinded:"+isFinded);
		return constructor;
	}

	/**
	 * 通过构造函数创建类的对象
	 * 
	 * 例子：
	 * +++无参数
	 * // 得到构造函数
	 * java.lang.reflect.Constructor<?> c = ReflectTool.getConstructor("com.droidsimple.lang.reflect.ReflectBBB");
	 * // 创建类的对象
	 * Object objectClass = ReflectTool.createObject(c);
	 * 
	 * +++有参数
	 * // 得到构造函数
	 * java.lang.reflect.Constructor<?> c = ReflectTool.getConstructor("com.droidsimple.lang.reflect.ReflectBBB", int.class, String.class);
	 * // 创建类的对象
	 * Object objectClass = ReflectTool.createObject(c, 34, "abvnnnn");
	 * 
	 * 注意:
	 * clazz.newInstance()相当于无参数
	 * 
	 * @return
	 */
	public static Object createObject(java.lang.reflect.Constructor<?> constructor, Object... args) {
		try {
			return constructor.newInstance(args);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 得到方法（不论private和protected及public都可以得到，不论是否static都可以得到）
	 * 
	 * 例子：
	 * 
	 * 得到无参数的方法：
	 * java.lang.reflect.Method method = ReflectTool.getMethod("com.droidsimple.lang.reflect.ReflectBBB", "private_static_print");
	 * 
	 * 得到有参数的方法：
	 * java.lang.reflect.Method method = ReflectTool.getMethod("com.droidsimple.lang.reflect.ReflectBBB", "public_static_print", int.class, String.class);
	 * 
	 * getDeclaredMethod()与getMethod()之间的区别：
	 * 由此可见，getDeclaredMethod*()获取的是类自身声明的所有方法，包含public、protected和private方法。
	 * getMethod*()获取的是类的所有共有方法，这就包括自身的所有public方法，和从基类继承的、从接口实现的所有public方法。
	 * 
	 * 注意：
	 * 因此用反射调用私有方法，必须用getDeclaredMethod方法，同时注意调用私有方法和改变私有变量一样，
	 * 必须在调用前设置 method.setAccessible(true)，这就是传说中的暴力反射吧。
	 * 
	 * @param classPath 类的绝对路径
	 * @param methodName 方法名
	 * @param methodParame 方法参数
	 * @return
	 */
	public static java.lang.reflect.Method getMethod(String classPath, String methodName, Class<?>... methodParame) {
		if (isEmptyString(classPath)) {
			if(DEBUG) Log.e(TAG, "isEmptyString(classPath) == true");
			return null;
		}
		
		if (isEmptyString(methodName)) {
			if(DEBUG) Log.e(TAG, "isEmptyString(methodName) == true");
			return null;
		}

		boolean isFinded = false;

		Class<?> clazz = null;
		try {
			clazz = Class.forName( classPath );
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		java.lang.reflect.Method method = null;

		if (clazz != null) {
			try {
				method = clazz.getMethod(methodName, methodParame);
				isFinded = (method != null)?true:false;
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}

		if (isFinded == false && clazz != null) { // 如果getDeclaredMethod()方法找不到，则使用getMethod()找。
			try {
				method = clazz.getDeclaredMethod(methodName, methodParame);
				isFinded = (method != null)?true:false;
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}

		if (isFinded && method != null) {
			method.setAccessible(true); // 调用私有的方法要setAccessible(true)
		}

		if(DEBUG) Log.e(TAG, "getMethod():classPath:"+classPath+",methodName:"+methodName+",isFinded:"+isFinded);
		return method;
	}

	/**
	 * 调用对象的方法
	 * 
	 * @param entity 对象，可以为null
	 * @param method 反射方法
	 * @param args 参数列表
	 * @return
	 */
	public static Object invokeMethod(Object entity, java.lang.reflect.Method method, Object... args) {
		if (method == null) {
			if(DEBUG) Log.e(TAG, new NullPointerException("method == null").toString());
			return null;
		}
		
		try {
			method.setAccessible(true); // 调用私有的方法要setAccessible(true)
			return method.invoke(entity, args);
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
	 * 调用静态方法
	 * 
	 * 例子：
	 * 		// 无参数
		java.lang.reflect.Method m1 = ReflectTool.getMethod("com.droidsimple.lang.reflect.ReflectBBB", "private_static_print");
		ReflectTool.invokeStaticMethod(m1);

		// 有参数
		java.lang.reflect.Method m2 = ReflectTool.getMethod("com.droidsimple.lang.reflect.ReflectBBB", "public_static_print", int.class, String.class);
		ReflectTool.invokeStaticMethod(m2, 3, "aaaa");
	 * 
	 * @param method
	 * @param args
	 * @return
	 */
	public static Object invokeStaticMethod(java.lang.reflect.Method method, Object... args) {
		return invokeMethod(null, method, args);
	}

	/**
	 * 得到属性
	 * 
	 * @param classPath
	 * @param fieldName
	 * @return
	 */
	public static java.lang.reflect.Field getField(String classPath, String fieldName) {
		if (isEmptyString(classPath)) {
			if(DEBUG) Log.e(TAG, "isEmptyString(classPath) == true");
			return null;
		}
		if (isEmptyString(fieldName)) {
			if(DEBUG) Log.e(TAG, "isEmptyString(fieldName) == true");
			return null;
		}

		boolean isFinded = false;

		Class<?> clazz = null;
		try {
			clazz = Class.forName( classPath );
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		java.lang.reflect.Field field = null;

		if (clazz != null) {
			try {
				field = clazz.getField(fieldName);
				isFinded = (field != null)?true:false;
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}

		if (isFinded == false && clazz != null) { // 如果getDeclaredField()方法找不到，则使用getField()找。
			try {
				field = clazz.getDeclaredField(fieldName);
				isFinded = (field != null)?true:false;
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}

		if (isFinded && field != null) {
			field.setAccessible(true); // 调用私有的方法要setAccessible(true)
		}

		if(DEBUG) Log.e(TAG, "getField():classPath:"+classPath+",fieldName:"+fieldName+",isFinded:"+isFinded);
		return field;
	}

	/**
	 * 得到属性的值
	 * 
	 * @param objectClass 可以为null
	 * @param classPath
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValue(Object objectClass, String classPath, String fieldName) {

		java.lang.reflect.Field field = getField(classPath, fieldName);

		if (field == null) {
			return null;
		}

		Object objectValue = null;
		try {
			if (objectClass == null) { // 静态属性不用objectClass
				objectValue = field.get(null);
			}else {
				objectValue = field.get(objectClass);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return objectValue;
	}

	/**
	 * 得到静态属性的属性
	 * 
	 * @param classPath
	 * @param fieldName
	 * @return
	 */
	public static Object getStaticFieldValue(String classPath, String fieldName) {
		return getFieldValue(null, classPath, fieldName);
	}

	/**
	 * 格式化构造方法
	 * 
	 * @param method
	 * @return
	 */
	public static String toConstructorString(java.lang.reflect.Constructor<?> constructor) {
		if (constructor == null) {
			return null;
		}
		String string = String.format("%s %s(%s)", java.lang.reflect.Modifier.toString(constructor.getModifiers()), 
				constructor.getName(), 
				formatMethodParame(constructor.getParameterTypes()));
		return string;
	}

	/**
	 * 格式化方法
	 * 
	 * @param method
	 * @return
	 */
	public static String toMethodString(java.lang.reflect.Method method) {
		if (method == null) {
			return null;
		}
		String string = String.format("%s %s %s(%s)", java.lang.reflect.Modifier.toString(method.getModifiers()), 
				method.getReturnType().getCanonicalName(), 
				method.getName(), 
				formatMethodParame(method.getParameterTypes()));
		return string;
	}

	/**
	 * 格式化属性
	 * 
	 * @param method
	 * @return
	 */
	public static String toFieldString(java.lang.reflect.Field field) {
		if (field == null) {
			return null;
		}
		String string = String.format("%s %s %s", java.lang.reflect.Modifier.toString(field.getModifiers()), 
				field.getType().getCanonicalName(), 
				field.getName());
		return string;
	}

	/**
	 * 格式化方法参数
	 * 
	 * @return
	 */
	public static String formatMethodParame(Class<?>[] clazz) {
		StringBuilder builder = new StringBuilder();
		int len = clazz.length;
		for (int n = 0; n < len; n++) {
			if (n > 0) {
				builder.append(",");
			}
			builder.append(clazz[n].getCanonicalName());
		}
		return builder.toString();
	}

	/**
	 * 将修饰符id转成字符串
	 * 
	 * @param modifiers
	 * @return
	 */
	public static String modifiers2String(int modifiers) {
		return java.lang.reflect.Modifier.toString(modifiers);
	}

	public static boolean isAbstract(int modifiers) {
		return java.lang.reflect.Modifier.isAbstract(modifiers);
	}

	public static boolean isFinal(int modifiers) {
		return java.lang.reflect.Modifier.isFinal(modifiers);
	}

	public static boolean isInterface(int modifiers) {
		return java.lang.reflect.Modifier.isInterface(modifiers);
	}

	public static boolean isNative(int modifiers) {
		return java.lang.reflect.Modifier.isNative(modifiers);
	}

	public static boolean isPrivate(int modifiers) {
		return java.lang.reflect.Modifier.isPrivate(modifiers);
	}

	public static boolean isProtected(int modifiers) {
		return java.lang.reflect.Modifier.isProtected(modifiers);
	}

	public static boolean isPublic(int modifiers) {
		return java.lang.reflect.Modifier.isPublic(modifiers);
	}

	public static boolean isStatic(int modifiers) {
		return java.lang.reflect.Modifier.isStatic(modifiers);
	}

	public static boolean isStrict(int modifiers) {
		return java.lang.reflect.Modifier.isStrict(modifiers);
	}

	public static boolean isSynchronized(int modifiers) {
		return java.lang.reflect.Modifier.isSynchronized(modifiers);
	}

	public static boolean isTransient(int modifiers) {
		return java.lang.reflect.Modifier.isTransient(modifiers);
	}

	public static boolean isVolatile(int modifiers) {
		return java.lang.reflect.Modifier.isVolatile(modifiers);
	}

	/**
	 * 打印类信息
	 * 
	 * @param classPath
	 */
	public static void printClass(String classPath) {
		String text = String.format("------ printClass( %s ) start ------", classPath);
		if(DEBUG) Log.e(TAG, text);
		try {
			Class<?> clazz = Class.forName(classPath);
			if (clazz != null) {

				// 打印类的自身属性，如修饰符。
				if(DEBUG) Log.w(TAG, "------ Class(类自身属性) start ------");
				if(DEBUG) Log.w(TAG, "类的绝对路径getCanonicalName:"+clazz.getCanonicalName());
				if(DEBUG) Log.w(TAG, "getName:"+clazz.getName());
				if(DEBUG) Log.w(TAG, "getModifiers:"+clazz.getModifiers());
				if(DEBUG) Log.w(TAG, "modifiers2String:"+modifiers2String( clazz.getModifiers() ));
				if(DEBUG) Log.w(TAG, "类名getSimpleName:"+clazz.getSimpleName());

				if(DEBUG) Log.w(TAG, "是否为注释类isAnnotation:"+clazz.isAnnotation());
				if(DEBUG) Log.w(TAG, "isAnonymousClass:"+clazz.isAnonymousClass());
				if(DEBUG) Log.w(TAG, "isArray:"+clazz.isArray());
				if(DEBUG) Log.w(TAG, "是否为枚举类isEnum:"+clazz.isEnum());
				if(DEBUG) Log.w(TAG, "是否为接口类isInterface:"+clazz.isInterface());
				if(DEBUG) Log.w(TAG, "isLocalClass:"+clazz.isLocalClass());
				if(DEBUG) Log.w(TAG, "isMemberClass:"+clazz.isMemberClass());
				if(DEBUG) Log.w(TAG, "isPrimitive:"+clazz.isPrimitive());
				if(DEBUG) Log.w(TAG, "isSynthetic:"+clazz.isSynthetic());

				Class<?> superclass = clazz.getSuperclass();
				if (superclass != null) {
					if(DEBUG) Log.w(TAG, "getSuperclass:"+superclass.getCanonicalName());			
				}

				Class<?> declaringClass = clazz.getDeclaringClass();
				if (declaringClass != null) {
					if(DEBUG) Log.w(TAG, "getDeclaringClass:"+clazz.getDeclaringClass().getCanonicalName());			
				}

				Class<?> enclosingClass = clazz.getEnclosingClass();
				if (enclosingClass != null) {
					if(DEBUG) Log.w(TAG, "getEnclosingClass:"+clazz.getEnclosingClass().getCanonicalName());
				}

				if(DEBUG) Log.w(TAG, "------ Class() end ------");

				if(DEBUG) Log.w(TAG, "------ getDeclaredClasses() start ------");
				Class<?>[] declaredClasses = clazz.getDeclaredClasses();
				int declaredClasses_N = 0;
				for (Class<?> a : declaredClasses) {
					if(DEBUG) Log.w(TAG, "DeclaredClasses["+declaredClasses_N+"]:"+a.toString());
					declaredClasses_N++;
				}
				if(DEBUG) Log.w(TAG, "------ DeclaredClasses() end ------");

				if(DEBUG) Log.w(TAG, "------ getInterfaces(要实现的接口) start ------");
				Class<?>[] interfaces = clazz.getInterfaces();
				int interfaces_N = 0;
				for (Class<?> a : interfaces) {
					if(DEBUG) Log.w(TAG, "Interfaces["+interfaces_N+"]:"+a.toString());
					interfaces_N++;
				}
				if(DEBUG) Log.w(TAG, "------ getInterfaces() end ------");

				// 打印注解
				if(DEBUG) Log.w(TAG, "------ getAnnotations(注解) start ------");
				java.lang.annotation.Annotation[] annotations = clazz.getAnnotations();
				int Annotation_N = 0;
				for (java.lang.annotation.Annotation a : annotations) {
					if(DEBUG) Log.w(TAG, "Annotation["+Annotation_N+"]:"+a.toString());
					Annotation_N++;
				}
				if(DEBUG) Log.w(TAG, "------ getAnnotations() end ------");

				if(DEBUG) Log.w(TAG, "------ getDeclaredAnnotations(注解) start ------");
				java.lang.annotation.Annotation[] declaredAnnotations = clazz.getDeclaredAnnotations();
				int a_n_2 = 0;
				for (java.lang.annotation.Annotation a : declaredAnnotations) {
					if(DEBUG) Log.w(TAG, "DeclaredAnnotation["+a_n_2+"]:"+a.toString());
					a_n_2++;
				}
				if(DEBUG) Log.w(TAG, "------ getDeclaredAnnotations() end ------");

				java.lang.reflect.Constructor<?>[] constructors = clazz.getConstructors();
				if (constructors != null) {
					if(DEBUG) Log.w(TAG, "------ getConstructors() start ------");
					if(DEBUG) Log.w(TAG, "getConstructors()数量:"+constructors.length);
					for (java.lang.reflect.Constructor<?> c : constructors) {
						if(DEBUG) Log.w(TAG, toConstructorString(c));
					}
					if(DEBUG) Log.w(TAG, "------ getConstructors() end ------");
				}
				java.lang.reflect.Constructor<?>[] constructors2 = clazz.getDeclaredConstructors();
				if (constructors2 != null) {
					if(DEBUG) Log.w(TAG, "------ getDeclaredConstructors() start ------");
					if(DEBUG) Log.w(TAG, "getDeclaredConstructors()数量:"+constructors2.length);
					for (java.lang.reflect.Constructor<?> c : constructors2) {
						if(DEBUG) Log.w(TAG, toConstructorString(c));
					}
					if(DEBUG) Log.w(TAG, "------ getDeclaredConstructors() end ------");
				}
				java.lang.reflect.Method[] methods = clazz.getMethods();
				if (methods != null) {
					if(DEBUG) Log.w(TAG, "------ getMethods(所有共有方法public) start ------");
					if(DEBUG) Log.w(TAG, "getMethods()数量:"+methods.length);
					for (java.lang.reflect.Method m : methods) {
						if(DEBUG) Log.w(TAG, toMethodString(m));
					}
					if(DEBUG) Log.w(TAG, "------ getMethods(所有共有方法public) end ------");
				}
				java.lang.reflect.Method[] methods2 = clazz.getDeclaredMethods();
				if (methods2 != null) {
					if(DEBUG) Log.w(TAG, "------ getDeclaredMethods(自身所有方法，public、protected、private) start ------");
					if(DEBUG) Log.w(TAG, "getDeclaredMethods()数量:"+methods2.length);
					for (java.lang.reflect.Method m : methods2) {
						if(DEBUG) Log.w(TAG, toMethodString(m));
					}
					if(DEBUG) Log.w(TAG, "------ getDeclaredMethods(自身所有方法，public、protected、private) end ------");
				}
				java.lang.reflect.Field[] fields = clazz.getFields();
				if (fields != null) {
					if(DEBUG) Log.w(TAG, "------ getFields() start ------");
					if(DEBUG) Log.w(TAG, "getFields()数量:"+fields.length);
					for (java.lang.reflect.Field f : fields) {
						if(DEBUG) Log.w(TAG, toFieldString(f));
					}
					if(DEBUG) Log.w(TAG, "------ getFields() end ------");
				}
				java.lang.reflect.Field[] fields2 = clazz.getDeclaredFields();
				if (fields2 != null) {
					if(DEBUG) Log.w(TAG, "------ getDeclaredFields() start ------");
					if(DEBUG) Log.w(TAG, "getDeclaredFields()数量:"+fields2.length);
					for (java.lang.reflect.Field f : fields2) {
						if(DEBUG) Log.w(TAG, toFieldString(f));
					}
					if(DEBUG) Log.w(TAG, "------ getDeclaredFields() end ------");
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		text = String.format("------ printClass( %s ) end ------", classPath);
		if(DEBUG) Log.e(TAG, text);
	}

	/**
	 * 打印类信息
	 * 
	 * @param classPath
	 */
	public static void printClass(Class<?> clazz) {
		if (clazz == null) {
			return;
		}
		printClass( clazz.getCanonicalName() );
	}

	// TODO
	public static void printField(java.lang.reflect.Field field) {


	}

	// TODO
	public static void printMethod(java.lang.reflect.Method method) {


	}

}
