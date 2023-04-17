import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'footballnotification_platform_interface.dart';

/// An implementation of [FootballnotificationPlatform] that uses method channels.
class MethodChannelFootballnotification extends FootballnotificationPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('footballnotification');

  @override
  Future getPlatformVersion(var matchid, bool goal, bool card, bool subset, String token) async {
    await methodChannel.invokeMethod<String>('getPlatformVersion',
        {"matchid": matchid, "goal": goal, "card": card, "subst": subset, "token": token});
  }
}
