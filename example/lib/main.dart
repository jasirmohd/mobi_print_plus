import 'package:flutter/material.dart';

import 'package:flutter/services.dart';
import 'package:mobi_print_plus/enums.dart';
import 'package:mobi_print_plus/mobi_print_plus.dart';

import 'dart:ui' as ui;

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  final _mobiPrintPlusPlugin = MobiPrintPlus();

  List<int> multiList = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];

  @override
  void initState() {
    super.initState();
  }

  Future<Uint8List> readFileBytes(String path) async {
    ByteData fileData = await rootBundle.load(path);
    Uint8List fileUnit8List = fileData.buffer
        .asUint8List(fileData.offsetInBytes, fileData.lengthInBytes);
    return fileUnit8List;
  }

  Future<Uint8List> _getImageFromAsset(String iconPath) async {
    return await readFileBytes(iconPath);
  }

  Future<void> print(Uint8List image) async {
    await MobiPrintPlus.initPrinter();
    await MobiPrintPlus.printLogo(image);
    await MobiPrintPlus.printLineWrap(1);
    await MobiPrintPlus.printText('test rint');
    await MobiPrintPlus.printLineWrap(1);
    await MobiPrintPlus.printQrCode('123456789');
    await MobiPrintPlus.printLine();
    await MobiPrintPlus.printBarCode('9876 54633 34565');
    await MobiPrintPlus.printLine();
    await MobiPrintPlus.printBoldText('test print', MobiFontSize.SM, MobiPrintAlign.CENTER, true, false);
    await MobiPrintPlus.printLineWrap(1);
    await MobiPrintPlus.printText('test');
    await MobiPrintPlus.exitPrint();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisSize: MainAxisSize.max,
            children: [
              Text('Mobi print plugin test'),
              InkWell(
                onTap: () async {
                  Uint8List byte = await _getImageFromAsset(
                      'assets/tamaampay_png_print.png');
                  var codec = await ui.instantiateImageCodec(byte,
                      targetHeight: 150, targetWidth: 300);
                  var frameInfo = await codec.getNextFrame();
                  ui.Image targetUiImage = frameInfo.image;

                  ByteData? targetByteData = await targetUiImage.toByteData(
                      format: ui.ImageByteFormat.png);
                  // print('target image ByteData size is ${targetByteData!.lengthInBytes}');
                  Uint8List targetlUinit8List =
                      targetByteData!.buffer.asUint8List();

                  for (int i = 0; i < multiList.length; i++) {
                    await print(targetlUinit8List);
                  }
                },
                child: Container(
                  height: 50,
                  width: 200,
                  decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(10.0),
                      color: Colors.blue),
                  alignment: Alignment.center,
                  child: Text(
                    'Print',
                    style: TextStyle(
                        color: Colors.white,
                        fontWeight: FontWeight.bold,
                        fontSize: 16),
                  ),
                ),
              )
            ],
          ),
        ),
      ),
    );
  }
}
