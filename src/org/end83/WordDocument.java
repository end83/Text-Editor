package org.end83;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WordDocument extends JScrollPane implements DocumentListener {

    private String name;
    private String path;
    private JTextArea text;
    private boolean newDocument;
    private boolean unsaved;

    @Override
    public String getName() {
        return name;
    }

    public JTextArea getText() {
        return text;
    }

    public String getPath() {
        return path;
    }

    public boolean isNewDocument() {
        return newDocument;
    }

    public boolean isUnsaved() {
        return unsaved;
    }

    public WordDocument(boolean newDocument){
        this("Untitled.txt", new JTextArea(), newDocument);
    }
    public WordDocument(String name, JTextArea text, boolean newDocument){
        super(text);
        this.name = name;
        this.path = "";
        this.text = text;
        this.newDocument = newDocument;
        this.text.getDocument().addDocumentListener(this);
    }

    public WordDocument(File file, JTextArea text, boolean newDocument){
        super(text);
        this.name = file.getName();
        this.text = text;
        this.newDocument = newDocument;
        this.path = file.getAbsolutePath();
    }

    public void save(){
        save(path);
    }

    public boolean save(String path){
        try {
            File file = new File(path);
            if(file.exists()){
                if(newDocument || !this.path.equals(path)) {
                    int returnValue = JOptionPane.showConfirmDialog(null, "Do you wish to overwrite current file?", "!!Warning!!", JOptionPane.YES_NO_OPTION);
                    if (returnValue == JOptionPane.NO_OPTION) {
                        return false;
                    }
                }
            }
            this.name = file.getName();
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(text.getText());
            bw.close();
            this.newDocument = false;
            this.path = path;
            this.unsaved = true;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return true;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        update();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        update();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        update();
    }

    private void update(){
        unsaved = true;
    }
}
