class InferenceService {
  Future<double> predict(String word) async {
    // TODO: integrate TFLite model here
    // return a fake accuracy for now
    await Future.delayed(const Duration(milliseconds: 500));
    return 0.8; // fake accuracy
  }
}
