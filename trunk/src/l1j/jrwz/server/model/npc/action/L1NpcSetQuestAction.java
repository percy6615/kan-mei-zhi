package l1j.jrwz.server.model.npc.action;

import l1j.jrwz.server.model.L1Object;
import l1j.jrwz.server.model.Instance.L1PcInstance;
import l1j.jrwz.server.model.npc.L1NpcHtml;

import org.w3c.dom.Element;

public class L1NpcSetQuestAction extends L1NpcXmlAction {
    private final int _id;
    private final int _step;

    public L1NpcSetQuestAction(Element element) {
        super(element);

        _id = L1NpcXmlParser.parseQuestId(element.getAttribute("Id"));
        _step = L1NpcXmlParser.parseQuestStep(element.getAttribute("Step"));

        if (_id == -1 || _step == -1) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public L1NpcHtml execute(String actionName, L1PcInstance pc, L1Object obj,
            byte[] args) {
        pc.getQuest().set_step(_id, _step);
        return null;
    }

}
