package huantai.smarthome.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

import huantai.smarthome.initial.CommonModule.GosConstant;

/**
 * Created by lj_xwl on 2017/10/10.
 */

public class ShareDeviceUtils {

    private static final int QR_WIDTH = 800;
    private static final int QR_HEIGHT = 800;
    //生成二维码
    public static  void makeQRCode(String mac, Context context) {

//        uid = spf.getString("Uid",null);
//        token =spf.getString("Token",null);

//        String mac=device.getMacAddress();

//        String text = "http://blog.csdn.net/gao36951";
        String url = "type=bang"+"&mac="+mac+"&productKey="+ GosConstant.Product_Key
                +"&productSecret="+GosConstant.Product_Secret;

        Log.i("xxs", "url::::"+url);
        try {
            // 判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1) {
                return ;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            ImageView ImageView = new ImageView(context);
            ImageView.setImageBitmap(bitmap);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("分享设备二维码");
            builder.setView(ImageView);
            //   builder.setMessage(url);
            builder.show();
            // 显示到一个ImageView上面
            // sweepIV.setImageBitmap(bitmap);

        } catch (WriterException e) {
            Log.i("log", "生成二维码错误" + e.getMessage());
        }
    }
}
