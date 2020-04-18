/**
 * Inniheldur helstu aðferðir,virkni og gögn sem Textaritill.java mun nota
 * Aðallega notað til að skipta forritinu upp í nokkra klasa fyrir læsileika forritsins.
 * Inniheldur aðferðir til að vista, sækja, opna skrá. Einnig geym
 * @author Natanel Demissew
 */
import java.awt.Color;
import java.awt.Font;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import javax.swing.*;

public class TextIO{
    private String[] textData = new String[10];//geymir textan sem forritið sækir. 
    private String[] selPaths = new String[10];// geymir hlekkin á skráni
    private JTextArea currentTextArea;//núverandi textasvæði
    private String title = "untitled";//titill núverandiflipu
    private JTabbedPane tabbedPane;
    private Color textColor;//geymir litin á textan
    private Color textAreaColor;
    /**
     * Fastayrðing gagna:
     * textData(String[]) - geymir upphafsstöðu allaflipa(til að athuga breyting texta flipunar)
     * selPaths(String[]) - geymir streng sem hlekk á flipu
     * currentTextAre(JTextArea) - núverandi textasvæði sem notandi vinnur með
     * title(String) - geymir titil á nuverandi flipu
     * tabbedPane(JTabbedPane) - tabbedPane hlutur sem við notum til að búa til og nota flipur
     * textColor(color) - inniheldur lit fyrir textan
     * textAreaColor(Color) - inniheldur lit fyrir textasvæðið 
     */

     /**
      * upphafstillir flipuna, textColor,textAreaColor, og
       fyllir á textData og SelPaths sem tóman streng
      */
    protected TextIO(){
        tabbedPane = new JTabbedPane();
        textColor = Color.BLACK;
        textAreaColor = Color.white;
        Arrays.fill(textData,"");
        Arrays.fill(selPaths,"");
    }
    /**
     * stillir á litinn fyrir textArea
     */
    protected void setTextAreaColor(Color color){
        this.textAreaColor = color;
    }
    /**
     * leið fyrir Textaritil að vinna með tabbedPane
     * @return tabbedPane tilviksbreytan
     */
    protected JTabbedPane getTabbedPane(){
        return this.tabbedPane;
    }
    /**
     * Leið fyrir Textaritil að vinna með textColor
     * @param color textColor tilviksbreyta. Litur fyrir texta útlit
     */
    protected void setTextColor(Color color){
        this.textColor = color;
    }
    /**
     * Leið fyrir textaritil að stilla á titil á flipuna
     * @param title titill á flipu
     */
    protected void setTitle(String title){
        this.title = title;
    }
    /**
     * Leið fyrir Textaritil að sækja titilin
     */
    protected String getTitle(){
        return this.title;
    }
    /**
     * skilar tilviksfylkið sem geymir 
     * upphafstexta flipana
     * @return fylki af öllum textum 
     */
    protected String[] getTextData(){
        return this.textData;
    }
    /**
     * skilar upphafstexta ákveðna flipu
     * @param idx vísi á tabflipu
     * @return texti tabflipunar
     */
    protected String getTextDataAtIndex(int idx){
        return this.textData[idx];
    }
    /**
     * skilar hlekkinn sem flipan í sæti "idx" er að nota
     * @param idx vísi á tab flipu
     * @return hlekk á skjal flipunar
     */
    protected String getSelPathsAtIndex(int idx){
        return this.selPaths[idx];
    }
    /**
     * gerir textaritilnum kleift að vinna með textasvæði
     * sem verið er að nota á stunduni
     * @return núverandi textasvæði
     */
    protected JTextArea getCurrentTextArea(){
        return currentTextArea;
    }
    /**
     * setur hlekkinn(s) í sæti idx fyrir idx-ta flipuna
     * @param idx vísi á tab flipu
     * @param s hlekkurinn fyrir tab flipu í sæti "idx"
     */
    protected void setSelPathsAtIndex(int idx, String s){
        this.selPaths[idx] = s;
    }
   /**
    * setur upphafstextan(ný lesinn skrá) á tabflipu í idx-ta sæti
    * @param idx vísi í tab flipu
    * @param s texti 
    */
    protected void setTextDataAtIndex(int idx, String s){
        this.textData[idx] = s;
    }
    /**
     * stillir á textasvæðið sem við viljum vinna með
     * 
     * @param textArea textasvæðið sem við viljum vinna með
     */
    protected void setCurrentTextArea(JTextArea textArea){
        this.currentTextArea = textArea;
    }

