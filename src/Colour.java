public enum Colour {
  BLACK('B'), //(char)(9823)
  WHITE('W'), //(char)(9817)
  NONE('.');

  private final char colourRep;

  private Colour(char colourRep) {
    this.colourRep = colourRep;
  }

  public char colourRep() {
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