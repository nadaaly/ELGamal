import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static int alpha;
    public static int p=997;
    public static ArrayList<Integer> keArray = new ArrayList<>();
    public static int d;

    public static int squareAndMultiply(int m, int e, int n) {
        double y = m;
        String s = Integer.toBinaryString(e);
        for (int i =1; i<s.length();i++){
            y = Math.pow(y,2) % n;
            if (s.charAt(i) =='1'){
                y = (y * m) % n;
            }
        }
        return (int)y;
    }
    public static String fermatPrimeTest(int primeNum) {
        for (int i =1; i<100; i++){
            int a = (int)Math.floor(Math.random()*(primeNum-2-(2+1))+2);
            if(squareAndMultiply(a,primeNum-1,primeNum) != 1){
                return "The number is composite";
            } else {
                return "The number is prime";
            }
        }
        return "0";
    }
    public static int extendedEuclidean(int e, int phi)
    {
        int r1,r0;
        if (e < phi){
            r1 = e;
            r0 = phi;
        }
        else {
            r1 = phi;
            r0 = e;
        }
        List<Integer> r = new ArrayList<>();
        List<Integer> S = new ArrayList<>();
        List<Integer> T = new ArrayList<>();

        r.add(r0);
        r.add(r1);
        int S0 = 1,T1 = 1;
        int S1 = 0 ,T0 = 0;

        S.add(S0);
        S.add(S1);

        T.add(T0);
        T.add(T1);

        int i = 1;
        int temp=0;
        int Q = 0;
        do {
            i++;
            temp = r.get(i-2) % r.get(i-1);
            r.add(temp);
            Q = ( r.get(i-2) - r.get(i) ) / r.get(i-1);

            temp =  S.get(i-2) - (Q  * S.get(i-1));
            S.add(temp);

            temp =  T.get(i-2) - (Q  * T.get(i-1));
            T.add(temp);

        }while(r.get(i) != 0);

        return T.get(i-1);
    }

    /////////////////////////////////////////////////////////
    public static ArrayList<Integer> getDivisors(int order){
        ArrayList<Integer> divisors = new ArrayList<>();
        for (int i =1;i<=order ; i++){
            if(order%i==0){
                divisors.add(i);
            }
        }
        return divisors;
    }
    public static int getAlpha(int p){
        boolean check = true;
        ArrayList<Integer> distinct = new ArrayList<>();
        int order = p-1;
        ArrayList<Integer> divisors = getDivisors(order);
        for(int generator =1; generator<order; generator++){
            for (int j=0; j<divisors.size(); j++){
                int results = squareAndMultiply(generator,divisors.get(j),p);
                if(results == 1 && j != divisors.size()-1){
                    check = false;
                    break;
                } else  {
                    check= true;
                    distinct.add(results);
                }
            }

            if(check && !checkDublicates(distinct)){
                return generator;
            }
        }
        return 0;
    }

    private static boolean checkDublicates(ArrayList<Integer> array) {
        for(int i=0; i<array.size();i++){
            for (int j=i+1; j<array.size();j++){
                if(array.get(i) == array.get(j)){
                    return false;
                }
            }
        }
        return true;
    }

    public static ArrayList<Integer> encrypt(List<Integer> ascii) {
        ArrayList<Integer> cipher = new ArrayList<>();
        // Key generation
        alpha = getAlpha(p);
        d = (int)Math.floor((Math.random()*(p-2) - 2) + 2);

        int beta = squareAndMultiply(alpha,d,p);

        for (Integer integer : ascii) {
            int i = (int) Math.floor((Math.random() * (p - 2) - 2) + 2); // ((Math.random() * (max - min)) + min);
            System.out.println("I: " + i);

            int Ke = squareAndMultiply(alpha, i, p);
            System.out.println("Ephemeral Key: " + Ke);

            int Km = squareAndMultiply(beta, i, p);
            System.out.println("Shared key in Encryption: " + Km);

            int y = (integer * Km) % p;
            cipher.add(y);
            keArray.add(Ke);
        }
        return cipher;
    }
    public static ArrayList<Integer> decrypt(ArrayList<Integer> y) {
        ArrayList<Integer> plaintext = new ArrayList<>();
        for (int j=0; j <y.size(); j++){
            int Km = squareAndMultiply(keArray.get(j),d, p);
            System.out.println("Shared key in Decryption: " + Km);
            int kmInverse = extendedEuclidean(Km,p);
            if (kmInverse<0){
                kmInverse+= p;
            }
            System.out.println("Km Inverse: " + kmInverse);

            int decrypted = (y.get(j)* kmInverse) % p;
            plaintext.add(decrypted);
        }
        return plaintext;
    }

    public static void main(String[] args) {
        ArrayList<Integer> cipher;
        ArrayList<Integer> plainText;

        Scanner input = new Scanner(System.in);
        String msg;

        System.out.println("Enter The message ");
        msg = input.nextLine();
        List<Integer> ascii = new ArrayList<>();
        for (int i=0;i<msg.length();i++)
        {
            ascii.add((int)msg.charAt(i));
        }

        System.out.println("The Prime Number: " + p);

        System.out.print("Z*p = [");
        for (int i = 1; i< p; i++){
            System.out.print(i+",");
        }
        System.out.println("]");

        System.out.println("Fermat Test: " + fermatPrimeTest(p));

        System.out.println("---------------------------");

        System.out.println("Encryption:");
        cipher = encrypt(ascii);

        System.out.println("---------------------------");
        System.out.println("Alpha: " + alpha);
        System.out.println("Private key: " + d);
        System.out.println("---------------------------");

        System.out.println("Cipher Text: ");
        for(int i : cipher){
            System.out.print(Integer.toHexString(i) + " ");
        }
        System.out.println();
        System.out.println("---------------------------");
        System.out.println("Decryption:");
        plainText = decrypt(cipher);
        System.out.println("---------------------------");
        System.out.println("Plain Text: ");
        for(int i : plainText){
            System.out.print(Character.toString(i));
        }
    }
}