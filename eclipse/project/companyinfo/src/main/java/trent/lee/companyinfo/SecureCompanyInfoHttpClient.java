package trent.lee.companyinfo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

public class SecureCompanyInfoHttpClient {
	
	private String webServiceUrl;
	private String userName;
	private String password;
	
	public SecureCompanyInfoHttpClient(String wsUrl) {
		this.webServiceUrl = wsUrl;
	}	
	
	public SecureCompanyInfoHttpClient(String wsUrl, String user, String pw) {
		this.webServiceUrl = wsUrl;
		this.userName = user;
		this.password = pw;
	}
	
    public void processRequest(byte[] requestPayload, OutputStream outStream){
    	
    	Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.INFO, "[CompanyInfoHttpClient][processRequest] => BEGIN");		
        Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.INFO, "[CompanyInfoHttpClient][processRequest] => BEGIN requestPayload:");
        Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.INFO, new String(requestPayload));
        Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.INFO, "[CompanyInfoHttpClient][processRequest] => END requestPayload: ");
        
        
        ////////////////////////////////////////////////////////
        // Instantiate HttpClient
        ////////////////////////////////////////////////////////
        //Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.INFO, "[CompanyInfoHttpClient][processRequest] => Instantiating HttpClient ... ");
        //HttpClient client = HttpClientBuilder.create().build();
        
        CloseableHttpClient httpClient = null;
        
        if ( this.userName != null && this.userName.isEmpty() == false
        		&& this.password != null && this.password.isEmpty() == false) {
        	httpClient = this.buildBasicAuthClient();
        } else {
        	try {
        		httpClient = this.buildCertAuthClient();
			} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | CertificateException
					| IOException exc) {
				Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.SEVERE, null, exc);
				exc.printStackTrace();
				return;
			}
        }
        
        ////////////////////////////////////////////////////////
        // Create and Prepare Post Request
        ////////////////////////////////////////////////////////
        //Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.INFO, "[CompanyInfoHttpClient][processRequest] => Creating and Preparing HttpPost ... ");
        
        HttpPost httpPost = new HttpPost(this.webServiceUrl);       
        httpPost.setHeader("Content-type", "text/xml; charset=UTF-8");
        try {
        	httpPost.setEntity(new StringEntity(new String(requestPayload)));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
      
        ////////////////////////////////////////////////////////
        // Execute Post Request
        ////////////////////////////////////////////////////////			
        //Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.INFO, "[CompanyInfoHttpClient][processRequest] => Executing Post Request ... ");      
        //HttpResponse response = null;
        
        CloseableHttpResponse httpResponse = null;
        try {
        	httpResponse = httpClient.execute(httpPost);
        } catch (IOException ex) {
            Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        ////////////////////////////////////////////////////////
        // Process Response (BufferedReader)
        ////////////////////////////////////////////////////////	      
        Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.INFO, "[CompanyInfoHttpClient][processRequest] => Response Code: " + httpResponse.getStatusLine().getStatusCode());
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        } catch (IOException ex) {
            Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.SEVERE, null, ex);
            return;
        } catch (UnsupportedOperationException ex) {
            Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        ////////////////////////////////////////////////////////
        // Process Response (BufferedWriter)
        ////////////////////////////////////////////////////////	         
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
        
        /////////////////////////////////////////////////////////
        // Process Response (read each line of response payload)
        /////////////////////////////////////////////////////////              
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
            	//System.out.println(line);
                writer.write(line);
                writer.newLine();
            }
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        /////////////////////////////////////////////////////////////////////////////////////////////
        // CLEAN UP (close HttpResponse)
        /////////////////////////////////////////////////////////////////////////////////////////////
        if ( httpResponse != null ) {
        	try {
				httpResponse.close();
			} catch (IOException exc) {
				Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.SEVERE, null, exc);
				exc.printStackTrace();
			}
        }
        
        /////////////////////////////////////////////////////////////////////////////////////////////
        // CLEAN UP (close HttpClient)
        /////////////////////////////////////////////////////////////////////////////////////////////        
        if ( httpClient != null ) {
        	try {
				httpClient.close();
			} catch (IOException exc) {
				Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.SEVERE, null, exc);
				exc.printStackTrace();
			}
        }
        
        Logger.getLogger(CompanyInfoHttpClient.class.getName()).log(Level.INFO, "[CompanyInfoHttpClient][processRequest] => END");
     }	
    
     private CloseableHttpClient buildBasicAuthClient() {
    	 
    	 CredentialsProvider credsProvider = new BasicCredentialsProvider();
         
    	 credsProvider.setCredentials(
		         new AuthScope(this.webServiceUrl, 443),
		         new UsernamePasswordCredentials(this.userName, this.password));
         
         CloseableHttpClient httpClient = HttpClients.custom()
                 .setDefaultCredentialsProvider(credsProvider)
                 .build();    
         
         return httpClient;
     }
         
     private CloseableHttpClient buildCertAuthClient() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
    	 
         // Trust own CA and all self-signed certs
    	 SSLContext sslcontext = null;
       	 sslcontext = SSLContexts.custom()
		         .loadTrustMaterial(new File("my.keystore"), "nopassword".toCharArray(),
		                 new TrustSelfSignedStrategy())
		         .build();
         
         // Allow TLSv1 protocol only
         SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                 sslcontext,
                 new String[] { "TLSv1" },
                 null,
                 SSLConnectionSocketFactory.getDefaultHostnameVerifier());  
         
         CloseableHttpClient httpClient = HttpClients.custom()
                 .setSSLSocketFactory(sslsf)
                 .build();         
         
    	 return httpClient;
     }    
}
