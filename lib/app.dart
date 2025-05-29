import 'package:flutter/material.dart';
import 'screens/home_screen.dart';

class SueoPrizeApp extends StatelessWidget {
  const SueoPrizeApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Sueo Prize',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const HomeScreen(),
    );
  }
}
