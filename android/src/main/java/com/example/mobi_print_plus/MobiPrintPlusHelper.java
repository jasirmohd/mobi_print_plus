package com.example.mobi_print_plus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.RemoteException;
import android.util.Log;

import com.mobiiot.androidqapi.api.MobiiotAPI;
import com.mobiiot.androidqapi.api.Utils.PrinterServiceUtil;
import com.mobiiot.androidqapi.api.Utils.ServiceUtil;
import com.sagereal.printer.PrinterInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;


public class MobiPrintPlusHelper {

    private static PrinterInterface printerInterface;

    public  static  MobiPrintPlusHelper mContext;

    PrinterMode mode;

//    private Context context;
//
//    public MobiPrintPlusHelper(Context context){
//        this.context = context;
//    }
    public static MobiPrintPlusHelper getInstance(){
        if(mContext == null){
            return mContext = new MobiPrintPlusHelper();
        }
        return mContext;
    }

    public void initMobiPrinterService(Context context){
        new MobiiotAPI(context);
        ServiceUtil.bindRemoteService(context);
    }

    public boolean isMobiPrinterAvailable(){
        if(PrinterServiceUtil.getPrinterService() != null){
            printerInterface = PrinterServiceUtil.getPrinterService();
            return true;
        }
        return false;
    }

    public void initMobiPrinter(Context context){
        try{
            MobiPrintPlusHelper.getInstance().initMobiPrinterService(context);
            if(MobiPrintPlusHelper.getInstance().isMobiPrinterAvailable()){
                mode = PrinterMode.MICRO;
            }else{
                mode = PrinterMode.NO_PRINTER;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void feedPaper(){
        if(printerInterface == null){
            return;
        }
        MobiPrintPlusHelper.getInstance().printMultiLine(3);
        try{

            printerInterface.printEndLine();
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public void printText(String text){
        try {
            if(printerInterface == null) {
                printerInterface = PrinterServiceUtil.getPrinterService();
            }
                printerInterface.printText(text);
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public void printBoldText(String text,float size,int alignment,boolean isBold, boolean isUnderLine){
        try{
            if(printerInterface == null) {
                printerInterface = PrinterServiceUtil.getPrinterService();
            }
            printerInterface.printText_FullParm(text,Math.round(size),0,Math.round(size),alignment,isBold,isUnderLine);
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public void printMultiLine(int lines){
        try{
            if(printerInterface == null) {
                printerInterface = PrinterServiceUtil.getPrinterService();
            }
            for(int i=0; i < lines; i++){
                printerInterface.printEndLine();
            }
        }catch (RemoteException e){
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void setAlign(int data){
        try{
            if(printerInterface == null) {
                printerInterface = PrinterServiceUtil.getPrinterService();
            }
            printerInterface.printText_FullParm("",0,0,0,data,false,false);
        } catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public void printLogo(byte[] image) {
            Bitmap bitmap = getBitmapFromDrawable(image);
            byte[] bitmapData = PrintBitmapUtils.getPrintPhotoByteArray(bitmap);
            try {
                if(printerInterface == null) {
                    printerInterface = PrinterServiceUtil.getPrinterService();
                }
                MobiPrintPlusHelper.getInstance().setAlign(1);
                printerInterface.printBitmap_bytes_r(bitmapData);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }

    public void printQr(String data){
        try{
            Bitmap bitmapQrCode = null;
            try{
                bitmapQrCode = PrintBitmapUtils.createQRCode(data);
            } catch (Exception e){
                e.printStackTrace();
            }
            byte[] printQrCodeByteArray = PrintBitmapUtils.getPrintQRByteArray(bitmapQrCode);
            if(printerInterface == null) {
                printerInterface = PrinterServiceUtil.getPrinterService();
            }
            MobiPrintPlusHelper.getInstance().setAlign(1);
            printerInterface.printBitmap_bDate(printQrCodeByteArray);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public void printBarCode(String data,String type){
        try{
            Bitmap bitmapBarCode = null;
            try{
                bitmapBarCode = PrintBitmapUtils.createBarCode(data,type);
            }catch (Exception e){
                e.printStackTrace();
            }
            byte[] printBarCodeByteArray = PrintBitmapUtils.getPrintBarCodeByteArray(bitmapBarCode);
            if(printerInterface == null) {
                printerInterface = PrinterServiceUtil.getPrinterService();
            }
            MobiPrintPlusHelper.getInstance().setAlign(1);
            printerInterface.printBitmap_bDate(printBarCodeByteArray);
            printerInterface.printText_FullParm(data,0,0,0,1,false,false);
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromDrawable(byte[] image) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inTargetDensity = 160;
        options.inDensity = 160;

        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0,image.length, options);
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* Ignored for PNGs */, blob);
        byte[] bitmapdata = blob.toByteArray();

        return BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
    }

    public enum PrinterMode {
        NO_PRINTER,
        MICRO
    }
}

