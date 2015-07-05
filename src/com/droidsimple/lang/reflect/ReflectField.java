package com.droidsimple.lang.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.droidsimple.util.Log;


/**
 * 反射属性类
 * 
 * 属性有：枚举、对象、基本类型
 * 
 * @author simplesome
 *
 */
public class ReflectField {

	private static final String TAG = ReflectField.class.getSimpleName();
	private static boolean DEBUG = false;

	// 得到声明类<lass<?>，就是此方法所在的类。
	private Class<?> clazz = null;

	// 修饰符
	private int modifier = Modifier.PRIVATE;

	// 名称
	private String name = null;

	// 类型的Class<?>
	private Class<?> type = null;

	// 自身
	private Field field = null;

	// 枚举模式
	private ReflectMode reflectMode = ReflectMode.Self_Exclude_Extends;

	// 注释
	private Annotation[] annotations = null;

	// 表示要解析的是clazz还是object
	@Deprecated
	private boolean isEntity = false;
	@Deprecated
	private Object entity = null;

	//	private boolean isAccessible = false; // 因为会变化
	private boolean isEnumConstant = false;
	private boolean isSynthetic = false;

	/**
	 * 设置调试日志开关
	 * 
	 * @param debug
	 */
	public static void setDebugEnable(boolean debug) {
		DEBUG = debug;
	}

	public ReflectField(Field field, ReflectMode mode) {
		init(field, mode, null);
	}

	// TODO 未实现,另一种表示field的方式 .
	public ReflectField(Class<?> clazz, String fieldName) {

	}

	// TODO
	public ReflectField(Object entity, String fieldName) {

	}

	// TODO
	public ReflectField(String classPath, String fieldName) {

	}

	// TODO 不要传入实例，此框架用于获取Field、Method，所以不需要值。
	//	public ReflectField(Field field, ReflectMode mode, Object entity) {
	//		init(field, mode, entity);
	//	}

	/**
	 * 初始化
	 * 
	 * @param field
	 * @param entity
	 */
	private void init(Field field, ReflectMode mode, Object entity) {

		this.field = field;

		this.entity = entity;

		if (this.entity != null) {
			this.isEntity = true;
		}

		this.reflectMode = mode;

		// 得到声明类<lass<?>，就是此方法所在的类。
		this.clazz = this.field.getDeclaringClass();

		this.modifier = this.field.getModifiers();
		this.name = this.field.getName();

		//  返回一个 Type 对象，它表示此 Field 对象所表示字段的声明类型。
		//		this.type = this.field.getGenericType();
		// 返回一个 Class 对象，它标识了此 Field 对象所表示字段的声明类型。
		//		this.type = this.field.getType();

		this.type = this.field.getType();

		//		this.isAccessible = this.field.isAccessible();
		this.isEnumConstant = this.field.isEnumConstant();
		this.isSynthetic = this.field.isSynthetic();

		// +++ 分解注解
		if (this.reflectMode == ReflectMode.Self_Exclude_Extends) {
			this.annotations = this.field.getDeclaredAnnotations(); // 自身注解
		}else if (this.reflectMode == ReflectMode.Public_And_Extends || this.reflectMode == ReflectMode.Public_And_Extends_Exclude_Object) {
			this.annotations = this.field.getAnnotations(); // 共有注解
		}

	}

