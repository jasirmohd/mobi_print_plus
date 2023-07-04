package com.example.mobi_print_plus;

import android.content.Context;

import java.io.IOException;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

public class MobiPrintPlugin implements FlutterPlugin, MethodCallHandler {

    ///The MethodChannel that will the communication between Flutter and native Android
    ///
    ///This local reference serves to register the plugin with the Flutter Engine and unregister it
    ///when the Flutter Engine is detached from the Activity
//    private static MobiPrintPlusHelper mobiPrintPlusHelper;

    private Context context;

    private MethodChannel channel;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding){
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(),"mobi_print_plus");
//        mobiPrintPlusHelper = new MobiPrintPlusHelper(flutterPluginBinding.getApplicationContext());
        channel.setMethodCallHandler(this);
        context = flutterPluginBinding.getApplicationContext();
    }

    // This static function is optional and equivalent to onAttachedToEngine. It
    // supports the old pre-Flutter-1.12 Android projects. You are encouraged to
    // continue supporting plugin registration via this function while apps migrate
    // to use the new Android APIs post-flutter-1.12 via
    // https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith
    // to keep them functionally equivalent. Only one of onAttachedToEngine or
    // registerWith will be called depending on the user's project.
    // onAttachedToEngine or registerWith must both be defined in the same class.

    @Override
    public void onMethodCall(@NonNull MethodCall call,@NonNull Result result) {
        switch (call.method){
            case "INIT_PRINTER":
                MobiPrintPlusHelper.getInstance().initMobiPrinter(context);
                result.success(true);
                break;
            case "PRINT_TEXT":
                String text = call.argument("text");
                MobiPrintPlusHelper.getInstance().printText(text);
                break;
            case "PRINT_BOLD_TEXT":
                String boldText = call.argument("text");
                String textSize = call.argument("text_size");
                float f=Float.parseFloat(textSize);
                int textAlign = call.argument("align");
                boolean isBold = call.argument("is_bold");
                boolean isUnderLine = call.argument("is_underline");
//                MobiPrintPlusHelper.getInstance().printBoldText(boldText,f,textAlign,isBold,isUnderLine);
                break;
            case "PRINT_LOGO":
                byte[] image = call.argument("image");
                MobiPrintPlusHelper.getInstance().printLogo(image);
                break;
            case "PRINT_QR_CODE":
                String qrData = call.argument("qr_code");
                MobiPrintPlusHelper.getInstance().printQr(qrData);
                break;
            case "PRINT_BAR_CODE":
                String barCode = call.argument("bar_code");
                MobiPrintPlusHelper.getInstance().printBarCode(barCode,"");
                break;
            case "PRINT_LINE":
                int lines = call.argument("line");
                MobiPrintPlusHelper.getInstance().printMultiLine(lines);
                break;
            case "EXIT_PRINT":
                MobiPrintPlusHelper.getInstance().feedPaper();
                result.success(true);
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding){}
}
