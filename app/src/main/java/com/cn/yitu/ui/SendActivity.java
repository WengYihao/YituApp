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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.yitu.bean.ImageItem;
import com.cn.yitu.config.MyConfig;
import com.cn.yitu.config.base.CallBack;
import com.cn.yitu.config.base.okhttp;
import com.cn.yitu.server.QueryHTTP;
import com.cn.yitu.server.SecurityReceive;
import com.cn.yitu.xutil.Bimp;
import com.cn.yitu.xutil.BitmapUtils;
import com.cn.yitu.xutil.SharePreferenceXutil;
import com.cn.yitu.xutil.SystemUtil;
import com.cn.yitu.xutil.ToastXutil;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SendActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_PHOTO = 1;
    private List<Bitmap> mResults = new ArrayList<>();
    public static Bitmap bimap;
    private GridView gridview;
    private GridAdapter adapter;
    private ArrayList<String> result;
    private TextView back,send;
    private CheckBox isCheck;
    private EditText report;
    private String token;
    private QueryHTTP server;
    private ProgressDialog progDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        bimap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_focused);
        mResults.add(bimap);
        initView();
    }

    private void initView(){
        back  =(TextView)findViewById(R.id.back);
        send = (TextView)findViewById(R.id.send);
        isCheck  =(CheckBox)findViewById(R.id.isCheak);
        gridview = (GridView)findViewById(R.id.gridview);
        report = (EditText)findViewById(R.id.report);
        back.setOnClickListener(this);
        send.setOnClickListener(this);

        gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new GridAdapter(this);
        adapter.update();
        gridview.setAdapter(adapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == mResults.size() - 1 || i == Bimp.tempSelectBitmap.size()) {
                    Intent intent = new Intent(SendActivity.this, PhotoPickerActivity.class);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);
                    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, PhotoPickerActivity.DEFAULT_NUM);
                    // 总共选择的图片数量
                    intent.putExtra(PhotoPickerActivity.TOTAL_MAX_MUN, Bimp.tempSelectBitmap.size());
                    startActivityForResult(intent, PICK_PHOTO);
                } else {
                    Intent intent = new Intent(SendActivity.this,
                            PreviewPhotoActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", i);
                    startActivity(intent);
                }
            }
        });

        token = SharePreferenceXutil.getToken();
        server = new QueryHTTP();
        progDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                this.finish();
                break;
            case R.id.send:
                sendForOk();
                break;
        }
    }

    public void sendForOk(){
        showDialog();
        String url = MyConfig.url+"interface/mobile/update/updateNowLocationds.do";
        Map<String ,Object> map = new HashMap<String, Object>();
        map.put("account_token", token);
        map.put("state", isCheck.isChecked()+"");
        map.put("report", report.getText().toString());
        for (int i = 0; i < result.size(); i++) {
            String key = i + 1 + "_picture";
            File val = new File(result.get(i));
            map.put(key,val);
        }
        okhttp okhttp = new okhttp();
        okhttp.uploadImgAndParameter(map, url, new CallBack() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int resultnumber = jsonObject.getInt("resultnumber");
                    switch (resultnumber){
                        case 200:
                            dismissDialog();
                            ToastXutil.show("上传成功");
                            SecurityReceive.isOpen = false;
                            SendActivity.this.finish();
                            break;
                        default:
                            dismissDialog();
                            break;
                    }
                }catch (Exception e){

                }

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                showResult(result);
                Log.i("123","123");
            }
        }else{
            Log.i("123","456");
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
                linearParams.height = SystemUtil.getWidthInPx(SendActivity.this)/3;// 控件的高强制设成屏幕的三分之一
                linearParams.width =SystemUtil.getWidthInPx(SendActivity.this)/3;// 控件的宽强制设成屏幕的三分之一
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
                        Log.i("123","我在这里更新");
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
                            break;
                        } else {
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
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
        Log.i("123",result.toString()+"-----");
        Log.i("123",mResults.toString()+"=====");
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
