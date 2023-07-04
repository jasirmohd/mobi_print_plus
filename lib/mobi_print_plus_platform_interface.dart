import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'mobi_print_plus_method_channel.dart';

abstract class MobiPrintPlusPlatform extends PlatformInterface {
  /// Constructs a MobiPrintPlusPlatform.
  MobiPrintPlusPlatform() : super(token: _token);

  static final Object _token = Object();

  static MobiPrintPlusPlatform _instance = MethodChannelMobiPrintPlus();

  /// The default instance of [MobiPrintPlusPlatform] to use.
  ///
  /// Defaults to [MethodChannelMobiPrintPlus].
  static MobiPrintPlusPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [MobiPrintPlusPlatform] when
  /// they register themselves.
  static set instance(MobiPrintPlusPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
