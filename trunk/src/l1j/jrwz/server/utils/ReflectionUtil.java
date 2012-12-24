/**
 *                            License
 * THE WORK (AS DEFINED BELOW) IS PROVIDED UNDER THE TERMS OF THIS  
 * CREATIVE COMMONS PUBLIC LICENSE ("CCPL" OR "LICENSE"). 
 * THE WORK IS PROTECTED BY COPYRIGHT AND/OR OTHER APPLICABLE LAW.  
 * ANY USE OF THE WORK OTHER THAN AS AUTHORIZED UNDER THIS LICENSE OR  
 * COPYRIGHT LAW IS PROHIBITED.
 * 
 * BY EXERCISING ANY RIGHTS TO THE WORK PROVIDED HERE, YOU ACCEPT AND  
 * AGREE TO BE BOUND BY THE TERMS OF THIS LICENSE. TO THE EXTENT THIS LICENSE  
 * MAY BE CONSIDERED TO BE A CONTRACT,
 * THE LICENSOR GRANTS YOU THE RIGHTS CONTAINED 
 * HERE IN CONSIDERATION OF YOUR ACCEPTANCE OF SUCH TERMS AND CONDITIONS.
 * 
 */
package l1j.jrwz.server.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class <code>ReflectionUtil</code> 反射控制器.
 * 
 * @author jrwz
 * @version 2012-6-3上午01:16:05
 * @see l1j.jrwz.server.utils
 * @since JDK1.7
 */
public final class ReflectionUtil {

    /** 提示信息. */
    private static final Logger LOG = Logger.getLogger(ReflectionUtil.class.getName());

    /**
     * Class的名称.
     * 
     * @param className
     *            名称
     * @return 如果没有找到 null
     */
    public static Class<?> classForName(final String className) {
        try {
            return Class.forName(className);
        } catch (final ClassNotFoundException e) {
            logException(e);
        }
        return null;
    }

    /**
     * 取得构造.
     * 
     * @param cls
     *            Class
     * @param args
     *            参数
     * @return 没有则返回 null
     */
    public static Constructor<?> getConstructor(final Class<?> cls, final Class<?>... args) {
        try {
            return cls.getDeclaredConstructor(args);
        } catch (final SecurityException e) {
            logException(e);
        } catch (final NoSuchMethodException e) {
            logException(e);
        }
        return null;
    }

    /**
     * 异常提示信息.
     * 
     * @param e
     *            异常
     */
    private static void logException(final Exception e) {
        LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }

    /**
     * 新实例.
     * 
     * @param <T>
     *            <T>
     * @param cls
     *            Class
     * @return 新实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(final Class<?> cls) {
        final Constructor<?> con = getConstructor(cls, new Class<?>[0]);
        return (T) newInstance(con, new Object[0]);
    }

    /**
     * 新实例.
     * 
     * @param <T>
     *            <T>
     * @param con
     *            构造
     * @param args
     *            参数
     * @return 如果没有则返回 null
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(final Constructor<?> con, final Object... args) {
        try {
            con.setAccessible(true);
            return (T) con.newInstance(args);
        } catch (final IllegalArgumentException e) {
            logException(e);
        } catch (final InstantiationException e) {
            logException(e);
        } catch (final IllegalAccessException e) {
            logException(e);
        } catch (final InvocationTargetException e) {
            logException(e);
        }
        return null;
    }

    /**
     * 新实例.
     * 
     * @param <T>
     *            <T>
     * @param className
     *            名称
     * @return 新实例
     */
    public static <T> T newInstance(final String className) {
        final Class<?> cls = ReflectionUtil.classForName(className);
        return ReflectionUtil.<T> newInstance(cls);
    }

    /**
     * 新实例.
     * 
     * @param <T>
     *            <T>
     * @param <T2>
     *            <T2>
     * @param className
     *            名称
     * @param arg
     *            参数
     * @param argValue
     *            参数值
     * @return 新实例
     */
    public static <T, T2> T newInstance(final String className, final Class<T2> arg, final T2 argValue) {
        final Class<?> cls = ReflectionUtil.classForName(className);
        final Constructor<?> con = ReflectionUtil.getConstructor(cls, arg);
        return ReflectionUtil.<T> newInstance(con, argValue);
    }

    /**
     * 反射控制器.
     */
    private ReflectionUtil() {
    }

}
