import java.io.IOException;

/**
 * Utility class to convert a TensorFlow SavedModel to TensorFlow Lite format.
 *
 * <p>This program calls the official TensorFlow Lite converter using a
 * {@code ProcessBuilder}. It assumes that the {@code tflite_convert}
 * command is available in the executing environment.</p>
 *
 * <p>Usage:</p>
 * <pre>
 *   java ModelConverter <saved_model_dir> <output_file.tflite>
 * </pre>
 */
public class ModelConverter {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 2) {
            System.err.println("Usage: java ModelConverter <saved_model_dir> <output_file.tflite>");
            System.exit(1);
        }

        String savedModelDir = args[0];
        String outputFile = args[1];

        ProcessBuilder pb = new ProcessBuilder(
            "tflite_convert",
            "--saved_model_dir=" + savedModelDir,
            "--output_file=" + outputFile,
            "--allow_custom_ops"
        );
        pb.inheritIO();
        Process p = pb.start();
        int exit = p.waitFor();
        if (exit != 0) {
            throw new RuntimeException("tflite_convert failed with exit code " + exit);
        }
        System.out.println("Model converted and saved to " + outputFile);
    }
}
