
/**
 * Tilviksbreyturnar eru eingöngu breytur sem eru eingöngu notaðar í útlitsstillingu
 * og einnig geymslu gagna sem verið er að vinna með.
 * Má keyra Textaritill.java með því að:
 * Textaritill textaRitill = new Textaritill(); opnar textaritilinn sem er tómt og óháð ákveðini srká
 *eða
 Textaritill textaRitill = new Textaritill(String s ) þar sem s er "path"
 eða hlekkur á skrá sem við viljum opna.
 * @author Natanel Demissew Ketema, ndk1
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.text.AttributeSet.ColorAttribute;

import java.awt.image.*;
import net.miginfocom.swing.MigLayout;
import java.beans.*;
import java.io.*;
import java.nio.*;
import java.util.Stack;
import java.awt.event.*;
import java.util.*;

public class Textaritill extends JFrame {
    private JMenuBar theMenuBar = new JMenuBar(); 
    private JMenu file = new JMenu("File");
    private JMenu themesAndTools = new JMenu("Themes and Tools");
    private final JMenuItem searchAndReplace = new JMenuItem("Search and Replace - Ctrl + R");
    private final JMenu chooseTheme = new JMenu("Choose Theme");
    private final JMenu backgroundTheme = new JMenu("Background");
    private final JMenuItem textTheme = new JMenuItem("Text");
    private final JMenuItem textAreaTheme = new JMenuItem("Text Area");
    private final JMenuItem newFile = new JMenuItem("New - Ctrl + N");
    private final JMenuItem save = new JMenuItem("Save - Ctrl + S");
    private final JMenuItem saveAs = new JMenuItem("Save As - Ctrl + A");
    private final JMenuItem open = new JMenuItem("Open Ctrl + O");
    private final JMenuItem newTab = new JMenuItem("New Tab - Ctrl + T");
    private final JMenuItem darkTheme = new JMenuItem("Dark Theme - Ctrl + D");
    private final JMenuItem lightTheme = new JMenuItem("Light Theme - Ctrl + L");
    private Color[] colors = new Color[5];
    private Color lightTextColor;
    private TextIO textIO = new TextIO();
    private final JTabbedPane tPane;
    private Color darkBlu = new Color(33,35,43).brighter();
    private boolean isDark = false;

    /**
     * Fastayrðing gagna: 
     * theMenuBar(JMenuBar) - heldur utan um "file" valblaðið og einnig "Themes and Tools"
     * Kemur efst í utlitsstillingu.
     * file(JMenu) - inniheldur hlutina "save", "save as", "open" ,"new" og "new tab"
     * themesAndTools(JMenu) - inniheldur "Search and Replace" hlut og "Choose Theme hlut"
     * searchAndReplace(JMenuItem) - takki sem gerir notanda kleift að leita að orðum og umbreyta þeim
     * choseTheme(JMenu) - Inniheldur "Background" og "Text" valblað og hlut
     * backgroundTheme(JMenu) - inniheldur hluti sem eru notuð til að skipta um þema "Dark theme" og "Light Theme"
     * textTheme(JMenuItem) - Hlutur sem leyfir notanda að velja lit á textan sinn
     * textAreTheme(JMenuItem) - Hlutur sem leyfir notanda a velja lit á bakgrunn textasvæðisins
     * newFile(JMenuItem) - Leyfir notenda að opna tómt textasvæði í núverandi flipu
     * save(JMenuItem) - leyfir notenda að vista texta í núverandi flipu
     * saveAs(JMenuItem) - leyfir notenda að vista núverandi texta í skrá 
     * open(JMenuItem) - leyfir notenda að opna skrá í núverandi flipu
     * newTab(JMenuItem) - leyfir notenda að opna nýja flipu
     * darkTheme(JMenuItem) - breytir í dekkri þema
     * lightTheme(JMenuItem) - breytir í ljósa þemu
     * colors(Colors[]) - geymir liti
     * lightTextColor - geymir lit fyrir texta(light theme)
     * textIO(TextIO) - klasi sem inniheldur helstu aðferðirnar sem ritillinn notar
     * tPane(JTabbedPane) - tabFlipan sem ritillinn mun nota
     */

    /**
     * Upphaf stillir valblaðsrönd, valblöð,
     * textaritlinum sjálfum og flettutakka. Allt sem kemur í viðmótinu
     * Hlustar á save,open, new og save As, þema og fl
     * setur keyListener á íhluti JFrame
     */
    public Textaritill() {
        tPane = textIO.getTabbedPane();
        setLayout(new MigLayout());
        file.add(newFile);
        file.add(save);
        file.add(saveAs);
        file.add(open);
        file.add(newTab);
        themesAndTools.add(searchAndReplace);
        backgroundTheme.add(darkTheme);
        backgroundTheme.add(lightTheme);
        chooseTheme.add(backgroundTheme);
        chooseTheme.add(textTheme);
        chooseTheme.add(textAreaTheme);
        themesAndTools.add(chooseTheme);
        textThemeListener(textTheme);
        areaThemeListener(textAreaTheme);
        searchAndReplaceListener(searchAndReplace);
        newTabListener(newTab);
        newFileListener(newFile);
        saveListener(save);
        saveAsListener(saveAs);
        openListener(open);
        darkThemeListener(darkTheme);
        lightThemeListener(lightTheme);
        theMenuBar.add(file);
        colors[0] = theMenuBar.getBackground();
        colors[1] = file.getBackground();
        colors[2] = Color.white;
        colors[3] = save.getBackground();
        colors[4] = getBackground();
        lightTextColor = save.getForeground();
        theMenuBar.add(themesAndTools);
        setJMenuBar(theMenuBar);
        textIO.createNewTab();      
        tabbedPaneListener(tPane);
        tPane.setPreferredSize(new Dimension(400,400));
        getContentPane().add(tPane,"wrap,push,grow");
        setTitle("Text Editor");
        windowListener();
        pack();
        EventQueue.invokeLater(()->{setVisible(true);});
        //Hlustar á lyklaborð og framkvæmir aðferðina sem passar.
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
          .addKeyEventDispatcher(new KeyEventDispatcher() {
              @Override
              public boolean dispatchKeyEvent(KeyEvent evt) {
                if (evt.isControlDown()) {
                    if(evt.getKeyCode() == KeyEvent.VK_S && evt.getID() == KeyEvent.KEY_PRESSED)
                        textIO.save(textIO.getSelPathsAtIndex(textIO.getTabbedPane().getSelectedIndex()));
                    if(evt.isShiftDown()){
                        if(evt.getKeyCode() == KeyEvent.VK_A && evt.getID() == KeyEvent.KEY_PRESSED) textIO.saveAs();
                    }
                    if(evt.getKeyCode() == KeyEvent.VK_O && evt.getID() == KeyEvent.KEY_PRESSED) textIO.open();
                    if(evt.getKeyCode() == KeyEvent.VK_N && evt.getID() == KeyEvent.KEY_PRESSED) newFile();
                    if(evt.getKeyCode() == KeyEvent.VK_T && evt.getID() == KeyEvent.KEY_PRESSED) textIO.createNewTab();
                    if(evt.getKeyCode() == KeyEvent.VK_R && evt.getID() == KeyEvent.KEY_PRESSED) new ReplaceFrame(textIO);
                    if(evt.getKeyCode() == KeyEvent.VK_D && evt.getID() == KeyEvent.KEY_PRESSED) darkTheme();
                    if(evt.getKeyCode() == KeyEvent.VK_L && evt.getID() == KeyEvent.KEY_PRESSED) lightTheme();;
                } 
                return false;
              }
        });
    }

    /**
     * kallar á Textaritil með path.
     * path má vera hlekkur sem endar á "File" eða "File.txt"
     * @param path stengur af skrá sem við viljum opna við ræsingu tölvunar
     */
    public Textaritill(String path){
        this();
        textIO.readFile(path);
    }
    /**
     * Fyrir: 
     * Eftir: Umbreytir útlit forritsins í dekkri þemu.
     * Breytir background á flestum í hlutum forritsins
     */
    private void darkTheme(){
        Color white = new Color(235,225,216);
        getContentPane().setBackground(darkBlu);
        file.setOpaque(true);
        file.setForeground(white);
        file.setBackground(darkBlu);
         theMenuBar.setOpaque(true);
        theMenuBar.setBackground(darkBlu);
        themesAndTools.setOpaque(true);
        themesAndTools.setForeground(white);
        themesAndTools.setBackground(darkBlu);
        searchAndReplace.setOpaque(true);
        searchAndReplace.setBackground(darkBlu);
        searchAndReplace.setForeground(white);
        chooseTheme.setOpaque(true);
        chooseTheme.setBackground(darkBlu);
        chooseTheme.setForeground(white);
        backgroundTheme.setOpaque(true);
        backgroundTheme.setBackground(darkBlu);
        backgroundTheme.setForeground(white);
        textTheme.setOpaque(true);
        textTheme.setBackground(darkBlu);
        textTheme.setForeground(white);
        newFile.setOpaque(true);
        newFile.setBackground(darkBlu);
        newFile.setForeground(white);
        save.setOpaque(true);
        save.setBackground(darkBlu);
        save.setForeground(white);
        saveAs.setOpaque(true);
        saveAs.setBackground(darkBlu);
        saveAs.setForeground(white);
        open.setOpaque(true);
        open.setBackground(darkBlu);
        open.setForeground(white);
        newTab.setOpaque(true);
        newTab.setBackground(darkBlu);
        newTab.setForeground(white);
        tPane.setOpaque(true);
        tPane.setBackground(darkBlu);
        darkTheme.setOpaque(true);
        darkTheme.setBackground(darkBlu);
        darkTheme.setForeground(white);
        lightTheme.setOpaque(true);
        lightTheme.setBackground(darkBlu);
        lightTheme.setForeground(white);
        textIO.getCurrentTextArea().setBackground(darkBlu);
        textIO.getCurrentTextArea().setForeground(white);
        textIO.setTextColor(white);
        textIO.setTextAreaColor(darkBlu);
        isDark = true;
    }
    /**
     * Gerir það sama og darkTheme(), umbreytir í light theme
     */
    private void lightTheme(){
        getContentPane().setBackground(colors[4]);
        file.setOpaque(true);
        file.setForeground(lightTextColor);
        file.setBackground(colors[3]);
        theMenuBar.setOpaque(false);
        theMenuBar.setBackground(colors[0]);
        themesAndTools.setOpaque(false);
        themesAndTools.setForeground(lightTextColor);
        themesAndTools.setBackground(colors[1]);
        searchAndReplace.setOpaque(false);
        searchAndReplace.setBackground(colors[3]);
        searchAndReplace.setForeground(lightTextColor);
        chooseTheme.setOpaque(false);
        chooseTheme.setBackground(colors[3]);
        chooseTheme.setForeground(lightTextColor);
        backgroundTheme.setOpaque(true);
        backgroundTheme.setBackground(colors[3]);
        backgroundTheme.setForeground(lightTextColor);
        textTheme.setOpaque(false);
        textTheme.setBackground(colors[3]);
        textTheme.setForeground(lightTextColor);
        newFile.setOpaque(false);
        newFile.setBackground(colors[3]);
        newFile.setForeground(lightTextColor);
        save.setOpaque(false);
        save.setBackground(colors[3]);
        save.setForeground(lightTextColor);
        saveAs.setOpaque(false);
        saveAs.setBackground(colors[3]);
        saveAs.setForeground(lightTextColor);
        open.setOpaque(false);
        open.setBackground(colors[3]);
        open.setForeground(lightTextColor);
        newTab.setOpaque(false);
        newTab.setBackground(colors[3]);
        newTab.setForeground(lightTextColor);
        tPane.setOpaque(false);
        tPane.setBackground(colors[4]);
        darkTheme.setBackground(colors[3]);
        darkTheme.setForeground(lightTextColor);
        lightTheme.setOpaque(false);
        lightTheme.setBackground(colors[3]);
        lightTheme.setForeground(lightTextColor);
        textIO.getCurrentTextArea().setBackground(colors[2]);
        textIO.getCurrentTextArea().setForeground(Color.black);
        textIO.setTextColor(Color.black);
        textIO.setTextAreaColor(colors[2]);
        isDark = false;
    }
    /**
     * Keyrir darkTheme() aðferðina til að umbreyta yfir í "dark theme"
     * @param darkTheme "Dark Theme" hluturinn í Background valblað
     */
    private void darkThemeListener(JMenuItem darkTheme){
        darkTheme.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                darkTheme();
            }
        });
    }
    /**
     * Keyrir lightTheme() til að umbreyta utlit
     * @param lightTheme "Light Theme" hluturinn í Background valblað
     */
    private void lightThemeListener(JMenuItem lightTheme){
        lightTheme.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                lightTheme();
            }
        });
    }

    /**Hlustar á "New" til að vísa eða opna tóma skrá
     * @param newFile menuitem fyrir "New"
     */
    private void newFileListener(JMenuItem newFile){
        newFile.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                newFile();
            }
        });
    }
    /**
     * Athugar hvort óhætt sé að hreinsa núverandi flipu.
     * Ef já hreinsar allt saman annars spyr notenda hvað ætti að vera um
     * gögnin í flipanum
     */
    private void newFile(){
        int prompt = -1;
        if(textIO.isUnsaved()){
            prompt = textIO.savePrompt();
        }
        if(Math.abs(prompt) == 1 || prompt == 0){
            textIO.setSelPathsAtIndex(textIO.getTabbedPane().getSelectedIndex(),null);
            tPane.setTitleAt(textIO.getTabbedPane().getSelectedIndex(),"untitled");
            textIO.getCurrentTextArea().setText("");
            textIO.setTextDataAtIndex(textIO.getTabbedPane().getSelectedIndex(), "");
        }
    }
    /**
     * byr til nýja flipu þegar smellt er á
     * @param newTab "New Tab" hlutur í valblaðinu
     */
    private void newTabListener(JMenuItem newTab){
        newTab.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                textIO.createNewTab();
            }
        });
    }
    /**
     * Listar á Search and Replace hlutinn í valblaðinu
     * og opnar dialog glugga.
     */
    private void searchAndReplaceListener(JMenuItem sr){
        sr.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ReplaceFrame rf = !isDark ? new ReplaceFrame(textIO) : 
                new ReplaceFrame(textIO, darkBlu, Color.white.darker());
            }
        });
    }
   /**
    * Hlustar á hvort smellt sé á "Text" Hlutnum
    * Skiptir um lit á texta í textArea eftir val notendans
    * @param textTheme "Text" hlutur í themes
    */
    private void textThemeListener(JMenuItem textTheme){
        textTheme.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.out.println("theme");
                JColorChooser jc = new JColorChooser();
                Object obj = jc;
                int val = JOptionPane.showConfirmDialog(null,jc,"Color Chooser", JOptionPane.OK_CANCEL_OPTION);
                if(val == JOptionPane.YES_OPTION){
                    for(int i = 0; i < tPane.getTabCount();i++){
                        JTextArea area = textIO.getTextAreaAtIndex(i);
                        area.setForeground(jc.getColor());
                    }
                }
                textIO.setTextColor(jc.getColor());
            }
        });
    }
    private void areaThemeListener(JMenuItem textAreaTheme){
        textAreaTheme.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JColorChooser jc = new JColorChooser();
                Object obj = jc;
                int val = JOptionPane.showConfirmDialog(null,jc,"Color Chooser", JOptionPane.OK_CANCEL_OPTION);
                if(val == JOptionPane.YES_OPTION){
                    for(int i = 0; i < tPane.getTabCount();i++){
                        JTextArea area = textIO.getTextAreaAtIndex(i);
                        area.setBackground(jc.getColor());
                    }
                }
                textIO.setTextColor(jc.getColor());
            }
        });
    }
    //Hlustar á TabbedPane þegar skipt er á milli flippa 
    /**
     * Hlustar á breytingar við tabbedPane.
     * changeListener keyrist þegar eitthvað gerist við tabbedPane.
     * Aðallega notað til þess að sækja textArea af núverandi flipu
     * @param tp tabbedPane() tilvik í klasanum textIO
     */
    private void tabbedPaneListener(JTabbedPane tp){
            tp.addChangeListener(new ChangeListener(){
                public void stateChanged(ChangeEvent e) {
                    JViewport vp =(JViewport) ((JScrollPane)tPane.getSelectedComponent()).getViewport();
                    textIO.setCurrentTextArea((JTextArea)vp.getView());
                    String selPath = textIO.getSelPathsAtIndex(textIO.getTabbedPane().getSelectedIndex());
                    textIO.setTitle(selPath.substring(selPath.lastIndexOf("\\")+1));
                    if(tPane.getTabCount() >= textIO.getTextData().length-3) textIO.resizeArrays(tPane.getTabCount());
                }
            });
    }
    /** Hlustar á open og spyr hvort notandi vill vista
     * ef breytt skrá er að ræða. Annars opnar JFileChooser
     * og les in skrána í textArea
     * @param open MenuItem fyrir "Open"
     */
    private void openListener(JMenuItem open) {
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textIO.open();
            }
        });
    }
    
    
    /**
     * Hlustar á glugganum þegar smellt er á "close" þá viljum við vita hvort núverandi
     * texti í thTextArea sé vistuð eða ekki. Ef skráin er ekki vistuð köllum á closeProcedure()
     * Annars lokum við forritinu og hættum 
     */
    private void windowListener() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                boolean unsaved = false;
                for(int i = 0; i < tPane.getTabCount(); i++){
                    JTextArea area = textIO.getTextAreaAtIndex(i);
                    if(!area.getText().equals(textIO.getTextDataAtIndex(i))) unsaved = true;
                }
                if(unsaved){
                    closingProcedure();
                }else{
                    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                }
            }
        });
    }
   
    

    /**
     * sama og savePrompt() nema lokar á forritunu þegar smellt á "nei" 
     */
    private void closingProcedure() {
            textIO.setTitle(textIO.getTabbedPane().getTitleAt(textIO.getTabbedPane().getSelectedIndex()));
            int input = JOptionPane.showConfirmDialog(null, "Do you want to save changes to " + textIO.getTitle());
            if(input == JOptionPane.YES_OPTION){
               textIO.getSelPathsAtIndex(textIO.getTabbedPane().getSelectedIndex());
               textIO.setTextDataAtIndex(textIO.getTabbedPane().getSelectedIndex(), textIO.getCurrentTextArea().getText());
               textIO.save(textIO.getSelPathsAtIndex(textIO.getTabbedPane().getSelectedIndex()));
               setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            }else if(input == JOptionPane.NO_OPTION){
                setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            }else{
                setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            }
    }
     

    /**
     * Hlustar á takkanum "Save As" í "File" til að kalla á saveAs()
     * @param saveAs "Hlutur" í Valblað "File"
     */
    private void saveAsListener(JMenuItem saveAs) {
        saveAs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textIO.saveAs();
            }
        });
    }
     /**Hlustar eftir "Save" og kallar á save()
     * 
     * @param save MenuItem "Save" 
     */
    private void saveListener(JMenuItem save) {
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textIO.save(textIO.getSelPathsAtIndex(textIO.getTabbedPane().getSelectedIndex()));
            }
        });
    }


    public static void main(String[] args) {
        Runnable r = () -> {
            if(args.length == 0){// keyrist ef ekkert er sett inn í args
                Textaritill tr = new Textaritill();
            }else{
                Textaritill tr = new Textaritill(args[0]); //keyrist ef path í args[0]
            }
        };
        SwingUtilities.invokeLater(r);
    }
}