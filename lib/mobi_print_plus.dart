import 'package:flutter/services.dart';

import 'enums.dart';

class MobiPrintPlus {

  ///*mobi_print_plus
  ///
  ///A callable method to start the communication with the native code!
  static const MethodChannel _channel = MethodChannel('mobi_print_plus');

  ///*initPrinter
  ///
  ///This method will reset the printer styles, font and everything that can change your text, but the method will *NOT* erase the buffer inside.
   static Future<bool?> initPrinter() async {
    final bool? status = await _channel.invokeMethod("INIT_PRINTER");
    return status;
   }

  ///*printText
  ///
  ///This method will print a simple text in your printer
  static Future<void> printText(String text) async{
    Map<String, dynamic> arguments = <String,dynamic>{"text":'$text\n'};
    await _channel.invokeMethod("PRINT_TEXT",arguments);
    await initPrinter();
  }

  ///*printBoldText
  ///
  ///This method will print a simple bold text in your printer
  static Future<void> printBoldText(String text,MobiFontSize textSize,MobiPrintAlign align, bool isBold, bool isUnderLine) async{
     String size = '0.0';
     switch(MobiFontSize){
       case MobiFontSize.SM:
         size = '0.1';
         break;
       case MobiFontSize.MD:
         size = '0.6';
         break;
       case MobiFontSize.LG:
         size = '0.11';
         break;
       default:
         size = '0.1';
         break;
     }

     int alignment = 1;
     switch(MobiPrintAlign){
       case MobiPrintAlign.LEFT:
         alignment = 1;
         break;
       case MobiPrintAlign.CENTER:
         alignment = 2;
         break;
       case MobiPrintAlign.RIGHT:
         alignment = 3;
         break;
       default:
         size = '0.1';
         break;
     }

    Map<String, dynamic> arguments = <String,dynamic>{"text":'$text\n',"text_size":size,"align":alignment,"is_bold":isBold,"is_underline":isUnderLine};
    await _channel.invokeMethod("PRINT_BOLD_TEXT",arguments);
  }

  ///*printLogo
  ///
  ///This method will print a simple Logo in your printer
  static Future<void> printLogo(Uint8List image) async{
    Map<String, dynamic> arguments = <String,dynamic>{"image":image};
    await _channel.invokeMethod("PRINT_LOGO",arguments);
  }

  ///*printQrCode
  ///
  ///This method will print a simple qr code in your printer
  static Future<void> printQrCode(String qrCode) async{
    Map<String, dynamic> arguments = <String,dynamic>{"qr_code":qrCode};
    await _channel.invokeMethod("PRINT_QR_CODE",arguments);
  }

  ///*printBarCode
  ///
  ///This method will print a simple barCode in your printer
  static Future<void> printBarCode(String barCode) async{
    Map<String, dynamic> arguments = <String,dynamic>{"bar_code":barCode};
    await _channel.invokeMethod("PRINT_BAR_CODE",arguments);
  }

  ///*printLine
  ///
  ///This method will print a simple line space in your printer
  static Future<void> printLineWrap(int lines) async{
    Map<String, dynamic> arguments = <String,dynamic>{"line":lines};
    await _channel.invokeMethod("PRINT_LINE",arguments);
  }

  ///*printText
  ///
  ///This method will print a simple text in your printer
  static Future<void> printLine({String ch = '-',int len = 31}) async{
    await printText(List.filled(len, ch[0]).join());
  }

  ///*exitPrint
  ///
  ///This method will print a simple text in your printer
  static Future<bool?> exitPrint([bool clear = true]) async{
   final bool? status = await _channel.invokeMethod("EXIT_PRINT");
    return status;
  }

  getPlatformVersion() {}

}
