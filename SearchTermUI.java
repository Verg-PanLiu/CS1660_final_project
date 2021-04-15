import javax.swing.*;   
import java.awt.event.*;
import java.awt.*;    
import java.io.*;
import java.util.*; 
import java.util.zip.*;   

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class SearchTermUI extends JFrame implements ActionListener{
	JLabel label1;
    JTextField textField;
    JButton button1;
    boolean visible;
    int nextUI;
    String term;

    SearchTermUI(){
		label1 = new JLabel("Enter Your Search Term");  
		label1.setFont(new Font("Courier", Font.BOLD,35)); 
        label1.setBounds(240,170, 500,40); 


        textField = new JTextField("");
        textField.setBounds(240,260,400, 40);
        textField.setFont(new Font("Courier", Font.BOLD,20));
        TextPrompt textprompt = new TextPrompt("Type Your Search Here ...", textField); 

        button1 = new JButton("Search");
        button1.setBounds(300,400,300, 60);   
        button1.setFont(new Font("Arial", Font.PLAIN, 25));
        
        button1.addActionListener(this);  

		add(button1);
		add(textField);		
		add(label1);


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
		if(e.getSource() == button1){
            term = textField.getText();
            setVisible(false);
            visible = false;
            nextUI = 7;   
	    }
    }

    public String getTerm(){
        return term;
    }
}