    /**
     * Les in texta úr File og birtar á textArea
     * Ef FileNotFound/IOexception prentar villu
     * @param file type File sem tekið er inn til að lesa úr
     */
    protected void readFile(String path) {
        File file = path.indexOf(".") < 0 ? new File(path+".txt") : new File(path);
        setTitleAndSelPath(file);
        currentTextArea.setText("");
        try {
            BufferedReader in;
            in = new BufferedReader(new FileReader(file));
            String line = in.readLine();
            while (line != null) {
                currentTextArea.setText(currentTextArea.getText() + line);
                line = in.readLine();
            }
            in.close();
            textData[tabbedPane.getSelectedIndex()] = currentTextArea.getText();
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found");
        } catch (IOException ex) {
            System.out.println("IOException");
        }
    }
    /**
     * Ef path er null eða ekki til þá opnast JFileChooser bjóða notanda að vísta
     * Annars uppfærir skrána í "path" með textann sem er í textArea
     *@param path "path" eða hlekkur af tagi streng sem vísar í skrá
     */
    protected void save(String path) {
        if(textData[tabbedPane.getSelectedIndex()].equals(currentTextArea.getText())) return;
        if(path == null || !new File(path).exists()){
            saveAs();
        }else{
            try (PrintWriter out = new PrintWriter(path)) {
                out.println(currentTextArea.getText());
            }
            catch(IOException e){
                System.out.println("Cannot save to path");
            }
        }
    }
    /**
     * tvöfaldar stærð fylkisins til að tryggja 
     * að hann verður aldrei fullur
     * @param size stærð á fylkinu
     */
    protected void resizeArrays(int size){
        String[] tempTextArr = textData;
        String[] tempSelArr = selPaths;
        String[] newTextArr = new String[size*2];
        String[] newSelArr = new String[size*2];
        Arrays.fill(newSelArr,"");
        Arrays.fill(newTextArr,"");
        for(int i = 0; i < tempTextArr.length;i++){
            newTextArr[i] = tempTextArr[i];
            newSelArr[i] = tempSelArr[i];
        }
        selPaths = newSelArr;
        textData = newTextArr;
    }
    /**
     * 
     * @return true ef text != textArea.getText()(skjalið ekki vistað) annars false
     */
    protected boolean isUnsaved() {
        return !textData[tabbedPane.getSelectedIndex()].equals(currentTextArea.getText());
    }
    /**
     * Opnar JFileChooser() vístar núverandi skjal í skrá sem notandi velur
     */
    protected void saveAs(){
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setAcceptAllFileFilterUsed(true);
        fc.setDialogTitle("Please Select a Folder");
        int val = fc.showSaveDialog(fc);
        if (val == JFileChooser.APPROVE_OPTION) {
            setTitleAndSelPath(fc.getSelectedFile());
            String path = selPaths[tabbedPane.getSelectedIndex()].substring(0, selPaths[tabbedPane.getSelectedIndex()].lastIndexOf("\\"));
            File myFile = new File(path,title+".txt");
            try {
                Files.write(Paths.get(myFile.toURI()),currentTextArea.getText().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            textData[tabbedPane.getSelectedIndex()] = currentTextArea.getText();
        }
    }
    /**
     * Fyrir:
     * Eftir: Býr til tab flipu í tabbedPane() 
     * sem inniheldur textasvæði sem hægt er aðskrifa í
     */
    protected void createNewTab(){
        JScrollPane scroll = new JScrollPane( //setur lárétt og lóðrétt scroll
        ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JTextArea area = new JTextArea();
        area.setForeground(textColor);
        area.setBackground(textAreaColor);
        area.setFont(new Font("Arial Black", Font.PLAIN, 15));
        scroll.getViewport().add(area);
        tabbedPane.addTab("untitled",scroll);
        JViewport vp =(JViewport) ((JScrollPane)tabbedPane.getSelectedComponent()).getViewport();
        setCurrentTextArea((JTextArea)vp.getView());
    }
    /**
     * sækir og skilar textasvæðið í sæti idx
     * @param idx vísi í tab flipu
     * @return textasvæði 
     */
    protected JTextArea getTextAreaAtIndex(int idx){
        JScrollPane sp = (JScrollPane)tabbedPane.getComponent(idx);
        JViewport vp =(JViewport)sp.getViewport();
        JTextArea area = (JTextArea)vp.getView();
        return area;
    }
    /**
     * Tekur inn skrá og stillir selPath sem streng af path(hlekk í skrá).
     * Til 
     * @param file File sem við erum að lesa inn
     */
    protected void setTitleAndSelPath(File file){
        selPaths[tabbedPane.getSelectedIndex()] = file.getPath();
        title = selPaths[tabbedPane.getSelectedIndex()].substring(selPaths[tabbedPane.getSelectedIndex()].lastIndexOf("\\")+1);
        tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), title);
    }

     /**
     * Fyrir: 
     * Eftir: Spyr hvað notandi vill gera við núverandi texta í textArea.
     * Keyrist þegar óvistuð skrá er að ræða
     */
    protected int savePrompt() {
        int input = JOptionPane.showConfirmDialog(
            null, "Do you want to save changes to " + 
            tabbedPane.getTitleAt(getTabbedPane().getSelectedIndex()));
        if(input == JOptionPane.YES_OPTION){
           save(getSelPathsAtIndex(getTabbedPane().getSelectedIndex()));
           setTextDataAtIndex(getTabbedPane().getSelectedIndex(), getCurrentTextArea().getText());
        }
        return input;
    }
    /**
     * Ef óhætt er að opna skrá þá 
     * býr til eintak af JFileChooser og les inn skrá í núverandi flipu 
     * 
     */
    protected void open(){
        int prompt = -1;
        if(isUnsaved()){
            prompt = savePrompt();
        }
        if(Math.abs(prompt) == 1 || prompt == 0){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(true);
            fileChooser.setDialogTitle("Please Select a File");
            int val = fileChooser.showOpenDialog(fileChooser);
            if (val == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                readFile(path);
                setTitleAndSelPath(fileChooser.getSelectedFile());
            }
        }
    }
}