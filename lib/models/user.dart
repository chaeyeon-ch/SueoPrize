class User {
  String id;
  String name;
  int points;
  List<String> badges;

  User({required this.id, required this.name, this.points = 0, List<String>? badges})
      : badges = badges ?? [];
}
