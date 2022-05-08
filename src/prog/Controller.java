package prog;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Controller {
    private static final List<Character> ALPHABET = Arrays.asList(
            'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К',
            'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я',

            'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к',
            'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я',

            '.', ',', '«', '»', ':', '!', '?', ' ');

    public void run() throws Exception {

        boolean noExit = true;
        while (noExit) {
            int numberPosition = 0;
            try {
                System.out.println("Пожалуйста, выберете операцию, которую необходимо выполнить:");
                System.out.println("1. Шифрование");
                System.out.println("2. Расшифровка");
                System.out.println("3. Криптоанализ");
                System.out.println("4. ВЫХОД");

                Scanner console = new Scanner(System.in);
                numberPosition = console.nextInt();
            } catch (Exception e) {
                System.out.println("Неправильнный ввод операции");
            }
            String inputFilePath;
            int secretKey;
            ArrayList<Character> input;
            ArrayList<Character> result;
            switch (numberPosition) {
                case 1:
                    inputFilePath = getInputFilePath();
                    secretKey = getSecretKey();

                    input = convertFileToCharList(inputFilePath);
                    result = new Caesar(ALPHABET).encode(input, secretKey);
                    saveOutFile(result, inputFilePath, "_encoded");

                    break;
                case 2:
                    inputFilePath = getInputFilePath();
                    secretKey = getSecretKey();

                    input = convertFileToCharList(inputFilePath);
                    result = new Caesar(ALPHABET).decode(input, secretKey);
                    saveOutFile(result, inputFilePath, "_decoded");
                    break;
                case 3:
                    inputFilePath = getInputFilePath();

                    input = convertFileToCharList(inputFilePath);
                    int hackedKey = new Caesar(ALPHABET).hackKey(input);
                    System.out.println("Ключ, подобранный криптоанализом = " + hackedKey);
                    result = new Caesar(ALPHABET).decode(input, hackedKey);
                    saveOutFile(result, inputFilePath, "_hacked");
                    break;

                case 4:
                    noExit = false;
                    break;
            }
        }
    }

    private int getSecretKey() {
        int secretKey = 0;
        boolean validKey;
        do {
            try {
                System.out.println("Пожалуйста, введите ключ в формате числового значения");
                Scanner key = new Scanner(System.in);
                secretKey = key.nextInt();
                validKey = false;
            } catch (Exception e) {
                System.out.println("Ключ должен быть числовым значением");
                validKey = true;
            }
        } while (validKey);
        return secretKey;
    }

    private String getInputFilePath() {
        System.out.println("Пожалуйста, укажите путь к файлу");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private ArrayList<Character> convertFileToCharList(String filePath) throws IOException {
        ArrayList<Character> result = new ArrayList<>();
        try (FileReader reader = new FileReader(filePath)) {
            char[] buffer = new char[65536];
            while (reader.ready()) {
                int real = reader.read(buffer);

                for (int i = 0; i < real; i++) {
                    result.add(buffer[i]);
                }
            }
        }
        return result;
    }

    private void saveOutFile(ArrayList<Character> data, String inputFilePath, String fileNameSuffix) throws IOException {

        Path file = Path.of(inputFilePath);
        String fileNameWithExtension = file.getFileName().toString();
        int pos = fileNameWithExtension.lastIndexOf(".");
        StringBuilder stringBuilder = new StringBuilder(fileNameWithExtension);
        String fileName = stringBuilder.insert(pos, fileNameSuffix).toString();
        String parentPath = Path.of(inputFilePath).getParent().toString();
        String outputFilePath = Paths.get(parentPath, fileName).toString();

        try (FileWriter writer = new FileWriter(outputFilePath)) {
            for (int i = 0; i < data.size(); i++) {
                char ch = data.get(i);
                writer.write(ch);
            }
        }
        System.out.println("Файл сохранен: " + outputFilePath);
    }
}

