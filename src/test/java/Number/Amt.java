package Number;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @auther xzl on 11:11 2018/5/11
 */
public class Amt {
    public static void main(String[] args) {
        String amt = "0.0";
        String amt1 = "30.064900";
        BigDecimal bigDecimal = new BigDecimal(amt);
        BigDecimal bigDecimal1 = new BigDecimal(amt1);
        System.out.println(amt+" ====== "+(bigDecimal.intValue() == 0));
        System.out.println(amt+" ====== "+(bigDecimal1.intValue() == 0)+"===实际值--"+bigDecimal1);

        String re_StrTime = null;

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        // 例如：cc_time=1291778220
        long lcc_time = Long.valueOf("081132");
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        System.out.println(re_StrTime);
    }
}
