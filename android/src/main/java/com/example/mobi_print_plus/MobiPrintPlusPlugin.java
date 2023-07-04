package com.example.mobi_print_plus;

import android.content.Context;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/** MobiPrintPlusPlugin */
public class MobiPrintPlusPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;

  private Context context;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(),"mobi_print_plus");
//        mobiPrintPlusHelper = new MobiPrintPlusHelper(flutterPluginBinding.getApplicationContext());
    channel.setMethodCallHandler(this);
    context = flutterPluginBinding.getApplicationContext();
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    switch (call.method){
      case "INIT_PRINTER":
        MobiPrintPlusHelper.getInstance().initMobiPrinter(context);
        result.success(true);
        break;
      case "PRINT_TEXT":
        String text = call.argument("text");
        MobiPrintPlusHelper.getInstance().printText(text);
        result.success(true);
        break;
      case "PRINT_BOLD_TEXT":
        String boldText = call.argument("text");
        String textSize = call.argument("text_size");
        float f=Float.parseFloat(textSize);
        int textAlign = call.argument("align");
        boolean isBold = call.argument("is_bold");
        boolean isUnderLine = call.argument("is_underline");
        MobiPrintPlusHelper.getInstance().printBoldText(boldText,f,textAlign,isBold,isUnderLine);
        result.success(true);
        break;
      case "PRINT_LOGO":
        byte[] image = call.argument("image");
        MobiPrintPlusHelper.getInstance().printLogo(image);
        result.success(true);
        break;
      case "PRINT_QR_CODE":
        String qrData = call.argument("qr_code");
        MobiPrintPlusHelper.getInstance().printQr(qrData);
        result.success(true);
        break;
      case "PRINT_BAR_CODE":
        String barCode = call.argument("bar_code");
        MobiPrintPlusHelper.getInstance().printBarCode(barCode,"CODE128");
        result.success(true);
        break;
      case "PRINT_LINE":
        int lines = call.argument("line");
        MobiPrintPlusHelper.getInstance().printMultiLine(lines);
        result.success(true);
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
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
