import 'package:flutter_test/flutter_test.dart';
import 'package:footballnotification/footballnotification.dart';
import 'package:footballnotification/footballnotification_platform_interface.dart';
import 'package:footballnotification/footballnotification_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFootballnotificationPlatform
    with MockPlatformInterfaceMixin
    implements FootballnotificationPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final FootballnotificationPlatform initialPlatform = FootballnotificationPlatform.instance;

  test('$MethodChannelFootballnotification is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFootballnotification>());
  });

  test('getPlatformVersion', () async {
    Footballnotification footballnotificationPlugin = Footballnotification();
    MockFootballnotificationPlatform fakePlatform = MockFootballnotificationPlatform();
    FootballnotificationPlatform.instance = fakePlatform;

    expect(await footballnotificationPlugin.getPlatformVersion(), '42');
  });
}
