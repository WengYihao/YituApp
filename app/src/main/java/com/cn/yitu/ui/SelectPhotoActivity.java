package com.cn.yitu.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.yitu.bean.ImageItem;
import com.cn.yitu.config.base.CallBack;
import com.cn.yitu.server.QueryHTTP;
import com.cn.yitu.xutil.Bimp;
import com.cn.yitu.xutil.BitmapUtils;
import com.cn.yitu.xutil.GraphicsBitmapUtils;
import com.cn.yitu.xutil.SharePreferenceXutil;
import com.cn.yitu.xutil.SystemUtil;
import com.cn.yitu.xutil.ToastXutil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectPhotoActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_PHOTO = 1;
    private List<Bitmap> mResults = new ArrayList<>();
    private TextView send;
    public static Bitmap bimap;
    private GridView noScrollgridview;
    private GridAdapter adapter;
    private ArrayList<String> result;
    private QueryHTTP server;
    private String token;
    private ProgressDialog progDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_photo);
        bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_focused);
        mResults.add(bimap);
        initView();
    }
    private void initView() {
        send = (TextView)findViewById(R.id.send);
        send.setOnClickListener(this);
        noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
        noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        noScrollgridview.setAdapter(adapter);
        server = new QueryHTTP();
        token = SharePreferenceXutil.getToken();
        progDialog = new ProgressDialog(this);
        noScrollgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == mResults.size() - 1 || i == Bimp.tempSelectBitmap.size()) {
                    Intent intent = new Intent(SelectPhotoActivity.this, PhotoPickerActivity.class);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);
                    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);
                    // 总共选择的图片数量
                    intent.putExtra(PhotoPickerActivity.TOTAL_MAX_MUN, Bimp.tempSelectBitmap.size());
                    startActivityForResult(intent, PICK_PHOTO);
                } else {
                    Intent intent = new Intent(SelectPhotoActivity.this,
                            PreviewPhotoActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", i);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send:
                showDialog();
                Map<String,String> map = new HashMap<>();
                for (int i = 0;i < mResults.size()-1;i++){
                    String key = i + 1 + "_picture";
                    String val = Base64.encodeToString(GraphicsBitmapUtils.Bitmap2Bytes(mResults.get(i)), Base64.DEFAULT);
                    map.put(key,val);
                    Log.i("123",i+"---");
                }
                map.put("account_token",token);
                Log.i("123",map.toString()+"-------"+map.size());
                server.sendPhoto(map, new CallBack() {
                    @Override
                    public void onResponse(String response) {
//                        Log.i("123",response+"---");
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            int resultnumber = jsonObject.getInt("resultnumber");
                            switch (resultnumber){
                                case 200:
                                    dismissDialog();
                                    ToastXutil.show("上传成功");
                                    SelectPhotoActivity.this.finish();
                                    break;
                                case 202:
                                    dismissDialog();
                                    ToastXutil.show("参数为空");
                                    break;
                                case 232:
                                    dismissDialog();
                                    ToastXutil.show("请先选择打扫区域");
                                    break;
                                case 233:
                                    dismissDialog();
                                    ToastXutil.show("已提交此区域的打扫信息");
                                    break;
                                case 234:
                                    dismissDialog();
                                    ToastXutil.show("上传保洁信息失败");
                                    break;
                            }
                        }catch (Exception e){

                        }

                    }
                });

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                showResult(result);
            }
        }
    }

    private void showResult(ArrayList<String> paths) {
        if (mResults == null) {
            mResults = new ArrayList<>();
        }
        if (paths.size() != 0) {
            mResults.remove(mResults.size() - 1);
        }
        for (int i = 0; i < paths.size(); i++) {
            // 压缩图片
            Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromFd(paths.get(i), 400, 500);
            // 针对小图也可以不压缩
//            Bitmap bitmap = BitmapFactory.decodeFile(paths.get(i));
            mResults.add(bitmap);

            ImageItem takePhoto = new ImageItem();
            takePhoto.setBitmap(bitmap);
            Bimp.tempSelectBitmap.add(takePhoto);
        }
        mResults.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_focused));
        adapter.notifyDataSetChanged();
    }

    /**
     * 适配器
     */
    public class GridAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }
        public void update() {
            loading();
        }

        public int getCount() {
            if (Bimp.tempSelectBitmap.size() == 3) {
                return 3;
            }
            return (Bimp.tempSelectBitmap.size() + 1);
        }

        public Object getItem(int arg0) {
            return mResults.get(arg0);
        }

        public long getItemId(int arg0) {
            return arg0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_gridview, null);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.imageView1);
                LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) holder.image.getLayoutParams(); //取控件textView当前的布局参数
                linearParams.height = SystemUtil.getWidthInPx(SelectPhotoActivity.this)/3;// 控件的高强制设成屏幕的三分之一
                linearParams.width =SystemUtil.getWidthInPx(SelectPhotoActivity.this)/3;// 控件的宽强制设成屏幕的三分之一
                holder.image.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == Bimp.tempSelectBitmap.size()) {
                holder.image.setImageBitmap(BitmapFactory.decodeResource(
                        getResources(), R.drawable.icon_addpic_focused));
                if (position == 3) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position).getBitmap());
            }
            return convertView;
        }

        public class ViewHolder {
            public ImageView image;
        }

        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        public void loading (){
            new Thread(new Thread(){
                @Override
                public void run() {
                    while (true) {
                        if (Bimp.max == Bimp.tempSelectBitmap.size()) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            Log.i("123","1111111111111111"+"---"+Bimp.tempSelectBitmap.size());
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                            Log.i("123","222222222222222"+"---"+Bimp.tempSelectBitmap.size());
                        }
                    }
                }
            });
        }
    }

    protected void onRestart() {
        adapter.update();
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bimp.tempSelectBitmap.clear();
        Log.i("123",mResults.toString());
    }
    /**
     * 显示进度条对话框
     */
    public void showDialog() {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在连接服务器...");
        progDialog.show();
    }

    /**
     * 隐藏进度条对话框
     */
    public void dismissDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }
}

