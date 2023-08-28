package servlet.servlet;

import Classe.Chambre;
import Classe.ReserActCha;
import Classe.Utilisateur;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    boolean exists;
    String message ="";
    ObjectOutputStream oos;
    ObjectInputStream ois;
    ArrayList<Chambre> listChambres;
    ArrayList<Chambre> listChambresReservee;
    ArrayList<ReserActCha> listResa;
    String aPayer;


    public void init() {
        try {
            InetAddress ip = InetAddress.getByName("localhost");

            Socket s = new Socket(ip, 5056);
            System.out.println(s);
            oos = new ObjectOutputStream(s.getOutputStream());
            ois = new ObjectInputStream(s.getInputStream());
            listChambres = new ArrayList<>();
            listChambresReservee = new ArrayList<>();
            listResa = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException {

        /* String newVisit = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL,Locale.FRANCE).format(new java.util.Date());
        newVisit = newVisit.replace(' ', '.'); // + "\n" for Internet Explorer
        Cookie lastVisitCookie = new Cookie ("lastVisit", newVisit);
        response.addCookie(lastVisitCookie);
        String lastVisit = null;
        Cookie[] tabCookies = request.getCookies();
        if(tabCookies != null)
        {
            for (Cookie tabCookie : tabCookies)
            {
                if (tabCookie.getName().equals("lastVisit"))
                    lastVisit = tabCookie.getValue();
            }
        } */

        HttpSession session = request.getSession(true);
        String action = request.getParameter("action");

        switch (action) {

            case "Log in" :
                oos.writeObject("LOGIN");
                exists = false;
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.set_nomUser(request.getParameter("username"));
                utilisateur.set_password(request.getParameter("password"));

                oos.writeObject(utilisateur);
                String reponse = (String) ois.readObject();

                if(reponse.equals("OK"))
                {
                    message="";
                    session.setAttribute("logon.isDone", request.getParameter("username"));
                    response.sendRedirect("JSPInit.jsp");
                }
                else {
                    message="Erreur login !";
                    PrintWriter out = response.getWriter();
                    out.println("<html><body>");
                    out.println("<h1>" + message + "</h1>");
                    out.println("</body></html>");
                }
                break;


            case "Sign in" :
            break;

            case "recherche" :
                System.out.println("lancer recherche");
                listChambres.removeAll(listChambres);
                oos.writeObject("BROOM");
                ReserActCha reservationChambre = new ReserActCha();
                reservationChambre.set_categorie(request.getParameter("categorie"));
                reservationChambre.set_typeCha(request.getParameter("typeChambre"));
                reservationChambre.set_nbNuits(Integer.parseInt(request.getParameter("nbNuits")));
                reservationChambre.set_date(request.getParameter("dateArrivee"));
                reservationChambre.set_persRef(request.getParameter("persRef"));
                oos.writeObject(reservationChambre);
                int compteur = 0;
                while (true) {
                    Chambre chambre = (Chambre) ois.readObject();
                    if (chambre == null)
                        break;
                    else {
                        listChambres.add(chambre);
                        compteur++;
                    }
                }
                if(compteur == 0) {
                    oos.writeObject("Aucune");
                }
                else {
                    oos.writeObject("OK");
                }
                System.out.println(listChambres);
                session.setAttribute("chambres", listChambres);
                response.sendRedirect("JSPInit.jsp");
                break;

            case "valider":
                //System.out.println(Integer.parseInt(request.getParameter(request.getParameter("ChambreReservee"))));
                if(request.getParameter("ChambreReservee") != null) {
                    int id = Integer.parseInt(request.getParameter(request.getParameter("ChambreReservee")));
                    Chambre chambreAResa = new Chambre();
                    for (int i=0; i<listChambres.size();i++) {
                        if(listChambres.get(i).get_numeroChambre() == id) {
                            chambreAResa.set_numeroChambre(listChambres.get(i).get_numeroChambre());
                            chambreAResa.set_prixHTVA(listChambres.get(i).get_prixHTVA());
                            listChambresReservee.add(chambreAResa);
                            listChambres.remove(i);
                            break;
                        }
                    }
                    oos.writeObject(chambreAResa);
                    String confirmation = (String) ois.readObject();
                    if (confirmation.equals("NOK")) {
                        JOptionPane.showMessageDialog(null, "Erreur Réservation", "Alert", JOptionPane.WARNING_MESSAGE);
                    } else {
                        ReserActCha reservation = (ReserActCha) ois.readObject();
                        listResa.add(reservation);
                        System.out.println(reservation.get_id());
                        System.out.println(reservation.get_numChambre());
                        System.out.println(reservation.get_prixCha());
                        JOptionPane.showMessageDialog(null, "Réservation acceptée", "Alert", JOptionPane.WARNING_MESSAGE);
                    }
                }
                session.setAttribute("chambres", listChambres);
                session.setAttribute("listeChambreReservee",listChambresReservee);
                session.setAttribute("listeResa",listResa);
                response.sendRedirect("JSPInit.jsp");
                break;

            case "Log out" :
                session.setAttribute("logon.isDone", null);
                session.setAttribute("login.target", null);
                oos.writeObject("LOGOUT");
                response.sendRedirect("index.jsp");
                break;

            case "Caddie" :
                response.sendRedirect("JSPCaddie.jsp");
                break;
            case "Payer" :
                int numChambre = Integer.parseInt(request.getParameter(request.getParameter("ChambrePayee")));

                for(int i=0;i<listChambresReservee.size();i++) {
                    if(listChambresReservee.get(i).get_numeroChambre()==numChambre) {//on cherche numchambre pour tel date => une seule possible
                        for (int j = 0; j < listResa.size(); j++) { //on prend la reservation correspondante pour avoir l'id de la reservation
                            if (listChambresReservee.get(i).get_numeroChambre() == listResa.get(j).get_numChambre() && listChambresReservee.get(i).get_prixHTVA() == listResa.get(j).get_prixCha()) {
                                aPayer = String.valueOf(listResa.get(j).get_id());
                            }
                        }
                    }
                }
                System.out.println(aPayer);
                response.sendRedirect("JSPPay.jsp");
                break;
            case "Vider la liste" :

                listChambres.removeAll(listChambres);
                listChambresReservee.removeAll(listChambresReservee);

                for (int i=0;i<listResa.size();i++) {
                    oos.writeObject("CROOM");
                    oos.writeObject(String.valueOf(listResa.get(i).get_id()));
                    String confirmation = (String) ois.readObject();
                    System.out.println(confirmation);
                    //peut creer des erreurs si la suppression de la reservation n'est pas faite dans la bd
                    //a faire plus tard si le temps
                }

                listResa.removeAll(listResa);

                session.setAttribute("chambres", listChambres);
                session.setAttribute("listeChambreReservee",listChambresReservee);
                session.setAttribute("listeResa",listResa);
                response.sendRedirect("JSPCaddie.jsp");
                break;
            case "accepter" :
                oos.writeObject("PROOMWEB");
                oos.writeObject(aPayer);
                String confirmation = (String) ois.readObject();
                if (confirmation.equals("NOK")) {
                    JOptionPane.showMessageDialog(null, "Erreur Paiement", "Alert", JOptionPane.WARNING_MESSAGE);
                } else {
                    for(int i=0;i<listResa.size();i++) {
                        if (listResa.get(i).get_id()==Integer.parseInt(aPayer)) {//on trouve la reservation a supprimer
                            for(int j=0;j<listChambresReservee.size();j++) {
                                if(listChambresReservee.get(j).get_numeroChambre()==listResa.get(i).get_numChambre()) {//on trouve la chambre correspondant la reservation a supprimer
                                    listChambresReservee.remove(j);
                                }
                            }
                            listResa.remove(i);
                        }
                    }
                    JOptionPane.showMessageDialog(null, "Paiement acceptée", "Alert", JOptionPane.WARNING_MESSAGE);
                }
                aPayer="";
                response.sendRedirect("JSPCaddie.jsp");
                break;
            case "annuler" :
                response.sendRedirect("JSPCaddie.jsp");
                break;
            case "retour recherche" :
                listChambres.removeAll(listChambres);
                session.setAttribute("chambres", listChambres);
                response.sendRedirect("JSPInit.jsp");
                break;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        Object existe = session.getAttribute("logon.isDone");
        if(existe == null)
        {
            session.setAttribute("login.target", request.getRequestURL().toString());
            response.sendRedirect(request.getScheme() + "://" + request.getServerName()+ ":" + request.getServerPort() + "/ServletRTI");
            return;
        }
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        if(session.getAttribute("logon.isDone") == null)
            session.setAttribute("logon.isDone", request.getParameter("username"));

        String target = (String)session.getAttribute("login.target");
        if(target == null) {
            try {
                processRequest(request, response);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else
            response.sendRedirect(target);
    }

    public void destroy() {
    }
}