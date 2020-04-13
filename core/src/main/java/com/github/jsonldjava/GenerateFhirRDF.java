package com.github.jsonldjava;

import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class GenerateFhirRDF {
//    private static String JSON_LD_R4_DIR = "/Users/m091864/git/jsonld-java-test/data";
//    private static String OUTPUT_DIR = "/Users/m091864/git/jsonld-java-test/data/Output";

    public void generate_R4_Conversion_with_JSON_LD_11(String jsonld_r4_dir, String rdf_output_dir)  throws Exception {

        // go through all JSON LD R4 files and convert with JSONLD_JAVA 1.1
        File folder = new File(jsonld_r4_dir);
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".json")) {
                System.out.println("PARSING FILE: " + file.getName());
                parseFile(rdf_output_dir, file);
            }
        }
    }


    private void parseFile (String outputDir, File jsonldFile) {
        try {
            // Open a valid json(-ld) input file
            InputStream inputStream = new FileInputStream(jsonldFile.getAbsoluteFile());

            // Read the file into an Object (The type of this object will be a List, Map, String, Boolean,
            // Number or null depending on the root object in the file).
            Object jsonObject = JsonUtils.fromInputStream(inputStream);

            // Create a context JSON map containing prefixes and definitions
            Map context = new HashMap();

            // Customise context...
            // Create an instance of JsonLdOptions with the standard JSON-LD options
            JsonLdOptions options = new JsonLdOptions();

            // Customise options...
            // Call whichever JSONLD function you want! (e.g. compact)
            Object compact = JsonLdProcessor.compact(jsonObject, context, options);

            Object rdf = JsonLdProcessor.toRDF(jsonObject, options);



            String jsonString = JsonUtils.toPrettyString(compact);
            String rdfString = JsonUtils.toPrettyString(rdf);

            // Print out the result (or don't, it's your call!)
//            System.out.println(jsonString);
//            System.out.println("##### ------------------------- #####");

//            writeFile(outputDir, jsonString, jsonldFile.getName());
//            writeFile(outputDir, rdfString, "rdf_" + jsonldFile.getName());
            writeFile(outputDir, rdfString,  jsonldFile.getName());

            // Print out the result (or don't, it's your call!)
            //System.out.println(JsonUtils.toPrettyString(rdf));

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void writeFile (String outputDir, String fileContents, String fileName) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputDir + "/" + fileName));
        writer.write(fileContents);
        writer.close();

    }

}
