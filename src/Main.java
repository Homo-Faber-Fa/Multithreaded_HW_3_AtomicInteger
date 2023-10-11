import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static Random random = new Random();
    static AtomicInteger lengthThree = new AtomicInteger();
    static AtomicInteger lengthFour = new AtomicInteger();
    static AtomicInteger lengthFive = new AtomicInteger();
    final static String TEXT = "Красивых слов с длиной  %d: %d шт‚\n";


    public static void main(String[] args) {
        String[] nicknames = new String[100_000];
        for (int i = 0; i < nicknames.length; i++) {
            nicknames[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread one = new Thread(() -> {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < nicknames.length; i++) {
                stringBuilder.append(nicknames[i]);
                if (nicknames[i].equals(stringBuilder.reverse().toString())) {
                    addToAtomics(nicknames.length);
                }
                stringBuilder.setLength(0);
            }
        });

        Thread two = new Thread(() -> {
            String str;
            for (int i = 0; i < nicknames.length; i++) {
                str = nicknames[i].replaceAll(String.valueOf(nicknames[i].charAt(0)), "");
                if (str.length() == 0) {
                    addToAtomics(nicknames.length);
                }
            }
        });

        Thread three = new Thread(() -> {
            boolean beautifulNickname;
            for (int i = 0; i < nicknames.length; i++) {
                beautifulNickname = true;
                char[] charsOfNickname = nicknames[i].toCharArray();
                for (int j = 1; j < charsOfNickname.length; j++) {
                    if (charsOfNickname[j - 1] > charsOfNickname[j]) {
                        beautifulNickname = false;
                        break;
                    }
                }
                if (beautifulNickname) {
                    addToAtomics(nicknames[i].length());
                }
            }
        });

        one.start();
        two.start();
        three.start();

        new Thread(() -> {
            try {
                one.join();
                two.join();
                three.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.printf(TEXT, 3, lengthThree.get());
            System.out.printf(TEXT, 4, lengthFour.get());
            System.out.printf(TEXT, 5, lengthFive.get());
        }).start();
    }

    public static String generateText(String letters, int length) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void addToAtomics(int lenth) {
        if (lenth == 3) {
            lengthThree.incrementAndGet();
        } else if (lenth == 4) {
            lengthFour.incrementAndGet();
        } else if (lenth == 5) {
            lengthFive.incrementAndGet();
        }
    }
}

