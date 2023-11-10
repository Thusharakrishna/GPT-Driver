package org.example;

import org.testng.TestNG;

import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.net.URI;
import java.util.Arrays;

public class RunUI {

    static JTextArea logArea;
    private static final String basePath = "src/main/java/org/example/";
    private static final String testOutputPath = "test-output/dashboard.html";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("AutoDriver - UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 800);

        // Output components
        logArea = new JTextArea(15, 40);
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);

        // Layout
        JPanel outputPanel = new JPanel();
        outputPanel.add(new JLabel("Logs:"));
        outputPanel.add(logScrollPane);

        JButton apiTestsButton = new JButton("Run UI Tests");
        JButton openReportButton = new JButton("Open Report");
        outputPanel.add(apiTestsButton);
        outputPanel.add(openReportButton);

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(outputPanel, BorderLayout.CENTER);

        // API Tests Button ActionListener
        apiTestsButton.addActionListener(e -> runTestNGSuite(basePath + "runui.xml", "runui.xml"));

        // Open Report Button ActionListener
        openReportButton.addActionListener(e -> openTestNGReportInBrowser());

        frame.setVisible(true);
    }

    private static void runTestNGSuite(String suiteFilePath, String suiteName) {
        logArea.setText("Running TestNG suite: " + suiteName + "...\n");

        try {
            TestNG testng = new TestNG();
            testng.setTestSuites(Arrays.asList(new String[]{suiteFilePath}));

            // Capture the standard output and error streams
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

            PrintStream outStream = new PrintStream(outputStream);
            PrintStream errStream = new PrintStream(errorStream);

            System.setOut(outStream);
            System.setErr(errStream);

            testng.run();

            // Display logs from the standard output and error streams
            logArea.append(outputStream.toString());
            logArea.append(errorStream.toString());
            logArea.append("TestNG suite " + suiteName + " executed successfully.\n");
        } catch (Exception ex) {
            logArea.append("An error occurred while running TestNG: " + ex.getMessage() + "\n");
        }
    }

    private static void openTestNGReportInBrowser() {
        try {
            File reportFile = new File(testOutputPath);
            if (reportFile.exists() && Desktop.isDesktopSupported()) {
            	String reportPath = reportFile.getAbsolutePath().replace("\\", "/");
                Desktop.getDesktop().browse(new URI("file:///" + reportPath));
            } else {
                logArea.append("Report file not found or unable to open in a browser.\n");
            }
        } catch (Exception ex) {
            logArea.append("An error occurred while opening the report: " + ex.getMessage() + "\n");
        }
    }
}
