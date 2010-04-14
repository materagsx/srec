package com.github.srec.rec.component;

import com.github.srec.rec.EventRecorder;
import com.github.srec.RecorderEvent;
import com.github.srec.Utils;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * Understands recording text entered in text fields.
 *
 * @author Vivek Prahlad
 */
public class TextFieldRecorder extends AbstractComponentRecorder {
    private static final Logger logger = Logger.getLogger(TextFieldRecorder.class);
    private Map<JTextComponent, DocumentListener> listenerMap = new HashMap<JTextComponent, DocumentListener>();
    private ComponentVisibility visibility;

    /**
     * Flag which indicates whether the recorder should scan for labels if the text field has no name.
     */
    private boolean scanForLabels = true;

    public TextFieldRecorder(EventRecorder recorder, ComponentVisibility visibility) {
        super(recorder, JTextField.class);
        this.visibility = visibility;
    }

    @Override
    public void register() {
        super.register();
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (event.getID() != KeyEvent.KEY_TYPED) return;
                if (((KeyEvent) event).getKeyChar() == '\t' && event.getSource() instanceof JTextField) {
                    JTextField tf = (JTextField) event.getSource();
                    recorder.record(new RecorderEvent("type_special", tf.getName(), tf, false, "Tab"));

                }
            }
        }, AWTEvent.KEY_EVENT_MASK);
    }

    void componentShown(Component component) {
        DocumentListener listener = new TextFieldListener(textField(component));
        textField(component).getDocument().addDocumentListener(listener);
        listenerMap.put(textField(component), listener);
    }

    void componentHidden(Component component) {
        DocumentListener listener = listenerMap.remove(textField(component));
        textField(component).getDocument().removeDocumentListener(listener);
    }

    protected boolean matchesComponentType(AWTEvent event) {
        return event.getSource() instanceof JTextComponent;
    }

    private JTextComponent textField(Component component) {
        return (JTextComponent) component;
    }

    private class TextFieldListener implements DocumentListener {
        private JTextComponent textField;

        public TextFieldListener(JTextComponent textField) {
            this.textField = textField;
        }

        public void insertUpdate(DocumentEvent e) {
            record();
        }

        private void record() {
            if (!visibility.isShowingAndHasFocus(textField)) return;
            String locator = textField.getName();
            if (isBlank(locator) && scanForLabels) {
                JLabel label = Utils.getLabelFor(textField.getParent(), textField);
                if (label != null) locator = "label=" + label.getText();
            }
            logger.debug("TextField event registered: '" + locator + "', value: '" + textField.getText() + "'");
            recorder.record(new RecorderEvent("type", locator, textField, textField.getText()));
        }

        public void removeUpdate(DocumentEvent e) {
            record();
        }

        public void changedUpdate(DocumentEvent e) {
            //Do nothing for default text fields.
        }
    }
}