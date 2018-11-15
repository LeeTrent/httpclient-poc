package trent.lee.companyinfo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletInputStream;

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
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		this.processRequest(request, response);;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet CompanyInfoServlet</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet CompanyInfoServlet at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        }
    	
        Logger.getLogger(CompanyInfoServlet.class.getName()).log(Level.INFO, "[CompanyInfoServlet][processRequest] => BEGIN");	
               
        ServletInputStream inStream = request.getInputStream();
        byte[] requestPayload = IOUtils.toByteArray(inStream);
        
        CompanyInfoHttpClient client = new CompanyInfoHttpClient();
        response.setContentType("text/xml;charset=UTF-8");
        client.processRequest(requestPayload, response.getOutputStream());
        
       
    	
//        response.setContentType("text/html;charset=UTF-8");
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet CompanyInfoServlet</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet CompanyInfoServlet at " + request.getContextPath() + "</h1>");
//            out.println("<h2>Company Info!</h2>");
//            
//            byte[] b = IOUtils.toByteArray(inStream);
//            String myString = new String(b);
//            
//            Logger.getLogger(CompanyInfoServlet.class.getName()).log(Level.INFO, "[CompanyInfoServlet][processRequest] => BEGIN myString:");
//            Logger.getLogger(CompanyInfoServlet.class.getName()).log(Level.INFO, myString);
//            Logger.getLogger(CompanyInfoServlet.class.getName()).log(Level.INFO, "[CompanyInfoServlet][processRequest] => END myString: ");
//            
//            out.println("<h3>myString:</h3>");
//            out.println("<p>");
//            out.println(myString);
//            out.println("</p>");
//            
//            out.println("<ul>");
//            for ( int ii = 0; ii < b.length; ii++ ) {
//                out.println("<li>");
//                out.println(b[ii]);
//                out.println("</li>");
//            }
//            out.println("</ul>");
//
//            out.println("</body>");
//            out.println("</html>");
//        }    	
    	
        Logger.getLogger(CompanyInfoServlet.class.getName()).log(Level.INFO, "[CompanyInfoServlet][processRequest] => END");
    	
    }	
}
