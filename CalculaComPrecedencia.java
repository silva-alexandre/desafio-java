import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
    * Calculadora com ordem de precêdencia e sequencia dos cálculo.
 */
public class CalculaComPrecedencia {

    public static void main(String args[])
    {
        Scanner scanner = new Scanner(System.in);
        CalculaComPrecedencia c = new CalculaComPrecedencia();

        // Loop principal para entrada de expressões até o comando 'sair'
        while (true) {
            System.out.print("Digite a expressão (ou 'sair' para encerrar): ");
            String expressao = scanner.nextLine();
            if (expressao.equalsIgnoreCase("sair")) {
                break;
            }
            System.out.println("Resultado: " + c.calculadora(expressao));
        }
        scanner.close();
    }

    /**
     * Calcula o resultado de uma expressão matemática.
     * @param expressao Expressão matemática em formato String.
     * @return Resultado da expressão ou mensagem de erro caso falhe.
     */
    public String calculadora(String expressao) {
        try {
            double resultado = avaliarExpressao(expressao);
            return String.valueOf(resultado);
        } catch (Exception e) {
            return "Erro ao avaliar a expressão: " + e.getMessage();
        }
    }

    /**
     * Avalia a expressão matemática usando pilhas para números e operadores.
     * @param expressao Expressão matemática a ser avaliada.
     * @return Resultado numérico da expressão.
     */
    private double avaliarExpressao(String expressao) {
        Stack<Double> numeros = new Stack<>();
        Stack<Character> operadores = new Stack<>();

        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i);

            // Captura números e os empilha
            if (Character.isDigit(c)) {
                StringBuilder sb = new StringBuilder();
                while (i < expressao.length() && (Character.isDigit(expressao.charAt(i)) || expressao.charAt(i) == '.')) {
                    sb.append(expressao.charAt(i));
                    i++;
                }
                i--;
                numeros.push(Double.parseDouble(sb.toString()));
            } else if (c == '(') {
                operadores.push(c);
            } else if (c == ')') {
                // Resolve operações dentro dos parênteses
                while (operadores.peek() != '(') {
                    numeros.push(calcular(operadores.pop(), numeros.pop(), numeros.pop()));
                }
                operadores.pop();
            } else if (c == '^') {
                // Lidar com operador de potência
                double base = numeros.pop();
                i++;
                StringBuilder sb = new StringBuilder();
                while (i < expressao.length() && Character.isDigit(expressao.charAt(i))) {
                    sb.append(expressao.charAt(i));
                    i++;
                }
                i--;
                int expoente = Integer.parseInt(sb.toString());
                double resultado = Math.pow(base, expoente);
                System.out.println(base + " ^ " + expoente + " = " + resultado);
                numeros.push(resultado);
            } else if (isOperador(c)) {
                // Resolve operadores com maior ou igual precedência antes de empilhar o novo operador
                while (!operadores.isEmpty() && precedencia(c) <= precedencia(operadores.peek())) {
                    numeros.push(calcular(operadores.pop(), numeros.pop(), numeros.pop()));
                }
                operadores.push(c);
            }
        }

        // Resolve as operações restantes após a leitura completa da expressão
        while (!operadores.isEmpty()) {
            numeros.push(calcular(operadores.pop(), numeros.pop(), numeros.pop()));
        }

        return numeros.pop();
    }

    /**
     * Executa a operação matemática entre dois números.
     * @param operador Operador matemático (+, -, *, /).
     * @param b Segundo número.
     * @param a Primeiro número.
     * @return Resultado da operação.
     */
    private double calcular(char operador, double b, double a) {
        double resultado = 0;
        switch (operador) {
            case '+':
                resultado = a + b;
                System.out.println(a + " + " + b + " = " + resultado);
                break;
            case '-':
                resultado = a - b;
                System.out.println(a + " - " + b + " = " + resultado);
                break;
            case '*':
                resultado = a * b;
                System.out.println(a + " * " + b + " = " + resultado);
                break;
            case '/':
                resultado = a / b;
                System.out.println(a + " / " + b + " = " + resultado);
                break;
        }
        return resultado;
    }

    /**
     * Verifica se o caractere é um operador matemático.
     * @param c Caractere a ser verificado.
     * @return true se for operador, false caso contrário.
     */
    private boolean isOperador(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    /**
     * Retorna a precedência do operador.
     * @param op Operador a ser avaliado.
     * @return Valor da precedência (1 para + e -, 2 para * e /, 3 para ^).
     */
    private int precedencia(char op) {
        return switch (op) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            case '^' -> 3;
            default -> -1;
        };
    }
}
