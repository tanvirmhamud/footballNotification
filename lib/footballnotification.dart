
import 'footballnotification_platform_interface.dart';

class Footballnotification {
  Future getPlatformVersion(var matchid, bool goal, bool card, bool subset, String token) {
    return FootballnotificationPlatform.instance.getPlatformVersion(matchid,goal,card,subset,token);
  }
}
