package net.sourceforge.simcpux;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.sourceforge.simcpux.util.ToastUtil;
import net.sourceforge.simcpux.util.Util;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            toPay();
        }
    };

    private IWXAPI api;

    private PayReq req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
//        api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1");
    }

    public void testWxPay(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
                ToastUtil.shortToastInBackgroundThread(getActivity(), "获取订单中...");
                try {
                    byte[] buf = Util.httpGet(url);
                    if (buf != null && buf.length > 0) {
                        String content = new String(buf);
                        Log.e("get server pay params:", content);
                        JSONObject json = new JSONObject(content);
                        if (null != json && !json.has("retcode")) {
                            req = new PayReq();
                            //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                            req.appId = json.getString("appid");
                            req.partnerId = json.getString("partnerid");
                            req.prepayId = json.getString("prepayid");
                            req.nonceStr = json.getString("noncestr");
                            req.timeStamp = json.getString("timestamp");
                            req.packageValue = json.getString("package");
                            req.sign = json.getString("sign");
                            req.extData = "app data"; // optional
                            ToastUtil.shortToastInBackgroundThread(getActivity(), "正常调起支付");
                            toPay();
                        } else {
                            Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
                            ToastUtil.shortToastInBackgroundThread(getActivity(), "返回错误" + json.getString("retmsg"));
                        }
                    } else {
                        Log.d("PAY_GET", "服务器请求错误");
                        ToastUtil.shortToastInBackgroundThread(getActivity(), "服务器请求错误");
                    }
                } catch (Exception e) {
                    Log.e("PAY_GET", "异常：" + e.getMessage());
                    ToastUtil.shortToastInBackgroundThread(getActivity(), "异常：" + e.getMessage());
                }
            }
        }).start();

    }

    private void toPay() {
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
    }

    private Context getActivity() {
        return this;
    }
}
