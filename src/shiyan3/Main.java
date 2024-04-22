package shiyan3;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Main {
    public static ArrayList<Integer> FCFS(int[] read_list,int position){
    ArrayList<Integer> move_list = new ArrayList<>();
    int total_time = 0 ;
    total_time += Math.abs(position - read_list[0]);
    for(int i = 0 ; i < read_list.length - 1 ; i ++){
        total_time += Math.abs(read_list[i] - read_list[i+1]);
        move_list.add(read_list[i]);
    }
    move_list.add(read_list[read_list.length - 1]);
    System.out.println("移动顺序" + move_list);
    System.out.println("寻址长度：" + position);
    return move_list;
    }

    public static ArrayList<Integer> SSTF(int[] read_list,int position){
        ArrayList<Integer> move_list = new ArrayList<>();
        int totaltime = 0 ;
        ArrayList<Integer> pos = new ArrayList<>();
        for(int i = 0 ; i < read_list.length ; i ++){
            pos.add(read_list[i]);
        }

        while(!pos.isEmpty()) {
            int closestIndex = 0;
            int minDistant = Integer.MAX_VALUE;
            for (int i = 0 ; i < pos.size() ; i ++){
                int distant = Math.abs(pos.get(i) - position);
                if(distant < minDistant){
                    minDistant = distant;
                    closestIndex = i;
                }
            }
            totaltime +=minDistant;
            position = pos.get(closestIndex);
            move_list.add(position);
            pos.remove(closestIndex);
        }
        System.out.println("移动顺序" + move_list);
        System.out.println("寻址长度：" + position);
        return move_list;
    }

    public static ArrayList<Integer> SCAN(int[] read_list,int postion,int direct){
        ArrayList<Integer> move_list = new ArrayList<>();
        int totaltime = 0 ;
        ArrayList<Integer> left_side = new ArrayList<>();
        ArrayList<Integer> right_side = new ArrayList<>();

        for(int num : read_list){
            if(num < postion){
                left_side.add(num);
            }else{
                right_side.add(num);
            }
        }
        if(direct == -1){
            Collections.sort(left_side);
            Collections.reverse(left_side);
        }
        return move_list;
    }

    public static void main(String[] args){
        int[] read_list = {18,38,39,55,90,150,160,184};
        int position = 100 ;

        System.out.println("FCFS结果：" );
        FCFS(read_list,position);
        System.out.println("SSTF结果：" );
        SSTF(read_list,position);

    }

}
