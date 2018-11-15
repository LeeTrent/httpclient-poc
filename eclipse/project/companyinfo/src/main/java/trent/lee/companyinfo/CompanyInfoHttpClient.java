package trent.lee.companyinfo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class CompanyInfoHttpClient {

	private static final String url = "http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso";
	
    public void execute(byte[] input, OutputStream outStream){
        
        ////////////////////////////////////////////////////////
        // Instantiate HttpClient
        ////////////////////////////////////////////////////////
        HttpClient client = HttpClientBuilder.create().build();
            
        ////////////////////////////////////////////////////////
        // Create Post Request
        ////////////////////////////////////////////////////////
        HttpPost post = new HttpPost(CompanyInfoHttpClient.url);       
        post.setHeader("Content-type", "text/xml; charset=UTF-8");
        try {
            post.setEntity(new StringEntity(new String(input)));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
      
        ////////////////////////////////////////////////////////
        // Execute Post Request
        ////////////////////////////////////////////////////////			
        HttpResponse response = null;
        try {
            response = client.execute(post);
        } catch (IOException ex) {
            Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        ////////////////////////////////////////////////////////
        // Process Response
        ////////////////////////////////////////////////////////	      
        Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.INFO, 
                            "Response Code : " + response.getStatusLine().getStatusCode());
        
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        } catch (IOException ex) {
            Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (UnsupportedOperationException ex) {
            Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        BufferedWriter writer = null;
        try {
            ////////////////////////////////////////////////////////
            // Write Response Pay Load to OutputStream
            ////////////////////////////////////////////////////////
            writer = new BufferedWriter(new OutputStreamWriter(outStream, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
     }	
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
