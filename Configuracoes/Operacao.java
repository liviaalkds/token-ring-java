package Configuracoes;

public final class Operacao {
    private Operacao() {
    }

    public static long aplicar(long token, int operacao) {
        switch (operacao) {
            case 0:
                return Math.addExact(token, 1);
            case 1:
                return Math.multiplyExact(token, 2);
            default:
                throw new IllegalArgumentException("Operação inválida: " + operacao);
        }
    }
}
