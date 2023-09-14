package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import DAO.DAOImpl.PatientDAOImpl;
import domain.Patient;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jpa.EntityManagerHelper;

@WebServlet(name="patientinfo",
        urlPatterns={"/PatientInfo"})
public class PatientInfo extends HttpServlet {
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");


        PrintWriter out = response.getWriter();

        EntityManager em = EntityManagerHelper.getEntityManager();

        PatientDAOImpl patientManager = new PatientDAOImpl(em);

        patientManager.save(new Patient(request.getParameter("name"), request.getParameter("tel"),request.getParameter("numsecsoc")));

        List<Patient> lp = patientManager.getAll();

        out.println("<HTML>\n<BODY>\n<ul>");
        lp.forEach(patient -> {
            out.println("<li>" +
                    patient.getName() + patient.getTel() + patient.getNumSecSoc() +
                    "</li>");
        });
        out.println("</ul></BODY></HTML>");
    }
}
