import 'enums.dart';

///*MobiStyle*
///
///With this class you can build your own layout to print some text.
///The text can come already with [align], [fontSize] and *bold*, and then you don't need to type 3 commands to do the same THING!

class MobiStyle {
  MobiFontSize? fontSize;
  MobiPrintAlign? align;
  bool? bold;
  bool? underline;

  MobiStyle({this.fontSize, this.align, this.bold, this.underline});
}