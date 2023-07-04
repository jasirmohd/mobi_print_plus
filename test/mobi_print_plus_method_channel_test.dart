import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mobi_print_plus/mobi_print_plus_method_channel.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  MethodChannelMobiPrintPlus platform = MethodChannelMobiPrintPlus();
  const MethodChannel channel = MethodChannel('mobi_print_plus');

  setUp(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(
      channel,
      (MethodCall methodCall) async {
        return '42';
      },
    );
  });

  tearDown(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(channel, null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
