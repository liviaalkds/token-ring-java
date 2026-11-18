public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        boolean ehMestre = args != null
                && args.length >= 1
                && "0".equals(args[0]);

        try {
            Parametros parametros = new Parametros(args, ehMestre);
            System.out.printf(
                    "id=%d operacao=%d voltas=%d%n",
                    parametros.getId(),
                    parametros.getOperacao(),
                    parametros.getVoltas()
            );
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
