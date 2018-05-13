import java.io.*;
import java.util.Scanner;

public class GetInformation {

    public void getInf() throws FileNotFoundException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Введите имя файла, в котором наодится скрытая информация");
        String fileWithInf = scanner.nextLine();
        String path = fileWithInf.substring(0, fileWithInf.lastIndexOf('\\') + 1);

        try {

            BufferedReader inFileWithInf = new BufferedReader(new FileReader(fileWithInf));
            StringBuilder answer = new StringBuilder();

            String s2 = inFileWithInf.readLine();
            while (s2 != null) {
                for (int i = 0; i < s2.length(); i++) {
                    if (i + 3 < s2.length() && s2.charAt(i) == '{' && s2.charAt(i + 1) == '\\'
                            && s2.charAt(i + 2) == '*') {
                        int j = i + 3;
                        while (true) {
                            if (j == s2.length() || s2.charAt(j) == ';' || s2.charAt(j) == '}') {
                                answer.append(takeInformation(i, s2, 3));
                                i = newIndex(i, s2, 3);
                                break;
                            } else if (s2.charAt(j) == ' ' || s2.charAt(j) == '{') {
                                break;
                            }
                            j++;
                        }
                    } else if (i + 8 < s2.length() && s2.charAt(i) == '{' && s2.charAt(i + 1) == '\\'
                            && (s2.substring(i + 2, i + 2 + 6).equals("author") || s2.substring(i + 2, i + 2 + 6).equals("edmins"))) {
                        answer.append(takeInformation(i, s2, 8));
                        i = newIndex(i, s2, 8);
                    } else if (i + 7 < s2.length() && s2.charAt(i) == '{' && s2.charAt(i + 1) == '\\'
                            && s2.substring(i + 2, i + 2 + 5).equals("title")) {
                        answer.append(takeInformation(i, s2, 7));
                        i = newIndex(i, s2, 7);
                    } else if (i + 9 < s2.length() && s2.charAt(i) == '{' && s2.charAt(i + 1) == '\\'
                            && (s2.substring(i + 2, i + 2 + 7).equals("comment") || s2.substring(i + 2, i + 2 + 7).equals("subject")
                            || s2.substring(i + 2, i + 2 + 7).equals("version") || s2.substring(i + 2, i + 2 + 7).equals("doccomm"))) {
                        answer.append(takeInformation(i, s2, 9));
                        i = newIndex(i, s2, 9);
                    } else if (i + 10 < s2.length() && s2.charAt(i) == '{' && s2.charAt(i + 1) == '\\'
                            && (s2.substring(i + 2, i + 2 + 8).equals("operator") || s2.substring(i + 2, i + 2 + 8).equals("keywords")
                            || s2.substring(i + 2, i + 2 + 8).equals("nextfile") || s2.substring(i + 2, i + 2 + 8).equals("nofpages")
                            || s2.substring(i + 2, i + 2 + 8).equals("nofwords") || s2.substring(i + 2, i + 2 + 8).equals("nofchars"))) {
                        answer.append(takeInformation(i, s2, 10));
                        i = newIndex(i, s2, 10);
                    } else if (i + 6 < s2.length() && s2.charAt(i) == '{' && s2.charAt(i + 1) == '\\'
                            && s2.substring(i + 2, i + 2 + 4).equals("vern")) {
                        answer.append(takeInformation(i, s2, 6));
                        i = newIndex(i, s2, 6);
                    }
                }

                s2 = inFileWithInf.readLine();
            }

            inFileWithInf.close();

            byte[] information = new byte[answer.length() / 3];
            for (int i = 0; i < answer.length(); i += 3) {
                int number = Integer.parseInt(answer.substring(i, i + 3)) - 128;
                information[i / 3] = Byte.parseByte(String.valueOf(number));
            }

            String nameFile = path + "hideInformation";
            try (FileOutputStream fos = new FileOutputStream(nameFile)){
                fos.write(information);
            }

        } catch (IOException e) {
            System.out.println("Файл не найден");
        }
    }

    private String takeInformation(int startPositionForHide, String stringFromFile, int lengthOfNameBlock) {
        StringBuilder newStringForOut = new StringBuilder();
        int endIndex = startPositionForHide + lengthOfNameBlock;

        if (stringFromFile.charAt(startPositionForHide + lengthOfNameBlock) == ' ' || stringFromFile.charAt(startPositionForHide + lengthOfNameBlock) == '\\')
            endIndex++;

        while (true) {
            if (endIndex == stringFromFile.length() || stringFromFile.charAt(endIndex) == ';' || stringFromFile.charAt(endIndex) == '}')
                break;
            newStringForOut.append(stringFromFile.charAt(endIndex));
            endIndex++;
        }

        return newStringForOut.toString();
    }

    private int newIndex(int startPositionForHide, String stringFromFile, int lengthOfNameBlock) {
        int endIndex = startPositionForHide + lengthOfNameBlock;
        while (true) {
            if (endIndex == stringFromFile.length() || stringFromFile.charAt(endIndex) == ';' || stringFromFile.charAt(endIndex) == '}')
                break;
            endIndex++;
        }

        return endIndex - 1;
    }
}
