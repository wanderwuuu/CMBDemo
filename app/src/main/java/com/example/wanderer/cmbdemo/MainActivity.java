package com.example.wanderer.cmbdemo;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;

import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import cmb.MerchantCode;
import cmb.pb.util.CMBKeyboardFunc;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    private WebView webView;
    private MerchantCode merchantCode;
    private SimpleDateFormat strDate;
    private String genMerchantCode;
    private String amount = "0.01";
    private String BillNo = "0000000011";
    private String userId = "233";
    private String payeeId = "2333";
    //    String url = "https://netpay.cmbchina.com/netpayment/BaseHttp.dll?PrePayEUserP";//
    String url = "http://61.144.248.29:801/netpayment/BaseHttp.dll?PrePayEUserP";//测试环境
    String MerchantUrl = "http://101.200.89.159";//测试通知URL
    private String data;
    private String strReserved;
    private String data1;
    private String encode;
    private String url1 = "http://admin2.4006510600.com/index.php?ctl=public&act=genMerchantCode";
    private ImageView img_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.init("new");
        initUI();
        initData();
    }

    @Override
    public void onClick(View view) {
        finish();

    }

    private void initUI() {
        img_back = (ImageView) findViewById(R.id.back);
        img_back.setOnClickListener(this);
        webView = (WebView) findViewById(R.id.webview);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSaveFormData(false);
        settings.setSavePassword(false);
        settings.setSupportZoom(false);
        webView.setWebViewClient(new WebViewClient(){
                public   boolean   shouldOverrideUrlLoading(WebView view, String url){
                // 使用当前的WebView加载页面
                CMBKeyboardFunc kbFunc = new CMBKeyboardFunc(MainActivity.this);
                if(kbFunc.HandleUrlCall(webView, url) == false) {
                    return super.shouldOverrideUrlLoading(view, url);
                } else {
                    return true;
                }
                //view.loadUrl(url);
                //return  true ;
            }
        });

    }

    private void initData() {
        amount = getIntent().getStringExtra("amount");
        getMerchantCode();



    }


    private void getMerchantCode() {
        strDate = new SimpleDateFormat("yyyyMMdd");
        data = strDate.format(new Date());
        SimpleDateFormat str =  new SimpleDateFormat("yyyyMMddHHmmss");
        data1 = str.format(new Date());
        SimpleDateFormat hHmmss = new SimpleDateFormat("HHmmss");
        BillNo = hHmmss.format(new Date());
        final RequestParams params = new RequestParams();
        params.put("Key", "");
        params.put("Date", data);
        params.put("BranchID", "0022");
        params.put("Cono", "000005");
        params.put("BillNo", BillNo);
        params.put("Amount", amount);
        params.put("MerchantPara", "");
        params.put("MerchantUrl", MerchantUrl);
        params.put("PayerID", data + userId);
        params.put("PayeeID", data + payeeId);
        params.put("ClientIP", getIP());
        params.put("GoodsType", 54011600);
        params.put("PNo", "1234");
        params.put("MchNo", "P0023127");
        params.put("Seq", data1);
        params.put("URL", MerchantUrl);
        params.put("Para", "");
        params.put("MUID", "");
        params.put("Mobile", "");
        params.put("LBS", "");
        params.put("RskLvl", "");

        HttpClientUtils.post(url1,
                params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        Log.e("tag", "error");
                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                        Logger.init("new");
                        Logger.json(new String(arg2));
                        CodeInfo codeInfo = GsonUtils.parseJSON(new String(arg2), CodeInfo.class);
                        Log.e("tag", codeInfo.getErorcode() + "");
                        getData(codeInfo.getDatas());
                    }
                });
    }

    private void getData(String datas) {
        url = "http://61.144.248.29:801/netpayment/BaseHttp.dll?PrePayEUserP?" +
                "BranchID=0022" +
                "&CoNo=000005" +
                "&BillNo=" + BillNo +
                "&Amount=" + amount +
                "&Date=" + data +
                "&ExpireTimeSpan=30" +
                "&MerchantUrl="  + MerchantUrl +
                "&MerchantPara=" +
                "&MerchantCode=" + datas +
                "&MerchantRetUrl="  + MerchantUrl +
                "&MerchantRetPara=";
//        Log.e("tag", url);
//        webView.loadUrl(url);
        HttpClientUtils.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                com.orhanobut.logger.Logger.e(new String(responseBody));
                Log.e("tag", new String(responseBody));
                webView.loadUrl(url);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private String getIP() {
        //获取wifi服务
        String ip = "127.0.0.1";
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (wifiManager.isWifiEnabled()) {
//            wifiManager.setWifiEnabled(true);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = intToIp(ipAddress);
        } else {
            ip = getLocalIpAddress();
        }
        return ip;
    }

    private String intToIp(int i) {
        return (i & 0xFF ) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ( i >> 24 & 0xFF) ;
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    /**
                     *  这里需要注意：这里增加了一个限定条件( inetAddress instanceof Inet4Address ),
                     *  主要是在Android4.0高版本中可能优先得到的是IPv6的地址。
                     *  参考：http://blog.csdn.net/stormwy/article/details/8832164
                     */
                    if (!inetAddress.isLoopbackAddress()  && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        catch (SocketException ex) {
        }
        return "127.0.0.1";
    }



}
