package l1j.jrwz.server.serverpackets;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import l1j.jrwz.configure.Config;
import l1j.jrwz.server.codes.Opcodes;
import l1j.jrwz.server.model.Instance.L1PcInstance;

/**
 * 玩家输入WHO显示信息为布告栏(讯息阅读)模式
 */
public class S_WhoStationery extends ServerBasePacket {

    private static final String _S_WhoStationery = "[S] _S_WhoStationery";

    private byte[] _byte = null;

    /**
     * 玩家输入WHO显示信息为布告栏(讯息阅读)模式
     * 
     * @param pc
     *            查询的玩家
     */
    public S_WhoStationery(final L1PcInstance pc) {

        final double EXP = Config.RATE_XP;
        final double RWL = Config.RATE_WEIGHT_LIMIT;
        final double RDI = Config.RATE_DROP_ITEMS;
        final double RDA = Config.RATE_DROP_ADENA;
        final double RLA = Config.RATE_LA;
        final double RKA = Config.RATE_KARMA;
        final int RKC = pc.get_PKcount();
        // final int time = L1GameReStart.getWillRestartTime();

        final String S_WhoCharinfo = "经验倍率:" + EXP + " 倍\r\n" + "负重倍率:" + RWL + " 倍\r\n" + "掉宝倍率:" + RDI + " 倍\r\n"
                + "金币倍率:" + RDA + " 倍\r\n" + "正义倍率:" + RLA + " 倍\r\n" + "友好倍率:" + RKA + " 倍\r\n" + "总PK次数:" + RKC
                + " 次\r\n" + "距离重启时间剩余:" + (Config.REST_TIME ) / 60 + "小时" + Config.REST_TIME % 60 + "分钟";

        // 当前的 年、月、日 (范例:12/01/10)
        final SimpleDateFormat setDateFormat = new SimpleDateFormat("yy/MM/dd");
        this.writeC(Opcodes.S_OPCODE_BOARDREAD);
        this.writeD(0x00);
        this.writeS("Lineage"); // 作者
        this.writeS("系统讯息"); // 标题
        this.writeS(setDateFormat.format(Calendar.getInstance().getTime())); // 讨论编号
        this.writeS(S_WhoCharinfo); // 显示查询信息
    }

    @Override
    public byte[] getContent() {
        if (this._byte == null) {
            this._byte = this._bao.toByteArray();
        }
        return this._byte;
    }

    @Override
    public String getType() {
        return _S_WhoStationery;
    }

}
