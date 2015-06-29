# ReflectFrame

名称：
反射框架。

作者：
simplesome

功能：
提高反射效率。

原理：
采用HashMap在内存中缓存。

使用场景：
1) 获取隐藏类。
2) 构建ORM框架，如：构建SQLite框架。
3) 开发环境的向上兼容，如：android sdk 2.2开发环境使用2.3 API。

优点：
1) 支持java.io.Serializable、android.os.Parcelable序列化的类。
2) 三种方式Class<?>、Object、String创建ReflectClass。
3) 支持内部类。(后续版本支持)
4) 效率高。
5) 支持软引用机制，防止发生OOM。(后续版本支持)
6) 不依赖第三方库。

缺点:
1) 非线程安全。

类说明：
1) ReflectClass.java反射接口类。
2) ReflectClassCache.java内存缓存类。 
3) ReflectClassExclude.java排除无用属性和无用方法。
4) ReflectClassImpl.java实现ReflectClass.java接口类。
5) ReflectConstructor.java反射构造函数类。
6) ReflectField.java反射属性类。
7) ReflectMethod.java反射方法类。
8) ReflectMode.java反射模式类。
9) AbsReflectFactory.java反射工厂抽象类。
10) ReflectFactoryImpl.java反射工厂实现类AbsReflectFactory.java
11) ReflectFactory.java反射工厂创建类。
12) ReflectTool.java反射工具类。

调用流程：
ReflectFactory -> ReflectFactoryImpl -> ReflectClassCache -> ReflectClassImpl

三种方式创建ReflectClass：
1) Class<?>
ReflectClass c1 = ReflectFactory.createClass(android.view.View.class);
2) Object对象
android.view.View view = new android.view.View(context);
ReflectClass c2 = ReflectFactory.createClass(view);
3) String类的绝对路径
ReflectClass c3 = ReflectFactory.createClass("android.view.View");

提前缓存：
ReflectClassCache.put(android.view.View.class);
ReflectClassCache.put(new android.view.View(context));
ReflectClassCache.put("android.view.View");

将来要改良的特性：
1) 改良ReflectMethod.java
2) 改良ReflectTool.java
3) ReflectFactory.createClass("android.view.View$AAA$BBB")支持内部类。
4) 加入软引用机制或者LruCache。
