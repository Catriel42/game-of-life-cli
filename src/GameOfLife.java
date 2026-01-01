import java.util.Random;
import java.util.Scanner;

public class GameOfLife {

    private boolean[][] grid;
    private int width;
    private int height;
    private int generations;
    private int speed;
    private String population;

    public GameOfLife(int width, int height, int generations, int speed, String population) {
        this.width = width;
        this.height = height;
        this.generations = generations;
        this.speed = speed;
        this.population = population;
        this.grid = new boolean[height][width];
        parseInitialState(population);
    }

    //Método para parsear la p, y me valide también el valor rnd
    private void parseInitialState(String initialState) {
        if (initialState.equalsIgnoreCase("rnd")) {
            generateRandomPopulation();
            return;
        }
        String[] rows = initialState.split("#");
        for (int i = 0; i < rows.length; i++) {
            String row = rows[i];
            for (int j = 0; j < row.length(); j++) {
                if (row.charAt(j) == '1' || row.charAt(j) == '0') {
                    grid[i][j] = row.charAt(j) == '1';
                } else {
                    grid[i][j] = false; // Célula inválida, se le deja muerta
                }
            }
        }
    }

    //Método para generar valores randoms en la grid
    private void generateRandomPopulation() {
        Random random = new Random();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = random.nextBoolean();
            }
        }
    }

    //Método para impirmir en consola los parametros de GOL que proporcionemos, validando con siguientes métodos
    public void printParameters() {
        System.out.println("Width = [" + (isValidWidth() ? width : "Invalido") + "]");
        System.out.println("Height = [" + (isValidHeight() ? height : "Invalido") + "]");
        System.out.println("Generations = [" + (isValidGenerations() ? generations : "No presente") + "]");
        System.out.println("Speed = [" + (isValidSpeed() ? speed : "Invalido") + "]");
        System.out.println("Population = [" + (population.isEmpty() ? "No presente" : (isValidPopulation() ? "´" + population + "`" : "Invalido")) + "]\n");
    }
    //MÉTODOS DE VALIDACIÓN
    private boolean isValidWidth() {
        return width == 10 || width == 20 || width == 40 || width == 80;
    }

    private boolean isValidHeight() {
        return height == 10 || height == 20 || height == 40;
    }

    private boolean isValidGenerations() {
        return generations >= 0;
    }

    private boolean isValidSpeed() {
        return speed == 250 || speed == 1000;
    }

    private boolean isValidPopulation() {
        if (population.equalsIgnoreCase("rnd")) {
            return true;
        }
        // Verificar si la población tiene las dimensiones correctas
        String[] rows = population.split("#");
        for (String row : rows) {
            if (row.length() != width) {
                System.out.println("La población no coincide con el ancho especificado.");
                return false;
            }
        }
        if (rows.length != height) {
            System.out.println("La población no coincide con la altura especificada.");
            return false;
        }

        for (int i = 0; i < population.length(); i++) {
            char ch = population.charAt(i);
            if (ch != '0' && ch != '1' && ch != '#') {
                return false;
            }
        }
        return true;
    }

    //Método especial para saber si todas las células están muertas
    private boolean isGridDead() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (grid[i][j]) {
                    return false; // Si encuentra al menos una célula viva, devuelve false
                }
            }
        }
        return true; // Si no se encuentra ninguna célula viva, devuelve true
    }

    //Método de parseo de los strings en CLI para asignarlos en variables
    public static void main(String[] args) {
        int width = 0;
        int height = 0;
        int generations = 0;
        int speed = 0;
        String population = "";

        for (String arg : args) {
            String[] parts = arg.split("=");
            if (parts.length == 2) {
                switch (parts[0]) {
                    case "w":
                        width = Integer.parseInt(parts[1]);
                        break;
                    case "h":
                        height = Integer.parseInt(parts[1]);
                        break;
                    case "g":
                        generations = Integer.parseInt(parts[1]);
                        break;
                    case "s":
                        speed = Integer.parseInt(parts[1]);
                        break;
                    case "p":
                        population = parts[1];
                        break;
                    default:
                        System.out.println("Parámetro desconocido: " + parts[0]);
                }
            }
        }

        //Instancia de un GOL con los valores parseados y asignados del CLI
        GameOfLife game = new GameOfLife(width, height, generations, speed, population);
        game.printParameters();
        game.runGame();
    }

    //Método principal para correr el juego
    public void runGame() {
        // Verificar si los parámetros son válidos
        if (!isValidWidth() || !isValidHeight() || !isValidGenerations() || !isValidSpeed() || !isValidPopulation()) {
            System.out.println("Los parámetros de entrada no son válidos. El juego no puede comenzar.");
            return;
        }
        Thread userInputThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Presione cualquier tecla para detener el juego...");
            scanner.nextLine();
            scanner.close();
            System.exit(0); // Detener el programa cuando se presiona una tecla
        });

        userInputThread.start(); // Iniciar el hilo para la entrada del usuario

        for (int i = 0; i < generations || generations == 0; i++) {
            System.out.println("Generación " + (i + 1) + ":");
            printGrid();
            nextGeneration();

            // Verificar si todas las células están muertas
            if (isGridDead()) {
                System.out.println("Generación " + (i + 2) + ":");
                printGrid();
                System.out.println("¡GAME OVER!");
                break;
            }

            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    //Método para definir la siguiente generación con las reglas
    private void nextGeneration() {
        boolean[][] newGrid = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int neighbors = countNeighbors(i, j);
                if (grid[i][j]) {
                    if (neighbors < 2 || neighbors > 3) {  // Regla 1 y 2
                        newGrid[i][j] = false;
                    } else {
                        newGrid[i][j] = true;
                    }
                } else {
                    if (neighbors == 3) {   //Regla 3
                        newGrid[i][j] = true;
                    } else {
                        newGrid[i][j] = false;
                    }
                }
            }
        }
        grid = newGrid;
    }

    //Método para contar los vecinos vivos
    private int countNeighbors(int row, int col) {
        int count = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < height && j >= 0 && j < width && !(i == row && j == col) && grid[i][j]) {
                    count++;
                }
            }
        }
        return count;
    }

    //método para imprimir la grid
    private void printGrid() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(grid[i][j] ? "[* ] " : "[  ] ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
