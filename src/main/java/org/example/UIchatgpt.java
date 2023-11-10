package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static io.restassured.RestAssured.given;

public class UIchatgpt extends ReadJsonFile {
    @Test
    public void codeGeneration() throws IOException, ParseException {
        String guidelines = "src/main/java/org/example/guidelinesUI.txt";
        String testCases = "src/main/java/org/example/uitestcases.txt";
        JSONParser parser = new JSONParser();
        String guide = readJsonFile(guidelines);
        String testCase = readJsonFile(testCases);

        RestAssured.baseURI = "https://api.openai.com/v1/chat/completions";
        String token = "open API token";
        System.out.println("TestCases has been passed to ChatGPT\n Code generation in progress ...");
        String requestBody = "{\n" +
                "    \"model\": \"gpt-3.5-turbo\",\n" +
                "    \"messages\": [\n" +
                "        {\n" +
                "            \"role\": \"user\",\n" +
                "            \"content\": \"" + guide + " for following testcases " + testCase + "\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"temperature\": 0.7\n" +
                "}";

        Response response = given()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .body(requestBody)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .response();
        String content = response.jsonPath().get("choices[0].message.content");
        String codeBlock = content;
        if(content.contains("```")) {
            String a[] = content.split("```");
            codeBlock = a[1];
        }
        codeBlock.replaceAll("\\\\n", "\n");
        codeBlock = codeBlock.replace("java\n", "");
        System.out.println(codeBlock);
        createJavaClass(codeBlock);

    }

    public static String insertString(String codeBlock) {
        String originalString = "package org.example;\n";
        originalString = originalString + codeBlock;

        // return the modified String
        return originalString;
    }

    public void createJavaClass(String code) {
        String filename = "src/main/java/org/example/Sample.java";


        try {
            File file = new File(filename);
            FileWriter writer = new FileWriter(file);
            writer.write(code);
            writer.close();
            System.out.println("Java file created successfully.");
        } catch (IOException e) {
            System.out.println("An error occurred while creating the Java file.");
            e.printStackTrace();
        }
    }
}