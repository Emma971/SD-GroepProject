package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class School {
	private static School hetSchool;
	public static void setSchool(School school) {
		hetSchool = school;
	}
	public static School getSchool() {
		return hetSchool;
	}

	private String naam;
	private List<Rooster> allelessen = new ArrayList<Rooster>();
	
	public School(String naam) {
		this.naam = naam;
	}

	public String getNaam() {
		return naam;
	}

	public List<Rooster> getRooster() {
		return Collections.unmodifiableList(allelessen);
	}

	public Rooster voegLesToe(int lesID, String lesnaam, LocalDate lesdag, LocalDateTime van, LocalDateTime tot, String klasnaam) throws Exception {

		Rooster rooster = new Rooster(lesID, lesnaam, lesdag, van, tot);
		rooster.setKlas(new Klas(klasnaam));

		allelessen.add(rooster);

		return rooster;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder("lessen van school " + naam + ":");
		allelessen.forEach(rooster -> result.append("\n\t" + rooster.getLes() + "    "+ rooster.getlesdagDatum()+" : " + rooster.getlestijd()));
		return result.toString();
	}
}