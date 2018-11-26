import java.io.*;
import java.util.NoSuchElementException;

public class Scanner {
    private BufferedReader reader;
    private boolean isReadNext;
    private boolean isReadWord;
    private int lineNumber = 1;
    private String lineSep = System.lineSeparator();

    public Scanner(InputStream in, String encoding) throws UnsupportedEncodingException {
        reader = new BufferedReader(new InputStreamReader(in, encoding));
    }
    
    public Scanner(File file, String encoding) throws UnsupportedEncodingException, FileNotFoundException {
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
    }
    
    public boolean hasNextWord() throws IOException {
        int curChar;
        int prevChar = 0;
        while (true) {
            reader.mark(1);
            curChar = reader.read();
            if (curChar == -1) {
                isReadWord = false;
                return false;
            }
            if (ifNextLine(curChar, prevChar)) {
                lineNumber++;
            }
            prevChar = curChar;
            if (isGood((char) curChar)) {
                isReadWord = true;
                reader.reset();
                return true;
            }
        }
    }
    
    public String nextWord() throws IOException {
        StringBuilder curString = new StringBuilder();
        if (isReadWord || hasNextWord()) {
            isReadWord = false;
            int curChar;
            while (true) {
                reader.mark(1);
                curChar = reader.read();
                if (isGood((char)curChar)) {
                    curString.append((char) curChar);
                } else {
                    reader.reset();
                    return curString.toString();
                }
            }
        } else {
            throw new NoSuchElementException("Next word not found");
        }
    }
    
    public boolean hasNext() throws IOException {
        int curChar;
        int prevChar = 0;
        while (true) {
            reader.mark(1);
            curChar = reader.read();
            if (curChar == -1) {
                isReadNext = false;
                return false;
            }
            if (ifNextLine(curChar, prevChar)) {
                lineNumber++;
            }
            prevChar = curChar;
            if (!Character.isWhitespace(curChar)) {
                isReadNext = true;
                reader.reset();
                return true;
            }
        }
    }
    
    public String next() throws IOException {
        StringBuilder curString = new StringBuilder();
        if (isReadNext || hasNext()) {
            isReadNext = false;
            int curChar;
            int prevChar = 0;
            while (true) {
                curChar = reader.read();
                if (ifNextLine(curChar, prevChar)) {
                    lineNumber++;
                }
                prevChar = curChar;
                if (curChar == -1 || Character.isWhitespace(curChar)) {
                    return curString.toString();
                } else {
                    curString.append((char) curChar);
                }
            }
        } else {
            throw new NoSuchElementException("No strings found");
        }
    }
    
    public int nextInt () throws IOException, NumberFormatException {
        if (isReadNext || hasNext()) {
            return Integer.parseInt(next());
        } else {
            throw new NoSuchElementException("No numbers found");
        }
    }
    
    private static boolean isGood(char c) {
        return Character.getType(c) == Character.DASH_PUNCTUATION || Character.isLetter(c) || c == '\'';
    }
    
    public int getLineNumber() {
        return lineNumber;
    }
    
    public boolean ifNextLine (int curChar, int prevChar) {
        return lineSep.length() == 1 && String.valueOf((char)curChar).equals(lineSep) ||
                lineSep.length() == 2 && String.valueOf((char)prevChar+(char)curChar).equals(lineSep);
    }
    
    public void close() throws IOException {
        if (reader != null) {
            reader.close();
            reader = null;
        }
    }
}
