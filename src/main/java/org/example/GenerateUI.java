package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.util.Arrays;

import org.testng.TestNG;

public class GenerateUI {

    static JTextArea logArea;
    private static final String basePath = "src/main/java/org/example/";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("AutoDriver - UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 800);

        // Input components
        JTextArea textArea = new JTextArea(10, 40);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JButton runUAButton = new JButton("Generate UI Automation Scripts");
        JButton runUITestsButton = new JButton("Run UI Tests");
        JButton openReportButton = new JButton("Open Report"); // New button to open the report

        // Output components
        logArea = new JTextArea(15, 40);
        logArea.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(logArea);

        // Layout
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Enter Manual UI Testcases:"));
        inputPanel.add(new JScrollPane(textArea));
        inputPanel.add(runUAButton);
        inputPanel.add(runUITestsButton);
        inputPanel.add(openReportButton); // Add the "Open Report" button

        JPanel outputPanel = new JPanel();
        outputPanel.add(new JLabel("Logs:"));
        outputPanel.add(logScrollPane);

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(inputPanel, BorderLayout.NORTH);
        contentPane.add(outputPanel, BorderLayout.CENTER);

        runUAButton.addActionListener(e -> {
            // Execute the Generate Button ActionListener content here
            try {
                String fileName = basePath + "uitestcases.txt";
                String inputText = textArea.getText();

                File file = new File(fileName);
                if (file.exists()) {
                    file.delete();
                }

                BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
                bw.write(inputText);
                bw.close();

                logArea.append("Text replacement successful!\n");
            } catch (IOException ex) {
                logArea.append("An error occurred: " + ex.getMessage() + "\n");
            }

            // Run the TestNG suite
            runTestNGSuite(basePath + "generateui.xml", "generateui.xml");
        });

        runUITestsButton.addActionListener(e -> {
            // Run the UI tests using the existing TestNG XML execution logic
            runTestNGSuite(basePath + "runui.xml", "runui.xml");
        });

        openReportButton.addActionListener(e -> openTestNGReportInBrowser()); // Add the ActionListener to open the report

        frame.setVisible(true);
    }

    private static void runTestNGSuite(String suiteFilePath, String suiteName) {
        logArea.append("Running TestNG suite: " + suiteName + "...\n");

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
            File reportFile = new File("test-output/dashboard.html");
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
