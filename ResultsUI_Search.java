import javax.swing.*;   
import java.awt.event.*;
import java.awt.*;
import java.awt.font.TextAttribute;    
import java.io.*;
import java.util.*; 
import java.util.zip.*;   

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class ResultsUI_Search extends JFrame implements ActionListener{
    JButton button;
    JLabel label;
    JLabel label2;
    JTextArea textArea;
    boolean visible;
    int nextUI;
    String term;

    ResultsUI_Search(){
        button = new JButton("Go Back To Search");
        Font bold = new Font("Courier",Font.BOLD, 25);
        button.setFont(bold); 
        button.setForeground(Color.black);
        button.setBounds(550,50, 300,40); 
        button.addActionListener(this);

        label = new JLabel();
        Font bold1 = new Font("Courier",Font.BOLD, 20);  
        label.setFont(bold1); 
        label.setForeground(Color.black);
        label.setBounds(50,50, 500,45); 

        label2 = new JLabel();
        label2.setFont(bold1); 
        label2.setForeground(Color.black);
        label2.setBounds(50,100, 500,45); 

        Font bold2 = new Font("Courier",Font.BOLD, 15);
        textArea = new JTextArea(10, 10);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBounds(50,200, 800,680);
        textArea.setFont(bold2);
        
        add(label);
        add(label2);
        add(textArea);      
        add(button);

        setTitle("Search Engine");
        setSize(900, 900);
        setLocationRelativeTo(null);
        setLayout(null);    
        setVisible(false);
        visible = false;
        nextUI = 0;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);             
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {    
		if(e.getSource() == button){  
            setVisible(false);
            visible = false;
            nextUI = 3;  
	    }
    }
    public void displayResult() throws IOException{
        try{
            long startTime = System.currentTimeMillis();
            Scanner input = new Scanner(new File("output_V2.txt"));
            textArea.setText("");
            while(input.hasNextLine()) {
                String curr = input.nextLine();
                String [] s = curr.split("\t");
                if (s[0].equals(term)){
                    textArea.append(s[0] + "\n" + "Exist in:" + "\n"); 
                    String [] filenames = s[1].split(", ");
                    for (int i = 0; i < filenames.length; i++){
                        String name = filenames[i].split("=")[0] + "=" + filenames[i].split("=")[1];
                        if (i == 0)
                            textArea.append("Doc"+ (i+1) + ": " + name.substring(2, name.length()-1) + "\t");
                        else
                            textArea.append("Doc"+ (i+1) + ": " + name.substring(1, name.length()-1) + "\t");
                        if (i % 2 == 1)
                            textArea.append("\n");
                    }
                    break;                      
                }       
            }
            long endTime = System.currentTimeMillis();
            long timeElapsed = endTime - startTime;
            label2.setText("Your search was executed in " + timeElapsed + " ms");
        }
        catch (Exception ex) {ex.printStackTrace();}  
        textArea.repaint();
    }

    public void setTerm(String s){
        term = s;
        label.setText("You searched for the term: " + term);
    }

}