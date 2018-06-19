import com.utils.DateUtils;

import java.util.Date;

/**
 * @auther xzl on 13:33 2018/5/23
 */
public class TestDate {
    public static void main(String[] args) {
       String da =  DateUtils.getLastDayOfMonth("2018-02-23");
        System.out.println(da);
        System.out.println(DateUtils.getDayFromToday(new Date(),0));
        System.out.println("--------------");
//        System.out.println("--------------"+countPrimes(499979));//499979

        System.out.println( sout(499979));
    }
    public static int sout(int n){
        boolean[] isPrime = new boolean[n];
        for (int i = 2; i < n; i++) {
            isPrime[i] = true;
        }
        // Loop's ending condition is i * i < n instead of i < sqrt(n)
        // to avoid repeatedly calling an expensive function sqrt().
        for (int i = 2; i * i < n; i++) {
            if (!isPrime[i]) continue;
            for (int j = i * i; j < n; j += i) {
                isPrime[j] = false;
            }
        }
        int count = 0;
        for (int i = 2; i < n; i++) {
            if (isPrime[i]) count++;
        }
        return count;
    }

    public static int countPrimes(int n) {
        int value = 0;
        if(n<2) return value;
        for(int i=2;i<n;i++){
            int temp = 0;
            for(int j=2;j<=i/2;j++){
                if (temp>=1) break;
                if(i%j==0){
                    temp++;
                }
            }
            if (temp<1){
                value++;
            }
        }
        return value;
    }
}
