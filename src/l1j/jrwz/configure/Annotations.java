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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import l1j.jrwz.configure.CustomLoaders.CustomLoader;
import l1j.jrwz.configure.CustomLoaders.DefaultLoader;

/**
 * Class <code>Annotations</code> 注释类型(服务器设置).
 * 
 * @author jrwz
 * @version 2012-6-3上午01:11:38
 * @see l1j.jrwz.configure
 * @since JDK1.7
 */
public final class Annotations {

    /**
     * 1.(Retention).<br>
     * 指示注释类型的注释要保留多久。<br>
     * 如果注释类型声明中不存在 Retention 注释，则保留策略默认为 RetentionPolicy.CLASS。<br>
     * 只有元注释类型直接用于注释时，Target 元注释才有效。如果元注释类型用作另一种注释类型的成员，则无效。<br>
     * <br>
     * 
     * 1.1(RetentionPolicy)<br>
     * 注释保留策略。此枚举类型的常量描述保留注释的不同策略。它们与 Retention 元注释类型一起使用，以指定保留多长的注释。<br>
     * <br>
     * 
     * 1.2(RUNTIME)<br>
     * 编译器将把注释记录在类文件中，在运行时 VM 将保留注释，因此可以反射性地读取。<br>
     * <br>
     * 
     * 2.(Target)<br>
     * 指示注释类型所适用的程序元素的种类。<br>
     * 如果注释类型声明中不存在 Target 元注释，则声明的类型可以用在任一程序元素上。<br>
     * 如果存在这样的元注释，则编译器强制实施指定的使用限制。<br>
     * <br>
     * 
     * 2.1(ElementType)<br>
     * 程序元素类型。此枚举类型的常量提供了 Java 程序中声明的元素的简单分类。<br>
     * 这些常量与 Target 元注释类型一起使用，以指定在什么情况下使用注释类型是合法的。<br>
     * <br>
     * 
     * 2.2(FIELD)<br>
     * 字段声明（包括枚举常量）<br>
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Configures {
        /** 允许的默认值. */
        boolean allowDefaultValue() default true;

        /** 文件名. */
        String file();

        /** 是否可选. */
        boolean isOptional() default false;

        /** 密钥名. */
        String key() default "";

        /** 加载. */
        Class<? extends CustomLoader> loader() default DefaultLoader.class;
    }

}
