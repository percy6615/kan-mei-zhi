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
package l1j.jrwz.configure;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.regex.Pattern;

import l1j.jrwz.configure.Annotations.Configures;
import l1j.jrwz.configure.CustomLoaders.CustomLoader;
import l1j.jrwz.server.utils.ReflectionUtil;
import l1j.jrwz.server.utils.collections.Maps;

/**
 * Class <code>ConfigureLoader</code> 加载服务器配置.
 * 
 * @author jrwz
 * @version 2012-6-3上午01:14:30
 * @see l1j.jrwz.configure
 * @since JDK1.7
 */
public final class ConfigureLoader {

    /** 属性文件集合. */
    private final Map<File, L1Properties> propsMap = Maps.newHashMap();

    /**
     * 取得属性文件.
     * 
     * @param path
     *            文件路径名
     * @throws IOException
     *             当发生某种 I/O 异常时，抛出此异常。此类为异常的通用类，它是由失败的或中断的 I/O 操作生成的。
     * @return 文件路径名
     */
    private L1Properties getProps(final String path) throws IOException {
        final File propsFile = new File(path);
        L1Properties result = this.propsMap.get(propsFile);
        if (result == null) {
            result = this.loadProps(propsFile);
        }
        return result;
    }

    /**
     * 加载.
     * 
     * @param configClass
     *            配置Class
     */
    public void load(final Class<?> configClass) {
        try {
            for (final Field f : configClass.getFields()) {
                final Configures config = f.getAnnotation(Configures.class);
                if (config == null) {
                    continue; // 没有对应的注解
                }
                this.loadValue(f, config);
            }
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载属性文件.
     * 
     * @param propsFile
     *            属性文件
     * @throws IOException
     *             当发生某种 I/O 异常时，抛出此异常。此类为异常的通用类，它是由失败的或中断的 I/O 操作生成的。
     * @return 集合内的属性文件
     */
    private L1Properties loadProps(final File propsFile) throws IOException {
        final L1Properties result = L1Properties.load(propsFile);
        this.propsMap.put(propsFile, result);
        return result;
    }

    /**
     * 加载值.
     * 
     * @param f
     *            反射的字段
     * @param config
     *            配置
     * @throws IOException
     *             当发生某种 I/O 异常时，抛出此异常。此类为异常的通用类，它是由失败的或中断的 I/O 操作生成的。
     * @throws IllegalAccessException
     *             当应用程序试图创建一个实例（而不是数组）、设置或获取一个字段，或者调用一个方法，
     *             但当前正在执行的方法无法访问指定类、字段、方法或构造方法的定义时， 抛出 IllegalAccessException。
     *             (构造不带详细消息的 IllegalAccessException 。)
     */
    private void loadValue(final Field f, final Configures config) throws IOException, IllegalAccessException {
        final L1Properties props = this.getProps(config.file());
        if (props.isNull() && !config.isOptional()) { // 有错误则不加载
            throw new IOException(props.getException());
        }
        // XXX 此处待修改
        final String key = config.key().isEmpty() ? this.makeDefaultKey(f.getName()) : config.key();
        final String value = props.getProperty(key, config.allowDefaultValue());
        if (value == null) { // 保持默认值
            return;
        }
        final CustomLoader loader = ReflectionUtil.newInstance(config.loader());

        f.set(null, loader.loadValue(config, f.getType(), value));
    }

    /**
     * 创造默认密钥.
     * 
     * @param fieldName
     *            字段名
     * @return 字符序列组成的字符串
     */
    String makeDefaultKey(final String fieldName) {
        if (!Pattern.matches("^[A-Z0-9]+(_[A-Z0-9]+)*$", fieldName)) {
            throw new IllegalArgumentException(fieldName);
        }
        final StringBuilder result = new StringBuilder();
        for (final String seq : fieldName.split("_")) {
            result.append(seq.charAt(0));
            result.append(seq.substring(1).toLowerCase());
        }
        return result.toString();
    }

}
