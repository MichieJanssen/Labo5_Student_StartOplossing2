package ui.controller;


import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.db.StudentDB;
import domain.model.Student;

@WebServlet("/StudentInfo")
public class StudentInformatie extends HttpServlet {
	private static final long serialVersionUID = 1L;

	StudentDB klas = new StudentDB();

	public StudentInformatie() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String destination = "index.html";
		String command = request.getParameter("command");
		if (command == null) {
			command = "";
		}
		switch (command) {
			case "overview":
				destination = overview(request, response);
				break;
			case "find":
				destination = find(request, response);
				break;
			case "voegToe":
				destination = voegToe(request, response);
				break;
			case "verwijder":
				destination = verwijder(request, response);
				break;
			default:
				destination = "index.html";
		}
		RequestDispatcher view = request.getRequestDispatcher(destination);
		view.forward(request, response);
	}

	private String verwijder(HttpServletRequest request, HttpServletResponse response) {
		if(request.getParameter("Submit").equals("Zeker")) {
			String naam = request.getParameter("naam");
			String voornaam = request.getParameter("voornaam");
			klas.verwijder(klas.vind(naam, voornaam));
			return overview(request, response);
		} else {
			return "index.html";
		}
	}

	private String voegToe(HttpServletRequest request, HttpServletResponse response) {
		String naam = request.getParameter("naam");
		String voornaam = request.getParameter("voornaam");
		String leeftijd = request.getParameter("leeftijd");
		String studierichting = request.getParameter("studierichting");

		String destination = "index.html";

		if (naam.isEmpty() || voornaam.isEmpty() || leeftijd.isEmpty() || studierichting.isEmpty()) {
			destination = "studentForm.jsp";
		} else {
			Student student = new Student(naam, voornaam, Integer.parseInt(leeftijd), studierichting);
			klas.voegToe(student);
			destination = overview(request, response);
		}
		return destination;
	}

		private String find (HttpServletRequest request, HttpServletResponse response){
			String naam = request.getParameter("naam");
			String voornaam = request.getParameter("voornaam");
			String destination = "";

			if (naam == null || voornaam == null) {
				destination = "nietGevonden.jsp";
			} else {
				Student student = klas.vind(naam, voornaam);
				if (student == null) {
					destination = "nietGevonden.jsp";
				} else {
					destination = "gevonden.jsp";
					request.setAttribute("student", student);
				}
			}
			return destination;
		}

		private String overview (HttpServletRequest request, HttpServletResponse response){
			request.setAttribute("studenten", klas.getKlas());
			return "studentOverview.jsp";
		}
	}

