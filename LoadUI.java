import javax.swing.*;    
import java.awt.event.*;
import java.awt.*;    
import java.io.*;
import java.util.*; 
import java.util.zip.*;   

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

public class LoadUI extends JFrame implements ActionListener{
	JLabel label1;
    JLabel label2;
    JLabel label3;
    JLabel label4;
    JButton button1;
    JButton button2;
    boolean visible;
    int nextUI;

    LoadUI(){
		label1 = new JLabel("Engine was loaded");  
		label1.setBounds(290,50, 320,40); 
		label1.setFont(new Font("Courier", Font.BOLD,30)); 

        label2 = new JLabel("&");  
        label2.setBounds(430,100, 40,40); 
        label2.setFont(new Font("Courier", Font.BOLD,40)); 

        label3 = new JLabel("Inverted index were constructed successfully!");  
        label3.setBounds(50, 165, 800,40); 
        label3.setFont(new Font("Courier", Font.BOLD,30)); 

        label4 = new JLabel("Please Select Action");  
        label4.setBounds(230,300, 500,60); 
        label4.setFont(new Font("Courier", Font.BOLD,40)); 

        button1 = new JButton("Search for Term");
        button1.setBounds(300,400,300, 60);  
        button1.setFont(new Font("Arial", Font.PLAIN, 25));

        button2 = new JButton("Top-N");
        button2.setBounds(300,500,300, 60);  
        button2.setFont(new Font("Arial", Font.PLAIN, 25));

        button1.addActionListener(this);  
        button2.addActionListener(this);

		add(button1);
		add(button2);		
		add(label1);
        add(label2);
        add(label3);
        add(label4);


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
             setVisible(false);
             visible = false;
             nextUI = 6;   
	    }

		if(e.getSource() == button2){
             setVisible(false);
             visible = false;
             nextUI = 4;   
	    }
    }
}