	/**
	 * 得到声明类<lass<?>，就是此属性所在的类。
	 * 
	 * @return
	 */
	public Class<?> getClazz() {
		return this.clazz;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * 得到值，这里要考虑是clazz还是object，
	 * 要考虑field是否为static或者final等等
	 * 
	 * @param field
	 * @param entity
	 * @return
	 */
	private Object getValue(Field field, Object entity) {

		Object value = null;
		boolean isEntity = false;

		if (entity != null) {
			isEntity = true;
		}

		if (field != null) {

			// 设置权限
			//			if ( isAccessible() == false ) {
			field.setAccessible(true);
			//			}

			try {

				// 如果是static属性，则可get(null)
				if ( Modifier.isStatic( getModifier() ) ) {
					value = field.get( null );
				} else if (isEntity) {
					// 不是static，并且为object传入
					value = field.get( entity );
				}

			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return value;
	}

	public Object getValue(Object entity) {
		return getValue(this.field, entity);
	}

	//	public Object getValue() {
	//		if (isEntity) {
	//			return getValue(this.field, this.entity);
	//		}else{
	//			return getValue(this.field, null);
	//		}
	//	}

	public Class<?> getType() {
		return this.type;
	}

	//	public String getTypeString() {
	//		return this.typeString;
	//	}

	public Field getField() {
		return this.field;
	}

	public Annotation[] getAnnotations() {
		return this.annotations;
	}

	public int getModifier() {
		return this.modifier;
	}

	public ReflectMode getReflectMode() {
		return this.reflectMode;
	}

	@Deprecated
	public boolean isEntity() {
		return this.isEntity;
	}

	@Deprecated
	public Object getEntity() {
		return this.entity;
	}

	/**
	 * 是否有注释 
	 * 
	 * @return
	 */
	public boolean hasAnnotation() {

		if (this.annotations == null) {
			return false;
		}

		if (this.annotations.length > 0) {
			return true;
		}

		return false;
	}

	/*
	 * 判断此注释是否存在（已验证）
	 */
	public boolean hasAnnotation(Class<? extends Annotation> annotationClass) {
		return this.field.isAnnotationPresent(annotationClass);
	}

	public boolean isAccessible() {
		return this.field.isAccessible();
	}

	public void setAccessible(boolean flag) {
		this.field.setAccessible(flag);
	}

	/**
	 * 如果此字段表示枚举类型的元素，则返回 true；否则返回 false。
	 * 
	 * @return
	 */
	public boolean isEnumConstant() {
		return this.isEnumConstant;
	}

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
		return this.field.equals(obj);
	}

	/**
	 * 得到hash值
	 * 
	 * @return
	 */
	@Override
	public int hashCode() {
		return this.field.hashCode();
	}

	/**
	 * 打印
	 */
	public void print() {
		if(DEBUG) Log.w(TAG, "------ ReflectField print() start ------");

		if(DEBUG) Log.w(TAG, "this.getName():"+this.getName());
		if(DEBUG) Log.w(TAG, "toGenericString:"+this.field.toGenericString());
		if(DEBUG) Log.w(TAG, "toString:"+this.field.toString());

		if(DEBUG) Log.w(TAG, "getModifier:"+getModifier());
		if(DEBUG) Log.w(TAG, "Modifier.toString:"+Modifier.toString( getModifier() ));

		if (this.getType() != null) {
			if(DEBUG) Log.w(TAG, "getType:"+this.getType().getName());
		}

		if (this.getField().getGenericType() != null) {
			if(DEBUG) Log.w(TAG, "getGenericType:"+this.getField().getGenericType().toString());
		}

		if (this.field.getDeclaringClass() != null) {
			if(DEBUG) Log.w(TAG, "getDeclaringClass:"+this.field.getDeclaringClass().getName());
		}

		if(DEBUG) Log.w(TAG, "isEntity:"+this.isEntity);

		if (getAnnotations() != null) {
			if(DEBUG) Log.w(TAG, "getAnnotations size:"+getAnnotations().length);
			for (Annotation a : getAnnotations()) {
				if(DEBUG) Log.w(TAG, "getAnnotations():annotationType():"+a.annotationType().getName());
				if(DEBUG) Log.w(TAG, "getAnnotations():getClass()"+a.getClass().getName());
				if(DEBUG) Log.w(TAG, "getAnnotations():toString()"+a.toString());
			}
		}

		if(DEBUG) Log.w(TAG, "isAccessible:"+isAccessible());
		if(DEBUG) Log.w(TAG, "isEnumConstant:"+isEnumConstant());
		if(DEBUG) Log.w(TAG, "isSynthetic:"+isSynthetic());

		if (this.reflectMode != null) {
			if(DEBUG) Log.w(TAG, "mode:"+this.reflectMode.name());
		}
		if(DEBUG) Log.w(TAG, "------ ReflectField print() end ------");
	}

}
