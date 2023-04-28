
import 'footballnotification_platform_interface.dart';

class Footballnotification {
  Future getPlatformVersion(
      {List<int>? matchid, bool? goal, bool? card, bool? subset, String? token, List<int>? teamid, List<int>? season}) {
    return FootballnotificationPlatform.instance.getPlatformVersion(matchid: matchid,card: card,goal: goal,season: season,subset: subset,teamid: teamid,token: token);
  }
}
