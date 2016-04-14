

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SomeServlet
 */



@WebServlet("/GetUserServlet")
public class GetUserServlet extends HttpServlet {
   private static final long serialVersionUID = 1L;

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

       String userName = request.getParameter("userName").trim();
       if(userName == null || "".equals(userName)){
           userName = "Guest";
       }
        
       String greetings = "Hello " + userName;
        
       response.setContentType("text/plain");
       request.setAttribute("todo", "10");
       
       request.getRequestDispatcher("/indextry.jsp").forward(request, response);
       //response.getWriter().write(greetings);
   }

}
