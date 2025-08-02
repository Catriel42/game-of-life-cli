public class Main {
    // Variables para almacenar los parámetros
    static int width;
    static int height;
    static int generations;
    static int speed;
    static String population;
    static int neighborhood = 3; // Valor por defecto para neighborhood

    // Variables para rastrear la validez de los parámetros
    static boolean isWidthValid = false;
    static boolean isHeightValid = false;
    static boolean isGenerationsValid = false;
    static boolean isSpeedValid = false;
    static boolean isPopulationValid = false;
    static boolean isNeighborhoodValid = true; // Por defecto es válido (valor 3)

    public static void main(String[] args) {
        parseArguments(args);
        printResults();
        printInitialPopulation(population, width, height);
    }

    // Metodo para validar el ancho (10, 20, 40, 80)
    private static boolean isValidWidth(int width) {
        return width == 10 || width == 20 || width == 40 || width == 80;
    }

    // Metodo para validar la altura (10, 20, 40)
    private static boolean isValidHeight(int height) {
        return height == 10 || height == 20 || height == 40;
    }

    // Metodo para validar el número de generaciones (mayor o igual a 0)
    private static boolean isValidGenerations(int generations) {
        return generations >= 0;
    }

    // Metodo para validar la velocidad (250, 1000 milisegundos)
    private static boolean isValidSpeed(int speed) {
        return speed == 250 || speed == 1000;
    }

    // Metodo para validar la configuración inicial de la población (solo contiene '0', '1' y '#')
    private static boolean isValidPopulation(String population) {
        for (int i = 0; i < population.length(); i++) {
            char symbol = population.charAt(i);
            if (symbol != '0' && symbol != '1' && symbol != '#') {
                return false;
            }
        }
        return true;
    }

    // Metodo para validar el número de vecindarios (1, 2, 3, 4, 5)
    private static boolean isValidNeighborhood(int neighborhood) {
        return neighborhood >= 1 && neighborhood <= 5;
    }

    private static void parseArguments(String[] args) {
        for (String arg : args) {
            String[] parts = arg.split("=");
            if (parts.length != 2) {
                continue;
            }

            String key = parts[0];
            String value = parts[1];

            switch (key) {
                case "w":
                    try {
                        width = Integer.parseInt(value);
                        isWidthValid = isValidWidth(width);
                    } catch (NumberFormatException e) {
                        isWidthValid = false; // Inválido
                    }
                    break;
                case "h":
                    try {
                        height = Integer.parseInt(value);
                        isHeightValid = isValidHeight(height);
                    } catch (NumberFormatException e) {
                        isHeightValid = false; // Inválido
                    }
                    break;
                case "g":
                    try {
                        generations = Integer.parseInt(value);
                        isGenerationsValid = isValidGenerations(generations);
                    } catch (NumberFormatException e) {
                        isGenerationsValid = false; // Inválido
                    }
                    break;
                case "s":
                    try {
                        speed = Integer.parseInt(value);
                        isSpeedValid = isValidSpeed(speed);
                    } catch (NumberFormatException e) {
                        isSpeedValid = false; // Inválido
                    }
                    break;
                case "p":
                    population = value.replace("\"", ""); // Eliminar comillas si las hay
                    isPopulationValid = isValidPopulation(population);
                    break;
                case "n":
                    try {
                        neighborhood = Integer.parseInt(value);
                        isNeighborhoodValid = isValidNeighborhood(neighborhood);
                    } catch (NumberFormatException e) {
                        // Si no se ingresa un valor válido, se mantiene el valor por defecto (3)
                        isNeighborhoodValid = true; // El valor por defecto es válido
                    }
                    break;
                default:
                    // Ignorar argumentos desconocidos
                    break;
            }
        }
    }

    private static void printResults() {
        System.out.println("width = [" + (isWidthValid ? width : (width == 0 ? "No Presente" : "Invalido")) + "]");
        System.out.println("height = [" + (isHeightValid ? height : (height == 0 ? "No Presente" : "Invalido")) + "]");
        System.out.println("generations = [" + (isGenerationsValid ? generations : (generations == 0 ? "No Presente" : "Invalido")) + "]");
        System.out.println("speed = [" + (isSpeedValid ? speed : (speed == 0 ? "No Presente" : "Invalido")) + "]");
        System.out.println("population = [" + (isPopulationValid ? "'" + population + "'" : (population == null ? "No Presente" : "Invalido")) + "]");
        System.out.println("neighborhood = " + (isNeighborhoodValid ? neighborhood : (neighborhood == 0 ? "No Presente" : "Invalido")));
    }

    public static void printInitialPopulation(String population, int width, int height) {
        System.out.println("Poblacion Inicial: ");
        String[] rows = population.split("#");
        if( isValidHeight(height) && isValidWidth(width) && isValidPopulation(population)) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (i < rows.length && j < rows[i].length()) {
                        System.out.print(rows[i].charAt(j) + " ");
                    } else {
                        System.out.print("0 ");
                    }
                }
                System.out.println();
            }
        }
        else{
            System.out.println("No se puede generar la poblacion");
        }
    }
}