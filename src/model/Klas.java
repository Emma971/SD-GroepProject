package model;

public class Klas {
	private String naam;
	
	protected Klas(String nm) {
		naam = nm;
	}
	
	public String getNaam() {
		return naam;
	}

	public boolean equals(Object obj) {
		boolean gelijk = obj instanceof Klas;
		gelijk = gelijk && ((Klas)obj).naam.equals(naam);
		return gelijk;
	}
}