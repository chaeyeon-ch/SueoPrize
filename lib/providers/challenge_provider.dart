import 'package:flutter/material.dart';
import '../models/challenge.dart';

class ChallengeProvider extends ChangeNotifier {
  final List<String> _words = ['안녕', '사랑', '감사'];
  Challenge? _current;

  Challenge? get current => _current;

  void next() {
    _current = Challenge((_words..shuffle()).first);
    notifyListeners();
  }
}
