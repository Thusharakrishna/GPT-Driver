package org.example;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static io.restassured.RestAssured.given;

public class ApiChatgpt extends ReadJsonFile {
    @Test
    public void codeGeneration() throws IOException, ParseException {
        String filename = "src/main/java/org/example/apitestcases.txt";
        String guidelines = "src/main/java/org/example/guidelinesAPI.txt";
        JSONParser parser = new JSONParser();
        String jsonString = readJsonFile(filename);
        String guide = readJsonFile(guidelines);

        RestAssured.baseURI = "https://api.openai.com/v1/chat/completions";
        String token = "open API token";

        System.out.println("TestCases has been passed to ChatGPT\n Code generation in progress ...");


        String requestBody = "{\n" +
                "    \"model\": \"gpt-3.5-turbo\",\n" +
                "    \"messages\": [\n" +
                "        {\n" +
                "            \"role\": \"user\",\n" +
                "            \"content\": \"Generate runnable API automation code that fulfills the following requirements " + guide + "for following request " + jsonString + "\"\n" +
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
        String a[] = content.split("```");
        int firstIndex = content.indexOf("```");
        int secondIndex = content.indexOf("```", firstIndex + 1);
//        String codeBlock = content.substring(firstIndex + 3, secondIndex).replaceAll("\\\\n", "\n");
        content = a[1];
        String codeBlock = content.replaceAll("\\\\n", "\n");

        codeBlock = codeBlock.replace("java\n", "");
        if(!(codeBlock.contains("package org.example;")))
            insertString(codeBlock,
                    "package org.example;\n",
                    0);
        System.out.println(codeBlock);
        createJavaClass(codeBlock);

    }

    public static String insertString(
            String originalString,
            String stringToBeInserted,
            int index)
    {

        // Create a new string
        String newString = originalString.substring(0, index + 1)
                + stringToBeInserted
                + originalString.substring(index + 1);

        // return the modified String
        return newString;
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