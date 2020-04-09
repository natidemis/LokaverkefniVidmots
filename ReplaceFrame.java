/**
 * Þessi klasi er notaður með textaritilinn til að 
 * leyfa notanda að leita að orðum í textasvæðinu og einnig
 * umbreyta þeim.
 * @author Natanel Demissew
 */
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.text.Highlighter.HighlightPainter;
import java.awt.*;
import net.miginfocom.swing.MigLayout;
import java.awt.Container;
import java.awt.event.*;
import java.util.Arrays;
import java.util.regex.Pattern;

public class ReplaceFrame extends JDialog implements ActionListener {
    private JButton find = new JButton("Find");//Takki til að leita
    private JButton replace = new JButton("Replace"); //Takki til að umbreyta
    private JTextField replacetext = new JTextField("", 20);//textasvæði sem notandi vill breyta í
    private JTextField findtext = new JTextField("", 20);//textasvæði sem notandi vill leita eftir
    private TextIO textIO; //geymir inntak af textIO sem Textaritillinn er að vinna með
    private String data = "";//gögnin í núverandi textasvæði
    private Color myColor = getBackground();
    JPanel panel = new JPanel();
    /**
     * Fastayrðing gagna:
     * find(JButton) -  ef smellt er á vinnur með findtext(JTextfield)
     * replace(JButton) - ef smellt er á, vinnur með findtext og replacetext til að umbreyta
     * orðin í textasvæðinu
     * replacetext(JTextField) - texti sem notandi vill breyta í
     * findtext - texti sem notandi vill leita að eða breyta 
     * textIO - TextIO hlutur sem Textaritillinn er að vinna með
     * data - Strengur sem er í textasvæðinu
     * panel - heldur utan um textasvæðin og takkana.
     */

     /**
      * Upphafstillir data með því að sækja textan í núverandi svæði.
      * upphafstillir stærð og íhlutir gluggans.
      * @param textIO TextIO hlutur sem textaritillinn er að vinna með
      */
    ReplaceFrame(TextIO textIO) {
        data = textIO.getCurrentTextArea().getText();
        this.textIO = textIO;
        setModal(true);
        setSize(400, 100);
        setResizable(false);
        setTitle("Search And Replace");
        panel.add(findtext);
        panel.add(find);
        panel.add(replacetext);
        panel.add(replace);
        Container cpane = getContentPane();
        cpane.add(panel, "Center");
        find.addActionListener(this);
        replace.addActionListener(this);
        windowListener();
        setVisible(true);
    }
    /**
     * Gerir það sama og ReplaceFrame(TextIO) fyrir "dark Theme" 
     * 
     * @param textIO textIO klasi sem ritillinn vinnur með
     * @param themeColor þemalitur fyrir backgrunn
     * @param textColor litur fyrir texta
     */
    protected ReplaceFrame(TextIO textIO, Color themeColor, Color textColor){
        panel.setBackground(themeColor);
        replacetext.setBackground(themeColor.darker());
        replacetext.setForeground(textColor);
        findtext.setBackground(themeColor.darker());
        findtext.setForeground(textColor);
        find.setBackground(themeColor.darker());
        find.setForeground(textColor);
        replace.setBackground(themeColor.darker());
        replace.setForeground(textColor);
        data = textIO.getCurrentTextArea().getText();
        this.textIO = textIO;
        setModal(true);
        setSize(400, 100);
        setResizable(false);
        setTitle("Search And Replace");
        panel.add(findtext);
        panel.add(find);
        panel.add(replacetext);
        panel.add(replace);
        Container cpane = getContentPane();
        cpane.add(panel, "Center");
        find.addActionListener(this);
        replace.addActionListener(this);
        windowListener();
        setVisible(true);
    }
    /**
     * Fyrir:
     * Eftir: keyrir find() eða replace() eftir því hvaða takka var smellt á 
     * framkvæmist þegar notandi er buinn að uppfylla skilyrði í leit eða umbreytni
     */
    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals("Find") && !findtext.getText().equals(""))find();
        if ((evt.getActionCommand().equals("Replace") && !findtext.getText().equals("")) 
         && !replacetext.getText().equals("")){
            replace();
         }
    }
    /**
     * Fyrir:
     * Eftir: Leitar af öllum orðum/strengjum
     * sem uppfylla textan sem finnst í findtext og leitar að þeim í tilviksbreytuni "data"
     * og auðkennir þeim í gulum lit
     */
    private void find() {
        String input = findtext.getText();
        int index = data.indexOf(input);
        String startIdx = "";
        while (index >= 0) {
            startIdx += Integer.toString(index)+" ";
            index = data.indexOf(input, index + 1);
        }
        String[] indices = startIdx.split(" ");
        HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.yellow);
        for (int i = 0; i < indices.length; i++) {
            int idx = Integer.parseInt(indices[i]);
            System.out.println(idx);
            try {
                textIO.getCurrentTextArea().getHighlighter().addHighlight(idx,
                idx + input.length(), painter);
            } catch (BadLocationException e) {
                e.printStackTrace();
                throw new Error("Failed to highlight");
            }
        }              
    }
    /**
     * Fyrir: 
     * Eftir: Þegar lokað er á glugganum fjarlægir það auðkenningu textana
     */
    private void windowListener() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                textIO.getCurrentTextArea().getHighlighter().removeAllHighlights();
            }
        });
    }
    /**
     * Fyrir:
     * Eftir:Ef findtext og replacetext eru ekki tóm þá 
     * getum við leitað á öll eintök af textanum í "findtext" og umbreytum þeim í textan
     * sem er í replacetext
     */
    private void replace(){
        String input = findtext.getText();
        String replaceWith = replacetext.getText();
        data = data.replaceAll(Pattern.quote(input), replaceWith);
        textIO.getCurrentTextArea().setText(data);
    }
    
}