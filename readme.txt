
名称：
反射框架。

作者：
simplesome

github地址：
https://github.com/simplesome/ReflectFrame.git

使用教程：
无。

功能：
提高反射效率。

原理：
采用HashMap在内存中缓存。

使用场景：
1) 完完整整的使用隐藏类。(如：android.preference.VolumePreference、android.os.ServiceManager)
2) 构建ORM框架，如：构建SQLite框架。
3) 开发环境向上兼容，如：android sdk 2.2开发环境使用2.3 API。
4) 提高反射效率。

优点：
1) 过滤java.io.Serializable、android.os.Parcelable、java.lang.Cloneable实现类中的属性和方法。
2) 三种方式Class<?>、Object、String创建ReflectClass。
3) 支持内部类。
4) 效率高。
5) 支持软引用机制，防止发生OOM。(后续版本支持)
6) 除了reflect_log.jar不依赖其它第三方库。
7) 可用于Android和J2SE开发环境。
8) 完完整整的使用隐藏类。(后续版本支持)

缺点:
1) 非线程安全。

第三方依赖库：
日志库reflect_log.jar(支持Android和J2SE两种开发环境)

类说明：
1) IReflectClass.java反射类接口类。
2) ReflectClassCache.java内存缓存类。 
3) ReflectClassExclude.java排除无用属性和无用方法。
4) ReflectClass.java反射类实现类。
5) ReflectConstructor.java反射构造函数类。
6) ReflectField.java反射属性类。
7) ReflectMethod.java反射方法类。
8) ReflectMode.java反射模式类。
9) AbsReflectClassFactory.java反射类工厂抽象类。
10) ReflectClassFactory.java反射类工厂实现类
11) ReflectTool.java反射工具类。

调用流程：
ReflectClassFactory -> ReflectClassCache -> ReflectClass

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
2) 改良ReflectTool.java
4) 加入软引用机制或者LruCache。
5) 完完整整的使用隐藏类。
7） 完善ReflectMode.java，还有两个参数不支持呢。