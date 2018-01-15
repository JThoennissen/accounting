package application.accounting;
// class for parsing arguments from command-line

public class ArgParser {

    private String[] args = null;
    private boolean showHelp = false;
    private boolean showVersion = false;
    private String inputFilename = null;
    private String outputFilename = null;
    private String logFilename = null;
    private String nonOptions = null;
    private String interestRate = null;

    public ArgParser(String[] args) {

        this.args = args;
        parseArgs();
        
    } // end of special constructor "ArgParser(String[])"


    private void parseArgs() {
    // parses arguments of given command and checks if used valid
        int c;
        String arg;
        LongOpt[] longopts = new LongOpt[6];
  
        StringBuffer sb = new StringBuffer();
        longopts[0] = new LongOpt("input-file", LongOpt.REQUIRED_ARGUMENT, sb, 'i');
        longopts[1] = new LongOpt("output-file", LongOpt.REQUIRED_ARGUMENT, sb, 'o'); 
        longopts[2] = new LongOpt("log-file", LongOpt.OPTIONAL_ARGUMENT, null, 'l');
        longopts[3] = new LongOpt("rate-of-interest", LongOpt.REQUIRED_ARGUMENT, sb, 'r');
        longopts[4] = new LongOpt("help", LongOpt.NO_ARGUMENT, null, 'h');
        longopts[5] = new LongOpt("version", LongOpt.NO_ARGUMENT, null, 'v');
 
        Getopt g = new Getopt("testprog", args, "i:o:r:l:hv", longopts);
        
        while ((c = g.getopt()) != -1) {
            switch (c) {
            case 0:
            case 'i':
                arg = g.getOptarg();
                if (arg != null) {
                    inputFilename = arg;
                } // end of if ( i + 1 < args.length )
                else {
                    throw new IllegalArgumentException("missing filename");
                } // end of if ( i + 1 < args.length ) else
                break;
            case 1:
            case 'o':
                arg = g.getOptarg();
                if (arg != null) {
                    outputFilename = arg;
                } // end of if ( i + 1 < args.length )
                else {
                    throw new IllegalArgumentException("missing filename");
                } // end of if ( i + 1 < args.length ) else
                break;
            case 2:
            case 'l':
                arg = g.getOptarg();
                if (arg != null) {
                    logFilename = arg;
                } // end of if ( i + 1 < args.length )
                else {
                    throw new IllegalArgumentException("missing filename");
                } // end of if ( i + 1 < args.length ) else
                break;
            case 3:
            case 'r':
                arg = g.getOptarg();
                if (arg != null) {
                    interestRate = arg;
                } // end of if ( i + 1 < args.length )
                else {
                    throw new IllegalArgumentException("missing interest rate");
                } // end of if ( i + 1 < args.length ) else
                break;
            case 4:
            case 'h':
                System.out.println("User asks for help");
                showHelp = true;
                break;
            case 5:
            case 'v':
                System.out.println("User asks for the program's version");
                showVersion = true;
                break;
            default:
                arg = g.getOptarg();
                if ( sb == null ) {
                    sb = new StringBuffer();
                    sb.append(arg);
                } // end of if ( sb == null )
                else {
                    sb.append(" ").append(arg);
                } // end of if ( sb == null ) else
                break;
            }
        }
        
        if ( sb != null ) {
            nonOptions = sb.toString();
        } // end of if ()

    } // end of method "parseArgs()"


    @Override
    public String toString() {
    // returns String representation of command

        StringBuffer sb = new StringBuffer();

        for ( int i = 0; i < args.length; i++ ) {

            if ( args[i].equals("-h") || args[i].equals("--h") ||
                 args[i].equals("-v") || args[i].equals("--v") ) {

                sb.append(args[i]).append("\n");

            } // end of if ( args[i].equals("-h")  ...)
            else if ( args[i].equals("-i") || args[i].equals("--input-file") ||
                args[i].equals("-o") || args[i].equals("--output-file") ) {

                System.out.println("i: " + i + ", args.length: " + args.length);
                if ( i + 1 < args.length ) {
                    sb.append(args[i] + " " + args[++i]).append("\n");
                } // end of if ( i + 1 < args.length )
                else {
                    throw new IllegalArgumentException("missing filename");
                } // end of if ( i + 1 < args.length ) else

            } // end of else if ( args[i].equals("-i") ... )
            else if ( args[i].equals("-l") || args[i].equals("--log-file") ) {

                if ( i + 1 < args.length ) {

                    if ( args[i + 1].startsWith("-") ) {
                        sb.append(args[i]).append("\n");
                    } // end of if ( args[ i + 1].startsWith("-") )
                    else {
                        sb.append(args[i] + " " + args[++i]).append("\n");
                    } // end of if ( args[ i + 1].startsWith("-") ) else

                } // end of if ( i + 1 < args.length )

            } // end of else if ( args[i].equals("-i") ... )
            else {

                sb.append("non-option argument: " +
                          args[i]).append("\n");

            } // end of if ( args[i].equals("-h") || args[i].equals("--h") ) else

        } // end of for (int i = 0; i  < args.length; i++)

        return sb.toString();

    } // end of method "toString()"


    public boolean getShowHelp() {
        return showHelp;
    } // end of method "getShowHelp()"


    public boolean getShowVersion() {
        return showHelp;
    } // end of method "getShowVersion()"


    public String getInputFilename() {
        return inputFilename;
    } // end of method "getInputFilename()"


    public String getOutputFilename() {
        return outputFilename;
    } // end of method "getOutputFilename()"


    public String getLogFilename() {
        return logFilename;
    } // end of method "getLogFilename()"


    public String getNonOptions() {
        return nonOptions;
    } // end of method "getNonOptions()"
    
    public String getInterestRate() {
        return interestRate;
    }


    public static final void main(final String[] args) {
        ArgParser argParser = new ArgParser(args);
        System.out.println(argParser);
    } // end of method "main(String[] args)"

} // end of class "ArgParser"
