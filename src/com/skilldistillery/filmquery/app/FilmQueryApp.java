package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) throws SQLException {
		FilmQueryApp app = new FilmQueryApp();
		Scanner input = new Scanner(System.in).useDelimiter("\n");
		app.launch(input);
		input.close();
	}

	private void launch(Scanner input) throws SQLException {
		nline();
		startUserInterface(input);
	}

	private void startUserInterface(Scanner input) throws SQLException {

		System.out.println("|              Main Menu                 |");
		nline();
		System.out.println("| [1] Look up a film by its id           |");
		System.out.println("| [2] Look up a film by a search keyword |");
		System.out.println("| [3] Look up an actor by their id       |");
		System.out.println("| [4] Look up an actor by film id        |");
		System.out.println("| [5] Exit the application               |");
		nline();
		System.out.print("|  > ");

		boolean repeat = true;

		do {
			int menu = input.nextInt();
			switch (menu) {
			case 1:
				nline();
				lookUpFilmByItsId(input);
				cont(input);
				break;
			case 2:
				nline();
				lookUpFilmByASearchKeyword(input);
				cont(input);
				break;
			case 3:
				nline();
				lookUpActorByTheirId(input);
				cont(input);
				break;
			case 4:
				nline();
				lookUpActorByFilmId(input);
				cont(input);
				break;
			case 5:
				goodbye();
				repeat = false;
				System.exit(menu);
				break;
			default:
				System.out.println("|            Invalid Input               |");
				nline();
				cont(input);
				break;
			}
		} while (repeat);
	}

	private void lookUpFilmByItsId(Scanner input) throws SQLException {
		System.out.println("| Enter film id #                        | ");
		System.out.print("|  > ");
		int id = input.nextInt();
		Film film = db.findFilmById(id);
		if (film != null) {
			nline();
			System.out.println(film.toString("basic"));
			nline();
			System.out.println("| [1] View additional info               |");
			System.out.println("| [2] Return to Main Menu                |");
			nline();
			System.out.print("|  > ");
			int menu = input.nextInt();
			switch (menu) {
			case 1:
				nline();
				filmAdditionalInfo(id);
				break;
			case 2:
				nline();
				startUserInterface(input);
				break;
			}
		} else
			nline();
		System.out.println("| No film found with the id: " + id);
		nline();
	}

	private void filmAdditionalInfo(int id) throws SQLException {
		Film film = db.filmAdditionalInfo(id);
		System.out.println(film.toString("additional"));
	}

	private void lookUpFilmByASearchKeyword(Scanner input) throws SQLException {
		System.out.println("| Enter search keyword                   | ");
		System.out.print("|  > ");
		String keyword = input.next();
		List<Film> films = db.findFilmByASearchKeyword(keyword);
		if (films.isEmpty()) {
			nline();
			System.out.println("|           No matching film             |");
			nline();
		} else {
			nline();
			for (Film film : films) {
				System.out.println(film.toString("short"));
			}
			nline();
		}
	}

	private void lookUpActorByTheirId(Scanner input) throws SQLException {
		System.out.println("| Enter actor id #                       | ");
		System.out.print("|  > ");
		int id = input.nextInt();
		Actor actor = db.findActorById(id);
		if (actor != null) {
			sline();
			System.out.println(actor);
			nline();
		} else {
			nline();
			System.out.println("| No actor found with the id: " + id);
			nline();
		}
	}

	private void lookUpActorByFilmId(Scanner input) throws SQLException {

		System.out.println("| Enter film id #                        | ");
		System.out.print("|  > ");
		int id = input.nextInt();
		List<Actor> actors = db.findActorsByFilmId(id);
		if (actors != null) {
			sline();
			System.out.println("\n| Actors:" + actors.toString().replaceAll("\\[|\\]|,", ""));
		} else
			System.out.println("| No actor found for the movie id: " + id);
		nline();
	}

	private void cont(Scanner input) throws SQLException {
		System.out.println("| Press [ Enter ] for Main Menu          |");
		sline();
		if (input.next().equals("")) {
			startUserInterface(input);
		} else {
			startUserInterface(input);
		}
	}

	private void goodbye() {
		nline();
		System.out.println("|               Goodbye!                 |");
		nline();
	}

	private void nline() {
		System.out.println("|----------------------------------------|");
	}

	private void sline() {
		System.out.print("|----------------------------------------|");
	}
}
