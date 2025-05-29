import 'package:flutter/material.dart';

class QuizScreen extends StatelessWidget {
  const QuizScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('수어 퀴즈 배틀')),
      body: const Center(
        child: Text('퀴즈 배틀 기능 구현 예정'),
      ),
    );
  }
}
