package util;

public class Tui {
    public static final String GREEN = "\033[32m";
    public static final String BLUE = "\033[34m";
    public static final String MAGENTA = "\033[35m";
    public static final String LIGHTCIAN = "\033[96m";
    public static final String BOLD = "\033[1m";
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
