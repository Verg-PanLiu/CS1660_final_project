import javax.swing.*;    
import java.awt.event.*;
import java.awt.*;    
import java.io.*;
import java.util.*; 
import java.util.zip.*;   
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.google.api.services.storage.model.StorageObject;
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
public class RunningUI extends JFrame{
    JLabel label1;
    JLabel label2;
    boolean visible;
    int nextUI;

    RunningUI(){
        label1 = new JLabel("The inverted index is constructing ...");  
        label1.setBounds(80,175, 800,60); 
        label1.setFont(new Font("Courier", Font.BOLD,35)); 
        label2 = new JLabel("Please wait ...");  
        label2.setBounds(300,240, 360,60); 
        label2.setFont(new Font("Courier", Font.BOLD,40)); 
		add(label1);		
		add(label2);

		setTitle("Search Engine");
        setSize(900, 800);
        setLocationRelativeTo(null);
		setLayout(null);    
        setVisible(false);
        visible = false;
        nextUI = 0;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
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
        Blob blob = storage.get(BlobId.of("cs1660-input-data", "output_V2/part-r-00000"));
        blob.downloadTo(Paths.get("./output_V2.txt"));  
    }
    public void mergeResults() throws IOException{
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./my-project-cs1660-48ad423a0277.json"));
        HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(credentials);

        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        StorageObject target = new StorageObject();
        target.setBucket("cs1660-input-data");
        target.setName("output.txt");

        Storage.ComposeRequest.Builder requestBuilder = new Storage.ComposeRequest.Builder();

        Page<Blob> blobs = storage.list("cs1660-input-data", BlobListOption.currentDirectory(), BlobListOption.prefix("output/"));
        Iterator<Blob> iterator = blobs.iterateAll().iterator();
        ArrayList<String> blobList = new ArrayList<>();
        ArrayList<BlobId> blobIDs = new ArrayList<>();
        while(iterator.hasNext()) {
            Blob currBlob = iterator.next();
            blobIDs.add(currBlob.getBlobId());
            String name = currBlob.getName();
            if (!name.equals("output/_SUCCESS")){
                blobList.add(name);
            }
        }
        requestBuilder.addSource(blobList);
    
        requestBuilder.setTarget(BlobInfo.newBuilder("cs1660-input-data", "output.txt").build());
        Storage.ComposeRequest request = requestBuilder.build();
        storage.compose(request);
     
    }
}