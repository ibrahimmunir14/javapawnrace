public class Square {
    private int x;
    private int y;
    private Colour occupier = Colour.NONE;

    public Square(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public int getX() {
      return this.x;
    }

    public int getY() {
      return this.y;
    }

    public Colour occupiedBy() {
      return occupier;
    }

    public void setOccupier(Colour colour) {
        this.occupier = colour;
    }

    public char getCol() {
      return Util.XtoCol(x);
    }

    public int getRow() {
      return Util.YtoRow(y);
    }

}