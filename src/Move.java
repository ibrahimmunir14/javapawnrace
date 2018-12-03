public class Move {
  private Square from;
  private Square to;
  private boolean isCapture;
  private boolean isEnPassantCapture;

  public Move(Square from, Square to, boolean isCapture, boolean isEnPassantCapture) {
    this.from = from;
    this.to = to;
    this.isCapture = isCapture;
    this.isEnPassantCapture = isEnPassantCapture;
  }

  public Square getTo() {
    return to;
  }

  public Square getFrom() {
    return from;
  }

  public boolean isCapture() {
    return isCapture;
  }

  public boolean isEnPassantCapture() {
    return isEnPassantCapture;
  }

  public String getSAN() {
    if (!isCapture()) {
        return to.getColSAN() + "" + to.getRowSAN();
    }
    else {
        return from.getColSAN() + "x" + to.getColSAN() + "" + to.getRowSAN();
    }
  }
}