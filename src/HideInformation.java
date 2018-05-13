import java.io.*;
import java.util.Scanner;

public class HideInformation {

    public void hide() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите имя файла, в котором вы хотите скрыть информацию");
        String fileForHide = scanner.nextLine();
        System.out.println("Введите имя файла, который вы хотите скрыть");
        String fileWithInf = scanner.nextLine();

        try {

            File oldFile = new File(fileForHide);
            File newFile = new File("out.rtf");

            BufferedReader inFileForHide = new BufferedReader(new FileReader(fileForHide));
            BufferedReader inFileForHide1 = new BufferedReader(new FileReader(fileForHide));
            PrintWriter out = new PrintWriter(newFile);
            FileInputStream inFileWithInf = new FileInputStream(fileWithInf);

            int smallCount = 0;
            int bigCount = 0;
            String s1 = inFileForHide.readLine();
            while (s1 != null) {
                for (int i = 0; i < s1.length(); i++) {
                    if (i + 3 < s1.length() && s1.charAt(i) == '{' && s1.charAt(i + 1) == '\\' && s1.charAt(i + 2) == '*') {
                        int j = i + 3;
                        while (true) {
                            if (j == s1.length() || s1.charAt(j) == ';' || s1.charAt(j) == '}') {
                                bigCount++;
                                break;
                            } else if (s1.charAt(j) == ' ' || s1.charAt(j) == '{') {
                                break;
                            }
                            j++;
                        }
                        i = j - 1;
                    } else if (i + 9 < s1.length() && s1.charAt(i) == '{' && s1.charAt(i + 1) == '\\'
                            && (s1.substring(i + 2, i + 2 + 7).equals("comment") || s1.substring(i + 2, i + 2 + 7).equals("subject")
                            || s1.substring(i + 2, i + 2 + 7).equals("version") || s1.substring(i + 2, i + 2 + 7).equals("doccomm"))) {
                        smallCount++;
                        i += 8;
                    } else if (i + 10 < s1.length() && s1.charAt(i) == '{' && s1.charAt(i + 1) == '\\'
                            && (s1.substring(i + 2, i + 2 + 8).equals("operator") || s1.substring(i + 2, i + 2 + 8).equals("keywords")
                            || s1.substring(i + 2, i + 2 + 8).equals("nextfile") || s1.substring(i + 2, i + 2 + 8).equals("nofpages")
                            || s1.substring(i + 2, i + 2 + 8).equals("nofwords") || s1.substring(i + 2, i + 2 + 8).equals("nofchars"))) {
                        smallCount++;
                        i += 9;
                    } else if (i + 8 < s1.length() && s1.charAt(i) == '{' && s1.charAt(i + 1) == '\\'
                            && (s1.substring(i + 2, i + 2 + 6).equals("author") || s1.substring(i + 2, i + 2 + 6).equals("edmins"))) {
                        smallCount++;
                        i += 7;
                    } else if (i + 7 < s1.length() && s1.charAt(i) == '{' && s1.charAt(i + 1) == '\\'
                            && s1.substring(i + 2, i + 2 + 5).equals("title")) {
                        smallCount++;
                        i += 6;
                    } else if (i + 6 < s1.length() && s1.charAt(i) == '{' && s1.charAt(i + 1) == '\\'
                            && s1.substring(i + 2, i + 2 + 4).equals("vern")) {
                        smallCount++;
                        i += 5;
                    }
                }

                s1 = inFileForHide.readLine();
            }

            inFileForHide.close();

            byte[] information = new byte[inFileWithInf.available()];
            inFileWithInf.read(information, 0, inFileWithInf.available());

            String s2 = inFileForHide1.readLine();
            int start = 0;
            int newCount = 0;
            int smallHowMuchOnformationWrite = 0;
            int bigHowMuchOnformationWrite = 0;
            if (smallCount + bigCount > information.length) {
                smallHowMuchOnformationWrite = 1;
                bigHowMuchOnformationWrite = 1;
                newCount = information.length;
            } else {
                smallHowMuchOnformationWrite = 10;
                bigHowMuchOnformationWrite = (information.length - (smallCount * smallHowMuchOnformationWrite)) / bigCount;
                newCount = smallCount + bigCount;
            }

            while (s2 != null) {
                for (int i = 0; i < s2.length(); i++) {
                    if (i + 3 < s2.length() && s2.charAt(i) == '{' && s2.charAt(i + 1) == '\\' && s2.charAt(i + 2) == '*') {
                        int j = i + 3;
                        while (true) {
                            if (j == s2.length() || s2.charAt(j) == ';' || s2.charAt(j) == '}') {
                                String newStringForOut = addInformation(i, s2, newCount, bigHowMuchOnformationWrite, information, start, 3);
                                out.print(newStringForOut);
                                i = newIndex(i, s2, 3);
                                newCount--;
                                start += bigHowMuchOnformationWrite;
                                break;
                            } else if (s2.charAt(j) == ' ' || s2.charAt(j) == '{') {
                                out.print(s2.charAt(i));
                                break;
                            }
                            j++;
                        }
                    } else if (i + 8 < s2.length() && s2.charAt(i) == '{' && s2.charAt(i + 1) == '\\'
                            && (s2.substring(i + 2, i + 2 + 6).equals("author") || s2.substring(i + 2, i + 2 + 6).equals("edmins"))) {
                        String newStringForOut = addInformation(i, s2, newCount, smallHowMuchOnformationWrite, information, start, 8);
                        out.print(newStringForOut);
                        i = newIndex(i, s2, 8);
                        newCount--;
                        start += smallHowMuchOnformationWrite;
                    } else if (i + 7 < s2.length() && s2.charAt(i) == '{' && s2.charAt(i + 1) == '\\'
                            && s2.substring(i + 2, i + 2 + 5).equals("title")) {
                        String newStringForOut = addInformation(i, s2, newCount, smallHowMuchOnformationWrite, information, start, 7);
                        out.print(newStringForOut);
                        i = newIndex(i, s2, 7);
                        newCount--;
                        start += smallHowMuchOnformationWrite;
                    } else if (i + 9 < s2.length() && s2.charAt(i) == '{' && s2.charAt(i + 1) == '\\'
                            && (s2.substring(i + 2, i + 2 + 7).equals("comment") || s2.substring(i + 2, i + 2 + 7).equals("subject")
                            || s2.substring(i + 2, i + 2 + 7).equals("version") || s2.substring(i + 2, i + 2 + 7).equals("doccomm"))) {
                        String newStringForOut = addInformation(i, s2, newCount, smallHowMuchOnformationWrite, information, start, 9);
                        out.print(newStringForOut);
                        i = newIndex(i, s2, 9);
                        newCount--;
                        start += smallHowMuchOnformationWrite;
                    } else if (i + 10 < s2.length() && s2.charAt(i) == '{' && s2.charAt(i + 1) == '\\'
                            && (s2.substring(i + 2, i + 2 + 8).equals("operator") || s2.substring(i + 2, i + 2 + 8).equals("keywords")
                            || s2.substring(i + 2, i + 2 + 8).equals("nextfile") || s2.substring(i + 2, i + 2 + 8).equals("nofpages")
                            || s2.substring(i + 2, i + 2 + 8).equals("nofwords") || s2.substring(i + 2, i + 2 + 8).equals("nofchars"))) {
                        String newStringForOut = addInformation(i, s2, newCount, smallHowMuchOnformationWrite, information, start, 10);
                        out.print(newStringForOut);
                        i = newIndex(i, s2, 10);
                        newCount--;
                        start += smallHowMuchOnformationWrite;
                    } else if (i + 6 < s2.length() && s2.charAt(i) == '{' && s2.charAt(i + 1) == '\\'
                            && s2.substring(i + 2, i + 2 + 4).equals("vern")) {
                        String newStringForOut = addInformation(i, s2, newCount, smallHowMuchOnformationWrite, information, start, 6);
                        out.print(newStringForOut);
                        i = newIndex(i, s2, 6);
                        newCount--;
                        start += smallHowMuchOnformationWrite;
                    } else {
                        out.print(s2.charAt(i));
                    }
                }

                out.println();
                s2 = inFileForHide1.readLine();
            }

            inFileForHide1.close();
            inFileWithInf.close();
            out.close();

            oldFile.delete();
            newFile.renameTo(oldFile);

        } catch (IOException e) {
            System.out.println("Файл не найден");
        }
    }

    private String addInformation(int startPositionForHide, String stringFromFile,
                                  int numberOfBlock, int lengthOfBlock, byte[] information,
                                  int startPositionInInformation, int lengthOfNameBlock) {
        StringBuilder newStringForOut = new StringBuilder();

        newStringForOut.append(stringFromFile.substring(startPositionForHide, startPositionForHide + lengthOfNameBlock));
        if (stringFromFile.charAt(startPositionForHide + lengthOfNameBlock) == ' ')
            newStringForOut.append(" ");
        else if (stringFromFile.charAt(startPositionForHide + lengthOfNameBlock) == '\\')
            newStringForOut.append("\\");

        if (numberOfBlock > 0) {
            if (numberOfBlock == 1)
                lengthOfBlock = information.length - startPositionInInformation;

            for (int j = startPositionInInformation; j < startPositionInInformation + lengthOfBlock; j++) {
                String str = String.valueOf(information[j] + 128);
                while (str.length() < 3)
                    str = "0" + str;
                newStringForOut.append(str);
            }
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
