package com.droidsimple.lang.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.droidsimple.util.Log;

/**
 * 反射类实现类
 * 
 * 功能：
 * 1.构造函数支持三种：String Object Class<?> 类型
 * 2.支持序列化java.io.Serializable、android.os.Parcelable，即过滤掉无用的属性和方法
 * 3.支持普通类、注释类、接口类、枚举类、抽象类、内部类。
 * 
 * @author simplesome
 *
 */
public class ReflectClass implements IReflectClass {

	private static final String TAG = ReflectClass.class.getSimpleName();
	private static boolean DEBUG = false;

	// 自身类
	private Class<?> clazz = null;

	// 是否存在
	private boolean isExists = false;

	// 父类
	private Class<?> superClass = null;

	// 名称
	private String name = null;
	private String simpleName = null;
	private String canonicalName = null;

	// 修饰符
	private int modifier = Modifier.PUBLIC;

	private boolean isAnnotation = false;
	private boolean isAnonymousClass = false;
	private boolean isArray = false;
	private boolean isEnum = false;
	private boolean isInterface = false;
	private boolean isLocalClass = false;
	private boolean isMemberClass = false;
	private boolean isPrimitive = false;
	private boolean isSynthetic = false;

	private List< ReflectField > fields = new ArrayList< ReflectField >();

	private List< ReflectMethod > methods = new ArrayList< ReflectMethod >();

	// 全部构造方法(分解)
	private List< ReflectConstructor > constructors = new ArrayList< ReflectConstructor >();

	// 全部接口(不分解)
	private Class<?>[] interfaces = null;

	// 全部注解(不分解)
	private Annotation[] annotations = null;

	// 是否实现序列化android.os.Parcelable
	//	private boolean implParcelable = false;

	// 是否实现序列化java.io.Serializable
	//	private boolean implSerializable = false;

	// 枚举模式
	private ReflectMode reflectMode = ReflectMode.Self_Exclude_Extends;

	// 表示要解析的是clazz还是object --- 仅用在ReflectField
	@Deprecated
	private boolean isEntity = false;
	@Deprecated
	private Object entity = null;

	/**
	 * 设置调试日志开关
	 * 
	 * @param debug
	 */
	public static void setDebugEnable(boolean debug) {
		DEBUG = debug;
	}

	public ReflectClass(Class<?> clazz) {
		this( clazz, ReflectMode.Self_Exclude_Extends );
	}

	public ReflectClass(Object entity) {
		this( entity, ReflectMode.Self_Exclude_Extends );
	}

	public ReflectClass(String classPath) {
		this( classPath, ReflectMode.Self_Exclude_Extends );
	}

	public ReflectClass(Class<?> clazz, ReflectMode mode) {
		init( clazz, mode );
	}

	public ReflectClass(Object entity, ReflectMode mode) {
		this.entity = entity;
		if (this.entity != null) {
			this.isEntity = true;
		}
		init( entity.getClass(), mode );
	}

