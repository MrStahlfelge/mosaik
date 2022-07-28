package org.ergoplatform.mosaik

import java.awt.Color
import java.awt.FlowLayout
import java.awt.event.WindowEvent
import java.awt.event.WindowFocusListener
import javax.swing.*
import javax.swing.border.EmptyBorder
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

// kindly borrowed from libGDX. Ugly, but it does it
object TextInputDialog {
    fun showInputDialog(listener: ((String?) -> Unit), title: String, text: String, hint: String) {
        SwingUtilities.invokeLater {
            val panel = JPanel(FlowLayout())
            val textPanel: JPanel = object : JPanel() {
                override fun isOptimizedDrawingEnabled(): Boolean {
                    return false
                }
            }
            textPanel.layout = OverlayLayout(textPanel)
            panel.add(textPanel)
            val textField = JTextField(20)
            textField.text = text
            textField.alignmentX = 0.0f
            textPanel.add(textField)
            val placeholderLabel = JLabel(hint)
            placeholderLabel.foreground = Color.GRAY
            placeholderLabel.alignmentX = 0.0f
            textPanel.add(placeholderLabel, 0)
            textField.document.addDocumentListener(object : DocumentListener {
                override fun removeUpdate(arg0: DocumentEvent) {
                    updated()
                }

                override fun insertUpdate(arg0: DocumentEvent) {
                    updated()
                }

                override fun changedUpdate(arg0: DocumentEvent) {
                    updated()
                }

                private fun updated() {
                    if (textField.text.length == 0) placeholderLabel.isVisible =
                        true else placeholderLabel.isVisible =
                        false
                }
            })
            val pane = JOptionPane(
                panel, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, null,
                null
            )
            pane.initialValue = null
            pane.componentOrientation = JOptionPane.getRootFrame().componentOrientation
            val border = textField.border
            placeholderLabel.border = EmptyBorder(border.getBorderInsets(textField))
            val dialog = pane.createDialog(null, title)
            pane.selectInitialValue()
            dialog.addWindowFocusListener(object : WindowFocusListener {
                override fun windowLostFocus(arg0: WindowEvent) {}
                override fun windowGainedFocus(arg0: WindowEvent) {
                    textField.requestFocusInWindow()
                }
            })
            dialog.isModal = true
            dialog.isAlwaysOnTop = true
            dialog.isVisible = true
            dialog.dispose()
            val selectedValue = pane.value
            if (selectedValue != null && selectedValue is Int
                && (selectedValue as Int).toInt() == JOptionPane.OK_OPTION
            ) {
                listener.invoke(textField.text)
            }
        }
    }
}
