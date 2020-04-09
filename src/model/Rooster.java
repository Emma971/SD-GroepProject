package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Rooster {
	private String les;
	private LocalDate lesDatum;
	private int weeknummer;
	private LocalDateTime lesBegintijd;
	private LocalDateTime lesEindTijd;
	private Klas klas;
	private int lesID;


	protected Rooster(int lesID, String les, LocalDate lesdag, LocalDateTime beginTijd, LocalDateTime eindTijd) {

//        LocalTime beginTijd = LocalTime.parse(beginTijdString);
//        LocalTime eindTijd  = LocalTime.parse(eindTijdString);

		this.lesID = lesID;
		this.les = les;
		lesDatum = lesdag;
		lesBegintijd = beginTijd;
		lesEindTijd = eindTijd;

		LocalDate dagX = lesDatum;
		DateTimeFormatter forX = DateTimeFormatter.ofPattern("w");
		int week = Integer.parseInt(dagX.format(forX));
		weeknummer=week;
	}

	public String getLes() {
		return les;
	}

	protected void setKlas(Klas k) {
		klas = k;
	}

	public Klas getKlas() {
		return klas;
	}

	public static String getklasnaam(){ return getklasnaam();}

	public LocalDate getlesdagDatum() {
		return lesDatum;
	}

	public int getRoosterWeeknummer() {return weeknummer;}

	public String getlestijd() {
		return ""+ lesBegintijd.getHour()+":"+lesBegintijd.getMinute() + " - " + lesEindTijd.getHour()+":"+lesEindTijd.getMinute();
	}

	public int getLesID() {
		return lesID;
	}

	public boolean equals(Object obj) {
		boolean gelijk = obj instanceof Rooster;

		gelijk = gelijk && ((Rooster) obj).les.equals(les);
		gelijk = gelijk && ((Rooster) obj).lesDatum.equals(lesDatum);
		gelijk = gelijk && ((Rooster) obj).lesBegintijd.equals(lesBegintijd);
		gelijk = gelijk && ((Rooster) obj).lesEindTijd.equals(lesEindTijd);
		gelijk = gelijk && ((Rooster) obj).klas.equals(klas);

		return gelijk;
	}
}