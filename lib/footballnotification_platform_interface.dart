import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'footballnotification_method_channel.dart';

abstract class FootballnotificationPlatform extends PlatformInterface {
  /// Constructs a FootballnotificationPlatform.
  FootballnotificationPlatform() : super(token: _token);

  static final Object _token = Object();

  static FootballnotificationPlatform _instance = MethodChannelFootballnotification();

  /// The default instance of [FootballnotificationPlatform] to use.
  ///
  /// Defaults to [MethodChannelFootballnotification].
  static FootballnotificationPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FootballnotificationPlatform] when
  /// they register themselves.
  static set instance(FootballnotificationPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future getPlatformVersion(var matchid,bool goal, bool card, bool subset) {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
