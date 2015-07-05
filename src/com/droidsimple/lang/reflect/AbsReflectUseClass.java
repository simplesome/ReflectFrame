package com.droidsimple.lang.reflect;

public abstract class AbsReflectUseClass {

	private Object thisObject = null;

	protected void newReflectConstructor(Object... args) {
		//		Constructor<?> c = ReflectTool.getConstructor("android.preference.VolumePreference", Context.class, AttributeSet.class);
		//		obj = ReflectTool.createObject(c, context, attrs);
	}

	protected Object invokeReflectMethod(String methodName, Object... args) {
		//		IReflectClass clazz = ReflectClassStaticFactory.createClass("android.preference.VolumePreference");
		//		Object o = ReflectTool.invokeMethod(obj, null, streamType);
		return null;
	}

	protected static Object invokeReflectStaticMethod(String methodName, Object... args) {
		//		ReflectTool.invokeMethod(obj, null, );
		return null;
	}

	protected Object getReflectThis() {
		return this.thisObject;
	}

	public void setReflectThis(Object object) {
		this.thisObject = object;
	}

	public Object getReflectValue(String fieldName) {
		return null;
	}

}
