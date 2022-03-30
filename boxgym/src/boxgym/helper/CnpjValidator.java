package boxgym.helper;

public class CnpjValidator {

    private static final int[] pesoCnpj = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    public static boolean isValid(String cnpj) {
        return isValidCnpj(cnpj);
    }

    private static int calcularDigito(String str, int[] peso) {
        int soma = 0;

        for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
            digito = Integer.parseInt(str.substring(indice, indice + 1));
            soma += digito * peso[peso.length - str.length() + indice];
        }
        soma = 11 - soma % 11;

        return soma > 9 ? 0 : soma;
    }

    private static String padLeft(String text, char character) {
        return String.format("%11s", text).replace(' ', character);
    }

    private static boolean isValidCnpj(String cnpj) {
        cnpj = cnpj.trim().replaceAll("\\D", "");

        if ((cnpj == null) || (cnpj.length() != 14)) {
            return false;
        }

        Integer digito1 = calcularDigito(cnpj.substring(0, 12), pesoCnpj);
        Integer digito2 = calcularDigito(cnpj.substring(0, 12) + digito1, pesoCnpj);

        return cnpj.equals(cnpj.substring(0, 12) + digito1.toString() + digito2.toString());
    }
}
