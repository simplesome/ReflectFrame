package com.droidsimple.lang.reflect;

import java.util.HashMap;
import java.util.Map;

import com.droidsimple.util.Log;


/**
 * 反射无用属性和无用方法排除类。
 * TODO 缺点：目前是按方法名进行过滤，建议也要包括方法的参数。
 * 
 * @author simplesome
 *
 */
public class ReflectClassExclude {

	private static final String TAG = ReflectClassExclude.class.getSimpleName();
	private static boolean DEBUG = false;

	// +++ 过滤序列化没有必要属性
	// public static final android.os.Parcelable.Creator CREATOR
	private static final String Exclude_Field_Parcelable_CREATOR = "CREATOR";
	// == ReflectMode.Public时会有
	// public static final int CONTENTS_FILE_DESCRIPTOR
	private static final String Exclude_Field_Parcelable_CONTENTS_FILE_DESCRIPTOR = "CONTENTS_FILE_DESCRIPTOR";
	// public static final int PARCELABLE_WRITE_RETURN_VALUE
	private static final String Exclude_Field_Parcelable_PARCELABLE_WRITE_RETURN_VALUE = "PARCELABLE_WRITE_RETURN_VALUE";

	// private static final long serialVersionUID = 42L;
	private static final String Exclude_Field_Serializable_serialVersionUID = "serialVersionUID";

	private static Map< String, String > exclede_field_parcelable_map = new HashMap< String, String >();
	private static Map< String, String > exclede_field_serializable_map = new HashMap< String, String >();

	// 全部需要过滤的属性
	private static Map< String, String > exclede_field_map = new HashMap< String, String >();

	// 全部方法(分解) 过滤掉序列化的方法
	// public int describeContents()
	private static final String Exclude_Method_Parcelable_describeContents = "describeContents";
	// public void writeToParcel(android.os.Parcel,int)
	private static final String Exclude_Method_Parcelable_writeToParcel = "writeToParcel";
	// static void access$0(com.droidsimple.lang.reflect.TestReflectBeanA,int)
	// static void access$1(com.droidsimple.lang.reflect.TestReflectBeanA,java.lang.String)
	private static final String Exclude_Method_access = "access$"; // 这个是比较前缀

	private static Map< String, String > exclede_method_parcelable_map = new HashMap< String, String>();

	private static final String Exclude_Method_Cloneable_clone = "clone";

	private static Map< String, String > exclede_method_Cloneable_map = new HashMap< String, String>();

	// 全部需要过滤的方法
	private static Map< String, String > exclede_method_map = new HashMap< String, String>();

	// java.lang.Object公有方法Public
	private static final String Exclude_Object_Method_equals = "equals";
	private static final String Exclude_Object_Method_getClass = "getClass";
	private static final String Exclude_Object_Method_hashCode = "hashCode";
	private static final String Exclude_Object_Method_notify = "notify";
	private static final String Exclude_Object_Method_notifyAll = "notifyAll";
	private static final String Exclude_Object_Method_toString = "toString";
	private static final String Exclude_Object_Method_wait = "wait";

	// java.lang.Object公有方法Public排除列表
	private static Map< String, String > exclede_object_method_map = new HashMap< String, String>();

