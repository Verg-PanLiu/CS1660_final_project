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

public class ResultsUI_TopN extends JFrame implements ActionListener{
    JButton button;
    JLabel label;
    JTextArea textArea;
    boolean visible;
    int nextUI;


    ResultsUI_TopN(){
        button = new JButton("Go Back To Search");
        Font bold = new Font("Courier",Font.BOLD, 25);
        button.setFont(bold); 
        button.setForeground(Color.black);
        button.setBounds(550,50, 300,40); 
        button.addActionListener(this);

        label = new JLabel("Top-N Frequent Terms");
        Font bold1 = new Font("Courier",Font.BOLD, 35);  
        label.setFont(bold1); 
        label.setForeground(Color.black);
        label.setBounds(200,130, 500,45); 

        Font bold2 = new Font("Courier",Font.BOLD, 20);
        textArea = new JTextArea(10, 10);
        textArea.setBounds(200,200, 500,500);
        textArea.setFont(bold2);

        add(label);
        add(textArea);      
        add(button);

        setTitle("Search Engine");
        setSize(900, 800);
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
        int counter = 0;
        try{
            Scanner input = new Scanner(new File("output.txt"));
            while(input.hasNextLine()) {
                counter++;
                String curr = input.nextLine();
                textArea.append(curr + "\n");
            }            
        }
        catch (Exception ex) {ex.printStackTrace();}  
        textArea.repaint();
    }
}