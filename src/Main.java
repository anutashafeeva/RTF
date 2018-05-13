import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        new Main().start();
    }
    private void start() throws FileNotFoundException {

        Scanner sc = new Scanner(System.in);
        System.out.println("1. Скрыть информацию");
        System.out.println("2. Получить скрытую информацию");
        int meth = sc.nextInt();

        switch (meth){
            case 1:{
                HideInformation hi = new HideInformation();
                hi.hide();
                break;
            }
            case 2:{
                GetInformation gi = new GetInformation();
                gi.getInf();
                break;
            }
            default:{
                System.out.println("Неверная команда");
            }
        }
    }
}
