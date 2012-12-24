/*
 * ServerConfigMain.java
 *
 * Created on __DATE__, __TIME__
 */

package com.lineage.config.tool;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;

import javolution.util.FastList;

/**
 * @author CurseSlime
 */
public class ServerConfig extends javax.swing.JFrame {

    private static final long serialVersionUID = 5552272419152935070L;
    private static final String CHAR_SET = "UTF-8";

    private List<ConfigFile> _configs = new FastList<ConfigFile>();

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    JFrame.setDefaultLookAndFeelDecorated(true);
                    JDialog.setDefaultLookAndFeelDecorated(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new ServerConfig().setVisible(true);
            }
        });
    }

    public static String unCamelize(String keyName) {
        Pattern p = Pattern.compile("\\p{Lu}");
        Matcher m = p.matcher(keyName);
        StringBuffer sb = new StringBuffer();
        int last = 0;
        while (m.find()) {
            if (m.start() != last + 1) {
                m.appendReplacement(sb, new StringBuilder().append(" ").append(m.group()).toString());
            }
            last = m.start();
        }
        m.appendTail(sb);
        return sb.toString().trim();
    }

    /** Creates new form ServerConfigMain */
    public ServerConfig() {
        initComponents();
        loadConfigs();
        buildInterface();
    }

    // GEN-BEGIN:initComponents
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        toolBar = new javax.swing.JToolBar();
        btnSave = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();
        btnInfo = new javax.swing.JButton();
        _tabPane = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("L1j Properties Edit");
        setIconImage(ImageTable.getIcon("config.ico"));
        setMinimumSize(new java.awt.Dimension(750, 500));
        setName("mainFrame");

        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.setFocusable(false);

        btnSave.setIcon(ImageTable.getImage("save.png"));
        btnSave.setMnemonic('S');
        btnSave.setText("\u4fdd\u5b58(S)");
        btnSave.setName("btnSave");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        toolBar.add(btnSave);

        btnExit.setIcon(ImageTable.getImage("exit.png"));
        btnExit.setMnemonic('c');
        btnExit.setText("\u5173\u95ed(C)");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        toolBar.add(btnExit);

        btnInfo.setIcon(ImageTable.getImage("info.png"));
        btnInfo.setMnemonic('h');
        btnInfo.setText("\u8bf4\u660e(H)");
        btnInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInfoActionPerformed(evt);
            }
        });
        toolBar.add(btnInfo);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(toolBar, javax.swing.GroupLayout.Alignment.TRAILING,
                        javax.swing.GroupLayout.DEFAULT_SIZE, 647, Short.MAX_VALUE)
                .addComponent(_tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 647, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup()
                        .addComponent(toolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 39,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(_tabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE)));

        pack();
    }// </editor-fold>
     // GEN-END:initComponents

    // GEN-END:initComponents

    private void btnInfoActionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane.showMessageDialog(this, "本程序修改自l2j Server,jrwz", "说明", 1);
    }

    void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {
        StringBuffer errors = new StringBuffer();
        for (ConfigFile cf : getConfigs()) {
            try {
                cf.save();
            } catch (Exception e1) {
                e1.printStackTrace();
                errors.append(new StringBuilder().append("保存错误 ").append(cf.getName()).append(".properties. ")
                        .append(" 原因:").append(e1.getLocalizedMessage()).append("\r\n").toString());
            }
        }

        if (errors.length() == 0) {
            JOptionPane.showMessageDialog(this, "配置保存成功", "OK", 1);
        } else {
            JOptionPane.showMessageDialog(this, errors, "Error", 0);

            System.exit(2);
        }
    }

    void btnExitActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    public List<ConfigFile> getConfigs() {
        return this._configs;
    }

    public void setConfigs(List<ConfigFile> configs) {
        this._configs = configs;
    }

    /**
     * 创建界面
     */
    private void buildInterface() {
        ToolTipManager.sharedInstance().setDismissDelay(2147483647);
        ToolTipManager.sharedInstance().setInitialDelay(0);
        ToolTipManager.sharedInstance().setReshowDelay(0);

        GridBagConstraints cons = new GridBagConstraints();
        cons.fill = 0;
        cons.anchor = 23;
        cons.insets = new Insets(2, 2, 2, 2);
        for (ConfigFile cf : getConfigs()) {
            JPanel panel = new JPanel() {
                private static final long serialVersionUID = -179122651452028472L;

                public void scrollRectToVisible(Rectangle r) {
                }
            };
            panel.setLayout(new GridBagLayout());

            cons.gridy = 0;
            cons.weighty = 0.0D;
            for (ConfigFile.ConfigComment cc : cf.getConfigProperties())
                if ((cc instanceof ConfigFile.ConfigProperty)) {
                    ConfigFile.ConfigProperty cp = (ConfigFile.ConfigProperty) cc;
                    cons.gridx = 0;

                    JLabel keyLabel = new JLabel(
                            new StringBuilder().append(cp.getDisplayName()).append(':').toString(),
                            ImageTable.getImage("b.png"), 2);

                    String comments = new StringBuilder().append("<b>").append(cp.getName()).append(":</b><br>")
                            .append(cp.getComments()).toString();

                    comments = comments.replace("\r\n", "<br>");
                    comments = new StringBuilder().append("<html><font size='5'>").append(comments)
                            .append("</font></html>").toString();

                    keyLabel.setToolTipText(comments);
                    cons.weightx = 0.0D;
                    panel.add(keyLabel, cons);
                    cons.gridx += 1;

                    JComponent valueComponent = cp.getValueComponent();
                    valueComponent.setToolTipText(comments);
                    cons.weightx = 1.0D;
                    panel.add(valueComponent, cons);
                    cons.gridx += 1;
                    cons.gridy += 1;
                }
            cons.gridy += 1;
            cons.weighty = 1.0D;
            panel.add(new JLabel(), cons);
            this._tabPane.addTab(cf.getName(), new JScrollPane(panel));
        }
    }

    /**
     * 加载配置文件
     */
    private void loadConfigs() {
        File configsDir = new File("config");
        for (File file : configsDir.listFiles())
            if ((file.getName().endsWith(".properties")) && (file.isFile()) && (file.canWrite())
                    && (!file.getName().endsWith("sql.properties")) && (!file.getName().endsWith("java.properties"))) {
                try {
                    parsePropertiesFile(file);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, new StringBuilder().append("读取错误").append(file.getName())
                            .toString(), "Error", 0);

                    System.exit(3);
                }
            }
    }

    /**
     * 读取配置文件，存入变量Configs
     * 
     * @param file
     * @throws IOException
     */
    private void parsePropertiesFile(File file) throws IOException {
        LineNumberReader lnr = new LineNumberReader(new InputStreamReader(new FileInputStream(file), CHAR_SET));

        StringBuilder commentBuffer = new StringBuilder();
        ConfigFile cf = new ConfigFile(file);
        String line;
        while ((line = lnr.readLine()) != null) {
            line = line.trim();

            if (line.startsWith("#")) {
                if (commentBuffer.length() > 0) {
                    commentBuffer.append("\r\n");
                }
                commentBuffer.append(line.substring(1));
            } else if (line.length() == 0) { // 断行
                if (commentBuffer.length() > 0) {
                    cf.addConfigComment(commentBuffer.toString());
                }
                commentBuffer.setLength(0);
            } else if (line.indexOf('=') >= 0) {
                String[] kv = line.split("=");
                String key = kv[0].trim();
                String value = "";
                if (kv.length > 1) {
                    value = kv[1].trim();
                }

                if (line.indexOf('\\') >= 0) { // QU. 为什么这里要加这个判断，如果有备注的话?
                    while (((line = lnr.readLine()) != null) && (line.indexOf('\\') >= 0)) {
                        value = new StringBuilder().append(value).append("\r\n").append(line).toString();
                    }
                    value = new StringBuilder().append(value).append("\r\n").append(line).toString();
                }

                String comments = commentBuffer.toString();
                commentBuffer.setLength(0);

                cf.addConfigProperty(key, parseValue(value), comments);
            }
        }
        getConfigs().add(cf);
        lnr.close();
    }

    private Object parseValue(String value) {
        if ((value.equalsIgnoreCase("false")) || (value.equalsIgnoreCase("true"))) {
            return Boolean.valueOf(Boolean.parseBoolean(value));
        }

        if (value.equals("localhost")) {
            value = "127.0.0.1";
        }

        // 如果为IPV4格式
        String[] parts = value.split("\\.");
        if (parts.length == 4) {
            boolean ok = true;
            for (int i = 0; (i < 4) && (ok); i++) {
                try {
                    int parseInt = Integer.parseInt(parts[i]);
                    if ((parseInt < 0) || (parseInt > 255))
                        ok = false;
                } catch (NumberFormatException e) {
                    ok = false;
                }
            }

            if (ok) {
                try {
                    return InetAddress.getByName(value);
                } catch (UnknownHostException e) {
                }
            }
        }

        return value;
    }

    /**
     * 配置文件 Class <code>ConfigFile</code>.
     */
    static class ConfigFile {
        private final List<ConfigComment> _configs = new FastList<ConfigComment>();
        private final File _file;
        private String _name;

        public ConfigFile(File file) {
            this._file = file;
            int lastIndex = file.getName().lastIndexOf('.');
            setName(file.getName().substring(0, lastIndex));
        }

        public void addConfigComment(String comment) {
            this._configs.add(new ConfigComment(comment));
        }

        public void addConfigProperty(String name, Object value, String comments) {
            addConfigProperty(name, value, ServerConfig.ValueType.firstTypeMatch(value), comments);
        }

        public void addConfigProperty(String name, Object value, ServerConfig.ValueType type, String comments) {
            this._configs.add(new ConfigProperty(name, value, type, comments));
        }

        public List<ConfigComment> getConfigProperties() {
            return this._configs;
        }

        public String getName() {
            return this._name;
        }

        public void save() throws IOException {
            BufferedWriter bufWriter = null;
            try {
                bufWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this._file), CHAR_SET));

                for (ConfigComment cc : this._configs)
                    cc.save(bufWriter);
            } finally {
                if (bufWriter != null)
                    bufWriter.close();
            }
        }

        public void setName(String name) {
            this._name = name;
        }

        /**
         * 配置文件属性值
         */
        class ConfigProperty extends ConfigFile.ConfigComment {
            private String _propname;
            private Object _value;
            private ServerConfig.ValueType _type;
            private JComponent _component;

            public ConfigProperty(String name, Object value, ServerConfig.ValueType type, String comments) {
                super(comments);

                if (!type.getType().isAssignableFrom(value.getClass())) {
                    throw new IllegalArgumentException("值的实例类型与类型参数不匹配.");
                }
                this._propname = name;
                this._type = type;
                this._value = value;
            }

            public JComponent createValueComponent() {
                switch (this.getType()) {
                case BOOLEAN:
                    boolean bool = ((Boolean) getValue()).booleanValue();
                    JCheckBox checkBox = new JCheckBox();
                    checkBox.setSelected(bool);
                    return checkBox;
                case IPv4:
                    return new JIPTextField((Inet4Address) getValue());
                case DOUBLE:
                case INTEGER:
                case STRING:
                }
                String val = getValue().toString();
                JTextArea textArea = new JTextArea(val);
                textArea.setFont(UIManager.getFont("TextField.font"));

                int rows = 1;
                for (int i = 0; i < val.length(); i++) {
                    if (val.charAt(i) == '\\') {
                        rows++;
                    }
                }
                textArea.setRows(rows);
                textArea.setColumns(Math.max(val.length() / rows, 20));
                return textArea;
            }

            public String getDisplayName() {
                return unCamelize(this._propname);
            }

            public String getName() {
                return this._propname;
            }

            public ServerConfig.ValueType getType() {
                return this._type;
            }

            public Object getValue() {
                return this._value;
            }

            public JComponent getValueComponent() {
                if (this._component == null) {
                    this._component = createValueComponent();
                }
                return this._component;
            }

            public void save(Writer writer) throws IOException {
                String value;
                if ((getValueComponent() instanceof JCheckBox)) {
                    value = Boolean.toString(((JCheckBox) getValueComponent()).isSelected());

                    value = value.substring(0, 1).toUpperCase() + value.substring(1);
                } else {

                    if ((getValueComponent() instanceof JIPTextField)) {
                        value = ((JIPTextField) getValueComponent()).getText();
                    } else {

                        if ((getValueComponent() instanceof JTextArea))
                            value = ((JTextArea) getValueComponent()).getText();
                        else
                            throw new IllegalStateException("未处理的组件值");
                    }
                }

                StringBuilder sb = new StringBuilder();
                sb.append('#');
                sb.append(getComments().replace("\r\n", "\r\n#"));
                sb.append("\r\n");
                sb.append(getName());
                sb.append(" = ");
                sb.append(value);
                sb.append("\r\n");
                sb.append("\r\n");
                writer.write(sb.toString());
            }

            public void setName(String name) {
                this._propname = name;
            }

            public void setType(ServerConfig.ValueType type) {
                this._type = type;
            }

            public void setValue(String value) {
                this._value = value;
            }
        }

        /**
         * 
         * Class <code>ConfigComment</code>.
         */
        class ConfigComment {
            private String _comments;

            public ConfigComment(String comments) {
                this._comments = comments;
            }

            public String getComments() {
                return this._comments;
            }

            public void save(Writer writer) throws IOException {
                StringBuilder sb = new StringBuilder();
                sb.append('#');
                sb.append(getComments().replace("\r\n", "\r\n#"));
                sb.append("\r\n\r\n");
                writer.write(sb.toString());
            }

            public void setComments(String comments) {
                this._comments = comments;
            }
        }
    }

    /**
     * 枚举值类型 Class <code>ValueType</code>.
     */
    public static enum ValueType {
        BOOLEAN(Boolean.class), DOUBLE(Double.class), INTEGER(Integer.class), IPv4(Inet4Address.class), STRING(
                String.class);

        private final Class<?> _type;

        public static ValueType firstTypeMatch(Object value) {
            for (ValueType vt : values()) {
                if (vt.getType() == value.getClass()) {
                    return vt;
                }
            }
            throw new NoSuchElementException("没有相匹配的: " + value.getClass().getName());
        }

        private ValueType(Class<?> type) {
            this._type = type;
        }

        public Class<?> getType() {
            return this._type;
        }
    }

    // GEN-BEGIN:variables
    // Variables declaration - do not modify
    private javax.swing.JTabbedPane _tabPane;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnInfo;
    private javax.swing.JButton btnSave;
    private javax.swing.JToolBar toolBar;
    // End of variables declaration//GEN-END:variables
}