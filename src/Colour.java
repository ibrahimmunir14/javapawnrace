public enum Colour {
    BLACK("Black"),
    WHITE("White"),
    NONE("None");

    private final String colourRep;

    private Colour(String colourRep) {
      this.colourRep = colourRep;
    }

    public String colourRep() {
        return this.colourRep;
    }

}