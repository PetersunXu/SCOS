package es.source.code.activity;

import android.app.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Food extends Application{
    public int[] icon={R.drawable.cold_doufu,R.drawable.cold_niurou,R.drawable.cold_lapi,R.drawable.cold_shala,R.drawable.cold_maodou,
    R.drawable.hotdish_jichi,R.drawable.hotdish_paigu,R.drawable.hotdish_xiaren,R.drawable.hotdish_chashao,R.drawable.hotdish_hongshaoyu};
    public String[] icon_name={"豆腐","牛肉","拉皮","沙拉","毛豆","鸡翅","排骨","虾仁","叉烧","红烧鱼"};
    public int[] price ={10,20,12,15,10,20,25,30,15,20};
    public boolean[] isadd={false,false,false,false,false,false,false,false,false,false};
    public boolean[] issubmit={false,false,false,false,false,false,false,false,false,false};
    public int[] foodnum={5,5,5,5,5,5,5,5,5,5};
    public List<Map<String, Object>> Not_Submit_list;
    public List<Map<String, Object>> Submit_list;
    public  List<Map<String, Object>> foodlist;
    public  List<Map<String, Object>> coldfoodlist;
    public  List<Map<String, Object>> hotfoodlist;
    public  List<Map<String, Object>> updatefoodlist=new ArrayList<Map<String, Object>>();

    public  int getprice()
    {
        int p=0;
        for(int i=0;i<10;i++)
        {
            if(isadd[i]==true)
                p=p+price[i];
        }
        return p;
    }

    public int getnum()
    {
        int num=0;
        for(int i=0;i<10;i++)
        {
            if(isadd[i]==true)
            num++;
        }
        return num;
    }

    public   void get_Submit_list(){
        if(Submit_list == null){
            //创建数据源
            Submit_list = new ArrayList<Map<String, Object>>();
        }else{
            //清空数据源
            Submit_list.clear();
        }
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (issubmit[i]) {
                map.put("image", icon[i]);
                map.put("title", icon_name[i]);
                map.put("price", price[i] + "rmb");
                Submit_list.add(map);
            }
        }

    }

    public   void get_notSubmit_list(){
        if(Not_Submit_list == null){
            //创建数据源
            Not_Submit_list = new ArrayList<Map<String, Object>>();
        }else{
            //清空数据源
            Not_Submit_list.clear();
        }
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (!issubmit[i]&&isadd[i]) {
                map.put("image", icon[i]);
                map.put("title", icon_name[i]);
                map.put("price", price[i] + "rmb");
                Not_Submit_list.add(map);
            }
        }

    }

    public   void getfoodlist(){
        if(foodlist == null){
            //创建数据源
            foodlist = new ArrayList<Map<String, Object>>();
        }else{
            //清空数据源
            foodlist.clear();
        }

        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("title", icon_name[i]);
            map.put("price",price[i]+"rmb");
            map.put("isadd",isadd[i]);
            map.put("foodnum",foodnum[i]+"件");
            foodlist.add(map);
        }
    }

    public   void getcoldfoodlist(){
        if(coldfoodlist == null){
            //创建数据源
            coldfoodlist = new ArrayList<Map<String, Object>>();
        }else{
            //清空数据源
            coldfoodlist.clear();
        }

        for (int i = 0; i < 5; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("title", icon_name[i]);
            map.put("price",price[i]+"rmb");
            map.put("isadd",isadd[i]);
            map.put("foodnum",foodnum[i]+"件");
            coldfoodlist.add(map);
        }
    }

    public   void gethotlist(){
        if(hotfoodlist == null){
            //创建数据源
            hotfoodlist = new ArrayList<Map<String, Object>>();
        }else{
            //清空数据源
            hotfoodlist.clear();
        }

        for (int i = 0; i < 5; i++) {
            Map<String, Object> map=new HashMap<String, Object>();
            map.put("image", icon[i+5]);
            map.put("title", icon_name[i+5]);
            map.put("price",price[i+5]+"rmb");
            map.put("isadd",isadd[i+5]);
            map.put("foodnum",foodnum[i+5]+"件");
            hotfoodlist.add(map);
        }
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }


    public void setIsadd(boolean b,int position)
    {
        if (b) isadd[position] = true;
        else isadd[position] = false;
    }
}
