import javax.swing.*;    
import java.awt.event.*;
import java.awt.*;    
import java.io.*;
import java.util.*; 
import java.util.zip.*;   
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;


import com.google.cloud.storage.*;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.auth.Credentials;
import com.google.cloud.ServiceOptions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.google.api.services.storage.model.StorageObject;
import com.google.api.services.dataproc.*;
import com.google.api.services.dataproc.model.*;
import com.google.api.services.dataproc.model.HadoopJob;
import com.google.api.services.dataproc.model.Job;
import com.google.api.services.dataproc.model.JobPlacement;
import com.google.api.services.dataproc.model.SubmitJobRequest;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.json.JSONObject;
import org.json.JSONArray;
import com.google.gson.Gson;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage.*;
import com.google.api.gax.paging.Page;
import java.nio.file.Paths;
public class TopNUI extends JFrame implements ActionListener{
	JLabel label1;
    JTextField textField;
    JButton button1;
    boolean visible;
    int nextUI;

    TopNUI(){
		label1 = new JLabel("Enter Your N Value");  
		label1.setFont(new Font("Courier", Font.BOLD,35)); 
        label1.setBounds(270,170, 400,40); 


        textField = new JTextField("");
        textField.setBounds(240,260,400, 40);
        textField.setFont(new Font("Courier", Font.BOLD,20));
        TextPrompt textprompt = new TextPrompt("Type Your N", textField); 

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
            int N = Integer.parseInt(textField.getText());
            try{
                topN(N);
                checkFinished();
                Thread.sleep(1000);
                setVisible(false);
                visible = false;
                nextUI = 5;
            }                 
            catch (Exception ex) {ex.printStackTrace();}                    
	    }
    }

    public void checkFinished() throws IOException{
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./my-project-cs1660-48ad423a0277.json"));
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);
        Dataproc dataproc = new Dataproc.Builder(new NetHttpTransport(),new JacksonFactory(), requestInitializer).build();
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();        
        while(true){
            String jsonStr = new Gson().toJson(dataproc.projects().regions().jobs().list("my-project-cs1660", "us-east1").execute());
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray c = jsonObj.getJSONArray("jobs");
            int count = 0;
            for (int i = 0 ; i < c.length(); i++) {
                JSONObject obj = c.getJSONObject(i);
                if (obj.get("status").toString().charAt(10) == 'D'){
                    count++;
                }
            }
            if (count == c.length()){
                break;
            }
        }
        Blob blob = storage.get(BlobId.of("cs1660-input-data", "output_topN/part-r-00000"));
        blob.downloadTo(Paths.get("./output.txt"));        
    }

    private void topN(int N) throws IOException{
        System.out.println(N);
        String fileUrl = "gs://cs1660-input-data/output.txt";
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./my-project-cs1660-48ad423a0277.json"));
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

        Dataproc dataproc = new Dataproc.Builder(new NetHttpTransport(),new JacksonFactory(), requestInitializer).build();

        HadoopJob hJob = new HadoopJob();
        hJob.setMainJarFileUri("gs://cs1660-input-data/TopN.jar");
        hJob.setArgs(ImmutableList.of("-D", "myValue=" + N, fileUrl,"gs://cs1660-input-data/output_topN")); 
        dataproc.projects().regions().jobs().submit("my-project-cs1660" , "us-east1", new SubmitJobRequest()
                .setJob(new Job()
                    .setPlacement(new JobPlacement()
                        .setClusterName("cluster-5a79"))
                            .setHadoopJob(hJob)))
                                .execute();           
    }
  
}