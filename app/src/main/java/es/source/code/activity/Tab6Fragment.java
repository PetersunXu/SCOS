package es.source.code.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
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

import es.source.code.model.User;

import static es.source.code.activity.Tab1Fragment.listViewAdapter;

//已点菜单的Fragment
public class Tab6Fragment extends Fragment {

    public CustomProgressDialog progressDialog ;
    private Food food;
    private ListView listView;
    private TextView tv_totalprice;
    private TextView tv_num;
    private Button btn_submit;
    private User user;

    public static ListViewAdapter_notorder adapter_notorder;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.tab6fragment , container, false);
        listView = (ListView)view.findViewById(R.id.list_view_order);
        food = (Food)getActivity().getApplication();
        food.get_Submit_list();
        adapter_notorder=new ListViewAdapter_notorder(getActivity(), food.Submit_list);
        listView.setAdapter(adapter_notorder);
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
        btn_submit=view.findViewById(R.id.submit);
        Intent intent = getActivity().getIntent();
        Bundle bundle = intent.getExtras();
        user = (User)bundle.getSerializable("Object");

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final double price=food.getprice()*0.7;
                if (user.getOldUser()) {
                    for (int i = 0; i < food.isadd.length; i++)
                    {
                        food.isadd[i] = false;
                        food.issubmit[i] = false;

                        food.getcoldfoodlist();
                    }

                    listViewAdapter.notifyDataSetChanged();
                    MyClass a=new MyClass();
                    a.execute();

                    progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            String a= "您好，老顾客，本次你可享受7折优惠!您的消费金额是"+price+"rmb";
                            CharSequence text=a;
                            int duration = Toast.LENGTH_SHORT;
                            Toast toast = Toast.makeText(getContext(), text, duration);
                            toast.show();

                            btn_submit.setEnabled(false);
                        }
                    });
                } else
                    {
                    for (int i = 0; i < food.isadd.length; i++)
                    {
                        food.isadd[i] = false;
                        food.issubmit[i] = false;

                        food.getcoldfoodlist();
                        listViewAdapter.notifyDataSetChanged();
                    }
                        CharSequence text = "结账成功!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(getContext(), text, duration);
                        toast.show();
                }
            }
        });

        return view;
    }


    class ListViewAdapter_notorder extends BaseAdapter {

        private List<Map<String, Object>> data;
        private LayoutInflater layoutInflater;
        private Context context;
        public ListViewAdapter_notorder(Context context,List<Map<String, Object>> data){
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
//            final int p=position;
//            final Button btn = component.view;
//            component.view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(btn.getText().toString().equals("点菜")) {
//                        btn.setText("退点");
//                        btn.setTextColor(android.graphics.Color.RED);
//                    }
//                    else{
//                        btn.setText("点菜");
//                        btn.setTextColor(Color.BLACK);
//                    }
//                }
//            });
            return convertView;
        }
    }

    class  MyClass extends AsyncTask<String,Integer,Void> {

        private int progress;
        @Override
        protected void onPreExecute() {
            progressDialog  = new CustomProgressDialog(getContext());
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
//            for (int i= 1; i <101; i++) {
//                publishProgress(i);
//                SystemClock.sleep(60);
//            }

            for (int i= 1; i <4; i++) {
                if (progress < 100){
                    progress += 25;
                    publishProgress(progress);
                }
                SystemClock.sleep(1500);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.cancel();
        }
    }
}
