import 'package:flutter/material.dart';

class BingoScreen extends StatelessWidget {
  const BingoScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('수어 빙고판')),
      body: const Center(
        child: Text('빙고판 기능 구현 예정'),
      ),
    );
  }
}
