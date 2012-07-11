package Test;


/**
 *
 * @author HOANG TRUONG DINH
 */
public class test1 {

    public static void main(String[] args) {
//        for (int i = 0; i < 9; i++) {
//            int n = 0;
//            if (i < 5) {
//                n = i;
//            } else if (i >= 5) {
//                n = 8 - i;
//            }
//            for (int j = 0; j < 4 - n; j++) {
//                System.out.printf("%4s", " ");
//            }
//            for (int j = 0; j < n + 1; j++) {
//                if (!(j == 4 && i == 4)) {
//                    System.out.print(j+4+","+i+" ");
//                } else {
//                    System.out.printf("%4s", " ");
//                }
//            }
//            for (int j = 1; j < n + 1; j++) {
//                    System.out.print(j+4+","+i+" ");
//            }
//            for (int j = 1; j < 4 - n; j++) {
//                System.out.printf("%4s", " ");
//            }
//            System.out.print("\n\n");
//        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (!(j == 4 && i == 4) && ((i < 5) && (j >= 4 - i) && (j <= 4 + i) || ((i >= 5) && (j >= i - 4) && (j < 9 - i + 4)))) {
//                    System.out.print(j + "," + i + " ");
                    //System.out.printf("%4s", "*");
                } else {
//                    System.out.printf("%4s", " ");
                }
            }
            System.out.print("\n\n");
        }
    }
}
