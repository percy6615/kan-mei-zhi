/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.jrwz.server.utils;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

public class SystemUtil {
    private static String change(final long value) {
        return (value >> 20L) + "M"; // (/1024L / 1024L)
    }

    /**
     * 取得目前的作业系统
     * 
     * @return Linux or Windows
     */
    public static String gerOs() {
        String Os = "";
        final String OsName = System.getProperty("os.name");
        if (OsName.toLowerCase().indexOf("windows") >= 0) {
            Os = "Windows";
        } else if (OsName.toLowerCase().indexOf("linux") >= 0) {
            Os = "Linux";
        }
        return Os;
    }

    /**
     * 得知作业系统是几位元 Only for Windows
     * 
     * @return x86 or x64
     */
    public static String getOsArchitecture() {
        final String x64_System = "C:\\Windows\\SysWOW64";
        String result;
        final File dir = new File(x64_System);
        if (dir.exists()) {
            result = "x64";
        } else {
            result = "x86";
        }
        return result;
    }

    /**
     * 取得系统内存使用率(堆与栈). 初始、当前、最高
     */
    public static void getUsedMemory() {
        final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        final String msg1 = "物理内存使用率: " + change(memoryMXBean.getHeapMemoryUsage().getInit()) + "/"
                + change(memoryMXBean.getHeapMemoryUsage().getUsed()) + "/"
                + change(memoryMXBean.getHeapMemoryUsage().getMax());
        final String msg2 = "核心内存使用率: " + change(memoryMXBean.getNonHeapMemoryUsage().getInit()) + "/"
                + change(memoryMXBean.getNonHeapMemoryUsage().getUsed()) + "/"
                + change(memoryMXBean.getNonHeapMemoryUsage().getMax());
        System.out.println("\r\n" + msg1 + "\r\n" + msg2 + "\r\n");
    }

    /**
     * 返回兆字节的可用系统堆大小. 栈的大小是不包括在这个值。
     * 
     * @return 使用中的堆大小
     */
    public static long getUsedMemoryMB() {
        final long totalMemory = Runtime.getRuntime().totalMemory();
        final long freeMemory = Runtime.getRuntime().freeMemory();
        return (totalMemory - freeMemory) >> 20L; // (/1024L / 1024L)
    }
}
