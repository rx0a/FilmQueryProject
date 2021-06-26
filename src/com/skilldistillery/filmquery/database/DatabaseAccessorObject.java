package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.davidmoten.text.utils.WordWrap;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {

	@Override
	public Film findFilmById(int filmId) throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sdvid?useSSL=false", "student",
				"student");

		String sql = "SELECT film.id, film.title, film.description, film.release_year, film.rental_rate, "
				+ "film.rental_duration, film.length, film.replacement_cost, category.name, language.name,"
				+ "film.rating\n" + "FROM film  JOIN film_category \n" + "ON film.id = film_category.film_id\n"
				+ "JOIN category \n" + "ON film_category.category_id = category.id\n" + "JOIN language \n"
				+ "ON film.language_id = language.id \n" + "WHERE film.id = ?";

		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmId);
		ResultSet rs = stmt.executeQuery();

		Film film = null;

		while (rs.next()) {

			int id = rs.getInt("id");
			String title = rs.getString("title");
			String description = rs.getString("description");
			String wrappedDescription = WordWrap.from(description).maxWidth(40).insertHyphens(true).wrap();
			String releaseYear = rs.getString("release_Year");
			String language = rs.getString("language.name");
			String rating = rs.getString("film.rating");
			List<Actor> actors = findActorsByFilmId(id);

			film = new Film(title, wrappedDescription, rating, releaseYear, language, actors);
		}
		rs.close();
		stmt.close();
		conn.close();
		return film;
	}

	@Override
	public Film filmAdditionalInfo(int filmId) throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sdvid?useSSL=false", "student",
				"student");

		String sql = "SELECT film.id, film.title, film.description, film.release_year, film.rental_rate, "
				+ "film.rental_duration, film.length, film.replacement_cost, category.name, language.name,"
				+ "film.rating\n" + "FROM film  JOIN film_category \n" + "ON film.id = film_category.film_id\n"
				+ "JOIN category \n" + "ON film_category.category_id = category.id\n" + "JOIN language \n"
				+ "ON film.language_id = language.id \n" + "WHERE film.id = ?";

		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmId);
		ResultSet rs = stmt.executeQuery();

		Film film = null;

		while (rs.next()) {

			String rentalDuration = rs.getString("rental_duration");
			String rentalRate = rs.getString("rental_rate");
			String length = rs.getString("length");
			String replacementCost = rs.getString("replacement_cost");
			String category = rs.getString("category.name");

			film = new Film(rentalDuration, rentalRate, length, replacementCost, category);
		}
		rs.close();
		stmt.close();
		conn.close();
		return film;
	}

	public List<Film> findFilmByASearchKeyword(String keyword) throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sdvid?useSSL=false", "student",
				"student");

		String sql = "SELECT film.id, film.title, film.description, film.release_year, film.rental_rate, "
				+ "film.rental_duration, film.length, film.replacement_cost, category.name, language.name,"
				+ "film.rating\n" + "FROM film  JOIN film_category \n" + "ON film.id = film_category.film_id\n"
				+ "JOIN category \n" + "ON film_category.category_id = category.id\n" + "JOIN language \n"
				+ "ON film.language_id = language.id \n" + "WHERE film.title LIKE ? OR film.description LIKE ?";

		PreparedStatement stmt = conn.prepareStatement(sql);

		String search = "%" + keyword + "%";
		stmt.setString(1, search);
		stmt.setString(2, search);
		ResultSet rs = stmt.executeQuery();

		List<Film> films = new ArrayList<Film>();
		Film film = null;

		while (rs.next()) {

			int id = rs.getInt("id");
			String title = rs.getString("title");
			String description = rs.getString("description");
			String wrappedDescription = WordWrap.from(description).maxWidth(40).insertHyphens(true).wrap();
			String releaseYear = rs.getString("release_Year");
			String language = rs.getString("language.name");
			String rating = rs.getString("film.rating");
			List<Actor> actors = findActorsByFilmId(id);

			film = new Film(id, title, wrappedDescription, rating, releaseYear, language, actors);
			films.add(film);
		}
		rs.close();
		stmt.close();
		conn.close();
		return films;
	}

	@Override
	public Actor findActorById(int actorId) throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sdvid?useSSL=false", "student",
				"student");
		String sql = "SELECT actor.id, actor.first_name, actor.last_name "
				+ "FROM actor JOIN film_actor ON actor.id = film_actor.actor_id "
				+ "JOIN film ON film_actor.film_id = film.id WHERE actor.id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, actorId);
		ResultSet rs = stmt.executeQuery();

		Actor actor = null;

		while (rs.next()) {
			int id = rs.getInt("id");
			String firstName = rs.getString("first_name");
			String lastName = rs.getString("last_name");
			actor = new Actor(id, firstName, lastName);
		}
		rs.close();
		stmt.close();
		conn.close();
		return actor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) throws SQLException {

		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sdvid?useSSL=false", "student",
				"student");
		String sql = "SELECT actor.id, actor.first_name, actor.last_name "
				+ "FROM actor JOIN film_actor ON actor.id = film_actor.actor_id "
				+ "JOIN film ON film_actor.film_id = film.id WHERE film.id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmId);
		ResultSet rs = stmt.executeQuery();

		List<Actor> actors = new ArrayList<Actor>();
		Actor actor = null;

		while (rs.next()) {
			int actorId = rs.getInt("actor.id");
			String firstName = rs.getString("first_name");
			String lastName = rs.getString("last_name");
			actor = new Actor(actorId, firstName, lastName);
			actors.add(actor);
		}
		rs.close();
		stmt.close();
		conn.close();
		return actors;
	}
}
