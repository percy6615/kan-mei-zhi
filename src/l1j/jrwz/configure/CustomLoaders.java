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

import java.util.Calendar;

import l1j.jrwz.configure.Annotations.Configures;

/**
 * Class <code>CustomLoaders</code> 自定义加载.
 * 
 * @author jrwz
 * @version 2012-6-3上午01:10:22
 * @see l1j.jrwz.configure
 * @since JDK1.7
 */
public final class CustomLoaders {

    /**
     * 自定义加载接口.
     */
    interface CustomLoader {
        /**
         * 加载值.
         * 
         * @param config
         *            配置
         * @param type
         *            类型
         * @param value
         *            值
         * @return 结果
         */
        Object loadValue(Configures config, Class<?> type, String value);
    }

    /**
     * 默认加载.
     */
    static class DefaultLoader implements CustomLoader {
        @Override
        public Object loadValue(final Configures config, final Class<?> type, final String value) {
            Object result = null;

            if (type.equals(int.class)) {
                result = Integer.parseInt(value);
            } else if (type.equals(String.class)) {
                result = value;
            } else if (type.equals(boolean.class)) {
                result = Boolean.parseBoolean(value);
            } else if (type.equals(double.class)) {
                result = Double.parseDouble(value);
            }
            return result;
        }
    }

    /**
     * 加载战争时间.
     */
    static class WarTimeLoader implements CustomLoader {
        @Override
        public Object loadValue(final Configures config, final Class<?> type, final String value) {
            // 只提取数值
            final String time = value.replaceFirst("^(\\d+)d|h|m$", "$1");
            return Integer.parseInt(time);
        }
    }

    /**
     * 加载战争时间控制器.
     */
    static class WarTimeUnitLoader implements CustomLoader {
        @Override
        public Object loadValue(final Configures config, final Class<?> type, final String value) {
            // 只提取单元
            final String unit = value.replaceFirst("^\\d+(d|h|m)$", "$1");
            if (unit.equals("d")) {
                return Calendar.DATE;
            }
            if (unit.equals("h")) {
                return Calendar.HOUR_OF_DAY;
            }
            if (unit.equals("m")) {
                return Calendar.MINUTE;
            }
            throw new IllegalArgumentException();
        }
    }

}
