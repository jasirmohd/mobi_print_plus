package com.example.mobi_print_plus;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.encoder.QRCode;
import com.mobiiot.androidqapi.api.Utils.BitmapUtils;
import com.mobiiot.androidqapi.api.Utils.DataFormatConversion;

import java.util.Arrays;
import java.util.Hashtable;

public class PrintBitmapUtils {

    private static Canvas canvas;
    private static int height = 84;
    private static int width = 120;

    public static Bitmap createQRCode(String str) throws WriterException {
        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 370, 370);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = Color.BLACK;
                } else {
                    pixels[y * width + x] = Color.WHITE;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        Log.d("jiangcunbin", "bitmap1 Qrcode phote bitmap:" + bitmap.toString());
        return bitmap;
    }

    public static Bitmap createBarCode(String str,String type) throws WriterException {
        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix matrix = null;
        switch (type){
            case "UPCA":
                matrix = new MultiFormatWriter().encode(str, BarcodeFormat.UPC_A, 300, 170);
                break;
            case "UPCE":
                matrix = new MultiFormatWriter().encode(str, BarcodeFormat.UPC_E, 300, 170);
                break;
            case "JAN13":
                matrix = new MultiFormatWriter().encode(str, BarcodeFormat.EAN_13, 300, 170);
                break;
            case "JAN8":
                matrix = new MultiFormatWriter().encode(str, BarcodeFormat.EAN_8, 300, 170);
                break;
            case "CODE39":
                matrix = new MultiFormatWriter().encode(str, BarcodeFormat.CODE_39, 300, 170);
                break;
            case "ITF":
                matrix = new MultiFormatWriter().encode(str, BarcodeFormat.ITF, 300, 170);
                break;
            case "CODABAR":
                matrix = new MultiFormatWriter().encode(str, BarcodeFormat.CODABAR, 300, 170);
                break;
            case "CODE93":
                matrix = new MultiFormatWriter().encode(str, BarcodeFormat.CODE_93, 300, 170);
                break;
            case "CODE128":
                matrix = new MultiFormatWriter().encode(str, BarcodeFormat.CODE_128, 300, 170);
                break;
            default:
                break;
        }
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = Color.BLACK;
                } else {
                    pixels[y * width + x] = Color.WHITE;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        Log.d("jiangcunbin", "bitmap1 Qrcode phote bitmap:" + bitmap.toString());
        return bitmap;
    }

    public static byte[] getPrintPhotoByteArray(Bitmap bmp) {
        if (bmp == null) {
            return null;
        }

        try {
            int compressHeight = (int) ((height * 184) / width);
//            int compressHeight = 200;

            Bitmap imageCrop = BitmapUtils.zoomImg(bmp,320, compressHeight);
            // 位图大小
            //bitmap size
            int nBmpWidth = imageCrop.getWidth();
            int nBmpHeight = imageCrop.getHeight();
            // 图像数据大小
            //image data size
            int bufferSize = nBmpHeight * (nBmpWidth * 3 + nBmpWidth % 4);

            // 创建位图数组：这里的数组大小为总大小：bmp文件头（14）+bmp信息头（40）+图片大小（bufferSize）。
            /*Create a bitmap array: the size of the array here is the total size: bmp file header (14) +
            bmp information header (40) + picture size (bufferSize)*/
            byte[] totalSize = new byte[14 + 40 + bufferSize];

            // bmp文件头
            int bfType = 0x4d42;
            long bfSize = 14 + 40 + bufferSize;
            int bfReserved1 = 0;
            int bfReserved2 = 0;
            long bfOffBits = 14 + 40;
            // 保存bmp文件头
            byte[] writeWord1 = DataFormatConversion.writeWord(bfType);
            System.arraycopy(writeWord1, 0, totalSize, 0, 2);
            byte[] writeDword2 = DataFormatConversion.writeDword(bfSize);
            System.arraycopy(writeDword2, 0, totalSize, 2, 4);
            byte[] writeDword3 = DataFormatConversion.writeDword(bfReserved1);
            System.arraycopy(writeDword3, 0, totalSize, 6, 2);
            byte[] writeDword4 = DataFormatConversion.writeDword(bfReserved2);
            System.arraycopy(writeDword4, 0, totalSize, 8, 2);
            byte[] writeDword5 = DataFormatConversion.writeDword(bfOffBits);
            System.arraycopy(writeDword5, 0, totalSize, 10, 4);
            Log.d("jiangcunbin", "totalSize14     :  " + Arrays.toString(totalSize));

            // bmp信息头
            long biSize = 40L;
            long biWidth = nBmpWidth;
            long biHeight = nBmpHeight;
            int biPlanes = 1;
            int biBitCount = 24;
            long biCompression = 0L;
            long biSizeImage = 3 * 384L * compressHeight;
            long biXpelsPerMeter = 0L;
            long biYPelsPerMeter = 0L;
            long biClrUsed = 0L;
            long biClrImportant = 0L;
            // 保存bmp信息头
            // writeDword(fileos, biSize);//4
            byte[] writeDword6 = DataFormatConversion.writeDword(biSize);
            System.arraycopy(writeDword6, 0, totalSize, 14, 4);
            // writeLong(fileos, biWidth);//4
            byte[] writeDword7 = DataFormatConversion.writeLong(biWidth);
            System.arraycopy(writeDword7, 0, totalSize, 18, 4);
            // writeLong(fileos, biHeight);//4
            byte[] writeDword8 = DataFormatConversion.writeLong(biHeight);
            System.arraycopy(writeDword8, 0, totalSize, 22, 4);
            // writeWord(fileos, biPlanes);//2
            byte[] writeDword9 = DataFormatConversion.writeWord(biPlanes);
            System.arraycopy(writeDword9, 0, totalSize, 26, 2);
            // writeWord(fileos, biBitCount);//2
            byte[] writeWord10 = DataFormatConversion.writeWord(biBitCount);
            System.arraycopy(writeWord10, 0, totalSize, 28, 2);
            // writeDword(fileos, biCompression);//4
            byte[] writeDword11 = DataFormatConversion.writeDword(biCompression);
            System.arraycopy(writeDword11, 0, totalSize, 30, 4);
            // writeDword(fileos, biSizeImage);//4
            byte[] writeDword12 = DataFormatConversion.writeDword(biSizeImage);
            System.arraycopy(writeDword12, 0, totalSize, 34, 4);
            // writeLong(fileos, biXpelsPerMeter);//4
            byte[] writeDword13 = DataFormatConversion.writeLong(biXpelsPerMeter);
            System.arraycopy(writeDword13, 0, totalSize, 38, 4);
            // writeLong(fileos, biYPelsPerMeter);//4
            byte[] writeDword14 = DataFormatConversion.writeLong(biYPelsPerMeter);
            System.arraycopy(writeDword14, 0, totalSize, 42, 4);
            // writeDword(fileos, biClrUsed);//4
            byte[] writeDword15 = DataFormatConversion.writeDword(biClrUsed);
            System.arraycopy(writeDword15, 0, totalSize, 46, 4);
            // writeDword(fileos, biClrImportant);//4
            byte[] writeDword16 = DataFormatConversion.writeDword(biClrImportant);
            System.arraycopy(writeDword16, 0, totalSize, 50, 4);
            Log.d("jiangcunbin", "totalSize54     :  " + Arrays.toString(totalSize));

            // 像素扫描
            byte[] bmpData = new byte[bufferSize];
            int wWidth = (nBmpWidth * 3 + nBmpWidth % 4);
            for (int nCol = 0, nRealCol = nBmpHeight - 1; nCol < nBmpHeight; ++nCol, --nRealCol)
                for (int wRow = 0, wByteIdex = 0; wRow < nBmpWidth; wRow++, wByteIdex += 3) {
                    int clr = imageCrop.getPixel(wRow, nCol);
                    bmpData[nRealCol * wWidth + wByteIdex] = (byte) Color.blue(clr);
                    bmpData[nRealCol * wWidth + wByteIdex + 1] = (byte) Color
                            .green(clr);
                    bmpData[nRealCol * wWidth + wByteIdex + 2] = (byte) Color
                            .red(clr);
                }

            System.arraycopy(bmpData, 0, totalSize, 54, bufferSize);

            return totalSize;
        } catch (Exception e) {
            Log.d("jiangcunbin", "  Exception   print QRcode" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getPrintQRByteArray(Bitmap bmp) {
        if (bmp == null) {
            return null;
        }

        try {
//            int compressHeight = (int) ((height * 274) / width);
            int compressHeight = 320;

            Bitmap imageCrop = BitmapUtils.zoomImg(bmp, 320, 320);
            // 位图大小
            int nBmpWidth = imageCrop.getWidth();
            int nBmpHeight = imageCrop.getHeight();
            // 图像数据大小
            int bufferSize = nBmpHeight * (nBmpWidth * 3 + nBmpWidth % 4);

            // 创建位图数组：这里的数组大小为总大小：bmp文件头（14）+bmp信息头（40）+图片大小（bufferSize）。
            byte[] totalSize = new byte[14 + 40 + bufferSize];

            // bmp文件头
            int bfType = 0x4d42;
            long bfSize = 14 + 40 + bufferSize;
            int bfReserved1 = 0;
            int bfReserved2 = 0;
            long bfOffBits = 14 + 40;
            // 保存bmp文件头
            byte[] writeWord1 = DataFormatConversion.writeWord(bfType);
            System.arraycopy(writeWord1, 0, totalSize, 0, 2);
            byte[] writeDword2 = DataFormatConversion.writeDword(bfSize);
            System.arraycopy(writeDword2, 0, totalSize, 2, 4);
            byte[] writeDword3 = DataFormatConversion.writeDword(bfReserved1);
            System.arraycopy(writeDword3, 0, totalSize, 6, 2);
            byte[] writeDword4 = DataFormatConversion.writeDword(bfReserved2);
            System.arraycopy(writeDword4, 0, totalSize, 8, 2);
            byte[] writeDword5 = DataFormatConversion.writeDword(bfOffBits);
            System.arraycopy(writeDword5, 0, totalSize, 10, 4);
            Log.d("jiangcunbin", "totalSize14     :  " + Arrays.toString(totalSize));

            // bmp信息头
            long biSize = 40L;
            long biWidth = nBmpWidth;
            long biHeight = nBmpHeight;
            int biPlanes = 1;
            int biBitCount = 24;
            long biCompression = 0L;
            long biSizeImage = 3 * 384L * compressHeight;
            long biXpelsPerMeter = 0L;
            long biYPelsPerMeter = 0L;
            long biClrUsed = 0L;
            long biClrImportant = 0L;
            // 保存bmp信息头
            // writeDword(fileos, biSize);//4
            byte[] writeDword6 = DataFormatConversion.writeDword(biSize);
            System.arraycopy(writeDword6, 0, totalSize, 14, 4);
            // writeLong(fileos, biWidth);//4
            byte[] writeDword7 = DataFormatConversion.writeLong(biWidth);
            System.arraycopy(writeDword7, 0, totalSize, 18, 4);
            // writeLong(fileos, biHeight);//4
            byte[] writeDword8 = DataFormatConversion.writeLong(biHeight);
            System.arraycopy(writeDword8, 0, totalSize, 22, 4);
            // writeWord(fileos, biPlanes);//2
            byte[] writeDword9 = DataFormatConversion.writeWord(biPlanes);
            System.arraycopy(writeDword9, 0, totalSize, 26, 2);
            // writeWord(fileos, biBitCount);//2
            byte[] writeWord10 = DataFormatConversion.writeWord(biBitCount);
            System.arraycopy(writeWord10, 0, totalSize, 28, 2);
            // writeDword(fileos, biCompression);//4
            byte[] writeDword11 = DataFormatConversion.writeDword(biCompression);
            System.arraycopy(writeDword11, 0, totalSize, 30, 4);
            // writeDword(fileos, biSizeImage);//4
            byte[] writeDword12 = DataFormatConversion.writeDword(biSizeImage);
            System.arraycopy(writeDword12, 0, totalSize, 34, 4);
            // writeLong(fileos, biXpelsPerMeter);//4
            byte[] writeDword13 = DataFormatConversion.writeLong(biXpelsPerMeter);
            System.arraycopy(writeDword13, 0, totalSize, 38, 4);
            // writeLong(fileos, biYPelsPerMeter);//4
            byte[] writeDword14 = DataFormatConversion.writeLong(biYPelsPerMeter);
            System.arraycopy(writeDword14, 0, totalSize, 42, 4);
            // writeDword(fileos, biClrUsed);//4
            byte[] writeDword15 = DataFormatConversion.writeDword(biClrUsed);
            System.arraycopy(writeDword15, 0, totalSize, 46, 4);
            // writeDword(fileos, biClrImportant);//4
            byte[] writeDword16 = DataFormatConversion.writeDword(biClrImportant);
            System.arraycopy(writeDword16, 0, totalSize, 50, 4);
            Log.d("jiangcunbin", "totalSize54     :  " + Arrays.toString(totalSize));

            // 像素扫描
            byte[] bmpData = new byte[bufferSize];
            int wWidth = (nBmpWidth * 3 + nBmpWidth % 4);
            for (int nCol = 0, nRealCol = nBmpHeight - 1; nCol < nBmpHeight; ++nCol, --nRealCol)
                for (int wRow = 0, wByteIdex = 0; wRow < nBmpWidth; wRow++, wByteIdex += 3) {
                    int clr = imageCrop.getPixel(wRow, nCol);
                    bmpData[nRealCol * wWidth + wByteIdex] = (byte) Color.blue(clr);
                    bmpData[nRealCol * wWidth + wByteIdex + 1] = (byte) Color
                            .green(clr);
                    bmpData[nRealCol * wWidth + wByteIdex + 2] = (byte) Color
                            .red(clr);
                }

            System.arraycopy(bmpData, 0, totalSize, 54, bufferSize);

            return totalSize;
        } catch (Exception e) {
            Log.d("jiangcunbin", "  Exception   print QRcode" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getPrintBarCodeByteArray(Bitmap bmp) {
        if (bmp == null) {
            return null;
        }

        try {
            int compressHeight = (int) ((height * 190) / width);
//            int compressHeight = 150;

            Bitmap imageCrop = BitmapUtils.zoomImg(bmp, 320, compressHeight);
            // 位图大小
            int nBmpWidth = imageCrop.getWidth();
            int nBmpHeight = imageCrop.getHeight();
            // 图像数据大小
            int bufferSize = nBmpHeight * (nBmpWidth * 3 + nBmpWidth % 4);

            // 创建位图数组：这里的数组大小为总大小：bmp文件头（14）+bmp信息头（40）+图片大小（bufferSize）。
            byte[] totalSize = new byte[14 + 40 + bufferSize];

            // bmp文件头
            int bfType = 0x4d42;
            long bfSize = 14 + 40 + bufferSize;
            int bfReserved1 = 0;
            int bfReserved2 = 0;
            long bfOffBits = 14 + 40;
            // 保存bmp文件头
            byte[] writeWord1 = DataFormatConversion.writeWord(bfType);
            System.arraycopy(writeWord1, 0, totalSize, 0, 2);
            byte[] writeDword2 = DataFormatConversion.writeDword(bfSize);
            System.arraycopy(writeDword2, 0, totalSize, 2, 4);
            byte[] writeDword3 = DataFormatConversion.writeDword(bfReserved1);
            System.arraycopy(writeDword3, 0, totalSize, 6, 2);
            byte[] writeDword4 = DataFormatConversion.writeDword(bfReserved2);
            System.arraycopy(writeDword4, 0, totalSize, 8, 2);
            byte[] writeDword5 = DataFormatConversion.writeDword(bfOffBits);
            System.arraycopy(writeDword5, 0, totalSize, 10, 4);
            Log.d("jiangcunbin", "totalSize14     :  " + Arrays.toString(totalSize));

            // bmp信息头
            long biSize = 40L;
            long biWidth = nBmpWidth;
            long biHeight = nBmpHeight;
            int biPlanes = 1;
            int biBitCount = 24;
            long biCompression = 0L;
            long biSizeImage = 3 * 384L * compressHeight;
            long biXpelsPerMeter = 0L;
            long biYPelsPerMeter = 0L;
            long biClrUsed = 0L;
            long biClrImportant = 0L;
            // 保存bmp信息头
            // writeDword(fileos, biSize);//4
            byte[] writeDword6 = DataFormatConversion.writeDword(biSize);
            System.arraycopy(writeDword6, 0, totalSize, 14, 4);
            // writeLong(fileos, biWidth);//4
            byte[] writeDword7 = DataFormatConversion.writeLong(biWidth);
            System.arraycopy(writeDword7, 0, totalSize, 18, 4);
            // writeLong(fileos, biHeight);//4
            byte[] writeDword8 = DataFormatConversion.writeLong(biHeight);
            System.arraycopy(writeDword8, 0, totalSize, 22, 4);
            // writeWord(fileos, biPlanes);//2
            byte[] writeDword9 = DataFormatConversion.writeWord(biPlanes);
            System.arraycopy(writeDword9, 0, totalSize, 26, 2);
            // writeWord(fileos, biBitCount);//2
            byte[] writeWord10 = DataFormatConversion.writeWord(biBitCount);
            System.arraycopy(writeWord10, 0, totalSize, 28, 2);
            // writeDword(fileos, biCompression);//4
            byte[] writeDword11 = DataFormatConversion.writeDword(biCompression);
            System.arraycopy(writeDword11, 0, totalSize, 30, 4);
            // writeDword(fileos, biSizeImage);//4
            byte[] writeDword12 = DataFormatConversion.writeDword(biSizeImage);
            System.arraycopy(writeDword12, 0, totalSize, 34, 4);
            // writeLong(fileos, biXpelsPerMeter);//4
            byte[] writeDword13 = DataFormatConversion.writeLong(biXpelsPerMeter);
            System.arraycopy(writeDword13, 0, totalSize, 38, 4);
            // writeLong(fileos, biYPelsPerMeter);//4
            byte[] writeDword14 = DataFormatConversion.writeLong(biYPelsPerMeter);
            System.arraycopy(writeDword14, 0, totalSize, 42, 4);
            // writeDword(fileos, biClrUsed);//4
            byte[] writeDword15 = DataFormatConversion.writeDword(biClrUsed);
            System.arraycopy(writeDword15, 0, totalSize, 46, 4);
            // writeDword(fileos, biClrImportant);//4
            byte[] writeDword16 = DataFormatConversion.writeDword(biClrImportant);
            System.arraycopy(writeDword16, 0, totalSize, 50, 4);
            Log.d("jiangcunbin", "totalSize54     :  " + Arrays.toString(totalSize));

            // 像素扫描
            byte[] bmpData = new byte[bufferSize];
            int wWidth = (nBmpWidth * 3 + nBmpWidth % 4);
            for (int nCol = 0, nRealCol = nBmpHeight - 1; nCol < nBmpHeight; ++nCol, --nRealCol)
                for (int wRow = 0, wByteIdex = 0; wRow < nBmpWidth; wRow++, wByteIdex += 3) {
                    int clr = imageCrop.getPixel(wRow, nCol);
                    bmpData[nRealCol * wWidth + wByteIdex] = (byte) Color.blue(clr);
                    bmpData[nRealCol * wWidth + wByteIdex + 1] = (byte) Color
                            .green(clr);
                    bmpData[nRealCol * wWidth + wByteIdex + 2] = (byte) Color
                            .red(clr);
                }

            System.arraycopy(bmpData, 0, totalSize, 54, bufferSize);

            return totalSize;
        } catch (Exception e) {
            Log.d("jiangcunbin", "  Exception   print QRcode" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] getItunesArray(Bitmap bmp) {
        if (bmp == null) {
            return null;
        }

        try {

            int compressHeight = (int) ((height * 184) / width);

            Bitmap imageCrop = BitmapUtils.zoomImg(bmp, 320, compressHeight);
            // 位图大小
            int nBmpWidth = imageCrop.getWidth();
            int nBmpHeight = imageCrop.getHeight();
            // 图像数据大小
            int bufferSize = nBmpHeight * (nBmpWidth * 3 + nBmpWidth % 4);

            // 创建位图数组：这里的数组大小为总大小：bmp文件头（14）+bmp信息头（40）+图片大小（bufferSize）。
            byte[] totalSize = new byte[14 + 40 + bufferSize];

            // bmp文件头
            int bfType = 0x4d42;
            long bfSize = 14 + 40 + bufferSize;
            int bfReserved1 = 0;
            int bfReserved2 = 0;
            long bfOffBits = 14 + 40;
            // 保存bmp文件头
            byte[] writeWord1 = DataFormatConversion.writeWord(bfType);
            System.arraycopy(writeWord1, 0, totalSize, 0, 2);
            byte[] writeDword2 = DataFormatConversion.writeDword(bfSize);
            System.arraycopy(writeDword2, 0, totalSize, 2, 4);
            byte[] writeDword3 = DataFormatConversion.writeDword(bfReserved1);
            System.arraycopy(writeDword3, 0, totalSize, 6, 2);
            byte[] writeDword4 = DataFormatConversion.writeDword(bfReserved2);
            System.arraycopy(writeDword4, 0, totalSize, 8, 2);
            byte[] writeDword5 = DataFormatConversion.writeDword(bfOffBits);
            System.arraycopy(writeDword5, 0, totalSize, 10, 4);
            Log.d("jiangcunbin", "totalSize14     :  " + Arrays.toString(totalSize));

            // bmp信息头
            long biSize = 40L;
            long biWidth = nBmpWidth;
            long biHeight = nBmpHeight;
            int biPlanes = 1;
            int biBitCount = 24;
            long biCompression = 0L;
            long biSizeImage = 3 * 384L * compressHeight;
            long biXpelsPerMeter = 0L;
            long biYPelsPerMeter = 0L;
            long biClrUsed = 0L;
            long biClrImportant = 0L;
            // 保存bmp信息头
            // writeDword(fileos, biSize);//4
            byte[] writeDword6 = DataFormatConversion.writeDword(biSize);
            System.arraycopy(writeDword6, 0, totalSize, 14, 4);
            // writeLong(fileos, biWidth);//4
            byte[] writeDword7 = DataFormatConversion.writeLong(biWidth);
            System.arraycopy(writeDword7, 0, totalSize, 18, 4);
            // writeLong(fileos, biHeight);//4
            byte[] writeDword8 = DataFormatConversion.writeLong(biHeight);
            System.arraycopy(writeDword8, 0, totalSize, 22, 4);
            // writeWord(fileos, biPlanes);//2
            byte[] writeDword9 = DataFormatConversion.writeWord(biPlanes);
            System.arraycopy(writeDword9, 0, totalSize, 26, 2);
            // writeWord(fileos, biBitCount);//2
            byte[] writeWord10 = DataFormatConversion.writeWord(biBitCount);
            System.arraycopy(writeWord10, 0, totalSize, 28, 2);
            // writeDword(fileos, biCompression);//4
            byte[] writeDword11 = DataFormatConversion.writeDword(biCompression);
            System.arraycopy(writeDword11, 0, totalSize, 30, 4);
            // writeDword(fileos, biSizeImage);//4
            byte[] writeDword12 = DataFormatConversion.writeDword(biSizeImage);
            System.arraycopy(writeDword12, 0, totalSize, 34, 4);
            // writeLong(fileos, biXpelsPerMeter);//4
            byte[] writeDword13 = DataFormatConversion.writeLong(biXpelsPerMeter);
            System.arraycopy(writeDword13, 0, totalSize, 38, 4);
            // writeLong(fileos, biYPelsPerMeter);//4
            byte[] writeDword14 = DataFormatConversion.writeLong(biYPelsPerMeter);
            System.arraycopy(writeDword14, 0, totalSize, 42, 4);
            // writeDword(fileos, biClrUsed);//4
            byte[] writeDword15 = DataFormatConversion.writeDword(biClrUsed);
            System.arraycopy(writeDword15, 0, totalSize, 46, 4);
            // writeDword(fileos, biClrImportant);//4
            byte[] writeDword16 = DataFormatConversion.writeDword(biClrImportant);
            System.arraycopy(writeDword16, 0, totalSize, 50, 4);
            Log.d("jiangcunbin", "totalSize54     :  " + Arrays.toString(totalSize));

            // 像素扫描
            byte[] bmpData = new byte[bufferSize];
            int wWidth = (nBmpWidth * 3 + nBmpWidth % 4);
            for (int nCol = 0, nRealCol = nBmpHeight - 1; nCol < nBmpHeight; ++nCol, --nRealCol)
                for (int wRow = 0, wByteIdex = 0; wRow < nBmpWidth; wRow++, wByteIdex += 3) {
                    int clr = imageCrop.getPixel(wRow, nCol);
                    bmpData[nRealCol * wWidth + wByteIdex] = (byte) Color.blue(clr);
                    bmpData[nRealCol * wWidth + wByteIdex + 1] = (byte) Color
                            .green(clr);
                    bmpData[nRealCol * wWidth + wByteIdex + 2] = (byte) Color
                            .red(clr);
                }

            System.arraycopy(bmpData, 0, totalSize, 54, bufferSize);

            return totalSize;
        } catch (Exception e) {
            Log.d("jiangcunbin", "  Exception   print QRcode" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
