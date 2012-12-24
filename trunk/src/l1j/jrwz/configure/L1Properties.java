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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Class <code>L1Properties</code> 属性文件.
 * 
 * @author jrwz
 * @version 2012-6-3上午01:02:23
 * @see l1j.jrwz.configure
 * @since JDK1.7
 */
public class L1Properties {

    /**
     * 空属性文件.
     */
    private static class NullProperties extends L1Properties {
        /** IO异常. */
        private final IOException _e;

        /**
         * 空属性文件.
         * 
         * @param e
         *            IO异常
         */
        public NullProperties(final IOException e) {
            this._e = e;
        }

        @Override
        public IOException getException() {
            return this._e;
        }

        @Override
        public String getProperty(final String key, final boolean allowDefaultValue) {
            return null;
        }

        @Override
        public boolean isNull() {
            return true;
        }
    }

    /** 提示信息. */
    private static final Logger LOG = Logger.getLogger(L1Properties.class.getName());

    /**
     * 加载.
     * 
     * @param file
     *            文件
     * @return 文件
     */
    public static L1Properties load(final File file) {
        LOG.fine("加载属性文件: " + file.getName());
        try {
            return new L1Properties(file);
        } catch (final IOException e) {
            return new NullProperties(e);
        }
    }

    /** 属性文件. */
    private final Properties props = new Properties();

    /**
     * 属性文件.
     */
    private L1Properties() {
    }

    /**
     * 属性文件.
     * 
     * @param file
     *            文件
     * @throws IOException
     *             当发生某种 I/O 异常时，抛出此异常。此类为异常的通用类，它是由失败的或中断的 I/O 操作生成的。
     */
    private L1Properties(final File file) throws IOException {
        final InputStream is = new FileInputStream(file);
        this.props.load(is);
        is.close();
    }

    /**
     * 取得IO异常.
     * 
     * @return 构造不带详细消息的 UnsupportedOperationException。
     */
    public IOException getException() {
        throw new UnsupportedOperationException();
    }

    /**
     * 取得属性文件.
     * 
     * @param key
     *            密钥
     * @param flag
     *            允许的默认值
     * @return 属性文件
     */
    public String getProperty(final String key, final boolean flag) {
        if (!this.props.containsKey(key)) {
            this.notifyLoadingDefault(key, flag);
            return null;
        }
        return this.props.getProperty(key);
    }

    /**
     * 是否为空.
     * 
     * @return true or false
     */
    public boolean isNull() {
        return false;
    }

    /**
     * 通知加载预设值.
     * 
     * @param key
     *            密钥
     * @param flag
     *            允许的默认值
     */
    private void notifyLoadingDefault(final String key, final boolean flag) {
        if (!flag) {
            // 默认值是不允许的。错误
            throw new RuntimeException(key + " 不存在. 他没有预设值.");
        }
        // 加载默认值的提示信息
        LOG.info(key + " 不存在. 服务器使用默认值.");
    }

}
