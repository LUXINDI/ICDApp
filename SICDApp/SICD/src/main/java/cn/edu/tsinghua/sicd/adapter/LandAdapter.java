package cn.edu.tsinghua.sicd.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.*;

import org.w3c.dom.Text;

import java.util.List;

import cn.edu.tsinghua.sicd.R;
import cn.edu.tsinghua.sicd.models.LandTransportSelectResult;

/**
 * Created by douglaschan on 2016/3/11.
 */

public class LandAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    //得到一个LayoutInfalter对象用来导入布局
    private  int selectedPosition=0;

    private List<LandTransportSelectResult> items;

    private Context context;

    /**
     * 构造函数
     */
    public LandAdapter(Context context, List<LandTransportSelectResult> items) {
        this.mInflater = LayoutInflater.from(context);
        this.items=items;
        this.context=context;

    }

    @Override
    public int getCount() {
        return items.size();//返回数组的长度
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    /**
     * 书中详细解释该方法
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //观察convertView随ListView滚动情况

        Log.v("MyListViewBase", "getView " + position + " " + convertView);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_land, null);
            holder = new ViewHolder();
            /**得到各个控件的对象*/
            holder.textView1 = (TextView) convertView.findViewById(R.id.text1);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image1
            );
            holder.textView2 = (TextView) convertView.findViewById(R.id.text2);

            convertView.setTag(holder);//绑定ViewHolder对象

        } else {
            holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
        }


        holder.textView1.setText(items.get(position).getKeyword2());
        holder.textView2.setText(items.get(position).getCode());
        holder.textView2.setVisibility(View.GONE);

        holder.textView1.setOnClickListener(new MyOnClickListener(holder.textView2));



        holder.imageView.setImageResource(R.drawable.icon_section);

        Log.d("home", "render..." + position);

        return convertView;
    }

    public class MyOnClickListener implements View.OnClickListener{
        private TextView tv;
        public MyOnClickListener(TextView tv){
            this.tv=tv;
        }

        @Override
        public void onClick(View v) {
            if(tv.getVisibility()==View.GONE)
                tv.setVisibility(View.VISIBLE);
            else
                tv.setVisibility(View.GONE);
        }
    }

    /**
     * 存放控件
     */
    public final class ViewHolder {

        public TextView textView1;
        public TextView textView2;
        public ImageView imageView;


    }



}


