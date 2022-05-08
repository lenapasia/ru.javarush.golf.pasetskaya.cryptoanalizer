package prog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Caesar {

    private static final HashMap<Character[], Double> letterToProbability = new HashMap<Character[], Double>() {{
        put(new Character[]{'а'}, 0.062);
        put(new Character[]{'б'}, 0.014);
        put(new Character[]{'в'}, 0.038);
        put(new Character[]{'г'}, 0.013);
        put(new Character[]{'д'}, 0.025);
        put(new Character[]{'ж'}, 0.007);
        put(new Character[]{'з'}, 0.016);
        put(new Character[]{'и'}, 0.062);
        put(new Character[]{'й'}, 0.01);
        put(new Character[]{'к'}, 0.028);
        put(new Character[]{'л'}, 0.035);
        put(new Character[]{'м'}, 0.026);
        put(new Character[]{'н'}, 0.053);
        put(new Character[]{'о'}, 0.09);
        put(new Character[]{'п'}, 0.023);
        put(new Character[]{'р'}, 0.04);
        put(new Character[]{'с'}, 0.045);
        put(new Character[]{'т'}, 0.053);
        put(new Character[]{'у'}, 0.021);
        put(new Character[]{'ф'}, 0.002);
        put(new Character[]{'х'}, 0.009);
        put(new Character[]{'ц'}, 0.004);
        put(new Character[]{'ч'}, 0.012);
        put(new Character[]{'ш'}, 0.006);
        put(new Character[]{'щ'}, 0.003);
        put(new Character[]{'ы'}, 0.016);
        put(new Character[]{'э'}, 0.003);
        put(new Character[]{'ю'}, 0.006);
        put(new Character[]{'я'}, 0.018);
        put(new Character[]{' '}, 0.174);
        put(new Character[]{'е', 'ё'}, 0.072);
        put(new Character[]{'ь', 'ъ'}, 0.014);
    }};

    public Caesar(List<Character> alphabet) {
        this.alphabet = alphabet;
    }

    private List<Character> alphabet;

    private Character shiftAlphabet(Character character, int shift) {
        int trueShift = shift % alphabet.size();
        int trueIndex = alphabet.indexOf(character) + trueShift;
        if (trueIndex > alphabet.size() - 1) {
            trueIndex = (trueIndex % (alphabet.size() - 1)) - 1;
        } else if (trueIndex < 0) {
            trueIndex = alphabet.size() + trueIndex;
        }
        return alphabet.get(trueIndex);
    }

    public ArrayList<Character> encode(ArrayList<Character> characters, int key) {
        ArrayList<Character> result = new ArrayList<>();

        for (Character character : characters) {
            if (alphabet.contains(character)) {
                result.add(shiftAlphabet(character, key));
            } else {
                result.add(character);
            }
        }
        return result;
    }

    public ArrayList<Character> decode(ArrayList<Character> characters, int key) {
        return encode(characters, -key);
    }

    public int hackKey(ArrayList<Character> encodedMessage) {
        // вычисление ожидаемого количества встречаемости символов по справочной вероятности встречаемости
        double[] expectedQuantities = getExpectedLettersQuantity(encodedMessage);

        Double minChiSquare = null;
        Integer keyMinChiSquare = null;
        for (int key = 0; key < alphabet.size(); key++) {
            ArrayList<Character> decodedMessage = decode(encodedMessage, key);

            // вычисление фактического количества встречаемости символов в декодированном сообщении
            long[] actualQuantities = getActualLettersQuantity(decodedMessage);

            // вычисление Хи квадрата
            double chiSquare = MathUtil.chiSquare(expectedQuantities, actualQuantities);

            // поиск ключа, соответствующего минимальному Хи квадрату
            if (minChiSquare == null || chiSquare < minChiSquare) {
                keyMinChiSquare = key;
                minChiSquare = chiSquare;
            }
        }
        return keyMinChiSquare;
    }


    private double[] getExpectedLettersQuantity(ArrayList<Character> encryptedMessage) {
        ArrayList<Double> expectedQuantities = new ArrayList<>();
        for (Double probability : letterToProbability.values()) {
            double expectedFrequency = probability * encryptedMessage.size();
            expectedQuantities.add(expectedFrequency);
        }

        double[] expected = new double[expectedQuantities.size()];
        for (int i = 0; i < expectedQuantities.size(); i++) {
            expected[i] = expectedQuantities.get(i);
        }
        return expected;
    }

    private long[] getActualLettersQuantity(ArrayList<Character> decodedMessage) {
        long[] actualQuantities = new long[letterToProbability.size()];
        for (int i = 0; i < actualQuantities.length; i++) {
            actualQuantities[i] = 0;
        }


        HashMap<Character, Integer> letterToIndex = new HashMap<>();
        int j = 0;
        for (Map.Entry<Character[], Double> eachEntry : letterToProbability.entrySet()) {
            for (Character character : eachEntry.getKey()) {
                letterToIndex.put(character, j);
            }
            j++;
        }

        for (Character character : decodedMessage) {
            Character lowerCaseChar = Character.toLowerCase(character);
            if (letterToIndex.containsKey(lowerCaseChar)) {
                int index = letterToIndex.get(lowerCaseChar);
                long quantity = actualQuantities[index] + 1;
                actualQuantities[index] = quantity;
            }
        }
        return actualQuantities;
    }
}
