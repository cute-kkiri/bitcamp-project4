package util;

public class Tui {
    public static final String BLACK = "\033[30m";
    public static final String GRAY = "\033[38;5;8m";
    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[32m";
    public static final String YELLOW = "\033[33m";
    public static final String BLUE = "\033[34m";
    public static final String MAGENTA = "\033[35m";
    public static final String CIAN = "\033[36m";
    public static final String WHITE = "\033[37m";
    public static final String BGBLACK = "\033[40m";
    public static final String BGRED = "\033[41m";
    public static final String GRGREEN = "\033[42m";
    public static final String BGYELLOW = "\033[43m";
    public static final String BGBLUE = "\033[44m";
    public static final String BGMAGENTA = "\033[45m";
    public static final String BGCIAN = "\033[46m";
    public static final String BGWHITE = "\033[47m";
    public static final String LIGHTBLACK = "\033[90m";
    public static final String LIGHTRED = "\033[91m";
    public static final String LIGTHGREEN = "\033[92m";
    public static final String LIGHTYELLOW = "\033[93m";
    public static final String LIGTHBLUE = "\033[94m";
    public static final String LIGHTMAGENTA = "\033[95m";
    public static final String LIGHTCIAN = "\033[96m";
    public static final String LIGHTWHITE = "\033[97m";
    public static final String BGLIGHTBLACK = "\033[100m";
    public static final String BGLIGHTRED = "\033[101m";
    public static final String GRLIGHTGREEN = "\033[102m";
    public static final String BGLIGHTYELLOW = "\033[103m";
    public static final String BGLIGHTBLUE = "\033[104m";
    public static final String BGLIGHTMAGENTA = "\033[105m";
    public static final String BGLIGHTCIAN = "\033[106m";
    public static final String BGLIGHTWHITE = "\033[107m";
    public static final String BOLD = "\033[1m";
    public static final String FAINT = "\033[2m";
    public static final String ITALIC = "\033[3m";
    public static final String UNDERLINE = "\033[4m";
    public static final String RESET = "\033[0m";

    public static void printLogo() {
        String logo =
                "******   ** ****     **   ********    *******  \n" +
                "/*////** /**/**/**   /**  **//////**  **/////** \n" +
                "/*   /** /**/**//**  /** **      //  **     //**\n" +
                "/******  /**/** //** /**/**         /**      /**\n" +
                "/*//// **/**/**  //**/**/**    *****/**      /**\n" +
                "/*    /**/**/**   //****//**  ////**//**     ** \n" +
                "/******* /**/**    //*** //********  //*******  \n" +
                "///////  // //      ///   ////////    ///////   \n";

        StringBuilder colorfulLogo = new StringBuilder(logo);
        replaceAndColor(colorfulLogo, '*', LIGHTCIAN, RESET);
        replaceAndColor(colorfulLogo, '/', BLUE, RESET);

        System.out.println(colorfulLogo);
        System.out.println("진 사람 올 때 메로나 🍈🍦");
    }

    private static void replaceAndColor(StringBuilder str, char target, String style, String reset) {
        int index = str.indexOf(Character.toString(target));
        while (index != -1) {
            str.replace(index, index + 1, style + target + reset);
            index = str.indexOf(Character.toString(target), index + style.length() + 1 + reset.length());
        }
    }
}
