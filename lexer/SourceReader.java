package lexer;
import java.io.*;

public class SourceReader {
    private BufferedReader source;
    private int lineno = 0;
    private int position;
    private boolean isPriorEndLine = true;
    private String nextLine;
    private String sourceCode;
    private String sourceFile;

    public SourceReader( String sourceFile ) throws IOException {
        this.sourceFile = sourceFile;
        source = new BufferedReader( new FileReader( sourceFile ));
    }

    void close() {
        try {
            source.close();
        } catch( Exception e ) {
            /* no-op */
        }
    }

    public char read() throws IOException {
        if( isPriorEndLine ) {
            lineno++;
            position = -1;
            nextLine = source.readLine();
            if( nextLine != null ) {
                System.out.println( "READLINE:   " + nextLine );
            }
            isPriorEndLine = false;
        }
        if( nextLine == null ) {
            throw new IOException();
        }
        if( nextLine.length() == 0 ) {
            isPriorEndLine = true;
            return ' ';
        }
        position++;
        if( position >= nextLine.length() ) {
            isPriorEndLine = true;
            return ' ';
        }
        return nextLine.charAt( position );
    }

    public int getPosition() {
        return position;
    }

    public int getLineno() {
        return lineno;
    }

    public String getSourceCode()  throws IOException {
        sourceCode = "";
        nextLine = "";
        lineno = 0;
        source = new BufferedReader( new FileReader( sourceFile ));
        nextLine = source.readLine();
        lineno += 1;
        while( nextLine != null ) {
            sourceCode = String.format("%s%3d: %s\n", sourceCode, lineno, nextLine );
            nextLine = source.readLine();
            lineno += 1;
        }
        return sourceCode;
    }
}
