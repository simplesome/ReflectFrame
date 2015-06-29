# ReflectFrame


请简读：readme.txt


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
