import 'package:flutter/material.dart';

class CameraView extends StatelessWidget {
  final Widget child;
  const CameraView({Key? key, required this.child}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.black,
      child: Center(child: child),
    );
  }
}
