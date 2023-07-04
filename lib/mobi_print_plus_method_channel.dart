import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'mobi_print_plus_platform_interface.dart';

/// An implementation of [MobiPrintPlusPlatform] that uses method channels.
class MethodChannelMobiPrintPlus extends MobiPrintPlusPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('mobi_print_plus');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
