package lexer;

public class Symbol {
    private String name;
    private Tokens kind;

    private Symbol( String n, Tokens kind ) {
        name = n;
        this.kind = kind;
    }

    private static java.util.HashMap<String,Symbol> symbols = new java.util.HashMap<String,Symbol>();

    public String toString() {
        return name;
    }

    public Tokens getKind() {
        return kind;
    }

    public static Symbol symbol( String newTokenString, Tokens kind ) {
        Symbol s = symbols.get( newTokenString );
        if( s == null ) {
            if( kind == Tokens.BogusToken ) {
                return null;
            }
            s = new Symbol( newTokenString, kind );
            symbols.put( newTokenString, s );
        }
        return s;
    }
}