	static {
		// 属性
		exclede_field_parcelable_map.put(Exclude_Field_Parcelable_CREATOR, Exclude_Field_Parcelable_CREATOR);
		exclede_field_parcelable_map.put(Exclude_Field_Parcelable_CONTENTS_FILE_DESCRIPTOR, Exclude_Field_Parcelable_CONTENTS_FILE_DESCRIPTOR);
		exclede_field_parcelable_map.put(Exclude_Field_Parcelable_PARCELABLE_WRITE_RETURN_VALUE, Exclude_Field_Parcelable_PARCELABLE_WRITE_RETURN_VALUE);

		exclede_field_serializable_map.put(Exclude_Field_Serializable_serialVersionUID, Exclude_Field_Serializable_serialVersionUID);

		exclede_field_map.putAll(exclede_field_parcelable_map);
		exclede_field_map.putAll(exclede_field_serializable_map);

		// 方法
		exclede_method_parcelable_map.put(Exclude_Method_Parcelable_describeContents, Exclude_Method_Parcelable_describeContents);
		exclede_method_parcelable_map.put(Exclude_Method_Parcelable_writeToParcel, Exclude_Method_Parcelable_writeToParcel);

		exclede_method_Cloneable_map.put(Exclude_Method_Cloneable_clone, Exclude_Method_Cloneable_clone);

		exclede_method_map.putAll(exclede_method_parcelable_map);
		exclede_method_map.putAll(exclede_method_Cloneable_map);

		// Object方法
		exclede_object_method_map.put(Exclude_Object_Method_equals, Exclude_Object_Method_equals);
		exclede_object_method_map.put(Exclude_Object_Method_getClass, Exclude_Object_Method_getClass);
		exclede_object_method_map.put(Exclude_Object_Method_hashCode, Exclude_Object_Method_hashCode);
		exclede_object_method_map.put(Exclude_Object_Method_notify, Exclude_Object_Method_notify);
		exclede_object_method_map.put(Exclude_Object_Method_notifyAll, Exclude_Object_Method_notifyAll);
		exclede_object_method_map.put(Exclude_Object_Method_toString, Exclude_Object_Method_toString);
		exclede_object_method_map.put(Exclude_Object_Method_wait, Exclude_Object_Method_wait);
		if (DEBUG) Log.w(TAG, "exclede_object_method_map size:"+exclede_object_method_map.size());
	}

	/**
	 * 设置调试日志开关
	 * 
	 * @param debug
	 */
	public static void setDebugEnable(boolean debug) {
		DEBUG = debug;
	}

	/**
	 * 是否为排除的属性
	 * 
	 * @return
	 */
	public static boolean isExcludeField(String field) {
		boolean isExcludeField = false;
		if (field != null) {
			if (exclede_field_map.get(field) != null) {
				isExcludeField = true;
			}
		}
		if (DEBUG) Log.w(TAG, "isExcludeField:"+isExcludeField+",field:"+field);
		return isExcludeField;
	}

	/**
	 * 添加要过滤的field
	 * 
	 * @param exculdeField
	 */
	public static void addExcludeField(String exculdeField) {
		exclede_field_map.put(exculdeField, exculdeField);
	}

	/**
	 * 得到过滤的field map
	 * 
	 * @return
	 */
	public static Map<String, String> getExcludeField() {
		return exclede_field_map;
	}

	/**
	 * 是否为排除的方法
	 * 
	 * @return
	 */
	public static boolean isExcludeMethod(String method) {
		boolean isExcludeMethod = false;
		if (method != null) {
			if (exclede_method_map.get(method) != null) {
				isExcludeMethod = true;
			}else{
				if (method.startsWith(Exclude_Method_access)) { // 判断前缀字符串
					// 排除Method_Exclude_access方法
					isExcludeMethod = true;
				}
			}
		}
		if (DEBUG) Log.w(TAG, "isExcludeMethod:"+isExcludeMethod+",method:"+method);
		return isExcludeMethod;
	}

	/**
	 * 添加要过滤的method
	 * 
	 * @param exculdeMethod
	 */
	public static void addExcludeMethod(String exculdeMethod) {
		exclede_method_map.put(exculdeMethod, exculdeMethod);
	}

	/**
	 * 得到过滤的method map
	 * 
	 * @return
	 */
	public static Map<String, String> getExcludeMethod() {
		return exclede_method_map;
	}

	/**
	 * 判断是否为排除Object中的方法
	 * 
	 * @param method
	 * @return
	 */
	public static boolean isExcludeObjectMethod(String method) {
		boolean isExcludeObjectMethod = false;
		if (method != null) {
			if (exclede_object_method_map.get(method) != null) {
				isExcludeObjectMethod = true;
			}
		}
		if (DEBUG) Log.w(TAG, "isExcludeObjectMethod:"+isExcludeObjectMethod+",method:"+method);
		return isExcludeObjectMethod;
	}

}
