package boxgym.helper;

public class TableViewCount {
    public static String footerMessage(int count, String text) {
        if(count == 1) return "Exibindo " + String.valueOf(count) + " " + text;
        if(count > 1) return "Exibindo " + String.valueOf(count) + " " + text + "s";
        return "Exibindo nenhum " + text;
    }
}
