package webapp;

import com.google.gson.Gson;
import webapp.model.Algorithm;
import webapp.model.District;
import webapp.model.HibernateUtil;
import webapp.model.UserInput;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "MainPage")
public class MainPage extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json = request.getParameter("userInput");
        Gson gson = new Gson();
        UserInput input = gson.fromJson(json, UserInput.class);
        Algorithm algorithm = Algorithm.getInstance();
        String jsonResult = null;
        try {
            jsonResult = algorithm.handler(input);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        response.setContentType("text/plain");
        response.getWriter().write(jsonResult);
    }
}
