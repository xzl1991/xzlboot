package BlockQueue;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @auther xzl on 15:15 2018/5/11
 */
public class Movie {
    //共20个眼镜
     ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue(20);
     public Movie(){
         //初始化
         for (int i=0;i<20;i++){
             arrayBlockingQueue.add("第="+i+"。个眼镜");
         }
     }
    //生产
    public  void  returnClass(String ticket){
        arrayBlockingQueue.add(ticket);
    }
    //买
    public String tackClass(){
        try {
           return arrayBlockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
