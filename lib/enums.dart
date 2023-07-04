library enums;


///*MobiPrintAlign
///
///Enum to set printer alignment
enum MobiPrintAlign{
  LEFT,
  CENTER,
  RIGHT,
}

///*MobiPrintFontSize
///
///Enum to set font in the printer
enum MobiFontSize{
  SM,MD,LG
}

///*MobiBarcodeType
///
///Enum to set Barcode Type
enum MobiBarcodeType{
  UPCA,
  UPCE,
  JAN13,
  JAN8,
  CODE39,
  ITF,
  CODABAR,
  CODE93,
  CODE128
}

///*MobiBarcodeTextPos
///
///Enum to set how the text will be printed in barcode
enum MobiBarcodeTextPos{
  NO_TEXT,
  TEXT_ABOVE,
  TEXT_UNDER,
  BOTH,
}

