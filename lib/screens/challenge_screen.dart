import 'package:flutter/material.dart';

class ChallengeScreen extends StatelessWidget {
  const ChallengeScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('수어 따라잡기 챌린지')),
      body: const Center(
        child: Text('챌린지 모드 (AI 채점) 구현 예정'),
      ),
    );
  }
}
