package lexer;

public class Token {
    private int leftPosition,rightPosition, lineNumber;
    private Symbol symbol;

    public Token( int lineNumber, int leftPosition, int rightPosition, Symbol sym ) {
        this.leftPosition = leftPosition;
        this.rightPosition = rightPosition;
        this.symbol = sym;
        this.lineNumber = lineNumber;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void print() {
        System.out.format("%-11s left: %-8d right: %-8d line: %-8d %s\n",
            symbol.toString(),
            leftPosition,
            rightPosition,
            lineNumber,
            symbol.getKind()
        );
        return;
    }

    public String toString() {
        return symbol.toString();
    }

    public int getLeftPosition() {
        return leftPosition;
    }

    public int getRightPosition() {
        return rightPosition;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public Tokens getKind() {
        return symbol.getKind();
    }
}
