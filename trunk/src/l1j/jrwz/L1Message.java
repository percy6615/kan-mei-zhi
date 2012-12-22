package l1j.jrwz;

import java.util.ResourceBundle;

public class L1Message {

    private static L1Message _instance;

    public static L1Message getInstance() {
        if (_instance == null) {
            _instance = new L1Message();
        }
        return _instance;
    }

    ResourceBundle resource;

    public static String _memoryUse;

    private L1Message() {
        resource = ResourceBundle.getBundle("messages");
        initLocaleMessage();
    }

    public void initLocaleMessage() {
        _memoryUse = resource.getString("l1j.jrwz.memoryUse");
    }

}
