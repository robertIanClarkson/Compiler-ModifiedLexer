package lexer;
import java.io.*;

public class Lexer {
    private boolean atEOF = false;
    private char ch;
    private SourceReader source;
    private int startPosition, endPosition;

    public Lexer( String sourceFile ) throws Exception {
        new TokenType();
        source = new SourceReader( sourceFile );
        ch = source.read();
    }

    public Token newIdToken( int lineNumber, int startPosition, int endPosition, String id ) {
        return new Token(
            lineNumber,
            startPosition,
            endPosition,
            Symbol.symbol( id, Tokens.Identifier )
        );
    }

    public Token newNumberToken( int lineNumber, int startPosition, int endPosition, String number ) {
        return new Token(
            lineNumber,
            startPosition,
            endPosition,
            Symbol.symbol( number, Tokens.INTeger )
        );
    }

    public Token newStringLitToken( int lineNumber, int startPosition, int endPosition, String string ) {
        return new Token(
            lineNumber,
            startPosition,
            endPosition,
            Symbol.symbol( string, Tokens.StringLit )
        );
    }

    public Token newNumberLitToken( int lineNumber, int startPosition, int endPosition, String number ) {
        return new Token(
            lineNumber,
            startPosition,
            endPosition,
            Symbol.symbol( number, Tokens.NumberLit )
        );
    }

    public Token makeToken( String s, int startPosition, int endPosition ) {
        if( s.equals("//") ) {
            try {
                int oldLine = source.getLineno();
                do {
                    ch = source.read();
                } while( oldLine == source.getLineno() );
            } catch (Exception e) {
                atEOF = true;
            }
            return nextToken();
        }
        Symbol sym = Symbol.symbol( s, Tokens.BogusToken );
        if( sym == null ) {
            System.out.println( "******** illegal character: " + s );
            atEOF = true;
            return nextToken();
        }
        return new Token( source.getLineno(), startPosition, endPosition, sym );
    }

    public Token nextToken() {
        if( atEOF ) {
            if( source != null ) {
                source.close();
            }
            return null;
        }
        try {
            while( Character.isWhitespace( ch )) {
                ch = source.read();
            }
        } catch( Exception e ) {
            atEOF = true;
            return nextToken();
        }
        startPosition = source.getPosition();
        endPosition = startPosition - 1;
        if ( ch == '\'') {
            try {
                String string = "";
                int lineNumberCheck = source.getLineno();
                ch = source.read();
                startPosition = source.getPosition();
                while ( ch != '\'' ) {
                    string = string + ch;
                    ch = source.read();
                }
                ch = source.read();
                endPosition = source.getPosition();
                if ( lineNumberCheck != source.getLineno() ) {
                    return makeToken( "\\n", startPosition, endPosition );
                } else {
                    return newStringLitToken( source.getLineno(), startPosition, endPosition, string );
                }
            } catch (Exception e) {
                atEOF = true;
                return nextToken();
            }
        }
        if( Character.isJavaIdentifierStart( ch )) {
            String id = "";
            try {
                do {
                    endPosition++;
                    id += ch;
                    ch = source.read();
                } while( Character.isJavaIdentifierPart( ch ));
            } catch( Exception e ) {
                atEOF = true;
            }
            return newIdToken( source.getLineno(), startPosition, endPosition, id );
        }
        if( Character.isDigit( ch )) {
            String number = "";
            try {
                do {
                    endPosition++;
                    number += ch;
                    ch = source.read();
                    if (ch == '.' ) {
                        number += ch;
                        ch = source.read();
                    }
                } while( Character.isDigit( ch ));
            } catch( Exception e ) {
                atEOF = true;
            }
            if ( number.contains( String.valueOf('.') ) ) {
                return newNumberLitToken( source.getLineno(), startPosition, endPosition, number );
            } else {
                return newNumberToken( source.getLineno(), startPosition, endPosition, number );
            }
        }
        String charOld = "" + ch;
        String op = charOld;
        Symbol sym;
        try {
            endPosition++;
            ch = source.read();
            op += ch;
            sym = Symbol.symbol( op, Tokens.BogusToken );
            if (sym == null) {
                return makeToken( charOld, startPosition, endPosition );
            }
            endPosition++;
            ch = source.read();
            return makeToken( op, startPosition, endPosition );
        } catch( Exception e ) { /* no-op */ }
        atEOF = true;
        if( startPosition == endPosition ) {
            op = charOld;
        }
        return makeToken( op, startPosition, endPosition );
    }

    public String toString() {
        try {
            return source.getSourceCode();
        } catch (Exception e) {
            return "";
        }
    }

    public void clearSource() {
        source = null;
    }

    public static void main(String args[]) {
        Token token;
        try {
            if ( args.length == 0 ) {
                System.out.println( "usage: java lexer.Lexer filename.x" );
            } else {
                Lexer lex = new Lexer( args[0] );
                token = lex.nextToken();
                while( token != null ) {
                    token.print();
                    token = lex.nextToken();
                }
                System.out.print( "\n" + lex );
                lex.clearSource();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File \'" + args[0] + "\' not found!");
        } catch (Exception e) {
            System.out.println( e );
        }
    }
}
