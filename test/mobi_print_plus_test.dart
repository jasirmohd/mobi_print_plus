import 'package:flutter_test/flutter_test.dart';
import 'package:mobi_print_plus/mobi_print_plus.dart';
import 'package:mobi_print_plus/mobi_print_plus_platform_interface.dart';
import 'package:mobi_print_plus/mobi_print_plus_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockMobiPrintPlusPlatform
    with MockPlatformInterfaceMixin
    implements MobiPrintPlusPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final MobiPrintPlusPlatform initialPlatform = MobiPrintPlusPlatform.instance;

  test('$MethodChannelMobiPrintPlus is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelMobiPrintPlus>());
  });

  test('getPlatformVersion', () async {
    MobiPrintPlus mobiPrintPlusPlugin = MobiPrintPlus();
    MockMobiPrintPlusPlatform fakePlatform = MockMobiPrintPlusPlatform();
    MobiPrintPlusPlatform.instance = fakePlatform;

    expect(await mobiPrintPlusPlugin.getPlatformVersion(), '42');
  });
}
