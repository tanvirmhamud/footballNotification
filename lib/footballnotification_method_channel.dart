import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'footballnotification_platform_interface.dart';

/// An implementation of [FootballnotificationPlatform] that uses method channels.
class MethodChannelFootballnotification extends FootballnotificationPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('footballnotification');

  @override
  Future getPlatformVersion(
      {required List<int> matchid,
      required bool goal,
      required bool card,
      required bool subset,
      required String token,
      required List<int> teamid,
      required List<int> season,
      required bool matchstart,
      required int time,
      required bool notification}) async {
    if (notification == true) {
      await methodChannel.invokeMethod<String>('getPlatformVersion', {
        "matchid": matchid,
        "goal": goal,
        "card": card,
        "subst": subset,
        "token": token,
        "teamid": teamid,
        "season": season,
        "matchstart": matchstart,
        "time": time
      });
    } else {
      await methodChannel.invokeMethod<String>('notificatonoff');
    }
  }
}
