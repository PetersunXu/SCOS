package es.source.code.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class Tab1Fragment extends Fragment {
    private ListView listView;
    public static ListViewAdapter listViewAdapter;
    private Food food ;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.tab1fragment , container, false);
        listView = (ListView)view.findViewById(R.id.list_view);

        food =(Food)getActivity().getApplication();
        food.getcoldfoodlist();
        listViewAdapter=new ListViewAdapter(getActivity(), food.coldfoodlist);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//这里写ListView中item被点击后进入菜品详单
                Intent intent1 = new Intent(getActivity(),FoodDetailed.class);
                intent1.putExtra("position",position);
                startActivity(intent1);
            }
        });
        return view;
    }


    class ListViewAdapter extends BaseAdapter {

        private List<Map<String, Object>> data;
        private LayoutInflater layoutInflater;
        private Context context;
        public ListViewAdapter(Context context,List<Map<String, Object>> data){
            this.context=context;
            this.data=data;
            this.layoutInflater=LayoutInflater.from(context);
        }

        public final class Component{
            public ImageView image;
            public TextView title;
            public TextView price;
            public TextView foodnum;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            Component component=null;
            if(convertView==null){
                component=new Component();
                //获得组件，实例化组件
                convertView=layoutInflater.inflate(R.layout.listview, null);
                component.image=(ImageView)convertView.findViewById(R.id.image);
                component.title=(TextView)convertView.findViewById(R.id.title);
                component.price=(TextView)convertView.findViewById(R.id.price);
                component.view=(Button)convertView.findViewById(R.id.view);
                component.foodnum=(TextView)convertView.findViewById(R.id.foodnum);
                convertView.setTag(component);
            }else{
                component=(Component)convertView.getTag();
            }
//给组件绑定数据
            component.image.setBackgroundResource((Integer)data.get(position).get("image"));
            component.title.setText((String)data.get(position).get("title"));
            component.price.setText((String)data.get(position).get("price"));
            component.foodnum.setText((String)data.get(position).get("foodnum").toString());

            if(food.isadd[position]){
                component.view.setText("退点");
                component.view.setTextColor(android.graphics.Color.RED);
            }else
            {
                component.view.setText("点菜");
                component.view.setTextColor(Color.BLACK);
            }
            final Button btn = component.view;
            component.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(btn.getText().toString().equals("点菜")) {
                        btn.setText("退点");
                        btn.setTextColor(android.graphics.Color.RED);

                        CharSequence text = "点菜成功!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();

                        final int p=position;

                        food.isadd[p]=true;
                        food.foodnum[p]--;
                        food.getcoldfoodlist();
                        notifyDataSetChanged();

                    }
                    else{
                        btn.setText("点菜");
                        btn.setTextColor(Color.BLACK);

                        final int p=position;

                        food.isadd[p]=false;
                        food.foodnum[p]++;
                        food.getcoldfoodlist();
                        notifyDataSetChanged();
                    }
                }
            });

            return convertView;
        }



    }

}
