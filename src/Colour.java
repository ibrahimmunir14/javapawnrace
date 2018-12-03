public enum Colour {
  BLACK("B"),
  WHITE("W"),
  NONE(".");

  private final String colourRep;

  private Colour(String colourRep) {
    this.colourRep = colourRep;
  }

  public String colourRep() {
    return this.colourRep;
  }

  public Colour opponentColour() {
    switch (this) {
      case BLACK : return Colour.WHITE;
      case WHITE : return Colour.BLACK;
      default    : return Colour.NONE;
    }
  }
}