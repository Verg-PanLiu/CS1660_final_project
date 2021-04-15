import javax.swing.*;    
import java.awt.event.*;
import java.awt.*;    
import java.io.*;
import java.util.*; 
import java.util.zip.*;   

public class UserInterface{
    WelcomeUI welcome;
    LoadUI load;
    SearchTermUI search;
    TopNUI top;
    ResultsUI_TopN results_topN;
    ResultsUI_Search results_search;
    RunningUI run;
    UserInterface(){	 
        welcome = new WelcomeUI();	  
        load = new LoadUI();
        search = new SearchTermUI(); 
        top = new TopNUI();  
        results_topN = new ResultsUI_TopN();
        results_search = new ResultsUI_Search(); 
        run = new RunningUI();       
    }
    
	public static void main(String[] args) throws IOException {
		UserInterface UI = new UserInterface();
		while (true){
			if (UI.welcome.visible == true){
				System.out.print("");	 
			}

            if (UI.welcome.visible == false && UI.welcome.nextUI == 2){
                System.out.print("");
				UI.run.setVisible(true);
				UI.run.visible = true;
                UI.welcome.nextUI = 0;
                UI.run.checkFinished();
                UI.run.mergeResults();
                UI.run.setVisible(false);
                UI.run.visible = false;
                UI.run.nextUI = 3;
			}

            if (UI.run.visible == false && UI.run.nextUI == 3){
                System.out.print("");
                UI.load.setVisible(true);
                UI.load.visible = true;
                UI.run.nextUI = 0;
            }

            if (UI.load.visible == false && UI.load.nextUI == 4){
                System.out.print("");
                UI.top.setVisible(true);
                UI.top.visible = true;
                UI.load.nextUI = 0;
            }

            if (UI.load.visible == false && UI.load.nextUI == 6){
                System.out.print("");
                UI.search.setVisible(true);
                UI.search.visible = true;
                UI.load.nextUI = 0;
            }

            if (UI.search.visible == false && UI.search.nextUI == 7){
                System.out.print("");
                String s = UI.search.getTerm();
                UI.results_search.setTerm(s);                
                UI.results_search.displayResult();
                UI.results_search.setVisible(true);
                UI.results_search.visible = true;
                UI.search.nextUI = 0;
            } 

            if (UI.results_search.visible == false && UI.results_search.nextUI == 3){
                System.out.print("");
                UI.load.setVisible(true);
                UI.load.visible = true;
                UI.results_search.nextUI = 0;
            }    

            if (UI.top.visible == false && UI.top.nextUI == 5){
                System.out.print("");
                UI.results_topN.displayResult();
                UI.results_topN.setVisible(true);
                UI.results_topN.visible = true;
                UI.top.nextUI = 0;
            } 

            if (UI.results_topN.visible == false && UI.results_topN.nextUI == 3){
                System.out.print("");
                UI.load.setVisible(true);
                UI.load.visible = true;
                UI.results_topN.nextUI = 0;
            }                 			
		}		
	}

}