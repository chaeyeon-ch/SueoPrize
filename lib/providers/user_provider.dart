import 'package:flutter/material.dart';
import '../models/user.dart';

class UserProvider extends ChangeNotifier {
  User _user = User(id: '1', name: 'Guest');

  User get user => _user;

  void addPoints(int value) {
    _user.points += value;
    notifyListeners();
  }

  void addBadge(String badge) {
    _user.badges.add(badge);
    notifyListeners();
  }
}
