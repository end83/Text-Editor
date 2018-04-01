package org.end83;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Main extends JFrame implements ActionListener{

    private JTabbedPane tabPane;
    public static void main(String[] args) {
        new Main().setVisible(true);
    }
    private Main(){
        super("Plain Document");
        setSize(800,600);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitSafely();
            }
        });
        initialize();
    }
    private void initialize(){

        tabPane = new JTabbedPane();
        WordDocument wordDoc = new WordDocument(true);
        tabPane.add(wordDoc.getName(), wordDoc);

        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("File");

        JMenuItem open = new JMenuItem("Open");
        JMenuItem newDoc = new JMenuItem("New");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem saveAs = new JMenuItem("Save As");
        JMenuItem exit = new JMenuItem("Exit");

        newDoc.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | InputEvent.SHIFT_DOWN_MASK));

        JMenuItem[] items = {newDoc, open, save, saveAs, exit};
        for(JMenuItem item : items){
            item.addActionListener(this);
        }
        file.add(newDoc);
        file.add(open);
        file.add(save);
        file.add(saveAs);
        file.addSeparator();
        file.add(exit);

        menu.add(file);
        add(tabPane);
        setJMenuBar(menu);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equalsIgnoreCase("new")){
            newDoc();
        }
        else if(e.getActionCommand().equalsIgnoreCase("open")){
            open();
        }else if(e.getActionCommand().equalsIgnoreCase("save")){
            save();
        }
        else if(e.getActionCommand().equalsIgnoreCase("save as")){
            saveAs();
        }
        else if(e.getActionCommand().equalsIgnoreCase("exit")){
            System.exit(0);
        }
    }

    private void newDoc(){
        WordDocument doc = new WordDocument(true);
        tabPane.addTab(doc.getName(), doc);
        tabPane.setSelectedComponent(doc);
    }

    private  void open(){
        JFileChooser chooser = new JFileChooser("./");
        int returnValue = chooser.showOpenDialog(this);
        if(returnValue == JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            WordDocument doc = new WordDocument(file, new JTextArea(), false);
            tabPane.add(doc.getName(), doc);
            try{
                BufferedReader br = new BufferedReader(new FileReader("file1.txt"));
                String line;
                while((line = br.readLine())!=null){
                    doc.getText().append(line+"\n");
                }
                br.close();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            tabPane.setSelectedComponent(doc);
        }

    }

    private void save(){
        WordDocument doc = (WordDocument)tabPane.getSelectedComponent();
        if(doc.isNewDocument())
            saveAs();
        else
            doc.save();
    }

    private void saveAs(){
        JFileChooser chooser = new JFileChooser("./");
        int returnValue = chooser.showSaveDialog(this);
        if(returnValue == JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            WordDocument doc = (WordDocument)tabPane.getSelectedComponent();
            if(doc.save(file.getAbsolutePath()))
                tabPane.setTitleAt(tabPane.getSelectedIndex(), file.getName());
        }
    }

    void exitSafely(){
        for(int i=0;i<tabPane.getTabCount();i++){
            WordDocument doc = (WordDocument)tabPane.getComponentAt(i);
            if(doc.isUnsaved()){
                int returnValue = JOptionPane.showConfirmDialog(null, "Do you wish to exit?\n There are still unsaved changes", "!!Warning!!", JOptionPane.YES_NO_OPTION);
                if (returnValue == JOptionPane.YES_OPTION) {
                    dispose();
                }
                else{
                    return;
                }
            }
        }
        dispose();
    }
}