	/**
	 * 如果是内部类，一定要用$变成
	 * com.lang.reflect.test.ReflectBeanA$ReflectMemberBeanB
	 * 否则找不着此类。
	 * 
	 * @param classPath
	 * @param mode
	 */
	public ReflectClass(String classPath, ReflectMode mode) {
		Class<?> c = null;
		try {
			c = Class.forName(classPath);
			init( c, mode );
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 初始化
	 * 
	 * @param classPath
	 */
	private void init(Class<?> clazz, ReflectMode mode) {

		if (clazz == null) {
			this.isExists = false;
			return;
		}

		if (mode == null) {
			this.reflectMode = ReflectMode.Self_Exclude_Extends;
		}

		this.clazz = clazz;
		this.reflectMode = mode;
		this.isExists = true;

		try {

			// 名称
			this.name = clazz.getName();
			this.simpleName = clazz.getSimpleName();
			this.canonicalName = clazz.getCanonicalName();

			// 修饰符
			this.modifier = clazz.getModifiers();

			// 父类
			this.superClass = clazz.getSuperclass();

			this.isAnnotation = this.clazz.isAnnotation();
			this.isAnonymousClass = this.clazz.isAnonymousClass();
			this.isArray = this.clazz.isArray();
			this.isEnum = this.clazz.isEnum();
			this.isInterface = this.clazz.isInterface();
			this.isLocalClass = this.clazz.isLocalClass();
			this.isMemberClass = this.clazz.isMemberClass();
			this.isPrimitive = this.clazz.isPrimitive();
			this.isSynthetic = this.clazz.isSynthetic();

			// +++ 分解注解
			if (this.reflectMode == ReflectMode.Self_Exclude_Extends) {
				this.annotations = this.clazz.getDeclaredAnnotations(); // 自身注解
			}else if (this.reflectMode == ReflectMode.Public_And_Extends || this.reflectMode == ReflectMode.Public_And_Extends_Exclude_Object) {
				this.annotations = this.clazz.getAnnotations(); // 共有注解
			}

			// +++ 分解接口
			this.interfaces = this.clazz.getInterfaces(); // 接口没有getDeclaredInterfaces()方法

			//				if (__interfaces != null) {
			//					for (Class<?> cls : __interfaces) {
			//						if (cls != null) {
			//							// 判断是否实现 了序列化
			//							if (cls.equals( android.os.Parcelable.class )) {
			////								this.implParcelable = true;
			//							} else if (cls.equals( java.io.Serializable.class )) {
			////								this.implSerializable = true;
			//							}
			//							getInterfaces().add( cls );
			//						}
			//					}
			//				}

			// +++ 分解构造
			Constructor<?>[] __constructors = null;
			if (this.reflectMode == ReflectMode.Self_Exclude_Extends) {
				__constructors = this.clazz.getDeclaredConstructors(); // 自身注解
			}else if (this.reflectMode == ReflectMode.Public_And_Extends || this.reflectMode == ReflectMode.Public_And_Extends_Exclude_Object) {
				__constructors = this.clazz.getConstructors(); // 共有注解
			}

			if (__constructors != null) {
				for (Constructor<?> __c : __constructors) {
					if (__c != null) {
						ReflectConstructor reflectConstructor = new ReflectConstructor(__c, this.reflectMode);
						getConstructors().add(reflectConstructor);
					}
				}
			}

			// +++ 分解属性
			Field[] __fields = null;
			if (this.reflectMode == ReflectMode.Self_Exclude_Extends) {
				__fields = this.clazz.getDeclaredFields(); // 自身注解
			}else if (this.reflectMode == ReflectMode.Public_And_Extends || this.reflectMode == ReflectMode.Public_And_Extends_Exclude_Object) {
				__fields = this.clazz.getFields(); // 共有注解
			}

			//				try {
			//					DebugUtil.startTime("class getFields AAA");
			//					Field ffff = this.clazz.getField("aaa");// 0.02,因map.get用时0.011ms，所以不必缓存
			//					DebugUtil.endTime();
			//					
			//					DebugUtil.startTime("class setAccessible AAA");
			//					ffff.setAccessible(true); // 0.002
			//					DebugUtil.endTime();
			//					
			//					DebugUtil.startTime("class setInt AAA");
			//					ffff.setInt(objectClass, 34); // 0.03
			//					DebugUtil.endTime();
			//					
			//					DebugUtil.startTime("class getInt AAA");
			//					int ccc = ffff.getInt(objectClass); // 0.01
			//					DebugUtil.endTime();
			//				} catch (NoSuchFieldException e) {
			//					e.printStackTrace();
			//				} catch (IllegalAccessException e) {
			//					e.printStackTrace();
			//				}

			if (__fields != null) {
				for (Field __f : __fields) {
					if (__f != null) {
						// 把不要的属性过滤掉
						if (ReflectClassExclude.isExcludeField(__f.getName()) == false) {
							ReflectField reflectField = null;
							if (isEntity && false) {
								// 是对象
								//								reflectField = new ReflectField(__f, this.reflectMode, this.entity);
							} else {
								// 是clazz
								reflectField = new ReflectField(__f, this.reflectMode);
							}
							// add
							getFields().add(reflectField);
						}
					}
				}
			}

			// +++ 分解方法
			Method[] __methods = null;
			if (this.reflectMode == ReflectMode.Self_Exclude_Extends) {
				__methods = this.clazz.getDeclaredMethods(); // 自身注解
			}else if (this.reflectMode == ReflectMode.Public_And_Extends || this.reflectMode == ReflectMode.Public_And_Extends_Exclude_Object) {
				__methods = this.clazz.getMethods(); // 共有注解
			}

			if (__methods != null) {
				for (Method __m : __methods) {
					if (__m != null) {
						// 把不要的方法过滤掉
						if (ReflectClassExclude.isExcludeMethod(__m.getName()) == false) {
							// 不要Object的Public方法
							if (this.reflectMode == ReflectMode.Public_And_Extends_Exclude_Object) {
								if (ReflectClassExclude.isExcludeObjectMethod(__m.getName()) == false) {
									ReflectMethod reflectMethod = new ReflectMethod(__m, this.reflectMode);
									getMethods().add(reflectMethod);
								}
							} else {
								ReflectMethod reflectMethod = new ReflectMethod(__m, this.reflectMode);
								getMethods().add(reflectMethod);
							}
						}
					}
				}
			}

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Class<?> getClazz() {
		return this.clazz;
	}

	@Override
	public int getModifier() {
		return this.modifier;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getSimpleName() {
		return this.simpleName;
	}

	/*
	 * 如果类是内部类，得到名称不包含$，而是.
	 */
	@Override
	public String getCanonicalName() {
		return this.canonicalName;
	}

	@Override
	public List<ReflectConstructor> getConstructors() {
		return this.constructors;
	}

	@Override
	public List<ReflectField> getFields() {
		return this.fields;
	}

	@Override
	public List<ReflectMethod> getMethods() {
		return this.methods;
	}

	@Override
	public Annotation[] getAnnotations() {
		return this.annotations;
	}

	@Override
	public Class<?> getSuperClass() {
		return this.superClass;
	}

	@Override
	public Class<?>[] getInterfaces() {
		return this.interfaces;
	}

	@Override
	public Field getField(String name) {
		try {
			if (this.reflectMode == ReflectMode.Self_Exclude_Extends) {
				return this.clazz.getDeclaredField(name); // 自身注解
			}else if (this.reflectMode == ReflectMode.Public_And_Extends || this.reflectMode == ReflectMode.Public_And_Extends_Exclude_Object) {
				return this.clazz.getField(name); // 共有注解
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Method getMethod(String name, Class<?>[] methodParame) {
		try {
			if (this.reflectMode == ReflectMode.Self_Exclude_Extends) {
				return this.clazz.getDeclaredMethod(name, methodParame); // 自身注解
			}else if (this.reflectMode == ReflectMode.Public_And_Extends || this.reflectMode == ReflectMode.Public_And_Extends_Exclude_Object) {
				return this.clazz.getMethod(name, methodParame); // 共有注解
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ReflectMode getReflectMode() {
		return this.reflectMode;
	}

	@Override
	public boolean isExists() {
		return this.isExists;
	}

	/**
	 * 如果此 Class 对象表示一个注释类型则返回 true。
	 * 此类是否为注释类
	 * 
	 * @return
	 */
	@Override
	public boolean isAnnotation() {
		return this.isAnnotation;
	}

	/*
	 * 判断此注释是否存在（已验证）
	 */
	@Override
	public boolean hasAnnotation(Class<? extends Annotation> annotationClass) {
		return this.clazz.isAnnotationPresent(annotationClass);
	}

	@Override
	public boolean isAnonymousClass() {
		return this.isAnonymousClass;
	}

	/**
	 * 判定此 Class 对象是否表示一个数组类。
	 * 
	 * @return
	 */
	@Override
	public boolean isArray() {
		return this.isArray;
	}

	@Override
	public boolean isEnum() {
		return this.isEnum;
	}

	@Override
	public boolean isInterface() {
		return this.isInterface;
	}

	/*
	 * 判断此接口是否存在（已验证）
	 */
	@Override
	public boolean hasInterface(Class<?> interfaceClass) {
		if (interfaceClass == null) {
			return false;
		}

		if (this.interfaces == null) {
			return false;
		}

		for (Class<?> i : this.interfaces) {
			if (i.equals(interfaceClass)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean isLocalClass() {
		return this.isLocalClass;
	}

	@Override
	public boolean isMemberClass() {
		return this.isMemberClass;
	}

	/**
	 * 判定指定的 Class 对象是否表示一个基本类型。
	 * 
	 * @return
	 */
	@Override
	public boolean isPrimitive() {
		return this.isPrimitive;
	}

	/**
	 * 如果此类是复合类，则返回 true，否则 false。
	 * 
	 * @return
	 */
	@Override
	public boolean isSynthetic() {
		return this.isSynthetic;
	}

	/**
	 * 比较相等
	 * 
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj) {
		return this.clazz.equals(obj);
	}

	/**
	 * 得到hash值
	 * 
	 * @return
	 */
	@Override
	public int hashCode() {
		return this.clazz.hashCode();
	}

	/**
	 * 打印
	 */
	@Override
	public void print() {

		if(DEBUG) Log.w(TAG, "------ ReflectClass print() start ------");

		if (this.getClazz() != null) {

			if(DEBUG) Log.w(TAG, "this.getName():"+this.getName());
			if(DEBUG) Log.w(TAG, "this.getSimpleName():"+this.getSimpleName());
			if(DEBUG) Log.w(TAG, "this.getCanonicalName():"+this.getCanonicalName());
			if(DEBUG) Log.w(TAG, "this.toString():"+this.clazz.toString());

			if(DEBUG) Log.w(TAG, "this.getClazz().desiredAssertionStatus():"+this.getClazz().desiredAssertionStatus());

			if (this.getSuperClass() != null) {
				if(DEBUG) Log.w(TAG, "this.getSuperClass():"+this.getSuperClass().getName());
			}

			Class<?>[] classes = this.getClazz().getClasses();
			if (classes != null) {
				if(DEBUG) Log.w(TAG, "getClasses() size:"+classes.length);
				for (Class<?> c : classes) {
					if(DEBUG) Log.w(TAG, "getClasses():"+c.getName());
				}
			}else{
				if(DEBUG) Log.w(TAG, "getClasses: == null");
			}

			Class<?>[] declaredClasses = this.getClazz().getDeclaredClasses();
			if (declaredClasses != null) {
				if(DEBUG) Log.w(TAG, "getDeclaredClasses() size:"+declaredClasses.length);
				for (Class<?> c : declaredClasses) {
					if(DEBUG) Log.w(TAG, "getDeclaredClasses():"+c.getName());
				}
			}else{
				if(DEBUG) Log.w(TAG, "getDeclaredClasses: == null");
			}

			if (this.getClazz().getComponentType() != null) {
				if(DEBUG) Log.w(TAG, "getComponentType:"+this.getClazz().getComponentType().getName());
			}else{
				if(DEBUG) Log.w(TAG, "getComponentType: == null");
			}

			if (this.getClazz().getDeclaringClass() != null) {
				if(DEBUG) Log.w(TAG, "getDeclaringClass:"+this.getClazz().getDeclaringClass().getName());
			}else{
				if(DEBUG) Log.w(TAG, "getDeclaringClass: == null");
			}

			if (this.getClazz().getEnclosingClass() != null) {
				if(DEBUG) Log.w(TAG, "getEnclosingClass:"+this.getClazz().getEnclosingClass().getName());
			}else{
				if(DEBUG) Log.w(TAG, "getEnclosingClass: == null");
			}

			// 获得泛型接口
			Type[] genericInterfaces = this.getClazz().getGenericInterfaces();
			if (genericInterfaces != null) {
				if(DEBUG) Log.w(TAG, "getGenericInterfaces() size:"+genericInterfaces.length);
				for (Type t : genericInterfaces) {
					if(DEBUG) Log.w(TAG, "getGenericInterfaces():"+t.toString());
				}
			}else{
				if(DEBUG) Log.w(TAG, "getGenericInterfaces: == null");
			}

			// 得到通用的父类
			if (this.getClazz().getGenericSuperclass() != null) {
				if(DEBUG) Log.w(TAG, "getGenericSuperclass:"+this.getClazz().getGenericSuperclass().toString());
			}else{
				if(DEBUG) Log.w(TAG, "getGenericSuperclass: == null");
			}

		}

		if(DEBUG) Log.w(TAG, "getModifier:"+getModifier());
		if(DEBUG) Log.w(TAG, "Modifier.toString:"+Modifier.toString( getModifier() ));

		// TODO 考虑注释使用class包装 ReflectAnnotation
		if (getAnnotations() != null) {
			if(DEBUG) Log.w(TAG, "getAnnotations size:"+getAnnotations().length);
			for (Annotation a : getAnnotations()) {
				if(DEBUG) Log.w(TAG, "getAnnotations():annotationType():"+a.annotationType().getName());
				if(DEBUG) Log.w(TAG, "getAnnotations():getClass()"+a.getClass().getName());
				if(DEBUG) Log.w(TAG, "getAnnotations():toString()"+a.toString());
			}
		}

		// TODO 考虑注释使用class包装 ReflectInterface
		if (getInterfaces() != null) {
			if(DEBUG) Log.w(TAG, "getInterfaces size:"+getInterfaces().length);
			for (Class<?> c : getInterfaces()) {
				if(DEBUG) Log.w(TAG, "getInterfaces():"+c.getName());
			}
		}

		if(DEBUG) Log.w(TAG, "isExists:"+isExists());

		if(DEBUG) Log.w(TAG, "isAnnotation:"+isAnnotation());
		//		if(DEBUG) Log.w(TAG, "hasAnnotation(class):"+hasAnnotation(TestReflectAnnotation.class));
		//		if(DEBUG) Log.w(TAG, "hasAnnotation(class):"+hasAnnotation(ReflectClass.class));

		if(DEBUG) Log.w(TAG, "isAnonymousClass:"+isAnonymousClass());
		if(DEBUG) Log.w(TAG, "isArray:"+isArray());
		if(DEBUG) Log.w(TAG, "isEnum:"+isEnum());

		if(DEBUG) Log.w(TAG, "isInterface:"+isInterface());
		if(DEBUG) Log.w(TAG, "hasInterface(Cloneable.class):"+hasInterface(Cloneable.class));
		if(DEBUG) Log.w(TAG, "hasInterface(List.Class):"+hasInterface(List.class));

		if(DEBUG) Log.w(TAG, "isLocalClass:"+isLocalClass());
		if(DEBUG) Log.w(TAG, "isMemberClass:"+isMemberClass());
		if(DEBUG) Log.w(TAG, "isPrimitive:"+isPrimitive());
		if(DEBUG) Log.w(TAG, "isSynthetic:"+isSynthetic());

		if (this.reflectMode != null) {
			if(DEBUG) Log.w(TAG, "mode:"+this.reflectMode.name());
		}

		if (this.getConstructors() != null) {
			if(DEBUG) Log.w(TAG, "getConstructors size:"+this.getConstructors().size());
			for (ReflectConstructor c : this.getConstructors()) {
				c.print();
			}
		}

		if (this.getFields() != null) {
			if(DEBUG) Log.w(TAG, "getFields size:"+this.getFields().size());
			for (ReflectField f : this.getFields()) {
				f.print();
			}
		}

		if (this.getMethods() != null) {
			if(DEBUG) Log.w(TAG, "getMethods size:"+this.getMethods().size());
			for (ReflectMethod m : this.getMethods()) {
				m.print();
			}
		}

		if(DEBUG) Log.w(TAG, "------ ReflectClass print() end ------");
	}

}
