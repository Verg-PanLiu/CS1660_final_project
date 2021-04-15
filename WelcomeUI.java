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
public class WelcomeUI extends JFrame implements ActionListener{
	JLabel label; 
	JLabel selectFiles;
    JButton button1;
    JButton button2;
    JRadioButton hugo;    
    JRadioButton shakespeare;
    JRadioButton tolstoy;
    String s;
    ArrayList<String> filePaths = new ArrayList<String>();
    ArrayList<String> filenames = new ArrayList<String>();
    ArrayList<String> fileSelected = new ArrayList<String>();
    boolean visible;
    int nextUI;
    WelcomeUI(){
		label = new JLabel("Load My Engine");  
		label.setBounds(300,175, 400,60); 
		label.setFont(new Font("Courier", Font.BOLD,40));

        hugo =new JRadioButton("Hugo");    
        shakespeare =new JRadioButton("Shakespeare");  
        tolstoy =new JRadioButton("Tolstoy");

        
        hugo.setBounds(50,200,180,30);
        hugo.setFont(new Font("Arial", Font.BOLD, 25));    
        shakespeare.setBounds(50,240,240,30);  
        shakespeare.setFont(new Font("Arial", Font.BOLD, 25));  
        tolstoy.setBounds(50,280,180,30);
        tolstoy.setFont(new Font("Arial", Font.BOLD, 25));

        button1 = new JButton("Choose Files");
        button1.setBounds(350,275,200, 60);  
        button1.setFont(new Font("Arial", Font.PLAIN, 25));

        selectFiles = new JLabel();
        selectFiles.setBounds(300,340, 600,100);
        selectFiles.setFont(new Font("Arial", Font.BOLD,20));

        button2 = new JButton("Load Engine");
        button2.setBounds(290,450,300, 60);  
        button2.setFont(new Font("Arial", Font.PLAIN, 25));

        button1.addActionListener(this);  
        button2.addActionListener(this);

		add(button1);
		add(button2);		
		add(label);
		add(selectFiles);

        add(hugo);       
        add(shakespeare);
        add(tolstoy);

		setTitle("Search Engine");
        setSize(900, 800);
        setLocationRelativeTo(null); 
		setLayout(null);   
        setVisible(true);
        visible = true;
        nextUI = 0;   
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     		
    }

    public void actionPerformed(java.awt.event.ActionEvent e){    
		if(e.getSource() == button1){
            if(hugo.isSelected()){
                filePaths.add("./Data/Hugo.tar.gz");
                filenames.add("Hugo.tar.gz");           
            }
            if(shakespeare.isSelected()){
                filePaths.add("./Data/shakespeare.tar.gz");
                filenames.add("shakespeare.tar.gz");               
            } 
            if(tolstoy.isSelected()){
                filePaths.add("./Data/Tolstoy.tar.gz");
                filenames.add("Tolstoy.tar.gz");                
            } 

	        StringBuilder str = new StringBuilder("");
	        str.append("<html>");        
	        try{                  
				for (int j = 0; j < filenames.size(); j++) {
					String filename = filenames.get(j);
                    str.append(filename + "<br>");
				}
				str.append("</html>");  
				s = str.toString();               	
                selectFiles.setText(s); 	
	        }		          
	        catch (Exception ex) {ex.printStackTrace();}                    
	    }

		if(e.getSource() == button2){
			try{
				extract(filePaths);
				selectFiles.setText("");
                uploadFiles();
                invertedIndex();                                 
				setVisible(false);
				visible = false;
                nextUI = 2;	
			}
			catch (Exception ex) {ex.printStackTrace();}           
		}

	}

    private void invertedIndex() throws IOException{
        String fileUrl = "gs://cs1660-input-data/input/*/*" ;
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./my-project-cs1660-48ad423a0277.json"));
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

        Dataproc dataproc = new Dataproc.Builder(new NetHttpTransport(),new JacksonFactory(), requestInitializer).build();

        HadoopJob hJob = new HadoopJob();
        hJob.setMainJarFileUri("gs://cs1660-input-data/InvertedIndex.jar");
        hJob.setArgs(ImmutableList.of("InvertedIndex",fileUrl,"gs://cs1660-input-data/output")); 
        dataproc.projects().regions().jobs().submit("my-project-cs1660" , "us-east1", new SubmitJobRequest()
                .setJob(new Job()
                    .setPlacement(new JobPlacement()
                        .setClusterName("cluster-5a79"))
                            .setHadoopJob(hJob)))
                                .execute();
        HadoopJob hJob2 = new HadoopJob();
        hJob2.setMainJarFileUri("gs://cs1660-input-data/InvertedIndexV2.jar");
        hJob2.setArgs(ImmutableList.of("InvertedIndexV2",fileUrl,"gs://cs1660-input-data/output_V2")); 
        dataproc.projects().regions().jobs().submit("my-project-cs1660" , "us-east1", new SubmitJobRequest()
                .setJob(new Job()
                    .setPlacement(new JobPlacement()
                        .setClusterName("cluster-5a79"))
                            .setHadoopJob(hJob2)))
                                .execute();                                   
        System.out.println("Inverted indicies were constructed successfully!");   
    }

    private void uploadFiles () throws IOException{
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./my-project-cs1660-48ad423a0277.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).setProjectId("my-project-cs1660").build().getService();

        for (int i = 0; i < fileSelected.size();i++){
            String fileName;
            if (fileSelected.get(i).charAt(0)!= 'H' && fileSelected.get(i).charAt(0)!= 'T'){
                fileName = "input/shakespeare/" + fileSelected.get(i);
            }else{
                fileName = "input/" + fileSelected.get(i);
            }
            BlobId blobId = BlobId.of("cs1660-input-data", fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
            storage.create(blobInfo, Files.readAllBytes(Paths.get("./input/" + fileSelected.get(i))));
        }
        System.out.println("Selected files were successfully uploaded!");
    }
    
    private void extract (ArrayList<String> filePaths) throws IOException {
        String COMPRESSED_FILE;
        String DESTINATION_PATH;
        File destFile;
		for (int i = 0; i < filePaths.size(); i++){
		    COMPRESSED_FILE = filePaths.get(i);
		    DESTINATION_PATH = "./input";
            destFile = new File(DESTINATION_PATH);
            //System.out.println(" tar entry- " + destFile.getName()); 
            unTarFile(COMPRESSED_FILE, destFile);       				
		}    
    }

    private void unTarFile(String tarFile, File destFile) {
        TarArchiveInputStream tis = null;
        try {
            FileInputStream fis = new FileInputStream(tarFile);
            // .gz
            GZIPInputStream gzipInputStream = new GZIPInputStream(new BufferedInputStream(fis));
            //.tar.gz
            tis = new TarArchiveInputStream(gzipInputStream);
            TarArchiveEntry tarEntry = null;
            while ((tarEntry = tis.getNextTarEntry()) != null) {
                
                if(tarEntry.isDirectory()){
                    continue;
                }else {
                    // In case entry is for file ensure parent directory is in place
                    // and write file content to Output Stream

                    fileSelected.add(tarEntry.getName());
                    //System.out.println(tarEntry.getName());
                    File outputFile = new File(destFile + File.separator + tarEntry.getName());
                    outputFile.getParentFile().mkdirs();
                    IOUtils.copy(tis, new FileOutputStream(outputFile));
                }
            }
        }catch(IOException ex) {
            System.out.println("Error while untarring a file- " + ex.getMessage());
        }finally {
            if(tis != null) {
                try {
                    tis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}