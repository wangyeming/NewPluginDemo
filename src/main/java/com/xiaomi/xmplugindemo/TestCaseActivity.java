
package com.xiaomi.xmplugindemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXImageObject;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.xiaomi.smarthome.common.ui.widget.XmRadioGroup;
import com.xiaomi.smarthome.device.api.Callback;
import com.xiaomi.smarthome.device.api.SceneInfo;
import com.xiaomi.smarthome.device.api.UserInfo;
import com.xiaomi.smarthome.device.api.XmPluginBaseActivity;
import com.xiaomi.smarthome.device.api.XmPluginHostApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class TestCaseActivity extends XmPluginBaseActivity {

    static final int SCAN_BARCODE = 1;
    LinearLayout mListContainer;
    LayoutInflater mInflater;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_case);

        mHostActivity.setTitleBarPadding(findViewById(R.id.title_bar));

        findViewById(R.id.title_bar_return).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((TextView) findViewById(R.id.title_bar_title)).setText("插件测试用例");

        findViewById(R.id.title_bar_more).setVisibility(View.GONE);

        mListContainer = (LinearLayout) findViewById(R.id.container);
        mInflater = LayoutInflater.from(this);

        addTestCase("intent parcelable", new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ParcelData pData = new ParcelData();
                pData.mData = 2;
                intent.putExtra("pData", pData);
                startActivity(intent, FragmentActivity.class.getName());
            }
        });

        addTestCase("openShareActivity", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.openShareActivity();
            }
        });

        addTestCase("goUpdateActivity", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.goUpdateActivity();
            }
        });

//        addTestCase("startLoadScene", new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mHostActivity.startLoadScene();
//            }
//        });

        addTestCase("startCreateSceneByDid", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.startCreateSceneByDid(mDeviceStat.did);
            }
        });

        addTestCase("startEditScene", new OnClickListener() {
            @Override
            public void onClick(View v) {
                List<SceneInfo> sceneInfos = mHostActivity.getSceneByDid(mDeviceStat.did);
                if (sceneInfos != null && sceneInfos.size() > 0)
                    mHostActivity.startEditScene(sceneInfos.get(0).mSceneId);
            }
        });

        addTestCase("startSetTimerList", new OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray onParams = new JSONArray();
                onParams.put(0xffffff);
                JSONArray offParams = new JSONArray();
                offParams.put(0);
                mHostActivity.startSetTimerList(mDeviceStat.did, "set_rgb", onParams.toString(),
                        "set_rgb", offParams.toString(), mDeviceStat.did, "开关",
                        "开关灯");
            }
        });

        addTestCase("openShareMediaActivity", new OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getWindow().getDecorView();
                if (view == null) {
                    return;
                }
                Bitmap bitmap = null;//= view.getDrawingCache();
                if (bitmap == null) {
                    if (view.getWidth() > 0 && view.getHeight() > 0) {
                        bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
                        Canvas canvas = new Canvas(bitmap);
                        view.draw(canvas);
                    }
                }

                if (bitmap == null) {
                    return;
                }
                File dir = getExternalCacheDir();
                if (dir == null) {
                    Toast.makeText(activity(), "没有存储空间", Toast.LENGTH_SHORT).show();
                    return;
                }
                File shareFile = new File(dir, "share.jpg");
                boolean savesuccess = false;
                try {
                    OutputStream os = new FileOutputStream(shareFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
                    os.close();
                    savesuccess = true;
                    bitmap.recycle();
                    bitmap = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (savesuccess) {
                    mHostActivity.openShareMediaActivity("智能家庭",
                            "测试分享",
                            shareFile.getAbsolutePath()
                    );
                }
            }
        });


        addTestCase("startEditCustomScene", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.startEditCustomScene();
            }
        });

        addTestCase("goUpdateActivity", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.goUpdateActivity(mDeviceStat.did);
            }
        });

        addTestCase("startEditCustomScene", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.startEditCustomScene();
            }
        });

        addTestCase("loadWebView", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.loadWebView("http://smartmifaq.mi-ae.cn/AirPurifierQA/index.html","Q & A");
            }
        });

        addTestCase("createWXAPI", new OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getWindow().getDecorView();
                if (view == null) {
                    return;
                }
                Bitmap bitmap = null;//= view.getDrawingCache();
                if (bitmap == null) {
                    if (view.getWidth() > 0 && view.getHeight() > 0) {
                        bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);
                        Canvas canvas = new Canvas(bitmap);
                        view.draw(canvas);
                    }
                }

                if (bitmap == null) {
                    return;
                }

                IWXAPI wxapi = XmPluginHostApi.instance().createWXAPI(activity(),true);
                WXMediaMessage msg = new WXMediaMessage();
                msg.title = "test";
                msg.description = "wx share test";
                msg.setThumbImage(resizeBitmap(bitmap,150));
                msg.mediaObject = new WXImageObject(bitmap);

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneSession;

                boolean ret = wxapi.sendReq(req);
            }
        });

        addTestCase("水电煤缴费", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.openRechargePage(0,mDeviceStat.latitude,mDeviceStat.longitude);
            }
        });

        addTestCase("水缴费", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.openRechargePage(1,mDeviceStat.latitude,mDeviceStat.longitude);
            }
        });
        addTestCase("电缴费", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.openRechargePage(2,mDeviceStat.latitude,mDeviceStat.longitude);
            }
        });
        addTestCase("燃气缴费", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.openRechargePage(3,mDeviceStat.latitude,mDeviceStat.longitude);
            }
        });

        addTestCase("查询剩余煤气费", new OnClickListener() {
            @Override
            public void onClick(View v) {
                XmPluginHostApi.instance().getRechargeBalances(3,mDeviceStat.latitude,mDeviceStat.longitude,new Callback<JSONObject>(){

                    @Override
                    public void onSuccess(JSONObject result) {
                        if(result==null){
                            Toast.makeText(activity(),"没有查询到余额",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(activity(),""+result.optInt("balance"),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(int error, String errorInfo) {
                        Toast.makeText(activity(),errorInfo,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        addTestCase("扫描二维码", new OnClickListener() {
            @Override
            public void onClick(View v) {
                mHostActivity.openScanBarcodePage(null,SCAN_BARCODE);
            }
        });

    }

    void addTestCase(String name, OnClickListener listener) {
        View view = mInflater.inflate(R.layout.list_item, null);
        ((TextView) view.findViewById(R.id.name)).setText(name);
        view.setOnClickListener(listener);
        LinearLayout.LayoutParams lp = new XmRadioGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mListContainer.addView(view, lp);
    }

    public static Bitmap resizeBitmap(Bitmap target, int newWidth)
    {
        int width = target.getWidth();
        int height = target.getHeight();
        Matrix matrix = new Matrix();
        if(width>height) {
            float scaleWidth = ((float) newWidth) / width;
            matrix.postScale(scaleWidth, scaleWidth);
        }else {
            float scaleHeight = ((float) newWidth) / height;
            matrix.postScale(scaleHeight, scaleHeight);
        }
        Bitmap bmp = Bitmap.createBitmap(target, 0, 0, width, height, matrix,
                true);
        return bmp;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==SCAN_BARCODE){
                String result = data.getStringExtra("scan_result");
                Toast.makeText(activity(),result,Toast.LENGTH_SHORT).show();
            }
        }
    }

}
