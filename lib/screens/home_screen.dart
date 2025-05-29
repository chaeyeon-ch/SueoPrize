import 'package:flutter/material.dart';
import 'challenge_screen.dart';
import 'quiz_screen.dart';
import 'bingo_screen.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Sueo Prize Home')),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            ElevatedButton(
              onPressed: () => Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => const ChallengeScreen()),
              ),
              child: const Text('챌린지'),
            ),
            ElevatedButton(
              onPressed: () => Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => const QuizScreen()),
              ),
              child: const Text('퀴즈 배틀'),
            ),
            ElevatedButton(
              onPressed: () => Navigator.push(
                context,
                MaterialPageRoute(builder: (_) => const BingoScreen()),
              ),
              child: const Text('빙고'),
            ),
          ],
        ),
      ),
    );
  }
}
