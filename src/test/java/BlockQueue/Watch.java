package BlockQueue;

/**
 * @auther xzl on 15:23 2018/5/11
 */
public class Watch implements Runnable{
    private Movie movie;
    private String name;
    public Watch(Movie movie, String name){
        this.movie = movie;
        this.name = name;
    }
    @Override
    public void run() {
        String num = movie.tackClass();
        try {
            if (num.indexOf("5")>-1){//5 ,15 观众看的时间长
                Thread.sleep(2000);
                System.out.println(name+"-要多看 2000s:"+num);
//                movie.returnClass(num);
                return;
            }
            System.out.println(name+"===========================-看完了,编号"+num);
//            movie.returnClass(num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
