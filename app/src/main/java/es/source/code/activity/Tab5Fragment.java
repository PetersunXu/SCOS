package es.source.code.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.List;
import java.util.Map;

import es.source.code.model.User;

import static es.source.code.activity.Tab6Fragment.adapter_notorder;

//已点菜单的Fragment
public class Tab5Fragment extends Fragment {

    private Food food;
    private TextView tv_totalprice;
    private TextView tv_num;
    private ListView listView;
    private Button btn_passorder;
    private User user;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.tab5fragment , container, false);
        listView = (ListView)view.findViewById(R.id.list_view_order);
        food = (Food)getActivity().getApplication();
        food.get_notSubmit_list();
        final ListViewAdapter_order adapter=new ListViewAdapter_order(getActivity(), food.Not_Submit_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//这里写ListView中item被点击后进入菜品详单
                Intent intent1 = new Intent(getActivity(),FoodDetailed.class);
                startActivity(intent1);
            }
        });

        tv_totalprice=view.findViewById(R.id.totalprice);
        tv_totalprice.setText("菜品价格："+food.getprice()+"rmb");
        tv_num=view.findViewById(R.id.totalnum);
        tv_num.setText("订单总量："+food.getnum()+"件");
        btn_passorder=view.findViewById(R.id.pass);
        btn_passorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<food.isadd.length;i++)
                {
                    if (food.isadd[i]==true) {
                        food.issubmit[i] = true;
                    }
                }

                food.Not_Submit_list.clear();
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("image", food.icon[4]);
//                map.put("title", food.icon_name[4]);
//                map.put("price", food.price[4] + "rmb");
//                food.Submit_list.add(map);
                food.get_Submit_list();
                adapter_notorder.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    class ListViewAdapter_order extends BaseAdapter {

        private List<Map<String, Object>> data;
        private LayoutInflater layoutInflater;
        private Context context;

        public ListViewAdapter_order(Context context,List<Map<String, Object>> data){
            this.context=context;
            this.data = data;
            this.layoutInflater=LayoutInflater.from(context);
        }
        /**
         * 组件集合，对应list.xml中的控件
         */
        public final class Component{
            public ImageView image;
            public TextView title;
            public TextView price;
            public Button view;
        }
        @Override
        public int getCount() {
            return data.size();
        }
        /**
         * 获得某一位置的数据
         */
        @Override
        public Object getItem(int position) {
            return data.get(position);
        }
        /**
         * 获得唯一标识
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Component component=null;
            if(convertView==null){
                component=new Component();
                //获得组件，实例化组件
                convertView=layoutInflater.inflate(R.layout.listview_notorder, null);
                component.image=(ImageView)convertView.findViewById(R.id.image);
                component.title=(TextView)convertView.findViewById(R.id.title);
                component.price=(TextView)convertView.findViewById(R.id.price);
                convertView.setTag(component);
            }else{
                component=(Component)convertView.getTag();
            }
            //绑定数据
            component.image.setBackgroundResource((Integer)data.get(position).get("image"));
            component.title.setText((String)data.get(position).get("title"));
            component.price.setText((String)data.get(position).get("price"));

//            notifyDataSetChanged();
            return convertView;
        }
    }
}
