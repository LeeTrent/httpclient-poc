package trent.lee.companyinfo;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CompanyInfoServlet
 */
@WebServlet(name = "CompanyInfoServlet", urlPatterns = {"/index.jsp", "/index.html", "/CompanyInfoServlet"})
public class CompanyInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CompanyInfoServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		this.processRequest(request, response);;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
        Logger.getLogger(CompanyInfoServlet.class.getName()).log(Level.INFO, "[CompanyInfoServlet][processRequest] => BEGIN");	
               
        ServletInputStream inStream = request.getInputStream();
        byte[] requestPayload = IOUtils.toByteArray(inStream);
        
        
        /////////////////////////////////////////////////////////////
        // NO AUTHENTICATION REQUIRED
        ////////////////////////////////////////////////////////////
        //CompanyInfoHttpClient client = new CompanyInfoHttpClient();
        
        /////////////////////////////////////////////////////////////
        // REQUIRES AUTHENTICATION 
        ////////////////////////////////////////////////////////////        
        SecureCompanyInfoHttpClient client = new SecureCompanyInfoHttpClient("http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso");
             
        response.setContentType("text/xml;charset=UTF-8");
        client.processRequest(requestPayload, response.getOutputStream());
        
        Logger.getLogger(CompanyInfoServlet.class.getName()).log(Level.INFO, "[CompanyInfoServlet][processRequest] => END");    	
    }	
}
