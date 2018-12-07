public enum Colour {
  BLACK('B', "Black"), //(char)(9823)
  WHITE('W', "White"), //(char)(9817)
  NONE('.', "None");

  private final char colourRep;
  private final String colourString;

  private Colour(char colourRep, String colourString) {
    this.colourRep = colourRep;
    this.colourString = colourString;
  }

  public char colourRep() {
    return this.colourRep;
  }
  public String colourString() {
    return this.colourString;
  }

  public Colour opponentColour() {
    switch (this) {
      case BLACK : return Colour.WHITE;
      case WHITE : return Colour.BLACK;
      default    : return Colour.NONE;
    }
  }
}