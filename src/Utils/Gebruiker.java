package Utils;

public class Gebruiker {
    private int ID;
    private String naam;
    private String type;
    private int typeID;
    private String klasNaam;

    public Gebruiker(int ID, String naam, String type, int typeID, String klasNaam) {
        this.ID = ID;
        this.naam = naam;
        this.type = type;
        this.typeID = typeID;
        this.klasNaam = klasNaam;
    }

    public int getID() {
        return ID;
    }

    public String getNaam() {
        return naam;
    }

    public String getType() {
        return type;
    }

    public int getTypeID() {
        return typeID;
    }

    public String getKlasNaam() {
        return klasNaam;
    }
}
