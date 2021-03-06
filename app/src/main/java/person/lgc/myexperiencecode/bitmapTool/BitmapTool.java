package person.lgc.myexperiencecode.bitmapTool;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 用于获取图片的bitmap，并且将其缩放到和合适的大小
 *
 * @author acm_lgc
 */
public class BitmapTool {

    private BitmapFactory.Options optsa = new BitmapFactory.Options();

    /**
     * 获取并缩放路径下的图片 ，返回其Bitmap对象
     *
     * @param path String 图片，
     * @return Bitmap 路径下适应大小的图片
     */
    public Bitmap charge(String path, int needWith) {
        Bitmap bm = null;

        optsa.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, optsa);
        float width = optsa.outWidth, height = optsa.outHeight;

        optsa.inJustDecodeBounds = false;
        /** 不同尺寸图片的缩放比例 */
        optsa.inSampleSize = (int) (Math.min(height, width) / needWith);
        bm = BitmapFactory.decodeFile(path, optsa);
        return bm;
    }

    /**
     * 将整个图片获取出来，不损失精度
     *
     * @return
     */
    public static Bitmap getLosslessBitmap(String path) {
        BitmapFactory.Options optsa = new BitmapFactory.Options();
        optsa.inDither = true;
        optsa.inPreferQualityOverSpeed = true;
        optsa.inDensity = 0;
        optsa.inTargetDensity = 0;
        optsa.inScaled = false;
        optsa.inMutable = true;
        return BitmapFactory.decodeFile(path, optsa);
    }

    /**
     * 保存bitmap到指定的路径
     *
     * @param bitmap
     * @param path   路径必须不存在，存在时会覆盖文件，返回失败
     * @return 返回字符串代表不同的状态，成功是是返回"创建成功"四个字
     */
    public static String saveBitmap(Context context, Bitmap bitmap, String path) {
        return saveBitmap(context, bitmap, path, true);
    }

    public static String saveBitmap(Context context, Bitmap bitmap, String path, boolean isSendBroad) {
        String suffix = path.substring(path.lastIndexOf("."), path.length());

        Bitmap.CompressFormat bmc = null;
        if (suffix.equals(".jpg") || suffix.equals(".jpeg"))
            bmc = Bitmap.CompressFormat.JPEG;
        else if (suffix.equals(".png"))
            bmc = Bitmap.CompressFormat.PNG;
        else if (suffix.equals(".webp"))
            bmc = Bitmap.CompressFormat.WEBP;

        FileOutputStream fo = null;
        try {
            File file = new File(path);
            if (file.exists())//如果文件已存在
            {
                file.delete();
            } else file.createNewFile();
            fo = new FileOutputStream(path);
            bitmap.compress(bmc, 100, fo);
            fo.flush();
            if (isSendBroad) {
                //发送添加图片的广播
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                context.sendBroadcast(intent);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "保存失败";
        } catch (IOException e) {
            e.printStackTrace();
            return "创建文件失败";
        } finally {
            if (fo != null)
                try {
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return "创建成功";
    }

    public static long getSize(Bitmap sourceBm) {
            return sourceBm.getRowBytes()*sourceBm.getHeight();
    }
}