package com.lineage.config.tool;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public final class JIPTextField extends JPanel implements FocusListener {
    private static final long serialVersionUID = 1L;
    private JTextField[] _textFields;
    private List<FocusListener> _focusListeners;

    public JIPTextField() {
        this("...");
    }

    public JIPTextField(Inet4Address value) {
        this(value.getHostAddress());
    }

    public JIPTextField(String textIp) {
        super.addFocusListener(this);
        initIPTextField(textIp);
        for (JTextField _textField : this._textFields)
            _textField.addFocusListener(this);
    }

    public void addFocusListener(FocusListener fl) {
        if (this._focusListeners == null) {
            this._focusListeners = new LinkedList<FocusListener>();
        }

        if ((fl != null) && (!this._focusListeners.contains(fl)))
            this._focusListeners.add(fl);
    }

    public void focusGained(FocusEvent event) {
        if (this._focusListeners != null)
            for (FocusListener fl : this._focusListeners)
                fl.focusGained(event);
    }

    public void focusLost(FocusEvent event) {
        if (((isCorrect()) || (isEmpty())) && (this._focusListeners != null))
            for (FocusListener fl : this._focusListeners)
                fl.focusLost(event);
    }

    public String getText() {
        String str = "";
        for (int i = 0; i < 4; i++) {
            if (this._textFields[i].getText().length() == 0)
                str = str + "0";
            else {
                str = str + this._textFields[i].getText();
            }
            if (i < 3) {
                str = str + ".";
            }
        }
        return str;
    }

    public boolean isCorrect() {
        for (int i = 0; i < 4; i++) {
            if (this._textFields[i].getText().length() == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty() {
        for (int i = 0; i < 4; i++) {
            if (this._textFields[i].getText().length() != 0) {
                return false;
            }
        }
        return true;
    }

    public void removeFocusListener(FocusListener fl) {
        if (this._focusListeners != null)
            this._focusListeners.remove(fl);
    }

    public void setEnabled(boolean enabled) {
        for (JTextField _textField : this._textFields)
            if (_textField != null)
                _textField.setEnabled(enabled);
    }

    public void setText(String str) {
        try {
            str.length();

            InetAddress ip = InetAddress.getByName(str);
            byte[] b = ip.getAddress();
            for (int i = 0; i < 4; i++) {
                if (b[i] >= 0)
                    this._textFields[i].setText(Byte.toString(b[i]));
                else {
                    this._textFields[i].setText(Integer.toString(b[i] + 256));
                }
            }
            return;
        } catch (UnknownHostException ex) {
        } catch (NullPointerException npe) {
        }
        for (int i = 0; i < 4; i++)
            this._textFields[i].setText("");
    }

    private void initIPTextField(String textIp) {
        ActionListener nextfocusaction = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ((Component) evt.getSource()).transferFocus();
            }
        };
        setLayout(new GridBagLayout());
        this._textFields = new JTextField[4];

        GridBagConstraints cons = new GridBagConstraints();
        cons.anchor = 19;
        cons.fill = 2;
        cons.insets = new Insets(1, 1, 1, 1);
        cons.gridx = 0;
        cons.gridy = 0;

        MaxLengthDocument previous = null;
        String[] parts = textIp.split("\\.");
        for (int i = 0; i < 4; i++) {
            String str = parts[i];
            if (i > 0) {
                JLabel dot = new JLabel(".");
                cons.weightx = 0.0D;
                add(dot, cons);
                cons.gridx += 1;
            }
            MaxLengthDocument maxDoc = new MaxLengthDocument(3);
            this._textFields[i] = new JTextField(maxDoc, str, 3);
            if (previous != null) {
                previous.setNext(this._textFields[i]);
            }
            previous = maxDoc;
            add(this._textFields[i], cons);
            this._textFields[i].addActionListener(nextfocusaction);
            cons.gridx += 1;
        }
    }

    public class MaxLengthDocument extends PlainDocument {
        private static final long serialVersionUID = 1L;
        private final int _max;
        private JTextField _next;

        public MaxLengthDocument(int maxLength) {
            this(maxLength, null);
        }

        public MaxLengthDocument(int maxLength, JTextField next) {
            this._max = maxLength;
            setNext(next);
        }

        public final JTextField getNext() {
            return this._next;
        }

        public final void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
            if (getLength() + str.length() > this._max) {
                if (getNext() != null) {
                    if (getNext().getText().length() > 0) {
                        getNext().select(0, getNext().getText().length());
                    } else {
                        getNext().getDocument().insertString(0, str, a);
                    }
                    getNext().requestFocusInWindow();
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            } else
                super.insertString(offset, str, a);
        }

        public final void setNext(JTextField next) {
            this._next = next;
        }
    }
}