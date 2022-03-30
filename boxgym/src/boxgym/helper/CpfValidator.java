package boxgym.helper;

public class CpfValidator {

    private static final int[] pesoCpf = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

    public static boolean isValid(String cpf) {
        return isValidCpf(cpf);
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

    private static boolean isValidCpf(String cpf) {
        cpf = cpf.trim().replaceAll("\\D", "");

        if ((cpf == null) || (cpf.length() != 11)) {
            return false;
        }

        for (int j = 0; j < 10; j++) {
            if (padLeft(Integer.toString(j), Character.forDigit(j, 10)).equals(cpf)) {
                return false;
            }
        }

        Integer digito1 = calcularDigito(cpf.substring(0, 9), pesoCpf);
        Integer digito2 = calcularDigito(cpf.substring(0, 9) + digito1, pesoCpf);

        return cpf.equals(cpf.substring(0, 9) + digito1.toString() + digito2.toString());
    }
}
