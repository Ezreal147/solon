package org.noear.solon.core;

import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Aop 管理中心
 *
 * 负责常用对外接口
 * */
public class Aop {
    //::工厂
    /**
     * Aop处理工厂
     */
    private static AopFactory _f = new AopFactory();

    /**
     * 获取Aop处理工厂
     */
    public static AopFactory factory() {
        return _f;
    }

    /**
     * 设置Aop处理工厂
     */
    public static void factorySet(AopFactory factory) {
        _f = factory;
    }

    //::包装bean

    /**
     * 包装bean（clz），不负责注册
     */
    public static BeanWrap wrap(Class<?> clz, Object bean) {
        BeanWrap wrap = _f.getWrap(clz);
        if (wrap == null) {
            wrap = new BeanWrap(clz, bean);
        }

        return wrap;
    }

    public static BeanWrap wrapAndPut(Class<?> clz){
       return wrapAndPut(clz, null);
    }

    public static BeanWrap wrapAndPut(Class<?> clz, Object bean){
        BeanWrap wrap = wrap(clz, bean);
        if (wrap.raw() != null) {
            _f.putWrap(clz, wrap);
        }

        return wrap;
    }

    //::获取bean

    /**
     * 获取bean (key)
     */
    public static <T> T get(String key) {
        BeanWrap bw = _f.getWrap(key);
        return bw == null ? null : bw.get();
    }

    /**
     * 获取bean (clz)
     */
    public static <T> T get(Class<?> clz) {
        return wrapAndPut(clz).get();
    }

    /**
     * 异步获取bean (key)
     */
    public static void getAsyn(String key, Consumer<BeanWrap> callback) {
        getAsynDo(key, callback);
    }

    /**
     * 异步获取bean (clz)
     */
    public static void getAsyn(Class<?> clz, Consumer<BeanWrap> callback) { //FieldWrapTmp fwT,
        getAsynDo(clz, callback);
    }

    private static void getAsynDo(Object key, Consumer<BeanWrap> callback) {
        BeanWrap wrap = _f.getWrap(key);

        if (wrap == null || wrap.raw() == null) {
            _f.beanSubscribe(key, callback);
        } else {
            callback.accept(wrap);
        }
    }

    /**
     * 尝试注入（建议使用：get(clz) ）
     */
    public static <T> T inject(T bean) {
        _f.inject(bean);
        return bean;
    }

    public static <T> T inject(T bean, Properties propS) {
        ClassWrap.get(bean.getClass()).fill(bean, propS::getProperty, null);
        return bean;
    }

    //::bean事件处理

    /**
     * 加载bean
     */
    public static void beanLoad(Class<?> source) {
        _f.beanLoad(source);
    }

    /**
     * 添加bean加载完成事件
     */
    public static void beanOnloaded(Runnable fun) {
        _f.loadedEvent.add(fun);
    }

    /**
     * 遍历bean (拿到的是bean包装)
     */
    public static void beanForeach(BiConsumer<String, BeanWrap> action) {
        _f.beans.forEach(action);

    }
}