import 'footballnotification_platform_interface.dart';

class Footballnotification {
  Future getPlatformVersion({
    required List<int> matchid,
    required bool goal,
    required bool card,
    required bool subset,
    required String token,
    required List<int> teamid,
    required List<int> season,
    required bool notification,
    required bool matchstart,
    required int time,
  }) {
    return FootballnotificationPlatform.instance.getPlatformVersion(
        matchid: matchid,
        card: card,
        goal: goal,
        season: season,
        subset: subset,
        teamid: teamid,
        token: token,
        matchstart: matchstart,
        time: time,
        notification: notification);
  }
}
